package com.genexus.android.core.controls;

import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.genexus.android.core.controls.common.EditInputDescriptions;
import com.genexus.android.layout.ControlHelper;
import com.genexus.android.core.base.controls.IGxControlNotifyEvents;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.controls.IGxEditFinishAware;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.controls.IGxOverrideThemeable;
import com.genexus.android.core.base.controls.MappedValue;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeOverrideProperties;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.common.EditInput;
import com.genexus.android.core.controls.common.HistoryHelper;
import com.genexus.android.core.controls.common.SuggestionsAdapter;
import com.genexus.android.core.ui.Anchor;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.KeyboardUtils;
import com.genexus.android.core.utils.ThemeUtils;
import com.genexus.android.core.utils.UITestingUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class GxEditText extends AppCompatAutoCompleteTextView implements IGxEdit, IGxEditThemeable, IGxEditFinishAware, IGxControlNotifyEvents, IGxLocalizable, IGxOverrideThemeable, IGxControlRuntime
{
	protected int mBaseInputType;
	private Coordinator mCoordinator;
	protected LayoutItemDefinition mDefinition;
	private HistoryHelper mHistoryHelper;

	private EditInput mEditInput;

	private boolean mIsSettingText;
	private boolean mIsTextEdited;
	private String mLastText;
	private boolean mUpdateValueAfterTextChangedPending;
	private String mLastTextValueChanging;

	private ThemeClassDefinition mThemeClass;
	private ThemeOverrideProperties mThemeOverrideProperties = new ThemeOverrideProperties();

	private EnterKeyType mEnterKeyType;
	private String mEnterKeyEvent;

	private static final String ENTER_EVENT_DEFAULT = "<Platform Default>";
	private static final String ENTER_EVENT_NEXT_FIELD = "<Go To Next Field>";
	private static final String ENTER_EVENT_NONE = "<None>";

	private static final String ENTER_CAPTION_NEXT = "Next";
	private static final String ENTER_CAPTION_DONE = "Done";
	private static final String ENTER_CAPTION_GO = "Go";
	private static final String ENTER_CAPTION_SEARCH = "Search";
	private static final String ENTER_CAPTION_SEND = "Send";
	protected boolean mValueSetProgramatically;

	private enum EnterKeyType { Default, NextField, Done, Custom }

	private static final Set<String> SUGGEST_ENABLED = Strings.newSet("OnRequest", "Incremental");

	public GxEditText(Context context, Coordinator coordinator, LayoutItemDefinition definition)
	{
		super(context);
		mCoordinator = coordinator;
		mDefinition = definition;

		mEditInput = EditInput.newEditInput(coordinator, definition);

		setInputType(InputType.TYPE_CLASS_TEXT);
		setSelectAllOnFocus(true);
		setHint(definition.getInviteMessage());
		setMaxEms(10);

		mLastText = Strings.EMPTY;
		mValueSetProgramatically = false;
		mLastTextValueChanging = Strings.EMPTY;

		Integer maximumLength = mEditInput.getEditLength();
		if (maximumLength != null)
			setMaximumLength(maximumLength);

		if (isLongVarChar())
		{
			// Longvarchar is assumed to be a text, i.e. with multiline and capitalization.
			setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
			setGravity(Gravity.TOP);
			setHorizontallyScrolling(false);
			setVerticalScrollBarEnabled(true);
		}
		setupInputConfiguration();
		mBaseInputType = getInputType();
		setUpPasswordInput(getInputType());
		setupAutocomplete();
		setupEnterKey();

		if (definition.getEventHandler("ControlValueChanging") != null) {
			addTextChangedListener(mTextWatcher);
		}
	}

	private boolean isLongVarChar()
	{
		DataItem dataItem = mDefinition.getDataItem();
		if (dataItem != null && dataItem.getLength() > 0 && DataTypes.isLongCharacter(mDefinition.getDataTypeName().getDataType()))
			return true;
		return false;
	}

	protected void setUpPasswordInput(int baseInputType)
	{
		if (isPassword())
		{
			if ((baseInputType & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_NUMBER)
			{
				// TYPE_NUMBER_VARIATION_PASSWORD for numerics.
				setInputType(baseInputType | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			}
			else
			{
				// For characters (and others) just use the PASSWORD variation.
				setInputType(baseInputType | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			// set typeface for input password as default. this way honor the theme class.
			// more info: https://stackoverflow.com/a/3444882
			setTypeface(Typeface.DEFAULT);
		}
	}

	private void setupInputConfiguration() {
		if (mDefinition.getControlInfo() != null) {
			if (isPassword()) {
				String inputPicture = mDefinition.getDataItem().getInputPicture();
				if (DataTypes.isCharacter(mDefinition.getDataTypeName().getDataType())) {
					// If the field is character type, but the accepted input is only numeric, show the numeric keyboard.
					if (inputPicture != null && inputPicture.matches("9+"))
						setInputType(InputType.TYPE_CLASS_NUMBER);
				}
			} else {
				int flags = 0;

				if (mEditInput.getSupportsAutocorrection()) {
					// Consider special pictures
					String inputPicture = mDefinition.getDataItem().getInputPicture();
					if (DataTypes.isCharacter(mDefinition.getDataTypeName().getDataType())) {
						// If the field is character type, but the accepted input is only numeric, show the numeric keyboard.
						if (inputPicture != null && inputPicture.matches("9+"))
							setInputType(InputType.TYPE_CLASS_NUMBER);

						// If "@!" means Case = True (this should be a value of AutoCapitalization).
						if (inputPicture != null && inputPicture.equals("@!"))
							flags |= InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
						else {
							int autoCapitalization = Services.Strings.parseIntEnum(mDefinition.getControlInfo().optStringProperty("@AutoCapitalization"), "None", "FirstWord", "EachWord"); // 0, 1, 2.
							if (autoCapitalization == 1)
								flags |= InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
							else if (autoCapitalization == 2)
								flags |= InputType.TYPE_TEXT_FLAG_CAP_WORDS;
						}
					}

					boolean autoCorrection = mDefinition.getControlInfo().getBooleanProperty("@AutoCorrection", true); // Default true for compatibility.
					if (autoCorrection)
						flags |= InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
					else
					{
						flags = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS; // if not set only this flag it not work.
					}
				} else {
					// Autocorrection or autocapitalization makes no sense for input type = descriptions.
					flags = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
				}

				if (flags== InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
				{
					// not work anymore in some keyboard, like gboard. Now is a hint only, not an obligation
					// https://android.googlesource.com/platform/frameworks/base/+/71803994a3b83af7464a67c62fce47c3bf64e168%5E%21/#F0
					if (getInputType()==InputType.TYPE_CLASS_TEXT) // set only no suggestions to work in 3rd keyboard ie. swift key
						setInputType(flags);
					else
						setInputType(getInputType() | flags);
				}
				else
					setInputType(getInputType() | flags);
			}
		}
	}

	private void setupAutocomplete()
	{
		ControlInfo controlInfo = mDefinition.getControlInfo();
		if (controlInfo != null)
		{
			// There are two (exclusive) possibilities for auto-completion:
			if (SUGGEST_ENABLED.contains(controlInfo.optStringProperty("@EditAutocomplete")))
			{
				// 1) Suggestions (based on provider data).
				SuggestionsAdapter adapter = new SuggestionsAdapter(mCoordinator, mDefinition);
				setThreshold(1);
				setAdapter(adapter);
			}
			else if (controlInfo.optBooleanProperty("@Autocomplete"))
			{
				// 2) Enable history (based on previously entered values).
				mHistoryHelper = new HistoryHelper(mDefinition);
				updateHistorySuggestions();
			}
		}
	}

	private void setupEnterKey()
	{
		mEnterKeyType = EnterKeyType.Default;

		String enterEvent = mDefinition.optStringProperty("@EnterEvent");
		if (!Strings.hasValue(enterEvent) || ENTER_EVENT_DEFAULT.equals(enterEvent))
			return; // Do nothing, use default behavior (next/done).

		setOnEditorActionListener(mEditorActionListener);

		if (ENTER_EVENT_NEXT_FIELD.equalsIgnoreCase(enterEvent))
		{
			mEnterKeyType = EnterKeyType.NextField;
			setImeOptions(EditorInfo.IME_ACTION_NEXT);
		}
		else if (ENTER_EVENT_NONE.equalsIgnoreCase(enterEvent))
		{
			mEnterKeyType = EnterKeyType.Done;
			setImeOptions(EditorInfo.IME_ACTION_DONE);
		}
		else
		{
			// Run a custom event (remove user event quotes).
			mEnterKeyType = EnterKeyType.Custom;
			mEnterKeyEvent = enterEvent.replace("'", Strings.EMPTY);

			// fix varchar, remove multiline to respect key type and key event
			if (isLongVarChar())
			{
				// Longvarchar is assumed to be a text, and capitalization. remove multiline
				setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
			}

			// ... and use custom caption.
			String enterCaption = mDefinition.optStringProperty("@EnterKeyCaption");
			Integer imeAction = imeActionFromEnterCaption(enterCaption);
			if (imeAction == null)
				imeAction = EditorInfo.IME_ACTION_DONE; // Unknown caption, but if we don't set an IME action the event will not fire.

			setImeOptions(imeAction);
		}
	}

	private static Integer imeActionFromEnterCaption(String enterCaption)
	{
		if (!Strings.hasValue(enterCaption))
			return null;

		if (ENTER_CAPTION_NEXT.equalsIgnoreCase(enterCaption))
			return EditorInfo.IME_ACTION_NEXT;
		else if (ENTER_CAPTION_DONE.equalsIgnoreCase(enterCaption))
			return EditorInfo.IME_ACTION_DONE;
		else if (ENTER_CAPTION_GO.equalsIgnoreCase(enterCaption))
			return EditorInfo.IME_ACTION_GO;
		else if (ENTER_CAPTION_SEARCH.equalsIgnoreCase(enterCaption))
			return EditorInfo.IME_ACTION_SEARCH;
		else if (ENTER_CAPTION_SEND.equalsIgnoreCase(enterCaption))
			return EditorInfo.IME_ACTION_SEND;
		else
			return null;
	}

	private final OnEditorActionListener mEditorActionListener = new OnEditorActionListener()
	{
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			actionId = actionId & EditorInfo.IME_MASK_ACTION;

			if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || (actionId >= EditorInfo.IME_ACTION_GO && actionId <= EditorInfo.IME_ACTION_PREVIOUS))
				dismissDropDown();

			// The user may be executing the standard action by long-pressing on the enter key.
			boolean isStandardAction = (actionId == EditorInfo.IME_ACTION_NEXT ||
										actionId == EditorInfo.IME_ACTION_PREVIOUS);

			if (mEnterKeyType == EnterKeyType.Custom && !isStandardAction)
			{
				// With a hardware keyboard, ACTION_DOWN is followed by ACTION_UP, so fire
				// the event on ACTION_UP only. IMPORTANT: We need to return true for ACTION_DOWN,
				// otherwise we will not be passed the ACTION_UP.
				if (event != null && event.getAction() == KeyEvent.ACTION_DOWN)
					return true;

				KeyboardUtils.hideKeyboard(GxEditText.this);

				mEditInput.setText(getText().toString(), new EditInput.OnMappedAvailable()
				{
					@Override
					public void run(MappedValue mapped)
					{
						// Run the custom event.
						mCoordinator.runAction(mEnterKeyEvent, new Anchor(GxEditText.this));
					}
				});

				return true;
			}

			return false;
		}
	};

	@Override
	public void onEditorAction(int actionCode)
	{
		// Workaround for Android issue: OnEditorActionListener.onEditorAction() is NOT called for IME_ACTION_DONE.
		if ((actionCode & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE)
			dismissDropDown();

		super.onEditorAction(actionCode);
	}

	private void updateHistorySuggestions()
	{
		HistoryHelper.HistoryAdapter adapter = mHistoryHelper.buildAdapter(getContext(), mDefinition.getThemeClass());
		setThreshold(1);
		setAdapter(adapter);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
	{
		super.onFocusChanged(focused, direction, previouslyFocusedRect);

		if (focused)
		{
			if (mEnterKeyType == EnterKeyType.Default)
			{
				// AutoCompleteTextView doesn't handle the next/done buttons as the TextView does.
				// Try to calculate appropriate IME options in the same way.
				View nextView = focusSearch();
				setImeOptions(nextView != null ? EditorInfo.IME_ACTION_NEXT : EditorInfo.IME_ACTION_DONE);
			}
		}
		else
		{
			// Losing focus. Fire ControlValueChanged if value was changed by the user.
			checkForControlValueChanged();
		}
	}

	private View focusSearch() {
		View nextView = focusSearch(FOCUS_RIGHT);
		if (nextView != null)
			return nextView;

		return focusSearch(FOCUS_DOWN);
	}

	@Override
	public void finishEdit() {
		checkForControlValueChanged();
	}

	public void checkForControlValueChanged()
	{
		String currentText = getText().toString();
		if (!currentText.equals(mLastText))
		{
			mLastText = currentText;
			mEditInput.setText(currentText, new EditInput.OnMappedAvailable()
			{
				@Override
				public void run(MappedValue mapped)
				{
					if (mCoordinator != null)
						mCoordinator.onValueChanged(GxEditText.this, true);
				}
			});
		}
	}

	@Override
	public void setText(CharSequence text, BufferType type)
	{
		mIsSettingText = true;
		super.setText(text, type);
		mIsSettingText = false;
	}

	public void setTextAsEdited(CharSequence text)
	{
		super.setText(text);
		mIsTextEdited = true;
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
	{
		this.requestLayout(); // call just in case the height changed

		if (!mIsSettingText)
			mIsTextEdited = true;

		super.onTextChanged(text, start, lengthBefore, lengthAfter);

		if (!mIsSettingText)
		{
			// If 1s passes without further typing, assume the user has finished input.
			final int DELAY_MEANS_STOPPED_TYPING = 1000;
			removeCallbacks(mUpdateValueAfterTextChanged);
			postDelayed(mUpdateValueAfterTextChanged, DELAY_MEANS_STOPPED_TYPING);
			mUpdateValueAfterTextChangedPending = true;
		}
	}

    @Override
	protected void replaceText(CharSequence text)
    {
    	// This runs when the user selects one of the suggestions.
    	super.replaceText(text);
    	mUpdateValueAfterTextChanged.run();
    }

	private final Runnable mUpdateValueAfterTextChanged = new Runnable()
	{
		@Override
		public void run()
		{
			mUpdateValueAfterTextChangedPending = false;
			mEditInput.setText(getText().toString(), new EditInput.OnMappedAvailable()
			{
				@Override
				public void run(MappedValue mapped)
				{
					// When the EditInput reports a new value, call onValueChanged() so dependencies can update
					// e.g. a Dynamic Combo that depends on an Edit with Input Type = Descriptions.
					// However, DO NOT fire ControlValueChanged, as editing isn't finished until focus is lost.
					if (mCoordinator != null && mEditInput instanceof EditInputDescriptions && Strings.hasValue(mapped.value))
						mCoordinator.onValueChanged(GxEditText.this, false);
				}
			});
		}
	};

	@Override
	protected void onDetachedFromWindow()
	{
		removeCallbacks(mUpdateValueAfterTextChanged);
		super.onDetachedFromWindow();
	}

	@Override
	public void notifyEvent(EventType type)
	{
		if (type == EventType.ACTION_CALLED)
		{
			if (mHistoryHelper != null && mIsTextEdited)
			{
				mHistoryHelper.store(mEditInput.getText());
				updateHistorySuggestions(); // Refresh the adapter to include the new value.
			}
		}
	}

	protected void setMaximumLength(int maxLength)
	{
		if (maxLength > 0)
		{
			InputFilter[] filterArray = new InputFilter[1];
			filterArray[0] = new InputFilter.LengthFilter(maxLength);
			setFilters(filterArray);
		}
	}

	public boolean showPasswordHint(){
		//noinspection SimplifiableIfStatement
		if (mDefinition.getControlInfo() != null)
			return mDefinition.getControlInfo().getBooleanProperty("@IdEnableShowPasswordHint", false);
		else
			return false;
	}

	public boolean isPassword()
	{
		//noinspection SimplifiableIfStatement
		if (mDefinition.getControlInfo() != null)
			return mDefinition.getControlInfo().getBooleanProperty("@IsPassword", false);
		else
			return false;
	}

	@Override
	public String getGxValue()
	{
		if (mUpdateValueAfterTextChangedPending)
		{
			// Force it to run immediately! Otherwise we would be returning stale data.
			removeCallbacks(mUpdateValueAfterTextChanged);
			mUpdateValueAfterTextChanged.run();
		}

		return mEditInput.getValue();
	}

	@Override
	public void setGxValue(final String value)
	{
		mValueSetProgramatically = true;
		mEditInput.setValue(value, new EditInput.OnMappedAvailable()
		{
			@Override
			public void run(MappedValue input)
			{
				// Prevent setting the same text because selection / cursor position is lost.
				// Also prevent the autocomplete popup from showing.
				if (!Strings.areEqual(input.value, getText().toString()))
					setTextPreservingSelection(input.value);

				mLastText = input.value;
				mLastTextValueChanging = mLastText;
				mIsTextEdited = false;
				mValueSetProgramatically = false;
			}
		});
	}

	/***
	 * Sets a text value and preserves the selection.
	 * If the selection is at the end it remains at the end
	 * else selection stays at the same position
	 * unless the new value is shorter and the old position
	 * is beyond the new length in which case selection is
	 * set at the end.
	 * @param value to set
	 */
	protected void setTextPreservingSelection(String value) {
		int selectionStart = getSelectionStart();
		int selectionEnd = getSelectionEnd();

		int lengthBeforeSet = length();
		setText(value, false);
		int lengthAfterSet = length(); // Use this length instead of value.length() because the length may be bounded in the GX type

		if (selectionStart == lengthBeforeSet || selectionStart > lengthAfterSet)
			selectionStart = lengthAfterSet;
		if (selectionEnd == lengthBeforeSet || selectionEnd > lengthAfterSet)
			selectionEnd = lengthAfterSet;

		// Preserve selection
		if (selectionStart >= 0 && selectionEnd >= 0)
			setSelection(selectionStart, selectionEnd);
	}

	@Override
	public String getGxTag()
	{
		return (String)this.getTag();
	}

	@Override
	public void setGxTag(String data)
	{
		this.setTag(data);
	}

	@Override
	public void setValueFromIntent(Intent data) { }

	@Override
	public IGxEdit getViewControl()
	{
		return new GxTextView(getContext(), mCoordinator, mDefinition, mEditInput.getValuesFormatter());
	}

	@Override
	public IGxEdit getEditControl()
	{
		return this;
	}

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		setFocusable(enabled);
		setFocusableInTouchMode(enabled);
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass;
		ThemeUtils.setFontProperties(this, themeClass);
		if (!mThemeClass.getShowEditTextLine())
		{
			this.setBackgroundDrawable(null);
		}
	}

	@Override
	public void onTranslationChanged() {
		setHint(mDefinition.getInviteMessage());
	}

	// needed for override EditText foreColor
	@Override
	public void setOverride(String propertyName, String propertyValue)
	{
		mThemeOverrideProperties.setOverride(propertyName, propertyValue);
		applyEditClass(mThemeClass);
	}

	@Override
	public ThemeOverrideProperties getThemeOverrideProperties()
	{
		return mThemeOverrideProperties;
	}

	private final TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// Nothing to do.
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (!mValueSetProgramatically || UITestingUtils.Companion.isRunningUnderTest()) {
				String newText = s.toString();
				if (!newText.equals(mLastTextValueChanging)) {
					mCoordinator.onValueChanging(GxEditText.this, newText);
					mLastTextValueChanging = newText;
				}
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Nothing to do.
		}
	};

	@Override
	public void setPropertyValue(String name, Value value) {
		if (ControlHelper.PROPERTY_ISPASSWORD.equalsIgnoreCase(name)) {
			setIsPassword(value.coerceToBoolean());
		}
	}

	@Override
	public Value getPropertyValue(String name) {
		if (ControlHelper.PROPERTY_ISPASSWORD.equalsIgnoreCase(name)) {
			return Value.newBoolean(getIsPassword());
		} else {
			return null;
		}
	}

	public boolean getIsPassword() {
		return (getInputType() & InputType.TYPE_TEXT_VARIATION_PASSWORD)
				== InputType.TYPE_TEXT_VARIATION_PASSWORD;
	}

	public void setIsPassword(boolean isPassword) {
		if (isPassword) {
			setInputType(mBaseInputType | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		} else {
			// Restore to the base input type
			setInputType(mBaseInputType);
		}
	}
}

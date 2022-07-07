package com.genexus.android.core.controls;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.genexus.android.R;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.controls.IGxOverrideThemeable;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeOverrideProperties;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.FormatHelper;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.ThemeUtils;

public class GxDateTimeEdit extends LinearLayout implements IGxEdit, IGxEditThemeable, IGxLocalizable, IGxOverrideThemeable
{
	private final Coordinator mCoordinator;
	private final LayoutItemDefinition mDefinition;

	private TextView mDateButton;
	private TextView mTimeButton;

	private String mDataType; // Date, Time, DateTime
	private String mPicture;
	private int mLength;
	private int mDecimals;

	private boolean mShowDate;
	private boolean mShowTime;

	private Date mValue;

	private ThemeClassDefinition mThemeClass;
	private ThemeOverrideProperties mThemeOverrideProperties = new ThemeOverrideProperties();

	public GxDateTimeEdit(Context context, Coordinator coordinator, LayoutItemDefinition definition)
	{
		super(context);
		mDefinition = definition;
		mCoordinator = coordinator;
		mDataType = DataTypes.DATE;

		createButtons();

		String dataType = mDefinition.getDataTypeName().getDataType();
		String picture = mDefinition.getDataItem().getInputPicture();
		mLength = mDefinition.getDataItem().getLength();
		mDecimals = mDefinition.getDataItem().getDecimals();

		setDataType(dataType, picture);

		String inviteMessage = getDefinition().getInviteMessage();
		if (Strings.hasValue(inviteMessage))
		{
			if (mShowDate)
				mDateButton.setHint(inviteMessage); // Show hint in date for date and date+time.
			else if (mShowTime)
				mTimeButton.setHint(inviteMessage);
		}
		// use time invite message for datetime controls.
		if (mShowTime)
		{
			String timeInviteMessage = getDefinition().getTimeInviteMessage();
			if (Strings.hasValue(timeInviteMessage))
			{
				mTimeButton.setHint(timeInviteMessage);
			}
		}

		updateText();
	}

	public TextView getDateButton() {
		return mDateButton;
	}

	public TextView getTimeButton() {
		return mTimeButton;
	}

	private void createButtons()
	{
		mDateButton = createButton();
		mTimeButton = createButton();

		setOrientation(HORIZONTAL);
		addView(mDateButton);
		addView(mTimeButton);

		mDateButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showDateDialog(null);
			}
		});

		mTimeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showTimeDialog(null);
			}
		});
	}

	private TextView createButton()
	{
		// Use spinerStyle with underline.
		TextView button = new AppCompatButton(getContext(), null, R.attr.spinnerUnderlineCustomStyle);

		button.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));
		button.setGravity(mDefinition.getCellGravity());
		button.setFocusableInTouchMode(false); // So that "first click" works.
		return button;
	}

	public void setDataType(String dataType, String picture)
	{
		mDataType = dataType;
		mPicture = picture;

		//noinspection IfCanBeSwitch
		if (mDataType.equals(DataTypes.DATE))
		{
			mShowDate = true;
			mShowTime = false;
		}
		else if (mDataType.equals(DataTypes.DTIME) || mDataType.equals(DataTypes.DATETIME))
		{
			mShowDate = true;
			mShowTime = true;
			if ( (Strings.hasValue(mPicture) && mPicture.length() <= 5)  || mLength==0 )// Special case, datetime with time picture.
				mShowDate = false;
		}
		else if (mDataType.equals(DataTypes.TIME))
		{
			mShowDate = false;
			mShowTime = true;
		}
		else
		{
			// Default is date
			Services.Log.warning("Unexpected datatype: " + dataType);
			mShowDate = true;
			mShowTime = false;
		}

		mDateButton.setVisibility(mShowDate ? VISIBLE : GONE);
		mTimeButton.setVisibility(mShowTime ? VISIBLE : GONE);
	}

	protected LayoutItemDefinition getDefinition()
	{
		return mDefinition;
	}

	private void showDateDialog(DatePickerDialog.OnDateSetListener customListener)
	{
		Calendar c = getCalendar();

		DatePickerDialog.OnDateSetListener listener = (customListener != null ? customListener : mOnDateSetListener);
		DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}

	private final DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			// OK pressed in DatePicker.
			Calendar c = getCalendar();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, monthOfYear);
			c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			updateValueFromCalendar(c);
		}
	};

	private void showTimeDialog(TimePickerDialog.OnTimeSetListener customListener)
	{
		Calendar c = getCalendar();
		final String timeS = Settings.System.getString(getContext().getContentResolver(), Settings.System.TIME_12_24);
		final boolean is24HourView = timeS == null || !timeS.equals("12");

		TimePickerDialog.OnTimeSetListener listener = (customListener != null ? customListener : mOnTimeSetListener);
		TimePickerDialog dialog = new TimePickerDialog(getContext(), listener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), is24HourView);
		dialog.show();
	}

	private final TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener()
	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute)
		{
			// OK pressed in TimePicker.
			Calendar c = getCalendar();
			c.set(Calendar.HOUR_OF_DAY, hourOfDay);
			c.set(Calendar.MINUTE, minute);
			c.set(Calendar.SECOND, 0);

			updateValueFromCalendar(c);
		}
	};

	private Calendar getCalendar()
	{
		Calendar c = Calendar.getInstance();
		if (mValue != null)
			c.setTime(mValue);

		return c;
	}

	private void updateValueFromCalendar(Calendar c)
	{
		removeSecondsAndMillis(c);
		Date newValue = c.getTime();
		if (!newValue.equals(mValue))
		{
			mValue = newValue;
			updateText();

			// Fire ControlValueChanged.
			if (mCoordinator != null)
				mCoordinator.onValueChanged(GxDateTimeEdit.this, true);
		}
	}

	private void removeSecondsAndMillis(Calendar calendar) {
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	@Override
	public String getGxValue()
	{
		//noinspection IfCanBeSwitch
		if (DataTypes.isTime(mDataType, mLength))
			return Services.Strings.getDateTimeStringForServer(mValue, true, mDecimals==12);
		else if (mDataType.equals(DataTypes.DATE))
			return Services.Strings.getDateStringForServer(mValue);
		else
			return Services.Strings.getDateTimeStringForServer(mValue, false, mDecimals==12);
	}

	private String getText()
	{
		return FormatHelper.formatDate(mValue, mDataType, mPicture);
	}

	@Override
	public void setGxValue(String value)
	{
		//noinspection IfCanBeSwitch
		if (DataTypes.isTime(mDataType, mLength))
			mValue = Services.Strings.getDateTime(value, true, mDecimals>=8, mDecimals==12);
		else if (mDataType.equals(DataTypes.DATE))
			mValue = Services.Strings.getDate(value);
		else
			mValue = Services.Strings.getDateTime(value, false, mDecimals>=8, mDecimals==12);

		updateText();

		if (DataTypes.isTime(mDataType, mLength)) {
			if (mValue!=null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(mValue);
				if (calendar.get(Calendar.HOUR_OF_DAY) == 0 &&
					calendar.get(Calendar.MINUTE) == 0 &&
					calendar.get(Calendar.SECOND) == 0 &&
					calendar.get(Calendar.MILLISECOND) == 0) {
					// When the time is 00:00 set mValue as null so the picker
					// shows the current time instead of 00:00
					mValue = null;
				}
			}
		}
	}

	private void updateText() {
		if (mShowDate && mShowTime) {
			String dateText = Services.Strings.getDateString(mValue, mPicture);
			String timeText = Services.Strings.getTimeString(mValue, mPicture);

			mDateButton.setText(dateText);
			mTimeButton.setText(timeText);
		} else {
			String text = getText();

			if (mShowDate)
				mDateButton.setText(text);
			else if (mShowTime)
				mTimeButton.setText(text);
		}
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		mDateButton.setEnabled(enabled);
		mTimeButton.setEnabled(enabled);
	}

	@Override
	public String getGxTag()
	{
		return (String)getTag();
	}

	@Override
	public void setGxTag(String tag)
	{
		setTag(tag);
	}

	@Override
	public void setValueFromIntent(Intent data) { }

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}

	@Override
	public IGxEdit getEditControl()
	{
		return this;
	}

	@Override
	public IGxEdit getViewControl()
	{
		return new GxTextView(getContext(), mDefinition);
	}

	public void showDateTimeDialog(final OnDateTimeChangedListener listener)
	{
		if (listener == null)
			throw new IllegalArgumentException("Listener cannot be null.");

		if (mShowDate)
		{
			showDateDialog(new DatePickerDialog.OnDateSetListener()
			{
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
				{
					mOnDateSetListener.onDateSet(view, year, monthOfYear, dayOfMonth);

					// Either continue to time dialog or finish here.
					if (mShowTime)
					{
						showTimeDialog(new TimePickerDialog.OnTimeSetListener()
						{
							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int minute)
							{
								mOnTimeSetListener.onTimeSet(view, hourOfDay, minute);
								listener.onDateTimeChanged(getGxValue(), getText());
							}
						});
					}
					else
						listener.onDateTimeChanged(getGxValue(), getText());

				}
			});
		}
		else if (mShowTime)
		{
			showTimeDialog(new TimePickerDialog.OnTimeSetListener()
			{
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute)
				{
					mOnTimeSetListener.onTimeSet(view, hourOfDay, minute);
					listener.onDateTimeChanged(getGxValue(), getText());
				}
			});
		}
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass;
		ThemeUtils.setFontProperties(mDateButton, themeClass);
		ThemeUtils.setFontProperties(mTimeButton, themeClass);
		if (!mThemeClass.getShowEditTextLine())
		{
			((AppCompatButton)mDateButton).setBackgroundDrawable(null);
			((AppCompatButton)mTimeButton).setBackgroundDrawable(null);
		}
	}

	@Override
	public void onTranslationChanged()
	{
		if (mShowDate && !mShowTime && Strings.hasValue(getDefinition().getInviteMessage()))
		{
			mDateButton.setHint(getDefinition().getInviteMessage());
		}
		if (mShowTime && !mShowDate && Strings.hasValue(getDefinition().getInviteMessage()))
		{
			mTimeButton.setHint(getDefinition().getInviteMessage());
		}
	}

	public interface OnDateTimeChangedListener
	{
		void onDateTimeChanged(String value, String text);
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
}

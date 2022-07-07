package com.genexus.android.core.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.ui.Coordinator;

@SuppressLint("ViewConstructor")
public class GxCheckBox extends AppCompatCheckBox implements IGxEdit,
															 OnCheckedChangeListener {
	public static final String PROPERTY_CHECK_VALUE = "@ControlCheckValue";
	public static final String PROPERTY_UNCHECK_VALUE = "@ControlUnCheckValue";
	public static final String PROPERTY_TITLE = "@ControlTitle";
	private Coordinator mCoordinator;
	private String mCheckedValue;
	private String mUncheckedValue;
	private boolean mFireControlValueChanged;

	public GxCheckBox(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
		super(context);
		init(coordinator, definition);
	}

	protected void init(Coordinator coordinator, LayoutItemDefinition definition) {
		mCoordinator = coordinator;
		mFireControlValueChanged = true;
		setOnCheckedChangeListener(this);
		if (definition != null && definition.getControlInfo() != null) {
			setProperties(definition.getControlInfo());
		}
	}

	private void setProperties(ControlInfo controlProperties) {
		setTitle(controlProperties.optStringProperty(PROPERTY_TITLE));
		setCheckedValue(controlProperties.optStringProperty(PROPERTY_CHECK_VALUE));
		setUncheckedValue(controlProperties.optStringProperty(PROPERTY_UNCHECK_VALUE));
	}

	public void setTitle(CharSequence title) {
		super.setText(title);
	}

	public CharSequence getTitle() {
		return super.getText();
	}

	public String getCheckedValue() {
		return mCheckedValue;
	}

	public void setCheckedValue(String checkedValue) {
		mCheckedValue = checkedValue;
	}

	public String getUncheckedValue() {
		return mUncheckedValue;
	}

	public void setUncheckedValue(String uncheckedValue) {
		mUncheckedValue = uncheckedValue;
	}

	@Override
	public String getGxValue() {
		return isChecked() ? getCheckedValue() : getUncheckedValue();
	}

	@Override
	public void setGxValue(String value) {
		if (TextUtils.isEmpty(value)) {
			return;
		}

		boolean currentState = isChecked();
		boolean newState = value.equalsIgnoreCase(getCheckedValue());

		if (newState != currentState) {
			mFireControlValueChanged = false;
			setChecked(newState);
			mFireControlValueChanged = true;
		}
	}

	@Override
	public String getGxTag() {
		return (String) this.getTag();
	}

	@Override
	public void setGxTag(String data) {
		this.setTag(data);
	}

	@Override
	public void setValueFromIntent(Intent data) {
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		// if already in this state, do not change focusable properties again
		boolean needChangeFocusable = true;
		if (super.isEnabled()==enabled)
			needChangeFocusable = false;

		super.setEnabled(enabled);
		// apply only is enabled changed to avoid rare behavior in focus / touch of check box.
		if (needChangeFocusable)
		{
			super.setFocusable(enabled);
			// the assign with side effect is this one: setFocusableInTouchMode
			super.setFocusableInTouchMode(enabled);
			super.setClickable(enabled);
		}
	}

	@Override
	public IGxEdit getViewControl() {
		setEnabled(false);
		return this;
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (mCoordinator != null)
			mCoordinator.onValueChanged(this, mFireControlValueChanged);
	}

	@Override
	public boolean isEditable() {
		return isEnabled(); // Editable when enabled.
	}
}

package com.genexus.android.core.controls;

import android.content.Context;
import android.content.Intent;

import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.google.android.material.textfield.TextInputLayout;

import com.genexus.android.layout.ControlHelper;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;

import androidx.annotation.NonNull;

public class GxTextInputLayout extends TextInputLayout implements IGxEdit, IGxControlRuntime, IGxEditThemeable
{
	private final GxEditText mEditText;

	public GxTextInputLayout(Context context, GxEditText editText) {
		super(context);
		mEditText = editText;
	}

	@Override
	public String getGxValue() {
		return mEditText.getGxValue();
	}

	@Override
	public void setGxValue(String value) {
		mEditText.setGxValue(value);
	}

	@Override
	public String getGxTag() {
		return (String)getTag();
	}

	@Override
	public void setGxTag(String tag) {
		setTag(tag);
	}

	@Override
	public void setValueFromIntent(Intent data) { }

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}

	@Override
	public boolean isEditable() {
		return isEnabled();
	}

	@Override
	public IGxEdit getViewControl() {
		return this;
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	@Override
	public void setPropertyValue(String name, Value value) {
		if (ControlHelper.PROPERTY_ISPASSWORD.equalsIgnoreCase(name)) {
			mEditText.setPropertyValue(name, value);
		}
	}

	@Override
	public Value getPropertyValue(String name) {
		if (ControlHelper.PROPERTY_ISPASSWORD.equalsIgnoreCase(name)) {
			return mEditText.getPropertyValue(name);
		} else {
			return null;
		}
	}

	public void setCaption(String caption) {
		setHint(caption);
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		mEditText.applyEditClass(themeClass);
	}

	@Override
	public void setTag(int key, final Object tag)
	{
		// set tag to child edit text.
		mEditText.setTag( key, tag);
		super.setTag(key, tag);
	}

	@Override
	public void setGravity(int gravity)
	{
		// set gravity to child edit text.
		mEditText.setGravity(gravity);
		super.setGravity(gravity);
	}
}

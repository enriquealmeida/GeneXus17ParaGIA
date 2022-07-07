package com.genexus.android.core.controls;

import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.view.Gravity;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.Properties;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.expressions.ExpressionFormatHelper;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.FormatHelper;
import com.genexus.android.core.ui.Coordinator;

public class GxEditTextNumeric extends GxEditText {
	private final String mInputPicture;
	private final int mNumberIntegerLengthLimit;
	private String mDisplayValue = Strings.ZERO;
	private String mEditableValue = Strings.ZERO;
	private boolean mHasFocus;

	public GxEditTextNumeric(Context context, Coordinator coordinator, LayoutItemDefinition def) {
		super(context, coordinator, def);
		setInputType(InputType.TYPE_CLASS_NUMBER);

		DataItem dataItem = def.getDataItem();
		int numberLength = dataItem.getLength();
		int decimalsLength = dataItem.getDecimals();
		boolean isNumberSigned = dataItem.getSigned();

		mInputPicture = dataItem.getInputPicture();
		mNumberIntegerLengthLimit = (numberLength > 0 && decimalsLength > 0)
				? numberLength - decimalsLength - 1 : numberLength;

		setupInputFilterLength(numberLength, decimalsLength, isNumberSigned, mInputPicture);
		mBaseInputType = getInputType();
		setUpPasswordInput(getInputType());
		setMaxEms(10);

		// default align , not work if floating and align != default
		if (def.getLabelPosition().equals(Properties.LabelPositionType.FLOATING)) {
			// in float not set default align, do it in databound control
			// only set for label end, because is not working
			if (def.getCellGravity() == Alignment.NONE
					&& def.getThemeClass() != null
					&& def.getThemeClass().getHorizontalLabelAlignment() == Alignment.END) {
				setGravity(Gravity.END);
			}
		} else {
			setGravity(Gravity.END);
		}
	}

	private void setupInputFilterLength(int numberLength, int decimalsLength,
										boolean isNumberSigned, String picture) {
		int inputFilterLength = numberLength;

		if (numberLength > 0 && decimalsLength > 0) {
			setInputType(getInputType() | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			inputFilterLength++;
		}

		if (numberLength > 0 && isNumberSigned) {
			setInputType(getInputType() | InputType.TYPE_NUMBER_FLAG_SIGNED);
			inputFilterLength++;
		}

		if (Strings.hasValue(picture) && picture.length() > inputFilterLength) {
			inputFilterLength = picture.length();
		}

		setMaximumLength(inputFilterLength);
	}

	@Override
	public void setGxValue(String value) {
		super.setGxValue(value); // Needed to track the last value for ControlValueChanged.
		mValueSetProgramatically = true;
		internalSetValue(value);
		mValueSetProgramatically = false;
	}

	@Override
	public String getGxValue() {
		// As a special case, if the display value is blank (i.e. a picture like "ZZZ.ZZ") then the editable value
		// is also shown as blank. However, it is returned as zero when asked by getValue().
		return Services.Strings.hasValue(mEditableValue) ? mEditableValue : Strings.ZERO;
	}

	private void internalSetValue(String editableValue) {
		if (!Services.Strings.hasValue(editableValue))
			editableValue = Strings.ZERO;

		if (Services.Strings.hasValue(mInputPicture)) {
			BigDecimal value;
			try {
				value = new BigDecimal(editableValue);
			} catch (NumberFormatException ex) {
				// try parse with format and decimal separator.
				value = ExpressionFormatHelper.strToNumber(editableValue);
			}

			mEditableValue = value.toPlainString();

			// Apply only if possible
			if (numlength(value) <= mNumberIntegerLengthLimit) {
				mDisplayValue = FormatHelper.formatNumber(value, mInputPicture);
			} else {
				mDisplayValue = editableValue;
			}
		} else {
			mEditableValue = editableValue;
			mDisplayValue = editableValue;
		}

		// As a special case, if the display value is blank (i.e. a picture like "ZZZ.ZZ") then the
		// editable value is also shown as blank. However, it is returned as zero when asked by getValue().
		if (mDisplayValue.length() == 0)
			mEditableValue = Strings.EMPTY;

		// IMPORTANT: Use a custom flag (instead of isFocused()) because this code depends on
		// onFocusChanged() having ALREADY executed, which may not be the case. So if it hasn't executed
		// treat as if it didn't actually have focus yet.
		String text = (mHasFocus ? mEditableValue : mDisplayValue);
		setTextPreservingSelection(text);
	}

	// length of the integer part
	private static int numlength(BigDecimal number) {
		long longValue = number.abs().longValue();
		if (longValue == 0) return 1;
		int l;
		for (l = 0; longValue > 0; ++l)
			longValue /= 10;
		return l;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		mHasFocus = focused;

		if (focused) {
			// Change display_string to editable_string (calculated from previously set value)
			setText(mEditableValue);
			selectAll();
		} else {
			// Read value from editable_string and show display_string
			String editedValue = getText().toString();
			internalSetValue(editedValue);
		}

		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	public void finishEdit() {
		String editedValue = getText().toString();
		internalSetValue(editedValue);
		super.finishEdit();
	}

	@Override
	public boolean getIsPassword() {
		return (getInputType() & InputType.TYPE_NUMBER_VARIATION_PASSWORD)
				== InputType.TYPE_NUMBER_VARIATION_PASSWORD;
	}

	@Override
	public void setIsPassword(boolean isPassword) {
		if (isPassword) {
			setInputType(mBaseInputType | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
		} else {
			// Restore to the base input type
			setInputType(mBaseInputType);
		}
	}
}

package com.genexus.android.controls.wheels.measures;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.GxLinearLayout;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.ThemeUtils;
import com.genexus.android.controls.wheels.R;
import com.genexus.android.controls.wheels.GxWheelPicker;

@SuppressLint("ViewConstructor")
public class GxMeasuresControl extends GxLinearLayout implements IGxEdit
{
	public static final String NAME = "SDPhysicalMeasure";

	private LayoutItemDefinition mDefinition = null;
	private Coordinator mCoordinator = null;
	private String mTag = null;

	private GxWheelPicker mWheelControlNumeric = null;
	private GxWheelPicker mWheelControlDecimal = null;
	private IGxMeasuresHelper mMeasuresHelper = null;

	// Wheel properties
	private String mCurrentValue = null;

	// Wheel dialog
	private AlertDialog mWheelControlDialog = null;
	private Button mAction = null;
	private TextView mText = null;


	public GxMeasuresControl(Context context, Coordinator coordinator, LayoutItemDefinition layoutItemDefinition) {
		super(context);
		mCoordinator = coordinator;
		mDefinition = layoutItemDefinition;

		if (layoutItemDefinition.getControlInfo() != null) {
			setControlInfo(layoutItemDefinition.getControlInfo());
		}

		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mWheelControlNumeric = new GxWheelPicker(getContext());
		mWheelControlDecimal = new GxWheelPicker(getContext());
		mWheelControlNumeric.setCyclic(true);
		mWheelControlDecimal.setCyclic(true);

		mText = new TextView(getContext());
		mText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mText.setGravity(Gravity.CENTER);
		addView(mText);
		mAction = new AppCompatButton(getContext());
		mAction.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mWheelControlDialog == null) {
					mMeasuresHelper.actionClickListener();
					mWheelControlDialog = createDialog();
				}
				mWheelControlDialog.show();
			}
		});
		addView(mAction);
	}

    private void setupWheelsContainer(LinearLayout linearLayout) {
    	linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    	linearLayout.setGravity(Gravity.CENTER);
    	linearLayout.addView(mWheelControlNumeric, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		linearLayout.addView(mWheelControlDecimal, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
    }

	private AlertDialog createDialog() {
		LinearLayout dialogContent = new LinearLayout(getContext());
		setupWheelsContainer(dialogContent);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setView(dialogContent)
			// Action buttons
			.setPositiveButton(R.string.GXM_button_ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					onWheelValueChanged();
					mAction.setText(mCurrentValue);
					mText.setText(mCurrentValue);
					mMeasuresHelper.okClickListener();
				}
			})
			.setNeutralButton(mMeasuresHelper.getTextButtonChange(), null)
			.setNegativeButton(R.string.GXM_cancel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

		final AlertDialog alertDialog = builder.create();

		alertDialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
				neutralButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mMeasuresHelper.changeValue(mWheelControlNumeric.getCurrentItem(), mWheelControlDecimal.getCurrentItem());
						mMeasuresHelper.setValueInWheelControl(GxMeasuresControl.this);
						Button neutralButton = (Button) v;
						neutralButton.setText(mMeasuresHelper.getTextButtonChange());
					}
				});
			}
		});

		mMeasuresHelper.setValueInWheelControl(this);

		return alertDialog;
	}

    private void onWheelValueChanged() {
    	String previousValue = mCurrentValue;
    	mCurrentValue = mMeasuresHelper.getCurrentStringValue(mWheelControlNumeric.getCurrentItem(), mWheelControlDecimal.getCurrentItem());
    	if (mCoordinator != null && !mCurrentValue.equals(previousValue)) {
    		mCoordinator.onValueChanged(GxMeasuresControl.this, true);
    	}
    }

	public void setWheelControlViewAdapter(int minNumeric, int maxNumeric, int minDecimal, int maxDecimal, int valueNumeric, int valueDecimal) {
		mWheelControlNumeric.setViewAdapter(minNumeric, maxNumeric);
		mWheelControlNumeric.setCurrentItem(valueNumeric);
		if (maxDecimal - minDecimal != 0) {
			mWheelControlDecimal.setVisibility(View.VISIBLE);
			mWheelControlDecimal.setViewAdapter(minDecimal, maxDecimal);
			mWheelControlDecimal.setCurrentItem(valueDecimal);
		} else {
			mWheelControlDecimal.setVisibility(View.GONE);
		}
	}

	private void setControlInfo(ControlInfo info) {
		String measureType = info.optStringProperty("@SDPhysicalMeasureMeasureType");
		if (measureType.equalsIgnoreCase(GxMeasuresHelper.MeasureType.HEIGHT))
			mMeasuresHelper = new GxMeasuresHeightHelper();
		else if (measureType.equalsIgnoreCase(GxMeasuresHelper.MeasureType.WEIGHT))
			mMeasuresHelper = new GxMeasuresWeightHelper();
		else if (measureType.equalsIgnoreCase(GxMeasuresHelper.MeasureType.TEMPERATURE))
			mMeasuresHelper = new GxMeasuresTemperatureHelper();
		//else if (measureType.equalsIgnoreCase(GxMeasuresHelper.MeasureType.Volume))
			//mMeasuresHelper = new GxMeasuresVolumeHelper();
		else
			//use Default Height
			mMeasuresHelper = new GxMeasuresHeightHelper();
	}

	private void setDefaultValues(String strValue) {
		double value = Services.Strings.tryParseDouble(strValue, 0);
		mCurrentValue = mMeasuresHelper.getDisplayValue(value);
		mAction.setText(mCurrentValue);
    	mText.setText(mCurrentValue);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mText.setVisibility(enabled ? View.GONE : View.VISIBLE);
		mAction.setVisibility(enabled ? View.VISIBLE : View.GONE);
	}

	@Override
	public String getGxValue() {
		return mMeasuresHelper.getGxValue(GxMeasuresHelper.VALUE_KEY, GxMeasuresHelper.UNIT_KEY, GxMeasuresHelper.CONVERTED_VALUE_KEY);
	}

	@Override
	public void setGxValue(String value) {
		//{"Unit":"in","Value":68.0,"ConvertedValue":0.0}
		try {
			JSONObject valueJSONObject = new JSONObject(value);
			String valueUnit = valueJSONObject.getString(GxMeasuresHelper.UNIT_KEY);
			double valueKey = valueJSONObject.getDouble(GxMeasuresHelper.VALUE_KEY);
			mCurrentValue = mMeasuresHelper.getDisplayValue(mMeasuresHelper.getValueKey(valueKey, valueUnit));
			mAction.setText(mCurrentValue);
	    	mText.setText(mCurrentValue);
		} catch (JSONException e) {
			setDefaultValues(value);
		}
	}

	@Override
	public String getGxTag() {
		return mTag;
	}

	@Override
	public void setGxTag(String tag) {
		mTag = tag;
	}

	@Override
	public void setValueFromIntent(Intent data) {
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
	protected void setBackgroundBorderProperties(ThemeClassDefinition themeClass)
	{
		// Override to pass layout item definition (GxLinearLayout does not always correspond to a layout item).
		ThemeUtils.setBackgroundBorderProperties(this, themeClass, BackgroundOptions.defaultFor(mDefinition));
	}

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}
}

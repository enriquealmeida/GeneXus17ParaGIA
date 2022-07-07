package com.genexus.android.controls.wheels;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.IValuesFormatter;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.GxTextView;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.ui.Coordinator;

@SuppressLint("ViewConstructor")
public class GxMultiWheelPicker extends FrameLayout implements IGxEdit {
	public static final String NAME = "SDMultiWheel";

	private static final String TYPE_INT = "int";
	private static final String TYPE_CHAR = "char";

	private Coordinator mCoordinator = null;
	private String mTag = null;

	private String mCurrentValue = null; // As a JSON string.

	private Button mAction = null;
	private Dialog mMultiWheelDialog = null;
	private ArrayList<GxWheelPicker> mMultiWheelArray = new ArrayList<GxWheelPicker>();
	private ArrayList<String> mUnits = new ArrayList<String>();

	private LayoutItemDefinition mDefinition = null;
	
	public GxMultiWheelPicker(Context context, Coordinator coordinator, LayoutItemDefinition item) {
		super(context);
		mCoordinator = coordinator;
		mDefinition = item;
		
		setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		mAction = new AppCompatButton(getContext());
		mAction.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMultiWheelDialog();
			}
		});
		addView(mAction);
	}

	private GxWheelPicker getWheelPicker(String type, String value, JSONArray range) throws JSONException {
		GxWheelPicker wheel = new GxWheelPicker(getContext());
		int currentItem = 0;

		if (type.equalsIgnoreCase(TYPE_INT)) {
			int min = range.getInt(0);
			int max = range.getInt(1);
			currentItem = Integer.parseInt(value) - min;
			wheel.setViewAdapter(min, max);
		} else if (type.equalsIgnoreCase(TYPE_CHAR)) {
			String[] rangeList = new String[range.length()];
			for (int j = 0; j < range.length(); j++) {
				String str = range.getString(j);
				rangeList[j] = str;
				if (str.equals(value)) {
					currentItem = j;
				}
			}
			wheel.setViewAdapter(rangeList);
		} else {
			Services.Log.error("Invalid SD MutliWheel type.");
			return null;
		}

		wheel.setCurrentItem(currentItem);
		wheel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		wheel.setCyclic(true);

		return wheel;
	}

	private void showMultiWheelDialog() {
		LinearLayout dialogContent = new LinearLayout(getContext());
		dialogContent.setOrientation(LinearLayout.HORIZONTAL);
		dialogContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));
		dialogContent.setGravity(Gravity.CENTER);

		try {
			mMultiWheelArray = new ArrayList<GxWheelPicker>();
			if (!TextUtils.isEmpty(mCurrentValue)) {
				JSONArray multiWheelValues = new JSONArray(mCurrentValue);
				for (int i = 0; i < multiWheelValues.length(); i++) {
					JSONObject wheelsdt = multiWheelValues.getJSONObject(i);
					String unit = (String) wheelsdt.get("Unit");
					String type = (String) wheelsdt.get("Type");
					String value = (String) wheelsdt.get("Value");
					JSONArray range = (JSONArray) wheelsdt.get("Range");

					GxWheelPicker wheel = getWheelPicker(type, value, range);
					if (wheel != null) {
						dialogContent.addView(wheel, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
						mMultiWheelArray.add(wheel);
						mUnits.add(unit);
					}
				}
			} else {
				Services.Log.error("Wheel definition is null");
			}
		} catch (JSONException e) {
			Services.Log.error("Error reading SD MultiWheel's json data.", e);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setView(dialogContent)
			// Action buttons
			.setPositiveButton(R.string.GXM_button_ok, okButtonListener)
			.setNegativeButton(R.string.GXM_cancel, cancelButtonListener);

		mMultiWheelDialog = builder.create();
		mMultiWheelDialog.show();
	}

	private DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (getMutliWheelValue() == null) {
				return;
			}
			String previousValue = mCurrentValue;
			mCurrentValue = getMutliWheelValue();
			if (mCoordinator != null && !mCurrentValue.equals(previousValue)) {
				mCoordinator.onValueChanged(GxMultiWheelPicker.this, true);
			}
			mAction.setText(jsonToString(mCurrentValue));
		}
	};

	private DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
		}
	};

	private String jsonToString(String json) {
		String res = "";
		try {
			JSONArray wheels = new JSONArray(json);
			for (int i = 0; i < wheels.length(); i++) {
				JSONObject w = wheels.getJSONObject(i);
				String value = (String) w.get("Value");
				String unit = (String) w.get("Unit");
				res += value + " " + unit;
				if (i < wheels.length() - 1) {
					res += " ";
				}
			}
		} catch (JSONException e) {
			Services.Log.error("Error converting json to string");
		}

		return res;
	}

	private String getMutliWheelValue() {
		try {
			JSONArray values = new JSONArray(mCurrentValue);
			for (int i = 0; i < mMultiWheelArray.size(); i++) {
				JSONObject wheelSdt = values.getJSONObject(i);
				wheelSdt.put("Value", mMultiWheelArray.get(i).getCurrentItemText());
			}

			return values.toString();
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public void setGxValue(String json) {
		mCurrentValue = json;
		mAction.setText(jsonToString(json));
	}

	@Override
	public String getGxValue() {
		return mCurrentValue;
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
	public void setEnabled(boolean enabled) 
	{
		super.setEnabled(enabled);
		mAction.setEnabled(enabled);
	}

	@Override
	public IGxEdit getViewControl() 
	{
		return new GxTextView(getContext(), mCoordinator, mDefinition, new Formatter());
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	private class Formatter implements IValuesFormatter
	{
		@Override
		public CharSequence format(String value)
		{
			return jsonToString(value); 
		}

		@Override
		public boolean needsAsync()
		{
			return false;
		}
	}
	
	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}
}

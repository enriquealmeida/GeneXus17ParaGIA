package com.genexus.android.controls.wheels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genexus.android.core.base.controls.IGxControlNotifyEvents;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.controls.GxTextView;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.IGxEditWithDependencies;
import com.genexus.android.core.ui.Coordinator;

import java.util.List;

import com.genexus.android.controls.spinnerwheel.AbstractWheel;
import com.genexus.android.controls.spinnerwheel.OnWheelScrollListener;

@SuppressLint("ViewConstructor")
public class GxWheelControl extends LinearLayout implements IGxEditWithDependencies, IGxControlRuntime, IGxControlNotifyEvents
{
	private static final String PROPERTY_CYCLIC = "Cyclic";
	public static final String NAME = "SDWheel";

	private LayoutItemDefinition mDefinition = null;
	private Coordinator mCoordinator = null;
	private String mTag = null;

	private IGxWheelControl mWheelControlDefinition;

	private GxWheelPicker mWheelControlNumeric = null;
	private GxWheelPicker mWheelControlDecimal = null;

	// Show wheel in-line or as a picker dialog.
	private boolean mShowInline = false;

	// Wheel properties
	private String mCurrentValue = null;
	private boolean mIsCyclic = false;
	private boolean mOnlyNumericWheel = false;

	// Wheel dialog
	private AlertDialog mWheelControlDialog = null;
	private Button mAction = null;
	private TextView mText = null;

	public GxWheelControl(Context context, Coordinator coordinator, LayoutItemDefinition layoutItemDefinition) {
		super(context);
		mCoordinator = coordinator;
		mDefinition = layoutItemDefinition;

		if (layoutItemDefinition.getControlInfo() != null) {
			setControlInfo(layoutItemDefinition.getDataItem(), layoutItemDefinition.getControlInfo());
		}

		mWheelControlNumeric = new GxWheelPicker(getContext());
		mWheelControlDecimal = new GxWheelPicker(getContext());
		mWheelControlNumeric.setCyclic(mIsCyclic);
		mWheelControlDecimal.setCyclic(mIsCyclic);
		mWheelControlDefinition.setViewAdapter(mCurrentValue, mWheelControlNumeric, mWheelControlDecimal);

		if (mShowInline) {
			setupWheelsContainer(this);
			mWheelControlNumeric.addScrollingListener(onWheelScrollListener);
			if (!mOnlyNumericWheel) {
				mWheelControlDecimal.addScrollingListener(onWheelScrollListener);
			}
		} else {
			mText = new TextView(getContext());
			mText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mText.setGravity(Gravity.CENTER);
			mText.setText(mCurrentValue);
			addView(mText);
			mAction = new AppCompatButton(getContext());
			mAction.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mAction.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mWheelControlDialog == null) {
						mWheelControlDialog = createDialog();
					}
					mWheelControlDialog.show();
				}
			});
			mAction.setText(mCurrentValue);
			addView(mAction);
		}

		setEnabled(true); // Complete setup (see setEnabled() implementation).
	}

    private OnWheelScrollListener onWheelScrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(AbstractWheel wheel) {
			// Nothing to do.
		}

		@Override
		public void onScrollingFinished(AbstractWheel wheel) {
			onWheelValueChanged();
		}

    };

    private void onWheelValueChanged() {
    	String previousValue = mCurrentValue;
    	mCurrentValue = mWheelControlDefinition.getCurrentStringValue(mWheelControlNumeric, mWheelControlDecimal);
    	mWheelControlDefinition.setViewAdapter(mCurrentValue, mWheelControlNumeric, mWheelControlDecimal);
    	if (mCoordinator != null && !mCurrentValue.equals(previousValue)) {
    		mCoordinator.onValueChanged(GxWheelControl.this, true);
    	}
    }

    private void setupWheelsContainer(LinearLayout linearLayout) {
    	linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    	linearLayout.setGravity(Gravity.CENTER);
    	linearLayout.addView(mWheelControlNumeric, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		if (!mOnlyNumericWheel) {
			linearLayout.addView(mWheelControlDecimal, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
		}
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
				}
			})
			.setNegativeButton(R.string.GXM_cancel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mWheelControlDefinition.setViewAdapter(mCurrentValue, mWheelControlNumeric, mWheelControlDecimal);
					dialog.cancel();
				}
			});

		return builder.create();
	}

	private void setControlInfo(DataItem dataItem, ControlInfo info) {
		mIsCyclic = info.optBooleanProperty("@SDWheelCyclic");
		mShowInline = info.optStringProperty("@SDWheelDisplayStyle").equalsIgnoreCase("inline");
		String dataSourceFrom = info.optStringProperty("@DataSourceFrom");

		// If the control is numeric, controlValues comes empty. Otherwise, it contains the enum values.
		if (dataSourceFrom.equalsIgnoreCase("Values")) {
			mWheelControlDefinition = new GxWheelValuesControl(dataItem, info);
			mOnlyNumericWheel = true;
		}
		else if (dataSourceFrom.equalsIgnoreCase("Attributes") || dataSourceFrom.equalsIgnoreCase("DataProvider")) {
			mWheelControlDefinition = new GxWheelAttributesControl(mCoordinator, info);
			mOnlyNumericWheel = true;
		}
		else if (dataSourceFrom.equalsIgnoreCase("Range")) {
			mWheelControlDefinition = new GxWheelNumericControl(info);
			mOnlyNumericWheel = ((GxWheelNumericControl) mWheelControlDefinition).isOnlyWheelControlNumeric();
		}

		mCurrentValue = mWheelControlDefinition.getDisplayInitialValue();

		mWheelControlDefinition.setOnReady(new IGxWheelControlReady() {
			@Override
			public void onReady() {
				String valueToSet = mSetValueOnReady;
				if (valueToSet != null) {
					mSetValueOnReady = null;
					setGxValue(valueToSet);
				}
				else {
					setGxValue(mCurrentValue); // reload
				}
			}
		});
	}

	@Override
	public String getGxValue() {
		return mWheelControlDefinition.getGxValue(mCurrentValue);
	}

	private String mSetValueOnReady;

	@Override
	public void setGxValue(String value) {
		if (mWheelControlDefinition.isReady()) {
			mCurrentValue = mWheelControlDefinition.getGxDisplayValue(value);
			mWheelControlDefinition.setViewAdapter(mCurrentValue, mWheelControlNumeric, mWheelControlDecimal);
			if (!mShowInline) {
				mAction.setText(mCurrentValue);
				mText.setText(mCurrentValue);
			}
		} else {
			mSetValueOnReady = value;
			mWheelControlDefinition.onFirstSetGxValue();
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
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (mShowInline) {
			mWheelControlNumeric.setEnabled(enabled);
			mWheelControlDecimal.setEnabled(enabled);
		} else {
			mText.setVisibility(enabled ? View.GONE : View.VISIBLE);
			mAction.setVisibility(enabled ? View.VISIBLE : View.GONE);
		}
	}

	@Override
	public IGxEdit getViewControl() {
		return new GxTextView(getContext(), mDefinition);
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}

	@Override
	public void setPropertyValue(String name, Value value) {
		switch (name) {
			case PROPERTY_CYCLIC:
				mIsCyclic = value.coerceToBoolean();
				mWheelControlNumeric.setCyclic(mIsCyclic);
				mWheelControlDecimal.setCyclic(mIsCyclic);
				break;
			default:
				throw new IllegalArgumentException(String.format("Unknown property: %s", name));
		}
	}

	@Override
	public Value getPropertyValue(String name) {
		switch (name) {
			case PROPERTY_CYCLIC:
				return Value.newBoolean(mIsCyclic);
			default:
				throw new IllegalArgumentException(String.format("Unknown property: %s", name));
		}
	}

	@Override
	public List<String> getDependencies() {
		return mWheelControlDefinition.getDependencies();
	}

	@Override
	public void onDependencyValueChanged(String name, Object value) {
		mWheelControlDefinition.onDependencyValueChanged(name, value);
	}

	@Override
	public void notifyEvent(IGxControlNotifyEvents.EventType type)
	{
		if (type == IGxControlNotifyEvents.EventType.REFRESH && mWheelControlDefinition.isReady()) {
			mWheelControlDefinition.onRefresh();
		}
	}
}

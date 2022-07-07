package com.genexus.android.controls.wheels;

import java.util.List;

public interface IGxWheelControl {

	Boolean isReady();

	void setOnReady(IGxWheelControlReady ready);

	String getDisplayInitialValue();

	void setViewAdapter(String mDisplayValue, GxWheelPicker mWheelControlNumeric,  GxWheelPicker mWheelControlDecimal);

	String getCurrentStringValue(GxWheelPicker mWheelControlNumeric, GxWheelPicker mWheelControlDecimal);

	String getGxDisplayValue(String value);

	String getGxValue(String displayValue);

	void onFirstSetGxValue();

	List<String> getDependencies();

	void onDependencyValueChanged(String name, Object value);

	void onRefresh();
}

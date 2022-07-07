package com.genexus.android.controls;

import android.content.Context;

import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;
import com.genexus.android.controls.wheels.GxMultiWheelPicker;
import com.genexus.android.controls.wheels.GxWheelControl;
import com.genexus.android.controls.wheels.measures.GxMeasuresControl;


public class WheelsModule implements GenexusModule {

	@Override
	public void initialize(Context context) {
		UcFactory.addControl(new UserControlDefinition(GxWheelControl.NAME, GxWheelControl.class));
		UcFactory.addControl(new UserControlDefinition(GxMultiWheelPicker.NAME, GxMultiWheelPicker.class));
		UcFactory.addControl(new UserControlDefinition(GxMeasuresControl.NAME, GxMeasuresControl.class));
	}
}

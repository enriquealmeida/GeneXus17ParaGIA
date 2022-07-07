package com.genexus.android.core.controls;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.ui.Coordinator;

public class GxEditTextPhone extends GxEditText{
	public GxEditTextPhone(Context context, Coordinator coordinator, LayoutItemDefinition def) {
		super(context, coordinator, def);
		this.setInputType(InputType.TYPE_CLASS_PHONE);
		setGravity(Gravity.END);
		setMaxEms(10);
	}
}

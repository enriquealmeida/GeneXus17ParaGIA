package com.genexus.android.core.controls;

import android.content.Context;
import android.text.InputType;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.ui.Coordinator;

public class GxEditTextMail extends GxEditText{
	public GxEditTextMail(Context context, Coordinator coordinator, LayoutItemDefinition def) {
		super(context, coordinator, def);
		this.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		setMaxEms(10);
	}
}

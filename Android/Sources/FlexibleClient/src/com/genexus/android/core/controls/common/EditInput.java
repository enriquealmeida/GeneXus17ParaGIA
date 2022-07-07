package com.genexus.android.core.controls.common;

import com.genexus.android.core.base.controls.MappedValue;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.IValuesFormatter;
import com.genexus.android.core.ui.Coordinator;

public abstract class EditInput
{
	public abstract boolean getSupportsAutocorrection();
	public abstract Integer getEditLength();
	public abstract IValuesFormatter getValuesFormatter();

	public abstract void setValue(String value, OnMappedAvailable onTextAvailable);
	public abstract void setText(String text, OnMappedAvailable onValueAvailable);
	public abstract String getValue();
	public abstract String getText();

	public static EditInput newEditInput(Coordinator coordinator, LayoutItemDefinition layoutItem)
	{
		if (EditInputDescriptions.isInputTypeDescriptions(layoutItem))
			return new EditInputDescriptions(coordinator, layoutItem);
		else
			return new EditInputValues(layoutItem);
	}

	public interface OnMappedAvailable
	{
		void run(MappedValue mapped);
	}
}

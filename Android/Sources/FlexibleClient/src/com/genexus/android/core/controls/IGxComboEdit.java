package com.genexus.android.core.controls;

import java.util.List;

import com.genexus.android.core.base.metadata.EnumValuesDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;

public interface IGxComboEdit extends IGxEdit
{
	LayoutItemDefinition getDefinition();
	
	// TODO: Should be "<? extends SomethingMoreGeneric>", to allow other option lists.
	void setComboValues(List<? extends EnumValuesDefinition> values);
}

package com.genexus.android.testing;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;

public class TestingUtils {
	public static LayoutItemDefinition createControlDefinition(ControlInfo controlInfo) {
		LayoutItemDefinition definition = new LayoutItemDefinition(new LayoutDefinition(null), null);
		definition.setControlInfo(controlInfo);
		return definition;
	}
}

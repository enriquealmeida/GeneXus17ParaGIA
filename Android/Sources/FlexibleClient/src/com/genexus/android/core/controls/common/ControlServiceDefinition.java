package com.genexus.android.core.controls.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;

@SuppressWarnings("checkstyle:MemberName")
public abstract class ControlServiceDefinition
{
	public final LayoutItemDefinition LayoutItem;
	public final String Service;
	public final List<String> ServiceInput;

	protected ControlServiceDefinition(LayoutItemDefinition itemDefinition, String serviceSuffix)
	{
		LayoutItem = itemDefinition;
		ControlInfo info = itemDefinition.getControlInfo();
		if (info != null)
		{
			Service = info.optStringProperty("@service" + serviceSuffix);

			String serviceInputParameters = info.optStringProperty("@service" + serviceSuffix + "Inputs");
			if (Services.Strings.hasValue(serviceInputParameters))
				ServiceInput = Arrays.asList(Services.Strings.split(serviceInputParameters, ","));
			else
				ServiceInput = new ArrayList<>();
		}
		else
		{
			Service = null;
			ServiceInput = new ArrayList<>();
		}
	}
}

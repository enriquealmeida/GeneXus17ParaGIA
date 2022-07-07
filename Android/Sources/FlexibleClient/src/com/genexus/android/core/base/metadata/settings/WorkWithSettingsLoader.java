package com.genexus.android.core.base.metadata.settings;

import android.content.Context;

import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.utils.Strings;

public class WorkWithSettingsLoader
{
	private static final String FILE_NAME = "settings";

	public static WorkWithSettings load(Context context, GenexusApplication application)
	{
		String resourceName = String.format("%s.%s", Strings.toLowerCase(application.getAppEntry()), FILE_NAME);
		INodeObject jsonData = MetadataLoader.getDefinition(context, resourceName);
		if (jsonData == null)
			jsonData = MetadataLoader.getDefinition(context, FILE_NAME);
		if (jsonData == null)
			return null;

		return loadFromJson(jsonData);
	}

	private static WorkWithSettings loadFromJson(INodeObject jsonData)
	{
		return new WorkWithSettings(jsonData);
	}
}

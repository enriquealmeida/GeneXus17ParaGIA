package com.genexus.android.core.base.metadata.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.InstanceProperties;
import com.genexus.android.core.base.metadata.enums.ImageUploadModes;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class WorkWithSettings implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final List<PlatformDefinition> mPlatforms;
	private boolean mUseUtcConversion = true;
	private String mIDEConnectionString = "";

	private final InstanceProperties mInstanceProperties = new InstanceProperties();
	private final PropertiesObject mModelProperties = new PropertiesObject();

	private WorkWithSettings()
	{
		mPlatforms = new ArrayList<>();
	}

	WorkWithSettings(INodeObject jsonData)
	{
		this();

		INodeObject rootJson = jsonData.getNode("Config");
		if (rootJson == null)
			return;

		mUseUtcConversion = rootJson.optBoolean("@ConvertTimeFromUTC", true);
		mIDEConnectionString = rootJson.optString("@IDE_ConnectionString", "");

		// Read Instance Properties
		INodeObject instancePropNode = rootJson.getNode("instanceProperties");
		if (instancePropNode != null)
			mInstanceProperties.deserialize(instancePropNode);

		// Read Model Properties
		INodeObject modelPropNode = rootJson.getNode("ModelProperties");
		if (modelPropNode != null)
			mModelProperties.deserialize(modelPropNode);

		readPlatforms(rootJson);
	}

	public static WorkWithSettings empty()
	{
		return new WorkWithSettings();
	}

	public boolean useUtcConversion()
	{
		return mUseUtcConversion;
	}
	
	public String getIDEConnectionString()
	{
		return mIDEConnectionString;
	}

	public PlatformDefinition getPlatform(int index)
	{
		return mPlatforms.get(index);
	}

	public PlatformDefinition getPlatform(String name)
	{
		if (!Services.Strings.hasValue(name))
		{
			// Not specified! Search for the first ALL/ALL platform.
			// Previously this was the first one, but since "General" was renamed/moved this is no longer the case.
			for (int i = 0; i < mPlatforms.size(); i++)
			{
				PlatformDefinition platform = mPlatforms.get(i);
				if (platform.getOS() == PlatformDefinition.OS_ALL && platform.getSmallestWidthRange().isAll())
					return platform;
			}

			// If none are found, then return nothing.
			return PlatformDefinition.unknown();
		}

		for (int i = 0; i < mPlatforms.size(); i++)
			if (mPlatforms.get(i).getName().equalsIgnoreCase(name))
				return mPlatforms.get(i);

		return PlatformDefinition.unknown();
	}

	public List<PlatformDefinition> getPlatforms()
	{
		return mPlatforms;
	}

	private void readPlatforms(INodeObject rootJson)
	{
		INodeObject platformsJson = rootJson.getNode("Platforms");
		if (platformsJson == null)
			return;

		INodeCollection platformCollectionJson = platformsJson.optCollection("Platform");
		int collectionSize = platformCollectionJson.length();
		for (int i = 0; i < collectionSize; i++)
			readPlatform(platformCollectionJson.getNode(i));
	}

	private void readPlatform(INodeObject platformJson)
	{
		PlatformDefinition platform = new PlatformDefinition(platformJson);
		mPlatforms.add(platform);
	}

	public @NonNull InstanceProperties getInstanceProperties()
	{
		return mInstanceProperties;
	}

	public @NonNull PropertiesObject getModelProperties()
	{
		return mModelProperties;
	}

	public UploadSizeDefinition getUploadSize(int sizeMode)
	{
		if (sizeMode == ImageUploadModes.MEDIUM)
		{
			return new UploadSizeDefinition(sizeMode, mModelProperties.optStringProperty("@MediumImageUploadSize"));
		}
		else if (sizeMode == ImageUploadModes.SMALL)
		{
			return new UploadSizeDefinition(sizeMode, mModelProperties.optStringProperty("@SmallImageUploadSize"));
		}
		else
		{
			//Default large or actualsize.
			return new UploadSizeDefinition(sizeMode, mModelProperties.optStringProperty("@LargeImageUploadSize"));
		}
	}
}

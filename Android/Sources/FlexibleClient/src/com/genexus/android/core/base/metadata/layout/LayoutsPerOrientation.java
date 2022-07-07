package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.services.Services;

public class LayoutsPerOrientation
{
	private final LayoutDefinition mPortrait;
	private final LayoutDefinition mLandscape;

	public LayoutsPerOrientation(LayoutDefinition portrait, LayoutDefinition landscape)
	{
		mPortrait = portrait;
		mLandscape = landscape;
	}

	public static LayoutsPerOrientation none()
	{
		return new LayoutsPerOrientation(null, null);
	}

	public LayoutDefinition getCurrent()
	{
		Orientation deviceOrientation = Services.Device.getScreenOrientation();
		if (deviceOrientation == Orientation.UNDEFINED)
			deviceOrientation = Orientation.PORTRAIT; // Force portrait for square devices.

		if (deviceOrientation == Orientation.PORTRAIT)
			return getCurrentLayoutFrom(mPortrait, mLandscape, deviceOrientation);
		else
			return getCurrentLayoutFrom(mLandscape, mPortrait, deviceOrientation);
	}

	private static LayoutDefinition getCurrentLayoutFrom(LayoutDefinition current, LayoutDefinition rotated, Orientation deviceOrientation)
	{
		if (current != null)
		{
			current.deserialize();
			current.setActualOrientation(deviceOrientation, rotated != null);
			return current;
		}
		else if (rotated != null)
		{
			rotated.deserialize();
			rotated.setActualOrientation(Orientation.opposite(deviceOrientation), false);
			return rotated;
		}
		else
			return null;
	}
}

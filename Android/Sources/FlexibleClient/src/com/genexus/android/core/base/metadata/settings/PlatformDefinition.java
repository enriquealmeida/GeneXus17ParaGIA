package com.genexus.android.core.base.metadata.settings;

import java.io.Serializable;

import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Range;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.base.utils.Version;

public class PlatformDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final int OS_ALL = 0;
	public static final int OS_ANDROID = 1;
	public static final int OS_BLACKBERRY = 2;
	public static final int OS_IOS = 3;
	public static final int OS_UNKNOWN = -1;

	public static final int NAVIGATION_DEFAULT = 0;
	public static final int NAVIGATION_FLIP = 1;
	public static final int NAVIGATION_SPLIT = 2;
	public static final int NAVIGATION_SLIDE = 3;
	public static final int NAVIGATION_UNKNOWN = -1;

	public static final int SMALLEST_WIDTH_DP_TABLET = 600;

	private String mName;
	private int mOS;
	private Range mSmallestWidthRange;
	private Version mOSVersion;
	private String mTheme;
	private int mNavigation;
	private Orientation mDefaultOrientation;

	private PlatformDefinition() { }

	public PlatformDefinition(INodeObject jsonData)
	{
		mName = jsonData.optString("@Name");
		mOS = Services.Strings.parseIntEnum(jsonData.optString("@OS"), "All", "Android", "Blackberry", "iOS");
		mSmallestWidthRange = parseSmallestWidthRange(jsonData);
		mOSVersion = new Version(jsonData.optString("@Version"));
		mTheme = MetadataLoader.getAttributeName(jsonData.optString("@Theme"));
		mNavigation = Services.Strings.parseIntEnum(jsonData.optString("@NavigationStyle"), "Default", "Flip", "Split", "Slide");
		mDefaultOrientation = Orientation.parse(jsonData.optString("@DefaultLayoutOrientation"));
	}

	private static Range parseSmallestWidthRange(INodeObject jsonData)
	{
		if (jsonData.has("@MinimumShortestBound") || jsonData.has("@MaximumShortestBound"))
		{
			int minimum = jsonData.optInt("@MinimumShortestBound");
			int maximum = jsonData.optInt("@MaximumShortestBound");
			return new Range(minimum != 0 ? minimum : null, maximum != 0 ? maximum : null);
		}
		else
		{
			// Calculate from old "Size" property.
			String size = jsonData.optString("@Size");
			if (Strings.hasValue(size))
			{
				if (size.equalsIgnoreCase("Small"))
					return new Range(null, SMALLEST_WIDTH_DP_TABLET - 1);
				else if (size.equalsIgnoreCase("Medium"))
					return new Range(SMALLEST_WIDTH_DP_TABLET, 719);
				else if (size.equalsIgnoreCase("Large"))
					return new Range(720, null);
			}

			return new Range(null, null); // "All", unknown value, or no property.
		}
	}

	public static PlatformDefinition unknown()
	{
		PlatformDefinition unknown = new PlatformDefinition();
		unknown.mName = "Unknown";
		unknown.mOS = OS_UNKNOWN;
		unknown.mSmallestWidthRange = new Range(null, null);
		unknown.mTheme = Strings.EMPTY;
		unknown.mNavigation = NAVIGATION_UNKNOWN;
		unknown.mDefaultOrientation = Orientation.UNDEFINED;
		return unknown;
	}

	@Override
	public String toString()
	{
		return mName;
	}

	public String getName() { return mName; }
	public int getOS() { return mOS; }
	public Range getSmallestWidthRange() { return mSmallestWidthRange; }
	public Version getOSVersion() { return mOSVersion; }
	public String getTheme() { return mTheme; }
	public int getNavigation() { return mNavigation; }
	public Orientation getDefaultOrientation() { return mDefaultOrientation; }
}

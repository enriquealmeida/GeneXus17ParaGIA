package com.genexus.android.core.base.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.genexus.android.core.base.metadata.InstanceProperties;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutsPerOrientation;
import com.genexus.android.core.base.metadata.settings.PlatformDefinition;
import com.genexus.android.core.base.metadata.settings.WorkWithSettings;
import com.genexus.android.core.base.services.Services;

public class PlatformHelper {
	private static int sNavigationStyle = PlatformDefinition.NAVIGATION_UNKNOWN;

	public static int getNavigationStyle()
	{
		// Calculate and cache.
		if (sNavigationStyle == PlatformDefinition.NAVIGATION_UNKNOWN)
			sNavigationStyle = calculateNavigationStyle();

		return sNavigationStyle;
	}

	private static int calculateNavigationStyle()
	{
		// Get the navigation style from the first applicable platform that doesn't have "default" set, if any.
		for (PlatformDefinition platform : getValidPlatforms())
		{
			int navigationStyle = platform.getNavigation();
			if (navigationStyle > PlatformDefinition.NAVIGATION_DEFAULT)
				return navigationStyle;
		}

		return PlatformDefinition.NAVIGATION_DEFAULT;
	}

	/**
	 * Returns the list of Platform Definitions that are "compatible" with the current one, sorted
	 * from most precise match to most generic one (e.g. [Android Phone, Any Android, Any Phone, Any]).
	 * That way, callers can search for a platform with a particular set value (theme, navigation style).
	 */
	public static List<PlatformDefinition> getValidPlatforms()
	{
		ArrayList<PlatformDefinition> platforms = new ArrayList<>();
		WorkWithSettings settings = Services.Application.getDefinition().getPatternSettings();

		if (settings != null)
		{
			for (PlatformDefinition platform : settings.getPlatforms())
			{
				if (isValidPlatform(platform))
					platforms.add(platform);
			}
		}

		Collections.sort(platforms, new PlatformScoreComparator());
		return platforms;
	}

	/**
	 * Gets the platform definition that most closely matches the characteristics of the current device.
	 */
	public static PlatformDefinition getPlatform()
	{
		List<PlatformDefinition> platforms = getValidPlatforms();
		if (platforms.size() != 0)
			return platforms.get(0);
		else
			return PlatformDefinition.unknown();
	}

	private static class PlatformScoreComparator implements Comparator<PlatformDefinition>
	{
		// We want a comparator that returns the "better match" in first place.
		private static final int EQUAL = 0;
		private static final int PREFER_LEFT = -1;
		private static final int PREFER_RIGHT = 1;

		@Override
		@SuppressWarnings("ConstantConditions")
		public int compare(PlatformDefinition lhs, PlatformDefinition rhs)
		{
			// For "acceptable" platforms, we consider OS, Size, Version in that order.
			if (!isValidPlatform(lhs) || !isValidPlatform(rhs))
				throw new IllegalArgumentException("PlatformScoreComparator can only handle valid platforms.");

			// OS: Exact match before "Any".
			int deviceOS = Services.Device.getOS();
			boolean leftOSExact = (lhs.getOS() == deviceOS);
			boolean rightOSExact = (rhs.getOS() == deviceOS);
			if (leftOSExact && !rightOSExact) return PREFER_LEFT;
			if (rightOSExact && !leftOSExact) return PREFER_RIGHT;

			// Size: Platform has minimum and maximum smallest width.
			// Since both are acceptable we already know that both contain the device's smallest width.
			// Ranges may overlap. If one range is totally contained in the other, then choose
			// the smaller one. The IDE should check that partial overlaps don't occur, so in
			// that case we just consider them "equal" (since this is a comparator it must return
			// consistent results).
			Range leftSizeRange = lhs.getSmallestWidthRange();
			Range rightSizeRange = rhs.getSmallestWidthRange();
			if (rightSizeRange.contains(leftSizeRange) && !leftSizeRange.contains(rightSizeRange))
				return PREFER_LEFT;
			else if (leftSizeRange.contains(rightSizeRange) && !rightSizeRange.contains(leftSizeRange))
				return PREFER_RIGHT;

			// Version: Platform has "minimum version". Prefer closest to the device over farther away over generic.
			// Since both are acceptable we already know that they are eiter "any" or <= than the device's own version,
			// so choose the "greatest" version if both are non-any.
			Version leftVersion = lhs.getOSVersion();
			Version rightVersion = rhs.getOSVersion();
			if (!leftVersion.isEmpty() && !rightVersion.isEmpty())
			{
				if (leftVersion.isGreaterThan(rightVersion))
					return PREFER_LEFT;
				else if (rightVersion.isGreaterThan(leftVersion))
					return PREFER_RIGHT;
			}
			else if (!leftVersion.isEmpty() && rightVersion.isEmpty())
				return PREFER_LEFT;
			else if (!rightVersion.isEmpty() && leftVersion.isEmpty())
				return PREFER_RIGHT;

			// All equal.
			return EQUAL;
		}
	}

	public static LayoutsPerOrientation bestLayouts(List<LayoutDefinition> layouts)
	{
		if (layouts == null || layouts.size() == 0)
			return LayoutsPerOrientation.none();

		LayoutDefinition portrait = bestLayoutFor(layouts, Orientation.PORTRAIT);
		LayoutDefinition landscape = bestLayoutFor(layouts, Orientation.LANDSCAPE);

		Orientation defaultOrientation = getDefaultOrientation();
		if (defaultOrientation != Orientation.UNDEFINED)
		{
			// If a default orientation is specified, discard any chosen layouts that have it
			// and whose orientation does not match the one it was selected for. Doing this
			// instead of treating it as defined for the specific orientation is _on purpose_
			// (see spec of the Default Layout Orientation property for details).
			if (defaultOrientation == Orientation.LANDSCAPE && portrait != null && portrait.getOrientation() == Orientation.UNDEFINED)
				portrait = null;

			if (defaultOrientation == Orientation.PORTRAIT && landscape != null && landscape.getOrientation() == Orientation.UNDEFINED)
				landscape = null;
		}

		return new LayoutsPerOrientation(portrait, landscape);
	}

	private static LayoutDefinition bestLayoutFor(List<LayoutDefinition> layouts, Orientation orientation)
	{
		ArrayList<LayoutDefinition> validLayouts = new ArrayList<>();

		for (LayoutDefinition layout : layouts)
		{
			if (isValidLayoutFor(layout, orientation))
				validLayouts.add(layout);
		}

		Collections.sort(validLayouts, new LayoutScoreComparator(orientation));
		if (validLayouts.size() != 0)
			return validLayouts.get(0);
		else
			return null;
	}

	private static class LayoutScoreComparator implements Comparator<LayoutDefinition>
	{
		private final Orientation mDesiredOrientation;

		private static final int PREFER_LEFT = PlatformScoreComparator.PREFER_LEFT;
		private static final int PREFER_RIGHT = PlatformScoreComparator.PREFER_RIGHT;
		private static final int EQUAL = PlatformScoreComparator.EQUAL;

		public LayoutScoreComparator(Orientation desiredOrientation)
		{
			mDesiredOrientation = desiredOrientation;
		}

		@Override
		public int compare(LayoutDefinition lhs, LayoutDefinition rhs)
		{
			// Compare platforms first.
			int platformComparison = new PlatformScoreComparator().compare(lhs.getPlatformDefinition(), rhs.getPlatformDefinition());
			if (platformComparison != PlatformScoreComparator.EQUAL)
				return platformComparison;

			// If both are the same, compare orientations. We already know that we don't have invalid orientations.
			boolean leftOrientationExact = (lhs.getOrientation() == mDesiredOrientation);
			boolean rightOrientationExact = (rhs.getOrientation() == mDesiredOrientation);
			if (leftOrientationExact && !rightOrientationExact) return PREFER_LEFT;
			if (rightOrientationExact && !leftOrientationExact) return PREFER_RIGHT;

			return EQUAL;
		}
	}

	public static boolean isValidLayout(LayoutDefinition layout)
	{
		return isValidPlatform(layout.getPlatformDefinition());
	}

	private static boolean isValidLayoutFor(LayoutDefinition layout, Orientation orientation)
	{
		if (!isValidLayout(layout))
			return false;

		Orientation layoutOrientation = layout.getOrientation();
		if (layoutOrientation != orientation && layoutOrientation != Orientation.UNDEFINED)
			return false; // Specific other orientation -> unusable.

		return true;
	}

	private static boolean isValidPlatform(PlatformDefinition platform)
	{
		if (platform.getOS() != PlatformDefinition.OS_ALL && platform.getOS() != Services.Device.getOS())
			return false; // Specific other OS.

		if (!platform.getSmallestWidthRange().contains(Services.Device.getScreenSmallestWidth()))
			return false; // Specific range for smallest width, that does not contain the actual smallest width.

		//noinspection RedundantIfStatement
		if (!platform.getOSVersion().isEmpty() && platform.getOSVersion().isGreaterThan(Services.Device.getOSVersion()))
			return false; // Specific version that is greater than the device's version.

		return true;
	}

	public static Orientation getDefaultOrientation()
	{
		// Reads the default orientation set in main object or platform.
		InstanceProperties mainProperties = Services.Application.get().getMainProperties();
		if (mainProperties != null)
		{
			String strOrientation = String.valueOf(mainProperties.getProperty("@DefaultLayoutOrientation"));
			Orientation mainOrientation = Orientation.parse(strOrientation);
			if (mainOrientation != Orientation.UNDEFINED)
				return mainOrientation;
		}

		List<PlatformDefinition> platforms = getValidPlatforms();
		if (platforms.size() != 0)
		{
			// Only check the "exact" platform. <Any> is a valid value for this property and it doesn't
			// mean that we should "look up". Inheritance is already resolved by the IDE.
			return platforms.get(0).getDefaultOrientation();
		}

		return Orientation.UNDEFINED;
	}

	// Clear any static data that points to old application.
	public static void reset() {
		sNavigationStyle = PlatformDefinition.NAVIGATION_UNKNOWN;
	}
}

package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.utils.Cast;

/**
 * Helper class for common functionality in UI action controls (buttons and action bar items).
 */
class LayoutActionDefinitionHelper
{
	public static boolean isVisible(ILayoutActionDefinition layoutData)
	{
		return getBooleanProperty(layoutData, "@visible", true);
	}

	public static boolean isEnabled(ILayoutActionDefinition layoutData)
	{
		return getBooleanProperty(layoutData, "@enabled", true);
	}

	public static ThemeClassDefinition getThemeClass(ILayoutActionDefinition layoutData)
	{
		return Services.Themes.getThemeClass(getProperty(layoutData, "@class"));
	}

	public static String getCaption(ILayoutActionDefinition layoutData)
	{
		String caption = getProperty(layoutData, "@caption");
		return Services.Language.getTranslation(caption);
	}

	public static String getImage(ILayoutActionDefinition layoutData)
	{
		return MetadataLoader.getObjectName(getProperty(layoutData, "@image"));
	}

	public static String getDisabledImage(ILayoutActionDefinition layoutData)
	{
		return MetadataLoader.getObjectName(getProperty(layoutData, "@disabledImage"));
	}

	public static String getHighlightedImage(ILayoutActionDefinition layoutData)
	{
		return MetadataLoader.getObjectName(getProperty(layoutData, "@highlightedImage"));
	}

	public static int getImagePosition(ILayoutActionDefinition layoutData)
	{
		return Alignment.parseImagePosition(getProperty(layoutData, "@imagePosition"));
	}

	public static String getProperty(ILayoutActionDefinition layoutAction, String propertyName)
	{
		// Read from control first, then from action definition.
		// Don't use optStringProperty(), because it returns empty string when the key is not present.
		// That is not what we want, because a property (e.g. @image) may be overriden with an empty value.
		String value = Cast.as(String.class, layoutAction.getProperty(propertyName));
		if (value == null)
		{
			ActionDefinition action = layoutAction.getEvent();
			if (action != null)
				value = action.optStringProperty(propertyName);
		}

		return value;
	}

	private static boolean getBooleanProperty(ILayoutActionDefinition layoutAction, String propertyName, boolean defaultValue)
	{
		return Services.Strings.tryParseBoolean(getProperty(layoutAction, propertyName), defaultValue);
	}
}

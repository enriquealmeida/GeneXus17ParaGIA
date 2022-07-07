package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;

/**
 * Interface for "action controls" in layout or action bar.
 */
public interface ILayoutActionDefinition
{
	String getEventName();
	ActionDefinition getEvent();

	boolean isVisible();
	boolean isEnabled();
	ThemeClassDefinition getThemeClass();

	String getCaption();
	String getImage();
	String getDisabledImage();
	String getHighlightedImage();

	Object getProperty(String name);
}

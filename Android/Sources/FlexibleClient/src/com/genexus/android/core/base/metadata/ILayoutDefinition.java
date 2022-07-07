package com.genexus.android.core.base.metadata;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;

/**
 * Base interface for layouts
 */
public interface ILayoutDefinition
{
	/**
	 * View should display the application bar.
	 */
	boolean getShowApplicationBar();

	/**
	 * Enable Header Row Pattern.
	 */
	boolean getEnableHeaderRowPattern();

	/**
	 * Theme class for the application bar when use Header Row.
	 */
	ThemeClassDefinition getHeaderRowApplicationBarClass();

	/**
	 * Theme class for the application bar.
	 */
	ThemeClassDefinition getApplicationBarClass();
}

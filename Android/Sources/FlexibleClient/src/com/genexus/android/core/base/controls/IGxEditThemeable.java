package com.genexus.android.core.base.controls;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.controls.IGxEdit;

/**
 * Themeable interface for user controls that are bound to attributes or variables
 * (instantiated inside a DataBoundControl view). Will generally apply text styles, but
 * NOT background or border, which are handled by the DataBoundControl itself.
 */
public interface IGxEditThemeable extends IGxEdit
{
	void applyEditClass(@NonNull ThemeClassDefinition themeClass);
}

package com.genexus.android.core.base.services;

import android.graphics.Typeface;

import androidx.annotation.Nullable;

import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.common.handlers.fonts.OnReceiveFontHandler;

public interface IFontsService {
	/**
	 * First searches for the font in the embedded assets and if not found, then in the system
	 * default fonts
	 */
	@Nullable Typeface getFontFamily(ThemeDefinition theme, String name, int weight, boolean italic, OnReceiveFontHandler receiver);
}

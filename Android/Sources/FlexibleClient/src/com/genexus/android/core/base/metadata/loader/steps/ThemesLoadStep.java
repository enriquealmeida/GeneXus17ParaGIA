package com.genexus.android.core.base.metadata.loader.steps;

import android.content.Context;

import com.genexus.android.core.base.metadata.loader.MetadataLoadStep;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.loader.MetadataParser;
import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class ThemesLoadStep implements MetadataLoadStep {
	private final Context mContext;

	public ThemesLoadStep(Context context) {
		mContext = context;
	}

	@Override
	public void load() {
		String theTheme = Services.Themes.calculateAppThemeName();
		if (!Services.Strings.hasValue(theTheme))
			return;

		// load theme named theTheme if it is on themes.json list
		INodeObject themes = MetadataLoader.getDefinition(mContext, "themes");
		if (themes != null) {
			INodeCollection arrThemes = themes.optCollection("Themes");
			for (int i = 0; i < arrThemes.length(); i++) {
				INodeObject obj = arrThemes.getNode(i);
				String themeName = obj.optString("Name");
				String themeType = obj.optString("Type");
				boolean isDesignSystem = themeType != null && themeType.equalsIgnoreCase("78b3fa0e-174c-4b2b-8716-718167a428b5");
				ThemeDefinition def;
				if (themeName.length() > 0 && themeName.equalsIgnoreCase(theTheme)) {
					def = MetadataParser.readOneTheme(mContext, themeName, isDesignSystem);
				} else {
					// create an unloaded theme to save if it is a design system
					def = new ThemeDefinition(themeName, isDesignSystem);
				}
				if (def != null)
					Services.Application.getDefinition().putTheme(def);
			}
		}
	}
}

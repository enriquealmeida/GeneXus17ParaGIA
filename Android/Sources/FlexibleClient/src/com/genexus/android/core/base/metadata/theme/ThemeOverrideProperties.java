package com.genexus.android.core.base.metadata.theme;

import java.util.HashMap;

import com.genexus.android.core.base.services.Services;

public class ThemeOverrideProperties
{
	private HashMap<String, String> mOverrides = new HashMap<>();

	public static class Overrides {
		public static final String BACKGROUND_COLOR = "background_color" ;
		public static final String BACKGROUND_IMAGE = "background_image";
		public static final String FORE_COLOR = "color";
	}

	public void setOverride(String name, String value) {
		if (!Services.Strings.hasValue(value))
			mOverrides.remove(name);
		else
			mOverrides.put(name, value);
	}

	//@Override
	public Object getOverride(String name) {
		if (mOverrides.containsKey(name))
			return mOverrides.get(name);
		return null;
	}

	public String getBackgroundColor() {
		if (mOverrides.containsKey(Overrides.BACKGROUND_COLOR))
			return mOverrides.get(Overrides.BACKGROUND_COLOR);
		return null;
	}

	public String getBackgroundImage() {
		if (mOverrides.containsKey(Overrides.BACKGROUND_IMAGE))
			return mOverrides.get(Overrides.BACKGROUND_IMAGE);
		return null;
	}

	public String getForeColor() {
		if (mOverrides.containsKey(Overrides.FORE_COLOR))
			return mOverrides.get(Overrides.FORE_COLOR);
		return null;
	}

}

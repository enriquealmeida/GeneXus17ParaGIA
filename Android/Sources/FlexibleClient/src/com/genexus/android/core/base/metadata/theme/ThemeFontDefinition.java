package com.genexus.android.core.base.metadata.theme;

import java.io.Serializable;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.FontStyle;
import android.os.Build;
import android.util.TypedValue;

import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class ThemeFontDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String mFontFamily;
	private Integer mFontWeight;
	private boolean mFontItalic;
	private Integer mFontStyle;
	private Float mFontSize;
	private boolean mFontDecorationStrikeThrough;

	public ThemeFontDefinition(PropertiesObject properties)
	{
		mFontFamily = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
			mFontWeight = FontStyle.FONT_WEIGHT_NORMAL;
		else
			mFontWeight = 400; // FontStyle.FONT_WEIGHT_NORMAL
		mFontItalic = false;
		mFontStyle = Typeface.NORMAL;
		mFontSize = null;
		mFontDecorationStrikeThrough = false;

		// Use font category defaults first.
		String fontCategory = properties.optStringProperty("font_category");
		if (Strings.hasValue(fontCategory))
			loadFromFontCategory(fontCategory);

		// Overrides from individual properties.
		String fontFamily = properties.optStringProperty("font_family");
		if (Strings.hasValue(fontFamily))
			mFontFamily = fontFamily;

		String fontWeightStr = properties.optStringProperty("font_weight");
		mFontWeight = getFontWeightFromMetadata(fontWeightStr);
		String fontStyleStr = properties.optStringProperty("font_style");
		mFontItalic = getFontItalicFromMetadata(fontStyleStr);
		Integer fontStyle = getFontWeightAndStyle(fontWeightStr, fontStyleStr);
		if (fontStyle != null)
			mFontStyle = fontStyle;

		String fontSizeStr = properties.optStringProperty("font_size");
		fontSizeStr = fontSizeStr.replace("pt", Strings.EMPTY).replace("dip", Strings.EMPTY);
		Float fontSize = Services.Strings.tryParseFloat(fontSizeStr);
		if (fontSize != null)
			mFontSize = fontSize;

		String fontDecoration = properties.optStringProperty("text_decoration");
		if (Strings.hasValue(fontDecoration) && fontDecoration.equalsIgnoreCase("line-through"))
			mFontDecorationStrikeThrough = true;
	}

	private void loadFromFontCategory(String fontCategory)
	{
		// Replace spaces (e.g. "Display 2" -> "Display2").
		fontCategory = fontCategory.replace(Strings.SPACE, Strings.EMPTY);

		// Predefined GX styles, mapped as per http://www.google.com/design/spec/style/typography.html
		if (fontCategory.equalsIgnoreCase("Headline"))
		{
			// Regular 24sp
			mFontFamily = getDefaultFontFamily(null);
			mFontSize = 24f;
		}
		else if (fontCategory.equalsIgnoreCase("Subhead") || fontCategory.equalsIgnoreCase("Subheadline"))
		{
			// Regular 16sp
			mFontFamily = getDefaultFontFamily(null);
			mFontSize = 16f;
		}
		else if (fontCategory.equalsIgnoreCase("Body") || fontCategory.equalsIgnoreCase("Body1"))
		{
			// Regular 14sp
			mFontFamily = getDefaultFontFamily(null);
			mFontSize = 14f;
		}
		else if (fontCategory.equalsIgnoreCase("Body2"))
		{
			// Medium 14sp
			mFontFamily = getDefaultFontFamily(FONT_VARIANT_MEDIUM);
			mFontSize = 14f;
		}
		else if (fontCategory.equalsIgnoreCase("Caption") || fontCategory.equalsIgnoreCase("Caption1") || fontCategory.equalsIgnoreCase("Caption2") || fontCategory.equalsIgnoreCase("Footnote"))
		{
			// Regular 12sp
			mFontFamily = getDefaultFontFamily(null);
			mFontSize = 12f;
		}
		else if (fontCategory.equalsIgnoreCase("Display1"))
		{
			// Regular 34sp
			mFontFamily = getDefaultFontFamily(null);
			mFontSize = 34f;
		}
		else if (fontCategory.equalsIgnoreCase("Display2"))
		{
			// Regular 45sp
			mFontFamily = getDefaultFontFamily(null);
			mFontSize = 45f;
		}
		else if (fontCategory.equalsIgnoreCase("Display3"))
		{
			// Regular 56sp
			mFontFamily = getDefaultFontFamily(null);
			mFontSize = 56f;
		}
		else if (fontCategory.equalsIgnoreCase("Display4"))
		{
			// Light 112sp
			mFontFamily = getDefaultFontFamily(FONT_VARIANT_LIGHT);
			mFontSize = 112f;
		}
		else if (fontCategory.equalsIgnoreCase("Title"))
		{
			// Medium 20sp
			mFontFamily = getDefaultFontFamily(FONT_VARIANT_MEDIUM);
			mFontSize = 20f;
		}
		else if (fontCategory.equalsIgnoreCase("Menu"))
		{
			// Medium 14sp
			mFontFamily = getDefaultFontFamily(FONT_VARIANT_MEDIUM);
			mFontSize = 14f;
		}
		else if (fontCategory.equalsIgnoreCase("Button"))
		{
			// Medium 14sp (all caps)
			mFontFamily = getDefaultFontFamily(FONT_VARIANT_MEDIUM);
			mFontSize = 14f;
		}
	}

	private static final String FONT_VARIANT_MEDIUM = "medium";
	private static final String FONT_VARIANT_THIN = "thin";
	private static final String FONT_VARIANT_LIGHT = "light";
	private static final String FONT_VARIANT_CONDENSED = "condensed";

	private static String getDefaultFontFamily(String variant)
	{
		if (!Strings.hasValue(variant) || variant.equalsIgnoreCase("regular"))
			return "sans-serif";

		// "Medium" was introduced in Android 5.0.
		if (variant.equalsIgnoreCase(FONT_VARIANT_MEDIUM))
			return "sans-serif-medium";

		if (variant.equalsIgnoreCase(FONT_VARIANT_THIN))
			return "sans-serif-thin";

		// "Light" & "Condensed" were introduced in Android 4.1
		if (variant.equalsIgnoreCase(FONT_VARIANT_LIGHT))
			return "sans-serif-light";
		if (variant.equalsIgnoreCase(FONT_VARIANT_CONDENSED))
			return "sans-serif-condensed";

		return "sans-serif";
	}

	private static Integer getFontWeightFromMetadata(String fontWeight)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			switch (fontWeight) {
				case "thin":
					return FontStyle.FONT_WEIGHT_THIN;
				case "extraLight":
					return FontStyle.FONT_WEIGHT_EXTRA_LIGHT;
				case "light":
					return FontStyle.FONT_WEIGHT_LIGHT;
				case "normal":
				default:
					return FontStyle.FONT_WEIGHT_NORMAL;
				case "medium":
					return FontStyle.FONT_WEIGHT_MEDIUM;
				case "semiBold":
					return FontStyle.FONT_WEIGHT_SEMI_BOLD;
				case "bold":
					return FontStyle.FONT_WEIGHT_BOLD;
				case "extraBold":
					return FontStyle.FONT_WEIGHT_EXTRA_BOLD;
				case "black":
					return FontStyle.FONT_WEIGHT_BLACK;
			}
		} else {
			switch (fontWeight) {
				case "thin":
					return 100;
				case "extraLight":
					return 200;
				case "light":
					return 300;
				case "normal":
				default:
					return 400;
				case "medium":
					return 500;
				case "semiBold":
					return 600;
				case "bold":
					return 700;
				case "extraBold":
					return 800;
				case "black":
					return 900;
			}
		}
	}

	private static boolean getFontItalicFromMetadata(String fontStyle)
	{
		return fontStyle.equalsIgnoreCase("italic");
	}

	private static Integer getFontWeightAndStyle(String fontWeight, String fontStyle)
	{
		if (fontWeight.equalsIgnoreCase("bold") && fontStyle.equalsIgnoreCase("italic"))
			return Typeface.BOLD_ITALIC;
		if (fontWeight.equalsIgnoreCase("bold"))
			return Typeface.BOLD;
		if (fontStyle.equalsIgnoreCase("italic"))
			return Typeface.ITALIC;

		return null;
	}

	public String getFontFamily()
	{
		return mFontFamily;
	}

	public Integer getFontSize()
	{
		// mFontSize is in dips, convert to pixels.
		if (mFontSize != null)
			return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mFontSize, Resources.getSystem().getDisplayMetrics());
		else
			return null;
	}

	public Integer getFontWeight() { return mFontWeight; }

	public boolean getFontItalic() { return mFontItalic; }

	public Integer getFontStyle()
	{
		return mFontStyle;
	}

	public boolean getStrikeThrough() { return mFontDecorationStrikeThrough; }
}

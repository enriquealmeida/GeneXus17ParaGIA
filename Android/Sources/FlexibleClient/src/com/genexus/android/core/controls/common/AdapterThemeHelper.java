package com.genexus.android.core.controls.common;

import android.view.View;
import android.widget.TextView;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.utils.BackgroundOptions;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.ThemeUtils;

/**
 * Helper class for control-based adapters (such as suggestion or combo drop-downs).
 */
class AdapterThemeHelper
{
	static void applyStyle(View view, ThemeClassDefinition themeClass, boolean setBackground)
	{
		if (themeClass != null)
		{
			TextView text = Cast.as(TextView.class, view.findViewById(android.R.id.text1));
			if (text != null)
			{
				ThemeUtils.setFontProperties(text, themeClass);
				if (setBackground)
					ThemeUtils.setBackgroundBorderProperties(text, themeClass, BackgroundOptions.DEFAULT);
			}
		}
	}
}

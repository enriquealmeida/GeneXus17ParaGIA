package com.genexus.android.core.controls.common;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Layout;
import android.widget.TextView;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;

/**
 * Helper class for TextViews.
 */
public class TextViewUtils
{
	public static void setText(@NonNull TextView view, @NonNull String text, @NonNull LayoutItemDefinition definition)
	{
		view.setText(toCharSequence(text, definition));
	}

	public static CharSequence toCharSequence(@NonNull String text, @NonNull LayoutItemDefinition itemDefinition)
	{
		if (itemDefinition.isHtml())
			return Services.Strings.fromHtml(text);
		else
			return text;
	}

	public static void configureBreakStrategy(AppCompatTextView textView)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			textView.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);

	}

}

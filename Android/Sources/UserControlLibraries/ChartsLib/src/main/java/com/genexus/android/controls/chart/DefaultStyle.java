package com.genexus.android.controls.chart;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;

import com.genexus.android.core.resources.BuiltInResources;

/**
 * Default style properties for charts.
 */
public class DefaultStyle
{
	public static final float TEXT_SIZE_BIG = 20f; // dp
	public static final float TEXT_SIZE_MEDIUM = 16f; // dp
	public static final float TEXT_SIZE_SMALL = 12f; // dp
	public static final float TEXT_SIZE_EXTRA_SMALL = 10f; // dp

	public static final int[] MATERIAL_COLORS = {
			Color.parseColor("#f44336"), // Red
			Color.parseColor("#2196F3"), // Blue
			Color.parseColor("#4CAF50"), // Green
			Color.parseColor("#FFC107"), // Orange
			Color.parseColor("#673AB7"), // Deep Purple
			Color.parseColor("#795548"), // Brown
			Color.parseColor("#00BCD4"), // Cyan
			Color.parseColor("#CDDC39"), // Lime
	};

	private static boolean sInitialized;
	public static int sForegroundColor;

	static void initialize(@NonNull Context context)
	{
		if (!sInitialized)
		{
			sInitialized = true;

			sForegroundColor = BuiltInResources.getResource(context, Color.WHITE, Color.BLACK, Color.BLACK);
		}
	}

	public static int getColor(@NonNull int[] colors, int index)
	{
		if (index < 0)
			throw new IllegalArgumentException("index must be >= 0");

		return colors[index % colors.length];
	}
}

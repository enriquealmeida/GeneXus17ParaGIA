package com.genexus.android.controls.chart.common;

import android.graphics.Typeface;

import com.genexus.android.controls.chart.DefaultStyle;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.ComponentBase;
import com.github.mikephil.charting.components.Legend;

/**
 * Chart utilities.
 */
public class Utils
{
	/**
	 * Sets up default text styles for chart components (description, legend, axes, c)
	 * @param chart Chart object
	 */
	public static void setDefaultStyle(Chart chart)
	{
		setTextStyle(chart.getDescription(), DefaultStyle.TEXT_SIZE_BIG);

		chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
		chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
		setTextStyle(chart.getLegend(), DefaultStyle.TEXT_SIZE_MEDIUM);

		// Axis values
		if (chart instanceof LineChart)
		{
			Typeface condensed = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
			chart.getXAxis().setTypeface(condensed);
			((LineChart)chart).getAxisLeft().setTypeface(condensed);
			((LineChart)chart).getAxisRight().setTypeface(condensed);

			setTextStyle(chart.getXAxis(), DefaultStyle.TEXT_SIZE_SMALL);
			setTextStyle(((LineChart)chart).getAxisLeft(), DefaultStyle.TEXT_SIZE_SMALL);
			setTextStyle(((LineChart)chart).getAxisRight(), DefaultStyle.TEXT_SIZE_SMALL);
		}
	}

	private static void setTextStyle(ComponentBase component, float textSize)
	{
		component.setTextSize(textSize);
		component.setTextColor(DefaultStyle.sForegroundColor);
	}
}

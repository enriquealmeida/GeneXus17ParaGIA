package com.genexus.android.controls.chart.pie;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Toast;

import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.controls.chart.common.Utils;
import com.genexus.android.controls.chart.DefaultStyle;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Pie Chart.
 */
@SuppressLint("ViewConstructor")
public class GxPieChart extends PieChart implements IGridView
{
	private GxPieChartDefinition mDefinition;
	private GridEventsListener mEventsListener;

	public GxPieChart(Context context, GxPieChartDefinition definition)
	{
		super(context);
		mDefinition = definition;
		setupChart();
	}

	private void setupChart()
	{
		Utils.setDefaultStyle(this);
		getDescription().setEnabled(false);
		setOnChartValueSelectedListener(mValueSelectedListener);
		setUsePercentValues(mDefinition.isShowInPercentage());

		CharSequence title = mDefinition.getTitle();
		if (!TextUtils.isEmpty(title))
		{
			setDrawHoleEnabled(true);
			setHoleColor(Color.TRANSPARENT);
			setCenterTextSize(DefaultStyle.TEXT_SIZE_MEDIUM);
			setCenterTextColor(DefaultStyle.sForegroundColor);
			setCenterText(title);
		}

		if (mDefinition.showValuesOutsideSlice())
		{
			setEntryLabelColor(DefaultStyle.sForegroundColor);
			setEntryLabelTextSize(DefaultStyle.TEXT_SIZE_MEDIUM);
		}
		else
		{
			setEntryLabelColor(Color.WHITE);
			setEntryLabelTextSize(DefaultStyle.TEXT_SIZE_SMALL);
		}
	}

	@Override
	public void addListener(GridEventsListener listener)
	{
		mEventsListener = listener;
	}

	@Override
	public void update(ViewData data)
	{
		GxPieChartData gxData = new GxPieChartData(mDefinition, data);

		ArrayList<PieEntry> values = new ArrayList<>();
		for (GxPieChartData.Item item : gxData.getItems())
			values.add(new PieEntry(item.value, item.category, item));

		PieDataSet chartDataSet = new PieDataSet(values, null);
		applyDataStyle(chartDataSet);

		setData(new PieData(chartDataSet));
		mDefaultValueFormatter.setup(gxData.getValueDecimals());

		// force the chart to re draw
		invalidate(); // refresh after setting data
	}

	private void applyDataStyle(PieDataSet dataSet)
	{
		if (mDefinition.isShowInPercentage())
			dataSet.setValueFormatter(mPercentValueFormatter);

		if (mDefinition.showValuesOutsideSlice())
		{
			dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
			dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
		}

		// Default styling, should use theme class.
		dataSet.setColors(DefaultStyle.MATERIAL_COLORS);
		dataSet.setValueTextColor(mDefinition.showValuesOutsideSlice() ? DefaultStyle.sForegroundColor : Color.WHITE);
		dataSet.setValueTextSize(DefaultStyle.TEXT_SIZE_SMALL); // dp
	}

	private final IValueFormatter mPercentValueFormatter = new IValueFormatter()
	{
		@Override
		public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
		{
			String formattedValue = getDefaultValueFormatter().getFormattedValue(value, entry, dataSetIndex, viewPortHandler);
			if (isUsePercentValuesEnabled())
				formattedValue += "%";

			return formattedValue;
		}
	};

	private final OnChartValueSelectedListener mValueSelectedListener = new OnChartValueSelectedListener()
	{
		@Override
		public void onValueSelected(Entry e, Highlight h)
		{
			if (Strings.hasValue(mDefinition.getDetailTextItem()))
			{
				GxPieChartData.Item valueItem = (GxPieChartData.Item)e.getData();
				if (valueItem != null && Strings.hasValue(valueItem.comment))
					Toast.makeText(getContext(), valueItem.comment, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onNothingSelected() { }
	};
}

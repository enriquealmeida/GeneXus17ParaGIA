package com.genexus.android.controls.chart.timeline;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.controls.chart.DefaultStyle;
import com.genexus.android.controls.chart.common.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

/**
 * Timeline Chart.
 */
@SuppressLint("ViewConstructor")
public class GxTimelineChart extends LineChart implements IGridView
{
	private GxTimelineChartDefinition mDefinition;
	private GridEventsListener mEventsListener;

	public GxTimelineChart(Context context, GxTimelineChartDefinition definition)
	{
		super(context);
		mDefinition = definition;
		setupChart();
	}

	private void setupChart()
	{
		// For now we assume that "one day" is the minimum unit.
		// setXAxisRenderer(new TimelineAxisRenderer());
		getXAxis().setGranularity(TimeUnit.DAYS.toMillis(1));
		getXAxis().setValueFormatter(mAxisDateFormatter);

		Utils.setDefaultStyle(this);
		setExtraOffsets(0f, 5f, 0f, 0f);
		setScaleEnabled(true);
		setDoubleTapToZoomEnabled(false);

		if (mDefinition.getXAxisPosition() == GxTimelineChartDefinition.X_AXIS_POSITION_TOP)
			getXAxis().setPosition(XAxis.XAxisPosition.TOP);

		// For y, we have getAxisLeft() and getAxisRight().
		if (mDefinition.getYAxisPosition() == GxTimelineChartDefinition.Y_AXIS_POSITION_RIGHT)
		{
			getAxisLeft().setEnabled(false);
			getAxisRight().setEnabled(true);
		}

		if (!TextUtils.isEmpty(mDefinition.getTitle()))
			getDescription().setText(mDefinition.getTitle().toString());
	}

	@Override
	public void addListener(GridEventsListener listener)
	{
		mEventsListener = listener;
	}

	@Override
	public void update(ViewData data)
	{
		GxTimelineChartData gxData = new GxTimelineChartData(mDefinition, data);

		ArrayList<ILineDataSet> dataSets = new ArrayList<>();

		for (GxTimelineChartData.Series series : gxData.getSeries())
		{
			ArrayList<Entry> dataSetValues = new ArrayList<>();
			for (GxTimelineChartData.Value value : series.getValues())
				dataSetValues.add(new Entry(value.date.getTime(), value.y, value));

			LineDataSet dataSet = new LineDataSet(dataSetValues, series.getLabel());
			applyDataStyle(dataSet, dataSets.size());
			dataSet.setHighlightEnabled(true);
			dataSets.add(dataSet);
		}

		setData(new LineData(dataSets));

		// force the chart to re draw
		invalidate(); // refresh after setting data
	}

	private void applyDataStyle(LineDataSet dataSet, int index)
	{
		dataSet.setColor(DefaultStyle.getColor(DefaultStyle.MATERIAL_COLORS, index));
		dataSet.setCircleColor(DefaultStyle.getColor(DefaultStyle.MATERIAL_COLORS, index));
		dataSet.setCircleRadius(3f); //dp
		dataSet.setDrawCircleHole(false);
		dataSet.setLineWidth(3f); // dp
		dataSet.setDrawValues(false);
		dataSet.setValueTextSize(DefaultStyle.TEXT_SIZE_EXTRA_SMALL); // dp
	}

	/*
	private class TimelineAxisRenderer extends XAxisRenderer
	{
		private TimelineAxisRenderer()
		{
			super(getViewPortHandler(), getXAxis(), getRendererXAxis().getTransformer());
		}

		@Override
		protected void computeAxisValues(float min, float max)
		{
			// TODO: We could implement a sort of "semantic zoom" here, showing different
			// TODO: time period units according to the total period displayed (ie. years, months, days).
			super.computeAxisValues(min, max);
		}
	}
	*/

	private final IAxisValueFormatter mAxisDateFormatter = new IAxisValueFormatter()
	{
		@Override
		public String getFormattedValue(float value, AxisBase axis)
		{
			return DateUtils.formatDateTime(getContext(), (long)value, DateUtils.FORMAT_NUMERIC_DATE);
		}
	};
}

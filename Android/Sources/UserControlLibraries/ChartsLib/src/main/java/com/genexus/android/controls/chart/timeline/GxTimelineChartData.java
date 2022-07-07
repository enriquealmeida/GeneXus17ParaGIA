package com.genexus.android.controls.chart.timeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controllers.ViewData;

/**
 * Data model for a Timeline chart.
 */
class GxTimelineChartData
{
	private final GxTimelineChartDefinition mDefinition;

	private ArrayList<Series> mSeries;

	public static class Series
	{
		private final GxTimelineChartDefinition.Series mDefinition;
		private final List<Value> mValues;

		private Series(GxTimelineChartDefinition.Series definition)
		{
			mDefinition = definition;
			mValues = new ArrayList<>();
		}

		public String getLabel()
		{
			return mDefinition.label;
		}

		public List<Value> getValues()
		{
			return mValues;
		}
	}

	public static class Value
	{
		public Date date;
		public float y;
	}

	public GxTimelineChartData(GxTimelineChartDefinition definition, ViewData data)
	{
		mDefinition = definition;
		loadFrom(data.getEntities());
	}

	private void loadFrom(EntityList entities)
	{
		// Initialize series.
		mSeries = new ArrayList<>();
		for (GxTimelineChartDefinition.Series seriesDefinition : mDefinition.getSeries())
			mSeries.add(new Series(seriesDefinition));

		// Read x-values (dates) and y-values (floats) for each record of all series.
		for (Entity entity : entities)
		{
			Date itemDate = getXValue(entity);
			if (itemDate != null)
			{
				// Read values for each one of the series.
				for (Series series : mSeries)
				{
					Value value = new Value();
					value.date = itemDate;
					value.y = getYValue(series.mDefinition, entity);

					series.getValues().add(value);
				}
			}
		}
	}

	private Date getXValue(Entity item)
	{
		String strValue = item.optStringProperty(mDefinition.getDateAttribute());
		return Services.Strings.getDate(strValue);
	}

	private float getYValue(GxTimelineChartDefinition.Series series, Entity item)
	{
		String strValue = item.optStringProperty(series.valueAttribute);
		return Services.Strings.tryParseFloat(strValue, 0f);
	}

	public List<Series> getSeries()
	{
		return mSeries;
	}
}

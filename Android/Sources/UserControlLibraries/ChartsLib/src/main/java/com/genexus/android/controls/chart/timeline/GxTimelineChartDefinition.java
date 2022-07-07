package com.genexus.android.controls.chart.timeline;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.ControlPropertiesDefinition;

public class GxTimelineChartDefinition extends ControlPropertiesDefinition
{
	public static final String CHART_TYPE = "timeline";

	private static final String VALUE_NONE = "(none)";

	private String mDateAttribute;
	private ArrayList<Series> mSeries;
	private CharSequence mTitle;
	private int mXAxisPosition;
	private int mYAxisPosition;
	private ArrayList<TimePeriod> mTimePeriods;

	public static final int X_AXIS_POSITION_BOTTOM = 0;
	public static final int X_AXIS_POSITION_TOP = 1;
	public static final int Y_AXIS_POSITION_LEFT = 0;
	public static final int Y_AXIS_POSITION_RIGHT = 1;

	public static class Series
	{
		public String label;
		public String valueAttribute;
	}

	public static class TimePeriod
	{
		public String description;
		public int date;
		public int value;
	}

	// TimeLine period lengths
	private static final String DATE = "d";
	private static final String WEEK = "w";
	private static final String MONTH = "m";
	private static final String YEAR = "y";

	//Charts TimePeriod
	private CharSequence[] mTimePeriodCharSequence = { "1d", "5d", "1m", "3m", "6m", "1y", "Max"};

	public GxTimelineChartDefinition(LayoutItemDefinition definition)
	{
		super(definition);
		ControlInfo controlInfo = definition.getControlInfo();

		mDateAttribute = readDataExpression("@SDChartsTimeAttribute", "@SDChartsTimeField");

		mSeries = new ArrayList<>();

		String[] seriesLabels = parseCollection(controlInfo.optStringProperty("@SDChartsSeriesLabelCollection"));
		String[] valueAttributes = parseCollection(controlInfo.optStringProperty("@SDChartsSeriesAttributeCollection"));
		String[] valueFields = parseCollection(controlInfo.optStringProperty("@SDChartsSeriesFieldCollection"));

		if (valueFields.length != 0 && valueAttributes.length == 1)
		{
			// For bound SDT collections.
			for (int i = 0; i < valueAttributes.length; i++)
				valueFields[i] = buildDataExpression(valueAttributes[0], valueFields[i]);

			valueAttributes = valueFields;
		}

		for (int i = 0; i < valueAttributes.length; i++)
		{
			Series series = new Series();
			series.valueAttribute = valueAttributes[i];
			series.label = (i < seriesLabels.length ? seriesLabels[i] : "");
			mSeries.add(series);
		}

		mTitle = controlInfo.optStringProperty("@SDChartsTitle");
		mXAxisPosition = Services.Strings.parseIntEnum(controlInfo.optStringProperty("@SDChartsXAxisPosition"), X_AXIS_POSITION_BOTTOM, "Bottom", "Top");
		mYAxisPosition = Services.Strings.parseIntEnum(controlInfo.optStringProperty("@SDChartsYAxisPosition"), Y_AXIS_POSITION_LEFT, "Left", "Right");
	}

	private static String[] parseCollection(String str)
	{
		if (Strings.hasValue(str) && !Strings.areEqual(str, VALUE_NONE, true))
		{
			String[] parts = str.split(Strings.COMMA);
			for (int i = 0; i < parts.length; i++)
				parts[i] = parts[i].trim();

			return parts;
		}
		else
			return new String[0];
	}

	public String getDateAttribute()
	{
		return mDateAttribute;
	}

	public List<Series> getSeries()
	{
		return mSeries;
	}

	public CharSequence getTitle()
	{
		return mTitle;
	}

	public int getXAxisPosition()
	{
		return mXAxisPosition;
	}

	public int getYAxisPosition()
	{
		return mYAxisPosition;
	}

	/*
	public List<TimePeriod> getTimePeriods()
	{
		return mTimePeriods;
	}

	private void obtainTimePeriodCollection(ControlInfo info) {
		String strTimePeriod = info.optStringProperty("@SDChartsTimePeriodCollection");

		//Add period Max
		if (!strTimePeriod.equalsIgnoreCase("max"))
			strTimePeriod = strTimePeriod.concat(",Max");

		if (strTimePeriod.length() !=0)
			mTimePeriodCharSequence = strTimePeriod.replace(Strings.SPACE, Strings.EMPTY).split(Strings.COMMA);

		mTimePeriods = new ArrayList<>();
		try {
			for (int i = 0; i < mTimePeriodCharSequence.length; i++)
			{
				TimePeriod period = obtainPeriodTime(i);
				mTimePeriods.add(period);
			}
		}
		catch (Exception e) { }
	}

	private TimePeriod obtainPeriodTime(int position)
	{
		TimePeriod period = new TimePeriod();
		period.description = mTimePeriodCharSequence[position].toString();
		if (!period.description.equalsIgnoreCase("Max"))
		{
			String strDate = period.description.substring(period.description.length()-1, period.description.length());
			if (strDate.equalsIgnoreCase(DATE))
				period.date = Calendar.DATE;
			if (strDate.equalsIgnoreCase(WEEK))
				period.date = Calendar.WEEK_OF_YEAR;
			if (strDate.equalsIgnoreCase(MONTH))
				period.date = Calendar.MONTH;
			if (strDate.equalsIgnoreCase(YEAR))
				period.date = Calendar.YEAR;
			period.value = Integer.parseInt(period.description.substring(0, period.description.length()-1));
		}
		return period;
	}
	*/
}

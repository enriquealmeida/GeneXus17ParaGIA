package com.genexus.android.controls.chart;

import android.content.Context;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.controls.chart.pie.GxPieChart;
import com.genexus.android.controls.chart.pie.GxPieChartDefinition;
import com.genexus.android.controls.chart.timeline.GxTimelineChart;
import com.genexus.android.controls.chart.timeline.GxTimelineChartDefinition;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.usercontrols.IControlFactory;
import com.genexus.android.core.usercontrols.IGxUserControl;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;

/**
 * Charts Module.
 * Supports creation of different Chart classes according to the "chart type" property value.
 */
public class ChartsModule implements GenexusModule
{
	@Override
	public void initialize(Context context)
	{
		UcFactory.addControl(new UserControlDefinition(ChartFactory.USER_CONTROL_NAME, new ChartFactory()));
	}

	private static class ChartFactory implements IControlFactory
	{
		static final String USER_CONTROL_NAME = "SD Charts";

		@Override
		public IGxUserControl create(Context context, Coordinator coordinator, LayoutItemDefinition definition)
		{
			// This needs an Activity context (to get theme), so it cannot be done in initialize().
			DefaultStyle.initialize(context);

			if (definition.getControlInfo() != null)
			{
				String chartType = definition.getControlInfo().optStringProperty("@SDChartsChartType");

				if (GxPieChartDefinition.CHART_TYPE.equalsIgnoreCase(chartType))
					return new GxPieChart(context, new GxPieChartDefinition(definition));
				else if (GxTimelineChartDefinition.CHART_TYPE.equalsIgnoreCase(chartType))
					return new GxTimelineChart(context, new GxTimelineChartDefinition(definition));
				else
					throw new IllegalArgumentException("Unknown chart type: " + chartType);
			}

			return null;
		}
	}
}

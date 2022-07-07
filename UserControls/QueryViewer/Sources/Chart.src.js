var HCChart;
var _highChartsDrawPointsWrapped;
var DEFAULTCHARTSPACING = 10;

qv.chart = (function () {

	function IsTimelineChart(qViewer) {
		return qViewer.RealChartType == QueryViewerChartType.Timeline || qViewer.RealChartType == QueryViewerChartType.SmoothTimeline || qViewer.RealChartType == QueryViewerChartType.StepTimeline;
	}

	function IsDatetimeXAxis(qViewer) {
		return IsTimelineChart(qViewer) || (qViewer.RealChartType == QueryViewerChartType.Sparkline );
	}

	function IsStackedChart(qViewer) {
		return qViewer.RealChartType == QueryViewerChartType.StackedColumn || qViewer.RealChartType == QueryViewerChartType.StackedColumn3D || qViewer.RealChartType == QueryViewerChartType.StackedColumn100 ||  qViewer.RealChartType == QueryViewerChartType.StackedBar ||  qViewer.RealChartType == QueryViewerChartType.StackedBar100 ||  qViewer.RealChartType == QueryViewerChartType.StackedArea ||  qViewer.RealChartType == QueryViewerChartType.StackedArea100 ||  qViewer.RealChartType == QueryViewerChartType.StackedLine ||  qViewer.RealChartType == QueryViewerChartType.StackedLine100;
	}

	function IsCircularChart(qViewer) {
		return qViewer.RealChartType == QueryViewerChartType.Pie || qViewer.RealChartType == QueryViewerChartType.Pie3D || qViewer.RealChartType == QueryViewerChartType.Doughnut || qViewer.RealChartType == QueryViewerChartType.Doughnut3D;
	}

	function IsFunnelChart(qViewer) {
		return qViewer.RealChartType == QueryViewerChartType.Funnel || qViewer.RealChartType == QueryViewerChartType.Pyramid;
	}

	function IsPolarChart(qViewer) {
		return qViewer.RealChartType == QueryViewerChartType.Radar || qViewer.RealChartType == QueryViewerChartType.FilledRadar || qViewer.RealChartType == QueryViewerChartType.PolarArea;
	}

	function IsSingleSerieChart(qViewer) {
		return IsCircularChart(qViewer) || IsFunnelChart(qViewer);
	}

	function IsCombinationChart(qViewer) {
		return (qViewer.RealChartType == QueryViewerChartType.ColumnLine || qViewer.RealChartType == QueryViewerChartType.Column3DLine) && qViewer.Chart.Series.DataFields.length > 1 ;
	}

	function IsGaugeChart(qViewer) {
		return qViewer.RealChartType == QueryViewerChartType.CircularGauge || qViewer.RealChartType == QueryViewerChartType.LinearGauge;
	}

	function IsAreaChart(qViewer) {
		return qViewer.RealChartType == QueryViewerChartType.Area || qViewer.RealChartType == QueryViewerChartType.StackedArea || qViewer.RealChartType == QueryViewerChartType.StackedArea100 || qViewer.RealChartType == QueryViewerChartType.SmoothArea  || qViewer.RealChartType == QueryViewerChartType.StepArea;
	}

	function IsLineChart(qViewer) {
		return qViewer.RealChartType == QueryViewerChartType.Line || qViewer.RealChartType == QueryViewerChartType.StackedLine || qViewer.RealChartType == QueryViewerChartType.StackedLine100 || qViewer.RealChartType == QueryViewerChartType.SmoothLine  || qViewer.RealChartType == QueryViewerChartType.StepLine || qViewer.RealChartType == QueryViewerChartType.Sparkline || IsTimelineChart(qViewer);
	}

	function IsBarChart(qViewer) {
		return qViewer.RealChartType == QueryViewerChartType.Bar || qViewer.RealChartType == QueryViewerChartType.StackedBar || qViewer.RealChartType == QueryViewerChartType.StackedBar100;
	}

	function HasYAxis(qViewer) {
		return !IsCircularChart(qViewer) && !IsFunnelChart(qViewer) && !IsGaugeChart(qViewer);
	}

	function IsSplittedChart(qViewer) {
		if (IsStackedChart(qViewer))
			return false;						// Para las gráficas Stacked no tiene sentido separar en varias gráficas pues dejan de apilarse las series
		else
			return (qViewer.PlotSeries == QueryViewerPlotSeries.InSeparateCharts || IsSingleSerieChart(qViewer)) && qViewer.Chart.Series.DataFields.length > 1;	// Fuerzo gráficas separadas para este tipo de gráficas porque sino no se pueden dibujar
	}

	function NumberOfCharts(qViewer) {
		return IsSplittedChart(qViewer) ? qViewer.Chart.Series.DataFields.length : 1;
	}

	function splitChartContainer(qViewer) {
		var viewerId = qViewer.userControlId();
		var container = qv.util.dom.getEmptyContainer(qViewer);
		if ((IsTimelineChart(qViewer) || IsSplittedChart(qViewer)) && container.offsetHeight == 0) {
			container.style.height = "400px";		// Necesito tener un alto distinto de cero antes de dibujar las gráficas, si no todas quedan con alto 400px
		}
		if (IsTimelineChart(qViewer)) {
			qv.util.dom.createDiv(container, optionsId(viewerId), "QVTimelineHeaderContainer", "", { width: "100%", height: TimelineHeaderHeight + "px" }, "");
			var centerDiv = qv.util.dom.createDiv(container, centerId(viewerId), "", "", { width: "100%", height: "calc(100% - " + TimelineHeaderHeight + "px - " + TimelineFooterHeight + "px)" }, "");
			qv.util.dom.createDiv(container, footerId(viewerId), "gx-qv-footer", "", { width: "100%", height: TimelineFooterHeight + "px" }, "");
			container.style.padding = DEFAULTCHARTSPACING + "px";
			container = centerDiv;
		}
		if (IsSplittedChart(qViewer)) {
			var totDIVs = qViewer.Chart.Series.DataFields.length;
			var divHeight = parseInt(100 / totDIVs);
			var percentLeft = 100 % totDIVs;
			var baseId;
			if (IsTimelineChart(qViewer))
				baseId = centerId(viewerId);
			else
				baseId = viewerId;
			for (var i = 0; i < totDIVs; i++)
				qv.util.dom.createDiv(container, baseId + "_chart" + i.toString(), "", "", { width: "100%", height: (divHeight + (i < percentLeft ? 1 : 0)).toString() + "%" }, "");
		}
	}

	function getHoverPoints(qViewer, index) {
		var points = [];
		for (var i = 0; i < qViewer.Charts.length; i++) {
			for (var j = 0; j < qViewer.Charts[i].series.length; j++) {
				var point = qViewer.Charts[i].series[j].data[index];
				points.push(point);
			}
		}
		return points;
	}

	function syncPoints(qViewer, container, index, visible, highlightIfVisible) {
		for (var i = 0; i < qViewer.Charts.length; i++) {
			if (container.id != qViewer.Charts[i].container.id) {
				for (var j = 0; j < qViewer.Charts[i].series.length; j++) {
					var point = qViewer.Charts[i].series[j].data[index];
					if (visible) {
						if (highlightIfVisible) point.setState('hover');
						qViewer.Charts[i].tooltip.hide(0);
					}
					else {
						point.setState();
					}
				}
			}
		}
	}

	function getChartType_forHightCharts(chartType) {
		switch (chartType) {
			case QueryViewerChartType.Column:
			case QueryViewerChartType.Column3D:
			case QueryViewerChartType.StackedColumn:
			case QueryViewerChartType.StackedColumn3D:
			case QueryViewerChartType.StackedColumn100:
			case QueryViewerChartType.PolarArea:
				return "column";
			case QueryViewerChartType.Bar:
			case QueryViewerChartType.StackedBar:
			case QueryViewerChartType.StackedBar100:
			case QueryViewerChartType.LinearGauge:
				return "bar";
			case QueryViewerChartType.Area:
			case QueryViewerChartType.StackedArea:
			case QueryViewerChartType.StackedArea100:
			case QueryViewerChartType.FilledRadar:
			case QueryViewerChartType.StepArea:
				return "area";
			case QueryViewerChartType.SmoothArea:
				return "areaspline";
			case QueryViewerChartType.Line:
			case QueryViewerChartType.StackedLine:
			case QueryViewerChartType.StackedLine100:
			case QueryViewerChartType.Radar:
			case QueryViewerChartType.Timeline:
			case QueryViewerChartType.StepTimeline:
			case QueryViewerChartType.Sparkline:
			case QueryViewerChartType.StepLine:
				return "line";
			case QueryViewerChartType.SmoothLine:
			case QueryViewerChartType.SmoothTimeline:
				return "spline";
			case QueryViewerChartType.ColumnLine:
			case QueryViewerChartType.Column3DLine:
				return "column";
			case QueryViewerChartType.Pie:
			case QueryViewerChartType.Pie3D:
			case QueryViewerChartType.Doughnut:
			case QueryViewerChartType.Doughnut3D:
				return "pie";
			case QueryViewerChartType.Funnel:
				return "funnel";
			case QueryViewerChartType.Pyramid:
				return "pyramid";
			case QueryViewerChartType.CircularGauge:
				return "solidgauge";
			default:
				return "line";
		}
	}

	function SetHighchartsOptions() {
		Highcharts.setOptions({
			lang: {
				months: [gx.getMessage("GXPL_QViewerJanuary"), gx.getMessage("GXPL_QViewerFebruary"), gx.getMessage("GXPL_QViewerMarch"), gx.getMessage("GXPL_QViewerApril"), gx.getMessage("GXPL_QViewerMay"), gx.getMessage("GXPL_QViewerJune"), gx.getMessage("GXPL_QViewerJuly"), gx.getMessage("GXPL_QViewerAugust"), gx.getMessage("GXPL_QViewerSeptember"), gx.getMessage("GXPL_QViewerOctober"), gx.getMessage("GXPL_QViewerNovember"), gx.getMessage("GXPL_QViewerDecember")],
				weekdays: [gx.getMessage("GXPL_QViewerSunday"), gx.getMessage("GXPL_QViewerMonday"), gx.getMessage("GXPL_QViewerTuesday"), gx.getMessage("GXPL_QViewerWednesday"), gx.getMessage("GXPL_QViewerThursday"), gx.getMessage("GXPL_QViewerFriday"), gx.getMessage("GXPL_QViewerSaturday")],
				shortMonths: [gx.getMessage("GXPL_QViewerJanuary").substring(0, 3), gx.getMessage("GXPL_QViewerFebruary").substring(0, 3), gx.getMessage("GXPL_QViewerMarch").substring(0, 3), gx.getMessage("GXPL_QViewerApril").substring(0, 3), gx.getMessage("GXPL_QViewerMay").substring(0, 3), gx.getMessage("GXPL_QViewerJune").substring(0, 3), gx.getMessage("GXPL_QViewerJuly").substring(0, 3), gx.getMessage("GXPL_QViewerAugust").substring(0, 3), gx.getMessage("GXPL_QViewerSeptember").substring(0, 3), gx.getMessage("GXPL_QViewerOctober").substring(0, 3), gx.getMessage("GXPL_QViewerNovember").substring(0, 3), gx.getMessage("GXPL_QViewerDecember").substring(0, 3)],
				numericSymbols: null
			}
		});
	}

	function SetItemClickData(eventData, qViewer, name, type, value, selected, row) {

		function GetContextElement(axisOrDatum, value) {
			var contextElement = {};
			contextElement.Name = axisOrDatum.Name;
			var pictureProperties = qv.util.GetPictureProperties(axisOrDatum.DataType, axisOrDatum.Picture);
			var formattedValue = qv.util.ApplyPicture(value, axisOrDatum.Picture, axisOrDatum.DataType, pictureProperties);
			contextElement.Values = [formattedValue];
			return contextElement;
		}

		eventData.Name = name;
		eventData.Type = type;
		eventData.Axis = "";
		eventData.Value = value;
		eventData.Selected = selected;
		eventData.Context = [];
		for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
			var axis = qViewer.Metadata.Axes[i];
			if (!axis.IsComponent) {
				var contextElement = GetContextElement(axis, row[axis.DataField]);
				eventData.Context.push(contextElement);
			}
		}
		for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
			var datum = qViewer.Metadata.Data[i];
			if (!datum.IsComponent) {
				var contextElement = GetContextElement(datum, row[datum.DataField]);
				eventData.Context.push(contextElement);
			}
		}
		eventData.Filters = [];
	}

	function ToggleChartsSelection(charts, index) {
		var selectedResult = false;
		for (var i = 0; i < charts.length; i++) {
			var chart = charts[i];
			for (var j = 0; j < chart.series.length; j++) {
				var point = chart.series[j].points[index];
				if (point.selected) {
					point.select();
					break;
				}
				else {
					point.select(true, j > 0);
					selectedResult = true;
				}
			}
		}
		return selectedResult;
	}

	function GetRowsToSelect(qViewer, selection) {
		var rowsToSelect = [];
		for (var i = 0; i < qViewer.Data.Rows.length; i++) {
			var row = qViewer.Data.Rows[i];
			var selected = true;
			for (j = 0; j < selection.length; j++) {
				var selectionItem = selection[j];
				if (row[selectionItem.DataField])
					if (qv.util.trim(row[selectionItem.DataField]) != qv.util.trim(selectionItem.Value)) {
						selected = false;
						break;
					}
			}
			if (selected)
				rowsToSelect.push(i);
		}
		return rowsToSelect;
	}

	function SelectChartsPoints(charts, indexes) {
		for (var i = 0; i < charts.length; i++) {
			var chart = charts[i];
			var accumulate = false;
			for (var j = 0; j < indexes.length; j++) {
				var index = indexes[j];
				for (var k = 0; k < chart.series.length; k++) {
					var point = chart.series[k].points[index];
					point.select(true, accumulate);
					if (!accumulate)
						accumulate = true;
				}
			}
		}
	}

	function DeselectChartsPoints(charts) {
		for (var i = 0; i < charts.length; i++) {
			var chart = charts[i];
			var points = chart.getSelectedPoints();
			if (points.length > 0)
				points[0].select(false, false);
		}
	}

	function onHighchartsItemClickEventHandler(event) {

		var qViewer = qv.collection[this.chart.options.qv.viewerId];
		var selected = false;
		if (qViewer.SelectionAllowed()) {
			if (qViewer.SelectionType == "Point") {
				event.point.select();
				selected = event.point.selected;
				if (qViewer.PlotSeries == QueryViewerPlotSeries.InSeparateCharts && selected) {
					for (var i = 0; i < qViewer.Charts.length; i++) {
						var chart = qViewer.Charts[i];
						var point = chart.series[0].points[event.point.index];
						if (point != event.point)
							point.select(false);
					}
				}
			}
			else
				selected = ToggleChartsSelection(qViewer.Charts, event.point.index);
		}
		if (qViewer.ItemClick && qViewer.Metadata.Data[event.point.series.index].RaiseItemClick) {
			var serie = qViewer.Chart.Series.ByName[this.name];
			var formattedValue = qv.util.formatNumber(event.point.y, serie.NumberFormat.DecimalPrecision, serie.Picture, false);
			var row = qViewer.Data.Rows[event.point.index];
			SetItemClickData(qViewer.ItemClickData, qViewer, serie.FieldName, QueryViewerElementType.Datum, formattedValue, selected, row);
			qViewer.ItemClick();
		}
	}

	function onHighchartsXAxisClickEventHandler(event, tickInd, tick, chart, raiseItemClick, selectionAllowed) {
		var qViewer = qv.collection[chart.options.qv.viewerId];
		var selected = false;
		if (selectionAllowed && !IsTimelineChart(qViewer))
			selected = ToggleChartsSelection(qViewer.Charts, tick.pos);
		if (raiseItemClick) {
			var name;
			if (qViewer.Chart.Categories.DataFields.length == 1) {
				var dataField = qViewer.Chart.Categories.DataFields[0];
				var axis = qv.util.GetAxisByDataField(qViewer, dataField);
				name = axis.Name;
			}
			else
				name = "";
			var row = qViewer.Data.Rows[tickInd];
			SetItemClickData(qViewer.ItemClickData, qViewer, name, QueryViewerElementType.Axis, tick.label.textStr, selected, row);
			qViewer.ItemClick();
		}
	}

	function GetBoldText(text, color) {
		if (color == "")
			return qv.util.dom.createSpan(null, "", "", "", { fontWeight: "bold" }, null, text).outerHTML;
		else
			return qv.util.dom.createSpan(null, "", "", "", { fontWeight: "bold", color: color }, null, text).outerHTML;
	}

	function CircularGaugeTooltipAndDataLabelFormatter(evArg, qViewer) {
		var serie = qViewer.Chart.Series.ByName[evArg.series.name];
		var chartSize = Math.min(qViewer.getContainerControl().offsetHeight, qViewer.getContainerControl().offsetWidth) / NumberOfCharts(qViewer);
		var fontSize = chartSize / 13;
		return qv.util.dom.createSpan(null, "", "", "", { color: GetColorStringFromHighchartsObject(qViewer, evArg), fontSize: fontSize + "px" }, null, qv.util.formatNumber(evArg.point.y, 2, "ZZZZZZZZZZZZZZ9.99", true) + "%").outerHTML;
	}

	function DataLabelFormatter(evArg, qViewer) {
		var chartSerie = qViewer.Chart.Series.ByName[evArg.series.name];
		var multiplier = qViewer.RealChartType == QueryViewerChartType.LinearGauge ? chartSerie.TargetValue / 100 : 1;
		var value = qv.util.formatNumber(evArg.point.y * multiplier, chartSerie.NumberFormat.DecimalPrecision, chartSerie.Picture, false);
		var chartType = evArg.series.chart.options.chart.type;
		var label = value;
		return label;
	}

	function TooltipFormatter(evArg, chartSeries, sharedTooltip)
	{
		var qViewer;
		if (!sharedTooltip) {
			qViewer = qv.collection[evArg.series.chart.options.qv.viewerId];
			var serie = chartSeries[evArg.series.name];
			var picture = IsGaugeChart(qViewer) ? "ZZZZZZZZZZZZZZ9.99" : serie.Picture;
			var decimalPrecision = IsGaugeChart(qViewer) ? 2 : serie.NumberFormat.DecimalPrecision;
			var removeTrailingZeroes = IsGaugeChart(qViewer);
			return '<b>' + (evArg.point.id != "" ? evArg.point.id : evArg.series.name) + '</b>: ' + qv.util.formatNumber(evArg.point.y, decimalPrecision, picture, removeTrailingZeroes) + (IsGaugeChart(qViewer) ? "%" : "");
		}
		else {
			var firstPoint;
			var index;
			if (!evArg.points) {
				firstPoint = evArg.point;
				index = firstPoint.index;
			}
			else {
				firstPoint = evArg.points[0];
				index = firstPoint.point.index;
			}
			qViewer = qv.collection[firstPoint.series.chart.options.qv.viewerId];
			var isSingleSerieChart = IsSingleSerieChart(qViewer);
			var hoverPoints = getHoverPoints(qViewer, index);
			var x = (!evArg.key ? (!evArg.x ? "" : evArg.x) : evArg.key);
			var hasTitle = x != "" && qViewer.RealChartType != QueryViewerChartType.Sparkline;	// en Sparkline la x no viene formateada
			var res = "";
			if (hasTitle)
				res += GetBoldText(x, isSingleSerieChart ? GetColorStringFromHighchartsObject(qViewer, evArg) : "");
			for (var i = 0; i < hoverPoints.length; i++) {
				var point = hoverPoints[i];
				var serie = chartSeries[point.series.name];
				res += (hasTitle || i > 0 ? '<br/>' : '') + GetBoldText(point.series.name + ': ', isSingleSerieChart ? "" : GetColorStringFromHighchartsObject(qViewer, point));
				res += qv.util.formatNumber(point.y, serie.NumberFormat.DecimalPrecision, serie.Picture, false);
			}
			return res;
		}
	}

	function PieTooltipFormatter(evArg, chartSeries, sharedTooltip) {
		if (!sharedTooltip) {
			var percentage = Math.round(evArg.point.percentage * 100) / 100;
			return '<b>' + (evArg.point.name != "" ? evArg.point.name : evArg.point.series.name) + '</b>: ' + percentage + '%';
		}
		else {
			var qViewer = qv.collection[evArg.point.series.chart.options.qv.viewerId];
			var isSingleSerieChart = IsSingleSerieChart(qViewer);
			var hoverPoints = getHoverPoints(qViewer, evArg.point.index);
			var x = hoverPoints.length > 0 ? hoverPoints[0].id : "";
			var hasTitle = x != "";
			var res = "";
			if (hasTitle)
				res += GetBoldText(x, isSingleSerieChart ? GetColorStringFromHighchartsObject(qViewer, evArg) : "");
			for (var i = 0; i < hoverPoints.length; i++) {
				var point = hoverPoints[i];
				var serie = chartSeries[point.series.name];
				res += (hasTitle || i > 0 ? '<br/>' : '') + GetBoldText(point.series.name + ': ', isSingleSerieChart ? "" : GetColorStringFromHighchartsObject(qViewer, point.series));
				var percentage = Math.round(point.percentage * 100) / 100;
				res += percentage + '%';;
			}
			return res;
		}
	}

	function Stacked100TooltipFormatter(evArg) {
		var percentage = Math.round(evArg.point.percentage * 100) / 100;
		return '<b>' + (evArg.point.id != "" ? evArg.point.id : evArg.series.name) + '</b>: ' + percentage + '%';
	}

	function DateTimeTooltipFormatter(evArg, chartSeries) {

		function GetDuration(point) {

			var value = point.y;
			var points = point.series.data;
			var index = point.index;
			var duration = "";
			var max = index;
			for (var i = index + 1; i < points.length; i++) {
				if (points[i].y != value)
					break;
				max = i;
			}
			if (max < points.length - 1)
				max++;
			var min = index;
			for (var i = index - 1; i >= 0; i--) {
				if (points[i].y != value)
					break;
				min = i;
			}
			var seconds = (points[max].x - points[min].x) / 1000;
			duration = " (" + gx.getMessage("GXPL_QViewerDuration") + ": " + qv.util.seconsdToText(seconds) + ")";
			return duration;
		}

		var hoverPoints;
		var qViewer = qv.collection[evArg.points[0].series.chart.options.qv.viewerId];
		if (IsSplittedChart(qViewer))
			hoverPoints = getHoverPoints(qViewer, evArg.points[0].point.index);
		else {
			hoverPoints = [];
			jQuery.each(evArg.points, function (index, point) {
				hoverPoints.push(point.point);
			});
		}
		// Agrupa la lista de puntos por name
		var points_by_name = {};
		var compare = false;
		for (var i = 0; i < hoverPoints.length; i++) {
			var name = hoverPoints[i].series.name;
			if (points_by_name[name] == undefined) {
				points_by_name[name] = [];
			}
			points_by_name[name].push(hoverPoints[i]);
			if (points_by_name[name].length > 1) compare = true;
		}
		var res = '';
		var currentTotal = 0;
		var previousTotal = 0;
		var oldUtc;
		for (var key_name in points_by_name) {
			var serie = chartSeries[key_name];
			var points = points_by_name[key_name];
			var color = GetColorStringFromHighchartsObject(qViewer, points[0]);
			if (compare)
				res += qv.util.dom.createSpan(null, "", "", "", { fontWeight: "bold", color: color }, null, key_name).outerHTML + "<br/>";
			for (var ind = 0; points[ind] != undefined; ind++) {
				var p = points[ind];
				var utc = parseInt(p.real_x ? p.real_x : p.x);
				if (p.real_x) {
					previousTotal += p.y;
				} else {
					currentTotal += p.y;
				}
				if (hoverPoints.length > 0 && hoverPoints[0].series.chart.options.qv.comparing) { //Si esta habilitada la comparación (la fecha es diferente con el período anterior)
					//Se muestra toda la información de cada serie con el color correspondiente
					res += p.name + ": " + qv.util.formatNumber(p.y, serie.NumberFormat.DecimalPrecision, serie.Picture, false) + "<br/>";
				}
				else {
					//Se muestra el nombre de cada con el color correspondiente. La fecha que es común va arriba en negro. El valor de cada serie en bold (sin color)							
					if (oldUtc != utc) {
						res += p.name + '<br/>';
						oldUtc = utc;
					}
					var duration = qViewer.RealChartType == QueryViewerChartType.StepTimeline ? GetDuration(p) : "";
					var keySpan = qv.util.dom.createSpan(null, "", "", "", { color: color }, null, key_name + ": ").outerHTML;
					var valueSpan = qv.util.dom.createSpan(null, "", "", "", { fontWeight: "bold" }, null, qv.util.formatNumber(p.y, serie.NumberFormat.DecimalPrecision, serie.Picture, false)).outerHTML;
					res += keySpan + valueSpan + duration + '<br/>';
				}
			}
		}
		return res;
	}

	function selectionEventHandler(event) {
		if (typeof _avoidSelectionEventHandler === "undefined")
			_avoidSelectionEventHandler = false;
		if (!_avoidSelectionEventHandler) {
			// Desmarca el botón de zoom seleccionado cuando se hace un zoom seleccionando puntos en la gráfica
			deselectZoom(prevClickedZoomId);
			prevClickedZoomId = null;
			if (event.xAxis) {
				var qvOptions = event.target.options.qv;
				var xAxis = event.xAxis[0];
				var minPercent = (xAxis.min - qvOptions.dataMin) / (qvOptions.dataMax - qvOptions.dataMin) * 100;
				var maxPercent = (xAxis.max - qvOptions.dataMin) / (qvOptions.dataMax - qvOptions.dataMin) * 100;
				InitializeSlider(event.target.options.qv.viewerId, minPercent, maxPercent);
			}
			else
				InitializeSlider(event.target.options.qv.viewerId, 0, 100);
			var qViewer = qv.collection[event.target.options.qv.viewerId];
			if (IsSplittedChart(qViewer)) {
				var containers;
				var containerId = qViewer.getContainerControl().id;
				containers = jQuery("[id^=" + containerId + "_chart]");
				var charts = [];
				jQuery.each(containers, function (index, div) {
					if (div.id != event.target.renderTo.id) {
						var chart = jQuery("#" + div.id).highcharts();
						charts.push(chart);
					}
				});
				jQuery.each(charts, function (index, chart) {
					if (event.xAxis) {
						chart.get('xaxis').setExtremes(xAxis.min, xAxis.max);
					}
					else {
						_avoidSelectionEventHandler = true;
						chart.zoomOut();
						_avoidSelectionEventHandler = false;
					}
				});
			}
		}
	}

	function groupPoints(chartCategories, chartSeriePoints, xAxisDataType, aggregation, groupOption)
	{

		function getGroupStartPoint(dateStr, name, xAxisDataType, dateFormat, groupOption) {

			function yearWith4Digits(xAxisDataType, name) {
				return xAxisDataType == QueryViewerDataType.Date ? name.length == 10 : name.charAt(10) == " ";
			}

			function formatDate(dateStr, dateFormat, yearWith4Digits, includeMonth, includeDay) {
				var year = dateStr.substr(0, 4);
				var month = dateStr.substr(5, 2);
				var day = dateStr.substr(8, 2);
				var date = dateFormat;
				if (!includeMonth) date = date.replace("M", "");
				if (!includeDay) date = date.replace("D", "");
				var newDate = "";
				for (var i = 0; i < date.length; i++) {
					newDate += i == 0 ? "" : "/";
					newDate += date.charAt(i);
				}
				date = newDate.replace("Y", yearWith4Digits ? year : year.substr(2, 2));
				if (includeMonth)
					date = date.replace("M", month);
				if (includeDay)
					date = date.replace("D", day);
				return date;
			}

			var dateStrStartPoint;
			var nameStartPoint;
			if (dateStr != "") {
				groupOption = groupOption || (xAxisDataType == QueryViewerDataType.Date ? "Days" : "Seconds");
				switch (groupOption) {
					case "Years":
						dateStrStartPoint = dateStr.substr(0, 4) + "-01-01";
						nameStartPoint = formatDate(dateStrStartPoint, dateFormat, yearWith4Digits(xAxisDataType, name), false, false);
						break;
					case "Months":
						dateStrStartPoint = dateStr.substr(0, 7) + "-01";
						nameStartPoint = formatDate(dateStrStartPoint, dateFormat, yearWith4Digits(xAxisDataType, name), true, false);
						break;
					case "Semesters":
						var startingMonth = dateStr.substr(5, 2) <= "06" ? "01" : "07";
						dateStrStartPoint = dateStr.substr(0, 5) + startingMonth + "-01";
						var semester = dateStr.substr(5, 2) <= "06" ? "01" : "02";
						dateStrSemester = dateStr.substr(0, 5) + semester + "-01";
						nameStartPoint = formatDate(dateStrSemester, dateFormat, yearWith4Digits(xAxisDataType, name), true, false);
						break;
					case "Quarters":
						var startingMonth = dateStr.substr(5, 2) <= "03" ? "01" : (dateStr.substr(5, 2) <= "06" ? "04" : (dateStr.substr(5, 2) <= "09" ? "07" : "10"));
						dateStrStartPoint = dateStr.substr(0, 5) + startingMonth + "-01";
						var quarter = dateStr.substr(5, 2) <= "03" ? "01" : (dateStr.substr(5, 2) <= "06" ? "02" : (dateStr.substr(5, 2) <= "09" ? "03" : "04"));
						dateStrQuarter = dateStr.substr(0, 5) + quarter + "-01";
						nameStartPoint = formatDate(dateStrQuarter, dateFormat, yearWith4Digits(xAxisDataType, name), true, false);
						break;
					case "Weeks":
						var date = new gx.date.gxdate(dateStr, "Y4MD");
						var dow = gx.date.dow(date);
						date = gx.date.dtadd(date, -86400 * (dow - 1));
						dateStrStartPoint = qv.util.dateToString(date, false);
						nameStartPoint = formatDate(dateStrStartPoint, dateFormat, yearWith4Digits(xAxisDataType, name), true, true);
						break;
					case "Days":
						dateStrStartPoint = xAxisDataType == QueryViewerDataType.Date ? dateStr : dateStr.substr(0, 10);
						nameStartPoint = xAxisDataType == QueryViewerDataType.Date ? name : name.substr(0, 10);
						break;
					case "Hours":
						dateStrStartPoint = dateStr.substr(0, 13) + ":00:00";
						nameStartPoint = name.substr(0, 13) + ":00";
						break;
					case "Minutes":
						dateStrStartPoint = dateStr.substr(0, 16) + ":00";
						nameStartPoint = name.substr(0, 16);
						break;
					case "Seconds":
						dateStrStartPoint = dateStr;
						nameStartPoint = name;
						break;
				}
			}
			else {
				dateStrStartPoint = "";
				nameStartPoint = "";
			}
			return { dateStr: dateStrStartPoint, name: nameStartPoint };
		}

		var point;
		var lastStartPoint = { dateStr: null, name: null };
		var currentYValues = [];
		var currentYQuantities = [];
		var points = [];
		for (i = 0; i < chartSeriePoints.length; i++) {
			var name = chartCategories.Values[i].ValueWithPicture;
			var xValue = chartCategories.Values[i].Value;
			var yValue;
			var yQuantity;
			if (chartSeriePoints[i].Value != null) {
				if (aggregation == QueryViewerAggregationType.Count) {
					yValue = 0;		// No se utiliza
					yQuantity = parseFloat(qv.util.trim(chartSeriePoints[i].Value));
				}
				else {
					if (aggregation == QueryViewerAggregationType.Average) {
						yValue = parseFloat(qv.util.trim(chartSeriePoints[i].Value_N));
						yQuantity = parseFloat(qv.util.trim(chartSeriePoints[i].Value_D));
					}
					else {
						yValue = parseFloat(qv.util.trim(chartSeriePoints[i].Value));
						yQuantity = 1;
					}
				}
			}
			else {
				yValue = null;
				yQuantity = 0;
			}
			var currentStartPoint = getGroupStartPoint(xValue, name, xAxisDataType, gx.dateFormat, groupOption);
			if (currentStartPoint.dateStr == lastStartPoint.dateStr || i == 0) {
				if (yValue != null) {
					currentYValues.push(yValue);
					currentYQuantities.push(yQuantity);
				}
				if (i == 0)
					lastStartPoint = currentStartPoint;
			}
			else {
				point = { x: lastStartPoint.dateStr, y: qv.util.aggregate(aggregation, currentYValues, currentYQuantities), name: lastStartPoint.name };
				points.push(point);
				lastStartPoint = currentStartPoint;
				currentYValues = [yValue];
				currentYQuantities = [yQuantity];
			}
		}
		if (currentYValues.length > 0 && currentYQuantities.length > 0) {
			point = { x: lastStartPoint.dateStr, y: qv.util.aggregate(aggregation, currentYValues, currentYQuantities), name: lastStartPoint.name };
			points.push(point);
		}
		return points;
	}

	function getSpacing(qViewer)
	{
		var spacingTop;
		var spacingRight;
		var spacingBottom;
		var spacingLeft;
		if (IsTimelineChart(qViewer)) {
			spacingTop = DEFAULTCHARTSPACING;
			spacingRight = 0;
			spacingBottom = DEFAULTCHARTSPACING;
			spacingLeft = 0;
		}
		else {
			spacingTop = DEFAULTCHARTSPACING;
			spacingRight = DEFAULTCHARTSPACING;
			spacingBottom = DEFAULTCHARTSPACING;
			spacingLeft = DEFAULTCHARTSPACING;
		}
		return [spacingTop, spacingRight, spacingBottom, spacingLeft];
	}

	function circularGaugeWidths(chartSeriesCount, serieNumber) {
		var width;
		var center;
		var lowerExtreme;
		var upperExtreme;
		if (chartSeriesCount <= 3)
			width = 24;
		else
			width = 50 / (chartSeriesCount - 1) - 1;		// Para que no pase más del 50% del Gauge hacia adentro;
		center = 100 - (width + 1) * (serieNumber - 1);
		lowerExtreme = center - width / 2;
		upperExtreme = center + width / 2;
		return { Width: width, Center: center, LowerExtreme: lowerExtreme, UpperExtreme: upperExtreme};
	}

	function linearGaugeWidths(chartSeriesCount, serieNumber) {
		var width = 1 / chartSeriesCount / 2;
		var center = -0.5 + (serieNumber - 0.5) / chartSeriesCount;
		var lowerExtreme = center - width / 2;
		var upperExtreme = center + width / 2;
		return { Width: width, Center: center, LowerExtreme: lowerExtreme, UpperExtreme: upperExtreme };
	}

	function GetColorStringFromHighchartsObject(qViewer, highchartsObject) {

		function GetHighchartsDefaultColors(qViewer) {

			function LoadColorsObj(colorsObj, styleSheet, rulePrefix) {
				var colorsCount = 0;
				for (var i = 0; i < styleSheet.cssRules.length; i++) {
					var rule = styleSheet.cssRules[i];
					if (rule.selectorText && rule.selectorText.indexOf(rulePrefix) == 0) {
						var key = rule.selectorText.replace(rulePrefix, "");
						colorsObj[key] = rule.style.fill;
						colorsCount++;
					}
					if (colorsCount == HIGHCHARTS_MAX_COLORS)
						break;
				}
			}

			if (!qViewer._HighchartsDefaultColors) {
				qViewer._HighchartsDefaultColors = [];
				var colorsObj = {};
				var styleSheet;
				var rulePrefix;
				styleSheet = qv.util.GetQueryViewerStyleSheet(qViewer);		// Inicializo con los colores default del QueryViewer
				rulePrefix = ".highcharts-color-";
				LoadColorsObj(colorsObj, styleSheet, rulePrefix);
				if (qViewer.Class != "") {
					styleSheet = qv.util.GetThemeStyleSheet(qViewer);			// Sustituyo con los colores definidos en el tema
					rulePrefix = "." + qv.util.GetContainerControlClass(qViewer) + " " + ".highcharts-color-";
					LoadColorsObj(colorsObj, styleSheet, rulePrefix);
				}
				for (var i = 0; i < HIGHCHARTS_MAX_COLORS; i++)
					qViewer._HighchartsDefaultColors.push(colorsObj[i.toString()]);
			}
			return qViewer._HighchartsDefaultColors;
		}

		var classPrefix = "highcharts-color-";
		var colorIndex;
		var color;
		if (highchartsObject.className && highchartsObject.className.indexOf(classPrefix) == 0)
			colorIndex = parseInt(highchartsObject.className.replace(classPrefix, ""));
		else
			colorIndex = highchartsObject.colorIndex;
		if (colorIndex < HIGHCHARTS_MAX_COLORS) {
			var defaultColors = GetHighchartsDefaultColors(qViewer);
			color = defaultColors[colorIndex];
		}
		else {
			color = HIGHCHARTS_CUSTOM_COLOR[colorIndex - HIGHCHARTS_MAX_COLORS];
			if (qv.util.color.IsHexaColor(color))
				color = "#" + color;
		}
		return color;
	}

	function SetHighchartsColor(qViewer, highchartsObject, color, colorIndexForDefaultColors) {

		function AddHighchartsCustomColor(qViewer, color) {

			var localColorIndex = HIGHCHARTS_CUSTOM_COLOR.indexOf(color);
			var globalColorIndex;
			if (localColorIndex < 0) {
				HIGHCHARTS_CUSTOM_COLOR.push(color);
				localColorIndex = HIGHCHARTS_CUSTOM_COLOR.length - 1;
				globalColorIndex = HIGHCHARTS_MAX_COLORS + localColorIndex;
				var prefix = qv.util.color.IsHexaColor(color) ? "#" : "";
				var rule = "." + HIGHCHARTS_COLOR_PREFIX + globalColorIndex + " {fill: " + prefix + color + "; stroke: " + prefix + color + ";}";
				var themeStyleSheet = qv.util.GetThemeStyleSheet(qViewer);
				themeStyleSheet.insertRule(rule, themeStyleSheet.cssRules.length);
			}
			else
				globalColorIndex = HIGHCHARTS_MAX_COLORS + localColorIndex;
			return globalColorIndex;
		}

		if (!qv.util.IsNullColor(color)) {
			var colorIndex;
			if (color.IsDefault)
				colorIndex = color.ColorIndex;
			else
				colorIndex = AddHighchartsCustomColor(qViewer, color.Color);
			if (colorIndexForDefaultColors)
				highchartsObject.colorIndex = colorIndex;
			else
				highchartsObject.className = HIGHCHARTS_COLOR_PREFIX + colorIndex;	// Para PlotBands, por ejemplo, no anda setear el colorIndex
		}
	}

	function AddHighchartsCSSRules(qViewer) {
		// Workaround por no poder hacer estos seteos mediante la propiedad className en el tooltip
		if (qViewer.RealChartType == QueryViewerChartType.CircularGauge) {
			var themeStyleSheet = qv.util.GetThemeStyleSheet(qViewer);
			var rule = "#" + qViewer.ContainerName + " .highcharts-tooltip-box {fill: none !important; stroke-width: 0 !important; }";
			themeStyleSheet.insertRule(rule, themeStyleSheet.cssRules.length);
		}
	}

	function getHighchartOptions(qViewer, chartSerie, serieIndex) {

		function getChartObject(qViewer, serieIndex) {
			var chart = {};
			chart.styledMode = true;
			chart.spacing = getSpacing(qViewer);
			if (!IsSplittedChart(qViewer)) {
				if (!IsTimelineChart(qViewer))
					chart.renderTo = qViewer.getContainerControl();
				else
					chart.renderTo = document.getElementById(centerId(qViewer.userControlId()));
			}
			else {
				var viewerId = qViewer.userControlId();
				var baseId;
				if (IsTimelineChart(qViewer))
					baseId = centerId(viewerId);
				else
					baseId = viewerId;
				chart.renderTo = document.getElementById(baseId + "_chart" + serieIndex.toString());
			}
			if (!IsCombinationChart(qViewer)) {
				chart.type = getChartType_forHightCharts(qViewer.RealChartType);
			}
			if (qViewer.RealChartType == QueryViewerChartType.Radar || qViewer.RealChartType == QueryViewerChartType.FilledRadar || qViewer.RealChartType == QueryViewerChartType.PolarArea)
				chart.polar = true;
			else if (qViewer.RealChartType == QueryViewerChartType.Column3D || qViewer.RealChartType == QueryViewerChartType.StackedColumn3D || qViewer.RealChartType == QueryViewerChartType.Column3DLine)
				chart.options3d = { enabled: true, alpha: 15, beta: 15, depth: 50, viewDistance: 25 };
			else if (qViewer.RealChartType == QueryViewerChartType.Pie3D || qViewer.RealChartType == QueryViewerChartType.Doughnut3D)
				chart.options3d = { enabled: true, alpha: 45, beta: 0 };
			else if (IsTimelineChart(qViewer)) {
				chart.zoomType = 'x';
				chart.resetZoomButton = { theme: { display: 'none' } };
				chart.events = {};
				chart.events.selection = function (event) {
					selectionEventHandler(event);
				};
			}
			return chart;
		}

		function getNoCreditsObject() {
			var credits = { enabled: false };
			return credits;
		}

		function getLegendObject(qViewer) {
			var legend = { enabled: (qViewer.Chart.Series.DataFields.length > 1 && !IsSplittedChart(qViewer)) || IsSingleSerieChart(qViewer), margin: 0 };
			return legend;
		}

		function getTitleObject(qViewer, serieIndex) {
			var title;
			if (qViewer.Title == "")
				title = { text: null };
			else {
				var titleStr = (serieIndex == null || serieIndex == 0) ? qv.util.getTranslation(qViewer.Title) : null;
				title = { text: titleStr };
			}
			return title;
		}

		function getSubtitleObject(qViewer, chartSerie) {
			var subtitle = {};
			if (IsSplittedChart(qViewer) && (IsSingleSerieChart(qViewer) || qViewer.RealChartType == QueryViewerChartType.CircularGauge)) {
				subtitle.text = chartSerie.Name;
				subtitle.floating = true;
				subtitle.align = "left";
				subtitle.verticalAlign = "middle";
			}
			return subtitle;
		}

		function getXAxisObject(qViewer, serieIndex) {
			xAxis = {};
			if (qViewer.RealChartType != QueryViewerChartType.CircularGauge) {
				var isDatetimeXAxis = IsDatetimeXAxis(qViewer);
				if (qViewer.RealChartType != QueryViewerChartType.Radar && qViewer.RealChartType != QueryViewerChartType.FilledRadar && qViewer.RealChartType != QueryViewerChartType.PolarArea && qViewer.RealChartType != QueryViewerChartType.LinearGauge && qViewer.RealChartType != QueryViewerChartType.Sparkline)
					xAxis.title = { text: XAxisTitle(qViewer) };
				else {
					if (qViewer.RealChartType == QueryViewerChartType.Sparkline && IsSplittedChart(qViewer))
						xAxis.title = { text: qViewer.Chart.Series.ByIndex[serieIndex].Name };
					xAxis.lineWidth = 0;
					if (qViewer.RealChartType == QueryViewerChartType.LinearGauge || qViewer.RealChartType == QueryViewerChartType.Sparkline)
						xAxis.tickPositions = [];
					if (qViewer.RealChartType == QueryViewerChartType.Radar || qViewer.RealChartType == QueryViewerChartType.FilledRadar)
						xAxis.tickmarkPlacement = "on";
					else
						xAxis.tickmarkPlacement = "between";
				}
				if (qViewer.RealChartType == QueryViewerChartType.LinearGauge) {
					var i = 0;
					var widths;
					if (IsSplittedChart(qViewer))
						widths = linearGaugeWidths(1, 1);
					xAxis.plotBands = [];
					for (var serieName in qViewer.Chart.Series.ByName) {
						if (!IsSplittedChart(qViewer) || i == serieIndex) {
							var chartSerie = qViewer.Chart.Series.ByName[serieName];
							if (!IsSplittedChart(qViewer))
								widths = linearGaugeWidths(qViewer.Chart.Series.DataFields.length, i + 1);
							plotBand = {};
							var color;
							if (!qv.util.IsNullColor(chartSerie.Color))
								color = chartSerie.Color;
							else
								color = chartSerie.Points[0].Color;
							SetHighchartsColor(qViewer, plotBand, color, false)
							plotBand.from = widths.LowerExtreme;
							plotBand.to = widths.UpperExtreme;
							xAxis.plotBands.push(plotBand);
						}
						i++;
					}
				}
				if (!isDatetimeXAxis)
					xAxis.categories = [];
				var anyCategoryLabel = false;
				for (i = 0; i < qViewer.Chart.Categories.Values.length; i++) {
					if (qViewer.Chart.Categories.Values[i].ValueWithPicture != "")
						anyCategoryLabel = true;
					if (!isDatetimeXAxis) {
						xAxis.categories[i] = qViewer.Chart.Categories.Values[i].ValueWithPicture;
					}
				}
				if (!isDatetimeXAxis) {
					xAxis.labels = {};
					xAxis.labels.enabled = anyCategoryLabel;
					if (qViewer.XAxisLabels != QueryViewerXAxisLabels.Horizontally && !IsBarChart(qViewer) && !IsPolarChart(qViewer)) {
						var angle;
						var offsetY;
						if (qViewer.XAxisLabels == QueryViewerXAxisLabels.Vertically) {
							angle = 90;
							offsetY = 5;
						}
						else {
							angle = parseInt(qViewer.XAxisLabels.replace("Rotated", ""));
							offsetY = 10;
						}
						xAxis.labels.rotation = 360 - angle;
						xAxis.labels.y = offsetY;
						xAxis.labels.align = "right";
					}
				}
				else {
					xAxis.type = 'datetime';
					xAxis.id = 'xaxis';
					xAxis.minRange = 1;		// 1ms máximo zoom (el default es demasiado grande)
					if (qv.util.XAxisDataType(qViewer) == QueryViewerDataType.Date) {
						if (qViewer.Chart.Categories.MaxValue != null && qViewer.Chart.Categories.MinValue != null) {
							var minDate = new gx.date.gxdate(qViewer.Chart.Categories.MinValue, "Y4MD");
							var maxDate = new gx.date.gxdate(qViewer.Chart.Categories.MaxValue, "Y4MD");
							if (maxDate.Value.getTime() - minDate.Value.getTime() < 10 * 24 * 3600 * 1000)
								xAxis.tickInterval = 24 * 3600 * 1000;
						}
					}
				}
				if (IsPolarChart(qViewer))
					xAxis.className = "highcharts-no-axis-line highcharts-yes-grid-line";		// Clases no estándar de Highcharts
				else if (qViewer.RealChartType == QueryViewerChartType.Sparkline)
					xAxis.className = "highcharts-no-axis-line highcharts-no-grid-line";		// Clases no estándar de Highcharts
		}
			return xAxis;
		}

		function getYAxisObject(qViewer, chartSerie) {
		
			function SamePicture(series) {
				if (series.length == 0)
					return false;
				else {
					var picture;
					for (var i = 0; i < series.length; i++) {
						if (i == 0)
							picture = series[i].Picture;
						else {
							if (series[i].Picture != picture)
								return false;
						}
					}
					return true;
				}
			}
			
			if (qViewer.RealChartType == QueryViewerChartType.Sparkline)
				yAxis = { visible: false };
			else {
				var yAxisName;
				if (qViewer.RealChartType == QueryViewerChartType.CircularGauge)
					yAxisName = null;
				else
					yAxisName = IsSplittedChart(qViewer) ? chartSerie.Name : YAxisTitle(qViewer);
				if (IsCombinationChart(qViewer) && !IsSplittedChart(qViewer)) {
					yAxis = [];
					yAxis[0] = { title: { text: yAxisName } };
					yAxis[1] = { title: { text: "" }, opposite: true };		// El eje secundario por ahora no es posible setearle titulo
				} else {
					yAxis = { title: { text: yAxisName } };
				}
				yAxis.plotLines = [];
				yAxis.plotBands = [];
				if (HasYAxis(qViewer)) {
					for (var i = 0; i < qViewer.Chart.PlotBands.length; i++) {
						var chartPlotBand = qViewer.Chart.PlotBands[i];
						if (chartSerie == null || chartSerie.Name == chartPlotBand.SeriesName) {
							if (chartPlotBand.From == chartPlotBand.To && chartPlotBand.From != null) {
								var plotLine = { value: chartPlotBand.From, width: 1, zIndex: 3 };
								SetHighchartsColor(qViewer, plotLine, chartPlotBand.Color, false);
								yAxis.plotLines.push(plotLine);
							}
							else {
								var plotBand = {};
								SetHighchartsColor(qViewer, plotBand, chartPlotBand.Color, false);
								plotBand.from = !chartPlotBand.From ? Number.MIN_VALUE : chartPlotBand.From;
								plotBand.to = !chartPlotBand.To ? Number.MAX_VALUE : chartPlotBand.To;
								yAxis.plotBands.push(plotBand);
							}
						}
					}
					if (IsSplittedChart(qViewer) || qViewer.Chart.Series.ByIndex.length == 1 || SamePicture(qViewer.Chart.Series.ByIndex)) {
						var firstSerie = IsSplittedChart(qViewer) ? chartSerie : qViewer.Chart.Series.ByIndex[0];
						yAxis.labels = {};
						yAxis.labels.formatter = function () {
							return qv.util.formatNumber(this.value, firstSerie.NumberFormat.DecimalPrecision, firstSerie.Picture, true);
						};
					}
				}
				if (qViewer.RealChartType == QueryViewerChartType.Radar || qViewer.RealChartType == QueryViewerChartType.FilledRadar || qViewer.RealChartType == QueryViewerChartType.PolarArea) {
					yAxis.min = 0;
					if (qViewer.RealChartType == QueryViewerChartType.Radar || qViewer.RealChartType == QueryViewerChartType.FilledRadar)
						yAxis.gridLineInterpolation = "polygon";
					else
						yAxis.gridLineInterpolation = "circle";
				}
				if (IsGaugeChart(qViewer)) {
					yAxis.min = 0;
					yAxis.max = 0;
					for (var serieName in qViewer.Chart.Series.ByName) {
						if (!IsSplittedChart(qViewer) || serieName == chartSerie.Name) {
							var chartSerieAux = qViewer.Chart.Series.ByName[serieName];
							yAxis.max = Math.max(yAxis.max, 100 * chartSerieAux.MaximumValue / chartSerieAux.TargetValue);
						}
					}
					if (qViewer.RealChartType == QueryViewerChartType.LinearGauge || yAxis.max != 100) {
						var plotLine = {};
						plotLine.className = "highcharts-dashed-plot-line";		// Clase no estándar de Highcharts
						plotLine.value = 100;
						if (IsSplittedChart(qViewer) || qViewer.Chart.Series.DataFields.length == 1) {
							var cs = IsSplittedChart(qViewer) ? chartSerie : qViewer.Chart.Series.ByIndex[0];
							var y;
							var x;
							var align;
							if (qViewer.RealChartType == QueryViewerChartType.LinearGauge) {
								y = -10;
								x = -5;
								align = "right";
							}
							else {
								y = 15;
								x = 0;
								align = "center";
							}
							plotLine.label = { text: cs.TargetValue, verticalAlign: 'bottom', rotation: 0, y: y, x: x, align: align };
						}
						yAxis.plotLines.push(plotLine);
					}
				}
				if (qViewer.RealChartType == QueryViewerChartType.LinearGauge) {
					yAxis.className = "highcharts-no-axis-line highcharts-no-grid-line";		// Clases no estándar de Highcharts
					yAxis.labels = { enabled: false };
				} else if (qViewer.RealChartType == QueryViewerChartType.CircularGauge) {
					yAxis.lineWidth = 0;
					yAxis.tickPositions = [];
				}
				
				// Check series min and max values
				var anyPositiveValue = false;
				var anyNegativeValue = false;
				for (var serieName in qViewer.Chart.Series.ByName) {
					var chartSerieAux = qViewer.Chart.Series.ByName[serieName];
					if (chartSerieAux.PositiveValues)
						anyPositiveValue = true;
					if (chartSerieAux.NegativeValues)
						anyNegativeValue = true;
				}
				if (!anyNegativeValue && !anyPositiveValue) {
					if (IsCombinationChart(qViewer) && !IsSplittedChart(qViewer)) {
						yAxis[0].min = 0;
						yAxis[1].min = 0;
						yAxis[0].max = 1;
						yAxis[1].max = 1;
					}
					else {
						yAxis.min = 0;
						yAxis.max = 1;
					}
				}
				else if (gx.lang.gxBoolean(qViewer.XAxisIntersectionAtZero)) {
					if (!anyNegativeValue) {
						if (IsCombinationChart(qViewer) && !IsSplittedChart(qViewer)) {
							yAxis[0].min = 0;
							yAxis[1].min = 0;
						}
						else
							yAxis.min = 0;
					}
					if (!anyPositiveValue) {
						if (IsCombinationChart(qViewer) && !IsSplittedChart(qViewer)) {
							yAxis[0].max = 0;
							yAxis[1].max = 0;
						}
						else
							yAxis.max = 0;
					}
				}
			}
			return yAxis;
		}

		function getPlotOptionsObject(chartType, qViewer) {

			function LinearGaugePlotHeight(qViewer) {
				var marginBottom;
				if (IsSplittedChart(qViewer) || qViewer.Chart.Series.DataFields.length == 1)
					marginBottom = 23 * NumberOfCharts(qViewer);	// por el título del eje Y
				else
					marginBottom = 29;	// por la leyenda
				return qViewer.getContainerControl().offsetHeight - marginBottom;
			}

			function getMarker(qViewer) {
				var marker = { radius: 1, symbol: "circle", states: { hover: { radius: 4 } } };
				if (qViewer.SelectionAllowed()) {
					marker.states.select = { radius: 5, lineWidth: 1 };
				}
				return marker;
			}

			var showvalues = gx.lang.gxBoolean(qViewer.ShowValues);
			var plotOptions = {};
			plotOptions.series = {};
			plotOptions.series.events = {};
			if (qViewer.RealChartType == QueryViewerChartType.CircularGauge) {
				plotOptions.series.dataLabels = {};
				plotOptions.series.dataLabels.enabled = (qViewer.Chart.Series.DataFields.length == 1 && gx.lang.gxBoolean(qViewer.ShowValues)) || IsSplittedChart(qViewer);
				plotOptions.series.dataLabels.y = 0;
				plotOptions.series.dataLabels.borderWidth = 0;
				plotOptions.series.dataLabels.formatter = function () {
					return CircularGaugeTooltipAndDataLabelFormatter(this, qViewer);
				}
				plotOptions.series.marker = { enabled: false };
			}
			else {
				if (showvalues) {
					plotOptions.series.dataLabels = {};
					plotOptions.series.dataLabels.enabled = true;
					plotOptions.series.dataLabels.connectorColor = '#000000'
					plotOptions.series.dataLabels.formatter = function () {
						return DataLabelFormatter(this, qViewer);
					}
					if (qViewer.RealChartType == QueryViewerChartType.LinearGauge)
						plotOptions.series.dataLabels.inside = true;
				}
			}
			if (IsSplittedChart(qViewer) && qViewer.RealChartType != QueryViewerChartType.CircularGauge) {
				plotOptions.series.point = {};
				plotOptions.series.point.events = {};
				var highlightIfVisible = qViewer.RealChartType != QueryViewerChartType.LinearGauge;
				plotOptions.series.point.events.mouseOver = function () { syncPoints(qViewer, this.series.chart.container, this.index, true, highlightIfVisible); };
				plotOptions.series.point.events.mouseOut = function () { syncPoints(qViewer, this.series.chart.container, this.index, false, highlightIfVisible); };
			}
			if (qViewer.ItemClick || qViewer.SelectionAllowed())
				plotOptions.series.events.click = onHighchartsItemClickEventHandler;	// Asocia el manejador para el evento click de la chart
			switch (chartType) {
				case "bar":
					plotOptions.bar = {};
					if (qViewer.RealChartType == QueryViewerChartType.StackedBar) {
						plotOptions.series.stacking = 'normal';
						plotOptions.bar.stacking = 'normal';
					} else if (qViewer.RealChartType == QueryViewerChartType.StackedBar100) {
						plotOptions.series.stacking = 'percent';
						plotOptions.bar.stacking = 'percent';
					}
					if (qViewer.RealChartType == QueryViewerChartType.LinearGauge) {
						var widths = linearGaugeWidths(qViewer.Chart.Series.DataFields.length, 1);
						var width = widths.Width * LinearGaugePlotHeight(qViewer);
						plotOptions.bar.pointWidth = width;
						plotOptions.bar.pointPadding = 0;
						plotOptions.bar.groupPadding = 0;
						var minValue = Number.MAX_VALUE;
						for (var i = 0; i < qViewer.Chart.Series.DataFields.length; i++) {
							var chartSerie = qViewer.Chart.Series.ByIndex[i];
							var value = chartSerie.Points[0].Value / chartSerie.TargetValue;
							if (value < minValue)
								minValue = value;
						}
						var minLength = minValue * qViewer.getContainerControl().offsetWidth;
						plotOptions.bar.borderRadius = Math.min(width / 2, minLength / 2);
					}
					break;
				case "column":
					plotOptions.column = {};
					if (qViewer.RealChartType == QueryViewerChartType.StackedColumn || qViewer.RealChartType == QueryViewerChartType.StackedColumn3D || qViewer.RealChartType == QueryViewerChartType.PolarArea) {
						plotOptions.series.stacking = 'normal';
						plotOptions.column.stacking = 'normal';
					} else if (qViewer.RealChartType == QueryViewerChartType.StackedColumn100) {
						plotOptions.series.stacking = 'percent';
						plotOptions.column.stacking = 'percent';
					}
					if (qViewer.RealChartType == QueryViewerChartType.PolarArea) {
						plotOptions.column.pointPadding = 0;
						plotOptions.column.groupPadding = 0;
					}
					break;
				case "area":
					plotOptions.area = {};
					if (qViewer.RealChartType == QueryViewerChartType.StepArea)
						plotOptions.area.step = "center";
					plotOptions.area.marker = getMarker(qViewer);
					if (qViewer.RealChartType == QueryViewerChartType.StackedArea)
						plotOptions.area.stacking = 'normal';
					else if (qViewer.RealChartType == QueryViewerChartType.StackedArea100)
						plotOptions.area.stacking = 'percent';
					break;
				case "areaspline":
					plotOptions.areaspline = {};
					plotOptions.areaspline.marker = getMarker(qViewer);
					break;
				case "line":
					plotOptions.line = {};
					plotOptions.line.marker = getMarker(qViewer);
					if (qViewer.RealChartType == QueryViewerChartType.StepLine)
						plotOptions.line.step = "center";
					else if (qViewer.RealChartType == QueryViewerChartType.StepTimeline)
						plotOptions.line.step = "left";
					if (IsTimelineChart(qViewer))
						plotOptions.series.connectNulls = true;		// Para el caso de la time se setea esta configuracion para que la Highcharts interpole los datos, evitando que se generen saltos (gaps) en la la linea graficada. Cuando se tienen datos faltantes para el eje x (fechas para las cuales no se obtuvieron datos)
					else if (qViewer.RealChartType == QueryViewerChartType.StackedLine)
						plotOptions.line.stacking = 'normal';
					else if (qViewer.RealChartType == QueryViewerChartType.StackedLine100)
						plotOptions.line.stacking = 'percent';
					break;
				case "spline":
					plotOptions.spline = {};
					plotOptions.spline.marker = getMarker(qViewer);
					break;
				case "pie":
					plotOptions.pie = {};
					if (qViewer.RealChartType == QueryViewerChartType.Doughnut || qViewer.RealChartType == QueryViewerChartType.Doughnut3D)
						plotOptions.pie.innerSize = '35%';
					if (qViewer.RealChartType == QueryViewerChartType.Pie3D || qViewer.RealChartType == QueryViewerChartType.Doughnut3D)
						plotOptions.pie.depth = 35;
					plotOptions.pie.dataLabels = { enabled: showvalues };
					plotOptions.pie.showInLegend = true;
					break;
				case "solidgauge":
					plotOptions.solidgauge = {};
					plotOptions.solidgauge.showInLegend = true;
					plotOptions.solidgauge.rounded = true;
					break;
				case "funnel":
					plotOptions.funnel = {};
					plotOptions.funnel.showInLegend = true;
					plotOptions.funnel.dataLabels = { enabled: showvalues };
					break;
				case "pyramid":
					plotOptions.pyramid = {};
					plotOptions.pyramid.showInLegend = true;
					plotOptions.pyramid.dataLabels = { enabled: showvalues };
					break;
			}
			return plotOptions;
		}

		function getTooltipObject(qViewer) {
			var tooltip = {};
			if (IsTimelineChart(qViewer)) {
				tooltip.borderRadius = 1;
				tooltip.shadow= true;
				tooltip.shared = true;
				tooltip.useHTML = false; //Hay un bug que hace que si esta en true se muestra el html por fuera del aréa del tooltip
				tooltip.formatter = function () {
					return DateTimeTooltipFormatter(this, qViewer.Chart.Series.ByName)
				}
			}
			else {
				if (qViewer.RealChartType == QueryViewerChartType.StackedColumn100 || qViewer.RealChartType == QueryViewerChartType.StackedBar100 || qViewer.RealChartType == QueryViewerChartType.StackedArea100 || qViewer.RealChartType == QueryViewerChartType.StackedLine100)
					tooltip.formatter = function () {
						return Stacked100TooltipFormatter(this);
					}
				else if (IsCircularChart(qViewer))
					tooltip.formatter = function () {
						return PieTooltipFormatter(this, qViewer.Chart.Series.ByName, IsSplittedChart(qViewer));
					}
				else if (qViewer.RealChartType == QueryViewerChartType.CircularGauge) {
					tooltip.enabled = (qViewer.Chart.Series.DataFields.length > 1 || !gx.lang.gxBoolean(qViewer.ShowValues)) && !IsSplittedChart(qViewer);
					tooltip.formatter = function () {
						return CircularGaugeTooltipAndDataLabelFormatter(this, qViewer);
					};
					tooltip.positioner = function (labelWidth, labelHeight) {
						return { x: (this.chart.plotWidth - labelWidth) / 2, y: (this.chart.plotHeight) / 2 };
					}
				}
				else
					tooltip.formatter = function () {
						return TooltipFormatter(this, qViewer.Chart.Series.ByName, IsSplittedChart(qViewer) && !IsGaugeChart(qViewer));
					}
			}
			return tooltip;
		}

		function getSeriesObject(qViewer, serieIndex, groupOption) {

			function getSerieObject(qViewer, chartSerie, serieIndex, series, groupOption) {
				var serie = {};
				if ((qViewer.ItemClick && qViewer.Metadata.Data[serieIndex].RaiseItemClick))
					serie.className = "highcharts-drilldown-point";
				serie.visible = chartSerie.Visible == QueryViewerVisible.Yes || chartSerie.Visible == QueryViewerVisible.Always;
				serie.events = {
					legendItemClick: function(e) {
						if (chartSerie.Visible == QueryViewerVisible.Always)
							e.preventDefault();
						else {
							var runtimeElements = qv.chart.GetRuntimeMetadata(qViewer);
							var elementName = qViewer.Chart.Series.ByIndex[e.target.index].FieldName;
							var runtimeElement = qv.util.GetElementInCollection(runtimeElements, "Name", elementName);
							runtimeElement.Hidden = !runtimeElement.Hidden; 
							qv.util.autorefresh.UpdateLayoutSameGroup(qViewer, runtimeElements, false);
						}
					}
				};
				if (IsTimelineChart(qViewer)) {
					serie.name = chartSerie.Name;
					serie.data = [];
					serie.turboThreshold = 0;
					if (!qv.util.IsNullColor(chartSerie.Color))
						SetHighchartsColor(qViewer, serie, chartSerie.Color, true);
					var points = groupPoints(qViewer.Chart.Categories, chartSerie.Points, qv.util.XAxisDataType(qViewer), chartSerie.Aggregation, groupOption);
					for (j = 0; j < points.length; j++) {
						var name = points[j].name;
						var xValue = points[j].x;
						var value = points[j].y;
						var date = new gx.date.gxdate(xValue, "Y4MD");
						serie.data[j] = { x: date.Value.getTime() - date.Value.getTimezoneOffset() * 60000, y: value, name: name };
						if (qv.util.IsNullColor(chartSerie.Color))
							SetHighchartsColor(qViewer, serie.data[j], chartSerie.Points[j].Color, true);
					}
				}
				else {
					var widths;
					if (qViewer.RealChartType == QueryViewerChartType.CircularGauge) {
						if (IsSplittedChart(qViewer))
							widths = circularGaugeWidths(1, 1);
						else
							widths = circularGaugeWidths(qViewer.Chart.Series.DataFields.length, serieIndex + 1);
					}
					serie.name = chartSerie.Name;
					serie.data = [];
					serie.turboThreshold = 0;
					if (!qv.util.IsNullColor(chartSerie.Color)) {
						SetHighchartsColor(qViewer, serie, chartSerie.Color, true);
					}
					if ((qViewer.RealChartType == QueryViewerChartType.Radar || qViewer.RealChartType == QueryViewerChartType.FilledRadar || qViewer.RealChartType == QueryViewerChartType.PolarArea))
						serie.pointPlacement = (qViewer.RealChartType == QueryViewerChartType.Radar || qViewer.RealChartType == QueryViewerChartType.FilledRadar ? "on" : null);
					for (j = 0; j < chartSerie.Points.length; j++) {
						var value = chartSerie.Points[j].Value != null ? parseFloat(qv.util.trim(chartSerie.Points[j].Value).replace(",", ".")) : null;
						if (IsGaugeChart(qViewer))
							value = value / chartSerie.TargetValue * 100;
						var name = qViewer.Chart.Categories.Values[j].ValueWithPicture;
						serie.data[j] = {};
						serie.data[j].y = value;
						serie.data[j].name = IsGaugeChart(qViewer) ? "" : name;
						serie.data[j].id = serie.data[j].name;
						if (IsDatetimeXAxis(qViewer)) {
							var xValue = qViewer.Chart.Categories.Values[j].Value;
							var date = new gx.date.gxdate(xValue, "Y4MD");
							serie.data[j].x = date.Value.getTime() - date.Value.getTimezoneOffset() * 60000;
							serie.data[j].id = date;
						}
						if (qViewer.RealChartType == QueryViewerChartType.CircularGauge) {
							serie.data[j].radius = (widths.UpperExtreme).toString() + '%';
							serie.data[j].innerRadius = (widths.LowerExtreme).toString() + '%';
						}
						if (qViewer.RealChartType == QueryViewerChartType.CircularGauge) {
							var color;
							if (!qv.util.IsNullColor(chartSerie.Color))
								color = chartSerie.Color;
							else
								color = chartSerie.Points[0].Color;
							SetHighchartsColor(qViewer, serie.data[j], color, true);
						}
						else if (qv.util.IsNullColor(chartSerie.Color))
							SetHighchartsColor(qViewer, serie.data[j], chartSerie.Points[j].Color, true);
					}
				}
				return serie;
			}

			var series = [];
			var i = 0
			for (var serieName in qViewer.Chart.Series.ByName) {
				if (!IsSplittedChart(qViewer) || i == serieIndex) {
					var chartSerie = qViewer.Chart.Series.ByName[serieName];
					var serie = getSerieObject(qViewer, chartSerie, i, series, groupOption)
					var k = serieIndex != null ? serieIndex : i;
					if (IsCombinationChart(qViewer)) {
						if (k % 2 == 0) {
							serie.type = 'column';
							serie.yAxis = 0;
						}
						else {
							serie.type = 'line';
							serie.yAxis = IsSplittedChart(qViewer) ? 0 : 1;
						}
					}
					series.push(serie);
				}
				i++;
			}

			return series;

		}

		function getPaneObject(qViewer, serieIndex) {
			var pane = {};
			if (qViewer.RealChartType == QueryViewerChartType.CircularGauge) {
				pane.background = [];
				var widths;
				if (IsSplittedChart(qViewer))
					widths = circularGaugeWidths(1, 1);
				var i = 0;
				for (var serieName in qViewer.Chart.Series.ByName) {
					if (!IsSplittedChart(qViewer) || i == serieIndex) {
						var chartSerie = qViewer.Chart.Series.ByName[serieName];
						var oneBackground = {};
						if (!IsSplittedChart(qViewer))
							widths = circularGaugeWidths(qViewer.Chart.Series.DataFields.length, i + 1);
						oneBackground.outerRadius = (widths.UpperExtreme).toString() + '%';
						oneBackground.innerRadius = (widths.LowerExtreme).toString() + '%';
						var color;
						if (!qv.util.IsNullColor(chartSerie.Color))
							color = chartSerie.Color;
						else
							color = chartSerie.Points[0].Color;
						SetHighchartsColor(qViewer, oneBackground, color, false);
						oneBackground.borderWidth = 0;
						pane.background.push(oneBackground);
					}
					i++;
				}
			}
			return pane;
		}

		var groupOption = qv.util.XAxisDataType(qViewer) == QueryViewerDataType.Date ? "Days" : "Seconds";

		var options = {};
		options.qv = {};
		options.qv.viewerId = qViewer.userControlId(); // Almacena el identificador del control en las opciones de la grafica

		options.chart = getChartObject(qViewer, serieIndex);
		options.credits = getNoCreditsObject();
		options.legend = getLegendObject(qViewer);
		options.title = getTitleObject(qViewer, serieIndex);
		options.subtitle = getSubtitleObject(qViewer, chartSerie);
		options.pane = getPaneObject(qViewer, serieIndex);
		options.xAxis = getXAxisObject(qViewer, serieIndex);
		options.yAxis = getYAxisObject(qViewer, chartSerie);
		options.plotOptions = getPlotOptionsObject(options.chart.type, qViewer);
		options.tooltip = getTooltipObject(qViewer);
		options.series = getSeriesObject(qViewer, serieIndex, groupOption);

		return options;
	}

	function getAllHighchartOptions(qViewer) {
		var arrOptions = [];
		if (!IsSplittedChart(qViewer)) {
			var options = getHighchartOptions(qViewer, null, null);
			arrOptions.push(options);
		}
		else {
			var serieIndex = 0
			for (var serieName in qViewer.Chart.Series.ByName) {
				var chartSerie = qViewer.Chart.Series.ByName[serieName];
				var options = getHighchartOptions(qViewer, chartSerie, serieIndex);
				arrOptions.push(options);
				serieIndex++;
			}
		}
		return arrOptions;
	}

	function GetDatumByDataField(qViewer, dataField) {
		for (var i = 0; i < qViewer.Metadata.Data.length; i++)
			if (qViewer.Metadata.Data[i].DataField == dataField)
				return qViewer.Metadata.Data[i];
	}

	function XAxisTitle(qViewer) {
		var xAxisTitle = "";
		if (qViewer.XAxisTitle == "")
			for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
				var axis = qViewer.Metadata.Axes[i];
				if (axis.Visible == QueryViewerVisible.Yes || axis.Visible == QueryViewerVisible.Always)
					xAxisTitle += (xAxisTitle == "" ? "" : ", ") + qv.util.getTranslation(axis.Title);		// Se concatenan los títulos
			}
		else
			xAxisTitle = qv.util.getTranslationAlsoIfStatic(qViewer.XAxisTitle);
		return xAxisTitle;
	}

	function YAxisTitle(qViewer) {
		var yAxisTitle = "";
		if (qViewer.YAxisTitle == "") {
			if (qViewer.Chart.Series.DataFields.length == 1) {
				var fieldTitle = GetDatumByDataField(qViewer, qViewer.Chart.Series.DataFields[0]).Title;
				yAxisTitle = qv.util.getTranslation(fieldTitle);
			}
		}
		else
			yAxisTitle = qv.util.getTranslationAlsoIfStatic(qViewer.YAxisTitle);
		return yAxisTitle;
	}

	function renderChart(qViewer) {
		var i = 0;
		var qvClasses = qv.util.GetContainerControlClasses(qViewer);
		if (qvClasses != "")
			qv.util.SetUserControlClass(qViewer, qvClasses);
		var errMsg = qv.util.ProcessDataAndMetadata(qViewer);
		if (errMsg == "") {
			splitChartContainer(qViewer);
			var arrOptions = getAllHighchartOptions(qViewer);
			AddHighchartsCSSRules(qViewer);
			SetHighchartsOptions();
			var HCCharts = [];
			for (var serie = 0; serie < arrOptions.length; serie++) {
				HCChart = new Highcharts.Chart(arrOptions[serie], HCFinishedLoadingCallback);
				HCCharts.push(HCChart);
			}
			qViewer.Charts = HCCharts;
			if (IsTimelineChart(qViewer))
				FillHeaderAndFooter(HCCharts, arrOptions);
			qViewer._ControlRenderedTo = qViewer.RealType;
			qv.util.hideActivityIndicator(qViewer);
		}
		else
			qv.util.renderError(qViewer, errMsg);
	}

	// ------------------------------------------------------ Timeline ------------------------------------------------------

	var prevClickedZoomId = null
	var viewerId = null
	var TimelineHeaderHeight = 35;
	var TimelineFooterHeight = 50;

	function isNumeric(n) {
		return !isNaN(parseFloat(n)) && isFinite(n);
	}

	// Determina si el navegador es Microsoft Internet Explorer en una version anterior a la 9	
	function isOldIEf() {
		return gx.util.browser.isIE() && gx.util.browser.ieVersion() <= 8.0;
	}

	function getZoomId(z) {
		return (viewerId + "_Zoom_" + z + "m");
	}

	function getZoomControl(z) {
		if (isNumeric(z))
			return gx.dom.el(getZoomId(z));
		else //control id
			return gx.dom.el(z);
	}

	function changeZoomControlUnderline(z, decoration) {
		zoom = getZoomControl(z);
		if (zoom)
			zoom.style.textDecoration = decoration;
	}
	
	function selectZoom(z) {
		changeZoomControlUnderline(z, "underline");
	}

	function deselectZoom(z) {
		changeZoomControlUnderline(z, "none");
	}

	function triggerZoom(z) {
		zoom = getZoomControl(z);
		if (zoom)
			zoom.onclick();
		else
			if (z != 0) {		// Intento con un zoom más alejado
				var newZoom;
				switch (z) {
					case 12:
						newZoom = 6;
						break;
					case 6:
						newZoom = 3;
						break;
					case 3:
						newZoom = 2;
						break;
					case 2:
						newZoom = 1;
						break;
					case 1:
						newZoom = 0;
						break;
				}
				triggerZoom(newZoom);
			}
	}

	function hideZoom(z) {
		zoom = getZoomControl(z);
		if (zoom)
			zoom.style.display = "none";
	}

	function showZoom(z) {
		zoom = getZoomControl(z);
		if (zoom)
			zoom.style.display = "";
	}

	function getSelectedZoomFactor() {
		return parseInt(prevClickedZoomId.substring(0, prevClickedZoomId.length - 1).substring(prevClickedZoomId.indexOf("_Zoom_") + 6));
	}

	//Esta funcion se invoca mas arriba en el handler del evento de seleccion de la timeline
	function deselectActiveZoom() {
		deselectZoom(prevClickedZoomId);
	}

	function EnableDisableCompareControls(qViewerId, enabled) {
		gx.dom.el(qViewerId + "_options_compare_enable").disabled = !enabled;
		gx.dom.el(qViewerId + "_options_compare_text").disabled = !enabled;
		gx.dom.el(qViewerId + "_options_compare_text").style.opacity = (!enabled ? 0.5 : 1);
		gx.dom.el(qViewerId + "_options_compare_options").disabled = !enabled;
		gx.dom.el(qViewerId + "_options_compare_options").style.opacity = (!enabled ? 0.5 : 1);
	}

	function EnableCompareControls(qViewerId, enabled) {
		EnableDisableCompareControls(qViewerId, true);
	}

	function DisableCompareControls(qViewerId, enabled) {
		EnableDisableCompareControls(qViewerId, false);
	}

	var sliderCursorWidth = 10;
	var sliderClickedOffsetX = 0;
	var sliderClickedqViewerId = "";
	var sliderClickedPaddingLeft = 0;
	var sliderClickedPaddingRight = 0;
	var sliderClickedRangeWidth = 0;
	var sliderClickedContainerWidth = 0;
	var sliderResizingLeft = false;
	var sliderResizingRight = false;
	var sliderMovingBar = false;

	function optionsId(qViewerId) {
		return qViewerId + "_options";
	}

	function centerId(qViewerId) {
		return qViewerId + "_center";
	}

	function footerId(qViewerId) {
		return qViewerId + "_footer";
	}

	function footerSliderId(qViewerId) {
		return qViewerId + "_footer_slider";
	}

	function footerChartId(qViewerId) {
		return qViewerId + "_footer_chart";
	}

	function footerRangeId(qViewerId) {
		return qViewerId + "_footer_range";
	}

	function footerLeftCursorId(qViewerId) {
		return qViewerId + "_footer_left_cursor";
	}

	function footerRightCursorId(qViewerId) {
		return qViewerId + "_footer_right_cursor";
	}

	function footerCenterId(qViewerId) {
		return qViewerId + "_footer_center";
	}

	function InitializeSlider(qViewerId, minValue, maxValue) {
		minValue = setMinAndMax(minValue, 0, 100);
		maxValue = setMinAndMax(maxValue, 0, 100);

		jQuery("#" + footerId(qViewerId))
			.css("width", "100%")
			.css("height", TimelineFooterHeight.toString() + "px");
		jQuery("#" + footerChartId(qViewerId))
			.css("width", "100%")
			.css("height", (TimelineFooterHeight-2).toString() + "px");

		var controlWidth = qv.collection[qViewerId].getContainerControl().offsetWidth;
		var paddingLeft = minValue * controlWidth / 100;
		var paddingRight = (100 - maxValue) * controlWidth / 100;
		// Fix: PaddingLeft y PaddingRight deben dejar lugar para los cursores. Esto hace que no se pueda llegar a un rango de tamaño cero nunca, pero queda feo si se solapan los cursores
		if (controlWidth - paddingLeft - paddingRight < 2 * sliderCursorWidth) {
			var pixelsToRemove = 2 * sliderCursorWidth - (controlWidth - paddingLeft - paddingRight);
			var pixelsToRemoveLeft;
			var pixelsToRemoveRight;
			if (pixelsToRemove % 2 == 0)
				pixelsToRemoveLeft = pixelsToRemove / 2;
			else
				pixelsToRemoveLeft = (pixelsToRemove + 1) / 2;
			pixelsToRemoveRight = pixelsToRemove - pixelsToRemoveLeft;
			paddingLeft -= pixelsToRemoveLeft;
			paddingRight -= pixelsToRemoveRight;
			if (paddingLeft < 0)
			{
				paddingRight += paddingLeft;
				paddingLeft = 0;
			}
			if (paddingRight < 0)
			{
				paddingLeft += paddingRight;
				paddingRight = 0;
			}
		}
		var width = controlWidth - paddingLeft - paddingRight;

		jQuery("#" + footerSliderId(qViewerId))
			.css("width", "100%")
			.css("height", TimelineFooterHeight.toString() + "px")
			.css("padding-left", (100 * paddingLeft / controlWidth).toString() + "%")
			.css("padding-right", (100 * paddingRight / controlWidth).toString() + "%");
		jQuery("#" + footerRangeId(qViewerId))
			.css("width", "100%")
			.css("height", TimelineFooterHeight.toString() + "px");
		jQuery("#" + footerLeftCursorId(qViewerId))
			.css("width", sliderCursorWidth.toString() + "px")
			.css("height", TimelineFooterHeight.toString() + "px");
		jQuery("#" + footerRightCursorId(qViewerId))
			.css("width", sliderCursorWidth.toString() + "px")
			.css("height", TimelineFooterHeight.toString() + "px");
		jQuery("#" + footerCenterId(qViewerId))
			.css("width", "calc(100% - " + 2 * sliderCursorWidth + "px)")
			.css("height", TimelineFooterHeight.toString() + "px")
			.css("left", sliderCursorWidth.toString() + "px");
	}

	function setMin(value, minValue)
	{
		return Math.max(value, minValue);
	}

	function setMax(value, maxValue) {
		return Math.min(value, maxValue);
	}

	function setMinAndMax(value, minValue, maxValue) {
		return setMax(setMin(value, minValue), maxValue);
	}

	function normalizedSliderOffset(qViewerId, event) {
		var paddingLeft = parseInt(jQuery("#" + footerSliderId(qViewerId)).css("padding-left"));
		var barWidth = parseInt(jQuery("#" + footerRangeId(qViewerId)).css("width"));
		var offsetX;
		if (event.originalEvent.touches)
			offsetX = event.originalEvent.touches[0].pageX - event.originalEvent.touches[0].target.offsetLeft;
		else
			offsetX = event.offsetX;
		switch (event.target.id) {
			case footerSliderId(qViewerId):
				return offsetX;
			case footerLeftCursorId(qViewerId):
				return offsetX + paddingLeft;
			case footerCenterId(qViewerId):
				return offsetX + paddingLeft;
			case footerRightCursorId(qViewerId):
				return offsetX + paddingLeft + barWidth - sliderCursorWidth;
		}
	}

	function attachSliderEvents(qViewerId) {

		function leftCursorMousedown(event) {
			event.preventDefault();
			sliderResizingLeft = true;
		}

		function rightCursorMousedown(event) {
			event.preventDefault();
			sliderResizingRight = true;
		}

		function centerMousedown(event) {
			event.preventDefault();
			if (!sliderResizingLeft && !sliderResizingRight)
				sliderMovingBar = true;
		}

		function sliderMousedown(event) {
			event.preventDefault();
			sliderClickedqViewerId = qViewerId;
			var slider = jQuery("#" + footerSliderId(sliderClickedqViewerId));
			var range = jQuery("#" + footerRangeId(sliderClickedqViewerId));
			var paddingLeft = parseInt(slider.css("padding-left"));
			sliderClickedOffsetX = normalizedSliderOffset(sliderClickedqViewerId, event);
			sliderClickedPaddingLeft = parseInt(slider.css("padding-left"));
			sliderClickedPaddingRight = parseInt(slider.css("padding-right"));
			sliderClickedContainerWidth = parseInt(slider.css("width"));
			sliderClickedRangeWidth = parseInt(range.css("width"));
		}

		function documentMouseup(event) {
			if (sliderMovingBar || sliderResizingRight || sliderResizingLeft) {
				var slider = jQuery("#" + footerSliderId(sliderClickedqViewerId));
				var qViewer = qv.collection[sliderClickedqViewerId];
				var compare = gx.dom.el(sliderClickedqViewerId + '_options_compare_enable').checked;
				var controlWidth = qViewer.getContainerControl().offsetWidth;
				var containerId;
				var containers;
				if (IsTimelineChart(qViewer))
					containerId = centerId(qViewer.userControlId());
				else
					containerId = qViewer.getContainerControl().id;
				if (IsSplittedChart(qViewer))
					containers = jQuery("[id^=" + containerId + "_chart]");
				else
					containers = jQuery("#" + containerId);
				var charts = [];
				jQuery.each(containers, function (index, div) {
					var chart = jQuery("#" + div.id).highcharts();
					charts.push(chart);
				});
				var firstChart = charts[0];
				var paddingLeft = parseInt(slider.css("padding-left"));
				var paddingRight = parseInt(slider.css("padding-right"));
				var minPercent = 100 * paddingLeft / controlWidth;
				var maxPercent = 100 * (1 - paddingRight / controlWidth);
				if (minPercent == 0 && maxPercent == 100)
					jQuery.each(charts, function (index, chart) {
						chart.zoomOut();
					});
				else {
					var extremes = firstChart.get('xaxis').getExtremes();
					var qvOptions = firstChart.options.qv;
					var minDate = qvOptions.dataMin + minPercent / 100 * (qvOptions.dataMax - qvOptions.dataMin);
					var maxDate = qvOptions.dataMin + maxPercent / 100 * (qvOptions.dataMax - qvOptions.dataMin);
					var redraw = (compare) ? false : true;
					jQuery.each(charts, function (index, chart) {
						chart.get('xaxis').setExtremes(minDate, maxDate, redraw);
					});
				}
				if (compare)
					GroupAndCompareFunction(charts);
				if (sliderResizingRight || sliderResizingLeft) {
					deselectZoom(prevClickedZoomId);
					prevClickedZoomId = null;
				}
			}
			sliderMovingBar = false;
			sliderResizingLeft = false;
			sliderResizingRight = false;
		}

		function sliderMousemove(event) {
			if (sliderMovingBar || sliderResizingRight || sliderResizingLeft) {
				var increment = normalizedSliderOffset(sliderClickedqViewerId, event) - sliderClickedOffsetX;
				if (sliderResizingLeft)
					increment = setMax(increment, sliderClickedRangeWidth - 2 * sliderCursorWidth);
				else
					increment = setMax(increment, sliderClickedPaddingRight);
				if (sliderResizingRight)
					increment = setMin(increment, -sliderClickedRangeWidth + 2 * sliderCursorWidth);
				else
					increment = setMin(increment, -sliderClickedPaddingLeft)
				if (increment != 0) {
					var center = jQuery("#" + footerCenterId(sliderClickedqViewerId));
					var slider = jQuery("#" + footerSliderId(sliderClickedqViewerId));
					var range = jQuery("#" + footerRangeId(sliderClickedqViewerId));
					if (sliderResizingLeft)
						slider.css("padding-left", (100 * (sliderClickedPaddingLeft + increment) / sliderClickedContainerWidth).toString() + "%");
					else if (sliderResizingRight)
						slider.css("padding-right", (100 * (sliderClickedPaddingRight - increment) / sliderClickedContainerWidth).toString() + "%");
					else {
						slider.css("padding-left", (100 * (sliderClickedPaddingLeft + increment) / sliderClickedContainerWidth).toString() + "%");
						slider.css("padding-right", (100 * (sliderClickedPaddingRight - increment) / sliderClickedContainerWidth).toString() + "%");
					}
				}

			}
		}

		var leftCursor = jQuery("#" + footerLeftCursorId(qViewerId));
		var rightCursor = jQuery("#" + footerRightCursorId(qViewerId));
		var center = jQuery("#" + footerCenterId(qViewerId));
		var slider = jQuery("#" + footerSliderId(qViewerId));
		var range = jQuery("#" + footerRangeId(qViewerId));

		// Attachments for mouse events
		leftCursor.mousedown(function (event) {
			leftCursorMousedown(event);
		});
		rightCursor.mousedown(function (event) {
			rightCursorMousedown(event);
		});
		center.mousedown(function (event) {
			centerMousedown(event);
		});
		slider.mousedown(function (event) {
			sliderMousedown(event);
		});
		jQuery(document).mouseup(function (event) {
			documentMouseup(event);
		});
		slider.mousemove(function (event) {
			sliderMousemove(event);
		});


		// Attachments for finger events
		leftCursor.on("touchstart", function (event) {
			leftCursorMousedown(event);
		});
		rightCursor.on("touchstart", function (event) {
			rightCursorMousedown(event);
		});
		center.on("touchstart", function (event) {
			centerMousedown(event);
		});
		slider.on("touchstart", function (event) {
			sliderMousedown(event);
		});
		jQuery(document).on("touchend", function (event) {
			documentMouseup(event);
		});
		jQuery(document).on("touchcancel", function (event) {
			documentMouseup(event);
		});
		slider.on("touchmove", function (event) {
			sliderMousemove(event);
		});

	}

	function CreateFooter(parent, qViewerId) {
		var div1 = qv.util.dom.createDiv(parent, footerSliderId(qViewerId), "gx-qv-footer-slider", "", {}, "");
		var div2 = qv.util.dom.createDiv(div1, footerRangeId(qViewerId), "gx-qv-footer-range", "", {}, "");
		qv.util.dom.createDiv(div2, footerLeftCursorId(qViewerId), "gx-qv-footer-left-cursor", "", {}, "");
		qv.util.dom.createDiv(div2, footerRightCursorId(qViewerId), "gx-qv-footer-right-cursor", "", {}, "");
		qv.util.dom.createDiv(div2, footerCenterId(qViewerId), "gx-qv-footer-center", "", {}, "");
		qv.util.dom.createDiv(parent, footerChartId(qViewerId), "gx-qv-footer-chart", "", {}, "");
	}

	function getTimelineFooterChartOptions(qViewer, arrOptions)
	{
		var containerId = footerChartId(qViewer.userControlId());
		var chartType = (qViewer.RealChartType == QueryViewerChartType.SmoothTimeline ? "spline" : "line");
		var step = qViewer.RealChartType == QueryViewerChartType.StepTimeline ? "left" : "";
		var series = [];
		if (!IsSplittedChart(qViewer))
			for (var i = 0; i < arrOptions[0].series.length; i++)
				series.push(arrOptions[0].series[i]);
		else {
			for (var i = 0; i < arrOptions.length; i++)
				series.push(arrOptions[i].series[0]);
		}
		return qv.chart.getSparklineChartOptions(qViewer, containerId, chartType, false, step, series);
	}

	function GroupAndCompareFunction(charts) {
		var firstChart = charts[0];
		var viewerId = firstChart.options.qv.viewerId;
		var qViewer = qv.collection[viewerId];

		// Chequea si esta marcado el chkbox que indica que se quiere comparar
		var compare = gx.dom.el(viewerId + "_options_compare_enable").checked;
		jQuery.each(charts, function (index, chart) {
			chart.options.qv.comparing = compare;
		});

		// Obtiene el tipo de periodo contra el que se quiere comparar
		var extremes = firstChart.get('xaxis').getExtremes();
		if (extremes.userMin != undefined)
			extremes.min = extremes.userMin;	//Sin esto, la llamada a setextremes (con redraw en false) realizado en el zoom no actualiza el min hasta el próximo dibujado.
		if (extremes.userMax != undefined)
			extremes.max = extremes.userMax;	//Sin esto, la llamada a setextremes (con redraw en false) realizado en el zoom no actualiza el min hasta el próximo dibujado.

		if (compare) {

			var options = gx.dom.el(viewerId + "_options_compare_options").children;
			var selectedOptionValue;
			for (ind = 0; options[ind] != undefined && selectedOptionValue == undefined; ind++) {
				if (options[ind].selected) {
					selectedOptionValue = options[ind].value;
				}
			}
			var minDateCompare;
			var maxDateCompare;
			if (selectedOptionValue == 'PrevPeriod') {
				maxDateCompare = new Date(extremes.min);
				minDateCompare = new Date(extremes.min - (extremes.max - extremes.min));
			} else if (selectedOptionValue == 'PrevYear') {
				minDateCompare = new Date(extremes.min);
				minDateCompare = new Date(minDateCompare.setFullYear(minDateCompare.getFullYear() - 1));
				maxDateCompare = new Date(extremes.max);
				maxDateCompare = new Date(maxDateCompare.setFullYear(maxDateCompare.getFullYear() - 1));
			}
			minDateCompare = minDateCompare.getTime();
			maxDateCompare = maxDateCompare.getTime();

			hideZoom(viewerId + "_Zoom_0m");//Si esta habilitada la comparación oculto el zoom all
		}
		else
			showZoom(viewerId + "_Zoom_0m");

		// Elimina todas las series existentes de la grafica
		jQuery.each(charts, function (index, chart) {
			var series_colorIndexes = []
			while (chart.series.length > 0) {
				if (!chart.options.qv.colorIndexesUsed) {
					series_colorIndexes.push(chart.series[0].colorIndex);
				}
				chart.series[0].remove(true)
			}
			if (!chart.options.qv.colorIndexesUsed) {
				chart.options.qv.colorIndexesUsed = series_colorIndexes;
			}
		});

		// Carga las series con los datos que correspondan
		var ns = 0
		for (var serieName in qViewer.Chart.Series.ByName) {
			var chartSerie = qViewer.Chart.Series.ByName[serieName];
			var chart;
			var serieColorIndex;
			if (IsSplittedChart(qViewer)) {
				chart = charts[ns];
				serieColorIndex = chart.options.qv.colorIndexesUsed[0];
			}
			else {
				chart = firstChart;
				serieColorIndex = chart.options.qv.colorIndexesUsed[ns];
			}

			// Serie con el periodo seleccionado por el usuario
			var serie1 = {};
			serie1.turboThreshold = 0;
			serie1.colorIndex = serieColorIndex;
			serie1.id = serieName + "1";
			serie1.name = serieName;
			serie1.data = [];

			if (compare) {
				// Serie con el periodo contra el que se va a comparar
				var serie2 = {};
				serie2.className = "highcharts-dashed-series-line";
				serie2.turboThreshold = 0;
				serie2.colorIndex = serieColorIndex;
				serie2.id = serieName + "2";
				serie2.name = serieName;
				serie2.data = [];
			}

			var points;
			var groupOption = document.getElementById(viewerId + "_options_group_options").value;
			points = groupPoints(qViewer.Chart.Categories, chartSerie.Points, qv.util.XAxisDataType(qViewer), chartSerie.Aggregation, groupOption);

			for (i = 0; i < points.length; i++) {
				var value = points[i].y;
				var date = new gx.date.gxdate(points[i].x, "Y4MD");
				var name = points[i].name;
				var timeValue1 = date.Value.getTime() - date.Value.getTimezoneOffset() * 60000;
				var original_time_value = date.Value.getTime() - date.Value.getTimezoneOffset() * 60000;
				if (compare) {
					var addToSerie1 = false;
					var addToSerie2 = false;
					if (timeValue1 <= extremes.max && timeValue1 >= extremes.min) {
						addToSerie1 = true;
					}
					if (timeValue1 <= maxDateCompare && timeValue1 >= minDateCompare) {
						addToSerie2 = true;
						var tmpDate = new Date(timeValue1);
						var timeValue2;
						if (selectedOptionValue == "PrevPeriod") {
							if (chart.options.qv.predef_zoom == "1m") {
								timeValue2 = tmpDate.setMonth(tmpDate.getMonth() + 1);
							} else if (chart.options.qv.predef_zoom == "2m") {
								timeValue2 = tmpDate.setMonth(tmpDate.getMonth() + 2);
							} else if (chart.options.qv.predef_zoom == "3m") {
								timeValue2 = tmpDate.setMonth(tmpDate.getMonth() + 3);
							} else if (chart.options.qv.predef_zoom == "6m") {
								timeValue2 = tmpDate.setMonth(tmpDate.getMonth() + 6);
							} else if (chart.options.qv.predef_zoom == "12m") {
								timeValue2 = tmpDate.setFullYear(tmpDate.getFullYear() + 1);
							} else {
								timeValue2 += extremes.max - extremes.min;
							}
						} else if (selectedOptionValue == "PrevYear") {
							timeValue2 = tmpDate.setFullYear(tmpDate.getFullYear() + 1);
						}
					}
					if (addToSerie1) {
						var point = {};
						point.x = timeValue1;
						point.y = value;
						point.name = name;
						serie1.data.push(point);
					}
					if (addToSerie2) {
						var point = {};
						point.x = timeValue2;
						point.y = value;
						point.name = name;
						point.real_x = original_time_value;
						serie2.data.push(point);
					}
				} else {
					serie1.data.push({ x: timeValue1, y: value, name: name });
				}
			}

			chart.addSeries(serie1);
			if (compare) {
				chart.addSeries(serie2);
			}
			ns++;
		}

	}

	function getSuitableZoomFactor(points, maxPoints) {
		if (points.length < maxPoints)
			return 0;
		else {
			var maxValue = points[points.length - 1].x;
			var minValue = points[points.length - maxPoints].x;
			var cantMeses = (maxValue - minValue) / 1000 / 3600 / 24 / (365 / 12);
			if (cantMeses <= 1)
				return 1;
			else if (cantMeses <= 2)
				return 2;
			else if (cantMeses <= 3)
				return 3;
			else if (cantMeses <= 6)
				return 6;
			else
				return 12;
		}
	}

	function FillHeaderAndFooter(charts, arrOptions)
	{

		function CreateGroupByCombo(parent, qViewer, showYears, showSemesters, showQuarters, showMonths, showWeeks, showHours, showMinutes) {
			var qViewerId = qViewer.userControlId();
			var select = qv.util.dom.createSelect(parent, optionsId(qViewerId) + "_group_options");
			if (qv.util.XAxisDataType(qViewer) == QueryViewerDataType.DateTime) {
				qv.util.dom.createOption(select, "Seconds", qViewer._groupOption == "Seconds", gx.getMessage("GXPL_QViewerSeconds"));
				if (showMinutes) {
					qv.util.dom.createOption(select, "Minutes", qViewer._groupOption == "Minutes", gx.getMessage("GXPL_QViewerMinutes"));
					if (showHours) {
						qv.util.dom.createOption(select, "Hours", qViewer._groupOption == "Hours", gx.getMessage("GXPL_QViewerHours"));
					}
				}
			}
			if (showDays || qv.util.XAxisDataType(qViewer) == QueryViewerDataType.Date) {
				qv.util.dom.createOption(select, "Days", qViewer._groupOption == "Days", gx.getMessage("GXPL_QViewerDays"));
				if (showWeeks) {
					qv.util.dom.createOption(select, "Weeks", qViewer._groupOption == "Weeks", gx.getMessage("GXPL_QViewerWeeks"));
					if (showMonths) {
						qv.util.dom.createOption(select, "Months", qViewer._groupOption == "Months", gx.getMessage("GXPL_QViewerMonths"));
						if (showQuarters) {
							qv.util.dom.createOption(select, "Quarters", qViewer._groupOption == "Quarters", gx.getMessage("GXPL_QViewerQuarters"));
							if (showSemesters) {
								qv.util.dom.createOption(select, "Semesters", qViewer._groupOption == "Semesters", gx.getMessage("GXPL_QViewerSemesters"));
								if (showYears) {
									qv.util.dom.createOption(select, "Years", qViewer._groupOption == "Years", gx.getMessage("GXPL_QViewerYears"));
								}
							}
						}
					}
				}
			}
			return select;
		}

		function CreateHeader(parent, qViewer, include1m, include2m, include3m, include6m, include1y, showYears, showSemesters, showQuarters, showMonths, showWeeks, showHours, showMinutes) {

			function CreateHeaderGroup(parent) {
				return qv.util.dom.createDiv(parent, "", "QVTimelineHeaderGroup", "", { height: TimelineHeaderHeight + "px", flexGrow: 1 }, "");
			}

			function CreateZoomItem(parent, text, id) {
				return qv.util.dom.createAnchor(parent, id, { cursor: "pointer", paddingLeft: "6px" }, text);
			}

			var qViewerId = qViewer.userControlId();
			var divFlexTable = qv.util.dom.createDiv(parent, "", "QVTimelineHeader", "", { display: "flex", flexDirection: "row", flexWrap: "wrap" }, "");
			var headerGroup1 = CreateHeaderGroup(divFlexTable);
			var headerGroup2 = CreateHeaderGroup(divFlexTable);
			var headerGroup3 = CreateHeaderGroup(divFlexTable);

			var headerGroup1Div = qv.util.dom.createDiv(headerGroup1, "", "", "", { "float": "left" }, "");

			// Zoom
			qv.util.dom.createText(headerGroup1Div, gx.getMessage("GXPL_QViewerJSChartZoom"));
			if (include1m) {
				CreateZoomItem(headerGroup1Div, gx.getMessage("GXPL_QViewerJSChartZoomLevelToOneMonth"), qViewerId + "_Zoom_1m");
				if (include2m) {
					CreateZoomItem(headerGroup1Div, gx.getMessage("GXPL_QViewerJSChartZoomLevelToTwoMonth"), qViewerId + "_Zoom_2m");
					if (include3m) {
						CreateZoomItem(headerGroup1Div, gx.getMessage("GXPL_QViewerJSChartZoomLevelToThreeMonth"), qViewerId + "_Zoom_3m");
						if (include6m) {
							CreateZoomItem(headerGroup1Div, gx.getMessage("GXPL_QViewerJSChartZoomLevelToSixMonth"), qViewerId + "_Zoom_6m");
							if (include1y) {
								CreateZoomItem(headerGroup1Div, gx.getMessage("GXPL_QViewerJSChartZoomLevelToOneYear"), qViewerId + "_Zoom_12m");
							}
						}
					}
				}
			}
			CreateZoomItem(headerGroup1Div, gx.getMessage("GXPL_QViewerJSChartZoomLevelToAll"), qViewerId + "_Zoom_0m");

			// Group by
			var headerGroup2Div = qv.util.dom.createDiv(headerGroup2, "", "", "", { "float": "right", paddingLeft: "12px" }, "");
			qv.util.dom.createSpan(headerGroup2Div, optionsId(qViewerId) + "_groupby_text", "", "", { whiteSpace: "nowrap", paddingRight: "6px" }, null, gx.getMessage("GXPL_QViewerGroupBy"));
			CreateGroupByCombo(headerGroup2Div, qViewer, showYears, showSemesters, showQuarters, showMonths, showWeeks, showHours, showMinutes);

			// Compare to
			var headerGroup3Div = qv.util.dom.createDiv(headerGroup3, "", "", "", { "float": "right", paddingLeft: "12px" }, "");
			qv.util.dom.createInput(headerGroup3Div, optionsId(qViewerId) + "_compare_enable", "checkbox", { verticalAlign: "sub" });
			qv.util.dom.createSpan(headerGroup3Div, optionsId(qViewerId) + "_compare_text", "", "", { whiteSpace: "nowrap", paddingRight: "6px", paddingLeft: "6px" }, null, gx.getMessage("GXPL_QViewerCompareWith"));
			var select = qv.util.dom.createSelect(headerGroup3Div, optionsId(qViewerId) + "_compare_options");
			qv.util.dom.createOption(select, "PrevPeriod", false, gx.getMessage("GXPL_QViewerPreviousPeriod"));
			qv.util.dom.createOption(select, "PrevYear", false, gx.getMessage("GXPL_QViewerPreviousYear"));

			return divFlexTable;
		}

		// Crea un nuevo div conteniendo los links para hacer zoom predefenidos programaticamente.
		var firstChart = charts[0];
		viewerId = firstChart.options.qv.viewerId;
		var qViewer = qv.collection[viewerId];
		var divOptions = document.getElementById(optionsId(viewerId));
		var divFooter = document.getElementById(footerId(viewerId));

		var extremes = firstChart.get('xaxis').getExtremes();
		var winTime = extremes.dataMax - extremes.dataMin;
		jQuery.each(charts, function (index, chart) {
			chart.options.qv.dataMax = extremes.dataMax;
			chart.options.qv.dataMin = extremes.dataMin;
		});
		var moreThanOneMonth = winTime > 30.42 * 24 * 3600 * 1000;
		var moreThanTwoMonths = winTime > 60.83 * 24 * 3600 * 1000;
		var moreThanThreeMonths = winTime > 91.25 * 24 * 3600 * 1000;
		var moreThanSixMonths = winTime > 182.5 * 24 * 3600 * 1000;
		var moreThanOneYear = winTime > 365 * 24 * 3600 * 1000;

		qViewer._groupOption = qViewer._groupOption || (qv.util.XAxisDataType(qViewer) == QueryViewerDataType.Date ? "Days" : "Seconds")

		var minDate = new gx.date.gxdate('');
		var maxDate = new gx.date.gxdate('');
		minDate.Value.setTime(extremes.dataMin + minDate.Value.getTimezoneOffset() * 60000);
		maxDate.Value.setTime(extremes.dataMax + maxDate.Value.getTimezoneOffset() * 60000);

		var showYears = true;
		var showSemesters = true;
		var showQuarters = true;
		var showMonths = true;
		var showWeeks = true;
		var showDays = true;
		var showHours = true;
		var showMinutes = true;

		if (gx.date.year(minDate) == gx.date.year(maxDate)) {
			showYears = false;
			if (!(gx.date.month(minDate) <= 6 && gx.date.month(maxDate) >= 7)) {
				showSemesters = false;
				if (!((gx.date.month(minDate) <= 3 && gx.date.month(maxDate) >= 4) || (gx.date.month(minDate) <= 6 && gx.date.month(maxDate) >= 7) || (gx.date.month(minDate) <= 9 && gx.date.month(maxDate) >= 10))) {
					showQuarters = false;
					if (gx.date.month(minDate) == gx.date.month(maxDate)) {
						showMonths = false;
						if ((gx.date.day(minDate) - gx.date.dow(minDate)) == (gx.date.day(maxDate) - gx.date.dow(maxDate))) {
							showWeeks = false;
							if (gx.date.day(minDate) == gx.date.day(maxDate)) {
								showDays = false;
								if (gx.date.hour(minDate) == gx.date.hour(maxDate)) {
									showHours = false;
									if (gx.date.minute(minDate) == gx.date.minute(maxDate)) {
										showMinutes = false;
									}
								}
							}
						}
					}
				}
			}
		}


		CreateHeader(divOptions, qViewer, moreThanOneMonth, moreThanTwoMonths, moreThanThreeMonths, moreThanSixMonths, moreThanOneYear, showYears, showSemesters, showQuarters, showMonths, showWeeks, showHours, showMinutes);
		CreateFooter(divFooter, viewerId);
		attachSliderEvents(viewerId);

		////////////////////////////////////////////////////////////////////////////////////////////
		// Event handlers para las opciones de "compare to past"
		////////////////////////////////////////////////////////////////////////////////////////////

		gx.dom.el(divOptions.id + '_compare_enable').onclick = function () {
			GroupAndCompareFunction(charts);
		};
		gx.dom.el(divOptions.id + '_compare_options').onchange = function () {
			if (gx.dom.el(divOptions.id + '_compare_enable').checked) {
				GroupAndCompareFunction(charts);
			}
		};
		gx.dom.el(divOptions.id + '_group_options').onchange = function () {
			GroupAndCompareFunction(charts);
		};
		var doZoom = function (zoomFactor) {

			var compare = gx.dom.el(divOptions.id + '_compare_enable').checked;

			if (zoomFactor > 0) {

				var minDate, maxDate;
				var extremes = firstChart.get('xaxis').getExtremes();
				maxDate = extremes.dataMax;
				minDate = maxDate - 30.417 * zoomFactor * 24 * 3600 * 1000; //30.4166 = 365dias/12meses
				var redraw = (compare) ? false : true;
				jQuery.each(charts, function (index, chart) {
					chart.get('xaxis').setExtremes(minDate, maxDate, redraw);
				});
				var qvOptions = firstChart.options.qv;
				var minPercent = (minDate - qvOptions.dataMin) / (qvOptions.dataMax - qvOptions.dataMin) * 100;
				var maxPercent = (maxDate - qvOptions.dataMin) / (qvOptions.dataMax - qvOptions.dataMin) * 100;
				InitializeSlider(firstChart.options.qv.viewerId, minPercent, maxPercent);

				EnableCompareControls(firstChart.options.qv.viewerId, true);
			}
			else //Zoom All
			{
				jQuery.each(charts, function (index, chart) {
					chart.zoomOut();
				});
				DisableCompareControls(firstChart.options.qv.viewerId, false);
			}

			//Resalto el selector de zoom seleccionado
			deselectZoom(prevClickedZoomId);
			selectZoom(this.id);

			jQuery.each(charts, function (index, chart) {
				chart.options.qv.predef_zoom = zoomFactor + "m";
			});
			prevClickedZoomId = this.id;

			//Si esta habilitada la comparación recalculo las fechas
			if (compare)
				GroupAndCompareFunction(charts);

		};

		////////////////////////////////////////////////////////////////////////////////////////////		
		// Carga los links de zooms con los event handlers necesarios
		// Zoom automatico a x meses		
		var array_zooms = [0, 1, 2, 3, 6, 12];
		for (var index in array_zooms) {
			var x = array_zooms[index];
			var zoomXm = gx.dom.el(viewerId + "_Zoom_" + x + "m");
			if (zoomXm) {
				zoomXm.onclick = doZoom.closure(zoomXm, [x]);
			}
		}
		////////////////////////////////////////////////////////////////////////////////////////////

		// Al final, se muestra un rango de fechas que despliegue un máximo de 20 puntos
		var zoomFactor = getSuitableZoomFactor(firstChart.series[0].points, 20);
		triggerZoom(zoomFactor);

		var footerChartOptions = getTimelineFooterChartOptions(qViewer, arrOptions);
		var FooterHCChart = new Highcharts.Chart(footerChartOptions);	// Agrego la gráfica del footer


	}

	//This function execute when the Highcharts object is finished loading and rendering.
	function HCFinishedLoadingCallback(chart) {

		var qViewer = qv.collection[chart.options.qv.viewerId];
		if (!IsDatetimeXAxis(qViewer)) {
			var selectionAllowed = qViewer.SelectionAllowed();
			var raiseItemClick = false;
			if (qViewer.ItemClick)
				for (var i = 0; i < qViewer.Metadata.Axes.length; i++)
					if (qViewer.Metadata.Axes[i].RaiseItemClick) {
						raiseItemClick = true;
						break;
					}
			if (raiseItemClick || selectionAllowed) {
				// Asocia el manejador para el evento click sobre el eje x
				jQuery.each(chart.xAxis[0].ticks, function (tick_index, tick) {
					if (tick && tick.label) {
						if (raiseItemClick)
							tick.label.addClass("gx-qv-clickable-element");
						tick.label.on("click", function (event) {
							onHighchartsXAxisClickEventHandler(event, tick_index, tick, chart, raiseItemClick, selectionAllowed);
						});
					}
				});
			}
		}
	}

	// ---------------------------------------------------- end Timeline ----------------------------------------------------

	return {

		tryToRenderChart: function (qViewer) {
			var errMsg = "";

			// Ejecuto el primer servicio y verifico que no haya habido error
			var d1 = new Date();
			var t1 = d1.getTime();

			qViewer.xml = qViewer.xml || {};

			qv.services.GetRecordsetCacheKeyIfNeeded(qViewer, function (resText, qViewer) {			// Servicio GetRecordsetCacheKey
				var newRecordsetCacheKey = false;
				if (resText != qViewer.xml.recordsetCacheKey) {
					qViewer.xml.recordsetCacheKey = resText;
					newRecordsetCacheKey = true;
				}
				if (!qv.util.anyError(resText)) {
					if (newRecordsetCacheKey)
						qv.services.parseRecordsetCacheKeyXML(qViewer);

					qv.services.GetMetadataIfNeeded(qViewer, function (resText, qViewer) {			// Servicio GetMetadata
						var newMetadata = false;
						if (resText != qViewer.xml.metadata) {
							qViewer.xml.metadata = resText;
							newMetadata = true;
						}
						var d2 = new Date();
						var t2 = d2.getTime();
						if (!qv.util.anyError(resText)) {
							if (newMetadata)
								qv.services.parseMetadataXML(qViewer);

							if (qViewer.Metadata.ParserResult.Success) {
								qv.services.GetDataIfNeeded(qViewer, function (resText, qViewer) {		// Servicio GetData
									var newData = false;
									if (resText != qViewer.xml.data) {
										qViewer.xml.data = resText;
										newData = true;
									}
									var d3 = new Date();
									var t3 = d3.getTime();
									if (!qv.util.anyError(resText)) {
										if (newData)
											qv.services.parseDataXML(qViewer);
										renderChart(qViewer);
									} else {
										// Error en el servicio GetData
										errMsg = qv.util.getErrorFromText(resText);
										qv.util.renderError(qViewer, errMsg);
									}
								});
							}
							else
								qv.util.renderError(qViewer, qViewer.Metadata.ParserResult.Message);
						}
						else {
							// Error en el servicio GetMetadata
							errMsg = qv.util.getErrorFromText(resText);
							qv.util.renderError(qViewer, errMsg);
						}
					});

				}
				else {
					// Error en el servicio GetRecordsetCachekey
					errMsg = qv.util.getErrorFromText(resText);
					qv.util.renderError(qViewer, errMsg);
				}
			});

		},

		GetMetadataChart: function (qViewer) {

			function UpdateMetadata(qViewer, designtimeMetadata) {

				function UpdateField(designtimeMetadata, name, visible) {
					for (var i = 0; i < designtimeMetadata.length; i++) {
						var element = designtimeMetadata[i];
						if (element.Name == name) {
							element.Visible = visible ? QueryViewerVisible.Yes : QueryViewerVisible.No;
							break;
						}
					}
				}
				
				if (qViewer.PlotSeries == QueryViewerPlotSeries.InTheSameChart)
					for (var serieIndex = 0; serieIndex < qViewer.Charts[0].series.length; serieIndex++) {
						var serie = qViewer.Charts[0].series[serieIndex];
						var name = qViewer.Chart.Series.ByIndex[serieIndex].FieldName;
						UpdateField(designtimeMetadata, name, serie.visible);
					}
				else
					for (var serieIndex = 0; serieIndex < qViewer.Charts.length; serieIndex++) {
						var serie = qViewer.Charts[serieIndex].series[0];
						var name = qViewer.Chart.Series.ByIndex[serieIndex].FieldName;
						UpdateField(designtimeMetadata, name, serie.visible);
					}
			}
			
			var designtimeMetadata = qv.util.GetDesigntimeMetadata(qViewer);
			UpdateMetadata(qViewer, designtimeMetadata);
			return designtimeMetadata;
		},

		GetRuntimeMetadata: function(qViewer) {
		
			function GetRuntimeElement(name, type, axis, position, visible) {
				return { Name: name, Type: type, Axis: axis, Position: position, Hidden: visible == QueryViewerVisible.Never || element.Visible == QueryViewerVisible.No};
			}
			
			var elements = [];
			var lastPosition;
			lastPosition = 0;
			for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
				var element = qViewer.Metadata.Axes[i];
				if (element.Visible != QueryViewerVisible.Never)
					lastPosition += 1;
				elements.push(GetRuntimeElement(element.Name, QueryViewerElementType.Axis, element.Axis, element.Visible != QueryViewerVisible.Never ? lastPosition : null, element.Visible));
			}
			lastPosition = 0;
			for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
				var element = qViewer.Metadata.Data[i];
				if (element.Visible != QueryViewerVisible.Never)
					lastPosition += 1;
				elements.push(GetRuntimeElement(element.Name, QueryViewerElementType.Datum, "", element.Visible != QueryViewerVisible.Never ? lastPosition : null, element.Visible));
			}
			if (qViewer.PlotSeries == QueryViewerPlotSeries.InTheSameChart)
				for (var serieIndex = 0; serieIndex < qViewer.Charts[0].series.length; serieIndex++) {
					var serie = qViewer.Charts[0].series[serieIndex];
					var name = qViewer.Chart.Series.ByIndex[serieIndex].FieldName;
					element = qv.util.GetElementInCollection(elements, "Name", name);
					element.Hidden = !serie.visible;
				}
			else
				for (var serieIndex = 0; serieIndex < qViewer.Charts.length; serieIndex++) {
					var serie = qViewer.Charts[serieIndex].series[0];
					var name = qViewer.Chart.Series.ByIndex[serieIndex].FieldName;
					element = qv.util.GetElementInCollection(elements, "Name", name);
					element.Hidden = !serie.visible;
				}
			return elements;
		},
		
		GetDataChart: function (qViewer) {
			return qViewer.xml.data;
		},

		getSparklineChartOptions: function (qViewer, containerId, chartType, noBackground, step, series) {
			var options = {};
			options.chart = {};
			options.chart.renderTo = containerId;
			options.chart.margin = 0;
			options.chart.type = chartType;
			options.chart.styledMode = true;
			if (noBackground)
				options.chart.className = "highcharts-no-background";		// Clase no estándar de Highcharts
			options.title = {};
			options.title.text = "";
			options.credits = {};
			options.credits.enabled = false;
			options.xAxis = {};
			options.xAxis.visible = false;
			options.yAxis = {};
			options.yAxis.visible = false;
			options.legend = {};
			options.legend.enabled = false;
			options.tooltip = {};
			options.tooltip.enabled = false;
			options.plotOptions = {};
			options.plotOptions.series = {};
			options.plotOptions.series.animation = false;
			options.plotOptions.series.connectNulls = true;
			options.plotOptions.series.enableMouseTracking = false;
			options.plotOptions.series.lineWidth = 1;
			options.plotOptions.series.shadow = false;
			options.plotOptions.series.states = {};
			options.plotOptions.series.states.hover = {};
			options.plotOptions.series.states.hover.lineWidth = 1;
			options.plotOptions.series.marker = {};
			options.plotOptions.series.marker.radius = 0;
			options.plotOptions.series.marker.states = {};
			options.plotOptions.series.marker.states.hover = {};
			options.plotOptions.series.marker.states.hover.radius = 2;
			if (step != "")
				options.plotOptions.series.step = step;
			options.series = []
			for (var i = 0; i < series.length; i++)
				options.series.push(series[i]);
			return options;
		},

		IsDatetimeXAxis: function (qViewer) {
			return IsDatetimeXAxis(qViewer);
		},

		IsSingleSerieChart: function (qViewer) {
			return IsSingleSerieChart(qViewer);
		},
		
		IsAreaChart: function (qViewer) {
			return IsAreaChart(qViewer);
		},
		
		IsLineChart: function (qViewer) {
			return IsLineChart(qViewer);
		},
		
		IsSplittedChart: function (qViewer) {
			return IsSplittedChart(qViewer);
		},

		IsGaugeChart: function (qViewer) {
			return IsGaugeChart(qViewer);
		},

		Select: function (qViewer, selection) {
			var rowsToSelect = GetRowsToSelect(qViewer, selection);
			if (rowsToSelect.length > 0)
				SelectChartsPoints(qViewer.Charts, rowsToSelect);
			else
				DeselectChartsPoints(qViewer.Charts);
		},

		Deselect: function (qViewer) {
			DeselectChartsPoints(qViewer.Charts);
		}

	}

})()
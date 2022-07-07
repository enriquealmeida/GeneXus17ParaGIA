qv.card = (function () {

	function selectStyle(datum, value) {

		if (datum.ConditionalStyles.length == 0)
			return qv.util.css.parseStyle(datum.Style);
		else {
			for (var i = 0; i < datum.ConditionalStyles.length; i++) {
				var conditionalStyle = datum.ConditionalStyles[i];
				if (qv.util.satisfyStyle(value, conditionalStyle))
					return (qv.util.css.parseStyle(conditionalStyle.StyleOrClass));
			}
			return qv.util.css.parseStyle(datum.Style);
		}
	}

	function analizeSeries(qViewer, datum, xDataField, xDataType) {

		function analizeMain(start, regressionStart, xDataField, yDataField, rows, xDataType) {
			var minValue = null;
			var maxValue = null;
			var minWhen = null;
			var maxWhen = null;
			var chartSeriesData = [];
			var lr = {AnyTrend: false};
			if (xDataField != "") {
				var end = rows.length - 1;
				var n = 0;
				var sum_x = 0;
				var sum_y = 0;
				var sum_xy = 0;
				var sum_xx = 0;
				var sum_yy = 0;
				var regressionStartDate = null;
				var regressionStartY = null;
				for (var i = start; i <= end; i++) {
					if (rows[i][xDataField] != undefined && rows[i][yDataField] != undefined) {
						n += 1;
						var date = new gx.date.gxdate(rows[i][xDataField], "Y4MD");
						var yValue = parseFloat(rows[i][yDataField]);
						chartSeriesData.push({ x: date.Value.getTime() - date.Value.getTimezoneOffset() * 60000, y: yValue });
						if (minValue == null && maxValue == null) {
							minValue = yValue;
							maxValue = yValue;
							minWhen = rows[i][xDataField];
							maxWhen = rows[i][xDataField];
						}
						else {
							if (yValue > maxValue) {
								maxWhen = rows[i][xDataField];
								maxValue = yValue;
							}
							if (yValue < minValue) {
								minWhen = rows[i][xDataField];
								minValue = yValue;
							}
						}
						if (i >= regressionStart) {
							if (regressionStartDate == null && regressionStartY == null) {
								regressionStartDate = date;
								regressionStartY = yValue;
							}
							// Cambio de variable para no manejar números tan grandes
							var x = xDataType == QueryViewerDataType.Date ? gx.date.daysDiff(date, regressionStartDate) : gx.date.secDiff(date, regressionStartDate);
							var y = yValue - regressionStartY;
							sum_x += x;
							sum_y += y;
							sum_xy += x * y;
							sum_xx += x * x;
							sum_yy += y * y;
						}
					}
				}
				lr.AnyTrend = n > 1;
				lr.Slope = (n * sum_xy - sum_x * sum_y) / (n * sum_xx - sum_x * sum_x);
				lr.Intercept = (sum_y - lr.Slope * sum_x) / n;
				lr.R2 = Math.pow((n * sum_xy - sum_x * sum_y) / Math.sqrt((n * sum_xx - sum_x * sum_x) * (n * sum_yy - sum_y * sum_y)), 2);
			}
			var data = {};
			data.LinearRegression = lr;
			data.MinValue = minValue;
			data.MinWhen = minWhen;
			data.MaxValue = maxValue;
			data.MaxWhen = maxWhen;
			data.ChartSeriesData = chartSeriesData;
			return data;
		}

		function getRegressionStartDateStr(trendPeriod, xDataType) {
			var endDate = gx.date.now();
			var startDate;
			switch (trendPeriod) {
				case QueryViewerTrendPeriod.LastYear:
					startDate = gx.date.addyr(endDate, -1);
					break;
				case QueryViewerTrendPeriod.LastSemester:
					startDate = gx.date.addmth(endDate, -6);
					break;
				case QueryViewerTrendPeriod.LastQuarter:
					startDate = gx.date.addmth(endDate, -3);
					break;
				case QueryViewerTrendPeriod.LastMonth:
					startDate = gx.date.addmth(endDate, -1);
					break;
				case QueryViewerTrendPeriod.LastWeek:
					startDate = gx.date.dtadd(endDate, -86400 * 7);
					break;
				case QueryViewerTrendPeriod.LastDay:
					startDate = gx.date.dtadd(endDate, -86400);
					break;
				case QueryViewerTrendPeriod.LastHour:
					startDate = gx.date.dtadd(endDate, -3600);
					break;
				case QueryViewerTrendPeriod.LastMinute:
					startDate = gx.date.dtadd(endDate, -60);
					break;
				case QueryViewerTrendPeriod.LastSecond:
					startDate = gx.date.dtadd(endDate, -1);
					break;
			}
			var startDateStr = qv.util.dateToString(startDate, xDataType == QueryViewerDataType.DateTime);
			return startDateStr;
		}

		// Busco un eje de tipo date o datetime
		var data = {};
		var noTrend = { AnyTrend: false };
		var regressionStart;
		if (qViewer.IncludeTrend) {
			if (qViewer.TrendPeriod == QueryViewerTrendPeriod.SinceTheBeginning || qViewer.TrendPeriod == "")
				regressionStart = 0;
			else {
				var trendStartDate = getRegressionStartDateStr(qViewer.TrendPeriod, xDataType);
				regressionStart = qViewer.Data.Rows.length - 1;
				for (var i = qViewer.Data.Rows.length - 2; i >= 0; i--) {
					var currentDate = qViewer.Data.Rows[i][xDataField];
					if (currentDate < trendStartDate)
						break;
					regressionStart = i;
				}
			}
		}
		else
			regressionStart = qViewer.Data.Rows.length - 1;	// Start = End para que no calcule regresión lineal
		var start = qViewer.IncludeSparkline || qViewer.IncludeMaxAndMin ? 0 : regressionStart;
		data = analizeMain(start, regressionStart, xDataField, datum.DataField, qViewer.Data.Rows, xDataType);
		return data;
	}

	function setEventData(eventData, qViewer, name, allRows) {

		function getContext(qViewer, allRows) {

			function getElementValues(dataField, rows, allRows) {
				var values = [];
				if (allRows)
					for (var i = 0; i < rows.length; i++)
						values.push(rows[i][dataField]);
				else
					values.push(rows[rows.length - 1][dataField]);
				return values;
			}

			var context = [];
			for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
				var axis = qViewer.Metadata.Axes[i];
				if (!axis.IsComponent) {
					var element = {};
					element.Name = axis.Name;
					element.Values = getElementValues(axis.DataField, qViewer.Data.Rows, allRows);
					context.push(element);
				}
			}
			for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
				var datum = qViewer.Metadata.Data[i];
				if (!datum.IsComponent) {
					var element = {};
					element.Name = datum.Name;
					element.Values = getElementValues(datum.DataField, qViewer.Data.Rows, allRows);
					context.push(element);
				}
			}
			return context;
		}

		var clickedDatum = null;
		for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
			if (qViewer.Metadata.Data[i].Name == name) {
				clickedDatum = qViewer.Metadata.Data[i];
				break;
			}
		}
		if (clickedDatum != null) {
			eventData.Name = clickedDatum.Name;
			eventData.Type = QueryViewerElementType.Datum;
			eventData.Axis = "";
			var lastRow;
			if (allRows)
				lastRow = qv.util.aggregateData(qViewer.Metadata.Data, qViewer.Data.Rows);
			else
				lastRow = qViewer.Data.Rows[qViewer.Data.Rows.length - 1];
			eventData.Value = lastRow[clickedDatum.DataField];
			eventData.Context = getContext(qViewer, allRows);
		}
	}

	function sparklineChartId(qViewerId, i) {
		return qViewerId + "_card_sparkline_" + i;
	}

	function GetSparklineOptions(qViewer, seriesData, i) {
		var containerId = sparklineChartId(qViewer.userControlId(), i);
		var series = [{ data: seriesData }];
		return qv.chart.getSparklineChartOptions(qViewer, containerId, "line", true, "", series);
	}

	function getMinMaxTable(parent, text1, text2, text3) {
		var styleObj = { textAlign: "center", paddingTop: "0px", paddingRight: "5px", paddingBottom: "0px", paddingLeft: "5px" };
		var table = qv.util.dom.createTable(parent, "", {});
		var tr;
		var td;
		var span;
		tr = qv.util.dom.createRow(table);
		td = qv.util.dom.createCell(tr, 1, "", styleObj, "");
		span = qv.util.dom.createSpan(td, "", "qv-card-max-and-min-value", text2, {}, null, text1);
		tr = qv.util.dom.createRow(table);
		td = qv.util.dom.createCell(tr, 1, "", styleObj, "");
		span = qv.util.dom.createSpan(td, "", "qv-card-max-and-min-title", text2, {}, null, text3);
		return table;
	}

	function age(dateStr) {
		var date = new gx.date.gxdate(dateStr, "Y4MD");
		var now = gx.date.now();
		var seconds = gx.date.secDiff(now, date);
		return gx.getMessage("GXPL_QViewerTimeAgo").replace("{0}", qv.util.seconsdToText(seconds));
	}

	function valueOrPercentage(qViewer, valueStr, datum, decimals) {
		var value;
		var percentage;
		if (valueStr != "") {
			value = qv.util.formatNumber(parseFloat(valueStr), decimals, datum.Picture, false);
			percentage = qv.util.formatNumber(parseFloat(valueStr * 100 / datum.TargetValue), 2, "ZZZZZZZZZZZZZZ9.99", false) + '%';
		}
		else {
			value = "";
			percentage = "";
		}
		switch (qViewer.ShowDataAs) {
			case QueryViewerShowDataAs.Values:
				return value;
			case QueryViewerShowDataAs.Percentages:
				return percentage;
			case QueryViewerShowDataAs.ValuesAndPercentages:
				return value + ' (' + percentage + ')';
			default:
				return value;
		}
	}

	function renderCard(qViewer) {

		function TrendIcon(parent, qViewer, data) {
			var icon;
			if (data.LinearRegression.Slope > 0)
				icon = "keyboard_arrow_up";
			else if (data.LinearRegression.Slope < 0)
				icon = "keyboard_arrow_down";
			else
				icon = "drag_handle";
			var trendTooltip;
			if (qViewer.TrendPeriod == "")
				trendTooltip = gx.getMessage("GXPL_QViewerSinceTheBeginningTrend");
			else
				trendTooltip = gx.getMessage("GXPL_QViewer" + qViewer.TrendPeriod + "Trend");
			var styleObj = { width: "0px", marginLeft: "5px" };
			if (!qViewer.IncludeSparkline) {
				styleObj.position = "relative";
				styleObj.top = "2px";
			}
			styleObj.fontSize = "36px";
			var icon = qv.util.dom.createIcon(parent, trendTooltip, styleObj, icon);
			return icon;
		}

		var anyRows = qViewer.Data.Rows.length > 0;
		var aggregateRows = true;
		var xDataField = "";
		var xDataType;
		for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
			var axis = qViewer.Metadata.Axes[i];
			if (axis.DataType == QueryViewerDataType.Date || axis.DataType == QueryViewerDataType.DateTime) {	// Si hay alguna fecha no agrego los datos porque voy a querer ver la evolución a lo largo del tiempo
				aggregateRows = false;
				xDataField = axis.DataField;
				xDataType = axis.DataType;
				break;
			}
		}
		var lastRow;
		if (anyRows) {
			if (aggregateRows)
				lastRow = qv.util.aggregateData(qViewer.Metadata.Data, qViewer.Data.Rows);
			else
				lastRow = qViewer.Data.Rows[qViewer.Data.Rows.length - 1];
		}
		var container = qv.util.dom.getEmptyContainer(qViewer);
		var width;
		var height;
		if (qViewer.IsAutoResize()) {
			width = "";
			height = "";
		}
		else {
			width = "100%";
			height = "100%";
		}
		var tableOuter = qv.util.dom.createTable(container, qv.util.GetContainerControlClasses(qViewer), { width: width, height: height });
		var trOuter = qv.util.dom.createRow(tableOuter);
		var tdOuter = qv.util.dom.createCell(trOuter, 1, "center", {}, "");
		width = qViewer.Orientation == QueryViewerOrientation.Horizontal ? "100%" : "";
		height = qViewer.Orientation == QueryViewerOrientation.Vertical ? "100%" : "";
		var tableInner = qv.util.dom.createTable(tdOuter, "", { width: width, height: height, whiteSpace: "nowrap" });											// Para todos los indicadores en la card
		var trInner;
		if (qViewer.Orientation == QueryViewerOrientation.Horizontal)
			trInner = qv.util.dom.createRow(tableInner);
		var dataAllSeries = [];
		for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
			var datum = qViewer.Metadata.Data[i];
			if (datum.Visible == QueryViewerVisible.Yes || datum.Visible == QueryViewerVisible.Always) {
				var numberFormat = qv.util.ParseNumericPicture(datum.DataType, datum.Picture);
				var decimals = numberFormat.DecimalPrecision;
				var value;
				var valueStr;
				var ageStr;
				if (anyRows) {
					value = lastRow[datum.DataField];
					valueStr = valueOrPercentage(qViewer, lastRow[datum.DataField], datum, decimals);
					ageStr = age(lastRow[xDataField]);
				}
				else {
					value = "";
					valueStr = "";
					ageStr = "";
				}
				if ((gx.lang.gxBoolean(qViewer.IncludeTrend) || gx.lang.gxBoolean(qViewer.IncludeSparkline) || gx.lang.gxBoolean(qViewer.IncludeMaxAndMin)) && anyRows) {
					var data = analizeSeries(qViewer, datum, xDataField, xDataType);
					dataAllSeries.push(data);
				}
				var styleStr = selectStyle(datum, value);
				var styleObj = {};
				var elementValueClass = "qv-card-element-value";
				if (qv.util.startsWith(styleStr, "{") && qv.util.endsWith(styleStr, "}"))
					styleObj = JSON.parse(styleStr);
				else
					elementValueClass = styleStr;	// El style es en realidad el nombre de una clase
				var onClick = null;
				if (qViewer.ItemClick && datum.RaiseItemClick) {
					elementValueClass += (elementValueClass == "" ? "" : " ") + "gx-qv-clickable-element";
					onClick = function () {
						qv.card.fireItemClickEvent(event, qViewer, aggregateRows)
					};
				}
				if (qViewer.Orientation == QueryViewerOrientation.Vertical)
					trInner = qv.util.dom.createRow(tableInner);
				var horizontalPadding = qViewer.Orientation == QueryViewerOrientation.Horizontal ? "10px" : "";
				var verticalPadding = qViewer.Orientation == QueryViewerOrientation.Vertical ? "10px" : "";
				var tdInner = qv.util.dom.createCell(trInner, 1, "center", { paddingRight: horizontalPadding, paddingLeft: horizontalPadding, paddingBottom: verticalPadding, paddingTop: verticalPadding }, "");
				var table1 = qv.util.dom.createTable(tdInner, "", {});
				var tr1 = qv.util.dom.createRow(table1);
				var td1 = qv.util.dom.createCell(tr1, 1, "center", {}, "");
				var table2 = qv.util.dom.createTable(td1, "", {});
				var tr2 = qv.util.dom.createRow(table2);
				td2 = qv.util.dom.createCell(tr2, 1, "center", {}, "");
				var span2 = qv.util.dom.createSpan(td2, qViewer.getContainerControl().id + "_" + datum.Name, elementValueClass, (xDataField != "" ? ageStr : ""), styleObj, onClick, valueStr);
				var td2 = qv.util.dom.createCell(tr2, 1, "", {}, "");
				if (anyRows && qViewer.IncludeTrend && !qViewer.IncludeSparkline && data.LinearRegression.AnyTrend)
					TrendIcon(td2, qViewer, data);
				tr1 = qv.util.dom.createRow(table1);
				td1 = qv.util.dom.createCell(tr1, 1, "center", {}, "");
				span1 = qv.util.dom.createSpan(td1, "", "qv-card-element-title", (xDataField != "" ? ageStr : ""), {}, null, datum.Title);
				if (qViewer.IncludeSparkline && xDataField != "" && anyRows) {
					tr1 = qv.util.dom.createRow(table1);
					td1 = qv.util.dom.createCell(tr1, 1, "", {}, "");
					var table2 = qv.util.dom.createTable(td1, "", { width: "100%" });
					var tr2 = qv.util.dom.createRow(table2);
					var td2;
					td2 = qv.util.dom.createCell(tr2, 1, "", { width: "100%" }, "");
					var div = qv.util.dom.createDiv(td2, sparklineChartId(qViewer.userControlId(), i), "", "", { height: "50px", width: "100%", minWidth: "100px" }, "");
					td2 = qv.util.dom.createCell(tr2, 1, "", { width: "0%" }, "");
					if (anyRows && qViewer.IncludeTrend && data.LinearRegression.AnyTrend)
						TrendIcon(td2, qViewer, data);
				}
				if (qViewer.IncludeMaxAndMin && xDataField != "" && anyRows) {
					tr1 = qv.util.dom.createRow(table1);
					td1 = qv.util.dom.createCell(tr1, 1, "center", {}, "");
					var table2 = qv.util.dom.createTable(td1, "", { "margin-top": "10px" });
					var tr2 = qv.util.dom.createRow(table2);
					var td2;
					td2 = qv.util.dom.createCell(tr2, 1, "", {}, "");
					var table3;
					table3 = getMinMaxTable(td2, valueOrPercentage(qViewer, data.MinValue, datum, decimals), age(data.MinWhen), gx.getMessage("GXPL_QViewerCardMinimum"));
					td2 = qv.util.dom.createCell(tr2, 1, "", {}, "");
					table3 = getMinMaxTable(td2, valueOrPercentage(qViewer, data.MaxValue, datum, decimals), age(data.MaxWhen), gx.getMessage("GXPL_QViewerCardMaximum"));
				}
			}
		}
		if (qViewer.IncludeSparkline && xDataField != "" && anyRows) {
			for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
				var sparklineOptions = GetSparklineOptions(qViewer, dataAllSeries[i].ChartSeriesData, i);
				var SparklineHCChart = new Highcharts.Chart(sparklineOptions);
			}
		}
		qViewer._ControlRenderedTo = qViewer.RealType;
		qv.util.hideActivityIndicator(qViewer);
	}

	return {

		tryToRenderCard: function (qViewer) {
			var errMsg = "";

			// Ejecuto el primer servicio y verifico que no haya habido error
			var d1 = new Date();
			var t1 = d1.getTime();

			qViewer.xml = qViewer.xml || {};

			qv.services.GetRecordsetCacheKeyIfNeeded(qViewer, function (resText, qViewer) {				// Servicio GetRecordsetCacheKey
				var newRecordsetCacheKey = false;
				if (resText != qViewer.xml.recordsetCacheKey) {
					qViewer.xml.recordsetCacheKey = resText;
					newRecordsetCacheKey = true;
				}
				if (!qv.util.anyError(resText)) {
					if (newRecordsetCacheKey)
						qv.services.parseRecordsetCacheKeyXML(qViewer);

					qv.services.GetMetadataIfNeeded(qViewer, function (resText, qViewer) {				// Servicio GetMetadata
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
								qv.services.GetDataIfNeeded(qViewer, function (resText, qViewer) {			// Servicio GetData
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
										renderCard(qViewer);
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

		GetMetadataCard: function (qViewer) {
			return qv.util.GetDesigntimeMetadata(qViewer);
		},

		GetDataCard: function (qViewer) {
			return qViewer.xml.data;
		},

		fireItemClickEvent: function (ev, qViewer, allRows) {
			var id = ev.target.id;
			var name = id.substr(id.lastIndexOf("_") + 1);
			setEventData(qViewer.ItemClickData, qViewer, name, allRows);
			qViewer.ItemClick();
		}

	}

})()
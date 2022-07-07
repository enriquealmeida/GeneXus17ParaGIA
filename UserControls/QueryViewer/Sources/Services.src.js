var STATE_DONE = 4
var STATUS_OK = 200
var METADATA_VERSION = 2

qv.services = (function () {

	function GetObjectNameFromObjectProperty(propValue) {
		var array = eval(propValue);
		return array[1].replace(/\\/g, ".");
	}

	function MetadataChanged(qViewer, key) {

		function MetadataType(outputType) {
			switch (outputType) {
				case QueryViewerOutputType.Table:
					return "IgnoreType";
				case QueryViewerOutputType.Chart:
				case QueryViewerOutputType.Card:
					return "IgnoreTypeAndAxis";
				default:
					return "IgnoreNot";
			}
		}

		var runtimeFieldsJSON = qv.services.RuntimeFieldsJSON(qViewer);
		var runtimeParametersJSON = qv.services.RuntimeParametersJSON(qViewer);
		var queryInfoTextForNullValues = "";
		var queryInfoFieldsJSON = "";
		var metadataType = MetadataType(qViewer.RealType);
		if (qViewer.QueryInfo != "") {
			var queryInfo = eval('(' + qViewer.QueryInfo + ')');
			queryInfoTextForNullValues = queryInfo.TextForNullValues;
			queryInfoFieldsJSON = gx.json.serializeJson(queryInfo.Fields);
		}
		if (qViewer.ObjectName != key.ObjectName || qViewer.ObjectType != key.ObjectType || qViewer.ObjectId != key.ObjectId || qViewer.Object != key.Object || runtimeFieldsJSON != key.RuntimeFieldsJSON || runtimeParametersJSON != key.RuntimeParametersJSON || queryInfoTextForNullValues != key.QueryInfoTextForNullValues || queryInfoFieldsJSON != key.QueryInfoFieldsJSON || metadataType != key.MetadataType) {
			key.ObjectName = qViewer.ObjectName;
			key.ObjectType = qViewer.ObjectType;
			key.ObjectId = qViewer.ObjectId;
			key.Object = qViewer.Object;
			key.RuntimeFieldsJSON = runtimeFieldsJSON;
			key.RuntimeParametersJSON = runtimeParametersJSON;
			key.QueryInfoTextForNullValues = queryInfoTextForNullValues;
			key.QueryInfoFieldsJSON = queryInfoFieldsJSON;
			key.MetadataType = metadataType;
			return true;
		}
		else
			return false;
	}

	function DataChanged(qViewer, key) {

		function DataType(outputType) {
			if (outputType == QueryViewerOutputType.Table)
				return "PagedRecordSet";
			else if (outputType == QueryViewerOutputType.PivotTable)
				return "PagedLineSet";
			else
				return "NotPaged";
		}

		function GetFieldsJSON(qViewer, fields, keyOrderType) {
			var fieldsStr = SortAscendingForced(qViewer);
			for (var i = 0; i < fields.length; i++) {
				var field = fields[i];
				var orderType = "";
				if (field[keyOrderType] != undefined)
					orderType = field[keyOrderType].Type;
				if (orderType != "") {
					var sdtOrderType = qv.services.postInfo._priv.SdtWithValuesJSON("Order", orderType, QueryViewerAxisOrderType.Custom, field[keyOrderType].Values, false);
					fieldsStr += "," + sdtOrderType;
				}
				if (field["Analytics"] != undefined) {
					fieldsStr += "," + field["Analytics"].ShowValuesAs;
					fieldsStr += "," + field["Analytics"].RollingAverageType;
					fieldsStr += "," + field["Analytics"].RollingAverageTerms;
					fieldsStr += "," + field["Analytics"].DifferenceFrom;
					fieldsStr += "," + field["Analytics"].ShowAsPercentage;
				}
			}
			return fieldsStr;
		}

		var runtimeParametersJSON = qv.services.RuntimeParametersJSON(qViewer);
		var fieldsJSON = GetFieldsJSON(qViewer, qViewer.Axes, "AxisOrder");
		var queryInfoSQLSentence = "";
		var queryInfoParametersJSON = "";
		var queryInfoFieldsJSON = "";
		var dataType = DataType(qViewer.RealType);
		if (qViewer.QueryInfo != "") {
			var queryInfo = eval('(' + qViewer.QueryInfo + ')');
			queryInfoSQLSentence = queryInfo.SQLSentence;
			queryInfoParametersJSON = gx.json.serializeJson(queryInfo.Parameters);
			queryInfoFieldsJSON = GetFieldsJSON(qViewer, queryInfo.Fields, "Order");
		}
		if (qViewer.ObjectName != key.ObjectName || qViewer.ObjectType != key.ObjectType || qViewer.ObjectId != key.ObjectId || qViewer.Object != key.Object || runtimeParametersJSON != key.RuntimeParametersJSON || fieldsJSON != key.FieldsJSON || queryInfoSQLSentence != key.QueryInfoSQLSentence || queryInfoParametersJSON != key.QueryInfoParametersJSON || queryInfoFieldsJSON != key.QueryInfoFieldsJSON || dataType != key.DataType) {
			key.ObjectName = qViewer.ObjectName;
			key.ObjectType = qViewer.ObjectType;
			key.ObjectId = qViewer.ObjectId;
			key.Object = qViewer.Object;
			key.RuntimeParametersJSON = runtimeParametersJSON;
			key.FieldsJSON = fieldsJSON;
			key.QueryInfoSQLSentence = queryInfoSQLSentence;
			key.QueryInfoParametersJSON = queryInfoParametersJSON;
			key.QueryInfoFieldsJSON = queryInfoFieldsJSON;
			key.DataType = dataType;
			return true;
		}
		else
			return false;
	}

	function DashboardSpecChanged(dViewer, key) {
		if (dViewer.Object) {
			var objectName = GetObjectNameFromObjectProperty(dViewer.Object);
			if (objectName != key.ObjectName) {
				key.ObjectName = objectName;
				return true;
			}
			else
				return false;
		}
		else
			return dViewer.DashboardSpec == "";
	}

	function SortAscendingForced(qViewer) {
		return (qViewer.RealType == QueryViewerOutputType.Chart && qv.chart.IsDatetimeXAxis(qViewer)) || (qViewer.RealType == QueryViewerOutputType.Card && (qViewer.IncludeSparkline || qViewer.IncludeTrend))
	}
	
	function ServicesVersionOK(version) {
		return version == null || parseInt(version) == METADATA_VERSION;		// Se controla que sea distinto de null por la ejecución de consultas a través del API de GXquery, que no devuelven versión por ser servicios más viejos
	}
	
	function ParserResult(success, message) {
		return { Success: success, Message: message};
	}

	return {

		url: (function () {

			function getServiceURL(qViewer, serviceName) {
				var GenType = qv.util.getGenerator();
				var fc = foolCache(); // Para que el explorador no cachee los resultados de los servicios
				var debugSubstr = qViewer.enableServerDebug ? "_debug" : "";
				switch (GenType) {
					case "C#":
						return baseUrl() + "agxpl_get" + debugSubstr + ".aspx?" + serviceName + "," + fc;
					case "Java":
						return GetJavaBaseUrl(false) + "qviewer.services.agxpl_get" + debugSubstr + "?" + serviceName + "," + fc;
					default:
						return "";
				}
			}

			function foolCache() {
				var currentTime = new Date();
				var miliSecs = currentTime.getTime();
				return miliSecs;
			}

			function staticDirectory() {
				var staticDir;
				if (gx.staticDirectory != "")
					staticDir = gx.staticDirectory;
				else
					staticDir = this.getCookie('StaticContent');
				if (qv.util.startsWith(staticDir, "/"))
					return staticDir.substr(1, staticDir.length - 1);
				else
					return staticDir;
			}

			function GetJavaBaseUrl(staticContent) {
				var baseUrl = gx.util.resourceUrl(gx.basePath, true);
				if (staticContent)
					baseUrl += staticDirectory();
				return baseUrl;
			}

			function baseUrl() {
				var url = new gx.util.Url(location.href);
				var path = url.path;
				var pathResult = path.substr(0, path.lastIndexOf('/') + 1);
				if (url.port == "")
					urlresult = url.protocol + "://" + url.host + pathResult;
				else
					urlresult = url.protocol + "://" + url.host + ":" + url.port + pathResult;
				return urlresult;
			}

			return {

				getDefaultOutputURL: function (qViewer) {
					return getServiceURL(qViewer, "defaultoutput");
				},

				getRecordsetCacheKeyURL: function (qViewer) {
					return getServiceURL(qViewer, "recordsetcachekey");
				},

				getMetadataURL: function (qViewer) {
					return getServiceURL(qViewer, "metadata");
				},

				getDataURL: function (qViewer) {
					return getServiceURL(qViewer, "data");
				},

				getAttributeValuesURL: function (qViewer) {
					return getServiceURL(qViewer, "attributevalues");
				},

				getPageDataForTableURL: function (qViewer) {
					return getServiceURL(qViewer, "pagedatafortable");
				},

				getPageDataForPivotTableURL: function (qViewer) {
					return getServiceURL(qViewer, "pagedataforpivottable");
				},

				getQueryParametersURL: function (qViewer) {
					return getServiceURL(qViewer, "queryparameters");
				},

				getDashboardSpecURL: function (qViewer) {
					return getServiceURL(qViewer, "dashboardspec");
				},

				getStaticDirectory: function () {
					if (qv.util.getGenerator() === "Java")
						return GetJavaBaseUrl(true);
					else
						return staticDirectory();
				}
			
			}

		})(),

		postInfo: (function () {

			function RuntimeFieldsJSON(qViewer, encodevaluescollection) {
				var strFields = '';
				var strValues = '';
				var existFields;
				var existValues;
				var picture;
				var targetValue;
				var maximumValue;
				var style;
				var subtotals;
				var CanDragToPages;
				var RaiseItemClick;
				var ShowValuesAs;
				var RollingAverageType;
				var RollingAverageTerms;
				var DifferenceFrom;
				var ShowAsPercentage;
				var orderType;
				var expandCollapseType;
				var filterType;
				existFields = false;
				for (i = 0; qViewer.Axes[i] != undefined; i++) {
					existFields = true;
					strFields += (strFields != '' ? ',' : '');
					strFields += '{';
					picture = '';
					targetValue = 0;
					maximumValue = 0;
					style = '';
					subtotals = '';
					CanDragToPages = true;
					RaiseItemClick = true;
					ShowValuesAs = '';
					RollingAverageType = '';
					RollingAverageTerms = 0;
					DifferenceFrom = '';
					ShowAsPercentage = ''
					orderType = '';
					expandCollapseType = '';
					filterType = '';
					var valuesStyles = ValuesStylesJSON(qViewer.Axes[i]);
					var conditionalStyles = ConditionalStylesJSON(qViewer.Axes[i]);
					var grouping = GroupingJSON(qViewer.Axes[i]);
					if (qViewer.Axes[i].Format != undefined) {
						picture = (qViewer.Axes[i].Format.Picture ? qViewer.Axes[i].Format.Picture : "");
						targetValue = (qViewer.Axes[i].Format.TargetValue ? qViewer.Axes[i].Format.TargetValue : 0);
						maximumValue = (qViewer.Axes[i].Format.MaximumValue ? qViewer.Axes[i].Format.MaximumValue : 0);
						style = (qViewer.Axes[i].Format.Style ? qViewer.Axes[i].Format.Style : "");
						subtotals = (qViewer.Axes[i].Format.Subtotals ? qViewer.Axes[i].Format.Subtotals : "");
						CanDragToPages = (qViewer.Axes[i].Format.CanDragToPages != undefined ? qViewer.Axes[i].Format.CanDragToPages : true);
					}
					if (qViewer.Axes[i].Actions != undefined) {
						RaiseItemClick = qViewer.Axes[i].Actions.RaiseItemClick;
					}
					if (qViewer.Axes[i].Analytics != undefined) {
						ShowValuesAs = qViewer.Axes[i].Analytics.ShowValuesAs;
						RollingAverageType = qViewer.Axes[i].Analytics.RollingAverageType;
						RollingAverageTerms = qViewer.Axes[i].Analytics.RollingAverageTerms;
						DifferenceFrom = qViewer.Axes[i].Analytics.DifferenceFrom;
						ShowAsPercentage = qViewer.Axes[i].Analytics.ShowAsPercentage;
					}
					if (qViewer.Axes[i].AxisOrder != undefined) {
						orderType = qViewer.Axes[i].AxisOrder.Type;
					}
					if (qViewer.Axes[i].ExpandCollapse != undefined) {
						expandCollapseType = qViewer.Axes[i].ExpandCollapse.Type;
					}
					if (qViewer.Axes[i].Filter != undefined) {
						filterType = qViewer.Axes[i].Filter.Type;
					}
					if (filterType == undefined) filterType = QueryViewerFilterType.ShowSomeValues;

					strFields += '"Name":"' + encodeURIComponent(qViewer.Axes[i].Name) + '"';
					strFields += ',';
					strFields += '"Caption":"' + encodeURIComponent(qViewer.Axes[i].Title) + '"';
					strFields += ',';
					strFields += '"Aggregation":"' + qViewer.Axes[i].Aggregation + '"';
					strFields += ',';
					strFields += '"Visible":"' + qViewer.Axes[i].Visible + '"';
					strFields += ',';
					strFields += '"Type":"' + qViewer.Axes[i].Type + '"';
					strFields += ',';
					strFields += '"Axis":"' + qViewer.Axes[i].Axis + '"';
					strFields += ',';
					strFields += '"Picture":"' + encodeURIComponent(picture) + '"';
					strFields += ',';
					strFields += '"TargetValue":' + targetValue;
					strFields += ',';
					strFields += '"MaximumValue":' + maximumValue;
					strFields += ',';
					strFields += '"Style":"' + style + '"';
					strFields += ',';
					strFields += '"Subtotals":"' + subtotals + '"';
					strFields += ',';
					strFields += '"CanDragToPages":' + CanDragToPages;
					strFields += ',';
					strFields += '"RaiseItemClick":' + RaiseItemClick;
					if (ShowValuesAs != '') {
						strFields += ',';
						strFields += '"Analytics":{';
						strFields += '"ShowValuesAs":"' + ShowValuesAs + '"';
						strFields += ',';
						strFields += '"RollingAverageType":"' + RollingAverageType + '"';
						strFields += ',';
						strFields += '"RollingAverageTerms":' + RollingAverageTerms;
						strFields += ',';
						strFields += '"DifferenceFrom":"' + DifferenceFrom + '"';
						strFields += ',';
						strFields += '"ShowAsPercentage":"' + ShowAsPercentage + '"';
						strFields += '}';
					}
					if (orderType != '') {
						var sdtOrderType = SdtWithValuesJSON("Order", orderType, QueryViewerAxisOrderType.Custom, qViewer.Axes[i].AxisOrder.Values, encodevaluescollection);
						strFields += (sdtOrderType != '' ? ',' + sdtOrderType : '');
					}
					if (expandCollapseType != '') {
						var sdtExpandCollapse = SdtWithValuesJSON("ExpandCollapse", expandCollapseType, QueryViewerExpandCollapseType.ExpandSomeValues, qViewer.Axes[i].ExpandCollapse.Values, encodevaluescollection);
						strFields += (sdtExpandCollapse != '' ? ',' + sdtExpandCollapse : '');
					}
					if (filterType != '' && qViewer.Axes[i].Filter.Values) {
						var sdtFilter = SdtWithValuesJSON("Filter", filterType, QueryViewerFilterType.ShowSomeValues, qViewer.Axes[i].Filter.Values, encodevaluescollection);
						strFields += (sdtFilter != '' ? ',' + sdtFilter : '');
					}
					strFields += (valuesStyles != '' ? ',' + valuesStyles : '');
					strFields += (conditionalStyles != '' ? ',' + conditionalStyles : '');
					strFields += (grouping != '' ? ',' + grouping : '');
					strFields += '}';
				}
				if (existFields) strFields = '"RuntimeFields":[' + strFields + "]";
				return strFields;
			}

			function ValuesStylesJSON(axis) {
				var strStyles = '';
				var existStyles = false;
				if (axis.Format != undefined) {
					if (axis.Format.ValuesStyles != undefined) {
						for (var i = 0; axis.Format.ValuesStyles[i] != undefined; i++) {
							existStyles = true;
							strStyles += (strStyles != '' ? ',' : '');
							strStyles += '{';
							strStyles += '"Value":"' + encodeURIComponent(axis.Format.ValuesStyles[i].Value) + '"';
							strStyles += ',';
							strStyles += '"Style":"' + encodeURIComponent(axis.Format.ValuesStyles[i].StyleOrClass) + '"';
							strStyles += ',';
							strStyles += '"Propagate":' + (axis.Format.ValuesStyles[i].ApplyToRowOrColumn ? "true" : "false");
							strStyles += '}';
						}
					}
				}
				if (existStyles) strStyles = '"ValuesStyles":[' + strStyles + ']';
				return strStyles;
			}

			function ConditionalStylesJSON(axis) {
				var strStyles = '';
				var existStyles = false;
				if (axis.Format != undefined) {
					if (axis.Format.ConditionalStyles != undefined) {
						for (var i = 0; axis.Format.ConditionalStyles[i] != undefined; i++) {
							existStyles = true;
							strStyles += (strStyles != '' ? ',' : '');
							strStyles += '{';
							strStyles += '"Operator":"' + axis.Format.ConditionalStyles[i].Operator + '"';
							strStyles += ',';
							strStyles += '"Value1":"' + encodeURIComponent(axis.Format.ConditionalStyles[i].Value1) + '"';
							if (axis.Format.ConditionalStyles[i].Operator == QueryViewerConditionOperator.Interval) {
								strStyles += ',';
								strStyles += '"Value2":"' + encodeURIComponent(axis.Format.ConditionalStyles[i].Value2) + '"';
							}
							strStyles += ',';
							strStyles += '"Style":"' + encodeURIComponent(axis.Format.ConditionalStyles[i].StyleOrClass) + '"';
							strStyles += '}';
						}
					}
				}
				if (existStyles) strStyles = '"ConditionalStyles":[' + strStyles + ']';
				return strStyles;
			}

			function GroupingJSON(axis) {
				var strGrouping = '';
				if (axis.Grouping != undefined) {
					strGrouping += '{';
					strGrouping += '"GroupByYear":' + (axis.Grouping.GroupByYear ? "true" : "false");
					strGrouping += ',';
					strGrouping += '"YearTitle":"' + axis.Grouping.YearTitle + '"';
					strGrouping += ',';
					strGrouping += '"GroupBySemester":' + (axis.Grouping.GroupBySemester ? "true" : "false");
					strGrouping += ',';
					strGrouping += '"SemesterTitle":"' + axis.Grouping.SemesterTitle + '"';
					strGrouping += ',';
					strGrouping += '"GroupByQuarter":' + (axis.Grouping.GroupByQuarter ? "true" : "false");
					strGrouping += ',';
					strGrouping += '"QuarterTitle":"' + axis.Grouping.QuarterTitle + '"';
					strGrouping += ',';
					strGrouping += '"GroupByMonth":' + (axis.Grouping.GroupByMonth ? "true" : "false");
					strGrouping += ',';
					strGrouping += '"MonthTitle":"' + axis.Grouping.MonthTitle + '"';
					strGrouping += ',';
					strGrouping += '"GroupByDayOfWeek":' + (axis.Grouping.GroupByDayOfWeek ? "true" : "false");
					strGrouping += ',';
					strGrouping += '"DayOfWeekTitle":"' + axis.Grouping.DayOfWeekTitle + '"';
					strGrouping += ',';
					strGrouping += '"HideValue":' + (axis.Grouping.HideValue ? "true" : "false");
					strGrouping += '}';
					strGrouping = '"Grouping":' + strGrouping;
				}
				return strGrouping;
			}

			function SdtWithValuesJSON(sdtName, actualType, typeWithValues, values, encodevalues) {
				var strAux = '';
				if (actualType != '') {
					strAux += '"' + sdtName + '":';
					strAux += '{';
					strAux += '"Type":"' + actualType + '"';
					strValues = "";
					if (actualType == typeWithValues) {
						existValues = false;
						for (j = 0; values[j] != undefined; j++) {
							existValues = true;
							strValues += (strValues != '' ? ',' : '');
							if (encodevalues) strValues += '"' + encodeURIComponent(values[j]) + '"';
							else strValues += '"' + values[j] + '"';
						}
						if (existValues) strValues = ',' + '"Values":[' + strValues + ']';
					}
					strAux += strValues;
					strAux += '}';
				}
				return strAux;
			}

			function RuntimeParametersJSON(qViewer) {
				var strParameters = '';
				var existParameters = false;

				if (qViewer.Object) {
					var array = eval(qViewer.Object);
					for (i = 2; i < array.length; i++) {
						existParameters = true;
						strParameters += (strParameters != '' ? ',' : '');
						strParameters += '{';
						strParameters += '"Value":"' + encodeURIComponent(array[i]) + '"';
						strParameters += '}';
					}
				}
				else {
					for (i = 0; qViewer.Parameters[i] != undefined; i++) {
						existParameters = true;
						strParameters += (strParameters != '' ? ',' : '');
						strParameters += '{';
						strParameters += '"Name":"' + qViewer.Parameters[i].Name + '"';
						strParameters += ',';
						strParameters += '"Value":"' + encodeURIComponent(qViewer.Parameters[i].Value) + '"';
						strParameters += '}';
					}
				}
				if (existParameters) strParameters = '"RuntimeParameters":[' + strParameters + ']';
				return strParameters;
			}

			function ExpandCollapseJSON(ExpandCollapse) {
				var strExpandCollapse = '';
				var existExpandCollapse = false;
				for (var i = 0; ExpandCollapse[i] != undefined; i++) {
					existExpandCollapse = true;
					strExpandCollapse += (strExpandCollapse != '' ? ',' : '');
					strExpandCollapse += '{';
					strExpandCollapse += '"DataField":"' + ExpandCollapse[i].DataField + '"';
					strExpandCollapse += ',';
					strExpandCollapse += '"NullExpanded":' + ExpandCollapse[i].NullExpanded;
					strExpandedValues = "";
					existValues = false;
					for (var j = 0; ExpandCollapse[i].NotNullValues.Expanded[j] != undefined; j++) {
						existValues = true;
						strExpandedValues += (strExpandedValues != '' ? ',' : '');
						strExpandedValues += '"' + encodeURIComponent(ExpandCollapse[i].NotNullValues.Expanded[j]) + '"';
					}
					if (existValues) strExpandedValues = ',' + '"Expanded":[' + strExpandedValues + ']';
					strCollapsedValues = "";
					existValues = false;
					for (var j = 0; ExpandCollapse[i].NotNullValues.Collapsed[j] != undefined; j++) {
						existValues = true;
						strCollapsedValues += (strCollapsedValues != '' ? ',' : '');
						strCollapsedValues += '"' + encodeURIComponent(ExpandCollapse[i].NotNullValues.Collapsed[j]) + '"';
					}
					if (existValues) strCollapsedValues = ',' + '"Collapsed":[' + strCollapsedValues + ']';
					strExpandCollapse += ',';
					strExpandCollapse += '"NotNullValues":{"DefaultAction":"' + ExpandCollapse[i].NotNullValues.DefaultAction + '"' + strExpandedValues + strCollapsedValues + '}';
					strExpandCollapse += '}';
				}
				if (existExpandCollapse) strExpandCollapse = '"ExpandCollapse":[' + strExpandCollapse + ']';
				return strExpandCollapse;
			}

			function FiltersJSON(Filters) {
				var strFilters = '';
				var existFilters = false;
				for (i = 0; Filters[i] != undefined; i++) {
					existFilters = true;
					strFilters += (strFilters != '' ? ',' : '');
					strFilters += '{';
					strFilters += '"DataField":"' + Filters[i].DataField + '"';
					strFilters += ',';
					strFilters += '"NullIncluded":' + Filters[i].NullIncluded;
					strIncludedValues = "";
					existValues = false;
					for (j = 0; Filters[i].NotNullValues.Included[j] != undefined; j++) {
						existValues = true;
						strIncludedValues += (strIncludedValues != '' ? ',' : '');
						strIncludedValues += '"' + encodeURIComponent(Filters[i].NotNullValues.Included[j]) + '"';
					}
					if (existValues) strIncludedValues = ',' + '"Included":[' + strIncludedValues + ']';
					strExcludedValues = "";
					existValues = false;
					for (j = 0; Filters[i].NotNullValues.Excluded[j] != undefined; j++) {
						existValues = true;
						strExcludedValues += (strExcludedValues != '' ? ',' : '');
						strExcludedValues += '"' + encodeURIComponent(Filters[i].NotNullValues.Excluded[j]) + '"';
					}
					if (existValues) strExcludedValues = ',' + '"Excluded":[' + strExcludedValues + ']';
					strFilters += ',';
					strFilters += '"NotNullValues":{"DefaultAction":"' + Filters[i].NotNullValues.DefaultAction + '"' + strIncludedValues + strExcludedValues + '}';
					strFilters += '}';
				}
				if (existFilters) strFilters = '"Filters":[' + strFilters + ']';
				return strFilters;
			}

			function AxesInfoJSON(AxesInfo) {
				var strAxesInfo = '';
				var existAxesInfo = false;
				for (var i = 0; AxesInfo[i] != undefined; i++) {
					existAxesInfo = true;
					strAxesInfo += (strAxesInfo != '' ? ',' : '');
					strAxesInfo += '{';
					strAxesInfo += '"DataField":"' + AxesInfo[i].DataField + '"';
					strAxesInfo += ',';
					strAxesInfo += '"Visible":' + !AxesInfo[i].Hidden;
					strAxesInfo += ',';
					strAxesInfo += '"Axis":"' + AxesInfo[i].Axis.Type + '"';
					strAxesInfo += ',';
					strAxesInfo += '"Position":' + (qv.util.IsInteger(AxesInfo[i].Axis.Position) ? AxesInfo[i].Axis.Position : 0);
					strAxesInfo += ',';
					strAxesInfo += '"Order":"' + AxesInfo[i].Order + '"';
					strAxesInfo += ',';
					strAxesInfo += '"Subtotals":' + AxesInfo[i].Subtotals;
					strAxesInfo += '}';
				}
				if (existAxesInfo) strAxesInfo = '"AxesInfo":[' + strAxesInfo + ']';
				return strAxesInfo;
			}

			function DataInfoJSON(DataInfo) {
				var strDataInfo = '';
				var existDataInfo = false;
				for (var i = 0; DataInfo[i] != undefined; i++) {
					existDataInfo = true;
					strDataInfo += (strDataInfo != '' ? ',' : '');
					strDataInfo += '{';
					strDataInfo += '"DataField":"' + DataInfo[i].DataField + '"';
					strDataInfo += ',';
					strDataInfo += '"Visible":' + !DataInfo[i].Hidden;
					strDataInfo += ',';
					strDataInfo += '"Position":' + (qv.util.IsInteger(DataInfo[i].Position) ? DataInfo[i].Position : 0);
					strDataInfo += '}';
				}
				if (existDataInfo) strDataInfo = '"DataInfo":[' + strDataInfo + ']';
				return strDataInfo;
			}

			function OrderJSON(DataField, Type) {
				return '{"DataField":"' + DataField + '","Type":"' + Type + '"}';
			}

			function RecordsetCacheInfoJSON(qViewer) {
				if (qViewer.UseRecordsetCache)
					return '{"ActualKey":"' + qViewer.RecordsetCache.ActualKey + '","OldKey":"' + qViewer.RecordsetCache.OldKey + '","MinutesToKeep":' + qViewer.MinutesToKeepInRecordsetCache + ',"MaximumSize":' + qViewer.MaximumCacheSize + '}';
				else
					return '{"ActualKey":"","OldKey":"","MinutesToKeep":0,"MaximumSize":0}';
			}

			function getObjectBasicInfo(qViewer) {
				var str = "";
				str += (qViewer.ParentObject.PackageName != '' ? '"ApplicationNamespace":"' + qViewer.ParentObject.PackageName + '"' + ',' : '');
				if (qViewer.Object)
					str += '"ObjectName":"' + GetObjectNameFromObjectProperty(qViewer.Object) + '"';
				else {
					str += '"ObjectName":"' + qViewer.ObjectName + '"';
					str += ',';
					str += '"Alt_ObjectType":"' + qViewer.ObjectType + '"';
					str += ',';
					str += '"Alt_ObjectId":' + qViewer.ObjectId;
				}
				return str;
			}

			return {

				DefaultOutput: function (qViewer) {
					var str = "";
					str += '{';
					str += getObjectBasicInfo(qViewer);
					str += (qViewer.ObjectInfo != '' ? ',' + '"ObjectInfo":' + qViewer.ObjectInfo : '');
					str += '}';
					return "Data=" + encodeURIComponent(str);
				},

				RecordsetCacheKey: function (qViewer) {
					var str = "";
					str += '{';
					str += getObjectBasicInfo(qViewer);
					str += '}';
					return "Data=" + encodeURIComponent(str);
				},

				Metadata: function (qViewer) {
					var str = "";
					var runtimeParametersJSON = RuntimeParametersJSON(qViewer);
					var runtimeFieldsJSON = RuntimeFieldsJSON(qViewer, true);
					str += '{';
					str += getObjectBasicInfo(qViewer);
					str += (qViewer.QueryInfo != '' ? ',' + '"QueryInfo":' + qViewer.QueryInfo : '');
					str += (qViewer.AppSettings != '' ? ',' + '"AppSettings":' + qViewer.AppSettings : '');
					str += (runtimeParametersJSON != '' ? ',' + runtimeParametersJSON : '');
					str += (runtimeFieldsJSON != '' ? ',' + runtimeFieldsJSON : '');
					str += ',';
					str += '"OutputType":"' + qViewer.RealType + '"';
					str += ',';
					str += '"AllowElementsOrderChange":' + qViewer.AllowElementsOrderChange;
					str += ',';
					str += '"RememberLayout":' + qViewer.RememberLayout;
					str += ',';
					str += '"ShowDataLabelsIn":"' + qViewer.ShowDataLabelsIn + '"';
					str += ',';
					str += '"RecordsetCacheInfo":' + RecordsetCacheInfoJSON(qViewer);
					str += ',';
					str += '"ReturnSampleData":' + qViewer.ReturnSampleData;
					str += ',';
					str += '"TranslationType":"' + qViewer.TranslationType + '"';
					str += '}';
					return "Data=" + encodeURIComponent(str);
				},

				Data: function (qViewer) {
					var str = "";
					var runtimeParametersJSON = RuntimeParametersJSON(qViewer);
					var runtimeFieldsJSON = RuntimeFieldsJSON(qViewer, true);
					str += '{';
					str += getObjectBasicInfo(qViewer);
					str += (qViewer.QueryInfo != '' ? ',' + '"QueryInfo":' + qViewer.QueryInfo : '');
					str += (qViewer.AppSettings != '' ? ',' + '"AppSettings":' + qViewer.AppSettings : '');
					str += (runtimeParametersJSON != '' ? ',' + runtimeParametersJSON : '');
					str += (runtimeFieldsJSON != '' ? ',' + runtimeFieldsJSON : '');
					str += ',';
					str += '"OutputType":"' + qViewer.RealType + '"';
					str += ',';
					str += '"SortAscendingForced":' + SortAscendingForced(qViewer);
					str += ',';
					str += '"AllowElementsOrderChange":' + qViewer.AllowElementsOrderChange;
					str += ',';
					str += '"RecordsetCacheInfo":' + RecordsetCacheInfoJSON(qViewer);
					str += ',';
					str += '"ReturnSampleData":' + qViewer.ReturnSampleData;
					str += ',';
					str += '"TranslationType":"' + qViewer.TranslationType + '"';
					str += '}';
					return "Data=" + encodeURIComponent(str);
				},

				AttributeValues: function (qViewer, DataField, PageNumber, PageSize, Filter) {
					var str = "";
					var runtimeParametersJSON = RuntimeParametersJSON(qViewer);
					var runtimeFieldsJSON = RuntimeFieldsJSON(qViewer, true);
					str += '{';
					str += getObjectBasicInfo(qViewer);
					str += ',';
					str += '"DataField":"' + DataField + '"';
					str += ',';
					str += '"PageNumber":' + PageNumber;
					str += ',';
					str += '"PageSize":' + PageSize;
					str += ',';
					str += '"Filter":"' + Filter + '"';
					str += (qViewer.QueryInfo != '' ? ',' + '"QueryInfo":' + qViewer.QueryInfo : '');
					str += (qViewer.AppSettings != '' ? ',' + '"AppSettings":' + qViewer.AppSettings : '');
					str += (runtimeParametersJSON != '' ? ',' + runtimeParametersJSON : '');
					str += (runtimeFieldsJSON != '' ? ',' + runtimeFieldsJSON : '');
					str += ',';
					str += '"OutputType":"' + qViewer.RealType + '"';
					str += ',';
					str += '"AllowElementsOrderChange":' + qViewer.AllowElementsOrderChange;
					str += ',';
					str += '"RecordsetCacheInfo":' + RecordsetCacheInfoJSON(qViewer);
					str += ',';
					str += '"ReturnSampleData":' + qViewer.ReturnSampleData;
					str += ',';
					str += '"TranslationType":"' + qViewer.TranslationType + '"';
					str += '}';
					return "Data=" + encodeURIComponent(str);
				},

				TablePageData: function (qViewer, PageNumber, PageSize, ReturnTotPages, DataFieldOrder, OrderType, Filters, LayoutChanged) {
					var str = "";
					var runtimeParametersJSON = RuntimeParametersJSON(qViewer);
					var runtimeFieldsJSON = RuntimeFieldsJSON(qViewer, true);
					var filtersJSON = FiltersJSON(Filters);
					str += '{';
					str += getObjectBasicInfo(qViewer);
					str += ',';
					str += '"PageNumber":' + PageNumber;
					str += ',';
					str += '"PageSize":' + (gx.lang.gxBoolean(qViewer.Paging) ? PageSize : 0);
					str += ',';
					str += '"ReturnTotPages":' + ReturnTotPages;
					str += ',';
					str += '"Order":' + OrderJSON(DataFieldOrder, OrderType);
					str += (filtersJSON != '' ? ',' + filtersJSON : '');
					str += (qViewer.QueryInfo != '' ? ',' + '"QueryInfo":' + qViewer.QueryInfo : '');
					str += (qViewer.AppSettings != '' ? ',' + '"AppSettings":' + qViewer.AppSettings : '');
					str += (runtimeParametersJSON != '' ? ',' + runtimeParametersJSON : '');
					str += (runtimeFieldsJSON != '' ? ',' + runtimeFieldsJSON : '');
					str += ',';
					str += '"AllowElementsOrderChange":' + qViewer.AllowElementsOrderChange;
					str += ',';
					str += '"RecordsetCacheInfo":' + RecordsetCacheInfoJSON(qViewer);
					str += ',';
					str += '"LayoutChanged":' + LayoutChanged;
					str += ',';
					str += '"ReturnSampleData":' + qViewer.ReturnSampleData;
					str += ',';
					str += '"TranslationType":"' + qViewer.TranslationType + '"';
					str += '}';
					return "Data=" + encodeURIComponent(str);
				},

				PivotTablePageData: function (qViewer, PageNumber, PageSize, ReturnTotPages, AxesInfo, DataInfo, Filters, ExpandCollapse, LayoutChanged) {
					var str = "";
					var runtimeParametersJSON = RuntimeParametersJSON(qViewer);
					var runtimeFieldsJSON = RuntimeFieldsJSON(qViewer, true);
					var axesInfoJSON = AxesInfoJSON(AxesInfo);
					var dataInfoJSON = DataInfoJSON(DataInfo);
					var filtersJSON = FiltersJSON(Filters);
					var expandCollapseJSON = ExpandCollapseJSON(ExpandCollapse);
					str += '{';
					str += getObjectBasicInfo(qViewer);
					str += ',';
					str += '"PageNumber":' + PageNumber;
					str += ',';
					str += '"PageSize":' + (gx.lang.gxBoolean(qViewer.Paging) ? PageSize : 0);
					str += ',';
					str += '"ReturnTotPages":' + ReturnTotPages;
					str += ',';
					str += '"ShowDataLabelsIn":"' + qViewer.ShowDataLabelsIn + '"';
					str += ',';
					str += '"TotalForRows":"' + qViewer.TotalForRows + '"';
					str += ',';
					str += '"TotalForColumns":"' + qViewer.TotalForColumns + '"';
					str += (axesInfoJSON != '' ? ',' + axesInfoJSON : '');
					str += (dataInfoJSON != '' ? ',' + dataInfoJSON : '');
					str += (filtersJSON != '' ? ',' + filtersJSON : '');
					str += (expandCollapseJSON != '' ? ',' + expandCollapseJSON : '');
					str += (qViewer.QueryInfo != '' ? ',' + '"QueryInfo":' + qViewer.QueryInfo : '');
					str += (qViewer.AppSettings != '' ? ',' + '"AppSettings":' + qViewer.AppSettings : '');
					str += (runtimeParametersJSON != '' ? ',' + runtimeParametersJSON : '');
					str += (runtimeFieldsJSON != '' ? ',' + runtimeFieldsJSON : '');
					str += ',';
					str += '"AllowElementsOrderChange":' + qViewer.AllowElementsOrderChange;
					str += ',';
					str += '"RecordsetCacheInfo":' + RecordsetCacheInfoJSON(qViewer);
					str += ',';
					str += '"LayoutChanged":' + LayoutChanged;
					str += ',';
					str += '"ReturnSampleData":' + qViewer.ReturnSampleData;
					str += ',';
					str += '"TranslationType":"' + qViewer.TranslationType + '"';
					str += '}';
					return "Data=" + encodeURIComponent(str);
				},

				getQueryParameters: function (qViewer) {
					var str = "";
					str += '{';
					str += getObjectBasicInfo(qViewer);
					str += (qViewer.ObjectInfo != '' ? ',' + '"ObjectInfo":' + qViewer.ObjectInfo : '');
					str += '}';
					return "Data=" + encodeURIComponent(str);
				},

				DashboardSpec: function (dViewer) {
					var objectName = "";
					if (dViewer.Object)
						objectName = GetObjectNameFromObjectProperty(dViewer.Object);
					var postInfo = { ApplicationNamespace: dViewer.ParentObject.PackageName, ObjectName: objectName, Alt_ObjectId: 0 };
					var postInfoStr = JSON.stringify(postInfo);
					return "Data=" + encodeURIComponent(postInfoStr);
				},

				_priv: {

					RuntimeParametersJSON: function (qViewer) {
						return RuntimeParametersJSON(qViewer)
					},

					RuntimeFieldsJSON: function (qViewer) {
						return RuntimeFieldsJSON(qViewer)
					},

					SdtWithValuesJSON: function (sdtName, actualType, typeWithValues, values, encodevalues) {
						return SdtWithValuesJSON(sdtName, actualType, typeWithValues, values, encodevalues);
					}

				}

			}

		})(),

		RuntimeParametersJSON: function (qViewer) {
			return this.postInfo._priv.RuntimeParametersJSON(qViewer);
		},

		RuntimeFieldsJSON: function (qViewer, encodevaluescollection) {
			return this.postInfo._priv.RuntimeFieldsJSON(qViewer, encodevaluescollection);
		},

		getXMLHttpRequest: function () {
			if (window.XMLHttpRequest) {
				return new window.XMLHttpRequest;
			} else {
				try {
					return new ActiveXObject("MSXML2.XMLHTTP.3.0");
				} catch (ex) {
					return null;
				}
			}
		},

		CallServerSync: function (qViewer, serviceUrlFn, postInfoFn, postInfoParms) {
			postInfoParms = postInfoParms || [];
			postInfoParms.unshift(qViewer);
			var src = serviceUrlFn.call(qv.services.url, qViewer);
			var postInfo = postInfoFn.apply(qv.services.postInfo, postInfoParms);
			var xmlHttp = qv.services.getXMLHttpRequest();
			xmlHttp.open("POST", src, false); // sync
			xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			xmlHttp.send(postInfo);
			return xmlHttp.responseText;
		},

		createAsyncServerCallFn: function (qViewer, serviceUrlFn, postInfoFn, externalQueryMember, responsePreProcessFn) {
			return function (callback, postInfoParms) {
				postInfoParms = postInfoParms || [];
				postInfoParms.unshift(qViewer);
				var responseFn = function (response) {
					if (responsePreProcessFn && qViewer.RealType != QueryViewerOutputType.Card) {
						response = responsePreProcessFn.call(qv.util.css, response);
					}
					callback(response, qViewer);
				};
				if (!gx.lang.gxBoolean(qViewer.IsExternalQuery)) {
					var src = serviceUrlFn.call(qv.services.url, qViewer)
					var	postInfo = postInfoFn.apply(qv.services.postInfo, postInfoParms)
					var	xmlHttp = qv.services.getXMLHttpRequest()
					var	responseHandler = function () {
							if (xmlHttp.readyState == STATE_DONE && xmlHttp.status == STATUS_OK) {
								responseFn(xmlHttp.responseText);
							}
						};
					if (gx.util.browser.isIE())
						xmlHttp.onreadystatechange = responseHandler;
					else
						xmlHttp.onload = responseHandler;
					xmlHttp.open("POST", src); // async
					xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
					xmlHttp.send(postInfo);
				}
				else
					responseFn(qViewer.ExternalQueryResultObj()[externalQueryMember]);
			}
		},

		parseRecordsetCacheKeyXML: function (qViewer) {
			if (qViewer.xml.recordsetCacheKey != "") {
				qViewer.RecordsetCache = qViewer.RecordsetCache || {};
				qViewer.RecordsetCache.OldKey = (qViewer.RecordsetCache.ActualKey ? qViewer.RecordsetCache.ActualKey : "");
				qViewer.RecordsetCache.ActualKey = qViewer.xml.recordsetCacheKey;	// Es un texto plano, no un XML que haya que parsear
			}
		},

		parseDataXML: function (qViewer) {
			if (qViewer.xml.data != "") {
				var xmlDoc = qv.util.dom.xmlDocument(qViewer.xml.data);
				var rootElement = qv.util.dom.getSingleElementByTagName(xmlDoc, "Recordset");
				qViewer.Data = qViewer.Data || {};
				var rows;
				if (!gx.util.browser.isIE()) {
					rows = qv.util.dom.getMultipleElementsByTagName(rootElement, "Record");
				} else {
					rows = qv.util.dom.getMultipleElementsByTagName(qv.util.dom.getSingleElementByTagName(rootElement, "Page"), "Record");
				}
				qViewer.Data.Rows = [];
				for (var i = 0; i < rows.length; i++) {
					var row = {};
					for (var j = 0; j < rows[i].childNodes.length; j++) {
						var node = rows[i].childNodes[j];
						if (node.nodeType == 1) {
							var name = node.nodeName;
							var value = (node.childNodes.length == 1 ? node.childNodes[0].nodeValue : "");
							row[name] = value;
						}
					}
					qViewer.Data.Rows.push(row);
				}
			}
		},

		parseMetadataXML: function (qViewer) {

			function translateStyleOperator(op1, op2) {
				if (op1 == "greaterequal" && op2 == "lessequal")
					return QueryViewerConditionOperator.Interval;
				else
					switch (op1) {
						case "equal":
							return QueryViewerConditionOperator.Equal;
						case "less":
							return QueryViewerConditionOperator.LessThan;
						case "greater":
							return QueryViewerConditionOperator.GreaterThan;
						case "lessequal":
							return QueryViewerConditionOperator.LessOrEqual;
						case "greaterequal":
							return QueryViewerConditionOperator.GreaterOrEqual;
						case "notequal":
							return QueryViewerConditionOperator.NotEqual;
					}
			}

			function getValuesGroup(parentElement, groupTag, valueTag, searchTotalValue) {
				var obj = {};
				var groupElement = qv.util.dom.getSingleElementByTagName(parentElement, groupTag);
				if (groupElement != null) {
					obj.GroupFound = true;
					obj.TotalValueFound = false;
					var values = qv.util.dom.getMultipleElementsByTagName(groupElement, valueTag);
					obj.Values = [];
					for (var i = 0; i < values.length; i++) {
						var value = (values[i].firstChild != null ? values[i].firstChild.nodeValue : "");
						if (value == "TOTAL" && searchTotalValue)
							obj.TotalValueFound = true;
						else
							obj.Values.push(value);
					}
				}
				else
					obj.GroupFound = false;
				return obj;
			}

			function removeCSSReplaceMarker(style) {
				var replaceMarker = "gxpl_cssReplace(";
				if (style.indexOf(replaceMarker) == 0) {	// El style es en realidad el nombre de una clase
					var posIni = style.indexOf(replaceMarker);
					var posFin = style.indexOf(")", posIni);
					style = style.substring(posIni + replaceMarker.length, posFin);
				}
				return style;
			}

			function getValuesStyles(parentElement) {
				var valuesStylesElements;
				var parentElement2 = qv.util.dom.getSingleElementByTagName(parentElement, "formatValues");
				if (parentElement2 != null)
					valuesStylesElements = qv.util.dom.getMultipleElementsByTagName(parentElement2, "value");
				else
					valuesStylesElements = [];
				var ValuesStyles = []
				for (var i = 0; i < valuesStylesElements.length; i++) {
					var valueStyle = {};
					valueStyle.Value = qv.util.trim(valuesStylesElements[i].firstChild != null ? valuesStylesElements[i].firstChild.nodeValue : "");
					valueStyle.StyleOrClass = removeCSSReplaceMarker(valuesStylesElements[i].getAttribute("format"));
					valueStyle.ApplyToRowOrColumn = valuesStylesElements[i].getAttribute("recursive") == "yes";
					ValuesStyles.push(valueStyle);
				}
				return ValuesStyles;
			}

			function getConditionalStyles(parentElement) {
				var conditionalStylesElements;
				var parentElement2 = qv.util.dom.getSingleElementByTagName(parentElement, "conditionalFormats");
				if (parentElement2 != null)
					conditionalStylesElements = qv.util.dom.getMultipleElementsByTagName(parentElement2, "rule");
				else
					conditionalStylesElements = [];
				var ConditionalStyles = []
				for (var i = 0; i < conditionalStylesElements.length; i++) {
					var conditionalStyle = {};
					conditionalStyle.Operator = translateStyleOperator(conditionalStylesElements[i].getAttribute("op1"), conditionalStylesElements[i].getAttribute("op2"));
					conditionalStyle.Value1 = parseFloat(conditionalStylesElements[i].getAttribute("value1"));
					if (conditionalStyle.Operator == QueryViewerConditionOperator.Interval)
						conditionalStyle.Value2 = parseFloat(conditionalStylesElements[i].getAttribute("value2"));
					conditionalStyle.StyleOrClass = removeCSSReplaceMarker(conditionalStylesElements[i].getAttribute("format"));
					ConditionalStyles.push(conditionalStyle);
				}
				return ConditionalStyles;
			}

			if (qViewer.xml.metadata != "") {
				var xmlDoc = qv.util.dom.xmlDocument(qViewer.xml.metadata);
				var rootElement = qv.util.dom.getSingleElementByTagName(xmlDoc, "OLAPCube");
				var servicesVersion = rootElement.getAttribute("Version");
				if (ServicesVersionOK(servicesVersion)) {
					qViewer.Metadata = qViewer.Metadata || {};
					// Obtengo propiedades generales
					qViewer.Metadata.TextForNullValues = rootElement.getAttribute("textForNullValues");
					// Obtengo los ejes
					var axes = qv.util.dom.getMultipleElementsByTagName(rootElement, "OLAPDimension");
					qViewer.Metadata.Axes = [];
					for (var i = 0; i < axes.length; i++) {
						var axis = {};
						axis.Name = axes[i].getAttribute("name");
						axis.Title = axes[i].getAttribute("displayName");
						axis.DataField = axes[i].getAttribute("dataField");
						axis.DataType = axes[i].getAttribute("dataType");
						axis.Visible = axes[i].getAttribute("visible");
						axis.Axis = axes[i].getAttribute("axis");
						axis.Picture = axes[i].getAttribute("picture");
						if (axis.Picture == "")
							switch (axis.DataType) {
								case QueryViewerDataType.Integer:
									axis.Picture = "ZZZZZZZZZZZZZZ9";
									break;
								case QueryViewerDataType.Real:
									axis.Picture = "ZZZZZZZZZZZZZZ9.99";
									break;
								case QueryViewerDataType.Date:
									axis.Picture = "99/99/9999";
									break;
								case QueryViewerDataType.DateTime:
									axis.Picture = "99/99/9999 99:99:99";
									break;
								case QueryViewerDataType.GUID:
									axis.Picture = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
									break;
							}
						axis.CanDragToPages = qv.util.dom.getBooleanAttribute(axes[i], "canDragToPages", true);
						axis.RaiseItemClick = qv.util.dom.getBooleanAttribute(axes[i], "raiseItemClick", true);
						axis.IsComponent = qv.util.dom.getBooleanAttribute(axes[i], "isComponent", false);
						axis.Style = axes[i].getAttribute("format");
						var includeGroup = getValuesGroup(axes[i], "include", "value", true);
						var excludeGroup = getValuesGroup(axes[i], "exclude", "value", true);
						var expandGroup = getValuesGroup(axes[i], "expand", "value", false);
						var customOrderGroup = getValuesGroup(axes[i], "customOrder", "Value", false);
						if (axes[i].getAttribute("summarize") == "no")
							axis.Subtotals = QueryViewerSubtotals.No;
						else {
							if (excludeGroup.GroupFound && excludeGroup.TotalValueFound)
								axis.Subtotals = QueryViewerSubtotals.Hidden;
							else
								axis.Subtotals = QueryViewerSubtotals.Yes;
						}
						axis.Filter = {};
						if (!includeGroup.GroupFound && !excludeGroup.GroupFound) {
							axis.Filter.Type = QueryViewerFilterType.ShowAllValues;
							axis.Filter.Values = [];
						}
						else if (!includeGroup.GroupFound && excludeGroup.GroupFound && excludeGroup.Values.length == 0 & excludeGroup.TotalValueFound) {
							axis.Filter.Type = QueryViewerFilterType.ShowAllValues;
							axis.Filter.Values = [];
						}
						else if (includeGroup.GroupFound && !excludeGroup.GroupFound && includeGroup.Values.length == 0) {
							axis.Filter.Type = QueryViewerFilterType.HideAllValues;
							axis.Filter.Values = [];
						}
						else {
							axis.Filter.Type = QueryViewerFilterType.ShowSomeValues;
							axis.Filter.Values = includeGroup.Values;
						}
						axis.ExpandCollapse = {};
						if (!expandGroup.GroupFound) {
							axis.ExpandCollapse.Type = QueryViewerExpandCollapseType.ExpandAllValues;
							axis.ExpandCollapse.Values = [];
						}
						else if (expandGroup.GroupFound && expandGroup.Values.length == 0) {
							axis.ExpandCollapse.Type = QueryViewerExpandCollapseType.CollapseAllValues;
							axis.ExpandCollapse.Values = [];
						}
						else {
							axis.ExpandCollapse.Type = QueryViewerExpandCollapseType.ExpandSomeValues;
							axis.ExpandCollapse.Values = expandGroup.Values;
						}
						axis.Order = {};
						var order = axes[i].getAttribute("order");
						if (order == null) {
							axis.Order.Type = QueryViewerAxisOrderType.None;
							axis.Order.Values = [];
						}
						else {
							axis.Order.Type = qv.util.capitalize(order);
							if (axis.Order.Type == QueryViewerAxisOrderType.Custom)
								axis.Order.Values = customOrderGroup.Values;
							else
								axis.Order.Values = [];
						}
						axis.ValuesStyles = getValuesStyles(axes[i]);
						qViewer.Metadata.Axes.push(axis);
					}
					// Obtengo los datos
					var data = qv.util.dom.getMultipleElementsByTagName(rootElement, "OLAPMeasure");
					qViewer.Metadata.Data = [];
					for (var i = 0; i < data.length; i++) {
						var datum = {};
						datum.Name = data[i].getAttribute("name");
						datum.Title = data[i].getAttribute("displayName");
						datum.DataField = data[i].getAttribute("dataField");
						datum.Aggregation = data[i].getAttribute("aggregation");
						datum.DataType = data[i].getAttribute("dataType");
						datum.Visible = data[i].getAttribute("visible");
						datum.Picture = data[i].getAttribute("picture");
						datum.RaiseItemClick = qv.util.dom.getBooleanAttribute(data[i], "raiseItemClick", true);
						datum.IsComponent = qv.util.dom.getBooleanAttribute(data[i], "isComponent", false);
						if (datum.Picture == "")
							datum.Picture = datum.DataType == QueryViewerDataType.Integer ? "ZZZZZZZZZZZZZZ9" : "ZZZZZZZZZZZZZZ9.99";
						datum.Aggregation = qv.util.capitalize(datum.Aggregation);
						datum.TargetValue = parseFloat(data[i].getAttribute("targetValue"));
						datum.MaximumValue = parseFloat(data[i].getAttribute("maximumValue"));
						datum.Style = data[i].getAttribute("format");
						if (datum.TargetValue <= 0)
							datum.TargetValue = 100;
						if (datum.MaximumValue <= 0)
							datum.MaximumValue = 100;
						datum.ConditionalStyles = getConditionalStyles(data[i]);
						var formula = qv.util.dom.getCharacterAttribute(data[i], "formula", "");
						datum.IsFormula = formula != "";
						datum.Formula = formula;
						qViewer.Metadata.Data.push(datum);
					}
					qViewer.Metadata.ParserResult = ParserResult(true, "");
				}
				else {
					qViewer.Metadata.ParserResult = ParserResult(false, gx.getMessage("GXPL_QViewerWrongMetadataVersion").replace("{0}", METADATA_VERSION));
				}
			}
		},

		GetDataIfNeeded: function (qViewer, callbackFn) {
			qViewer.Data = qViewer.Data || {};
			qViewer.Data.LastServiceCallKey = qViewer.Data.LastServiceCallKey || {};
			if (qViewer.xml.data == undefined || DataChanged(qViewer, qViewer.Data.LastServiceCallKey) || gx.lang.gxBoolean(qViewer.IsExternalQuery))
				qViewer.calculatePivottableData(callbackFn);
			else
				callbackFn(qViewer.xml.data, qViewer);
		},

		GetMetadataIfNeeded: function (qViewer, callbackFn) {
			qViewer.Metadata = qViewer.Metadata || {};
			qViewer.Metadata.LastServiceCallKey = qViewer.Metadata.LastServiceCallKey || {};
			if (qViewer.xml.metadata == undefined || MetadataChanged(qViewer, qViewer.Metadata.LastServiceCallKey) || gx.lang.gxBoolean(qViewer.IsExternalQuery))
				qViewer.calculatePivottableMetadata(callbackFn);
			else
				callbackFn(qViewer.xml.metadata, qViewer);
		},

		GetDashboardSpecIfNeeded: function (dViewer, callbackFn) {
			dViewer.DashboardSpec = dViewer.DashboardSpec || "";
			dViewer.LastServiceCallKey = dViewer.LastServiceCallKey || {};
			if (dViewer.DashboardSpec == undefined || DashboardSpecChanged(dViewer, dViewer.LastServiceCallKey)) {
				dViewer.LoadingSpec = true;
				dViewer.GetDashboardSpec(callbackFn);
			}
			else
				callbackFn(dViewer.DashboardSpec, dViewer);
		},

		GetRecordsetCacheKeyIfNeeded: function (qViewer, callbackFn) {
			if (qViewer.UseRecordsetCache && !gx.lang.gxBoolean(qViewer.IsExternalQuery)) {
				qViewer.RecordsetCache = qViewer.RecordsetCache || {};
				qViewer.RecordsetCache.LastServiceCallKey = qViewer.RecordsetCache.LastServiceCallKey || {};
				if (qViewer.xml.recordsetCacheKey == undefined || DataChanged(qViewer, qViewer.RecordsetCache.LastServiceCallKey))
					qViewer.getRecordsetCacheKey(callbackFn);
				else
					callbackFn(qViewer.xml.recordsetCacheKey, qViewer);
			}
			else
				callbackFn("", qViewer);
		},

		IsObjectSet: function (qViewer) {
			return qViewer.Object || qViewer.ObjectName != "" || (qViewer.ObjectType != "" && qViewer.ObjectId != 0);
		}

	}

})()


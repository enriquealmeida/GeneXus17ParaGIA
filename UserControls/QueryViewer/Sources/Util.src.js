var HIGHCHARTS_COLOR_PREFIX = "highcharts-color-";
var HIGHCHARTS_MAX_COLORS = 36;
var HIGHCHARTS_CUSTOM_COLOR = [];

var qv = { collection: {}, fadeTimeouts: {} }

var QueryViewerVisible = { Always: "Always", Yes: "Yes", No: "No", Never: "Never" };
var QueryViewerElementType = { Axis: "Axis", Datum: "Datum" };
var QueryViewerAxisType = { Rows: "Rows", Columns: "Columns", Pages: "Pages" };
var QueryViewerFilterType = { ShowAllValues: "ShowAllValues", HideAllValues: "HideAllValues", ShowSomeValues: "ShowSomeValues" };
var QueryViewerAxisOrderType = { None: "None", Ascending: "Ascending", Descending: "Descending", Custom: "Custom" };
var QueryViewerExpandCollapseType = { ExpandAllValues: "ExpandAllValues", CollapseAllValues: "CollapseAllValues", ExpandSomeValues: "ExpandSomeValues" };
var QueryViewerDataType = { Integer: "integer", Real: "real", Character: "character", Boolean: "boolean", Date: "date", DateTime: "datetime", GUID: "guid", GeoPoint: "geopoint" };
var QueryViewerAggregationType = { Sum: "Sum", Average: "Average", Count: "Count", Max: "Max", Min: "Min" };
var QueryViewerOutputType = { Card: "Card", Chart: "Chart", PivotTable: "PivotTable", Table: "Table", Map: "Map", Default: "Default" };
var QueryViewerMapType = { Choropleth: "Choropleth", Bubble: "Bubble" };
var QueryViewerRegion = { Country: "Country", Continent: "Continent", World: "World" };
var QueryViewerContinent = { SouthAmerica: "SouthAmerica", NorthAmerica: "NorthAmerica", Africa: "Africa", Asia: "Asia", Europe: "Europe", Oceania: "Oceania", Antarctica: "Antarctica" };
var QueryViewerCountry = {
	AF: "AF", AL: "AL", DZ: "DZ", AD: "AD", AO: "AO", AI: "AI", AG: "AG", AR: "AR", AM: "AM", AU: "AU", AT: "AT", AZ: "AZ", BH: "BH", BD: "BD", BB: "BB", BY: "BY", BE: "BE", BZ: "BZ", BJ: "BJ", BM: "BM", BT: "BT", BO: "BO",
	BA: "BA", BW: "BW", BR: "BR", VG: "VG", BN: "BN", BG: "BG", BF: "BF", BI: "BI", KH: "KH", CM: "CM", CA: "CA", CV: "CV", CF: "CF", TD: "TD", CL: "CL", CN: "CN", CO: "CO", KM: "KM", CK: "CK", CR: "CR", HR: "HR", CU: "CU",
	CY: "CY", CZ: "CZ", CD: "CD", DK: "DK", DJ: "DJ", DM: "DM", DO: "DO", TL: "TL", EC: "EC", EG: "EG", SV: "SV", GQ: "GQ", ER: "ER", EE: "EE", SZ: "SZ", ET: "ET", FO: "FO", FJ: "FJ", FI: "FI", FR: "FR", GA: "GA", GM: "GM",
	GE: "GE", DE: "DE", GH: "GH", GR: "GR", GL: "GL", GD: "GD", GT: "GT", GN: "GN", GW: "GW", GY: "GY", HT: "HT", HN: "HN", HU: "HU", IS: "IS", IN: "IN", ID: "ID", IR: "IR", IQ: "IQ", IE: "IE", IL: "IL", IT: "IT", CI: "CI",
	JM: "JM", JP: "JP", JO: "JO", KZ: "KZ", KE: "KE", KI: "KI", XK: "XK", KW: "KW", KG: "KG", LA: "LA", LV: "LV", LB: "LB", LS: "LS", LR: "LR", LY: "LY", LI: "LI", LT: "LT", LU: "LU", MG: "MG", MW: "MW", MY: "MY", MV: "MV",
	ML: "ML", MT: "MT", MH: "MH", MR: "MR", MU: "MU", MX: "MX", FM: "FM", MD: "MD", MC: "MC", MN: "MN", ME: "ME", MA: "MA", MZ: "MZ", MM: "MM", NA: "NA", NR: "NR", NP: "NP", NL: "NL", NZ: "NZ", NI: "NI", NE: "NE", NG: "NG",
	KP: "KP", MK: "MK", NO: "NO", OM: "OM", PK: "PK", PW: "PW", PA: "PA", PS: "PS", PG: "PG", PY: "PY", PE: "PE", PH: "PH", PL: "PL", PT: "PT", PR: "PR", QA: "QA", CG: "CG", RO: "RO", RU: "RU", RW: "RW", KN: "KN", LC: "LC",
	VC: "VC", WS: "WS", SM: "SM", ST: "ST", SA: "SA", SN: "SN", RS: "RS", SC: "SC", SL: "SL", SG: "SG", SK: "SK", SI: "SI", SB: "SB", SO: "SO", ZA: "ZA", KR: "KR", SS: "SS", ES: "ES", LK: "LK", SD: "SD", SR: "SR", SE: "SE",
	CH: "CH", SY: "SY", TW: "TW", TJ: "TJ", TZ: "TZ", TH: "TH", BS: "BS", TG: "TG", TK: "TK", TO: "TO", TT: "TT", TN: "TN", TR: "TR", TM: "TM", TV: "TV", UG: "UG", UA: "UA", AE: "AE", GB: "GB", US: "US", UY: "UY", UZ: "UZ",
	VU: "VU", VA: "VA", VE: "VE", VN: "VN", EH: "EH", YE: "YE", ZM: "ZM", ZW: "ZW"
};
var QueryViewerMapLibrary = { Highcharts: "Highcharts", ECharts: "ECharts" };

var QueryViewerChartType = { Column: "Column", Column3D: "Column3D", StackedColumn: "StackedColumn", StackedColumn3D: "StackedColumn3D", StackedColumn100: "StackedColumn100", Bar: "Bar", StackedBar: "StackedBar", StackedBar100: "StackedBar100", Area: "Area", StackedArea: "StackedArea", StackedArea100: "StackedArea100", SmoothArea: "SmoothArea", StepArea: "StepArea", Line: "Line", StackedLine: "StackedLine", StackedLine100: "StackedLine100", SmoothLine: "SmoothLine", StepLine: "StepLine", Pie: "Pie", Pie3D: "Pie3D", Doughnut: "Doughnut", Doughnut3D: "Doughnut3D", LinearGauge: "LinearGauge", CircularGauge: "CircularGauge", Radar: "Radar", FilledRadar: "FilledRadar", PolarArea: "PolarArea", Funnel: "Funnel", Pyramid: "Pyramid", ColumnLine: "ColumnLine", Column3DLine: "Column3DLine", Timeline: "Timeline", SmoothTimeline: "SmoothTimeline", StepTimeline: "StepTimeline", Sparkline: "Sparkline" };
var QueryViewerConditionOperator = { Equal: "EQ", LessThan: "LT", GreaterThan: "GT", LessOrEqual: "LE", GreaterOrEqual: "GE", NotEqual: "NE", Interval: "IN" };
var QueryViewerConditionOperatorSymbol = { Equal: "=", LessThan: "<", GreaterThan: ">", LessOrEqual: "≤", GreaterOrEqual: "≥", NotEqual: "<>", Interval: "-" };
var QueryViewerOrientation = { Horizontal: "Horizontal", Vertical: "Vertical" };
var QueryViewerPlotSeries = { InTheSameChart: "InTheSameChart", InSeparateCharts: "InSeparateCharts" };
var QueryViewerShowDataAs = { Values: "Values", Percentages: "Percentages", ValuesAndPercentages: "ValuesAndPercentages" };
var QueryViewerSubtotals = { Yes: "Yes", Hidden: "Hidden", No: "No" };
var QueryViewerTrendPeriod = { SinceTheBeginning: "SinceTheBeginning", LastYear: "LastYear", LastSemester: "LastSemester", LastQuarter: "LastQuarter", LastMonth: "LastMonth", LastWeek: "LastWeek", LastDay: "LastDay", LastHour: "LastHour", LastMinute: "LastMinute", LastSecond: "LastSecond" };
var QueryViewerXAxisLabels = { Horizontally: "Horizontally", Rotated30: "Rotated30", Rotated45: "Rotated45", Rotated60: "Rotated60", Vertically: "Vertically" };
var QueryViewerAutoresizeType = { Both: "Both", Vertical: "Vertical", Horizontal: "Horizontal" };
var QueryViewerTranslationType = { NoTranslation: "None", Static: "Static", RunTime: "RunTime" };

qv.util = {

	color: {

		getHtmlColor: function (color) {
			var htmlColor;
			if ((typeof (color) == "string") || (typeof (color) == "number")) {
				if (gx.color.toHex) {
					if (color == -1) // -1 = null color
						htmlColor = "";
					else htmlColor = "#" + gx.color.toHex(color); // GeneXus X Ev. 1
				} else htmlColor = "#" + gxToHex(color) // GeneXus X
			} else htmlColor = (color == undefined ? "#000000" : color.Html);
			return htmlColor;
		},

		parseCSSColor: function (color) {
			if (color.substring(0, 3) == "rgb") {
				var values = color.replace("rgb", "").replace("(", "").replace(")", "").split(",");
				var numColor = gx.color.rgb(parseInt(values[0]), parseInt(values[1]), parseInt(values[2]));
				return this.getHtmlColor(numColor);
			} else return color;
		},

		convertValueToColor: function (value) {
			var valueColor;
			if (value.indexOf("#") == -1)
				valueColor = "#" + value;
			else
				valueColor = value;
			var vColor = valueColor.substring(1, valueColor.length);
			while (vColor.length < 6)
				vColor = "0" + vColor;
			return "#" + vColor;
		},

		IsHexaColor: function (colorStr) {
			return (colorStr.length === 6 && !isNaN(parseInt(colorStr, 16)));
		},

		lightenDarkenColor: function (col, amt) {
			var usePound = false;
			if (col[0] == "#") {
				col = col.slice(1);
				usePound = true;
			}
			var num = parseInt(col, 16);
			var r = (num >> 16) + amt;
			if (r > 255)
				r = 255;
			else if (r < 0)
				r = 0;
			var b = ((num >> 8) & 0x00FF) + amt;
			if (b > 255)
				b = 255;
			else if (b < 0)
				b = 0;
			var g = (num & 0x0000FF) + amt;
			if (g > 255)
				g = 255;
			else if (g < 0)
				g = 0;
			return (usePound ? "#" : "") + (g | (b << 8) | (r << 16)).toString(16);
		}

	},

	css: (function () {

		var CSSStyles = {}

		function getCSSStyle(className) {
			if (CSSStyles[className] != undefined)
				return CSSStyles[className];
			else {
				styleObj = loadCSSStyle(className);
				if (styleObj != undefined) {
					styleTransformed = transformCSSStyle(styleObj)
					CSSStyles[className] = styleTransformed;
					return styleTransformed;
				} else return "";
			}
		}

		function loadCSSStyle(className) {
			var css;
			var cssName = gx.theme + ".css";
			var cssFound = false;
			var styleObj;
			for (var i = 0; i < document.styleSheets.length; i++)
				if (document.styleSheets[i].href != null && document.styleSheets[i].href.indexOf(cssName) >= 0) {
					cssFound = true;
					css = document.styleSheets[i];
					break;
				}
			if (cssFound) if (css.cssRules) crossRules = css.cssRules;
			else if (css.rules) crossRules = css.rules;
			var lengthRuleSelected = Number.MAX_SAFE_INTEGER;
			for (var i = 0; i < crossRules.length; i++) {
				var rule = crossRules[i];
				if (rule.selectorText != undefined)
					if (rule.selectorText.toLowerCase().indexOf("." + className.toLowerCase()) == 0 && rule.selectorText.length < lengthRuleSelected) {
						styleObj = rule.style;
						lengthRuleSelected = rule.selectorText.length;
					}
			}
			return styleObj;
		}

		function transformCSSStyle(styleObj) {
			var strAux = "";
			if (styleObj.color != "") strAux += (strAux == "" ? "" : ";") + "color:" + qv.util.color.parseCSSColor(styleObj.color);
			if (styleObj.backgroundColor != "") strAux += (strAux == "" ? "" : ";") + "backgroundColor:" + qv.util.color.parseCSSColor(styleObj.backgroundColor);
			if (styleObj.borderStyle != "") strAux += (strAux == "" ? "" : ";") + "borderStyle:" + styleObj.borderStyle;
			if (styleObj.borderWidth != "") strAux += (strAux == "" ? "" : ";") + "borderThickness:" + styleObj.borderWidth.replace("pt", "").replace("px", "");
			if (styleObj.borderColor != "") strAux += (strAux == "" ? "" : ";") + "borderColor:" + qv.util.color.parseCSSColor(styleObj.borderColor);
			//if (styleObj.paddingLeft != "")        strAux += (strAux == "" ? "" : ";") + "paddingLeft:"      + styleObj.paddingLeft.replace("pt", "").replace("px", "");
			//if (styleObj.paddingRight != "")       strAux += (strAux == "" ? "" : ";") + "paddingRight:"     + styleObj.paddingRight.replace("pt", "").replace("px", "");
			//if (styleObj.paddingTop != "")         strAux += (strAux == "" ? "" : ";") + "paddingTop:"       + styleObj.paddingTop.replace("pt", "").replace("px", "");
			//if (styleObj.paddingBottom != "")      strAux += (strAux == "" ? "" : ";") + "paddingBottom:"    + styleObj.paddingBottom.replace("pt", "").replace("px", "");
			if (styleObj.fontFamily != "") strAux += (strAux == "" ? "" : ";") + "fontFamily:" + styleObj.fontFamily.replace(/"/g, "").replace(/'/g, "");
			if (styleObj.fontSize != "") strAux += (strAux == "" ? "" : ";") + "fontSize:" + styleObj.fontSize.replace("pt", "").replace("px", "");
			if (styleObj.fontWeight != "") strAux += (strAux == "" ? "" : ";") + "fontWeight:" + styleObj.fontWeight;
			if (styleObj.fontStyle != "") strAux += (strAux == "" ? "" : ";") + "fontStyle:" + styleObj.fontStyle;
			if (styleObj.textDecoration != "") strAux += (strAux == "" ? "" : ";") + "textDecoration:" + styleObj.textDecoration;
			//if (styleObj.textAlign != "")          strAux += (strAux == "" ? "" : ";") + "textAlign:"        + styleObj.textAlign;

			// Faltan
			//    backgroundAlpha   = "1"     // 0 .. 1
			//    cornerRadius    = "0"       // 0 .. n
			//    dropShadowEnabled = "true"    // true | false
			//    shadowDirection   = "right"   // left | right | center
			return strAux;
		}
	
		return {

			replaceCssClasses: function (xml) {
				var replaceMarker = "gxpl_cssReplace(";
				while ((posIni = xml.indexOf(replaceMarker)) >= 0) {
					posFin = xml.indexOf(")", posIni);
					className = xml.substring(posIni + replaceMarker.length, posFin);
					style = getCSSStyle(className);
					toReplace = xml.substring(posIni, posFin + 1);
					xml = xml.replace(toReplace, style);
				}
				return xml;
			},

			parseStyle: function (style) {
				var cssStyle = "";
				if (style != "" && style != undefined) {
					if (style.indexOf(":") < 0)
						return style;	// No se parsea pues el el nombre de una clase
					else {
						var arr = style.split(";");
						for (var i = 0; i < arr.length; i++) {
							var arr2 = arr[i].split(":");
							var key = arr2[0];
							var value = arr2[1];
							var key2 = (key == "borderThickness" ? "borderWidth" : key);
							var units = (key == "borderThickness" || key == "fontSize" ? "px" : "");
							cssStyle += (cssStyle == "" ? "" : ",") + '"' + key2 + '":"' + value + units + '"';
						}
					}
					return "{" + cssStyle + "}";
				}
				return "{}";
			}

		}

	})(),

	autorefresh: (function () {

		return {

			UpdateLayoutSameGroup: function (qViewer, sourceElements, allowElementsOrderChange) {

				function ParseParametersXML(qViewer, xml) {
					var xmlDoc = qv.util.dom.xmlDocument(xml);
					var rootElement = qv.util.dom.getSingleElementByTagName(xmlDoc, "gxpl_ParameterCollection");
					if (qViewer.Metadata == undefined)
						qViewer.Metadata = {};
					qViewer.Metadata.Parameters = [];
					var parameters = qv.util.dom.getMultipleElementsByTagName(rootElement, "gxpl_Parameter");
					for (var i = 0; i < parameters.length; i++) {
						var parameter = {};
						parameter.Name = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(parameters[i], "Name"));
						parameter.Type = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(parameters[i], "Type"));
						parameter.IsCollection = gx.lang.gxBoolean(qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(parameters[i], "IsCollection")));
						qViewer.Metadata.Parameters.push(parameter);
					}
				}

				function UpdateTargetElementsAndParametersFromSourceElements(qViewer, sourceElements) {
				
					function GetParameterInQuery(qViewer, axisName) {
						for (var i = 0 ; i < qViewer.Metadata.Parameters.length; i++) {
							var parameter = qViewer.Metadata.Parameters[i];
							if (parameter.IsCollection && parameter.Name == axisName)
								return [i, parameter];
						}
						return [null, null];
					}

					function GetRuntimeParameter(qViewer, axisName) {
						var runtimeParameter = null;
						for (var i = 0; i < qViewer.Parameters.length; i++) {
							if (qViewer.Parameters[i].Name == axisName) {
								runtimeParameter = qViewer.Parameters[i];
								break;
							}
						}
						return runtimeParameter;
					}

					function GetRuntimeParameterValue(sourceAxis, parameter) {
						var valueList = "";
						var delimiter = (parameter.Type == "I" || parameter.Type == "R" || parameter.Type == "B" ? "" : "\"");
						for (var i = 0; i < sourceAxis.Filter.Values.length; i++) {
							valueList += (valueList == "" ? "" : ",") + delimiter + sourceAxis.Filter.Values[i] + delimiter;
						}
						valueList = "[" + valueList + "]";
						return valueList;
					}

					var elements = [];
					if (!qViewer._parametersCloned) {
						// Clono la collection de parámetros pues aunque se carguen en los eventos con la misma variable el autorefresh agrega diferente a la collection dependiendo de si es chart o no chart
						qViewer._parametersCloned = true;
						qViewer.Parameters = qv.util.cloneObject(qViewer.Parameters);
					}
					var keys = qv.util.GetOrderedElementsKeys(qViewer, sourceElements);
					var targetMetadata = qViewer.GetMetadata();
					for (var i = 0; i < keys.length; i++) {
						var index = parseInt(keys[i].substr(6, 4));
						sourceElement = sourceElements[index];
						var parameterArray = GetParameterInQuery(qViewer, sourceElement.Name);
						var parameterPosition = parameterArray[0];
						var parameter = parameterArray[1];
						var targetElement = qv.util.GetElementInCollection(targetMetadata, "Name", sourceElement.Name);
						var applyToElement = (targetElement != null);
						var applyToParameter = !applyToElement;
						if (applyToElement) {
							qv.util.MergeFields(sourceElement, targetElement);
							elements.push(targetElement);
						}
						if (applyToParameter) {
							// no existe un eje para aplicar los cambios, busco si puedo aplicarlos en un parametro
							if (parameter != null) {
								// existe un parámetro en la query destino, aplico los cambios ahí
								var runtimeParameterValue = GetRuntimeParameterValue(sourceElement, parameter);
								if (qViewer.Object) {
									var array = eval(qViewer.Object);
									array[parameterPosition + 2] = runtimeParameterValue;
									qViewer.Object = JSON.stringify(array);
								}
								else {
									var runtimeParameter = GetRuntimeParameter(qViewer, sourceElement.Name);
									if (runtimeParameter == null) {
										runtimeParameter = {};
										runtimeParameter.Name = sourceElement.Name;
										qViewer.Parameters.push(runtimeParameter);
									}
									runtimeParameter.Value = runtimeParameterValue;
								}
							}
						}
					}
					qViewer.Axes = elements;
				}

				function UpdateQueryViewer(qViewer, sourceElements, allowElementsOrderChange) {
					UpdateTargetElementsAndParametersFromSourceElements(qViewer, sourceElements);
					qViewer.RememberLayout = false;
					qViewer.AllowElementsOrderChange = allowElementsOrderChange;
					qv.util.autorefresh.SaveAxesAndParametersState(qViewer);
					qViewer.realShow();
				}

				if ((qViewer.AutoRefreshGroup != undefined) && (qViewer.AutoRefreshGroup != null) && (qViewer.AutoRefreshGroup != "") && (qv.util.trim(qViewer.AutoRefreshGroup) != "")) {

					qv.util.autorefresh.DeleteAxesAndParametersState(qViewer);
					for (index in qv.collection) {
						qViewerOther = qv.collection[index];
						if (qv.services.IsObjectSet(qViewerOther))
							if (qViewerOther.userControlId() != qViewer.userControlId()) {
								if (qViewer.AutoRefreshGroup == qViewerOther.AutoRefreshGroup) {
									// In same Refresh Group
									qViewerOther._isAutorefresh = true;
									if (qViewerOther.Metadata != undefined && qViewerOther.Metadata.Parameters != undefined)
										UpdateQueryViewer(qViewerOther, sourceElements, allowElementsOrderChange);
									else
										qViewerOther.getQueryParameters(function (parametersXML, qViewer) {
											ParseParametersXML(qViewer, parametersXML);
											UpdateQueryViewer(qViewer, sourceElements, allowElementsOrderChange);
										});
								}
							}
					}
				}

			},

			LoadAxesAndParametersState: function (qViewer) {

				function GetObjectByName(array, name) {
					for (var i = 0; i < array.length; i++) {
						if (array[i].Name.toLowerCase() == name.toLowerCase())
							return array[i];
					}
					return null;
				}
			
				function MergeArraysByName(destinationArr, sourceArr) {
					for (var i = 0; i < sourceArr.length; i++) {
						var sourceObj = sourceArr[i];
						var destinationObj = GetObjectByName(destinationArr, sourceObj.Name);
						if (destinationObj != null)
							destinationObj = Object.assign(destinationObj, sourceObj);
					}
					return destinationArr;
				}
				
				var path = window.location.pathname + "_" + qViewer.userControlId() + "_" + qViewer.ObjectId + "_" + qViewer.ObjectName + "_";
				var cookieparameterID = path + "Parameters";
				var cookieAxesID = path + "Axes";
				var parametersString = qv.util.storage.getItem(cookieparameterID);
				var AxesString = qv.util.storage.getItem(cookieAxesID);
				if (parametersString != null && parametersString != "") {
					qViewer.Parameters = MergeArraysByName(JSON.parse(parametersString), qViewer.Parameters);
					qViewer.RememberLayout = false;
				}
				if (AxesString != null && AxesString != "") {
					qViewer.Axes = MergeArraysByName(JSON.parse(AxesString), qViewer.Axes);
					qViewer.RememberLayout = false;
				}
			},

			SaveAxesAndParametersState: function (qViewer) {
				var path = window.location.pathname + "_" + qViewer.userControlId() + "_" + qViewer.ObjectId + "_" + qViewer.ObjectName + "_";
				var cookieparameterID = path + "Parameters";
				var cookieAxesID = path + "Axes";
				qv.util.storage.setItem(cookieparameterID, JSON.stringify(qViewer.Parameters));
				qv.util.storage.setItem(cookieAxesID, JSON.stringify(qViewer.Axes));
			},

			DeleteAxesAndParametersState: function (qViewer) {
				var path = window.location.pathname + "_" + qViewer.userControlId() + "_" + qViewer.ObjectId + "_" + qViewer.ObjectName + "_";
				var cookieparameterID = path + "Parameters";
				var cookieAxesID = path + "Axes";
				qv.util.storage.removeItem(cookieparameterID);
				qv.util.storage.removeItem(cookieAxesID);
			}

		}

	})(),        
	
	dom: {

		getSingleElementByTagName: function (parent, name) {
			var nodes;
			var node;
			nodes = this.getMultipleElementsByTagName(parent, name);
			node = (nodes.length > 0 ? nodes[0] : null);
			return node;
		},

		getMultipleElementsByTagName: function (parent, name) {
			var nodes;
			var node;
			if (!gx.util.browser.isIE() || gx.util.browser.ieVersion() >= 12)
				nodes = parent.getElementsByTagName(name);
			else // Internet Explorer
				nodes = parent.selectNodes(name);
			return nodes;
		},

		getBooleanAttribute: function (element, attName, defaultValue) {
			var attValue = element.getAttribute(attName);
			if (attValue == null)
				return defaultValue;
			else
				return attValue.toLowerCase() == "true";
		},

		getCharacterAttribute: function (element, attName, defaultValue) {
			var attValue = element.getAttribute(attName);
			if (attValue == null)
				return defaultValue;
			else
				return attValue;
		},

		getValueNode: function (node) {
			if ((node == null) || (node == undefined))
				return null;
			if ((node.firstChild != null) && (node.firstChild != undefined))
				return node.firstChild.nodeValue;
			else
				return null;
		},

		selectXPathNode: function (xmlDoc, xpath) {
			var nodes;
			var node;
			if (xmlDoc.evaluate) { // Firefox, Chrome, Opera and Safari
				nodes = xmlDoc.evaluate(xpath, xmlDoc, null, XPathResult.ANY_TYPE, null);
				node = nodes.iterateNext();
			} else {			// Internet Explorer
				nodes = xmlDoc.selectNodes(xpath);
				node = (nodes.length > 0 ? nodes[0] : null);
			}
			return node;
		},

		xmlDocument: function (text) {
			var xmlDoc;
			if (!gx.util.browser.isIE() || gx.util.browser.ieVersion() >= 12) {
				parser = new DOMParser();
				xmlDoc = parser.parseFromString(text, "text/xml");
			} else // Internet Explorer
			{
				xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
				xmlDoc.async = "false";
				xmlDoc.loadXML(text);
			}
			return xmlDoc;
		},

		getEmptyContainer: function (qViewer) {
			var container = qViewer.getContainerControl();
			while (container.firstChild) container.removeChild(container.firstChild);
			return container;
		},

		setStyle: function (element, styleObj) {
			for (key in styleObj)
				element.style[key] = styleObj[key];
		},

		createText: function (parent, text) {
			var textNode = document.createTextNode(text);
			parent.appendChild(textNode);
			return textNode;
		},

		createElement: function (parent, tagName, id, className, styleObj, onClick, text) {
			var element = document.createElement(tagName);
			if (id != "")
				element.id = id;
			if (className != "")
				element.className = className;
			qv.util.dom.setStyle(element, styleObj);
			if (typeof onClick == 'function')
				element.onclick = onClick;
			if (text != "")
				var textNode = qv.util.dom.createText(element, text);
			if (parent != null)
				parent.appendChild(element);
			return element;
		},

		createTable: function (parent, className, styleObj) {
			var table = qv.util.dom.createElement(parent, "TABLE", "", className, styleObj, null, "");
			var tBody = document.createElement("TBODY");
			table.appendChild(tBody);
			return tBody;
		},

		createRow: function (parentTable) {
			return qv.util.dom.createElement(parentTable, "TR", "", "", {}, null, "");
		},

		createCell: function (parentRow, colSpan, align, styleObj, text) {
			var cell = qv.util.dom.createElement(parentRow, "TD", "", "", styleObj, null, text);
			if (colSpan != 1)
				cell.colSpan = colSpan;
			if (align != "")
				cell.align = align;
			return cell;
		},

		createSpan: function (parent, id, className, title, styleObj, onClick, text) {
			var span = qv.util.dom.createElement(parent, "SPAN", id, className, styleObj, onClick, text);
			if (title != "")
				span.title = title;
			return span;
		},

		createDiv: function (parent, id, className, title, styleObj, text) {
			var div = qv.util.dom.createElement(parent, "DIV", id, className, styleObj, null, text);
			if (title != "")
				div.title = title;
			return div;
		},

		createAnchor: function (parent, id, styleObj, text) {
			return qv.util.dom.createElement(parent, "A", id, "", styleObj, null, text);
		},

		createInput: function (parent, id, type, styleObj) {
			var input = qv.util.dom.createElement(parent, "INPUT", id, "", styleObj, null, "");
			if (type != "")
				input.type = type;
			return input;
		},

		createSelect: function (parent, id) {
			return qv.util.dom.createElement(parent, "SELECT", id, "", {}, null, "");
		},

		createOption: function (parent, value, selected, text) {
			var option = qv.util.dom.createElement(parent, "OPTION", "", "", {}, null, text);
			if (value != "")
				option.value = value;
			if (selected)
				option.selected = true;
			return option;
		},

		createIcon: function (parent, title, styleObj, text) {
			var icon = qv.util.dom.createElement(parent, "I", "", "material-icons", styleObj, null, text);
			if (title != "")
				icon.title = title;
			return icon;
		}

	},

	storage: (function() {

		function createCookie(name, value, days) {
			if (days) {
				var date = new Date();
				date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
				var expires = "; expires=" + date.toGMTString();
			} else
				var expires = "";
			document.cookie = name + "=" + value + expires + "; path=/";
		}

		function readCookie(name) {
			var nameEQ = name + "=";
			var ca = document.cookie.split(';');
			for (var i = 0; i < ca.length; i++) {
				var c = ca[i];
				while (c.charAt(0) == ' ') c = c.substring(1, c.length);
				if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
			}
			return null;
		}

		function eraseCookie(name) {
			createCookie(name, "", -1);
		}

		return {

			setItem: function (key, data) {
				if (!!localStorage)
					localStorage.setItem(key, data);
				else
					createCookie(key, data, 365);
			},

			getItem: function (key) {
				if (!!localStorage)
					return localStorage.getItem(key);
				else
					return readCookie(key);
			},

			removeItem: function (key) {
				if (!!localStorage)
					localStorage.removeItem(key);
				else
					eraseCookie(key);
			}
		}

	})(),

	ExecuteTracker: function (trackerId, trackerData) {
		var trackers = Array();
		for (var i = 0; i < gx.fx.ctx.trackers.length; i++)
			if (gx.fx.ctx.trackers[i].types[0] == trackerId) trackers[trackers.length] = gx.fx.ctx.trackers[i];
		for (var i = 0; i < trackers.length; i++) {
			var tgxO = gx.O;
			gx.O = trackers[i].obj;
			var tCmp = gx.csv.cmpCtx;
			gx.csv.cmpCtx = trackers[i].obj.CmpContext;
			gx.evt.setProcessing(false);
			trackers[i].hdl.call(trackers[i].obj, null, null, trackerData);
			gx.O = tgxO;
			gx.csv.cmpCtx = tCmp;
		}
	},

	satisfyStyle: function (value, conditionalStyle) {
		switch (conditionalStyle.Operator) {
			case QueryViewerConditionOperator.Equal:
				return value == conditionalStyle.Value1;
			case QueryViewerConditionOperator.GreaterThan:
				return value > conditionalStyle.Value1;
			case QueryViewerConditionOperator.LessThan:
				return value < conditionalStyle.Value1;
			case QueryViewerConditionOperator.GreaterOrEqual:
				return value >= conditionalStyle.Value1;
			case QueryViewerConditionOperator.LessOrEqual:
				return value <= conditionalStyle.Value1;
			case QueryViewerConditionOperator.NotEqual:
				return value != conditionalStyle.Value1;
			case QueryViewerConditionOperator.Interval:
				return value >= conditionalStyle.Value1 && value <= conditionalStyle.Value2;
		}
	},

	isGeneXusPreview: function () {
		return IsQueryObjectPreview();
	},

	getGenerator: function () {
		var gen;
		if (gx.gen.isDotNet())
			gen = "C#";
		else if (gx.gen.isJava())
			gen = "Java";
		else
			gen = "";
		return gen;
	},

	getErrorFromText: function (resultText) {
		if (resultText == "<Result OK=\"True\"></Result>")
			return ""; // No hubo error
		else
			return resultText.replace("<Result OK=\"False\"><Dsc>", "").replace("</Dsc></Result>", "");
	},

	trim: function (str) {
		if (typeof str != 'string')
			return null;
		return str.replace(/^[\s]+/, '').replace(/[\s]+$/, '').replace(/[\s]{2,}/, ' ');
	},

	capitalize: function (str) {
		return str.charAt(0).toUpperCase() + str.slice(1);
	},

	decapitalize: function (str) {
		return str.charAt(0).toLowerCase() + str.slice(1);
	},

	dateToString: function (date, includeTime) {
		var dateStr = date.getStringWithFmt("Y4MD").replace(/\//g, "-");
		if (includeTime) {
			date.TimeFmt = 24;
			dateStr += " " + date.getTimeString(true, true);
		}
		return dateStr;
	},

	cloneObject: function (obj) {
		return JSON.parse(JSON.stringify(obj));
	},

	endsWith: function (string, subString) {
		return string.substr(string.length - subString.length, subString.length) == subString;
	},

	startsWith: function (string, subString) {
		return string.substr(0, subString.length) == subString;
	},

	formatNumber: function (number, decimalPrecision, picture, removeTrailingZeroes) {

		var formattedNumber = gx.num.formatNumber(number, decimalPrecision, picture, 0, true, false);
		if (removeTrailingZeroes) {
			if (formattedNumber.indexOf(gx.decimalPoint) >= 0) {
				while (qv.util.endsWith(formattedNumber, "0"))
					formattedNumber = formattedNumber.slice(0, -1);
				if (qv.util.endsWith(formattedNumber, gx.decimalPoint))
					formattedNumber = formattedNumber.slice(0, -1);
			}
		}
		return formattedNumber;
	},

	formatDateTime: function (date, dataType, dateFormat, includeHours, includeMinutes, includeSeconds, includeMilliseconds) {
		var gxDate = new gx.date.gxdate(date, "Y4MD");
		var formattedDate = gxDate.getStringWithFmt(dateFormat);
		if (dataType == QueryViewerDataType.DateTime) {
			gxDate.TimeFmt = 24;
			formattedDate += " " + gxDate.getTimeString(includeMinutes, includeSeconds, includeHours, includeMilliseconds);
		}
		return formattedDate;
	},

	seconsdToText: function (seconds) {
		var text;
		var picture = "ZZZZZZZZZZZZZZ9";
		var decimalPrecision = 0;
		if (seconds < 60)							// less than 1 minute
			text = qv.util.formatNumber(Math.round(seconds), decimalPrecision, picture, false) + " " + qv.util.decapitalize(gx.getMessage("GXPL_QViewerSeconds"));
		else if (seconds < 60 * 60)					// less than 1 hour
			text = qv.util.formatNumber(Math.round(seconds / 60), decimalPrecision, picture, false) + " " + qv.util.decapitalize(gx.getMessage("GXPL_QViewerMinutes"));
		else if (seconds < 60 * 60 * 24)			// less than 1 day
			text = qv.util.formatNumber(Math.round(seconds / 60 / 60), decimalPrecision, picture, false) + " " + qv.util.decapitalize(gx.getMessage("GXPL_QViewerHours"));
		else if (seconds < 60 * 60 * 24 * 30.44)	// less than 1 month
			text = qv.util.formatNumber(Math.round(seconds / 60 / 60 / 24), decimalPrecision, picture, false) + " " + qv.util.decapitalize(gx.getMessage("GXPL_QViewerDays"));
		else if (seconds < 60 * 60 * 24 * 365.25)	// less than 1 year
			text = qv.util.formatNumber(Math.round(seconds / 60 / 60 / 24 / 30.44), decimalPrecision, picture, false) + " " + qv.util.decapitalize(gx.getMessage("GXPL_QViewerMonths"));
		else										// more than 1 year
			text = qv.util.formatNumber(Math.round(seconds / 60 / 60 / 24 / 365.25), decimalPrecision, picture, false) + " " + qv.util.decapitalize(gx.getMessage("GXPL_QViewerYears"));
		return text;
	},

	ParseNumericPicture: function (dataType, picture) {
		var decimalPrecision;
		var useThousandsSeparator;
		var prefix = "";
		var suffix = "";
		if (picture == "") {
			decimalPrecision = (dataType == QueryViewerDataType.Integer ? 0 : 2);
			useThousandsSeparator = false;
		}
		else {			// Saco los datos de la picture
			if (picture.indexOf(".") < 0 && picture.indexOf(",") < 0) {		// No tiene ni punto ni coma
				decimalPrecision = 0;
				useThousandsSeparator = false;
			} else if (picture.indexOf(".") >= 0 && picture.indexOf(",") < 0) {	// Tiene solo punto
				decimalPrecision = (dataType == QueryViewerDataType.Integer ? 0 : picture.length - picture.indexOf(".") - 1);
				useThousandsSeparator = false;
			} else if (picture.indexOf(".") < 0 && picture.indexOf(",") >= 0) {	// Tiene solo coma
				decimalPrecision = 0;
				useThousandsSeparator = true;
			} else {															// Tiene punto y coma
				decimalPrecision = (dataType == QueryViewerDataType.Integer ? 0 : picture.length - picture.indexOf(".") - 1);
				useThousandsSeparator = true;
			}
			// Obtengo prefijo y sufijo.
			// pictureArea = 1 (prefijo), 2 (número) o 3 (sufijo)
			var pictureArea = 1;
			for (var i = 0; i < picture.length; i++) {
				var chr = picture.substr(i, 1);
				if ((chr == "." || chr == "," || chr == "9" || chr == "Z") && pictureArea == 1)
					pictureArea = 2;
				if ((chr != "." && chr != "," && chr != "9" && chr != "Z") && pictureArea == 2)
					pictureArea = 3;
				switch (pictureArea) {
					case 1:
						prefix += chr;
						break;
					case 3:
						suffix += chr;
						break;
				}
			}
		}
		return { DecimalPrecision: decimalPrecision, UseThousandsSeparator: useThousandsSeparator, Prefix: prefix, Suffix: suffix };
	},

	ParseDateTimePicture: function (dataType, picture) {
		var dateFormat = gx.dateFormat;
		var includeHours = dataType == QueryViewerDataType.DateTime;
		var includeMinutes = dataType == QueryViewerDataType.DateTime;
		var includeSeconds = dataType == QueryViewerDataType.DateTime;
		var includeMilliseconds = dataType == QueryViewerDataType.DateTime;
		if (picture != "") {
			if (picture.indexOf("9999") >= 0 && dateFormat.indexOf("Y4") < 0)
				dateFormat = dateFormat.replace("Y", "Y4");
			else if (picture.indexOf("9999") < 0 && dateFormat.indexOf("Y4") >= 0)
				dateFormat = dateFormat.replace("Y4", "Y");
			if (dataType == QueryViewerDataType.DateTime) {
				var posSeparator = picture.indexOf(" ");
				if (posSeparator >= 0) {
					var timePart = picture.substr(posSeparator + 1, picture.length - posSeparator);
					includeHours = timePart.length >= 2;
					includeMinutes = timePart.length >= 5;
					includeSeconds = timePart.length >= 8;
					includeMilliseconds = timePart.length == 12;
				}
				else {
					includeHours = false;
					includeMinutes = false;
					includeSeconds = false;
					includeMilliseconds = false;
				}
			}
		}
		return { DateFormat: dateFormat, IncludeHours: includeHours, IncludeMinutes: includeMinutes, IncludeSeconds: includeSeconds, IncludeMilliseconds: includeMilliseconds };
	},

	evaluate : function (formula, baseName, variables) {
		for (var i = 1; i <= variables.length; i++)
			formula = formula.replace(baseName + i.toString(), variables[i-1]);
		return eval(formula);
	},
	
	aggregate: function (aggregation, values, quantities) {
		aggregation = aggregation || QueryViewerAggregationType.Sum;
		var sumValues = null;
		var sumQuantities = null;
		var minValue = null;
		var maxValue = null;
		switch (aggregation) {
			case QueryViewerAggregationType.Sum:
				for (var i = 0; i < values.length; i++) {
					if (values[i] != null)
						sumValues += values[i];
				}
				return sumValues;
			case QueryViewerAggregationType.Average:
				for (var i = 0; i < values.length; i++) {
					if (values[i] != null) {
						sumValues += values[i];
						sumQuantities += quantities[i];
					}
				}
				return sumValues != null ? sumValues / sumQuantities : null;
			case QueryViewerAggregationType.Count:
				for (var i = 0; i < quantities.length; i++) { sumQuantities += quantities[i]; }
				return sumQuantities;
			case QueryViewerAggregationType.Max:
				for (var i = 0; i < values.length; i++) { maxValue = (maxValue == null ? values[i] : (values[i] > maxValue ? values[i] : maxValue)); }
				return maxValue;
			case QueryViewerAggregationType.Min:
				for (var i = 0; i < values.length; i++) { minValue = (minValue == null ? values[i] : (values[i] < minValue ? values[i] : minValue)); }
				return minValue;
		}
	},

	anyError: function (resultText) {
		return (resultText.indexOf("<Result OK=\"False\"><Dsc>") == 0);
	},

	showActivityIndicator: function (qViewer) {
		var f = 'qv.util.showActivityIndicatorMain("' + qViewer.getContainerControl().id + '");';
		var ucId = qViewer.userControlId();
		if (qv.fadeTimeouts[ucId])
			clearTimeout(qv.fadeTimeouts[ucId]);
		qv.fadeTimeouts[ucId] = setTimeout(f, 500); 
	},

	showActivityIndicatorMain: function (containerId) {
		var container = document.getElementById(containerId);
		gx.dom.addClass(container, "gx-qv-loading");
		for (var i = 0; i < container.childNodes.length; i++)
			gx.dom.addClass(container.childNodes[i], "gx-qv-loading-children");
	},

	hideActivityIndicator: function (qViewer) {
		var ucId = qViewer.userControlId();
		if (qv.fadeTimeouts[ucId]) {
			clearTimeout(qv.fadeTimeouts[ucId]);
			qv.fadeTimeouts[ucId] = undefined;
		}
		var container = qViewer.getContainerControl();
		gx.dom.removeClass(container, "gx-qv-loading");
		for (var i = 0; i < container.childNodes.length; i++)
			gx.dom.removeClass(container.childNodes[i], "gx-qv-loading-children");
	},

	renderError: function (qViewer, errMsg) {
		if (IsQueryObjectPreview() && !IsDashboardEdit()) // Preview en GeneXus (se muestra en el output)
			window.external.SendText(qViewer.ControlName, errMsg);
		else // Aplicación generada
		{
			var container = qv.util.dom.getEmptyContainer(qViewer);
			var div = qv.util.dom.createDiv(container, "", "gx-qv-centered-text", "", { width: gx.dom.addUnits(qViewer.Width), height: gx.dom.addUnits(qViewer.Height), borderColor: "silver", borderWidth: "thin", borderStyle: "solid" }, gx.getMessage("GXPL_QViewerError") + ": " + errMsg);
			qViewer._ControlRenderedTo = undefined;
			qv.util.hideActivityIndicator(qViewer);
		}
	},

	GetDesigntimeMetadata: function (qViewer) {

		var queryViewerAxes = [];
		// Agrego los ejes
		for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
			var axis = qViewer.Metadata.Axes[i];
			if (!axis.IsComponent) {
				var queryViewerElement = {};
				queryViewerElement.Name = axis.Name;
				queryViewerElement.Title = axis.Title;
				queryViewerElement.DataField = axis.DataField;
				queryViewerElement.Visible = axis.Visible;
				queryViewerElement.Type = QueryViewerElementType.Axis;
				queryViewerElement.Axis = axis.Axis;
				queryViewerElement.Filter = axis.Filter;
				queryViewerElement.ExpandCollapse = axis.ExpandCollapse;
				queryViewerElement.AxisOrder = axis.Order;
				queryViewerElement.Format = {};
				queryViewerElement.Format.Subtotals = axis.Subtotals;
				queryViewerElement.Format.Picture = axis.Picture;
				queryViewerElement.Format.CanDragToPages = axis.CanDragToPages;
				queryViewerElement.Format.Style = axis.Style;
				if (axis.ValuesStyles.length > 0)
					queryViewerElement.Format.ValuesStyles = axis.ValuesStyles;
				queryViewerElement.Actions = {};
				queryViewerElement.Actions.RaiseItemClick = axis.RaiseItemClick;
				queryViewerAxes.push(queryViewerElement);
			}
		}
		// Agrego los datos
		for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
			var datum = qViewer.Metadata.Data[i];
			if (!datum.IsComponent) {
				var queryViewerElement = {};
				queryViewerElement.Name = datum.Name;
				queryViewerElement.Title = datum.Title;
				queryViewerElement.DataField = datum.DataField;
				queryViewerElement.Visible = datum.Visible;
				queryViewerElement.Type = QueryViewerElementType.Datum;
				queryViewerElement.Axis = "";
				queryViewerElement.Aggregation = datum.Aggregation;
				queryViewerElement.Format = {};
				queryViewerElement.Format.Picture = datum.Picture;
				queryViewerElement.Format.Style = datum.Style;
				queryViewerElement.Format.TargetValue = datum.TargetValue;
				queryViewerElement.Format.MaximumValue = queryViewerElement.Format.TargetValue;
				if (datum.ConditionalStyles.length > 0)
					queryViewerElement.Format.ConditionalStyles = datum.ConditionalStyles;
				queryViewerElement.Actions = {};
				queryViewerElement.Actions.RaiseItemClick = datum.RaiseItemClick;
				queryViewerAxes.push(queryViewerElement);
			}
		}
		return queryViewerAxes;
	},
	
	MergeFields: function (runtimeField, designtimeField) {
		if (designtimeField.Visible != QueryViewerVisible.Never) {
			designtimeField.Visible = runtimeField.Hidden ? QueryViewerVisible.No : QueryViewerVisible.Yes;
			if (designtimeField.Type == QueryViewerElementType.Axis) {
				designtimeField.Axis = runtimeField.Axis;
				if (runtimeField.Order)
				{
					switch (designtimeField.AxisOrder.Type) {
						case QueryViewerAxisOrderType.None:
						case QueryViewerAxisOrderType.Ascending:
						case QueryViewerAxisOrderType.Descending:
							designtimeField.AxisOrder.Type = runtimeField.Order;
							break;
						case QueryViewerAxisOrderType.Custom:
							if (runtimeField.Order == QueryViewerAxisOrderType.Descending)
								designtimeField.AxisOrder.Values.reverse();
							break;
					}
				}
				if (designtimeField.Format.Subtotals != QueryViewerSubtotals.No)
					if (runtimeField.Subtotals != undefined)
						designtimeField.Format.Subtotals = (runtimeField.Subtotals == QueryViewerSubtotals.Yes ? QueryViewerSubtotals.Yes : QueryViewerSubtotals.Hidden);
				if (runtimeField.Filter)
					designtimeField.Filter = runtimeField.Filter;
				if (runtimeField.ExpandCollapse)
					designtimeField.ExpandCollapse = runtimeField.ExpandCollapse;
			}
		}
	},

	GetOrderedElementsKeys: function (qViewer, elements) {

		function padLeft(nr, n, str) {
			return Array(n - String(nr).length + 1).join(str || '0') + nr;
		}

		var keys = [];
		for (var i = 0; i < elements.length; i++) {
			var element = elements[i];
			var typePrefix;
			if (qViewer.RealType == QueryViewerOutputType.Table)
				typePrefix = "0";														// No se distingue entre ejes y datos
			else
				typePrefix = (element.Type == QueryViewerElementType.Axis ? "1" : "2");	// Los ejes van antes que los datos
			var key = typePrefix;
			if (element.Position != null) {
				var axisPrefix;
				if (qViewer.RealType == QueryViewerOutputType.PivotTable) {				// Se distingue entre tipo de eje
					if (element.Type == QueryViewerElementType.Axis) {
						switch (element.Axis) {
							case QueryViewerAxisType.Columns:
								axisPrefix = "B";
								break;
							case QueryViewerAxisType.Pages:
								axisPrefix = "C";
								break;
							default:
								axisPrefix = "A";
								break;
						}
					}
					else
						axisPrefix = "D";
				}
				else
					axisPrefix = "A";													// No se distingue entre tipo de eje
				key += axisPrefix + padLeft(element.Position, 4, "0") + padLeft(i, 4, "0");
			}
			else
				key += "Z" + padLeft(i, 4, "0") + padLeft(i, 4, "0");		// Elementos invisibles van al final	
			keys.push(key);
		}
		keys.sort();
		return keys;

	},

	GetContainerControlClass: function (qViewer) {
		if (qViewer.Class != "") {
			var ucClass = qViewer.Class + "-" + qViewer.RealType.toLowerCase();
			return ucClass;
		}
		else
			return "";
	},

	GetContainerControlClasses: function (qViewer) {
		var ucCls = qv.util.GetContainerControlClass(qViewer);
		var classes;
		switch (qViewer.RealType) {
			case QueryViewerOutputType.Card:
				classes = "qv-card";
				break;
			case QueryViewerOutputType.Chart:
				classes = "qv-chart";
				break;
			case QueryViewerOutputType.PivotTable:
				classes = "qv-pivottable";
				break;
			case QueryViewerOutputType.Table:
				classes = "qv-table";
				break;
			case QueryViewerOutputType.Map:
				classes = "qv-map";
				break;
		}
		if (ucCls != "")
			classes += " " + ucCls;
		return classes;
	},

	SetUserControlClass: function (uc, className) {
		uc.getContainerControl().className = "gx_usercontrol " + className;
	},
	
	GetElementInCollection: function(col, property, elementName) {
		for (var i = 0 ; i < col.length; i++) {
			var element = col[i];
			if (element[property] == elementName)
				return element;
		}
		return null;
	},

	IsInteger: function(value) {
		if (Number.isInteger)
			return Number.isInteger(value);
		else 
			return typeof value === "number" && isFinite(value) && Math.floor(value) === value;		// Internet Explorer
	},

	ApplyPicture: function (value, picture, dataType, pictureProperties) {
		switch (dataType) {
			case QueryViewerDataType.Integer:
			case QueryViewerDataType.Real:
				return qv.util.formatNumber(value, pictureProperties.DecimalPrecision, picture, false);
			case QueryViewerDataType.Boolean:
			case QueryViewerDataType.Character:
			case QueryViewerDataType.GUID:
			case QueryViewerDataType.GeoPoint:
				return qv.util.trim(value);
			case QueryViewerDataType.Date:
			case QueryViewerDataType.DateTime:
				return qv.util.formatDateTime(value, dataType, pictureProperties.DateFormat, pictureProperties.IncludeHours, pictureProperties.IncludeMinutes, pictureProperties.IncludeSeconds, pictureProperties.IncludeMilliseconds);
		}
	},

	GetPictureProperties: function (dataType, picture) {
		var pictureProperties;
		if (dataType == QueryViewerDataType.Integer || dataType == QueryViewerDataType.Real)
			pictureProperties = qv.util.ParseNumericPicture(dataType, picture);
		else if (dataType == QueryViewerDataType.Date || dataType == QueryViewerDataType.DateTime)
			pictureProperties = qv.util.ParseDateTimePicture(dataType, picture);
		else
			pictureProperties = null;
		return pictureProperties;
	},

	GetAxisByDataField: function (qViewer, dataField) {
		for (var i = 0; i < qViewer.Metadata.Axes.length; i++)
			if (qViewer.Metadata.Axes[i].DataField == dataField)
				return qViewer.Metadata.Axes[i];
	},

	GetThemeStyleSheet: function (qViewer) {
		if (!qViewer._themeStyleSheet) {
			var cssName = gx.theme + ".css";
			qViewer._themeStyleSheet = qv.util.GetStyleSheet(cssName);
		}
		return qViewer._themeStyleSheet;
	},

	GetNullColor: function () {
		return qv.util.GetColorObject("");
	},

	GetColorObject: function (colorStr) {
		return { IsDefault: false, Color: colorStr, ColorIndex: '-1' };
	},

	IsNullColor: function (color) {
		return color.IsDefault ? color.ColorIndex < 0 : color.Color == "";
	},

	GetQueryViewerStyleSheet: function (qViewer) {
		if (!qViewer._queryViewerStyleSheet) {
			var cssName = "QueryViewer.css";
			qViewer._queryViewerStyleSheet = qv.util.GetStyleSheet(cssName);
		}
		return qViewer._queryViewerStyleSheet;
	},

	GetStyleSheet: function (cssName) {
		var styleSheet;
		for (var i = 0; i < document.styleSheets.length; i++)
			if (document.styleSheets[i].href != null && document.styleSheets[i].href.indexOf(cssName) >= 0) {
				styleSheet = document.styleSheets[i];
				break;
			}
		return styleSheet;
	},

	XAxisDataType: function (qViewer) {
		var cantRowsOrColumns = 0;
		var dataType = "";
		for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
			var axis = qViewer.Metadata.Axes[i];
			if (axis.Visible == QueryViewerVisible.Yes || axis.Visible == QueryViewerVisible.Always) {
				cantRowsOrColumns++;
				dataType = axis.DataType;
			}
		}
		if (cantRowsOrColumns == 1)
			return dataType;
		else
			return QueryViewerDataType.Character;		// Pues se concatenan los valores
	},

	ProcessDataAndMetadata: function (qViewer) {

		function TotData(data) {
			var totData = 0;
			for (var i = 0; i < data.length; i++)
				if (data[i].DataField != "F0")	// Quantity
					totData++;
			return totData;
		}

		function VisibleDatum(totData, datum) {
			if (totData == 1)
				return datum.Visible == QueryViewerVisible.Yes || datum.Visible == QueryViewerVisible.Always;
			else
				return datum.Visible != QueryViewerVisible.Never;
		}

		function GetCategoriesAndSeriesDataFields(qViewer) {
			qViewer.Chart.Categories = {};
			qViewer.Chart.Categories.DataFields = [];
			for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
				var axis = qViewer.Metadata.Axes[i];
				if (axis.Visible == QueryViewerVisible.Yes || axis.Visible == QueryViewerVisible.Always)
					if (qViewer.RealType != QueryViewerOutputType.Map || axis.DataType == QueryViewerDataType.Character)	// only character dimensions are valid for Maps
						qViewer.Chart.Categories.DataFields.push(axis.DataField);
			}
			qViewer.Chart.Series = {};
			qViewer.Chart.Series.DataFields = [];
			var totData = TotData(qViewer.Metadata.Data);
			for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
				var datum = qViewer.Metadata.Data[i];
				if (VisibleDatum(totData, datum))
					qViewer.Chart.Series.DataFields.push(datum.DataField);
			}
		}

		function GetAxesByDataFieldObj(qViewer) {
			var axesByDataField = {};
			for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
				var axis = qViewer.Metadata.Axes[i];
				var pictureProperties = qv.util.GetPictureProperties(axis.DataType, axis.Picture);
				axesByDataField[axis.DataField] = { Picture: axis.Picture, DataType: axis.DataType, PictureProperties: pictureProperties, Filter: axis.Filter };
			}
			return axesByDataField;
		}

		function GetDataByDataFieldObj(qViewer, uniqueAxis) {

			function IsMulticoloredSerie(qViewer, datum, uniqueAxis) {

				function ExistColors(styles) {
					// Verifica si hay colores a partir de Styles condicionales
					var existColors = false;
					for (var i = 0; i < styles.length; i++) {
						var style = styles[i];
						var arr = GetColorFromStyle(style.StyleOrClass, false);
						var colorFound = arr[0];
						if (colorFound) {
							existColors = true;
							break;
						}
					}
					return existColors;
				}

				var multicoloredSerie;
				if (qViewer.RealType == QueryViewerOutputType.Map && ((qViewer.MapType == QueryViewerMapType.Choropleth && qViewer.Chart.colorAxis != "") || (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Chart.colorAxis != "")))
					multicoloredSerie = false;					// Estos tipos de gráfica tienen que dibujar sí o sí cada valor de referencia con un color diferente
				else if (qViewer.RealType == QueryViewerOutputType.Map && ((qViewer.MapType == QueryViewerMapType.Choropleth && (qViewer.Chart.colorAxis.dataClasses.length == 0)) || (qViewer.MapType == QueryViewerMapType.Bubble && (qViewer.Chart.colorAxis.dataClasses.length == 0))))
					multicoloredSerie = true;				// En este tipo de mapas todos los valores van a ir del mismo color 
				if (qViewer.RealType == QueryViewerOutputType.Chart && (qv.chart.IsSingleSerieChart(qViewer) || (qViewer.RealChartType == QueryViewerChartType.PolarArea && qViewer.Chart.Series.DataFields.length == 1)))
					multicoloredSerie = true;					// Estos tipos de gráfica tienen que dibujar sí o sí cada valor con un color diferente
				else if (qViewer.RealType == QueryViewerOutputType.Chart && (qv.chart.IsAreaChart(qViewer) || qv.chart.IsLineChart(qViewer) || qViewer.RealChartType == QueryViewerChartType.Radar || qViewer.RealChartType == QueryViewerChartType.FilledRadar))
					multicoloredSerie = false;					// Estos tipos de gráfica no pueden ser multicolores porque son líneas o áreas y no estamos dejando pintar partes de una linea o area de colores diferentes
				else if (qViewer.RealType == QueryViewerOutputType.Chart && (qViewer.Chart.Series.DataFields.length > 1 && !qv.chart.IsSplittedChart(qViewer)))
					multicoloredSerie = false;					// Multi series: al haber más de una serie hay una leyenda indicando el color de cada serie, por lo tanto todos los valores tienen que tener el mismo color
				else {
					// Single series
					var existConditionalColors = ExistColors(datum.ConditionalStyles);
					var existValuesColors = false;
					if (uniqueAxis != null)
						existValuesColors = ExistColors(uniqueAxis.ValuesStyles);	// Si tengo una sola categoria tambien se puede hacer por valor si corresponde
					multicoloredSerie = (existConditionalColors || existValuesColors);	// Es multicolor si existen colores condicionales o colores por valor
				}
				return multicoloredSerie;
			}

			var dataByDataField = {};
			var totData = TotData(qViewer.Metadata.Data);
			for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
				var datum = qViewer.Metadata.Data[i];
				if (VisibleDatum(totData, datum)) {
					var multicolored = IsMulticoloredSerie(qViewer, datum, uniqueAxis)
					dataByDataField[datum.DataField] = { Datum: datum, Multicolored: multicolored };
				}
			}
			return dataByDataField;

		}

		function GetColorFromStyle(style, isBackgroundColor) {
			var color = "";
			var colorFound = false;
			var colorKey = isBackgroundColor ? "backgroundcolor" : "color";
			if (style != "") {
				var keyValuePairs = style.split(";");
				for (var i = 0; i < keyValuePairs.length; i++) {
					var keyValuePairStr = keyValuePairs[i];
					var keyValuePair = keyValuePairStr.split(":");
					if (keyValuePair.length == 2) {
						var key = keyValuePair[0];
						var value = keyValuePair[1];
						if (key.toLowerCase() == colorKey) {
							color = value;
							colorFound = (value != "");
							break;
						}
					}
				}
				if (colorFound && color.substr(0, 1) == "#")
					color = color.replace("#", "");
			}
			return [colorFound, color];
		}

		function GetColor(multicoloredSerie, datum, uniqueAxis, seriesIndex, colorIndex, categoryLabel, value) {

			function GetValueStyleColor(axis, value) {
				// Obtiene el color que corresponde al valor según el ValueStyle
				var arr = [false, ""];
				for (var i = 0; i < axis.ValuesStyles.length; i++) {
					var valueStyle = axis.ValuesStyles[i];
					if (valueStyle.Value == value) {
						arr = GetColorFromStyle(valueStyle.StyleOrClass, false);
						break;
					}
				}
				return arr;
			}

			function GetConditionalColor(datum, value) {
				// Obtiene el color que corresponde al valor según el Style condicional
				var arr = [false, ""];
				var conditionSatisfied = false;
				for (var i = 0; i < datum.ConditionalStyles.length; i++) {
					var conditionalStyle = datum.ConditionalStyles[i];
					conditionSatisfied = qv.util.satisfyStyle(value, conditionalStyle);
					if (conditionSatisfied) {
						arr = GetColorFromStyle(conditionalStyle.StyleOrClass, false);
						break;
					}
				}
				return arr;
			}

			var color;
			var colorIndexAux = -1;
			var isDefaultColor = false;
			var arr;
			if (multicoloredSerie) {		// Cada valor de la serie tiene un color diferente
				var colorFound = false;
				if (uniqueAxis != null) {
					arr = GetValueStyleColor(uniqueAxis, categoryLabel);	// Busco primero en los style por valor
					colorFound = arr[0];
					color = arr[1];
				}
				if (!colorFound) {
					arr = GetConditionalColor(datum, value)	// Busco luego en los styles condicionales
					colorFound = arr[0];
					color = arr[1];
					if (!colorFound) {
						colorIndexAux = colorIndex % HIGHCHARTS_MAX_COLORS;
						isDefaultColor = true;
					}
				}
			}
			else {		// Todos los valores de la serie con el mismo valor
				arr = GetColorFromStyle(datum.Style, false);
				colorFound = arr[0];
				color = arr[1];
				if (!colorFound) {
					colorIndexAux = seriesIndex % HIGHCHARTS_MAX_COLORS;
					isDefaultColor = true;
				}
			}
			return { IsDefault: isDefaultColor, Color: color, ColorIndex: colorIndexAux };
		}

		function AddCategoryValue(qViewer, row, valueIndex, axesByDataField) {

			function GetCategoryLabel(qViewer, row, axesByDataField) {

				var label = "";
				var labelWithPicture = "";
				for (var i = 0; i < qViewer.Chart.Categories.DataFields.length; i++) {
					var dataField = qViewer.Chart.Categories.DataFields[i];
					var value;
					var valueWithPicture;
					if (row[dataField] != undefined) {
						value = qv.util.trim(row[dataField]);
						valueWithPicture = qv.util.ApplyPicture(value, axesByDataField[dataField].Picture, axesByDataField[dataField].DataType, axesByDataField[dataField].PictureProperties);
					}
					else {
						value = qViewer.Metadata.TextForNullValues;
						valueWithPicture = qViewer.Metadata.TextForNullValues;
					}
					label += (label == "" ? "" : ", ") + value;
					labelWithPicture += (labelWithPicture == "" ? "" : ", ") + valueWithPicture;
				}
				return [label, labelWithPicture];
			}

			var arr = GetCategoryLabel(qViewer, row, axesByDataField);
			var categoryValue = {};
			categoryValue.Value = arr[0];
			categoryValue.ValueWithPicture = arr[1];
			qViewer.Chart.Categories.Values.push(categoryValue);
			if (valueIndex == 0) {
				qViewer.Chart.Categories.MinValue = categoryValue.Value;
				qViewer.Chart.Categories.MaxValue = categoryValue.Value;
			}
			else {
				if (categoryValue.Value > qViewer.Chart.Categories.MaxValue)
					qViewer.Chart.Categories.MaxValue = categoryValue.Value;
				if (categoryValue.Value < qViewer.Chart.Categories.MinValue)
					qViewer.Chart.Categories.MinValue = categoryValue.Value;
			}

		}

		function AddSeriesValues(qViewer, row, valueIndex, dataByDataField, uniqueAxis) {
			for (var i = 0; i < qViewer.Chart.Series.DataFields.length; i++) {
				var serie = qViewer.Chart.Series.ByIndex[i]
				var dataField = qViewer.Chart.Series.DataFields[i];
				var value = row[dataField] != undefined ? row[dataField] : null;
				var point = {};
				point.Value = value;
				var datum = dataByDataField[dataField].Datum;
				var multicoloredSerie = dataByDataField[dataField].Multicolored;
				if (datum.Aggregation == QueryViewerAggregationType.Average) {
					var value_N = row[dataField + "_N"];
					var value_D = row[dataField + "_D"];
					if (value_N == undefined && value_D == undefined) {
						// Caso de un dataprovider donde se le asigna agregación = Average por código
						value_N = value;
						value_D = "1";
					}
					point.Value_N = value_N;
					point.Value_D = value_D;
				}
				if (multicoloredSerie)
					point.Color = GetColor(multicoloredSerie, datum, uniqueAxis, 0, valueIndex, qViewer.Chart.Categories.Values[valueIndex].Value, value);
				else
					point.Color = qv.util.GetNullColor();
				serie.Points.push(point);
				if (point.Value > 0) serie.PositiveValues = true;
				if (point.Value < 0) serie.NegativeValues = true;
				if (valueIndex === 0) {
					serie.MinValue = parseFloat(point.Value);
					serie.MaxValue = parseFloat(point.Value);
				}
				else {
					if (parseFloat(point.Value) > serie.MaxValue)
						serie.MaxValue = parseFloat(point.Value);
					if (parseFloat(point.Value) < serie.MinValue)
						serie.MinValue = parseFloat(point.Value);
				}

			}
		}

		function CalculatePlotBands(qViewer, datum) {
			for (var j = 0; j < datum.ConditionalStyles.length; j++) {
				var conditionalStyle = datum.ConditionalStyles[j];
				var arr = GetColorFromStyle(conditionalStyle.StyleOrClass, true);
				var colorFound = arr[0];
				var backgroundColor = arr[1];
				if (colorFound) {
					plotBand = {};
					plotBand.Color = qv.util.GetColorObject(backgroundColor);
					if (conditionalStyle.Operator == QueryViewerConditionOperator.Interval) {
						plotBand.From = parseFloat(conditionalStyle.Value1);
						plotBand.To = parseFloat(conditionalStyle.Value2);
					} else if (conditionalStyle.Operator == QueryViewerConditionOperator.Equal) {
						plotBand.From = parseFloat(conditionalStyle.Value1);
						plotBand.To = parseFloat(conditionalStyle.Value1);
					}
					else if (conditionalStyle.Operator == QueryViewerConditionOperator.GreaterOrEqual || conditionalStyle.Operator == QueryViewerConditionOperator.GreaterThan)
						plotBand.From = parseFloat(conditionalStyle.Value1);
					else if (conditionalStyle.Operator == QueryViewerConditionOperator.LessOrEqual || conditionalStyle.Operator == QueryViewerConditionOperator.LessThan)
						plotBand.To = parseFloat(conditionalStyle.Value1);
					plotBand.SeriesName = datum.Title != "" ? datum.Title : datum.Name;
					qViewer.Chart.PlotBands.push(plotBand);
				}
			}
		}

		function IsFilteredRow(qViewer, row) {
			var filtered = false;
			for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
				var axis = qViewer.Metadata.Axes[i];
				if (axis.Visible == QueryViewerVisible.Yes || axis.Visible == QueryViewerVisible.Always) {
					var value = qv.util.trim(row[axis.DataField]);
					// Controlo contra la propiedad Filter
					if (axis.Filter.Type == QueryViewerFilterType.HideAllValues) {
						filtered = true;
						break;
					}
					else if (axis.Filter.Type == QueryViewerFilterType.ShowSomeValues) {
						if (axis.Filter.Values.indexOf(value) < 0) {
							filtered = true;
							break;
						}
					}
					if (qViewer.RealType === QueryViewerOutputType.Map && axis.DataType === QueryViewerDataType.Character) {
						// En mapas de Continent y Country filtro los países o estados que quedan fuera del mapa
						if (qViewer.Region === QueryViewerRegion.Country) {
							if (!qv.util.startsWith(value.toUpperCase(), qViewer.Country + "-")) {
								filtered = true;
								break;
							}
						} else if (qViewer.Region === QueryViewerRegion.Continent) {
							switch (qViewer.Continent) {
								case QueryViewerContinent.Africa:
									filtered = !qv.map.IsAfricanCountry(value.toUpperCase());
									break;
								case QueryViewerContinent.Asia:
									filtered = !qv.map.IsAsianCountry(value.toUpperCase());
									break;
								case QueryViewerContinent.Europe:
									filtered = !qv.map.IsEuropeanCountry(value.toUpperCase());
									break;
								case QueryViewerContinent.NorthAmerica:
									filtered = !qv.map.IsNorthAmericanCountry(value.toUpperCase());
									break;
								case QueryViewerContinent.Oceania:
									filtered = !qv.map.IsOceanianCountry(value.toUpperCase());
									break;
								case QueryViewerContinent.SouthAmerica:
									filtered = !qv.map.IsSouthAmericanCountry(value.toUpperCase());
									break;
								default:
									filtered = true;
									break;
							}
						}
					}
				}
			}
			return filtered;
		}

		function XAxisDataTypeOK(qViewer) {
			var dataType = qv.util.XAxisDataType(qViewer);
			var dataTypeOK;
			var errCode;
			switch (qViewer.RealType) {
				case QueryViewerOutputType.Chart:
					dataTypeOK = qv.chart.IsDatetimeXAxis(qViewer) ? dataType == QueryViewerDataType.Date || dataType == QueryViewerDataType.DateTime : true;
					errCode = "GXPL_QViewerNoDatetimeAxis";
					break;
				case QueryViewerOutputType.Map:
					dataTypeOK = (dataType == QueryViewerDataType.GeoPoint || dataType == QueryViewerDataType.Character);
					errCode = "GXPL_QViewerNoMapAxis";
					break;
				default:
					dataTypeOK = true;
					break;
			}
			return { IsOK: dataTypeOK, Error: dataTypeOK ? "" : gx.getMessage(errCode) };
		}

		function NormalizeTargetAndMaximumValues(serie) {
			if (serie.TargetValue <= 0)
				serie.TargetValue = 100;
			if (serie.MaximumValue <= 0)
				serie.MaximumValue = 100;
			if (serie.MaximumValue < serie.TargetValue)
				serie.MaximumValue = serie.TargetValue;
		}

		function aggregatePoints(chartSerie) {
			var currentYValues = [];
			var currentYQuantities = [];
			var firstColor = "";
			for (i = 0; i < chartSerie.Points.length; i++) {
				var yValue;
				var yQuantity;
				if (chartSerie.Aggregation == QueryViewerAggregationType.Count) {
					yValue = 0;		// No se utiliza
					yQuantity = parseFloat(qv.util.trim(chartSerie.Points[i].Value));
				}
				else {
					if (chartSerie.Aggregation == QueryViewerAggregationType.Average) {
						yValue = parseFloat(qv.util.trim(chartSerie.Points[i].Value_N));
						yQuantity = parseFloat(qv.util.trim(chartSerie.Points[i].Value_D));
					}
					else {
						yValue = parseFloat(qv.util.trim(chartSerie.Points[i].Value));
						yQuantity = 1;
					}
				}
				currentYValues.push(yValue);
				currentYQuantities.push(yQuantity);
				if (firstColor == "") firstColor = chartSerie.Points[i].Color;
			}
			var value = qv.util.aggregate(chartSerie.Aggregation, currentYValues, currentYQuantities).toString();
			chartSerie.Points = [{ Value: value, Value_N: value, Value_D: "1", Color: firstColor }];
			chartSerie.NegativeValues = value < 0;
			chartSerie.PositiveValues = value > 0;
		}

		function CalculateColorAxis(qViewer, datum) {

			qViewer.Chart.colorAxis = qViewer.Chart.colorAxis || {};
			qViewer.Chart.colorAxis.dataClasses = qViewer.Chart.colorAxis.dataClasses || [];

			for (var j = 0; j < datum.ConditionalStyles.length; j++) {

				var conditionalStyle = datum.ConditionalStyles[j];
				colorAxis = {};
				colorAxis.dataClasses = [];
				var dataclasses = {};
				dataclasses.color = conditionalStyle.StyleOrClass.replace("color:", "");

				if (conditionalStyle.Operator == QueryViewerConditionOperator.Interval) {

					dataclasses.from = parseFloat(conditionalStyle.Value1);
					dataclasses.to = parseFloat(conditionalStyle.Value2);
					dataclasses.Operator = QueryViewerConditionOperatorSymbol.Interval;

				} else if (conditionalStyle.Operator == QueryViewerConditionOperator.Equal) {

					dataclasses.from = parseFloat(conditionalStyle.Value1);
					dataclasses.Operator = QueryViewerConditionOperatorSymbol.Equal;

				} else if (conditionalStyle.Operator == QueryViewerConditionOperator.GreaterOrEqual) {

					dataclasses.from = parseFloat(conditionalStyle.Value1);
					dataclasses.Operator = QueryViewerConditionOperatorSymbol.GreaterOrEqual;

				} else if (conditionalStyle.Operator == QueryViewerConditionOperator.GreaterThan) {

					dataclasses.from = parseFloat(conditionalStyle.Value1);
					dataclasses.Operator = QueryViewerConditionOperatorSymbol.GreaterThan;

				} else if (conditionalStyle.Operator == QueryViewerConditionOperator.LessThan) {

					dataclasses.to = parseFloat(conditionalStyle.Value1);
					dataclasses.Operator = QueryViewerConditionOperatorSymbol.LessThan;

				} else if (conditionalStyle.Operator == QueryViewerConditionOperator.LessOrEqual) {

					dataclasses.to = parseFloat(conditionalStyle.Value1);
					dataclasses.Operator = QueryViewerConditionOperatorSymbol.LessOrEqual;

				}
				else if (conditionalStyle.Operator == QueryViewerConditionOperator.NotEqual) {

					dataclasses.from = parseFloat(conditionalStyle.Value1);
					dataclasses.Operator = QueryViewerConditionOperatorSymbol.NotEqual;

				}
				qViewer.Chart.colorAxis.dataClasses.push(dataclasses);
			}
		}

		var xAxisDataTypeStatus = XAxisDataTypeOK(qViewer);
		if (xAxisDataTypeStatus.IsOK) {

			qViewer.Chart = {};

			// Obtengo DataFields de categorias y series
			GetCategoriesAndSeriesDataFields(qViewer);

			// Inicializo categorias
			qViewer.Chart.Categories.MinValue = null;
			qViewer.Chart.Categories.MaxValue = null;
			qViewer.Chart.Categories.Values = [];
			var axesByDataField = GetAxesByDataFieldObj(qViewer);

			// Inicializo series
			qViewer.Chart.Series.ByName = {};
			qViewer.Chart.Series.ByIndex = [];
			var uniqueAxis = qViewer.Chart.Categories.DataFields.length == 1 ? qv.util.GetAxisByDataField(qViewer, qViewer.Chart.Categories.DataFields[0]) : null;
			var dataByDataField = GetDataByDataFieldObj(qViewer, uniqueAxis);
			qViewer.Chart.PlotBands = [];
			for (var i = 0; i < qViewer.Chart.Series.DataFields.length; i++) {
				var dataField = qViewer.Chart.Series.DataFields[i];
				var datum = dataByDataField[dataField].Datum;
				var serie = {};
				serie.MinValue = null;		// Minimum value for the serie from the dataset
				serie.MaxValue = null;		// Maximum value for the serie from the dataset
				var multicoloredSerie = dataByDataField[dataField].Multicolored;
				serie.FieldName = datum.Name;					// Nombre del field correspondiente a serie
				serie.Name = datum.Title != "" ? datum.Title : datum.Name;
				serie.Visible = datum.Visible;
				serie.DataType = datum.DataType;
				serie.Aggregation = datum.Aggregation;
				var picture = datum.Picture;
				serie.Picture = (picture == "" ? (serie.DataType == QueryViewerDataType.Integer ? "ZZZZZZZZZZZZZZ9" : "ZZZZZZZZZZZZZZ9.99") : picture);
				serie.NumberFormat = qv.util.ParseNumericPicture(serie.DataType, serie.Picture);
				if (!multicoloredSerie)
					serie.Color = GetColor(multicoloredSerie, datum, uniqueAxis, i, 0, "", 0);
				else
					serie.Color = qv.util.GetNullColor();
				serie.TargetValue = datum.TargetValue;
				serie.MaximumValue = datum.MaximumValue;	// MaximumValue property value (not the maximum value for the serie from the dataset)
				NormalizeTargetAndMaximumValues(serie);
				serie.PositiveValues = false;
				serie.NegativeValues = false;
				serie.Points = [];
				qViewer.Chart.Series.ByName[serie.Name] = serie;
				qViewer.Chart.Series.ByIndex.push(serie);
				// Si el dato tiene estilos condicionales, agrego las PlotBands correspondientes
				if (qViewer.RealType == QueryViewerOutputType.Chart)
					CalculatePlotBands(qViewer, datum);
				// Calculo colores segun los ConditionalStyles para las leyendas del mapa
				if (qViewer.RealType == QueryViewerOutputType.Map)
					CalculateColorAxis(qViewer, datum);
			}

			// Recorro valores y lleno categorías y series
			var valueIndex = 0;
			for (var i = 0; i < qViewer.Data.Rows.length; i++) {
				var row = qViewer.Data.Rows[i];
				if (!IsFilteredRow(qViewer, row)) {
					AddCategoryValue(qViewer, row, valueIndex, axesByDataField);
					AddSeriesValues(qViewer, row, valueIndex, dataByDataField, uniqueAxis);
					valueIndex++;
				}
			}

			if (qViewer.RealType == QueryViewerOutputType.Chart && qv.chart.IsGaugeChart(qViewer))
				for (var i = 0; i < qViewer.Chart.Series.DataFields.length; i++) {
					var serie = qViewer.Chart.Series.ByIndex[i];
					aggregatePoints(serie);		// Sólo puede haber un punto por serie para el Gauge
				}

			return "";

		}
		else
			return xAxisDataTypeStatus.Error;
	},

	getTranslation: function (literal) {
		if (qv.TranslationType == QueryViewerTranslationType.RunTime)
			return gx.getMessage(literal);
		else
			return literal;
	},

	getTranslationAlsoIfStatic: function (literal) {		// Hack to get the correct translation in cases where literals are not translated if static translation is specified
		if (qv.TranslationType == QueryViewerTranslationType.RunTime || qv.TranslationType == QueryViewerTranslationType.Static)		
			return gx.getMessage(literal);
		else
			return literal;
	},

	aggregateData: function (data, rows) {

		function aggregateDatum(datum, rows) {
			var currentYValues = [];
			var currentYQuantities = [];
			var variables = [];
			var firstColor = "";
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (datum.IsFormula) {
					var j = 0;
					do {
						j++;
						var value = row[datum.DataField + "_" + j.toString()];
						if (value != undefined) {
							value = parseFloat(value);
							if (i == 0)
								variables.push(value);
							else
								variables[j-1] += value;
						}
					} while (value != undefined);
				}
				else {
					var yValue;
					var yQuantity;
					if (datum.Aggregation == QueryViewerAggregationType.Count) {
						yValue = 0;		// No se utiliza
						yQuantity = parseFloat(row[datum.DataField]);
					}
					else {
						if (datum.Aggregation == QueryViewerAggregationType.Average) {
							yValue = parseFloat(row[datum.DataField + "_N"]);
							yQuantity = parseFloat(row[datum.DataField + "_D"]);
						}
						else {
							yValue = parseFloat(row[datum.DataField]);
							yQuantity = 1;
						}
					}
					currentYValues.push(yValue);
					currentYQuantities.push(yQuantity);
				}
			}
			if (datum.IsFormula)
				return qv.util.evaluate(datum.Formula, datum.DataField + "_", variables);
			else
				return qv.util.aggregate(datum.Aggregation, currentYValues, currentYQuantities).toString();
		}

		var newRow = {};
		for (var i = 0; i < data.length; i++) {
			var datum = data[i];
			var aggValue = aggregateDatum(datum, rows);
			newRow[datum.DataField] = aggValue;
		}
		return newRow;
	}
}

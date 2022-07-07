var UCDashboardForEditor;
var WidgetType = { Object: "Object", Filter: "Filter", TextBlock: "TextBlock", Button: "Button", Image: "Image", Blank: "Blank" };
var ControlType = { Editbox: "Edit", Checkbox: "Check box", RadioButton: "Radio button", ComboBox: "Combo box", Slider: "Slider", DropDownList: "Drop-down list", Switch: "Switch" };
var DataType = { Integer: "Numeric (without decimals)", Real: "Numeric (with decimals)", Character: "Character", Boolean: "Boolean", Date: "Date", DateTime: "DateTime", GUID: "GUID", GeoPoint: "GeoPoint" };
var FilterType = { Value: "Value", Range: "Range", Collection: "Collection" };
var ParameterType = { Value: "Value", Collection: "Collection" };
var ControlOrientation = { Horizontal: "Horizontal", Vertical: "Vertical" };
var ChartType = { Column: "Column", Column3D: "Column3D", StackedColumn: "StackedColumn", StackedColumn3D: "StackedColumn3D", StackedColumn100: "StackedColumn100", Bar: "Bar", StackedBar: "StackedBar", StackedBar100: "StackedBar100", Area: "Area", StackedArea: "StackedArea", StackedArea100: "StackedArea100", SmoothArea: "SmoothArea", StepArea: "StepArea", Line: "Line", StackedLine: "StackedLine", StackedLine100: "StackedLine100", SmoothLine: "SmoothLine", StepLine: "StepLine", Timeline: "Timeline", SmoothTimeline: "SmoothTimeline", StepTimeline: "StepTimeline", Sparkline: "Sparkline", Pie: "Pie", Pie3D: "Pie3D", Doughnut: "Doughnut", Doughnut3D: "Doughnut3D", LinearGauge: "LinearGauge", CircularGauge: "CircularGauge", Radar: "Radar", FilledRadar: "FilledRadar", PolarArea: "PolarArea", Funnel: "Funnel", Pyramid: "Pyramid", ColumnLine: "ColumnLine", Column3DLine: "Column3DLine" };
var PlotSeries = { InTheSameChart: "InTheSameChart", InSeparateCharts: "InSeparateCharts" };
var XAxisLabels = { Horizontally: "Horizontally", Rotated30: "Rotated 30º", Rotated45: "Rotated 45º", Rotated60: "Rotated 60º", Vertically: "Vertically" };
var ShowDataAs = { Values: "Values", Percentages: "Percentages", ValuesAndPercentages: "ValuesAndPercentages" };
var TrendPeriod = { SinceTheBeginning: "SinceTheBeginning", LastYear: "LastYear", LastSemester: "LastSemester", LastQuarter: "LastQuarter", LastMonth: "LastMonth", LastWeek: "LastWeek", LastDay: "LastDay", LastHour: "LastHour", LastMinute: "LastMinute", LastSecond: "LastSecond" };
var Orientation = { Horizontal: "Horizontal", Vertical: "Vertical" };
var ShowDataLabelsIn = { Columns: "Columns", Rows: "Rows" };
var Total = { Yes: "Yes", No: "No" };
var ShowValuesAs = { NoCalculation: "NoCalculation", RunningTotal: "RunningTotal", RollingAverage: "RollingAverage" };
var RollingAverageType = { Simple: "Simple", Central: "Central", Cumulative: "Cumulative", Weighted: "Weighted", Exponential: "Exponential" };
var DifferenceFrom = { PreviousValue: "PreviousValue", FirstValue: "FirstValue" };
var ShowAsPercentage = { No: "No", Yes: "Yes"};
var MapType = { Choropleth: "Choropleth", Bubble: "Bubble", Point: "Point" };
var Region = { World: "World", Continent: "Continent", Country: "Country" };
var Continent = { NorthAmerica: "NorthAmerica" };	// Only default value enumerated here
var Country = { US: "US" };							// Only default value enumerated here
var ObjectType = { Query: "Query", DataProvider: "DataProvider" };
var OnItemClick = { DoNothing: "Do nothing", RaiseItemClick: "Raise ItemClick event", ApplyFilters: "Apply filters", HighlightValues: "Highlight values" };
var FiltersPosition = { Left: "Left", Right: "Right", Top: "Top" };
var MainTableLayout = { Free: "Free", TwoColumns: "Two columns", ThreeColumns: "Three columns", HeaderTwoColumns: "Header + two columns", HeaderThreeColumns: "Header + three columns", HeaderTwoColumnsFooter: "Header + two columns + footer", HeaderThreeColumnsFooter: "Header + three columns + footer" };
var Methods = { SetFilterValue: "SetFilterValue", SetFilterRange: "SetFilterRange", SetFilterValues: "SetFilterValues" };
var MAIN_TABLE_ID = "1";
var FILTERS_TABLE_ID = "2";
var ChildType = JSON.parse(JSON.stringify(WidgetType));
ChildType.Table = ElementType.Table;

function UpdateDashboardEditorElement(id, type, json, cssRules) {
	var element = UCDashboardForEditor.editor.GetElement(id);
	if (element != null) {
		if (cssRules != "")
			UCDashboardForEditor.InsertCssRules(cssRules);
		var newElement = JSON.parse(json);
		UCDashboardForEditor.UpdateElement(element, newElement, type);
	}
}

function UpdateDashboardEditorParameters(json) {
	var dashboard = UCDashboardForEditor.DashboardObject;
	dashboard.Parameters = JSON.parse(json);
	UCDashboardForEditor.LoadParametersDefaults(dashboard);
	UCDashboardForEditor.UpdateParameterVariables(dashboard);
}

function SelectAllDashboardElements() {
	UCDashboardForEditor.editor.SelectAll();
}

function MouseOverElement(elementId) {
	UCDashboardForEditor.editor.MouseOver(null, elementId);
}

function MouseOutElement(elementId) {
	UCDashboardForEditor.editor.MouseOut(null, elementId);
}

function MouseClickElement(elementId) {
	UCDashboardForEditor.editor.SelectElement(null, elementId, false, true);
}

function DashboardViewer($) {
	this.WidgetsExpanded = true;
	this.LoadingSpec = false;
	this.HeldMethods = [];

	this.SetItemClickData = function (data) {
		this.ItemClickData = data;
	}

	this.GetItemClickData = function () {
		return this.ItemClickData;
	}

	this.SetFiltersChangedData = function (data) {
		this.FiltersChangedData = data;
	}

	this.GetFiltersChangedData = function () {
		return this.FiltersChangedData;
	}

	this.SetValuesHighlightedData = function (data) {
		this.ValuesHighlightedData = data;
	}

	this.GetValuesHighlightedData = function () {
		return this.ValuesHighlightedData;
	}

	this.show = function () {

		qv.services.GetDashboardSpecIfNeeded(this, function (resText, dViewer) {				// Servicio GetDashboardSpec

			dViewer.UpdateDashboardObject(resText);
			var dashboard = dViewer.DashboardObject;
			if (dashboard != null) {
				qv.TranslationType = dViewer.TranslationType;		// all DashboardViewers share the same value because it's an Environment property
				var self = dViewer;
				var cfg = {
					dashboard: dashboard,
					widgetsExpanded: function () {
						return self.WidgetsExpanded
					},

					callbacks: {
						selectElements: function (elementIdsCSV) {
							window.external.SelectElements(elementIdsCSV);
						},
						selectObjectItem: function (elementId, itemName) {
							window.external.SelectObjectItem(elementId, itemName);
						},
						addElement: function (elementType, dropZone, dropTargetId) {
							window.external.AddElement(elementType, dropZone, dropTargetId);
						},
						moveElement: function (dragSourceId, dropZone, dropTargetId) {
							window.external.MoveElement(dragSourceId, dropZone, dropTargetId);
						},
						moveElements: function (dragSourcesId, dropZone, dropTargetId) {
							window.external.MoveElements(dragSourcesId, dropZone, dropTargetId);
						},
						addKBObjects: function (objectNames, dropZone, dropTargetId) {
							window.external.AddKBObjects(objectNames, dropZone, dropTargetId);
						}
					},

					layoutDefaults: function (layout) {
						self.LayoutDefaults(layout);
					},
					tableDefaults: function (table) {
						self.TableDefaults(table);
					},
					rowDefaults: function (row) {
						self.RowDefaults(row);
					},
					cellDefaults: function (cell) {
						self.CellDefaults(cell);
					},
					widgetDefaults: function (widget, dashboard, widgetType) {
						self.WidgetDefaults(widget, dashboard, widgetType);
					},
					getWidgetContentHTML: function (dashboard, widget) {
						return self.GetWidgetContentHTML(dashboard, widget);
					},
					addExtraRow: function (layout, table) {
						return (table.Rows.length == 0 || table.ElementId == FILTERS_TABLE_ID || (table.ElementId == MAIN_TABLE_ID && layout.MainTableLayout == MainTableLayout.Free) || self.IsMainTableLayoutTable(table))
					},
					widgetOverflowStr: function (widget) {
						return "";
					},
					cellHasVisibleContent: function (isEditor, cell) {
						return cell.ChildType == ChildType.Filter ? (cell.Filter.Visible || isEditor) : true;
					},
					isMainTableLayoutTable: function (table) {
						return self.IsMainTableLayoutTable(table);
					},
					isHeight100Percent: function (mainTableLayout, rowIndex, table) {
						return self.IsHeight100Percent(mainTableLayout, rowIndex, table);
					},
					getElementIcon: function (widget) {
						switch (widget.WidgetType) {
							case WidgetType.Object:
								return "Object.ico";
							case WidgetType.Filter:
								return "Filter.ico";
							case WidgetType.TextBlock:
								return "TextBlock.ico";
							case WidgetType.Image:
								return "Image.ico";
							case WidgetType.Button:
								return "Button.ico";
							default:
								return "";
						}
					},
					getUpperMostAncestorElementId: function (element) {
						var currentElement = element;
						do {
							var id = currentElement.ElementId;
							currentElement = currentElement.ParentElement;
						}
						while (id != MAIN_TABLE_ID && id != FILTERS_TABLE_ID);
						return id;
					},
					onAddElement: function (dashboard, element) {
						self.OnAddElement(dashboard, element);
					},
					onRemoveElement: function (dashboard, element) {
						self.OnRemoveElement(dashboard, element);
					},
					isEditor: function () {
						return self.IsDashboardEdit();
					},
					dependsOnElement: function (selectedElement, relatedElement) {
						return self.DependsOnElement(selectedElement, relatedElement);
					},
					impactsElement: function (selectedElement, relatedElement) {
						return self.ImpactsElement(selectedElement, relatedElement);
					},
					getContainerControlId: function () {
						return self.getContainerControl().id;
					},
					getMe: function () {
						return self.me() + ".editor";
					},
					getMessage: function (s) {
						return gx.getMessage(s);
					},
					get: function (selector) {
						return $(selector);
					},
					startsWith: function (string, substring) {
						return qv.util.startsWith(string, substring);
					},
					endsWith: function (string, substring) {
						return qv.util.endsWith(string, substring);
					},
					replaceVariablesValuesInText: function (text, variables) {
						return self.ReplaceVariablesValuesInText(text, variables);
					},
					getHamburgerMenuHTML: function (widget) {
						return self.GetObjectMenuHTML(widget);
					},
					getElementReferences: function (element) {
						return self.GetElementReferences(element);
					}
				};

				dViewer.LoadParameterVariables(dashboard);
				dViewer.editor = new BrowserLayout(cfg);
				dViewer.LoadFilterVariables(dashboard);

				dViewer.SetContainerSize();
				dViewer.RenderDashboard();
				var refreshPeriod = dViewer.ReplaceVariableValue(dashboard.Layout.RefreshPeriod, [DataType.Integer, DataType.Real], false, dashboard.Variables.Filters);
				dViewer.SetRefreshPeriod(dViewer, refreshPeriod);
			}
			else
				dViewer.RenderError(gx.getMessage("GX_Dashboard_NotAssigned"));
			dViewer.LoadingSpec = false;
			dViewer.ReleaseMethods();
		});

	}

	// ------------------------------------------------------------- Methods -------------------------------------------------------------

	this.HoldMethod = function(method, parameters) {
		this.HeldMethods.push({Method: method, Parameters: parameters});		// Hold method execution until dashboard spec is loaded
	}
	
	this.ReleaseMethods = function() {
		for (var i = 0; i < this.HeldMethods.length; i++) {		// Execute methods that were fired before dashboard spec was loaded
			var method = this.HeldMethods[i].Method;
			var parameters = this.HeldMethods[i].Parameters;
			switch (method)
			{
				case Methods.SetFilterValue:
					this.SetFilterValue(parameters[0], parameters[1]);
					break;
				case Methods.SetFilterRange:
					this.SetFilterRange(parameters[0], parameters[1], parameters[2], parameters[3]);
					break;
				case Methods.SetFilterValues:
					this.SetFilterValues(parameters[0], JSON.parse(parameters[1]));
					break;
			}
		}
		this.HeldMethods.splice(0, this.HeldMethods.length);
	}
	
	this.Refresh = function() {
		this.RefreshDashboard();
	}

	this.GetFilterValue = function(name) {
		var filter = this.GetFilterByAnyName(name);
		if (filter != null && filter.Type != FilterType.Collection) {
			if (filter.Type == FilterType.Value)
				return filter.SerializedValue;
			else {		// Range
				if (filter.NameLowerValue == name)
					return filter.SerializedLowerValue;
				else
					return filter.SerializedUpperValue;
			}
		}
		else
			return "";
	}

	this.GetFilterValues = function(name) {
		var filter = this.GetFilterByName(name);
		if (filter != null && filter.Type == FilterType.Collection) {
			var values = [];
			for (var j = 0; j < filter.Values.length; j++)
				values.push(this.ValueToString(filter.Values[j], filter.DataType, filter.Picture, filter.PictureProperties));
		}
		return values;
	}

	this.SetFilterValue = function(name, value) {
		if (!this.LoadingSpec) {
			var filter = this.GetFilterByName(name);
			if (filter != null && filter.Type == FilterType.Value && !filter.IsHidden) {
				var filterChanged = this.UpdateFilter1(null, name, value);
				if (filterChanged)
					this.RefreshWidget(filter);
			}
			else {
				var result = this.GetObjectElementForFilter(name);
				if (result != null) {
					var picture = this.DefaultPicture(result.Element.DataType);
					var pictureProperties = this.GetPictureProperties(result.Element.DataType, picture);
					var checkValueResult = this.CheckFilterValue(value, result.Element.DataType, FilterType.Value, picture, pictureProperties, null, null, false);
					if (checkValueResult.Success) {
						var filterChanged = this.AddHiddenFilter(result.Object, name, checkValueResult.ParsedValue, result.Element.DataType);
						if (filterChanged) {
							this.RefreshDashboard([name]);
							result.Object.QueryViewer.Select([{ Name: name, Value: value }]);
						}
					}
				}
			}
		}
		else
			this.HoldMethod(Methods.SetFilterValue, [name, value]);
	}

	this.SetFilterRange = function(lowerName, lowerValue, upperName, upperValue) {
		if (!this.LoadingSpec) {
			var filter
			if (lowerName == "")
				filter = this.GetFilterByAnyName(upperName);
			else
				filter = this.GetFilterByAnyName(lowerName);
			if (filter != null && filter.Type == FilterType.Range) {
				var filterChanged;
				if (lowerName == "")
					filterChanged = this.UpdateFilter2(null, filter.NameLowerValue, upperName, filter.SerializedLowerValue, upperValue);
				else if (upperName == "")
					filterChanged = this.UpdateFilter2(null, lowerName, filter.NameUpperValue, lowerValue, filter.SerializedUpperValue);
				else
					filterChanged = this.UpdateFilter2(null, lowerName, upperName, lowerValue, upperValue);
				if (filterChanged)
					this.RefreshWidget(filter);
			}
		}
		else
			this.HoldMethod(Methods.SetFilterRange, [lowerName, lowerValue, upperName, upperValue]);
	}

	this.SetFilterValues = function(name, values) {
		if (!this.LoadingSpec) {
			var filter = this.GetFilterByName(name);
			if (filter != null && filter.Type == FilterType.Collection) {
				var filterChanged = this.UpdateFilter1(null, name, JSON.stringify(values));
				if (filterChanged)
					this.RefreshWidget(filter);
			}
		}
		else
			this.HoldMethod(Methods.SetFilterValues, [name, JSON.stringify(values)]);
	}

	// ------------------------------------------------------------- General -------------------------------------------------------------

	this.GetObjectElementForFilter = function(name) {
		var dashboard = this.DashboardObject;
		for (var i = 0; i < dashboard.Objects.length; i++) {
			var object = dashboard.Objects[i];
			if (object.OnItemClick == OnItemClick.ApplyFilters) {
				for (var j = 0; j < object.Elements.length; j++) {
					var objectElement = object.Elements[j];
					if (objectElement.Name.toLowerCase() == name.toLowerCase())
						return { Object: object, Element: objectElement };
				}
			}
		}
		return null;
	}

	this.SetRefreshPeriod = function (uc, refreshPeriod) {
		if (this._refreshInterval)
			clearInterval(this._refreshInterval);
		if (!isNaN(refreshPeriod) && refreshPeriod != 0)
			this._refreshInterval = setInterval(uc.me() + '.RefreshDashboard();', refreshPeriod * 1000)
	}

	this.RenderError = function (errMsg) {
		var html = '<div style="height: ' + this.Height + '; width: ' + this.Width + '; border: silver thin solid">' + this.CenteredTextHTML("", "", gx.getMessage("GX_Dashboard_Error") + ": " + errMsg) + '</div>';
		this.setHtml(html);
	}

	this.CenteredTextHTML = function (id, className, text) {
		var idStr = id == '' ? '' : ' id="' + id + '"';
		var classStr = className == '' ? '' : ' class="' + className + '"';
		return '<div' + classStr + ' style="display:table;width:100%;height:100%"><div style="display:table-cell;vertical-align:middle;text-align:center"><span' + idStr + '>' + text + '</span></div></div>';
	}

	this.LoadParametersDefaults = function (dashboard) {
		this._ValidParametersCalculated = false;
		for (var i = 0; i < dashboard.Parameters.length; i++)
			this.ParameterDefaults(dashboard.Parameters[i]);
	}

	this.UpdateDashboardObject = function (actualSpec) {

		function UpdateParametersValues(uc, dashboard) {
			if (uc.Object) {
				var array = eval(uc.Object);
				for (var i = 0; i < dashboard.Parameters.length; i++) {
					var parameter = dashboard.Parameters[i];
					if (i + 2 <= array.length - 1) {
						var parserResult = uc.ParseValue(array[i + 2], parameter.DataType, parameter.Type == ParameterType.Collection);
						if (parserResult.Success) {
							parameter.Value = parserResult.ParsedValue;
							uc.SerializeParameterValue(parameter);
						}
					}
				}
			}
		}

		if (actualSpec != this.DashboardSpec)
			this.DashboardSpec = actualSpec;
		if (this.DashboardSpec != "") {
			this.DashboardObject = JSON.parse(this.DashboardSpec);
			this.LoadParametersDefaults(this.DashboardObject);
			UpdateParametersValues(this, this.DashboardObject);
			this.DashboardDefaults(this.DashboardObject);
			if (this.IsDashboardEdit())
				UCDashboardForEditor = this;
		}
		else
			this.DashboardObject = null;
	}

	this.GetDashboardSpec = qv.services.createAsyncServerCallFn(this, qv.services.url.getDashboardSpecURL, qv.services.postInfo.DashboardSpec);

	this.IsRelated = function (element, filterOrObject) {
		if (filterOrObject.WidgetType == WidgetType.Filter)
			return this.IsFilterRelated(element, filterOrObject);
		else if (filterOrObject.WidgetType == WidgetType.Object)
			return this.IsObjectRelated(element, filterOrObject);
		else
			return false;
	}

	this.IsFilterRelated = function (element, filter) {
		if (!filter.IsHidden) {
			if (filter.Type == FilterType.Range)
				return this.CaseInsensitiveIndexOf(element.Parameters, filter.NameLowerValue) >= 0 || this.CaseInsensitiveIndexOf(element.Parameters, filter.NameUpperValue) >= 0;
			else
				return this.CaseInsensitiveIndexOf(element.Parameters, filter.Name) >= 0;
		}
		else
			return false;
	}

	this.IsObjectRelated = function (element, object) {
		if (object.OnItemClick == OnItemClick.ApplyFilters) {
			for (var i = 0; i < object.Elements.length; i++) {
				var objectElement = object.Elements[i];
				if (this.CaseInsensitiveIndexOf(element.Parameters, objectElement.Name) >= 0)
					return true;
			}
			return false;
		}
		else
			return false;
	}

	this.DependsOnElement = function (selectedElement, relatedElement) {
		if (selectedElement.ElementType != ElementType.Widget && selectedElement.ElementType != ElementType.Layout)
			return false;
		else if (relatedElement.WidgetType == WidgetType.Filter || relatedElement.WidgetType == WidgetType.Object)
			return this.IsRelated(selectedElement, relatedElement);
		else
			return false;
	}

	this.ImpactsElement = function (selectedElement, relatedElement) {
		if (selectedElement.ElementType != ElementType.Widget && selectedElement.ElementType != ElementType.Layout)
			return false;
		else if (selectedElement.WidgetType == WidgetType.Filter || selectedElement.WidgetType == WidgetType.Object)
			return this.IsRelated(relatedElement, selectedElement);
		else
			return false;
	}

	this.SetContainerSize = function () {
		var container = document.getElementById(this.ContainerName);
		if (this.Width != "")
			container.style.width = gx.dom.addUnits(this.Width);
		if (this.Height != "")
			container.style.height = gx.dom.addUnits(this.Height);
	}

	var EditorDefaults = { PageSize: 20, EmptyItemText: "GX_EmptyItemText" };

	this.IsDashboardEdit = function () {
		if (!this._IsDashboardEdit) {
			this._IsDashboardEdit = window.external != undefined &&
				window.external.SelectElements != undefined &&
				window.external.DeleteSelectedElements != undefined &&
				window.external.SelectObjectItem != undefined &&
				window.external.AddElement != undefined &&
				window.external.MoveElement != undefined &&
				window.external.MoveElements != undefined &&
				window.external.AddKBObjects != undefined &&
				window.external.SetFilterValue != undefined &&
				window.external.SetFilterValues != undefined &&
				window.external.SetFrameCollapsed != undefined &&
				window.external.SendText != undefined;
		}
		return this._IsDashboardEdit;
	}

	this.ProcessResponsiveSizes = function (table) {

		function ProcessWidth(table, rowIndex, scale) {
			var row = table.Rows[rowIndex];
			var totWidth = 0
			var totCellsWithWidth = 0;
			for (var i = 0; i < row.Cells.length; i++) {
				var cell = row.Cells[i];
				if (cell.ResponsiveSizes.Width[scale]) {
					totWidth += cell.ResponsiveSizes.Width[scale];
					totCellsWithWidth += 1;
				}
			}
			var widthLeft = 12 - totWidth;
			var cellsLeft = row.Cells.length - totCellsWithWidth;
			for (var i = 0; i < row.Cells.length; i++) {
				var cell = row.Cells[i];
				if (!cell.ResponsiveSizes.Width[scale]) {
					var width = Math.ceil(widthLeft / cellsLeft);
					cell.ResponsiveSizes.Width[scale] = width;
					widthLeft -= width;
					cellsLeft -= 1;
				}
			}
		}

		function ProcessMove(table, rowIndex, cellIndex, scale) {

			function SumWidths(arrInfo, cellIndex) {
				var totalWidth = 0;
				for (var i = 0; i < arrInfo.length; i++) {
					if (arrInfo[i].OldPos == cellIndex)
						break;
					if (arrInfo[i].Visible)
						totalWidth += arrInfo[i].Width;
				}
				return totalWidth;
			}

			function compareNewPos(a, b) {

				function compareOldPos(a, b) {
					if (a.OldPos < b.OldPos)
						return -1;
					if (a.OldPos > b.OldPos)
						return 1;
					return 0;
				}

				if (a.NewPos < b.NewPos)
					return -1;
				if (a.NewPos > b.NewPos)
					return 1;
				return compareOldPos(a, b);
			}

			var arrInfo = []
			var anyMove = false;
			var row = table.Rows[rowIndex];
			for (var i = 0; i < row.Cells.length; i++) {
				var cell = row.Cells[i];
				var width = cell.ResponsiveSizes.Width[scale] || parseInt(12 / row.Cells.length);
				var offset = cell.ResponsiveSizes.Offset[scale] || 0;
				var move = cell.ResponsiveSizes.Move[scale] || 0;
				var visible = cell.ResponsiveSizes.Visible[scale] != undefined ? cell.ResponsiveSizes.Visible[scale] : true;
				arrInfo.push({ OldPos: i, NewPos: (move != 0 ? move : row.Cells.length), Width: width + offset, Visible: visible });
				if (move != 0)
					anyMove = true;
			}
			if (anyMove) {
				var widthBefore = SumWidths(arrInfo, cellIndex);
				arrInfo.sort(compareNewPos);
				var widthAfter = SumWidths(arrInfo, cellIndex);
				var move = widthAfter - widthBefore;
				if (move > 0)
					row.Cells[cellIndex].ResponsiveSizes.Push[scale] = move;
				else if (move < 0)
					row.Cells[cellIndex].ResponsiveSizes.Pull[scale] = -move;
				else {
					var previousScale = Object.values(ResponsiveScale)[Object.values(ResponsiveScale).indexOf(scale) - 1];
					if (previousScale != undefined) {
						var previousPush = row.Cells[cellIndex].ResponsiveSizes.Push[previousScale];
						var previousPull = row.Cells[cellIndex].ResponsiveSizes.Pull[previousScale];
						if (previousPush > 0)
							row.Cells[cellIndex].ResponsiveSizes.Push[scale] = 0;
						if (previousPull > 0)
							row.Cells[cellIndex].ResponsiveSizes.Pull[scale] = 0;
					}
				}
			}
		}

		for (var rowIndex = 0; rowIndex < table.Rows.length; rowIndex++) {
			row = table.Rows[rowIndex];
			var anyWidth = {};
			for (var cellIndex = 0; cellIndex < row.Cells.length; cellIndex++) {
				var cell = row.Cells[cellIndex];
				var rsObj = { Width: {}, Offset: {}, Visible: {}, Move: {}, Push: {}, Pull: {} };
				for (var scaleIndex = 0; scaleIndex < table.ResponsiveSizes.length; scaleIndex++) {
					var scaleRS = table.ResponsiveSizes[scaleIndex];
					if (rowIndex < scaleRS.rows.length) {
						var rowRS = scaleRS.rows[rowIndex];
						if (cellIndex < rowRS.length) {
							var cellRS = rowRS[cellIndex];
							if (cellRS.width != undefined) {
								rsObj.Width[scaleRS.scale] = Math.round(cellRS.width * 12 / 100);
								anyWidth[scaleRS.scale] = true;
							}
							if (cellRS.offset != undefined)
								rsObj.Offset[scaleRS.scale] = Math.round(cellRS.offset * 12 / 100);
							if (cellRS.visible != undefined)
								rsObj.Visible[scaleRS.scale] = cellRS.visible;
							if (cellRS.move != undefined)
								rsObj.Move[scaleRS.scale] = cellRS.move;
						}
					}
				}
				cell.ResponsiveSizes = rsObj;
			}
			// Proceso los Width para toda la fila (reparto el espacio que sobra entre los no seteados)
			for (var scaleIndex = 0; scaleIndex < table.ResponsiveSizes.length; scaleIndex++) {
				var scale = table.ResponsiveSizes[scaleIndex].scale;
				if (anyWidth[scale])
					ProcessWidth(table, rowIndex, scale);
			}

			// Proceso el Move para toda la fila
			for (var cellIndex = 0; cellIndex < row.Cells.length; cellIndex++)
				for (var scaleIndex = 0; scaleIndex < table.ResponsiveSizes.length; scaleIndex++)
					ProcessMove(table, rowIndex, cellIndex, table.ResponsiveSizes[scaleIndex].scale);
		}
	}

	this.DashboardDefaults = function (dashboard) {
		dashboard.Name = dashboard.Name || "";
		dashboard.Description = dashboard.Description || "";
		dashboard.Objects = [];
		dashboard.Filters = [];
		dashboard.Buttons = [];
		dashboard.Images = [];
		dashboard.TextBlocks = [];
	}

	this.LayoutDefaults = function (layout) {
		layout.Title = layout.Title != undefined ? qv.util.getTranslation(layout.Title) : "";
		layout.RefreshPeriod = layout.RefreshPeriod || 0;
		layout.MainTableLayout = layout.MainTableLayout || MainTableLayout.Free;
		layout.FiltersPosition = layout.FiltersPosition || FiltersPosition.Right;
		layout.ElementId = layout.ElementId || "";
		layout.Parameters = layout.Parameters || [];
	}

	this.ParameterDefaults = function (parameter) {
		parameter.Name = parameter.Name || "";
		parameter.DataType = parameter.DataType || DataType.Integer;
		parameter.Type = parameter.Type || ParameterType.Value;
		parameter.Picture = parameter.Picture || this.DefaultPicture(parameter.DataType);
		var pictureProperties = this.GetPictureProperties(parameter.DataType, parameter.Picture);
		if (pictureProperties != null)
			parameter.PictureProperties = pictureProperties;
		if (!parameter.Value) {
			parameter.Value = this.GetEmptyValue(parameter.DataType, parameter.Type == ParameterType.Collection);
			this.SerializeParameterValue(parameter);
		}
	}

	this.TableDefaults = function (table) {
		table.ControlName = table.ControlName || "";
		table.ElementId = table.ElementId || "";
		table.Class = table.Class || "";
		table.Rows = table.Rows || [];
		table.Mode = TableMode.Bootstrap;
		table.ResponsiveSizes = table.ResponsiveSizes || [];
		this.ProcessResponsiveSizes(table);
	}

	this.RowDefaults = function (row) {
		row.ElementId = row.ElementId || "";
		row.RowClass = row.RowClass || "";
		row.Cells = row.Cells || [];
	}

	this.CellDefaults = function (cell) {
		cell.ElementId = cell.ElementId || "";
		cell.CellClass = cell.CellClass || "";
		cell.ChildType = cell.ChildType || "";
		cell.IsEmpty = cell.IsEmpty || cell.ChildType == "";
		cell.HasTable = cell.ChildType == ChildType.Table;
	}

	this.BasicWidgetDefaults = function (widget, dashboard, widgetType) {
		widget.ElementId = widget.ElementId || "";
		widget.ControlName = widget.ControlName || "";
		widget.WidgetType = widgetType;
		widget.FrameVisible = widget.FrameVisible || false;
		widget.FrameTitle = widget.FrameTitle != undefined ? qv.util.getTranslation(widget.FrameTitle) : "";
		widget.FrameAllowCollapsing = widget.FrameAllowCollapsing || false;
		widget.FrameCollapsed = widget.FrameCollapsed || false;
		widget.Parameters = widget.Parameters || [];
	}

	this.ObjectElementDefaults = function (element, dashboard) {
		element.Name = element.Name || "";
		element.DataType = element.DataType || DataType.Integer;
		element.Title = element.Title || "";
		element.DataField = element.DataField || "";
		element.Visible = element.Visible || "";
		element.Type = element.Type || "";
		element.Axis = element.Axis || "";
		element.Aggregation = element.Aggregation || "";
		element.Format = element.Format || {};
		element.Format.Picture = element.Format.Picture || "";
		element.Format.Subtotals = element.Format.Subtotals || "";
		element.Format.CanDragToPages = element.Format.CanDragToPages != undefined ? element.Format.CanDragToPages : true;		// se trata diferente por ser booleana con default = true
		element.Format.Style = element.Format.Style || "";
		element.Format.TargetValue = element.Format.TargetValue || 0;
		element.Format.MaximumValue = element.Format.MaximumValue || 0;
		element.Format.ValueStyles = element.Format.ValueStyles || [];
		element.Format.ConditionalStyles = element.Format.ConditionalStyles || [];
		element.Filter = element.Filter || {};
		element.Filter.Type = element.Filter.Type || "";
		element.Filter.Values = element.Filter.Values || [];
		element.ExpandCollapse = element.ExpandCollapse || {};
		element.ExpandCollapse.Type = element.ExpandCollapse.Type || "";
		element.ExpandCollapse.Values = element.ExpandCollapse.Values || [];
		element.AxisOrder = element.AxisOrder || {};
		element.AxisOrder.Type = element.AxisOrder.Type || "";
		element.AxisOrder.Values = element.AxisOrder.Values || [];
		element.Grouping = element.Grouping || {};
		element.Grouping.GroupByYear = element.Grouping.GroupByYear || false;
		element.Grouping.YearTitle = element.Grouping.YearTitle || "";
		element.Grouping.GroupBySemester = element.Grouping.GroupBySemester || false;
		element.Grouping.SemesterTitle = element.Grouping.SemesterTitle || "";
		element.Grouping.GroupByQuarter = element.Grouping.GroupByQuarter || false;
		element.Grouping.QuarterTitle = element.Grouping.QuarterTitle || "";
		element.Grouping.GroupByMonth = element.Grouping.GroupByMonth || false;
		element.Grouping.MonthTitle = element.Grouping.MonthTitle || "";
		element.Grouping.GroupByDayOfWeek = element.Grouping.GroupByDayOfWeek || false;
		element.Grouping.DayOfWeekTitle = element.Grouping.DayOfWeekTitle || "";
		element.Grouping.HideValue = element.Grouping.HideValue || false;
		element.RaiseItemClick = element.RaiseItemClick != undefined ? element.RaiseItemClick : true;		// se trata diferente por ser booleana con default = true
		element.Analytics = element.Analytics || {};
		element.Analytics.ShowValuesAs = element.Analytics.ShowValuesAs || ShowValuesAs.NoCalculation;
		element.Analytics.RollingAverageType = element.Analytics.RollingAverageType || RollingAverageType.Simple;
		element.Analytics.RollingAverageTerms = element.Analytics.RollingAverageTerms || 10;
		element.Analytics.DifferenceFrom = element.Analytics.DifferenceFrom || DifferenceFrom.PreviousValue;
		element.Analytics.ShowAsPercentage = element.Analytics.ShowAsPercentage || ShowAsPercentage.No;
	}

	this.ObjectElementsDefaults = function (elements, dashboard) {
		for (var i = 0; i < elements.length; i++)
			this.ObjectElementDefaults(elements[i], dashboard);
	}

	this.ObjectDefaults = function (object, dashboard) {
		this.BasicWidgetDefaults(object, dashboard, WidgetType.Object);
		object.Name = object.Name || "";
		object.Type = object.Type || ObjectType.Query;
		object.Elements = object.Elements || [];
		this.ObjectElementsDefaults(object.Elements, dashboard);
		object.Class = object.Class || "";
		object.ContainerClass = object.ContainerClass || "";
		object.FrameClass = object.FrameClass || "";
		object.FrameTitleClass = object.FrameTitleClass || "";
		object.FrameBodyClass = object.FrameBodyClass || "";
		object.Output = object.Output || {};
		object.Output.Title = object.Output.Title || "";
		object.Output.Type = object.Output.Type || OutputType.Chart;
		object.Output.ChartType = object.Output.ChartType || ChartType.Column;
		object.Output.PlotSeries = object.Output.PlotSeries || PlotSeries.InTheSameChart;
		object.Output.XAxisLabels = object.Output.XAxisLabels || XAxisLabels.Horizontally;
		object.Output.XAxisIntersectionAtZero = object.Output.XAxisIntersectionAtZero || false;
		object.Output.ShowValues = object.Output.ShowValues != undefined ? object.Output.ShowValues : true;		// se trata diferente por ser booleana con default = true
		object.Output.XAxisTitle = object.Output.XAxisTitle || "";
		object.Output.YAxisTitle = object.Output.YAxisTitle || "";
		object.Output.ShowDataAs = object.Output.ShowDataAs || ShowDataAs.Values;
		object.Output.Orientation = object.Output.Orientation || Orientation.Horizontal;
		object.Output.IncludeTrend = object.Output.IncludeTrend || false;
		object.Output.TrendPeriod = object.Output.TrendPeriod || TrendPeriod.SinceTheBeginning;
		object.Output.IncludeSparkline = object.Output.IncludeSparkline || false;
		object.Output.IncludeMaxAndMin = object.Output.IncludeMaxAndMin || false;
		object.Output.Paging = object.Output.Paging != undefined ? object.Output.Paging : true;		// se trata diferente por ser booleana con default = true
		object.Output.PageSize = object.Output.PageSize || EditorDefaults.PageSize;
		object.Output.ShowDataLabelsIn = object.Output.ShowDataLabelsIn || ShowDataLabelsIn.Columns;
		object.Output.TotalForRows = object.Output.TotalForRows || Total.Yes;
		object.Output.TotalForColumns = object.Output.TotalForColumns || Total.Yes;
		object.Output.MapType = object.Output.MapType || MapType.Choropleth;
		object.Output.Region = object.Output.Region || Region.World;
		object.Output.Continent = object.Output.Continent || Continent.NorthAmerica;
		object.Output.Country = object.Output.Country || Country.US;
		object.OnItemClick = object.OnItemClick || OnItemClick.DoNothing;
	}

	this.FilterDefaults = function (filter, dashboard) {
		this.BasicWidgetDefaults(filter, dashboard, WidgetType.Filter);
		filter.Type = filter.Type || FilterType.Value;
		filter.Name = filter.Name || "";
		filter.NameLowerValue = filter.NameLowerValue || "";
		filter.NameUpperValue = filter.NameUpperValue || "";
		filter.Visible = filter.Visible != undefined ? filter.Visible : true;		// se trata diferente por ser booleana con default = true
		filter.CaptionClass = filter.CaptionClass || "";
		filter.ValueClass = filter.ValueClass || "";
		filter.ContainerClass = filter.ContainerClass || "";
		filter.FrameClass = filter.FrameClass || "";
		filter.FrameTitleClass = filter.FrameTitleClass || "";
		filter.FrameBodyClass = filter.FrameBodyClass || "";
		filter.Caption = filter.Caption != undefined ? qv.util.getTranslation(filter.Caption) : filter.Name;
		filter.Value = filter.Value || "";
		filter.LowerValue = filter.LowerValue || "";
		filter.UpperValue = filter.UpperValue || "";
		filter.Values = filter.Values || [];
		filter.MinValue = filter.MinValue || "";
		filter.MaxValue = filter.MaxValue || "";
		filter.DataType = filter.DataType || DataType.Integer;
		filter.Picture = filter.Picture || "";
		filter.Control = filter.Control || {};
		filter.Control.Type = filter.Control.Type || ControlType.Editbox;
		filter.Control.Dynamic = filter.Control.Dynamic || false;
		filter.Control.DscAttDataType = filter.Control.DscAttDataType || DataType.Character;
		if (filter.Control.Dynamic)
			filter.Control.RefreshValues = filter.Control.RefreshValues || false;
		else {
			filter.Control.Values = filter.Control.Values || [];
			filter.Control.Descriptions = filter.Control.Descriptions || [];
		}
		filter.Control.EmptyItem = filter.Control.EmptyItem || false;
		filter.Control.EmptyItemText = filter.Control.EmptyItemText != undefined ? qv.util.getTranslation(filter.Control.EmptyItemText) : gx.getMessage(EditorDefaults.EmptyItemText);
		filter.Control.Orientation = filter.Control.Orientation || ControlOrientation.Horizontal;
		filter.Control.InviteMessage = filter.Control.InviteMessage != undefined ? qv.util.getTranslation(filter.Control.InviteMessage) : "";	//TODO: hay que ocultar el InviteMessage en el caso de Date y DateTime
		filter.IsHidden = false;
	}

	this.ButtonDefaults = function (button, dashboard) {
		this.BasicWidgetDefaults(button, dashboard, WidgetType.Button);
		button.Caption = button.Caption != undefined ? qv.util.getTranslation(button.Caption) : "";
		button.Class = button.Class || "";
		button.ContainerClass = button.ContainerClass || "";
		button.FrameClass = button.FrameClass || "";
		button.FrameTitleClass = button.FrameTitleClass || "";
		button.FrameBodyClass = button.FrameBodyClass || "";
	}

	this.ImageDefaults = function (image, dashboard) {
		this.BasicWidgetDefaults(image, dashboard, WidgetType.Image);
		image.LanguageDependant = image.LanguageDependant || false;
		image.ThemeDependant = image.ThemeDependant || false;
		image.Sources = JSON.parse(image.Sources || "{}");
		image.AlternateText = image.AlternateText != undefined ? qv.util.getTranslation(image.AlternateText) : "";
		image.Class = image.Class || "";
		image.ContainerClass = image.ContainerClass || "";
		image.FrameClass = image.FrameClass || "";
		image.FrameTitleClass = image.FrameTitleClass || "";
		image.FrameBodyClass = image.FrameBodyClass || "";
	}

	this.TextBlockDefaults = function (textBlock, dashboard) {
		this.BasicWidgetDefaults(textBlock, dashboard, WidgetType.TextBlock);
		textBlock.Caption = textBlock.Caption != undefined ? qv.util.getTranslation(textBlock.Caption) : "";
		textBlock.Class = textBlock.Class || "";
		textBlock.ContainerClass = textBlock.ContainerClass || "";
		textBlock.FrameClass = textBlock.FrameClass || "";
		textBlock.FrameTitleClass = textBlock.FrameTitleClass || "";
		textBlock.FrameBodyClass = textBlock.FrameBodyClass || "";
	}

	this.WidgetDefaults = function (widget, dashboard, widgetType) {
		switch (widgetType) {
			case WidgetType.Object:
				this.ObjectDefaults(widget, dashboard);
				break;
			case WidgetType.Filter:
				this.FilterDefaults(widget, dashboard);
				this.ProcessFilter(widget);
				break;
			case WidgetType.Image:
				this.ImageDefaults(widget, dashboard);
				break;
			case WidgetType.Button:
				this.ButtonDefaults(widget, dashboard);
				break;
			case WidgetType.TextBlock:
				this.TextBlockDefaults(widget, dashboard);
				break;
		}
	}

	this.ReplaceVariables = function (element, replaceFunction) {
		if (!element.VariablesReplaced) {
			replaceFunction(this, element);
			element.VariablesReplaced = true;
		}
	}

	this.ReplaceDashboardParametersInLayout = function (layout) {
		this.ReplaceVariables(layout, function (uc, layout) {
			var dashboard = uc.DashboardObject;
			var variables = dashboard.Variables.ValidParameters.concat(dashboard.Variables.Standard);
			layout.Title = uc.ReplaceVariablesValuesInText(layout.Title, variables);
			layout.RefreshPeriod = uc.ReplaceVariableValue(layout.RefreshPeriod, [DataType.Integer], false, variables);
		});
	}

	this.ReplaceDashboardParametersInWidget = function (widget) {
		this.ReplaceVariables(widget, function (uc, widget) {
			var dashboard = uc.DashboardObject;
			var variables = dashboard.Variables.ValidParameters.concat(dashboard.Variables.Standard);
			widget.FrameTitle = uc.ReplaceVariablesValuesInText(widget.FrameTitle, variables);
			switch (widget.WidgetType) {
				case WidgetType.Object:
					uc.ReplaceVariablesInObject(widget, variables);
					for (var i = 0; i < widget.Elements.length; i++)
						uc.ReplaceVariablesInObjectElement(widget.Elements[i], variables);
					break;
				case WidgetType.Filter:
					uc.ReplaceVariablesInFilter(widget, variables);
					break;
				case WidgetType.TextBlock:
					uc.ReplaceVariablesInTextBlock(widget, variables);
					break;
				case WidgetType.Button:
					uc.ReplaceVariablesInButton(widget, variables);
					break;
				case WidgetType.Image:
					uc.ReplaceVariablesInImage(widget, variables);
					break;
			}
		});
	}

	this.ReplaceVariablesInObject = function (object, variables) {
		switch (object.Output.Type) {
			case OutputType.Chart:
				object.Output.Title = this.ReplaceVariablesValuesInText(object.Output.Title, variables);
				object.Output.XAxisTitle = this.ReplaceVariablesValuesInText(object.Output.XAxisTitle, variables);
				object.Output.YAxisTitle = this.ReplaceVariablesValuesInText(object.Output.YAxisTitle, variables);
				break;
			case OutputType.Table:
			case OutputType.PivotTable:
				object.Output.PageSize = this.ReplaceVariableValue(object.Output.PageSize, [DataType.Integer], false, variables);
				break;
		}
	}

	this.ReplaceVariablesInObjectElement = function (element, variables) {
		if (element.Visible == QueryViewerVisible.Yes || element.Visible == QueryViewerVisible.Always) {
			element.Title = this.ReplaceVariablesValuesInText(element.Title, variables);
			if (element.Type == QueryViewerElementType.Axis && element.Axis != QueryViewerAxisType.Pages && (element.DataType == DataType.Date || element.DataType == DataType.DateTime)) {
				if (element.Grouping.GroupByYear)
					element.Grouping.YearTitle = this.ReplaceVariablesValuesInText(element.Grouping.YearTitle, variables);
				if (element.Grouping.GroupBySemester)
					element.Grouping.SemesterTitle = this.ReplaceVariablesValuesInText(element.Grouping.SemesterTitle, variables);
				if (element.Grouping.GroupByQuarter)
					element.Grouping.QuarterTitle = this.ReplaceVariablesValuesInText(element.Grouping.QuarterTitle, variables);
				if (element.Grouping.GroupByMonth)
					element.Grouping.MonthTitle = this.ReplaceVariablesValuesInText(element.Grouping.MonthTitle, variables);
				if (element.Grouping.GroupByDayOfWeek)
					element.Grouping.DayOfWeekTitle = this.ReplaceVariablesValuesInText(element.Grouping.DayOfWeekTitle, variables);
			}
			if (element.Type == QueryViewerElementType.Datum) {
				element.Format.TargetValue = this.ReplaceVariableValue(element.Format.TargetValue, [DataType.Integer, DataType.Real], false, variables);
				element.Format.MaximumValue = this.ReplaceVariableValue(element.Format.MaximumValue, [DataType.Integer, DataType.Real], false, variables);
			}
			if (element.Type == QueryViewerElementType.Axis && element.Axis != QueryViewerAxisType.Pages) {
				var validDataTypes = [element.DataType];
				if (element.DataType == DataType.Real)
					validDataTypes.push(DataType.Integer);
				if (element.Filter.Type == QueryViewerFilterType.ShowSomeValues)
					element.Filter.Values = this.ReplaceVariableValue(element.Filter.Values, validDataTypes, true, variables);
				if (element.ExpandCollapse.Type == QueryViewerExpandCollapseType.ExpandSomeValues)
					element.ExpandCollapse.Values = this.ReplaceVariableValue(element.ExpandCollapse.Values, validDataTypes, true, variables);
				if (element.AxisOrder.Type == QueryViewerAxisOrderType.Custom)
					element.AxisOrder.Values = this.ReplaceVariableValue(element.AxisOrder.Values, validDataTypes, true, variables);
			}
		}
	}

	this.ReplaceVariablesInFilter = function (filter, variables) {
		filter.Caption = this.ReplaceVariablesValuesInText(filter.Caption, variables);
		if (filter.Control.Type == ControlType.Editbox)
			filter.Control.InviteMessage = this.ReplaceVariablesValuesInText(filter.Control.InviteMessage, variables);
	}

	this.ReplaceVariablesInFilterOnlyParameters = function (filter, variables) {
		if (filter.Type != FilterType.Collection) {
			if (filter.Type == FilterType.Value)
				this.ReplaceVariableValueInFilter(filter.Value, "Value", "SerializedValue", filter, variables);
			else {
				this.ReplaceVariableValueInFilter(filter.LowerValue, "LowerValue", "SerializedLowerValue", filter, variables);
				this.ReplaceVariableValueInFilter(filter.UpperValue, "UpperValue", "SerializedUpperValue", filter, variables);
			}
			this.ReplaceVariableValueInFilter(filter.MinValue, "MinValue", "", filter, variables);
			this.ReplaceVariableValueInFilter(filter.MaxValue, "MaxValue", "", filter, variables);
		}
		else
			this.ReplaceVariableValueInFilter(filter.Values, "Values", "SerializedValue", filter, variables);
	}

	this.ReplaceVariablesInButton = function (button, variables) {
		button.Caption = this.ReplaceVariablesValuesInText(button.Caption, variables);
	}

	this.ReplaceVariablesInImage = function (image, variables) {
		image.AlternateText = this.ReplaceVariablesValuesInText(image.AlternateText, variables);
	}

	this.ReplaceVariablesInTextBlock = function (textBlock, variables) {
		textBlock.Caption = this.ReplaceVariablesValuesInText(textBlock.Caption, variables);
	}

	this.GetQueryViewerElementId = function (elementId) {
		return this.editor.GetDOMElementId(elementId) + "_QueryViewer";
	}

	this.GetRangeElementId = function (elementId) {
		return this.editor.GetDOMElementId(elementId) + "_Range";
	}

	this.IsMainTable = function (table) {
		return table.ElementId == MAIN_TABLE_ID || table.ElementId == FILTERS_TABLE_ID;
	}

	this.IsMainTableLayoutTable = function (table) {
		if (this.IsMainTable(table))
			return false;
		else
			return this.DashboardObject.Layout.MainTableLayout != MainTableLayout.Free && table.ParentElement.ParentElement.ParentElement.ElementId == MAIN_TABLE_ID;
	}

	this.IsHeight100Percent = function (mainTableLayout, rowIndex, table) {
		if (table.ElementId == MAIN_TABLE_ID)
			switch (mainTableLayout) {
				case MainTableLayout.TwoColumns:
				case MainTableLayout.ThreeColumns:
					return true;
				case MainTableLayout.HeaderTwoColumns:
				case MainTableLayout.HeaderThreeColumns:
				case MainTableLayout.HeaderTwoColumnsFooter:
				case MainTableLayout.HeaderThreeColumnsFooter:
					return rowIndex == 1;
				default:
					return rowIndex == table.Rows.length - 1;
			}
		else
			return rowIndex == table.Rows.length - 1;
	}

	this.GetPictureProperties = function(dataType, picture) {
		if (dataType == DataType.Integer || dataType == DataType.Real)
			return qv.util.ParseNumericPicture(dataType == DataType.Integer ? "integer" : "", picture);
		else if (dataType == DataType.Date || dataType == DataType.DateTime)
			return qv.util.ParseDateTimePicture(dataType == DataType.DateTime ? "datetime" : "", picture);
		else
			return null;
	}

	this.MakeFilterItem = function(filterName, filter) {
		var filterItem = {};
		filterItem.Name = filterName;
		filterItem.Values = [];
		if (filter != null) {
			switch (filter.Type) {
				case FilterType.Value:
					filterItem.Values.push(filter.SerializedValue);
					break;
				case FilterType.Range:
					if (filter.NameLowerValue == filterName)
						filterItem.Values.push(filter.SerializedLowerValue);
					else
						filterItem.Values.push(filter.SerializedUpperValue);
					break;
				case FilterType.Collection:
					for (var j = 0; j < filter.Values.length; j++)
						filterItem.Values.push(this.ValueToString(filter.Values[j], filter.DataType, filter.Picture, filter.PictureProperties));
					break;
			}
		}
		return filterItem;
	}

	this.LoadOtherFilterValues = function (eventParameter) {
		eventParameter.AllFilters = [];
		var dashboard = this.DashboardObject
		for (var i = 0; i < dashboard.Filters.length; i++) {
			var filter = dashboard.Filters[i];
			var filterItem;
			if (filter.Type != FilterType.Range)
				eventParameter.AllFilters.push(this.MakeFilterItem(filter.Name, filter));
			else {
				eventParameter.AllFilters.push(this.MakeFilterItem(filter.NameLowerValue, filter));
				eventParameter.AllFilters.push(this.MakeFilterItem(filter.NameUpperValue, filter));
			}
		}
	}

	this.LoadFiltersChangedData = function (filterNames) {
		this.FiltersChangedData = {};
		this.FiltersChangedData.ChangedFilters = [];
		for (var i = 0; i < filterNames.length; i++) {
			var filter = this.GetFilterByAnyName(filterNames[i]);
			var filterItem = this.MakeFilterItem(filterNames[i], filter);
			filterItem.Enabled = filter != null;
			this.FiltersChangedData.ChangedFilters.push(filterItem);
		}
		this.LoadOtherFilterValues(this.FiltersChangedData);
	}
	
	this.LoadItemClickData = function (obj, qViewer) {
		this.ItemClickData = {};
		this.ItemClickData.Object = obj.Name;
		this.ItemClickData.Element = qViewer.ItemClickData.Name;
		this.ItemClickData.Value = qViewer.ItemClickData.Value;
		this.ItemClickData.Context = JSON.parse(JSON.stringify(qViewer.ItemClickData.Context));
		this.LoadOtherFilterValues(this.ItemClickData);
	}

	this.LoadValuesHighlightedData = function (selection) {
		this.ValuesHighlightedData = {};
		this.ValuesHighlightedData.Elements = selection;
		this.LoadOtherFilterValues(this.ValuesHighlightedData);
	}

	this.RefreshDashboard = function (changedFilters) {
		if (changedFilters && this.FiltersChanged) {
			this.LoadFiltersChangedData(changedFilters);
			this.FiltersChanged();
		}
		var dashboard = this.DashboardObject;
		this.UpdateFilterVariables(dashboard, changedFilters);
		for (var i = 0; i < dashboard.AllWidgets.length; i++) {
			var widget = dashboard.AllWidgets[i];
			this.RefreshElementIfNeeded(widget, changedFilters);
		}
		this.RefreshElementIfNeeded(dashboard.Layout, changedFilters);
	}

	this.CaseInsensitiveIndexOf = function (arr, value) {
		for (var i = 0; i < arr.length; i++)
			if (arr[i].toLowerCase() == value.toLowerCase())
				return i;
		return -1;
	}

	this.RefreshElementIfNeeded = function (element, changedFilters) {

		function affectedByFilterChange(uc, element, changedFilters) {
			if (!changedFilters)
				return true;	// no se especificaron los filtros
			else {
				for (var i = 0; i < changedFilters.length; i++) {
					var pos = uc.CaseInsensitiveIndexOf(element.Parameters, changedFilters[i]);
					if (pos >= 0)
						return true;
				}
				return false;
			}
		}

		var refreshElement = affectedByFilterChange(this, element, changedFilters);
		if (refreshElement) {
			if (element.ElementType == ElementType.Layout)
				this.RefreshLayout();
			else if (element.ElementType == ElementType.Widget) {
				if (element.WidgetType == WidgetType.Filter && element.Control.Dynamic)
					element.Control.RefreshValues = true;	// Hay que refrescar los valores del filtro porque usa un parámetro que acaba de cambiar de valor
				this.RefreshWidget(element);
			}
		}
	}

	this.GetThemeStyleSheet = function () {

		function GetStyleSheet(cssName) {
			var styleSheet;
			for (var i = 0; i < document.styleSheets.length; i++)
				if (document.styleSheets[i].href != null && document.styleSheets[i].href.indexOf(cssName) >= 0) {
					styleSheet = document.styleSheets[i];
					break;
				}
			return styleSheet;
		}

		if (!this._themeStyleSheet) {
			var cssName = gx.theme + ".css";
			this._themeStyleSheet = GetStyleSheet(cssName);
		}
		return this._themeStyleSheet;
	}

	this.InsertCssRules = function (cssRules) {
		var styleSheet = this.GetThemeStyleSheet();
		var arr = cssRules.split('}');
		for (var i = 0; i < arr.length; i++) {
			if (arr[i] != "") {
				var cssRule = arr[i] + '}';
				styleSheet.insertRule(cssRule);
			}
		}
	}

	this.UpdateElement = function (element, newElement, type) {
		switch (type) {
			case ElementType.Table:
				this.UpdateTable(element, newElement);
				break;
			case ElementType.Row:
				this.UpdateRow(element, newElement, false);
				break;
			case ElementType.Cell:
				this.UpdateCell(element, newElement);
				break;
			case ElementType.Widget:
				this.UpdateWidget(element, newElement);
				break;
		}
		this.editor.UpdateSelectedElementPath(element);
	}

	this.UpdateCell = function (cell, newCell) {

		function AddChild(uc, dashboard, cell, cellChild, childType) {
			cell.ChildType = childType;
			cell[childType] = cellChild;
			cell.IsEmpty = false;
			var cellChildContainer = document.createElement("DIV");
			var domIds = [];
			var html;
			if (newChild.ElementType == ElementType.Table)
				html = uc.editor.GetTableHTML(dashboard, cellChild, true, domIds, false);
			else
				html = uc.editor.GetWidgetHTML(dashboard, cell.ParentElement.ParentElement, cell, cellChild, domIds);
			uc.RemoveDomElements(domIds);
			cellChildContainer.innerHTML = html;
			var cellContainer = uc.GetElementContainer(cell.ElementId);
			if (cellContainer.childNodes[0])
				cellContainer.removeChild(cellContainer.childNodes[0]);
			cellContainer.appendChild(cellChildContainer.firstChild);
			cellContainer.classList.remove("gx-db-cell-empty");
		}

		var dashboard = this.DashboardObject;
		var cellContainer = this.GetElementContainer(cell.ElementId);
		this.CellDefaults(newCell);

		if (newCell.IsEmpty && !cell.IsEmpty) {
			// Vaciar una celda no está permitido por el editor (se borra la celda entera)
		} else if (!newCell.IsEmpty && cell.IsEmpty) {
			// Se agregó un elemento a la celda
			var newChild = newCell[newCell.ChildType];
			this.editor.ParentInitialize(newChild, cell);
			if (newCell.HasTable)
				this.editor.TableDefaults(newChild, dashboard, "");
			else {
				this.editor.WidgetDefaults(newChild, dashboard, newCell.ChildType);
				this.editor.AddElement(dashboard, newChild);
			}
			AddChild(this, dashboard, cell, newChild, newCell.ChildType);
		}
		else if (!cell.IsEmpty && !newCell.IsEmpty) {
			// Si el hijo es una tabla tengo que verificar que no haya cambiado de contenido
			var child = cell[cell.ChildType];
			var newChild = newCell[newCell.ChildType];
			if (cell.HasTable)
				this.UpdateTable(child, newChild);
			else
				this.UpdateWidget(child, newChild);
		}
		if (cell.CellClass != newCell.CellClass) {
			cell.CellClass = newCell.CellClass;
			this.UpdateDOMElementClass(cellContainer, newCell.CellClass, "gx-db-cell");
		}

	}

	this.UpdateRow = function (row, newRow, forceUpdateCellClasses) {

		function RemoveCell(uc, dashboard, row, rowContainer, cell, cellIndex) {
			var cellContainer = uc.GetElementContainer(cell.ElementId);
			if (rowContainer.id == cellContainer.parentNode.id)
				rowContainer.removeChild(cellContainer);
			row.Cells.splice(cellIndex, 1);
			uc.editor.RemoveElement(dashboard, cell);
			if (!cell.IsEmpty) {
				var cellChild = cell[cell.ChildType];
				uc.editor.SelectedElementIds.splice(uc.editor.SelectedElementIds.indexOf(cellChild.ElementId), 1);
				uc.editor.RemoveElement(dashboard, cellChild);
			}
		}

		function AddCell(uc, dashboard, row, rowIndex, cell, cellIndex) {
			var currentCellContainer;
			if (cellIndex < row.Cells.length)
				currentCellContainer = uc.GetElementContainer(row.Cells[cellIndex].ElementId);
			// Creo e inserto la nueva celda
			row.Cells.splice(cellIndex, 0, cell);
			// Agrego la celda en el DOM
			var cellContainer = document.createElement("DIV");
			var table = row.ParentElement;
			var domIds = [];
			var html = uc.editor.GetCellHTML(dashboard, table, row, rowIndex, cell, cellIndex, "", domIds);
			uc.RemoveDomElements(domIds);
			cellContainer.innerHTML = html;
			var rowContainer = uc.GetElementContainer(row.ElementId);
			if (currentCellContainer)
				rowContainer.insertBefore(cellContainer.firstChild, currentCellContainer);
			else
				rowContainer.appendChild(cellContainer.firstChild);
		}

		function MoveCell(uc, row, rowContainer, from, to) {
			var fromContainer = uc.GetElementContainer(row.Cells[cellIndex].ElementId);
			var toContainer = uc.GetElementContainer(row.Cells[i].ElementId);
			row.Cells.splice(to, 0, row.Cells.splice(from, 1)[0]);
			rowContainer.insertBefore(fromContainer, toContainer);
		}

		function UpdateCellClasses(uc, table, row, rowIndex, cellIndex) {
			// Actualizo las clases de la celda
			var cell = row.Cells[cellIndex];
			var cellClass = uc.editor.CellClass2(table, row, rowIndex, cell, cellIndex);
			var cellContainer = uc.GetElementContainer(cell.ElementId);
			cellContainer.className = cellClass;
		}

		function UpdateCellChartSize(row, cellIndex) {
			var cell = row.Cells[cellIndex];
			if (!cell.IsEmpty) {
				var cellChild = cell[cell.ChildType];
				if (cellChild.WidgetType == WidgetType.Object && cellChild.Output.Type == OutputType.Chart) {
					var qViewer = cellChild.QueryViewer;
					if (qViewer) {
						qViewer.ForceChartRendering = true;
						code = "try { qv.chart.checkChartSize(" + qViewer.me() + ") } catch (e) {}";
						qViewer.IntervalCheckChartSize = setInterval(code, 0);	// Para que actualice el tamaño inmediatamente
					}
				}
			}
		}

		var dashboard = this.DashboardObject;
		var updateCellClass = row.Cells.length != newRow.Cells.length;
		var rowContainer = this.GetElementContainer(row.ElementId);
		this.RowDefaults(newRow);
		var addedCellIds = [];

		// Borro celdas que fueron eliminadas
		for (var i = row.Cells.length - 1; i >= 0; i--) {
			var cell = row.Cells[i];
			var newCellIndex = this.GetCellIndex(newRow, cell.ElementId)
			if (newCellIndex < 0)
				RemoveCell(this, dashboard, row, rowContainer, cell, i);
		}

		// Agrego celdas nuevas
		var rowIndex = this.GetRowIndex(row.ParentElement, row.ElementId);
		for (var i = 0; i < newRow.Cells.length; i++) {
			var newCell = newRow.Cells[i];
			var cellIndex = this.GetCellIndex(row, newCell.ElementId)
			if (cellIndex < 0) {
				if (!newCell.IsEmpty) {
					addedCellIds.push(newCell.ElementId);
					this.editor.ParentInitialize(newCell, row);
					this.editor.CellDefaults(newCell, dashboard);
					AddCell(this, dashboard, row, rowIndex, newCell, i);
				}
			}
		}

		// Muevo celdas que cambiaron de lugar
		for (var i = 0; i < row.Cells.length; i++) {
			var newCell = newRow.Cells[i];
			var newCellId = newRow.Cells[i].ElementId;
			var cellIndex = this.GetCellIndex(row, newCellId);
			if (cellIndex != i)
				MoveCell(this, row, rowContainer, cellIndex, i);
		}

		// Modifico celdas que permanecen pero cambiaron su contenido
		for (var i = 0; i < newRow.Cells.length; i++)
			if (addedCellIds.indexOf(row.Cells[i].ElementId) < 0)
				this.UpdateCell(row.Cells[i], newRow.Cells[i]);

		// Actualizo la clase de las celdas que quedaron
		if (updateCellClass || forceUpdateCellClasses)
			for (var i = 0; i < row.Cells.length; i++)
				UpdateCellClasses(this, row.ParentElement, row, rowIndex, i);

		if (updateCellClass)	// Si cambió el número de celdas tengo que hacer el resize de las gráficas
			for (var i = 0; i < row.Cells.length; i++)
				UpdateCellChartSize(row, i);

		if (row.RowClass != newRow.RowClass) {
			row.RowClass = newRow.RowClass;
			this.UpdateDOMElementClass(rowContainer, newRow.RowClass, "gx-db-row");
		}
	}

	this.UpdateTable = function (table, newTable) {

		function RemoveRow(uc, dashboard, table, tableContainer, row, rowIndex) {
			var rowContainer = uc.GetElementContainer(row.ElementId);
			if (tableContainer.id = rowContainer.parentNode.id)
				tableContainer.removeChild(rowContainer);
			table.Rows.splice(rowIndex, 1);
			for (var i = 0; i < row.Cells.length; i++) {
				var cell = row.Cells[i];
				uc.editor.RemoveElement(dashboard, cell);
				if (!cell.IsEmpty) {
					var cellChild = cell[cell.ChildType];
					uc.editor.RemoveElement(dashboard, cellChild);
				}
			}
			uc.editor.RemoveElement(dashboard, row);
		}

		function AddRow(uc, dashboard, table, row, rowIndex) {
			var currentRowContainer;
			if (rowIndex < table.Rows.length)
				currentRowContainer = uc.GetElementContainer(table.Rows[rowIndex].ElementId);
			// Creo e inserto la nueva fila
			table.Rows.splice(rowIndex, 0, row);
			// Agrego la fila en el DOM
			var rowContainer = document.createElement("DIV");
			var domIds = [];
			var html = uc.editor.GetRowHTML(dashboard, table, row, rowIndex, domIds);
			uc.RemoveDomElements(domIds);
			rowContainer.innerHTML = html;
			var tableContainer = uc.GetElementContainer(table.ElementId);
			if (currentRowContainer)
				tableContainer.insertBefore(rowContainer.firstChild, currentRowContainer);
			else
				tableContainer.appendChild(rowContainer.firstChild);
		}

		var dashboard = this.DashboardObject;
		var forceUpdateCellClasses = false;
		var tableContainer = this.GetElementContainer(table.ElementId);
		this.TableDefaults(newTable);
		var addedRowIds = [];

		// Borro filas que fueron eliminadas
		var lastRowIndex = table.ElementId == MAIN_TABLE_ID || table.ElementId == FILTERS_TABLE_ID || this.IsMainTableLayoutTable(table) ? table.Rows.length - 2 : table.Rows.length - 1; // No se borran nunca las filas para agregar elementos al final
		for (var i = lastRowIndex; i >= 0; i--) {
			var row = table.Rows[i];
			var newRowIndex = this.GetRowIndex(newTable, row.ElementId)
			if (newRowIndex < 0) {
				forceUpdateCellClasses = true;
				RemoveRow(this, dashboard, table, tableContainer, row, i);
			}
		}

		// Agrego filas nuevas
		for (var i = 0; i < newTable.Rows.length; i++) {
			var newRow = newTable.Rows[i];
			var rowIndex = this.GetRowIndex(table, newRow.ElementId)
			if (rowIndex < 0) {
				addedRowIds.push(newRow.ElementId);
				forceUpdateCellClasses = true;
				this.editor.ParentInitialize(newRow, table);
				this.editor.RowDefaults(newRow, dashboard);
				AddRow(this, dashboard, table, newRow, i);
			}
		}

		// Si la tabla quedó sin ninguna fila, agrego para poder seguir editandola
		if (table.Rows.length == 0) {
			newRow = { ElementId: table.ElementId + "_NEWROW", Cells: [{ ElementId: table.ElementId + "_NEWROW" + "_NEWCELL", IsEmpty: true }] };
			this.editor.ParentInitialize(newRow, table);
			this.editor.RowDefaults(newRow, dashboard);
			AddRow(this, dashboard, table, newRow, 0);
		}

		// Modifico filas que permanecen pero cambiaron su contenido
		for (var i = 0; i < newTable.Rows.length; i++)
			if (addedRowIds.indexOf(table.Rows[i].ElementId) < 0)
				this.UpdateRow(table.Rows[i], newTable.Rows[i], forceUpdateCellClasses);

		if (table.Class != newTable.Class) {
			table.Class = newTable.Class;
			this.UpdateDOMElementClass(tableContainer, newTable.Class, "gx-db-table");
		}

	}

	this.UpdateWidget = function (widget, newWidget) {
		var dashboard = this.DashboardObject;
		var widgetContainer = this.GetElementContainer(widget.ElementId);
		this.WidgetDefaults(newWidget, dashboard, widget.WidgetType);
		var updateContainerClass = widget.ContainerClass != newWidget.ContainerClass;
		this.CopyObjectProperties(newWidget, widget);
		this.RefreshWidget(widget);
		if (updateContainerClass)
			this.UpdateDOMElementClass(widgetContainer, widget.ContainerClass, "gx-db-widget");
	}

	this.CopyObjectProperties = function (source, destination) {
		for (var property in source)
			if (property != "Selected")
				if (destination[property] instanceof Object && !(destination[property] instanceof Array) && !(destination[property] instanceof gx.date.gxdate) && property != "ParentElement")
					this.CopyObjectProperties(source[property], destination[property]);
				else
					destination[property] = source[property];
	}

	this.GetRowIndex = function (table, id) {
		for (var i = 0; i < table.Rows.length; i++)
			if (table.Rows[i].ElementId == id)
				return i;
		return -1;
	}

	this.GetCellIndex = function (row, id) {
		for (var i = 0; i < row.Cells.length; i++)
			if (row.Cells[i].ElementId == id)
				return i;
		return -1;
	}

	this.GetElementContainer = function (elementId) {
		var containerId = this.editor.GetDOMElementId(elementId);
		return document.getElementById(containerId);
	}

	this.RemoveDomElements = function (ids) {
		for (var i = 0; i < ids.length; i++) {
			var element = document.getElementById(ids[i]);
			if (element != null)
				element.parentNode.removeChild(element);
		}
	}

	this.OnAddElement = function (dashboard, element) {
		switch (element.WidgetType) {
			case WidgetType.Object:
				dashboard.Objects.push(element);
				break;
			case WidgetType.Filter:
				dashboard.Filters.push(element);
				break;
			case WidgetType.Button:
				dashboard.Buttons.push(element);
				break;
			case WidgetType.Image:
				dashboard.Images.push(element);
				break;
			case WidgetType.TextBlock:
				dashboard.TextBlocks.push(element);
				break;
		}
	}

	this.OnRemoveElement = function (dashboard, element) {
		switch (element.WidgetType) {
			case WidgetType.Object:
				this.editor.RemoveElementById(dashboard.Objects, element.ElementId);
				break;
			case WidgetType.Filter:
				this.editor.RemoveElementById(dashboard.Filters, element.ElementId);
				break;
			case WidgetType.Button:
				this.editor.RemoveElementById(dashboard.Buttons, element.ElementId);
				break;
			case WidgetType.Image:
				this.editor.RemoveElementById(dashboard.Images, element.ElementId);
				break;
			case WidgetType.TextBlock:
				this.editor.RemoveElementById(dashboard.TextBlocks, element.ElementId);
				break;
		}
	}

	this.ValuesIndexOf = function (array, value, dataType) {
		if ((dataType == DataType.Date || dataType == DataType.DateTime) && value instanceof gx.date.gxdate) {
			var found = false;
			for (i = 0; i < array.length; i++)
				if (value.compare(array[i]) == 0) {
					found = true;
					break;
				}
			return found ? i : -1;
		}
		else
			return array.indexOf(value);
	}

	this.SameValue = function (value1, value2, dataType) {
		if (dataType == DataType.Date || dataType == DataType.DateTime)
			return value1.compare(value2) == 0;
		else
			return value1 === value2;
	}

	this.CompareValues = function (value1, value2, dataType) {
		if (dataType == DataType.Date || dataType == DataType.DateTime)
			return value1.compare(value2);
		else
			return value1 === value2 ? 0 : (value1 < value2 ? -1 : 1);
	}

	this.ParseValue = function (value, dataType, isCollection) {
		return this.ParseValueMain(value, dataType, isCollection, false);
	}

	this.ParseInternalValue = function (value, dataType, isCollection) {
		return this.ParseValueMain(value, dataType, isCollection, true);
	}

	this.ParseValueMain = function (value, dataType, isCollection, internalRepresentation) {
		var parsedValue;
		var parseOK;
		if (isCollection) {
			try {
				var values = JSON.parse(value);
				var parsedValue = [];
				parseOK = true;
				for (var i = 0; i < values.length; i++) {
					var parseResult = this.ParseValueMain(values[i], dataType, false, true);
					if (parseResult.Success)
						parsedValue.push(parseResult.ParsedValue);
				}
			} catch (e) {
				parseOK = false;
			}
		}
		else
			switch (dataType) {
				case DataType.Integer:
					parsedValue = parseInt(value);
					parseOK = !isNaN(parsedValue);
					break;
				case DataType.Real:
					parsedValue = parseFloat(value);
					parseOK = !isNaN(parsedValue);
					break;
				case DataType.Character:
					parsedValue = value.toString();
					parseOK = true;
					break;
				case DataType.Boolean:
					parsedValue = value.toString().toLowerCase() == "true";
					parseOK = true;
					break;
				case DataType.Date:
				case DataType.DateTime:
					if (internalRepresentation)
						parsedValue = new gx.date.gxdate(value, "Y4MD");	// Viene del JSON
					else
						parsedValue = new gx.date.gxdate(value);
					parseOK = true;
					break;
				case DataType.GUID:
					parsedValue = value.length <= 36 ? value : value.substr(0, 36);
					parsedValue = parsedValue.toLowerCase().replace(/-/g, "");
					parsedValue = parsedValue.replace(/([0-f]{8})([0-f]{4})([0-f]{4})([0-f]{4})([0-f]{12})/, "$1-$2-$3-$4-$5");
					parseOK = /([0-f]{8})-([0-f]{4})-([0-f]{4})-([0-f]{4})-([0-f]{12})/.test(parsedValue);
					break;
				case DataType.GeoPoint:
					if (qv.util.trim(value) == "") {
						parseOK = true;
						parsedValue = "";
					}
					else {
						var coordinates = qv.util.trim(value.toUpperCase().replace('POINT', '').replace('(', '').replace(')', '')).split(' ');
						if (coordinates.length == 2) {
							var parserResultLatitude = this.ParseValueMain(coordinates[0], DataType.Real, false, internalRepresentation);
							if (parserResultLatitude.Success) {
								var parserResultLongitude = this.ParseValueMain(coordinates[1], DataType.Real, false, internalRepresentation);
								if (parserResultLongitude.Success) {
									if (parserResultLatitude.ParsedValue >= -90 && parserResultLatitude.ParsedValue <= 90 && parserResultLongitude.ParsedValue >= -180 && parserResultLongitude.ParsedValue <= 180) {
										parseOK = true;
										parsedValue = "POINT (" + parserResultLatitude.ParsedValue + " " + parserResultLongitude.ParsedValue + ")";
									}
									else
										parseOK = false;
								}
								else
									parseOK = false;
							}
							else
								parseOK = false;
						}
						else
							parseOK = false;
					}
					break;
				default:
					parseOK = false;
					break;
			}
		return { Success: parseOK, ParsedValue: parsedValue };
	}

	this.ValueToString = function (value, dataType, picture, pictureProperties) {
		return this.ValueToStringMain(value, dataType, picture, pictureProperties, false);
	}

	this.ValueToInternalString = function (value, dataType, picture, pictureProperties) {
		return this.ValueToStringMain(value, dataType, picture, pictureProperties, true);
	}

	this.ValueToStringMain = function (value, dataType, picture, pictureProperties, internalRepresentation) {

		if (value == null)
			return "";
		else {
			switch (dataType) {
				case DataType.Integer:
				case DataType.Real:
					return gx.num.formatNumber(value, pictureProperties.DecimalPrecision, picture, 0, true, false);
				case DataType.Character:
					return picture == "@!" ? value.toUpperCase() : value;
				case DataType.Date:
				case DataType.DateTime:
					var formattedDate;
					if (internalRepresentation) {
						formattedDate = value.getStringWithFmt("Y4MD");
						if (formattedDate == value.emptyDateString("Y4MD"))
							formattedDate = "0000-00-00";
						else
							formattedDate = formattedDate.replace(/\//g, "-");
					}
					else
						formattedDate = value.getStringWithFmt(pictureProperties.DateFormat);
					if (dataType == DataType.DateTime) {
						var oldTimeFmt = value.TimeFmt;
						if (internalRepresentation)
							value.TimeFmt = 24;
						var includeHours = internalRepresentation || pictureProperties.IncludeHours;
						var includeMinutes = internalRepresentation || pictureProperties.IncludeMinutes;
						var includeSeconds = internalRepresentation || pictureProperties.IncludeSeconds;
						var formattedTime = includeHours ? value.getTimeString(includeMinutes, includeSeconds, includeHours) : "";
						if (formattedTime == "::")
							formattedTime = "00:00:00";
						if (formattedTime != "")
							formattedDate += " " + formattedTime;
						if (internalRepresentation)
							value.TimeFmt = oldTimeFmt;
					}
					return formattedDate;
				case DataType.GUID:
					return value;
				case DataType.GeoPoint:
					return value;
				default:
					return value.toString();
			}
		}
	}

	this.ValuesToString = function (array, dataType, picture, pictureProperties) {
		if (dataType == DataType.Date || dataType == DataType.DateTime) {
			var newArray = [];
			for (var i = 0; i < array.length; i++) {
				var value = array[i];
				var valueStr = this.ValueToInternalString(value, dataType, picture, pictureProperties);
				newArray.push(valueStr);
			}
			return JSON.stringify(newArray);		// JSON no acepta Date
		}
		else
			return JSON.stringify(array);
	}

	this.DefaultPicture = function (dataType) {
		switch (dataType) {
			case DataType.Integer:
				return "ZZZZZZZZZZZZZZ9";
			case DataType.Real:
				return "ZZZZZZZZZZZZZZ9.99";
			case DataType.Date:
				return "99/99/9999";
			case DataType.DateTime:
				return "99/99/9999 99:99:99";
			case DataType.GUID:
				return "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
			default:
				return "";
		}
	}

	this.ProcessFilter = function (filter) {

		function CheckVariable(uc, value, dataType, filterType) {
			return uc.CheckVariable(value, [dataType], filterType == FilterType.Collection, uc.DashboardObject.Variables.AllParameters.concat(uc.DashboardObject.Variables.Standard));
		}

		function Parse(uc, value, dataType, filterType, allowNull) {
			if (value == "" && allowNull)
				return null;
			var notEmptyValue = value == "" ? uc.GetEmptyValueStr(dataType, filterType == FilterType.Collection) : value;
			var parserResult = uc.ParseInternalValue(notEmptyValue, dataType, filterType == FilterType.Collection);
			if (parserResult.Success)
				return parserResult.ParsedValue;
			else if (allowNull)
				return null;
			else {
				var emptyValue = uc.GetEmptyValueStr(dataType, filterType == FilterType.Collection);
				return Parse(uc, emptyValue, dataType, filterType, false);
			}
		}

		if (filter.Picture == "")
			filter.Picture = this.DefaultPicture(filter.DataType);
		var pictureProperties = this.GetPictureProperties(filter.DataType, filter.Picture);
		if (pictureProperties != null)
			filter.PictureProperties = pictureProperties;
		var checkResult;
		if (filter.Type != FilterType.Collection) {
			if (filter.Type == FilterType.Value) {
				checkResult = CheckVariable(this, filter.Value, filter.DataType, filter.Type);
				if (!checkResult.IsVariable) {
					filter.Value = Parse(this, filter.Value, filter.DataType, filter.Type, false);
					filter.SerializedValue = this.ValueToString(filter.Value, filter.DataType, filter.Picture, filter.PictureProperties);
				}
				else
					filter.SerializedValue = checkResult.SerializedValue;
			}
			else {
				checkResult = CheckVariable(this, filter.LowerValue, filter.DataType, filter.Type);
				if (!checkResult.IsVariable) {
					filter.LowerValue = Parse(this, filter.LowerValue, filter.DataType, filter.Type, false);
					filter.SerializedLowerValue = this.ValueToString(filter.LowerValue, filter.DataType, filter.Picture, filter.PictureProperties);
				}
				else
					filter.SerializedLowerValue = checkResult.SerializedValue;
				checkResult = CheckVariable(this, filter.UpperValue, filter.DataType, filter.Type);
				if (!checkResult.IsVariable) {
					filter.UpperValue = Parse(this, filter.UpperValue, filter.DataType, filter.Type, false);
					filter.SerializedUpperValue = this.ValueToString(filter.UpperValue, filter.DataType, filter.Picture, filter.PictureProperties);
				}
				else
					filter.SerializedUpperValue = checkResult.SerializedValue;
			}
			checkResult = CheckVariable(this, filter.MinValue, filter.DataType, filter.Type);
			if (!checkResult.IsVariable)
				filter.MinValue = Parse(this, filter.MinValue, filter.DataType, filter.Type, true);
			checkResult = CheckVariable(this, filter.MaxValue, filter.DataType, filter.Type);
			if (!checkResult.IsVariable)
				filter.MaxValue = Parse(this, filter.MaxValue, filter.DataType, filter.Type, true);
		}
		else {
			checkResult = CheckVariable(this, filter.Values, filter.DataType, filter.Type);
			if (!checkResult.IsVariable) {
				for (var j = 0; j < filter.Values.length; j++)
					filter.Values[j] = Parse(this, filter.Values[j], filter.DataType, FilterType.Value, false);
				filter.SerializedValue = this.ValuesToString(filter.Values, filter.DataType, filter.Picture, filter.PictureProperties);
			}
			else
				filter.SerializedValue = checkResult.SerializedValue;
		}
		if (!filter.Control.Dynamic && (filter.Control.Type == ControlType.Checkbox || filter.Control.Type == ControlType.ComboBox || filter.Control.Type == ControlType.RadioButton || filter.Control.Type == ControlType.DropDownList)) {
			if (filter.DataType == DataType.Boolean) {
				filter.Control.Values = [true, false];
				filter.Control.Descriptions = ["true", "false"];
			}
			else
				for (var j = 0; j < filter.Control.Values.length; j++) {
					filter.Control.Values[j] = Parse(this, filter.Control.Values[j], filter.DataType, false, false);
					filter.Control.Descriptions[j] = Parse(this, qv.util.getTranslation(filter.Control.Descriptions[j]), DataType.Character, false, false);
				}
		}
	}

	this.RenderDashboard = function () {
		var dbClasses = "gx-db" + (this.Class != "" ? " " : "") + this.Class;
		qv.util.SetUserControlClass(this, dbClasses);
		this.InsertCssRules(this.CSSRules);
		var dashboard = this.DashboardObject;
		var html = "";
		if (dashboard.Layout) {
			var layoutHtml = this.GetLayoutHTML(dashboard);
			if (this.IsDashboardEdit()) {
				var selectorHTML = this.editor.GetElementSelectorHTML(dashboard.Layout);
				html = this.TwoRowsTableHTML(null, "", "100%", "100%", selectorHTML, layoutHtml);
			}
			else
				html = layoutHtml;
			html += '<div id ="gx-dashboard-error-placeholder" class="gx-db-error" style="position:fixed;z-index:100;display:none"></div>';
			if (this.IsDashboardEdit())
				html += '<div id ="gx-dashboard-references-placeholder" style="position:fixed;z-index:100;display:none"></div>';
		}
		this.setHtml(html);
	}

	this.UpdateDOMElementClass = function (element, elementClass, defaultClassName) {
		for (var i = element.classList.length - 1; i >= 0; i--) {
			var className = element.classList.item(i);
			if (className == defaultClassName || !qv.util.startsWith(className, "gx-db-"))
				element.classList.remove(className);
		}
		element.classList.add(elementClass == "" ? defaultClassName : elementClass);
	}

	this.RefreshLayout = function () {
		var dashboard = this.DashboardObject;
		var variables = dashboard.Variables.Filters;
		var titleSpan = document.getElementById("gx-db-title-text");
		if (titleSpan != null)
			titleSpan.innerText = this.ReplaceVariablesValuesInText(dashboard.Layout.Title, variables);
		var refreshPeriod = this.ReplaceVariableValue(dashboard.Layout.RefreshPeriod, [DataType.Integer, DataType.Real], false, variables);
		this.SetRefreshPeriod(this, refreshPeriod);
	}

	this.RefreshWidget = function (widget) {
		if (widget.WidgetType == WidgetType.Filter && widget.Control.Type == ControlType.Editbox && (widget.DataType == DataType.Date || widget.DataType == DataType.DateTime)) {
			var dateRangePicker = $('#' + this.GetRangeElementId(widget.ElementId)).data("daterangepicker");
			if (dateRangePicker != null)
				dateRangePicker.hide();
		}

		var container = this.GetElementContainer(widget.ElementId);
		var html = this.editor.GetWidgetContentHTML(this.DashboardObject, widget, null);
		if (widget.WidgetType != WidgetType.Object)
			container.innerHTML = html;
	}

	this.SerializeParameterValue = function (parameter) {
		if (this.IsDashboardEdit())
			parameter.SerializedValue = "&" + parameter.Name;
		else {
			if (parameter.Type == ParameterType.Value)
				parameter.SerializedValue = this.ValueToString(parameter.Value, parameter.DataType, parameter.Picture, parameter.PictureProperties);
			else
				parameter.SerializedValue = this.ValuesToString(parameter.Value, parameter.DataType, parameter.Picture, parameter.PictureProperties);
		}
	}

	this.MakeVariable = function (name, value, serializedValue, dataType, isCollection) {
		return { Name: name, Value: value, SerializedValue: serializedValue, DataType: dataType, IsCollection: isCollection };
	}

	this.LoadParameterVariables = function (dashboard) {

		function StandardVariables(uc) {

			function TodayVariable(uc) {
				var name = "Today";
				var value = gx.date.today();
				var dataType = DataType.Date;
				var picture = "99/99/9999";
				var pictureProperties = qv.util.ParseDateTimePicture("", "99/99/9999");
				var serializedValue = uc.IsDashboardEdit() ? "&" + name : uc.ValueToString(value, dataType, picture, pictureProperties);
				return uc.MakeVariable(name, value, serializedValue, dataType, false);
			}

			function TimeVariable(uc) {
				var name = "Time";
				var gxdate = gx.date.today();
				gxdate.HasTimePart = true;
				var value = gxdate.getTimeString(true, true, true);
				var serializedValue = uc.IsDashboardEdit() ? "&" + name : value;
				return uc.MakeVariable("Time", value, serializedValue, DataType.Character, false);
			}

			function PgmnameVariable(uc) {
				var name = "Pgmname";
				var value = uc.DashboardObject.Name;
				var serializedValue = uc.IsDashboardEdit() ? "&" + name : value;
				return uc.MakeVariable("Pgmname", value, serializedValue, DataType.Character, false);
			}

			function PgmdescVariable(uc) {
				var name = "Pgmdesc";
				var value = uc.DashboardObject.Description;
				var serializedValue = uc.IsDashboardEdit() ? "&" + name : value;
				return uc.MakeVariable("Pgmdesc", value, serializedValue, DataType.Character, false);
			}

			var variables = [];
			variables.push(TodayVariable(uc));
			variables.push(TimeVariable(uc));
			variables.push(PgmnameVariable(uc));
			variables.push(PgmdescVariable(uc));
			return variables;
		}

		dashboard.Variables = {};
		dashboard.Variables.Standard = StandardVariables(this);
		dashboard.Variables.AllParameters = this.ParametersToVariables(dashboard.Parameters);
	}

	this.LoadFilterVariables = function (dashboard) {

		dashboard.Variables.ValidParameters = this.ParametersToVariables(this.ValidParameters());
		for (var i = 0; i < dashboard.Filters.length; i++) {
			var filter = dashboard.Filters[i];
			//this.ProcessFilter(filter);
			if (!this.IsDashboardEdit())
				this.ReplaceVariablesInFilterOnlyParameters(filter, dashboard.Variables.AllParameters.concat(dashboard.Variables.Standard));
		}
		dashboard.Variables.Filters = this.FiltersToVariables(dashboard.Filters);
	}

	this.UpdateParameterVariables = function (dashboard) {
		dashboard.Variables.AllParameters = this.ParametersToVariables(dashboard.Parameters);
		dashboard.Variables.ValidParameters = this.ParametersToVariables(this.ValidParameters());
	}

	this.UpdateFilterVariables = function (dashboard, changedFilters) {

		function SearchFilterByAnyName(uc, dashboard, name) {
			var filterFound = false;
			var filterVariable = {};
			for (var i = 0; i < dashboard.Filters.length; i++) {
				filter = dashboard.Filters[i];
				switch (filter.Type) {
					case FilterType.Value:
						if (filter.Name.toLowerCase() == name.toLowerCase()) {
							filterFound = true;
							filterVariable = uc.MakeVariable(filter.Name, filter.Value, filter.SerializedValue, filter.DataType, false);
						}
						break;
					case FilterType.Range:
						if (filter.NameLowerValue.toLowerCase() == name.toLowerCase()) {
							filterFound = true;
							filterVariable = uc.MakeVariable(filter.NameLowerValue, filter.LowerValue, filter.SerializedLowerValue, filter.DataType, false);
						}
						else if (filter.NameUpperValue.toLowerCase() == name.toLowerCase()) {
							filterFound = true;
							filterVariable = uc.MakeVariable(filter.NameUpperValue, filter.UpperValue, filter.SerializedUpperValue, filter.DataType, false);
						}
						break;
					case FilterType.Collection:
						if (filter.Name.toLowerCase() == name.toLowerCase()) {
							filterFound = true;
							filterVariable = uc.MakeVariable(filter.Name, filter.Values, filter.SerializedValue, filter.DataType, true);
						}
						break;
				}
				if (filterFound)
					break;
			}
			return { Found: filterFound, Variable: filterVariable };
		}

		function GetVariableIndexByName(variables, name) {
			for (var i = 0; i < variables.length; i++) {
				var variable = variables[i];
				if (variable.Name.toLowerCase() == name.toLowerCase())
					return i;
			}
			return -1;
		}

		if (!changedFilters)
			dashboard.Variables.Filters = this.FiltersToVariables(dashboard.Filters);	// no se especificaron los filtros
		else {
			for (var i = 0; i < changedFilters.length; i++) {
				var variableIndex = GetVariableIndexByName(dashboard.Variables.Filters, changedFilters[i]);
				var searchResult = SearchFilterByAnyName(this, dashboard, changedFilters[i]);
				var filterFound = searchResult.Found;
				var newVariable = searchResult.Variable;
				if (!filterFound && variableIndex >= 0)
					dashboard.Variables.Filters.splice(variableIndex, 1);		// se eliminó el filtro (elimino la variable)
				else if (filterFound && variableIndex < 0)
					dashboard.Variables.Filters.push(newVariable);				// se agregó el filtro (agrego la variable)
				else if (filterFound && variableIndex >= 0)
					dashboard.Variables.Filters[variableIndex] = newVariable;	// se modificó el filtro (actualizo la variable)
			}
		}
	}

	this.FiltersToVariables = function (filters) {
		var variables = [];
		for (var i = 0; i < filters.length; i++) {
			var filter = filters[i];
			switch (filter.Type) {
				case FilterType.Value:
					variables.push(this.MakeVariable(filter.Name, filter.Value, filter.SerializedValue, filter.DataType, false));
					break;
				case FilterType.Range:
					variables.push(this.MakeVariable(filter.NameLowerValue, filter.LowerValue, filter.SerializedLowerValue, filter.DataType, false));
					variables.push(this.MakeVariable(filter.NameUpperValue, filter.UpperValue, filter.SerializedUpperValue, filter.DataType, false));
					break;
				case FilterType.Collection:
					variables.push(this.MakeVariable(filter.Name, filter.Values, filter.SerializedValue, filter.DataType, true));
					break;
			}
		}
		return variables;
	}

	this.ParametersToVariables = function (parameters) {
		var variables = [];
		for (var i = 0; i < parameters.length; i++) {
			var parameter = parameters[i];
			variables.push(this.MakeVariable(parameter.Name, parameter.Value, parameter.SerializedValue, parameter.DataType, parameter.Type == ParameterType.Collection));
		}
		return variables;
	}

	this.ReplaceVariablesValuesInText = function (text, variables) {
		if (text.indexOf("&") >= 0) {
			return this.ReplaceValuesInTextMain(variables, text);
		}
		else
			return text;
	}

	this.ReplaceValuesInTextMain = function (dictionary, text) {

		function SearchVariables(text, name, serializedValue, replaceInfo) {
			if (text.indexOf("&") >= 0) {
				var fin = false;
				var start = -1;
				while (!fin) {
					start = text.toLowerCase().indexOf("&" + name.toLowerCase(), start + 1);
					if (start >= 0) {
						var nextChar = text.substr(start + name.length + 1, 1);
						if (!((nextChar >= "a" && nextChar <= "z") || (nextChar >= "A" && nextChar <= "Z") || (nextChar >= "0" && nextChar <= "9") || nextChar == "_"))
							replaceInfo.push({ Pos: start, Name: name, SerializedValue: serializedValue });
					}
					else
						fin = true;
				}
			}
		}

		function comparePos(a, b) {
			if (a.Pos < b.Pos)
				return -1;
			if (a.Pos > b.Pos)
				return 1;
			return 0;
		}

		var replaceInfo = [];
		for (var i = 0; i < dictionary.length; i++) {
			var entry = dictionary[i];
			SearchVariables(text, entry.Name, entry.SerializedValue, replaceInfo);
		}

		replaceInfo.sort(comparePos);

		var newText = text;
		for (var i = replaceInfo.length - 1; i >= 0; i--)
			newText = newText.substr(0, replaceInfo[i].Pos) + replaceInfo[i].SerializedValue + newText.substr(replaceInfo[i].Pos + replaceInfo[i].Name.length + 1);
		return newText;

	}

	this.ReplaceVariableValue = function (value, validDataTypes, isCollection, variables) {
		var checkResult = this.CheckVariable(value, validDataTypes, isCollection, variables);
		if (checkResult.IsVariable)
			return checkResult.Value;
		else
			return value;
	}

	this.ReplaceVariableValueInFilter = function (value, valueProperty, serializedValueProperty, filter, variables) {
		var checkResult = this.CheckVariable(value, [filter.DataType], filter.Type == FilterType.Collection, variables);
		if (checkResult.IsVariable) {
			filter[valueProperty] = checkResult.Value;
			if (serializedValueProperty)
				filter[serializedValueProperty] = checkResult.SerializedValue;
		}
	}

	this.CheckVariable = function (varName, varValidDataTypes, varIsCollection, variables) {
		var isVariable = false;
		var variableName = "";
		var variableValue = null;
		var variableSerializedValue = null;
		if (typeof varName === "string" && varName.startsWith("&")) {
			var varNameAux = varName.substr(1, varName.length - 1).toLowerCase();
			for (var i = 0; i < variables.length; i++) {
				var variable = variables[i];
				if (variable.Name.toLowerCase() == varNameAux && varValidDataTypes.indexOf(variable.DataType) >= 0 && variable.IsCollection == varIsCollection) {
					isVariable = true;
					variableName = "&" + variable.Name;
					variableValue = variable.Value;
					variableSerializedValue = variable.SerializedValue;
					break;
				}
			}
		}
		return { IsVariable: isVariable, Name: variableName, Value: variableValue, SerializedValue: variableSerializedValue };
	}

	this.CheckVariableInEditMode = function (value, dataType, isCollection) {
		var checkResult;
		if (this.IsDashboardEdit()) {
			var dashboard = this.DashboardObject;
			var validDataTypes = [dataType];
			if (dataType == DataType.Real)
				validDataTypes.push(DataType.Integer);
			var checkResult = this.CheckVariable(value, validDataTypes, isCollection, dashboard.Variables.AllParameters.concat(dashboard.Variables.Standard));
		}
		else
			checkResult = { IsVariable: false };
		return checkResult;
	}

	this.ResolveVariable = function(value, dataType, isCollection) {
		var resolvedValue;
		var wasVariable;
		if (value != null) {
			var checkResult = this.CheckVariableInEditMode(value, dataType, isCollection);
			resolvedValue = checkResult.IsVariable ? this.GetEmptyValue(dataType, isCollection) : value;
			wasVariable = checkResult.IsVariable;
		}
		else {
			resolvedValue = value;
			wasVariable = false;
		}
		return {Value: resolvedValue, WasVariable: wasVariable};
	}

	this.ResolveVariableValue = function(value, dataType, isCollection) {
		var resolveObj = this.ResolveVariable(value, dataType, isCollection);
		return resolveObj.Value;
	}

	this.ValidParameters = function () {

		function ExistsFilter(dashboard, parName) {
			for (var i = 0; i < dashboard.Filters.length; i++) {
				var filter = dashboard.Filters[i];
				if (!filter.IsHidden) {
					var isRangeFilter = filter.Type == FilterType.Range;
					if ((!isRangeFilter && filter.Name.toLowerCase() == parName.toLowerCase()) || (isRangeFilter && (filter.NameLowerValue.toLowerCase() == parName.toLowerCase() || filter.NameUpperValue.toLowerCase() == parName.toLowerCase())))
						return true;
				}
			}
			return false;
		}

		if (!this._ValidParametersCalculated) {
			var dashboard = this.DashboardObject;
			this._ValidParameters = [];
			for (var i = 0; i < dashboard.Parameters.length; i++) {
				var parameter = dashboard.Parameters[i];
				if (!ExistsFilter(dashboard, parameter.Name) && this.GetObjectElementForFilter(parameter.Name) == null)
					this._ValidParameters.push(parameter);
			}
			this._ValidParametersCalculated = true;
		}
		return this._ValidParameters;
	}

	this.GetElementReferences = function (element) {

		function SearchElementParameterInFilters(dashboard, elementParameter) {
			var filterName = "";
			for (var i = 0; i < dashboard.Filters.length; i++) {
				var filter = dashboard.Filters[i];
				if (!filter.IsHidden) {
					if (filter.Type == FilterType.Range) {
						if (filter.NameLowerValue.toLowerCase() == elementParameter.toLowerCase()) {
							filterName = filter.NameLowerValue;
							break;
						}
						else if (filter.NameUpperValue.toLowerCase() == elementParameter.toLowerCase()) {
							filterName = filter.NameUpperValue;
							break;
						}
					}
					else {
						if (filter.Name.toLowerCase() == elementParameter.toLowerCase()) {
							filterName = filter.Name;
							break;
						}
					}
				}
			}
			return filterName;
		}

		function SearchElementParameterInObjectElements(uc, elementParameter) {
			var result = uc.GetObjectElementForFilter(elementParameter);
			if (result == null)
				return "";
			else
				return result.Element.Name;
		}

		function SearchElementParameterInParameters(dashboard, elementParameter) {
			var parName = "";
			for (var i = 0; i < dashboard.Parameters.length; i++) {
				var par = dashboard.Parameters[i];
				if (par.Name.toLowerCase() == elementParameter.toLowerCase()) {
					parName = par.Name;
					break;
				}
			}
			return parName;
		}

		var dashboard = this.DashboardObject;

		// Dependencias
		var dependenciesHTML = "";
		var filterDependencies = "";
		var objectElementDependencies = "";
		var parameterDependencies = "";
		var notResolvedDependencies = "";
		for (var i = 0; i < element.Parameters.length; i++) {
			var elementParameter = element.Parameters[i];

			// Busco entre los filtros
			var filterName = SearchElementParameterInFilters(dashboard, elementParameter);
			if (filterName != "")
				filterDependencies += (filterDependencies == "" ? "" : ", ") + filterName;
			else {
				// Busco entre los objetos con OnItemClick = ApplyFilters
				var objectElementName = SearchElementParameterInObjectElements(this, elementParameter);
				if (objectElementName != "")
					objectElementDependencies += (objectElementDependencies == "" ? "" : ", ") + objectElementName;
				else {
					// Busco entre los parámetros
					var parName = SearchElementParameterInParameters(dashboard, elementParameter);
					if (parName != "")
						parameterDependencies += (parameterDependencies == "" ? "" : ", ") + parName;
					else
						notResolvedDependencies += (notResolvedDependencies == "" ? "" : ", ") + elementParameter;
				}
			}
		}
		if (filterDependencies != "" || parameterDependencies != "" || objectElementDependencies != "") {
			dependenciesHTML += '<span class="gx-db-dependencies-title">' + gx.getMessage("GX_Dashboard_DependsOn") + ':</span>';
			if (filterDependencies != "")
				dependenciesHTML += '<br><span class="gx-db-dependencies-subtitle">' + gx.getMessage("GX_Dashboard_Filters") + ':</span>&nbsp' + filterDependencies;
			if (objectElementDependencies != "")
				dependenciesHTML += '<br><span class="gx-db-dependencies-subtitle">' + gx.getMessage("GX_Dashboard_ObjectElements") + ':</span>&nbsp' + objectElementDependencies;
			if (parameterDependencies != "")
				dependenciesHTML += '<br><span class="gx-db-dependencies-subtitle">' + gx.getMessage("GX_Dashboard_Parameters") + ':</span>&nbsp' + parameterDependencies;
		}
		if (notResolvedDependencies != "") {
			if (dependenciesHTML != "")
				dependenciesHTML += '<br>';
			dependenciesHTML += '<span class="gx-db-not-resolved-dependencies-title">' + gx.getMessage("GX_Dashboard_UnknownValues") + ':</span><br>' + notResolvedDependencies;
		}

		// Impacto
		var impactHTML = "";
		var impactedElements = "";
		if (element.ElementType == ElementType.Widget && ((element.WidgetType == WidgetType.Filter && !element.IsHidden) || (element.WidgetType == WidgetType.Object && element.OnItemClick == OnItemClick.ApplyFilters))) {
			for (var i = 0; i < dashboard.AllElements.length; i++) {
				var otherElement = dashboard.AllElements[i];
				if (element != otherElement && (otherElement.ElementType == ElementType.Widget || otherElement.ElementType == ElementType.Layout)) {
					var impacted;
					if (element.WidgetType == WidgetType.Filter)
						impacted = this.IsFilterRelated(otherElement, element);
					else if (element.WidgetType == WidgetType.Object)
						impacted = this.IsObjectRelated(otherElement, element);
					if (impacted)
						impactedElements += (impactedElements == "" ? "" : ", ") + (otherElement.ElementType == ElementType.Layout ? otherElement.ElementType : otherElement.ControlName);
				}
			}
			if (impactedElements != "")
				impactHTML += '<span class="gx-db-impact-title">' + gx.getMessage("GX_Dashboard_ImpactedElements") + ':</span><br>' + impactedElements;
		}

		return { DependenciesHTML: dependenciesHTML, ImpactHTML: impactHTML };
	}

	this.GetWidget = function (id) {
		var dashboard = this.DashboardObject;
		for (var i = 0; i < dashboard.AllWidgets.length; i++) {
			widget = dashboard.AllWidgets[i];
			if (widget.ElementId == id)
				return widget;
		}
		return null;
	}

	this.OldRadioButton = function () {
		return gx.plugdesign.definition.templates.indexOf('radio-button') < 0;
	}

	this.TwoRowsTableHTML = function (element, divId, height, width, html1, html2) {
		return this.editor.TwoRowsTableHTML(element, divId, height, width, html1, html2);
	}

	this.TwoCellsTableHTML = function (element, divId, height, width, html1, html2, switchPlaces) {
		var tableClass = this.editor.TableClass(element == null, false, "");
		var rowClass = this.editor.RowClass("");
		var bootstrapClasses3;
		var bootstrapClasses9;
		if (this.IsDashboardEdit()) {
			bootstrapClasses3 = "gx-db-flex-cell gx-db-flex-cell-3" + (switchPlaces ? " gx-db-flex-cell-order-2" : "");
			bootstrapClasses9 = "gx-db-flex-cell gx-db-flex-cell-9" + (switchPlaces ? " gx-db-flex-cell-order-1" : "");
		}
		else {
			bootstrapClasses3 = "col-xs-12 col-sm-3" + (switchPlaces ? " col-sm-push-9" : "");
			bootstrapClasses9 = "col-xs-12 col-sm-9" + (switchPlaces ? " col-sm-pull-3" : "");
		}
		var cellClass1 = this.editor.CellClass(bootstrapClasses3, false, false, "");
		var cellClass2 = this.editor.CellClass(bootstrapClasses9, false, false, "");
		var tableStyle = 'height:' + height + ';width:' + width;
		var html = this.editor.DIVStart(element, divId, tableClass, tableStyle, false, true, false);
		html += '<div class="' + rowClass + '" style="height:100%">';
		html += '<div class="' + cellClass1 + '" style="height:100%">' + html1 + '</div>';
		html += '<div class="' + cellClass2 + '" style="height:100%">' + html2 + '</div>';
		html += '</div>';
		html += '</div>';
		return html;
	}

	this.GetLayoutHTML = function (dashboard) {

		function GetFiltersAndMainTableHtml(uc, includeFilters, filtersPosition, element, divId, htmlFiltersTable, htmlMainTable) {
			if (includeFilters || uc.IsDashboardEdit())
				switch (filtersPosition) {
					case FiltersPosition.Top:
						return uc.TwoRowsTableHTML(element, divId, "100%", "100%", htmlFiltersTable, htmlMainTable);
					case FiltersPosition.Right:
						return uc.TwoCellsTableHTML(element, divId, "100%", "100%", htmlFiltersTable, htmlMainTable, true);
					case FiltersPosition.Left:
						return uc.TwoCellsTableHTML(element, divId, "100%", "100%", htmlFiltersTable, htmlMainTable, false);
				}
			else
				return htmlMainTable;
		}

		var divId = this.editor.GetDOMElementId(dashboard.Layout.ElementId);
		var mainTableIds = [];
		var filtersTableIds = [];
		var htmlMainTable = this.editor.GetTableHTML(dashboard, dashboard.Layout.MainTable, false, mainTableIds, true);
		var htmlFiltersTable = this.editor.GetTableHTML(dashboard, dashboard.Layout.FiltersTable, false, filtersTableIds, true);
		this.ReplaceDashboardParametersInLayout(dashboard.Layout);
		var html;
		if (dashboard.Layout.Title != "") {
			var contentHtml = GetFiltersAndMainTableHtml(this, dashboard.Filters.length > 0, dashboard.Layout.FiltersPosition, null, "", htmlFiltersTable, htmlMainTable);
			var title = this.ReplaceVariablesValuesInText(dashboard.Layout.Title, dashboard.Variables.Filters);
			var titleHtml = this.CenteredTextHTML("gx-db-title-text", "gx-db-title", title);
			html = this.TwoRowsTableHTML(dashboard.Layout, divId, "100%", "100%", titleHtml, contentHtml);
		}
		else
			html = GetFiltersAndMainTableHtml(this, dashboard.Filters.length > 0, dashboard.Layout.FiltersPosition, dashboard.Layout, divId, htmlFiltersTable, htmlMainTable);
		return html;
	}

	this.GetWidgetContentHTML = function (dashboard, widget) {
		var htmlWidget;
		this.ReplaceDashboardParametersInWidget(widget);
		switch (widget.WidgetType) {
			case WidgetType.Object:
				htmlWidget = this.GetObjectWidgetHTML(widget);
				break;
			case WidgetType.Filter:
				htmlWidget = this.GetFilterWidgetHTML(widget, dashboard.Variables.Filters);
				break;
			case WidgetType.TextBlock:
				htmlWidget = this.GetTextBlockWidgetHTML(widget, dashboard.Variables.Filters);
				break;
			case WidgetType.Button:
				htmlWidget = this.GetButtonWidgetHTML(widget, dashboard.Variables.Filters);
				break;
			case WidgetType.Image:
				htmlWidget = this.GetImageWidgetHTML(widget, dashboard.Variables.Filters);
				break;
			case WidgetType.Blank:
				htmlWidget = "";
				break;
			default:
				htmlWidget = "Not implemented";
				break;
		}
		return htmlWidget;
	}

	this.GetObjectMenuHTML = function (widget) {
		var html = '';
		if (widget.WidgetType == WidgetType.Object) {
			var dashboard = this.DashboardObject;
			var topStr = !this.WidgetsExpanded ? "calc((100% - 33px) / 2)" : "2px";
			html += '<div style="position:absolute;right:2px;top:' + topStr + '" class="dropdown"><button class="gx-db-object-menu btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"><span class="gx-db-object-menu-icon glyphicon glyphicon-menu-hamburger"></span></button>';
			var alignmentClass = (dashboard.Layout.FiltersPosition == FiltersPosition.Right ? "" : " dropdown-menu-right");
			html += '<ul class="dropdown-menu' + alignmentClass + '">';
			for (var i = 0; i < widget.Elements.length; i++) {
				var elementName = widget.Elements[i].Name;
				html += '<li><a href="#" onclick="' + this.me() + '.editor.SelectObjectItem(event, \'' + widget.ElementId + '\', \'' + elementName + '\')">' + elementName + '</a></li>';
			}
			html += '</ul>';
			html += '</div>';
		}
		return html;
	}

	this.GetObjectWidgetHTML = function (widget) {
		var f = this.me() + '.RenderQueryViewer(\'' + widget.ElementId + '\');';
		setTimeout(f, 0);
		var html = '';
		html += '<div id="' + this.GetQueryViewerElementId(widget.ElementId) + '"></div>';
		if (this.IsDashboardEdit())
			html += this.GetObjectMenuHTML(widget);
		return html;
	}

	this.GetTextBlockWidgetHTML = function (textBlock, variables) {
		var caption = this.ReplaceVariablesValuesInText(textBlock.Caption, variables);
		var cls = textBlock.Class == "" ? "gx-db-textblock" : textBlock.Class;
		var html = '<span class="' + cls + '">' + caption + '</span>';
		return html;
	}

	this.GetButtonWidgetHTML = function (button, variables) {
		var caption = this.ReplaceVariablesValuesInText(button.Caption, variables);
		var cls = button.Class == "" ? "gx-db-button" : button.Class;
		var html = '<input type="button" class="btn btn-default ' + cls + '" value="' + caption + '"></button>';
		return html;
	}

	this.GetImageWidgetHTML = function (image, variables) {
	
		function GetLanguageName(languageCode) {
			switch (languageCode) {
				case "eng":
					return "English";
				case "spa":
					return "Spanish";
				case "ita":
					return "Italian";
				case "por":
					return "Portuguese";
				case "jap":
					return "Japanese";
				case "chs":
					return "SimplifiedChinese";
				case "cht":
					return "TraditionalChinese";
				default:
					return qv.util.capitalize(languageCode);
			}
		}
		
		var altText = this.ReplaceVariablesValuesInText(image.AlternateText, variables);
		var key = (image.LanguageDependant ? GetLanguageName(gx.languageCode) : "") + "_" + (image.ThemeDependant ? (this.IsDashboardEdit() ? this.DefaultStyle : gx.theme) : "");
		var src = "Resources/" + image.Sources[key];
		var altStr = altText != "" ? ' alt="' + altText + '"' : '';
		var cls = image.Class == "" ? "gx-db-image" : image.Class;
		var html = '<img class="' + cls + '" src="' + src + '"' + altStr + '>';
		return html;
	}

	this.GetFilterWidgetHTML = function (filter, variables) {

		function GetFilterCaptionHTML(uc, filter, variables, additionalClasses, inputId) {
			if (filter.Caption != "") {
				var captionCls = filter.CaptionClass == "" ? "gx-db-filter-caption" : filter.CaptionClass;
				if (additionalClasses != "")
					captionCls += " " + additionalClasses;
				var forInputIdStr = inputId == '' ? '' : ' for="' + inputId + '"';
				return '<label class="' + captionCls + '"' + forInputIdStr + '>' + uc.ReplaceVariablesValuesInText(filter.Caption, variables) + '</label>';
			}
			else
				return "";
		}

		function NeedToRefreshValues(filter) {
			return filter.Control.Dynamic && (filter.Control.RefreshValues || !filter.Control.ValuesLoaded);
		}

		function RenderEditboxFilter(uc, filter, variables) {
			var html = '';
			html += GetFilterCaptionHTML(uc, filter, variables, "", "");
			var valueCls = filter.ValueClass == "" ? "gx-db-filter-value" : filter.ValueClass;
			var inviteMessage = uc.ReplaceVariablesValuesInText(filter.Control.InviteMessage, variables);
			var inviteMessageAssign = inviteMessage != "" ? ' placeholder="' + inviteMessage + '"' : '';
			var valueStr;
			switch (filter.Type) {
				case FilterType.Value:
					valueStr = filter.SerializedValue;
					break;
				case FilterType.Range:
					valueStr = filter.SerializedLowerValue + " - " + filter.SerializedUpperValue;
					break;
				case FilterType.Collection:
					valueStr = filter.SerializedValue.replace(/"/g, "&quot;");
					break;
			}
			var onFocusOnBlurFunctions = "";
			if (uc.IsDashboardEdit()) {
				// Los Input dentro de un DIV draggable pierden funcionalidad
				// Workaround: http://stackoverflow.com/questions/21680363/prevent-drag-event-to-interfere-with-input-elements-in-firefox-using-html5-drag
				onFocusOnBlurFunctions = ' onfocus="' + uc.me() + '.editor.RemoveDraggableAttributes(\'' + filter.ElementId + '\')" onblur="' + uc.me() + '.editor.RestoreDraggableAttributes()"';
			}
			var onChangeFunction;
			if (filter.Type == FilterType.Range)
				onChangeFunction = 'SplitAndUpdateFilter2(this, \'' + filter.NameLowerValue + '\', \'' + filter.NameUpperValue + '\', this.value)"';
			else
				onChangeFunction = 'UpdateFilter1(this, \'' + filter.Name + '\', this.value)"';
			html += '<input type="text" class="' + valueCls + ' form-control" value="' + valueStr + '"' + inviteMessageAssign + ' onchange="' + uc.me() + '.' + onChangeFunction + onFocusOnBlurFunctions + '>';
			return html;
		}

		function RenderSliderFilter(uc, filter, variables) {
		
			function DoRenderSliderFilter() {
				var f = uc.me() + '.CreateSlider(\'' + filter.ElementId + '\');';
				if (filter._sliderTimeout)
					clearTimeout(filter._sliderTimeout);
				filter._sliderTimeout = setTimeout(f, 0);
			}
			
			function SliderFilterScripts () {
				var staticDir = qv.services.url.getStaticDirectory();
				return [staticDir + "DashboardViewer/noUiSlider/nouislider.min.js"];
			}
			
			var html = '';
			html += GetFilterCaptionHTML(uc, filter, variables, "", "");
			html += '<div id="' + uc.GetRangeElementId(filter.ElementId) + '"></div><div style="height:20px"></div>';
			gx.http.loadScripts(SliderFilterScripts(), DoRenderSliderFilter);
			return html;
		}

		function RenderCalendarFilter(uc, filter, variables) {
		
			function DoRenderCalendarFilter() {
				var f = uc.me() + '.CreateDatePicker(\'' + filter.ElementId + '\');';
				if (filter._calendarTimeout)
					clearTimeout(filter._calendarTimeout);
				filter._calendarTimeout = setTimeout(f, 0);
			}
			
			function CalendarFilterScripts () {
				var staticDir = qv.services.url.getStaticDirectory();
				return [staticDir + "DashboardViewer/DateRangePicker/moment.min.js", staticDir + "DashboardViewer/DateRangePicker/daterangepicker.js"];
			}
			
			var useDateRangePicker = !(filter.Type == FilterType.Value && gx.util.browser.isSmartDevice());
			var html = '';
			html += GetFilterCaptionHTML(uc, filter, variables, "", "");
			var valueCls = filter.ValueClass == "" ? "gx-db-filter-value" : filter.ValueClass;
			var valueStr;
			if (filter.Type == FilterType.Range) {
				valueStr = '';
				var checkResult1 = uc.CheckVariableInEditMode(filter.LowerValue, filter.DataType, false);
				var checkResult2 = uc.CheckVariableInEditMode(filter.UpperValue, filter.DataType, false);
				if (checkResult1.IsVariable || checkResult2.IsVariable || !gx.date.isNullDate(filter.LowerValue) || !gx.date.isNullDate(filter.UpperValue)) {
					if (checkResult1.IsVariable)
						valueStr += checkResult1.SerializedValue;
					else
						valueStr += gx.date.isNullDate(filter.LowerValue) ? '' : uc.ValueToString(filter.LowerValue, filter.DataType, filter.Picture, filter.PictureProperties);
					valueStr += ' - ';
					if (checkResult2.IsVariable)
						valueStr += checkResult2.SerializedValue;
					else
						valueStr += gx.date.isNullDate(filter.UpperValue) ? '' : uc.ValueToString(filter.UpperValue, filter.DataType, filter.Picture, filter.PictureProperties);
					valueStr = ' value="' + valueStr + '"';
				}
			}
			else {
				var checkResult = uc.CheckVariableInEditMode(filter.Value, filter.DataType, false);
				if (checkResult.IsVariable)
					valueStr = ' value="' + checkResult.SerializedValue + '"';
				else
					valueStr = gx.date.isNullDate(filter.Value) ? '' : ' value="' + (useDateRangePicker ? uc.ValueToString(filter.Value, filter.DataType, filter.Picture, filter.PictureProperties) : uc.ValueToInternalString(filter.Value, filter.DataType, filter.Picture, filter.PictureProperties).replace(' ', 'T')) + '"';
			}
			var onChangeFunction;
			if (filter.Type == FilterType.Range)
				onChangeFunction = 'SplitAndUpdateFilter2(this, \'' + filter.NameLowerValue + '\', \'' + filter.NameUpperValue + '\', this.value)"';
			else if (useDateRangePicker)
				onChangeFunction = 'UpdateFilter1(this, \'' + filter.Name + '\', this.value)"';
			else
				onChangeFunction = 'UpdateFilter1Internal(this, \'' + filter.Name + '\', this.value)"';
			if (useDateRangePicker) {
				var mobileOption = gx.util.browser.isSmartDevice() ? ' onfocus="blur()"' : '';	// avoid showing the virtual keyboard in mobile
				html += '<input id="' + uc.GetRangeElementId(filter.ElementId) + '" type="text" class="' + valueCls + ' form-control"' + valueStr + ' onchange="' + uc.me() + '.' + onChangeFunction + mobileOption + '>';
				gx.http.loadScripts(CalendarFilterScripts(), DoRenderCalendarFilter);
			}
			else {
				var minValueStr = filter.MinValue != null ? ' min="' + uc.ValueToInternalString(filter.MinValue, filter.DataType, filter.Picture, filter.PictureProperties).replace(' ', 'T') + '"' : '';
				var maxValueStr = filter.MaxValue != null ? ' max="' + uc.ValueToInternalString(filter.MaxValue, filter.DataType, filter.Picture, filter.PictureProperties).replace(' ', 'T') + '"' : '';
				html += '<input type="' + (filter.DataType == DataType.Date ? "date" : "datetime-local") + '" class="' + valueCls + ' form-control"' + valueStr + minValueStr + maxValueStr + ' onchange="' + uc.me() + '.' + onChangeFunction + '>';
			}
			return html;
		}

		function RenderRadioButtonFilter(uc, filter, variables) {
			if (uc.OldRadioButton())
				return RenderOldRadioButtonFilter(uc, filter, variables);
			else
				return RenderNewRadioButtonFilter(uc, filter, variables);
		}

		function RenderNewRadioButtonFilter(uc, filter, variables) {

			function ValueHTML(uc, strValue, description, active, centerAlign, filter, valueCls) {
				var activeStr = active ? " active" : "";
				var alignCenterStr = centerAlign ? ' style="text-align:center"' : '';
				var html = '<div class="btn-group"><button type="button" class="' + valueCls + ' btn btn-default' + activeStr + '"' + alignCenterStr + ' onclick="' + uc.me() + '.UpdateFilter1(this, \'' + filter.Name + '\', \'' + strValue + '\')">' + description + '</button></div>';
				return html;
			}

			var valueCls = filter.ValueClass == "" ? "gx-db-filter-value" : filter.ValueClass;
			var html = '';
			html += GetFilterCaptionHTML(uc, filter, variables, "", "");
			var centerAlign = filter.Control.Orientation == ControlOrientation.Vertical;
			if (filter.Control.Orientation == ControlOrientation.Horizontal)
				html += '<div class="btn-group btn-group-justified">';
			else
				html += '<div class="btn-group-vertical" style="width:100%">';
			if (NeedToRefreshValues(filter)) {
				var loadingMsg = gx.getMessage("GX_Dashboard_Loading");
				html += ValueHTML(uc, loadingMsg, loadingMsg, false, centerAlign, filter, valueCls);
			}
			else {
				if (filter.Control.EmptyItem) {
					var value = uc.GetEmptyValue(filter.DataType, false);
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), filter.Control.EmptyItemText, uc.SameValue(value, filter.Value, filter.DataType), centerAlign, filter, valueCls);
				}
				for (var i = 0; i < filter.Control.Values.length; i++) {
					var value = filter.Control.Values[i];
					var description = filter.Control.Descriptions[i];
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), description, uc.SameValue(value, filter.Value, filter.DataType), centerAlign, filter, valueCls);
				}
			}
			html += '</div>';
			return html;
		}

		function RenderComboBoxFilter(uc, filter, variables) {

			function ValueHTML(uc, strValue, description, selected, filter) {
				var selectedStr = selected ? ' selected' : '';
				var html = '<option value="' + strValue + '"' + selectedStr + '>' + description + '</option>';
				return html;
			}

			var html = '';
			html += GetFilterCaptionHTML(uc, filter, variables, "", "");
			var valueCls = filter.ValueClass == "" ? "gx-db-filter-value" : filter.ValueClass;
			var needToRefreshValues = NeedToRefreshValues(filter);
			html += '<select class="' + valueCls + ' form-control" onchange="' + uc.me() + '.UpdateFilter1(this, \'' + filter.Name + '\', this.value)">';
			if (needToRefreshValues) {
				var loadingMsg = gx.getMessage("GX_Dashboard_Loading");
				html += ValueHTML(uc, loadingMsg, loadingMsg, false, filter);
			}
			else {
				if (filter.Control.EmptyItem) {
					var value = uc.GetEmptyValue(filter.DataType, false);
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), filter.Control.EmptyItemText, uc.SameValue(value, filter.Value, filter.DataType), filter);
				}
				for (var i = 0; i < filter.Control.Values.length; i++) {
					var value = filter.Control.Values[i];
					var description = filter.Control.Descriptions[i];
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), description, uc.SameValue(value, filter.Value, filter.DataType), filter);
				}
			}
			html += '</select>';
			return html;
		}

		function RenderDropDownListFilter(uc, filter, variables) {

			function DoRenderDropDownListFilter() {
				var f = uc.me() + '.CreateDropDownList(\'' + filter.ElementId + '\');';
				if (filter._dropDownListTimeout)
					clearTimeout(filter._dropDownListTimeout);
				filter._dropDownListTimeout = setTimeout(f, 0);
			}
			
			function DropDownListFilterScripts () {
				var staticDir = qv.services.url.getStaticDirectory();
				return [staticDir + "DashboardViewer/bootstrap-select/bootstrap-select.min.js"];
			}
			
			function ValueHTML(uc, strValue, description, selected, filter, valueCls) {
				var selectedStr = selected ? ' selected' : '';
				var html = '<option class="' + valueCls + '" value="' + strValue + '"' + selectedStr + '>' + description + '</option>';
				return html;
			}

			var html = '';
			html += GetFilterCaptionHTML(uc, filter, variables, "", "");
			var valueCls = filter.ValueClass == "" ? "gx-db-filter-value" : filter.ValueClass;
			var needToRefreshValues = NeedToRefreshValues(filter);
			var loadingMsg = needToRefreshValues ? ' title="' + gx.getMessage("GX_Dashboard_Loading") + '"' : '';
			var messages = ' data-select-all-text="' + gx.getMessage("GX_Dashboard_SelectAll") + '" data-deselect-all-text="' + gx.getMessage("GX_Dashboard_DeselectAll") + '" data-none-selected-text="' + gx.getMessage("GX_Dashboard_NothingSelected") + '" data-count-selected-text="' + gx.getMessage("GX_Dashboard_ItemsSelectedQuantity") + '" data-none-results-text="' + gx.getMessage("GX_Dashboard_NoResultsMatch") + '" data-multiple-separator=", "';
			var mobileOption = gx.util.browser.isSmartDevice() ? ' data-mobile="true"' : '';
			html += '<select data-style="' + valueCls + '" class="selectpicker form-control" multiple' + mobileOption + ' data-icon-base = "material-icons" data-tick-icon = "" data-selected-text-format="count > 3" data-actions-box="true" data-live-search="true" data-live-search-placeholder="' + gx.getMessage("GX_Dashboard_SearchFilter") + '" onchange="' + uc.me() + '.UpdateDropDownListFilter(this, \'' + filter.Name + '\')"' + loadingMsg + messages + '>';
			if (!needToRefreshValues) {
				if (filter.Control.EmptyItem) {
					var value = uc.GetEmptyValue(filter.DataType, false);
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), filter.Control.EmptyItemText, uc.ValuesIndexOf(filter.Values, value, filter.DataType) >= 0, filter, valueCls);
				}
				var sdClass = gx.util.browser.isSmartDevice() ? ' gx-db-sd' : '';
				for (var i = 0; i < filter.Control.Values.length; i++) {
					var value = filter.Control.Values[i];
					var description = filter.Control.Descriptions[i];
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), description, uc.ValuesIndexOf(filter.Values, value, filter.DataType) >= 0, filter, valueCls + sdClass);
				}
			}
			html += '</select>';
			gx.http.loadScripts(DropDownListFilterScripts(), DoRenderDropDownListFilter);
			return html;
		}

		function RenderOldRadioButtonFilter(uc, filter, variables) {

			function ValueHTML(uc, strValue, description, checked, filter, valueCls) {
				var html = '';
				var checkedStr = checked ? "checked " : "";
				var inputStr = '<input type="radio" name="optradio_' + filter.ControlName + '" onclick="' + uc.me() + '.UpdateFilter1(this, \'' + filter.Name + '\', \'' + strValue + '\')"' + checkedStr + '>';
				if (filter.Control.Orientation == ControlOrientation.Horizontal)
					html = '<label class="' + valueCls + ' radio-inline">' + inputStr + description + '</label>';
				else
					html = '<div class="radio"><label class="' + valueCls + '">' + inputStr + description + '</label></div>';
				return html;
			}

			var html = '';
			html += GetFilterCaptionHTML(uc, filter, variables, "", "");
			var valueCls = filter.ValueClass == "" ? "gx-db-filter-value" : filter.ValueClass;
			if (filter.Caption != "" && filter.Control.Orientation == ControlOrientation.Horizontal)
				html += '<p>';
			if (NeedToRefreshValues(filter)) {
				var loadingMsg = gx.getMessage("GX_Dashboard_Loading");
				html += ValueHTML(uc, loadingMsg, loadingMsg, false, filter, valueCls);
			}
			else {
				if (filter.Control.EmptyItem) {
					var value = uc.GetEmptyValue(filter.DataType, false);
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), filter.Control.EmptyItemText, uc.SameValue(value, filter.Value, filter.DataType), filter, valueCls);
				}
				for (var i = 0; i < filter.Control.Values.length; i++) {
					var value = filter.Control.Values[i];
					var description = filter.Control.Descriptions[i];
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), description, uc.SameValue(value, filter.Value, filter.DataType), filter, valueCls);
				}
			}
			if (filter.Caption != "" && filter.Control.Orientation == ControlOrientation.Horizontal)
				html += '</p>';
			return html;
		}

		function RenderMultipleCheckboxesFilter(uc, filter, variables) {

			function ValueHTML(uc, strValue, description, checked, filter, valueCls) {
				var html = "";
				var checkedStr = checked ? "checked " : "";
				var classStr = gx.util.browser.isSmartDevice() ? 'class="gx-db-sd" ' : '';
				var inputId = uc.GetRangeElementId(filter.ElementId) + "_" + strValue;
				var inputStr = '<input id="' + inputId + '" type="checkbox" ' + classStr + checkedStr + 'onchange="' + uc.me() + '.UpdateFilterValue(this, \'' + filter.Name + '\', \'' + strValue + '\', this.checked)">';
				var iconStr = '<i class="material-icons" onclick="document.getElementById(\'' + inputId + '\').checked = this.textContent == \'check_box_outline_blank\';' + uc.me() + '.UpdateFilterValue(this, \'' + filter.Name + '\', \'' + strValue + '\', this.textContent == \'check_box_outline_blank\')">' + (checked ? 'check_box' : 'check_box_outline_blank') + '</i>';
				if (filter.Control.Orientation == ControlOrientation.Horizontal)
					html = inputStr + iconStr + '<label class="' + valueCls + ' checkbox-inline" for="' + inputId + '">' + description + '</label>';
				else
					html = '<div class="checkbox">' + inputStr + iconStr + '<label class="' + valueCls + '" for="' + inputId + '">' + description + '</label></div>';
				return html;
			}

			var html = '';
			html += GetFilterCaptionHTML(uc, filter, variables, "", "");
			var valueCls = filter.ValueClass == "" ? "gx-db-filter-value" : filter.ValueClass;
			if (filter.Caption != "" && filter.Control.Orientation == ControlOrientation.Horizontal)
				html += '<p>';
			if (NeedToRefreshValues(filter)) {
				var loadingMsg = gx.getMessage("GX_Dashboard_Loading");
				html += ValueHTML(uc, loadingMsg, loadingMsg, false, filter, valueCls);
			}
			else {
				if (filter.Control.EmptyItem) {
					var value = uc.GetEmptyValue(filter.DataType, false);
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), filter.Control.EmptyItemText, uc.ValuesIndexOf(filter.Values, value, filter.DataType) >= 0, filter, valueCls);
				}
				for (var i = 0; i < filter.Control.Values.length; i++) {
					var value = filter.Control.Values[i];
					var description = filter.Control.Descriptions[i];
					html += ValueHTML(uc, uc.ValueToString(value, filter.DataType, filter.Picture, filter.PictureProperties), description, uc.ValuesIndexOf(filter.Values, value, filter.DataType) >= 0, filter, valueCls);
				}
			}
			if (filter.Caption != "" && filter.Control.Orientation == ControlOrientation.Horizontal)
				html += '</p>';
			return html;
		}

		function RenderSingleCheckboxFilter(uc, filter, variables) {
			// for Boolean DataType
			var html = '';
			var checkedStr = filter.Value ? "checked " : "";
			var classStr = gx.util.browser.isSmartDevice() ? 'class="gx-db-sd" ' : '';
			var inputId = uc.GetRangeElementId(filter.ElementId);
			var inputStr = '<input id="' + inputId + '" type="checkbox" ' + classStr + checkedStr + 'onchange="' + uc.me() + '.UpdateFilter1(this, \'' + filter.Name + '\', this.checked.toString())">';
			html += inputStr;
			var iconStr = '<i class="material-icons" onclick="document.getElementById(\'' + inputId + '\').checked = this.textContent == \'check_box_outline_blank\';' + uc.me() + '.UpdateFilter1(this, \'' + filter.Name + '\', this.textContent == \'check_box_outline_blank\')">' + (filter.Value ? 'check_box' : 'check_box_outline_blank') + '</i>';
			html += iconStr;
			html += GetFilterCaptionHTML(uc, filter, variables, "checkbox-inline", inputId);
			return html;
		}

		function RenderSwitchFilter(uc, filter, variables) {
		
			function DoRenderSwitchFilter() {
				var f = uc.me() + '.CreateSwitch(\'' + filter.ElementId + '\');';
				if (filter._switchTimeout)
					clearTimeout(filter._switchTimeout);
				filter._switchTimeout = setTimeout(f, 0);
			}
			
			function SwitchFilterScripts () {
				var staticDir = qv.services.url.getStaticDirectory();
				return [staticDir + "DashboardViewer/bootstrap-switch/bootstrap-switch.min.js"];
			}

			var html = '';
			html += GetFilterCaptionHTML(uc, filter, variables, "gx-db-switch-label", "");
			html += '<input type="checkbox" id="' + uc.GetRangeElementId(filter.ElementId) + '">';
			gx.http.loadScripts(SwitchFilterScripts(), DoRenderSwitchFilter);
			return html;
		}

		function RenderIntegerFilter(uc, filter, variables) {
			switch (filter.Control.Type) {
				case ControlType.Editbox:
					return RenderEditboxFilter(uc, filter, variables);
				case ControlType.RadioButton:
					return RenderRadioButtonFilter(uc, filter, variables);
				case ControlType.ComboBox:
					return RenderComboBoxFilter(uc, filter, variables);
				case ControlType.Slider:
					return RenderSliderFilter(uc, filter, variables);
				default:
					return RenderEditboxFilter(uc, filter, variables);
			}
		}

		function RenderRealFilter(uc, filter, variables) {
			return RenderIntegerFilter(uc, filter, variables);
		}

		function RenderCharacterFilter(uc, filter, variables) {
			switch (filter.Control.Type) {
				case ControlType.Editbox:
					return RenderEditboxFilter(uc, filter, variables);
				case ControlType.RadioButton:
					return RenderRadioButtonFilter(uc, filter, variables);
				case ControlType.ComboBox:
					return RenderComboBoxFilter(uc, filter, variables);
				default:
					return RenderEditboxFilter(uc, filter, variables);
			}
		}

		function RenderBooleanFilter(uc, filter, variables) {
			switch (filter.Control.Type) {
				case ControlType.Editbox:
					return RenderEditboxFilter(uc, filter, variables);
				case ControlType.Checkbox:
					return RenderSingleCheckboxFilter(uc, filter, variables);
				case ControlType.RadioButton:
					return RenderRadioButtonFilter(uc, filter, variables);
				case ControlType.ComboBox:
					return RenderComboBoxFilter(uc, filter, variables);
				case ControlType.Switch:
					return RenderSwitchFilter(uc, filter, variables);
				default:
					return RenderSingleCheckboxFilter(uc, filter, variables);
			}
		}

		function RenderDateFilter(uc, filter, variables) {
			switch (filter.Control.Type) {
				case ControlType.Editbox:
					return RenderCalendarFilter(uc, filter, variables);
				case ControlType.RadioButton:
					return RenderRadioButtonFilter(uc, filter, variables);
				case ControlType.ComboBox:
					return RenderComboBoxFilter(uc, filter, variables);
				case ControlType.Slider:
					return RenderSliderFilter(uc, filter, variables);
				default:
					return RenderEditboxFilter(uc, filter, variables);
			}
		}

		function RenderDateTimeFilter(uc, filter, variables) {
			return RenderDateFilter(uc, filter, variables);
		}

		function RenderGUIDFilter(uc, filter, variables) {
			return RenderCharacterFilter(uc, filter, variables);
		}

		function RenderGeoPointFilter(uc, filter, variables) {
			return RenderCharacterFilter(uc, filter, variables);
		}

		function RenderInvisibleFilter(uc, filter) {
			var valueStr;
			if (filter.Type == FilterType.Range)
				valueStr = filter.NameLowerValue + " = " + filter.SerializedLowerValue + "; " + filter.NameUpperValue + " = " + filter.SerializedUpperValue;
			else
				valueStr = filter.Name + " = " + filter.SerializedValue;
			return valueStr;
		}

		if (filter.Visible) {
			if (NeedToRefreshValues(filter)) {
				var dBoard = this;
				var queryName = this.QueryFilterName(filter);
				var LoadControlValuesFn = qv.services.createAsyncServerCallFn(this.FakeQueryViewer(queryName), qv.services.url.getDataURL, qv.services.postInfo.Data, "GetData");
				LoadControlValuesFn(function (resText) {
					dBoard.ControlValuesLoaded(filter, resText);
				});
			}

			if (filter.Type == FilterType.Collection)
				switch (filter.Control.Type) {
					case ControlType.Editbox:
						return RenderEditboxFilter(this, filter, variables);
					case ControlType.Checkbox:
						return RenderMultipleCheckboxesFilter(this, filter, variables);
					case ControlType.DropDownList:
						return RenderDropDownListFilter(this, filter, variables);
					default:
						return RenderMultipleCheckboxesFilter(this, filter, variables);
				}
			else
				switch (filter.DataType) {
					case DataType.Integer:
						return RenderIntegerFilter(this, filter, variables);
					case DataType.Real:
						return RenderRealFilter(this, filter, variables);
					case DataType.Character:
						return RenderCharacterFilter(this, filter, variables);
					case DataType.Boolean:
						return RenderBooleanFilter(this, filter, variables);
					case DataType.Date:
						return RenderDateFilter(this, filter, variables);
					case DataType.DateTime:
						return RenderDateTimeFilter(this, filter, variables);
					case DataType.GUID:
						return RenderGUIDFilter(this, filter, variables);
					case DataType.GeoPoint:
						return RenderGeoPointFilter(this, filter, variables);
					default:
						return "Not implemented";
				}
		}
		else
			return RenderInvisibleFilter(this, filter);
	}

	this.ControlValuesLoaded = function (filter, resText) {
		var qViewer = { xml: { data: resText } };
		if (!qv.util.anyError(resText))
			qv.services.parseDataXML(qViewer);
		else {
			qViewer.Data = { Rows: [] };
			var errMsg = qv.util.getErrorFromText(resText);
			this.ShowError(filter.ElementId, errMsg, false);
		}
		filter.Control.RefreshValues = false;
		filter.Control.ValuesLoaded = true;
		filter.Control.Values = [];
		filter.Control.Descriptions = [];
		for (var j = 0; j < qViewer.Data.Rows.length; j++) {
			var row = qViewer.Data.Rows[j];
			var value = qv.util.trim(row["F1"]);
			var description = qv.util.trim(row["F2"]);
			if (value != null && description != null) {
				var parserValueResult = this.ParseInternalValue(value, filter.DataType, false);
				var parserDescriptionResult = this.ParseInternalValue(description, filter.Control.DscAttDataType, false);
				if (parserValueResult.Success && parserDescriptionResult.Success) {
					filter.Control.Values.push(parserValueResult.ParsedValue);
					var picture = this.DefaultPicture(filter.Control.DscAttDataType);
					var pictureProperties = this.GetPictureProperties(filter.Control.DscAttDataType, picture);
					filter.Control.Descriptions.push(this.ValueToString(parserDescriptionResult.ParsedValue, filter.Control.DscAttDataType, picture, pictureProperties));
				}
			}
		}
		if (this.ValuesIndexOf(filter.Control.Values, filter.Value, filter.DataType) < 0 && filter.Control.Values.length > 0)
			filter.Value = filter.Control.EmptyItem ? this.GetEmptyValue(filter.DataType, false) : filter.Control.Values[0];
		this.RefreshWidget(filter);
	}

	this.CreateDatePicker = function (filterId) {

		function ConvertDateFormat(pictureProperties) {
			var dateFmt;
			if (pictureProperties.DateFormat.indexOf('4') >= 0)
				dateFmt = pictureProperties.DateFormat.replace('D', 'DD/').replace('M', 'MM/').replace('Y4', 'YYYY/').substr(0, 10);
			else
				dateFmt = pictureProperties.DateFormat.replace('D', 'DD/').replace('M', 'MM/').replace('Y', 'YY/').substr(0, 8);
			if (pictureProperties.IncludeHours)
				dateFmt += gx.timeFormat == 12 ? " hh" : " HH";
			if (pictureProperties.IncludeMinutes)
				dateFmt += ":mm";
			if (pictureProperties.IncludeSeconds)
				dateFmt += ":ss";
			if (gx.timeFormat == 12)
				dateFmt += " A";
			return dateFmt;
		}

		function CheckVariable(uc, value, dataType, filterType) {
			return uc.CheckVariable(value, [dataType], filterType == FilterType.IsCollection, uc.DashboardObject.Variables.AllParameters);
		}

		var dashboard = this.DashboardObject;
		var filter = this.GetWidget(filterId);

		if (filter != null) {
			var configuration = {
				parentEl: '#' + this.editor.GetDOMElementId(1),		// la main table es el único contenedor suficientemente ancho como para que queden los dos calendarios uno al lado del otro
				autoApply: filter.DataType == DataType.Date,
				linkedCalendars: false,
				opens: (dashboard.Layout.FiltersPosition == FiltersPosition.Right ? "left" : "right"),
				singleDatePicker: filter.Type != FilterType.Range,
				autoUpdateInput: false,
				showDropdowns: true,
				locale: {
					format: ConvertDateFormat(filter.PictureProperties),
					monthNames: [
						gx.getMessage("GX_Dashboard_January"),
						gx.getMessage("GX_Dashboard_February"),
						gx.getMessage("GX_Dashboard_March"),
						gx.getMessage("GX_Dashboard_April"),
						gx.getMessage("GX_Dashboard_May"),
						gx.getMessage("GX_Dashboard_June"),
						gx.getMessage("GX_Dashboard_July"),
						gx.getMessage("GX_Dashboard_August"),
						gx.getMessage("GX_Dashboard_September"),
						gx.getMessage("GX_Dashboard_October"),
						gx.getMessage("GX_Dashboard_November"),
						gx.getMessage("GX_Dashboard_December")
					],
					daysOfWeek: [
						gx.getMessage("GX_Dashboard_Sunday"),
						gx.getMessage("GX_Dashboard_Monday"),
						gx.getMessage("GX_Dashboard_Tuesday"),
						gx.getMessage("GX_Dashboard_Wednesday"),
						gx.getMessage("GX_Dashboard_Thursday"),
						gx.getMessage("GX_Dashboard_Friday"),
						gx.getMessage("GX_Dashboard_Saturday")
					],
					applyLabel: gx.getMessage("GX_Dashboard_Apply"),
					cancelLabel: gx.getMessage("GX_Dashboard_Cancel")
				}
			};
			if (filter.MinValue != null) {
				var checkResult = CheckVariable(this, filter.MinValue, filter.DataType, filter.Type);
				if (!checkResult.IsVariable)
					configuration.minDate = filter.MinValue.getStringWithFmt(filter.PictureProperties.DateFormat);
			}
			if (filter.MaxValue != null) {
				var checkResult = CheckVariable(this, filter.MaxValue, filter.DataType, filter.Type);
				if (!checkResult.IsVariable)
					configuration.maxDate = filter.MaxValue.getStringWithFmt(filter.PictureProperties.DateFormat);
			}
			if (filter.DataType == DataType.DateTime && filter.PictureProperties.IncludeHours) {
				configuration.timePicker = true;
				configuration.timePicker24Hour = gx.timeFormat == 24;
				configuration.timePickerSeconds = filter.PictureProperties.IncludeSeconds;
			}
			var dpInput = $("#" + this.GetRangeElementId(filter.ElementId));
			dpInput.daterangepicker(configuration);
			var uc = this;
			dpInput.on('apply.daterangepicker', function (ev, picker) {
				function DateToStr(uc, filter, date) {
					var gxDate = new gx.date.gxdate(date);
					gxDate.HasTimePart = filter.DataType == DataType.DateTime;
					var valueStr = uc.ValueToString(gxDate, filter.DataType, filter.Picture, filter.PictureProperties);
					return valueStr;
				}
				if (filter.Type == FilterType.Range) {
					var valueStr1 = DateToStr(uc, filter, picker.startDate._d);
					var valueStr2 = DateToStr(uc, filter, picker.endDate._d);
					$(this).val(valueStr1 + ' - ' + valueStr2);
					uc.UpdateFilter2(this, filter.NameLowerValue, filter.NameUpperValue, valueStr1, valueStr2);
				}
				else {
					var valueStr = DateToStr(uc, filter, picker.startDate._d);
					$(this).val(valueStr);
					uc.UpdateFilter1(this, filter.Name, valueStr);
				}
			});
		}
	}

	this.CreateSlider = function (filterId) {

		var filter = this.GetWidget(filterId);
		if (filter != null) {
			var valueNumber1;
			var valueNumber2;
			var minValueNumber;
			var maxValueNumber;
			var stepNumber;
			var filterValue;
			var filterLowerValue;
			var filterUpperValue;
			var filterMaxValue = this.ResolveVariableValue(filter.MaxValue, filter.DataType, false);
			var filterMinValue = this.ResolveVariableValue(filter.MinValue, filter.DataType, false);
			if (filter.Type == FilterType.Value)
				filterValue = this.ResolveVariableValue(filter.Value, filter.DataType, false);
			else {
				filterLowerValue = this.ResolveVariableValue(filter.LowerValue, filter.DataType, false);
				filterUpperValue = this.ResolveVariableValue(filter.UpperValue, filter.DataType, false);
			}
			filter._noUiSliderSlideExecuted = false;
			if (filter.Type == FilterType.Range)
				filter._noUiSliderIsLowerValue = true;
			switch (filter.DataType) {
				case DataType.Integer:
				case DataType.Real:
					if (filter.Type == FilterType.Value) {
						valueNumber1 = filterValue;
						valueNumber2 = valueNumber1;
					}
					else {
						valueNumber1 = filterLowerValue;
						valueNumber2 = filterUpperValue;
					}
					minValueNumber = filterMinValue != null ? filterMinValue : valueNumber1 - 50;
					maxValueNumber = filterMaxValue != null ? filterMaxValue : valueNumber2 + 50;
					stepNumber = filter.DataType == DataType.Integer ? 1 : 1 / Math.pow(10, filter.PictureProperties.DecimalPrecision);
					break;
				case DataType.Date:
					if (filter.Type == FilterType.Value) {
						filter.MinValueResolved = filterMinValue != null ? filterMinValue : gx.date.dtadd(filterValue, -50 * 24 * 60 * 60);		// 50 días antes
						filter.MaxValueResolved = filterMaxValue != null ? filterMaxValue : gx.date.dtadd(filterValue, 50 * 24 * 60 * 60);		// 50 días después
					}
					else {
						filter.MinValueResolved = filterMinValue != null ? filterMinValue : gx.date.dtadd(filterLowerValue, -50 * 24 * 60 * 60);		// 50 días antes
						filter.MaxValueResolved = filterMaxValue != null ? filterMaxValue : gx.date.dtadd(filterUpperValue, 50 * 24 * 60 * 60);		// 50 días después
					}
					minValueNumber = 0;
					maxValueNumber = gx.date.daysDiff(filter.MaxValueResolved, filter.MinValueResolved);					// Incrementos en días
					if (filter.Type == FilterType.Value) {
						valueNumber1 = gx.date.daysDiff(filterValue, filter.MinValueResolved);
						valueNumber2 = valueNumber1;
					}
					else {
						valueNumber1 = gx.date.daysDiff(filterLowerValue, filter.MinValueResolved);
						valueNumber2 = gx.date.daysDiff(filterUpperValue, filter.MinValueResolved);
					}
					stepNumber = 1;
					break;
				case DataType.DateTime:
					if (filter.Type == FilterType.Value) {
						filter.MinValueResolved = filterMinValue != null ? filterMinValue : gx.date.dtadd(filterValue, -50 * 24 * 60 * 60);		// 50 días antes
						filter.MaxValueResolved = filterMaxValue != null ? filterMaxValue : gx.date.dtadd(filterValue, 50 * 24 * 60 * 60);		// 50 días después
					}
					else {
						filter.MinValueResolved = filterMinValue != null ? filterMinValue : gx.date.dtadd(filterLowerValue, -50 * 24 * 60 * 60);		// 50 días antes
						filter.MaxValueResolved = filterMaxValue != null ? filterMaxValue : gx.date.dtadd(filterUpperValue, 50 * 24 * 60 * 60);		// 50 días después
					}
					minValueNumber = 0;
					maxValueNumber = gx.date.dtdiff(filter.MaxValueResolved, filter.MinValueResolved);			// Incrementos en segundos
					if (filter.Type == FilterType.Value) {
						valueNumber1 = gx.date.dtdiff(filterValue, filter.MinValueResolved);
						valueNumber2 = valueNumber1;
					}
					else {
						valueNumber1 = gx.date.dtdiff(filterLowerValue, filter.MinValueResolved);
						valueNumber2 = gx.date.dtdiff(filterUpperValue, filter.MinValueResolved);
					}
					stepNumber = 1;
					break;
			}
			var uc = this;
			var slider = document.getElementById(this.GetRangeElementId(filter.ElementId));
			var options = {
				start: filter.Type == FilterType.Value ? [valueNumber1] : [valueNumber1, valueNumber2],
				step: stepNumber,
				tooltips: true,
				connect: filter.Type == FilterType.Range,
				behaviour: 'drag-tap',
				format: {
					to: function (value) {
						var isVariable = false;
						if (!filter._noUiSliderSlideExecuted) {
							var valueToCheck;
							if (filter.Type == FilterType.Value)
								valueToCheck = filter.Value;
							else {
								valueToCheck = filter._noUiSliderIsLowerValue ? filter.LowerValue : filter.UpperValue;
								filter._noUiSliderIsLowerValue = !filter._noUiSliderIsLowerValue;
							}
							var checkResult = uc.CheckVariableInEditMode(valueToCheck, filter.DataType, filter.Type == FilterType.Collection);
							isVariable = checkResult.IsVariable;
						}
						if (isVariable)
							return checkResult.SerializedValue;
						else {
							var newValue;
							switch (filter.DataType) {
								case DataType.Integer:
									newValue = value;
									break;
								case DataType.Real:
									newValue = value;
									break;
								case DataType.Date:
									newValue = gx.date.dtadd(filter.MinValueResolved, Math.round(value) * 24 * 60 * 60);
									break;
								case DataType.DateTime:
									newValue = gx.date.dtadd(filter.MinValueResolved, Math.round(value));
									break;
							}
							return uc.ValueToString(newValue, filter.DataType, filter.Picture, filter.PictureProperties);
						}
					},
					from: function (value) {
						return parseFloat(value);
					}
				}
			};
			if (minValueNumber != maxValueNumber)
				options.range = { 'min': minValueNumber, 'max': maxValueNumber };
			else {
				options.range = { 'min': valueNumber1 - 1, 'max': valueNumber2 + 1 };
				options.disabled = true;
			}
			noUiSlider.create(slider, options);
			slider.noUiSlider.on('slide', function () {
				filter._noUiSliderSlideExecuted = true;
			});
			slider.noUiSlider.on('set', function () {
				if (filter.Type != FilterType.Range)
					uc.UpdateFilter1(null, filter.Name, this.get());
				else {
					var values = this.get();
					uc.UpdateFilter2(null, filter.NameLowerValue, filter.NameUpperValue, values[0], values[1]);
				}
			});
			var valueCls = filter.ValueClass == "" ? "gx-db-filter-value" : filter.ValueClass;
			$("#" + this.GetRangeElementId(filter.ElementId) + " .noUi-tooltip").addClass(valueCls);
		}
	}

	this.CreateSwitch = function (filterId) {
		var filter = this.GetWidget(filterId);
		if (filter != null) {
			var options = {
				size: 'small',
				state: filter.Value,
				handleWidth: 0,
				labelWidth: 10,
				onText: '',
				offText: '',
				onColor: 'primary',
				offColor: 'default'
			};
			var uc = this;
			var switchCtrl = $("#" + this.GetRangeElementId(filter.ElementId));
			switchCtrl.bootstrapSwitch(options);
			switchCtrl.on('switchChange.bootstrapSwitch', function (event, state) {
				uc.UpdateFilter1(null, filter.Name, state.toString());
				if (!state) {
					switchCtrl.bootstrapSwitch('onColor', 'default');
					switchCtrl.bootstrapSwitch('offColor', 'default');
				}
				else {
					switchCtrl.bootstrapSwitch('onColor', 'primary');
					switchCtrl.bootstrapSwitch('offColor', 'primary');
				}
			});

		}
	}

	this.CreateDropDownList = function (filterId) {
		var filter = this.GetWidget(filterId);
		if (filter != null) {
			$('#' + this.editor.GetDOMElementId(widget.ElementId) + ' > .selectpicker').selectpicker();
		}
	}
	
	this.GetQueryViewerAxes = function (obj, variables) {
		var elements = JSON.parse(JSON.stringify(obj.Elements));
		for (var i = 0; i < elements.length; i++)
			this.ReplaceVariablesInObjectElement(elements[i], variables);
		return elements;
	}

	this.GetQueryViewerParameters = function (dashboard) {

		function GetParameter(uc, name, value, dataType, isCollection, picture, pictureProperties) {
			var valueAux = dataType == DataType.Real ? value.replace(',', '.') : value;
			var resolveObj = uc.ResolveVariable(valueAux, dataType, isCollection);
			var resolvedValue;
			if (resolveObj.WasVariable) {
				if (isCollection)
					resolvedValue = uc.ValuesToString(resolveObj.Value, dataType, picture, pictureProperties);
				else
					resolvedValue = uc.ValueToString(resolveObj.Value, dataType, picture, pictureProperties);
			}
			else
				resolvedValue = resolveObj.Value;
			return { Name: name, Value: resolvedValue };
		}

		var parameters = [];
		for (var i = 0; i < dashboard.Filters.length; i++) {
			var filter = dashboard.Filters[i];
			var parameter;
			if (filter.Type != FilterType.Range) {
				parameter = GetParameter(this, filter.Name, filter.SerializedValue, filter.DataType, filter.Type == FilterType.Collection, filter.Picture, filter.PictureProperties);
				parameters.push(parameter);
			}
			else {
				parameter = GetParameter(this, filter.NameLowerValue, filter.SerializedLowerValue, filter.DataType, filter.Type == FilterType.Collection, filter.Picture, filter.PictureProperties);
				parameters.push(parameter);
				parameter = GetParameter(this, filter.NameUpperValue, filter.SerializedUpperValue, filter.DataType, filter.Type == FilterType.Collection, filter.Picture, filter.PictureProperties);
				parameters.push(parameter);
			}
		}
		for (var i = 0; i < dashboard.Parameters.length; i++) {
			var dvParam = dashboard.Parameters[i];
			var parameter = GetParameter(this, dvParam.Name, dvParam.SerializedValue, dvParam.DataType, dvParam.Type == ParameterType.Collection, dvParam.Picture, dvParam.PictureProperties);
			parameters.push(parameter);
		}
		return parameters;
	}

	this.GetEmptyValueStr = function (dataType, isCollection) {
		if (isCollection)
			return "[]";
		else
			switch (dataType) {
				case DataType.Integer:
				case DataType.Real:
					return "0";
				case DataType.Character:
					return "";
				case DataType.Boolean:
					return "false";
				case DataType.Date:
					return "0000-00-00";
				case DataType.DateTime:
					return "0000-00-00 00:00:00";
				case DataType.GUID:
					return "00000000-0000-0000-0000-000000000000";
				case DataType.GeoPoint:
					return "";
				default:
					return "";
			}
	}

	this.GetEmptyValue = function (dataType, isCollection) {
		if (isCollection)
			return [];
		else
			switch (dataType) {
				case DataType.Integer:
				case DataType.Real:
					return 0;
				case DataType.Character:
					return "";
				case DataType.Boolean:
					return false;
				case DataType.Date:
				case DataType.DateTime:
					return new gx.date.gxdate();
				case DataType.GUID:
					return "00000000-0000-0000-0000-000000000000";
				case DataType.GeoPoint:
					return "";
				default:
					return null;
			}
	}

	this.NextControlId = function () {
		if (!this._queryViewerId)
			this._queryViewerId = 1;
		else
			this._queryViewerId++;
		return this.ParentObject.GXLastCtrlId + this._queryViewerId + 1;
	}

	this.QueryFilterName = function (filter) {
		var dashboard = this.DashboardObject;
		var filterKey = filter.ControlName;
		return dashboard.Name + "_" + filterKey;
	}

	this.RenderQueryViewer = function (widgetId) {

		var obj = this.GetWidget(widgetId);
		if (obj != null) {

			var containerId = this.GetQueryViewerElementId(obj.ElementId);
			var qViewer;
			if (!obj.QueryViewer || obj.QueryViewer.ContainerName != containerId) {
				var parentObj = this.ParentObject;
				var controlId = this.NextControlId();
				var className = "QueryViewer";
				var controlName = className + obj.ElementId;
				var fieldName = controlName.toUpperCase();
				qViewer = gx.uc.getNew(parentObj, controlId, 0, className, containerId, controlName, fieldName);
				parentObj.setUserControl(qViewer);
				obj.QueryViewer = qViewer;
			}
			else
				qViewer = obj.QueryViewer;
			qViewer._EmbeddedInDashboard = true;
			var variables = this.DashboardObject.Variables.Filters;
			var parameters = this.GetQueryViewerParameters(this.DashboardObject);
			var axes = this.GetQueryViewerAxes(obj, variables);
			this.InitializeQueryViewer(qViewer);

			var cls = obj.Class == "" ? "gx-db-object" : obj.Class;
			qViewer.Class = cls;
			qViewer.Visible = true;
			qViewer.Enabled = true;
			qViewer.SetParameters(parameters);
			qViewer.SetAxes(axes);
			qViewer.ReturnSampleData = (obj.Type == ObjectType.DataProvider && this.IsDashboardEdit() ? true : this.ReturnSampleData);
			qViewer.ObjectName = obj.Name;
			qViewer.ObjectType = obj.Type;
			qViewer.Title = this.ReplaceVariablesValuesInText(obj.Output.Title, variables);
			qViewer.Type = obj.Output.Type;
			qViewer.ChartType = obj.Output.ChartType;
			qViewer.PlotSeries = obj.Output.PlotSeries;
			qViewer.XAxisLabels = obj.Output.XAxisLabels;
			qViewer.XAxisIntersectionAtZero = obj.Output.XAxisIntersectionAtZero;
			qViewer.XAxisTitle = this.ReplaceVariablesValuesInText(obj.Output.XAxisTitle, variables);
			qViewer.YAxisTitle = this.ReplaceVariablesValuesInText(obj.Output.YAxisTitle, variables);
			qViewer.ShowValues = obj.Output.ShowValues;
			qViewer.ShowDataAs = obj.Output.ShowDataAs;
			qViewer.Orientation = obj.Output.Orientation;
			qViewer.IncludeTrend = obj.Output.IncludeTrend;
			qViewer.TrendPeriod = obj.Output.TrendPeriod;
			qViewer.IncludeSparkline = obj.Output.IncludeSparkline;
			qViewer.IncludeMaxAndMin = obj.Output.IncludeMaxAndMin;
			qViewer.Paging = obj.Output.Paging;
			qViewer.PageSize = this.ReplaceVariableValue(obj.Output.PageSize, [DataType.Integer], false, variables);
			qViewer.ShowDataLabelsIn = obj.Output.ShowDataLabelsIn;
			qViewer.TotalForRows = obj.Output.TotalForRows;
			qViewer.TotalForColumns = obj.Output.TotalForColumns;
			qViewer.MapLibrary = QueryViewerMapLibrary.ECharts;
			qViewer.MapType = obj.Output.MapType;
			qViewer.Region = obj.Output.Region;
			qViewer.Continent = obj.Output.Continent;
			qViewer.Country = obj.Output.Country;
			qViewer.TranslationType = this.TranslationType;

			if (obj.OnItemClick == OnItemClick.ApplyFilters || obj.OnItemClick == OnItemClick.HighlightValues || (obj.OnItemClick == OnItemClick.RaiseItemClick && this.ItemClick)) {
				qViewer.AllowSelection = obj.OnItemClick == OnItemClick.ApplyFilters || obj.OnItemClick == OnItemClick.HighlightValues;
				var dBoard = this;
				qViewer.ItemClick = function () { dBoard.ProcessItemClick(obj, qViewer); };
			}
			else {
				if (qViewer.ItemClick) {
					delete qViewer.ItemClick;
				}
			}
			if (obj.OnItemClick != OnItemClick.ApplyFilters && obj.HiddenFilters && obj.HiddenFilters.length > 0) {
				var removedFilters = [];
				for (var i = 0; i < qViewer.Metadata.Axes.length; i++) {
					var axis = qViewer.Metadata.Axes[i];
					this.RemoveHiddenFilter(obj, axis.Name);
					removedFilters.push(axis.Name);
				}
				if (removedFilters.length > 0)
					this.RefreshDashboard(removedFilters);
			}

			qViewer.show();

		}
	}

	this.ProcessItemClick = function (obj, qViewer) {
		switch (obj.OnItemClick) {
			case OnItemClick.ApplyFilters:
				this.ApplyHiddenFilters(obj, qViewer);
				break;
			case OnItemClick.HighlightValues:
				var selection = this.HighlightValues(obj, qViewer);
				if (this.ValuesHighlighted) {
					this.LoadValuesHighlightedData(selection);
					this.ValuesHighlighted();
				}
				break;
			case OnItemClick.RaiseItemClick:
				this.LoadItemClickData(obj, qViewer);
				this.ItemClick();
				break;
		}
	}

	this.FakeQueryViewer = function (queryName) {
		var dashboard = this.DashboardObject;
		var parameters = this.GetQueryViewerParameters(dashboard);
		var fakeQV = {};
		fakeQV.RealType = "Table";
		fakeQV.Axes = [];
		fakeQV.Parameters = parameters;
		fakeQV.ParentObject = { PackageName: this.ParentObject.PackageName };
		fakeQV.ObjectName = queryName;
		fakeQV.ObjectType = ObjectType.Query;
		fakeQV.ObjectId = 0;
		fakeQV.QueryInfo = "";
		fakeQV.AppSettings = [];
		fakeQV.AllowElementsOrderChange = false;
		fakeQV.UseRecordsetCache = false;
		fakeQV.ReturnSampleData = this.ReturnSampleData;
		return fakeQV;
	}

	this.InitializeQueryViewer = function (qViewer) {		//TODO: ESTO DEBERIA SER PARTE DEL QUERYVIEWER
		qViewer.AutoRefreshGroup = "";
		qViewer.SetDragAndDropData({});
		qViewer.SetItemExpandData({});
		qViewer.SetItemCollapseData({});
		qViewer.SetFilterChangedData({});
		qViewer.SetItemClickData({});
		qViewer.SetItemDoubleClickData({});
		qViewer.ObjectId = 0;
		qViewer.ObjectType = "";
		qViewer.QueryInfo = "";
		qViewer.AppSettings = "";
		qViewer.IsExternalQuery = false;
		qViewer.ExternalQueryResult = "";
		qViewer.AvoidAutomaticShow = false;
		qViewer.ExecuteShow = false;
		qViewer.ObjectInfo = "";
		qViewer.SetAxes([]);
		qViewer.SetParameters([]);
		qViewer.ObjectName = "";
		qViewer.Class = "";
		qViewer.AutoResize = false;
		qViewer.Width = "100%";
		qViewer.Height = "100%";
		qViewer.ShowAxesSelectors = "BothSelectors";
		qViewer.AllowElementsOrderChange = false;
		qViewer.DisableColumnSort = false;
		qViewer.FontFamily = "";
		qViewer.FontSize = 0;
		qViewer.FontColor = gx.color.fromRGB(0, 0, 0);
		qViewer.ExportToXML = true;
		qViewer.ExportToHTML = true;
		qViewer.ExportToXLS = true;
		qViewer.ExportToXLSX = true;
		qViewer.ExportToPDF = true;
		qViewer.Title = "";
		qViewer.Type = OutputType.Chart;
		qViewer.ChartType = ChartType.Column;
		qViewer.Paging = true;
		qViewer.PageSize = EditorDefaults.PageSize;
		qViewer.CurrentPage = 0;
		qViewer.ShowDataLabelsIn = ShowDataLabelsIn.Columns;
		qViewer.TotalForRows = Total.Yes;
		qViewer.TotalForColumns = Total.Yes;
		qViewer.PlotSeries = PlotSeries.InTheSameChart;
		qViewer.XAxisLabels = XAxisLabels.Horizontally;
		qViewer.XAxisIntersectionAtZero = false;
		qViewer.ShowValues = true;
		qViewer.XAxisTitle = "";
		qViewer.YAxisTitle = "";
		qViewer.ShowDataAs = ShowDataAs.Values;
		qViewer.Orientation = Orientation.Horizontal;
		qViewer.IncludeTrend = false;
		qViewer.TrendPeriod = TrendPeriod.SinceTheBeginning;
		qViewer.IncludeSparkline = false;
		qViewer.IncludeMaxAndMin = false;
		qViewer.RememberLayout = true;
	}

	this.HighlightValues = function (obj, qViewer) {
		var selection = [];
		var itemClickData = qViewer.ItemClickData;
		if (itemClickData.Selected) {
			for (var i = 0; i < itemClickData.Context.length; i++) {
				var contextItem = itemClickData.Context[i];
				var axis = this.GetAxisInMetadata(qViewer.Metadata, contextItem.Name);
				if (axis != null && contextItem.Values.length == 1)
					selection.push({ Name: axis.Name, Value: contextItem.Values[0] });
			}
		}
		var dashboard = this.DashboardObject;
		for (var i = 0; i < dashboard.Objects.length; i++)
			if (dashboard.Objects[i].ElementId != obj.ElementId) {
				var qViewerOther = dashboard.Objects[i].QueryViewer;
				qViewerOther.AllowSelection = true;
				qViewerOther.Select(selection);
			}
		return selection;
	}

	this.CheckFilterValue = function (value, dataType, filterType, picture, pictureProperties, minValue, maxValue, internalRepresentation) {

		function GetNotExpectedDataTypeMessage(dataType, filterType) {
			if (filterType == FilterType.Collection)
				return gx.getMessage("GX_Dashboard_NotAValidJSON");
			switch (dataType) {
				case DataType.Integer:
				case DataType.Real:
					return gx.getMessage("GX_Dashboard_NotAValidNumber");
				case DataType.Boolean:
					return gx.getMessage("GX_Dashboard_NotAValidBoolean");
				case DataType.Date:
					return gx.getMessage("GX_Dashboard_NotAValidDate");
				case DataType.DateTime:
					return gx.getMessage("GX_Dashboard_NotAValidDateTime");
				case DataType.GUID:
					return gx.getMessage("GX_Dashboard_NotAValidGUID");
				case DataType.GeoPoint:
					return gx.getMessage("GX_Dashboard_NotAValidGeoPoint");
				default:
					return "";
			}
		}

		function TestExtremeValue(value, dataType, filterType, extremeValue, lowerExtreme) {
			if (extremeValue != null && dataType != DataType.Boolean && filterType != FilterType.Collection)
				if (dataType == DataType.Date || dataType == DataType.DateTime)
					return lowerExtreme ? value.compare(extremeValue) >= 0 : value.compare(extremeValue) <= 0;
				else
					return lowerExtreme ? value >= extremeValue : value <= extremeValue;
			return true;
		}

		function TestMinValue(value, dataType, filterType, minValue) {
			return TestExtremeValue(value, dataType, filterType, minValue, true);
		}

		function TestMaxValue(value, dataType, filterType, maxValue) {
			return TestExtremeValue(value, dataType, filterType, maxValue, false);
		}

		var parsedValue = null;
		var parsedSerializedValue = "";
		var parserMessage = "";
		var notEmptyValue = value == "" ? this.GetEmptyValueStr(dataType, filterType == FilterType.Collection) : value;
		var checkResult = this.CheckVariableInEditMode(notEmptyValue, dataType, filterType == FilterType.Collection);
		var parseResult = {};
		if (checkResult.IsVariable) {
			parseResult.Success = true;
			parseResult.ParsedValue = checkResult.Value;
			parsedSerializedValue = checkResult.SerializedValue;
		}
		else {
			if (internalRepresentation)
				parseResult = this.ParseInternalValue(notEmptyValue, dataType, filterType == FilterType.Collection);
			else
				parseResult = this.ParseValue(notEmptyValue, dataType, filterType == FilterType.Collection);				//TODO: cuando se parsea y es distinto al valor que se ingresó, sustituir por el valor correcto, por ejemplo "10aaa" para numeros sutituir por "10" en el control.
		}
		if (parseResult.Success)
			if (TestMinValue(parseResult.ParsedValue, dataType, filterType, minValue))
				if (TestMaxValue(parseResult.ParsedValue, dataType, filterType, maxValue)) {
					parsedValue = parseResult.ParsedValue;
					if (!checkResult.IsVariable)
						parsedSerializedValue = (filterType == FilterType.Collection ? this.ValuesToString(parsedValue, dataType, picture, pictureProperties) : this.ValueToString(parsedValue, dataType, picture, pictureProperties));
				}
				else
					parserMessage = gx.getMessage("GX_Dashboard_MaximumExceeded");
			else
				parserMessage = gx.getMessage("GX_Dashboard_MinimumExceeded");
		else
			parserMessage = GetNotExpectedDataTypeMessage(dataType, filterType);
		return { Success: parserMessage == "", IsVariable: checkResult.IsVariable, ParsedValue: parsedValue, ParsedSerializedValue: parsedSerializedValue, Message: parserMessage };
	}

	this.UpdateFilterLayout = function (control, filter, internalRepresentation) {

		function UpdateEditboxLayout(control, valueStr) {
			control.value = valueStr;
		}

		function UpdateRadioButtonLayout(uc, control) {
			if (!uc.OldRadioButton()) {
				for (var i = 0; i < control.parentNode.parentNode.childNodes.length; i++) {
					var button = control.parentNode.parentNode.childNodes[i].childNodes[0];
					button.classList.remove("active");
				}
				control.classList.add("active");
			}
		}

		function UpdateCheckboxLayout(uc, control) {
			var inputCtrl = control.tagName == "INPUT" ? control : control.previousSibling;
			var inputFound = false;
			for (var i = 0; i < control.parentNode.childNodes.length; i++) {
				var elem = control.parentNode.childNodes[i];
				if (elem.id == inputCtrl.id)
					inputFound = true;
				if (elem.classList.contains("material-icons") && inputFound) {
					elem.textContent = elem.textContent == "check_box_outline_blank" ? "check_box" : "check_box_outline_blank";
					break;
				}
			}
		}

		if (control != null) {
			switch (filter.Control.Type) {
				case ControlType.Editbox:
					var valueStr;
					switch (filter.Type) {
						case FilterType.Value:
							if (internalRepresentation)
								valueStr = this.ValueToInternalString(filter.Value, filter.DataType, filter.Picture, filter.PictureProperties).replace(' ', 'T');		// solo para Date y DateTime en SD
							else
								valueStr = filter.SerializedValue;
							break;
						case FilterType.Range:
							valueStr = filter.SerializedLowerValue + " - " + filter.SerializedUpperValue;
							break;
						case FilterType.Collection:
							valueStr = filter.SerializedValue;
							break;
					}
					UpdateEditboxLayout(control, valueStr);
					break;
				case ControlType.RadioButton:
					UpdateRadioButtonLayout(this, control);
					break;
				case ControlType.Checkbox:
					UpdateCheckboxLayout(this, control);
					break;
			}
		}
	}

	this.UpdateFilter1 = function (control, name, value) {
		return this.UpdateFilterMain(control, name, null, value, null, false);
	}

	this.UpdateFilter2 = function (control, name1, name2, value1, value2) {
		return this.UpdateFilterMain(control, name1, name2, value1, value2, false);
	}

	this.UpdateFilter1Internal = function (control, name, value) {
		return this.UpdateFilterMain(control, name, null, value, null, true);
	}

	this.SplitAndUpdateFilter2 = function (control, name1, name2, value) {
		var values = value.split(' - ');
		if (values.length == 1)
			values = value.split('-');
		var value1 = "";
		var value2 = "";
		if (values.length >= 1)
			value1 = values[0];
		if (values.length >= 2)
			value2 = values[1];
		this.UpdateFilter2(control, name1, name2, value1, value2);
	}

	this.UpdateFilterMain = function (control, name1, name2, value1, value2, internalRepresentation) {
		var filter = this.GetFilterByNames([name1, name2]);
		var filter1Changed = false;
		var filter2Changed = false;
		if (filter != null) {
			var checkValueResult1 = this.CheckFilterValue(value1, filter.DataType, filter.Type, filter.Picture, filter.PictureProperties, filter.MinValue, filter.MaxValue, internalRepresentation);
			var checkValueResult2;
			if (filter.Type == FilterType.Range)
				checkValueResult2 = this.CheckFilterValue(value2, filter.DataType, filter.Type, filter.Picture, filter.PictureProperties, filter.MinValue, filter.MaxValue, internalRepresentation);
			else
				checkValueResult2 = { Success: true, Message: "" };
			if (checkValueResult1.Success && checkValueResult2.Success && filter.Type == FilterType.Range && this.CompareValues(checkValueResult1.ParsedValue, checkValueResult2.ParsedValue, filter.DataType) == 1) {
				checkValueResult1.Success = false;
				checkValueResult1.Message = gx.getMessage("GX_Dashboard_LowerValueGreaterThanUpperValue");
			}
			if (checkValueResult1.Success && checkValueResult2.Success) {
				if (filter.Type == FilterType.Collection) {
					filter1Changed = filter.SerializedValue != checkValueResult1.ParsedSerializedValue;
					if (filter1Changed) {
						filter.Values = checkValueResult1.ParsedValue;
						filter.SerializedValue = checkValueResult1.ParsedSerializedValue;
					}
				}
				else {
					if (filter.Type != FilterType.Range)
						filter1Changed = filter.SerializedValue !== checkValueResult1.ParsedSerializedValue;
					else {
						filter1Changed = filter.SerializedLowerValue !== checkValueResult1.ParsedSerializedValue;
						filter2Changed = filter.SerializedUpperValue !== checkValueResult2.ParsedSerializedValue;
					}
					if (filter1Changed || filter2Changed) {
						if (filter.Type != FilterType.Range) {
							filter.Value = checkValueResult1.ParsedValue;
							filter.SerializedValue = checkValueResult1.ParsedSerializedValue;
						}
						else {
							filter.LowerValue = checkValueResult1.ParsedValue;
							filter.UpperValue = checkValueResult2.ParsedValue;
							filter.SerializedLowerValue = checkValueResult1.ParsedSerializedValue;
							filter.SerializedUpperValue = checkValueResult2.ParsedSerializedValue;
						}
					}
				}
				this.HideError();
			}
			else
				this.ShowError(filter.ElementId, checkValueResult1.Message + (checkValueResult2.Message == "" ? "" : " ") + checkValueResult2.Message, true);
			if (filter1Changed || filter2Changed || filter.Control.Type == ControlType.Editbox)	// Si es Editbox actualizo siempre porque tiene que aplicar la picture
				this.UpdateFilterLayout(control, filter, internalRepresentation);
			if (filter1Changed || filter2Changed) {
				var names = [];
				if (name1 != null && filter1Changed) names.push(name1);
				if (name2 != null && filter2Changed) names.push(name2);
				this.RefreshDashboard(names);
				if (this.IsDashboardEdit()) {
					var command;
					if (filter.Type != FilterType.Range) {
						var internalValue = (checkValueResult1.IsVariable ? checkValueResult1.ParsedSerializedValue : (filter.Type == FilterType.Collection ? filter.SerializedValue : this.ValueToInternalString(filter.Value, filter.DataType, filter.Picture, filter.PictureProperties)));
						window.external.SetFilterValue(filter.ElementId, internalValue);
					}
					else {
						var internalValue1 = (checkValueResult1.IsVariable ? checkValueResult1.ParsedSerializedValue : this.ValueToInternalString(filter.LowerValue, filter.DataType, filter.Picture, filter.PictureProperties));
						var internalValue2 = (checkValueResult2.IsVariable ? checkValueResult2.ParsedSerializedValue : this.ValueToInternalString(filter.UpperValue, filter.DataType, filter.Picture, filter.PictureProperties));
						window.external.SetFilterValues(filter.ElementId, internalValue1, internalValue2);
					}
				}
			}
		}
		return filter1Changed || filter2Changed;
	}

	this.ShowError = function (elementId, message, fade) {
		var errorDiv = this.editor.GetErrorDiv();
		var element = $('#' + this.editor.GetDOMElementId(elementId));
		var position = element.offset();
		var computedStyle = window.getComputedStyle(document.getElementById(this.editor.GetDOMElementId(elementId)));
		var paddingBottom = parseInt(computedStyle.getPropertyValue("padding-bottom"));
		var paddingLeft = parseInt(computedStyle.getPropertyValue("padding-left"));
		errorDiv.style.left = gx.dom.addUnits(parseInt(position.left + paddingLeft));
		errorDiv.style.top = gx.dom.addUnits(parseInt(position.top + element.height() + paddingBottom));
		errorDiv.style.width = gx.dom.addUnits(element.width());
		errorDiv.innerText = message;
		errorDiv.style.display = "block";
		errorDiv.classList.remove("gx-db-fade");
		if (this._errorFadeTimeout)
			clearTimeout(this._errorFadeTimeout);
		if (fade) {
			var f = this.me() + '.FadeError();';
			this._errorFadeTimeout = setTimeout(f, 5000);
		}
	}

	this.HideError = function () {
		var errorDiv = this.editor.GetErrorDiv();
		errorDiv.style.display = "none";
	}

	this.FadeError = function () {
		var errorDiv = this.editor.GetErrorDiv();
		errorDiv.classList.add("gx-db-fade");
	}

	this.UpdateFilterValue = function (control, name, value, selected) {
		var dashboard = this.DashboardObject;
		var filterChanged = false;
		for (var i = 0; i < dashboard.Filters.length; i++) {
			var filter = dashboard.Filters[i];
			if (filter.Name == name) {
				var checkValueResult = this.CheckFilterValue(value, filter.DataType, FilterType.Value, filter.Picture, filter.PictureProperties, null, null, false);
				if (checkValueResult.Success) {
					var valueIndex = this.ValuesIndexOf(filter.Values, checkValueResult.ParsedValue, filter.DataType);
					if (valueIndex < 0 && selected) {
						filter.Values.push(checkValueResult.ParsedValue);
						filter.SerializedValue = this.ValuesToString(filter.Values, filter.DataType, filter.Picture, filter.PictureProperties);
						filterChanged = true;
					}
					if (valueIndex >= 0 && !selected) {
						filter.Values.splice(valueIndex, 1);
						filter.SerializedValue = this.ValuesToString(filter.Values, filter.DataType, filter.Picture, filter.PictureProperties);
						filterChanged = true;
					}
				}
				else
					this.ShowError(filter.ElementId, checkValueResult.Message, true);
				if (filterChanged) {
					this.UpdateFilterLayout(control, filter, false);
					this.RefreshDashboard([name]);
					if (this.IsDashboardEdit())
						window.external.SetFilterValue(filter.ElementId, filter.SerializedValue);
				}
				break;
			}
		}
	}

	this.UpdateDropDownListFilter = function (control, name) {
		var filter = this.GetFilterByName(name);
		if (filter != null) {
			var delimiter;
			if (filter.DataType == DataType.Character || filter.DataType == DataType.Date || filter.DataType == DataType.DateTime || filter.DataType == DataType.GUID || filter.DataType == DataType.GeoPoint)
				delimiter = '"';
			else
				delimiter = '';
			var jsonValue = "";
			for (var i = 0; i < control.options.length; i++) {
				if (control.options[i].selected) {
					var checkValueResult = this.CheckFilterValue(control.options[i].value, filter.DataType, FilterType.Value, filter.Picture, filter.PictureProperties, null, null, false);
					if (checkValueResult.Success) {
						var value = checkValueResult.ParsedValue;
						var valueStr = delimiter + this.ValueToInternalString(value, filter.DataType, filter.Picture, filter.PictureProperties) + delimiter;
						jsonValue += (jsonValue == '' ? '' : ', ') + valueStr;
					}
				}
			}
			jsonValue = '[' + jsonValue + ']';
			this.UpdateFilter1(control, name, jsonValue);
		}
	}

	this.ApplyHiddenFilters = function (obj, qViewer) {
		var itemClickData = qViewer.ItemClickData;
		var changedFilters = [];
		for (var i = 0; i < itemClickData.Context.length; i++) {
			var contextItem = itemClickData.Context[i];
			var axis = this.GetAxisInMetadata(qViewer.Metadata, contextItem.Name);
			if (axis != null && contextItem.Values.length == 1) {
				if (itemClickData.Selected) {
					var dataType = this.GetHiddenFilterDataType(axis);
					var parseResult = this.ParseValue(contextItem.Values[0], dataType, false);
					if (parseResult.Success) {
						var filterChanged = this.AddHiddenFilter(obj, axis.Name, parseResult.ParsedValue, dataType);
						if (filterChanged)
							changedFilters.push(axis.Name);
					}
				}
				else {
					this.RemoveHiddenFilter(obj, axis.Name);
					changedFilters.push(axis.Name);
				}
			}
		}
		if (changedFilters.length > 0)
			this.RefreshDashboard(changedFilters);
	}

	this.RemoveHiddenFilter = function (obj, name) {
		var dashboard = this.DashboardObject;
		for (var i = 0; i < dashboard.Filters.length; i++) {
			filter = dashboard.Filters[i];
			if (filter.Name == name && filter.IsHidden) {
				dashboard.Filters.splice(i, 1);
				break;
			}
		}
		var pos = obj.HiddenFilters.indexOf(name);
		if (pos >= 0)
			obj.HiddenFilters.splice(pos, 1);
	}

	this.GetFilterByName = function (name) {
		return this.GetFilterByNames([name]);
	}

	this.GetFilterByNames = function (names) {
		var dashboard = this.DashboardObject;
		for (var i = 0; i < dashboard.Filters.length; i++) {
			filter = dashboard.Filters[i];
			var isRangeFilter = filter.Type == FilterType.Range;
			if ((!isRangeFilter && filter.Name == names[0]) || (isRangeFilter && filter.NameLowerValue == names[0] && filter.NameUpperValue == names[1]))
				return filter;
		}
		return null;
	}

	this.GetFilterByAnyName = function (name) {
		var dashboard = this.DashboardObject;
		for (var i = 0; i < dashboard.Filters.length; i++) {
			filter = dashboard.Filters[i];
			if (filter.Type == FilterType.Range) {
				if (filter.NameLowerValue.toLowerCase() == name.toLowerCase() || filter.NameUpperValue.toLowerCase() == name.toLowerCase())
					return filter;
			}
			else {
				if (filter.Name.toLowerCase() == name.toLowerCase())
					return filter;
			}
		}
		return null;
	}

	this.AddHiddenFilter = function (obj, name, value, dataType) {
		var dashboard = this.DashboardObject;
		var found = false;
		var filter;
		var filterChanged = false;
		for (var i = 0; i < dashboard.Filters.length; i++) {
			filter = dashboard.Filters[i];
			if (filter.Name == name) {
				found = true;
				break;
			}
		}
		if (!found) {
			filter = {};
			this.FilterDefaults(filter, dashboard);
			filter.IsHidden = true;
			filter.Name = name;
			filter.DataType = dataType;
			filter.Type = FilterType.Value;
			filter.MinValue = "";
			filter.MaxValue = "";
			dashboard.Filters.push(filter);
			obj.HiddenFilters = obj.HiddenFilters || [];
			obj.HiddenFilters.push(name);
			filter.Value = value;
			this.ProcessFilter(filter);
			filterChanged = true;
		}
		else {
			filterChanged = filter.Value !== value;
			if (filterChanged) {
				filter.Value = value;
				filter.SerializedValue = this.ValueToString(filter.Value, filter.DataType, filter.Picture, filter.PictureProperties);
			}
		}
		return filterChanged;
	}

	this.GetAxisInMetadata = function (metadata, name) {
		for (var i = 0; i < metadata.Axes.length; i++) {
			var axis = metadata.Axes[i];
			if (axis.Name == name)
				return axis;
		}
		return null;
	}

	this.GetHiddenFilterDataType = function (axis) {
		if (axis != null)
			switch (axis.DataType) {
				case "integer":
					return DataType.Integer;
				case "real":
					return DataType.Real;
				case "character":
					return DataType.Character
				case "boolean":
					return DataType.Boolean;
				case "date":
					return DataType.Date;
				case "datetime":
					return DataType.DateTime;
				case "guid":
					return DataType.GUID;
				case "geopoint":
					return DataType.GeoPoint;
				default:
					return DataType.Integer;
			}
		else
			return DataType.Integer;
	}
}

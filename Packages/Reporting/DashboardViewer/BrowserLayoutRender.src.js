var ElementType = { Dashboard: "Dashboard", Layout: "Layout", Table: "Table", Row: "Row", Cell: "Cell", Widget: "Widget" };
var OutputType = { Card: "Card", Chart: "Chart", PivotTable: "PivotTable", Table: "Table" };
var DropZone = { Top: "Top", Bottom: "Bottom", Left: "Left", Right: "Right", Outside: "Outside", Inside: "Inside" };
var TableMode = { Bootstrap: "Bootstrap", Grid: "Grid", Flex: "Flex" };
var Command = { AddElement: "GX_DASHBOARD_ADDELEMENT", MoveElement: "GX_DASHBOARD_MOVEELEMENT" , MoveElements: "GX_DASHBOARD_MOVEELEMENTS" };
var TableBootstrap = { Mode: TableMode.Bootstrap };
var ResponsiveScale = { ExtraSmall: "xs", Small: "sm", Medium: "md", Large: "lg" };
var ElementCssClass = { Selected: "Selected", NotSelected: "NotSelected", DependsOn: "DependsOn", Impacts: "Impacts" };

function BrowserLayout(cfg) {
	this.ReturnSampleData = false;
	this.SelectedElementIds = [];

	this.Initialize = function() {
		this.DashboardDefaults(cfg.dashboard);
	}

	this.SelfInitialize = function (element, elementType) {
		element.ElementType = elementType;
		element.Selected = false;
	}

	this.ParentInitialize = function (element, parentElement) {
		element.ParentElement = parentElement;
	}

	this.AddElement = function (dashboard, element) {
		dashboard.AllElements.push(element);
		if (element.ElementType == ElementType.Widget) {
			dashboard.AllWidgets.push(element);
			cfg.onAddElement(dashboard, element);
		}
	}

	this.RemoveElement = function (dashboard, element) {
		this.RemoveElementById(dashboard.AllElements, element.ElementId);
		if (element.ElementType == ElementType.Widget) {
			this.RemoveElementById(dashboard.AllWidgets, element.ElementId);
			cfg.onRemoveElement(dashboard, element);
		}
	}

	this.RemoveElementById = function (array, elementId) {
		for (var i = 0; i < array.length; i++)
			if (array[i].ElementId == elementId) {
				array.splice(i, 1);
				return true;
			}
		return false;
	}

	this.DashboardDefaults = function (dashboard) {
		this.SelfInitialize(dashboard, ElementType.Dashboard);
		this.ParentInitialize(dashboard, null);
		dashboard.AllWidgets = [];
		var layout = dashboard.Layout;
		if (layout) {
			this.ParentInitialize(layout, dashboard);
			this.LayoutDefaults(layout, dashboard);
		}
	}

	this.LayoutDefaults = function (layout, dashboard) {
		this.SelfInitialize(layout, ElementType.Layout);
		cfg.layoutDefaults(layout);
		dashboard.AllElements = [layout];
		var mainTable = layout.MainTable;
		this.ParentInitialize(mainTable, layout);
		this.TableDefaults(mainTable, dashboard, cfg.getMessage("GX_Dashboard_DropElementsHere"));
		var filtersTable = layout.FiltersTable;
		if (filtersTable) {
			this.ParentInitialize(filtersTable, layout);
			this.TableDefaults(filtersTable, dashboard, cfg.getMessage("GX_Dashboard_DropFiltersHere"));
		}
	}

	this.TableDefaults = function (table, dashboard, cellEmptyMessage) {
		this.SelfInitialize(table, ElementType.Table);
		cfg.tableDefaults(table);
		table.CellEmptyMessage = cellEmptyMessage;
		if (cfg.isEditor() && cfg.addExtraRow(dashboard.Layout, table))
			table.Rows.push({ ElementId: table.ElementId + "_NEWROW", Cells: [] });
		for (var i = 0; i < table.Rows.length; i++) {
			var row = table.Rows[i];
			this.ParentInitialize(row, table);
			this.RowDefaults(row, dashboard);
		}
		this.AddElement(dashboard, table);
	}

	this.RowDefaults = function (row, dashboard) {
		this.SelfInitialize(row, ElementType.Row);
		cfg.rowDefaults(row);
		if (cfg.isEditor() && row.Cells.length == 0)
			row.Cells.push({ ElementId: row.ElementId + "_NEWCELL", IsEmpty: true });
		for (var i = 0; i < row.Cells.length; i++) {
			var cell = row.Cells[i];
			this.ParentInitialize(cell, row);
			this.CellDefaults(cell, dashboard);
		}
		this.AddElement(dashboard, row);
	}

	this.CellDefaults = function (cell, dashboard) {
		this.SelfInitialize(cell, ElementType.Cell);
		cfg.cellDefaults(cell);
		if (!cell.IsEmpty) {
			var widget = cell[cell.ChildType];
			this.ParentInitialize(widget, cell);
			if (cell.HasTable) {
				var cellEmptyMessage = cfg.isMainTableLayoutTable(widget) ? cfg.getMessage("GX_Dashboard_DropElementsHere") : "";
				this.TableDefaults(widget, dashboard, cellEmptyMessage);
			}
			else {
				this.WidgetDefaults(widget, dashboard, cell.ChildType);
				this.AddElement(dashboard, widget);
			}
		}
		this.AddElement(dashboard, cell);
	}

	this.WidgetDefaults = function (widget, dashboard, widgetType) {
		this.SelfInitialize(widget, ElementType.Widget);
		cfg.widgetDefaults(widget, dashboard, widgetType);
		if (widget.Tables) {
			for (var i = 0; i < widget.Tables.length; i++) {
				this.ParentInitialize(widget.Tables[i], widget);
				this.TableDefaults(widget.Tables[i], dashboard, "");
			}
		}
	}

	this.TableClass = function (noBorder, tableClass) {
		var tblCls;
		if (cfg.isEditor()) {
			tblCls = "gx-db-flex-table" + (tableClass != "" ? " " + tableClass : "");
			if (noBorder)
				tblCls += " gx-db-no-border";
		}
		else
			tblCls = "container-fluid gx-db-no-margin gx-db-no-padding" + (tableClass != "" ? " " + tableClass : "");;
		return tblCls;
	}

	this.RowClass = function (rowClass) {
		if (cfg.isEditor())
			return "gx-db-flex-row" + (rowClass != "" ? " " + rowClass : "");
		else
			return "row gx-db-no-margin" + (rowClass != "" ? " " + rowClass : "");
	}

	this.CellClass2 = function (table, row, rowIndex, cell, cellIndex) {
		if (cfg.isEditor()) {
			var cellClassBase = " gx-db-dropTarget-outside";
			if (cell.IsEmpty)
				cellClassBase += " gx-db-cell-empty";
			var bootstrapClasses = this.GetBootstrapClasses(table, rowIndex, cellIndex);
			return this.CellClass(bootstrapClasses, rowIndex < table.Rows.length - 1, cellIndex < row.Cells.length - 1, cell.CellClass != "" ? cell.CellClass : "gx-db-cell") + cellClassBase;
		}
		else {
			var bootstrapClasses = "";
			if (table.Mode == TableMode.Bootstrap)
				bootstrapClasses = this.GetBootstrapClasses(table, rowIndex, cellIndex);
			return this.CellClass(bootstrapClasses, rowIndex < table.Rows.length - 1, cellIndex < row.Cells.length - 1, cell.CellClass != "" ? cell.CellClass : "gx-db-cell");
		}
	}

	this.CellClass = function (bootstrapClasses, bottomBorder, rightBorder, cellClass) {
		var cellCls;
		if (cfg.isEditor()) {
			cellCls = (bootstrapClasses != "" ? bootstrapClasses + " " : "") + (cellClass != "" ? " " + cellClass : "");
			if (bottomBorder)
				cellCls += " gx-db-bottom-border";
			if (rightBorder)
				cellCls += " gx-db-right-border";
		}
		else
			cellCls = (bootstrapClasses != "" ? bootstrapClasses + " " : "") + "gx-db-no-padding" + (cellClass != "" ? " " + cellClass : "");
		return cellCls;
	}

	this.GetBootstrapClasses = function (table, rowIndex, cellIndex) {

		function GetBootstrapClassesForScale(table, rowIndex, cellIndex, scale) {

			function Visible(cellRS, scale) {
				if (scale == undefined)
					return true;
				else {
					if (cellRS.Visible[scale] != undefined)
						return cellRS.Visible[scale];
					else {
						var previousScale = undefined;
						switch (scale) {
							case ResponsiveScale.Small:
								previousScale = ResponsiveScale.ExtraSmall;
								break;
							case ResponsiveScale.Medium:
								previousScale = ResponsiveScale.Small;
								break;
							case ResponsiveScale.Large:
								previousScale = ResponsiveScale.Medium;
								break;
						}
						return Visible(cellRS, previousScale);
					}
				}
			}

			var row = table.Rows[rowIndex];
			var cell = row.Cells[cellIndex];
			var cellRS = cell.ResponsiveSizes;
			var classesArr = [];

			// Width
			var width;
			if (cellRS.Width[scale] != undefined)
				width = cellRS.Width[scale];
			else if (scale == ResponsiveScale.ExtraSmall)
				width = 12;
			else if (scale == ResponsiveScale.Small)
				width = parseInt(12 / row.Cells.length);
			if (width != undefined)
				classesArr.push("col-" + scale + "-" + width.toString());

			// Offset
			if (cellRS.Offset[scale] != undefined)
				classesArr.push("col-" + scale + "-offset-" + cellRS.Offset[scale].toString());
		
			// Visible
			if (!Visible(cellRS, scale))
				classesArr.push("hidden-" + scale);

			// Move
			if (cellRS.Push[scale] != undefined)
				classesArr.push("col-" + scale + "-push-" + cellRS.Push[scale].toString());
			if (cellRS.Pull[scale] != undefined)
				classesArr.push("col-" + scale + "-pull-" + cellRS.Pull[scale].toString());

			return classesArr.join(" ");

		}

		var bootstrapClasses;
		if (cfg.isEditor()) {
			var span = parseInt(12 / table.Rows[rowIndex].Cells.length);
			bootstrapClasses = "gx-db-flex-cell gx-db-flex-cell-" + span.toString();
		}
		else {
			var xsClasses = GetBootstrapClassesForScale(table, rowIndex, cellIndex, ResponsiveScale.ExtraSmall);
			var smClasses = GetBootstrapClassesForScale(table, rowIndex, cellIndex, ResponsiveScale.Small);
			var mdClasses = GetBootstrapClassesForScale(table, rowIndex, cellIndex, ResponsiveScale.Medium);
			var lgClasses = GetBootstrapClassesForScale(table, rowIndex, cellIndex, ResponsiveScale.Large);
			var classesArr = [];
			if (xsClasses != "")
				classesArr.push(xsClasses);
			if (smClasses != "")
				classesArr.push(smClasses);
			if (mdClasses != "")
				classesArr.push(mdClasses);
			if (lgClasses != "")
				classesArr.push(lgClasses);
			bootstrapClasses = classesArr.join(" ");
		}
		return bootstrapClasses;
	}

	this.TwoRowsTableHTML = function (element, divId, height, width, html1, html2) {
		var tableClass = this.TableClass(element == null, "");
		var rowClass = this.RowClass("");
		var bootstrapClasses = cfg.isEditor() ? "gx-db-flex-cell gx-db-flex-cell-12" : "col-xs-12";
		var cellClass = this.CellClass(bootstrapClasses, false, false, "");
		var tableStyle = 'height:' + height + ';width:' + width;
		var html = this.DIVStart(element, divId, tableClass, tableStyle, false, true, false);
		html += '<div class="' + rowClass + '"><div class="' + cellClass + ' gx-db-selectors-container">' + html1 + '</div></div>';
		if (cfg.isEditor())
			html += '<div class="' + rowClass + ' gx-db-flex-row-100"><div class="' + cellClass + '">' + html2 + '</div></div>';
		else
			html += '<div class="' + rowClass + ' gx-db-controls-container"><div class="' + cellClass + '" style="height:100%">' + html2 + '</div></div>';
		html += '</div>';
		return html;
	}

	this.GetTableHTML = function (dashboard, table, draggable, domIds, captureKeyboard) {

		var divId = this.GetDOMElementId(table.ElementId);
		domIds.push(divId);
		var isMainTableLayoutTable = cfg.isMainTableLayoutTable(table);
		var tableClass = this.TableClass(isMainTableLayoutTable, table.Class != "" ? table.Class : "gx-db-table");
		var tableStyle = '';
		if (table.Mode == TableMode.Grid) {
			tableStyle = "display:grid;";
			var rowHeights;
			if (table.Rows.length == 1)
				rowHeights = "1fr";
			else
			 	rowHeights = cfg.getGridRowsHeight(table);
			if (rowHeights)
				tableStyle += "grid-template-rows:" + rowHeights + ";";
			var colWidths = cfg.getGridColumnsWidth(table);
			if (colWidths)
				tableStyle += "grid-template-columns:" + colWidths + ";";
		}
		else if (table.Mode == TableMode.Flex) {
			tableStyle = "display:flex;" + cfg.getFlexTableStyle(table);
		}
		var divStart;
		divStart = this.DIVStart(table, divId, tableClass, tableStyle, draggable, !isMainTableLayoutTable, captureKeyboard);
		var html = '';
		html += divStart;
		for (var i = 0; i < table.Rows.length; i++) {
			var row = table.Rows[i];
			html += this.GetRowHTML(dashboard, table, row, i, domIds);
		}
		html += '</div>';
		return html;
	}

	this.GetRowHTML = function (dashboard, table, row, rowIndex, domIds) {

		var html = '';
		var rowClassBase = "";
		if (cfg.isEditor()) {
			rowClassBase = " gx-db-dropTarget-outside";
			if (cfg.isHeight100Percent(dashboard.Layout.MainTableLayout, rowIndex, table))
				rowClassBase += " gx-db-flex-row-100";
		}
		var rowClass = this.RowClass(row.RowClass != "" ? row.RowClass : "gx-db-row") + rowClassBase;
		var divId = this.GetDOMElementId(row.ElementId);
		domIds.push(divId);
		if (table.Mode == TableMode.Bootstrap)
			html += '<div id="' + divId + '" class="' + rowClass + '"' + '>';
		for (var i = 0; i < row.Cells.length; i++) {
			var cell = row.Cells[i];
			if (cfg.cellHasVisibleContent(cfg.isEditor(), cell)) {
				html += this.GetCellHTML(dashboard, table, row, rowIndex, cell, i, "", domIds);
			}
		}
		if (table.Mode == TableMode.Bootstrap)
			html += '</div>';
		return html;
	}

	this.GetCellHTML = function (dashboard, table, row, rowIndex, cell, cellIndex, cellStyle, domIds) {
		var html = '';
		var onDragOverFunction = "";
		var onDropFunction = "";
		var onDragLeaveFunction = "";
		var widgetPlaceholder = "";
		if (cfg.isEditor()) {
			onDragOverFunction = ' ondragover="' + cfg.getMe() + '.DragOver(event)"';
			onDropFunction = ' ondrop="' + cfg.getMe() + '.Drop(event)"';
			onDragLeaveFunction = ' ondragleave="' + cfg.getMe() + '.DragLeave(event)"';
			widgetPlaceholder = '<span class="gx-db-widget-placeholder">{0}</span>';
		}
		if (table.Mode == TableMode.Grid) {
			cellStyle = 'grid-column:' + (cellIndex+1) + '/span 1;grid-row:' + (rowIndex+1) + '/span 1;';
			if (cfg.endsWith(cell.ElementId, "_NEWROW_NEWCELL"))
				cellStyle = 'grid-column:1/span ' + table.Rows[0].Cells.length + ';grid-row:' + (rowIndex+1) + '/span 1';
			cellStyle = ' style="' + cellStyle + '"';
		}
		else if (table.Mode == TableMode.Flex) {

		}
		var cellClass = this.CellClass2(table, row, rowIndex, cell, cellIndex);
		var divId = this.GetDOMElementId(cell.ElementId);
		domIds.push(divId);
		html += '<div id="' + divId + '" class="' + cellClass + '"' + cellStyle + onDragOverFunction + onDropFunction + onDragLeaveFunction + '>';
		if (!cell.IsEmpty) {
			var widget = cell[cell.ChildType];
			if (cell.HasTable) {
				html += this.GetTableHTML(dashboard, widget, true, domIds, false);
			} else {
				html += this.GetWidgetHTML(dashboard, table, cell, widget, domIds);
			}
		}
		else if (cfg.endsWith(cell.ElementId, "_NEWROW_NEWCELL"))
			html += widgetPlaceholder.replace("{0}", table.CellEmptyMessage);
		else
			html += widgetPlaceholder.replace("{0}", "");
		html += '</div>';
		return html;
	}

	this.SetBorder = function (control, dropZone) {

		function SetDropTargetBorder(control, dropZone, noSibling) {

			function AddDropTargetClass(control, className) {
				if (!control.classList.contains(className)) {
					for (var i = control.classList.length - 1; i >= 0; i--) {
						var classNameAux = control.classList.item(i);
						if (cfg.startsWith(classNameAux, "gx-db-dropTarget")) {
							control.classList.remove(classNameAux);
							if (classNameAux == "gx-db-dropTarget-right-middle")
								control.nextSibling.classList.remove("gx-db-dropTarget-left-middle");
							if (classNameAux == "gx-db-dropTarget-left-middle")
								control.previousSibling.classList.remove("gx-db-dropTarget-right-middle");
							if (classNameAux == "gx-db-dropTarget-bottom-middle")
								control.nextSibling.classList.remove("gx-db-dropTarget-top-middle");
							if (classNameAux == "gx-db-dropTarget-top-middle")
								control.previousSibling.classList.remove("gx-db-dropTarget-bottom-middle");
							break;
						}
					}
					control.classList.add(className);
				}
			}

			if (control != null)
				switch (dropZone) {
					case DropZone.Inside:
						AddDropTargetClass(control, "gx-db-dropTarget-inside");
						break;
					case DropZone.Right:
						if (control.nextSibling == null || noSibling)
							AddDropTargetClass(control, "gx-db-dropTarget-right");
						else {
							AddDropTargetClass(control, "gx-db-dropTarget-right-middle");
							AddDropTargetClass(control.nextSibling, "gx-db-dropTarget-left-middle");
						}
						break;
					case DropZone.Left:
						if (control.previousSibling == null || noSibling)
							AddDropTargetClass(control, "gx-db-dropTarget-left");
						else {
							AddDropTargetClass(control, "gx-db-dropTarget-left-middle");
							AddDropTargetClass(control.previousSibling, "gx-db-dropTarget-right-middle");
						}
						break;
					case DropZone.Top:
						if (control.previousSibling == null || noSibling)
							AddDropTargetClass(control, "gx-db-dropTarget-top");
						else {
							AddDropTargetClass(control, "gx-db-dropTarget-top-middle");
							AddDropTargetClass(control.previousSibling, "gx-db-dropTarget-bottom-middle");
						}
						break;
					case DropZone.Bottom:
						if (control.nextSibling == null || noSibling)
							AddDropTargetClass(control, "gx-db-dropTarget-bottom");
						else {
							AddDropTargetClass(control, "gx-db-dropTarget-bottom-middle");
							AddDropTargetClass(control.nextSibling, "gx-db-dropTarget-top-middle");
						}
						break;
					default:
						AddDropTargetClass(control, "gx-db-dropTarget-outside");
						break;
				}
		}

		var tableModeGrid = true;
		if (dropZone != DropZone.Outside)
			tableModeGrid = control.parentElement.style.display == "grid" || control.parentElement.classList.contains("gx-db-widget");

		switch (dropZone) {
			case DropZone.Top:
			case DropZone.Bottom:
				if (tableModeGrid)
				{
					SetDropTargetBorder(control.parentNode, DropZone.Outside, true);
					SetDropTargetBorder(control, dropZone, true);
				}
				else
				{
					SetDropTargetBorder(control.parentNode, dropZone, false);
					SetDropTargetBorder(control, DropZone.Outside, false);
				}
				break;
			case DropZone.Inside:
			case DropZone.Right:
			case DropZone.Left:
				SetDropTargetBorder(control.parentNode, DropZone.Outside, tableModeGrid);
				SetDropTargetBorder(control, dropZone, tableModeGrid);
				break;
			case DropZone.Outside:
				SetDropTargetBorder(control.parentNode, dropZone, true);
				SetDropTargetBorder(control, dropZone, true);
				break;
		}
	}

	this.DraggableParents = function () {
		if (!this._DraggableParents) {
			this._DraggableParents = [];
		}
		return this._DraggableParents;
	}

	this.RemoveDraggableAttributes = function (elementId) {
		var draggableParents = this.DraggableParents();
		var parent = document.getElementById(this.GetDOMElementId(elementId));
		while (parent != null && !(parent instanceof HTMLDocument)) {
			if (parent.getAttribute("draggable")) {
				draggableParents.push(parent.id);
				parent.setAttribute("draggable", false);
			}
			parent = parent.parentNode;
		}
	}

	this.RestoreDraggableAttributes = function () {
		var draggableParents = this.DraggableParents();
		for (var i = 0; i < draggableParents.length; i++) {
			var parent = document.getElementById(draggableParents[i]);
			parent.setAttribute("draggable", true);
		}
		draggableParents.splice(0, draggableParents.length);
	}

	this.GetDropZone = function (ev) {

		var offset = cfg.get('#' + ev.currentTarget.id).offset();
		var x = ev.pageX - offset.left;
		var y = ev.pageY - offset.top;
		var w = ev.currentTarget.offsetWidth;
		var h = ev.currentTarget.offsetHeight;
		var y1 = x * h / w;
		var y2 = parseInt(h - x * h / w);
		if (x < 0 || y < 0 || x >= w || y >= h)
			return DropZone.Outside;
		else if (ev.currentTarget.childNodes.length == 1 && ev.currentTarget.childNodes[0].classList.contains("gx-db-widget-placeholder"))
			return DropZone.Inside;
		else if (y < y1 && y < y2)
			return DropZone.Top;
		else if (y > y1 && y > y2)
			return DropZone.Bottom;
		else if (y > y1 && y < y2)
			return DropZone.Left;
		else if (y < y1 && y > y2)
			return DropZone.Right;
	}

	this.DropInfo = function () {
		if (!this._DropInfo) {
			this._DropInfo = { DropTarget: null, DropZone: DropZone.Outside, DragUpperMostAncestorId: null };
		}
		return this._DropInfo;
	}

	this.DragOver = function (ev) {
		var dropInfo = this.DropInfo();
		if (this.CompatibleDropZone(ev, dropInfo)) {
			var dropInfo = this.DropInfo();
			ev.preventDefault();
			ev.stopPropagation();
			if (dropInfo.DropTarget != ev.currentTarget) {
				if (dropInfo.DropTarget != null)
					this.SetBorder(dropInfo.DropTarget, DropZone.Outside);
				dropInfo.DropTarget = ev.currentTarget;
			}
			var dropZone = this.GetDropZone(ev);
			dropInfo.DropZone = dropZone;
			this.SetBorder(dropInfo.DropTarget, dropInfo.DropZone);
		}
	}

	this.DragStart = function (ev, elementId) {
		ev.stopPropagation();
		if (ev.ctrlKey)
			this.SelectElement(ev, elementId, true, true);
		else if (this.SelectedElementIds.indexOf(elementId) < 0)
			this.SelectElement(ev, elementId, false, true);
		var dropInfo = this.DropInfo();
		var element = this.GetElement(elementId);
		var elementUpperMostAncestorId = cfg.getUpperMostAncestorElementId(element);
		dropInfo.DragUpperMostAncestorId = elementUpperMostAncestorId;
		if (this.SelectedElementIds.length == 1)
			ev.dataTransfer.setData("text", Command.MoveElement + "," + this.SelectedElementIds[0]);
		else
			ev.dataTransfer.setData("text", Command.MoveElements + "," + this.SelectedElementIds);
	}

	this.DragEnd = function () {
		var dropInfo = this.DropInfo();
		dropInfo.DragUpperMostAncestorId = null;
	}

	this.DragLeave = function (ev) {
		var dropZone = this.GetDropZone(ev);
		if (dropZone == DropZone.Outside)
			this.SetBorder(ev.currentTarget, DropZone.Outside);
	}

	this.Drop = function (ev) {
		ev.preventDefault();
		ev.stopPropagation();
		var dropInfo = this.DropInfo();
		this.SetBorder(dropInfo.DropTarget, DropZone.Outside);
		var data = ev.dataTransfer.getData("text");
		var dataArr = data.split(',');
		var dropTarget = dropInfo.DropTarget.id.replace(new RegExp(this.GetDOMElementId("")), "");
		if (dataArr.length == 1)
			cfg.callbacks.addKBObjects(dataArr[0], dropInfo.DropZone, dropTarget);
		else if (dataArr.length == 2 && dataArr[0] == Command.AddElement)
			cfg.callbacks.addElement(dataArr[1], dropInfo.DropZone, dropTarget);
		else if (dataArr.length == 2 && dataArr[0] == Command.MoveElement)
			cfg.callbacks.moveElement(dataArr[1], dropInfo.DropZone, dropTarget);
		else if (dataArr.length >= 2 && dataArr[0] == Command.MoveElements)
			cfg.callbacks.moveElements(dataArr.splice(1, dataArr.length - 1).join(), dropInfo.DropZone, dropTarget);
		this.DeselectAll();
	}

	this.CompatibleDropZone = function (ev, dropInfo) {
		var prefix = this.GetDOMElementId("");
		var dropCurrentTargetId = ev.currentTarget.id.replace(new RegExp(prefix), "");
		var currTargetElement = this.GetElement(dropCurrentTargetId);
		var dropCurrTargetUpperMostAncestorId = cfg.getUpperMostAncestorElementId(currTargetElement);
		return (dropCurrTargetUpperMostAncestorId == dropInfo.DragUpperMostAncestorId) || dropInfo.DragUpperMostAncestorId == null;
	}

	this.GetDOMElementId = function (elementId) {
		return cfg.getContainerControlId() + "_element_" + elementId;
	}

	this.GetPanelBodyId = function (elementId) {
		return cfg.getContainerControlId() + "_panel_body_" + elementId;
	}

	this.GetPanelCollapsibleId = function (elementId) {
		return cfg.getContainerControlId() + "_panel_collapsible_" + elementId;
	}

	this.GetDOMElementSelectorId = function () {
		return cfg.getContainerControlId() + "_selector";
	}

	this.GetErrorDiv = function () {
		var errorDivId = "gx-dashboard-error-placeholder";
		var errorDiv = document.getElementById(errorDivId);
		return errorDiv;
	}

	this.DIVStart = function (element, divId, divClasses, divStyle, draggable, selectable, captureKeyboard) {
		var draggableAtt = '';
		var dragStartFunction = '';
		var dragEndFunction = '';
		var onClickFunction = '';
		var onKeyUpFunction = '';
		var allDivClasses = divClasses;
		var tabindexAssignment;
		if (cfg.isEditor() && element != null) {
			draggableAtt = draggable ? ' draggable="true"' : '';
			dragStartFunction = draggable ? ' ondragstart="' + cfg.getMe() + '.DragStart(event,\'' + element.ElementId + '\');"' : '';
			dragEndFunction = draggable ? ' ondragend="' + cfg.getMe() + '.DragEnd();"' : '';
			onClickFunction = !selectable ? '' : ' onclick="' + cfg.getMe() + '.SelectElementClicking(event, \'' + element.ElementId + '\');" oncontextmenu="' + cfg.getMe() + '.RightClickElement(event, \'' + element.ElementId + '\');" onmouseover="' + cfg.getMe() + '.MouseOver(event, \'' + element.ElementId + '\');" onmouseout="' + cfg.getMe() + '.MouseOut(event, \'' + element.ElementId + '\');"';
			onKeyUpFunction = !captureKeyboard ? '' : ' onkeyup="' + cfg.getMe() + '.KeyUp(event);"';
			allDivClasses += (divClasses == "" ? "" : " ") + this.NotSelectedElementClass(element);
			tabindexAssignment = !captureKeyboard ? '' : ' tabindex="0"';
		}
		var idAssignment = divId != '' ? ' id="' + divId + '"' : '';
		var classAssignment = allDivClasses != '' ? ' class="' + allDivClasses + '"' : '';
		var styleAssignment = divStyle != '' ? ' style="' + divStyle + '"' : '';
		return '<div' + idAssignment + classAssignment + styleAssignment + tabindexAssignment + draggableAtt + onClickFunction + onKeyUpFunction + dragStartFunction + dragEndFunction + '>';
	}

	this.GetWidgetHTML = function (dashboard, table, cell, widget, domIds) {
		var html = this.GetWidgetContentHTML(dashboard, widget, domIds);
		var divId = this.GetDOMElementId(widget.ElementId);
		domIds.push(divId);
		var widgetStyle;
		if (table.Mode == TableMode.Grid)
			widgetStyle = cfg.getGridCellStyle(cell);
		else
			widgetStyle = cfg.widgetOverflowStr(widget);
		var cls = widget.ContainerClass == "" ? "gx-db-widget" : widget.ContainerClass;
		var widgetStart = this.DIVStart(widget, divId, cls, widgetStyle, true, true, false);
		var widgetEnd = '</div>';
		html = widgetStart + html + widgetEnd;
		return html;
	}

	this.GetWidgetContentHTML = function (dashboard, widget, domIds) {
		var tables;
		if (widget.Tables) {
			tables = [];
			for (var i = 0; i < widget.Tables.length; i++)
				tables.push(this.GetTableHTML(dashboard, widget.Tables[i], false, domIds, false));
		}
		if (cfg.widgetsExpanded()) {
			var htmlWidget = cfg.getWidgetContentHTML(dashboard, widget);
			if (widget.FrameVisible) {
				var htmlPanel = '';
				var panelBodyId = this.GetPanelBodyId(widget.ElementId);
				var panelCollapsibleId = this.GetPanelCollapsibleId(widget.ElementId);
				var clsFrame = widget.FrameClass == "" ? "gx-db-widget-frame" : widget.FrameClass;
				var clsFrameTitle = widget.FrameTitleClass == "" ? "gx-db-widget-frame-title" : widget.FrameTitleClass;
				var clsFrameBody = widget.FrameBodyClass == "" ? "gx-db-widget-frame-body" : widget.FrameBodyClass;
				var panelHeading = '';
				if (widget.FrameTitle && widget.FrameTitle != "") {
					var collapseImage = '';
					if (widget.FrameAllowCollapsing) {
						var collapseClass = widget.FrameCollapsed ? " collapsed" : "";
						collapseImage = '<div><span class="gx-db-widget-frame-collapse-image' + collapseClass + '" data-toggle="collapse" data-target="#' + panelCollapsibleId + '" onclick="' + cfg.getMe() + '.ToggleFrameCollapsed(\'' + widget.ElementId + '\');"></span></div>';
					}
					panelHeading = '<div class="panel-heading ' + clsFrameTitle + '"><div>' + cfg.replaceVariablesValuesInText(widget.FrameTitle, dashboard.Variables.Filters) + '</div>' + collapseImage + '</div>';
				}
				var style = 'position:relative;' + cfg.widgetOverflowStr(widget);
				htmlPanel += '<div class="panel panel-default gx-db-no-margin ' + clsFrame + '">';
				htmlPanel += panelHeading;
				if (widget.FrameAllowCollapsing) {
					var collapseClass = widget.FrameCollapsed ? "" : " in";
					htmlPanel += '<div id="' + panelCollapsibleId + '" class="panel-collapse collapse' + collapseClass + '">';
				}
				htmlPanel += '<div id="' + panelBodyId + '" class="panel-body ' + clsFrameBody + '" style="' + style + '">';
				htmlPanel += htmlWidget;
				htmlPanel += '</div>';
				if (widget.FrameAllowCollapsing)
					htmlPanel += '</div>';
				htmlPanel += '</div>';
				return htmlPanel;
			}
			else
				return htmlWidget;
		}
		else {
			return this.GetCollapsedWidgetHTML(widget, tables);
		}
	}

	this.GetCollapsedWidgetHTML = function (widget, tables) {
		var html = '';
		if (tables) {
			html += '<div class="gx-db-collapse-container"><div>' + this.GetElementIcon(widget) + widget.ControlName + '</div><div>';
			for (var i = 0; i < tables.length; i++) {
				html += '<div style="grid-column:' + (i+1) + '/span 1">' + tables[i] + '</div>';
			}
			html += '</div></div>';
		}
		else {
			html += '<div class="gx-db-collapsed">' + this.GetElementIcon(widget) + widget.ControlName + '</div>';
		}
		if (cfg.isEditor())
			html += cfg.getHamburgerMenuHTML(widget);
		return html;
	}

	this.SelectElementClicking = function (ev, elementId) {
		//TODO: Falta preguntar también por la tecla SHIFT
		this.SelectElement(ev, elementId, ev.ctrlKey, true);
	}

	this.ImpactsElementClass = function () {
		return "gx-db-impacts-element";
	}

	this.DependsOnElementClass = function () {
		return "gx-db-dependsOn-element";
	}

	this.NotSelectedElementClass = function (element) {
		return element.ElementType == ElementType.Table ? "gx-db-not-selected-table" : "gx-db-not-selected-element";
	}

	this.SelectedElementClass = function (element) {
		return element.ElementType == ElementType.Table ? "gx-db-selected-table" : "gx-db-selected-element";
	}

	this.NotMouseOverElementClass = function (element) {
		return element.ElementType == ElementType.Table ? "gx-db-not-mouseover-table" : "gx-db-not-mouseover-element";
	}

	this.MouseOverElementClass = function (element) {
		return element.ElementType == ElementType.Table ? "gx-db-mouseover-table" : "gx-db-mouseover-element";
	}

	this.GetElement = function (id) {
		var dashboard = cfg.dashboard;
		for (var i = 0; i < dashboard.AllElements.length; i++) {
			element = dashboard.AllElements[i];
			if (element.ElementId == id)
				return element;
		}
		return null;
	}

	this.SelectObjectItem = function (ev, elementId, itemName) {
		ev.stopPropagation();
		cfg.callbacks.selectObjectItem(elementId, itemName);
	}

	this.ToggleFrameCollapsed = function (elementId) {
		var widget = this.GetElement(elementId);
		widget.FrameCollapsed = !widget.FrameCollapsed;
		if (cfg.isEditor())
			window.external.SetFrameCollapsed(elementId, widget.FrameCollapsed);
	}

	this.RightClickElement = function(ev, elementId) {
		if (this.SelectedElementIds.indexOf(elementId) < 0)
			this.SelectElementClicking(ev, elementId);		// Right-click on an non-selected element selects it!
		else
			if (ev != null)
				ev.SelectElementExecuted = true;
	}
	
	this.SelectElement = function (ev, elementId, addToSelectedElements, updateSelectedElementPath) {
		if (ev != null && ev.SelectElementExecuted)
			return;		// No uso ev.stopPropagation porque me dejan de andar los ComboBoxes en edición pues no le llega nunca el click
		var selectedElement = null;
		var listModified;
		if (addToSelectedElements) {
			listModified = this.SelectedElementIds.indexOf(elementId) < 0;
			if (listModified)
				this.SelectedElementIds.push(elementId);
		}
		else {
			listModified = this.SelectedElementIds != [elementId];
			if (listModified)
				this.SelectedElementIds = [elementId];
			selectedElement = this.GetElement(elementId);
		}
		if (listModified) {
			var selLst = "";
			for (var i = 0; i < this.SelectedElementIds.length; i++) {
				selLst += (selLst == "" ? "" : ",") + this.SelectedElementIds[i];
			}
			cfg.callbacks.selectElements(selLst);
			this.UpdateSelection();
		}
		if (updateSelectedElementPath && selectedElement != null)
			this.UpdateSelectedElementPath(selectedElement);
		if (ev != null)
			ev.SelectElementExecuted = true;
	}

	this.SelectAll = function() {
		var dashboard = cfg.dashboard;
		this.SelectedElementIds.splice(0, this.SelectedElementIds.length);
		for (var i = 0; i < dashboard.AllElements.length; i++) {
			element = dashboard.AllElements[i];
		if (element.ElementType == ElementType.Widget || (element.ElementType == ElementType.Table && element.ParentElement.ElementType != ElementType.Layout))
			this.SelectedElementIds.push(element.ElementId);
		}
		this.UpdateSelection();
	}

	this.DeselectAll = function () {
		this.SelectedElementIds.splice(0, this.SelectedElementIds.length);
		this.UpdateSelection();
	}

	this.AddElementClass = function (div, element, classType) {
		this.RemoveAllElementClasses(div, element);
		var cls;
		switch (classType) {
			case ElementCssClass.Selected:
				cls = this.SelectedElementClass(element);
				break;
			case ElementCssClass.NotSelected:
				cls = this.NotSelectedElementClass(element);
				break;
			case ElementCssClass.DependsOn:
				cls = this.DependsOnElementClass();
				break;
			case ElementCssClass.Impacts:
				cls = this.ImpactsElementClass();
				break;
		}
		div.classList.add(cls);
	}

	this.RemoveAllElementClasses = function (div, element) {
		div.classList.remove(this.SelectedElementClass(element));
		div.classList.remove(this.NotSelectedElementClass(element));
		div.classList.remove(this.DependsOnElementClass());
		div.classList.remove(this.ImpactsElementClass());
	}

	this.UpdateSelection = function () {

		var dashboard = cfg.dashboard;

		// Marco los elementos seleccionados
		for (var i = 0; i < dashboard.AllElements.length; i++) {
			var element = dashboard.AllElements[i];
			if (element.ElementType != ElementType.Row && element.ElementType != ElementType.Cell) {
				var selectableDiv = document.getElementById(this.GetDOMElementId(element.ElementId));
				if (this.SelectedElementIds.indexOf(element.ElementId) < 0) {
					element.Selected = false;
					if (selectableDiv != null)
						this.AddElementClass(selectableDiv, element, ElementCssClass.NotSelected);
				}
				else {
					element.Selected = true;
					if (selectableDiv != null)
						this.AddElementClass(selectableDiv, element, ElementCssClass.Selected);
				}
			}
		}

		// Marco los elementos relacionados
		if (this.SelectedElementIds.length == 1) {
			var selectedElement = this.GetElement(this.SelectedElementIds[0]);
			for (var i = 0; i < dashboard.AllElements.length; i++) {
				var relatedElement = dashboard.AllElements[i];
				if (selectedElement != relatedElement && (relatedElement.ElementType == ElementType.Widget || relatedElement.ElementType == ElementType.Layout)) {
					var relatedDiv = document.getElementById(this.GetDOMElementId(relatedElement.ElementId));
					if (relatedDiv != null) {
						if (this.SelectedElementIds.length <= 1) {
							var dependsOnElement = cfg.dependsOnElement(selectedElement, relatedElement);
							var impactsElement = cfg.impactsElement(selectedElement, relatedElement);
							if (dependsOnElement)
								this.AddElementClass(relatedDiv, relatedElement, ElementCssClass.DependsOn);
							else if (impactsElement)
								this.AddElementClass(relatedDiv, relatedElement, ElementCssClass.Impacts);
							else
								this.AddElementClass(relatedDiv, relatedElement, ElementCssClass.NotSelected);
						}
						else
							this.AddElementClass(relatedDiv, relatedElement, ElementCssClass.NotSelected);
					}
				}
			}
			this.ShowReferences(selectedElement);
		}
		else
			this.HideReferences();
	}

	this.GetReferencesDiv = function () {
		var refDivId = "gx-dashboard-references-placeholder";
		var refDiv = document.getElementById(refDivId);
		return refDiv;
	}

	this.ShowReferences = function (element) {

		function DependenciesBorder(div) {
			div.classList.add("gx-db-dependencies");
			div.classList.remove("gx-db-impact");
		}

		function ImpactBorder(div) {
			div.classList.add("gx-db-impact");
			div.classList.remove("gx-db-dependencies");
		}

		function AvoidShowReferences() {
			if (!window.event)
				return true;
			else {
				var avoidedClasses = ["gx-db-filter-value", "gx-db-object-menu", "gx-db-object-menu-icon"];
				for (var i = 0; i < avoidedClasses.length; i++) {
					if (window.event.target.classList.contains(avoidedClasses[i]))
						return true;
				}
				return false;
			}
		}

		if ((element.ElementType == ElementType.Widget || element.ElementType == ElementType.Layout) && !AvoidShowReferences()) {
			var references = cfg.getElementReferences(element);
			if (references.DependenciesHTML != "" || references.ImpactHTML != "" ) {
				var refDiv = this.GetReferencesDiv();
				var html;
				if (references.DependenciesHTML != "" && references.ImpactHTML == "") {
					DependenciesBorder(refDiv);
					html = references.DependenciesHTML;
				}
				else if (references.DependenciesHTML == "" && references.ImpactHTML != "") {
					ImpactBorder(refDiv);
					html = references.ImpactHTML;
				}
				else {
					if (element.WidgetType == WidgetType.Filter) {
						ImpactBorder(refDiv);
						html = references.ImpactHTML + "<br>" + references.DependenciesHTML;
					}
					else {
						DependenciesBorder(refDiv);
						html = references.DependenciesHTML + "<br>" + references.ImpactHTML;
					}
				}
				refDiv.innerHTML = html;
				refDiv.style.display = "block";
				var offset = (element.WidgetType == WidgetType.Filter && cfg.dashboard.Layout.FiltersPosition == FiltersPosition.Right) ? -refDiv.offsetWidth : 0;
				refDiv.style.left = window.event.x + offset + "px";
				refDiv.style.top = window.event.y + "px";
				refDiv.classList.remove("gx-db-fade");
				if (this._referencesFadeTimeout)
					clearTimeout(this._referencesFadeTimeout);
				var f = cfg.getMe() + '.FadeReferences();';
				this._referencesFadeTimeout = setTimeout(f, 5000);
			}
			else
				this.HideReferences();
		}
		else
			this.HideReferences();
	}

	this.HideReferences = function () {
		var refDiv = this.GetReferencesDiv();
		refDiv.style.display = "none";
	}

	this.FadeReferences = function () {
		var refDiv = this.GetReferencesDiv();
		refDiv.classList.add("gx-db-fade");
	}

	this.UpdateSelectedElementPath = function (element) {
		var container = document.getElementById(this.GetDOMElementSelectorId());
		var html = this.GetElementSelectorHTML(element);
		container.innerHTML = html;
	}

	this.MouseOver = function (ev, elementId) {
		this.MouseCrossElementBorder(ev, elementId, true);
	}

	this.MouseOut = function (ev, elementId) {
		this.MouseCrossElementBorder(ev, elementId, false);
	}

	this.KeyUp = function (ev) {
		ev.stopPropagation();
		if (ev.keyCode == 46)
			window.external.DeleteSelectedElements();
	}

	this.MouseCrossElementBorder = function (ev, elementId, entering) {
		if (ev != null)
			ev.stopPropagation();
		if ((this._MouseOverElement == undefined && !entering) || (this._MouseOverElement != undefined && this._MouseOverElement.ElementId == elementId && entering))
		{ }	// nothing to do
		else {
			var element = this.GetElement(elementId);
			if (entering)
				this._MouseOverElement = element;
			else
				this._MouseOverElement = undefined;
			if (element != null) {
				var selectableDiv = document.getElementById(this.GetDOMElementId(element.ElementId));
				if (selectableDiv != null) {
					if (!entering) {
						selectableDiv.classList.remove(this.MouseOverElementClass(element));
						selectableDiv.classList.add(this.NotMouseOverElementClass(element));
					}
					else {
						selectableDiv.classList.remove(this.NotMouseOverElementClass(element));
						selectableDiv.classList.add(this.MouseOverElementClass(element));
					}
				}
			}
		}
	}

	this.GetElementIcon = function(element) {
		if (element.ElementType == ElementType.Widget)
			source = cfg.getElementIcon(element);
		else
			switch (element.ElementType) {
				case ElementType.Layout:
					source = "Layout.ico";
					break;
				case ElementType.Table:
					source = "Table.ico";
					break;
				case ElementType.Row:
					source = "Row.ico";
					break;
				case ElementType.Cell:
					source = "Cell.ico";
					break;
			}
			return '<img height="16" width="16" src="dashboardviewer/images/' + source + '">&nbsp;';
	}

	this.GetElementSelectorHTML = function (element) {

		function GetElementHTML(self, element, me) {
			var source = self.GetElementIcon(element);
			return '<button type="button" class="btn btn-default" onclick="' + me + '.SelectElement(event,\'' + element.ElementId + '\', false, false);" onmouseover="' + me + '.MouseOver(event,\'' + element.ElementId + '\');"  onmouseout="' + me + '.MouseOut(event,\'' + element.ElementId + '\');">' + source + (element.ElementType == ElementType.Layout ? element.ElementType : element.ControlName) + '</button>';
		}

		var tableStart = '<div class="' + this.TableClass(true, "") + '">';
		var bootstrapClasses = cfg.isEditor() ? "gx-db-flex-cell gx-db-flex-cell-12" : "col-xs-12";
		var cellClass = this.CellClass(bootstrapClasses, false, false, "");
		var rowClass = this.RowClass("");
		var rowStart = '<div id="' + this.GetDOMElementSelectorId() + '" class="' + rowClass + '"><div class="' + cellClass + '">';
		var rowEnd = '</div></div>';
		var tableEnd = '</div>';
		var html = '';
		var currentElement = element;
		var dashboard = cfg.dashboard;
		while (element != null) {
			var elementHtml = element.ElementType == ElementType.Row || element.ElementType == ElementType.Cell || element.ElementType == ElementType.Dashboard ? "" : GetElementHTML(this, element, cfg.getMe());
			html = elementHtml + html;
			element = element.ParentElement;
		}
		return tableStart + rowStart + html + rowEnd + tableEnd;
	}

	this.Initialize();
}

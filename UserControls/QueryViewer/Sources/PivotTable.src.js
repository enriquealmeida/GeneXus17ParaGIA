qv.pivot = (function () {

	function renderPivotTable(qViewer) {
		var qvClasses = qv.util.GetContainerControlClasses(qViewer);
		if (qvClasses != "")
			qv.util.SetUserControlClass(qViewer, qvClasses);
		var page = qViewer.userControlId() + "_" + qViewer.RealObjectName().replace(/,/g, "").replace(/\./g,"") + "_pivot_page";
		qViewer.pivotParams = {};
		qViewer.pivotParams.page = page;
		var content = qViewer.userControlId() + "_" + qViewer.RealObjectName().replace(/,/g, "").replace(/\./g,"") + "_pivot_content";
		qViewer.pivotParams.content = content;
		
		
		if ((qViewer._ControlRenderedTo != qViewer.RealType) || (jQuery("#"+content).length == 0)) {
			if ((qViewer._ControlRenderedTo != qViewer.RealType) && (qViewer.RealType == QueryViewerOutputType.PivotTable)) {
				var container = qv.util.dom.getEmptyContainer(qViewer);
				if (qViewer.Title) qv.util.dom.createDiv(container, page.replace("_pivot_page", "_title_div"), "", "", {}, "");
				qv.util.dom.createDiv(container, page, "", "", {}, "");
				qv.util.dom.createDiv(container, content, "", "", {}, "");
			} else {
				if (qViewer.Title) jQuery("#"+qViewer.ContainerName).find(".pivot_title_div").attr("id",page.replace("_pivot_page", "_title_div"))
				jQuery("#"+qViewer.ContainerName).find(".pivot_filter_div").attr("id",page)
				jQuery("#"+qViewer.ContainerName).find(".conteiner_table_div").attr("id",content)
			}
		}

		qViewer.pivotParams.container = qViewer.getContainerControl();
		qViewer.pivotParams.RealType = qViewer.RealType;
		qViewer.pivotParams.ObjectName = qViewer.RealObjectName();
		qViewer.pivotParams.ControlName = qViewer.ControlName;
		qViewer.pivotParams.PageSize = ((qViewer.Paging) && (qViewer.Paging != "false")) ? qViewer.PageSize : undefined;
		qViewer.pivotParams.metadata = qViewer.xml.metadata;
		var data;
		if (gx.lang.gxBoolean(qViewer.IsExternalQuery))
			data = qViewer.xml.data;
		else
			if (qViewer.RealType == QueryViewerOutputType.Table)
				data = qViewer.ServerPagingForTable ? qViewer.xml.pageData : qViewer.xml.data;
			else
				data = qViewer.ServerPagingForPivotTable ? qViewer.xml.pageData : qViewer.xml.data;
		qViewer.pivotParams.data = data;
		qViewer.pivotParams.UcId = qViewer.userControlId();
		qViewer.pivotParams.AutoResize = gx.lang.gxBoolean(qViewer.AutoResize) || gx.lang.gxBoolean(qViewer.ShrinkToFit);	// Property ShrinkToFit deprecated in GX16U3 (Issue 72724)
		qViewer.pivotParams.DisableColumnSort = qViewer.DisableColumnSort;
		qViewer.pivotParams.RememberLayout = qViewer.RememberLayout;
		qViewer.pivotParams.ShowDataLabelsIn = qViewer.ShowDataLabelsIn;
		qViewer.pivotParams.ServerPaging = qViewer.ServerPagingForTable && (!gx.lang.gxBoolean(qViewer.IsExternalQuery));
		qViewer.pivotParams.ServerPagingPivot = qViewer.ServerPagingForPivotTable && (!gx.lang.gxBoolean(qViewer.IsExternalQuery));;
		qViewer.pivotParams.ServerPagingCacheSize = 0
		qViewer.pivotParams.UseRecordsetCache = qViewer.UseRecordsetCache && !gx.lang.gxBoolean(qViewer.IsExternalQuery);
		qViewer.pivotParams.AllowSelection = qViewer.AllowSelection;
		qViewer.pivotParams.SelectLine = true;
		qViewer.pivotParams.TotalForColumns = qViewer.TotalForColumns; 
		qViewer.pivotParams.TotalForRows = qViewer.TotalForRows;
		qViewer.pivotParams.Title = qv.util.getTranslation(qViewer.Title);
		if (OAT.Loader != undefined) {
			renderJSPivot(qViewer.pivotParams, qv.collection, qViewer);
		} else {
			OAT.Loader.addListener(function () {
				renderJSPivot(qViewer.pivotParams, qv.collection, qViewer);
			});
		}
		qViewer._ControlRenderedTo = qViewer.RealType;
		if (qViewer.RealType == QueryViewerOutputType.Table)
			qv.util.hideActivityIndicator(qViewer);
	}

	function callServiceGetPageDataForTable(qViewer, t1, t2) {
		qViewer.getPageDataForTable(function (resXML, qViewer) {
			var d3 = new Date();
			var t3 = d3.getTime();
			if (!qv.util.anyError(resXML)) {
				if (resXML != "")
					qViewer.xml.pageData = resXML;
				renderPivotTable(qViewer);
			} else {
				errMsg = qv.util.getErrorFromText(resXML);
				qv.util.renderError(qViewer, errMsg);
			}
		}, [1, qViewer.PageSize, true, "", "", [], false]);
	}

	function getComponentItems(qViewer) {
		var componentItems = [];
		for (var i = 0; i < qViewer.Metadata.Axes.length; i++)
			if (qViewer.Metadata.Axes[i].IsComponent)
				componentItems.push(qViewer.Metadata.Axes[i].Name);
		for (var i = 0; i < qViewer.Metadata.Data.length; i++)
			if (qViewer.Metadata.Data[i].IsComponent)
				componentItems.push(qViewer.Metadata.Data[i].Name);
		return componentItems;
	}

	function loadContextItems(Node, excludedItems) {
		var Items = [];
		if (Node != null) {
			var itemIndex = -1;
			for (var i = 0; i < Node.childNodes.length; i++) {
				if (Node.childNodes[i].nodeName == "ITEM") {
					var itemName = Node.childNodes[i].getAttribute("name");
					if (excludedItems.indexOf(itemName) < 0) {
						itemIndex++;
						Items[itemIndex] = {};
						Items[itemIndex].Name = itemName;
						Items[itemIndex].Values = [];
						var valueIndex = -1;
						// Seek VALUES node
						var valuesNode = Node.childNodes[i]; // ITEM
						for (var j = 0; j < Node.childNodes[i].childNodes.length; j++)
							if (Node.childNodes[i].childNodes[j].nodeName == "VALUES") {
								valuesNode = Node.childNodes[i].childNodes[j];
								break;
							}
						// Seek VALUE nodes
						for (var j = 0; j < valuesNode.childNodes.length; j++)
							if (valuesNode.childNodes[j].nodeName == "VALUE") {
								valueIndex++;
								Items[itemIndex].Values[valueIndex] = (valuesNode.childNodes[j].firstChild != null ? valuesNode.childNodes[j].firstChild.nodeValue : "");
							}
					}
				}
			}
		}
		return Items;
	}

	return {

		tryToRenderPivotTable: function (qViewer) {
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
								if ((qViewer.ServerPagingForTable) && (qViewer.RealType == QueryViewerOutputType.Table) && (!gx.lang.gxBoolean(qViewer.IsExternalQuery))) {
									// Tabla con paginado en el server
									var previusStateSave = false;
									if (OAT.getStateWhenServingPaging) {
										previusStateSave = OAT.getStateWhenServingPaging(qViewer.userControlId() + '_' + qViewer.ObjectName, qViewer.ObjectName)
									}
									if ((!previusStateSave) || (!qViewer.RememberLayout)) {
											callServiceGetPageDataForTable(qViewer, t1, t2);
									} else {
										renderPivotTable(qViewer);
									}
								}
								else if ((qViewer.ServerPagingForPivotTable) && (qViewer.RealType == QueryViewerOutputType.PivotTable) && (!gx.lang.gxBoolean(qViewer.IsExternalQuery))) {
									// PivotTable con paginado en el server
									var previusStateSave = false;
									if (OAT.getStateWhenServingPaging) {
										previusStateSave = OAT.getStateWhenServingPaging(qViewer.userControlId() + '_' + qViewer.ObjectName, qViewer.ObjectName)
									}
									renderPivotTable(qViewer);
								}
								else {
									// Paginado en el cliente
									qv.services.GetDataIfNeeded(qViewer, function (resText, qViewer) {				// Servicio GetData
										if (resText != qViewer.xml.data)
											qViewer.xml.data = resText;
										var d3 = new Date();
										var t3 = d3.getTime();
										if (!qv.util.anyError(resText))
											renderPivotTable(qViewer);
										else {
											// Error en el servicio GetData
											errMsg = qv.util.getErrorFromText(resText);
											qv.util.renderError(qViewer, errMsg);
										}
									});
								}
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

		GetMetadataPivot: function (qViewer) {

			function MergeMetadatas(qViewer, designtimeMetadata, runtimeMetadata) {

				var fields = [];
				var keys = qv.util.GetOrderedElementsKeys(qViewer, runtimeMetadata);
				for (var i = 0; i < keys.length; i++) {
					var index = parseInt(keys[i].substr(6, 4));
					var runtimeField = runtimeMetadata[index];
					var designtimeField;
					var designtimeFieldCloned;
					for (var j = 0; j < designtimeMetadata.length; j++) {
						designtimeField = designtimeMetadata[j];
						if (designtimeField.Name == runtimeField.Name) {
							designtimeFieldCloned = qv.util.cloneObject(designtimeField);		// Clono para no perder los valores originales
							qv.util.MergeFields(runtimeField, designtimeFieldCloned);
							fields.push(designtimeFieldCloned);
							break;
						}
					}
				}
				return fields;

			}

			var designtimeMetadata = qv.util.GetDesigntimeMetadata(qViewer);
			if (designtimeMetadata.length > 0) {
				var xml = qViewer.oat_element.getMetadataXML();
				var runtimeMetadata = qv.pivot.GetRuntimeMetadata(xml, qViewer.RealType);
				return MergeMetadatas(qViewer, designtimeMetadata, runtimeMetadata);
			}
			else
				return [];
		},

		GetRuntimeMetadata: function (xml, type) {

			function GetTypeValuesObject(parentNode, typeNodeName, includeNodeName, withValuesNodeType, excludeTotalValue) {
				var nodeType = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(parentNode, typeNodeName));
				var values = [];
				if (nodeType == withValuesNodeType) {
					var parentNodeAux = qv.util.dom.getSingleElementByTagName(parentNode, includeNodeName);
					if (parentNodeAux != null) {
						var valueNodes = qv.util.dom.getMultipleElementsByTagName(parentNodeAux, "value");
						for (var j = 0; j < valueNodes.length; j++) {
							var value = qv.util.trim(qv.util.dom.getValueNode(valueNodes[j]));
							if (value != null && (value.toUpperCase() != "TOTAL" || !excludeTotalValue))
								values.push(value);
						}
					}
				}
				return { Type: nodeType, Values: values };
			}

			var xmlDoc = qv.util.dom.xmlDocument(xml);
			var rootElement = qv.util.dom.getSingleElementByTagName(xmlDoc, "OLAPCube");
			var elements = [];
			// Obtengo los ejes
			var domElements;
			domElements = qv.util.dom.getMultipleElementsByTagName(rootElement, "OLAPDimension");
			for (var i = 0; i < domElements.length; i++) {
				var element = {};
				var name = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(domElements[i], "name"));
				var hidden = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(domElements[i], "hidden")) == "true";
				var axisType = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(domElements[i], "axis"));
				var position = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(domElements[i], "position"));
				var summarize = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(domElements[i], "summarize"));
				var order = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(domElements[i], "order"));
				element.Name = name;
				element.Type = QueryViewerElementType.Axis;
				element.Hidden = hidden;
				element.Axis = (axisType != null && axisType != "" && axisType != "null") ? qv.util.capitalize(axisType) : null;
				element.Position = position;
				element.Order = (order != null && order != "" && order != "null") ? qv.util.capitalize(order) : null;
				if (type == QueryViewerOutputType.Table) {
					element.Filter = GetTypeValuesObject(domElements[i], "", "include", null, true);
					element.Filter.Type = QueryViewerFilterType.ShowSomeValues;
				}
				else {
					element.Subtotals = summarize == "no" ? QueryViewerSubtotals.No : QueryViewerSubtotals.Yes;
					element.Filter = GetTypeValuesObject(domElements[i], "filterType", "include", QueryViewerFilterType.ShowSomeValues, true);
					element.ExpandCollapse = GetTypeValuesObject(domElements[i], "collapseType", "includeExpand", QueryViewerExpandCollapseType.ExpandSomeValues, false);
				}
				elements.push(element);
			}
			// Obtengo los datos
			var domElements = qv.util.dom.getMultipleElementsByTagName(rootElement, "OLAPMeasure");
			for (var i = 0; i < domElements.length; i++) {
				var element = {};
				var name = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(domElements[i], "name"));
				var hidden = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(domElements[i], "hidden")) == "true";
				var position = qv.util.dom.getValueNode(qv.util.dom.getSingleElementByTagName(domElements[i], "position"));
				element.Name = name;
				element.Type = QueryViewerElementType.Datum;
				element.Hidden = hidden;
				element.Position = position;
				elements.push(element);
			}
			return elements;
		},

		GetDataPivot: function (qViewer) {
			var result = qViewer.oat_element.getDataXML()
			return result;
		},

		GetFilteredDataPivot: function (qViewer) {
			var result = qViewer.oat_element.getFilteredDataXML()
			return result;
		},

		onOAT_PIVOTDragAndDropEvent: function (qViewer, data) {
			var position;
			if (qViewer.RealType == QueryViewerOutputType.PivotTable) {
				if (IsQueryObjectPreview())
					window.external.SendText(qViewer.ControlName, data);
				if (qViewer.DragAndDrop) {
					var xml_doc = qv.util.dom.xmlDocument(data);
					var Node = qv.util.dom.selectXPathNode(xml_doc, "/DATA");
					qViewer.DragAndDropData.Name = Node.getAttribute("name");
					qViewer.DragAndDropData.Type = QueryViewerElementType.Axis;
					axis = Node.getAttribute("axis");
					qViewer.DragAndDropData.Axis = (axis == "rows" ? QueryViewerAxisType.Rows : (axis == "columns" ? QueryViewerAxisType.Columns : QueryViewerAxisType.Pages));
					qViewer.DragAndDropData.Position = Node.getAttribute("position");
					qViewer.DragAndDrop();
				}
			}
		},

		onItemClickEvent: function (qViewer, data, isDoubleClick) {
			var location;
			var eventData;
			if ((qViewer.ItemClick && !isDoubleClick) || (qViewer.ItemDoubleClick && isDoubleClick)) {
				eventData = (isDoubleClick ? eventData = qViewer.ItemDoubleClickData : qViewer.ItemClickData);
				var xml_doc = qv.util.dom.xmlDocument(data);
				var Node1 = qv.util.dom.selectXPathNode(xml_doc, "/DATA/ITEM");
				var Node2 = qv.util.dom.selectXPathNode(xml_doc, "/DATA/CONTEXT/RELATED");
				var Node3 = qv.util.dom.selectXPathNode(xml_doc, "/DATA/CONTEXT/FILTERS");
				eventData.Name = Node1.getAttribute("name");
				location = Node1.getAttribute("location");
				switch (location) {
					case "rows":
						eventData.Type = QueryViewerElementType.Axis;
						eventData.Axis = QueryViewerAxisType.Rows;
						break;
					case "columns":
						eventData.Type = QueryViewerElementType.Axis;
						eventData.Axis = QueryViewerAxisType.Columns;
						break;
					case "pages":
						eventData.Type = QueryViewerElementType.Axis;
						eventData.Axis = QueryViewerAxisType.Pages;
						break;
					default:
						eventData.Type = QueryViewerElementType.Datum;
						eventData.Axis = "";
						break;
				}
				eventData.Value = (Node1.firstChild != null ? Node1.firstChild.nodeValue : "");
				eventData.Selected = Node1.getAttribute("selected") == "true";
				var excludedItems = getComponentItems(qViewer);
				eventData.Context = loadContextItems(Node2, excludedItems);
				eventData.Filters = loadContextItems(Node3, excludedItems);
				if (isDoubleClick)
					qViewer.ItemDoubleClick();
				else
					qViewer.ItemClick();
			}
		},

		onItemExpandCollapseEvent: function (qViewer, data, isCollapse) {
			var location;
			var eventData;
			if (IsQueryObjectPreview())
				window.external.SendText(qViewer.ControlName, data);
			if ((qViewer.ItemExpand && !isCollapse) || (qViewer.ItemCollapse && isCollapse)) {
				eventData = (isCollapse ? eventData = qViewer.ItemCollapseData : qViewer.ItemExpandData);
				var xml_doc = qv.util.dom.xmlDocument(data);
				var Node1 = qv.util.dom.selectXPathNode(xml_doc, "/DATA/ITEM");
				var Node2 = qv.util.dom.selectXPathNode(xml_doc, "/DATA/CONTEXT/EXPANDEDVALUES");
				eventData.Name = Node1.getAttribute("name");
				eventData.Value = Node1.firstChild.nodeValue;
				eventData.ExpandedValues = [];
				var valueIndex = -1;
				for (var i = 0; i < Node2.childNodes.length; i++)
					if (Node2.childNodes[i].nodeName == "VALUE") {
						valueIndex++;
						eventData.ExpandedValues[valueIndex] = Node2.childNodes[i].firstChild.nodeValue;
					}
				if (isCollapse)
					qViewer.ItemCollapse();
				else
					qViewer.ItemExpand();
			}
		},

		Select: function (qViewer, selection) {
			qViewer.oat_element.selectValue(selection);
		},

		Deselect: function (qViewer) {
			qViewer.oat_element.deselectValue();
		}

	}

})()

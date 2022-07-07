qv.map = (function () {


	// ------------------------------------------------------ Common functions------------------------------------------------------

	//Establece el color a la serie, en caso de ser default toma un color de Highcharts
	function getColorSerie(qViewer) {
		if (qViewer.MapType == QueryViewerMapType.Choropleth || (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character) || (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint && qViewer.Chart.Series.DataFields[0] != "F0")) {
			if (qViewer.Chart.Series.ByIndex[0].Color.Color)
				return "#" + qViewer.Chart.Series.ByIndex[0].Color.Color;
			else
				return "#0F5B78";	// Default color for 1st serie
		} else if (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint && qViewer.Chart.Series.DataFields[0] == "F0") {			//Bubble Map con coord geopoint y dato asociado
			if (qViewer.Metadata.Axes[0].Style)
				return qViewer.Metadata.Axes[0].Style.substring(6, qViewer.Metadata.Axes[0].Style.length);
			else
				return "#0F5B78";	// Default color for 1st serie
		}
	}

	function getSerieData(qViewer) {

		function extractCoord(coordPoints) {

			var coord = coordPoints.substring(7, coordPoints.length - 1);
			for (var i = 0; i < coord.length; i++) {
				if (coord[i] == " ") {
					var lat = parseFloat(coord.substring(0, i));
					var lon = parseFloat(coord.substring(i + 1, coord.length));
				}
			}
			return [lat, lon];
		}

		var dataArray = [];
		if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character) {
			if (qViewer.Chart.Categories.DataFields.length > 0 && qViewer.Chart.Series.DataFields.length > 0) {
				for (var i = 0; i < qViewer.Chart.Categories.Values.length; i++) {
					var axisValue = qViewer.Chart.Categories.Values[i].Value;
					var datumValue = parseFloat(qViewer.Chart.Series.ByIndex[0].Points[i].Value);
					if (qViewer.MapLibrary === QueryViewerMapLibrary.ECharts)
						dataArray.push({ name: axisValue, value: datumValue });
					else
						dataArray.push([axisvalue, datumValue]);
				}
			}
		} else if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint) { //Bubble con tipo de dato geopoint

			var dataArray = [];
			var colorCond;
			var axis = qViewer.Metadata.Axes[0];
			for (var i = 0; i < qViewer.Data.Rows.length; i++) {
				var axisValue = extractCoord(qViewer.Data.Rows[i][axis.DataField]);
				var datumValue = parseFloat(qViewer.Chart.Series.ByIndex[0].Points[i].Value);
				if (qViewer.Chart.Series.DataFields[0] == "F0") { //La query no tiene un valor asociado a al coordenada un valor asociado 
					dataArray.push({ coord: [axisValue[0], axisValue[1]], additionalData: qViewer.Data.Rows[i]});
				} else { //La query tiene ademas de la coordenada un valor asociado 
					if (qViewer.Chart.colorAxis.dataClasses.length == 0) {
					dataArray.push({ coord: [axisValue[0], axisValue[1]], value: datumValue, itemStyle: { color: getColorSerie(qViewer) }, additionalData: qViewer.Data.Rows[i]});
					} else {
						colorCond = qViewer.Chart.Series.ByIndex[0].Points[i].Color.Color;
						if (checkIfIsHexColor(colorCond)) {
							colorCond = "#" + colorCond;
						}
						dataArray.push({ coord: [axisValue[0], axisValue[1]], value: datumValue, itemStyle: { color: colorCond }, additionalData: qViewer.Data.Rows[i]});

					}
				}
				
			}

		}


		return dataArray;

	}

	function SetItemClickDataMap(eventData, qViewer, name, type, value, selected, pointsValues, pointsCategories, index) {

		var axis = qViewer.Metadata.Axes[0];

		function GetContextElement(axisOrDatum, value) {
			var contextElement = {};
			contextElement.Name = axisOrDatum.Name;
			var pictureProperties = qv.util.GetPictureProperties(axisOrDatum.DataType, axisOrDatum.Picture);
			if (axisOrDatum.DataType != QueryViewerDataType.GeoPoint) {
				var formattedValue = qv.util.ApplyPicture(value, axisOrDatum.Picture, axisOrDatum.DataType, pictureProperties);
				contextElement.Values = [formattedValue];
			} else if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint && qViewer.MapType == QueryViewerMapType.Bubble) {
				contextElement.Values = value[axis.DataField];
			} else { 
				contextElement.Values = [value];
			}
			return contextElement;
		}

		eventData.Name = name;
		eventData.Type = type;
		eventData.Axis = "";
		eventData.Value = value;
		eventData.Selected = selected;
		eventData.Context = [];

		if (!axis.IsComponent) {
			if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint && qViewer.MapType == QueryViewerMapType.Bubble) {
				var contextElement = GetContextElement(axis, qViewer.Data.Rows[index]);
			} else if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint && qViewer.MapType == QueryViewerMapType.Choropleth) {
				var contextElement = GetContextElement(axis, pointsCategories[index].name);
			} else {
				var contextElement = GetContextElement(axis, pointsCategories[index].Value);
			}
			eventData.Context.push(contextElement);
		}

		for (var i = 0; i < qViewer.Metadata.Data.length; i++) {
			var datum = qViewer.Metadata.Data[i];
			if (!datum.IsComponent) {
				var contextElement;
				if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint && qViewer.MapType == QueryViewerMapType.Choropleth) {
					contextElement = GetContextElement(datum, pointsValues[index].value);
				} else {
					contextElement = GetContextElement(datum, pointsValues[index].Value);
				}
				eventData.Context.push(contextElement);
			}
		}
		eventData.Filters = [];
	}

	function initCursor(chart) {
		chart.on('mousedown', function (params) {
			chart.getZr().setCursorStyle('auto');
		});
		chart.on('mousemove', function (params) {
			chart.getZr().setCursorStyle('auto');
		});
		chart.on('mouseup', function (params) {
			chart.getZr().setCursorStyle('auto');
		});
		chart.on('mouseover', function (params) {
			chart.getZr().setCursorStyle('auto');
		});
		chart.on('mouseout', function (params) {
			chart.getZr().setCursorStyle('auto');
		});
	}

	function changeCursor(chart, qViewer) {
		chart.on('mousedown', function (params) {
			if (qViewer.MapType == QueryViewerMapType.Choropleth) {
				chart.getZr().setCursorStyle('pointer');
			} else {
				if (params.componentType === 'markPoint') {
					chart.getZr().setCursorStyle('pointer');
				} else {
					chart.getZr().setCursorStyle('auto');
				}
			}
		});
		chart.on('mousemove', function (params) {
			if (qViewer.MapType == QueryViewerMapType.Choropleth) {
				chart.getZr().setCursorStyle('pointer');
			} else {
				if (params.componentType === 'markPoint') {
					chart.getZr().setCursorStyle('pointer');
				} else {
					chart.getZr().setCursorStyle('auto');
				}
			}
		});
		chart.on('mouseup', function (params) {
			if (qViewer.MapType == QueryViewerMapType.Choropleth) {
				chart.getZr().setCursorStyle('pointer');
			} else {
				if (params.componentType === 'markPoint') {
					chart.getZr().setCursorStyle('pointer');
				} else {
					chart.getZr().setCursorStyle('auto');
				}
			}
		});
		chart.on('mouseover', function (params) {
			if (qViewer.MapType == QueryViewerMapType.Choropleth) {
				chart.getZr().setCursorStyle('pointer');
			} else {
				if (params.componentType === 'markPoint') {
					chart.getZr().setCursorStyle('pointer');
				} else {
					chart.getZr().setCursorStyle('auto');
				}
			}
		});
		chart.on('mouseout', function (params) {
			if (qViewer.MapType == QueryViewerMapType.Choropleth) {
				chart.getZr().setCursorStyle('pointer');
			} else {
				if (params.componentType === 'markPoint') {
					chart.getZr().setCursorStyle('pointer');
				} else {
					chart.getZr().setCursorStyle('auto');
				}
			}
		});
	}

	function MapsClickEvent(chart, qViewer) {
		chart.on('click', function (params) {
			if ((qViewer.MapType == QueryViewerMapType.Choropleth) || ((qViewer.MapType == QueryViewerMapType.Bubble) && (params.componentType === 'markPoint'))) {
				var serie = qViewer.Chart.Series.ByIndex[0];
				var formattedValue = qv.util.formatNumber(params.value, serie.NumberFormat.DecimalPrecision, serie.Picture, false);
				var pointsValues = qViewer.Chart.Series.ByIndex[0].Points;
				var pointsCategories;
				if (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint) {
					pointsCategories = qViewer.Data.Rows;
				} else if (qViewer.MapType == QueryViewerMapType.Choropleth && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint) {
					pointsValues = pointsCategories = geopointToCharacter(getSerieData(qViewer), chart, qViewer);
				} else {
					pointsCategories = qViewer.Chart.Categories.Values;
				}
				var selected;
				var option = chart.getOption();
				if (qViewer.SelectionAllowed()) {
					if (qViewer.MapType == QueryViewerMapType.Choropleth) {
						var point = option.series[0].selectedMap;
						if (point[Object.keys(point)[0]] == true) {
							selected = true;
						} else {
							selected = false;
						}
					} else {
						var colorBubble;
						if (qViewer.Chart.colorAxis.dataClasses.length == 0) { //cuando no tiene conditional style
							colorBubble = option.series[0].color;
						} else {
							colorBubble = qViewer.Chart.Series.ByIndex[0].Points[params.dataIndex].Color.Color;
							if (checkIfIsHexColor(colorBubble)) {
								colorBubble = "#" + colorBubble;
							}
						}
						if ((chart.BubbleSelect == params.dataIndex) && option.series[0].markPoint.data[params.dataIndex].itemStyle.color == colorBubble) {
							selected = true;
						} else if ((chart.BubbleSelect == params.dataIndex) && option.series[0].markPoint.data[params.dataIndex].itemStyle.color != colorBubble) {
							selected = false;
						} else if ((chart.BubbleSelect != params.dataIndex) && option.series[0].markPoint.data[params.dataIndex].itemStyle.color == colorBubble) {
							selected = true;
						} else {
							selected = false;
						}
					}
				} else {
					selected = false;
				}
				if (!(params.dataIndex > pointsCategories.length - 1 && Number.isNaN(params.value))) {
					SetItemClickDataMap(qViewer.ItemClickData, qViewer, serie.FieldName, QueryViewerElementType.Datum, formattedValue, selected, pointsValues, pointsCategories, params.dataIndex);
					qViewer.ItemClick();
				}
			}
		});
	}

	function bubblePoints(data, qViewer, chart) {

		function PointInPolygon(point, polygon) {

			var x = point[0], y = point[1];

			var inside = false;
			for (var i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
				var xi = polygon[i][0], yi = polygon[i][1];
				var xj = polygon[j][0], yj = polygon[j][1];

				var intersect = ((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
				if (intersect) {
					if (inside == false)
						inside = true;
					else
						inside = false;
				}
			}

			return inside;
		};

	
		function pointsInRegion(data, qViewer, chart) {

			var pointsSelected = [];

			function duplicateData(dataToVerificate) {
				var duplicate = false;

				for (var i = 0; i < pointsSelected.length; i++) {
					if (pointsSelected[i].coord === dataToVerificate.coord && pointsSelected[i].value === dataToVerificate.value) {
						duplicate = true;
					}
				}
				return duplicate;
			}

			//Verifico si pertenece a algun pais que contenga areas especiales
			if (qViewer.Region == QueryViewerRegion.Country && (qViewer.Country == QueryViewerCountry.US || qViewer.Country == QueryViewerCountry.ES || qViewer.Country == QueryViewerCountry.PT || qViewer.Country == QueryViewerCountry.FR || qViewer.Country == QueryViewerCountry.GQ || qViewer.Country == QueryViewerCountry.MU)) {

				var specialAreas = [[QueryViewerCountry.US, ['US-AK', 'US-HI', 'US-PR']], [QueryViewerCountry.ES, ['ES-CN', 'ES-IB']], [QueryViewerCountry.PT, ['PT-20', 'PT-30']], [QueryViewerCountry.FR, ['FR-GF', 'FR-RE', 'FR-GP', 'FR-MQ', 'FR-YT']], [QueryViewerCountry.GQ, ['GQ-AN', 'GQ-BS', 'GQ-BN']], [QueryViewerCountry.MU, ['MU-RO', 'MU-CC', 'MU-AG']]];
				var result;
				var xoffset;
				var yoffset;
				var width;
				var lon;
				var lat;
				var regions;
				var limitLon, limitLat;
				var areaWidth;
				var transform;

				for (var p = 0; p < specialAreas.length; p++) {
					if (specialAreas[p][0] == qViewer.Country) {
						regions = specialAreas[p];
						break;
					}
				}

				for (var i = 0; i < chart._coordSysMgr._coordinateSystems[0].regions.length; i++) {

					for (var j = 0; j < regions[1].length; j++) {

						for (var k = 0; k < data.length; k++) {

							if (chart._coordSysMgr._coordinateSystems[0].regions[i].name == regions[1][j]) {

								result = PointInPolygon(data[k].coord, chart._coordSysMgr._coordinateSystems[0].regions[i].properties.specialArea);

								if (result == true) {	//Punto pertenece a un specialArea, cambio las coordenadas

									lon = 0;
									lat = 0;
									height = chart._coordSysMgr._coordinateSystems[0].regions[i].properties.specialAreaHeight;
									xoffset = chart._coordSysMgr._coordinateSystems[0].regions[i]._rect.x;
									yoffset = chart._coordSysMgr._coordinateSystems[0].regions[i]._rect.y;
									width = chart._coordSysMgr._coordinateSystems[0].regions[i]._rect.width;

									if (chart._coordSysMgr._coordinateSystems[0].regions[i].properties.AreaWidth != undefined && chart._coordSysMgr._coordinateSystems[0].regions[i].properties.coordlimit) {

										limitLon = chart._coordSysMgr._coordinateSystems[0].regions[i].properties.coordlimit[0];
										limitLat = chart._coordSysMgr._coordinateSystems[0].regions[i].properties.coordlimit[1];
										areaWidth = chart._coordSysMgr._coordinateSystems[0].regions[i].properties.AreaWidth;
										areaHeight = chart._coordSysMgr._coordinateSystems[0].regions[i].properties.AreaHeight;
										lon = xoffset - [((limitLon - data[k].coord[0]) / areaWidth) * width];
										lat = yoffset + height - Math.abs((((limitLat - data[k].coord[1]) / areaHeight)) * height);

									} else {
										lon = data[k].coord[0] + (xoffset - data[k].coord[0]) + width;
										lat = ((((data[k].coord[1])) * (height)) / 90) + yoffset;
									}

									data[k].coord = [lon, lat];
									transform = data[k];
									transform['name'] = null;
									transform['name'] = chart._coordSysMgr._coordinateSystems[0].regions[i].name;
									pointsSelected.push(transform);
									break;
								}
							} else {

								for (var n = 0; n < chart._coordSysMgr._coordinateSystems[0].regions[i].geometries.length; n++) {

									if (PointInPolygon(data[k].coord, chart._coordSysMgr._coordinateSystems[0].regions[i].geometries[n].exterior)) {

										if (!duplicateData(data[k])) {
											transform = data[k];
											transform['name'] = null;
											transform['name'] = chart._coordSysMgr._coordinateSystems[0].regions[i].name;
											pointsSelected.push(transform);

										}
									}
								}

							}
						}
					}
				}
				return pointsSelected;

			} else if (qViewer.Region == QueryViewerRegion.World && qViewer.MapType != QueryViewerMapType.Choropleth) { //No es necesario filtrar por region ya que es un world map

				return data;

			} else { //En el caso que la region sea un continente o un pais sin areas especiales se filtra por los puntos pertenecientes a dicha region

				var find;
				var transform;

				for (var m = 0; m < data.length; m++) {

					find = false;

					for (var l = 0; l < chart._coordSysMgr._coordinateSystems[0].regions.length && find == false; l++) { //filtro por region

						for (var n = 0; n < chart._coordSysMgr._coordinateSystems[0].regions[l].geometries.length && find==false; n++) {

							if (PointInPolygon(data[m].coord, chart._coordSysMgr._coordinateSystems[0].regions[l].geometries[n].exterior)) {

								transform = data[m];
								transform['name'] = null;
								transform['name'] = chart._coordSysMgr._coordinateSystems[0].regions[l].name;
								pointsSelected.push(transform);
								find = true;
								break;
							}
						}
					}
				}

				return pointsSelected;

			}

		}

		var result = pointsInRegion(data, qViewer, chart);

		return result; 
	}



	function geopointToCharacter(data, chart, qViewer) {

		var transformData = [];
		var value = bubblePoints(data, qViewer, chart);
		var aggregationData = [];
		var resultAgg;
		

		function mergeData(dataToMerge) {
			var repetido = false;
			var values = [];

			for (var i = 0; i < aggregationData.length; i++) {
				if (aggregationData[i].name === dataToMerge.name) {
					values = aggregationData[i].additionalData;
					values.push(dataToMerge.additionalData[0]);
					aggregationData[i].additionalData = values;
					repetido = true;
				}
			}
			if (repetido == false) {
				aggregationData.push(dataToMerge);
			}
		}

		for (var j = 0; j < value.length; j++) {
			transformData.push({ name: value[j].name, value: 0 , additionalData: [value[j].additionalData] });
		}

		for (var k = 0; k < transformData.length; k++) {
			mergeData(transformData[k]);
		}

		for (var n = 0; n < aggregationData.length; n++) {
			resultAgg = qv.util.aggregateData(qViewer.Metadata.Data, aggregationData[n].additionalData, qViewer);
			aggregationData[n].value = resultAgg[qViewer.Metadata.Data[0].DataField];
		}
		
		return aggregationData;
	}


	// ------------------------------------------------------ Highcharts functions------------------------------------------------------

	//Devuelve la serie de un choroplethMap
	function choroplethMapSerie(qViewer) {

		var serie;

			serie = [
				{
					name: qv.util.getTranslation(qViewer.Title),
					type: 'map',
					borderColor: 'gray',
					colorByPoint: false,
					data: getSerieData(qViewer),
					joinBy: ['name', 'hc-key'],
					dataLabels: {
						enabled: true,
						format: '{point.name}'
					}
				}
			];
		

		return serie;
	}

	//Devuelve la serie de un BubbleMap
	function bubbleMapSerie(qViewer) {

		var series;

		//En el caso de ser BubbleData su serie va a ser distinta a si fuese un BubbleColor
		if (qViewer.Chart.colorAxis.dataClasses.length == 0) { // is a bubble data map no hay consitional style
			
			if (!qViewer.Chart.Series.ByIndex[0].Color.Color) {  //el usuario no establecio un color

				series = [{
					enableMouseTracking: false
				}, {
					name: qv.util.getTranslation(qViewer.Title),
					type: 'mapbubble',
					joinBy: ['name', 'hc-key'],
					data: getSerieData(qViewer),
					colorIndex: qViewer.Metadata.Data[0].colorIndex,
					opacity: 0.5,
					showInLegend: true,
					minSize: 4,
					maxSize: '12%'
				}
				];

			} else { // el usuario establecio un color

				series = [{
					color: '#E0E0E0',
					enableMouseTracking: false
				}, {
					name: qv.util.getTranslation(qViewer.Title),
					type: 'mapbubble',
					joinBy: ['name', 'hc-key'],
					data: getSerieData(qViewer),
					styledMode: false,
					color: getColorSerie(qViewer),
					showInLegend: true,
					minSize: 4,
					maxSize: '12%'
				}
				];


			}
			
		} else { // is a bubble color map

			series = [{
				color: '#E0E0E0',
				enableMouseTracking: false
			}, {
				name: qv.util.getTranslation(qViewer.Title),
				type: 'mapbubble',
				joinBy: ['name', 'hc-key'],
				data: getSerieData(qViewer),
				styledMode: false,
				showInLegend: true,
				minSize: 4,
				maxSize: '12%'
			}
			];

		}
		return series;
	}

	function getColorAxis(qViewer) {

		var colorAxis;
	

		if (qViewer.MapType == QueryViewerMapType.Choropleth && qViewer.Chart.colorAxis.dataClasses.length == 0) { // is a intensity color map

			colorAxis = {
				min: 0,
				maxColor: getColorSerie(qViewer)
			}
			return colorAxis;
		}
		if ((qViewer.MapType == QueryViewerMapType.Choropleth && qViewer.Chart.colorAxis.dataClasses.length != 0) || (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Chart.colorAxis.dataClasses.length != 0)) { // is a color scale map, bubble color map or point color map

			colorAxis = qViewer.Chart.colorAxis;
			return colorAxis;
		}

		if ((qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Chart.colorAxis.dataClasses.length == 0)) { // is a color scale map, bubble color map or point color map

			colorAxis = [];
			return colorAxis;
		}
	
		return colorAxis;
	}

	//Devuelve la legend de cada mapa segun el tipo que sea
	function getReferencyMap(qViewer) {

		var legend;
		

		if (qViewer.MapType == QueryViewerMapType.Choropleth) {

			legend = {
				enabled: true
			}

			return legend;

		}
		if (qViewer.MapType == QueryViewerMapType.Bubble) {

			var legend;

			if (qViewer.Chart.colorAxis.dataClasses.length != 0) { // is a bubble color map

				legend = {
					enabled: true
				}

			} else { // is a bubble data map

				legend = {
					enabled: false
				}

			}
			return legend;
		}


		return legend;

	}

	//Carga las series segun el tipo de mapa que se selecciono
	function getSerie(qViewer) {

		var serie;

		if (qViewer.MapType == QueryViewerMapType.Choropleth) {

			serie = choroplethMapSerie(qViewer);
			return serie;

		}

		if (qViewer.MapType == QueryViewerMapType.Bubble) {

			serie = bubbleMapSerie(qViewer);
			return serie;

		}

		return serie;
	}

	//Establece las tooltip para cada mapa segun el tipo que sea
	function getToolTipMaps(qViewer) {

		var tooltip;

		if (qViewer.MapType == QueryViewerMapType.Choropleth) {

			tooltip = {}
			return tooltip;

		}

		if (qViewer.MapType == QueryViewerMapType.Bubble) {

			tooltip = {}
			return tooltip;

		}


		return tooltip;
	}

	function getChartConfig(qViewer) {

		var chart;
		if ((qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Chart.Series.ByIndex[0].Color.Color) || (qViewer.MapType == QueryViewerMapType.Choropleth && qViewer.Chart.colorAxis.dataClasses.length != 0 && !qViewer.Chart.Series.ByIndex[0].Color.Color) || (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Chart.colorAxis.dataClasses.length != 0 && !qViewer.Chart.Series.ByIndex[0].Color.Color)) {
			chart = {
				map: qv.map.getMapDirectory(qViewer).mapKey,
				styledMode: false
			}
		} else {
			chart = {
				map: qv.map.getMapDirectory(qViewer).mapKey,
				styledMode: true
			}
		}

		return chart;
	}

	
	function mapaHighchartsOptions(container, qViewer) {

		// Create the chart
		Highcharts.mapChart(container, {

			chart: getChartConfig(qViewer),

			title: { text: qv.util.getTranslation(qViewer.Title) },

			mapNavigation: {
				enabled: true,
				buttonOptions: {
					verticalAlign: 'bottom'
				}
			},

			tooltip: getToolTipMaps(qViewer),

			series: getSerie(qViewer),

			colorAxis: getColorAxis(qViewer),

			legend: getReferencyMap(qViewer)

		
		});
		qViewer._ControlRenderedTo = qViewer.RealType;

	}

	// ------------------------------------------------------ Echarts functions ------------------------------------------------------

	function checkIfIsHexColor(inputString) {
		var re = /[0-9A-Fa-f]{6}/g;
		var isHex;
		if (re.test(inputString)) {
			isHex = true;
		} else {
			isHex = false;
		}
		re.lastIndex = 0;

		return isHex;
	}

	function convertData (data,chart,qViewer) {
		var res = [];
		var colorCond;

		if (qViewer.MapType == QueryViewerMapType.Choropleth && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint) {
			for (var j = 0; j < data.length; j++) {
				for (var i = 0; i < chart._coordSysMgr._coordinateSystems[0].regions.length; i++) {
					if (chart._coordSysMgr._coordinateSystems[0].regions[i].name == data[j].name) {
						if (qViewer.Chart.colorAxis.dataClasses.length != 0) {
							if (chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center != undefined) {
								colorCond = qViewer.Chart.Series.ByIndex[0].Points[j].Color.Color;
								if (checkIfIsHexColor(colorCond)) {
									colorCond = "#" + colorCond;
								}
								res.push({
									name: data[j].name,
									coord: [chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center[0], chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center[1]],
									value: data[j].value,
									itemStyle: {
										color: colorCond
									}
								});
							} else {
								res.push({
									name: data[j].name,
									coord: [chart._coordSysMgr._coordinateSystems[0].regions[i].center[0], chart._coordSysMgr._coordinateSystems[0].regions[i].center[1]],
									value: data[j].value,
									itemStyle: {
										color: colorCond
									}
								});
							}
						} else {
							if (chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center != undefined) {
								res.push({
									name: data[j].name,
									coord: [chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center[0], chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center[1]],
									value: data[j].value,
									itemStyle: {
										color: getColorSerie(qViewer)
									}
								});
							} else {
								res.push({
									name: data[j].name,
									coord: [chart._coordSysMgr._coordinateSystems[0].regions[i].center[0], chart._coordSysMgr._coordinateSystems[0].regions[i].center[1]],
									value: data[j].value,
									itemStyle: {
										color: getColorSerie(qViewer)
									}
								});
							}
						}
						break;
					}
				}
			}
		} else {
			for (var j = 0; j < qViewer.Chart.Categories.Values.length; j++) {
				for (var i = 0; i < chart._coordSysMgr._coordinateSystems[0].regions.length; i++) {
					if (chart._coordSysMgr._coordinateSystems[0].regions[i].name == qViewer.Chart.Categories.Values[j].Value) {
						if (qViewer.Chart.colorAxis.dataClasses.length != 0) {
							if (chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center != undefined) {
								colorCond = qViewer.Chart.Series.ByIndex[0].Points[j].Color.Color;
								if (checkIfIsHexColor(colorCond)) {
									colorCond = "#" + colorCond;
								}
								res.push({
									name: qViewer.Chart.Categories.Values[j].Value,
									coord: [chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center[0], chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center[1]],
									value: data[j].value,
									itemStyle: {
										color: colorCond
									}
								});
							} else {
								res.push({
									name: qViewer.Chart.Categories.Values[j].Value,
									coord: [chart._coordSysMgr._coordinateSystems[0].regions[i].center[0], chart._coordSysMgr._coordinateSystems[0].regions[i].center[1]],
									value: data[j].value,
									itemStyle: {
										color: colorCond
									}
								});
							}
						} else {
							if (chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center != undefined) {
								res.push({
									name: qViewer.Chart.Categories.Values[j].Value,
									coord: [chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center[0], chart._coordSysMgr._coordinateSystems[0].regions[i].properties.center[1]],
									value: data[j].value,
									itemStyle: {
										color: getColorSerie(qViewer)
									}
								});
							} else {
								res.push({
									name: qViewer.Chart.Categories.Values[j].Value,
									coord: [chart._coordSysMgr._coordinateSystems[0].regions[i].center[0], chart._coordSysMgr._coordinateSystems[0].regions[i].center[1]],
									value: data[j].value,
									itemStyle: {
										color: getColorSerie(qViewer)
									}
								});
							}
						}
						break;
					}
				}
			}
		}
			
		return res;
	}

	function mapaEchartsOptions(container, qViewer) {


		function getLegendObject(qViewer, data, chart) {

			function legendName(qViewer) {

				var pictureProperties = qv.util.GetPictureProperties(qViewer.Chart.Series.ByIndex[0].DataType, qViewer.Chart.Series.ByIndex[0].Picture);
				var names = [];
				var name;
				var operator;
				for (var i = 0; i < qViewer.Chart.colorAxis.dataClasses.length; i++) {
					operator = qViewer.Chart.colorAxis.dataClasses[i].Operator;
					if (operator == QueryViewerConditionOperatorSymbol.Interval) {
						name = qv.util.ApplyPicture(qViewer.Chart.colorAxis.dataClasses[i].from, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties) + operator + qv.util.ApplyPicture(qViewer.Chart.colorAxis.dataClasses[i].to, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
					} else if (operator == QueryViewerConditionOperatorSymbol.Equal) {
						name = "" + qv.util.ApplyPicture(qViewer.Chart.colorAxis.dataClasses[i].from, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
					} else if (operator == QueryViewerConditionOperatorSymbol.LessThan || operator == QueryViewerConditionOperatorSymbol.LessOrEqual) {
						name = operator + " " + qv.util.ApplyPicture(qViewer.Chart.colorAxis.dataClasses[i].to, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
					} else {
						name = operator + " " + qv.util.ApplyPicture(qViewer.Chart.colorAxis.dataClasses[i].from, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
					}
					names.push(name);
				}
				return names;
			}

			function legendSeriesBubble(qViewer, data, names, chart) {

				var series = [];
				var serieInicial;
				if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character) {
					serieInicial = {
						name: 'serie',
						type: 'map',
						markPoint: {
							nameProperty: "iso-code",
							data: data
						}
					};
				} else { //La query tiene asociado un eje del tipo geopoint
					var dataGeo = bubblePoints(data, qViewer, chart);
					var maxValue = Math.max.apply(Math, dataGeo.map(function (o) { return o.value; }));
					var container = qViewer.getContainerControl();
					var containerSize = Math.min(container.offsetWidth, container.offsetHeight);
					serieInicial = {
						name: 'serie',
						type: 'map',
						markPoint: {
							data: dataGeo,
							symbolSize: function (value) {
								if (value == undefined) {
									return 8;
								} else {
									return Math.sqrt(value / maxValue) * containerSize / 10;	// values represented by the bubble's areas (max size is 10% of the container's size)
								}
							}
						}
					};
				}
				series.push(serieInicial);


				for (var i = 0; i < qViewer.Chart.colorAxis.dataClasses.length; i++) {

					var serie = {
						name: names[i],
						type: 'map',
						data: []
					};
					series.push(serie);
				}
				return series;
			}

			function legendColors(qViewer) {
				if (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character)
					var colores = [''];
				else
					var colores = [];

				var color;
				for (var i = 0; i < qViewer.Chart.colorAxis.dataClasses.length; i++) {
					color = qViewer.Chart.colorAxis.dataClasses[i].color;
					colores.push(color);
				}
				return colores;
			}

			var legend = [];
			var names = legendName(qViewer);
			var colors = legendColors(qViewer);
			var series = legendSeriesBubble(qViewer, data, names, chart);

			legend.push(names, colors, series);
			return legend;
		}
		function getDataRange(qViewer, legendStyle) {

		function setColorSplitListMap(color) {

			var colorSplitList;

			if (color != null) { //El usuario definio color en el conditional Style 

				colorSplitList = color;

			} else { //El usuario no definio color en el conditional style

				colorSplitList = getColorSerie(qViewer);

			}

				return colorSplitList;
			}

			var dataRange;
			var pictureProperties = qv.util.GetPictureProperties(qViewer.Chart.Series.ByIndex[0].DataType, qViewer.Chart.Series.ByIndex[0].Picture);

		if ((qViewer.MapType == QueryViewerMapType.Choropleth && qViewer.Chart.colorAxis.dataClasses.length != 0) ) { //color Scale

			//le pasamos los datos de map.colorAxis tanto el color como los datos del conditionalStyle


			var splitListMap = [];
			
			for (var i = 0; i < qViewer.Chart.colorAxis.dataClasses.length; i++) {

					var splitList = {};
					var names = getLegendObject(qViewer, "", null)[0];
					if (qViewer.Chart.colorAxis.dataClasses[i].Operator == QueryViewerConditionOperatorSymbol.Interval) {
						splitList = {
							start: qViewer.Chart.colorAxis.dataClasses[i].from,
							end: qViewer.Chart.colorAxis.dataClasses[i].to,
							color: setColorSplitListMap(qViewer.Chart.colorAxis.dataClasses[i].color),
							label: names[i]
						}
					} else if (qViewer.Chart.colorAxis.dataClasses[i].Operator == QueryViewerConditionOperatorSymbol.Equal || qViewer.Chart.colorAxis.dataClasses[i].Operator == QueryViewerConditionOperatorSymbol.NotEqual) {
						splitList = {
							value: qViewer.Chart.colorAxis.dataClasses[i].from,
							color: setColorSplitListMap(qViewer.Chart.colorAxis.dataClasses[i].color),
							label: names[i]
						}
					} else if (qViewer.Chart.colorAxis.dataClasses[i].Operator == QueryViewerConditionOperatorSymbol.GreaterThan) {
						splitList = {
							start: qViewer.Chart.colorAxis.dataClasses[i].from,
							color: setColorSplitListMap(qViewer.Chart.colorAxis.dataClasses[i].color),
							label: names[i]
						}
					} else if (qViewer.Chart.colorAxis.dataClasses[i].Operator == QueryViewerConditionOperatorSymbol.GreaterOrEqual) {
						splitList = {
							start: qViewer.Chart.colorAxis.dataClasses[i].from - 1,
							color: setColorSplitListMap(qViewer.Chart.colorAxis.dataClasses[i].color),
							label: names[i]
						}
					} else if (qViewer.Chart.colorAxis.dataClasses[i].Operator == QueryViewerConditionOperatorSymbol.LessThan) {
						splitList = {
							end: qViewer.Chart.colorAxis.dataClasses[i].to,
							color: setColorSplitListMap(qViewer.Chart.colorAxis.dataClasses[i].color),
							label: names[i]
						}
					} else if (qViewer.Chart.colorAxis.dataClasses[i].Operator == QueryViewerConditionOperatorSymbol.LessOrEqual) {
						splitList = {
							end: qViewer.Chart.colorAxis.dataClasses[i].to + 1,
							color: setColorSplitListMap(qViewer.Chart.colorAxis.dataClasses[i].color),
							label: names[i]
						}
					}
					splitListMap.push(splitList);
				}
				dataRange = {
					left: 'left',
					orient: 'vertical',
					splitList: splitListMap,
					textStyle: legendStyle.TextStyle
				};

			} else if (qViewer.MapType == QueryViewerMapType.Choropleth && qViewer.Chart.colorAxis.dataClasses.length == 0) { //intensity scale

				var color = getColorSerie(qViewer);
				var maxValue;

				dataRange = {
					min: 0,
					calculable: true,
					orient: 'horizontal',
					left: 'center',
					color: [color, 'white'], //gradient color
					textStyle: legendStyle.TextStyle,
					formatter: function (name) {
						return qv.util.ApplyPicture(name, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
					}
				};


				if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character) {
					maxValue = qViewer.Chart.Series.ByIndex[0].MaxValue;
					dataRange.max = maxValue;
				}
			}
			if (qViewer.MapType == QueryViewerMapType.Choropleth) {
				if (legendStyle.BoxStyle.backgroundColor)
					dataRange.backgroundColor = legendStyle.BoxStyle.backgroundColor;
				if (legendStyle.BoxStyle.borderColor)
					dataRange.borderColor = legendStyle.BoxStyle.borderColor;
				if (legendStyle.BoxStyle.borderWidth)
					dataRange.borderWidth = legendStyle.BoxStyle.borderWidth;
			}

		return dataRange;

		}

		function getThemeDefinitions(qViewer) {

			function inheritTextStyle(fatherStyle, childStyle) {
				if (fatherStyle.fontFamily && !childStyle.fontFamily)
					childStyle.fontFamily = fatherStyle.fontFamily;
				if (fatherStyle.fontSize && !childStyle.fontSize)
					childStyle.fontSize = fatherStyle.fontSize;
				if (fatherStyle.fontStyle && !childStyle.fontStyle)
					childStyle.fontStyle = fatherStyle.fontStyle;
				if (fatherStyle.fontWeight && !childStyle.fontWeight)
					childStyle.fontWeight = fatherStyle.fontWeight;
				if (fatherStyle.color && !childStyle.color)
					childStyle.color = fatherStyle.color;
			}

			function updateTextStyle(cssStyle, textStyle) {
				if (cssStyle.fontFamily)
					textStyle.fontFamily = cssStyle.fontFamily;
				if (cssStyle.fontSize)
					textStyle.fontSize = parseFloat(cssStyle.fontSize);
				if (cssStyle.fontStyle)
					textStyle.fontStyle = cssStyle.fontStyle;
				if (cssStyle.fontWeight)
					textStyle.fontWeight = cssStyle.fontWeight;
				if (cssStyle.color)
					textStyle.color = cssStyle.color;
			}

			function updateBoxStyle(cssStyle, boxStyle) {
				if (cssStyle.backgroundColor)
					boxStyle.backgroundColor = cssStyle.backgroundColor;
				if (cssStyle.borderWidth)
					boxStyle.borderWidth = parseFloat(cssStyle.borderWidth);
				if (cssStyle.borderColor)
					boxStyle.borderColor = cssStyle.borderColor;
			}

			var textStyle = { fontFamily: "Source Sans Pro" };
			var titleTextStyle = {};
			var seriesLabelsTextStyle = {};
			var seriesLabelsBoxStyle = {};
			var legendTextStyle = {};
			var legendBoxStyle = {};
			var tooltipTextStyle = {};
			var tooltipBoxStyle = {};
			var backgroundColor = "white";
			var selectionColor = "lightcoral";
			var themeStyleSheet = qv.util.GetThemeStyleSheet(qViewer);
			var rules = themeStyleSheet.rules || themeStyleSheet.cssRules;
			var containerControlClass = qv.util.GetContainerControlClass(qViewer);
			for (var i = 0; i < rules.length; i++) {
				var cssStyle = rules[i].style;
				if (rules[i].selectorText === "." + containerControlClass)
					updateTextStyle(cssStyle, textStyle);
				else if (rules[i].selectorText === "." + containerControlClass + " .gx-qv-selected-element")
					selectionColor = cssStyle.backgroundColor;
				else if (rules[i].selectorText === "." + containerControlClass + ".qv-map")
					backgroundColor = cssStyle.backgroundColor;
				else if (rules[i].selectorText === "." + containerControlClass + " .gx-qv-title")
					updateTextStyle(cssStyle, titleTextStyle);
				else if (rules[i].selectorText === "." + containerControlClass + " .gx-qv-legend-box")
					updateBoxStyle(cssStyle, legendBoxStyle);
				else if (rules[i].selectorText === "." + containerControlClass + " .gx-qv-legend-text")
					updateTextStyle(cssStyle, legendTextStyle);
				else if (rules[i].selectorText === "." + containerControlClass + " .gx-qv-series-labels-box")
					updateBoxStyle(cssStyle, seriesLabelsBoxStyle);
				else if (rules[i].selectorText === "." + containerControlClass + " .gx-qv-series-labels-text")
					updateTextStyle(cssStyle, seriesLabelsTextStyle);
				else if (rules[i].selectorText === "." + containerControlClass + " .gx-qv-tooltip-box")
					updateBoxStyle(cssStyle, tooltipBoxStyle);
				else if (rules[i].selectorText === "." + containerControlClass + " .gx-qv-tooltip-text")
					updateTextStyle(cssStyle, tooltipTextStyle);
			}
			inheritTextStyle(textStyle, titleTextStyle);
			inheritTextStyle(textStyle, legendTextStyle);
			inheritTextStyle(textStyle, seriesLabelsTextStyle);
			inheritTextStyle(textStyle, tooltipTextStyle);
			return {
				TextStyle: textStyle,
				SelectionColor: selectionColor,
				BackgroundColor: backgroundColor,
				Title: {
					TextStyle: titleTextStyle
				},
				Legend: {
					TextStyle: legendTextStyle,
					BoxStyle: legendBoxStyle
				},
				SeriesLabels: {
					TextStyle: seriesLabelsTextStyle,
					BoxStyle: seriesLabelsBoxStyle
				},
				Tooltip: {
					TextStyle: tooltipTextStyle,
					BoxStyle: tooltipBoxStyle
				}
			};
		}

		function setLabelStyle(labelObject, style) {
			if (style.TextStyle.fontFamily)
				labelObject.fontFamily = style.TextStyle.fontFamily;
			if (style.TextStyle.fontSize)
				labelObject.fontSize = style.TextStyle.fontSize;
			if (style.TextStyle.fontStyle)
				labelObject.fontStyle = style.TextStyle.fontStyle;
			if (style.TextStyle.fontWeight)
				labelObject.fontWeight = style.TextStyle.fontWeight;
			if (style.TextStyle.color)
				labelObject.color = style.TextStyle.color;
			if (style.BoxStyle.backgroundColor)
				labelObject.backgroundColor = style.BoxStyle.backgroundColor;
			if (style.BoxStyle.borderColor)
				labelObject.borderColor = style.BoxStyle.borderColor;
			if (style.BoxStyle.borderWidth) {
				labelObject.padding = 3;
				labelObject.borderWidth = style.BoxStyle.borderWidth;
			}
		}

		function getSeriesObject(qViewer, themeDefinitions) {

			var seriesColor = getColorSerie(qViewer);
			var showValuesInMarkPoint = gx.lang.gxBoolean(qViewer.ShowValues) && qViewer.MapType === QueryViewerMapType.Bubble;
			var container = qViewer.getContainerControl();
			var containerSize = Math.min(container.offsetWidth, container.offsetHeight);

			var pictureProperties = qv.util.GetPictureProperties(qViewer.Chart.Series.ByIndex[0].DataType, qViewer.Chart.Series.ByIndex[0].Picture);

			var series = {
				type: 'map',
				map: qv.map.getMapDirectory(qViewer).mapKey,
				roam: true,
				scaleLimit: {
					min: 1
				},
				selectedMode: qViewer.SelectionAllowed() ? 'single' : false,
				label: {
					show: false,
					formatter: '{c}'
				},
				emphasis: {
					label: {
						show: false
					},
					itemStyle: {
						borderWidth: 0.5
					}
				},
				select: {
					label: {
						show: false
					},
					itemStyle: {
						borderWidth: 0.5
					}
				}
			};

			if (qViewer.MapType === QueryViewerMapType.Choropleth) {
				series.nameProperty = "iso-code";
				if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character) {
					series.data = getSerieData(qViewer);
				}
				setLabelStyle(series.label, themeDefinitions.SeriesLabels);
				if (qViewer.SelectionAllowed()) {
					series.select.itemStyle.areaColor = themeDefinitions.SelectionColor;
					series.emphasis.itemStyle.areaColor = themeDefinitions.SelectionColor;
				} else {
					series.emphasis.itemStyle.borderWidth = 1;
					series.emphasis.itemStyle.areaColor = null;
					series.emphasis.itemStyle.borderColor = qv.util.color.lightenDarkenColor(seriesColor, 50);
				}
			}
			else if (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character) {
				var maxValue = qViewer.Chart.Series.ByIndex[0].MaxValue;
				if (qViewer.Chart.colorAxis.dataClasses.length === 0)
					series.color = seriesColor;
				series.nameProperty = "iso-code";
				series.emphasis.itemStyle.areaColor = '#eeeeee';
				series.select.itemStyle.areaColor = '#eeeeee';
				series.data = getSerieData(qViewer),
					series.showLegendSymbol = false,
					series.markPoint = {
						symbol: 'circle',
						symbolSize: function (value) {
							return Math.sqrt(value / maxValue) * containerSize / 10;	// values represented by the bubble's areas (max size is 10% of the container's size)
						},
						label: {
							show: showValuesInMarkPoint,
							formatter: function (v) {
								return qv.util.ApplyPicture(v.value, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
							}
						},
						itemStyle: {
							opacity: 0.6,
							borderColor: qv.util.color.lightenDarkenColor(seriesColor, 50),
							borderWidth: 1
						},
						emphasis: {
							label: {
								show: showValuesInMarkPoint
							},
							itemStyle: {
								opacity: 0.7,
								borderColor: qv.util.color.lightenDarkenColor(seriesColor, 100),
								borderWidth: 2
							}
						}
					};
				setLabelStyle(series.markPoint.label, themeDefinitions.SeriesLabels);

			} else if (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint) {									//Bubble map con geopoint

				series.color = seriesColor;
				series.nameProperty = "iso-code";
				series.emphasis.itemStyle.areaColor = '#eeeeee';
				series.select.itemStyle.areaColor = '#eeeeee';
				series.data = [],
					series.markPoint = {
						symbol: 'circle',
						label: {
							show: showValuesInMarkPoint,
							formatter: function (v) {
								return qv.util.ApplyPicture(v.value, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
							}
						},
						itemStyle: {
							opacity: 0.6,
							borderColor: qv.util.color.lightenDarkenColor(seriesColor, 50),
							borderWidth: 1
						},
						emphasis: {
							label: {
								show: showValuesInMarkPoint
							},
							itemStyle: {
								opacity: 0.7,
								borderColor: qv.util.color.lightenDarkenColor(seriesColor, 100),
								borderWidth: 2
							}
						}
					};
				setLabelStyle(series.markPoint.label, themeDefinitions.SeriesLabels);
			}

			return series;
		}

		function getTitleObject(qViewer, titleStyle) {
			return {
				text: qv.util.getTranslation(qViewer.Title),
				left: "center",
				textStyle: titleStyle.TextStyle
			};
		}

		function getTooltipObject(qViewer, tooltipStyle) {
			var pictureProperties = qv.util.GetPictureProperties(qViewer.Chart.Series.ByIndex[0].DataType, qViewer.Chart.Series.ByIndex[0].Picture);

			var tooltipObject;
			if ((qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character) || qViewer.MapType == QueryViewerMapType.Choropleth) {
				tooltipObject = {
					trigger: 'item',
					textStyle: tooltipStyle.TextStyle,
					formatter: function (params) {
						for (var i = 0; i < chart._coordSysMgr._coordinateSystems[0].regions.length; i++) {
							if (chart._coordSysMgr._coordinateSystems[0].regions[i].name == params.name) {
								if (Number.isNaN(params.value)) {
									return chart._coordSysMgr._coordinateSystems[0].regions[i].properties.name + ' : ' + '0';
								} else {
									return chart._coordSysMgr._coordinateSystems[0].regions[i].properties.name + ' : ' + qv.util.ApplyPicture(params.value, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
								}
							}
						}
					}
				}
			} else if (qViewer.MapType == QueryViewerMapType.Bubble && qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint) {		//bubble map con geopoint
				coordData = getSerieData(qViewer);
				tooltipObject = {
					trigger: 'item',
					textStyle: tooltipStyle.TextStyle,
					formatter: function (params) {
						for (var serieName in qViewer.Chart.Series.ByName) {
							var nameValue = qViewer.Chart.Series.ByName[serieName].Name;
						}
						if (Number.isNaN(params.value)) {
							for (var i = 0; i < chart._coordSysMgr._coordinateSystems[0].regions.length; i++) {
								if (chart._coordSysMgr._coordinateSystems[0].regions[i].name == params.name) {
									return chart._coordSysMgr._coordinateSystems[0].regions[i].properties.name;
								}
							}
						}
						else {
							if (params.value != undefined) {
								return gx.getMessage("GXPL_QViewerMapLatitude") + " " + params.data.coord[0] + '<br />' + gx.getMessage("GXPL_QViewerMapLongitude") + " " + params.data.coord[1] + '<br />' + nameValue + ": " + qv.util.ApplyPicture(params.value, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
							} else {
								return gx.getMessage("GXPL_QViewerMapLatitude") + " " + params.data.coord[0] + '<br />' + gx.getMessage("GXPL_QViewerMapLongitude") + " " + params.data.coord[1];
							}
						}
					}
				}
			}

			if (tooltipStyle.BoxStyle.backgroundColor)
				tooltipObject.backgroundColor = tooltipStyle.BoxStyle.backgroundColor;
			if (tooltipStyle.BoxStyle.borderColor)
				tooltipObject.borderColor = tooltipStyle.BoxStyle.borderColor;
			if (tooltipStyle.BoxStyle.borderWidth)
				tooltipObject.borderWidth = tooltipStyle.BoxStyle.borderWidth;
			return tooltipObject;
		}

		function SetCheckMapSizeInterval(qViewer) {
			// Echarts maps don't resize automatically if the container is resized. So we check every 500 milliseconds the size of the container and resize the map accordingly
			var code = "try { qv.map.CheckMapSize(" + qViewer.me() + ") } catch (e) {}";
			qViewer.IntervalCheckChartSize = setInterval(code, 500);
		}



		if (container.offsetHeight == 0) {
			container.style.height = "400px";		// Chart library needs non-zero width and height
			container.style.overflow = 'hidden';
		}

		var chart = echarts.init(container);

		var themeDefinitions = getThemeDefinitions(qViewer);

		chart.clear();

		var option = {
			backgroundColor: themeDefinitions.BackgroundColor,
			series: getSeriesObject(qViewer, themeDefinitions),
			title: getTitleObject(qViewer, themeDefinitions.Title),
			dataRange: getDataRange(qViewer, themeDefinitions.Legend),
			tooltip: getTooltipObject(qViewer, themeDefinitions.Tooltip)
		};

		initCursor(chart);

		chart.setOption(option);

		var pictureProperties = qv.util.GetPictureProperties(qViewer.Chart.Series.ByIndex[0].DataType, qViewer.Chart.Series.ByIndex[0].Picture);

		chart.lastSelected = null;
		chart.BubbleSelect = null;

		//Cambio el cursor
		if (qViewer.SelectionAllowed()) {
			changeCursor(chart, qViewer);
		}

		if (qViewer.ItemClick && qViewer.Metadata.Data[0].RaiseItemClick) {
			MapsClickEvent(chart, qViewer);
		}

		if (qViewer.MapType == QueryViewerMapType.Bubble) {
			var data = convertData(getSerieData(qViewer), chart, qViewer);

			if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character) {
				data = convertData(getSerieData(qViewer), chart, qViewer);
			} else {
				data = getSerieData(qViewer);
			}
			var legend = getLegendObject(qViewer, data, chart);
			option = {
				color: legend[1],
				series: legend[2],
				legend: {
					orient: 'vertical',
					x: 'left',
					y: 'bottom',
					selectedMode: false,
					data: legend[0],
					textStyle: themeDefinitions.Legend.TextStyle,
					formatter: function (name) {
						return name;
					}
				}
			};

			if (qViewer.SelectionAllowed()) {
				chart.on('click', function (params) {
					if (params.componentType === 'markPoint') {
						var option = chart.getOption();
						var colorBubble;
						if (qViewer.Chart.colorAxis.dataClasses.length == 0) { //cuando no tiene conditional style
							colorBubble = option.series[0].color;
						} else {
							colorBubble = qViewer.Chart.Series.ByIndex[0].Points[params.dataIndex].Color.Color;
							if (checkIfIsHexColor(colorBubble)) {
								colorBubble = "#" + colorBubble;
							}
						}
						if (chart.lastSelected != params.dataIndex) {
							if (chart.lastSelected != null) {
								if (qViewer.Chart.colorAxis.dataClasses.length == 0) {
									option.series[0].markPoint.data[chart.lastSelected].itemStyle = { color: option.series[0].color }
								} else {
									colorLastBubble = qViewer.Chart.Series.ByIndex[0].Points[chart.lastSelected].Color.Color;
									if (checkIfIsHexColor(colorLastBubble)) {
										colorLastBubble = "#" + colorLastBubble;
									}
									option.series[0].markPoint.data[chart.lastSelected].itemStyle = { color: colorLastBubble};
								}
							}
							option.series[0].markPoint.data[params.dataIndex].itemStyle = { color: themeDefinitions.SelectionColor };
							chart.lastSelected = params.dataIndex;
							chart.BubbleSelect = params.dataIndex;
						} else {
							option.series[0].markPoint.data[params.dataIndex].itemStyle = { color: colorBubble };
							chart.lastSelected = null;
							chart.BubbleSelect = params.dataIndex;
						}
						chart.setOption(option);
					}
				});
			}

			if (qViewer.Chart.colorAxis.dataClasses.length != 0) {
				if (themeDefinitions.Legend.BoxStyle.backgroundColor)
					option.legend.backgroundColor = themeDefinitions.Legend.BoxStyle.backgroundColor;
				if (themeDefinitions.Legend.BoxStyle.borderColor)
					option.legend.borderColor = themeDefinitions.Legend.BoxStyle.borderColor;
				if (themeDefinitions.Legend.BoxStyle.borderWidth)
					option.legend.borderWidth = themeDefinitions.Legend.BoxStyle.borderWidth;
			}

			chart.setOption(option);

		} else if (qViewer.MapType == QueryViewerMapType.Choropleth) {
			var data;

			if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.GeoPoint) {
				data = geopointToCharacter(getSerieData(qViewer), chart, qViewer);
				maxValue = Math.max.apply(Math, data.map(function (o) { return o.value; }));

				option = {
					series: {
						data: data
					},
					dataRange: {
						max: maxValue
					}
				};

				
			}
			chart.setOption(option);

			if (gx.lang.gxBoolean(qViewer.ShowValues)) {
				if (qViewer.Metadata.Axes[0].DataType == QueryViewerDataType.Character) {
					dataValues = convertData(getSerieData(qViewer), chart, qViewer);
				} else {
					dataValues = convertData(data, chart, qViewer);
				}
				option = {
					series: {
						markPoint: {
							symbol: 'circle',
							symbolSize: function (v) {
								return 0;
							},
							label: {
								show: true,
								color: 'black',
								formatter: function (v) {
									return qv.util.ApplyPicture(v.value, qViewer.Chart.Series.ByIndex[0].Picture, qViewer.Chart.Series.ByIndex[0].DataType, pictureProperties);
								}
							},
							data: dataValues
						}
					}
				};
				setLabelStyle(option.series.markPoint.label, themeDefinitions.SeriesLabels);
			}
			chart.setOption(option);
			
		}
		qViewer.Charts = [chart];
		SetCheckMapSizeInterval(qViewer);
		qViewer._ControlRenderedTo = qViewer.RealType;

	}

	// ---------------------------------------------------- end  functions -------------------------------------------

	return {

		tryToRenderMap: function (qViewer) {

			function renderMap() {
				var errMsg = qv.util.ProcessDataAndMetadata(qViewer);
				if (errMsg == "") {
					var container = qViewer.getContainerControl();
					qv.util.hideActivityIndicator(qViewer);		// Must be hidden before starting to render the map in order to check the actual size of the container
					if (qViewer.MapLibrary == QueryViewerMapLibrary.Highcharts) {
						mapaHighchartsOptions(container, qViewer);
					} else { //ECharts
						var exist = echarts.getMap(qv.map.getMapDirectory(qViewer).mapKey);
						if (exist == null) {
							qv.util.renderError(qViewer, gx.getMessage("GXPL_QViewerNoMapSupport"));
						} else {
							mapaEchartsOptions(container, qViewer);
						}
					}
				}
				else
					qv.util.renderError(qViewer, errMsg);
			}

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
										renderMap();
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

		// Devuelve el directorio del mapa seleccionado para su posterior carga dinamica
		getMapDirectory: function(qViewer) {

			var dir = "QueryViewer/" + (qViewer.MapLibrary === QueryViewerMapLibrary.Highchart ? "highcharts/" : "Echarts/");
			var key;
			var mapa = {};

			if (qViewer.Region == QueryViewerRegion.Country) {

				dir = dir + "countries/" + qViewer.Country.toLowerCase() + "-all.js";
				key = "countries/" + qViewer.Country.toLowerCase() + "/" + qViewer.Country.toLowerCase() + "-all";

			} else {
				if (qViewer.Region == QueryViewerRegion.Continent) { 

					key = "custom/";
					dir = dir + "continents/";

					var keyAux;
					switch (qViewer.Continent) {
						case QueryViewerContinent.SouthAmerica:
							keyAux = "south-america";
							break;
						case QueryViewerContinent.NorthAmerica:
							keyAux = "north-america";
							break;
						default:
							keyAux = qViewer.Continent.toLowerCase();
							break;
					}
					key += keyAux;
					dir += keyAux + ".js";

				} else {
					if (qViewer.MapLibrary == QueryViewerMapLibrary.Highcharts) {
						key = "custom/world-highres";
						dir = dir + "continents/world-highres.js";
					} else { //Echarts
						key = "world";
						dir = dir + "World/world.js";
					}
				}
			}

			mapa = {
				mapKey:	key,
				mapDir:	qv.services.url.getStaticDirectory() + dir
			}

			return mapa;
		},

		GetMetadataMap: function (qViewer) {
			return qv.util.GetDesigntimeMetadata(qViewer);
		},

		GetDataMap: function (qViewer) {
			return qViewer.xml.data;
		},

		CheckMapSize: function (qViewer) {
			var container = qViewer.getContainerControl();
			if (container.offsetWidth != qViewer.Chart._lastWidth || container.offsetHeight != qViewer.Chart._lastHeight) {
				var chart = qViewer.Charts[0];
				chart.resize({ width: container.offsetWidth + 'px', height: container.offsetHeight + 'px' });
				qViewer.Chart._lastWidth = container.offsetWidth;
				qViewer.Chart._lastHeight = container.offsetHeight;
			}
		},

		IsAfricanCountry: function (country) {
			var countries = [
				"DZ", // "Algeria"
				"AO", // "Angola"
				"BJ", // "Benin"
				"BW", // "Botswana"
				"BF", // "Burkina Faso"
				"BI", // "Burundi"
				"CM", // "Cameroon"
				"CV", // "Cape Verde"
				"CF", // "Central African Republic"
				"TD", // "Chad"
				"KM", // "Comoros"
				"CD", // "Democratic Republic of the Congo"
				"DJ", // "Djibouti"
				"EG", // "Egypt"
				"GQ", // "Equatorial Guinea"
				"ER", // "Eritrea"
				"SZ", // "Eswatini"
				"ET", // "Ethiopia"
				"GA", // "Gabon"
				"GM", // "Gambia"
				"GH", // "Ghana"
				"GN", // "Guinea"
				"GW", // "Guinea Bissau" 
				"CI", // "Ivory Coast" 
				"KE", // "Kenya"
				"LS", // "Lesotho"
				"LR", // "Liberia"
				"LY", // "Libya"
				"MG", // "Madagascar"
				"MW", // "Malawi"
				"ML", // "Mali"
				"MR", // "Mauritania"
				"MU", // "Mauritius"
				"MA", // "Morocco"
				"MZ", // "Mozambique"
				"NA", // "Namibia"
				"NE", // "Niger"
				"NG", // "Nigeria"
				"CG", // "Republic of the Congo"
				"RW", // "Rwanda"
				"ST", // "São Tomé and Príncipe"
				"SN", // "Senegal"
				"SC", // "Seychelles"
				"SL", // "Sierra Leone"
				"SO", // "Somalia"
				"ZA", // "South Africa"
				"SS", // "South Sudan"
				"SD", // "Sudan"
				"TZ", // "Tanzania"
				"TG", // "Togo"
				"TN", // "Tunisia"
				"UG", // "Uganda"
				"EH", // "Western Sahara"
				"ZM", // "Zambia"
				"ZW"  // "Zimbabwe"
			];
			return countries.indexOf(country) >= 0;
		},

		IsAsianCountry: function (country) {
			var countries = [
				"AF", // "Afghanistan"
				"AZ", // "Azerbaijan"
				"AM", // "Armenia"
				"BH", // "Bahrain"
				"BD", // "Bangladesh"
				"BT", // "Bhutan"
				"BN", // "Brunei"
				"KH", // "Cambodia"
				"CN", // "China"
				"CY", // "Cyprus"
				"TL", // "East Timor"
				"GE", // "Georgia"
				"IN", // "India"
				"ID", // "Indonesia"
				"IR", // "Iran"
				"IQ", // "Iraq"
				"IL", // "Israel"
				"JP", // "Japan"
				"JO", // "Jordan"
				"KZ", // "Kazakhstan"
				"KW", // "Kuwait"
				"KG", // "Kyrgyzstan"
				"LA", // "Laos"
				"LB", // "Lebanon"
				"MY", // "Malaysia"
				"MV", // "Maldives"
				"MN", // "Mongolia"
				"MM", // "Myanmar"
				"NP", // "Nepal"
				"KP", // "North Korea"
				"OM", // "Oman"
				"PK", // "Pakistan"
				"PS", // "Palestine"
				"PH", // "Philippines"
				"QA", // "Qatar"
				"RU", // "Russia"
				"SA", // "Saudi Arabia"
				"SG", // "Singapore"
				"KR", // "South Korea"
				"LK", // "Sri Lanka"
				"PS", // "State of Palestine"
				"SY", // "Syria"
				"TW", // "Taiwan"
				"TJ", // "Tajikistan"
				"TH", // "Thailand"
				"TR", // "Turkey"
				"TM", // "Turkmenistan"
				"AE", // "United Arab Emirates"
				"UZ", // "Uzbekistan"
				"VN", // "Vietnam"
				"YE"  // "Yemen"
			];
			return countries.indexOf(country) >= 0;
		},

		IsEuropeanCountry: function (country) {
			var countries = [
				"AL", // "Albania"
				"AD", // "Andorra"
				"AT", // "Austria"
				"AZ", // "Azerbaijan"
				"AM", // "Armenia"
				"BY", // "Belarus"
				"BE", // "Belgium"
				"BA", // "Bosnia and Herzegovina"
				"BG", // "Bulgaria"
				"HR", // "Croatia"
				"CY", // "Cyprus"
				"CZ", // "Czech Republic"
				"DK", // "Denmark"
				"EE", // "Estonia"
				"FO", // "Faroe Islands"
				"FI", // "Finland"
				"FR", // "France"
				"GE", // "Georgia"
				"DE", // "Germany"
				"GR", // "Greece"
				"HU", // "Hungary"
				"IS", // "Iceland"
				"IE", // "Ireland"
				"IT", // "Italy"
				"XK", // "Kosovo"
				"LV", // "Latvia"
				"LI", // "Liechtenstein"
				"LT", // "Lithuania"
				"LU", // "Luxembourg"
				"MT", // "Malta"
				"MD", // "Moldova"
				"MC", // "Monaco"
				"ME", // "Montenegro"
				"NL", // "Netherlands"
				"MK", // "North Macedonia"
				"NO", // "Norway"
				"PL", // "Poland"
				"PT", // "Portugal"
				"RO", // "Romania"
				"RU", // "Russia"
				"SM", // "San Marino"
				"RS", // "Serbia"
				"SK", // "Slovakia"
				"SI", // "Slovenia"
				"ES", // "Spain"
				"SE", // "Sweden"
				"CH", // "Switzerland"
				"TR", // "Turkey"
				"UA", // "Ukraine"
				"GB", // "United Kingdom"
				"VA"  // "Vatican City"
			];
			return countries.indexOf(country) >= 0;
		},

		IsNorthAmericanCountry: function (country) {
			var countries = [
				"AI", // "Anguilla"
				"AG", // "Antigua and Barbuda"
				"BB", // "Barbados"
				"BZ", // "Belize"
				"BM", // "Bermuda"
				"VG", // "British Virgin Islands"
				"CA", // "Canada"
				"CR", // "Costa Rica"
				"CU", // "Cuba"
				"DM", // "Dominica"
				"DO", // "Dominican Republic"
				"SV", // "El Salvador"
				"GL", // "Greenland"
				"GD", // "Grenada"
				"GT", // "Guatemala"
				"HT", // "Haiti"
				"HN", // "Honduras"
				"JM", // "Jamaica"
				"MX", // "Mexico"
				"NI", // "Nicaragua"
				"PA", // "Panama"
				"PR", // "Puerto Rico"
				"LC", // "Saint Lucia"
				"KN", // "Saint Kitts and Nevis"
				"VC", // "Saint Vincent and the Grenadines"
				"BS", // "The Bahamas"
				"US", // "United States of America"
				"TT"  // "Trinidad and Tobago"
			];
			return countries.indexOf(country) >= 0;
		},

		IsOceanianCountry: function (country) {
			var countries = [
				"AU", // "Australia"
				"CK", // "Cook Islands"
				"FJ", // "Fiji"
				"ID", // "Indonesia"
				"KI", // "Kiribati"
				"MY", // "Malaysia"
				"MH", // "Marshall Islands"
				"FM", // "Micronesia"
				"NR", // "Nauru"
				"NC", // "New Caledonia"
				"NZ", // "New Zealand"
				"PW", // "Palau"
				"PG", // "Papua New Guinea"
				"PH", // "Philippines"
				"WS", // "Samoa"
				"SB", // "Solomon Islands"
				"TK", // "Tokelau"
				"TO", // "Tonga"
				"TV", // "Tuvalu"
				"VU"  // "Vanuatu"
			];
			return countries.indexOf(country) >= 0;
		},

		IsSouthAmericanCountry: function (country) {
			var countries = [
				"AR", // "Argentina"
				"BO", // "Bolivia"
				"BR", // "Brazil"
				"CL", // "Chile"
				"CO", // "Colombia"
				"EC", // "Ecuador"
				"GF", // "French Guiana"
				"GY", // "Guyana"
				"PY", // "Paraguay"
				"PE", // "Peru"
				"SR", // "Suriname"
				"TT", // "Trinidad and Tobago"
				"UY", // "Uruguay"
				"VE"  // "Venezuela"
			];
			return countries.indexOf(country) >= 0;
		}

	}
}) ()




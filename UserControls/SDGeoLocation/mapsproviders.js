function gxGoogleShow() {
	var TYPE_NORMAL = 'Standard';
	var TYPE_SATELLITE = 'Satellite';
	var TYPE_HYBRID = 'Hybrid';
	var TYPE_TERRAIN = 'Terrain';

	var markers = [];
	var polygons = [];
	var lastMarker = 0;
	var defaultLat = "-34.878334";
	var defaultLong = "-56.077964";
	var mapWidget; // Google Map Widget
	var mapUserControl; // GeneXus Map User Control
	var mapContainer;
	var tsp;

	/************************ GOOGLE PROVIDER CODE **********************************/
	/********************************************************************************/

	this.getMarkers = function () {
		return markers;
	};
	
	this.clear = function () {
		clearAllPoints();
	};
	
	this.updateGeoJson = function (geoJsonData) {
		DrawGeoJson(geoJsonData, "0", "0", AfterMarkerDrawn);
	};

	this.GoogleShow = function (GoogleMapControl) {
		mapUserControl = GoogleMapControl;
		//Map Options Config
		var mapType = ControlMapSetType(mapUserControl.MapType);
		//var myLatlng = DrawGeoJson(GoogleMapControl.wkt, GoogleMapControl.Latitude,GoogleMapControl.Longitude);
		var mapPrecision = mapUserControl.MapZoom; //parseInt(mapUserControl.GoogleMap.MapZoom || ...
		var mapScrollwheel = false; //gx.lang.gxBoolean(mapUserControl.ScrollWheel);
		var mapScaleControl = true;
		var mapTypeControlVisible = true;

		var myOptions = {
			//center: myLatlng,
			mapTypeId: mapType,
			zoom: mapPrecision,
			mapTypeControl: mapTypeControlVisible,
			scaleControl: mapScaleControl,
			navigationControl: true,
			scrollwheel: mapScrollwheel
		};
		if (!mapUserControl.Ready) {
			var container = document.getElementById(mapUserControl.ContainerName);
			container.style.width = "300px"; //gx.dom.addUnits(mapUserControl.Width);
			container.style.height = "240px" //gx.dom.addUnits(mapUserControl.Height);
			mapContainer = document.createElement("div");
			mapContainer.setAttribute("id", mapUserControl.ContainerName + "_MAP");
			mapContainer.style.width = "100%";
			mapContainer.style.height = "100%";
			container.appendChild(mapContainer);
			mapUserControl.Ready = true;

			mapWidget = new google.maps.Map(mapContainer, myOptions);
			mapWidget.setOptions(myOptions);
			
			
			//////////////////////////////////CUSTOM
			var Buttoncontainer = document.createElement('div');
			var Buttoncontrol = new MypositionControl(Buttoncontainer, mapWidget);
			Buttoncontainer.index = 1;
			mapWidget.controls[google.maps.ControlPosition.TOP_RIGHT].push(Buttoncontainer);
			////////////////////////////////
			
		}
		
		//clearAllPoints();
		// Add Marker    
		DrawGeoJson(GoogleMapControl.wkt, "0", "0", AfterMarkerDrawn);
	}

	function AfterMarkerDrawn(myMarker) {
		if (myMarker !== null) {
			markers.push(myMarker);
			setMapCenter(myMarker);

			initializeMapHandlers();
		}
		else {
			console.trace("Error: Received a null marker");
		}
	};

	// Set Map Center
	function setMapCenter(currentMarker) {
		//Set center in the last marker
		if (currentMarker instanceof google.maps.Marker) {
			mapWidget.setCenter(currentMarker.position);
		} else if (currentMarker instanceof google.maps.Polygon) {
			var vertices = currentMarker.getPath();
			var pCenter = getCenter(vertices);
			mapWidget.setCenter(pCenter);
		} else if (currentMarker instanceof google.maps.Polyline) {
			var vertices = currentMarker.getPath();
			var pCenter = getCenter(vertices);
			mapWidget.setCenter(pCenter);
		}
	}

	function getCenter(vertices) {

		var xmax = null;
		var xmin = null;
		var ymax = null;
		var ymin = null;

		for (var i = 0; i < vertices.getLength(); i++) {
			var xy = vertices.getAt(i);
			xmax = (xy.lng() > xmax || xmax == null) ? xy.lng() : xmax;
			xmin = (xy.lng() < xmin || xmin == null) ? xy.lng() : xmin;
			ymax = (xy.lat() > ymax || ymax == null) ? xy.lat() : ymax;
			ymin = (xy.lat() < ymin || ymin == null) ? xy.lat() : ymin;
		}
		var centerx = (xmax + xmin) / 2.0
		var centery = (ymax + ymin) / 2.0
		var pCenter = new google.maps.LatLng(centery.toString(), centerx.toString());
		return pCenter;
	}

	////////////////Config Marker Listeners

	function initializeMapHandlers() {
		////////////////Config Map Listeners
		google.maps.event.addListener(mapWidget, 'zoom_changed', function() {
			var zoomLevel = mapWidget.getZoom();
			if (zoomLevel == 0) {
				zoomLevel = 10;
				mapWidget.setZoom(zoomLevel);
			}
		});
		

		// WA to avoid gray areas because of containers being resized, when navigating with SPA
		setTimeout(function () {
			var currCenter = mapWidget.getCenter();
   		 	google.maps.event.trigger(mapWidget, "resize");
   			mapWidget.setCenter(currCenter);
		}, 500);
 
		
		google.maps.event.addListener(mapWidget, 'dblclick', function(event) {
			var location = event.latLng;
			clearAllPoints();
			var pointIcon = GoogleGetIcon(pointIcon);

			var markerOpts = {
				position: location,
				map: mapWidget,
				icon: pointIcon,
				draggable: true,
				clickable: false,
				title: ""
			};
			var marker = createMarker(markerOpts);

			//Replace Marker object
			markers.push(marker);
			mapWidget.setCenter(location);

			SetMarkerDragEndListeners(marker);
			if (mapUserControl.Click)
				mapUserControl.Click();
		});
	}

	function ControlSetLocation(location) {
		//mapUserControl.SetClickLatitude(location.lat().toString());
		//mapUserControl.SetClickLongitude(location.lng().toString());
		if (document.getElementById(mapUserControl.InformationControl.toUpperCase()))
			document.getElementById(mapUserControl.InformationControl.toUpperCase()).innerHTML = location;
	}

	function clearAllPoints() {
		for (var i = 0; i < markers.length; i++) {
			markers[i].setMap(null);
		}
		markers = [];
	}

	function SetMarkerDragEndListeners(marker) {
		//Allows drag and set the latLng when dragend
		google.maps.event.addListener(marker, 'dragend', function(event) {
			var location = event.latLng;
			marker.map.setCenter(location);
			ControlSetLocation(location);
		});
	}

	function createInfoWin(position) {
		var infowin2 = "<B>Latitude: </B>" + position.lat() + "<Br>";
		infowin2 += "<B>Longitude: </B>" + position.lng() + "<Br>";
		return infowin2;
	}

	function createMarker(markerOpts) {
		infowin2 = createInfoWin(markerOpts.position);;
		var infowindow = new google.maps.InfoWindow({
			content: infowin2,
			position: markerOpts.position
		});

		var marker = new google.maps.Marker(markerOpts);

		google.maps.event.addListener(marker, 'dblclick', function() {
			infowindow.open(mapWidget, marker);
		});
		return marker;
	}

	function DrawGeoJson(wkt, defLat, defLong, callbackFinish) {
		var myLatLng;
		var geoJson;
		var marker;
		var isCtrlLatNull = true;
		var pointClickable = true;
		var pointVisible = true;
		var pointIcon = "";

		if (!gx.lang.emptyObject(wkt)) {
			geoJson = wellknown.parse(wkt);
			if (geoJson) {
				switch (geoJson.type) {
					case "LineString":
						var lineCoords = []
						geoJson.coordinates.forEach(function(point) {
							lineCoords.push({
								lat: parseFloat(point[1]),
								lng: parseFloat(point[0])
							})
						});
						myLatLng = new google.maps.Polyline({
							path: lineCoords,
							geodesic: true,
							strokeColor: '#FF0f0f',
							strokeOpacity: 1.0,
							strokeWeight: 2
						});
						myLatLng.setMap(mapWidget);
						marker = myLatLng
						break;
					case "Polygon":
						var polyCoords = []
						geoJson.coordinates.forEach(function(pointList) {
							pointList.forEach(function(point) {
								polyCoords.push({
									lat: parseFloat(point[1]),
									lng: parseFloat(point[0])
								})
							})
						})
						myLatLng = new google.maps.Polygon({
							path: polyCoords,
							strokeColor: '#ff0f0f',
							strokeOpacity: 0.8,
							strokeWeight: 2,
							fillColor: '#ff0f0f',
							fillopacity: 0.35
						});
						myLatLng.setMap(mapWidget);
						marker = myLatLng
						break;
					case "Point":
						myLatLng = new google.maps.LatLng(geoJson.coordinates[1].toString(), geoJson.coordinates[0].toString());
						pointIcon = GoogleGetIcon(pointIcon);
						var infowin = createInfoWin(myLatLng);
						marker = new google.maps.Marker({
							position: myLatLng,
							map: mapWidget,
							icon: pointIcon,
							clickable: pointClickable,
							visible: pointVisible,
							htmlinfo: infowin,
							draggable: true
						});
						break;
					case "GeometryCollection": 
						myLatLng = new google.maps.LatLng(defaultLat, defaultLong);
						pointIcon = GoogleGetIcon(null);
						var infowin = createInfoWin(myLatLng);
						marker = new google.maps.Marker({
							position: myLatLng,
							map: mapWidget,
							icon: pointIcon,
							clickable: pointClickable,
							visible: pointVisible,
							htmlinfo: infowin,
							draggable: true
						});
						break;
				}
				isCtrlLatNull = false;
				callbackFinish(marker);
				return;
			}
		}
		if (isCtrlLatNull) {
			var callback = function (myLatLng) {
				var infowin = createInfoWin(myLatLng);
				var pointIcon2 = GoogleGetIcon(pointIcon);
				var markerOpt1 = {
					position: myLatLng,
					map: mapWidget,
					icon: pointIcon2,
					visible: pointVisible,
					draggable: true,
					clickable: false,
					htmlinfo: infowin
				};
				
				var marker = createMarker(markerOpt1);
				callbackFinish(marker);
				return;
			};
			
			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(
				function(pos) {
					myLatLng = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
					callback(myLatLng);
				}, function() {
					myLatLng = new google.maps.LatLng(defaultLat, defaultLong);
					callback(myLatLng);
					gx.util.alert.showError("Cannot get location from non-secure site");
				}
				);
				return;
			}
			else {
				var latitude = defLat || "0";
				var longitude = defLong || "0";
				myLatLng = new google.maps.LatLng(latitude, longitude);
				callback(myLatLng);
				return;
	}
		}
		
		callbackFinish(null);
	}

	function GetGoogleMapData(GoogleMapControl) {
		GoogleMapControl.GoogleMap.MapZoom = mapWidget.getZoom();
		GoogleMapControl.GoogleMap.MapLatitude = mapWidget.center.lat();
		GoogleMapControl.GoogleMap.MapLongitude = mapWidget.center.lng();
		GoogleMapControl.GoogleMap.MapType = ControlMapGetType(mapWidget.mapTypeId);
		return GoogleMapControl.GoogleMap;
	}

	function ControlMapGetType(GoogleType) {
		// Map Type
		var return_type;

		switch (GoogleType) {
			case google.maps.MapTypeId.ROADMAP:
				return_type = TYPE_NORMAL;
				break;
			case google.maps.MapTypeId.SATELLITE:
				return_type = TYPE_SATELLITE;
				break;
			case google.maps.MapTypeId.HYBRID:
				return_type = TYPE_HYBRID;
				break;
			case google.maps.MapTypeId.TERRAIN:
				return_type = TYPE_TERRAIN;
				break;
		}
		return return_type;
	}


	function ControlMapSetType(Type) {
		// Map Type
		var return_type;
		switch (Type) {
			case TYPE_NORMAL:
				return_type = google.maps.MapTypeId.ROADMAP;
				break;
			case TYPE_SATELLITE:
				return_type = google.maps.MapTypeId.SATELLITE;
				break;
			case TYPE_HYBRID:
				return_type = google.maps.MapTypeId.HYBRID;
				break;
			case TYPE_TERRAIN:
				return_type = google.maps.MapTypeId.TERRAIN;
				break;
		}
		return return_type;
	}

	function GoogleGetIcon(pointicon) {
		iconImage = gx.util.resourceUrl(gx.basePath + gx.staticDirectory + "SDGeoLocation/gpx-icon-end.png", true); 
		markerImage = new google.maps.MarkerImage(iconImage,
			new google.maps.Size(71, 71),
			new google.maps.Point(0, 0),
			new google.maps.Point(17, 34),
			new google.maps.Size(25, 40));
		return markerImage;
	}
	
	function MypositionControl(controlDiv, map) {

		  // Set CSS for the control border.
		  var controlUI = document.createElement('div');
		  controlUI.style.backgroundColor = '#fff';
		  controlUI.style.border = '2px solid #fff';
		  controlUI.style.borderRadius = '3px';
		  controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
		  controlUI.style.cursor = 'pointer';
		  controlUI.style.marginBottom = '2px';
		  controlUI.style.textAlign = 'center';
		  controlUI.title = 'Click to My Location';
		  controlUI.style.rigth = 0 ; 
		  controlDiv.appendChild(controlUI);

		  // Set CSS for the control interior.
		  var controlText = document.createElement('div');
		  controlText.style.color = 'rgb(25,25,25)';
		  controlText.style.lineHeight = '8px';
		  controlText.style.paddingLeft = '5px';
		  controlText.style.paddingRight = '5px';
		  controlText.innerHTML = '<img src="' +gx.util.resourceUrl(gx.basePath + gx.staticDirectory + "SDGeoLocation/mylocation.jpg", true) + '">';
		  controlUI.appendChild(controlText);
		  
		  var callAux = function (myLatLng) {
				clearAllPoints();
				var infowin = createInfoWin(myLatLng);
				var pointIcon3 = GoogleGetIcon(pointIcon3);
				var markerOpt1 = {
					position: myLatLng,
					map: mapWidget,
					icon: pointIcon3,
					visible: true,
					draggable: true,
					clickable: false,
					htmlinfo: infowin
				};
				var marker = createMarker(markerOpt1);
				AfterMarkerDrawn(marker);
				return;
			};
		  
		  controlUI.addEventListener('dblclick', function() {
			  /////////////
				if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(
				function(pos) {
					myLatLng = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
					callAux(myLatLng);
				}, function() {
					myLatLng = new google.maps.LatLng(defaultLat, defaultLong);
					callAux(myLatLng);
				}
				);
				return;
			}
			///////////
		  });

	}

}
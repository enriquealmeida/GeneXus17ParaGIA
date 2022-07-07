var gxmapinstances = [];
var version = "3.6"; 
var gxmapscriptLoading = false;
function gxMap() {
	this.GxMap;
	this.Icon;
	this.Title;
	this.Width;
	this.Height;
	this.Type;
	this.LatLong;
	this.wkt
	this.Precision;
	this.Ready = false;
	this.ScriptLoaded = false;	
	this.GoogleApiKey;

	var gooshow;
	
	this.SetClickLatitude = function(data) {
		this.ClickLatitude = data;
	}
	this.GetClickLatitude = function() {
		return this.ClickLatitude;
	}
	this.SetClickLongitude = function(data) {
		this.ClickLongitude = data;
	}
	this.GetClickLongitude = function() {
		return this.ClickLongitude;
	}

	// Databinding for property Attribute
	this.SetAttribute = function(data) {
		///UserCodeRegionStart:[SetAttribute] (do not remove this comment.)
		if (!data.toLowerCase().startsWith("point(")) {
			var locationComponents = data.split(',');
			if (locationComponents.length == 2) {
				data = "POINT(" +  locationComponents[1].trim() + " " + locationComponents[0].trim() + ")"
			}
		}
		this.wkt = data;
		if (gooshow) {
			gooshow.clear();
			gooshow.updateGeoJson(data);
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Attribute
	this.GetAttribute = function() {
        ///UserCodeRegionStart:[GetAttribute] (do not remove this comment.)
		if (gooshow) {
			var geoJson;
			var markers = gooshow.getMarkers();
			
			if (markers.length != 0) {
				if (this.isUsingGeolocation()) {
					return markers[0].position.lat() + "," + markers[0].position.lng();
				} else {
					if (markers.length == 1) {
						geoJson = {
							"type": "Point",
							"coordinates": [markers[0].position.lng(), markers[0].position.lat()]
						}
					} else if (markers.length > 1) {
						var len1=markers.length-1;
						geoJson = {
							"type": "Point",
							"coordinates": [markers[len1].position.lng(), markers[len1].position.lat()]
						};
						
					}
					return wellknown.stringify(geoJson);
				}
			}
		}
		return "";
        ///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.isUsingGeolocation = function() {
		var parent = this.ParentObject;
		var fld = parent.GXValidFnc[this.ControlId].fld;
		for (key in parent.VarControlMap) { 
			if (parent.VarControlMap[key].id === fld) { 
				if (this.ParentObject.VarControlMap[key].type  === "char") {
					return true;
				}
			}
		}
		return false;	
	}


	this.SetData = function(data) {
		this.GxMap = data; //this.GoogleMap = data; 

	}

	this.GetData = function() {
		var data = GetGoogleMapData(this);
		return data;
	} 	


	this.show = function() {			
		var callShow = function () {			
			for (var i = 0; i< gxmapinstances.length; i++) {
				var obj = gxmapinstances[i];
				obj.show();
			}
		}
		
		if (typeof google === 'object' && typeof google.maps === 'object') {			
			this.doShow();
		} else {			
			if (!gxmapscriptLoading) {			
				gxmapscriptLoading = true;
				var remoteScripts = [];			
                remoteScripts.push("https://maps.google.com/maps/api/js?key=" + this.GoogleApiKey);				
				gx.http.loadScripts(remoteScripts , callShow.closure(this));
			}
			gxmapinstances.push( {"show": this.doShow.closure(this)}) ;
		}
		
	}

	this.doShow = function() {
		if (gooshow === undefined)
			gooshow = new gxGoogleShow();
		gooshow.GoogleShow(this);
	}
}

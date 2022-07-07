package com.genexus.android.controls.maps.baidu;

import android.graphics.Color;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.utils.ThemeUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;

class BaiduMapsHelper {

	static int mapTypeToBaiduMapType(String mapType) {
		if (Strings.hasValue(mapType)) {
			if (mapType.equalsIgnoreCase(GxMapViewDefinition.MAP_TYPE_HYBRID))
				return BaiduMap.MAP_TYPE_NORMAL;
			else if (mapType.equalsIgnoreCase(GxMapViewDefinition.MAP_TYPE_SATELLITE))
				return BaiduMap.MAP_TYPE_SATELLITE;
			else if (mapType.equalsIgnoreCase(GxMapViewDefinition.MAP_TYPE_TERRAIN))
				return BaiduMap.MAP_TYPE_NORMAL;
		}

		return BaiduMap.MAP_TYPE_NORMAL;
	}

	static String mapTypeFromBaiduMapType(int googleMapType) {
		if (googleMapType == BaiduMap.MAP_TYPE_SATELLITE)
			return GxMapViewDefinition.MAP_TYPE_SATELLITE;

		return GxMapViewDefinition.MAP_TYPE_STANDARD;
	}

	static void applyClassToPolylineOptions(ThemeClassDefinition themeClassDefinition, PolylineOptions polylineOptions) {
		int lineWidth = themeClassDefinition.getLineWidth();
		if (lineWidth > 0)
			polylineOptions.width(lineWidth);

		polylineOptions.color(ThemeUtils.getColorId(themeClassDefinition.getStrokeColor(), Color.BLACK));
	}

	static void applyClassToPolygonOptions(ThemeClassDefinition themeClassDefinition, PolygonOptions polygonOptions) {
		int lineWidth = themeClassDefinition.getLineWidth();
		int lineColor = ThemeUtils.getColorId(themeClassDefinition.getStrokeColor(), Color.BLACK);
		polygonOptions.stroke(new Stroke(lineWidth, lineColor));
		polygonOptions.fillColor(ThemeUtils.getColorId(themeClassDefinition.getFillColor(), Color.TRANSPARENT));
	}
}

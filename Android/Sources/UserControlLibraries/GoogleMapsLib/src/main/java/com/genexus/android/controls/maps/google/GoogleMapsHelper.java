package com.genexus.android.controls.maps.google;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;

import com.genexus.android.GooglePlayServicesHelper;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.utils.ThemeUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

class GoogleMapsHelper {
	public static boolean checkGoogleMapsV2(Activity activity) {
		// Check that Google Play Services is available, and that the Google Maps app
		// is installed (necessary, and not covered by previous check).
		return (GooglePlayServicesHelper.checkPlayServices(activity) && isGoogleMapsInstalled(activity));
	}

	private static boolean isGoogleMapsInstalled(Context context) {
		try {
			context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
			return true;

			// To get:
			// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
			// context.startActivity(intent);
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	static int mapTypeToGoogleMapType(String mapType) {
		if (Strings.hasValue(mapType)) {
			if (mapType.equalsIgnoreCase(GxMapViewDefinition.MAP_TYPE_HYBRID))
				return GoogleMap.MAP_TYPE_HYBRID;
			else if (mapType.equalsIgnoreCase(GxMapViewDefinition.MAP_TYPE_SATELLITE))
				return GoogleMap.MAP_TYPE_SATELLITE;
			else if (mapType.equalsIgnoreCase(GxMapViewDefinition.MAP_TYPE_TERRAIN))
				return GoogleMap.MAP_TYPE_TERRAIN;
		}

		return GoogleMap.MAP_TYPE_NORMAL;
	}

	static String mapTypeFromGoogleMapType(int googleMapType) {
		switch (googleMapType) {
			case GoogleMap.MAP_TYPE_HYBRID:
				return GxMapViewDefinition.MAP_TYPE_HYBRID;
			case GoogleMap.MAP_TYPE_SATELLITE:
				return GxMapViewDefinition.MAP_TYPE_SATELLITE;
			case GoogleMap.MAP_TYPE_TERRAIN:
				return GxMapViewDefinition.MAP_TYPE_TERRAIN;
			default:
				return GxMapViewDefinition.MAP_TYPE_STANDARD;
		}
	}

	static void applyClassToPolylineOptions(ThemeClassDefinition themeClassDefinition, PolylineOptions polylineOptions) {
		int lineWidth = themeClassDefinition.getLineWidth();
		if (lineWidth > 0)
			polylineOptions.width(lineWidth);

		polylineOptions.color(ThemeUtils.getColorId(themeClassDefinition.getStrokeColor(), Color.BLACK));
	}

	static void applyClassToPolygonOptions(ThemeClassDefinition themeClassDefinition, PolygonOptions polygonOptions) {
		int lineWidth = themeClassDefinition.getLineWidth();
		if (lineWidth > 0)
			polygonOptions.strokeWidth(lineWidth);

		polygonOptions.strokeColor(ThemeUtils.getColorId(themeClassDefinition.getStrokeColor(), Color.BLACK));
		polygonOptions.fillColor(ThemeUtils.getColorId(themeClassDefinition.getFillColor(), Color.TRANSPARENT));
	}

	public static String getUrl(String location, int width, int height, String mapType, int zoom, String language) {
		StringBuilder sb = new StringBuilder("https://maps.google.com/maps/api/staticmap?");
		sb.append("markers=").append(Services.HttpService.uriEncode(location));
		sb.append("&zoom=").append(zoom);
		sb.append("&sensor=").append("false");
		sb.append("&maptype=").append(mapType);
		sb.append("&key=").append(GxMapViewDefinition.getApiKey());
		if (Strings.hasValue(language))
			sb.append("&language=").append(language);

		int displayDensity = (int) Services.Application.getAppContext().getResources().getDisplayMetrics().density;
		if (displayDensity >= 2) {
			// According to https://developers.google.com/maps/documentation/staticmaps/#scale_values
			// request an image with a smaller size but higher scale, so that the "physically displayed"
			// map looks about the same.
			// The actual bitmap size in pixels will be the original width and height supplied to this method.
			width = width / 2;
			height = height / 2;
			sb.append("&scale=").append(2);
		}

		sb.append("&size=").append(width).append("x").append(height);
		return sb.toString();
	}
}

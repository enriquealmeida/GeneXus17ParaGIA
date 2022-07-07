package com.artech.controls.maps.common;

import com.artech.application.MyApplication;
import com.artech.base.services.Services;
import com.artech.base.utils.Strings;
import com.artech.controls.maps.GxMapViewDefinition;

public class GoogleMapsImage
{
	public static String getUrl(String location, int width, int height, String mapType, int zoom)
	{
		String urlExtra = Strings.EMPTY;

		int displayDensity = (int)MyApplication.getAppContext().getResources().getDisplayMetrics().density;
		if (displayDensity >= 2)
		{
			// According to https://developers.google.com/maps/documentation/staticmaps/#scale_values
			// request an image with a smaller size but higher scale, so that the "physically displayed"
			// map looks about the same.
			// The actual bitmap size in pixels will be the original width and height supplied to this method. 
			width = width / 2;
			height = height / 2;
			urlExtra = "&scale=2";
		}
		String mapKey = GxMapViewDefinition.getApiKey();

		final String URL_FORMAT = "https://maps.google.com/maps/api/staticmap?markers=%s&zoom=%s&size=%sx%s&sensor=false&maptype=%s&key=%s%s";
		return String.format(URL_FORMAT, Services.HttpService.uriEncode(location), zoom, width, height, mapType, mapKey, urlExtra);
	}
}

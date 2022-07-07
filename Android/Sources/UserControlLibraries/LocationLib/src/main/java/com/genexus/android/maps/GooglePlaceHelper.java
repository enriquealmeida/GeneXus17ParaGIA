package com.genexus.android.maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Helper class to manage Places API functionality.
 */
public class GooglePlaceHelper
{
	private static Boolean sCheckForApiKeyResult = null;
	static IPickerPlaces  mPickerPlacesImpl = null;

	public static boolean isAvailable(Context context)
	{
		return false; // Places API from play services is deprecated, https://developers.google.com/places/android-sdk/placepicker
/*		if (!GooglePlayServicesHelper.isPlayServicesAvailable(context))
			return false;

		if (sCheckForApiKeyResult == null)
		{
			// Also verify that we have an API key for the Place Picker API.
			try
			{
				ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				sCheckForApiKeyResult = (app != null && Strings.hasValue(app.metaData.getString("com.google.android.geo.API_KEY")));
			}
			catch (PackageManager.NameNotFoundException e)
			{
				sCheckForApiKeyResult = false;
			}
		}
		// return if available and has an active implementation.
		return sCheckForApiKeyResult && (mPickerPlacesImpl!=null);
*/	}

	public static void registerPickerPlaces(IPickerPlaces pickerPlaces)
	{
		mPickerPlacesImpl = pickerPlaces;
	}

	//interface methods exposed
	public static @Nullable Intent buildIntent(@NonNull Activity activity, String initialValue, String mapType, boolean showMyLocation, int zoom)
	{
		return mPickerPlacesImpl.buildIntent(activity, initialValue, mapType, showMyLocation, zoom);
	}

	public static @Nullable String getLocationValueFromResult(@NonNull Context context, int resultCode, Intent data)
	{
		return mPickerPlacesImpl.getLocationValueFromResult(context, resultCode, data);
	}

	public static int getPlacePickerResultError()
	{
		return mPickerPlacesImpl.getPlacePickerResultError();
	}

	// Interface of places picker
	public interface IPickerPlaces
	{
		Intent buildIntent(@NonNull Activity activity, String initialValue, String mapType, boolean showMyLocation, int zoom);
		String getLocationValueFromResult(@NonNull Context context, int resultCode, Intent data);
		int getPlacePickerResultError();
	}
}

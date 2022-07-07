package com.genexus.android.controls.maps.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.GooglePlayServicesHelper;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.maps.GooglePlaceHelper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Helper class to manage Places API functionality.
 */
@SuppressWarnings("deprecation")
public class GooglePlacePickerHelper implements GooglePlaceHelper.IPickerPlaces
{
	private static final MapUtils MAP_UTILS = new MapUtils(null);
	private static final double DEFAULT_RADIUS_KM = 0.14; // guessed from default behavior when no LatLngBounds is supplied.

	@Override
	public @Nullable Intent buildIntent(@NonNull Activity activity, String initialValue, String mapType, boolean showMyLocation, int zoom)
	{
		try
		{
			PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

			// Center on current value, if any.
			LatLng latLng = MapUtils.stringToLatLng(initialValue);
			if (latLng != null)
			{
				LatLngBounds initialBounds = MAP_UTILS.getBoundingBox(new MapLocation(latLng), DEFAULT_RADIUS_KM).getLatLngBounds();
				builder.setLatLngBounds(initialBounds);
			}

			Intent intent = builder.build(activity);
			IntentFactory.setupLocationPickerIntent(intent, Strings.EMPTY, mapType, showMyLocation, zoom, true);
			return intent;
		}
		catch (GooglePlayServicesRepairableException e)
		{
			GooglePlayServicesHelper.showError(activity, e.getConnectionStatusCode());
			return null;
		}
		catch (GooglePlayServicesNotAvailableException e)
		{
			GooglePlayServicesHelper.showError(activity, e.errorCode);
			return null;
		}
	}

	@Override
	public @Nullable String getLocationValueFromResult(@NonNull Context context, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
			Place place = PlacePicker.getPlace(context, data);
			if (place != null)
			{
				LatLng latLng = place.getLatLng();
				return GeoFormats.buildGeolocation(latLng.latitude, latLng.longitude);
			}
		}
		else if (resultCode == PlacePicker.RESULT_ERROR)
		{
			Services.Log.warning("Call to PlacePicker returned with RESULT_ERROR. Is 'Google Places API for Android' enabled in the Developer Console?");
		}

		return null;
	}

	@Override
	public int getPlacePickerResultError()
	{
		return PlacePicker.RESULT_ERROR;
	}
}

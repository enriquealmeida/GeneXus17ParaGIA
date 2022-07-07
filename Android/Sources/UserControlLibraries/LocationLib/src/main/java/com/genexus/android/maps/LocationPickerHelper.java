package com.genexus.android.maps;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.location.R;

import static com.genexus.android.core.controls.maps.common.LocationPickerConstants.EXTRA_LOCATION;

public class LocationPickerHelper
{
	private final Activity mActivity;
	private final TextView mSelectedLocation;
	private boolean mIsSelected;

	public LocationPickerHelper(Activity activity, boolean showButtons)
	{
		mActivity = activity;
        activity.setTitle(R.string.GXM_SelectLocation);

		mSelectedLocation = activity.findViewById(R.id.selectedLocation);

		Button buttonOk = activity.findViewById(R.id.OkButton);
		Button buttonCancel = activity.findViewById(R.id.CancelButton);
		buttonOk.setOnClickListener(mOkClickListener);
		buttonCancel.setOnClickListener(mCancelClickListener);
		
		if (!showButtons)
		{
			buttonOk.setVisibility(View.GONE);
			buttonCancel.setVisibility(View.GONE);
			
		}
	}

	public void setPickedLocation(IMapLocation location)
	{
		mIsSelected = true;
		String locationString = String.format(Locale.US, "%.5f, %.5f", location.getLatitude(), location.getLongitude());
		mSelectedLocation.setText(locationString);
	}

	private OnClickListener mOkClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			selectLocation();
		}
	};

	private OnClickListener mCancelClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			cancelSelect();
		}
	};
	
	public void selectLocation() 
	{
		if (mIsSelected)
		{
			Intent data = new Intent();
			data.putExtra(EXTRA_LOCATION, mSelectedLocation.getText().toString());
			mActivity.setResult(Activity.RESULT_OK, data);
			mActivity.finish();
		}
	}
	
	public void cancelSelect() 
	{
		mActivity.setResult(Activity.RESULT_CANCELED);
		mActivity.finish();
	}

	public static String parseMapType(int mapTypeValue) {
		switch (mapTypeValue) {
			case 1:
				return GxMapViewDefinition.MAP_TYPE_STANDARD;
			case 2:
				return GxMapViewDefinition.MAP_TYPE_SATELLITE;
			case 3:
				return GxMapViewDefinition.MAP_TYPE_HYBRID;
			default :
				return GxMapViewDefinition.MAP_TYPE_TERRAIN;
		}
	}

}

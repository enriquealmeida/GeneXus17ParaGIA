package com.genexus.android.location.controls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.activities.GxBaseActivity;
import com.genexus.android.WithPermission;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.controls.IGxHandleActivityResult;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.GxEditText;
import com.genexus.android.core.controls.GxTextView;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.location.R;
import com.genexus.android.maps.GooglePlaceHelper;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.ThemeUtils;

import static com.genexus.android.core.controls.maps.common.LocationPickerConstants.EXTRA_LOCATION;

public class GxLocationEdit extends RelativeLayout implements IGxEdit, IGxEditThemeable, IGxHandleActivityResult
{
    @SuppressLint("ResourceType")
	@IdRes
    private static final int VIEW_ID = 75;  //just a valid integer
    private final GxEditText mEditText;
	private final Button mSelectButton;
	private final Activity mContext;
	private final IGxEdit mEdit;
	private final LayoutItemDefinition mDefinition;
	private final String mMapType;
	private final int mMapZoom;
	private final boolean mShowMyLocation;

	private boolean mShowMap = false;
	private GxSDGeoLocation mGeoLocationEdit;
	private Coordinator mCoordinator;

	private static final int PLACE_PICKER_REQUEST_CODE = 151;
	private static final int PERMISSION_REQUEST_CODE = 152;

	public GxLocationEdit(Context context, Coordinator coordinator, LayoutItemDefinition layoutItemDef)
	{
		super(context);
		mDefinition = layoutItemDef;
		mEdit = this;
		mContext = (Activity) context;
		mCoordinator = coordinator;
		mMapType = layoutItemDef.getControlInfo().optStringProperty("@SDGeoLocationMapType");
		mMapZoom = layoutItemDef.getControlInfo().optIntProperty("@SDGeoLocationMapZoom", 15);
		mShowMyLocation = layoutItemDef.getControlInfo().optBooleanProperty("@SDGeolocationShowMyLocation");

		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mEditText = new GxEditText(context, coordinator, layoutItemDef);
		mSelectButton = new AppCompatButton(context);
		mSelectButton.setText(R.string.GX_BtnSelect);

		addView(mEditText, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(mSelectButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

		updateMapControl();

		mSelectButton.setOnClickListener(mOnClickListener);
	}

	private final OnClickListener mOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (mShowMap)
			{
				callBestLocationPicker();
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				String[] items = { Services.Strings.getResource(R.string.GXM_MyLocation), Services.Strings.getResource(R.string.GXM_OtherLocation) };

				builder.setTitle(R.string.GXM_SelectLocation);
				builder.setItems(items, ondialogclick);
				AlertDialog alert = builder.create();
				alert.show();
			}
		}

		private DialogInterface.OnClickListener ondialogclick = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int option)
			{
				dialog.dismiss();

				if (option == 0)
				{
					setMyLocation();
				}
				else if (option == 1)
				{
					callBestLocationPicker();
				}
				else
					throw new IllegalArgumentException("Option should only be 0 or 1");
			}
		};
	};

	@SuppressWarnings("deprecation")
	private void setMyLocation()
	{
		new WithPermission.Builder<Void>((Activity)getContext())
				.require(Services.Location.getRequiredPermissions())
				.setRequestCode(PERMISSION_REQUEST_CODE)
				.attachToActivityController()
				.onSuccess(() -> {
					Location location = Services.Location.getLastKnownLocation();
					if (location != null)
						setEditValue(GeoFormats.buildGeolocation(location.getLatitude(), location.getLongitude()));
					else
						Toast.makeText(getContext(), R.string.GXM_CouldNotGetLocationInformation, Toast.LENGTH_SHORT).show();
				})
				.build()
				.run();
	}

	private void callBestLocationPicker()
	{
		if (GooglePlaceHelper.isAvailable(getContext()))
			callGooglePlacePicker();
		else
			callBasicGeolocationPicker();
	}

	private void callGooglePlacePicker()
	{
		Intent placePickerIntent = GooglePlaceHelper.buildIntent(mContext, getInternalValue(), mMapType, mShowMyLocation, mMapZoom);
		if (placePickerIntent != null)
		{
			if (mCoordinator.getUIContext().getActivityController() != null)
				mCoordinator.getUIContext().getActivityController().setActivityResultHandler(this);

			mContext.startActivityForResult(placePickerIntent, PLACE_PICKER_REQUEST_CODE);
		}
	}

	@Override
	public boolean handleOnActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == PLACE_PICKER_REQUEST_CODE)
		{
			String value = GooglePlaceHelper.getLocationValueFromResult(getContext(), resultCode, data);
			if (Strings.hasValue(value))
				setEditValue(value);

			return true; // Handled by us, even if unsuccessfully.
		}
		else
			return false;
	}

	private void callBasicGeolocationPicker()
	{
		GxBaseActivity.sPickingElementId = mEdit.getGxTag(); //hack because onActivityResult should be handled on activities
		ActivityLauncher.callLocationPicker(mContext, getInternalValue(), mMapType, mShowMyLocation, mMapZoom);
	}

	@Override
	public void setValueFromIntent(Intent data)
	{
		String contents = data.getStringExtra(EXTRA_LOCATION);
		setEditValue(contents);
	}

	private void updateMapControl()
	{
		if (mShowMap)
		{
			removeView(mEditText);
			removeView(mSelectButton);

			// use a relative layout for better layout with map.
			mGeoLocationEdit = new GxSDGeoLocation(getContext(), mCoordinator, mDefinition);
			mGeoLocationEdit.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mGeoLocationEdit.setOnClickListener(mOnClickListener);

			addView(mGeoLocationEdit);
			addView(mSelectButton);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// align select top right inside the map
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			mSelectButton.setLayoutParams(params);

		}
		else
		{
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// align right the button
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			mSelectButton.setLayoutParams(params);
			mSelectButton.setId(VIEW_ID);

			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			// edit text fill remain space
			params2.addRule(RelativeLayout.LEFT_OF, VIEW_ID);
			mEditText.setLayoutParams(params2);

			setHint(mDefinition.getInviteMessage());
		}
	}

	@Override
	public String getGxValue()
	{
		return GeoFormats.convertToExternal(getInternalValue(), mDefinition.getDataItem());
	}

	private String getInternalValue()
	{
		if (mGeoLocationEdit != null)
			return mGeoLocationEdit.getGxValue();
		else
			return mEditText.getGxValue();
	}


	@Override
	public void setGxValue(String value)
	{
		String internalValue = GeoFormats.convertToInternal(value, mDefinition.getDataItem());

		if (mGeoLocationEdit != null)
			mGeoLocationEdit.setGxValue(internalValue);
		else
			mEditText.setGxValue(internalValue);
	}

	private void setEditValue(String value) {
		String previousValue;
		String currentValue;

		if (mGeoLocationEdit != null) {
			previousValue = mGeoLocationEdit.getGxValue();
			mGeoLocationEdit.setGxValue(value);
			currentValue = mGeoLocationEdit.getGxValue();
		} else {
			previousValue = mEditText.getGxValue();
			mEditText.setGxValue(value);
			mEditText.setTextAsEdited(value);
			currentValue = mEditText.getGxValue();
		}

		boolean valueChanged = !TextUtils.equals(currentValue, previousValue);

		if (mCoordinator != null && valueChanged) {
			mCoordinator.onValueChanged(this, true);
		}
	}

	@Override
	public String getGxTag() {
		return (String) this.getTag();
	}

	@Override
	public void setGxTag(String data) {
		this.setTag(data);
	}

	private void setHint(String label)
	{
		if (!mShowMap)
			mEditText.setHint(label);
	}

	@Override
	public IGxEdit getViewControl() {
		return new GxTextView(getContext(), mDefinition);
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	public void setShowMap(boolean b)
	{
		mShowMap = b;
		updateMapControl();
	}

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		ThemeUtils.setFontProperties(mEditText, themeClass);
	}
}

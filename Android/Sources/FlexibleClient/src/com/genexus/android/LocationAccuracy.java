package com.genexus.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

public class LocationAccuracy {

	public static final int UNKNOWN = 0;
	public static final int REDUCED = 1;
	public static final int FULL = 2;

	@SuppressLint("InlinedApi")
	public static final String BACKGROUND = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
	public static final String COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
	public static final String FINE = Manifest.permission.ACCESS_FINE_LOCATION;

	public static int getStatus(@NonNull Context context) {
		if (PermissionUtil.checkSelfPermissions(context, new String[]{FINE}))
			return FULL;

		if (PermissionUtil.checkSelfPermissions(context, new String[]{COARSE}))
			return REDUCED;

		return UNKNOWN;
	}
}

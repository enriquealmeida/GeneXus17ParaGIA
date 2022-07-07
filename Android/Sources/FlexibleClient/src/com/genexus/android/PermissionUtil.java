package com.genexus.android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.genexus.android.LocationAccuracy.BACKGROUND;
import static com.genexus.android.LocationAccuracy.COARSE;
import static com.genexus.android.LocationAccuracy.FINE;

/**
 * Helper class for the Android 6.0 permissions framework (this code is not GX-specific).
 */
public class PermissionUtil
{
	/**
	 * Determines whether the app already posseses ALL members of a set of permissions.
	 *
	 * @see Context#checkSelfPermission(String)
	 */
	public static boolean checkSelfPermissions(@NonNull Context context, @NonNull String[] permissions)
	{
		for (String permission : permissions)
		{
			if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
				return false;
		}

		return true;
	}

	/**
	 * Determines whether the app already posseses ANY members of a set of permissions.
	 *
	 * @see Context#checkSelfPermission(String)
	 */
	public static boolean checkAnySelfPermissions(@NonNull Context context, @NonNull String[] permissions) {
		for (String permission : permissions)
			if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
				return true;

		return false;
	}

	/**
	 * Determines whether the show app should display an UI for rationale for ANY member of
	 * a set of permissions.
	 *
	 * @see Activity#shouldShowRequestPermissionRationale(String)
	 */
	public static boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions)
	{
		for (String permission : permissions)
		{
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
				return true;
		}

		return false;
	}

	public static boolean checkAnyLocationPermission(Context context) {
		return checkAnySelfPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
	}

	/**
	 * Checks that ALL given permissions have been granted by verifying that each entry in the
	 * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
	 *
	 * @see Activity#onRequestPermissionsResult(int, String[], int[])
	 */
	public static boolean verifyPermissionsResult(int[] grantResults)
	{
		for (int result : grantResults)
		{
			if (result != PackageManager.PERMISSION_GRANTED)
				return false;
		}

		return true;
	}

	/**
	 * Checks that at least one Location permission (COARSE or FINE) has been granted by verifying that
	 * at least one of the corresponding entry in grantResults is of the value {@link PackageManager#PERMISSION_GRANTED}.
	 *
	 * @see Activity#onRequestPermissionsResult(int, String[], int[])
	 */
	public static boolean verifyLocationPermissionsResult(String[] permissions, int[] grantResults) {
		if (permissions.length != grantResults.length || permissions.length > 2)
			return false;

		for (int i = 0; i < permissions.length; i++) {
			String permission = permissions[i];
			int result = grantResults[i];
			if (!permission.equals(COARSE) && !permission.equals(FINE))
				return false;

			if (result == PackageManager.PERMISSION_GRANTED)
				return true;
		}

		return false;
	}

	/**
	 * Checks if the ACCESS_BACKGROUND_LOCATION should be requested separately
	 * @param context The context
	 * @return true if the ACCESS_BACKGROUND_LOCATION permission is in Manifest, running on API 30 or higher,
	 *          and permissions list contains ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION.
	 */
	public static boolean shouldRequestBackgroundLocationSeparately(Context context, List<String> permissions) {
		return ((permissions.contains(COARSE) || permissions.contains(FINE))
				&& permissions.contains(BACKGROUND)) && PermissionUtil.shouldRequestBackgroundLocationSeparately(context);
	}

	private static boolean shouldRequestBackgroundLocationSeparately(Context context) {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && PermissionUtil.hasBackgroundLocationPermission(context);
	}

	/**
	 * Checks whether ACCESS_BACKGROUND_LOCATION is declared in AndroidManifest or not
	 * @param context The context
	 * @return true if ACCESS_BACKGROUND_LOCATION is declared in AndroidManifest; false otherwise
	 */
	@RequiresApi(api = Build.VERSION_CODES.Q)
	public static boolean hasBackgroundLocationPermission(Context context) {
		return hasPermission(context, BACKGROUND);
	}

	/**
	 * Checks whether a single permission is declared in AndroidManifest or not.
	 * @param context The context
	 * @param permission The permissions to check for
	 * @return true if the permission is declared in AndroidManifest; false otherwise
	 */
	public static boolean hasPermission(Context context, String permission) {
		if (context == null || !Strings.hasValue(permission))
			return false;

		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
			if (info != null && info.requestedPermissions != null) {
				for (String p : info.requestedPermissions)
					if (p.equals(permission))
						return true;
			}
		} catch (PackageManager.NameNotFoundException e) {
			Services.Log.error(e);
		}
		return false;
	}
}

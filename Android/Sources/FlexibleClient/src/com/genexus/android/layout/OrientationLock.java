package com.genexus.android.layout;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.view.Surface;

import com.genexus.android.ActivityResourceBase;
import com.genexus.android.ActivityResources;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;

/**
 * Helper class to lock an activity's orientation to its current one
 * (including reverse rotations in Froyo or newer).
 */
public class OrientationLock extends ActivityResourceBase
{
	private final Activity mActivity;
	private final int mOriginalOrientation;
	private Set<String> mLockReasons;

	public static final String REASON_LOAD_METADATA = "GX::LoadingMetadata";
	public static final String REASON_SHOW_POPUP = "GX::ShowingPopup";
	public static final String REASON_RUN_EVENT = "GX::RunningEvent";
	
	private OrientationLock(Activity activity)
	{
		mActivity = activity;
		mOriginalOrientation = activity.getRequestedOrientation();
		mLockReasons = new HashSet<>();
	}

	private void lockOrientation(String reason)
	{
		boolean wasLocked = (mLockReasons.size() != 0);
		mLockReasons.add(reason);
		
		if (wasLocked)
			return; // Was already locked, just (possibly) added reason.

		int orientation = mActivity.getResources().getConfiguration().orientation;
		int rotation;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			rotation = mActivity.getDisplay().getRotation();
		} else {
			rotation = getRotationDeprecated();
		}

		// Adapted from http://stackoverflow.com/a/8450316
		if (orientation == Configuration.ORIENTATION_PORTRAIT)
		{
            if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_180)
            	mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            else
            	mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
				// fix for devices reporting wrong rotation.
				if (deviceReportLandscapeInvert())
				{
					Services.Log.debug("OrientationLock","Current Rotation: ROTATION_0 or ROTATION_90");
					Services.Log.debug("OrientationLock","Device Report Invert, Lock Orientation: SCREEN_ORIENTATION_REVERSE_LANDSCAPE");
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
				}
				else
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			else {
				if (deviceReportLandscapeInvert())
				{
					Services.Log.debug("OrientationLock","Current Rotation: ROTATION_180 or ROTATION_270");
					Services.Log.debug("OrientationLock", "Device Report Invert, Lock Orientation: SCREEN_ORIENTATION_LANDSCAPE");
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
				else
					mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private int getRotationDeprecated() {
		return mActivity.getWindowManager().getDefaultDisplay().getRotation();
	}

	private void unlockOrientation(String reason)
	{
		if (!mLockReasons.contains(reason))
		{
			// Not locked for this reason?
			Services.Log.debug("Asked to unlock orientation for reason " + reason + " but it was not among the lock reasons. Ignored.");
			return;
		}

		mLockReasons.remove(reason);
		
		if (mLockReasons.size() == 0)
			mActivity.setRequestedOrientation(mOriginalOrientation);
	}
	
	public static void lock(Activity activity, String reason)
	{
		OrientationLock lock = ActivityResources.getResource(activity, OrientationLock.class,
			new Function<Activity, OrientationLock>()
			{
				@Override
				public OrientationLock run(Activity input){ return new OrientationLock(input); }
			});

		lock.lockOrientation(reason);
	}

	public static void unlock(Activity activity, String reason)
	{
		OrientationLock lock = ActivityResources.getResource(activity, OrientationLock.class);
		if (lock != null)
			lock.unlockOrientation(reason);
	}
	private boolean deviceReportLandscapeInvert()
	{
		boolean isPanasonicFz = (Build.MANUFACTURER.equalsIgnoreCase("panasonic") &&
			Build.MODEL.equalsIgnoreCase("FZ-L1"));
		if (isPanasonicFz) {
			Services.Log.debug("is isPanasonicFzL1 : true ");
			Services.Log.debug("MANUFACTURER : " + Build.MANUFACTURER + " MODEL : " + Build.MODEL);
		}
		return isPanasonicFz;
	}
}

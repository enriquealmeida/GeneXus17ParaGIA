package com.genexus.android.media.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.view.MenuItemCompat;
import androidx.mediarouter.app.MediaRouteActionProvider;
import androidx.mediarouter.app.MediaRouteButton;

import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.genexus.android.ActivityResourceBase;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.uamp.R;
import com.genexus.android.uamp.utils.LogHelper;
import com.genexus.android.media.MediaPlayerModule;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay;

/**
 * Support for Google Cast in activities.
 */
public class ActivityCastResource extends ActivityResourceBase
{
	private static final String TAG = LogHelper.makeLogTag(ActivityCastResource.class);

	private static final int DELAY_MILLIS = 1000;

	private Activity mActivity;
	private CastContext mCastContext;
	private Menu mToolbarMenu;
	private MenuItem mMediaRouteMenuItem;

	public ActivityCastResource(Activity activity)
	{
		mActivity = activity;
	}

	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState)
	{
		if (!MediaPlayerModule.isGoogleCastEnabled())
			return;

		mCastContext = CastContext.getSharedInstance(mActivity);
	}

	@Override
	public void onResume(Activity activity)
	{
		if (!MediaPlayerModule.isGoogleCastEnabled())
			return;

		mCastContext.addCastStateListener(mCastStateListener);
	}

	public void onCreateOptionsMenu(Menu menu)
	{
		if (!MediaPlayerModule.isGoogleCastEnabled())
			return;

		mToolbarMenu = menu;
		MenuItem castItem = menu.add(0, R.id.media_route_menu_item, 0, R.string.media_route_menu_title);
		castItem.setShowAsAction( MenuItem.SHOW_AS_ACTION_ALWAYS);

		Context context = SherlockHelper.getActionBarThemedContext(mActivity);
		MenuItemCompat.setActionProvider(castItem, new MediaRouteActionProvider(context));

		mMediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(mActivity.getApplicationContext(),
				menu, R.id.media_route_menu_item);
	}

	private final CastStateListener mCastStateListener = new CastStateListener() {
		@Override
		public void onCastStateChanged(int newState) {
			if (newState != CastState.NO_DEVICES_AVAILABLE) {
				new Handler(Looper.getMainLooper()).postDelayed(() -> {
					if (mMediaRouteMenuItem != null && mMediaRouteMenuItem.isVisible()) {
						LogHelper.d(TAG, "Cast Icon is visible");
						showFtu();
					}
				}, DELAY_MILLIS);
			}
		}
	};

	/**
	 * Shows the Cast First Time User experience to the user (an overlay that explains what is
	 * the Cast icon)
	 */
	private void showFtu()
	{
		if (mToolbarMenu == null)
			return;

		View view = mToolbarMenu.findItem(R.id.media_route_menu_item).getActionView();
		if (view != null && view instanceof MediaRouteButton)
		{
			IntroductoryOverlay overlay = new IntroductoryOverlay.Builder(mActivity, mMediaRouteMenuItem)
					.setTitleText(R.string.touch_to_cast_title)
					.setSingleTime()
					.build();
			overlay.show();
		}
	}

	@Override
	public void onPause(Activity activity)
	{
		if (!MediaPlayerModule.isGoogleCastEnabled())
			return;

		mCastContext.removeCastStateListener(mCastStateListener);
	}
}

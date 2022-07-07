package com.genexus.android.core.activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.genexus.android.R;
import com.genexus.android.ActivityResources;
import com.genexus.android.core.controls.GxToolbar;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.metadata.ILayoutDefinition;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.loader.ApplicationLoader;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.PlatformHelper;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.controllers.RefreshParameters;
import com.genexus.android.core.controls.ApplicationBarControl;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.IInspectableComponent;
import com.genexus.android.core.fragments.LayoutFragmentActivity;
import com.genexus.android.core.fragments.LayoutFragmentEditBC;
import com.genexus.android.core.ui.navigation.slide.SlideNavigationController;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.ThemeUtils;

import static com.genexus.android.core.base.metadata.theme.ApplicationBarClassExtensionKt.APPBAR_CLASS_NAME;

/**
 * Class used to centralize calls to tasks that must be performed at particular points of
 * an activity's lifecycle (create, pause, resume, destroy) and other helper methods.
 */
public class ActivityHelper {
	private static final String KEY_BAR_THEME_CLASS = "ApplicationBarThemeClass";
	private static LayoutFragmentActivity sMainActivity;
	private static Activity sCurrentActivity;
	private static boolean sMyLifecycleObserverAdded = false;
	private static List<MainActivitySetListener> mListeners = new ArrayList<>();
	private static HashSet<Integer> sActionRequestCodes = new HashSet<>();

	/**
	 * Performs initialization of an Activity (such as enabling hardware acceleration).
	 * Must be the first method called after super.onCreate().
	 */
	@SuppressWarnings("deprecation")
	public static void initialize(Activity activity, Bundle savedInstanceState) {
		SherlockHelper.requestWindowFeature(activity, Window.FEATURE_INDETERMINATE_PROGRESS);
		SherlockHelper.requestWindowFeature(activity, Window.FEATURE_PROGRESS);
		ActivityResources.onCreate(activity, savedInstanceState);

		//DebugService.onCreate(activity);
	}

	public static void setSupportActionBarAndShadow(AppCompatActivity activity) {
		setSupportActionBar(activity);

		// hide shadow in 5.x
		View toolbarShadow = activity.findViewById(R.id.toolbar_shadow);
		if (toolbarShadow != null)
			toolbarShadow.setVisibility(View.GONE);

	}

	public static void setSupportActionBar(AppCompatActivity activity) {
		// set support toolbar
		Toolbar toolbar = activity.findViewById(R.id.action_bar_toolbar);
		activity.setSupportActionBar(toolbar);
	}

	public static void setActionBarOverlay(Activity activity) {
		SherlockHelper.requestWindowFeature(activity, Window.FEATURE_ACTION_BAR_OVERLAY);
	}

	public static void setStatusBarOverlay(Activity activity) {
		// StatusBarOverlayingAvailable in Android 5.x or up
		// check http://stackoverflow.com/questions/27856603/lollipop-draw-behind-statusbar-with-its-color-set-to-transparent
		// api level 21. min level is 21 now, 21 for status bar color
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		// TODO: find an equivalent to setStatusBarOverlayDeprecated for Build.VERSION_CODES.R
		// in (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) setDecorFitsSystemWindows(false) change content size result
		setStatusBarOverlayDeprecated(activity);
	}

	@SuppressWarnings("deprecation")
	private static void setStatusBarOverlayDeprecated(Activity activity) {
		activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
	}

	/**
	 * Applies "global" theme properties (application bar color, background color
	 * and image) to an activity. Should be called in Activity.onCreate()
	 * <em>after</em> setContentView() has executed.
	 */
	@SuppressWarnings("deprecation")
	public static void applyStyle(Activity activity, ILayoutDefinition definition) {
		// set action bar home option enable
		ActionBar bar = SherlockHelper.getActionBar(activity);
		if (bar != null && hasActionBar(activity))
			customizeAppBar(activity, definition, bar);

		if (activity.getParent() == null) {
			// Set app background color and image.
			// Don't do it for "inner" activities; otherwise we end up with a "double background".
			ThemeClassDefinition appClass = Services.Themes.getApplicationClass();
			if (appClass != null)
				ThemeUtils.setBackground(activity, appClass);
		}

		// Set progress to end to hide progress bar at activity startup
		// Progress bar will only appear if progress is set to other value in other place. ie. webview
		SherlockHelper.setProgress(activity, Window.PROGRESS_END);
	}

	private static void customizeAppBar(Activity activity, ILayoutDefinition layout, ActionBar bar) {
		bar.setHomeButtonEnabled(true);
		bar.setDisplayShowHomeEnabled(true);

		SherlockHelper.setProgressBarIndeterminateVisibility(activity, false);

		boolean allowHeaderRow = true;
		if (activity instanceof LayoutFragmentActivity) {
			LayoutFragmentActivity layoutFragmentActivity = ((LayoutFragmentActivity) activity);
			if (layoutFragmentActivity.getMainFragment() != null && !layoutFragmentActivity.getMainFragment().isAllowHeaderRow())
				allowHeaderRow = false;
		}

		if (layout != null && allowHeaderRow && layout.getEnableHeaderRowPattern()) {
			ThemeClassDefinition appBarClass = layout.getHeaderRowApplicationBarClass();
			if (appBarClass != null) {
				setActionBarHeaderRowToTransparentMode(activity, layout, false);
				return;
			}
		}

		// if not use default action bar theme
		setActionBarTheme(activity, layout, false);
	}

	public static void setActionBarHeaderRowToTransparentMode(@NonNull Activity activity, @NonNull ILayoutDefinition layout, boolean animateBackgroundChange) {
		// use header row theme, transparent and elevation=0
		ActivityHelper.setActionBarThemeClass(activity, layout.getHeaderRowApplicationBarClass(), animateBackgroundChange);
		// set action bar click behavior
		setActionBarTransparentMode(activity, true);
	}

	private static void setActionBarTransparentMode(@NonNull Activity activity, boolean isTransparent) {
		GxToolbar actionBarToolbar = activity.findViewById(R.id.action_bar_toolbar);
		if (actionBarToolbar != null)
		{
			actionBarToolbar.setTransparentMode(isTransparent);
		}
	}

	public static void setActionBarHeaderRowToNormalMode(@NonNull Activity activity, @NonNull ILayoutDefinition layout, boolean animateBackgroundChange) {
		// use default theme, for application bar
		ActivityHelper.setActionBarTheme(activity, layout, animateBackgroundChange);
		// set action bar click behavior
		setActionBarTransparentMode(activity, false);
	}

	public static void setActionBarTheme(Activity activity, ILayoutDefinition layout, boolean animateBackgroundChange) {
		// Get specific application bar class from definition, or use generic one.
		ThemeClassDefinition appBarClass = null;
		if (layout != null)
			appBarClass = layout.getApplicationBarClass();
		if (appBarClass == null)
			appBarClass = Services.Themes.getThemeClass(APPBAR_CLASS_NAME);

		setActionBarThemeClass(activity, appBarClass, animateBackgroundChange);
	}

	public static void setActionBarThemeClass(Activity activity, ThemeClassDefinition themeClass) {
		setActionBarThemeClass(activity, themeClass, false);
	}

	public static void setActionBarThemeClass(Activity activity, ThemeClassDefinition themeClass, boolean animateBackgroundChange) {
		ActionBar bar = SherlockHelper.getActionBar(activity);
		if (bar != null && themeClass != null) {
			//Services.Log.debug("setActionBarThemeClass", "themeClass " + themeClass);

			ThemeClassDefinition previousClass = getActionBarThemeClass(activity);
			if (previousClass == themeClass)
				return; // Avoid re-applying the same exact class.

			ActivityTags.put(activity, KEY_BAR_THEME_CLASS, themeClass);

			//Services.Log.debug("setActionBarThemeClass", "themeClass2 " + themeClass);

			// Set title bar properties
			ThemeUtils.setActionBarBackground(activity, bar, themeClass, animateBackgroundChange, previousClass);
			ThemeUtils.setTitleFontProperties(activity, themeClass);
			ThemeUtils.setStatusBarColor(activity, themeClass, animateBackgroundChange, previousClass);
			ThemeUtils.setStatusBarAlignment(activity, themeClass);

			// app icon and title image supported also.
			ThemeUtils.setAppBarIconImage(bar, themeClass);
			ThemeUtils.setAppBarTitleImage(activity, bar, themeClass);
			ThemeUtils.setAppBarBackButtonImage(bar, themeClass);
		}
	}

	public static ThemeClassDefinition getActionBarThemeClass(Activity activity) {
		return Cast.as(ThemeClassDefinition.class, ActivityTags.get(activity, KEY_BAR_THEME_CLASS));
	}


	public static void setActionBarVisibilityForNavigationController(Activity activity, boolean visible)
	{
		if (activity instanceof GenexusActivity)
		{
			GenexusActivity gxActivity = (GenexusActivity) activity;
			// slide navigation already set action bar visibility for content fragment, do not override with main data.
			if (gxActivity.getNavigationController() instanceof SlideNavigationController)
				return;
		}
		setActionBarVisibility(activity, visible);
	}


	public static void setActionBarVisibility(Activity activity, boolean visible) {
		ActionBar bar = SherlockHelper.getActionBar(activity);
		if (bar != null) {
			if (visible)
				bar.show();
			else
				bar.hide();
		}
	}

	public static boolean hasActionBar(Activity activity) {
		ActionBar bar = SherlockHelper.getActionBar(activity);
		return (bar != null && bar.isShowing());
	}

	public static Activity getMainActivity() {
		return sMainActivity;
	}

	public static void addMainActivitySetListener(MainActivitySetListener listener) {
		mListeners.add(listener);
	}

	public static void removeMainActivitySetListener(MainActivitySetListener listener) {
		mListeners.remove(listener);
	}

	private static void onMainActivitySet() {
		for (MainActivitySetListener listener : new ArrayList<>(mListeners))
			listener.onMainActivitySet();

		if (!sMyLifecycleObserverAdded) {
			ProcessLifecycleOwner.get().getLifecycle().addObserver(new MyLifecycleObserver());
			sMyLifecycleObserverAdded = true; // to add it only once when the app is restarted for a minor update
		}
	}

	private static class MyLifecycleObserver implements LifecycleObserver {
		@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
		public void onResume() {
			// This onResume is for the entire app, it is called when returning from background,
			// it is also called at app startup but in that case getCurrentActivity() is null,
			// it is NOT called when an Activity is resumed when moving from a panel to another.
			IGxActivity activity = Cast.as(IGxActivity.class, getCurrentActivity());
			if (activity != null) {
				if (shouldRefresh(getCurrentActivity()))
					activity.getController().onRefresh(RefreshParameters.IMPLICIT);

				// Inside the if so it is not called at startup
				new ApplicationLoader(Services.Application.get()).checkMetadataInBackground((Context)activity);
			}
		}
	}

	public static Activity getCurrentActivity() {
		return sCurrentActivity;
	}

	public static boolean hasCurrentActivity() {
		return sCurrentActivity != null;
	}

	public static void onNewIntent(Activity activity, Intent intent) {
		ActivityResources.onNewIntent(activity, intent);
	}

	public static void onStart(Activity activity) {
		if (sMainActivity == null && activity instanceof LayoutFragmentActivity) {
			sMainActivity = (LayoutFragmentActivity)activity;
			onMainActivitySet();
		}
		ActivityResources.onStart(activity);
	}

	/**
	 * Registers the currently running activity.
	 */
	public static boolean onResume(Activity activity) {
		//DebugService.onResume(activity);
		if (!ActivityFlowControl.onResume(activity))
			return false;

		if (activity != sCurrentActivity) {
			// If missed previous onPause, signal it now.
			if (sCurrentActivity != null)
				onPause(sCurrentActivity);

			sCurrentActivity = activity;
			ActivityResources.onResume(activity);
		}

		return true;
	}

	public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		ActivityResources.onActivityResult(activity, requestCode, resultCode, data);
	}

	public static void onSaveInstanceState(Activity activity, Bundle outState) {
		ActivityResources.onSaveInstanceState(activity, outState);
	}

	public static void onPause(Activity activity) {
		ActivityFlowControl.onPause(activity);
		ActivityResources.onPause(activity);

		if (sCurrentActivity == activity) {
			LayoutFragmentActivity fragmentActivity = Cast.as(LayoutFragmentActivity.class, activity);
			if (fragmentActivity != null)
				fragmentActivity.controlsToData(); // So they aren't lost on refresh after coming from background
			sCurrentActivity = null;
		}
	}

	public static void onStop(Activity activity) {
		ActivityResources.onStop(activity);
	}

	public static void onDestroy(Activity activity) {
		if (sMainActivity == activity)
			sMainActivity = null; // If back is pressed to leave main panel
		ActivityResources.onDestroy(activity);
		unbindReferences(activity);
	}

	/**
	 * This is a workaround to avoid an IllegalStateException when coming from background
	 * but app is still in that state (Not allowed to start service Intent yet).
	 * onRefresh will only be executed when importance is at least FOREGROUND, thus avoiding crash.
	 * <p>
	 * see https://issuetracker.google.com/issues/113122354#comment20
	 */
	private static boolean shouldRefresh(Activity activity) {
		// no refresh in LayoutFragmentEditBC, cause lost of edit data.
		if (activity instanceof IGxActivity)
		{
		    IGxActivity gxActivity = (IGxActivity)activity;
		    for (IDataView dataView : gxActivity.getDataViews()) {
		        if (dataView instanceof LayoutFragmentEditBC)
		            return false;
		    }
		}

		return Services.Application.getLifecycle().isForeground();
	}

	/**
	 * Removes the reference to the activity from every view in a view hierarchy (listeners, images etc.).
	 * This method should be called in the onDestroy() method of each activity
	 * <p>
	 * see http://code.google.com/p/android/issues/detail?id=8488
	 */
	private static void unbindReferences(Activity activity) {
		try {
			View view = activity.findViewById(android.R.id.content);
			if (view != null) {
				unbindViewReferences(view);
				if (view instanceof ViewGroup)
					unbindViewGroupReferences((ViewGroup) view);
			}

			System.gc();
		} catch (@SuppressWarnings("checkstyle:IllegalCatch") Throwable t) {
			// TODO: We should investigate why and which exception we're catching here
		}
	}

	private static void unbindViewGroupReferences(ViewGroup viewGroup) {
		int nChildren = viewGroup.getChildCount();
		for (int i = 0; i < nChildren; i++) {
			View view = viewGroup.getChildAt(i);

			if (view instanceof WebView) {
				// WebView must be removed from the view hierarchy before calling destroy() on it.
				// Otherwise we will get a "WebView.destroy() called while still attached" exception.
				// Since we remove a view, adjust the iteration indexes accordingly.
				viewGroup.removeViewAt(i);
				nChildren--;
				i--;
			}

			unbindViewReferences(view);

			if (view instanceof ViewGroup)
				unbindViewGroupReferences((ViewGroup) view);
		}

		try {
			viewGroup.removeAllViews();
		} catch (UnsupportedOperationException mayHappen) {
			// AdapterViews, ListViews and potentially other ViewGroups don't support the removeAllViews operation
		}
	}

	// TODO: Check if it's actually required to call destroyDrawingCache().
	@SuppressWarnings("deprecation")
	private static void unbindViewReferences(View view) {
		// set all listeners to null (not every view and not every API level supports the methods).
		try {
			view.setOnClickListener(null);
			view.setOnCreateContextMenuListener(null);
			view.setOnFocusChangeListener(null);
			view.setOnKeyListener(null);
			view.setOnLongClickListener(null);
			view.setOnClickListener(null);
			view.setOnTouchListener(null);
			view.setTag(R.id.tag_grid_item_info, null);
		} catch (@SuppressWarnings("checkstyle:IllegalCatch") Throwable t) {
			// TODO: We should investigate why and which exception we're catching here
		}

		// set background to null and remove callback.
		Drawable d = view.getBackground();
		if (d != null)
			d.setCallback(null);

		if (view instanceof ImageView) {
			ImageView imageView = (ImageView) view;
			d = imageView.getDrawable();
			if (d != null)
				d.setCallback(null);

			imageView.setImageDrawable(null);
			imageView.setBackground(null);
		}

		// destroy webview
		if (view instanceof WebView) {
			view.destroyDrawingCache();
			((WebView) view).destroy();
		}
	}

	/**
	 * Sets the orientation of the activity to the default orientation specified in the app
	 * (either switching it, or locking to the current one).
	 *
	 * @return True if the orientation was switched, false otherwise.
	 */
	public static boolean setDefaultOrientation(Activity activity) {
		Orientation defaultOrientation = PlatformHelper.getDefaultOrientation();
		if (defaultOrientation != Orientation.UNDEFINED) {
			boolean isSwitch = (defaultOrientation != Services.Device.getScreenOrientation());
			setOrientation(activity, defaultOrientation);
			return isSwitch;
		} else
			return false;
	}

	public static void setOrientation(Activity activity, Orientation orientation) {
		if (orientation != Orientation.UNDEFINED) {
			int requestedOrientation = (orientation == Orientation.PORTRAIT ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			activity.setRequestedOrientation(requestedOrientation);
		}
	}

	public static View getInvalidMetadataMessage(IGxActivity activity) {
		return getInvalidMetadataMessage((Context) activity, activity.getModel().getName());
	}

	public static View getInvalidMetadataMessage(Context context, String objectName) {
		if (objectName == null)
			objectName = Services.Strings.getResource(R.string.GXM_Unknown);

		String message = String.format(Services.Strings.getResource(R.string.GXM_InvalidDefinition), objectName);
		return getErrorMessage(context, message);
	}

	public static View getErrorMessage(Context context, String message) {
		TextView text = new TextView(context);
		text.setText(message);
		return text;
	}

	public static void registerActionRequestCode(int requestCode) {
		sActionRequestCodes.add(requestCode);
	}

	public static boolean isActionRequest(int requestCode) {
		return (requestCode == RequestCodes.ACTION ||
				requestCode == RequestCodes.ACTIONNOREFRESH ||
				requestCode == RequestCodes.ACTION_ALWAYS_SUCCESSFUL ||
				sActionRequestCodes.contains(requestCode));
	}

	/**
	 * Returns a pair of (height, width) of the window's dimensions in dips.
	 */
	public static Pair<Integer, Integer> getAppUsableScreenSize(Context context) {
		final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Point size = getAppUsableScreenSizeInPixels(windowManager);

		int heightInDips = Services.Device.pixelsToDips(size.y);
		int widthInDips = Services.Device.pixelsToDips(size.x);

		return new Pair<>(heightInDips, widthInDips);
	}

	public static Point getAppUsableScreenSizeInPixels(WindowManager windowManager) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			return getAppUsableScreenSizeApi30(windowManager);
		} else {
			return getAppUsableScreenSizeDeprecated(windowManager);
		}
	}

	@RequiresApi(Build.VERSION_CODES.R)
	private static Point getAppUsableScreenSizeApi30(WindowManager windowManager) {
		final WindowMetrics metrics = windowManager.getCurrentWindowMetrics();
		// Gets all excluding insets
		final WindowInsets windowInsets = metrics.getWindowInsets();
		Insets insets = windowInsets.getInsetsIgnoringVisibility(
			WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());

		int insetsWidth = insets.right + insets.left;
		int insetsHeight = insets.top + insets.bottom;

		// Legacy size that Display#getSize reports
		final Rect bounds = metrics.getBounds();
		return new Point(bounds.width() - insetsWidth, bounds.height() - insetsHeight);
	}

	@SuppressWarnings("deprecation")
	private static Point getAppUsableScreenSizeDeprecated(WindowManager windowManager) {
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}

	public static void addApplicationBarComponent(Activity activity, List<IInspectableComponent> activeComponents) {
		if (SherlockHelper.hasActionBar(activity)) {
			ApplicationBarControl appBarControl = new ApplicationBarControl(activity);
			LayoutItemDefinition definition = new LayoutItemDefinition(null);
			definition.setName(appBarControl.getName());
			View view = appBarControl.getRootView();
			view.setTag(LayoutTag.CONTROL_DEFINITION, definition);
			view.setTag(LayoutTag.CONTROL_THEME_CLASS, appBarControl.getThemeClass());
			view.setTag(LayoutTag.CONTROL_GENEXUS, true);
			activeComponents.add(appBarControl);
		}
	}

	public interface MainActivitySetListener {
		void onMainActivitySet();
	}
}

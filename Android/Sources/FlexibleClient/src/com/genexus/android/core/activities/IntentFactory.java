package com.genexus.android.core.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.genexus.android.R;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.IPatternMetadata;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.settings.PlatformDefinition;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.PlatformHelper;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.common.StorageHelper;
import com.genexus.android.core.controllers.IDataSourceController;
import com.genexus.android.core.controls.maps.common.LocationPickerConstants;
import com.genexus.android.core.ui.navigation.CallOptions;
import com.genexus.android.core.ui.navigation.CallOptionsHelper;
import com.genexus.android.core.utils.ThemeUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;

public class IntentFactory {
	public static Intent createNotificationActionIntent(Context context, String action, String params, String object, boolean forceNewTask) {
		Intent intent = new Intent();
		intent.setClass(context, StartupActivity.class);
		intent.putExtra(StartupActivity.EXTRA_NOTIFICATION_ACTION, action);
		intent.putExtra(StartupActivity.EXTRA_NOTIFICATION_PARAMS, params);
		intent.putExtra(StartupActivity.EXTRA_NOTIFICATION_OBJECT, object);

		if (Services.Strings.hasValue(action)) {
			// set a random action to launch always as new intent
			intent.setAction(String.valueOf(Math.random()));
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}

		if (forceNewTask) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}

		return intent;
	}

	public static Intent getMainObject(IViewDefinition mainView, Activity activity, boolean asRoot) {
		Intent intent = new Intent();
		intent.putExtra(IntentParameters.IS_STARTUP_ACTIVITY, true);

		if (mainView instanceof DashboardMetadata) {
			intent.setClass(activity.getApplicationContext(), GenexusActivity.class);
			intent.putExtra(IntentParameters.DASHBOARD_METADATA, mainView.getName());
		} else if (mainView instanceof IDataViewDefinition) {
			setupDataViewIntent(intent, activity.getApplicationContext(),
					mainView.getConnectivitySupport(), mainView, Collections.emptyList(),
					DisplayModes.VIEW, null);
		} else {
			String message = String.format("%s is not a valid main view definition.", mainView.getObjectName());
			throw new IllegalArgumentException(message);
		}

		if (asRoot && !Services.Application.hasActiveMiniApp()) {
			IPatternMetadata pattern = Services.Application.getDefinition().getPattern(Services.Application.get().getAppEntry());

			if (pattern instanceof WorkWithDefinition || isDashboardTabOrNavigationSlide(pattern)) {
				// if ww is the main, the main dashboard call to it and the main is not in the stack
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			} else {
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			}
		}

		return intent;
	}

	public static Intent getStartupActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, StartupActivity.class);
		return intent;
	}

	private static boolean isDashboardTabOrNavigationSlide(IPatternMetadata pattern) {
		if (pattern instanceof DashboardMetadata) {
			DashboardMetadata dashboard = (DashboardMetadata) pattern;

			if (dashboard.getControl().equalsIgnoreCase(DashboardMetadata.CONTROL_TABS)
					|| PlatformHelper.getNavigationStyle() == PlatformDefinition.NAVIGATION_SLIDE) {
				return true;
			}
		}
		return false;
	}

	public static Intent getStartupActivityWithNewServicesURL(Context context, String newServicesURL) {
		Intent intent = getStartupActivity(context);
		intent.putExtra(IntentParameters.SERVER_URL, newServicesURL);
		return intent;
	}

	public static Intent getStartupActivityWithReloadMetadata(Context context) {
		Intent intent = getStartupActivity(context);
		intent.putExtra(IntentParameters.RELOAD_METADATA, true);

		// clear stack and reload app
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

		return intent;
	}

	public static Intent getDashboard(UIContext context, IViewDefinition dataView, List<String> parameters, short mode) {
		Intent intent = new Intent();
		setupDataViewIntent(intent, context.getApplicationContext(), context.getConnectivitySupport(),
				dataView, parameters, mode, null);
		intent.putExtra(IntentParameters.DASHBOARD_METADATA, dataView.getObjectName());
		return intent;
	}

	/**
	 * Returns an intent to open a webpage in an internal WebView (that shares session state,
	 * i.e. cookies) with the Android application. This should be used instead of startWebBrowser()
	 * whenever sharing this state is relevant. For example, web login, calling a webpanel that
	 * needs to share the GAM token, &c.
	 */
	public static Intent getIntentForWebApplication(Context context, String link) {
		Intent intent = new Intent();
		intent.setClass(context, WebViewActivity.class);
		intent.putExtra(ActivityLauncher.INTENT_EXTRA_LINK_ACTION, link);
		intent.putExtra("ShareSession", true);
		return intent;
	}

	public static Intent getMultimediaViewerIntent(Context context, String link, boolean showButtons, boolean autoPlay,
												   Orientation orientation, int currentPosition, float playbackRate) {
		Intent intent = new Intent();
		intent.setClass(context, VideoViewActivity.class);
		setIntentFlagsNewDocument(intent);

		// If not an absolute URL, add base path
		if (!link.contains("://") && !StorageHelper.isLocalFile(link))
			link = Services.Application.get().UriMaker.getImageUrl(link);

		intent.putExtra(VideoViewActivity.INTENT_EXTRA_LINK, link);
		intent.putExtra(VideoViewActivity.INTENT_EXTRA_SHOW_BUTTONS, showButtons);
		intent.putExtra(VideoViewActivity.INTENT_EXTRA_AUTO_PLAY, autoPlay);
		intent.putExtra(VideoViewActivity.INTENT_EXTRA_PLAYBACK_RATE, playbackRate);

		if (orientation != null)
			intent.putExtra(VideoViewActivity.INTENT_EXTRA_ORIENTATION, orientation.toString());

		if (currentPosition != 0)
			intent.putExtra(VideoViewActivity.INTENT_EXTRA_CURRENT_POSITION, currentPosition);

		return intent;
	}

	public static Intent getIntent(UIContext context, IViewDefinition dataView, List<String> parameters, short mode, Map<String, String> fieldParameters)
	{
		Intent intent = new Intent();
		setupDataViewIntent(intent, context, context.getConnectivitySupport(), dataView, parameters, mode, fieldParameters);
		return intent;
	}

	static void setupDataViewIntent(Intent intent, Context context, Connectivity connectivity,
	                                IViewDefinition dataView, List<String> parameters, short mode,
	                                Map<String, String> fieldParameters)
	{
		intent.setClass(context, GenexusActivity.class);
		intent.putExtra(ActivityLauncher.INTENT_EXTRA_OBJECT_NAME, dataView.getObjectName());
		intent.putExtra(IntentParameters.DATA_VIEW, dataView.getName());
		intent.putExtra(IntentParameters.MODE, mode);
		intent.putExtra(IntentParameters.CONNECTIVITY, connectivity);

		CallOptions callOptions = CallOptionsHelper.getCallOptions(dataView, mode);
		CallOptionsHelper.setCurrentCallOptions(intent, callOptions);

		IntentHelper.putList(intent, IntentParameters.PARAMETERS, parameters);
		IntentHelper.putMap(intent, IntentParameters.BC_FIELD_PARAMETERS, fieldParameters);
	}

	public static void setupLocationPickerIntent(Intent intent, String initialLocation, String mapType, boolean showMyLocation, int zoom, boolean autocompleteEnabled) {
		intent.putExtra(LocationPickerConstants.EXTRA_SHOW_LOCATION, showMyLocation);
		intent.putExtra(LocationPickerConstants.EXTRA_AUTOCOMPLETE_ENABLED, autocompleteEnabled);
		if (Strings.hasValue(mapType))
			intent.putExtra(LocationPickerConstants.EXTRA_MAP_TYPE, mapType);
		if (Strings.hasValue(initialLocation))
			intent.putExtra(LocationPickerConstants.EXTRA_LOCATION, initialLocation);
		if (zoom != 0)
			intent.putExtra(LocationPickerConstants.EXTRA_ZOOM, zoom);
	}

	public static Intent getLocationPickerIntent(Activity context, String initialLocation, String mapType, boolean showMyLocation, int zoom)
	{
		Class<? extends Activity> pickerClass = Services.Maps.getLocationPickerActivityClass();
		if (pickerClass != null)
		{
			Intent intent = new Intent();
			intent.setClass(context, pickerClass);

			//Autocomplete is only enabled for Google for now
			String providerId = Services.Maps.getProviderId();
			boolean autocompleteEnabled = providerId != null && providerId.equalsIgnoreCase("MAPS_GOOGLE_V2");
			setupLocationPickerIntent(intent, initialLocation, mapType, showMyLocation, zoom, autocompleteEnabled);
			return intent;
		}
		return null;
	}

	@SuppressLint("InlinedApi")
	public static void setIntentFlagsNewDocument(Intent intent)
	{
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
	}

	public static @NonNull Intent newCustomTabsIntent(@NonNull Context context, @NonNull Uri uri)
	{
		CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
		builder.setShowTitle(true);

		Integer primaryColor = ThemeUtils.getAndroidThemeColorId(context, R.attr.colorPrimary);
		if (primaryColor != null)
			builder.setToolbarColor(primaryColor);

		Intent intent = builder.build().intent;
		intent.setData(uri);
		return intent;
	}

	public static @NonNull Intent getFiltersIntent(UIContext context, IDataSourceController dataSource) {
		Intent intent = new Intent();
		intent.setClass(context, FiltersActivity.class);
		IntentFactory.setIntentFlagsNewDocument(intent);
		IntentHelper.putObject(intent, IntentParameters.Filters.DATA_SOURCE, IDataSourceDefinition.class, dataSource.getDefinition());
		IntentHelper.putObject(intent, IntentParameters.Filters.URI, GxUri.class, dataSource.getModel().getUri());
		intent.putExtra(IntentParameters.Filters.DATA_SOURCE_ID, dataSource.getId());
		intent.putExtra(IntentParameters.Filters.FILTERS_FK, dataSource.getModel().getFilterExtraInfo());
		intent.putExtra(IntentParameters.CONNECTIVITY, context.getConnectivitySupport());
		return intent;
	}

	public static @NonNull Intent getDetailFiltersIntent(UIContext context, IDataSourceDefinition dataSource, String attName, String rangeBegin, String rangeEnd, String filterDefault, String filterRangeFk) {
		Intent next = new Intent();
		next.setClass(context, DetailFiltersActivity.class);
		IntentHelper.putObject(next, IntentParameters.Filters.DATA_SOURCE, IDataSourceDefinition.class, dataSource);
		next.putExtra(IntentParameters.ATT_NAME, attName);
		next.putExtra(IntentParameters.RANGE_BEGIN, rangeBegin);
		next.putExtra(IntentParameters.RANGE_END, rangeEnd);
		next.putExtra(IntentParameters.FILTER_DEFAULT, filterDefault);
		next.putExtra(IntentParameters.FILTER_RANGE_FK, filterRangeFk);
		next.putExtra(IntentParameters.CONNECTIVITY, context.getConnectivitySupport());
		return next;
	}

	public static @NonNull Intent getWebViewActivityIntent(Context context, String link) {
		Intent webViewIntent = new Intent(context, WebViewActivity.class);
		webViewIntent.putExtra(ActivityLauncher.INTENT_EXTRA_LINK_ACTION, link);
		IntentFactory.setIntentFlagsNewDocument(webViewIntent);
		return webViewIntent;
	}
}

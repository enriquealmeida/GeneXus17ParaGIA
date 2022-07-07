package com.genexus.android.core.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.application.Preferences;
import com.genexus.android.core.base.metadata.DetailDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.RelationDefinition;
import com.genexus.android.core.base.metadata.WWLevelDefinition;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.controllers.IDataSourceController;
import com.genexus.android.core.controls.SearchBoxDefinition;
import com.genexus.android.core.ui.navigation.CallOptions;
import com.genexus.android.core.ui.navigation.CallOptionsHelper;
import com.genexus.android.core.ui.navigation.CallTarget;
import com.genexus.android.core.ui.navigation.CallType;
import com.genexus.android.core.utils.Cast;

import androidx.annotation.NonNull;

public class ActivityLauncher
{
	public static final String INTENT_EXTRA_OBJECT_NAME = "com.artech.activities.ActivityLauncher.ObjectName";
	public static final String INTENT_EXTRA_LINK_ACTION = "Link";

	public static void call(UIContext context, IDataViewDefinition dataView, List<String> parameters)
	{
		Intent intent = IntentFactory.getIntent(context, dataView, parameters, DisplayModes.VIEW, null);
		startActivity(context.getActivity(), intent);
	}

	public static void callSearchResult(UIContext context, IDataViewDefinition dataView, List<String> parameters,
										SearchBoxDefinition searchBoxDefinition)
	{
		Intent intent = IntentFactory.getIntent(context, dataView, parameters, DisplayModes.VIEW, null);
		intent.setClass(context, SearchResultsActivity.class);
		intent.putExtra(IntentParameters.SEARCH_BOX_DEFINITION, searchBoxDefinition);
		startActivity(context.getActivity(), intent);
	}

	public static void callForResult(UIContext context, IDataViewDefinition dataView, List<String> parameters, int requestCode, boolean isSelecting)
	{
		Intent intent = IntentFactory.getIntent(context, dataView, parameters, DisplayModes.VIEW, null);
		intent.putExtra(IntentParameters.IS_SELECTING, isSelecting);
		startActivityForResult(context.getActivity(), intent, requestCode);
	}

	/**
	 * @deprecated 2021-11-05; #to-remove: v17u9 <br>
	 * Use {@link IntentFactory#getIntent(UIContext, IViewDefinition, List, short, Map)} instead
	 */
	@Deprecated
	public static Intent getIntent(UIContext context, IViewDefinition dataView, List<String> parameters, short mode, Map<String, String> fieldParameters)
	{
		return IntentFactory.getIntent(context, dataView, parameters, mode, fieldParameters);
	}

	/**
	 * @deprecated 2021-11-05; #to-remove: v17u9 <br>
	 * Use {@link IntentFactory#setupLocationPickerIntent(Intent, String, String, boolean, int, boolean)} instead
	 */
	@Deprecated
	public static void setupLocationPickerIntent(Intent intent, String initialLocation, String mapType, boolean showMyLocation, int zoom, boolean autocompleteEnabled) {
		IntentFactory.setupLocationPickerIntent(intent, initialLocation, mapType, showMyLocation, zoom, autocompleteEnabled);
	}

	public static void callRelated(UIContext context, Entity baseEntity, RelationDefinition relation)
	{
		// TODO: We should have a global dictionary for Entity -> View Panel, just like for edit panels.
		WorkWithDefinition relatedWorkWith = Services.Application.getDefinition().getWorkWithForBC(relation.getBCRelated());
		if (relatedWorkWith != null && relatedWorkWith.getLevels().size() != 0 && relatedWorkWith.getLevel(0).getDetail() != null)
		{
			DetailDefinition detail = relatedWorkWith.getLevel(0).getDetail();
			ArrayList<String> keys = new ArrayList<>();
			for (String att : relation.getKeys())
				keys.add((String) baseEntity.getProperty(att));

			Intent intent = new Intent();
			intent.setClass(context, GenexusActivity.class);
			intent.putExtra(IntentParameters.CONNECTIVITY, context.getConnectivitySupport());
			intent.putExtra(IntentParameters.DATA_VIEW, detail.getName());
			IntentHelper.putList(intent, IntentParameters.PARAMETERS, keys);

			startActivity(context.getActivity(), intent);
		}
	}

	public static boolean call(UIContext context, String workWithName, List<String> parameters)
	{
		IDataViewDefinition definition = getDefaultDataView(workWithName);
		if (definition != null)
		{
			call(context, definition, parameters);
			return true;
		}
		else
			return false;
	}

	public static boolean callSearchResult(UIContext context, String workWithName, List<String> parameters,
										   SearchBoxDefinition searchBoxDefinition)
	{
		IDataViewDefinition definition = getDefaultDataView(workWithName);
		if (definition != null)
		{
			callSearchResult(context, definition, parameters, searchBoxDefinition);
			return true;
		}
		else
			return false;
	}

	public static void callPreferencesPanel(Activity activity, String serverUrl, boolean showToast, int messageRes) {
		String dynamicUrlPanel = Services.Application.get().getDynamicUrlPanel();
		IViewDefinition view = Strings.hasValue(dynamicUrlPanel) ? Services.Application.getDefinition().getView(dynamicUrlPanel) : null;
		Intent intent;
		if (view == null)
			intent = Preferences.newIntent(activity, showToast, messageRes, serverUrl);
		else {
			Connectivity connectivity = view.getConnectivitySupport();
			if (connectivity == Connectivity.Inherit) connectivity = Connectivity.Offline;
			intent = IntentFactory.getIntent(UIContext.base(activity, connectivity),
					view, null, DisplayModes.VIEW, null);
		}

		startActivityForResult(activity, intent, RequestCodes.PREFERENCE);
	}

	public static boolean call(UIContext context, String workWithName)
	{
		return call(context, workWithName, null);
	}

	public static boolean callForResult(UIContext from, String workWithName, int requestCode)
	{
		IDataViewDefinition definition = getDefaultDataView(workWithName);
		if (definition != null)
		{
			callForResult(from, definition, null, requestCode, false);
			return true;
		}
		else
			return false;
	}

	private static IDataViewDefinition getDefaultDataView(String objectName)
	{
		WorkWithDefinition pattern = Cast.as(WorkWithDefinition.class, Services.Application.getDefinition().getPattern(objectName));
		if (pattern != null && pattern.getLevels().size() != 0)
		{
			WWLevelDefinition wwLevel = pattern.getLevel(0);
			if (wwLevel != null)
			{
				if (wwLevel.getList() != null)
					return wwLevel.getList();
				else if (wwLevel.getDetail() != null)
					return wwLevel.getDetail();
			}
		}

		return null; // Could not find WW definition, or it was empty.
	}

	public static void callLocationPicker(Activity context, String initialLocation, String mapType, boolean showMyLocation, int zoom)
	{
		Intent intent = IntentFactory.getLocationPickerIntent(context, initialLocation, mapType, showMyLocation, zoom);
		if (intent!=null)
		{
			context.startActivityForResult(intent, RequestCodes.PICKER);
		}
	}

	/**
	 * @deprecated 2021-11-05; #to-remove: v17u9 <br>
	 * Use {@link IntentFactory#getLocationPickerIntent(Activity, String, String, boolean, int)} instead
	 */
	@Deprecated
	public static Intent getLocationPickerIntent(Activity context, String initialLocation, String mapType, boolean showMyLocation, int zoom)
	{
		return IntentFactory.getLocationPickerIntent(context, initialLocation, mapType, showMyLocation, zoom);
	}

	public static void startWebBrowser(Context context, String link)
	{
		// According to https://developer.chrome.com/multidevice/android/customtabs
		// Caller should not be setting FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_NEW_DOCUMENT.
		Intent intent = IntentFactory.newCustomTabsIntent(context, Uri.parse(link));

		try
		{
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			// Should never happen (unless no browser is installed!?).
			// Fall back to legacy WebViewActivity.
			Intent webViewIntent = IntentFactory.getWebViewActivityIntent(context, link);
			context.startActivity(webViewIntent);
		}
	}

	public static void callViewVideo(Context context, String link)
	{
		Intent intent = IntentFactory.getMultimediaViewerIntent(context, link, true, true, null, 0, 100);
   		context.startActivity(intent);
	}

	public static void callViewVideoFullscreen(Context context, String link, boolean showControls, boolean autoPlay,
											   Orientation orientation, int currentPosition, float playbackRate)
	{
		Intent intent = IntentFactory.getMultimediaViewerIntent(context, link, showControls, autoPlay, orientation, currentPosition, playbackRate);
		context.startActivity(intent);
	}

	public static void callViewAudio(Context context, String link)
	{
		Intent intent = IntentFactory.getMultimediaViewerIntent(context, link, true, true, null, 0, 100);
   		intent.putExtra(VideoViewActivity.INTENT_EXTRA_IS_AUDIO, true);
   		intent.putExtra(VideoViewActivity.INTENT_EXTRA_SHOW_BUTTONS, true);
   		context.startActivity(intent);
	}

	public static void callLogin(UIContext from)
	{
		// Call login panel.
		String loginObject = Services.Application.get().getLoginObject();
		WorkWithDefinition wwMetadata = (WorkWithDefinition) Services.Application.getDefinition().getPattern(loginObject);

		if (wwMetadata != null && wwMetadata.getLevels().size() != 0 && wwMetadata.getLevel(0).getDetail() != null)
		{
			CallOptionsHelper.setCallOption(loginObject, CallOptions.OPTION_TARGET, CallTarget.BLANK.getName());
			ActivityLauncher.callForResult(from, wwMetadata.getLevel(0).getDetail(), null, RequestCodes.LOGIN, false);
		}
		else
			Services.Log.error(String.format("Login object (%s) is not defined.", loginObject));
	}

	public static void callFilters(UIContext context, IDataSourceController dataSource)
	{
		Intent intent = IntentFactory.getFiltersIntent(context, dataSource);
		context.getActivity().startActivityForResult(intent, RequestCodes.FILTERS);
	}

	public static void callDetailFilters(UIContext context, IDataSourceDefinition dataSource, String attName, String rangeBegin, String rangeEnd, String filterDefault, String filterRangeFk)
	{
		Intent next = IntentFactory.getDetailFiltersIntent(context, dataSource, attName, rangeBegin, rangeEnd, filterDefault, filterRangeFk);
    	context.getActivity().startActivityForResult(next, 0);
	}

	/**
	 * Start an activity from a given context.
	 * This called is made on the UI thread (so that transitions work).
	 */
	public static void startActivity(final Activity from, final Intent intent)
	{
		Services.Device.runOnUiThread(() -> {
			from.startActivity(intent);
			applyCallOptions(from, intent);
		});
	}

	public static void startActivityForResult(final Activity from, final Intent intent, final int requestCode)
	{
		Services.Device.runOnUiThread(() -> {
			from.startActivityForResult(intent, requestCode);
			applyCallOptions(from, intent);
		});
	}

	public static void applyCallOptions(Activity fromActivity, Intent intent)
	{
		String objectName = intent.getStringExtra(INTENT_EXTRA_OBJECT_NAME);
		CallOptions callOptions = CallOptionsHelper.getCurrentCallOptions(intent);

		if (callOptions != null && fromActivity != null)
		{
			// Use enter/exit effects.
			if (callOptions.getEnterEffect() != null)
				callOptions.getEnterEffect().onCall(fromActivity);

			// Use replace/push.
			// TODO: Use popup, callout, target, target size.
			if (callOptions.getCallType() == CallType.REPLACE)
			{
				intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
				fromActivity.finish();
			}
		}

		// Remove global configured CallOptions after call.
		CallOptionsHelper.resetCallOptions(objectName);
	}

	public static void onReturn(Activity from, Intent intent)
	{
		CallOptions callOptions = CallOptionsHelper.getCurrentCallOptions(intent);

		if (callOptions != null && callOptions.getExitEffect() != null)
			callOptions.getExitEffect().onReturn(from);
	}

	/**
	 * @deprecated 2021-11-05; #to-remove: v17u9 <br>
	 * Use {@link IntentFactory#setIntentFlagsNewDocument(Intent)} instead
	 */
	@Deprecated
	public static void setIntentFlagsNewDocument(Intent intent)
	{
		IntentFactory.setIntentFlagsNewDocument(intent);
	}

	/**
	 * @deprecated 2021-11-05; #to-remove: v17u9 <br>
	 * Use {@link IntentFactory#newCustomTabsIntent(Context, Uri)} instead
	 */
	@Deprecated
	public static @NonNull Intent newCustomTabsIntent(@NonNull Context context, @NonNull Uri uri)
	{
		return IntentFactory.newCustomTabsIntent(context, uri);
	}
}

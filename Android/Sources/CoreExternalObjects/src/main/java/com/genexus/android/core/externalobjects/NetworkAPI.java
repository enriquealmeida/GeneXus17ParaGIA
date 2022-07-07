package com.genexus.android.core.externalobjects;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.ValidateAndSaveDynamicUrl;
import com.genexus.android.core.common.ValidateAppServerUri;
import com.genexus.android.core.common.ValidateAppServerUriListener;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.providers.EntityDataProvider;
import com.genexus.android.core.externalobjects.network.NetworkBroadcastReceiver;
import com.genexus.android.core.externalobjects.network.NetworkCallback;

import androidx.annotation.RequiresApi;

/**
 * This class allow access to network information from API.
 */
@SuppressWarnings("FieldCanBeLocal")
public class NetworkAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.SD.Network";
	private static final String PROPERTY_APPLICATION_SERVER_URL = "ApplicationServerURL";
	private static final String METHOD_IS_SERVER_AVAILABLE = "IsServerAvailable";
	private static final String METHOD_TYPE = "Type";
	private static final String METHOD_TRAFFIC_BASED_COST = "TrafficBasedCost";
	private static final String METHOD_SET_APPLICATION_SERVER_URL = "SetApplicationServerURL";
	private static final String EVENT_NETWORK_STATUS_CHANGED = "NetworkStatusChanged";

	public NetworkAPI(ApiAction action) {
		super(action);
		addReadonlyPropertyHandler(PROPERTY_APPLICATION_SERVER_URL, mGetApplicationServerURLProperty);
		addMethodHandler(METHOD_IS_SERVER_AVAILABLE, 0, mIsServerAvailableMethod);
		addMethodHandler(METHOD_IS_SERVER_AVAILABLE, 1, mIsServerAvailableMethod);
		addMethodHandler(METHOD_TYPE, 0, mTypeMethod);
		addMethodHandler(METHOD_TRAFFIC_BASED_COST, 0, mTrafficBasedCostMethod);
		addMethodHandler(METHOD_SET_APPLICATION_SERVER_URL, 1, mSetApplicationServerMethod);
	}

	private final ValidateAppServerUriListener mValidateAppServerUriListener = (url, result) -> {
		ApiAction apiAction = getAction();
		switch (result) {
			case ValidateAppServerUri.VALID_URL:
				GenexusApplication application = Services.Application.get();
				if (application.isSecure()) {
					if (Strings.hasValue(application.getAPIUri()))
						SecurityHelper.logout();
				} else
					EntityDataProvider.clearAllCaches();

				ActionExecution.cancelCurrent(apiAction);
				Intent intent = IntentFactory.getStartupActivityWithNewServicesURL(getActivity(), url);
				Services.Device.runOnUiThread(() -> getActivity().startActivity(intent));
				break;
			case ValidateAppServerUri.INVALID_URL:
			case ValidateAppServerUri.NO_CONNECTION:
				ActionExecution.continueCurrent(getActivity(), true, apiAction);
				break;
		}
	};

	private final IMethodInvoker mGetApplicationServerURLProperty =
		parameters -> ExternalApiResult.success(NetworkAPIOffline.applicationServerUrl());

	private final IMethodInvoker mIsServerAvailableMethod = parameters -> {
		String serverAddress = (parameters.size() != 0 ? parameters.get(0).toString() : null);
		return ExternalApiResult.success(NetworkAPIOffline.isServerAvailable(serverAddress));
	};

	private final IMethodInvoker mTypeMethod =
		parameters -> ExternalApiResult.success(NetworkAPIOffline.type());

	private final IMethodInvoker mTrafficBasedCostMethod =
		parameters -> ExternalApiResult.success(NetworkAPIOffline.trafficBasedCost());

	private final IMethodInvoker mSetApplicationServerMethod = parameters -> {
		String currentUrl = NetworkAPIOffline.applicationServerUrl();
		String newUrl = (String) parameters.get(0);
		if (currentUrl != null && currentUrl.equalsIgnoreCase(newUrl))
			return ExternalApiResult.SUCCESS_CONTINUE;

		ValidateAndSaveDynamicUrl.execute(newUrl, mValidateAppServerUriListener);
		return ExternalApiResult.SUCCESS_WAIT;
	};

	public static void registerNetworkChangeListener(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			registerNetworkCallback(context, connectivityManager);
		} catch (SecurityException e) {
			Services.Log.info("We're in the case of the Android 6.0.0 bug in which is not" +
				"being granted automatically for an app that declares it in the AndroidManifest.xml" +
				"as it happens in 6.0.1+. Using the fallback method...");
			registerBroadcastReceiver(context, connectivityManager);
		}

	}

	@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
	private static void registerNetworkCallback(Context context, ConnectivityManager connectivityManager) {
		connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new NetworkCallback(context, connectivityManager, EVENT_NETWORK_STATUS_CHANGED));
	}

	@SuppressWarnings("deprecation")
	private static void registerBroadcastReceiver(Context context, ConnectivityManager connectivityManager) {
		IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(new NetworkBroadcastReceiver(connectivityManager, EVENT_NETWORK_STATUS_CHANGED), intentFilter);
	}
}

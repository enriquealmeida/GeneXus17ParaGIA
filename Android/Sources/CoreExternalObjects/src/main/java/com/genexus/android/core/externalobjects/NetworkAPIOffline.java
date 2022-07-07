package com.genexus.android.core.externalobjects;

import android.content.Context;
import android.net.ConnectivityManager;
import androidx.core.net.ConnectivityManagerCompat;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

/**
 * This class allow access to device information.
 */
public class NetworkAPIOffline {
	//Properties

	/***
	 * Return a value that identify the server uri.
	 * @return
	 */
	public static String applicationServerUrl() {
		return Services.Application.get().getAPIUri();
	}


	//Methods

	/***
	 * Returns if app server url is available
	 * @return
	 */
	public static boolean isServerAvailable() {
		return isServerAvailable(null);
	}

	/***
	 * Returns if url is available
	 * @return
	 */
	public static boolean isServerAvailable(String url) {
		if (!Strings.hasValue(url))
			url = Services.Application.get().UriMaker.getRootUrl();

		return Services.HttpService.isReachable(url);
	}

	/***
	 * Returns type of connection
	 * @return
	 */
	public static int type() {
		return Services.HttpService.connectionType();
	}

	/***
	 * Returns type of connection to url
	 * @return
	 */
	public static int type(String url) {
		return Services.HttpService.connectionType();
	}

	/***
	 * Returns if traffic cost
	 * @return
	 */
	public static boolean trafficBasedCost() {
		ConnectivityManager connManager = (ConnectivityManager) Services.Application.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		return ConnectivityManagerCompat.isActiveNetworkMetered(connManager);
	}

	/***
	 * Returns if url traffic cost
	 * @return
	 */
	public static boolean trafficBasedCost(String url) {
		// We have no distinction for URL here.
		return trafficBasedCost();
	}
}

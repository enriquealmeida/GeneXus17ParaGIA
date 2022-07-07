package com.genexus.android.core.externalobjects.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.core.net.ConnectivityManagerCompat;

import com.genexus.android.api.EventDispatcher;
import com.genexus.android.core.base.synchronization.SynchronizationWhenConnected;
import com.genexus.android.core.externalobjects.NetworkAPI;

import java.util.Collections;

/**
 * Monitors changes to the network status.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {
	private final ConnectivityManager mConnectivityManager;
	private final String mEventName;

	public NetworkBroadcastReceiver(@NonNull ConnectivityManager connectivityManager,
									@NonNull String eventName) {
		mConnectivityManager = connectivityManager;
		mEventName = eventName;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onReceive(Context context, Intent intent) {
		android.net.NetworkInfo networkInfo = ConnectivityManagerCompat.getNetworkInfoFromBroadcast(mConnectivityManager, intent);

		// Fire Network.NetworkStatusChanged event to current panel.
		EventDispatcher.fireEvent(context, NetworkAPI.OBJECT_NAME, mEventName, Collections.emptyList());

		// Signal connection change to synchronization; it might want to send data.
		SynchronizationWhenConnected.onNetworkStatusChanged(context, networkInfo);
	}
}

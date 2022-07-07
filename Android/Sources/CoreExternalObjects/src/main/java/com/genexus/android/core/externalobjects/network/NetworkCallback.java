package com.genexus.android.core.externalobjects.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.genexus.android.api.EventDispatcher;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.synchronization.SynchronizationWhenConnected;
import com.genexus.android.core.externalobjects.NetworkAPI;

import java.util.Collections;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallback extends ConnectivityManager.NetworkCallback {
    private final Context mContext;
    private final ConnectivityManager mConnectivityManager;
	private final String mEventName;

	public NetworkCallback(@NonNull Context context,
						   @NonNull ConnectivityManager connectivityManager,
						   @NonNull String eventName) {
        mContext = context;
        mConnectivityManager = connectivityManager;
        mEventName = eventName;
    }

    private void onNetworkStatusChanged(NetworkCapabilities networkCapabilities) {
        // Fire Network.NetworkStatusChanged event to current panel.
        EventDispatcher.fireEvent(mContext, NetworkAPI.OBJECT_NAME, mEventName, Collections.emptyList());

        // Signal connection change to synchronization; it might want to send data.
        SynchronizationWhenConnected.onNetworkStatusChanged(mContext, networkCapabilities);
    }

    private void onNetworkStatusChanged(Network network) {
        NetworkCapabilities networkCapabilities = mConnectivityManager.getNetworkCapabilities(network);
        onNetworkStatusChanged(networkCapabilities);
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        Services.Log.debug("requestNetwork onAvailable()");
        // warning:  Do NOT call ConnectivityManager.getNetworkCapabilities(android.net.Network)
		// from https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onAvailable(android.net.Network)
        // TODO: change this or remove networkCapabilities for this listener? Need test.
		onNetworkStatusChanged(network);
    }

    @Override
    public void onCapabilitiesChanged (@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
    }

    @Override
    public void onLinkPropertiesChanged (@NonNull Network network, @NonNull LinkProperties linkProperties) {
        Services.Log.debug("requestNetwork onLinkPropertiesChanged()");
        onNetworkStatusChanged(network);
    }

    @Override
    public void onLosing(@NonNull Network network, int maxMsToLive) {
        Services.Log.debug("requestNetwork onLosing()");
        onNetworkStatusChanged(network);
    }

    @Override
    public void onLost(@NonNull Network network) {
        Services.Log.debug("requestNetwork onLost()");
        onNetworkStatusChanged(network);
    }
}

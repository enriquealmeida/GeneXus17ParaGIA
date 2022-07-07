package com.genexus.android.websockets;

import android.app.Activity;

import com.genexus.android.core.application.LifecycleListeners;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.fragments.IDataView;

import java.net.UnknownServiceException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

class WebSocketsService extends WebSocketListener implements Foreground.Listener, LifecycleListeners.Component {
	private static final String SOCKET_CLOSE_EXCEPTION_MSG = "Socket close";

	private final WebSocketsEvents mEventsListener;
	private final OkHttpClient mOkHttpClient;
	private final int mMaxConnectionAttempts;

	private HttpUrl mEndpointUrl;
	private ConnectionStatus mConnectionStatus;
	private int mConnectionAttempts;
	private WebSocket mWebSocket;
	private boolean mShouldReconnect;

	public WebSocketsService(WebSocketsEvents eventsListener, OkHttpClient okHttpClient,
							 int maxConnectionAttempts, String endpointUrl) {
		mEventsListener = eventsListener;
		mOkHttpClient = okHttpClient;
		mMaxConnectionAttempts = maxConnectionAttempts;
		mEndpointUrl = parseEndpointUrl(endpointUrl);
		mConnectionStatus = ConnectionStatus.DISCONNECTED;
		mShouldReconnect = false;
	}

	private HttpUrl parseEndpointUrl(String url) {
		// Replace ws/wss schemes since OkHttp explicitly does not support them.
		// See https://github.com/square/okhttp/issues/4035
		if (url.startsWith("ws://")) {
			url = url.replaceFirst("ws://", "http://");
		} else if (url.startsWith("wss://")) {
			url = url.replaceFirst("wss://", "https://");
		}

		return HttpUrl.parse(url);
	}

	private void requestConnection(HttpUrl wsUrl) {
		Request request = new Request.Builder()
				.url(wsUrl)
				.build();
		mWebSocket = mOkHttpClient.newWebSocket(request, this);
		Services.Log.debug(getClass().getSimpleName(),
				"Attempting to connect to " + wsUrl.toString());
		mConnectionAttempts++;
	}

	public boolean connect(String url) {
		HttpUrl endpointUrl = parseEndpointUrl(url);
		if (endpointUrl == null) {
			Services.Log.debug(getClass().getSimpleName(), "Invalid endpoint URL: " + url);
			return false;
		}

		mEndpointUrl = endpointUrl;
		mConnectionAttempts = 0;
		requestConnection(endpointUrl);

		return true;
	}

	public void disconnectGracefully() {
		if (mWebSocket == null) {
			return;
		}
		mWebSocket.close(1000, "");
		mWebSocket = null;
	}

	public void disconnectAbruptly() {
		if (mWebSocket == null) {
			return;
		}
		mWebSocket.cancel();
		mWebSocket = null;
	}

	public void send(String message) {
		if (mWebSocket == null) {
			return;
		}
		mWebSocket.send(message);
	}

	public ConnectionStatus getConnectionStatus() {
		return mConnectionStatus;
	}

	@Override
	public void onOpen(WebSocket webSocket, Response response) {
		Services.Log.debug(getClass().getSimpleName(),
				"Connection has been opened to " + response.request().url());
		mConnectionAttempts = 0;
		mConnectionStatus = ConnectionStatus.CONNECTED;
		mEventsListener.onConnected();
	}

	@Override
	public void onMessage(WebSocket webSocket, String text) {
		Services.Log.debug(getClass().getSimpleName(), "Message received: '" + text + "' from " + webSocket.request().url());
		mEventsListener.onMessageReceived(text);
	}

	@Override
	public void onClosed(WebSocket webSocket, int code, String reason) {
		Services.Log.debug(getClass().getSimpleName(),
				"Connection has been closed gracefully");
		mWebSocket = null;
		mConnectionStatus = ConnectionStatus.DISCONNECTED;
		mEventsListener.onConnectionClosed();
	}

	@Override
	public void onFailure(WebSocket webSocket, Throwable t, Response response) {
		Services.Log.debug(getClass().getSimpleName(), "Connection has been lost to " + webSocket.request().url());
		mWebSocket = null;
		mConnectionStatus = ConnectionStatus.DISCONNECTED;

		if (t instanceof UnknownServiceException) {
			Services.Log.debug("Clear text not permitted", t);
			mEventsListener.onConnectionFailed();
		} else if (t.getMessage() != null && t.getMessage().contains(SOCKET_CLOSE_EXCEPTION_MSG)) {
			Services.Log.debug("The connection was closed by this client on purpose");
			mEventsListener.onConnectionFailed();
		} else if (mConnectionAttempts <= mMaxConnectionAttempts) {
			requestConnection(mEndpointUrl);
		} else {
			Services.Log.debug("Max connection attempts has been reached");
			mEventsListener.onConnectionFailed();
		}
	}

	@Override
	public void onBecameForeground(Activity foregroundActivity) {
		if (mShouldReconnect) {
			mShouldReconnect = false;
			mConnectionAttempts = 0;
			requestConnection(mEndpointUrl);
		}
	}

	@Override
	public void onBecameBackground() {
		if (mWebSocket != null) {
			mShouldReconnect = true;
		}
		disconnectAbruptly();
	}

	@Override
	public void onComponentInitialized(IDataView component) {
		Foreground.get().removeListener(this);
		Foreground.get().addListener(this);

		if (Utils.hasAnyEventDeclared(component.getDefinition()) && mWebSocket == null) {
			mConnectionAttempts = 0;
			requestConnection(mEndpointUrl);
		}
	}

	interface WebSocketsEvents {
		void onConnected();
		void onConnectionFailed();
		void onMessageReceived(String message);
		void onConnectionClosed();
	}

	enum ConnectionStatus {
		DISCONNECTED, CONNECTED
	}
}

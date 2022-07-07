package com.genexus.android.websockets;

import com.genexus.android.core.base.services.Services;

import static com.genexus.android.websockets.WebSocketClient.PROPERTY_STATUS_VALUE_CONNECTED;
import static com.genexus.android.websockets.WebSocketClient.PROPERTY_STATUS_VALUE_DISCONNECTED;

@SuppressWarnings({"unused", "WeakerAccess"})
public class WebSocketAPIOffline {

	private static WebSocketsService mWebSocketsService;

	public static void open(String url) {
		initializeIfNeeded();

		mWebSocketsService.disconnectAbruptly();
		boolean result = mWebSocketsService.connect(url);
		Services.Log.debug("WebSocket connection open: " + result);
	}

	public static void close() {
		initializeIfNeeded();

		mWebSocketsService.disconnectGracefully();
	}

	public static void send(String message) {
		initializeIfNeeded();

		mWebSocketsService.send(message);
	}

	public static int status() {
		initializeIfNeeded();

		switch (mWebSocketsService.getConnectionStatus()) {
			case DISCONNECTED:
				return Integer.parseInt(PROPERTY_STATUS_VALUE_DISCONNECTED);
			case CONNECTED:
				return Integer.parseInt(PROPERTY_STATUS_VALUE_CONNECTED);
			default:
				throw new IllegalArgumentException("Invalid Connection Status value");
		}
	}

	private static void initializeIfNeeded() {
		if (!isInitialized())
			mWebSocketsService = WebSocketsServiceFactory.getInstance();
	}

	private static boolean isInitialized() {
		return mWebSocketsService != null;
	}

}

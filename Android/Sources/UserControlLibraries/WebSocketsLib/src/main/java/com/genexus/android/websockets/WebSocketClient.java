package com.genexus.android.websockets;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class WebSocketClient extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.Client.Socket";

	private static final String PROPERTY_STATUS = "Status";
	private static final String METHOD_OPEN = "Open";
	private static final String METHOD_CLOSE = "Close";
	private static final String METHOD_SEND = "Send";
	public static final String EVENT_CONNECTED = "Connected";
	public static final String EVENT_CONNECTION_FAILED = "ConnectionFailed";
	public static final String EVENT_MESSAGE_RECEIVED = "MessageReceived";
	private static final String EVENT_CONNECTION_CLOSED = "ConnectionClosed";
	public static final String PROPERTY_STATUS_VALUE_DISCONNECTED = "0";
	public static final String PROPERTY_STATUS_VALUE_CONNECTED = "1";

	public static final WebSocketsService.WebSocketsEvents WEB_SOCKETS_EVENTS_LISTENER;
	private final WebSocketsService mWebSocketsService;

	public WebSocketClient(ApiAction action) {
		super(action);
		mWebSocketsService = WebSocketsServiceFactory.getInstance();
		addReadonlyPropertyHandler(PROPERTY_STATUS, mGetPropertyStatus);
		addMethodHandler(METHOD_OPEN, 1, mMethodOpen);
		addSimpleMethodHandler(METHOD_CLOSE, 0, mMethodClose);
		addSimpleMethodHandler(METHOD_SEND, 1, mMethodSend);
	}

	/*** PROPERTIES ***/
	private final IMethodInvoker mGetPropertyStatus = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			switch (mWebSocketsService.getConnectionStatus()) {
				case DISCONNECTED:
					return ExternalApiResult.success(PROPERTY_STATUS_VALUE_DISCONNECTED);
				case CONNECTED:
					return ExternalApiResult.success(PROPERTY_STATUS_VALUE_CONNECTED);
				default:
					throw new IllegalArgumentException("Invalid Connection Status value");
			}
		}
	};

	/*** METHODS ***/
	private final IMethodInvoker mMethodOpen = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String url = (String) parameters.get(0);
			mWebSocketsService.disconnectAbruptly();
			boolean result = mWebSocketsService.connect(url);
			return result ? ExternalApiResult.SUCCESS_CONTINUE : ExternalApiResult.FAILURE;
		}
	};

	private final ISimpleMethodInvoker mMethodClose = new ISimpleMethodInvoker() {
		@Override
		public Object invoke(List<Object> parameters) {
			mWebSocketsService.disconnectGracefully();
			return null;
		}
	};

	private final ISimpleMethodInvoker mMethodSend = new ISimpleMethodInvoker() {
		@Override
		public Object invoke(List<Object> parameters) {
			String message = (String) parameters.get(0);
			mWebSocketsService.send(message);
			return null;
		}
	};

	/*** EVENTS ***/
	static {
		WEB_SOCKETS_EVENTS_LISTENER = new WebSocketsService.WebSocketsEvents() {
			@Override
			public void onConnected() {
				Utils.fireEvent(WebSocketClient.OBJECT_NAME, EVENT_CONNECTED);
			}

			@Override
			public void onConnectionFailed() {
				Utils.fireEvent(WebSocketClient.OBJECT_NAME, EVENT_CONNECTION_FAILED);
			}

			@Override
			public void onMessageReceived(String message) {
				Utils.fireEvent(WebSocketClient.OBJECT_NAME, EVENT_MESSAGE_RECEIVED, message);
			}

			@Override
			public void onConnectionClosed() {
				Utils.fireEvent(WebSocketClient.OBJECT_NAME, EVENT_CONNECTION_CLOSED);
			}
		};
	}
}

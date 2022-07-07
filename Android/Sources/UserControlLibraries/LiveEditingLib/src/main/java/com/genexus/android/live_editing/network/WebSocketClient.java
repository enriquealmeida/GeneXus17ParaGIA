package com.genexus.android.live_editing.network;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import com.genexus.android.device.ClientInformation;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.HttpHeaders;
import com.genexus.android.live_editing.commands.CommandConnect;
import com.genexus.android.live_editing.commands.CommandKBDoesNotMatchGUID;
import com.genexus.android.live_editing.commands.CommandKbOk;
import com.genexus.android.live_editing.commands.IClientCommand;
import com.genexus.android.live_editing.commands.IServerCommand;
import com.genexus.android.live_editing.support.Endpoint;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.Buffer;
import okio.ByteString;

import static com.genexus.android.live_editing.LiveEditingGenexusModule.TAG;
import static com.genexus.android.core.utils.FileUtils2.SCHEME_HTTP;

class WebSocketClient extends WebSocketListener implements INetworkClient {
	private enum Status {
		DISCONNECTED,
		CONNECTING,
		CONNECTED
	}

	private final OkHttpClient mOkHttpClient;
	private final Gson mGson;
	private final INetworkEventsListener mNetworkEventsListener;
	private final ConnectionStringInfo mConnectionStringInfo;
	private final ConnectionAttempts mConnectionAttempts;

	private Status mStatus;
	private boolean mServerCommandsCompressionEnabled;
	private @Nullable WebSocket mWebSocket;

	public WebSocketClient(@NonNull OkHttpClient okHttpClient,
						   @NonNull Gson gson,
						   @NonNull INetworkEventsListener networkEventsListener,
						   @NonNull ConnectionStringInfo connectionStringInfo,
						   @NonNull List<Endpoint> savedEndpoints) {
		mOkHttpClient = okHttpClient;
		mGson = gson;
		mNetworkEventsListener = networkEventsListener;
		mConnectionStringInfo = connectionStringInfo;
		mConnectionAttempts = new ConnectionAttempts(mConnectionStringInfo.ipList, savedEndpoints);
		mStatus = Status.DISCONNECTED;
	}

	@Override
	public void connect() {
		mConnectionAttempts.reset();
		mStatus = Status.CONNECTING;
		attemptConnection();
	}

	@Override
	public void disconnect() throws IllegalStateException {
		if (mWebSocket == null) {
			throw new IllegalStateException("WebSocketClient is already disconnected");
		}
		mWebSocket.close(1000, "");
		mWebSocket = null;
		mStatus = Status.DISCONNECTED;
		mNetworkEventsListener.onConnectionDropped();
	}

	@Override
	public void send(IClientCommand command) {
		if (mWebSocket == null) {
			throw new IllegalStateException("WebSocketClient is disconnected");
		}

		JsonObject json = mGson.toJsonTree(command, IClientCommand.class).getAsJsonObject();

		json.addProperty(HttpHeaders.DEVICE_ID, ClientInformation.id());
		json.addProperty(HttpHeaders.DEVICE_TYPE, ClientInformation.deviceType());
		json.addProperty(HttpHeaders.DEVICE_NAME, ClientInformation.deviceName());
		json.addProperty(HttpHeaders.DEVICE_OS_NAME, ClientInformation.osName());
		json.addProperty(HttpHeaders.DEVICE_OS_VERSION, ClientInformation.osVersion());
		json.addProperty("Language", Services.Language.getCurrentLanguage());
		json.addProperty("Theme", Services.Themes.getCurrentThemeName());
		json.addProperty("KBUUID", mConnectionStringInfo.kbUUID);
		json.addProperty("KBName", mConnectionStringInfo.kbName);
		json.addProperty("GZip", true);

		String jsonContent = mGson.toJson(json);

		boolean enqueued;

		if (!(command instanceof CommandConnect) && mServerCommandsCompressionEnabled) {
			Buffer contentBuffer = new Buffer();
			contentBuffer.writeUtf8(jsonContent);

			Buffer gzippedContent;
			try {
				gzippedContent = OkioUtils.gzip(contentBuffer);
			} catch (IOException e) {
				Services.Log.debug(getClass().getName(), "Error while compressing message content.");
				return;
			}
			enqueued = mWebSocket.send(gzippedContent.readByteString());
		} else {
			enqueued = mWebSocket.send(jsonContent);
		}

		if (!enqueued) {
			Services.Log.debug(getClass().getName(), "Attempt to send a command failed. The websocket either " +
					"overflowed, is closing or is already closed.");
		}
	}

	private void attemptConnection() {
		boolean maxAttemptsReached = !mConnectionAttempts.hasLeft();
		if (maxAttemptsReached) {
			mWebSocket = null;
			mStatus = Status.DISCONNECTED;
			mNetworkEventsListener.onMaxAttemptsReached();
		} else {
			Endpoint endpoint = mConnectionAttempts.getNext();

			HttpUrl httpUrl = new HttpUrl.Builder()
					.scheme(SCHEME_HTTP) // OkHttp requires it instead of ws:// scheme. See PR #1663.
					.host(endpoint.ip)
					.port(endpoint.port)
					.addEncodedPathSegment("live/")
					.build();

			Request request = new Request.Builder()
					.url(httpUrl)
					.build();

			mWebSocket = mOkHttpClient.newWebSocket(request, this);

			mNetworkEventsListener.onConnectionAttempt(endpoint);
		}
	}

	@Override
	public void onOpen(@NonNull final WebSocket webSocket, @NonNull Response response) {
		send(new CommandConnect(mConnectionStringInfo.kbUUID, mConnectionStringInfo.kbName));
	}

	@Override
	public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
		Services.Log.debug(TAG, "Message received: " + text);
		IServerCommand command = mGson.fromJson(text, IServerCommand.class);
		if (command instanceof CommandKbOk) {
			mStatus = Status.CONNECTED;
			mServerCommandsCompressionEnabled = ((CommandKbOk) command).isCompressionEnabled();
			mNetworkEventsListener.onConnectionEstablished(mConnectionAttempts.getCurrent());
		} else if (command instanceof CommandKBDoesNotMatchGUID) {
			attemptConnection();
		} else {
			mNetworkEventsListener.onCommandReceived(command);
		}
	}

	@Override
	public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
		Buffer gzippedBuffer = new Buffer();
		gzippedBuffer.write(bytes);

		Buffer gunzippedBuffer;

		try {
			gunzippedBuffer = OkioUtils.gunzip(gzippedBuffer);
		} catch (IOException e) {
			Services.Log.debug(getClass().getName(), "Error while uncompressing message content.");
			return;
		}

		onMessage(webSocket, gunzippedBuffer.readUtf8());
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
		Services.Log.debug(TAG, String.format("WebSocket closed. Code: %d - Reason: %s", code, reason));
		mWebSocket = null;
		mStatus = Status.DISCONNECTED;
		mNetworkEventsListener.onConnectionDropped();
	}

	@Override
	public void onFailure(@NonNull WebSocket webSocket, Throwable t, @Nullable Response response) {
		Services.Log.debug(TAG, String.format("WebSocket failure detected: %s", t.toString()));
		switch (mStatus) {
			case CONNECTED:
				mStatus = Status.DISCONNECTED;
				mNetworkEventsListener.onConnectionDropped();
				break;

			case CONNECTING:
				if (t instanceof UnknownHostException) {
					// Since in this case the callback is called inmediatly, OkHttp's job queue
					// is overflown with connection requests too quickly and is not able to
					// process them in order unless we delay the frecuency in which we enqueue them.
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) { }
				}
				attemptConnection();
				break;

			case DISCONNECTED:
				throw new IllegalStateException("Unexpected network failure happened while " +
						"being in the disconnected state.");
		}
	}
}

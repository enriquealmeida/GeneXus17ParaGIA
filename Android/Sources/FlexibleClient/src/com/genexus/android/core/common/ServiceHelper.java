package com.genexus.android.core.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.util.Pair;

import com.genexus.android.R;
import com.genexus.android.DebugService;
import com.genexus.android.core.common.okhttp.OkHttpClientHelper;
import com.genexus.android.core.common.okhttp.ProgressInputStreamRequestBody;
import com.genexus.android.core.common.okhttp.ServiceDataResult;
import com.genexus.android.json.NodeObject;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.metadata.loader.RemoteApplicationInfo;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.IHttpService;
import com.genexus.android.core.base.services.ServiceResponse;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.okhttp.NetworkLogger;
import com.genexus.android.core.common.okhttp.ServiceErrorParser;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.net.NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING;

class ServiceHelper implements IHttpService {

	private final Context mAppContext;
	private final GenexusApplication mGenexusApplication;

	private OkHttpClientHelper mOkHttpClient;

	public ServiceHelper(Context appContext, GenexusApplication genexusApplication) {
		mAppContext = appContext;
		mGenexusApplication = genexusApplication;
		mOkHttpClient = new OkHttpClientHelper();
	}

	@Override
	public JSONArray getJSONArrayFromUrl(String url) {
		IServiceDataResult result = getData(url, null, false);
		if (result.isOk() && result.getData().length() != 0)
			return result.getData();

		return null;
	}

	@Override
	public JSONObject getJSONFromUrl(String url) {
		IServiceDataResult result = getData(url, null, false);
		if (result.isOk() && result.getData().length() != 0) {
			try {
				return result.getData().getJSONObject(0);
			} catch (JSONException e) {
				// Should never happen, or isOk() would have returned false.
				return null;
			}
		} else
			return null;
	}

	@Override
	public JSONArray getMiniApps(String url, String superAppId, int versionId) {
		Request.Builder requestBuilder = OkHttpClientHelper.getRequestBuilder(url);
		HttpHeaders.addSuperAppHeaders(requestBuilder, superAppId, versionId);
		IServiceDataResult result = getData(requestBuilder, null, false, false);
		if (result.isOk() && result.getData().length() != 0)
			return result.getData();

		return null;
	}

	@Override
	public IServiceDataResult getEntityDataBC(String type, List<String> keys) {
		String url = mGenexusApplication.UriMaker.getOneBCUrl(type, keys);
		return getData(url, null, false);
	}

	@Override
	public IServiceDataResult getDataFromProvider(String uri, Date ifModifiedSince, boolean isCollection) {
		if (!Services.HttpService.isOnline()) {
			String message = Services.Strings.getResource(R.string.GXM_NoInternetConnection);
			return ServiceDataResult.error(DataRequest.ERROR_NETWORK, message);
		}

		return getData(uri, ifModifiedSince, isCollection);
	}

	@Override
	public void downloadAndExtractMetadata(Context context) {
		String appMetadata = mGenexusApplication.UriMaker.getApplicationMetadataUrl(mGenexusApplication.getAppEntry());
		Services.Log.debug(String.format("Downloading '%s'.", appMetadata));
		InputStream stream = getInputStreamFromUrl(appMetadata);

		// If the app package is not there, also try the "old" package containing everything.
		if (stream == null) {
			appMetadata = mGenexusApplication.UriMaker.getApplicationMetadataUrl("app");
			Services.Log.debug(String.format("Downloading '%s'.", appMetadata));
			stream = getInputStreamFromUrl(appMetadata);
		}

		if (stream == null) {
			return;
		}

		try {
			ZipHelper zipper = new ZipHelper(stream);
			zipper.unzip(context, mGenexusApplication);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	private InputStream getInputStreamFromUrl(String url) {
		try {
			URL fileUrl = new URL(url);
			URLConnection urlConnection = fileUrl.openConnection();
			return urlConnection.getInputStream();
		} catch (IOException e) {
			Services.Log.error(e);
			return null;
		}
	}

	private IServiceDataResult getData(String uri, Date ifModifiedSince, boolean parseList) {
		return getData(uri, ifModifiedSince, parseList, false);
	}

	private IServiceDataResult getData(String uri, Date ifModifiedSince, boolean parseList, boolean isRetryAttempt) {
		return getData(OkHttpClientHelper.getRequestBuilder(uri), ifModifiedSince, parseList, isRetryAttempt);
	}

	private IServiceDataResult getData(Request.Builder requestBuilder, Date ifModifiedSince, boolean parseList,
	                                   boolean isRetryAttempt) {
		final OkHttpClient client = getDefaultClient();

		HttpHeaders.addSecurityHeaders(requestBuilder);
		HttpHeaders.addMobileHeaders(requestBuilder);

		if (ifModifiedSince != null)
			requestBuilder.addHeader(HttpHeaders.IF_MODIFIED_SINCE, StringUtil.dateToHttpFormat(ifModifiedSince));

		Request request = requestBuilder.build();
		NetworkLogger.logRequest(request);

		try (Response response = client.newCall(request).execute()){

			DebugService.onHttpRequest();

			NetworkLogger.logResponse(request, response);
			ServiceDataResult result = new ServiceDataResult(request, response, parseList);

			// Retry if it's a recoverable error (e.g. token expired but successfully renewed).
			if (!isRetryAttempt && shouldRetryRequest(result.getErrorType()))
				return getData(requestBuilder, ifModifiedSince, parseList, true);

			return result;
		} catch (IOException ex) {
			// response auto closable.
			NetworkLogger.logException(request, ex);
			return ServiceDataResult.networkError(ex);
		}
	}

	private static boolean shouldRetryRequest(int responseCode) {
		// In case we get an authentication error, see if it can be resolved without
		// asking for user name & password, and in that case signal to repeat the query.
		//noinspection SimplifiableIfStatement
		if (responseCode == DataRequest.ERROR_SECURITY_AUTHENTICATION)
			return SecurityHelper.tryAutomaticLogin();

		return false;
	}

	@Override
	public ServiceResponse insertEntityData(String type, List<String> key, INodeObject node) {
		JSONObject json = ((NodeObject) node).getInner();
		String url = mGenexusApplication.UriMaker.getOneBCUrl(type, key);
		try {
			return postJson(url, json);
		} catch (IOException e) {
			return new ServiceResponse(e);
		}
	}

	@Override
	public ServiceResponse insertOrUpdateEntityData(String type, List<String> key, INodeObject node) {
		JSONObject json = ((NodeObject) node).getInner();
		String url = mGenexusApplication.UriMaker.getOneBCUrl(type, key, "insertorupdate=true");
		try {
			return postJson(url, json);
		} catch (IOException e) {
			return new ServiceResponse(e);
		}
	}

	@Override
	public ServiceResponse updateEntityData(String type, List<String> key, INodeObject node) {
		JSONObject json = ((NodeObject) node).getInner();
		String url = mGenexusApplication.UriMaker.getOneBCUrl(type, key);
		try {
			return putJson(url, json);
		} catch (IOException e) {
			return new ServiceResponse(e);
		}
	}

	@Override
	public ServiceResponse deleteEntityData(String type, List<String> key) {
		String url = mGenexusApplication.UriMaker.getOneBCUrl(type, key);
		try {
			return delete(url);
		} catch (IOException e) {
			return new ServiceResponse(e);
		}
	}

	public OkHttpClient getDefaultClient() {
		return mOkHttpClient.getDefaultClient();
	}

	@Override
	public CookieManager getCookieManager() {
		return OkHttpClientHelper.Companion.getCookieManager();
	}

	@Override
	public ServiceResponse uploadInputStreamToServer(String url, InputStream data, long dataLength, String mimeType, IProgressListener listener) {
		final OkHttpClient client = getDefaultClient();

		MediaType okHttpMediaType = MediaType.parse(mimeType);
		ProgressInputStreamRequestBody okHttpStream = new ProgressInputStreamRequestBody(data, dataLength, okHttpMediaType, listener);
		Services.Log.debug("uploadInputStreamToServer, setContentType: " + mimeType);

		Request.Builder requestBuilder = OkHttpClientHelper.getRequestBuilder(url);

		HttpHeaders.addMobileHeaders(requestBuilder);
		HttpHeaders.addSecurityHeaders(requestBuilder);

		RequestBody body = okHttpStream;
		requestBuilder.post(body);
		Request request = requestBuilder.build();

		NetworkLogger.logRequest(request);
		try (Response response = client.newCall(request).execute())
		{
			DebugService.onHttpRequest();
			NetworkLogger.logResponse(request, response);

			return responseToServiceResponse(request, response, true);
		} catch (IOException e) {
			NetworkLogger.logException(request, e);
			return new ServiceResponse(e);
		} finally {
			IOUtils.closeQuietly(data);
		}
	}

	@Override
	public ServiceResponse postJsonSyncResponse(String url, JSONArray jsonArray, String syncVersion) throws IOException {

		Request.Builder requestBuilder = OkHttpClientHelper.getRequestBuilder(url);

		requestBuilder.addHeader(HttpHeaders.GENEXUS_SYNC_VERSION, syncVersion);
		requestBuilder.addHeader(HttpHeaders.GENEXUS_APP_IDENTIFIER, mAppContext.getPackageName());

		OkHttpClient customClient = getDefaultClient();
		// set timeout
		if (mGenexusApplication.getSynchronizerTimeoutReceive() > 0) {
			int timeoutSeconds = (int) mGenexusApplication.getSynchronizerTimeoutReceive(); // in seconds
			customClient = OkHttpClientHelper.getCustomClient(timeoutSeconds, true);
		}

		RequestBody body = OkHttpClientHelper.getRequestBody(jsonArray.toString());
		requestBuilder.post(body);

		ServiceResponse response = doServerRequest(customClient, requestBuilder, false, false);
		return response;
	}

	@Override
	public ServiceResponse postJsonSyncReplicator(String url, JSONObject jsonObject) throws IOException {
		Request.Builder requestBuilder = OkHttpClientHelper.getRequestBuilder(url);

		OkHttpClient customClient = getDefaultClient();
		// set timeout
		if (mGenexusApplication.getSynchronizerTimeoutSend() > 0) {
			int timeoutSeconds = (int)mGenexusApplication.getSynchronizerTimeoutSend(); // in seconds
			customClient = OkHttpClientHelper.getCustomClient(timeoutSeconds, true);
		}

		RequestBody body = OkHttpClientHelper.getRequestBody(jsonObject.toString());
		requestBuilder.post(body);

		ServiceResponse response = doServerRequest(customClient, requestBuilder, true, false);
		return response;

	}

	@Override
	public ServiceResponse postJson(String url, JSONArray jsonArray) throws IOException {
		Request.Builder requestBuilder = OkHttpClientHelper.getRequestBuilder(url);
		RequestBody body = OkHttpClientHelper.getRequestBody(jsonArray.toString());
		requestBuilder.post(body);
		return doServerRequest(requestBuilder, true);
	}

	@Override
	public ServiceResponse postJson(String url, JSONObject jsonObject) throws IOException {
		Request.Builder requestBuilder = OkHttpClientHelper.getRequestBuilder(url);
		RequestBody body = OkHttpClientHelper.getRequestBody(jsonObject.toString());
		requestBuilder.post(body);
		return doServerRequest(requestBuilder, true);
	}

	private ServiceResponse putJson(String url, JSONObject jsonObject) throws IOException {
		Request.Builder requestBuilder = OkHttpClientHelper.getRequestBuilder(url);
		RequestBody body = OkHttpClientHelper.getRequestBody(jsonObject.toString());
		requestBuilder.put(body);
		return doServerRequest(requestBuilder, true);
	}

	private ServiceResponse delete(String url) throws IOException {
		Request.Builder requestBuilder = OkHttpClientHelper.getRequestBuilder(url);
		requestBuilder.delete();
		ServiceResponse response = doServerRequest(requestBuilder,  false);
		// do not read response Stream anymore, close it
		if (response!=null && response.Stream!=null)
			response.Stream.close();
		return response;
	}

	private ServiceResponse doServerRequest(Request.Builder requestBuilder, boolean readJsonInResponse) throws IOException {
		final OkHttpClient client = getDefaultClient();
		return doServerRequest(client, requestBuilder, readJsonInResponse, false);
	}

	private ServiceResponse doServerRequest(OkHttpClient client, Request.Builder requestBuilder, boolean readJsonInResponse, boolean isRetryAttempt) throws IOException {

		HttpHeaders.addSecurityHeaders(requestBuilder);
		HttpHeaders.addMobileHeaders(requestBuilder);

		Request request = requestBuilder.build();

		DebugService.onHttpRequest();
		NetworkLogger.logRequest(request);

		Response response = null;
		try {
			response = client.newCall(request).execute();
			NetworkLogger.logResponse(request, response);

			ServiceResponse serviceResponse = responseToServiceResponse(request, response, readJsonInResponse);

			// Retry if it's a recoverable error (e.g. token expired but successfully renewed).
			if (!isRetryAttempt && shouldRetryRequest(serviceResponse.StatusCode))
				return doServerRequest(client, requestBuilder, readJsonInResponse, true);

			return serviceResponse;
		} catch (IOException e) {
			// On exception, log *and* rethrow.
			NetworkLogger.logException(request, e);
			throw e;
		}
		finally {
			// close response if already readit. Keep open if use Stream output, will close it later.
			if (response!=null && readJsonInResponse)
				response.close();
		}

	}

	private static ServiceResponse responseToServiceResponse(Request getBase, final Response response, boolean returnJson) {
		ServiceResponse serviceResponse = new ServiceResponse();

		serviceResponse.HttpCode = response.code();
		if (serviceResponse.HttpCode == HttpURLConnection.HTTP_UNAUTHORIZED
				|| serviceResponse.HttpCode == HttpURLConnection.HTTP_FORBIDDEN
				|| serviceResponse.HttpCode == HttpURLConnection.HTTP_ACCEPTED) {
			Pair<Integer, String> error = ServiceErrorParser.parse(getBase, response);
			serviceResponse.StatusCode = error.first;
			serviceResponse.ErrorMessage = error.second;
			return serviceResponse;
		}

		try {
			if (returnJson)
				serviceResponse.Message = response.body().string();
			else
				serviceResponse.Stream = response.body().byteStream();
		} catch (IOException ex) {
			Services.Log.error(ex);
		}

		try {
			if (serviceResponse.getResponseOk()) {
				if (returnJson && serviceResponse.HttpCode != HttpURLConnection.HTTP_NO_CONTENT) {
					if (Services.Strings.hasValue(serviceResponse.Message))
						serviceResponse.Data = new NodeObject(new JSONObject(serviceResponse.Message));

					String headers = response.header("Warning");
					if (headers != null && headers.length() > 0) {
						serviceResponse.WarningMessage = Strings.EMPTY;
						readWarningFromHeader(serviceResponse, headers);
					}
				}
			} else {
				// Response is NOT ok. Try to read specific error message. If not present, return generic one.
				String errorMessage = null;

				String responseContent = serviceResponse.Message;
				if (!returnJson) {
					try {
						responseContent = IOUtils.toString(serviceResponse.Stream);
					} catch (IOException e) {
						responseContent = "";
					}
				}

				try {
					JSONObject jsonResponse = new JSONObject(responseContent);
					serviceResponse.Data = new NodeObject(jsonResponse);

					JSONObject errorObj = jsonResponse.optJSONObject("error");
					errorMessage = (errorObj != null ? errorObj.getString("message") : null);
				} catch (JSONException ignored) {
				} // An exception here means the response was not JSON or its format was unexpected.

				int httpStatusCode = response.code();
				if (errorMessage == null || httpStatusCode >= HttpURLConnection.HTTP_INTERNAL_ERROR) {
					// In case of error 500, ignore the returned message and show a generic one.
					if (errorMessage != null)
						Services.Log.error(errorMessage);

					String errorDetail = String.valueOf(httpStatusCode) + " - " + response.message();
					errorMessage = Services.Strings.getResource(R.string.GXM_ApplicationServerError, errorDetail);
				}

				serviceResponse.ErrorMessage = errorMessage;
			}

			return serviceResponse;
		} catch (JSONException e) {
			Services.Log.error(e);
			return new ServiceResponse(e);
		}
	}

	private static void readWarningFromHeader(ServiceResponse serviceResponse, String header) {
		int start = header.indexOf("\"Encoded:User:");
		boolean encoded = true;
		if (start == -1) {
			start = header.indexOf("\"User:");
			encoded = false;
		}
		if (start > 0) {
			int end = header.indexOf("\"", start + 1);
			if (end > start) {
				String messageWarning = header.substring(start + 6, end);
				if (encoded) {
					try {
						//Decode the message from server
						messageWarning = header.substring(start + 14, end);
						messageWarning = URLDecoder.decode(messageWarning, "UTF-8");
					} catch (UnsupportedEncodingException e) {
					}
				}
				serviceResponse.WarningMessage += messageWarning + Strings.SPACE;
			}
		}
	}

	@Override
	public boolean isOnline() {
		return connectionType() > 0;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int connectionType() {
		// 0 None, 1 Wifi , 2 WAN
		if (DebugService.isNetworkOffline())
			return 0;

		ConnectivityManager cm = (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
				if (nc != null) {
					if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
						return 1;
					else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !nc.hasCapability(NET_CAPABILITY_NOT_ROAMING))
						return 3;
					else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
						return 2;
						// try any other valid internet connection as 2, include vpn , like else in < Build.VERSION_CODES.M
						// TODO: add a specific value for i.e. TRANSPORT_VPN , nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
					else if (nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
						return 2;
				}
			} else {
				android.net.NetworkInfo networkInfo = cm.getActiveNetworkInfo();
				if (networkInfo == null)
					return 0;

				if (networkInfo.isConnected()) {
					if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
						return 1;
					else if (networkInfo.isRoaming())
						return 3;
					else
						return 2;
				}
			}
		}
		return 0;
	}

	@Override
	public RemoteApplicationInfo getRemoteApplicationInfo() {
		String uri = mGenexusApplication.UriMaker.getApplicationVersionUrl(mGenexusApplication.getAppEntry());
		JSONObject obj = Services.HttpService.getJSONFromUrl(uri);

		if (obj == null) {
			Services.Log.error(String.format("Could not read remote metadata version from '%s'.", uri));
			return null;
		}

		int majorVersion = Integer.parseInt(obj.optString("major"));
		int minorVersion = Integer.parseInt(obj.optString("minor"));
		String appStoreUrl = obj.optString("uri");
		return new RemoteApplicationInfo(majorVersion, minorVersion, appStoreUrl);
	}

	@Override
	public long getRemoteMetadataVersion(long current) {
		String uri = mGenexusApplication.UriMaker.getMetadataVersionUrl();
		JSONObject obj = Services.HttpService.getJSONFromUrl(uri);

		if (obj == null) {
			// gxversion.json may not be available
			// return a new number in that case, so it is downloaded always and we know the next time that there is one downloaded (!= 0)
			current++;
			return current == 0 ? 1 : current;
		}

		return Long.parseLong(obj.optString("version"));
	}

	@Override
	public String uriEncode(String key) {
		return Uri.encode(key);
	}

	@Override
	public String uriDecode(String key) {
		return Uri.decode(key);
	}

	//Security

	//Login
	@Override
	public ServiceResponse callAccessManager(String url, Map<String, String> parameters, String redirectUrlScheme) {
		// no custom timeout, not follow redirects.
		// set no follow redirect in OkHttp client
		final OkHttpClient client = OkHttpClientHelper.getCustomClient(0, false);

		Request.Builder requestBuilder = OkHttpClientHelper.getRequestBuilder(url);

		try {
			FormBody.Builder formBodyBuilder = new FormBody.Builder();

			// GAM Login parameters
			if (parameters != null)
				for (Map.Entry<String, String> parameter : parameters.entrySet())
					formBodyBuilder.add(parameter.getKey(), parameter.getValue());

			requestBuilder.post(formBodyBuilder.build());

			HttpHeaders.addMobileHeaders(requestBuilder);
			HttpHeaders.addSecurityHeaders(requestBuilder); // Needed for logout, at least.

			if (Strings.hasValue(redirectUrlScheme))
				requestBuilder.addHeader("redirect_urlscheme", redirectUrlScheme);

			Request request = requestBuilder.build();
			// use our logger info
			NetworkLogger.logRequest(request);

			try (Response response = client.newCall(request).execute())
			{
				NetworkLogger.logResponse(request, response);

				if (response.code() == HttpURLConnection.HTTP_SEE_OTHER) {
					String newUrl = ServiceDataResult.parseRedirectOnHeader(response);
					ServiceResponse serviceResponse = new ServiceResponse();

					serviceResponse.HttpCode = response.code();
					serviceResponse.Message = newUrl;

					// Read entity anyway. Why?
					Services.Log.debug("callAccessManager " + response.body().string());

					return serviceResponse;
				} else {
					return responseToServiceResponse(request, response, true);
				}
			} finally {
			}
		} catch (IOException ex) {
			return new ServiceResponse(ex);
		}
	}

	@Override
	public JSONObject getEntityDefaultsBC(String name) {
		String url = mGenexusApplication.UriMaker.getDefaultBCUrl(name);
		return getJSONFromUrl(url);
	}

	@Override
	public String getNetworkErrorMessage(IOException e) {
		// The message is usually the exception's message.
		// If it doesn't have one, use the class name, but substitute "known" ones by a more descriptive message.
		String detail = e.getMessage();
		if (detail == null) {
			// Special cases
			if (e instanceof SocketTimeoutException)
				detail = "connection timed out";
			else
				detail = e.getClass().getName();
		}

		return Services.Strings.getResource(R.string.GXM_NetworkError, detail);
	}

	@Override
	public boolean isReachable(String url) {
		if (url == null)
			throw new IllegalArgumentException("Url cannot be null");

		if (!isOnline())
			return false;

		try {
			URL netUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) netUrl.openConnection();
			connection.setConnectTimeout(4000);
			connection.setReadTimeout(4000);
			try {
				// We aren't interested in the response. So long as we're able to connect, it's fine.
				connection.connect();
				return true;
			} finally {
				connection.disconnect();
			}
		} catch (IOException e) {
			// Should be MalformedURLException, IOException.
			Services.Log.debug("Exception during ServiceHelper.isReachable: " + e.toString());
			return false;
		}
	}

	@Override
	public boolean checkApplicationUri(String applicationName, String appUrl) {
		try {
			String url = Uri.withAppendedPath(Uri.parse(appUrl), "gxmetadata/appid.json").toString();

			JSONObject jsonObject = Services.HttpService.getJSONFromUrl(url);

			if (jsonObject == null) {
				Services.Log.error(String.format("Error while downloading appid.json from server. from '%s'.", url));
				return false;
			}

			String kbName = jsonObject.getString("id");
			if (!kbName.equalsIgnoreCase(applicationName)) {
				Services.Log.error("AppId names don't match.");
				return false;
			}

		} catch (JSONException e) {
			Services.Log.error("Error while parsing the appid.json.");
			return false;
		}

		return true;
	}
}

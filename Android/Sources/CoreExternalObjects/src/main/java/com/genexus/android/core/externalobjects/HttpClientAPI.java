package com.genexus.android.core.externalobjects;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.internet.HttpClient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.genexus.android.core.utils.FileUtils2.SCHEME_HTTP;
import static com.genexus.android.core.utils.FileUtils2.SCHEME_HTTPS;

public class HttpClientAPI extends ExternalApi {
    public static final String OBJECT_NAME = "HttpClient";

    private static final String METHOD_NAME_EXECUTE = "Execute";
    private static final String METHOD_NAME_TO_STRING = "ToString";
    private static final String METHOD_NAME_TO_FILE = "ToFile";
    private static final String METHOD_NAME_GET_HEADER = "GetHeader";
    private static final String METHOD_NAME_ADD_AUTHENTICATION = "AddAuthentication";
    private static final String METHOD_NAME_ADD_PROXY_AUTHENTICATION = "AddProxyAuthentication";
    private static final String METHOD_NAME_ADD_HEADER = "AddHeader";
    private static final String METHOD_NAME_ADD_FILE = "AddFile";
    private static final String METHOD_NAME_ADD_STRING = "AddString";
    private static final String PROPERTY_HOST = "Host";
    private static final String PROPERTY_PORT = "Port";
	private static final String PROPERTY_INCLUDE_COOKIES = "IncludeCookies";
    private static final String PROPERTY_BASEURL = "BaseURL";
    private static final String PROPERTY_SECURE = "Secure";
    private static final String PROPERTY_TIMEOUT = "Timeout";
    private static final String PROPERTY_PROXY_SERVER_HOST = "ProxyServerHost";
    private static final String PROPERTY_PROXY_SERVER_PORT = "ProxyServerPort";
    private static final String PROPERTY_BASIC = "Basic";
    private static final String PROPERTY_DIGEST = "Digest";
    private static final String PROPERTY_ERROR_CODE = "ErrCode";
    private static final String PROPERTY_ERROR_DESCRIPTION = "ErrDescription";
    private static final String PROPERTY_REASON_LINE = "ReasonLine";
    private static final String PROPERTY_STATUS_CODE = "StatusCode";

    private static final int AUTHENTICATION_BASIC    = 0;
    private static final int AUTHENTICATION_DIGEST   = 1;
    private static final int AUTHENTICATION_NTLM     = 2;
    private static final int AUTHENTICATION_KERBEROS = 3;

    private static final int ERROR_OK = 0;
    private static final int ERROR_IO = 1;

    public HttpClientAPI(ApiAction action) {
        super(action);
        addPropertyHandler(PROPERTY_HOST, mGetHost, mSetHost);
        addPropertyHandler(PROPERTY_PORT, mGetPort, mSetPort);
        addPropertyHandler(PROPERTY_BASEURL, mGetBaseUrl, mSetBaseUrl);
        addPropertyHandler(PROPERTY_SECURE, mGetSecure, mSetSecure);
        addPropertyHandler(PROPERTY_TIMEOUT, mGetTimeout, mSetTimeout);
        addPropertyHandler(PROPERTY_PROXY_SERVER_HOST, mGetProxyServerHost, mSetProxyServerHost);
        addPropertyHandler(PROPERTY_PROXY_SERVER_PORT, mGetProxyServerPort, mSetProxyServerPort);
		addPropertyHandler(PROPERTY_INCLUDE_COOKIES, mGetIncludeCookies, mSetIncludeCookies);
		addReadonlyPropertyHandler(PROPERTY_BASIC, mGetBasic);
        addReadonlyPropertyHandler(PROPERTY_DIGEST, mGetDigest);
        addReadonlyPropertyHandler(PROPERTY_ERROR_CODE, mGetErrorCode);
        addReadonlyPropertyHandler(PROPERTY_ERROR_DESCRIPTION, mGetErrorDescription);
        addReadonlyPropertyHandler(PROPERTY_REASON_LINE, mGetReasonLine);
        addReadonlyPropertyHandler(PROPERTY_STATUS_CODE, mGetStatusCode);
        addMethodHandler(METHOD_NAME_EXECUTE, 2, mMethodExecute);
        addMethodHandler(METHOD_NAME_TO_STRING, 0, mMethodToString);
        addMethodHandler(METHOD_NAME_TO_FILE, 1, mMethodToFile);
        addMethodHandler(METHOD_NAME_GET_HEADER, 1, mMethodGetHeader);
        addMethodHandler(METHOD_NAME_ADD_AUTHENTICATION, 4, mMethodAddAuthentication);
        addMethodHandler(METHOD_NAME_ADD_PROXY_AUTHENTICATION, 4, mMethodAddProxyAuthentication);
        addMethodHandler(METHOD_NAME_ADD_HEADER, 2, mMethodAddHeader);
        addMethodHandler(METHOD_NAME_ADD_FILE, 1, mMethodAddFile);
        addMethodHandler(METHOD_NAME_ADD_STRING, 1, mMethodAddString);
    }

    @SuppressWarnings({"InconsistentCapitalization", "checkstyle:MemberName"})
    private static class AuthenticationData
    {
        public AuthenticationData(int method, String realm, String user, String password)
        {
            Method = method;
            Realm = realm;
            User = user;
            Password = password;
        }

        public int Method;
        public String Realm;
        public String User;
        public String Password;
    }

    private String mHost;
    private int mPort;
    private boolean mIncludeCookies = true;
    private boolean mUsingIncludeCookies = false;
    private String mBaseUrl;
    private int mSecure;
    private int mTimeout;
    private String mProxyHost;
    private int mProxyPort;
    private List<AuthenticationData> mAuthenticationList;
    private List<AuthenticationData> mProxyAuthenticationList;
    private HashMap<String, String> mHeaders;
    private String mContentString;
    private int mLastErrorCode;
    private String mLastErrorDescription;
    private String mStringResult;
	private InputStream mStreamResult;
    private Response mResponseResult;
    private HttpClient mHttpClientResult;

    private final IMethodInvoker mGetHost = parameters ->
        ExternalApiResult.success(mHost);

    private final IMethodInvoker mSetHost = parameters -> {
        mHost = (String) parameters.get(0);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetPort = parameters ->
        ExternalApiResult.success(mPort);

    private final IMethodInvoker mSetPort = parameters -> {
        mPort = Services.Strings.tryParseInt(parameters.get(0).toString(), 0);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

	private final IMethodInvoker mGetIncludeCookies = parameters ->
		ExternalApiResult.success(mIncludeCookies);

	private final IMethodInvoker mSetIncludeCookies = parameters -> {
		mIncludeCookies = Services.Strings.tryParseBoolean(parameters.get(0).toString(), true);
		mUsingIncludeCookies = true;
		return ExternalApiResult.SUCCESS_CONTINUE;
	};

    private final IMethodInvoker mGetBaseUrl = parameters ->
        ExternalApiResult.success(mBaseUrl);

    private final IMethodInvoker mSetBaseUrl = parameters -> {
        mBaseUrl = (String) parameters.get(0);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetSecure = parameters ->
        ExternalApiResult.success(mSecure);

    private final IMethodInvoker mSetSecure = parameters -> {
        mSecure = Services.Strings.tryParseInt(parameters.get(0).toString(), 0);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetTimeout = parameters ->
        ExternalApiResult.success(mTimeout);

    private final IMethodInvoker mSetTimeout = parameters -> {
        mTimeout = Services.Strings.tryParseInt(parameters.get(0).toString(), 0);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetProxyServerHost = parameters ->
        ExternalApiResult.success(mProxyHost);

    private final IMethodInvoker mSetProxyServerHost = parameters -> {
        mProxyHost = (String) parameters.get(0);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetProxyServerPort = parameters ->
        ExternalApiResult.success(mProxyPort);

    private final IMethodInvoker mSetProxyServerPort = parameters -> {
        mProxyPort = Services.Strings.tryParseInt(parameters.get(0).toString(), 0);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetBasic = parameters ->
        ExternalApiResult.success(AUTHENTICATION_BASIC);

    private final IMethodInvoker mGetDigest = parameters ->
        ExternalApiResult.success(AUTHENTICATION_DIGEST);

    private final IMethodInvoker mGetErrorCode = parameters ->
        ExternalApiResult.success(mLastErrorCode);

    private final IMethodInvoker mGetErrorDescription = parameters ->
        ExternalApiResult.success(mLastErrorDescription);

    private final IMethodInvoker mGetReasonLine = parameters -> {
        if (mResponseResult != null)
            return ExternalApiResult.success(mResponseResult.message());
        else if (mHttpClientResult != null)
            return ExternalApiResult.success(mHttpClientResult.getReasonLine());
		else if (mLastErrorCode == ERROR_IO)
			return ExternalApiResult.success(Strings.EMPTY);
        else
            return ExternalApiResult.FAILURE;
    };

    private final IMethodInvoker mGetStatusCode = parameters -> {
        if (mResponseResult != null)
            return ExternalApiResult.success(mResponseResult.code());
        else if (mHttpClientResult != null)
            return ExternalApiResult.success(mHttpClientResult.getStatusCode());
		else if (mLastErrorCode == ERROR_IO)
			return ExternalApiResult.success(0);
		else
            return ExternalApiResult.FAILURE;
    };

    private final IMethodInvoker mMethodExecute = parameters -> {
        String executeMethod = (String) parameters.get(0);
        String url = (String) parameters.get(1);
        resetResult();
        ExternalApiResult result;
        try {
            if (useStandardClassHttpClient())
                result = callStandardClassHttpClient(executeMethod, url);
            else
                result = callUsingOkHttpClient(executeMethod, url);
        }
        finally {
            resetInput();
        }
        return result;
    };

    private void resetInput() {
        mHost = null;
        mPort = 0;
        mBaseUrl = null;
        mSecure = 0;
        mTimeout = 0;
        mProxyHost = null;
        mProxyPort = 0;
        mAuthenticationList = null;
        mProxyAuthenticationList = null;
        mHeaders = null;
        mContentString = null;
    }

    private void resetResult() {
        mStringResult = null;
        mStreamResult = null;
        mResponseResult = null;
        mHttpClientResult = null;
    }

    private boolean useStandardClassHttpClient() {
        if (mAuthenticationList != null) {
            for (AuthenticationData data : mAuthenticationList) {
                if (data.Method != AUTHENTICATION_BASIC)
                    return true;
            }
        }
        if (mProxyAuthenticationList != null) {
            for (AuthenticationData data : mProxyAuthenticationList) {
                if (data.Method != AUTHENTICATION_BASIC)
                    return true;
            }
        }
        // to support include cookies property use standard http client.
        if (mUsingIncludeCookies)
        	return true;
        return false;
    }

    private ExternalApiResult callStandardClassHttpClient(String executeMethod, String url) {
        HttpClient client = new HttpClient();
        if (mHost != null)
            client.setHost(mHost);
        if (mPort != 0)
            client.setHost(Integer.toString(mPort));
        client.setIncludeCookies(mIncludeCookies);
        if (mBaseUrl != null)
            client.setBaseURL(mBaseUrl);
        if (mSecure != 0)
            client.setSecure(mSecure);
        if (mTimeout != 0)
            client.setTimeout(mTimeout);
        if (mProxyHost != null)
            client.setProxyServerHost(mProxyHost);
        if (mProxyPort != 0)
            client.setProxyServerPort(mProxyPort);
        if (mAuthenticationList != null) {
            for (AuthenticationData data : mAuthenticationList) {
                client.addAuthentication(data.Method, data.Realm, data.User, data.Password);
            }
        }
        if (mProxyAuthenticationList != null) {
            for (AuthenticationData data : mProxyAuthenticationList) {
                client.addProxyAuthentication(data.Method, data.Realm, data.User, data.Password);
            }
        }
        if (mHeaders != null) {
            for (String name : mHeaders.keySet()) {
                client.addHeader(name, mHeaders.get(name));
            }
        }
        if (mContentString != null)
            client.addString(mContentString);
        client.execute(executeMethod, url);
        mLastErrorCode = client.getErrCode();
        mLastErrorDescription = client.getErrDescription();
        mHttpClientResult = client;
        return ExternalApiResult.SUCCESS_CONTINUE;
    }

    private String getUrl(String url) {
        String finalUrl;
        if (mHost != null && !url.contains("://")) {
            if (mSecure == 1 && mPort == 80)
                mPort = 443;
            finalUrl = (mSecure == 1 ? SCHEME_HTTPS : SCHEME_HTTP) + "://" + mHost;
            if (mPort != 0)
                finalUrl += ":" + Integer.toString(mPort);
            if (mBaseUrl != null) {
                if (mBaseUrl.startsWith("/"))
                    finalUrl += mBaseUrl;
                else
                    finalUrl += "/" + mBaseUrl;
            }
            if (!finalUrl.endsWith("/"))
                finalUrl += "/";
            if (url.startsWith("/"))
                finalUrl += url.substring(1);
            else
                finalUrl += url;
        }
        else {
            finalUrl = url;
        }
        return finalUrl;
    }

    private ExternalApiResult callUsingOkHttpClient(String executeMethod, String url) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        Request.Builder requestBuilder = new Request.Builder();
        RequestBody body = null;
        if (mTimeout != 0)
            clientBuilder.connectTimeout(mTimeout, TimeUnit.SECONDS).readTimeout(mTimeout, TimeUnit.SECONDS);
        if (mHeaders != null) {
            for (String name : mHeaders.keySet()) {
                requestBuilder.addHeader(name, mHeaders.get(name));
            }
        }
        if (mContentString != null) {
            body = RequestBody.create(mContentString, null);
        }
        if (mProxyHost != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(mProxyHost, mProxyPort == 0 ? 80 : mProxyPort));
            clientBuilder.proxy(proxy);
        }
        if (mAuthenticationList != null) {
            for (AuthenticationData data : mAuthenticationList) {
                if (data.Method == AUTHENTICATION_BASIC) {
                    clientBuilder.authenticator((route, response) -> {
                        String credential = Credentials.basic(data.User, data.Password);
                        return response.request().newBuilder().header("Authorization", credential).build();
                    });
                    break; // just 1
                }
            }
        }
        if (mProxyAuthenticationList != null) {
            for (AuthenticationData data : mProxyAuthenticationList) {
                if (data.Method == AUTHENTICATION_BASIC) {
                    clientBuilder.proxyAuthenticator((route, response) -> {
                        String credential = Credentials.basic(data.User, data.Password);
                        return response.request().newBuilder().header("Authorization", credential).build();
                    });
                    break; // just 1
                }
            }
        }

        url = getUrl(url);
        Services.Log.info("HttpClient execute, url=" + url);
        requestBuilder.method(executeMethod, body).url(url);
        OkHttpClient client = clientBuilder.build();
        Request request = requestBuilder.build();
        try {
            Response response = client.newCall(request).execute();
            mLastErrorCode = ERROR_OK;
            mLastErrorDescription = null;
            mResponseResult = response;
            return ExternalApiResult.SUCCESS_CONTINUE;
        } catch (IOException e) {
            mLastErrorCode = ERROR_IO;
            mLastErrorDescription = e.getMessage();
            Services.Log.warning("HttpClient Execute failed", e);
            // execute should not fail, allow check for Error code and Error description later
			return ExternalApiResult.SUCCESS_CONTINUE;
        }
    }

    private String getStringResult() {
        if (mStringResult != null)
            return mStringResult;
        if (mResponseResult != null) {
            try {
                mStringResult = mResponseResult.body().string();
                mLastErrorCode = ERROR_OK;
                mLastErrorDescription = null;
                return mStringResult;
            } catch (IOException e) {
                mLastErrorCode = ERROR_IO;
                mLastErrorDescription = e.getMessage();
                Services.Log.error("HttpClient ToString failed", e);
                return null;
            }
        }
        else if (mHttpClientResult != null)
            return mHttpClientResult.getString();
		else
            return null;
    }

    private InputStream getStreamResult() {
        if (mStreamResult != null) {
            return mStreamResult;
        }
        if (mResponseResult != null) {
            if (mResponseResult.body() != null) {
                mStreamResult = mResponseResult.body().byteStream();
                mLastErrorCode = ERROR_OK;
                mLastErrorDescription = null;
                return mStreamResult;
            } else {
                mLastErrorCode = ERROR_IO;
                mLastErrorDescription = "Response body is null";
                Services.Log.error("HttpClient ToFile failed");
                return null;
            }
        } else if (mHttpClientResult != null) {
            try {
                return mHttpClientResult.getInputStream();
            } catch (IOException e) {
                mLastErrorCode = ERROR_IO;
                mLastErrorDescription = e.getMessage();
                Services.Log.error("HttpClient ToFile failed", e);
                return null;
            }
        } else {
            return null;
        }
    }

    private final IMethodInvoker mMethodToString = parameters -> {
        String stringResult = getStringResult();
        if (stringResult != null)
            return ExternalApiResult.success(stringResult);
		else if (mLastErrorCode == ERROR_IO)
			return ExternalApiResult.success(Strings.EMPTY);
		else
            return ExternalApiResult.FAILURE;
    };

    private final IMethodInvoker mMethodToFile = parameters -> {
        String filePath = (String) parameters.get(0);
        InputStream inputStream = getStreamResult();
        if (inputStream == null) {
            return ExternalApiResult.FAILURE;
        }
        else {
            try {
                FileOutputStream fos = new FileOutputStream(filePath);

				IOUtils.copy(inputStream, fos);

				fos.flush();
				fos.close();

		        mLastErrorCode = ERROR_OK;
                mLastErrorDescription = null;
                return ExternalApiResult.SUCCESS_CONTINUE;
            } catch (IOException e) {
                mLastErrorCode = ERROR_IO;
                mLastErrorDescription = e.getMessage();
                Services.Log.error("HttpClient ToFile failed", e);
                return ExternalApiResult.FAILURE;
            }
			finally {
				if (inputStream != null)
					try { inputStream.close(); } catch (IOException e) { ; }
			}
        }
    };

    private final IMethodInvoker mMethodGetHeader = parameters -> {
        String name = (String) parameters.get(0);
        if (mResponseResult != null)
            return ExternalApiResult.success(mResponseResult.header(name));
        else if (mHttpClientResult != null)
            return ExternalApiResult.success(mHttpClientResult.getHeader(name));
		else if (mLastErrorCode == ERROR_IO)
			return ExternalApiResult.success(Strings.EMPTY);
		else
            return ExternalApiResult.FAILURE;
    };

    private final IMethodInvoker mMethodAddAuthentication = parameters -> {
        int authMethod = Services.Strings.tryParseInt(parameters.get(0).toString(), -1);
        if (authMethod != -1) {
            String realm = (String) parameters.get(1);
            String user = (String) parameters.get(2);
            String password = (String) parameters.get(3);
            AuthenticationData data = new AuthenticationData(authMethod, realm, user, password);
            if (mAuthenticationList == null)
                mAuthenticationList = new ArrayList<>();
            mAuthenticationList.add(data);
            return ExternalApiResult.SUCCESS_CONTINUE;
        }
        else {
            return ExternalApiResult.FAILURE;
        }
    };

    private final IMethodInvoker mMethodAddProxyAuthentication = parameters -> {
        int authMethod = Services.Strings.tryParseInt(parameters.get(0).toString(), -1);
        if (authMethod != -1) {
            String realm = (String) parameters.get(1);
            String user = (String) parameters.get(2);
            String password = (String) parameters.get(3);
            AuthenticationData data = new AuthenticationData(authMethod, realm, user, password);
            if (mProxyAuthenticationList == null)
                mProxyAuthenticationList = new ArrayList<>();
            mProxyAuthenticationList.add(data);
            return ExternalApiResult.SUCCESS_CONTINUE;
        }
        else {
            return ExternalApiResult.FAILURE;
        }
    };

    private final IMethodInvoker mMethodAddHeader = parameters -> {
        String name = (String) parameters.get(0);
        String value = (String) parameters.get(1);
        if (mHeaders == null)
            mHeaders = new LinkedHashMap<>();
        mHeaders.put(name, value);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mMethodAddFile = parameters -> {
        String filePath = (String) parameters.get(0);
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader inputReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputReader);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            mLastErrorCode = ERROR_OK;
            mLastErrorDescription = null;
            return addToContentString(sb.toString());
        } catch (IOException e) {
            mLastErrorCode = ERROR_IO;
            mLastErrorDescription = e.getMessage();
            Services.Log.error("HttpClient AddFile failed", e);
            return ExternalApiResult.FAILURE;
        }
    };

    private ExternalApiResult addToContentString(String value)
    {
        if (mContentString == null)
            mContentString = value;
        else
            mContentString += value;
        return ExternalApiResult.SUCCESS_CONTINUE;
    }

    private final IMethodInvoker mMethodAddString = parameters -> {
        String value = (String) parameters.get(0);
        return addToContentString(value);
    };
}

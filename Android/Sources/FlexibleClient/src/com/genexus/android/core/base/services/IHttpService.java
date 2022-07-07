package com.genexus.android.core.base.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.genexus.android.core.base.metadata.loader.RemoteApplicationInfo;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.common.IProgressListener;
import com.genexus.android.core.common.IServiceDataResult;

public interface IHttpService {
	CookieManager getCookieManager();

	boolean isOnline();
	boolean isReachable(String url);
	int connectionType();

	RemoteApplicationInfo getRemoteApplicationInfo();
	long getRemoteMetadataVersion(long current);

	String uriEncode(String key);
	String uriDecode(String key);

	ServiceResponse insertEntityData(String type, List<String> key, INodeObject node);
	ServiceResponse insertOrUpdateEntityData(String type, List<String> key, INodeObject node);
	ServiceResponse updateEntityData(String type, List<String> key, INodeObject node);
	ServiceResponse deleteEntityData(String type, List<String> key);

	JSONObject getEntityDefaultsBC(String name);
	JSONArray getJSONArrayFromUrl(String url);
	JSONObject getJSONFromUrl(String url);
	JSONArray getMiniApps(String url, String superAppId, int versionId);

	IServiceDataResult getEntityDataBC(String type, List<String> keys);
	IServiceDataResult getDataFromProvider(String uri, Date ifModifiedSince, boolean isCollection);

	void downloadAndExtractMetadata(Context context);

	ServiceResponse postJsonSyncResponse(String url, JSONArray jsonArray, String syncVersion) throws IOException;
	ServiceResponse postJson(String url, JSONArray jsonArray) throws IOException;
	ServiceResponse postJson(String url, JSONObject jsonObject) throws IOException;
	ServiceResponse postJsonSyncReplicator(String url, JSONObject jsonObject) throws IOException;

	String getNetworkErrorMessage(IOException e);

	ServiceResponse uploadInputStreamToServer(String url, InputStream data, long dataLength,
											  String mimeType, IProgressListener listener);

	ServiceResponse callAccessManager(String url, Map<String, String> parameters, String redirectUrlScheme);

	boolean checkApplicationUri(String applicationName, String appUrl);
}

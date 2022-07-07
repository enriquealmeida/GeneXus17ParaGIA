package com.genexus.android.core.base.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genexus.android.core.base.utils.Strings;

public class UrlBuilder {
	public static final int NET_SERVER = 15;
	public static final int NETCORE_SERVER = 41;
	public static final int JAVA_SERVER = 12;
	private static final String EXTRA_PARAMETERS = "fmt=json";

	private final String mRootUrl; // Root URL of the website.
	private final String mBaseUrl; // Base URL for REST services.
	
	public UrlBuilder(String rootUrl) {
		mRootUrl = rootUrl;
		mBaseUrl = rootUrl + "/rest";
	}

	public String getBaseUrl() {
		return mBaseUrl;
	}

	public String getRootUrl() {
		return mRootUrl;
	}

	public String getBaseImagesUrl() {
		return mRootUrl;
	}

	public String getMetadataVersionUrl() {
		return mRootUrl + "/gxmetadata/gxversion.json";
	}

	public String getLoginUrl() {
		return mRootUrl + "/oauth/access_token";
	}

	public String getUserInformationUrl() {
		return mRootUrl + "/oauth/userinfo";
	}

	public String getLogoutUrl() {
		return mRootUrl + "/oauth/logout";
	}

	public String getServerImagesUrl(String objectName) {
		StringBuilder sb = new StringBuilder(mBaseUrl);
		sb.append('/');
		sb.append(objectName.replace('.', '/'));
		sb.append("/gxobject");

		return sb.toString();
	}

	public String getFontUrl(String fontFileName) {
		return mRootUrl + "/gxmetadata/" + Services.HttpService.uriEncode(fontFileName);
	}

	public String getDefaultBCUrl(String entity) {
		return mBaseUrl + '/' + entity.replace('.', '/') + "/_default";
	}

	public String getApplicationVersionUrl(String app) {
		return mRootUrl + "/gxmetadata/" + Strings.toLowerCase(app) + ".android.json";
	}

	public String getApplicationMetadataUrl(String app) {
		return mRootUrl + "/gxmetadata/" + Strings.toLowerCase(app) + ".android.gxsd";
	}

	public String getRootUrlHostName() {
		try {
			URI uri = new URI(mRootUrl);
			return uri.getHost();
		} catch (URISyntaxException e) {
			Services.Log.error("Error getting getRootUrlHostName " + e.getMessage());
		}
		return Strings.EMPTY;
	}

	public String getMultiCallUrl(String procedure) {
		return mRootUrl + "/gxmulticall?" + procedure;
	}

	public String getImageUrl(String imageResource) {
		if (imageResource.startsWith("http://") || imageResource.startsWith("https://"))
			return imageResource;
		StringBuilder sb = new StringBuilder(getBaseImagesUrl());
		sb.append('/');

		String lastSegment = imageResource.replace('\\', '/');

		//only encode if not already encoded, for compatibility
		try {
			// special case for java static images. imageResources contains base folder url.
			if (lastSegment.contains("/static/")) {
				String baseUrl = getBaseImagesUrl();
				int pos = baseUrl.lastIndexOf('/');
				if (pos > 1) {
					String baseFolder = baseUrl.substring(pos);
					if (lastSegment.contains(baseFolder)) {
						// remove base folder.
						lastSegment = lastSegment.replace(baseFolder, "");
					}
				}
			}

			new URI(sb.toString() + lastSegment);
			//Services.Log.debug("URI new  " + lastSegment);
		} catch (URISyntaxException ex) {
			//Services.Log.debug("URI new failed " + lastSegment);
			// try with encode url
			int pos = lastSegment.lastIndexOf('/') + 1;
			if (pos > 1)
				lastSegment = lastSegment.substring(0, pos) + Services.HttpService.uriEncode(lastSegment.substring(pos));
			else
				lastSegment = Services.HttpService.uriEncode(lastSegment);
		}

		sb.append(lastSegment);
		return sb.toString();
	}

	public String getObjectUrl(String name, Map<String, ?> parameters) {
		StringBuilder sb = new StringBuilder();
		sb.append(mBaseUrl);
		sb.append("/");
		sb.append(name);

		if (parameters != null && parameters.size() != 0) {
			sb.append(Strings.QUESTION);
			boolean isNotFirst = false;
			for (Map.Entry<String, ?> parameter : parameters.entrySet()) {
				if (isNotFirst)
					sb.append(Strings.AND);

				String key = parameter.getKey();
				if (key.startsWith("&"))
					key = key.substring(1);

				sb.append(key);
				sb.append(Strings.EQUAL);
				sb.append(Services.HttpService.uriEncode(parameter.getValue().toString()));
				isNotFirst = true;
			}
		}

		return sb.toString();
	}

	public String getAllBCUrl(String entity) {
		return mBaseUrl + '/' + entity.replace('.', '/') + '?' + EXTRA_PARAMETERS;
	}

	public String getOneBCUrl(String entity, List<String> keys) {
		return getOneBCUrl(entity, keys, null);
	}

	public String getOneBCUrl(String entity, List<String> keys, String parameters) {
		StringBuilder sb = new StringBuilder(mBaseUrl);
		sb.append('/');
		sb.append(entity.replace('.', '/'));
		sb.append('/');

		List<String> encodedKeys = new ArrayList<>();
		for (String key : keys) {
			encodedKeys.add(Services.HttpService.uriEncode(key));
		}
		sb.append(Services.Strings.join(encodedKeys, Strings.COMMA));

		sb.append('?');
		sb.append(EXTRA_PARAMETERS);
		if (parameters != null) {
			sb.append('&');
			sb.append(parameters);
		}

		return sb.toString();
	}

	public String getLinkUrl(String object, int serverType, boolean addExtension) {
		StringBuilder sb = new StringBuilder(mRootUrl);
		if (serverType == NET_SERVER || serverType == NETCORE_SERVER) {
			sb.append("/");
			sb.append(object);
			if (addExtension)
				sb.append(".aspx");
		} else if (serverType == JAVA_SERVER) {
			sb.append("/");
			// object name could already have parameters in partial urls. preserve it case.
			int index = object.indexOf(Strings.QUESTION);
			if (index != -1) {
				sb.append(Strings.toLowerCase(object.substring(0, index)));
				sb.append(object.substring(index));
			} else {
				sb.append(Strings.toLowerCase(object));
			}
		}
		return sb.toString();
	}
}

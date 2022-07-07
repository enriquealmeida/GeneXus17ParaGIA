package com.genexus.android.core.externalobjects;

import java.util.List;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.device.ClientInformation;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

/**
 * This class allow access to device information from API.
 */
public class ClientInformationAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.Client.ClientInformation";

	public ClientInformationAPI(ApiAction action) {
		super(action);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameterValues) {
		Object result = null;
		if (method.equalsIgnoreCase("id")) {
			// Get device Id
			result = ClientInformation.id();
		} else if (method.equalsIgnoreCase("osname")) {
			// Get device os name
			result = ClientInformation.osName();
		} else if (method.equalsIgnoreCase("osversion")) {
			// Get device os version
			result = ClientInformation.osVersion();
		} else if (method.equalsIgnoreCase("PlatformName")) {
			result = ClientInformation.getPlatformName();
		} else if (method.equalsIgnoreCase("language")) {
			// Get device language.
			result = ClientInformation.getLanguage();
		} else if (method.equalsIgnoreCase("devicetype")) {
			// Get device os version
			result = ClientInformation.deviceType();
		} else if (method.equalsIgnoreCase("appVersionCode")) {
			// Get device app version code
			result = ClientInformation.getAppVersionCode();
		} else if (method.equalsIgnoreCase("appVersionName")) {
			// Get device app version name
			result = ClientInformation.getAppVersionName();
		} else if (method.equalsIgnoreCase("instalationId")) {
			// Get device instalation id
			result = ClientInformation.instalationId();
		} else if (method.equalsIgnoreCase("applicationId")) {
			// Get device instalation id
			result = ClientInformation.applicationId();
		} else
			return ExternalApiResult.failureUnknownMethod(this, method);

		return ExternalApiResult.success(result);
	}
}

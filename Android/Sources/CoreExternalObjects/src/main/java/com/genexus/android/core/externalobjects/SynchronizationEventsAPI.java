package com.genexus.android.core.externalobjects;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.externalapi.superapps.SuperAppExternalApi;

import java.util.List;

/**
 * This class allow access to synchronization events from API.
 */
public class SynchronizationEventsAPI extends SuperAppExternalApi {
	public static final String OBJECT_NAME = "GeneXus.SD.Synchronization.SynchronizationEvents";

	public SynchronizationEventsAPI(ApiAction action) {
		super(action);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters) {
		List<String> parameterValues = toString(parameters);

		if (method.equalsIgnoreCase("hasevents")) {
			Integer status = readInteger(parameterValues, 0);
			boolean result = SynchronizationEventsOffline.hasEvents(status);
			return ExternalApiResult.success(result);
		} else if (method.equalsIgnoreCase("getevents")) {
			Integer status = readInteger(parameterValues, 0);
			Object result = SynchronizationEventsOffline.getEventsLocal(status);
			return ExternalApiResult.success(result);
		} else if (method.equalsIgnoreCase("markeventaspending")) {
			String guid = parameterValues.get(0);
			java.util.UUID guidVal = java.util.UUID.fromString(guid);

			// Mark event as pending
			SynchronizationEventsOffline.markEventAsPending(guidVal, true);
			return ExternalApiResult.SUCCESS_CONTINUE;
		} else if (method.equalsIgnoreCase("removeevent")) {
			String guid = parameterValues.get(0);
			java.util.UUID guidVal = java.util.UUID.fromString(guid);

			// Remove event
			SynchronizationEventsOffline.removeEvent(guidVal, true);
			return ExternalApiResult.SUCCESS_CONTINUE;
		} else
			return ExternalApiResult.failureUnknownMethod(this, method);
	}

	private static Integer readInteger(List<String> values, int arrayIndex) {
		Integer timeout = 0;
		if (values.size() > arrayIndex) {
			try {
				timeout = Integer.valueOf(values.get(arrayIndex));
			} catch (NumberFormatException ex) { /* return 0 as default */}
		}
		return timeout;
	}
}

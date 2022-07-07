package com.genexus.android.core.externalobjects;

import java.util.List;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

/**
 * This class allow access to device information from API.
 */
public class LogAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.Common.Log";

	private static final String METHOD_WRITE = "write";
	private static final String METHOD_ERROR = "error";
	private static final String METHOD_WARNING = "warning";
	private static final String METHOD_INFO = "info";
	private static final String METHOD_DEBUG = "debug";
	private static final String METHOD_FATAL = "fatal";

	public LogAPI(ApiAction action) {
		super(action);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters) {
		List<String> parameterValues = toString(parameters);

		String message = Strings.EMPTY;
		if (parameterValues.size() >= 1)
			message = parameterValues.get(0);

		String topic = Strings.EMPTY;
		if (parameterValues.size() >= 2)
			topic = parameterValues.get(1);

		int logLevel = 0;
		if (parameterValues.size() >= 3) {
			logLevel = readInteger(parameterValues, 2, 0);
			//message = parameterValues.get(1);
			//topic = parameterValues.get(2);
		}

		if (method.equalsIgnoreCase(METHOD_WRITE)) {
			if (parameterValues.size() >= 3) {
				Services.Log.write(topic, message, logLevel);
			} else {
				Services.Log.write(topic, message);
			}
		} else if (method.equalsIgnoreCase(METHOD_ERROR) || method.equalsIgnoreCase(METHOD_FATAL)) {
			Services.Log.error(topic, message);
		} else if (method.equalsIgnoreCase(METHOD_WARNING)) {
			Services.Log.warning(topic, message);
		} else if (method.equalsIgnoreCase(METHOD_INFO)) {
			Services.Log.info(topic, message);
		} else if (method.equalsIgnoreCase(METHOD_DEBUG)) {
			Services.Log.debug(topic, message);
		} else
			return ExternalApiResult.failureUnknownMethod(this, method);

		return ExternalApiResult.SUCCESS_CONTINUE;
	}


	private static Integer readInteger(List<String> values, int arrayIndex, int defaultValue) {
		Integer intValue = defaultValue;
		if (values.size() > arrayIndex) {
			try {
				intValue = Integer.valueOf(values.get(arrayIndex));
			} catch (NumberFormatException ex) { /* return 0 as default */}
		}
		return intValue;
	}
}

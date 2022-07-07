package com.genexus.android.core.externalobjects;

import java.util.List;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

public class CalendarAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.SD.Calendar";

	public CalendarAPI(ApiAction action) {
		super(action);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters) {
		if (method.equalsIgnoreCase("schedule")) {
			if (!SDActionsHelper.addAppointmentFromParameters(getActivity(), toString(parameters))) {
				return ExternalApiResult.failure(R.string.GXM_NoApplicationAvailable);
			}
			return ExternalApiResult.SUCCESS_CONTINUE;
		}

		return ExternalApiResult.failureUnknownMethod(this, method);
	}
}

package com.genexus.android.core.externalobjects;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

public class RuntimeAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.Common.Runtime";

	public RuntimeAPI(ApiAction action) {
		super(action);

		addSimpleMethodHandler("Environment", 0, parameters -> RuntimeAPIOffline.getEnvironment());

		addPropertyHandler("ExitCode",
				parameters -> ExternalApiResult.success(RuntimeAPIOffline.getExitCode()),
				parameters -> {
					int exitCode = Services.Strings.tryParseInt(parameters.get(0).toString(), 0);
					RuntimeAPIOffline.setExitCode(exitCode);
					return ExternalApiResult.SUCCESS_CONTINUE;
				});
	}
}

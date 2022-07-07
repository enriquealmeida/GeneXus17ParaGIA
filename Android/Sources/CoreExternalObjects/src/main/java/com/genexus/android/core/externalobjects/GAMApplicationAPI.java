package com.genexus.android.core.externalobjects;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.gam.GAMApplication;
import com.genexus.android.core.externalapi.ExternalApi;

import java.util.List;

public class GAMApplicationAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXusSecurity.GAMApplication";

	public GAMApplicationAPI(ApiAction action) {
		super(action);

		addSimpleMethodHandler("GetClientId", 0, mMethodGetClientID);
	}

	private ISimpleMethodInvoker mMethodGetClientID = new ISimpleMethodInvoker() {
		@Override
		public Object invoke(List<Object> parameters) {
			return GAMApplication.getClientId();
		}
	};
}

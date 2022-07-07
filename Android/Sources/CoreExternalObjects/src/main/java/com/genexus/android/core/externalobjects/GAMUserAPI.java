package com.genexus.android.core.externalobjects;

import java.util.List;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.gam.GAMUser;
import com.genexus.android.core.externalapi.ExternalApi;

public class GAMUserAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXusSecurity.GAMUser";

	public GAMUserAPI(ApiAction action) {
		super(action);

		addSimpleMethodHandler("GetId", 0, new ISimpleMethodInvoker() {
			@Override
			public Object invoke(List<Object> parameters) {
				return GAMUser.getCurrentUserId();
			}
		});

		addSimpleMethodHandler("GetLogin", 0, new ISimpleMethodInvoker() {
			@Override
			public Object invoke(List<Object> parameters) {
				return GAMUser.getCurrentUserLogin();
			}
		});

		addSimpleMethodHandler("GetName", 0, new ISimpleMethodInvoker() {
			@Override
			public Object invoke(List<Object> parameters) {
				return GAMUser.getCurrentUserName();
			}
		});

		addSimpleMethodHandler("GetExternalId", 0, new ISimpleMethodInvoker() {
			@Override
			public Object invoke(List<Object> parameters) {
				return GAMUser.getCurrentUserExternalId();
			}
		});

		addSimpleMethodHandler("GetEmail", 0, new ISimpleMethodInvoker() {
			@Override
			public Object invoke(List<Object> parameters) {
				return GAMUser.getCurrentUserEMail();
			}
		});

		addSimpleMethodHandler("IsAnonymous", 0, new ISimpleMethodInvoker() {
			@Override
			public Object invoke(List<Object> parameters) {
				return GAMUser.getCurrentUserIsAnonymous();
			}
		});
	}
}

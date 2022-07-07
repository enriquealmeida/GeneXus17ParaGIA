package com.genexus.android.core.common;

import android.text.TextUtils;

import java.util.List;
import java.util.Map;

import com.genexus.android.core.actions.Action;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.ILoginProvider;
import com.genexus.android.core.base.services.ServiceResponse;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class ActionsHelper
{
	public static ServiceResponse runLoginAction(Action loginAction) {
		return callAccessManager(loginAction.getParameterValues(), 0, SecurityHelper.TYPE_STANDARD, null, null);
	}

	public static ServiceResponse runLoginExternalAction(Action loginAction, String redirectUrlScheme) {
		List<Value> values = loginAction.getParameterValues();
		return callAccessManager(values, 1, getType(values), redirectUrlScheme, null);
	}

	public static boolean runLoginExternalActionWithProvider(Action loginAction) {
		List<Value> values = loginAction.getParameterValues();

		ILoginProvider loginProvider = Services.Extensions.getExternalLoginManager().getProvider(getType(values));
		if (loginProvider != null) {
			loginProvider.login(loginAction.getContext(), loginAction.getMyActivity(), loginAction);
			return true;
		}

		return false;
	}

	public static ServiceResponse afterActivityResultLoginExternalActionWithProvider(Action loginAction, String redirectUrlScheme) {
		List<Value> values = loginAction.getParameterValues();
		String type = getType(values);

		ILoginProvider loginProvider = Services.Extensions.getExternalLoginManager().getProvider(type);
		if (loginProvider != null) {
			String token = loginProvider.loginToken();
			if (!TextUtils.isEmpty(token))
				return callAccessManager(values, 1, type, redirectUrlScheme, token);
		}

		return null;
	}

	private static String getType(List<Value> values) {
		if (values.size() > 0)
			return Strings.toLowerCase(values.get(0) != null ? values.get(0).coerceToString() : Strings.EMPTY);
		else
			return Strings.EMPTY;
	}

	private static ServiceResponse callAccessManager(List<Value> values, int userNameIndex, String type, String redirectUrlScheme, String token) {
		String userName = Strings.EMPTY;
		String userPassword = Strings.EMPTY;
		if (values.size() > userNameIndex + 1) {
			userName = values.get(userNameIndex).coerceToString();
			userPassword = values.get(userNameIndex + 1).coerceToString();
		}

		Entity additionalParameters = null;
		if (values.size() > userNameIndex + 2 && values.get(userNameIndex + 2).getType() == Expression.Type.SDT)
			additionalParameters = values.get(userNameIndex + 2).coerceToEntity();

		Map<String, String> parameters = SecurityHelper.getOauthParameters(type, userName, userPassword, null, additionalParameters);
		if (token != null)
			parameters.put("native_token", token);

		return Services.HttpService.callAccessManager(Services.Application.get().UriMaker.getLoginUrl(), parameters, redirectUrlScheme);
	}
}

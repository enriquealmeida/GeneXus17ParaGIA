package com.genexus.android.payments.mercadopago;

import android.app.Activity;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.model.Token;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.services.Callback;

class CreateTokenCallback extends Callback<Token> {

	private final Activity mActivity;
	private final ApiAction mAction;

	CreateTokenCallback(ApiAction action) {
		mActivity = action.getMyActivity();
		mAction = action;
	}

	@Override
	public void success(Token token) {
		Services.Log.debug("Successfully retrieved card token: " + JsonUtil.toJson(token));
		Entity entity = MercadoPagoEntitiesFactory.buildSuccessfulCardResultSDT(token.getId(), token.getLastFourDigits());
		mAction.setOutputValue(Expression.Value.newValue(entity));
		ActionExecution.continueCurrent(mActivity, true, mAction);
	}

	@Override
	public void failure(ApiException apiException) {
		Services.Log.error("Failed to create card token: " + apiException.toString());
		Entity entity = MercadoPagoEntitiesFactory.buildFailedCardResultSDT(MercadoPagoManager.ERROR_CODE_PROCESS_ERROR);
		mAction.setOutputValue(Expression.Value.newValue(entity));
		ActionExecution.continueCurrent(mActivity, true, mAction);
	}
}

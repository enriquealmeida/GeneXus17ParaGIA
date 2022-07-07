package com.genexus.android.payments.mercadopago;

import android.Manifest;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.InstanceProperties;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import java.util.List;

public class MercadoPagoAPI extends ExternalApi {

	static final String OBJECT_NAME = "MercadoPago.MercadoPagoCheckout";
	private static final String METHOD_NEW_CARD = "GetCardToken";
	private static final String METHOD_SAVED_CARD = "GetSavedCardToken";

	private static final String PROPERTY_PRIVATE_KEY = "MPPrivateKey";
	private static final String PROPERTY_PUBLIC_KEY = "MPPublicKey";

	private final MercadoPagoManager mMercadoPagoManager;

	public MercadoPagoAPI(ApiAction action) {
		super(action);
		addMethodHandlerRequestingPermissions(METHOD_NEW_CARD, 8, new String[]
			{Manifest.permission.ACCESS_COARSE_LOCATION}, mGetCardTokenMethod);
		addMethodHandlerRequestingPermissions(METHOD_SAVED_CARD, 2, new String[]
			{Manifest.permission.ACCESS_COARSE_LOCATION}, mGetSavedCardToken);

		String publicKey = getKey(R.string.MercadoPagoAPIPublicKey, PROPERTY_PUBLIC_KEY);
		String privateKey = getKey(R.string.MercadoPagoAPIPrivateKey, PROPERTY_PRIVATE_KEY);
		mMercadoPagoManager = new MercadoPagoManager(getActivity(), publicKey, privateKey);
	}

	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mGetCardTokenMethod = new IMethodInvoker() {

		@Override
		public @NonNull
		ExternalApiResult invoke(List<Object> parameters) {
			String paymentMethod = (String) parameters.get(0);
			String cardNumber = (String) parameters.get(1);
			String securityCode = (String) parameters.get(2);
			String cardHolderName = (String) parameters.get(3);
			String identificationNumber = (String) parameters.get(4);
			String identificationType = (String) parameters.get(5);
			int expiryMonth = Integer.parseInt((String) parameters.get(6));
			int expiryYear = Integer.parseInt((String) parameters.get(7));
			setCurrentAction();

			// Returns an entity in case of failure. Otherwise, an async call is launched and we'll wait
			Entity result = mMercadoPagoManager.getFirstCardToken(paymentMethod, identificationType, cardNumber,
				securityCode, cardHolderName, identificationNumber, expiryMonth, expiryYear);

			return result == null ? ExternalApiResult.SUCCESS_WAIT : ExternalApiResult.success(result);
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mGetSavedCardToken = new IMethodInvoker() {

		@Override
		public @NonNull
		ExternalApiResult invoke(List<Object> parameters) {
			String cardId = (String) parameters.get(0);
			String securityCode = (String) parameters.get(1);
			setCurrentAction();

			// Returns an entity in case of failure. Otherwise, an async call is launched and we'll wait
			Entity result = mMercadoPagoManager.getSavedCardToken(cardId, securityCode);

			return result == null ? ExternalApiResult.SUCCESS_WAIT : ExternalApiResult.success(result);
		}
	};

	private void setCurrentAction() {
		mMercadoPagoManager.setCurrentAction(getAction());
	}

	private String getKey(int resourceId, String mainPropertyFallback) {
		String key = getContext().getResources().getString(resourceId);
		if (key.trim().isEmpty()) {
			InstanceProperties mainProperties = Services.Application.get().getMainProperties();
			if (mainProperties != null)
				key = mainProperties.optStringProperty(mainPropertyFallback);
		}

		return key;
	}
}

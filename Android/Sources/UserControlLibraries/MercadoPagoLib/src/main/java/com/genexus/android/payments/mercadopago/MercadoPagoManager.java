package com.genexus.android.payments.mercadopago;

import android.content.Context;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.synchronization.SynchronizedResult;
import com.mercadopago.android.px.model.CardToken;
import com.mercadopago.android.px.model.PaymentMethod;
import com.mercadopago.android.px.model.SavedCardToken;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.CardTokenException;
import com.mercadopago.android.px.services.Callback;
import com.mercadopago.android.px.services.MercadoPagoServices;

import java.util.List;

class MercadoPagoManager {

	private static final String EMPTY_PAYMENT_METHOD_ID = "0";
	private static final int ERROR_CODE_CARD_NUMBER = 1;
	private static final int ERROR_CODE_SECURITY_CODE = 2;
	private static final int ERROR_CODE_EXPIRATION_DATE = 3;
	private static final int ERROR_CODE_CARDHOLDER_NAME = 4;
	private static final int ERROR_CODE_IDENTIFICATION_NUMBER = 5;
	private static final int ERROR_CODE_IDENTIFICATION_TYPE = 6;
	private static final int ERROR_CODE_PAYMENT_METHOD = 7;
	static final int ERROR_CODE_PROCESS_ERROR = 8;
	static final int ERROR_CODE_OK = 0;

	private final MercadoPagoServices mMercadoPagoServices;
	private ApiAction mCurrentApiAction = null;

	MercadoPagoManager(Context context, String publicKey, String privateKey) {
		mMercadoPagoServices = new MercadoPagoServices(context, publicKey, privateKey);
	}

	public void setCurrentAction(ApiAction apiAction) {
		mCurrentApiAction = apiAction;
	}

	Entity getFirstCardToken(String paymentMethodParameter, String identificationType,
							 String cardNumber, String securityCode, String cardHolderName,
							 String identificationNumber, int expiryMonth, int expiryYear) {

		PaymentMethod paymentMethod = getPaymentMethod(paymentMethodParameter);
		if (paymentMethod == null || paymentMethod.getId().equals(EMPTY_PAYMENT_METHOD_ID))
			return MercadoPagoEntitiesFactory.buildFailedCardResultSDT(ERROR_CODE_PAYMENT_METHOD);

		CardToken cardToken = MercadoPagoEntitiesFactory.buildCardToken(cardNumber, securityCode,
			cardHolderName, identificationNumber, identificationType, expiryMonth, expiryYear);

		int formErrorCode = validateForm(cardToken, identificationType, paymentMethod);
		if (formErrorCode != ERROR_CODE_OK)
			return MercadoPagoEntitiesFactory.buildFailedCardResultSDT(formErrorCode);

		if (mCurrentApiAction == null)
			throw new IllegalStateException("Current ApiAction cannot be null");

		mMercadoPagoServices.createToken(cardToken, new CreateTokenCallback(mCurrentApiAction));
		return null;
	}

	Entity getSavedCardToken(String cardId, String securityCode) {
		SavedCardToken savedCardToken = new SavedCardToken(cardId, securityCode);

		if (!savedCardToken.validateSecurityCode()) {
			Services.Log.error("Security code verification failed");
			return MercadoPagoEntitiesFactory.buildFailedCardResultSDT(ERROR_CODE_SECURITY_CODE);
		}

		if (mCurrentApiAction == null)
			throw new IllegalStateException("Current ApiAction cannot be null");

		mMercadoPagoServices.createToken(savedCardToken, new CreateTokenCallback(mCurrentApiAction));
		return null;
	}

	private PaymentMethod getPaymentMethod(String paymentMethod) {
		SynchronizedResult<PaymentMethod> syncedPaymentMethod = new SynchronizedResult<>();
		mMercadoPagoServices.getPaymentMethods(new Callback<List<PaymentMethod>>() {
			@Override
			public void success(List<PaymentMethod> paymentMethods) {
				boolean found = false;
				for (PaymentMethod p : paymentMethods) {
					if (p.getId().toLowerCase().equals(paymentMethod.toLowerCase())) {
						Services.Log.debug("Payment method found: " + p.getId());
						found = true;
						syncedPaymentMethod.setResult(p);
						break;
					}
				}
				if (!found) {
					Services.Log.warning(String.format("'%s' payment method not found", paymentMethod));
					syncedPaymentMethod.setResult(new PaymentMethod(EMPTY_PAYMENT_METHOD_ID));
				}
			}

			@Override
			public void failure(ApiException error) {
				Services.Log.error("Failed to retrieve payment methods: " + error.toString());
				syncedPaymentMethod.setResult(new PaymentMethod(EMPTY_PAYMENT_METHOD_ID));
			}
		});
		return syncedPaymentMethod.getResult();
	}

	private int validateForm(CardToken cardToken, String identificationType, PaymentMethod paymentMethod) {
		try {
			cardToken.validateCardNumber(paymentMethod);
		} catch (CardTokenException ex) {
			Services.Log.error("Card number verification failed", ex);
			return ERROR_CODE_CARD_NUMBER;
		}

		try {
			cardToken.validateSecurityCode(paymentMethod);
		} catch (CardTokenException ex) {
			Services.Log.error("Security code verification failed", ex);
			return ERROR_CODE_SECURITY_CODE;
		}

		if (!cardToken.validateExpiryDate()) {
			Services.Log.error("Expiration date verification failed");
			return ERROR_CODE_EXPIRATION_DATE;
		}

		if (!cardToken.validateCardholderName()) {
			Services.Log.error("Cardholder name verification failed");
			return ERROR_CODE_CARDHOLDER_NAME;
		}

		if (identificationType == null) {
			//cardToken.validateIdentificationNumber() was removed from here because it is not necessary for MX
			Services.Log.error("Customer identification verification failed");
			return ERROR_CODE_IDENTIFICATION_NUMBER;
		}
		return ERROR_CODE_OK;
	}
}

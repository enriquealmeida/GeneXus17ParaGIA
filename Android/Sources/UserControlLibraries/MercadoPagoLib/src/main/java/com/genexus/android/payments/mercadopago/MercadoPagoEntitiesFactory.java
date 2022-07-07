package com.genexus.android.payments.mercadopago;

import android.os.Parcel;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.mercadopago.android.px.model.CardToken;
import com.mercadopago.android.px.model.Cardholder;
import com.mercadopago.android.px.model.Identification;

class MercadoPagoEntitiesFactory {

	private static final String QUALIFIED_SDT_NAME = "MercadoPago.Payments.GetCardTokenSDT";
	private static final String SDT_PROPERTY_ERROR = "Error";
	private static final String SDT_PROPERTY_ID = "id";
	private static final String SDT_PROPERTY_LAST_DIGITS = "last_four_digits";

	static Entity buildSuccessfulCardResultSDT(String token, String lastFourDigits) {
		Entity cardResultSDT = EntityFactory.newSdt(QUALIFIED_SDT_NAME);
		cardResultSDT.setProperty(SDT_PROPERTY_ID, token);
		cardResultSDT.setProperty(SDT_PROPERTY_LAST_DIGITS, lastFourDigits);
		cardResultSDT.setProperty(SDT_PROPERTY_ERROR, MercadoPagoManager.ERROR_CODE_OK);
		return cardResultSDT;
	}

	static Entity buildFailedCardResultSDT(int errorCode) {
		Entity cardResultSDT = EntityFactory.newSdt(QUALIFIED_SDT_NAME);
		cardResultSDT.setProperty(SDT_PROPERTY_ERROR, errorCode);
		return cardResultSDT;
	}

	static CardToken buildCardToken(String cardNumber, String securityCode, String cardHolderName,
									String identificationNumber, String identificationType,
									int expiryMonth, int expiryYear) {
		CardToken cardToken = CardToken.createEmpty();
		cardToken.setCardNumber(cardNumber);
		cardToken.setExpirationMonth(expiryMonth);
		cardToken.setExpirationYear(expiryYear);
		cardToken.setSecurityCode(securityCode);
		cardToken.setCardholder(buildCardholder(cardHolderName, identificationNumber, identificationType));
		return cardToken;
	}

	private static Cardholder buildCardholder(String cardHolderName, String identificationNumber, String identificationType) {
		Parcel parcel = Parcel.obtain();
		parcel.writeParcelable(buildIdentification(identificationNumber, identificationType), 0);
		parcel.writeString(cardHolderName);
		parcel.setDataPosition(0);
		return Cardholder.CREATOR.createFromParcel(parcel);
	}

	private static Identification buildIdentification(String identificationNumber, String identificationType) {
		Parcel parcel = Parcel.obtain();
		parcel.writeString(identificationNumber);
		parcel.writeString(identificationType);
		parcel.setDataPosition(0);
		return Identification.CREATOR.createFromParcel(parcel);
	}
}

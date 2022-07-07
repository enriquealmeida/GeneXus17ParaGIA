package com.genexus.android.controls.cardscanner;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import java.util.List;

import io.card.payment.CardIOActivity;

public class CardScannerApi extends ExternalApi {
    public static final String OBJECT_NAME = "GeneXus.SD.CardScanner";

    // API Property Names
    private static final String PROPERTY_IS_AVAILABLE = "IsAvailable";
    private static final String PROPERTY_REQUIRE_CARDHOLDER_NAME = "CollectCardholderName";
    private static final String PROPERTY_REQUIRE_CVV = "CollectCVV";
    private static final String PROPERTY_REQUIRE_EXPIRY = "CollectExpiry";
    private static final String PROPERTY_REQUIRE_POSTAL_CODE = "CollectPostalCode";
    private static final String PROPERTY_DETECTION_MODE = "DetectionMode";
    private static final String PROPERTY_SUPPRESS_MANUAL_ENTRY = "DisableManualEntry";
    private static final String PROPERTY_RESTRICT_POSTAL_CODE_NUMERIC = "RestrictPostalCodeNumeric";
    private static final String PROPERTY_SCAN_EXPIRY = "ScanExpiry";
    private static final String PROPERTY_SCAN_INSTRUCTIONS = "ScanInstructionsText";
    private static final String PROPERTY_SUPPRESS_CONFIRMATION = "SuppressScanConfirmation";
    private static final String PROPERTY_UNBLUR_DIGITS = "UnblurDigits";
    // API Method Names
    private static final String METHOD_SCAN_CARD = "ScanCard";
    private static final String METHOD_PRELOAD = "Preload";

    public CardScannerApi(ApiAction action) {
        super(action);
        initializeHandlers();
    }

    private void initializeHandlers() {
        addReadonlyPropertyHandler(PROPERTY_IS_AVAILABLE, mGetIsAvailable);
        addPropertyHandler(PROPERTY_REQUIRE_CARDHOLDER_NAME, mGetRequireCardholderNameProperty, mSetRequireCardholderNameProperty);
        addPropertyHandler(PROPERTY_REQUIRE_CVV, mGetRequireCVVProperty, mSetRequireCVVProperty);
        addPropertyHandler(PROPERTY_REQUIRE_EXPIRY, mGetRequireExpiryProperty, mSetRequireExpiryProperty);
        addPropertyHandler(PROPERTY_REQUIRE_POSTAL_CODE, mGetRequirePostalCodeProperty, mSetRequirePostalCodeProperty);
        addPropertyHandler(PROPERTY_DETECTION_MODE, mGetDetectionModeProperty, mSetDetectionModeProperty);
        addPropertyHandler(PROPERTY_SUPPRESS_MANUAL_ENTRY, mGetSuppressManualEntryProperty, mSetSuppressManualEntryProperty);
        addPropertyHandler(PROPERTY_RESTRICT_POSTAL_CODE_NUMERIC, mGetRestrictPostalCodeNumericProperty, mSetRestrictPostalCodeNumericProperty);
        addPropertyHandler(PROPERTY_SCAN_EXPIRY, mGetScanExpiryProperty, mSetScanExpiryProperty);
        addPropertyHandler(PROPERTY_SCAN_INSTRUCTIONS, mGetScanInstructionsProperty, mSetScanInstructionsProperty);
        addPropertyHandler(PROPERTY_SUPPRESS_CONFIRMATION, mGetSuppressConfirmationProperty, mSetSuppressConfirmationProperty);
        addPropertyHandler(PROPERTY_UNBLUR_DIGITS, mGetUnblurDigitsProperty, mSetUnblurDigitsProperty);
        addMethodHandler(METHOD_SCAN_CARD, 0, mScanCardMethod);
        addMethodHandler(METHOD_PRELOAD, 0, mPreloadMethod);
    }

    private final IMethodInvoker mGetIsAvailable = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().isAvailable());
    };

    private final IMethodInvoker mGetRequireCardholderNameProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getRequireCardholderName());
    };

    private final IMethodInvoker mSetRequireCardholderNameProperty = parameters -> {
        String v = (String) parameters.get(0);
        boolean requireCardholderName = Services.Strings.tryParseBoolean(v, CardScannerImpl.getInstance().getRequireCardholderName());
        CardScannerImpl.getInstance().setRequireCardholderName(requireCardholderName);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetRequireCVVProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getRequireCVV());
    };

    private final IMethodInvoker mSetRequireCVVProperty = parameters -> {
        String v = (String) parameters.get(0);
        boolean requestCVV = Services.Strings.tryParseBoolean(v, CardScannerImpl.getInstance().getRequireCVV());
        CardScannerImpl.getInstance().setRequireCVV(requestCVV);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetRequireExpiryProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getRequireExpiry());
    };

    private final IMethodInvoker mSetRequireExpiryProperty = parameters -> {
        String v = (String) parameters.get(0);
        boolean requireExpiry = Services.Strings.tryParseBoolean(v, CardScannerImpl.getInstance().getRequireExpiry());
        CardScannerImpl.getInstance().setRequireExpiry(requireExpiry);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetRequirePostalCodeProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getRequirePostalCode());
    };

    private final IMethodInvoker mSetRequirePostalCodeProperty = parameters -> {
        String v = (String) parameters.get(0);
        boolean requirePostalCode = Services.Strings.tryParseBoolean(v, CardScannerImpl.getInstance().getRequirePostalCode());
        CardScannerImpl.getInstance().setRequirePostalCode(requirePostalCode);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetDetectionModeProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getDetectionMode());
    };

    private final IMethodInvoker mSetDetectionModeProperty = parameters -> {
        String v = (String) parameters.get(0);
        int detectionMode = Services.Strings.tryParseInt(v, CardScannerImpl.getInstance().getDetectionMode());
        CardScannerImpl.getInstance().setDetectionMode(detectionMode);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetSuppressManualEntryProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getSuppressManualEntry());
    };

    private final IMethodInvoker mSetSuppressManualEntryProperty = parameters -> {
        String v = (String) parameters.get(0);
        boolean suppressManualEntry = Services.Strings.tryParseBoolean(v, CardScannerImpl.getInstance().getSuppressManualEntry());
        CardScannerImpl.getInstance().setSuppressManualEntry(suppressManualEntry);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetRestrictPostalCodeNumericProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getRestrictPostalCodeNumeric());
    };

    private final IMethodInvoker mSetRestrictPostalCodeNumericProperty = parameters -> {
        String v = (String) parameters.get(0);
        boolean restrictPostalCodeNumeric = Services.Strings.tryParseBoolean(v, CardScannerImpl.getInstance().getRestrictPostalCodeNumeric());
        CardScannerImpl.getInstance().setRestrictPostalCodeNumeric(restrictPostalCodeNumeric);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetScanExpiryProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getScanExpiry());
    };

    private final IMethodInvoker mSetScanExpiryProperty = parameters -> {
        String v = (String) parameters.get(0);
        boolean scanExpiry = Services.Strings.tryParseBoolean(v, CardScannerImpl.getInstance().getScanExpiry());
        CardScannerImpl.getInstance().setScanExpiry(scanExpiry);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetScanInstructionsProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getScanInstructions());
    };

    private final IMethodInvoker mSetScanInstructionsProperty = parameters -> {
        CardScannerImpl.getInstance().setScanInstructions((String) parameters.get(0));
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetSuppressConfirmationProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getSuppressConfirmation());
    };

    private final IMethodInvoker mSetSuppressConfirmationProperty = parameters -> {
        String v = (String) parameters.get(0);
        boolean suppressConfirmation = Services.Strings.tryParseBoolean(v, CardScannerImpl.getInstance().getSuppressConfirmation());
        CardScannerImpl.getInstance().setSuppressConfirmation(suppressConfirmation);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mGetUnblurDigitsProperty = parameters -> {
        return ExternalApiResult.success(CardScannerImpl.getInstance().getUnblurDigits());
    };

    private final IMethodInvoker mSetUnblurDigitsProperty = parameters -> {
        String v = (String) parameters.get(0);
        int unblurDigits = Services.Strings.tryParseInt(v, CardScannerImpl.getInstance().getUnblurDigits());
        CardScannerImpl.getInstance().setUnblurDigits(unblurDigits);
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvokerWithActivityResult mScanCardMethod = new IMethodInvokerWithActivityResult() {
		@Override
        public @NonNull ExternalApiResult invoke(List<Object> parameters) {
            Intent scanIntent = CardScannerImpl.getInstance().getScanIntent(getActivity());
            startActivityForResult(scanIntent, RequestCodes.ACTION_ALWAYS_SUCCESSFUL); // it returns things like CardIOActivity.RESULT_CARD_INFO not Activity.RESULT_OK
            return ExternalApiResult.SUCCESS_WAIT;
        }


		@Override
        public @NonNull ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
            if (resultCode == CardIOActivity.RESULT_ENTRY_CANCELED)
                return ExternalApiResult.FAILURE;

            Entity sdt = CardScannerImpl.getInstance().getScanCardResult(getActivity(), result);
            return ExternalApiResult.success(sdt);
        }
    };

    private final IMethodInvoker mPreloadMethod = parameters -> ExternalApiResult.SUCCESS_CONTINUE;
}

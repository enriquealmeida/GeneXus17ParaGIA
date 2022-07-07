package com.genexus.android.controls.cardscanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.genexus.android.core.base.metadata.theme.ApplicationClassExtensionKt;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;

/**
 * Card Scanner implementation.
 * based on https://www.card.io/
 */
public class CardScannerImpl {
    private static CardScannerImpl sInstance;

    static synchronized CardScannerImpl getInstance() {
        if (sInstance == null)
            sInstance = new CardScannerImpl();
        return sInstance;
    }

    // API Property Values
    private static final int DETECTION_MODE_IMAGE_AND_NUMBER = 0;
    private static final int DETECTION_MODE_IMAGE_ONLY = 1;
    private static final int DETECTION_MODE_AUTOMATIC = 2;

    private CardScannerImpl() {
        mRequireCardholderName = false;
        mRequireCVV = false;
        mRequireExpiry = false;
        mRequirePostalCode = false;
        mDetectionMode = DETECTION_MODE_IMAGE_AND_NUMBER;
        mSuppressManualEntry = false;
        mRestrictPostalCodeNumeric = false;
        mScanExpiry = true;
        mScanInstructions = null; // Use default
        mSuppressConfirmation = true;
        mUnblurDigits = 4;
    }

    private boolean mRequireCardholderName;
    private boolean mRequireCVV;
    private boolean mRequireExpiry;
    private boolean mRequirePostalCode;
    private int mDetectionMode;
    private boolean mSuppressManualEntry;
    private boolean mRestrictPostalCodeNumeric;
    private boolean mScanExpiry;
    private String mScanInstructions;
    private boolean mSuppressConfirmation;
    private int mUnblurDigits;

    public boolean isAvailable() {
        return CardIOActivity.canReadCardWithCamera();
    }

    public boolean getRequireCardholderName() {
        return mRequireCardholderName;
    }

    public void setRequireCardholderName(boolean requireCardholderName) {
        mRequireCardholderName = requireCardholderName;
    }

    public boolean getRequireCVV() {
        return mRequireCVV;
    }

    public void setRequireCVV(boolean requireCVV) {
        mRequireCVV = requireCVV;
    }

    public boolean getRequireExpiry() {
        return mRequireExpiry;
    }

    public void setRequireExpiry(boolean requireExpiry) {
        mRequireExpiry = requireExpiry;
    }

    public boolean getRequirePostalCode() {
        return mRequirePostalCode;
    }

    public void setRequirePostalCode(boolean requirePostalCode) {
        mRequirePostalCode = requirePostalCode;
    }

    public int getDetectionMode() {
        return mDetectionMode;
    }

    public void setDetectionMode(int detectionMode) {
        mDetectionMode = detectionMode;
    }

    public boolean getSuppressManualEntry() {
        return mSuppressManualEntry;
    }

    public void setSuppressManualEntry(boolean suppressManualEntry) {
        mSuppressManualEntry = suppressManualEntry;
    }

    public boolean getRestrictPostalCodeNumeric() {
        return mRestrictPostalCodeNumeric;
    }

    public void setRestrictPostalCodeNumeric(boolean restrictPostalCodeNumeric) {
        mRestrictPostalCodeNumeric = restrictPostalCodeNumeric;
    }

    public boolean getScanExpiry() {
        return mScanExpiry;
    }

    public void setScanExpiry(boolean scanExpiry) {
        mScanExpiry = scanExpiry;
    }

    public String getScanInstructions() {
        return mScanInstructions;
    }

    public void setScanInstructions(String scanInstructions) {
        mScanInstructions = scanInstructions;
    }

    public boolean getSuppressConfirmation() {
        return mSuppressConfirmation;
    }

    public void setSuppressConfirmation(boolean suppressConfirmation) {
        mSuppressConfirmation = suppressConfirmation;
    }

    public int getUnblurDigits() {
        return mUnblurDigits;
    }

    public void setUnblurDigits(int unblurDigits) {
        mUnblurDigits = unblurDigits;
    }

    public Intent getScanIntent(Context context) {
        Intent scanIntent = new Intent(context, CardIOActivity.class)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, mRequireCardholderName) // Boolean extra. Optional. Defaults to false. If set, the user will be prompted for the cardholder name.
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, mRequireCVV) // Boolean extra. Optional. Defaults to false. If set, the user will be prompted for the card CVV.
                .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, mRequireExpiry) // Boolean extra. Optional. Defaults to false. If set to false, expiry information will not be required.
                .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, mRequirePostalCode) // Boolean extra. Optional. Defaults to false. If set, the user will be prompted for the card billing postal code.
                .putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, mSuppressManualEntry) // Boolean extra. Optional. Defaults to false. Removes the keyboard button from the scan screen.
                .putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, mRestrictPostalCodeNumeric) // Boolean extra. Optional. Defaults to false. If set, the postal code will only collect numeric input. Set this if you know the expected country's postal code has only numeric postal codes.
                .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, mScanExpiry) // Boolean extra. Optional. Defaults to true. If set to true, and EXTRA_REQUIRE_EXPIRY is true, an attempt to extract the expiry from the card image will be made.
                .putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, mSuppressConfirmation) // Boolean extra. Optional. If this value is set to true the user will not be prompted to confirm their card number after processing.
//              .putExtra(CardIOActivity.EXTRA_UNBLUR_DIGITS, mUnblurDigits) // Integer extra. Optional. Defaults to -1 (no blur). Privacy feature. How many of the Card number digits NOT to blur on the resulting image. Setting it to 4 will blur all digits except the last four.
                .putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true) // Boolean extra. Optional. If this value is set to true, and the application has a theme, the theme for the card.io Activitys will be set to the theme of the application.
                .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true) // Boolean extra. Optional. If this value is set to true the card image will be passed as an extra in the data intent that is returned to your Activity using the EXTRA_CAPTURED_CARD_IMAGE key.
                .putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // Boolean extra. Optional. Defaults to false. When set to true the card.io logo will not be shown overlaid on the camera.

        if (Services.Themes.getApplicationClass() != null) {
            Integer guideColor = ApplicationClassExtensionKt.getActionTintColorId(Services.Themes.getApplicationClass());
            if (guideColor != null)
                scanIntent.putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, guideColor); // Integer extra. Optional. Defaults to Color.GREEN. Changes the color of the guide overlay on the camera.
        }

        if (mDetectionMode == DETECTION_MODE_IMAGE_ONLY) {
            scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_SCAN, true); // Boolean extra. Optional. Once a card image has been captured but before it has been processed, this value will determine whether to continue processing as usual. If the value is true the CardIOActivity will finish with a RESULT_SCAN_SUPPRESSED result code.
        }

        if (Services.Strings.hasValue(mScanInstructions)) {
            scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_INSTRUCTIONS, mScanInstructions); // String extra. Optional. Used to display instructions to the user while they are scanning their card.
        }

        // NOT used Extras
        // EXTRA_NO_CAMERA - Boolean extra. Optional. Defaults to false. If set, the card will not be scanned with the camera.
        // EXTRA_LANGUAGE_OR_LOCALE - String extra. Optional. The preferred language for all strings appearing in the user interface. If not set, or if set to null, defaults to the device's current language setting.
        // EXTRA_SCAN_OVERLAY_LAYOUT_ID - Integer extra. Optional. If this value is provided the view will be inflated and will overlay the camera during the scan process. The integer value must be the id of a valid layout resource.
        // EXTRA_USE_CARDIO_LOGO - Boolean extra. Optional. Defaults to false. If set, the card.io logo will be shown instead of the PayPal logo.
        // EXTRA_USE_PAYPAL_ACTIONBAR_ICON - Boolean extra. Optional. Use the PayPal icon in the ActionBar.

        return scanIntent;
    }

    public Entity getScanCardResult(Context context, Intent result) {
        Entity sdt = EntityFactory.newSdt("GeneXus.SD.CardInformation");
        if (result != null && result.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = result.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            sdt.setProperty("CardNumber", scanResult.cardNumber);
            sdt.setProperty("RedactedCardNumber", scanResult.getRedactedCardNumber());

            if (scanResult.isExpiryValid()) {
                sdt.setProperty("ExpiryMonth", scanResult.expiryMonth);
                sdt.setProperty("ExpiryYear", scanResult.expiryYear % 100); // ExpiryYear is Number(2.0)
            }
            if (scanResult.cvv != null) {
                // Never log or display a CVV
                sdt.setProperty("CVV", scanResult.cvv);
            }
            if (scanResult.postalCode != null) {
                sdt.setProperty("PostalCode", scanResult.postalCode);
            }

            sdt.setProperty("CardHolderName", scanResult.cardholderName);
            sdt.setProperty("CardType", mapCardType(scanResult.getCardType()));
            Bitmap cardLogo = scanResult.getCardType().imageBitmap(context);
            if (cardLogo != null)
                sdt.setProperty("CardLogo", getFilePath(context, cardLogo));
        }

        Bitmap cardImage = CardIOActivity.getCapturedCardImage(result);
        if (cardImage != null)
            sdt.setProperty("CardImage", getFilePath(context, cardImage));

        return sdt;
    }

    private String getFilePath(Context context, Bitmap bmp) {
        try {
            File file = File.createTempFile("card", ".png", context.getCacheDir());
            file.deleteOnExit();
            OutputStream outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return "file://" + file.getAbsolutePath();
        }
        catch (IOException e) {
            return "";
        }
    }

    private String mapCardType(CardType type) {
        switch (type) {
            case AMEX:
                return "amex";
            case DINERSCLUB:
                return "diners";
            case DISCOVER:
                return "discover";
            case JCB:
                return "JCB";
            case MASTERCARD:
                return "mastercard";
            case VISA:
                return "visa";
            case MAESTRO:
                return "maestro";
            case UNKNOWN:
            default:
                return "unrecognized";
        }
    }
}

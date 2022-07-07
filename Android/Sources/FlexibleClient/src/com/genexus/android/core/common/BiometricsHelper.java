package com.genexus.android.core.common;

import android.content.Intent;

import com.genexus.android.R;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.activities.LoginBiometricsActivity;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;

public class BiometricsHelper {
    private static ExternalApi sAuthenticationInstance;
    private static List<Object> sAuthenticationParameters;
    private static boolean sCallLoginNextStartBiometricActivity;

    private static final String OBJECT_NAME_AUTHENTICATION = "GeneXus.SD.DeviceAuthentication";
    private static final String OBJECT_NAME_PROGRESS = "GeneXus.Common.UI.Progress";
    private static final String METHOD_HIDE = "Hide";
    private static final String METHOD_IS_AVAILABLE = "IsAvailable";
    private static final String METHOD_AUTHENTICATE = "Authenticate";
    private static final String METHOD_REGISTER_REUSE_OBSERVER = "RegisterReuseObserver";
    private static final String METHOD_ENCRYPT = "Encrypt";
    private static final String METHOD_DECRYPT = "Decrypt";
    private static final String VALUE_POLICY_BIOMETRICS = "1";
    private static final String PROPERTY_ENABLE_BIOMETRICS = "IntegratedSecurityEnableBiometrics";
    private static final String PROPERTY_BIOMETRICS_REUSE_DURATION = "IntegratedSecutiryBiometricsReuseDuration";
    private static final String PROPERTY_BLUR_ON_BACKGROUND = "IntegratedSecurityBlurOnBackground";
    private static final int DEFAULT_BIOMETRICS_REUSE_DURATION = 300;

    public static void registerLifecycleObserver(UIContext context) {
        if (BiometricsHelper.isBiometricsEnabled()) {
            api(context, OBJECT_NAME_AUTHENTICATION).execute(METHOD_REGISTER_REUSE_OBSERVER, Collections.emptyList());
        }
    }

    public static boolean isBiometricsEnabled() {
        return Services.Application.get().getMainProperties().optBooleanProperty(PROPERTY_ENABLE_BIOMETRICS);
    }

    public static int biometricsReuseDuration() {
        return Services.Application.get().getMainProperties().optIntProperty(PROPERTY_BIOMETRICS_REUSE_DURATION, DEFAULT_BIOMETRICS_REUSE_DURATION);
    }

    public static boolean isBlurOnBackgroundEnabled() {
        return Services.Application.get().getMainProperties().optBooleanProperty(PROPERTY_BLUR_ON_BACKGROUND);
    }

    public static void callLoginNextStartBiometricActivity() {
        sCallLoginNextStartBiometricActivity = true;
    }

    public static void startBiometricsActivity(UIContext context) {
        if (sCallLoginNextStartBiometricActivity) {
            sCallLoginNextStartBiometricActivity = false;
            ActivityLauncher.callLogin(context);
        } else {
            Intent intent = new Intent(context, LoginBiometricsActivity.class);
            context.getActivity().startActivityForResult(intent, RequestCodes.LOGIN);
        }
    }

    public static boolean isFingerprintAvailable(UIContext context) {
        return (boolean) api(context, OBJECT_NAME_AUTHENTICATION).execute(METHOD_IS_AVAILABLE,
			Arrays.asList(new Object[]{VALUE_POLICY_BIOMETRICS})).getReturnValue();
    }

    public static ExternalApiResult authenticate(UIContext context, int mode) {
        String title = context.getResources().getString(R.string.GXM_AuthenticateTitle, context.getResources().getString(R.string.GXM_FingerprintTitle));
        String description = context.getResources().getString(mode == Cipher.ENCRYPT_MODE ? R.string.GXM_AuthenticateDescriptionSave : R.string.GXM_AuthenticateDescriptionLogin);
        sAuthenticationInstance = api(context, OBJECT_NAME_AUTHENTICATION);
        sAuthenticationParameters = Arrays.asList(new Object[]{VALUE_POLICY_BIOMETRICS, title, description, mode});
        return sAuthenticationInstance.execute(METHOD_AUTHENTICATE, sAuthenticationParameters);
    }

    public static ExternalApiResult authenticateOnActivityResult(int requestCode, int resultCode, Intent data) {
        return sAuthenticationInstance.afterActivityResult(requestCode, resultCode, data, METHOD_AUTHENTICATE, sAuthenticationParameters);
    }

    public static String encrypt(String v) {
        return (String) sAuthenticationInstance.execute(METHOD_ENCRYPT, Arrays.asList(new Object[]{v})).getReturnValue();
    }

    public static String decrypt(String v) {
        return (String) sAuthenticationInstance.execute(METHOD_DECRYPT, Arrays.asList(new Object[]{v})).getReturnValue();
    }

    public static void hideProgress(UIContext context) {
        api(context, OBJECT_NAME_PROGRESS).execute(METHOD_HIDE, Collections.emptyList());
    }

    private static ExternalApi api(UIContext context, String apiName) {
		boolean isComposite = true; // any value is fine because it is not used
		ApiAction dummyAction = new ApiAction(context, new ActionDefinition(null), null, isComposite);
		return Services.Application.getExternalApiFactory().getInstance(apiName, dummyAction);
	}
}

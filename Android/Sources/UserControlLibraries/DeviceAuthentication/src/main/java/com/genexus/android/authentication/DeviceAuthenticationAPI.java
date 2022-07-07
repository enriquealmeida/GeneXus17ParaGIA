package com.genexus.android.authentication;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.activities.LoginBiometricsActivity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.BiometricsHelper;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.utils.Cast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;

import static android.content.Context.KEYGUARD_SERVICE;

public class DeviceAuthenticationAPI extends ExternalApi {
    public static final String OBJECT_NAME = "GeneXus.SD.DeviceAuthentication";

    // API Property Names
    private static final String PROPERTY_BIOMETRICS_DESCRIPTION = "BiometricsDescription";
    private static final String PROPERTY_ALLOWABLE_REUSE_DURATION = "AllowableReuseDuration";
    // API Method Names
    private static final String METHOD_IS_AVAILABLE = "IsAvailable";
    private static final String METHOD_AUTHENTICATE = "Authenticate";
    private static final String METHOD_REGISTER_REUSE_OBSERVER = "RegisterReuseObserver";
    private static final String METHOD_ENCRYPT = "Encrypt";
    private static final String METHOD_DECRYPT = "Decrypt";

    private static final String VALUE_POLICY_ANY = "0";
    private static final String VALUE_POLICY_BIOMETRICS = "1";

    private static final String LOG_TAG = "Genexus-DeviceAuthentication";

    private static int mAllowableReuseDuration;
    private static Date mLastSuccessfullAuthentication;
    private static LifecycleObserver sLifecycleObserver;
    private static Cipher sFingerprintCypher;

    public DeviceAuthenticationAPI(ApiAction action) {
        super(action);
        addPropertyHandler(PROPERTY_BIOMETRICS_DESCRIPTION, mBiometricsDescriptionGet, null);
        addPropertyHandler(PROPERTY_ALLOWABLE_REUSE_DURATION, mAllowableReuseDurationGet, mAllowableReuseDurationSet);
        addMethodHandler(METHOD_IS_AVAILABLE, 1, mIsAvailableMethod);
        addMethodHandler(METHOD_AUTHENTICATE, 3, mAuthenticateMethod);
        addMethodHandler(METHOD_AUTHENTICATE, 4, mAuthenticateMethod);
        addMethodHandler(METHOD_REGISTER_REUSE_OBSERVER, 0, mRegisterReuseObserverMethod);
        addMethodHandler(METHOD_ENCRYPT, 1, mAuthenticateEncrypt);
        addMethodHandler(METHOD_DECRYPT, 1, mAuthenticateDecrypt);
    }

    private final IMethodInvoker mBiometricsDescriptionGet = parameters -> ExternalApiResult.success(isFingerprintAvailable() ? "Fingerprint" : "");

    private final IMethodInvoker mAllowableReuseDurationGet = parameters -> ExternalApiResult.success(mAllowableReuseDuration);

    private final IMethodInvoker mAllowableReuseDurationSet = parameters -> {
        final String duration = (String) parameters.get(0);
        try {
            mAllowableReuseDuration = Integer.parseInt(duration);
        } catch(NumberFormatException e) {
            Services.Log.error(LOG_TAG, "Allowable Reuse Duration parse value is not valid: " + duration);
            return ExternalApiResult.FAILURE;
        }
        return ExternalApiResult.SUCCESS_CONTINUE;
    };

    private final IMethodInvoker mIsAvailableMethod = parameters -> {
        final String method = (String) parameters.get(0);
        if (method.equals(VALUE_POLICY_ANY)) {
            KeyguardManager keyguardManager = (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);
			if (!keyguardManager.isKeyguardSecure())
				Services.Log.debug("Device isKeyguardSecure() false. lockscreen security in not enabled in your device");
            return ExternalApiResult.success(keyguardManager.isKeyguardSecure());
        }
        else if (method.equals(VALUE_POLICY_BIOMETRICS)) {
            return ExternalApiResult.success(isFingerprintAvailable());
        }
        else {
            return ExternalApiResult.success(false);
        }
    };

    @SuppressWarnings("deprecation")
    private boolean isFingerprintAvailable() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Services.Log.error(LOG_TAG, "This device Android version doesn't support fingerprint authentication");
            return false;
        }

        KeyguardManager keyguardManager = (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);
        if (keyguardManager == null || !keyguardManager.isKeyguardSecure()) {
            Services.Log.error(LOG_TAG, "Please enable lockscreen security in your device's Settings");
            return false;
        }

        // there is no other way to know if it has fingerprint support (API 29)
        androidx.core.hardware.fingerprint.FingerprintManagerCompat fingerprintManager = androidx.core.hardware.fingerprint.FingerprintManagerCompat.from(getContext());
        if (!fingerprintManager.isHardwareDetected()) {
            Services.Log.error(LOG_TAG, "This device doesn't support fingerprint authentication");
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            Services.Log.error(LOG_TAG, "No fingerprint configured. Please register at least one fingerprint in your device's Settings");
        } else {
            return true;
        }

        return false;
    }

    // TODO: Give access to the error messages to the end user. https://issues.genexus.com/viewissue.aspx?75920,
    private final IMethodInvokerWithActivityResult mAuthenticateMethod = new IMethodInvokerWithActivityResult() {
        private String mTitle;
        private String mUsageDescription;
        private int mMode;
        private ExternalApiResult mResult;

        @Override
        public @NonNull ExternalApiResult invoke(List<Object> parameters)
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                return ExternalApiResult.success(false); // not authentication available, return false

            String method = (String) parameters.get(0);
            mTitle = (String) parameters.get(1);
            mUsageDescription = (String) parameters.get(2);
            if (parameters.size() == 3)
                mMode = Cipher.ENCRYPT_MODE;
            else
                mMode = (int) parameters.get(3); // It can be Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE, if authentication is successful a call to METHOD_ENCRYPT or METHOD_DECRYPT can be made matching this value

            if (mLastSuccessfullAuthentication != null && (Calendar.getInstance().getTime().getTime() - mLastSuccessfullAuthentication.getTime()) / 1000 < mAllowableReuseDuration)
                return ExternalApiResult.success(true);

            if (isFingerprintAvailable() && (method.equals(VALUE_POLICY_ANY) || method.equals(VALUE_POLICY_BIOMETRICS))) {
                return showBiometricPrompt(false);
            } else if (method.equals(VALUE_POLICY_ANY)) {
                return showBiometricPrompt(true);
            } else {
				// not fingerprint and not authentication, return false
				return ExternalApiResult.success(false);
			}
       }

        // It requires version M for some new functions needed in the Cipher, also fingerprint support was introduced in that version
        @RequiresApi(api = Build.VERSION_CODES.M)
        private ExternalApiResult showBiometricPrompt(boolean useDeviceCredentialAllowed) {
            FragmentActivity activity = Cast.as(FragmentActivity.class, getActivity());
            if (activity == null) {
                Services.Log.error(LOG_TAG, "FragmentActivity is needed");
                return ExternalApiResult.success(false);
            }

            Cipher baseCipher = CipherHelper.getCipher(getActivity().getApplicationContext(), mMode);
            if (baseCipher != null) {
                BiometricPrompt.CryptoObject cryptoObject = new BiometricPrompt.CryptoObject(baseCipher);
                BiometricPrompt.PromptInfo.Builder builder = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(mTitle)
                        .setDescription(mUsageDescription);
                if (useDeviceCredentialAllowed)
                    builder.setDeviceCredentialAllowed(true);
                else
                    builder.setNegativeButtonText(Services.Strings.getResource(R.string.GXM_cancel));
                BiometricPrompt.PromptInfo info = builder.build();

                Executor executor = getMainThreadExecutor();
                final BiometricPrompt prompt = new BiometricPrompt(activity, executor,
                        new BiometricPrompt.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                sFingerprintCypher = null;
                                mResult = ExternalApiResult.success(false);
                                continueExecution();
                            }

                            @Override
                            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                sFingerprintCypher = result.getCryptoObject().getCipher();
                                mResult = ExternalApiResult.success(true);
                                continueExecution();
                            }
                        });
                executor.execute(() -> prompt.authenticate(info, cryptoObject));
            }
            return ExternalApiResult.SUCCESS_WAIT;
        }

        private Executor getMainThreadExecutor() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
                return new MainThreadExecutor();
            else
                return getContext().getMainExecutor();
        }

        class MainThreadExecutor implements Executor {
            private final Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(@NonNull Runnable r) {
                handler.post(r);
            }
        }

        private void continueExecution() {
            LoginBiometricsActivity biometricsActivity = Cast.as(LoginBiometricsActivity.class, getActivity());
            if (biometricsActivity != null)
                biometricsActivity.onAuthenticationResult(mResult);
            else
                ActionExecution.continueCurrent(getActivity(), false, getAction());
        }

        @Override
        public @NonNull ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
            if (mResult != null) {
                return mResult;
            } else if (resultCode == Activity.RESULT_OK) {
                mLastSuccessfullAuthentication = Calendar.getInstance().getTime();
                return ExternalApiResult.success(true);
            } else {
                return ExternalApiResult.success(false);
            }
        }
    };

    private final IMethodInvoker mRegisterReuseObserverMethod = parameters -> {
        if (sLifecycleObserver == null) {
            sLifecycleObserver = new LifecycleObserver() {
                private Date mLifecycleDate;

                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                public void onStart() {
                    if (mLifecycleDate != null) {
                        if (SecurityHelper.isBiometricsUsed()) {
                            int duration = BiometricsHelper.biometricsReuseDuration(); // seconds
                            long elapsed = Calendar.getInstance().getTime().getTime() - mLifecycleDate.getTime(); // milliseconds
                            if (elapsed / 1000 >= duration) {  // equal so it works with 0 as asking every time
                                BiometricsHelper.startBiometricsActivity(getContext());
                            }
                        }
                    }
                }

                @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
                public void onStop() {
                    mLifecycleDate = Calendar.getInstance().getTime();
                }
            };
            ProcessLifecycleOwner.get().getLifecycle().addObserver(sLifecycleObserver);
        }
        return ExternalApiResult.success(true);
    };

    private final IMethodInvoker mAuthenticateEncrypt = parameters -> {
        final String inValue = (String) parameters.get(0);
        String outValue = CipherHelper.encrypt(sFingerprintCypher, inValue);
        return ExternalApiResult.success(outValue);
    };

    private final IMethodInvoker mAuthenticateDecrypt = parameters -> {
        final String inValue = (String) parameters.get(0);
        String outValue = CipherHelper.decrypt(sFingerprintCypher, inValue);
        return ExternalApiResult.success(outValue);
    };
}

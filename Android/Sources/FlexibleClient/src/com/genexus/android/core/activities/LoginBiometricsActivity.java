package com.genexus.android.core.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import androidx.fragment.app.FragmentActivity;

import com.genexus.android.core.actions.ActionResult;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.services.ServiceResponse;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.BiometricsHelper;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.utils.TaskRunner;

import javax.crypto.Cipher;

public class LoginBiometricsActivity extends FragmentActivity {
    private ServiceResponse mResponse;
    private UIContext mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = UIContext.base(this, Connectivity.fromBundle(savedInstanceState));
        BiometricsHelper.authenticate(mContext, Cipher.DECRYPT_MODE);
    }

    public void onAuthenticationResult(ExternalApiResult out) {
        TaskRunner.execute(() -> { // SecurityHelper.afterLoginBiometrics() must be called in a background thread
            boolean useStandardLogin = true;
            if (out.getActionResult() == ActionResult.SUCCESS_CONTINUE && (boolean) out.getReturnValue()) {
                if (mResponse == null) {
                    Pair<Boolean, String> r = SecurityHelper.afterLoginBiometrics(mContext, v -> BiometricsHelper.decrypt(v));
                    if (r.first) {
                        useStandardLogin = false;
                    } else if (Strings.hasValue(r.second)) {
                        ServiceResponse response = SecurityHelper.tryRenewLogin(r.second);
                        if (response.getResponseOk()) {
                            mResponse = response;
                            BiometricsHelper.authenticate(mContext, Cipher.ENCRYPT_MODE);
                            return; // ask for fingerprint again to save the tokens
                        } else {
                            ResultDetail<SecurityHelper.LoginStatus> detail = SecurityHelper.afterLoginFail(response);
                            Services.Log.error("LoginBiometrics", detail.getMessage());
                        }
                    }
                } else {
                    ResultDetail<SecurityHelper.LoginStatus> detail = SecurityHelper.afterLoginBiometrics(mContext, v -> BiometricsHelper.encrypt(v), mResponse);
                    if (detail.getResult()) {
                        useStandardLogin = false;
                    }
                }
            }
            if (useStandardLogin) {
                // Don't use callLogin here because this Activity must be out of the back stack when it is called else it will create a new Task
                BiometricsHelper.callLoginNextStartBiometricActivity();
            }
            setResult(Activity.RESULT_OK);
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// call super is mandatory
		super.onActivityResult(requestCode, resultCode, data);

    	// catch callConfirmDeviceCredential() result
        ExternalApiResult out = BiometricsHelper.authenticateOnActivityResult(requestCode, resultCode, data);
        onAuthenticationResult(out);
    }
}

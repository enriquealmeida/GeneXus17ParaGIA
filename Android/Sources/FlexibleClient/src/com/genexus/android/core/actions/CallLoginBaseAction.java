package com.genexus.android.core.actions;

import javax.crypto.Cipher;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Pair;

import com.genexus.android.R;
import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.services.ServiceResponse;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.BiometricsHelper;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.TaskRunner;

import java.util.ArrayList;

public abstract class CallLoginBaseAction extends Action {
    protected String mErrorMessage = Strings.EMPTY;
    protected boolean mWaitForResult = false;

    private final ActionParameter mReturnValue;
    protected ServiceResponse mResponse;
    private boolean mAuthenticateCalled;
    private boolean mReturnContinue;

	private static final String GAM_EVENTS = "GeneXusSecurity.GAMLoginEvents";
	private static final String EVENT_2FA = "TwoFactorAuthenticationRequested";
	private static final String EVENT_OTP = "OTPAuthenticationRequested";

    CallLoginBaseAction(UIContext context, ActionDefinition definition, ActionParameters parameters) {
        super(context, definition, parameters);
        mReturnValue = ActionHelper.newAssignmentParameter(definition, "@returnValue", ActionHelper.ASSIGN_LEFT_EXPRESSION);
    }

    protected boolean checkBiometrics() {
        if (mResponse.getResponseOk() &&
                BiometricsHelper.isBiometricsEnabled() &&
                BiometricsHelper.isFingerprintAvailable(getContext())) {
            BiometricsHelper.hideProgress(getContext());

            getActivity().runOnUiThread(() -> {
                new AlertDialog.Builder(getActivity())
                        .setMessage(Services.Strings.getResource(R.string.GXM_DoYouWantBiometricQuestion, Services.Strings.getResource(R.string.GXM_FingerprintQuestion)))
                        .setPositiveButton(Services.Strings.getResource(R.string.GXM_Yes), (dialog, which) -> {
                            mAuthenticateCalled = true;
                            ExternalApiResult apiResult = BiometricsHelper.authenticate(getContext(), Cipher.ENCRYPT_MODE);
                            processResult(apiResult.getActionResult());
                        })
                        .setNegativeButton(Services.Strings.getResource(R.string.GXM_No), (dialog, which) -> {
                            TaskRunner.execute(this::afterLogin);
                            processResult(ActionResult.SUCCESS_CONTINUE);
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            });

            mWaitForResult = true;
            return true;
        } else {
            // Already handled.
            return afterLogin();
        }
    }

    private void processResult(ActionResult result) {
        if (result == ActionResult.FAILURE) {
            ActionExecution.cancelCurrent(this);
        } else if (result == ActionResult.SUCCESS_WAIT) {
            // Nothing to do, already on wait
        } else {
            mReturnContinue = true;
            ActionExecution.continueCurrent(getActivity(), false, this);
        }
    }

    protected abstract boolean afterLogin();

    protected void setOutputValue(Expression.Value value) {
        setOutputValue(mReturnValue, value);
    }

	protected boolean hasOutput()
	{
		return mReturnValue.getExpression() != null || Strings.hasValue(mReturnValue.getValue());
	}

	@Override
    public String getErrorMessage() {
        return mErrorMessage;
    }

    @Override
    public boolean catchOnActivityResult() {
        return mWaitForResult;
    }

    protected Pair<ActionResult, Boolean> afterActivityResultBiometrics(int requestCode, int resultCode, Intent result) {
        if (mReturnContinue) {
            mReturnContinue = false;
            return new Pair<>(ActionResult.SUCCESS_CONTINUE, true);
        } else if (mAuthenticateCalled) {
            mAuthenticateCalled = false;
            // authenticate result
            ExternalApiResult out = BiometricsHelper.authenticateOnActivityResult(requestCode, resultCode, result);
            if (out == null || (out.getActionResult() == ActionResult.SUCCESS_CONTINUE && !((Boolean) out.getReturnValue()))) {
                setOutputValue(Expression.Value.newBoolean(false));
                return new Pair<>(ActionResult.FAILURE, true);
            } else {
                if (out.getActionResult() == ActionResult.SUCCESS_CONTINUE) {
                    TaskRunner.execute(() -> { // SecurityHelper.afterLoginBiometrics() must be called in a background thread
                        SecurityHelper.afterLoginBiometrics(getContext(), BiometricsHelper::encrypt, mResponse);
                        processResult(ActionResult.SUCCESS_CONTINUE);
                    });
                    return new Pair<>(ActionResult.SUCCESS_WAIT, true);
                }
                return new Pair<>(out.getActionResult(), true);
            }
        }
        return new Pair<>(ActionResult.SUCCESS_CONTINUE, false);
    }

	private boolean redirectToChangePassword(ResultDetail<SecurityHelper.LoginStatus> result)
	{
		if (result.getData() == SecurityHelper.LoginStatus.CHANGE_PASSWORD) {
			// Go to the "change password" screen if available. The login screen waits.
			String changePasswordPanel = Services.Application.get().getChangePasswordObject();
			if (Services.Strings.hasValue(changePasswordPanel)) {
				// if must change password clear current session and token
				Services.Log.debug("do logout and call changePasswordPanel");
				SecurityHelper.logoutLocal();
				if (ActivityLauncher.callForResult(getContext(), changePasswordPanel, RequestCodes.ACTION)) {
					Services.Messages.showMessage(result.getMessage());

					mWaitForResult = true; // Current action must wait.
					return true;
				}
			}
		}
		return false;
	}

	private boolean fireOtpEvent(ResultDetail<SecurityHelper.LoginStatus> result) {
		if (result.getData() != SecurityHelper.LoginStatus.OTP_REQUIRED)
			return false;

		fireEvent(EVENT_OTP);
		return true;
	}

	private boolean fire2FAEvent(ResultDetail<SecurityHelper.LoginStatus> result) {
		if (result.getData() != SecurityHelper.LoginStatus.TFA_REQUIRED)
			return false;

		fireEvent(EVENT_2FA);
		return true;
	}

	private void fireEvent(String eventName) {
		ExternalObjectEvent event = new ExternalObjectEvent(GAM_EVENTS, eventName);
		Coordinator coordinator = event.getFormCoordinatorForEvent((IGxActivity) getActivity());
		if (coordinator == null) {
			Services.Log.error(String.format("Event '%s' not fired as it wasn't found", eventName));
			return;
		}

		event.fire(new ArrayList<>(), coordinator, null);
	}

	protected boolean afterLoginCommon(ResultDetail<SecurityHelper.LoginStatus> result) {
		if (result.getResult()) {
			setOutputValue(Expression.Value.newBoolean(true));
			return true;
		} else {
			if (redirectToChangePassword(result)) {
				mErrorMessage = Strings.EMPTY;
				return true; // Current action must wait.
			}

			boolean shouldFireEvent = fireOtpEvent(result) || fire2FAEvent(result);
			boolean continueExecution = shouldFireEvent || this.hasOutput();
			setOutputValue(Expression.Value.newBoolean(shouldFireEvent));
			mErrorMessage = continueExecution ? Strings.EMPTY : result.getMessage(); // Clear error message if execution continues
			return continueExecution; // Execution should continue if an event was fired or there's an output variable that handles result
		}
	}

	protected static boolean isNotAllowed() {
		return Services.Application.hasActiveMiniApp();
	}
}

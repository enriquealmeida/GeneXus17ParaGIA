package com.genexus.android.core.actions;

import java.net.HttpURLConnection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;

import com.genexus.android.gam.AuthBrowserHelper;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.ActionsHelper;
import com.genexus.android.core.common.SecurityHelper;


class CallLoginExternalAction extends CallLoginBaseAction
{
	private boolean mUsingProvider;

	CallLoginExternalAction(UIContext context, ActionDefinition definition, ActionParameters parameters) {
		super(context, definition, parameters);
	}

	@Override
	public boolean Do() {
		if (isNotAllowed())
			return false;

		if (ActionsHelper.runLoginExternalActionWithProvider(this)) {
			mUsingProvider = true;
			mWaitForResult = true;
			return true;
		} else {
			mResponse = ActionsHelper.runLoginExternalAction(this, AuthBrowserHelper.getSupportedRedirectUrlScheme());
			if (mResponse != null && mResponse.HttpCode == HttpURLConnection.HTTP_SEE_OTHER) {
				// Must show browser for login.
				// Does not set mReturnValue because it's unknown at this point.
				AuthBrowserHelper.requestAuthorization(getActivity(), Uri.parse(mResponse.Message));
				mWaitForResult = true;
				return true;
			} else {
				return checkBiometrics();
			}
		}
	}

	@Override
	protected boolean afterLogin() {
		ResultDetail<SecurityHelper.LoginStatus> result = SecurityHelper.afterLogin(mResponse);
		return afterLoginCommon(result);
	}

	@Override
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result) {
		if (mUsingProvider) {
			mResponse = ActionsHelper.afterActivityResultLoginExternalActionWithProvider(this, AuthBrowserHelper.getSupportedRedirectUrlScheme());
			if (mResponse != null) {
				if (afterLogin()) {
					return ActionResult.SUCCESS_CONTINUE;
				} else if (Strings.hasValue(mErrorMessage)) { // afterLogin loads mErrorMessage
					getActivity().runOnUiThread(() -> Services.Messages.showErrorDialog(getActivity(), mErrorMessage));
				}
			}
			if (hasOutput()) {
				setOutputValue(Value.newBoolean(false)); // set return value and
				return ActionResult.SUCCESS_CONTINUE; // continue anyway (new behavior)
			} else {
				ActionExecution.cancelCurrent(null); // Stop composite after a failed SDAction.LoginExternal().
				return ActionResult.FAILURE; // this doesn't cancel the composite
			}
		} else {
			Pair<ActionResult, Boolean> p = afterActivityResultBiometrics(requestCode, resultCode, result);
			if (!p.second && result != null && resultCode == Activity.RESULT_OK) {
				// AuthBrowserHelper.requestAuthorization
				ResultDetail<?> resultDetail = (ResultDetail<?>) result.getSerializableExtra(AuthBrowserHelper.EXTRA_EXTERNAL_LOGIN_RESULT);

				// process result from AuthBrowserHelper if was returned
				if (resultDetail != null) {
					setOutputValue(Value.newValue(resultDetail.getResult()));

					if (!resultDetail.getResult() && !hasOutput()) { // don't show error or cancel composite if !hasOutput() (new behaviour)
						// Show error message if present, otherwise fail silently.
						if (Strings.hasValue(resultDetail.getMessage()))
							getActivity().runOnUiThread(() -> Services.Messages.showErrorDialog(getActivity(), resultDetail.getMessage()));
						ActionExecution.cancelCurrent(null); // Stop composite after a failed SDAction.LoginExternal().
						return ActionResult.FAILURE; // this doesn't cancel the composite
					}
				}
			} else if (!p.second && !hasOutput()) {
				ActionExecution.cancelCurrent(null); // Stop composite after a failed SDAction.LoginExternal().
				return ActionResult.FAILURE; // this doesn't cancel the composite
			}
			return ActionResult.SUCCESS_CONTINUE;
		}
	}
}

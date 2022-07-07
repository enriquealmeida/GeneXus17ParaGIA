package com.genexus.android.core.actions;

import android.content.Intent;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.common.ActionsHelper;
import com.genexus.android.core.common.SecurityHelper;

class CallLoginAction extends CallLoginBaseAction
{
	CallLoginAction(UIContext context, ActionDefinition definition, ActionParameters parameters) {
		super(context, definition, parameters);
	}

	@Override
	public boolean Do() {
		if (isNotAllowed())
			return false;

		mResponse = ActionsHelper.runLoginAction(this);
		return checkBiometrics();
	}

	@Override
	protected boolean afterLogin() {
		ResultDetail<SecurityHelper.LoginStatus> result = SecurityHelper.afterLogin(mResponse);

		return afterLoginCommon(result);
	}


	@Override
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result) {
		return afterActivityResultBiometrics(requestCode, resultCode, result).first;
	}
}

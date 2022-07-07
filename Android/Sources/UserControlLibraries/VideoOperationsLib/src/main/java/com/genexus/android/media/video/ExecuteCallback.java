package com.genexus.android.media.video;

import android.app.Activity;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.expressions.Expression;

class ExecuteCallback extends Callback {

	private Activity mActivity;
	private ApiAction mAction;

	public ExecuteCallback(ApiAction apiAction) {
		this.mActivity = apiAction.getMyActivity();
		this.mAction = apiAction;
	}

	@Override
	public void onCommandExecuted(int returnCode, String outputVideo) {
		mAction.setOutputValue(Expression.Value.newString(outputVideo));
		ActionExecution.continueCurrent(mActivity, true, mAction);
	}
}

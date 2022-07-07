package com.genexus.android.beacons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.services.Services;

@SuppressLint("NewApi")
class BeaconCallback extends AdvertiseCallback {

	private final Activity mActivity;
	private final ApiAction mAction;

	BeaconCallback(ApiAction action) {
		mActivity = action.getMyActivity();
		mAction = action;
	}

	@Override
	public void onStartSuccess(AdvertiseSettings settingsInEffect) {
		Services.Log.info("Advertisement start succeeded");
		mAction.setOutputValue(Expression.Value.newBoolean(true));
		ActionExecution.continueCurrent(mActivity, true, mAction);
	}

	@Override
	public void onStartFailure(int errorCode) {
		Services.Log.error("Advertisement start failed with code " + errorCode);
		mAction.setOutputValue(Expression.Value.newBoolean(false));
		ActionExecution.continueCurrent(mActivity, true, mAction);
	}
}

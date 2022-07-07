package com.genexus.android.notifications.firebase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.genexus.android.core.base.services.Services;
import com.google.common.util.concurrent.ListenableFuture;

public class MyWorker extends ListenableWorker {

	public MyWorker(@NonNull Context appContext, @NonNull WorkerParameters params) {
		super(appContext, params);
	}

	@NonNull
	@Override
	public ListenableFuture<Result> startWork() {
		Services.Log.debug("Performing long running task in scheduled job");
		Data input = getInputData();
		return null;
	}

	@Override
	public void onStopped() {
		// Cleanup because you are being stopped.
	}
}

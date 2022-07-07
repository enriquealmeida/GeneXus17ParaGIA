package com.genexus.android.core.base.synchronization;

import com.genexus.android.core.base.services.Services;

public class SynchronizedResult<T> {

	private static final long TIMEOUT = 5000L;
	private T result;

	public T getResult() {
		long startTimeMillis = System.currentTimeMillis();
		while (result == null && System.currentTimeMillis() - startTimeMillis < TIMEOUT) {
			synchronized (this) {
				try {
					wait(TIMEOUT);
				} catch (InterruptedException e) {
					Services.Log.error("An exception occurred during synchronizing result: ", e);
				}
			}
		}
		return result;
	}

	public void setResult(T result) {
		this.result = result;
		synchronized (this) {
			notify();
		}
	}

}

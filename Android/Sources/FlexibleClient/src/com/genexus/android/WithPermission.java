package com.genexus.android;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.base.utils.ResultRunnable;
import com.genexus.android.core.base.utils.RunnableUtils;
import com.genexus.android.core.base.utils.Strings;
import com.google.android.material.snackbar.Snackbar;

/**
 * Helper for dealing with Android M permissions.
 */
public class WithPermission<ResultT> {
	private final Activity mActivity;
	private final Data<ResultT> mData;

	private final Object mWaitForPermissionResult;
	private Boolean mRequestResult;

	private WithPermission(Activity activity, Data<ResultT> data) {
		mActivity = activity;
		mData = data;
		mWaitForPermissionResult = new Object();
	}

	/**
	 * Executes the code supplied to the builder with the requested permissions.
	 * If the app already has the permissions, then the code is executed immediately. Otherwise
	 * {@link Activity#requestPermissions(String[], int)} is called and the code will be executed
	 * later, if they are granted.
	 *
	 * @return If {@link Builder#setBlockThread(boolean)} is set, then either the result of
	 * {@link Builder#onSuccess(ResultRunnable)} or {@link Builder#onFailure(ResultRunnable)} depending
	 * on whether the permission could be obtained. If the thread is not blocked, then the result
	 * of {@link Builder#onPermissionRequested(Function)}, or null.
	 */
	public ResultT run() {
		mRequestResult = null;
		if (mData.requestPermissions.isEmpty())
			mData.requestPermissions = mData.requiredPermissions;

		String[] permissions = Strings.newArray(mData.requestPermissions);
		Context context = mActivity;
		if (context == null)
			context = Services.Application.getAppContext();

		if (PermissionUtil.checkSelfPermissions(context, permissions)) {
			// Already had permission, we can proceed.
			return continueExecution(true);
		} else {
			if (mActivity == null) {
				Services.Log.warning("WithPermission.Builder was not supplied an activity. Asking for permissions is currently impossible without one, so this code will fail.");
				return continueExecution(false);
			} else {
				if (Strings.hasValue(mData.rationale) && PermissionUtil.shouldShowRequestPermissionRationale(mActivity, permissions))
					return showRationale(permissions, mData.rationale);

				return requestPermissions(permissions);
			}
		}
	}

	@SuppressWarnings("WaitNotInLoop")
	private ResultT requestPermissions(String[] permissions) {
		// This is fired BEFORE requestPermissions() to make sure the handler is registered
		// if the request fails immediately (if "Don't Ask Again" was selected before).
		ResultT afterPermissionRequestResult = null;
		if (mData.permissionRequestedCode != null)
			afterPermissionRequestResult = mData.permissionRequestedCode.run(this);

		ActivityCompat.requestPermissions(mActivity, permissions, mData.requestCode);

		if (!mData.blockThread) {
			// Let this thread finish.
			// The result will be picked up by onRequestPermissionsResult() later.
			return afterPermissionRequestResult;
		} else {
			if (Services.Device.isMainThread())
				throw new IllegalStateException("Cannot run() in main thread if Builder.setBlockThread(true) is set");

			synchronized (mWaitForPermissionResult) {
				try {
					mWaitForPermissionResult.wait();
				} catch (InterruptedException ignored) {
					// Should not happen, but if it does just allow thread to end.
					mRequestResult = false;
				}
			}

			if (mRequestResult == null)
				throw new IllegalStateException("mRequestResult should be assigned at this point!");

			return continueExecution(mRequestResult);
		}
	}

	@SuppressWarnings("WaitNotInLoop")
	private ResultT showRationale(String[] permissions, String rationale) {
		if (!Strings.hasValue(rationale))
			return continueExecution(false);

		Snackbar snackbar = Snackbar.make(mActivity.getWindow().getDecorView().findViewById(android.R.id.content),
				rationale, Snackbar.LENGTH_LONG)
				.setAction(R.string.GXM_button_ok, view -> { })
				.addCallback(new Snackbar.Callback() {
					@Override
					public void onDismissed(Snackbar snackbar, int event) {
						super.onDismissed(snackbar, event);
						synchronized (mWaitForPermissionResult) {
							mWaitForPermissionResult.notify();
						}
					}
				});

		snackbar.show();
		synchronized (mWaitForPermissionResult) {
			try {
				mWaitForPermissionResult.wait();
			} catch (InterruptedException ignored) {
				// Should not happen, but if it does just allow thread to end.
				mRequestResult = false;
			}
		}

		return requestPermissions(permissions);
	}

	/**
	 * Called to signal that the permission request has finished (successfully or not).
	 *
	 * @return If {@link Builder#setBlockThread(boolean)} is set, then null (result will be returned
	 * by original thread after waking it). Otherwise, the result of {@link Builder#onSuccess(ResultRunnable)}.
	 */
	public ResultT onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (mData.requestCode != requestCode)
			throw new IllegalArgumentException(String.format("Improperly forwarded onRequestPermissionsResult -- expected %s, but received %s instead.", mData.requestCode, requestCode));

		boolean success = PermissionUtil.verifyPermissionsResult(grantResults);
		if (!success) success = PermissionUtil.verifyLocationPermissionsResult(permissions, grantResults);

		if (mData.blockThread) {
			// Set the result, then signal the original thread and let it continue executing.
			mRequestResult = success;
			synchronized (mWaitForPermissionResult) {
				mWaitForPermissionResult.notify();
			}

			return null;
		} else {
			// Run the code in the current thread.
			return continueExecution(success);
		}
	}

	private ResultT continueExecution(boolean success) {
		if (!success && !mData.requiredPermissions.containsAll(mData.requestPermissions)) {
			String[] permissions = Strings.newArray(mData.requiredPermissions);

			Context context = mActivity;
			if (context == null)
				context = Services.Application.getAppContext();

			success = PermissionUtil.checkSelfPermissions(context, permissions);
		}

		if (success) {
			if (mData.successCode != null)
				return mData.successCode.run();
		} else {
			if (mData.failureCode != null)
				return mData.failureCode.run();
		}

		return null;
	}

	public static class Builder<ResultT> {
		private final Activity mActivity;
		private final Data<ResultT> mData;
		private boolean mAlternativeCodeLocked;

		public Builder(Activity activity) {
			mActivity = activity;
			mData = new Data<>();
		}

		public Builder<ResultT> require(String permission) {
			mData.requiredPermissions.add(permission);
			return this;
		}

		public Builder<ResultT> require(String[] permissions) {
			if (permissions != null)
				mData.requiredPermissions.addAll(Arrays.asList(permissions));
			return this;
		}

		public Builder<ResultT> request(String permission) {
			mData.requestPermissions.add(permission);
			return this;
		}

		public Builder<ResultT> request(String[] permissions) {
			if (permissions != null)
				mData.requestPermissions.addAll(Arrays.asList(permissions));
			return this;
		}

		public Builder<ResultT> setRationale(String rationale) {
			mData.rationale = rationale;
			return this;
		}

		public Builder<ResultT> setRequestCode(int requestCode) {
			mData.requestCode = requestCode;
			return this;
		}

		public Builder<ResultT> setBlockThread(boolean blockThread) {
			if (mAlternativeCodeLocked)
				throw new IllegalStateException("setBlockThread() cannot be called on this Builder -- modifications are not allowed.");

			mData.blockThread = blockThread;
			return this;
		}

		public Builder<ResultT> onSuccess(@NonNull ResultRunnable<ResultT> code) {
			mData.successCode = code;
			return this;
		}

		public Builder<ResultT> onSuccess(@NonNull final Runnable code) {
			return onSuccess(RunnableUtils.buildResultRunnableFromRunnable(code));
		}

		public Builder<ResultT> attachToActivityController() {
			if (mAlternativeCodeLocked)
				throw new IllegalStateException("attachToActivityController() cannot be called on this Builder -- modifications are not allowed.");

			mData.permissionRequestedCode = runner -> {
				if (!(runner.mActivity instanceof IGxActivity)) {
					throw new IllegalStateException("attachToActivityController() failed because Activity is not an instance of IGxBaseActivity.");
				} else {
					((IGxActivity) runner.mActivity).getController().setRequestPermissionsResultHandler((requestCode, permissions, grantResults) -> {
						if (mData.requestCode == requestCode) {
							runner.onRequestPermissionsResult(requestCode, permissions, grantResults);
							return true;
						} else
							return false; // Didn't belong to us.
					});
					return null;
				}
			};

			return this;
		}

		public Builder<ResultT> onPermissionRequested(Function<WithPermission<ResultT>, ResultT> code) {
			if (mAlternativeCodeLocked)
				throw new IllegalStateException("onPermissionRequested() cannot be called on this Builder -- modifications are not allowed.");

			mData.permissionRequestedCode = code;
			return this;
		}

		public Builder<ResultT> onFailure(ResultRunnable<ResultT> code) {
			if (mAlternativeCodeLocked)
				throw new IllegalStateException("onFailure() cannot be called on this Builder -- modifications are not allowed.");

			mData.failureCode = code;
			return this;
		}

		public Builder<ResultT> onFailure(Runnable code) {
			return onFailure(RunnableUtils.buildResultRunnableFromRunnable(code));
		}

		public Builder<ResultT> lockAlternativeCode() {
			mAlternativeCodeLocked = true;
			return this;
		}

		public WithPermission<ResultT> build() {
			if (mData.successCode == null)
				throw new IllegalStateException("'onSuccess' runnable is mandatory to complete this action.");

			return new WithPermission<>(mActivity, mData);
		}
	}

	private static class Data<ResultT> {
		private final Set<String> requiredPermissions = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		private Set<String> requestPermissions = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		private String rationale;
		private boolean blockThread = false;
		private int requestCode = RequestCodes.ACTION;

		private ResultRunnable<ResultT> successCode;
		private ResultRunnable<ResultT> failureCode;
		private Function<WithPermission<ResultT>, ResultT> permissionRequestedCode;
	}
}

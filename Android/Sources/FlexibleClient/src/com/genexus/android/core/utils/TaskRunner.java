package com.genexus.android.core.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Based on https://medium.com/swlh/asynctask-is-deprecated-now-what-f30c31362761
// Replacement for AsyncTask which was deprecated in API 30
public class TaskRunner {
	private static final Handler HANDLER = new Handler(Looper.getMainLooper());
	private static final Executor EXECUTOR = Executors.newCachedThreadPool();

	public static <R> void execute(BaseTask<R> task) {
		executeOnExecutor(EXECUTOR, task);
	}

	public static <R> void executeOnExecutor(Executor executor, BaseTask<R> task) {
		HANDLER.post(() -> {
			task.onPreExecute();
			EXECUTOR.execute(() -> {
				final R result = task.doInBackground();
				if (task.isCancelled())
					HANDLER.post(() -> task.onCancelled(result));
				else
					HANDLER.post(() -> task.onPostExecute(result));
			});
		});
	}

	public static void execute(Runnable runnable) {
		EXECUTOR.execute(runnable); // Execute on background
	}

	public static void executeUI(Runnable runnable) {
		HANDLER.post(runnable); // Execute on UI thread
	}

	public abstract static class BaseTask<R> {
		public void onPreExecute() { } // thread UI, called before doInBackground
		public R doInBackground() {
			return null;
		} // thread Background
		public void onPostExecute(R result) { } // thread UI, called after doInBackground if isCancelled() = false
		public void onCancelled(R result) { onCancelled(); } // thread UI, called after doInBackground if isCancelled() = true
		public void onCancelled() { }

		private boolean mIsCancelled;
		public void cancel() {
			mIsCancelled = true;
		}
		public boolean isCancelled() {
			return mIsCancelled;
		}
	}

	public abstract static class UpdateTask<U, R> extends BaseTask<R> {
		protected void publishProgress(U param) {
			TaskRunner.executeUI(() -> onProgressUpdate(param));
		}
		protected void onProgressUpdate(U param) { } // thread UI
	}
}

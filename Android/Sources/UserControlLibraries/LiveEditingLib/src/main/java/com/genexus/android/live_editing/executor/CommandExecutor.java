package com.genexus.android.live_editing.executor;

import java.util.concurrent.Executor;

import android.app.Activity;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.live_editing.inspector.IInspector;
import com.genexus.android.live_editing.commands.IServerCommand;
import com.genexus.android.live_editing.support.ILifecycleTracker;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;

import androidx.annotation.NonNull;

import static com.genexus.android.live_editing.LiveEditingGenexusModule.TAG;

class CommandExecutor implements ICommandExecutor {
	private final ILiveEditingImageManager mLiveEditingImageManager;
	private final IInspector mInspector;
	private final ILifecycleTracker mLifecycleTracker;
	private final Executor mExecutor;

	public CommandExecutor(@NonNull ILiveEditingImageManager liveEditingImageManager,
						   @NonNull IInspector inspector,
						   @NonNull ILifecycleTracker lifecycleTracker,
						   @NonNull Executor executor) {
		mLiveEditingImageManager = liveEditingImageManager;
		mInspector = inspector;
		mLifecycleTracker = lifecycleTracker;
		mExecutor = executor;
	}

	@Override
	public void enqueue(IServerCommand command) {
		mExecutor.execute(new ExecuteCommandTask(mLiveEditingImageManager, command));
	}

	private class ExecuteCommandTask implements Runnable {
		private final ILiveEditingImageManager mLiveEditingImageManager;
		private final IServerCommand mCommand;

		public ExecuteCommandTask(ILiveEditingImageManager liveEditingImageManager,
								  IServerCommand command) {
			mLiveEditingImageManager = liveEditingImageManager;
			mCommand = command;
		}

		@Override
		public void run() {
			boolean applyChanges = false;

			try {
				applyChanges = mCommand.execute(mLiveEditingImageManager);
			} catch (IllegalArgumentException e) {
				Services.Log.debug(CommandExecutor.this.getClass().getName(), e.getMessage());
			}

			if (!applyChanges) {
				Services.Log.warning(TAG, String.format("'%s' command changes won't be applied", mCommand.getClass().toString()));
				return;
			}

			for (final Activity activity : mLifecycleTracker.getActivities())
				activity.runOnUiThread(() -> mCommand.applyChanges(activity));

			if (mCommand.shouldInspectUIAfterApplyingChanges())
				mInspector.requestUIInspection();
		}
	}
}

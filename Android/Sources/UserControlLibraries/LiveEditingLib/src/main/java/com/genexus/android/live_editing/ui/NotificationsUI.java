package com.genexus.android.live_editing.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.live_editing.support.Endpoint;
import com.genexus.android.live_editing.LiveEditingGenexusModule;
import com.genexus.android.live_editing.R;
import com.google.android.material.snackbar.Snackbar;

class NotificationsUI implements IUserInterface {
	private static final int NOTIFICATION_ID = 53723;
	private final IApplication mApplication;
	private final Context mContext;
	private LiveEditingGenexusModule mLiveEditingGenexusModule;

	public NotificationsUI(IApplication application, LiveEditingGenexusModule liveEditingGenexusModule) {
		mApplication = application;
		mContext = mApplication.getAppContext();
		mLiveEditingGenexusModule = liveEditingGenexusModule;
	}

	@Override
	public void displayConnectionAttempt(Endpoint targetEndpoint) {
		String message = mContext.getString(R.string.status_connecting, targetEndpoint);
		showNotification(message, true);
	}

	@Override
	public void displayConnectionFailed() {
		Activity activity = ActivityHelper.getCurrentActivity();
		if (activity == null) {
			return;
		}

		stopDisplayingInformation();
		String message = mContext.getString(R.string.status_disconnected);
		showNotification(message, false);

		Snackbar.make(activity.findViewById(android.R.id.content), R.string.could_not_establish_connection, Snackbar.LENGTH_INDEFINITE)
				.setAction(R.string.retry, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mLiveEditingGenexusModule.initiate(mApplication);
					}
				})
				.show();
	}

	@Override
	public void displayConnectionEstablished(Endpoint endpoint) {
		Activity activity = ActivityHelper.getCurrentActivity();
		if (activity == null) {
			return;
		}

		stopDisplayingInformation();
		String message = mContext.getString(R.string.status_connected, endpoint);
		showNotification(message, true);

		Snackbar.make(activity.findViewById(android.R.id.content), R.string.connection_established, Snackbar.LENGTH_SHORT)
				.show();
	}

	@Override
	public void displayConnectionDropped() {
		Activity activity = ActivityHelper.getCurrentActivity();
		if (activity == null) {
			return;
		}

		stopDisplayingInformation();
		String message = mContext.getString(R.string.status_disconnected);
		showNotification(message, false);

		Snackbar
				.make(
						activity.findViewById(android.R.id.content),
						R.string.connection_dropped,
						Snackbar.LENGTH_INDEFINITE
				)
				.setAction(R.string.retry, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mLiveEditingGenexusModule.initiate(mApplication);
					}
				}).show();
	}

	@Override
	public void stopDisplayingInformation() {
		Services.Notifications.closeOngoingNotification(NOTIFICATION_ID);
	}

	private void showNotification(String message, boolean ongoing) {
		if (ongoing) {
			Services.Notifications.showOngoingNotification(
				NOTIFICATION_ID,
				mContext.getString(R.string.live_editing_title),
				message,
				android.R.drawable.ic_menu_edit,
				false);

			return;
		}

		Services.Notifications.showNotification(
			NOTIFICATION_ID,
			mContext.getString(R.string.live_editing_title),
			message,
			null);
	}
}

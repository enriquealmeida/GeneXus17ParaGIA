package com.genexus.android.notifications.jpush;

import android.content.Context;

import com.genexus.android.notification.BadgeManager;
import com.genexus.android.remotenotification.RemoteNotification;
import com.genexus.android.core.framework.GenexusModule;

import cn.jpush.android.api.JPushInterface;

public class JPushModule implements GenexusModule {

	@Override
	public void initialize(Context context)
	{
		JPushInterface.setDebugMode(true);
		//registerMessageReceiver(context);
		JPushInterface.init(context);

		JPushProvider notificationProvider = new JPushProvider();

		RemoteNotification.addProvider(notificationProvider);
		RemoteNotification.setDefaultProvider(notificationProvider);

		BadgeManager.INSTANCE.removeBadge(context);
	}

	/*
	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.genexus.notifications.jpush.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!JPushUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				JPushUtil.showToast(showMsg.toString() ,context);
			}
		}
	}

	public void registerMessageReceiver(Context context) {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		context.registerReceiver(mMessageReceiver, filter);
	}
	*/
}

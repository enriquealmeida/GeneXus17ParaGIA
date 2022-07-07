package com.genexus.android.notifications.jpush;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.notification.BadgeManager;
import com.genexus.android.core.base.services.Services;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Services.Log.debug(JPushUtil.LOGCAT_TAG, "[Receiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Services.Log.debug(JPushUtil.LOGCAT_TAG, "[Receiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Services.Log.debug(JPushUtil.LOGCAT_TAG, "[Receiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Services.Log.debug(JPushUtil.LOGCAT_TAG, "[Receiver] 接收到推送下来的通知");
            int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Services.Log.debug(JPushUtil.LOGCAT_TAG, "[Receiver] 接收到推送下来的通知的ID: " + notificationId);

			String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			if (content.isEmpty())
				processExtraData(context, bundle);

			BadgeManager.INSTANCE.updateBadgeCount(context, 1);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Services.Log.debug(JPushUtil.LOGCAT_TAG, "[Receiver] 用户点击打开了通知");

			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			String message = "Title: " + title + " Message: " + content;

			processExtraData(context, bundle);

			/*
        	//打开自定义的Activity
        	Intent i = new Intent(context, TestActivity.class);
        	i.putExtras(bundle);
        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);
        	*/

			//String action = result.action.actionID;

			BadgeManager.INSTANCE.updateBadgeCount(context, -1);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Services.Log.debug(JPushUtil.LOGCAT_TAG, "[Receiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Services.Log.warning(JPushUtil.LOGCAT_TAG, "[Receiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
			Services.Log.debug(JPushUtil.LOGCAT_TAG, "[Receiver] Unhandled intent - " + intent.getAction());
        }
	}

	private void processExtraData(Context context, Bundle bundle)
	{
		try
		{
			JSONObject data = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
			String gxDataString = data.optString("GXAdditionalData");
			if (!gxDataString.startsWith("{"))
				gxDataString = "{" + gxDataString + "}";
			JSONObject gxData = new JSONObject(gxDataString);

			JSONObject dataToProcess = null;
				/*
					Programado para recibir acciones de notificaciones
				if (Services.Strings.hasValue(action) )
				{
					//get the data for this action.
					JSONArray arrayDataToProcess = data.optJSONArray("uia");
					dataToProcess = getActionJsonObject(action, arrayDataToProcess);
				}
				*/
			if (dataToProcess==null)
			{
				dataToProcess = gxData.optJSONObject("da");
			}

			String action = null;
			String parameters = null;
			if (dataToProcess != null) {
				// action is now event , e in OneSignal
				if (dataToProcess.has("e"))
					action = dataToProcess.optString("e");
				// parameters are p in OneSignal
				if (dataToProcess.has("p"))
					parameters = dataToProcess.optString("p");
			}

			Intent intent = IntentFactory.createNotificationActionIntent(context, action, parameters, null,true);
			context.startActivity(intent);
		}
		catch (JSONException e) {
			//some exception handler code.
		}
	}
	/*
	private @Nullable JSONObject getActionJsonObject(String action, JSONArray arrayDataToProcess)
	{
		JSONObject dataToProcess = null;
		for (int i = 0; i < arrayDataToProcess.length(); i++) {
			dataToProcess = arrayDataToProcess.optJSONObject(i);
			String actionId = dataToProcess.optString("id");
			if (action.equalsIgnoreCase(actionId))
			{
				break;
			}
			dataToProcess = null;
		}
		return dataToProcess;
	}
	*/

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Services.Log.info(JPushUtil.LOGCAT_TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Services.Log.error(JPushUtil.LOGCAT_TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		/*
		if (Main.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
		*/
	}
}

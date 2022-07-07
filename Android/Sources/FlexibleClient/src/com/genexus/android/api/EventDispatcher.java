package com.genexus.android.api;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.expressions.ExpressionValueBridge;
import com.genexus.android.core.fragments.LayoutFragment;

public class EventDispatcher
{
	public static final String ACTION_NAME = "__GxAction";

	public static void fireEvent(Context context, String gxObjectName, String method, List<Value> parameters)
	{
		Intent intent = new Intent(LayoutFragment.GENEXUS_EVENTS);
		intent.putExtra(ACTION_NAME, gxObjectName + "." + method);

		for (int i = 0; i < parameters.size() ; i++)
			intent.putExtra(String.valueOf(i), ExpressionValueBridge.convertValueToEntityFormat(parameters.get(i), null).toString());

		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
}

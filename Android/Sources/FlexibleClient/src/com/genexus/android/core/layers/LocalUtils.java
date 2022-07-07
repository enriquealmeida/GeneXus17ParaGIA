package com.genexus.android.core.layers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sqldroid.SQLDroidDriver;

import com.genexus.android.core.base.application.MessageLevel;
import com.genexus.android.core.base.application.OutputMessage;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.DataItemHelper;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.artech.base.services.IPropertiesObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.Application;
import com.genexus.ClientContext;
import com.genexus.internet.MsgList;

public class LocalUtils
{
	private static final String SDT_MESSAGES = "GeneXus.Common.Messages";
	private static final String SDT_MESSAGE_LEVEL = "Message";
	private static final String SDT_MESSAGES_LEVEL = "Messages";
	private static final String BC_PREFERENCES_PREFIX = "BCMessages_";
	private static final String SDT_MESSAGE_LEVEL_TYPE = "Type";
	private static final String SDT_MESSAGE_LEVEL_DESCRIPTION = "Description";

	public static OutputResult outputNoImplementation(String objectName)
	{
		return OutputResult.error(messageNoImplementation(objectName));
	}

	public static String messageNoImplementation(String objectName)
	{
		return String.format("An implementation for object '%s' was not found in package.", objectName);
	}

	public static OutputResult translateOutput(boolean success, MsgList gxMessages)
	{
		ArrayList<OutputMessage> messages = new ArrayList<>();
		for (int i = 1; i <= gxMessages.size(); i++)
		{
			if (success && i==1)
				continue;
			String text = gxMessages.getItemText(i);
			MessageLevel level = CommonUtils.translateMessageLevel(gxMessages.getItemType(i));
			messages.add(new OutputMessage(level, text));
		}

		if (!success && messages.size() == 0)
			messages.add(new OutputMessage(MessageLevel.ERROR, "Unknown error"));

		return new OutputResult(messages);
	}

	public static void saveBCMessages(List<OutputMessage> messageList, String bcVariable) {
		String variableName = DataItemHelper.getNormalizedName(bcVariable);
		try {
			JSONObject globalObject = new JSONObject();
			JSONArray messagesJson = new JSONArray();
			for (OutputMessage message : messageList) {
				JSONObject object = new JSONObject();
				object.put(SDT_MESSAGE_LEVEL_TYPE, message.getLevel().getValue());
				object.put(SDT_MESSAGE_LEVEL_DESCRIPTION, message.getText());
				messagesJson.put(object);
			}
			globalObject.put(SDT_MESSAGES_LEVEL, messagesJson);
			String key = BC_PREFERENCES_PREFIX + variableName;
			String value = globalObject.toString();
			Services.Preferences.getAppSharedPreferences(BC_PREFERENCES_PREFIX)
					.edit().putString(key, value).apply();

		} catch (JSONException e) {
			Services.Log.error(e);
		}
	}

	public static EntityList retrieveBCMessages(String bcVariable) {
		String variableName = DataItemHelper.getNormalizedName(bcVariable);
		String key = BC_PREFERENCES_PREFIX + variableName;
		String value = Services.Preferences.getAppSharedPreferences(BC_PREFERENCES_PREFIX).getString(key, null);
		if (!Strings.hasValue(value))
			return null;

		EntityList entityList = new EntityList();
		try {
			JSONObject jsonObject = new JSONObject(value);
			JSONArray array = jsonObject.optJSONArray(SDT_MESSAGES_LEVEL);
			for (int i = 0; i < array.length(); i++) {
				JSONObject messageObject = array.getJSONObject(i);
				Entity entity = EntityFactory.newSdt(SDT_MESSAGES, SDT_MESSAGE_LEVEL);
				entity.setProperty(SDT_MESSAGE_LEVEL_TYPE, messageObject.optInt(SDT_MESSAGE_LEVEL_TYPE));
				entity.setProperty(SDT_MESSAGE_LEVEL_DESCRIPTION, messageObject.optString(SDT_MESSAGE_LEVEL_DESCRIPTION));
				entityList.add(entity);
			}
		} catch (JSONException e) {
			Services.Log.error(e);
		}
		return entityList;
	}

	public static Object toParameter(Object value)
	{
		if (value == null)
			return null;

		// Entity -> IPropertiesObject.
		if (value instanceof Entity)
			return value;

		// EntityList -> List<IPropertiesObject>
		if (value instanceof EntityList)
			return new ArrayList<IPropertiesObject>((EntityList)value);

		// TODO: Missing simple collections!
		// All other values -> String.
		return value.toString();
	}
	
	
	public static void beginTransaction()
	{
		try {
		if (SQLDroidDriver.getCurrentConnection()!=null &&
				!SQLDroidDriver.getCurrentConnection().getAutoCommit())
			{
				SQLDroidDriver.getCurrentConnection().onlyBeginTransaction();
			}	
		} catch (SQLException e) {
				Services.Log.error("Sqlexception " + e.getErrorCode() + " " + e.getMessage() + " " + e.toString());

				//MsgList mList = new MsgList();
				//mList.addItem("SQL Exception ." + e.getMessage(), 1, "");
				//return LocalUtils.translateOutput(false, mList);
			}
	}
	
	public static void endTransaction()
	{
		try {
			if (SQLDroidDriver.getCurrentConnection()!=null &&
					!SQLDroidDriver.getCurrentConnection().getAutoCommit())
			{
			
				SQLDroidDriver.getCurrentConnection().onlyEndTransaction();
			}
		} catch (SQLException e) {
			Services.Log.error("Sqlexception " + e.getErrorCode() + " " + e.getMessage() + " " + e.toString());

			//MsgList mList = new MsgList();
			//mList.addItem("SQL Exception ." + e.getMessage(), 1, "");
			//return LocalUtils.translateOutput(false, mList);
		} catch (IllegalStateException  e) {
			Services.Log.error("IllegalStateException " + e.getMessage() + " " + e.toString());
		}
		
	}

	@SuppressWarnings("deprecation")
	public static void commit()
	{
		Application.commit(ClientContext.getModelContext(), Services.Application.get().getRemoteHandle(), "DEFAULT");
	}
	
}

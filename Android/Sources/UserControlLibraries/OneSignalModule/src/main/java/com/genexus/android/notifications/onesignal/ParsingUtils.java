package com.genexus.android.notifications.onesignal;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParsingUtils {
	public static String parseAction(JSONObject data) {
		return data.has("e") ? data.optString("e") : null;
	}

	public static String parseParameters(JSONObject data) {
		return data.has("p") ? data.optString("p") : null;
	}

	public static String parseExecutionTime(JSONObject data) {
		return data.has("et") ? data.optString("et") : null;
	}

	public static JSONObject parseDefaultAction(JSONObject data) {
		return data.has("da") ? data.optJSONObject("da") : null;
	}

	public static JSONObject getActionJsonObject(String action, JSONArray arrayDataToProcess) {
		JSONObject dataToProcess = null;
		if (arrayDataToProcess != null) {
			for (int i = 0; i < arrayDataToProcess.length(); i++) {
				dataToProcess = arrayDataToProcess.optJSONObject(i);
				String actionId = dataToProcess.optString("id");
				if (action.equalsIgnoreCase(actionId)) {
					break;
				}
				dataToProcess = null;
			}
		}
		return dataToProcess;
	}
}

package com.genexus.android.serialization;

import org.json.JSONException;
import org.json.JSONObject;

public interface ISerializer<T>
{
	JSONObject serialize(T data) throws JSONException;
	T deserialize(JSONObject json) throws JSONException;
}

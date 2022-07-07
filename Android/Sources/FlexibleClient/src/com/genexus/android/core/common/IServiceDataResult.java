package com.genexus.android.core.common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public interface  IServiceDataResult
{

	// Status
	public boolean isOk();
	public boolean isUpToDate();
	public int getErrorType();
	public String getErrorMessage();

	// Results
	public JSONArray getData();
	public Date getLastModified();

	public Iterable<JSONObject> getDataObjects();


}

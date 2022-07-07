package com.genexus.android.core.common.okhttp;

import android.util.Pair;

import com.genexus.android.R;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.common.HttpHeaders;
import com.genexus.android.core.common.IServiceDataResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class ServiceDataResult implements IServiceDataResult
{
	private JSONArray mData;
	private int mStatusCode;
	private Date mLastModified;

	private int mErrorType;
	private String mErrorMessage;

	private static final String HEADER_LAST_MODIFIED = "Last-Modified";

	private ServiceDataResult()
	{
		mData = new JSONArray();
		mLastModified = new Date(0);
		mErrorType = DataRequest.ERROR_NONE;
	}

	public ServiceDataResult(Request get, Response response, boolean isCollection)
	{
		this();
		mStatusCode = response.code();

		if (mStatusCode == HttpURLConnection.HTTP_NOT_FOUND || mStatusCode >= HttpURLConnection.HTTP_INTERNAL_ERROR)
		{
			// Don't even read JSON data in this case.
			String httpError = String.valueOf(mStatusCode) + " - " + response.message();
			setAppServerError(DataRequest.ERROR_SERVER, httpError);
			// response has autoclose after use it in try with resources
			return;
		}

		if (mStatusCode == HttpURLConnection.HTTP_UNAUTHORIZED
				|| mStatusCode == HttpURLConnection.HTTP_FORBIDDEN
				|| mStatusCode == HttpURLConnection.HTTP_ACCEPTED) {
			// Not authenticated or authorized, see body for details.
			Pair<Integer, String> error = ServiceErrorParser.parse(get, response);
			setError(error.first, error.second);
			return;
		}

		// On 304 no data is returned.
		if (mStatusCode != HttpURLConnection.HTTP_NOT_MODIFIED)
		{
			// Read JSON data.
			readEntity(response, isCollection);
		}

		// Read last modified date, if present.
		String lastModified1 = response.header(HEADER_LAST_MODIFIED);
		if (lastModified1 != null && lastModified1.length() != 0)
			mLastModified = HttpHeaders.dateFromHttpFormat(lastModified1);
	}

	public static ServiceDataResult error(int errorType, String errorMessage)
	{
		ServiceDataResult result = new ServiceDataResult();
		result.setError(errorType, errorMessage);
		return result;
	}

	public static ServiceDataResult networkError(IOException exception)
	{
		return error(DataRequest.ERROR_NETWORK, Services.HttpService.getNetworkErrorMessage(exception));
	}

	// Status
	@Override
	public boolean isOk() { return (mErrorType == DataRequest.ERROR_NONE); }
	@Override
	public boolean isUpToDate() { return mStatusCode == HttpURLConnection.HTTP_NOT_MODIFIED; }
	@Override
	public int getErrorType() { return mErrorType; }
	@Override
	public String getErrorMessage() { return mErrorMessage; }

	// Results
	@Override
	public JSONArray getData() { return mData; }
	@Override
	public Date getLastModified() { return mLastModified; }

	@Override
	public Iterable<JSONObject> getDataObjects()
	{
		List<JSONObject> objects = new ArrayList<>();
		int count = mData.length();
		for (int i = 0; i < count; i++)
		{
			try
			{
				objects.add(mData.getJSONObject(i));
			}
			catch (JSONException e)
			{
			}
		}

		return objects;
	}

	private void setAppServerError(int errorType, String errorDetail)
	{
		String errorMessage = Services.Strings.getResource(R.string.GXM_ApplicationServerError, errorDetail);
		setError(errorType, errorMessage);
	}

	private void setError(int errorType, String errorMessage)
	{
		mErrorType = errorType;
		mErrorMessage = errorMessage;
	}

	private boolean readEntity(Response response, boolean isCollection)
	{
		try
		{
			// Read result from HTTP.
			String result = response.body().string();
			//String result = EntityUtils.toString(entity, HTTP.UTF_8);

			// Parse JSON result.
 			return readJson(result, isCollection);
		}
		catch (IOException ex)
		{
			Services.Log.error("readEntity", ex);
			setAppServerError(DataRequest.ERROR_DATA, ex.getClass().getName());
			return false;
		}
	}

	private boolean readJson(String str, boolean isCollection)
	{
		try
		{
			// Try to read as an array.
			mData = new JSONArray(str);
			return true;
		}
		catch (JSONException notAnArray)
		{
			// If that fails, read as an object (may also fail if it's not valid JSON at all).
			try
			{
				JSONObject jsonObject = new JSONObject(str);
				if (isCollection)
				{
					if (jsonObject.names() != null && jsonObject.names().length() > 0)
					{
						String elementName = jsonObject.names().getString(0);

						JSONArray jsonArray = jsonObject.optJSONArray(elementName);
						if (jsonArray!=null)
						{
							mData = jsonArray;
							return true;
						}

						// Try to read as list made of single element.
						JSONObject jsonObjArray = jsonObject.optJSONObject(elementName);
						if (jsonObjArray != null)
						{
							mData.put(jsonObjArray);
							return true;
						}
					}
				}

				mData.put(jsonObject);
				return true;
			}
			catch (JSONException notJson)
			{
				Services.Log.error("readJson", notJson);
				setError(DataRequest.ERROR_DATA, notJson.getMessage());
				return false;
			}
		}
	}

	public static String parseRedirectOnHeader(Response response)
	{
		String header = response.header("Location");
		String newUrl = Strings.EMPTY;
		if (header != null && Strings.hasValue(header))
			newUrl = header;

		return newUrl;
	}
}

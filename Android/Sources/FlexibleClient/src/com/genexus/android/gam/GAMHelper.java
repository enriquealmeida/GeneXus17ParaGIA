package com.genexus.android.gam;

import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.core.base.services.ClientStorage;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class GAMHelper
{
	private static final String USER_DATA_KEY = "gam_user_information";

	private final ClientStorage mStorage;

	public GAMHelper(ClientStorage storage)
	{
		mStorage = storage;
	}

	public void afterLogin(JSONObject jsonUserData)
	{
		if (jsonUserData != null)
		{
			// Save for later use.
			mStorage.putStringSecure(USER_DATA_KEY, jsonUserData.toString());

			// Store a GAMUser object for later querying.
			GAMUser user = new GAMUser(jsonUserData);
			GAMUser.setCurrentUser(user);
		}
	}

	public void afterLogin(String userId, boolean isAnonymous)
	{
		try
		{
			// Create fake jsonUserData with partial information.
			JSONObject jsonUserData = new JSONObject();
			jsonUserData.put(GAMUser.FIELD_USER_ID, userId);
			jsonUserData.put(GAMUser.FIELD_USER_NAME, "Unknown");
			jsonUserData.put(GAMUser.FIELD_USER_IS_ANONYMOUS, isAnonymous);

			afterLogin(jsonUserData);

		}
		catch (JSONException e)
		{
			Services.Log.warning("GAMHelper Error", e);
		}
	}

	public void restoreUserData()
	{
		String strUserData = mStorage.getString(USER_DATA_KEY, null);
		if (Strings.hasValue(strUserData))
		{
			// Load the GAMUser with the last stored data.
			try
			{
				JSONObject jsonUserData = new JSONObject(strUserData);
				GAMUser user = new GAMUser(jsonUserData);
				GAMUser.setCurrentUser(user);
			}
			catch (JSONException e)
			{
				Services.Log.warning("GAMHelper Error", e);
			}
		}
	}

	public void afterLogout()
	{
		// Clear current GAMUser.
		GAMUser.setCurrentUser(null);

		// Clear stored data.
		mStorage.remove(USER_DATA_KEY);
	}
}

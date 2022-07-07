package com.genexus.android.gam;

import java.util.Date;

import org.json.JSONObject;

import com.genexus.android.core.base.services.Services;

public class GAMCountry
{
	private final String mId;
	private final String mName;
	private final String mISO3;
	private final Date mDateCreated;
	private final String mUserCreated;
	private final Date mDateUpdated;
	private final String mUserUpdated;
	// TODO private final GAMLanguage[] mLanguages;

	private GAMCountry(JSONObject json)
	{
		mId = json.optString("Id");
		mName = json.optString("Name");
		mISO3 = json.optString("ISO_3");
		mDateCreated = Services.Strings.getDateTime(json.optString("DateCreated"));
		mUserCreated = json.optString("UserCreated");
		mDateUpdated = Services.Strings.getDateTime(json.optString("DateUpdated"));
		mUserUpdated = json.optString("UserUpdated");
	}

	static GAMCountry fromJson(JSONObject json)
	{
		return (json != null ? new GAMCountry(json) : null);
	}

	public String getId() { return mId; }
	public String getName() { return mName; }
	public String getISO3() { return mISO3; }
	public Date getDateCreated() { return mDateCreated; }
	public String getUserCreated() { return mUserCreated; }
	public Date getDateUpdated() { return mDateUpdated; }
	public String getUserUpdated() { return mUserUpdated; }
}

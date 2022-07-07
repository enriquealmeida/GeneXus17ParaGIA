package com.genexus.android.gam;

import java.util.Date;

import org.json.JSONObject;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

@SuppressWarnings("WeakerAccess")
public class GAMUser
{
	static final String FIELD_USER_ID = "GUID";
	static final String FIELD_USER_NAME = "Name";
	static final String FIELD_USER_IS_ANONYMOUS = "IsAutoRegisteredUser";

	private String mGUID;
	private String mNamespace;
	private String mAuthenticationTypeName;
	private String mName;
	private String mLogin;
	private String mEMail;
	private String mExternalId;
	private String mPassword;
	private String mFirstName;
	private String mLastName;
	private Date mBirthday;
	private String mGender;
	private String mURLImage;
	private String mURLProfile;
	private String mPhone;
	private String mAddress;
	private String mAddress2;
	private String mCity;
	private String mState;
	private String mPostCode;
	private GAMCountry mCountry;
	private String mLanguage;
	private String mTimeZone;
	private boolean mDontReceiveInformation;
	private boolean mIsBlocked;
	private Date mLastBlockedDate;
	private boolean mIsActive;
	private Date mActivationDate;
	private boolean mCannotChangePassword;
	private boolean mMustChangePassword;
	private boolean mPasswordNeverExpires;
	private Date mDateLastChangePassword;
	private int mSecurityPolicyId;
	private int mDefaultRoleId;
	private Date mDateLastAuthentication;
	private boolean mIsAutoRegisteredUser;
	private boolean mIsDeleted;
	private Date mDateCreated;
	private String mUserCreated;
	private Date mDateUpdated;
	private String mUserUpdated;
	private boolean mIsEnabledInRepository;
	// TODO: private final GAMUserAttribute[] mAttributes;

	private static GAMUser sCurrentUser;

	//Default constructor, need to call static method with instance object.
	public GAMUser()
	{
	}

	public GAMUser(JSONObject json)
	{
		mGUID = json.optString(FIELD_USER_ID);
		mNamespace = json.optString("NameSpace");
		mAuthenticationTypeName = json.optString("AuthenticationTypeName");
		mName = json.optString(FIELD_USER_NAME);
		mLogin = json.optString("Login");
		mEMail = json.optString("EMail");
		mExternalId = json.optString("ExternalId");
		mPassword = json.optString("Password");
		mFirstName = json.optString("FirstName");
		mLastName = json.optString("LastName");
		mBirthday = Services.Strings.getDate(json.optString("Birthday"));
		mGender = json.optString("Gender");
		mURLImage = json.optString("URLImage");
		mURLProfile = json.optString("URLProfile");
		mPhone = json.optString("Phone");
		mAddress = json.optString("Address");
		mAddress2 = json.optString("Address2");
		mCity = json.optString("City");
		mState = json.optString("State");
		mPostCode = json.optString("PostCode");
		mCountry = GAMCountry.fromJson(json.optJSONObject("Country"));
		mLanguage = json.optString("Language");
		mTimeZone = json.optString("TimeZone");
		mDontReceiveInformation = json.optBoolean("DontReceiveInformation");
		mIsBlocked = json.optBoolean("IsBlocked");
		mLastBlockedDate = Services.Strings.getDateTime(json.optString("LastBlockedDate"));
		mIsActive = json.optBoolean("IsActive");
		mActivationDate = Services.Strings.getDateTime(json.optString("ActivationDate"));
		mCannotChangePassword = json.optBoolean("CannotChangePassword");
		mMustChangePassword = json.optBoolean("MustChangePassword");
		mPasswordNeverExpires = json.optBoolean("PasswordNeverExpires");
		mDateLastChangePassword = Services.Strings.getDateTime(json.optString("DateLastChangePassword"));
		mSecurityPolicyId = json.optInt("SecurityPolicyId");
		mDefaultRoleId = json.optInt("DefaultRoleId");
		mDateLastAuthentication = Services.Strings.getDateTime(json.optString("DateLastAuthentication"));
		mIsAutoRegisteredUser = json.optBoolean(FIELD_USER_IS_ANONYMOUS);
		mIsDeleted = json.optBoolean("IsDeleted");
		mDateCreated = Services.Strings.getDateTime(json.optString("DateCreated"));
		mUserCreated = json.optString("UserCreated");
		mDateUpdated = Services.Strings.getDateTime(json.optString("DateUpdated"));
		mUserUpdated = json.optString("UserUpdated");
		mIsEnabledInRepository = json.optBoolean("IsEnabledInRepository");
	}

	// Instance API methods.
	public String getGUID() { return mGUID; }
	public String getNamespace() { return mNamespace; }
	public String getAuthenticationTypeName() { return mAuthenticationTypeName; }
	public String getName() { return mName; }
	public String getLogin() { return mLogin; }
	public String getEMail() { return mEMail; }
	public String getExternalId() { return mExternalId; }
	public String getPassword() { return mPassword; }
	public String getFirstName() { return mFirstName; }
	public String getLastName() { return mLastName; }
	public Date getBirthday() { return mBirthday; }
	public String getGender() { return mGender; }
	public String getURLImage() { return mURLImage; }
	public String getURLProfile() { return mURLProfile; }
	public String getPhone() { return mPhone; }
	public String getAddress() { return mAddress; }
	public String getAddress2() { return mAddress2; }
	public String getCity() { return mCity; }
	public String getState() { return mState; }
	public String getPostCode() { return mPostCode; }
	public GAMCountry getCountry() { return mCountry; }
	public String getLanguage() { return mLanguage; }
	public String getTimeZone() { return mTimeZone; }
	public Boolean getDontReceiveInformation() { return mDontReceiveInformation; }
	public Boolean getIsBlocked() { return mIsBlocked; }
	public Date getLastBlockedDate() { return mLastBlockedDate; }
	public Boolean getIsActive() { return mIsActive; }
	public Date getActivationDate() { return mActivationDate; }
	public Boolean getCannotChangePassword() { return mCannotChangePassword; }
	public Boolean getMustChangePassword() { return mMustChangePassword; }
	public Boolean getPasswordNeverExpires() { return mPasswordNeverExpires; }
	public Date getDateLastChangePassword() { return mDateLastChangePassword; }
	public Integer getSecurityPolicyId() { return mSecurityPolicyId; }
	public Integer getDefaultRoleId() { return mDefaultRoleId; }
	public Date getDateLastAuthentication() { return mDateLastAuthentication; }
	public Boolean getIsAutoRegisteredUser() { return mIsAutoRegisteredUser; }
	public Boolean getIsDeleted() { return mIsDeleted; }
	public Date getDateCreated() { return mDateCreated; }
	public String getUserCreated() { return mUserCreated; }
	public Date getDateUpdated() { return mDateUpdated; }
	public String getUserUpdated() { return mUserUpdated; }
	public Boolean getIsEnabledInRepository() { return mIsEnabledInRepository; }

	public static GAMUser getCurrentUser() { return sCurrentUser; }
	static void setCurrentUser(GAMUser user) { sCurrentUser = user; }

	// Static API methods.

	public static String getCurrentUserId()
	{
		return (sCurrentUser != null ? sCurrentUser.getGUID() : Strings.EMPTY);
	}

	public static String getCurrentUserLogin()
	{
		return (sCurrentUser != null ? sCurrentUser.getLogin() : Strings.EMPTY);
	}

	public static String getCurrentUserName()
	{
		return (sCurrentUser != null ? sCurrentUser.getName() : Strings.EMPTY);
	}

	public static String getCurrentUserExternalId()
	{
		return (sCurrentUser != null ? sCurrentUser.getExternalId() : Strings.EMPTY);
	}

	public static String getCurrentUserEMail()
	{
		return (sCurrentUser != null ? sCurrentUser.getEMail() : Strings.EMPTY);
	}

	public static Boolean getCurrentUserIsAnonymous()
	{
		// IsAnonymous() returns TRUE when the user is not logged in yet.
		return (sCurrentUser != null ? sCurrentUser.getIsAutoRegisteredUser() : true);
	}
}

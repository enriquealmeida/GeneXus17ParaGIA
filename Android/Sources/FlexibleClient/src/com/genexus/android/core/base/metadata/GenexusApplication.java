package com.genexus.android.core.base.metadata;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.services.UrlBuilder;
import com.genexus.android.core.base.synchronization.SynchronizationHelper.DataSyncCriteria;
import com.genexus.android.core.base.synchronization.SynchronizationHelper.LocalChangesProcessing;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.superapps.MiniApp;

public class GenexusApplication
{
	private ApplicationDefinition mDefinition = new ApplicationDefinition();

	private String mName = "ApplicationName";
	private boolean mIsOfflineApplication = false;
	private boolean mUseInternalStorageForDatabase = true;
	private String mReorMD5Hash = "";
	private int mRemoteHandler = -1;
	private String mSynchronizerReceiveCustomProcedure = Strings.EMPTY;

	private String mBaseAPIUri;
	private String mAppEntry;

	private boolean mIsSecure = false;
	private int mServerType = UrlBuilder.NET_SERVER;
	private String mAppId = Strings.EMPTY;
	private String mClientId = Strings.EMPTY;
	private boolean mEnableAnonymousUser = false;
	private String mLoginObject = Strings.EMPTY;
	private String mNotAuthorizedObject = Strings.EMPTY;
	private String mChangePasswordObject = Strings.EMPTY;

	private boolean mAllowWebViewExecuteJavascripts = true;
	private boolean mAllowWebViewExecuteLocalFiles = true;
	private boolean mShareSessionToWebView = false;

	private boolean mUseDynamicUrl = false;
	private String mDynamicUrlAppId = Strings.EMPTY;
	private String mDynamicUrlPanel = Strings.EMPTY;
	private int mMajorVersion = -1;
	private int mMinorVersion = 0;
	private boolean mAutomaticUpdate = false;

	private IViewDefinition mMainView;
	private InstanceProperties mMainProperties;

	//remove default AdMob publisher id
	//private String mAdMobPublisherId = "a14ef0c02f2aea9";
	private String mAdMobPublisherId = "";

	private boolean mUseAds = false;

	private String mNotificationSenderId = Strings.EMPTY;
	private boolean mUseNotification = false;
	private String mNotificationRegistrationHandler = Strings.EMPTY;

	private boolean mUseLegacyClientStorage = false;

	private boolean mSlidePositionRight = false;
	private boolean mSlideHideIcon = false;

	// metadata load
	private boolean mShouldReadMetadataFromResources;

	private boolean mShouldReloadMetadata;
	// reload metadata
	public boolean getShouldReloadMetadata() { return mShouldReloadMetadata; }
	public void setShouldReloadMetadata(boolean value) { mShouldReloadMetadata = value; }

	public GenexusApplication() {
		this(true);
	}

	protected GenexusApplication(boolean shouldReadMetadataFromResources) {
		mShouldReadMetadataFromResources = shouldReadMetadataFromResources;
	}

	public String getName() { return mName; }
	public void setName(String value) { mName = value; }

	public ApplicationDefinition getDefinition()
	{
		return mDefinition;
	}

	public void resetDefinition()
	{
		mDefinition = new ApplicationDefinition();
	}

	public String getAppId() { return mAppId; }
	public void setAppId(String value) { mAppId = value; }

	public int getServerType() { return mServerType; }
	public void setServerType(int value) { mServerType = value; }

	//Dynamic Url
	public boolean getUseDynamicUrl() { return mUseDynamicUrl; }
	public void setUseDynamicUrl(boolean value) { mUseDynamicUrl = value; }
	public String getDynamicUrlAppId() { return mDynamicUrlAppId; }
	public void setDynamicUrlAppId(String value) { mDynamicUrlAppId = value; }
	public String getDynamicUrlPanel() { return mDynamicUrlPanel; }
	public void setDynamicUrlPanel(String value) { mDynamicUrlPanel = value; }
	public boolean hasCustomDynamicUrlPanel() { return mUseDynamicUrl && Strings.hasValue(mDynamicUrlPanel); }

	// Uri Maker
	@SuppressWarnings("checkstyle:MemberName")
	public UrlBuilder UriMaker;

	// Metadata
	public IViewDefinition getMain() { return mMainView; }

	public void setMain(IViewDefinition value)
	{
		mMainView = value;
		mMainProperties = null;
	}

	public InstanceProperties getMainProperties()
	{
		if (mMainView != null)
			return mMainView.getInstanceProperties();
		else
			return mMainProperties;
	}

	public void setMainProperties(InstanceProperties properties)
	{
		mMainProperties = properties;
	}

	public String getAPIUri() { return mBaseAPIUri; }
	public void setAPIUri(String value) { mBaseAPIUri = value; }

	public String getAppEntry() { return mAppEntry; }
	public void setAppEntry(String value) { mAppEntry = value; }

	//Security
	public boolean isSecure() { return mIsSecure; }
	public void setIsSecure(boolean value) { mIsSecure = value; }

	public String getClientId() { return mClientId; }
	public void setClientId(String value) { mClientId = value; }

	public boolean getEnableAnonymousUser() { return mEnableAnonymousUser; }
	public void setEnableAnonymousUser(boolean value) { mEnableAnonymousUser = value; }

	public void setLoginObject(String value)
	{
		mLoginObject = value;
	}

	public String getLoginObject()
	{
		if (Services.Strings.hasValue(mLoginObject))
			return mLoginObject;

		return "gamsdlogin";
	}

	public String getNotAuthorizedObject() { return mNotAuthorizedObject; }
	public void setNotAuthorizedObject(String value) { mNotAuthorizedObject = value; }

	public String getChangePasswordObject() { return mChangePasswordObject; }
	public void setChangePasswordObject(String value) { mChangePasswordObject = value; }

	public boolean getAllowWebViewExecuteJavascripts() { return mAllowWebViewExecuteJavascripts; }
	public void setAllowWebViewExecuteJavascripts(boolean value) { mAllowWebViewExecuteJavascripts = value; }

	public boolean getAllowWebViewExecuteLocalFiles() { return mAllowWebViewExecuteLocalFiles; }
	public void setAllowWebViewExecuteLocalFiles(boolean value) { mAllowWebViewExecuteLocalFiles = value; }

	public boolean getShareSessionToWebView() { return mShareSessionToWebView; }
	public void setShareSessionToWebView(boolean value) { mShareSessionToWebView = value; }

	/**
	 *
	 * @param objName Gx object name without extension and without parameters
	 * @return Full url path
	 */
	public String link(String objName)
	{
		return UriMaker.getLinkUrl(objName, mServerType, true);
	}

	/**
	 *
	 * @param objPartialUrl Gx object name with extension and possible with parameters
	 * @return Full url path
	 */
	public String linkObjectUrl(String objPartialUrl)
	{
		return UriMaker.getLinkUrl(objPartialUrl, mServerType, false);
	}

	// Update
	public int getMajorVersion() { return mMajorVersion; }
	public void setMajorVersion(int value) { mMajorVersion = value; }
	public int getMinorVersion() { return mMinorVersion; }
	public void setMinorVersion(int value) { mMinorVersion = value; }
	public boolean isAutomaticUpdate() { return mAutomaticUpdate; }
	public void setAutomaticUpdate(boolean value) { mAutomaticUpdate = value; }

	public boolean shouldReadMetadataFromResources() {
		return mShouldReadMetadataFromResources;
	}

	public void setShouldReadMetadataFromResources(boolean shouldReadMetadataFromResources) {
		mShouldReadMetadataFromResources = shouldReadMetadataFromResources;
	}

	// Ads
	public String getAdMobPublisherId() { return mAdMobPublisherId; }
	public void setAdMobPublisherId(String publisherId)
	{
		if (publisherId != null && publisherId.length() != 0)
			mAdMobPublisherId = publisherId;
	}
	public void setUseAds(boolean value) { mUseAds = value; }
	public boolean getUseAds() { return mUseAds; }

	// Notifications
	public boolean getUseNotification() { return mUseNotification; }
	public void setUseNotification(boolean value) { mUseNotification = value; }
	public String getNotificationSenderId() { return mNotificationSenderId; }
	public void setNotificationSenderId(String value) { mNotificationSenderId = value; }
	public String getNotificationRegistrationHandler() { return mNotificationRegistrationHandler; }
	public void setNotificationRegistrationHandler(String value) { mNotificationRegistrationHandler = value; }

	// Client Storage
	public boolean getUseLegacyClientStorage() { return mUseLegacyClientStorage; }
	public void setUseLegacyClientStorage(boolean value) { mUseLegacyClientStorage = value; }

	// Offline applications.
	public boolean isOfflineApplication() { return mIsOfflineApplication; }
	public void setIsOfflineApplication(boolean value) { mIsOfflineApplication = value; }

	public boolean getUseInternalStorageForDatabase() { return mUseInternalStorageForDatabase; }
	public void setUseInternalStorageForDatabase(boolean value) { mUseInternalStorageForDatabase = value; }

	//reor md5
	public String getReorMD5Hash() { return mReorMD5Hash; }
	public void setReorMD5Hash(String value) { mReorMD5Hash = value; }

	// Sync Send
	public boolean getSynchronizerSendAutomatic()
	{
		return getSendLocalChangesProcessing() == LocalChangesProcessing.WhenConnected;
	}

	// Sync Save pending events
	public boolean getSynchronizerSavePendingEvents()
	{
		return getSendLocalChangesProcessing() != LocalChangesProcessing.Never;
	}

	// Sync Receive After elapsed time
	public boolean getSynchronizerReceiveAfterElapsedTime()
	{
		return getSynchronizerDataSyncCriteria() == DataSyncCriteria.AfterElapsedTime;
	}
	// Sync Receive custom proc

	public String getSynchronizerReceiveCustomProcedure() { return mSynchronizerReceiveCustomProcedure; }

	public void setSynchronizerReceiveCustomProcedure(String value) { mSynchronizerReceiveCustomProcedure = value; }

	public boolean getRunSynchronizerAtStartup()
	{
		return getSynchronizerDataSyncCriteria() == DataSyncCriteria.Automatic;
		//return mRunSynchronizerAtStartup;
	}

	//public void setRunSynchronizerAtStartup(boolean value) { mRunSynchronizerAtStartup = value; }

	// Property get value from mMainProperties if metadata is not loaded yet
	public String getSynchronizer()
	{
		if (mMainView != null)
		{
			if (mMainView instanceof IDataViewDefinition)
				return ((IDataViewDefinition) mMainView).getPattern().getInstanceProperties().getSynchronizer();
			else if (mMainView instanceof DashboardMetadata)
				return mMainView.getInstanceProperties().getSynchronizer();
		}
		else if (mMainProperties!=null)
		{
			return mMainProperties.getSynchronizer();
		}
		return null;
	}

	// Property get value from mMainProperties if metadata is not loaded yet
	public DataSyncCriteria getSynchronizerDataSyncCriteria()
	{
		if (mMainView != null)
		{
			if (mMainView instanceof IDataViewDefinition)
				return ((IDataViewDefinition) mMainView).getPattern().getInstanceProperties().getSynchronizerDataSyncCriteria();
			else if (mMainView instanceof DashboardMetadata)
				return mMainView.getInstanceProperties().getSynchronizerDataSyncCriteria();
		}
		else if (mMainProperties!=null)
		{
			return mMainProperties.getSynchronizerDataSyncCriteria();
		}
		return DataSyncCriteria.Manual;
	}

	// Property get value from mMainProperties if metadata is not loaded yet
	public long getSynchronizerMinTimeBetweenSync()
	{
		if (mMainView != null)
		{
			if (mMainView instanceof IDataViewDefinition)
				return ((IDataViewDefinition) mMainView).getPattern().getInstanceProperties().getSynchronizerMinTimeBetweenSync();
			else if (mMainView instanceof DashboardMetadata)
				return mMainView.getInstanceProperties().getSynchronizerMinTimeBetweenSync();
		}
		else if (mMainProperties!=null)
		{
			return mMainProperties.getSynchronizerMinTimeBetweenSync();
		}
		return 0;
	}

	// Property need full metadata load to work ok
	public LocalChangesProcessing getSendLocalChangesProcessing()
	{
		if (mMainView != null)
		{
			if (mMainView instanceof IDataViewDefinition)
				return ((IDataViewDefinition) mMainView).getPattern().getInstanceProperties().getSendLocalChangesProcessing();
			else if (mMainView instanceof DashboardMetadata)
				return mMainView.getInstanceProperties().getSendLocalChangesProcessing();
		}
		return LocalChangesProcessing.UserDefined;
	}

	// Property need full metadata load to work ok
	public long getSynchronizerMinTimeBetweenSends()
	{
		if (mMainView != null)
		{
			if (mMainView instanceof IDataViewDefinition)
				return ((IDataViewDefinition) mMainView).getPattern().getInstanceProperties().getSynchronizerMinTimeBetweenSends();
			else if (mMainView instanceof DashboardMetadata)
				return mMainView.getInstanceProperties().getSynchronizerMinTimeBetweenSends();
		}
		return 0;
	}

	// Property need full metadata load to work ok
	// in secs
	public long getSynchronizerTimeoutReceive()
	{
		if (mMainView != null)
		{
			if (mMainView instanceof IDataViewDefinition)
				return ((IDataViewDefinition)mMainView).getPattern().getInstanceProperties().getSynchronizerTimeoutReceive();
			else if (mMainView instanceof DashboardMetadata)
				return mMainView.getInstanceProperties().getSynchronizerTimeoutReceive();
		}
		return 0;
	}

	// Property need full metadata load to work ok
	// in secs
	public long getSynchronizerTimeoutSend()
	{
		if (mMainView != null)
		{
			if (mMainView instanceof IDataViewDefinition)
				return ((IDataViewDefinition) mMainView).getPattern().getInstanceProperties().getSynchronizerTimeoutSend();
			else if (mMainView instanceof DashboardMetadata)
				return mMainView.getInstanceProperties().getSynchronizerTimeoutSend();
		}
		return 0;
	}

	public int getRemoteHandle() { return mRemoteHandler; }

	public void setRemoteHandle(int value) { mRemoteHandler = value; }

	//temp property Slide Navigation
	public boolean getSlidePositionRight() { return mSlidePositionRight; }
	public void setSlidePositionRight(boolean value) { mSlidePositionRight = value; }

	public boolean getSlideHideIcon() { return mSlideHideIcon; }
	public void setSlideHideIcon(boolean value) { mSlideHideIcon = value; }

	public boolean isMiniApp() { return this instanceof MiniApp; }
}

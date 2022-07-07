package com.genexus.android.core.base.metadata.loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.webkit.URLUtil;

import com.genexus.android.R;
import com.artech.base.services.IContext;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.ContextImpl;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.InstanceProperties;
import com.genexus.android.core.base.metadata.loader.steps.DeepLinksLoadStep;
import com.genexus.android.core.base.metadata.loader.steps.DomainsLoadStep;
import com.genexus.android.core.base.metadata.loader.steps.EntitiesLoadStep;
import com.genexus.android.core.base.metadata.loader.steps.PatternSettingsLoadStep;
import com.genexus.android.core.base.metadata.loader.steps.ProceduresLoadStep;
import com.genexus.android.core.base.metadata.loader.steps.ResourcesLoadStep;
import com.genexus.android.core.base.metadata.loader.steps.SDTsLoadStep;
import com.genexus.android.core.base.metadata.loader.steps.ThemesLoadStep;
import com.genexus.android.gam.GAMUser;
import com.genexus.android.remoteconfig.RemoteConfig;
import com.genexus.android.remotenotification.RemoteNotification;
import com.genexus.android.core.application.ApplicationStorageHelper;
import com.genexus.android.core.base.metadata.loader.steps.AttributesLoadStep;
import com.genexus.android.core.base.metadata.loader.steps.PatternInstancesLoadStep;
import com.genexus.android.core.base.metadata.loader.steps.ServerInfoLoadStep;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.services.UrlBuilder;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.PreferencesHelper;
import com.genexus.android.core.common.SecurityHelper;

import org.sqldroid.SQLDroidHelper;

import java.util.Arrays;
import java.util.List;

public class ApplicationLoader {

	private final GenexusApplication mGenexusApplication;
	private Context mAppContext;
	private IContext mIContext;
	private ApplicationLoader.Listener mMetadataListener;
	private boolean mNeedCheckRemoteMetadataInBackground = false;
	private SharedPreferences mSharedPreferences;

	public interface Listener {
		void onMetadataDownloadStarted();
	}

	public ApplicationLoader(GenexusApplication genexusApplication) {
		mGenexusApplication = genexusApplication;
	}

	protected GenexusApplication getApplication() {
		return mGenexusApplication;
	}

	protected Context getAppContext() {
		return mAppContext;
	}

	protected IContext getIContext() {
		return mIContext;
	}

	protected SharedPreferences getSharedPreferences() {
		return mSharedPreferences;
	}

	/**
	 * Reads the minimum metadata necessary to make startup decisions.
	 */
	public void preloadApplication(Context context) {
		new PatternSettingsLoadStep(context, false, mGenexusApplication).load();
		if (loadMainObjectProperties(context))
			initLogging(mGenexusApplication);
	}

	public LoadResult loadApplication(IContext context,
									  Context appContext,
									  SyncManager.Listener syncListener,
									  ApplicationLoader.Listener metadataListener) {
		mIContext = context;
		mAppContext = appContext;
		mMetadataListener = metadataListener;
		mSharedPreferences = mAppContext.getSharedPreferences(MetadataLoader.getPrefsName(mGenexusApplication), Context.MODE_PRIVATE);
		String serverUrl = mGenexusApplication.getAPIUri();
		if (serverUrl != null && serverUrl.endsWith("/"))
			serverUrl = serverUrl.substring(0, serverUrl.length() - 1);

		//If a custom configuration panel is used, the application needs to be loaded anyway from resources
		//in order to be able to actually build the panel layout and logic
		boolean shouldExecuteCustomConfigPanel = false;
		if (!URLUtil.isNetworkUrl(serverUrl)) {
			if (!mGenexusApplication.hasCustomDynamicUrlPanel())
				return LoadResult.result(LoadResult.RESULT_INVALID_APP_URL);
			else
				shouldExecuteCustomConfigPanel = true;
		}

		mGenexusApplication.UriMaker = new UrlBuilder(serverUrl);

		// Start loading metadata
		Services.Application.getDefinition().setLoadingMetadata(true);

		if (!shouldExecuteCustomConfigPanel) {
			LoadResult processMetadataResult = processMetadata();
			if (processMetadataResult.getCode() == LoadResult.RESULT_UPDATE)
				return processMetadataResult;
		}

		// Load all metadata files.
		loadMetadata(appContext);

		Services.Application.getDefinition().setLoaded(true);
		// loading metadata ready
		Services.Application.getDefinition().setLoadingMetadata(false);

		if (shouldExecuteCustomConfigPanel)
			return LoadResult.result(LoadResult.RESULT_INVALID_APP_URL);

		// Post-processing.
		initializeMain();

		// Update logging setup, since its properties may have changed after updating metadata.
		initLogging(mGenexusApplication);

		// prepare SqlDroid Driver.
		SQLDroidHelper.initialize(appContext, new ApplicationStorageHelper(appContext));

		if (mGenexusApplication.isOfflineApplication()) {
			Services.Log.debug("Is OfflineApplication");
			// Create sync database before registering for notification
			if (!SyncManager.createSyncDatabase(mGenexusApplication)) {
				return LoadResult.error(new Exception("Database creation failed: could not find Reorganization programs."));
			}
		}

		// Load current user data from last session. This must be done
		// 1) before registerForNotifications() since the procedure may need authentication, and
		// 2) before synchronizing data, since it's common to use the GAMUser object there.
		SecurityHelper.restoreLoginInformation();

		registerForNotification(appContext, mGenexusApplication);

		if (!mGenexusApplication.isMiniApp())
			logDeviceInfo();

		if (syncListener != null)
			SyncManager.syncData(mGenexusApplication, syncListener);

		// Always acquire anonymous user on start. Fail if not connected.
		if (!Strings.hasValue(GAMUser.getCurrentUserId())
				&& mGenexusApplication.isSecure()
				&& mGenexusApplication.getEnableAnonymousUser()
				&& !SecurityHelper.tryAutomaticLogin()) {
			String errorMessage = Services.Strings.getResource(R.string.GXM_NetworkError, "connection failed");
			return LoadResult.error(new Exception(errorMessage));
		}

		if (mGenexusApplication.getUseDynamicUrl()
				&& (!mGenexusApplication.isOfflineApplication() || Services.HttpService.isOnline())
				&& !Services.HttpService.checkApplicationUri(mGenexusApplication.getName(), mGenexusApplication.getAPIUri())) {
			return LoadResult.result(LoadResult.RESULT_INVALID_APP_URL);
		}

		if (mNeedCheckRemoteMetadataInBackground)
			checkMetadataInBackground();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
			initializeApplicationShortcuts();

		setNotificationsPanel();

		//This is done here because props haven't been loaded yet when RemoteConfig's GenexusModule is initialized
		initializeRemoteConfig(mGenexusApplication.getMainProperties());

		return LoadResult.result(LoadResult.RESULT_OK);
	}

	private void initLogging(GenexusApplication application) {
		if (!application.isMiniApp())
			Services.Log.initialize(application.getMainProperties());
	}

	protected LoadResult processMetadata() {
		// Load metadata with current data in raw/zip.
		boolean shouldRunSync = SyncManager.isShouldRunSync(mGenexusApplication);
		boolean hasSyncAtStartup = SyncManager.hasSyncAtStartup(mGenexusApplication);

		boolean checkRemoteMetadataNow = !MetadataLoader.hasAppIdInRaw(mAppContext)
				|| (mGenexusApplication.isOfflineApplication() && hasSyncAtStartup && shouldRunSync)
				|| mGenexusApplication.getShouldReloadMetadata();

		// check metadata in app Startup only when necessary
		// do it in background instead to do not delay app startup.
		mNeedCheckRemoteMetadataInBackground = false;

		// get last minor downloaded.
		// If there is a download version, don't use the one from resources
		long currentDownloadedMetadataVersion = mSharedPreferences.getLong("API_VERSION", 0);
		if (currentDownloadedMetadataVersion != 0)
			mGenexusApplication.setShouldReadMetadataFromResources(false);

		if (!checkRemoteMetadataNow)
			mNeedCheckRemoteMetadataInBackground = true;
		else {
			RemoteApplicationInfo remoteAppInfo = Services.HttpService.getRemoteApplicationInfo();

			// Don't attempt to update the app if we can't retrieve this info from the remote server
			if (remoteAppInfo != null) {
				if (mGenexusApplication.getMajorVersion() < remoteAppInfo.getMajorVersion()) {
					// loading metadata failed
					Services.Application.getDefinition().setLoadingMetadata(false);
					return LoadResult.result(LoadResult.RESULT_UPDATE, remoteAppInfo.getAppStoreUrl());
				}

				String prefKey = MetadataLoader.getPrefsName(mGenexusApplication) + mGenexusApplication.getMajorVersion() + "-MinorVersion";
				int currentMinorVersion = (int) mIContext.getMinorVersion(prefKey, mGenexusApplication.getMinorVersion());

				// Download metadata from server if either:
				// - Current app minor version is lower than the remote
				// - Metadata is not embedded inside the app's resources
				if (currentMinorVersion < remoteAppInfo.getMinorVersion() || !MetadataLoader.hasAppIdInRaw(mAppContext)) {
					long remoteMetadataVersion = Services.HttpService.getRemoteMetadataVersion(currentDownloadedMetadataVersion);

					// Don't download the metadata if we already have the latest version downloaded
					if (currentDownloadedMetadataVersion != remoteMetadataVersion) {
						// add update app message , resolve with listener and snackbar.
						if (mMetadataListener != null)
							mMetadataListener.onMetadataDownloadStarted();

						Services.HttpService.downloadAndExtractMetadata(mAppContext);

						SharedPreferences.Editor editor = mSharedPreferences.edit();
						editor.putLong("API_VERSION", remoteMetadataVersion);
						editor.putString("DOWNLOADED_ZIP_VERSION", remoteAppInfo.getMajorVersion() + "." + remoteAppInfo.getMinorVersion());
						editor.apply();

						// save the new minor version, for this current mayor
						mIContext.saveMinorVersion(prefKey, remoteAppInfo.getMinorVersion());

						// now read metadata from file system and not from raw/
						mGenexusApplication.setShouldReadMetadataFromResources(false);
						mGenexusApplication.setShouldReloadMetadata(false);
					}
				}
			}
		}

		return LoadResult.result(LoadResult.RESULT_OK);
	}

	private void initializeApplicationShortcuts() {
		String shortcutsMenu = mGenexusApplication.getMainProperties().getShortcutsMenu();
		if (shortcutsMenu == null)
			return;

		IViewDefinition menuDefinition = mGenexusApplication.getDefinition().getView(shortcutsMenu);
		if (!(menuDefinition instanceof DashboardMetadata))
			return;

		Services.Shortcuts.addShortcuts((DashboardMetadata) menuDefinition);
	}

	private void setNotificationsPanel() {
		String notificationsPanel = mGenexusApplication.getMainProperties().getNotificationsPanel();
		if (Strings.hasValue(notificationsPanel))
			Services.Preferences.setString(PreferencesHelper.PREFERENCES_NOTIFICATIONS_PANEL, notificationsPanel);
	}

	private void initializeRemoteConfig(InstanceProperties properties) {
		if (RemoteConfig.Companion.getProvider() != null)
			RemoteConfig.Companion.initialize(properties);
	}

	public void checkMetadataInBackground(Context context)
	{
		mAppContext = context;
		checkMetadataInBackground();
	}

	//check metadata delayed
	private void checkMetadataInBackground()
	{
		Thread thread = new Thread(null, runnableDoCheckMetadata,"BackgroundCheckMetadata");
		thread.start();
	}

	private final Runnable runnableDoCheckMetadata = new Runnable(){
		@Override
		public void run(){
			checkMetadataApplication();
		}
	};

	private void checkMetadataApplication()
	{
		if (needsApplicationUpdate(new ContextImpl(mAppContext))) {
			// Go Home and clear stack.
			// call StartupActivity , telling that must check metadata, minor or mayor change detected.
			Intent intent = IntentFactory.getStartupActivityWithReloadMetadata(mAppContext);
			mAppContext.startActivity(intent);
		}
	}

	private boolean needsApplicationUpdate(IContext context)
	{
		if (Services.HttpService.isOnline() && Services.HttpService.isReachable(mGenexusApplication.getAPIUri()))
		{
			Services.Log.debug("start get remote version");
			RemoteApplicationInfo remoteAppInfo = Services.HttpService.getRemoteApplicationInfo();
			Services.Log.debug("end get remote version");
			Services.Log.debug("start check for update");

			// For prototyper all applications has currentMajorVersion = -1
			if (remoteAppInfo != null) {
				if (mGenexusApplication.getMajorVersion() < remoteAppInfo.getMajorVersion()) {
					// loading metadata failed.
					Services.Application.getDefinition().setLoadingMetadata(false);
					// set as not load , so new startup force load again.
					Services.Application.getDefinition().setLoaded(false);
					return true;
				}

				String prefKey = MetadataLoader.getPrefsName(mGenexusApplication) + mGenexusApplication.getMajorVersion() + "-MinorVersion";
				int currentMinorVersion = (int) context.getMinorVersion(prefKey, mGenexusApplication.getMinorVersion());
				// - Current app minor version is lower than the remote
				if (mGenexusApplication.getMajorVersion() == remoteAppInfo.getMajorVersion()
					&& currentMinorVersion < remoteAppInfo.getMinorVersion()) {
					// loading metadata failed
					Services.Application.getDefinition().setLoadingMetadata(false);
					return true;
				}
			}
			Services.Log.debug("end check for update");
		}
		return false; // Either no minor updates, or no update.
	}

	private boolean loadMainObjectProperties(Context context) {
		String resourceName = String.format("%s.properties", Strings.toLowerCase(mGenexusApplication.getAppEntry()));
		INodeObject json = MetadataLoader.getDefinition(context, resourceName);
		if (json != null) {
			INodeObject jsonProperties = json.getNode("properties");
			if (jsonProperties != null) {
				InstanceProperties mainProperties = new InstanceProperties();
				mainProperties.deserialize(jsonProperties);
				mGenexusApplication.setMainProperties(mainProperties);
				return true;
			}
		}

		return false;
	}

	private void loadMetadata(Context context) {
		// Read the project file, used below.
		MetadataFile metadataFile = new MetadataFile(context, mGenexusApplication.getAppEntry());

		List<MetadataLoadStep> steps = Arrays.asList(
			new ServerInfoLoadStep(context, mGenexusApplication),
			new PatternSettingsLoadStep(context, true, mGenexusApplication),
			new ThemesLoadStep(context),
			new ResourcesLoadStep(context),
			new DomainsLoadStep(context),
			new AttributesLoadStep(metadataFile),
			new SDTsLoadStep(metadataFile),
			new EntitiesLoadStep(context, metadataFile),
			new PatternInstancesLoadStep(context, metadataFile),
			new ProceduresLoadStep(metadataFile),
			new DeepLinksLoadStep(context, mGenexusApplication)
		);

		for (MetadataLoadStep step : steps) {
			step.load();
		}
	}

	private void initializeMain() {
		IViewDefinition mainView = mGenexusApplication.getDefinition().getView(mGenexusApplication.getAppEntry());
		if (mainView == null) {
			throw new IllegalArgumentException("Could not find the DataView for the main: " + mGenexusApplication.getAppEntry());
		}
		mGenexusApplication.setMain(mainView);
	}

	private void registerForNotification(Context appContext, GenexusApplication genexusApplication) {
		// Notification only available if enabled them and if device is 2.2
		if (mGenexusApplication.getUseNotification() && Services.Device.isDeviceNotificationEnabled()) {
			if (RemoteNotification.getDefaultProvider() != null) {
				// register with default provider
				RemoteNotification.getDefaultProvider().registerDevice(appContext, genexusApplication);
			}
		}
	}

	private void logDeviceInfo() {
		Services.Log.debug("Device Information");
		Services.Log.debug("Name: " + android.os.Build.MODEL);
		Services.Log.debug("Brand: " + android.os.Build.BRAND);
		Services.Log.debug("Product: " + android.os.Build.PRODUCT);
		Services.Log.debug("SDK: " + Services.Device.getSDKVersion());
		Services.Log.debug("OS: " + Services.Device.getOSVersion());
		Services.Log.debug("Version: " + Services.Device.getOSVersion());

		int widthDips = Services.Device.getScreenWidth();
		int heightDips = Services.Device.getScreenHeight();
		Services.Log.debug("Smallest width (dips): " + Services.Device.getScreenSmallestWidth());
		Services.Log.debug("Screen size (dips): " + widthDips + "x" + heightDips); // Screen size
		Services.Log.debug("Screen size (pixels): " + Services.Device.dipsToPixels(widthDips) + "x" + Services.Device.dipsToPixels(heightDips));
	}
}

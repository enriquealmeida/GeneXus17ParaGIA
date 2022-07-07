package com.genexus.android.core.base.metadata;

import com.genexus.android.remoteconfig.RemoteConfig;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.synchronization.SynchronizationHelper.DataSyncCriteria;
import com.genexus.android.core.base.synchronization.SynchronizationHelper.LocalChangesProcessing;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.base.utils.Version;

import androidx.annotation.Nullable;

public class InstanceProperties extends PropertiesObject
{
	private static final long SERIAL_VERSION_UID = 1L;
	private static final String SECURITY_NONE = "SecurityNone";

	private Version mVersion;

	@Override
	public void deserialize(INodeObject obj)
	{
		super.deserialize(obj);
		mVersion = new Version(obj.optString("@Version"));
	}

	@Override
	public Object getProperty(String name)
	{
		// Check both with and without '@'.
		Object value = super.getProperty(name);
		if (value == null)
		{
			if (name.startsWith("@"))
				name = name.substring(1);
			else
				name = "@" + name;

			value = super.getProperty(name);
		}

		return value;
	}

	public String getIntegratedSecurityLevel()
	{
		return optStringProperty("@IntegratedSecurityLevel");
	}

	public boolean notSecureInstance()
	{
		return getIntegratedSecurityLevel().equalsIgnoreCase(SECURITY_NONE);
	}

	public Version getDefinitionVersion()
	{
		return mVersion;
	}

	public Connectivity getConnectivitySupport()
	{
		String connectivity = optStringProperty("@idConnectivitySupport");
		if (Services.Strings.hasValue(connectivity)) {
			if (connectivity.equalsIgnoreCase("idOffline")) {
				return Connectivity.Offline;
			} else if (connectivity.equalsIgnoreCase("idInherit")) {
				return Connectivity.Inherit;
			}
		}
		return Connectivity.Online;
	}

	public boolean getShowLogoutButton() {
		return getBooleanProperty("@IntegratedSecurityShowLogoutButton", true);
	}

	public String getSynchronizer() {
		return MetadataLoader.getAttributeName( optStringProperty("@Synchronizer"));
	}

	public DataSyncCriteria getSynchronizerDataSyncCriteria()
	{
		String dataSyncCriteria = optStringProperty("@idDataSyncCriteria");
		if (Services.Strings.hasValue(dataSyncCriteria)) {
			if (dataSyncCriteria.equalsIgnoreCase("idAutomatic")) {
				return DataSyncCriteria.Automatic;
			} else if (dataSyncCriteria.equalsIgnoreCase("OnAppLaunch")) {
				return DataSyncCriteria.Automatic;
			}
			else if (dataSyncCriteria.equalsIgnoreCase("ElapsedTime")) {
				return DataSyncCriteria.AfterElapsedTime;
			}
			else if (dataSyncCriteria.equalsIgnoreCase("idManual")) {
				return DataSyncCriteria.Manual;
			}
		}
		return DataSyncCriteria.Manual;
	}

	public long getSynchronizerMinTimeBetweenSync()
	{
		return optLongProperty("@idMinTimeBetweenSync");
	}

	public LocalChangesProcessing getSendLocalChangesProcessing()
	{
		String localChangesProcessing = optStringProperty("@LocalChangesProcessing");
		if (Services.Strings.hasValue(localChangesProcessing)) {
			if (localChangesProcessing.equalsIgnoreCase("WhenConnected")) {
				return LocalChangesProcessing.WhenConnected;
			} else if (localChangesProcessing.equalsIgnoreCase("UserDefined")) {
				return LocalChangesProcessing.UserDefined;
			} else if (localChangesProcessing.equalsIgnoreCase("Never")) {
				return LocalChangesProcessing.Never;
			}
		}
		return LocalChangesProcessing.UserDefined;
	}
	
	public long getSynchronizerMinTimeBetweenSends()
	{
		return optLongProperty("@MinTimeBetweenSends");
	}

	public long getSynchronizerTimeoutReceive()
	{
		return optLongProperty("@idSyncTimeoutReceive");
	}

	public long getSynchronizerTimeoutSend()
	{
		return optLongProperty("@idSyncTimeoutSend");
	}

	public String getShortcutsMenu() {
		String shortcutsMenu = optStringProperty("@idAppShortcuts");
		if (shortcutsMenu.isEmpty())
			return null;

		if (shortcutsMenu.contains("-")) {
			String[] split = Services.Strings.split(shortcutsMenu, "-");
			return split[split.length - 1];
		}

		return shortcutsMenu;
	}

	public String getNotificationsPanel() {
		return optStringProperty("@idNotificationSettingsPanel");
	}

	public RemoteConfig.FetchCriteria getRemoteConfigFetchCriteria() {
		String activationCriteria = optStringProperty("@RemoteConfigValuesFetching");
		if (Strings.hasValue(activationCriteria)) {
			if (activationCriteria.equalsIgnoreCase("idFetchingOnAppActivation"))
				return RemoteConfig.FetchCriteria.WhenActivated;
			else if (activationCriteria.equalsIgnoreCase("idFetchingAfterTime"))
				return RemoteConfig.FetchCriteria.AfterElapsedTime;
			else if (activationCriteria.equalsIgnoreCase("idFetchingManual"))
				return RemoteConfig.FetchCriteria.Manual;
		}

		return RemoteConfig.FetchCriteria.WhenActivated;
	}

	public RemoteConfig.ActivationCriteria getRemoteConfigActivationCriteria() {
		String activationCriteria = optStringProperty("@RemoteConfigValuesApplication");
		if (Strings.hasValue(activationCriteria)) {
			if (activationCriteria.equalsIgnoreCase("idApplicationOnAppLaunch"))
				return RemoteConfig.ActivationCriteria.WhenLaunched;
			else if (activationCriteria.equalsIgnoreCase("idApplicationImmediately"))
				return RemoteConfig.ActivationCriteria.Immediately;
			else if (activationCriteria.equalsIgnoreCase("idApplicationManual"))
				return RemoteConfig.ActivationCriteria.Manual;
		}

		return RemoteConfig.ActivationCriteria.WhenLaunched;
	}

	public @Nullable NameMap<Object> getRemoteConfigDefaults() {
		String defaultValuesString = optStringProperty("@RemoteConfigDefaultValues");
		if (defaultValuesString.trim().isEmpty())
			return null;

		String[] commaSeparatedList = Services.Strings.split(defaultValuesString, ",");
		NameMap<Object> defaults = new NameMap<>();
		for (int i = 0; i < commaSeparatedList.length; i++) {
			String[] keyValuePair = Services.Strings.split(commaSeparatedList[i], ":");
			if (keyValuePair.length == 2)
				defaults.put(keyValuePair[0].trim(), keyValuePair[1].trim());
		}

		return defaults;
	}

	public long getRemoteConfigFetchIntervalMinutes() {
		long value = optLongProperty("@RemoteConfigMinInterval");
		return value == 0 ? 1440L : value;
	}
}

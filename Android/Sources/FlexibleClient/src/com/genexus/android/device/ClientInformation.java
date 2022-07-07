package com.genexus.android.device;

import android.os.Build;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.PlatformHelper;
import com.genexus.android.core.base.utils.Strings;

/**
 * This class allow access to device information.
 */
public class ClientInformation
{
	private static final String OS_NAME = "Android";
	private static String sApplicationId = null;

	/***
	 * Return a value that identify the device. This id is network independent.
	 * @return
	 */
	public static String id()
	{
		DeviceUuidFactory factory = new DeviceUuidFactory(Services.Application.getAppContext());
		return factory.getDeviceUuid().toString();
	}

	/***
	 * Returns Android as the operating system
	 * @return
	 */
	public static String osName() { return OS_NAME; }

	/***
	 * Return the OS Version code, you can see the values from
	 * http://developer.android.com/reference/android/os/Build.VERSION_CODES.html
	 * @return
	 */
	public static String osVersion()
	{
		// change implementation, issue 29979, retornar version release.
		//return String.valueOf(android.os.Build.VERSION.SDK_INT);
		return String.valueOf(android.os.Build.VERSION.RELEASE);
	}

	public static String deviceName()
	{
		return Build.BRAND + " - " + Build.MODEL;
	}

	public static String getPlatformName()
	{
		return PlatformHelper.getPlatform().getName();
	}

	public static String getLanguage()
	{
		return Services.Language.getLocaleString(Services.Language.getLocales());
	}

	public static int deviceType() { return 1; } // SmartDeviceType Enum, Android=1

	public static String getAppVersionCode()
	{
		if (Services.Application.get() != null && Services.Application.get().getMainProperties() != null)
			return Services.Application.get().getMainProperties().optStringProperty("@idAPPAndroidVersionCode");
		else
			return Strings.EMPTY;
	}

	public static String getAppVersionName()
	{
		if (Services.Application.get() != null && Services.Application.get().getMainProperties() != null)
		{
			String valueVersionCode = Services.Application.get().getMainProperties().optStringProperty("@idAPPAndroidVersionName");
			// if has default value is now written in metadata.
			if (Strings.hasValue(valueVersionCode))
				return valueVersionCode;
			else
				return "1.0";
		}
		else
			return Strings.EMPTY;
	}

	/***
	 * Return a value that identify the device app instalation.
	 * @return
	 */
	public static String instalationId()
	{
		DeviceInstallUuidFactory factory = new DeviceInstallUuidFactory(Services.Application.getAppContext());
		return factory.getDeviceInstalationUuid().toString();
	}

	public static String applicationId()
	{
		if (sApplicationId==null)
			sApplicationId = Services.Application.getAppContext().getPackageName();

		return sApplicationId;
	}

}

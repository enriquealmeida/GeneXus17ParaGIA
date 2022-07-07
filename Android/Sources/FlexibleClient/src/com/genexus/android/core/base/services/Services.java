package com.genexus.android.core.base.services;

@SuppressWarnings("checkstyle:StaticVariableName")
public class Services {
	public static IApplication Application;
	public static IAssetsService Assets;
	public static IDeviceService Device;
	public static IExceptions Exceptions;
	public static IFontsService Fonts;
	public static IHttpService HttpService;
	public static IImagesService Images;
	public static ILanguage Language;
	public static ILocationServices Location;
	public static ILog Log;
	public static IMaps Maps;
	public static IMaps.Offline MapsOffline;
	public static IMessages Messages;
	public static INotifications Notifications;
	public static IPreferences Preferences;
	public static IResourcesService Resources;
	public static ISerialization Serializer;
	public static IStringUtil Strings;
	public static ISyncService Sync;
	public static IThemes Themes;
	public static IExtensions Extensions;
	public static IShortcuts Shortcuts;
	public static ISuperApps SuperApps;

	public static void overrideResourcesService(IResourcesService resourcesService) {
		Resources = resourcesService;
	}
}

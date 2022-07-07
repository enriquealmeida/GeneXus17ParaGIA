//
//  GXNotificationsProvider_OneSignalExtensionLibrary.swift
//  GXNotificationsProvider_OneSignal
//

import GXCoreBL

@objc(GXNotificationsProvider_OneSignalExtensionLibrary)
public class GXNotificationsProvider_OneSignalExtensionLibrary: NSObject, GXExtensionLibraryProtocol {
	
	public func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXUserNotificationsManager.registerNotificationsProviderClass(GXUserNotificationsProviderOneSignal.self, forTypeIdentifier: GXUserNotificationsProviderOneSignal.notificationsProviderTypeIdentifier)
	}
}

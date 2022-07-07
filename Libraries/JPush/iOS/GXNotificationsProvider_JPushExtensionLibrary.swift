//
//  GXNotificationsProvider_JPushExtensionLibrary
//  GXNotificationsProvider_JPush
//

import GXCoreBL

@objc(GXNotificationsProvider_JPushExtensionLibrary)
public class GXNotificationsProvider_JPushExtensionLibrary: NSObject, GXExtensionLibraryProtocol {

	public func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXUserNotificationsManager.registerNotificationsProviderClass(GXUserNotificationsProviderJPush.self, forTypeIdentifier: GXUserNotificationsProviderJPush.notificationsProviderTypeIdentifier)
	}
}

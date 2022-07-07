//
//  GXAppDelegate.swift
//  $Main.Name$
//

import Foundation
import GXCoreBL

$if(Main.EnableNotification)$
extension GXUIApplicationClass {
	override open func registerForRemoteNotifications() {
		GXUserNotificationsManager.markRemoteNotificationsTokenRenewNotNeeded()
		super.registerForRemoteNotifications()
	}
}

$endif$
@objc
public class GXAppDelegate: AppDelegate_Shared {

#if DEBUG
	override public var gxShowDeveloperInfo: Bool {
		get { return true }
	}
#endif

$if(Main.iOSHasExtensionLibrariesInUse)$
	private lazy var gxapplicationExtensionLibraries_: [GXExtensionLibraryProtocol] = {
		var moduleClasses: [String] = []
$Main.iOSExtensionLibrariesInUseModuleClasses:{moduleClassName|			moduleClasses.append("$moduleClassName$")};separator="\n"$
		return moduleClasses.map { (moduleClassName) in
			let extLibClass = NSClassFromString(moduleClassName) as! NSObject.Type
			return extLibClass.init() as! GXExtensionLibraryProtocol
		};
	}()

	override public var gxapplicationExtensionLibraries: [GXExtensionLibraryProtocol] {
		return gxapplicationExtensionLibraries_
	}

$endif$
$if(Main.EnableNotification || Main.IsUsingLocalNotificationAPI)$
	// MARK: - User Notifications

	@available(iOS, deprecated: 10.0)
	public func application(_ application: UIApplication, didRegister notificationSettings: UIUserNotificationSettings) {
		GXUserNotificationsManager.onDidRegisterUserNotificationSettings()
	}

$endif$
$if(Main.EnableNotification)$
	// MARK: - Remote Notifications

	public func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
		GXUserNotificationsManager.onDidRegisterForRemoteNotifications(withDeviceToken: deviceToken)
	}

	public func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
		GXUserNotificationsManager.onDidFailToRegisterForRemoteNotificationsWithError(error)
	}

	@available(iOS, deprecated: 10.0)
	public func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) {
		GXUserNotificationsManager.onDidReceiveRemoteNotification(userInfo)
	}

$if(Main.SilentNotificationsAllowed)$
	public func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
		GXUserNotificationsManager.onDidReceiveRemoteNotification(userInfo, fetchCompletionHandler: {
			completionHandler(.newData)
		})
	}

$endif$
	@available(iOS, introduced: 9.0, deprecated: 10.0)
	public func application(_ application: UIApplication, handleActionWithIdentifier identifier: String?, forRemoteNotification userInfo: [AnyHashable : Any], withResponseInfo responseInfo: [AnyHashable : Any], completionHandler: @escaping () -> Swift.Void) {

		GXUserNotificationsManager.onHandleAction(withIdentifier: identifier, forRemoteNotification: userInfo, withResponseInfo: responseInfo, completionHandler: completionHandler)
	}

	@available(iOS, deprecated: 10.0)
	public func application(_ application: UIApplication, handleActionWithIdentifier identifier: String?, forRemoteNotification userInfo: [AnyHashable : Any], completionHandler: @escaping () -> Swift.Void) {

		GXUserNotificationsManager.onHandleAction(withIdentifier: identifier, forRemoteNotification: userInfo, completionHandler: completionHandler)
	}

$endif$
$if(Main.IsUsingLocalNotificationAPI)$
	// MARK: - Local Notifications

	@available(iOS, deprecated: 10.0)
	public func application(_ application: UIApplication, didReceive notification: UILocalNotification) {
		GXUserNotificationsManager.onDidReceiveLocalNotification(notification)
	}

	@available(iOS, introduced: 9.0, deprecated: 10.0)
	public func application(_ application: UIApplication, handleActionWithIdentifier identifier: String?, for notification: UILocalNotification, withResponseInfo responseInfo: [AnyHashable : Any], completionHandler: @escaping () -> Swift.Void) {

		GXUserNotificationsManager.onHandleAction(withIdentifier: identifier, for: notification, withResponseInfo: responseInfo, completionHandler: completionHandler)
	}

	@available(iOS, deprecated: 10.0)
	public func application(_ application: UIApplication, handleActionWithIdentifier identifier: String?, for notification: UILocalNotification, completionHandler: @escaping () -> Swift.Void) {

		GXUserNotificationsManager.onHandleAction(withIdentifier: identifier, for: notification, completionHandler: completionHandler)
	}

$endif$
$if(Main.HasApplicationShortcuts && Main.AppleDevice_iOS)$
	// MARK: - Application Shortcuts

	@objc(application:performActionForShortcutItem:completionHandler:)
	public func application(_ application: UIApplication, performActionFor shortcutItem: UIApplicationShortcutItem, completionHandler: @escaping (Bool) -> Void) {
		
		if !gxApplication(application, performActionFor: shortcutItem, completionHandler: completionHandler) {
			completionHandler(false)
		}
	}
$endif$
}

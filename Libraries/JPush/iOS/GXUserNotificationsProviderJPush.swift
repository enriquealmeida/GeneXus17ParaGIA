//
//  GXUserNotificationsProviderJPush.swift
//  GXNotificationsProvider_JPush
//

import Foundation
import GXCoreBL
import GXCoreModule_SD_Notifications
import UserNotifications

@objc(GXUserNotificationsProviderJPush)
open class GXUserNotificationsProviderJPush: NSObject, GXUserNotificationsProviderProtocol, GXRemoteNotificationsProviderAppDelegateProtocol, JPUSHRegisterDelegate {

    private var userId : String? = nil
    private class func registrationHandlerAdditionalData(withUserId userId: String?) -> [String: Any] {
        if let userId = userId {
            return [ "NotificationPlatformId": userId ]
        }
        else {
            return [:]
        }
    }
    private var waitingForUserIdCompletion: (([String: Any]) -> Void)? = nil
    
    public static let notificationsProviderTypeIdentifier = "JPush"

	// MARK: - GXUserNotificationsProviderProtocol
	public let typeIdentifier = GXUserNotificationsProviderJPush.notificationsProviderTypeIdentifier

	open func initializeProvider(withLaunchOptions launchOptions: [AnyHashable: Any]?) -> Void {
		let appKey = GXUserNotificationsManager.notificationsProviderProperties["AppID"] as? String
        let channel = GXUserNotificationsManager.notificationsProviderProperties["Channel"] as? String
        var isProduction: Bool?
        if let apsForProduction = GXUserNotificationsManager.notificationsProviderProperties["ApsForProduction"] as? String, apsForProduction == "true" {
            isProduction = true
        } else {
            isProduction = false
        }

        JPUSHService.setup(withOption: launchOptions, appKey: appKey, channel: channel ?? "", apsForProduction: isProduction!)
	}

    public final var remoteAppDelegate: GXRemoteNotificationsProviderAppDelegateProtocol {
        get {
            return self
        }
    }
    


	// MARK: Remote
	open func registerForPushNotifications() -> Void {
        if #available(iOS 10, *) {
            let entity = JPUSHRegisterEntity()
            entity.types = NSInteger(UNAuthorizationOptions.alert.rawValue) |
                NSInteger(UNAuthorizationOptions.sound.rawValue) |
                NSInteger(UNAuthorizationOptions.badge.rawValue)
            JPUSHService.register(forRemoteNotificationConfig: entity, delegate: self)
            
        } else if #available(iOS 8, *) {
            JPUSHService.register(
                forRemoteNotificationTypes: UIUserNotificationType.badge.rawValue |
                    UIUserNotificationType.sound.rawValue |
                    UIUserNotificationType.alert.rawValue,
                categories: nil)
        } else {
            JPUSHService.register(
                forRemoteNotificationTypes: UIRemoteNotificationType.badge.rawValue |
                    UIRemoteNotificationType.sound.rawValue |
                    UIRemoteNotificationType.alert.rawValue,
                categories: nil)
        }
    }

	public final var registeringForPushNotificationsAlsoRegisterForUserNotification: Bool {
		get { return true }
	}
    
    open func registrationHandlerAdditionalData(completion: @escaping ([String: Any]) -> Void) -> Void {
        if let userId = self.userId {
            let additionalData = GXUserNotificationsProviderJPush.registrationHandlerAdditionalData(withUserId: userId)
            completion(additionalData)
        } else {
            if let currentCompletion = waitingForUserIdCompletion {
                waitingForUserIdCompletion = {
                    currentCompletion($0)
                    completion($0)
                }
            }
            else {
                waitingForUserIdCompletion = completion
            }
        }
    }

	public final var handlesSilentNotificationsCompletion: Bool {
		get { return false }
	}

	public final var handlesNotificationsResponsesCompletion: Bool {
		get { return false }
	}

    // MARK: - GXRemoteNotificationsProviderAppDelegateProtocol
    public func onDidRegisterForRemoteNotifications(withDeviceToken devToken: Data) {
        JPUSHService.registerDeviceToken(devToken)
        JPUSHService.registrationIDCompletionHandler { (resCode, registrationID) in
            if let completion = self.waitingForUserIdCompletion {
                self.waitingForUserIdCompletion = nil
                let additionalData = GXUserNotificationsProviderJPush.registrationHandlerAdditionalData(withUserId: registrationID)
                completion(additionalData)
            }
        }
    }

    public func onDidFailToRegisterForRemoteNotificationsWithError(_ error: Error) {
      print("Failed to regsiter for remote notifications \(error)")
    }

    @available(iOS, deprecated: 10.0)
    public func onDidReceiveRemoteNotification(_ userInfo: [AnyHashable : Any]) {
        if #available(iOS 10, *) {
            if !GXJPushNotificationsHelpers.isSilentNotification(userInfo: userInfo) {
                return //Otherwise notifications would be executed twice in iOS 10+
            }
        }
        handleNotificationReceived(forRemoteNotification: userInfo)
    }

    public func onDidReceiveRemoteNotification(_ userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping () -> Void) {
        if #available(iOS 10, *) {
            return //Otherwise notifications would be executed twice in iOS 10+
        }
        handleNotificationReceived(forRemoteNotification: userInfo)
    }

    @available(iOS, introduced: 9.0, deprecated: 10.0)
    public func onHandleAction(withIdentifier identifier: String?, forRemoteNotification userInfo: [AnyHashable : Any], withResponseInfo responseInfo: [AnyHashable : Any], completionHandler: @escaping () -> Void) { // Silent Notifications
        if #available(iOS 10, *) {
            return //Otherwise notifications would be executed twice in iOS 10+
        }
        handleNotificationReceived(forRemoteNotification: userInfo)
    }

    public func onHandleAction(withIdentifier identifier: String?, forRemoteNotification userInfo: [AnyHashable : Any], completionHandler: @escaping () -> Void) {
        if #available(iOS 10, *) {
            return //Otherwise notifications would be executed twice in iOS 10+
        }
        handleNotificationAction(userInfo: userInfo, actionIdentifier: identifier, withCompletionHandler: completionHandler)
    }

    // MARK: - JPUSHRegisterDelegate
    @available(iOS 10.0, *)
    public func jpushNotificationCenter(_ center: UNUserNotificationCenter!, didReceive response: UNNotificationResponse!, withCompletionHandler completionHandler: (() -> Void)!) {

        handleNotificationAction(userInfo: response.notification.request.content.userInfo, actionIdentifier: response.actionIdentifier, withCompletionHandler: completionHandler)
    }

    @available(iOS 10.0, *)
    public func jpushNotificationCenter(_ center: UNUserNotificationCenter!, willPresent notification: UNNotification!, withCompletionHandler completionHandler: ((Int) -> Void)!) {

        handleNotificationReceived(forRemoteNotification: notification.request.content.userInfo)
        if !GXUserNotificationsManager.defaultGXUNNotificationPresentationOptions.isEmpty {
            completionHandler(GXJPushNotificationsHelpers.getNotificationDisplayOptions(forNotification: notification.request.content.userInfo))
        }
    }
    
    // MARK: - Private Helpers
    private func handleNotificationReceived(forRemoteNotification userInfo: [AnyHashable : Any]) {
        JPUSHService.handleRemoteNotification(userInfo)
        
        guard let additionalData = GXJPushNotificationsHelpers.getAdditionalData(from: userInfo), GXJPushNotificationsHelpers.isSilentNotification(userInfo: userInfo) else { return }
        
        if let notificationActionHandler = GXUNNotificationFactory.actionHandler(for: GXUNNotificationDefaultActionIdentifier, fromPushNotification: additionalData) {
            GXUserNotificationsManager.handleReceivedSilentNotification(notificationActionHandler)
        }
    }
    
    private func handleNotificationAction(userInfo: [AnyHashable : Any], actionIdentifier: String?, withCompletionHandler completionHandler: (() -> Void)!) {
        guard !GXJPushNotificationsHelpers.isSilentNotification(userInfo: userInfo) else { return }
        
        let gxNotificationResponse = GXJPushNotificationWrapper.from(resultUserInfo: userInfo, actionIdentifier: actionIdentifier)
        GXUserNotificationsManager.handleReceivedNotificationResponse(gxNotificationResponse, withCompletionHandler: completionHandler)
    }
}

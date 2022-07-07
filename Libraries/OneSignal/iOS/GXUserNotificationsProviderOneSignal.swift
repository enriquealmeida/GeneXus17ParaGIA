//
//  GXUserNotificationsProviderOneSignal.swift
//  GXNotificationsProvider_OneSignal
//

import Foundation
import GXCoreBL
import GXCoreModule_SD_Notifications
import OneSignal

@objc(GXUserNotificationsProviderOneSignal)
open class GXUserNotificationsProviderOneSignal: NSObject, GXUserNotificationsProviderProtocol, OSSubscriptionObserver {
	
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
	
	public static let notificationsProviderTypeIdentifier = "OneSignal"
	
	// MARK: - GXUserNotificationsProviderProtocol
	public let typeIdentifier = GXUserNotificationsProviderOneSignal.notificationsProviderTypeIdentifier
	
	open func initializeProvider(withLaunchOptions launchOptions: [AnyHashable: Any]?) -> Void {
		let inFocusDisplayOption: OSNotificationDisplayType
		if #available(iOS 10, *) {
			inFocusDisplayOption = GXUserNotificationsManager.defaultGXUNNotificationPresentationOptions.isEmpty ? .none : .notification
		}
		else {
			inFocusDisplayOption = .none
		}
		let appId = GXUserNotificationsManager.notificationsProviderProperties["AppID"] as? String
		let settings = [kOSSettingsKeyAutoPrompt: false,
		                kOSSettingsKeyInFocusDisplayOption: inFocusDisplayOption.rawValue,
		                kOSSettingsKeyInAppLaunchURL: false] as [String : Any]
		OneSignal.setLocationShared(false)
		OneSignal.initWithLaunchOptions(launchOptions,
		                                appId: appId ?? "",
		                                handleNotificationReceived:
			{ notification in
				guard let notification = notification, notification.isSilentNotification, let payload = notification.payload, let additionalData = payload.additionalData else { return }
				
				if let notificationActionHandler = GXUNNotificationFactory.actionHandler(for: GXUNNotificationDefaultActionIdentifier, fromPushNotification: additionalData) {
					GXUserNotificationsManager.handleReceivedSilentNotification(notificationActionHandler)
				}
		}, handleNotificationAction: { result in
			
			guard let result = result, let notification = result.notification, !notification.isSilentNotification else { return	}

			let gxNotificationResponse = GXOSNotificationOpenedResultWrapper.from(result: result)
			GXUserNotificationsManager.handleReceivedNotificationResponse(gxNotificationResponse)
		}, settings: settings)
		if let userId = OneSignal.getPermissionSubscriptionState().subscriptionStatus.userId {
			self.userId = userId
		}
		else {
			OneSignal.add(self as OSSubscriptionObserver);
		}
	}
	
	public final var usesMethodSwizzerlingOnDelegateClasses: Bool {
		get { return true }
	}
	
	// MARK: OSSubscriptionObserver
	
	open func onOSSubscriptionChanged(_ stateChanges: OSSubscriptionStateChanges!) {
		if let newUserId = stateChanges.to.userId {
			self.userId = newUserId
			if let completion = waitingForUserIdCompletion {
				waitingForUserIdCompletion = nil
				let additionalData = GXUserNotificationsProviderOneSignal.registrationHandlerAdditionalData(withUserId: newUserId)
				completion(additionalData)
			}
		}
	}
	
	// MARK: Remote
	
	open func registerForPushNotifications() -> Void {
		OneSignal.promptForPushNotifications(userResponse: nil)
	}
	
	public final var registeringForPushNotificationsAlsoRegisterForUserNotification: Bool {
		get { return true }
	}
	
	open func registrationHandlerAdditionalData(completion: @escaping ([String: Any]) -> Void) -> Void {
		if let userId = self.userId {
			let additionalData = GXUserNotificationsProviderOneSignal.registrationHandlerAdditionalData(withUserId: userId)
			completion(additionalData)
		}
		else {
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
}

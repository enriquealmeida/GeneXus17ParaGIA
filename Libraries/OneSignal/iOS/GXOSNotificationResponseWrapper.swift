//
//  GXOSNotificationResponseWrapper.swift
//  GXNotificationsProvider_OneSignal
//

import Foundation
import OneSignal
import GXCoreBL
import GXCoreModule_SD_Notifications

public class GXOSNotificationOpenedResultWrapper: NSObject, GXUNNotificationResponse, GXUNNotification, GXUNNotificationRequest, GXUNNotificationContent {
	
	public let osNotificationOpenedResult: OSNotificationOpenedResult
	
	class func from(result: OSNotificationOpenedResult) -> GXOSNotificationOpenedResultWrapper {
//		if result.userText == nil {
			return GXOSNotificationOpenedResultWrapper(result: result)
//		}
//		else {
//			return GXOSNotificationOpenedResultTextInputWrapper(result: result)
//		}
	}
	
	fileprivate init(result: OSNotificationOpenedResult) {
		self.osNotificationOpenedResult = result
		super.init()
	}
	
	// MARK: - GXUNNotificationResponse
	
	public var notification: GXUNNotification {
		get { return self }
	}
	
	public var actionIdentifier: String {
		get {
			guard let action = self.osNotificationOpenedResult.action, let actionID = action.actionID else {
				return GXUNNotificationDefaultActionIdentifier
			}
			
			if #available(iOS 10, *) {
				switch actionID {
				case UNNotificationDefaultActionIdentifier:
					return GXUNNotificationDefaultActionIdentifier
				case UNNotificationDismissActionIdentifier:
					return GXUNNotificationDismissActionIdentifier
				default: break
				}
			}
			else {
				if action.type == .opened || actionID == "__DEFAULT__" {
					return GXUNNotificationDefaultActionIdentifier
				}
			}
			return actionID
		}
	}
	
	// MARK: - GXUNNotification
	
	
	public var date: Date? { return nil }
	
	public var request: GXUNNotificationRequest { get { return self } }
	
	// MARK: - GXUNNotificationRequest
	
	@available(iOS 10.0, *)
	public var identifier: String {
		get { return self.osNotificationOpenedResult.notification?.payload?.notificationID ?? "" }
	}
	
	public var content: GXUNNotificationContent { get { return self } }
	
	// MARK: - GXUNNotificationRequest
	
	public var badge: NSNumber? {
		get {
			guard let payload = self.osNotificationOpenedResult.notification?.payload, payload.badge > 0 else { return nil }
			return NSNumber(value: payload.badge)
		}
	}
	
	public var body: String? {
		get {
			guard let payload = self.osNotificationOpenedResult.notification?.payload else { return nil }
			return payload.body
		}
	}
	
	public var categoryIdentifier: String? {
		get {
			guard let rawPayload = self.osNotificationOpenedResult.notification?.payload?.rawPayload,
				let aps = rawPayload["aps"] as? [AnyHashable : Any] else { return nil }
			
			return aps["category"] as? String
		}
	}
	
	public var subtitle: String? {
		get {
			guard let payload = self.osNotificationOpenedResult.notification?.payload else { return nil }
			return payload.subtitle
		}
	}
	
	public var threadIdentifier: String? {
		get {
			guard let rawPayload = self.osNotificationOpenedResult.notification?.payload?.rawPayload,
				let aps = rawPayload["aps"] as? [AnyHashable : Any] else { return nil }
			
			return aps["thread-id"] as? String
		}
	}
	
	public var title: String? {
		get {
			guard let payload = self.osNotificationOpenedResult.notification?.payload else { return nil }
			return payload.title
		}
	}
	
	public var userInfo: [AnyHashable : Any] {
		get {
			return self.osNotificationOpenedResult.notification?.payload?.additionalData ?? [:]
		}
	}
	
	public func actionHandler(for identifier: String) -> GXUNNotificationActionHandler? {
		return GXUNNotificationFactory.actionHandler(for: identifier, fromPushNotification: self.userInfo)
	}
}


//public class GXOSNotificationOpenedResultTextInputWrapper: GXOSNotificationOpenedResultWrapper {
//	
//	// MARK: GXUNNotificationResponse
//	
//	public var userText: String {
//		get { return self.osNotificationOpenedResult.userText ?? "" }
//	}
//}

//
//  GXOSNotificationResponseWrapper.swift
//  GXNotificationsProvider_OneSignal
//

import Foundation
import GXCoreBL
import GXCoreModule_SD_Notifications
import UserNotifications

public class GXJPushNotificationWrapper: NSObject, GXUNNotificationResponse, GXUNNotification, GXUNNotificationRequest, GXUNNotificationContent {
    
    fileprivate var responseUserInfo: [AnyHashable : Any]!
    fileprivate var internalActionIdentifier: String?
    
    class func from(resultUserInfo: [AnyHashable : Any], actionIdentifier: String?) -> GXJPushNotificationWrapper {
        return GXJPushNotificationWrapper(userInfo: resultUserInfo, actionIdentifier: actionIdentifier)
	}
	
    fileprivate init(userInfo: [AnyHashable : Any], actionIdentifier: String?) {
		self.responseUserInfo = userInfo
        self.internalActionIdentifier = actionIdentifier
        
		super.init()
	}
    
    private var apsUserInfo: [AnyHashable : Any] {
        get {
            let apsUserInfo = responseUserInfo["aps"] as? [AnyHashable : Any] ?? [:]
            return apsUserInfo
        }
    }
	
	// MARK: - GXUNNotificationResponse
	
	public var notification: GXUNNotification {
		get { return self }
	}
	
    
	public var actionIdentifier: String {
        get {
            guard let actionID = internalActionIdentifier else { return GXUNNotificationDefaultActionIdentifier }

            if #available(iOS 10.0, *) {
                switch actionID {
                case UNNotificationDefaultActionIdentifier:
                    return GXUNNotificationDefaultActionIdentifier
                case UNNotificationDismissActionIdentifier:
                    return GXUNNotificationDismissActionIdentifier
                default: break
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
        get { return self.responseUserInfo["_j_msgid"] as? String ?? ""}
	}
	
	public var content: GXUNNotificationContent { get { return self } }
	
	// MARK: - GXUNNotificationRequest
	
	public var badge: NSNumber? {
		get {
            guard let badge = apsUserInfo["badge"] as? Decimal, badge > 0 else { return nil }
			return badge as NSNumber
		}
	}
	
	public var body: String? {
		get {
            guard let alertUserInfo = apsUserInfo["alert"] as? [AnyHashable : Any], let body = alertUserInfo["body"] as? String else { return nil }
			return body
		}
	}
	
	public var categoryIdentifier: String? {
        guard let category = responseUserInfo["category"] as? String else { return nil }
        return category
	}
	
	public var subtitle: String? {
        get {
            guard let alertUserInfo = apsUserInfo["alert"] as? [AnyHashable : Any], let subtitle = alertUserInfo["subtitle"] as? String else { return nil }
            return subtitle
        }
	}
	
	public var threadIdentifier: String? {
        get {
            guard let threadIdentifier = apsUserInfo["thread-id"] as? String else { return nil }
            return threadIdentifier
        }
	}
	
	public var title: String? {
        get {
            guard let alertUserInfo = apsUserInfo["alert"] as? [AnyHashable : Any], let title = alertUserInfo["title"] as? String else { return nil }
            return title
        }
	}
	
	public var userInfo: [AnyHashable : Any] {
        return GXJPushNotificationsHelpers.getAdditionalData(from: responseUserInfo) ?? [:]
    }
	
	public func actionHandler(for identifier: String) -> GXUNNotificationActionHandler? {
		return GXUNNotificationFactory.actionHandler(for: identifier, fromPushNotification: self.userInfo)
	}
}

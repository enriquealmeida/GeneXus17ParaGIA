//
//  GXJPushHelper.swift
//  TestPushNotifications
//
//  Created by José Echagüe on 9/15/17.
//

import Foundation

class GXJPushNotificationsHelpers {
    
    static public func isSilentNotification(userInfo: [AnyHashable : Any]) -> Bool {
        if let apsUserInfo = userInfo["aps"] as? [AnyHashable : Any], let alertUserInfo = apsUserInfo["alert"] as? [AnyHashable : Any]{
            if apsUserInfo["badge"] != nil || apsUserInfo["sound"] != nil || alertUserInfo["title"] != nil || alertUserInfo["body"] != nil || alertUserInfo["subtitle"] != nil {
                return false
            }
        }
        return true
    }
    
    static public func hasContentAvailable(userInfo: [AnyHashable : Any]) -> Bool {
        if let apsUserInfo = userInfo["aps"] as? [AnyHashable : Any], let contentAvailable = apsUserInfo["content-available"] as? Bool {
            if contentAvailable == true {
                return true
            }
        }
        return false
    }
    
    static public func getAdditionalData(from userInfo: [AnyHashable : Any]) -> [AnyHashable : Any]? {
        if var additionalData = userInfo["GXAdditionalData"] as? String {
            additionalData = "{" + additionalData + "}"
            if let data = additionalData.data(using: .utf8) {
                do {
                    return (try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [AnyHashable : Any])
                } catch { print("Error parsing GXAdditionalData to JSON: \(error)") }
            }
        }
        return nil
    }
    
    static public func getNotificationDisplayOptions(forNotification userInfo: [AnyHashable : Any]) -> Int {
        var notificationDisplayOption = 0
        if let apsUserInfo = userInfo["aps"] as? [AnyHashable : Any] {
            if apsUserInfo["badge"] != nil {
                notificationDisplayOption += Int(GXUNNotificationPresentationOptions.badge.rawValue)
            }
            if apsUserInfo["sound"] != nil {
                notificationDisplayOption += Int(GXUNNotificationPresentationOptions.sound.rawValue)
            }
            if let alertUserInfo = apsUserInfo["alert"] as? [AnyHashable : Any] {
                if alertUserInfo["body"] != nil || alertUserInfo["title"] != nil || alertUserInfo["subtitle"] != nil {
                    notificationDisplayOption += Int(GXUNNotificationPresentationOptions.alert.rawValue)
                }
            }
        }
        return notificationDisplayOption
    }
}

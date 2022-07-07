//
//  GXFirebaseAnalyticsService.swift
//

import GXCoreBL
#if os(iOS)
import FirebaseAnalytics
import FirebaseCore
#endif

@objc(GXFirebaseAnalyticsService)
public class GXFirebaseAnalyticsService: NSObject {

	// MARK: - Singleton

	public static let sharedInstance = GXFirebaseAnalyticsService()

	private override init() {
		super.init()
	}

	// MARK: Private Helpers

	#if os(iOS)
	private struct Constants {
		static let eventNameMaxLength = 40
	}

	private func eventNameByReplacingInvalidCharacters(eventName: String) -> String {
		var result = eventName.replacingOccurrences(of: ".", with: "_")

		let validChars = NSMutableCharacterSet.alphanumeric()
		validChars.addCharacters(in: "_")

		let unwantedCharacters = validChars.inverted

		let validCharsComponents = result.components(separatedBy: unwantedCharacters)
		if validCharsComponents.count > 1 {
			result = validCharsComponents.joined(separator: "")
		}
		if result.count > Constants.eventNameMaxLength {
			result = String(result.suffix(Constants.eventNameMaxLength))
		}
		if result.starts(with: "_") {
			result = String(result.dropFirst())
		}
		if let firstChar = result.first, !firstChar.isLetter {
			result = String("A" + result.dropFirst())
		}
		return result
	}
	#else
	private func logFirebaseAnalyticsUnsupported() {
		guard GXLog.isLogEnabled() else {
			return
		}
		GXLog.loggerService().logMessage("Firebase Analytics is unavailable in \(GXClientInformation.osName())", for: .general, with: .debug, logToConsole: false)
	}
	#endif
}

extension GXFirebaseAnalyticsService: GXAnalyticsService {

	public func trackView(_ name: String) {
		#if os(iOS)
		Analytics.logEvent(AnalyticsEventScreenView, parameters: [
			AnalyticsParameterScreenClass: "gx_view"
		])
		#else
		self.logFirebaseAnalyticsUnsupported()
		#endif
	}

	public func trackView(from object: GXSDObjectLocator) {
		GXAnalyticsServiceHelper.defaultAnalyticsService(self, trackViewFromObject: object)
	}

	public func trackEventName(_ name: String, category: String, label: String?, value: NSNumber?) {
		#if os(iOS)
		let eventName = eventNameByReplacingInvalidCharacters(eventName: name)
		guard !eventName.isEmpty else {
			return
		}
		Analytics.logEvent(eventName, parameters: [
			"gx_category": category as NSObject,
			"gx_label": label ?? "" as NSObject,
			"gx_value": value ?? NSNumber(value: 0) as NSObject
		])
		#else
		self.logFirebaseAnalyticsUnsupported()
		#endif
	}

	public func trackEventName(_ name: String, from object: GXSDObjectLocator, sender: Any?) {
		#if os(iOS)
		GXAnalyticsServiceHelper.defaultAnalyticsService(self, trackEventName: name, from: object, sender: sender)
		#else
		self.logFirebaseAnalyticsUnsupported()
		#endif
	}

	public func setUserId(_ userId: String) {
		#if os(iOS)
		Analytics.setUserID(userId)
		#else
		self.logFirebaseAnalyticsUnsupported()
		#endif
	}

	public func setCommerceTrackerId(_ trackerId: String) {
		#if os(iOS)
		guard GXLog.isLogEnabled() else {
			return
		}
		GXLog.loggerService().logMessage("Firebase Analytics setCommerceTrackerId is not supported.", for: .general, with: .debug, logToConsole: false)
		#else
		self.logFirebaseAnalyticsUnsupported()
		#endif
	}

	public func trackPurchaseId(_ purchaseId: String, affiliation: String, revenue: NSNumber, tax: NSNumber, shipping: NSNumber, currencyCode: String) {
		#if os(iOS)
		Analytics.logEvent(AnalyticsEventPurchase, parameters: [
			AnalyticsParameterTransactionID: purchaseId,
			AnalyticsParameterAffiliation: affiliation,
			AnalyticsParameterValue: revenue,
			AnalyticsParameterTax: tax,
			AnalyticsParameterShipping: shipping,
			AnalyticsParameterCurrency: currencyCode
		])
		#else
		self.logFirebaseAnalyticsUnsupported()
		#endif
	}

	public func trackPurchasedItem(_ productId: String, purchaseId: String, name: String, category: String, price: NSNumber, quantity: NSNumber, currencyCode: String) {
		#if os(iOS)
		Analytics.logEvent(AnalyticsEventAddToCart, parameters: [
			AnalyticsParameterItemID: productId,
			AnalyticsParameterTransactionID: purchaseId,
			AnalyticsParameterItemName: name,
			AnalyticsParameterItemCategory: category,
			AnalyticsParameterPrice: price,
			AnalyticsParameterQuantity: quantity,
			AnalyticsParameterCurrency: currencyCode
		])
		#else
		self.logFirebaseAnalyticsUnsupported()
		#endif
	}

	public func trackPanelName(_ name: String) {
		#if DEBUG
		assertionFailure("DEPRECATED")
		#endif
		#if os(iOS)
		Analytics.logEvent(AnalyticsEventScreenView, parameters: [
			AnalyticsParameterScreenClass: "gx_panel"
		])
		#else
		self.logFirebaseAnalyticsUnsupported()
		#endif
	}

	public func trackEventName(_ name: String, category: String, sender: Any?) {
		#if DEBUG
		assertionFailure("DEPRECATED")
		#endif
		#if os(iOS)
		guard GXAnalyticsServiceHelper.shouldTrackEventName(name) else {
			return
		}

		let (label, value) = GXAnalyticsServiceHelper.analyticsSenderValues(sender)
		self.trackEventName(name, category: category, label: label, value: value)
		#else
		self.logFirebaseAnalyticsUnsupported()
		#endif
	}
}

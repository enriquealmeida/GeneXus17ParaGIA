//
//  GXGAMAdsViewProvider.swift
//

import GXCoreUI
import GXCoreModule_SD_Ads
import GoogleMobileAds

internal class GXGAMAdsViewProvider: NSObject, GXUCSDAdsViewProvider {
	
	public required init(layoutElement: GXLayoutElementUserControl) {
		self.layoutElement = layoutElement
		let properties = layoutElement.layoutElementUserControlCustomProperties
		adUnitId = properties.getPropertyValueString("@SDAdsViewAdUnitId") ?? ""
	}
	
	private let layoutElement: GXLayoutElementUserControl
	private var bannerView: GAMBannerView? = nil

	public var adUnitId: String
	{
		didSet {
			if oldValue != adUnitId {
				if let adsView = loadedAdsView {
					let newdAdUnitIDValue: String? = adUnitId.isEmpty ? nil : adUnitId
					if bannerView == adsView {
						bannerView!.adUnitID = newdAdUnitIDValue
					}
				}
			}
		}
	}
	
	internal static func logGADError(_ error: Error) {
		guard let log = GXFoundationServices.loggerService(), log.isLogEnabled else { return }
		let error = error as NSError
		var logType = GXLoggerType.general
		var logLevel = GXLoggerLevel.error
		if error.domain == GADErrorDomain {
			if let code = GADErrorCode(rawValue: error.code) {
				switch code {
				case .noFill, .mediationNoFill:
					logLevel = .info
				case .networkError, .serverError, .timeout:
					logType = .network
				default:
					break
				}
			}
		}
		log.logMessage(error.localizedDescription, for: logType, with: logLevel, logToConsole: true)
	}
	
	// MARK: GXUCSDAdsViewProvider
	
	static func newAdsViewProvider(with layoutElement: GXLayoutElementUserControl) -> GXUCSDAdsViewProvider {
		return GXGAMAdsViewProvider(layoutElement: layoutElement)
	}
	
	public weak var delegate: GXUCSDAdsViewProviderDelegate? = nil
	
	public var loadedAdsView: UIView? { return bannerView }
	
	public func adsView(withFrame frame: CGRect) -> UIView {
		if let adsView = loadedAdsView {
			adsView.frame = frame
			return adsView
		}
		
		let adSize = GADCurrentOrientationAnchoredAdaptiveBannerAdSizeWithWidth(frame.width)
		let bannerView = GAMBannerView(adSize: adSize, origin: frame.origin)
		bannerView.delegate = self
		bannerView.adUnitID = adUnitId.isEmpty ? nil : adUnitId
		bannerView.rootViewController = delegate?.adsViewProviderContainerViewController(self)
		self.bannerView = bannerView
		requestAd()
		return bannerView
	}
	
	public func setLoadedAdsViewFrame(_ frame: CGRect) {
		self.loadedAdsView?.frame = frame
	}
	
	public func requestAd() {
		guard let bannerView = loadedAdsView as? GAMBannerView, !adUnitId.isEmpty else { return }

		let request = GAMRequest();
		bannerView.load(request)
	}
	
	public func value(forPropertyName propertyName: String) -> Any? {
		switch propertyName {
		case "adunitid":
			return adUnitId
		default:
			return nil
		}
	}
	
	public func applyPropertyValue(_ propValue: Any?, toPropertyName propName: String) -> Bool {
		switch propName {
		case "adunitid":
			adUnitId = GXUtilities.string(from: propValue) ?? ""
			return true
		default:
			return false
		}
	}
}

extension GXGAMAdsViewProvider: GADBannerViewDelegate {
	
	public func bannerViewDidReceiveAd(_ bannerView: GADBannerView) {
		guard bannerView == self.bannerView else { return }
		delegate?.adsViewProviderDidReceiveAd?(self)
	}
	
	public func bannerView(_ bannerView: GADBannerView, didFailToReceiveAdWithError error: Error) {
		guard bannerView == self.bannerView else { return }
		Self.logGADError(error)
		delegate?.adsViewProvider?(self, didFailToReceiveAdWithError: error)
	}
}

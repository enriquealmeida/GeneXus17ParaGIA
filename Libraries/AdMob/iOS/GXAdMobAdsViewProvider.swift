//
//  GXAdMobAdsViewProvider.swift
//

import GXCoreUI
import GXCoreModule_SD_Ads
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
import GoogleMobileAds
$endif$

internal class GXAdMobAdsViewProvider: NSObject, GXUCSDAdsViewProvider {
	
	public required init(layoutElement: GXLayoutElementUserControl) {
		self.layoutElement = layoutElement
		let properties = layoutElement.layoutElementUserControlCustomProperties
		adUnitId = properties.getPropertyValueString("@SDAdsViewAdUnitId") ?? ""
	}
	
	private let layoutElement: GXLayoutElementUserControl
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
	private var bannerView: GADBannerView? = nil
$else$
	private var bannerView: UILabel? = nil
$endif$

	public var adUnitId: String
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
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
$endif$
	
	// MARK: GXUCSDAdsViewProvider
	
	static func newAdsViewProvider(with layoutElement: GXLayoutElementUserControl) -> GXUCSDAdsViewProvider {
		return GXAdMobAdsViewProvider(layoutElement: layoutElement)
	}
	
	public weak var delegate: GXUCSDAdsViewProviderDelegate? = nil
	
	public var loadedAdsView: UIView? { return bannerView }
	
	public func adsView(withFrame frame: CGRect) -> UIView {
		if let adsView = loadedAdsView {
			adsView.frame = frame
			return adsView
		}
		
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
		let adSize = GADCurrentOrientationAnchoredAdaptiveBannerAdSizeWithWidth(frame.width)
		let bannerView = GADBannerView(adSize: adSize, origin: frame.origin)
		bannerView.delegate = self
		bannerView.adUnitID = adUnitId.isEmpty ? nil : adUnitId
		bannerView.rootViewController = delegate?.adsViewProviderContainerViewController(self)
$else$
		let bannerView = UILabel(frame: frame)
		bannerView.adjustsFontSizeToFitWidth = true
		bannerView.textAlignment = .center
		bannerView.text = "AdMob Application Id Missing"
$endif$
		self.bannerView = bannerView
		requestAd()
		return bannerView
	}
	
	public func setLoadedAdsViewFrame(_ frame: CGRect) {
		self.loadedAdsView?.frame = frame
	}
	
	public func requestAd() {
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
		guard loadedAdsView != nil && !adUnitId.isEmpty else { return }

		let request = GADRequest();
		bannerView?.load(request)
$endif$
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

$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
extension GXAdMobAdsViewProvider: GADBannerViewDelegate {
	
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
$endif$

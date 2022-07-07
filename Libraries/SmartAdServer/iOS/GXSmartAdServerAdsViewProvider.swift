//
//  GXSmartAdServerAdsViewProvider.swift
//

import GXCoreUI
import GXCoreModule_SD_Ads
import SASDisplayKit

internal class GXSmartAdServerAdsViewProvider: NSObject, GXUCSDAdsViewProvider, SASBannerViewDelegate {
	
	public required init(layoutElement: GXLayoutElementUserControl) {
		self.layoutElement = layoutElement
	}
	
	private let layoutElement: GXLayoutElementUserControl
	private var bannerView: SASBannerView? = nil
	
	// MARK: GXUCSDAdsViewProvider
	
	static func newAdsViewProvider(with layoutElement: GXLayoutElementUserControl) -> GXUCSDAdsViewProvider {
		return GXSmartAdServerAdsViewProvider(layoutElement: layoutElement)
	}
	
	public weak var delegate: GXUCSDAdsViewProviderDelegate? = nil
	
	public var loadedAdsView: UIView? { return bannerView }
	
	public func adsView(withFrame frame: CGRect) -> UIView {
		if let bannerView = bannerView {
			bannerView.frame = frame
		}
		else {
			let bannerView = SASBannerView(frame: frame, loader: .activityIndicatorStyleWhite)
			self.bannerView = bannerView
			bannerView.autoresizingMask = []
			bannerView.translatesAutoresizingMaskIntoConstraints = true
			bannerView.delegate = self
			bannerView.modalParentViewController = delegate?.adsViewProviderContainerViewController(self)
			let siteId = Int(SASConfiguration.shared.siteId)
			let properties = layoutElement.layoutElementUserControlCustomProperties
			let formatId = properties.getPropertyValueInteger("@SDAdsViewSmartAdServerFormatId")
			let pageId = properties.getPropertyValueInteger("@SDAdsViewSmartAdServerPageId")
			let target = GXUtilities.nonEmptyString(from: properties.getPropertyValue("@SDAdsViewSmartAdServerTarget"))
			let master = properties.getPropertyValueBool("@SDAdsViewSmartAdServerIsMaster")
			let adPlacement = SASAdPlacement.init(siteId: siteId, pageId: pageId, formatId: formatId, keywordTargeting: target, master: master)
			bannerView.load(with: adPlacement)
		}
		return bannerView!
	}
	
	public func setLoadedAdsViewFrame(_ frame: CGRect) {
		loadedAdsView?.frame = frame
	}
	
	public func requestAd() {
		bannerView?.refresh()
	}
	
	// MARK: SASBannerViewDelegate
	
	func bannerViewDidLoad(_ bannerView: SASBannerView) {
		guard bannerView == self.bannerView else {
			return
		}
		
		delegate?.adsViewProviderDidReceiveAd?(self)
	}
	
	func bannerView(_ bannerView: SASBannerView, didFailToLoadWithError error: Error) {
		guard bannerView == self.bannerView else {
			return
		}
		
		delegate?.adsViewProvider?(self, didFailToReceiveAdWithError: error)
	}
}

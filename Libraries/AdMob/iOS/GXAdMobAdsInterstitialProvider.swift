//
//  GXAdMobAdsInterstitialProvider.swift
//

import GXCoreUI
import GXCoreModule_SD_Ads
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
import GoogleMobileAds
$endif$

internal class GXAdMobAdsInterstitialProvider: NSObject, GXSDAdsInterstitialProvider {
	
	private override init() { super.init() }
	
	static let shared = GXAdMobAdsInterstitialProvider()

$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
	private class AdContext {
		public var loadedAd: GADInterstitialAd? = nil
		public var showCompletion: ((_ wasPresented: Bool) -> Void)? = nil
	}

	private var interstitialsByAdUnitID: [String : AdContext] = [:]
	
	fileprivate func removeInterstitialIfMatches(_ ad: GADInterstitialAd, wasPresented: Bool) {
		gx_dispatch_sync_on_main_queue {
			guard let adContext = self.interstitialsByAdUnitID[ad.adUnitID], adContext.loadedAd == ad else { return }
			self.removeInterstitial(adUnitID: ad.adUnitID, wasPresented: wasPresented)
		}
	}
	
	fileprivate func removeInterstitial(adUnitID: String, wasPresented: Bool) {
		gx_dispatch_sync_on_main_queue {
			guard let adContext = self.interstitialsByAdUnitID[adUnitID] else { return }
			self.interstitialsByAdUnitID.removeValue(forKey: adUnitID)
			adContext.showCompletion?(wasPresented)
		}
	}
$endif$
	
	private static let kAdMobTestAdUnitID = "ca-app-pub-3940256099942544/4411468910"
	
	// MARK: GXAdsInterstitialProvider

	var delegate: GXSDAdsInterstitialProviderDelegate?

	func requestInterstitial(adUnitID: String) {
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
		guard !adUnitID.isEmpty else { return }
		gx_dispatch_on_main_queue {
			guard self.interstitialsByAdUnitID[adUnitID] == nil else { return }
			let adContext = AdContext()
			self.interstitialsByAdUnitID[adUnitID] = adContext
			GADInterstitialAd.load(withAdUnitID: adUnitID, request: nil) { [weak self] ad, error in
				guard let sself = self else { return }
				guard let ad = ad else {
					if let error = error {
						GXAdMobAdsViewProvider.logGADError(error)
					}
					sself.removeInterstitial(adUnitID: adUnitID, wasPresented: false)
					sself.delegate?.adsInterstitialProvider(sself, didFailToLoad: adUnitID)
					return
				}
				adContext.loadedAd = ad
				ad.fullScreenContentDelegate = self
				sself.delegate?.adsInterstitialProvider(sself, didLoad: ad.adUnitID)
			}
		}
$endif$
	}

	func isInterstitialReady(adUnitID: String, completion: @escaping (Bool) -> Void) {
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
		gx_dispatch_on_main_queue {
			var isReady = false
			if let adContext = self.interstitialsByAdUnitID[adUnitID] {
				isReady = adContext.loadedAd != nil && adContext.showCompletion == nil
			}
			completion(isReady)
		}
$else$
		completion(false)
$endif$
	}

	func showInterstitial(adUnitID: String, from controller: UIViewController, completion: @escaping (Bool) -> Void) {
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
		gx_dispatch_on_main_queue {
			guard let adContext = self.interstitialsByAdUnitID[adUnitID], let ad = adContext.loadedAd, adContext.showCompletion == nil else {
				completion(false)
				return
			}

			do {
				try ad.canPresent(fromRootViewController: controller)
			} catch {
				if let log = GXFoundationServices.loggerService(), log.isLogEnabled {
					log.logMessage(error.localizedDescription, for: .general, with: .error, logToConsole: true)
				}
				completion(false)
				return
			}

			#if DEBUG
			assert(adContext.showCompletion == nil)
			#endif
			adContext.showCompletion = completion
			ad.present(fromRootViewController: controller)
		}
$else$
		completion(false)
$endif$
	}

	func requestTestInterstitial() {
		requestInterstitial(adUnitID: Self.kAdMobTestAdUnitID)
	}

	func isTestInterstitialReady(completion: @escaping (Bool) -> Void) {
		isInterstitialReady(adUnitID: Self.kAdMobTestAdUnitID, completion: completion)
	}

	func showTestInterstitial(from controller: UIViewController, completion: @escaping (Bool) -> Void) {
		showInterstitial(adUnitID: Self.kAdMobTestAdUnitID, from: controller, completion: completion)
	}
}

$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
extension GXAdMobAdsInterstitialProvider: GADFullScreenContentDelegate {
	func adDidRecordClick(_ ad: GADFullScreenPresentingAd) {
		guard let ad = ad as? GADInterstitialAd else { return }
		delegate?.adsInterstitialProvider(self, didClick: ad.adUnitID)
	}
	
	func adDidPresentFullScreenContent(_ ad: GADFullScreenPresentingAd) {
		guard let ad = ad as? GADInterstitialAd else { return }
		removeInterstitialIfMatches(ad, wasPresented: true)
	}
	
	func ad(_ ad: GADFullScreenPresentingAd, didFailToPresentFullScreenContentWithError error: Error) {
		guard let ad = ad as? GADInterstitialAd else { return }
		removeInterstitialIfMatches(ad, wasPresented: false)
	}
	
	func adDidDismissFullScreenContent(_ ad: GADFullScreenPresentingAd) {
		guard let ad = ad as? GADInterstitialAd else { return }
		removeInterstitialIfMatches(ad, wasPresented: true)
		delegate?.adsInterstitialProvider(self, didClose: ad.adUnitID)
	}
}
$endif$

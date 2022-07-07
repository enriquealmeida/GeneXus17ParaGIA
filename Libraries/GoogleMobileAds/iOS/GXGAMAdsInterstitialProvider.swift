//
//  GXGAMAdsInterstitialProvider.swift
//

import GXCoreUI
import GXCoreModule_SD_Ads
import GoogleMobileAds

internal class GXGAMAdsInterstitialProvider: NSObject, GXSDAdsInterstitialProvider {

	private override init() { super.init() }

	static let shared = GXGAMAdsInterstitialProvider()

	private class AdContext {
		public var loadedAd: GAMInterstitialAd? = nil
		public var showCompletion: ((_ wasPresented: Bool) -> Void)? = nil
	}

	private var interstitialsByAdUnitID: [String : AdContext] = [:]
	
	fileprivate func removeInterstitialIfMatches(_ ad: GAMInterstitialAd, wasPresented: Bool) {
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

	private static let kGAMTestAdUnitID = "/6499/example/interstitial"

	// MARK: GXAdsInterstitialProvider

	var delegate: GXSDAdsInterstitialProviderDelegate?

	func requestInterstitial(adUnitID: String) {
		guard !adUnitID.isEmpty else { return }
		gx_dispatch_on_main_queue {
			guard self.interstitialsByAdUnitID[adUnitID] == nil else { return }
			let adContext = AdContext()
			self.interstitialsByAdUnitID[adUnitID] = adContext
			GAMInterstitialAd.load(withAdManagerAdUnitID: adUnitID, request: nil) { [weak self] ad, error in
				guard let sself = self else { return }
				guard let ad = ad else {
					if let error = error {
						GXGAMAdsViewProvider.logGADError(error)
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
	}

	func isInterstitialReady(adUnitID: String, completion: @escaping (Bool) -> Void) {
		gx_dispatch_on_main_queue {
			var isReady = false
			if let adContext = self.interstitialsByAdUnitID[adUnitID] {
				isReady = adContext.loadedAd != nil && adContext.showCompletion == nil
			}
			completion(isReady)
		}
	}

	func showInterstitial(adUnitID: String, from controller: UIViewController, completion: @escaping (Bool) -> Void) {
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
	}

	func requestTestInterstitial() {
		requestInterstitial(adUnitID: Self.kGAMTestAdUnitID)
	}

	func isTestInterstitialReady(completion: @escaping (Bool) -> Void) {
		isInterstitialReady(adUnitID: Self.kGAMTestAdUnitID, completion: completion)
	}

	func showTestInterstitial(from controller: UIViewController, completion: @escaping (Bool) -> Void) {
		showInterstitial(adUnitID: Self.kGAMTestAdUnitID, from: controller, completion: completion)
	}
}

extension GXGAMAdsInterstitialProvider: GADFullScreenContentDelegate {
	func adDidRecordClick(_ ad: GADFullScreenPresentingAd) {
		guard let ad = ad as? GAMInterstitialAd else { return }
		delegate?.adsInterstitialProvider(self, didClick: ad.adUnitID)
	}
	
	func adDidPresentFullScreenContent(_ ad: GADFullScreenPresentingAd) {
		guard let ad = ad as? GAMInterstitialAd else { return }
		removeInterstitialIfMatches(ad, wasPresented: true)
	}
	
	func ad(_ ad: GADFullScreenPresentingAd, didFailToPresentFullScreenContentWithError error: Error) {
		guard let ad = ad as? GAMInterstitialAd else { return }
		removeInterstitialIfMatches(ad, wasPresented: false)
	}
	
	func adDidDismissFullScreenContent(_ ad: GADFullScreenPresentingAd) {
		guard let ad = ad as? GAMInterstitialAd else { return }
		removeInterstitialIfMatches(ad, wasPresented: true)
		delegate?.adsInterstitialProvider(self, didClose: ad.adUnitID)
	}
}

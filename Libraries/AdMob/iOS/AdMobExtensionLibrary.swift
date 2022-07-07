//
//  AdMobExtensionLibrary.swift
//

import GXCoreBL
import GXCoreModule_SD_Ads
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
import GoogleMobileAds
$endif$

@objc(AdMobExtensionLibrary)
public class AdMobExtensionLibrary: NSObject, GXExtensionLibraryProtocol {

	public func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXUCSDAdsViewProviders.registerProvider(GXAdMobAdsViewProvider.self, forName: "admob")
		GXSDAdsInterstitialProviders.registerProvider(GXAdMobAdsInterstitialProvider.shared, forName: "admob")
$if(Main.Dynamic.GXEmptyFilter.AdMob_AppID)$
#if targetEnvironment(simulator)
		GADMobileAds.sharedInstance().requestConfiguration.testDeviceIdentifiers = [ GADSimulatorID ]
#endif
		GADMobileAds.sharedInstance().start(completionHandler: nil)
$else$
		GXFoundationServices.loggerService()?.logMessage("AdMob Application Id Missing", for: .general, with: .error, logToConsole: true)
$endif$
    }
}

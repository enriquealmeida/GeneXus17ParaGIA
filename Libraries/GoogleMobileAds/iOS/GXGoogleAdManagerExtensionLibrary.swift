//
//  GXGoogleAdManagerExtensionLibrary.swift
//

import GXCoreBL
import GXCoreModule_SD_Ads
import GoogleMobileAds

@objc(GXGoogleAdManagerExtensionLibrary)
public class GXGoogleAdManagerExtensionLibrary: NSObject, GXExtensionLibraryProtocol {

	public func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXUCSDAdsViewProviders.registerProvider(GXGAMAdsViewProvider.self, forName: "googlemobileads")
		GXSDAdsInterstitialProviders.registerProvider(GXGAMAdsInterstitialProvider.shared, forName: "googlemobileads")
#if targetEnvironment(simulator)
		GADMobileAds.sharedInstance().requestConfiguration.testDeviceIdentifiers = [ GADSimulatorID ]
#endif
		GADMobileAds.sharedInstance().start(completionHandler: nil)
    }
}

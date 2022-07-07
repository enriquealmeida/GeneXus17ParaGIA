//
//  SmartAdServerExtensionLibrary.swift
//

import GXCoreBL
import GXCoreModule_SD_Ads
import SASDisplayKit

@objc(SmartAdServerExtensionLibrary)
public class SmartAdServerExtensionLibrary: NSObject, GXExtensionLibraryProtocol {

	public func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXUCSDAdsViewProviders.registerProvider(GXSmartAdServerAdsViewProvider.self, forName: "smartad")
		if Constants.siteID != 0 && !Constants.baseURL.isEmpty {
			SASConfiguration.shared.configure(siteId: Constants.siteID, baseURL: Constants.baseURL)
		}
    }
	
	private struct Constants {
$if(Main.Dynamic.SmartAdServer_SiteID)$
		static let siteID = $Main.Dynamic.SmartAdServer_SiteID$
$else$
		static let siteID = 0
$endif$
		static let baseURL = "$Main.Dynamic.SmartAdServer_BaseURL$"
	}
}

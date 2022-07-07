//
//  GXFirebaseAnalyticsExtensionLibrary.swift
//

import GXCoreBL

@objc(GXFirebaseAnalyticsExtensionLibrary)
public class GXFirebaseAnalyticsExtensionLibrary: NSObject, GXExtensionLibraryProtocol {
	
	public func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXCoreBLServices.registerAnalyticsService(GXFirebaseAnalyticsService.sharedInstance)
	}
}

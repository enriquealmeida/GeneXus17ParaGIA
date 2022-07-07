//
//  Animation_LottieExtensionLibrary.swift
//

import GXCoreBL

@objc(LottieAnimationExtensionLibrary)
public class LottieAnimationExtensionLibrary: NSObject, GXExtensionLibraryProtocol {

	public func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
        GXActivityIndicatorViewProvidersManager.register(activityIndicatorViewProvider: LottieActivityIndicatorViewProvider(),
                                                         forIdentifier: LottieActivityIndicatorViewProvider.lottieTypeIdenfifier)
    }
}

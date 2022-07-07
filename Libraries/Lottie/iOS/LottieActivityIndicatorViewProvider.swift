//
//  LottieActivityIndicatorViewProvider.swift
//

import Foundation
import GXCoreUI

internal extension GXStyleClass.PropertyName {
	static let lottieFile = Self("idLottieFile")
}

@objc(LottieActivityIndicatorViewProvider)
public class LottieActivityIndicatorViewProvider: NSObject, GXActivityIndicatorViewProviderProtocol {

	internal static let lottieTypeIdenfifier = "idLottie"

	public func activityIndicatorView() -> GXActivityIndicatorView {
        return LottieActivityIndicatorView()
    }

	public var animationThemeClassExtensionProperties: [GXStyleClass.PropertyName] {
		return [.lottieFile]
	}
}

//
//  GXUCSDAdsViewProvider.swift
//  GXUCMobFox
//
//  Created by Fabian Inthamoussu on 6/4/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

import UIKit
import GXObjectsModel

@objc public protocol GXUCSDAdsViewProviderDelegate {
	
	func adsViewProviderContainerViewController(_ provider: GXUCSDAdsViewProvider) -> UIViewController?
	
	@objc optional func adsViewProviderDidReceiveAd(_ provider: GXUCSDAdsViewProvider)
	@objc optional func adsViewProvider(_ provider: GXUCSDAdsViewProvider, didFailToReceiveAdWithError error: Error)
}

@objc public protocol GXUCSDAdsViewProvider {
	
	static func newAdsViewProvider(with layoutElement: GXLayoutElementUserControl) -> GXUCSDAdsViewProvider
	
	var delegate: GXUCSDAdsViewProviderDelegate? { get set }
	
	var loadedAdsView: UIView? { get }
	func adsView(withFrame frame: CGRect) -> UIView
	func setLoadedAdsViewFrame(_ frame: CGRect)
	
	func requestAd()
	
	@objc optional func value(forPropertyName propertyName: String) -> Any?
	@objc optional func applyPropertyValue(_ propValue: Any?, toPropertyName propName: String) -> Bool
}

internal class GXUCSDAdsViewUnknownProvider: GXUCSDAdsViewProvider {
	
	static func newAdsViewProvider(with layoutElement: GXLayoutElementUserControl) -> GXUCSDAdsViewProvider {
		return GXUCSDAdsViewUnknownProvider()
	}
	
	public weak var delegate: GXUCSDAdsViewProviderDelegate? = nil
	
	public private(set) var loadedAdsView: UIView? = nil
	
	public func adsView(withFrame frame: CGRect) -> UIView {
		if let adsView = loadedAdsView {
			adsView.frame = frame
			return adsView
		}
		else {
			let adsView = UIView(frame: frame)
			loadedAdsView = adsView
			return adsView
		}
	}
	
	public func setLoadedAdsViewFrame(_ frame: CGRect) {
		guard let adsView = loadedAdsView else {
			return
		}
		
		adsView.frame = frame
	}
	
	public func requestAd() {
	}
}

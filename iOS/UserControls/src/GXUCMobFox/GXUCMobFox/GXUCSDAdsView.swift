//
//  GXUCSDAdsView.swift
//  GXUCMobFox
//
//  Created by Fabian Inthamoussu on 6/4/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

import GXCoreUI

@objc(GXUCSDAdsViewProviders)
public class GXUCSDAdsViewProviders: NSObject {
	
	private static var providerTypesByName: [String : GXUCSDAdsViewProvider.Type] = [:]
	
	@objc public class func registerProvider(_ providerType: GXUCSDAdsViewProvider.Type, forName name: String) {
		if providerTypesByName[name] == nil {
			providerTypesByName[name] = providerType
		}
	}
	
	internal class func providerType(forName providerName: String) -> GXUCSDAdsViewProvider.Type? {
		return providerTypesByName[providerName]
	}
}

@objc(GXUCSDAdsView)
public class GXUCSDAdsView: GXControlBaseWithLayout, GXUCSDAdsViewProviderDelegate {
	
	let providerName: String
	let provider: GXUCSDAdsViewProvider
	
	@objc public required init(layoutElement: GXLayoutElement, controlId: UInt, gxMode modeType: GXModeType, indexerStack indexer: [Any]?, parentControl: GXControlContainer?) {
		
		let layoutElementUC = layoutElement as! GXLayoutElementUserControl
		let properties = layoutElementUC.layoutElementUserControlCustomProperties
		providerName = properties.getPropertyValueString("@SDAdsViewAdsProvider") ?? ""
		provider = (GXUCSDAdsViewProviders.providerType(forName: providerName) ?? GXUCSDAdsViewUnknownProvider.self).newAdsViewProvider(with: layoutElementUC)
		super.init(layoutElement: layoutElement, controlId: controlId, gxMode: modeType, indexerStack: indexer, parentControl: parentControl)
		provider.delegate = self
	}
	
	override public func loadContentViews(withContentFrame contentFrame: CGRect, intoContainerView containerView: UIView) {
		let adsView = provider.adsView(withFrame: contentFrame)
		containerView.addSubview(adsView)
	}
	
	override public func layoutContentViews(withContentFrame contentFrame: CGRect) {
		provider.setLoadedAdsViewFrame(contentFrame)
	}
	
	override public func value(forPropertyName propertyName: String) -> Any? {
		return super.value(forPropertyName: providerName) ?? provider.value?(forPropertyName: providerName)
	}
	
	override public func applyPropertyValue(_ propValue: Any?, toPropertyName propName: String) -> Bool {
		return super.applyPropertyValue(propValue, toPropertyName: propName) || (provider.applyPropertyValue?(propValue, toPropertyName: propName) ?? false)
	}
	
	override public func executeMethod(_ methodName: String, withParameters parameters: [Any]) -> Any? {
		if methodName == "requestad" {
			provider.requestAd()
		}
		return nil
	}
	
	// MARK: GXUCSDAdsViewProviderDelegate
	
	public func adsViewProviderContainerViewController(_ provider: GXUCSDAdsViewProvider) -> UIViewController? {
		guard provider === self.provider else {
			return nil
		}
		
		return self.parentControl?.context.entityController?.gxUserInterfaceController
	}
	
	public func adsViewProviderDidReceiveAd(_ provider: GXUCSDAdsViewProvider) {
		guard provider === self.provider else {
			return
		}
		
		self.loadedView?.setNeedsLayout()
		self.visible = true
	}
	
	public func adsViewProvider(_ provider: GXUCSDAdsViewProvider, didFailToReceiveAdWithError error: Error) {
		guard provider === self.provider else {
			return
		}
		
		self.loadedView?.setNeedsLayout()
		self.visible = false
	}
}

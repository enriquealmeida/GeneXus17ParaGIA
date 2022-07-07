//
//  GXUC_MapList+Animations.swift
//  GXUCMaps-iOS
//
//  Created by José Echagüe on 7/13/18.
//  Copyright © 2018 GeneXus. All rights reserved.
//

import CoreLocation
import GXFoundation
import ObjectiveC

private var GXSDMapsAnimationLayerKey = "GXSDMapsAnimationLayerKey"
private var GXSDMapsAnimationsKey = "GXSDMapsAnimationsKey"

extension GXUC_MapList {
	
	//MARK: - Public Helpers
	
	@objc public func animationKey(atIndex index: UInt) -> String? {
		var animationKey: String?
		if let animationKeyAttribute = self.animationKeyAttribute {
			let value = self.value(forEntityDataFieldName: animationKeyAttribute,
								   fieldSpecifier: self.animationKeyFieldSpecifier,
								   forEntityAt: index,
								   section: 0,
								   enitityData: nil,
								   fieldDescriptor: nil,
								   indexer: nil,
								   indexedFieldSpecifier: nil)
			if let value = GXUtilities.string(from: value), value.count > 0 {
				animationKey = value
			}
		}
		return animationKey
	}
	
	@objc public func animationDuration(atIndex index: UInt) -> TimeInterval {
		var animationDuration = self.animationDefaultDuration
		
		if let animationDurationAttribute = self.animationDurationAttribute {
			let value = self.value(forEntityDataFieldName: animationDurationAttribute,
								   fieldSpecifier: self.animationDurationFieldSpecifier,
								   forEntityAt: index,
								   section: 0,
								   enitityData: nil,
								   fieldDescriptor: nil,
								   indexer: nil,
								   indexedFieldSpecifier: nil)
			if let value = GXUtilities.floatNumber(fromValue: value)?.floatValue, value > 0 {
				animationDuration = TimeInterval(value)
			}
		}
		return animationDuration
	}
	
	@objc public func animationEndBehavior(atIndex index: UInt) -> GXUCMapListAnimationEndBehavior {
		var animationEndBehavior = self.animationEndBehavior
		
		if let animationEndBehaviorAttribute = self.animationEndBehaviorAttribute {
			let value = self.value(forEntityDataFieldName: animationEndBehaviorAttribute,
								   fieldSpecifier: self.animationEndBehaviorFieldSpecifier,
								   forEntityAt: index,
								   section: 0,
								   enitityData: nil,
								   fieldDescriptor: nil,
								   indexer: nil,
								   indexedFieldSpecifier: nil)
			if let value = value as? String {
				animationEndBehavior = self.convertAnimationEndBehavior(from: value)
			}
		}
		return animationEndBehavior
	}
	
	//MARK: - Animations Layer
	
	@objc internal func update(annotation: GXUC_MapPinAnnotation,
							   toPosition position: CLLocationCoordinate2D,
							   withDuration duration: TimeInterval,
							   repeatBehavior: GXUCMapListAnimationEndBehavior) {
		if #available(iOSApplicationExtension 10.0, *) {
			// Stop current animation (if any)
			if (annotation.forwardAnimator?.isRunning ?? false) {
				annotation.forwardAnimator?.stopAnimation(false)
				annotation.forwardAnimator?.finishAnimation(at: .current)
			} else if (annotation.backwardAnimator?.isRunning ?? false) {
				annotation.backwardAnimator?.stopAnimation(false)
				annotation.backwardAnimator?.finishAnimation(at: .current)
			}
			
			let initialPosition = annotation.coordinate
			let animation = {
				annotation.coordinate = position
			}
			
			let animator = UIViewPropertyAnimator.init(duration: duration, curve: .linear, animations: animation)
			
			switch repeatBehavior {
			case .disappear:
				let animatorCompletion: (UIViewAnimatingPosition) -> Void = { [weak self] (position) in
					if let animationKey = annotation.animationKey {
						self?.removeAnimatedAnnotation(withKey: animationKey)
					}
				}
				animator.addCompletion(animatorCompletion)
				
			case .repeat:
				let backwardAnimation = {
					annotation.coordinate = initialPosition
				}
				
				var animatorCompletion: ((UIViewAnimatingPosition) -> Void)!
				annotation.backwardAnimator = UIViewPropertyAnimator.init(duration: duration, curve: .linear)
				
				let backwardCompletion: (UIViewAnimatingPosition) -> Void = { (position) in
					if (position == .end) {
						annotation.forwardAnimator?.addAnimations(animation)
						annotation.forwardAnimator?.addCompletion(animatorCompletion)
						annotation.forwardAnimator?.startAnimation()
					}
				}
				
				animatorCompletion = { (position) in
					if (position == .end) {
						annotation.backwardAnimator?.addAnimations(backwardAnimation)
						annotation.backwardAnimator?.addCompletion(backwardCompletion)
						annotation.backwardAnimator?.startAnimation()
					}
				}
				animator.addCompletion(animatorCompletion)
				
			default:
				break
			}
			
			annotation.forwardAnimator = animator
			animator.startAnimation()
		}
	}
}

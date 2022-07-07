//
//  GXUCAnimationView.swift
//  GXUCAnimationView
//
//  Created by José Echagüe on 7/24/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

import Foundation
import GXCoreUI
import Lottie

@objc(GXUCAnimationViewControl)
public class GXUCAnimationViewControl: GXControlBaseWithLayout
{
	struct Constants {
		static let PROPERTY_ANIMATION_NAME  = "animationName"
		static let PROPERTY_LOOPING         = "looping"
		static let PROPERTY_PROGRESS        = "progress"
        
		static let METHOD_SET_ANIMATION     = "setanimation"
		static let METHOD_SET_PROGRESS      = "setprogress"
		static let METHOD_PLAY              = "play"
		static let METHOD_PAUSE             = "pause"
	}
	
	public var layoutElementUserControl: GXLayoutElementUserControl {
		return layoutElement as! GXLayoutElementUserControl
	}
	
	@objc public var AnimationName: String? {
		didSet {
			animationViewReuseContext?.restoreLayoutElementAnimationName = false
			if AnimationName?.isEmpty ?? false {
				AnimationName = nil
			}
			if oldValue != AnimationName && isViewLoaded {
				loadLottieAnimation()
			}
		}
	}
	
	private var mLooping: Bool
	@objc public var Looping: Bool {
		get {
			guard let lotAnimation = lotAnimation else { return mLooping }
			return lotAnimation.loopMode == .loop
		}
		set {
			animationViewReuseContext?.restoreLayoutElementLooping = false
			mLooping = newValue
			if let lotAnimation = lotAnimation {
				let loopMode: LottieLoopMode = mLooping ? .loop : .playOnce
				if lotAnimation.loopMode != loopMode {
					lotAnimation.loopMode = loopMode
				}
			}
		}
	}
	
	private var mProgress: AnimationProgressTime {
		didSet {
			let validValue = type(of: self).validProgressValue(mProgress)
			if mProgress != validValue {
				mProgress = validValue
			}
		}
	}
	
	@objc public var Progress: CGFloat {
		get {
			guard let lotAnimation = lotAnimation else { return mProgress }
			return lotAnimation.currentProgress
		}
		set {
			animationViewReuseContext?.restoreLayoutElementProgress = false
			mProgress = newValue
			lotAnimation?.currentProgress = mProgress
		}
	}
	
	private typealias GXUCAnimationViewControlPlayingState = (from: AnimationProgressTime?, to: AnimationProgressTime?)?
	
	private static let playingStateDefaultValue: GXUCAnimationViewControlPlayingState = nil
	
	private var mPlayingState = playingStateDefaultValue
	
	private func setPlayingState(_ state: GXUCAnimationViewControlPlayingState) {
		animationViewReuseContext?.restoreIsPlayingDefault = false
		if let animView = lotAnimation, animView.isAnimationPlaying, let state = state, let oldState = mPlayingState {
			if state.from != oldState.from || state.to != oldState.to {
				animView.pause()
			}
		}
		mPlayingState = state
		updateIsAnimationPlayingIfNeeded()
	}
	
	@objc public var isPlaying: Bool {
		get {
			return lotAnimation?.isAnimationPlaying ?? (mPlayingState != nil)
		}
		set {
			setPlayingState(newValue ? (from: mPlayingState?.from, to: mPlayingState?.to) : nil)
		}
	}
	
	private func updateIsAnimationPlayingIfNeeded() {
		if let animView = lotAnimation, (mPlayingState != nil) != animView.isAnimationPlaying {
			if let state = mPlayingState {
				let completion = playCompletionHandler(withCurrentAnimView: animView)
				if let playTo = state.to {
					animView.play(fromProgress: state.from, toProgress: playTo, loopMode: nil, completion: completion)
				} else {
					animView.play(completion: completion)
				}
			}
			else {
				animView.pause();
			}
		}
	}
    
	private func playCompletionHandler(withCurrentAnimView animView: AnimationView) -> LottieCompletionBlock {
		return { [weak self, weak animView] (completed) in
			guard let sself = self, let animView = animView, sself.lotAnimation == animView else { return }
			
			if completed {
				sself.mPlayingState = animView.isAnimationPlaying ? (from: nil, to: nil) : nil
			}
			else {
				sself.loadedView?.setNeedsLayout()
			}
		}
	}

	
	var lotAnimation: AnimationView?
	
	// MARK: - Overrides
	
	required public init(layoutElement: GXLayoutElement, controlId: UInt, gxMode modeType: GXModeType, indexerStack indexer: [Any]?, parentControl: GXControlContainer?) {
		let layoutElementUserControl = layoutElement as! GXLayoutElementUserControl
		let selfClass = type(of: self)
		AnimationName = selfClass.animationName(from: layoutElementUserControl)
		mLooping      = selfClass.looping(from: layoutElementUserControl)
		mProgress     = selfClass.validProgressValue(selfClass.progress(from: layoutElementUserControl))
		super.init(layoutElement: layoutElement, controlId: controlId, gxMode: modeType, indexerStack: indexer, parentControl: parentControl)
	}
	
	override public func loadContentViews(withContentFrame contentFrame: CGRect, intoContainerView containerView: UIView) {
		loadLottieAnimation(withFrame: contentFrame, intoContainerView: containerView)
	}
	
	override public func layoutContentViews(withContentFrame contentFrame: CGRect) {
		if let animView = lotAnimation {
			animView.frame = contentFrame
			updateIsAnimationPlayingIfNeeded()
		}
	}
	
	override public func unloadView() {
		lotAnimation = nil
		super.unloadView()
	}
	
	override public func applyPropertyValue(_ propValue: Any?, toPropertyName propName: String) -> Bool {
		guard !super.applyPropertyValue(propValue, toPropertyName: propName) else {
			return true
		}
		
		switch (propName) {
		case Constants.PROPERTY_ANIMATION_NAME:
			AnimationName = GXUtilities.string(from: propValue)
		case Constants.PROPERTY_LOOPING:
			Looping = GXUtilities.bool(fromValue: propValue)
		case Constants.PROPERTY_PROGRESS:
			Progress = CGFloat(GXUtilities.floatNumber(fromValue: propValue)?.doubleValue ?? 0)
		default:
			return false
		}
		return true
	}
	
	override public func value(forPropertyName propertyName: String) -> Any? {
		switch (propertyName) {
		case Constants.PROPERTY_ANIMATION_NAME:
			return AnimationName ?? ""
		case Constants.PROPERTY_LOOPING:
			return NSNumber(value: Looping)
		case Constants.PROPERTY_PROGRESS:
			return NSNumber(value: Double(Progress))
		default:
			return super.value(forKey: propertyName)
		}
	}
	
	override public func executeMethod(_ methodName: String, withParameters parameters: [Any]) -> Any? {
		switch methodName {
		case Constants.METHOD_SET_ANIMATION:
			if (parameters.count == 2) {
				_ = applyPropertyValue(parameters[1], toPropertyName: Constants.PROPERTY_LOOPING)
				_ = applyPropertyValue(parameters[0], toPropertyName: Constants.PROPERTY_ANIMATION_NAME)
			}
			
		case Constants.METHOD_SET_PROGRESS:
			if (parameters.count == 1) {
				_ = applyPropertyValue(parameters[0], toPropertyName: Constants.PROPERTY_PROGRESS)
			}
			
		case Constants.METHOD_PLAY:
			let selfClass = type(of: self)
			let firstParam = parameters.count > 0 ? selfClass.validProgressValue(CGFloat(GXUtilities.floatNumber(fromValue: parameters[0])?.doubleValue ?? 0)) : nil
			let secondParam = parameters.count > 1 ? selfClass.validProgressValue(CGFloat(GXUtilities.floatNumber(fromValue: parameters[1])?.doubleValue ?? 0)) : nil
			let playingState: GXUCAnimationViewControlPlayingState
			switch (parameters.count) {
			case 2:
				playingState = (from: firstParam, to: secondParam)
			case 1:
				playingState = (from: nil, to: firstParam)
			default:
				playingState = (from: nil, to: nil)
			}
			setPlayingState(playingState)
			
		case Constants.METHOD_PAUSE:
			isPlaying = false
			
		default:
			return super.executeMethod(methodName, withParameters: parameters)
		}
		return nil
	}
	
	// MARK: - Private Helpers
	
	private func loadLottieAnimation() {
		guard let containerView = lotAnimation?.superview ?? loadedContainerView else { return }
		loadLottieAnimation(withFrame: lotAnimation?.frame ?? contentFrame(), intoContainerView: containerView)
	}
	
	private func loadLottieAnimation(withFrame frame: CGRect, intoContainerView containerView: UIView) {
		if let animationName = AnimationName {
			let animationView = lotAnimation ?? AnimationView(frame: frame)
			let newView = lotAnimation == nil
			if newView {
				lotAnimation = animationView
				animationView.contentMode = .scaleAspectFit
				containerView.addSubview(animationView)
			}
			else {
				animationView.frame = frame
			}
			let loopMode: LottieLoopMode = mLooping ? .loop : .playOnce
			if animationView.loopMode != loopMode {
				animationView.loopMode = loopMode
			}
			let realAnimationName = "\(animationName.lowercased())_animation"
			animationView.animation = Animation.named(realAnimationName)
			animationView.currentProgress = mProgress // After set animation
			updateIsAnimationPlayingIfNeeded()
		}
		else {
			lotAnimation?.removeFromSuperview()
			lotAnimation = nil
		}
	}
	
	private class func animationName(from layoutElement: GXLayoutElementUserControl) -> String? {
		let props = layoutElement.layoutElementUserControlCustomProperties
		return GXUtilities.nonEmptyString(from: props.getPropertyValue(Constants.PROPERTY_ANIMATION_NAME))
	}
	
	private class func looping(from layoutElement: GXLayoutElementUserControl) -> Bool {
		let props = layoutElement.layoutElementUserControlCustomProperties
		return props.getPropertyValueBool(Constants.PROPERTY_LOOPING)
	}
	
	private class func progress(from layoutElement: GXLayoutElementUserControl) -> CGFloat {
		let props = layoutElement.layoutElementUserControlCustomProperties
		return CGFloat(props.getPropertyValueFloat(Constants.PROPERTY_PROGRESS))
	}
	
	private class func validProgressValue(_ value: AnimationProgressTime) -> AnimationProgressTime {
		return (value < 0) ? 0 : (value > 1 ? 1 : value)
	}
	
	// MARK: - Reuse
	
	public var animationViewReuseContext: GXUCAnimationViewReuseContext? {
		return reuseContext as? GXUCAnimationViewReuseContext
	}
	
	override public func reuseContextClass() -> AnyClass {
		return GXUCAnimationViewReuseContext.self
	}
	
	#if DEBUG
	override public func prepareForReuseViews() {
		super.prepareForReuseViews()
		assert(animationViewReuseContext!.isKind(of: GXUCAnimationViewReuseContext.self), "\(NSStringFromSelector(#selector(reuseContextClass))) must be subclass of \(NSStringFromClass(GXUCAnimationViewReuseContext.self))")
	}
	#endif
	
	override public func endPrepareForReuseViews() {
		if let context = animationViewReuseContext {
			if (context.restoreLayoutElementAnimationName) {
				AnimationName = type(of: self).animationName(from: layoutElementUserControl)
			}
			if (context.restoreLayoutElementProgress) {
				Progress = type(of: self).progress(from: layoutElementUserControl)
			}
			if (context.restoreLayoutElementLooping) {
				Looping = type(of: self).looping(from: layoutElementUserControl)
			}
			if (context.restoreIsPlayingDefault) {
				setPlayingState(type(of: self).playingStateDefaultValue)
			}
			#if DEBUG
			assert(!context.restoreLayoutElementAnimationName, "\(NSStringFromSelector(#selector(getter: type(of: self).AnimationName))) not being restored?")
			assert(!context.restoreLayoutElementProgress, "\(NSStringFromSelector(#selector(getter: type(of: self).Progress))) not being restored?")
			assert(!context.restoreLayoutElementLooping, "\(NSStringFromSelector(#selector(getter: type(of: self).Looping))) not being restored?")
			assert(!context.restoreIsPlayingDefault, "\(NSStringFromSelector(#selector(getter: type(of: self).isPlaying))) not being restored?")
			#endif
		}
		super.endPrepareForReuseViews()
	}
}

public class GXUCAnimationViewReuseContext: GXControlBaseReuseContext {
	public var restoreLayoutElementAnimationName = true
	public var restoreLayoutElementLooping = true
	public var restoreLayoutElementProgress = true
	public var restoreIsPlayingDefault = true
}

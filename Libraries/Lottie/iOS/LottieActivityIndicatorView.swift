//
//  LottieActivityIndicatorView.swift
//

import Foundation
import GXCoreUI
import Lottie

@objc(LottieActivityIndicatorView)
public class LottieActivityIndicatorView : UIView, GXActivityIndicatorView, GXActivityIndicatorViewWrapped {

    private var progress: AnimationProgressTime?
    
	private var animationName: String? {
		didSet {
			if animationName != oldValue &&
				lottieAnimationView != nil &&
				(animationName == nil || isAnimating) {

				reloadAnimationView()
			}
		}
	}
	private var width: CGFloat? {
		didSet {
			if oldValue != width && lottieAnimationView != nil {
				view.setNeedsLayout()
				view.superview?.setNeedsLayout()
			}
		}
	}
	private var height: CGFloat? {
		didSet {
			if oldValue != width && lottieAnimationView != nil {
				view.setNeedsLayout()
				view.superview?.setNeedsLayout()
			}
		}
	}
    private var lottieAnimationView: AnimationView?

    // MARK: - GXActivityIndicatorView Protocol

    public func startAnimating() {
		isAnimating = true
    }

    public func stopAnimating() {
		isAnimating = false
    }

	public private(set) var isAnimating: Bool = false {
		didSet {
			if (isAnimating) {
				if let lottieView = lottieAnimationView {
					lottieView.isHidden = false
                    if (progress == nil) {
                        lottieView.play()
                    }
				}
				else {
					reloadAnimationView()
				}
			}
			else if let lottieView = lottieAnimationView {
				lottieView.isHidden = true
				lottieView.pause()
			}
		}
	}

    public var view: UIView {
        get {
            return self
        }
    }
    
    public func setProgress(_ newProgress: CGFloat) {
        if let progress = progress, newProgress != progress {
            self.lottieAnimationView?.play(fromProgress: progress, toProgress: newProgress, loopMode: nil, completion: nil)
        }
        self.progress = (newProgress != 1.0) ? newProgress : 0.0
    }

	// MARK: - GXActivityIndicatorViewWrapped

	public var wrappedActivityIndicatorViewPreferredMessagePosition: UIRectEdge {
		return .bottom
	}

	public var wrappedActivityIndicatorViewLayoutStyle: GXActivityIndicatorViewWrappedLayoutStyle {
		return .centerActivityIndicatorViewMessage
	}

    // MARK - UIView Overrides

	override public func apply(styleClass: GXStyleClass?, propertyDefaultResolver: GXStyleClassPropertyDefaultResolver?) {
        if let styleClass = styleClass {
            animationName = getAnimationName(styleClass: styleClass)
        }
    }

    public override func layoutSubviews() {
		if let lottieView = lottieAnimationView {
			var size = view.bounds.size
			let center = CGPoint(x: size.width * 0.5, y: size.height * 0.5)
			if let width = width {
				size.width = width
			}
			if let height = height {
				size.height = height
			}
			let frame = CGRect(origin: CGPoint(x: center.x - (size.width * 0.5),
			                                   y: center.y - (size.height * 0.5)),
			                   size: size)
			lottieView.frame = GXPixelAlignedFrameWithMainScreenScale(frame)
		}
    }

	override public func sizeThatFits(_ size: CGSize) -> CGSize {
		return CGSize(width: width ?? size.width, height: height ?? size.height)
	}

    // MARK: - Private Helpers

    private func reloadAnimationView() {
        if let animationName = animationName {
			let animation = Animation.named(animationName)
			let lottieView = lottieAnimationView ?? AnimationView(animation: animation)
			let loopMode: LottieLoopMode = (progress == nil) ? .loop : .playOnce
			let newView = lottieAnimationView == nil
			if newView {
				lottieAnimationView = lottieView
				lottieView.contentMode = .scaleAspectFit
			}
			else {
				lottieView.animation = animation
				lottieView.currentProgress = 0.0
			}
			if lottieView.loopMode != loopMode {
            	lottieView.loopMode = loopMode
			}
			if isAnimating {
                if (progress == nil) {
                    lottieView.play()
                }
			}
			else {
				lottieView.pause()
			}
			if newView {
            	view.addSubview(lottieView)
			}
        }
		else {
			lottieAnimationView?.removeFromSuperview()
			lottieAnimationView = nil
		}
    }

	private func getAnimationName(styleClass: GXStyleClass) -> String? {
		guard GXThemeClassAnimation.typeIdentifier(from: styleClass, resolvingToDefaultWith: nil) == LottieActivityIndicatorViewProvider.lottieTypeIdenfifier else {
			return nil
		}
		let themeClass = styleClass as? GXThemeClass
		let lottieFilePropertyValue: Any?
		if let themeClass = themeClass {
			lottieFilePropertyValue = themeClass.propertyValue(forName: .lottieFile, recursive: false)
		}
		else {
			lottieFilePropertyValue = styleClass.propertyValue(forName: .lottieFile)
		}
		if lottieFilePropertyValue != nil {
			return "\(styleClass.name.lowercased())_animation"
		}
		guard let parentThemeClass = themeClass?.parent else {
			return nil
		}
		return getAnimationName(styleClass: parentThemeClass)
    }
}

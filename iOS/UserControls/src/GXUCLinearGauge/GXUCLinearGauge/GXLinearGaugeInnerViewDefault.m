//
//  GXLinearGaugeInnerViewDefault.m
//  GXUCLinearGauge
//
//  Created by Fabian Inthamoussu on 29/6/18.
//  Copyright © 2018 GeneXus. All rights reserved.
//

#import "GXLinearGaugeInnerViewDefault.h"
#import "GXLinearGaugeGradientView.h"
#import "GXLinearGaugeShapeView.h"

#define kShapeBoundSize 7.0
#define kDefaultRangesViewHeight 3.0

@implementation GXLinearGaugeInnerViewDefault {
	NSMutableArray<GXLinearGaugeGradientView *> *_rangesColorView;
	NSMutableArray<UILabel *> *_rangesTextView;
	UILabel *_valueTextView;
	UILabel *_valueMinTextView;
	UILabel *_valueMaxTextView;
	GXLinearGaugeShapeView *_valueShapeView;
	GXLinearGaugeShapeView *_valueMinShapeView;
	GXLinearGaugeShapeView *_valueMaxShapeView;
	UIFont *_textFont;
	NSTimeInterval _animateNextLayoutWithDuration;
}

- (nonnull UIFont *)textFont {
	return _textFont ? : [self defaultFont];
}

- (void)setTextFont:(nullable UIFont *)textFont {
	if (_textFont != textFont) {
		UIFont *oldTextFont = _textFont;
		_textFont = textFont;
		if (textFont == nil || ![textFont isEqual:oldTextFont]) {
			UIFont *resolvedFont = self.textFont;
			_valueTextView.font = resolvedFont;
			_valueMinTextView.font = resolvedFont;
			_valueMaxTextView.font = resolvedFont;
			for (UILabel *rangeLabel in _rangesTextView) {
				rangeLabel.font = resolvedFont;
			}
			[self setNeedsLayout];
		}
	}
}

- (nonnull UIFont *)defaultFont {
	UIFont *font = [UIFont preferredFontForTextStyle:UIFontTextStyleCaption1];
	UIFontDescriptor *boldFontDesc = [font.fontDescriptor fontDescriptorWithSymbolicTraits:UIFontDescriptorTraitBold];
	if (boldFontDesc != nil) {
		font = [UIFont fontWithDescriptor:boldFontDesc size:0];
	}
	return font;
}

- (instancetype)initWithFrame:(CGRect)frame specification:(GXLinearGaugeSpecification *)specification {
	self = [super initWithFrame:frame specification:specification];
	NSUInteger rangesCount = specification.ranges.count;
	_rangesColorView = [NSMutableArray arrayWithCapacity:rangesCount];
	_rangesTextView = [NSMutableArray arrayWithCapacity:rangesCount];
	return self;
}

- (CGFloat)rangesViewHeight {
	CGFloat height = self.specification.height;
	return height <= 0 ? kDefaultRangesViewHeight : height;
}

- (void)layoutSubviews {
	[super layoutSubviews];
	if (self.firstOnSpecificationChangedPending) {
		return;
	}
	
	GXLinearGaugeSpecification *spec = self.specification;
	double totalLength = spec.totalLength;
	CGSize minTextSize = CGSizeZero;
	CGSize maxTextSize = CGSizeZero;
	if (_valueMinTextView != nil && !_valueMinTextView.hidden) {
		[_valueMinTextView sizeToFit];
		minTextSize = _valueMinTextView.bounds.size;
	}
	if (_valueMaxTextView != nil && !_valueMaxTextView.hidden) {
		[_valueMaxTextView sizeToFit];
		maxTextSize = _valueMaxTextView.bounds.size;
	}
	CGSize boundsSize = self.bounds.size;
	CGFloat paddingLeft = 0;
	CGFloat paddingRight = 0;
	CGFloat rangesViewHeight = [self rangesViewHeight];

	__block CGFloat xPosition = paddingLeft;
	CGFloat yPosition = MAX(0, boundsSize.height * 0.5 - rangesViewHeight);
	CGFloat yRangesViewsBottom = yPosition + rangesViewHeight;
	
	CGFloat(^translateXValueToControlCoordinates)(double);
	if (totalLength == 0) {
		translateXValueToControlCoordinates = ^CGFloat(double realValue) { return 0; };
	}
	else {
		translateXValueToControlCoordinates = ^CGFloat(double realValue) {
			CGFloat uiWidth = MAX(0, boundsSize.width - (paddingLeft + paddingRight));
			return realValue * uiWidth / totalLength;
		};
	}
	
	NSTimeInterval animationDuration = MAX(0, _animateNextLayoutWithDuration);
	_animateNextLayoutWithDuration = 0;
	void(^performAnimationBlock)(void(^ _Nullable)(void), void(^)(void)) = animationDuration <= 0 ? NULL : ^(void(^ _Nullable beforeAnimation)(void), void(^animation)(void)) {
		if (beforeAnimation != NULL) [UIView performWithoutAnimation:beforeAnimation];
		[UIView animateWithDuration:animationDuration delay:0 options:UIViewAnimationOptionBeginFromCurrentState animations:animation completion:NULL];
	};
	NSArray<GXLinearGaugeRangeSpec *> *ranges = spec.ranges;
	__block CGFloat previousInitialMaxX = xPosition;
	[ranges enumerateObjectsUsingBlock:^(GXLinearGaugeRangeSpec * _Nonnull range, NSUInteger idx, BOOL * _Nonnull stop) {
		GXLinearGaugeGradientView *rangeColorView = self->_rangesColorView[idx];
		CGFloat rangeWidth = round(translateXValueToControlCoordinates(range.length));
		CGRect rangeViewFrame = CGRectMake(xPosition, yPosition, rangeWidth, rangesViewHeight);
		rangeViewFrame = GXPixelAlignedFrameWithMainScreenScale(rangeViewFrame);
		if (performAnimationBlock != NULL) {
			CGRect currentFrame = rangeColorView.frame;
			CGRect initialFrame = currentFrame;
			BOOL ceroHeight = CGRectGetHeight(currentFrame) == 0;
			if (ceroHeight) {
				if (idx + 1 == ranges.count) {
					initialFrame = CGRectMake(previousInitialMaxX, rangeViewFrame.origin.y, MAX(0, CGRectGetMaxX(rangeViewFrame) - previousInitialMaxX), CGRectGetHeight(rangeViewFrame));
				}
				else {
					initialFrame = CGRectMake(previousInitialMaxX, rangeViewFrame.origin.y, CGRectGetWidth(currentFrame), CGRectGetHeight(rangeViewFrame));
				}
			}
			previousInitialMaxX = CGRectGetMaxX(initialFrame);
			performAnimationBlock(!ceroHeight ? NULL : ^{
				rangeColorView.frame = initialFrame;
			}, ^{
				rangeColorView.frame = rangeViewFrame;
			});
		}
		else {
			rangeColorView.frame = rangeViewFrame;
		}
		
		UILabel *rangeTextView = self->_rangesTextView[idx];
		rangeViewFrame = CGRectMake(xPosition, yRangesViewsBottom, MAX(0, rangeWidth), MIN(boundsSize.height, rangeTextView.font.lineHeight));
		rangeViewFrame = GXPixelAlignedFrameWithMainScreenScale(rangeViewFrame);
		if (performAnimationBlock != NULL) {
			BOOL fromZero = CGRectGetHeight(rangeTextView.bounds) == 0;
			CGPoint newCenter = CGPointMake(CGRectGetMidX(rangeViewFrame), CGRectGetMidY(rangeViewFrame));
			CGRect newBounds = rangeViewFrame;
			newBounds.origin = CGPointZero;
			performAnimationBlock(^{
				if (fromZero) {
					rangeTextView.center = newCenter;
				}
				else {
					rangeTextView.bounds = newBounds;
				}
			}, ^{
				if (!fromZero) {
					rangeTextView.center = newCenter;
				}
				else {
					rangeTextView.bounds = newBounds;
				}
			});
		}
		else {
			rangeTextView.frame = rangeViewFrame;
		}
		xPosition = xPosition + rangeWidth;
	}];
	
	if ((_valueShapeView != nil && !_valueShapeView.hidden) || (_valueTextView != nil && !_valueTextView.hidden)) {
		CGFloat currentValueXCoord;
		CGFloat minMaxDiff = spec.maxValue - spec.minValue;
		if (minMaxDiff == 0 || totalLength == 0) {
			currentValueXCoord = MAX(0, boundsSize.width - (paddingLeft + paddingRight)) * 0.5;
		}
		else {
			currentValueXCoord = paddingLeft + translateXValueToControlCoordinates((spec.currentValue - spec.minValue) * totalLength / minMaxDiff);
		}
		currentValueXCoord = currentValueXCoord - kShapeBoundSize;      // Take in account the XDisplacement
		if (_valueShapeView != nil && !_valueShapeView.hidden) {
			CGRect valueShapeFrame = CGRectMake(currentValueXCoord, yRangesViewsBottom, kShapeBoundSize * 2, kShapeBoundSize);
			_valueShapeView.frame = GXPixelAlignedFrameWithMainScreenScale(valueShapeFrame);
		}
		if (_valueTextView != nil && !_valueTextView.hidden) {
			[_valueTextView sizeToFit];
			CGSize textSize = _valueTextView.bounds.size;
			CGRect valueShapeFrame = CGRectMake(currentValueXCoord, yRangesViewsBottom + kShapeBoundSize, textSize.width, textSize.height);
			_valueTextView.frame = GXPixelAlignedFrameWithMainScreenScale(valueShapeFrame);
		}
	}
	
	if ((_valueMinTextView != nil && !_valueMinTextView.hidden) ||
		(_valueMinShapeView != nil && !_valueMinShapeView.hidden) ||
		(_valueMaxTextView != nil && !_valueMaxTextView.hidden) ||
		(_valueMaxShapeView != nil && !_valueMaxShapeView.hidden)) {
		
		if (_valueMinTextView != nil && !_valueMinTextView.hidden) {
			CGRect valueShapeFrame = CGRectMake(paddingLeft, yRangesViewsBottom + kShapeBoundSize, minTextSize.width, minTextSize.height);
			_valueMinTextView.frame = GXPixelAlignedFrameWithMainScreenScale(valueShapeFrame);
		}
		if (_valueMinShapeView != nil && !_valueMinShapeView.hidden) {
			CGRect valueShapeFrame = CGRectMake(paddingLeft, yRangesViewsBottom, kShapeBoundSize, kShapeBoundSize);
			_valueMinShapeView.frame = GXPixelAlignedFrameWithMainScreenScale(valueShapeFrame);
		}
		if (_valueMaxTextView != nil && !_valueMaxTextView.hidden) {
			CGRect valueShapeFrame = CGRectMake(MAX(0, boundsSize.width - (maxTextSize.width + paddingRight)), yRangesViewsBottom + kShapeBoundSize, maxTextSize.width, maxTextSize.height);
			_valueMaxTextView.frame = GXPixelAlignedFrameWithMainScreenScale(valueShapeFrame);
		}
		if (_valueMaxShapeView != nil && !_valueMaxShapeView.hidden) {
			CGRect valueShapeFrame = CGRectMake(MAX(0, boundsSize.width - (paddingRight + kShapeBoundSize)), yRangesViewsBottom, kShapeBoundSize, kShapeBoundSize);
			_valueMaxShapeView.frame = GXPixelAlignedFrameWithMainScreenScale(valueShapeFrame);
		}
	}
}

- (void)onSpecificationChanged:(nullable GXLinearGaugeSpecification *)oldSpecification {
	[super onSpecificationChanged:oldSpecification];
	[self updateSubviewsForDefaultTypeSpecification];
}

#pragma mark - Private Helpers

- (void)updateSubviewsForDefaultTypeSpecification {
	GXLinearGaugeSpecification *spec = self.specification;
	NSAssert(!spec.isEmpty && ![spec.type isEqualToString:@"Circular"], @"Invalid Gauge Specification Type");
	NSUInteger rangesCount = spec.ranges.count;
	NSAssert(_rangesTextView.count == _rangesColorView.count, @"Invalid State");
	if (_rangesColorView.count > rangesCount) {
		NSRange rangeToRemove = NSMakeRange(rangesCount, _rangesColorView.count - rangesCount);
		NSIndexSet *indexesToRemote = [NSIndexSet indexSetWithIndexesInRange:rangeToRemove];
		for (NSMutableArray<UIView *> *rangesViews in @[_rangesColorView, _rangesTextView]) {
			[rangesViews enumerateObjectsAtIndexes:indexesToRemote options:0 usingBlock:^(UIView * _Nonnull rangeView, NSUInteger idx, BOOL * _Nonnull stop) {
				[rangeView removeFromSuperview];
			}];
			[rangesViews removeObjectsInRange:rangeToRemove];
		}
	}
	
	if (rangesCount > 0) {
		[spec.ranges enumerateObjectsUsingBlock:^(GXLinearGaugeRangeSpec * _Nonnull range, NSUInteger idx, BOOL * _Nonnull stop) {
			GXLinearGaugeGradientView* rangeView;
			UILabel *rangeLabel;
			if (self->_rangesColorView.count > idx)
			{
				rangeView = self->_rangesColorView[idx];
				rangeLabel = self->_rangesTextView[idx];
			}
			else {
				rangeView = [self newRangeView];
				rangeLabel = [self newRangeLabel];
				[self addSubview:rangeView];
				[self addSubview:rangeLabel];
				[self->_rangesColorView addObject:rangeView];
				[self->_rangesTextView addObject:rangeLabel];
			}
			rangeView.backgroundColor = range.color;
			rangeLabel.text = range.name;
			rangeLabel.textColor = range.color;
		}];
	}
	
	BOOL showMinMax = spec.showMinMax;
	if (showMinMax) {
		UIColor *minColor = spec.colorForMinRange;
		UIColor *maxColor = spec.colorForMaxRange;
		if (_valueMinTextView == nil) {
			_valueMinTextView = [self newValueTextView];
			[self addSubview:_valueMinTextView];
		}
		_valueMinTextView.text = [NSString stringWithFormat:@"%.1f", spec.minValue];
		_valueMinTextView.textColor = minColor;
		
		if (_valueMaxTextView == nil) {
			_valueMaxTextView = [self newValueTextView];
			[self addSubview:_valueMaxTextView];
		}
		_valueMaxTextView.text = [NSString stringWithFormat:@"%.1f", spec.maxValue];
		_valueMaxTextView.textColor = maxColor;
		
		// Min Value Line
		if (_valueMinShapeView == nil) {
			_valueMinShapeView = [self newValueShapeViewWithType:GXLinearGaugeShapeTypeMin];
			[self addSubview:_valueMinShapeView];
		}
		_valueMinShapeView.shapeColor = minColor;
		
		// Max Value Line
		if (_valueMaxShapeView == nil) {
			_valueMaxShapeView = [self newValueShapeViewWithType:GXLinearGaugeShapeTypeMax];
			[self addSubview:_valueMaxShapeView];
		}
		_valueMaxShapeView.shapeColor = maxColor;
	}
	_valueMinTextView.hidden = !showMinMax || spec.minValue == spec.currentValue;
	_valueMaxTextView.hidden = !showMinMax || spec.maxValue == spec.currentValue;
	_valueMinShapeView.hidden = !showMinMax || spec.minValue == spec.currentValue;
	_valueMaxShapeView.hidden = !showMinMax || spec.maxValue == spec.currentValue;
	
	BOOL showValidValue = [spec isValid] && [spec showValue];
	if (showValidValue) {
		if (_valueShapeView == nil) {
			_valueShapeView = [self newValueShapeViewWithType:GXLinearGaugeShapeTypeValue];
			_valueShapeView.shapeColor = UIColor.blackColor;
			[self addSubview:_valueShapeView];
		}
		if (_valueTextView == nil) {
			_valueTextView = [self newValueTextView];
			_valueTextView.textColor = UIColor.blackColor;
			[self addSubview:_valueTextView];
		}
		_valueTextView.text = [NSString stringWithFormat:@"%.1f", spec.currentValue];
	}
	_valueShapeView.hidden = !showValidValue;
	_valueTextView.hidden = !showValidValue;
	_animateNextLayoutWithDuration = self.animateTransitionDutation;
	[self setNeedsLayout];
}

- (nonnull UILabel *)newValueTextView {
	UILabel *valueTextView = [UILabel new];
	valueTextView.autoresizingMask = UIViewAutoresizingNone;
	valueTextView.font = self.textFont;
	return valueTextView;
}

- (nonnull UILabel *)newRangeLabel {
	UILabel *rangeLabel = [UILabel new];
	rangeLabel.autoresizingMask = UIViewAutoresizingNone;
	rangeLabel.textAlignment = NSTextAlignmentCenter;
	rangeLabel.backgroundColor = UIColor.clearColor;
	rangeLabel.adjustsFontSizeToFitWidth = YES;
	rangeLabel.font = self.textFont;
	return rangeLabel;
}

- (nonnull GXLinearGaugeGradientView *)newRangeView {
	GXLinearGaugeGradientView *rangeView = [GXLinearGaugeGradientView new];
	rangeView.autoresizingMask = UIViewAutoresizingNone;
	return rangeView;
}

- (nonnull GXLinearGaugeShapeView *)newValueShapeViewWithType:(GXLinearGaugeShapeType)type {
	GXLinearGaugeShapeView *valueShapeView = [GXLinearGaugeShapeView new];
	valueShapeView.shapeType = type;
	valueShapeView.autoresizingMask = UIViewAutoresizingNone;
	return valueShapeView;
}

@end

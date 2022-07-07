//
//  GXLinearGaugeInnerViewCircular.m
//  GXLinearGaugeInnerViewCircular
//
//  Created by gmilano.
//

#import "GXLinearGaugeInnerViewCircular.h"

#define kInitialDegree -90

@implementation GXLinearGaugeInnerViewCircular
{
	NSArray<NSNumber *> *_transitioningToValues;
	NSArray<NSNumber *> *_transitioningFromValues;
	NSMutableArray<NSNumber *> *_rangesValues;
	
	UILabel *_outerTextAuxLabel;
	UIColor *_outerTextColor;
	UIFont *_outerTextFont;
	
	UIImage *_outerImage;
	CGSize _outerImageSize;
	
	UILabel *_innerTextAuxLabel;
	UIColor *_innerTextColor;
	UIFont *_innerTextFont;
	
	UIImage *_innerImage;
	CGSize _innerImageSize;
	
	BOOL _animationIsBusy;
	__weak CADisplayLink *_currentAnimationLink;
	NSTimeInterval _currentAnimationDuration;
	CFTimeInterval _currentAnimationStartTime;
}

- (nonnull UIColor *)innerTextColor {
	return _innerTextColor ? : [self defaultTextColor];
}

- (void)setInnerTextColor:(nullable UIColor *)innerTextColor {
	if (_innerTextColor != innerTextColor) {
		UIColor *oldTextColor = _innerTextColor;
		_innerTextColor = innerTextColor;
		if (oldTextColor == nil || ![_innerTextColor isEqual:oldTextColor]) {
			[self setNeedsDisplay];
		}
	}
}

- (nonnull UIFont *)innerTextFont {
	return _innerTextFont ? : [self defaultFont];
}

- (void)setInnerTextFont:(nullable UIFont *)innerTextFont {
	if (_innerTextFont != innerTextFont) {
		UIFont *oldTextFont = _innerTextFont;
		_innerTextFont = innerTextFont;
		if (oldTextFont == nil || ![_innerTextFont isEqual:oldTextFont]) {
			[self setNeedsDisplay];
		}
	}
}

- (nonnull UIColor *)outerTextColor {
	return _outerTextColor ? : [self defaultTextColor];
}

- (void)setOuterTextColor:(nullable UIColor *)outerTextColor {
	if (_outerTextColor != outerTextColor) {
		UIColor *oldTextColor = _outerTextColor;
		_outerTextColor = outerTextColor;
		if (oldTextColor == nil || ![_outerTextColor isEqual:oldTextColor]) {
			[self setNeedsDisplay];
		}
	}
}

- (nonnull UIFont *)outerTextFont {
	return _outerTextFont ? : [self defaultFont];
}

- (void)setOuterTextFont:(nullable UIFont *)outerTextFont {
	if (_outerTextFont != outerTextFont) {
		UIFont *oldTextFont = _outerTextFont;
		_outerTextFont = outerTextFont;
		if (oldTextFont == nil || ![_outerTextFont isEqual:oldTextFont]) {
			[self setNeedsDisplay];
		}
	}
}

- (CGFloat)thickness {
	CGFloat thickness = self.specification.thickness;
	if (thickness <= 0) {
		thickness = 9;
	}
	return thickness;
}

#pragma mark - Init Methods

- (instancetype)initWithFrame:(CGRect)frame specification:(nonnull GXLinearGaugeSpecification *)specification {
	self = [super initWithFrame:frame specification:specification];
	_rangesValues = [NSMutableArray arrayWithCapacity:specification.ranges.count];
	return self;
}

- (nonnull NSArray<NSNumber *> *)valuesFromSpecification:(nonnull GXLinearGaugeSpecification *)spec {
	NSArray<GXLinearGaugeRangeSpec *> *ranges = spec.ranges;
	if (ranges.count == 0)
		return @[];
	
	CGFloat maxValue = spec.maxValue;
	if (maxValue == 0) {
		maxValue = 100; // consider 100% is the maximum when no specified its value
	}
	CGFloat minValue = spec.minValue;
	CGFloat rangeAll = maxValue - minValue;
	
	if (rangeAll == 0)
		return @[];
	
	NSMutableArray<NSNumber *> *values = [NSMutableArray arrayWithCapacity:ranges.count];
	for (GXLinearGaugeRangeSpec *range in ranges) {
		[values addObject:@(range.length / rangeAll)];
	}
	return values;
}

#pragma mark - Public methods

- (void)setFrame:(CGRect)frame {
	if (!CGSizeEqualToSize(self.frame.size, frame.size)) {
		[self setNeedsDisplay];
	}
	super.frame = frame;
}

- (void)setBounds:(CGRect)bounds {
	if (!CGSizeEqualToSize(self.bounds.size, bounds.size)) {
		[self setNeedsDisplay];
	}
	super.bounds = bounds;
}

- (void)onSpecificationChanged:(nullable GXLinearGaugeSpecification *)oldSpecification {
	[super onSpecificationChanged:oldSpecification];
	NSAssert([self.specification.type isEqualToString:@"Circular"], @"Invalid Gauge Specification Type");
	[self refreshGraph:self.animateTransitions];
}

- (void)refreshGraph:(BOOL)animated {
	NSString *innerText = [GXUtilities nonEmptyStringFromObject:self.specification.title];
	if (_innerTextAuxLabel == nil && innerText != nil) {
		_innerTextAuxLabel = [UILabel new];
	}
	_innerTextAuxLabel.text = innerText;
	_transitioningToValues = [self valuesFromSpecification:self.specification];
	if (animated) {
		_transitioningFromValues = _rangesValues.copy;
		[self animateGauge];
	} else {
		[_rangesValues setArray:_transitioningToValues];
		_transitioningFromValues = _transitioningToValues = nil;
		[self setNeedsDisplay];
	}
}

#pragma mark - Private methods

- (nonnull UIFont *)defaultFont {
	UIFont *font = [UIFont preferredFontForTextStyle:UIFontTextStyleCaption1];
	UIFontDescriptor *boldFontDesc = [font.fontDescriptor fontDescriptorWithSymbolicTraits:UIFontDescriptorTraitBold];
	if (boldFontDesc != nil) {
		font = [UIFont fontWithDescriptor:boldFontDesc size:0];
	}
	return font;
}

- (nonnull UIColor *)defaultTextColor {
	return UIColor.darkGrayColor;
}

#pragma mark - Animating methods

- (void)animateGauge {
	if (_animationIsBusy) {
		[self performSelector:@selector(makeAnimateGauge) withObject:nil afterDelay:0.4];
	} else {
		_animationIsBusy = YES;
		[self makeAnimateGauge];
	}
}

- (void)makeAnimateGauge {
	CADisplayLink *link = [CADisplayLink displayLinkWithTarget:self selector:@selector(animateNumber:)];
	_currentAnimationLink = link;
	_currentAnimationStartTime = CACurrentMediaTime();
	_currentAnimationDuration = self.animateTransitionDutation;
	if (_currentAnimationDuration <= 0)
		_currentAnimationDuration = self.class.defaultAnimationDuration;
	[link addToRunLoop:[NSRunLoop currentRunLoop] forMode:NSRunLoopCommonModes];
	_animationIsBusy = NO;
}

- (void)animateNumber:(CADisplayLink *)link {
	if (_transitioningToValues == nil || _transitioningFromValues == nil || _currentAnimationLink != link) {
		if (_currentAnimationLink == link) {
			_currentAnimationLink = nil;
		}
		[link removeFromRunLoop:[NSRunLoop currentRunLoop] forMode:NSRunLoopCommonModes];
		return;
	}

	[self setNeedsDisplay];
	
	CFTimeInterval dt = (link.timestamp - _currentAnimationStartTime) / _currentAnimationDuration;
	if (dt >= 1 || [_rangesValues isEqualToArray:_transitioningToValues]) {
		[_rangesValues setArray:_transitioningToValues];
		[link removeFromRunLoop:[NSRunLoop currentRunLoop] forMode:NSRunLoopCommonModes];
		_transitioningToValues = nil;
		_animationIsBusy = NO;
		return;
	}
	
	__block CGFloat currentValue = 0;
	__block CGFloat rangeValuesSum = 0;
	NSUInteger newValuesCount = _transitioningToValues.count;
	[_transitioningToValues enumerateObjectsUsingBlock:^(NSNumber *rangeValueNum, NSUInteger idx, BOOL * _Nonnull stop) {
		CGFloat rangeValue = rangeValueNum.doubleValue;
		rangeValuesSum += rangeValue;
		if (self->_transitioningFromValues.count > idx) {
			CGFloat fromValue = self->_transitioningFromValues[idx].doubleValue;
			currentValue = (rangeValue - fromValue) * dt + fromValue;
		}
		else if (idx + 1 == newValuesCount && rangeValuesSum == 1) {
			currentValue = rangeValuesSum;
		}
		else {
			currentValue = (rangeValue - currentValue) * dt + currentValue;
		}
		if (self->_rangesValues.count > idx) {
			self->_rangesValues[idx] = @(currentValue);
		}
		else {
			[self->_rangesValues addObject:@(currentValue)];
		}
	}];
	if (_transitioningFromValues.count < newValuesCount) {
		NSRange rangeToAdd = NSMakeRange(_transitioningFromValues.count, newValuesCount - _transitioningFromValues.count);
		_transitioningFromValues = [_transitioningFromValues arrayByAddingObjectsFromArray:[_rangesValues subarrayWithRange:rangeToAdd]];
	}
	if (_rangesValues.count > newValuesCount) {
		[_rangesValues removeObjectsInRange:NSMakeRange(newValuesCount, _rangesValues.count - newValuesCount)];
	}
}

#pragma mark - Drawing methods

- (void)drawRect:(CGRect)rect {
	//1. Draw inner and outer circles
	[self drawCircles:rect];
	//2. Outer assets
	[self drawOuterAssets:rect];
	//3. Inner assets
	[self drawInnerAssets:rect];
}

- (void)drawCircles:(CGRect)rect {
	if (_rangesValues.count == 0)
		return;
	
	NSArray<GXLinearGaugeRangeSpec *> *ranges = self.specification.ranges;
	CGFloat thickness = [self thickness];
	CGPoint center = CGPointMake(CGRectGetWidth(self.bounds) * 0.5,  CGRectGetHeight(self.bounds) * 0.5);
	CGFloat radius = MIN(MAX(0, center.x - thickness), MAX(0, center.y - thickness));
	__block UIColor *color = nil;
	__block CGFloat startDegree = kInitialDegree;
	[_rangesValues enumerateObjectsUsingBlock:^(NSNumber *rangeValueNum, NSUInteger idx, BOOL * _Nonnull stop) {
		if (ranges.count > idx)
			color = ranges[idx].color;
		CGFloat endDegree = startDegree + (((CGFloat)(rangeValueNum.doubleValue)) * 360);
		[self drawCircleWithCenter:center
							 color:color
							radius:radius
						 endDegree:endDegree
					   startDegree:startDegree
						 lineWidth:thickness];
		startDegree = endDegree;
	}];
}

- (void)drawOuterAssets:(CGRect)rect {
	CGFloat thickness = [self thickness];
	//outer value text
	if(_outerTextAuxLabel.text.length > 0) {
		_outerTextAuxLabel.font = _outerTextFont;
		CGSize labelSize = _outerTextAuxLabel.attributedText.size;
		CGPoint textPoint = CGPointMake((rect.size.width/2)-(_outerImageSize.width / 2) - labelSize.width - 5, thickness - (labelSize.height / 2));
		[self drawText:rect
				  text:_outerTextAuxLabel.text
			 textColor:self.outerTextColor
			 textPoint:textPoint
				  font:self.outerTextFont
				  size:labelSize
		 textAlignment:NSTextAlignmentRight];
	}
	
	//outer image
	if(_outerImage != nil) {
		CGPoint imagePoint = CGPointMake((rect.size.width/2)-(_outerImageSize.width / 2), thickness - (_outerImageSize.height / 2));
		
		[self drawImage:imagePoint
				  image:_outerImage
			  imageSize:_outerImageSize];
	}
}

- (void)drawInnerAssets:(CGRect)rect {
	//middle point
	CGPoint imagePoint = CGPointMake((rect.size.width/2)-(_innerImageSize.width / 2), ((rect.size.height/2) - (_innerImageSize.height / 2)));
	
	//inner text
	if(_innerTextAuxLabel.text.length > 0) {
		imagePoint = CGPointMake(((rect.size.width/2)-(_innerImageSize.width / 2)), ((rect.size.height/2) - (_innerImageSize.height)));
		_innerTextAuxLabel.font = self.innerTextFont;
		CGSize labelSize = _innerTextAuxLabel.attributedText.size;
		CGPoint textPoint = CGPointMake((rect.size.width / 2) - (labelSize.width / 2), (rect.size.height / 2) - (labelSize.height / 2));
		if(_innerImage != nil){
			textPoint = CGPointMake((rect.size.width / 2) - (labelSize.width / 2), rect.size.height / 2);
		}
		
		[self drawText:rect
				  text:_innerTextAuxLabel.text
			 textColor:self.innerTextColor
			 textPoint:textPoint
				  font:self.innerTextFont
				  size:labelSize
		 textAlignment:NSTextAlignmentCenter];
	}
	
	//inner image
	if(_innerImage != nil) {
		[self drawImage:imagePoint
				  image:_innerImage
			  imageSize:_innerImageSize];
	}
}

- (void)drawCircleWithCenter:(CGPoint)center color:(UIColor*)color radius:(CGFloat)radius endDegree:(CGFloat)endDegree startDegree:(CGFloat)startDegree lineWidth:(CGFloat)lineWidth {
	UIBezierPath *bezierPath = [UIBezierPath bezierPath];
	bezierPath.lineWidth = lineWidth;
	[bezierPath addArcWithCenter:center radius:radius startAngle:[self radiansFromDegrees:startDegree] endAngle:[self radiansFromDegrees:endDegree] clockwise:YES];
	[color setStroke];
	[bezierPath stroke];
}

- (CGFloat)radiansFromDegrees:(CGFloat)degrees {
	 return degrees * (M_PI / 180.0f);
}

#pragma mark - Drawing Gauge Accessories

- (void)drawText:(CGRect)rect text:(NSString*)text textColor:(UIColor*)textColor textPoint:(CGPoint)textPoint font:(UIFont*)font size:(CGSize)size textAlignment:(NSTextAlignment)textAlignment {
	
	NSMutableParagraphStyle *style = [[NSMutableParagraphStyle alloc] init];
	[style setAlignment:textAlignment];
	NSDictionary *stringAttrs = @{ NSFontAttributeName:font, NSForegroundColorAttributeName:textColor , NSParagraphStyleAttributeName:style};
	
	NSAttributedString *attrStr = [[NSAttributedString alloc] initWithString:text attributes:stringAttrs];
	
	CGRect frame = CGRectMake(textPoint.x, textPoint.y, size.width, size.height);
	
	[attrStr drawInRect:frame];
}

//if width is bigger than height then it only uses pointWidth or vice versa
- (void)drawImage:(CGPoint)point image:(UIImage *)image imageSize:(CGSize)imageSize {
	[image drawInRect:CGRectMake(point.x, point.y, imageSize.width, imageSize.height)];
}

@end

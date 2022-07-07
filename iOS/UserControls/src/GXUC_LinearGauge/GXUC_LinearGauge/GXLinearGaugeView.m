//
//  GXLinearGaugeView.m
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/04/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXLinearGaugeView.h"
#import "GXLinearGaugeRangeSpec.h"
#import "GXLinearGaugeInnerViewCircular.h"
#import "GXLinearGaugeInnerViewDefault.h"

@import GXFoundation;
@import GXObjectsModel;

@implementation GXLinearGaugeView {
	GXLinearGaugeInnerViewBase *_gaugeInnerView;
	UILabel *_messageLabel;
}

- (instancetype)initWithFrame:(CGRect)frame {
	return [self initWithFrame:frame specification:[GXLinearGaugeSpecification new]];
}

- (instancetype)initWithFrame:(CGRect)frame specification:(GXLinearGaugeSpecification *)specification {
	NSParameterAssert(specification);
	self = [super initWithFrame:frame];
	_specification = specification;
	self.backgroundColor = UIColor.clearColor;
    return self;
}

@synthesize specification = _specification;

- (void)setSpecification:(nonnull GXLinearGaugeSpecification *)spec {
	NSParameterAssert(spec);
	if (_specification != spec) {
		GXLinearGaugeSpecification *oldSpecification = _specification;
		_specification = spec;
		[self updateSubviewsForCurrentSpecification:oldSpecification];
	}
}

@synthesize themeClass = _themeClass;

- (void)setThemeClass:(nullable GXThemeClass *)themeClass {
	if (_themeClass != themeClass) {
		_themeClass = themeClass;
		 [self applyThemeClass];
	}
}

- (void)applyThemeClass {
	[self applyThemeClassToGaugeInnerView];
	[self applyThemeClassToMessageLabel];
}

#pragma mark - Private Methods

- (void)updateSubviewsForCurrentSpecification:(nullable GXLinearGaugeSpecification *)oldSpecification  {
	GXLinearGaugeSpecification *spec = self.specification;
    if (spec.isEmpty) {
		if (oldSpecification == nil || ![oldSpecification isEqual:spec]) {
        	[self updateSubviewsForEmptySpecification];
		}
    }
	else {
		[self updateSubviewsForNonEmptySpecification];
    }
}

- (void)updateSubviewsForEmptySpecification {
	NSParameterAssert(self.specification.isEmpty);
	if (_gaugeInnerView != nil) {
		[_gaugeInnerView removeFromSuperview];
		_gaugeInnerView = nil;
	}
	if (!_messageLabel) {
		_messageLabel = [[UILabel alloc] initWithFrame:self.bounds];
		_messageLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
		_messageLabel.textAlignment = NSTextAlignmentCenter;
		_messageLabel.backgroundColor = UIColor.clearColor;
		_messageLabel.adjustsFontSizeToFitWidth = YES;
		[self applyThemeClassToMessageLabel];
	}
	_messageLabel.text = @"There is no data";
    [self addSubview:_messageLabel];
}

- (void)updateSubviewsForNonEmptySpecification {
	GXLinearGaugeSpecification *spec = self.specification;
	NSParameterAssert(!spec.isEmpty);
	if (_messageLabel != nil) {
		[_messageLabel removeFromSuperview];
		_messageLabel = nil;
	}
	BOOL circular = [spec.type isEqualToString:@"Circular"];
	Class requiredInnerViewClass = circular ? [GXLinearGaugeInnerViewCircular class] : [GXLinearGaugeInnerViewDefault class];
	if (_gaugeInnerView == nil || ![_gaugeInnerView isKindOfClass:requiredInnerViewClass]) {
		[_gaugeInnerView removeFromSuperview];
		_gaugeInnerView = [(GXLinearGaugeInnerViewBase *)[requiredInnerViewClass alloc] initWithFrame:self.bounds specification:spec];
		_gaugeInnerView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
		[self applyThemeClassToGaugeInnerView];
		[self addSubview:_gaugeInnerView];
	}
	else {
		_gaugeInnerView.specification = spec;
	}
}

- (void)applyThemeClassToGaugeInnerView {
	if (_gaugeInnerView == nil)
		return;
	
	GXThemeClass *themeClass = self.themeClass;
	BOOL animateTransitions = NO;
	NSNumber *animationDuration = nil;
	if ([themeClass conformsToProtocol:@protocol(GXThemeClassWithTransformation)]) {
		animateTransitions = ((id<GXThemeClassWithTransformation>)themeClass).animated;
		if (animateTransitions) {
			animationDuration = ((id<GXThemeClassWithTransformation>)themeClass).animationDuration;
		}
	}
	_gaugeInnerView.animateTransitions = animateTransitions;
	if (_gaugeInnerView.animateTransitions) {
		_gaugeInnerView.animateTransitionDutation = animationDuration != nil ? animationDuration.doubleValue : [_gaugeInnerView.class defaultAnimationDuration];
	}
	UIFont *themeClassFont = nil;
	UIColor *themeClassForeColor = nil;
	if ([themeClass conformsToProtocol:@protocol(GXThemeClassWithFont)]) {
		themeClassFont = ((id<GXThemeClassWithFont>)themeClass).font;
		themeClassForeColor = ((id<GXThemeClassWithFont>)themeClass).foreColor;
	}
	if ([_gaugeInnerView isKindOfClass:[GXLinearGaugeInnerViewCircular class]]) {
		((GXLinearGaugeInnerViewCircular *)_gaugeInnerView).innerTextFont = themeClassFont;
		((GXLinearGaugeInnerViewCircular *)_gaugeInnerView).innerTextColor = themeClassForeColor;
	}
	else if ([_gaugeInnerView isKindOfClass:[GXLinearGaugeInnerViewDefault class]]) {
		((GXLinearGaugeInnerViewDefault *)_gaugeInnerView).textFont = themeClassFont;
	}
}

- (void)applyThemeClassToMessageLabel {
	if (_messageLabel == nil)
		return;
	
	UIFont *font = nil;
	UIColor *foreColor = nil;
	GXThemeClass *themeClass = self.themeClass;
	if ([themeClass conformsToProtocol:@protocol(GXThemeClassWithFont)]) {
		font = ((id<GXThemeClassWithFont>)themeClass).font;
		foreColor = ((id<GXThemeClassWithFont>)themeClass).foreColor;
	}
	_messageLabel.font = font ? : [UIFont preferredFontForTextStyle:UIFontTextStyleCaption1];
	_messageLabel.textColor = foreColor ? : UIColor.blackColor;
}

@end

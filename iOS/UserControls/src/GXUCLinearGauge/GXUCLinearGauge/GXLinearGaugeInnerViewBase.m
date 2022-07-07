//
//  GXLinearGaugeInnerViewBase.m
//  GXUCLinearGauge
//
//  Created by Fabian Inthamoussu on 29/6/18.
//  Copyright © 2018 GeneXus. All rights reserved.
//

#import "GXLinearGaugeInnerViewBase.h"
@import GXObjectsModel;

@implementation GXLinearGaugeInnerViewBase

- (instancetype)initWithFrame:(CGRect)frame specification:(nonnull GXLinearGaugeSpecification *)specification {
	self = [super initWithFrame:frame];
	_animateTransitionDutation = self.class.defaultAnimationDuration;
	_specification = specification;
	_firstOnSpecificationChangedPending = YES;
	self.backgroundColor = UIColor.clearColor;
	return self;
}

@synthesize specification = _specification;

- (void)setSpecification:(nonnull GXLinearGaugeSpecification *)spec {
	NSParameterAssert(spec);
	if (_specification != spec) {
		GXLinearGaugeSpecification *oldSpecification = _specification;
		_specification = spec;
		if (oldSpecification == nil || ![oldSpecification isEqual:spec]) {
			[self onSpecificationChanged:oldSpecification];
		}
	}
}

- (void)didMoveToWindow {
	if (self.firstOnSpecificationChangedPending && self.window != nil) {
		[self onSpecificationChanged:nil];
	}
}

- (BOOL)animateTransitions {
	return self.animateTransitionDutation != 0;
}

- (void)setAnimateTransitions:(BOOL)animateTransitions {
	if (animateTransitions != self.animateTransitions) {
		self.animateTransitionDutation = animateTransitions ? self.class.defaultAnimationDuration : 0;
	}
}

@synthesize animateTransitionDutation = _animateTransitionDutation;

- (void)setAnimateTransitionDutation:(NSTimeInterval)animateTransitionDutation {
	_animateTransitionDutation = MAX(0, animateTransitionDutation);
}

#pragma mark - Subclasses override points

+ (NSTimeInterval)defaultAnimationDuration {
	return kUIViewAnimationDuration * 5;
}

- (void)onSpecificationChanged:(nullable GXLinearGaugeSpecification *)oldSpecification {
	_firstOnSpecificationChangedPending = NO;
}

@synthesize firstOnSpecificationChangedPending = _firstOnSpecificationChangedPending;

@end

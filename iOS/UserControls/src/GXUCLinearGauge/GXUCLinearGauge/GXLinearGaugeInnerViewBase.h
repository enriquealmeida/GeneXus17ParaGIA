//
//  GXLinearGaugeInnerViewBase.h
//  GXUCLinearGauge
//
//  Created by Fabian Inthamoussu on 29/6/18.
//  Copyright © 2018 GeneXus. All rights reserved.
//

@import UIKit;
#import <GXUCLinearGauge/GXLinearGaugeSpecification.h>

NS_ASSUME_NONNULL_BEGIN

@interface GXLinearGaugeInnerViewBase : UIView

- (instancetype)init NS_UNAVAILABLE;
- (instancetype)initWithFrame:(CGRect)frame NS_UNAVAILABLE;
- (nullable instancetype)initWithCoder:(NSCoder *)aDecoder NS_UNAVAILABLE;
- (instancetype)initWithFrame:(CGRect)frame specification:(GXLinearGaugeSpecification *)specification NS_DESIGNATED_INITIALIZER;

@property(nonatomic, strong) GXLinearGaugeSpecification *specification;

@property(nonatomic, assign) BOOL animateTransitions;
@property(nonatomic, assign) NSTimeInterval animateTransitionDutation;

#pragma mark - Subclasses override points

@property(class, nonatomic, assign, readonly) NSTimeInterval defaultAnimationDuration;

- (void)onSpecificationChanged:(nullable GXLinearGaugeSpecification *)oldSpecification;
@property(nonatomic, assign, readonly) BOOL firstOnSpecificationChangedPending;

@end

NS_ASSUME_NONNULL_END

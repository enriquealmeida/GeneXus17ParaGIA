//
//  GXLinearGaugeView.h
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/04/11.
//  Copyright 2011 Artech. All rights reserved.
//

@import UIKit;
@import GXObjectsModel;
#import <GXUCLinearGauge/GXLinearGaugeSpecification.h>

NS_ASSUME_NONNULL_BEGIN

@interface GXLinearGaugeView : UIView

- (instancetype)initWithFrame:(CGRect)frame specification:(GXLinearGaugeSpecification *)gaugeSpecification NS_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithCoder:(NSCoder *)aDecoder NS_UNAVAILABLE;

@property(nonatomic, strong) GXLinearGaugeSpecification *specification;
@property(nullable, nonatomic, strong) GXThemeClass *themeClass;

- (void)applyThemeClass;

@end

NS_ASSUME_NONNULL_END

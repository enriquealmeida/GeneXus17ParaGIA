//
//  GXLinearGaugeShapeView.h
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/04/11.
//  Copyright 2011 Artech. All rights reserved.
//

@import UIKit;

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(uint_least8_t, GXLinearGaugeShapeType) {
    GXLinearGaugeShapeTypeMin,
    GXLinearGaugeShapeTypeMax,
    GXLinearGaugeShapeTypeValue
};

@interface GXLinearGaugeShapeView : UIView

@property(nullable, nonatomic, strong) UIColor *shapeColor;
@property(nonatomic, assign) GXLinearGaugeShapeType shapeType;

- (instancetype)initWithFrame:(CGRect)frame shapeType:(GXLinearGaugeShapeType)shapeType NS_DESIGNATED_INITIALIZER;
- (nullable instancetype)initWithCoder:(NSCoder *)aDecoder NS_DESIGNATED_INITIALIZER;

@end

NS_ASSUME_NONNULL_END

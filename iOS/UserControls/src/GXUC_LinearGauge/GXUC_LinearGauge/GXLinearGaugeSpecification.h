//
//  GXLinearGaugeSpecification.h
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/04/11.
//  Copyright 2011 Artech. All rights reserved.
//

@import Foundation;
@import GXFoundation;
#import <GXUCLinearGauge/GXLinearGaugeRangeSpec.h>

NS_ASSUME_NONNULL_BEGIN

@interface GXLinearGaugeSpecification : NSObject

- (instancetype)initFromFromValue:(nullable NSString *)value NS_DESIGNATED_INITIALIZER;

@property (nullable, nonatomic, strong, readonly) NSString *title;
@property (nullable, nonatomic, strong, readonly) NSString *type;

@property (nonatomic, assign, readonly) double thickness;
@property (nonatomic, assign, readonly) double height;
@property (nonatomic, assign, readonly) double width;
@property (nonatomic, assign, readonly) double maxValue;
@property (nonatomic, assign, readonly) double minValue;
@property (nonatomic, assign, readonly) double currentValue;
@property (nonatomic, assign, readonly) BOOL showMinMax;
@property (nonatomic, assign, readonly) BOOL showValue;
@property (null_resettable, nonatomic, copy) NSArray<GXLinearGaugeRangeSpec *> *ranges;

@property (nonatomic, assign, readonly, getter=isEmpty) BOOL empty;

@property (nonatomic, assign, readonly) double totalLength;

/**
 * Checks min, max and current values to check if they are consistent (i.e. min <= current <= max)
 */
@property (nonatomic, assign, readonly, getter=isValid) BOOL valid;

@property (nonatomic, assign, readonly) UIColor *colorForMinRange;
@property (nonatomic, assign, readonly) UIColor *colorForMaxRange;

@end

NS_ASSUME_NONNULL_END

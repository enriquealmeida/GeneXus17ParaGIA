//
//  GXLinearGaugeSpecification.h
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/04/11.
//  Copyright 2011 Artech. All rights reserved.
//

@import Foundation;
@import UIKit;

NS_ASSUME_NONNULL_BEGIN

@interface GXLinearGaugeRangeSpec : NSObject

- (instancetype)initWithProperies:(nullable NSDictionary<NSString *, id> *)properties NS_DESIGNATED_INITIALIZER;

// Color for this Range, it is a hex number color
@property (nullable, nonatomic, strong, readonly) UIColor *color;
// Take into account that this property should be compatible with Maximum and Minimun value
@property (nonatomic, assign, readonly) double length;
@property (nullable, nonatomic, strong, readonly) NSString *name;

@end

NS_ASSUME_NONNULL_END

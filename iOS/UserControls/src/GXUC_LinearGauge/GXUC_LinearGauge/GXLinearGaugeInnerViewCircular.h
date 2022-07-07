//
//  GXLinearGaugeInnerViewCircular.h
//  GXLinearGaugeInnerViewCircular
//
//  Created by gmilano.
//

#import <GXUCLinearGauge/GXLinearGaugeInnerViewBase.h>

NS_ASSUME_NONNULL_BEGIN

@interface GXLinearGaugeInnerViewCircular : GXLinearGaugeInnerViewBase

@property(nonatomic, strong, null_resettable) UIColor *innerTextColor;
@property(nonatomic, strong, null_resettable) UIFont *innerTextFont;
@property(nonatomic, strong, null_resettable) UIColor *outerTextColor;
@property(nonatomic, strong, null_resettable) UIFont *outerTextFont;

@end

NS_ASSUME_NONNULL_END

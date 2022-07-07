//
//  GXLinearGaugeGradientView.m
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/12/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXLinearGaugeGradientView.h"
@import QuartzCore;

@implementation GXLinearGaugeGradientView

+ (Class)layerClass  {
    return [CAGradientLayer class];
}

#pragma mark - Public

- (void)add3DGradientEffect {
	CAGradientLayer *layer = (CAGradientLayer *)[self layer];
	CGColorRef color1 = [[UIColor colorWithWhite:1 alpha:.4] CGColor];
	CGColorRef color2 = [[UIColor colorWithWhite:0 alpha:.4] CGColor];
	
	layer.colors = @[(__bridge id)color1, (__bridge id)color2];
}

@end
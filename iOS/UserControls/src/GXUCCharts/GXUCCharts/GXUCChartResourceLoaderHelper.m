//
//  GXUCResourceLoaderHelper.m
//  UCChart
//
//  Created by admin on 7/29/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GXUCChartResourceLoaderHelper.h"
@import GXFoundation;

@implementation GXUCChartResourceLoaderHelper

+ (UIImage *)loadImageForBundle:(NSString *)imageName {
	return [UIImage imageNamed:imageName inBundle:[NSBundle bundleForClass:self] compatibleWithTraitCollection:nil];
}

@end

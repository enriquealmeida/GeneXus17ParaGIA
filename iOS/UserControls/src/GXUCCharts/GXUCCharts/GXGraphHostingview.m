//
//  GXGraphHostingview.m
//  GXFlexibleClient
//
//  Created by Fabian Inthamoussu on 18/02/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXGraphHostingview.h"
@import QuartzCore;

@implementation GXGraphHostingview

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    [[NSNotificationCenter defaultCenter] postNotificationName:@"GraphHostingViewClick" object:self];
	[super touchesEnded:touches withEvent:event];
}


@end
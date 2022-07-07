//
//  GXPieChartProperties.m
//  UCChart
//
//  Created by admin on 12/1/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GXPieChartProperties.h"

@implementation GXPieChartProperties

@synthesize numberOfDecimals = _numberOfDecimals;
@synthesize existCommentAttribute = _existCommentAttribute;

- (id)init
{
    self = [super init];
    if (self) {
        _numberOfDecimals = 0;
        _existCommentAttribute = NO;
    }
    
    return self;
}

@end

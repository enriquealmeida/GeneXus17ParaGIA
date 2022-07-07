//
//  GXPieChartSpecification.m
//  UCChart
//
//  Created by admin on 12/1/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GXPieChartPoint.h"

@implementation GXPieChartPoint

@synthesize valueXAttribute = _valXAttribute;
@synthesize valueSerieAttribute = _valSerieAttribute;
@synthesize valueCommentsAttribute = _valCommentsAttribute;

- (NSString *)description {
	return [NSString stringWithFormat:@"<%@\n\tvalueXAttribute: %@\n\tvalueSerieAttribute: %@\n\tvalueCommentsAttribute: %@>", [super description], _valXAttribute, _valSerieAttribute, _valCommentsAttribute];
}

@end

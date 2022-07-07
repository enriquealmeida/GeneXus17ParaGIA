//
//  GXLinearGaugeSpecification.m
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/04/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXLinearGaugeRangeSpec.h"
@import GXFoundation;


@implementation GXLinearGaugeRangeSpec

@synthesize color = _color;
@synthesize length = _length;
@synthesize name = _name;

#pragma mark Public Methods

- (instancetype)init {
	return [self initWithProperies:nil];
}

- (instancetype)initWithProperies:(nullable NSDictionary<NSString *,id> *)properties {
	self = [super init];
	if (properties != nil) {
		[self deserialize:properties];
	}
	return self;
}

- (BOOL)isEqual:(id)object {
	if (object != self && [object isKindOfClass:self.class]) {
		typeof(self) other = object;
		return other.length == self.length &&
		(other.color == self.color || (self.color != nil && [other.color isEqual:self.color])) &&
		(other.name == self.name || (self.name != nil && [other.name isEqualToString:self.name]));
	}
	else {
		return [super isEqual:object];
	}
}

- (NSUInteger)hash {
	return self.name.hash ^ @(self.length).hash ^ self.color.hash;
}

- (void)deserialize:(NSDictionary<NSString *, id> *)properties
{
	NSParameterAssert(properties);
	NSString *colorStr = [GXUtilities nonEmptyStringFromObject:properties[@"Color"]];
	if (colorStr != nil) {
		colorStr = [colorStr stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
		if (colorStr.length > 0) {
			colorStr = [colorStr characterAtIndex:0] == '#' ? colorStr : [@"#" stringByAppendingString:colorStr];
		}
		else {
			colorStr = nil;
		}
	}
    _color = [UIColor colorFromValue:colorStr];
	_length = MAX(0, [GXUtilities doubleNumberFromValue:properties[@"Length"]].doubleValue);
    _name = [GXUtilities nonEmptyStringFromObject:properties[@"Name"]];
}

@end

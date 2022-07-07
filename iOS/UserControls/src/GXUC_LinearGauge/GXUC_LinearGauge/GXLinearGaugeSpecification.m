//
//  GXLinearGaugeSpecification.m
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/04/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXLinearGaugeSpecification.h"
@import GXFoundation;
#import "GXLinearGaugeRangeSpec.h"

@implementation GXLinearGaugeSpecification {
	NSArray<GXLinearGaugeRangeSpec *> *_ranges;
    double _totalLength;
}

- (instancetype)init {
	return [self initFromFromValue:nil];
}

- (instancetype)initFromFromValue:(nullable NSString *)value {
	self = [super init];
	[self deserializeFromValue:value];
	return self;
}

@synthesize title = _title;
@synthesize type = _type;
@synthesize thickness = _thickness;
@synthesize height = _height;
@synthesize width = _width;
@synthesize maxValue = _maxValue;
@synthesize minValue = _minValue;
@synthesize currentValue = _currentValue;
- (nonnull NSArray<GXLinearGaugeRangeSpec *> *)ranges {
	return _ranges ? : @[];
}
@synthesize showMinMax =_showMinMax;
@synthesize showValue = _showValue;

#define kShowMinMaxDefault NO
#define kShowValueDefault YES

- (BOOL)isEmpty {
	return _totalLength == 0 &&
	self.title == nil &&
	self.type == nil &&
	self.thickness == 0 &&
	self.height == 0 &&
	self.width == 0 &&
	self.maxValue == 0 &&
	self.minValue == 0 &&
	self.currentValue == 0 &&
	self.showMinMax == kShowMinMaxDefault &&
	self.showValue == kShowValueDefault &&
	self.ranges.count == 0;
}

- (BOOL)isEqual:(id)object {
	if (object != self && [object isKindOfClass:self.class]) {
		typeof(self) other = object;
		return other.currentValue == self.currentValue &&
		other.showMinMax == self.showMinMax &&
		other.showValue == self.showValue &&
		other.thickness == self.thickness &&
		other.height == self.height &&
		other.width == self.width &&
		other.maxValue == self.maxValue &&
		other.minValue == self.minValue &&
		[other.ranges isEqualToArray:self.ranges] &&
		(other.title == self.title || (self.title != nil && [other.title isEqualToString:self.title])) &&
		(other.type == self.type || (self.type != nil && [other.type isEqualToString:self.type]));
	}
	else {
		return [super isEqual:object];
	}
}

- (NSUInteger)hash {
	return self.ranges.hash ^ @(self.currentValue).hash ^ @(self.showMinMax).hash ^ @(self.showValue).hash ^
	@(self.height).hash ^ @(self.width).hash ^ @(self.maxValue).hash ^ @(self.minValue).hash ^ self.title.hash ^ self.type.hash;
}

#pragma mark Public Methods

- (double)totalLength {
    if (_totalLength == 0 && _ranges.count > 0) {
        double result = 0;
		for (GXLinearGaugeRangeSpec *range in self.ranges) {
			result += range.length;
		}
        _totalLength = result;
    }
    return _totalLength;
}

- (BOOL)isValid {
    return self.minValue <= self.currentValue && self.currentValue <= self.maxValue;
}

- (UIColor *)colorForMinRange {
	return self.ranges.firstObject.color ? : [UIColor blackColor];
}

- (UIColor *)colorForMaxRange {
	return self.ranges.lastObject.color ? : [UIColor blackColor];
}

#pragma mark Private Methods

- (void)deserializeFromValue:(nullable NSString *)value {
	id parsedValue = nil;
	if (value.length > 0) {
		parsedValue = [value yajl_JSON:NULL];
	}
	if ([parsedValue isKindOfClass:[NSDictionary class]]) {
		[self deserializeFromDictionary:parsedValue];
	}
	else {
		[self resetAsEmpty];
	}
}

- (void)resetAsEmpty {
	_title = nil;
	_type = nil;
	_thickness = 0;
	_height = 0;
	_width = 0;
	_maxValue = 0;
	_minValue = 0;
	_currentValue = 0;
	_showMinMax = kShowMinMaxDefault;
	_showValue = kShowValueDefault;
	_ranges = nil;
	_totalLength = 0;
}

- (void)deserializeFromDictionary:(NSDictionary *)properties {
	NSParameterAssert(properties);
	_title = [GXUtilities stringFromObject:properties[@"Title"]];
	_type = [GXUtilities stringFromObject:properties[@"Type"]];
	_thickness =  MAX(0, [GXUtilities doubleNumberFromValue:properties[@"Thickness"]].doubleValue);
	_height =  MAX(0, [GXUtilities doubleNumberFromValue:properties[@"Height"]].doubleValue);
	_maxValue = [GXUtilities doubleNumberFromValue:properties[@"MaxValue"]].doubleValue;
	_minValue = [GXUtilities doubleNumberFromValue:properties[@"MinValue"]].doubleValue;
	_currentValue = [GXUtilities doubleNumberFromValue:properties[@"Value"]].doubleValue;
	_showMinMax = [GXUtilities boolFromValue:properties[@"ShowMinMax"] defaultValue:kShowMinMaxDefault];
	_showValue = [GXUtilities boolFromValue:properties[@"ShowValue"] defaultValue:kShowValueDefault];
	_ranges = [[GXUtilities arrayFromObject:properties[@"Ranges"]] map:^id(id rangeDict) {
		return [[GXLinearGaugeRangeSpec alloc] initWithProperies:[rangeDict isKindOfClass:[NSDictionary class]] ? rangeDict : nil];
	}];
	_totalLength = 0;
}

@end

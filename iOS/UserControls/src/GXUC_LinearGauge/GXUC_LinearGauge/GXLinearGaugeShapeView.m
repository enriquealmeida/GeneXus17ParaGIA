//
//  GXLinearGaugeShapeView.m
//  GXUC_LinearGauge
//
//  Created by Pablo Musso on 7/04/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXLinearGaugeShapeView.h"

@implementation GXLinearGaugeShapeView

@synthesize shapeColor = _shapeColor;
- (void)setShapeColor:(UIColor *)shapeColor {
	if (_shapeColor != shapeColor) {
		if (shapeColor == nil || ![_shapeColor isEqual:shapeColor]) {
			[self setNeedsDisplay];
		}
		_shapeColor = shapeColor;
	}
}
@synthesize shapeType = _shapeType;
- (void)setShapeType:(GXLinearGaugeShapeType)shapeType {
	if (_shapeType != shapeType) {
		_shapeType = shapeType;
		[self setNeedsDisplay];
	}
}

#pragma mark - Init & dealloc

- (instancetype)initWithFrame:(CGRect)frame {
	return [self initWithFrame:frame shapeType:GXLinearGaugeShapeTypeValue];
}

- (instancetype)initWithFrame:(CGRect)frame shapeType:(GXLinearGaugeShapeType)shapeType {
	self = [super initWithFrame:frame];
	_shapeType = shapeType;
	self.backgroundColor = UIColor.clearColor;
    return self;
}

#define kGXShapeType @"shapeType.gx"
#define kGXShapeColor @"shapeColor.gx"

- (nullable instancetype)initWithCoder:(NSCoder *)aDecoder {
	self = [super initWithCoder:aDecoder];
	if (self) {
		_shapeType = [aDecoder decodeIntegerForKey:kGXShapeType];
		_shapeColor = [aDecoder decodeObjectForKey:kGXShapeColor];
	}
	return self;
}

- (void)encodeWithCoder:(NSCoder *)aCoder {
	[super encodeWithCoder:aCoder];
	[aCoder encodeInteger:(NSInteger)_shapeType forKey:kGXShapeType];
	[aCoder encodeObject:_shapeColor forKey:kGXShapeColor];
}

#pragma mark UIView Overrides

- (void)drawRect:(CGRect)rect {
    CGContextRef context = UIGraphicsGetCurrentContext();    
    CGContextSetRGBStrokeColor(context, 255, 255, 224, 1);
    
    CGContextBeginPath(context);
    CGContextSetFillColorWithColor(context, self.shapeColor.CGColor);
    
    CGPoint p1;
    CGPoint p2;
    CGPoint p3;
    
    switch (_shapeType) {
        case GXLinearGaugeShapeTypeMin:
            p1 = CGPointMake(0, 0);
            p2 = CGPointMake(0,self.bounds.size.height);
            p3 = CGPointMake(self.bounds.size.width, self.bounds.size.height);
            break;
            
        case GXLinearGaugeShapeTypeMax:
            p1 = CGPointMake(self.bounds.size.width, 0);
            p2 = CGPointMake(self.bounds.size.width, self.bounds.size.height);
            p3 = CGPointMake(0, self.bounds.size.height);
            break;    
            
        case GXLinearGaugeShapeTypeValue:
            p1 = CGPointMake(self.bounds.size.width/2, 0);
            p2 = CGPointMake(0, self.bounds.size.height);
            p3 = CGPointMake(self.bounds.size.width, self.bounds.size.height);
            break;
    }
    
    CGContextMoveToPoint(context, p1.x, p1.y);
    CGContextAddLineToPoint(context, p2.x, p2.y);
    CGContextAddLineToPoint(context, p3.x, p3.y);
    CGContextAddLineToPoint(context, p1.x, p1.y);
    
    CGContextClosePath(context);
    CGContextFillPath(context);
}

@end



//
//  GXControlBarIndicatorUIControl.m
//  GXFlexibleClient
//
//  Created by willy on 5/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "GXControlBarIndicatorUIControl.h"
#import <QuartzCore/QuartzCore.h>

@implementation GXControlBarIndicatorUIControl

@synthesize value = _value;
@synthesize maxValue = _maxValue;
@synthesize rightToLeft, barHeight = _barHeight;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        _bar = [[GXGradientView alloc] initWithFrame:CGRectZero];
        [_bar setBackgroundColor:[UIColor lightGrayColor]];
        [_bar.layer setMasksToBounds:YES];
        _lastRect = CGRectZero;
        
        
        _box = [[GXGradientView alloc] initWithFrame:CGRectZero];
        [_box setBackgroundColor:[UIColor colorWithRed:.6 green:.6 blue:.6 alpha:.5]];
        [_box.layer setMasksToBounds:YES];
        [self addSubview:_box];
        [self addSubview:_bar];
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

- (void)setBarHeight:(NSUInteger)barHeight {
    _barHeight = barHeight;
    [_bar.layer setCornerRadius:_barHeight/3];
    [_box.layer setCornerRadius:_barHeight/3];
}

- (void)setBarColor:(UIColor *)barColor {
    if (barColor) {
        const CGFloat* components = CGColorGetComponents([barColor CGColor]);
        CGFloat r = components[0];
        CGFloat g = components[1];
        CGFloat b = components[2];
        CGFloat a = CGColorGetAlpha([barColor CGColor]);
        [_bar setGradientColor:barColor];
        [_bar setBackgroundColor:[UIColor colorWithRed:r/2 green:g/2 blue:b/2 alpha:a]];
    }
}
- (CGRect)boxRect {
    CGRect boxRect = [self initialBarRect];
    boxRect.origin.x = 0;
    boxRect.size.width = self.frame.size.width;
    return boxRect;
}

- (CGRect)initialBarRect {
    CGRect barRect = [self bounds];
    barRect.origin.y +=(self.frame.size.height /2 - self.barHeight /2);
    barRect.size.height = self.barHeight;
    if (self.rightToLeft)
        barRect.origin.x += self.frame.size.width;
    barRect.size.width = 0;
    return barRect;
}

- (CGRect)barRect {
    CGRect barRect = [self initialBarRect];
    if (_maxValue) {
        CGFloat factor = (float)_value / _maxValue;
        CGFloat width = [self bounds].size.width * factor;
        barRect.size.width = width;
        if (self.rightToLeft)
            barRect.origin.x -= width;
    }
    return barRect;
}

- (void)layoutSubviews {
    CGRect initRect = CGRectEqualToRect(_lastRect,CGRectZero)?[self initialBarRect]:_lastRect;
    CGRect barRect = [self barRect];
    _lastRect = barRect;
    [_bar setFrame:initRect];
    [_box setFrame:[self boxRect]];
    [UIView animateWithDuration:1 animations:^{
        [_bar setFrame:barRect];
    } completion:^(BOOL finished) {
    }];
}

- (void)setValue:(NSUInteger)value {
    if (value != _value) {
        _value = value;
        [self setNeedsLayout];
    }
}

- (void)setMaxValue:(NSUInteger)maxValue {
    if (maxValue != _maxValue) {
        _maxValue = maxValue;
        [self setNeedsLayout];
    }
}

@end

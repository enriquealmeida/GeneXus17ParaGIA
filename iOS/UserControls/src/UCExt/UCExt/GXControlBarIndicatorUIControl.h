//
//  GXControlBarIndicatorUIControl.h
//  GXFlexibleClient
//
//  Created by willy on 5/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GXGradientView.h"

@interface GXControlBarIndicatorUIControl : UIControl {
    @private
    CGRect _lastRect;
    NSUInteger _value;
    NSUInteger _maxValue;
    GXGradientView *_bar;
    GXGradientView *_box;
}

@property(nonatomic, assign)NSUInteger value;
@property(nonatomic, assign)NSUInteger maxValue;
@property(nonatomic, assign)BOOL rightToLeft;
@property(nonatomic, assign)NSUInteger barHeight;

-(void)setBarColor:(UIColor *)barColor;

@end

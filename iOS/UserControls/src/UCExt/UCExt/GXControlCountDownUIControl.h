//
//  GXControlCountDownUIControl.h
//  GXFlexibleClient
//
//  Created by willy on 4/30/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol GXControlCountDownUIControlDelegateProtocol <NSObject>

- (void)gXControlCountDownUIControlCounterEnded;
- (void)gXControlCountDownUIControlTick;

@end

@interface GXControlCountDownUIControl : UIControl {
    @private
    UILabel *_text;
    NSUInteger _value;
    NSInteger _internalValue;
    NSInteger _max;
    BOOL _running;
    BOOL _ignoreReset;
    NSUInteger _blinkBelow;
    BOOL _gaugeON;
    id<GXControlCountDownUIControlDelegateProtocol> __unsafe_unretained _delegate;
    NSTimeInterval _startTime;
}

@property(nonatomic, assign)NSUInteger value;
@property(nonatomic, assign)NSUInteger blinkBelow;

- (void)suspend;
- (void)resume;

- (void)setDelegate:(id<GXControlCountDownUIControlDelegateProtocol>)delegate;

@end

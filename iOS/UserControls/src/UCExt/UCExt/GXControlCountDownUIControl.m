//
//  GXControlCountDownUIControl.m
//  GXFlexibleClient
//
//  Created by willy on 4/30/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "GXControlCountDownUIControl.h"
#import <QuartzCore/QuartzCore.h>
#import <AudioToolbox/AudioToolbox.h>

#define kInterval 0.05
#define kArcWifth 20

typedef enum {
	ClockSountTick,
	ClockSoundAlarm,
	Sound_COUNT
	
} ClockSoundEffects;

struct SoundEffect {
ClockSoundEffects	clockSoundID;
const char*			soundResourceName;
SystemSoundID		systemSoundID;
};

static struct SoundEffect soundEffectTable[] = {
	{ ClockSountTick, "clocktick", 0 },
    { ClockSoundAlarm, "clockalarm", 0 }
};

@interface CountDownSoundEffects : NSObject {
}

+ (CountDownSoundEffects*)sharedInstance;
- (void)initializeSoundEffects;
- (void)playSoundEffect:(ClockSoundEffects)soundID;

@end

static CountDownSoundEffects* g_instance;

@implementation CountDownSoundEffects 

+ (CountDownSoundEffects*)sharedInstance {
	if (g_instance == nil) {
		g_instance = [CountDownSoundEffects new];
		[g_instance initializeSoundEffects];
	}
	return g_instance;
}

void AudioInterruptionListener(void *inClientData, UInt32 inInterruptionState) {
}

- (BOOL)createSoundWithName:(NSString*)name idPtr:(SystemSoundID*)idPtr {
	NSString* path = [[NSBundle mainBundle] pathForResource:name ofType:@"wav"];
    
	NSURL* url = [NSURL fileURLWithPath:path];
	OSStatus status = AudioServicesCreateSystemSoundID((__bridge CFURLRef)url, idPtr);
	return (status == 0);
}

- (void)initializeSoundEffects {
	AudioSessionInitialize(NULL, NULL, AudioInterruptionListener, (__bridge void *)(self));
	
	for (unsigned int i = 0; i < sizeof(soundEffectTable)/sizeof(struct SoundEffect); i++) {
		NSString* soundName = [NSString stringWithUTF8String:soundEffectTable[i].soundResourceName];
		[self createSoundWithName:soundName idPtr:&soundEffectTable[i].systemSoundID];
	}
}

- (void)playSoundEffect:(ClockSoundEffects)soundID {
	if (soundID < Sound_COUNT) {
		AudioServicesPlaySystemSound(soundEffectTable[soundID].systemSoundID);
	}
}


@end

@implementation GXControlCountDownUIControl

@synthesize value = _value;
@synthesize blinkBelow = _blinkBelow;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self setBackgroundColor:[UIColor clearColor]];
        _text = [[UILabel alloc] initWithFrame:CGRectMake(0,0,frame.size.width,frame.size.height)];
        [_text setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight];
        [_text setFont:[UIFont fontWithName:@"DS-Digital" size:30]];
        [_text setBackgroundColor:[UIColor clearColor]];
        [_text setTextColor:[UIColor greenColor]];
        [_text setTextAlignment:UITextAlignmentCenter];
        [self addSubview:_text];
        _value = 0;
        _blinkBelow = 5;
        _running = NO;
        _ignoreReset = NO;
    }
    return self;
}

- (void)setValue:(NSUInteger)value {
    if (_ignoreReset || value == 0)
        return;
    
    _internalValue = value * 100;
    _value = value;
    _max = _internalValue;
    _internalValue +=100;
    [_text setText:[NSString stringWithFormat:@"%d",self.value]];
    [self setNeedsDisplay];
    if (!_running) {
        [self performSelector:@selector(start) withObject:nil afterDelay:1];
    }
    _ignoreReset = YES;
}

- (void)suspend {
    _running = NO;
}

- (void)resume {
    _running = YES;
    [self timer];
}

- (void)start {
    _startTime = [[NSDate date] timeIntervalSince1970];
    [self tick];
    _running = YES;
    [self timer];
}

- (void)timer {
    [self performSelector:@selector(tick) withObject:nil afterDelay:kInterval];
}

- (CGPathRef)newPath {
    
    CGRect rect = [self bounds];
    CGMutablePathRef curvedPath = CGPathCreateMutable();
    CGPathAddArc(curvedPath, NULL, rect.origin.x + rect.size.width/2, rect.origin.y + rect.size.height/2 , rect.size.height/2-kArcWifth/2,2*M_PI - M_PI_2, -M_PI_2 + (2*M_PI / _max * (_max - (_internalValue-100))), YES);
    
    return curvedPath;
}

- (void)setDelegate:(id<GXControlCountDownUIControlDelegateProtocol>)delegate {
    _delegate = delegate;
}

- (void)notifyCounterEnd {
    if (_delegate) {
        [_delegate gXControlCountDownUIControlCounterEnded];
    }
}

- (void)notifyTick {
    if (_delegate) {
        [_delegate gXControlCountDownUIControlTick];
    }
}

- (void)tick {
    if (!_running) {
        return;
    }
    NSTimeInterval diff = [[NSDate date] timeIntervalSince1970] - _startTime;
    
    _internalValue = 100 + _max - diff*100;
    if (_internalValue < 0)
        _internalValue = 0;
    
    uint intValue = _internalValue / 100;
    if (intValue != _value) {
        _value = intValue;
        [self notifyTick];
        if (_value) {
            [[CountDownSoundEffects sharedInstance]playSoundEffect:ClockSountTick];
        }
        else {
            [[CountDownSoundEffects sharedInstance]playSoundEffect:ClockSoundAlarm];
            [UIView animateWithDuration:1 animations:^{
                _text.transform = CGAffineTransformMakeScale(10,10);
                _text.alpha = 0;
            } completion:^(BOOL finished) {
                _text.transform = CGAffineTransformMakeScale(1,1);
                _text.alpha = 1;
                [self performSelector:@selector(notifyCounterEnd) withObject:nil afterDelay:1];
            }];
        }
    }
    
    
    [_text setText:[NSString stringWithFormat:@"%d",self.value]];
    
    if (self.value <= self.blinkBelow) {
        if (_internalValue %2 == 0) {
            _gaugeON = !_gaugeON;
        }
    } else {
        _gaugeON = YES;
    }
    if (self.value)
        [self timer];
    else {
        _running = NO;
    }
    
    [self setNeedsDisplay];
}

- (void)drawRect:(CGRect)rect {
    if (self.value > self.blinkBelow || _gaugeON) {
        CGContextRef context = UIGraphicsGetCurrentContext();
        
        CGContextSetLineWidth(context, kArcWifth);
        CGContextSetShouldAntialias(context, YES);
        CGFloat color[4] = {1.0f, 1.0f, 1.0f, 0.5f};
        CGContextSetStrokeColor(context, color);
        
		CGPathRef path = [self newPath];
        CGContextAddPath(context, path);
		CGPathRelease(path);
        
        CGContextStrokePath(context);
    }
}

@end

//
//  GXUCLoadingView.m
//  GXFlexibleClient
//
//  Created by Pablo Musso on 5/5/11.
//  Copyright 2011 Artech. All rights reserved.
//


#import "GXUCLoadingView.h"
@import QuartzCore;

@implementation GXUCLoadingView {
    UILabel *_loadingLabel;
    UIActivityIndicatorView *_activityIndicatorView;
}

@synthesize labelThemeClass = _labelThemeClass;

- (void)setLabelThemeClass:(GXThemeClass *)labelThemeClass {
    _labelThemeClass = labelThemeClass;
    [_loadingLabel setTextColor:[self foreColor]];
}

@synthesize backgroundThemeClass = _backgroundThemeClass;

- (void)setBackgroundThemeClass:(GXThemeClass *)backgroundThemeClass {
    _backgroundThemeClass = backgroundThemeClass;
    [_activityIndicatorView setActivityIndicatorViewStyle:[self activityIndicatorViewStyle]];
}

#pragma mark - Init & dealloc

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self setOpaque:NO];
        [self setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight];
        
        CGFloat xPos = frame.size.width / 2;
        CGFloat yPos = frame.size.height / 2;
        
        const CGFloat DEFAULT_LABEL_WIDTH =  100;//280.0;
        const CGFloat DEFAULT_LABEL_HEIGHT = 50;//50.0;
        CGRect labelFrame = CGRectMake(xPos, yPos, DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_HEIGHT);
        _loadingLabel = [[UILabel alloc] initWithFrame:labelFrame];
		_loadingLabel.text = [GXResources.currentLanguage translationFor:@"GXM_Loading" notFoundMessage:nil];
        _loadingLabel.textColor = [self foreColor];
        _loadingLabel.backgroundColor = [UIColor clearColor];
        _loadingLabel.textAlignment = NSTextAlignmentCenter;
        _loadingLabel.font = [UIFont boldSystemFontOfSize:kUIFontLabelFontSize];
        _loadingLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin |UIViewAutoresizingFlexibleBottomMargin;
        
        [self addSubview:_loadingLabel];
        
        _activityIndicatorView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:[self activityIndicatorViewStyle]];
        [self addSubview:_activityIndicatorView];

        _activityIndicatorView.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleBottomMargin;
        [_activityIndicatorView startAnimating];
        
        CGFloat totalHeight = _loadingLabel.frame.size.height + _activityIndicatorView.frame.size.height;
        labelFrame.origin.x = floor(0.5 * (frame.size.width - DEFAULT_LABEL_WIDTH));
        labelFrame.origin.y = floor(0.5 * (frame.size.height - totalHeight));
        _loadingLabel.frame = labelFrame;
        
        CGRect activityIndicatorRect = _activityIndicatorView.frame;
        activityIndicatorRect.origin.x = 0.5 * (frame.size.width - activityIndicatorRect.size.width);
        activityIndicatorRect.origin.y = _loadingLabel.frame.origin.y + _loadingLabel.frame.size.height;
        _activityIndicatorView.frame = activityIndicatorRect;
    }
    
    return self;
}

#pragma mark - Private

- (UIColor *)foreColor {
	if (_labelThemeClass != nil && [_labelThemeClass conformsToProtocol:@protocol(GXThemeClassWithFont)]) {
		return ((id<GXThemeClassWithFont>)_labelThemeClass).foreColor;
	}
	return [UIColor blackColor];
}

- (UIColor *)backgroundColor {
    if (_backgroundThemeClass && [_backgroundThemeClass conformsToProtocol:@protocol(GXThemeClassWithBackground)]) {
        return [(id<GXThemeClassWithBackground>)_backgroundThemeClass backgroundColor];
    }
    else {
        return [UIColor whiteColor];
    }
}

- (UIActivityIndicatorViewStyle)activityIndicatorViewStyle {
    UIColor *bgColor = [self backgroundColor];

    if ([bgColor isEqual:[UIColor whiteColor]])
        return UIActivityIndicatorViewStyleGray;

    if ([bgColor isEqual:[UIColor blackColor]])
        return UIActivityIndicatorViewStyleWhiteLarge;

    return UIActivityIndicatorViewStyleGray;
}

@end

//
//  GXUC_ZoomView.m
//  UCChart
//
//  Created by admin on 7/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GXUCZoomView.h"

@implementation GXUCZoomView
@synthesize zoomMode = _mode;
@synthesize labelThemeClass = _labelThemeClass;
@synthesize backgroundThemeClass = _backgroundThemeClass;
@synthesize timePeriodCollection = _timePeriodCollection;


#pragma mark Construction/Destruction

- (id)initWithFrame:(CGRect)frame withTimePeriod:(NSArray*)timePeriod withLabelClassTheme:(GXThemeClass*)lThemeClass andBackgroundThemeClass:(GXThemeClass*) bThemeClass 
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code

        _oneDay = [[GXUCLinkView alloc] initWithNotificationName:@"OneDayClick"];
        _oneWeek = [[GXUCLinkView alloc] initWithNotificationName:@"OneWeekClick"];
        _fiveDays = [[GXUCLinkView alloc] initWithNotificationName:@"FiveDaysClick"];
        _oneMonth = [[GXUCLinkView alloc] initWithNotificationName:@"OneMonthClick"];
        _threeMonths = [[GXUCLinkView alloc] initWithNotificationName:@"ThreeMonthsClick"];
        _sixMonths = [[GXUCLinkView alloc] initWithNotificationName:@"SixMonthsClick"];
        _oneYear = [[GXUCLinkView alloc] initWithNotificationName:@"OneYearClick"];
        _maxTime = [[GXUCLinkView alloc] initWithNotificationName:@"MaxTimeClick"];    
        _zoom = [[GXUCLinkView alloc] init]; 

        _timePeriodCollection = timePeriod;

        _labelThemeClass = lThemeClass;

        _backgroundThemeClass = bThemeClass;


        if ([self includeTimeinZoom:@"1y"])
        {
            _mode = OneYear;
            _oldMode = OneYear;
        }
        else
        {
            _mode = MaxTime;
            _oldMode = MaxTime;
        }



        [self drawZoomView];
    }
    return self;
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (UIColor *)getForeColor {
	if (self.labelThemeClass != nil && [self.labelThemeClass conformsToProtocol:@protocol(GXThemeClassWithFont)]) {
		return ((id<GXThemeClassWithFont>)self.labelThemeClass).foreColor;
	}
    return [UIColor blackColor];
}

-(UIColor*) getForeColorForSelectedLink
{
    UIColor* backgroundColor = [self getBackgroundColor];

    if ([backgroundColor isEqual:[UIColor whiteColor]])
        return [UIColor blueColor];

    if ([backgroundColor isEqual:[UIColor blackColor]])
        return [UIColor yellowColor];

    return [UIColor blueColor];
}

-(UIColor*) getBackgroundColor
{
    GXThemeClass* themeClass = self.backgroundThemeClass;

    if (themeClass == nil)
        return [UIColor whiteColor];

	if ([themeClass conformsToProtocol:@protocol(GXThemeClassWithBackground)]) {
		return ((id<GXThemeClassWithBackground>)themeClass).backgroundColor;
	}
    else
        return [UIColor whiteColor];
}

+(UIFont*) getUsedUIFontLink
{
    return[UIFont fontWithName:@"Helvetica-Bold" size:(12.0)];
}

+(UIFont*) getUsedUIFontCaption
{
    return[UIFont fontWithName:@"Helvetica-Bold" size:(14.0)];
}


-(BOOL) includeTimeinZoom: (NSString*) time
{
    if (_timePeriodCollection == nil)
        return true;

    NSUInteger i = 0;
    BOOL found = false;

    while ((!found) && (i < [_timePeriodCollection count]))
    {
        NSString* item = (NSString*)[_timePeriodCollection objectAtIndex:i];
        found = [item isEqualToString:time];
        i++;
    }

    return found;

}

-(void) setTextLabelFrame:(GXUCLinkView*) link isLink:(BOOL) isLink text:(NSString*) text position:(CGFloat) position  linkSize:(CGSize) linkSize;
{
    // link.autoresizingMask = UIViewAutoresizingNone;
    [link setFrame:CGRectMake(position,  0, linkSize.width +6, linkSize.height)];
    link.adjustsFontSizeToFitWidth = YES;

    link.textAlignment = NSTextAlignmentCenter;
    link.textColor =  [self getForeColor];
    link.text = text;
    link.backgroundColor = [UIColor clearColor];
    link.font = [GXUCZoomView getUsedUIFontLink];
}

-(void) refreshZoomView {
    _oneWeek.textColor = [self getForeColor];
    _oneDay.textColor = [self getForeColor];
    _fiveDays.textColor = [self getForeColor];
    _oneMonth.textColor = [self  getForeColor];
    _threeMonths.textColor = [self getForeColor];
    _sixMonths.textColor = [self getForeColor];
    _oneYear.textColor = [self getForeColor];
    _maxTime.textColor = [self getForeColor];

    switch (_mode) {
        case OneDay:
            _oneDay.textColor = [self getForeColorForSelectedLink];
            break;
        case FiveDays:
            _fiveDays.textColor = [self getForeColorForSelectedLink];
            break;
        case OneWeek:
            _oneWeek.textColor = [self getForeColorForSelectedLink];
            break;
        case OneMonth:
            _oneMonth.textColor = [self getForeColorForSelectedLink];
            break;
        case ThreeMonths:
            _threeMonths.textColor = [self getForeColorForSelectedLink];
            break;
        case SixMonths:
            _sixMonths.textColor = [self getForeColorForSelectedLink];
            break;
        case OneYear:
            _oneYear.textColor = [self getForeColorForSelectedLink];
            break;
        case MaxTime:
            _maxTime.textColor = [self getForeColorForSelectedLink];
            break;
        default:
            break;
    }

}

- (void)oneDayClickHandler:(NSNotification *)notification {
    _oldMode = _mode;
    _mode = OneDay;

    if (_mode != _oldMode) {
        [self refreshZoomView];
        [[NSNotificationCenter defaultCenter]
         postNotificationName:@"ZoomViewClick"
         object:self];
    }
}

-(void)fiveDayClickHandler: (NSNotification *) notification
{
    _oldMode = _mode;
    _mode = FiveDays;

    if (_mode != _oldMode)
    {
        [self refreshZoomView];
        [[NSNotificationCenter defaultCenter]
         postNotificationName:@"ZoomViewClick"
         object:self];
    }

}

-(void)oneWeekClickHandler: (NSNotification *) notification
{
    _oldMode = _mode;
    _mode = OneWeek;

    if (_mode != _oldMode)
    {
        [self refreshZoomView];
        [[NSNotificationCenter defaultCenter]
         postNotificationName:@"ZoomViewClick"
         object:self];
    }

}

-(void)oneMonthClickHandler: (NSNotification *) notification
{
    _oldMode = _mode;
    _mode = OneMonth;

    if (_mode != _oldMode)
    {
        [self refreshZoomView];
        [[NSNotificationCenter defaultCenter]
         postNotificationName:@"ZoomViewClick"
         object:self];
    }

}

-(void)threeMonthsClickHandler: (NSNotification *) notification
{
    _oldMode = _mode;
    _mode = ThreeMonths;

    if (_mode != _oldMode)
    {
        [self refreshZoomView];
        [[NSNotificationCenter defaultCenter]
         postNotificationName:@"ZoomViewClick"
         object:self];
    }
}

-(void)sixMonthsClickHandler: (NSNotification *) notification
{
    _oldMode = _mode;
    _mode = SixMonths;

    if (_mode != _oldMode)
    {
        [self refreshZoomView];
        [[NSNotificationCenter defaultCenter]
         postNotificationName:@"ZoomViewClick"
         object:self];
    }

}

-(void)oneYearClickHandler: (NSNotification *) notification
{
    _oldMode = _mode;
    _mode = OneYear;
    if (_mode != _oldMode)
    {
        [self refreshZoomView];
        [[NSNotificationCenter defaultCenter]
         postNotificationName:@"ZoomViewClick"
         object:self];
    }

}

-(void)maxTimeClickHandler: (NSNotification *) notification
{
    _oldMode = _mode;
    _mode = MaxTime;
    if (_mode != _oldMode)
    {
        [self refreshZoomView];
        [[NSNotificationCenter defaultCenter]
         postNotificationName:@"ZoomViewClick"
         object:self];
    }

}


-(void) drawZoomView;
{
    CGSize maxsize;
    maxsize.width = self.bounds.size.width;
    maxsize.height = self.bounds.size.height;


    CGFloat xposition = 0;
    NSString* text = @"Zoom:";
    CGSize size = [text gxSizeWithFont:[GXUCZoomView getUsedUIFontLink]];
    [self setTextLabelFrame:_zoom  isLink:NO  text:text position:xposition  linkSize:size];
    xposition = xposition + size.width + 10;
    [self addSubview:_zoom];


    text = @" 1d ";
    if ([self includeTimeinZoom:@"1d"])
    {
        size = [text gxSizeWithFont:[GXUCZoomView getUsedUIFontLink]];
        [self setTextLabelFrame:_oneDay  isLink:YES  text:text position:xposition  linkSize:size];
        xposition = xposition + size.width + 10;

        _oneDay.userInteractionEnabled = YES;
        [[NSNotificationCenter defaultCenter]
         addObserver:self
         selector:@selector(oneDayClickHandler:)
         name:@"OneDayClick"
         object:_oneDay];

        [self addSubview:_oneDay];
    }

    text = @" 5d ";
    if ([self includeTimeinZoom:@"5d"])
    {
        size = [text gxSizeWithFont:[GXUCZoomView getUsedUIFontLink]];
        [self setTextLabelFrame:_fiveDays  isLink:YES  text:text position:xposition  linkSize:size];
        xposition = xposition + size.width + 10;
        _fiveDays.userInteractionEnabled = YES;
        [[NSNotificationCenter defaultCenter]
         addObserver:self
         selector:@selector(fiveDayClickHandler:)
         name:@"FiveDaysClick"
         object:_fiveDays];
        [self addSubview:_fiveDays];
    }

    text = @" 1w ";
    if ([self includeTimeinZoom:@"1w"])
    {
        size = [text gxSizeWithFont:[GXUCZoomView getUsedUIFontLink]];
        [self setTextLabelFrame:_oneWeek  isLink:YES  text:text position:xposition  linkSize:size];
        xposition = xposition + size.width + 10;

        _oneWeek.userInteractionEnabled = YES;
        [[NSNotificationCenter defaultCenter]
         addObserver:self
         selector:@selector(oneWeekClickHandler:)
         name:@"OneWeekClick"
         object:_oneWeek];
        [self addSubview:_oneWeek];
    }


    text = @" 1m ";
    if ([self includeTimeinZoom:@"1m"])
    {
        size = [text gxSizeWithFont:[GXUCZoomView getUsedUIFontLink]];
        [self setTextLabelFrame:_oneMonth  isLink:YES  text:text position:xposition  linkSize:size];
        xposition = xposition + size.width + 10;

        _oneMonth.userInteractionEnabled = YES;
        [[NSNotificationCenter defaultCenter]
         addObserver:self
         selector:@selector(oneMonthClickHandler:)
         name:@"OneMonthClick"
         object:_oneMonth];
        [self addSubview:_oneMonth];
    }

    text = @" 3m ";
    if ([self includeTimeinZoom:@"3m"])
    {

        size = [text gxSizeWithFont:[GXUCZoomView getUsedUIFontLink]];
        [self setTextLabelFrame:_threeMonths  isLink:YES  text:text position:xposition  linkSize:size];
        xposition = xposition + size.width + 10;

        _threeMonths.userInteractionEnabled = YES;
        [[NSNotificationCenter defaultCenter]
         addObserver:self
         selector:@selector(threeMonthsClickHandler:)
         name:@"ThreeMonthsClick"
         object:_threeMonths];

        [self addSubview:_threeMonths];
    }

    text = @" 6m ";
    if ([self includeTimeinZoom:@"6m"])
    {

        size = [text gxSizeWithFont:[GXUCZoomView getUsedUIFontLink]];
        [self setTextLabelFrame:_sixMonths  isLink:YES  text:text position:xposition  linkSize:size];
        xposition = xposition + size.width + 10;

        _sixMonths.userInteractionEnabled = YES;
        [[NSNotificationCenter defaultCenter]
         addObserver:self
         selector:@selector(sixMonthsClickHandler:)
         name:@"SixMonthsClick"
         object:_sixMonths];

        [self addSubview:_sixMonths];
    }

    text = @" 1y ";
    if ([self includeTimeinZoom:@"1y"])
    {
        size = [text gxSizeWithFont:[GXUCZoomView getUsedUIFontLink]];
        [self setTextLabelFrame:_oneYear  isLink:YES  text:text position:xposition  linkSize:size];
        xposition = xposition + size.width + 10;

        _oneYear.userInteractionEnabled = YES;
        [[NSNotificationCenter defaultCenter]
         addObserver:self
         selector:@selector(oneYearClickHandler:)
         name:@"OneYearClick"
         object:_oneYear];

        [self addSubview:_oneYear];
    }

    text = @" Max ";
    size = [text gxSizeWithFont:[GXUCZoomView getUsedUIFontLink]];
    [self setTextLabelFrame:_maxTime  isLink:YES  text:text position:xposition  linkSize:size];

    _maxTime.userInteractionEnabled = YES;
    [[NSNotificationCenter defaultCenter]
     addObserver:self
     selector:@selector(maxTimeClickHandler:)
     name:@"MaxTimeClick"
     object:_maxTime];
    [self addSubview:_maxTime];

    [self refreshZoomView];

}

+ (CGFloat)getMaxHeight:(CGSize)maxsize { 
    UIFont* f = [GXUCZoomView getUsedUIFontLink];
    NSString* text = [GXResources translationFor:@"GXM_Zoom"];
    CGSize   size = [text gxMultilineSizeWithFont:f constrainedToSize:maxsize truncatingLastVisibleLine:YES];
    return size.height;
}

@end

//
//  SDChartsMainView.m
//  UCChart
//
//  Created by Pablo Musso on 10/19/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "SDChartsMainView.h"
#import "SDChartsMainView+Internal.h"
#import "GXChartColorsProvider.h"
#import "GXUCDateHelper.h"
#import "GXUC_SDCharts.h"

@implementation SDChartsMainView {
    UILabel *_noDataLabel;
    UIFont *_noDataLabelFont;
    NSString *_noDataLabelText;
}

@synthesize dataSource = _dataSource;
@synthesize delegate = _delegate;

@synthesize chartDataSDT = _chartDataSDT;
@synthesize backgroundThemeClass = _backgroundThemeClass;
@synthesize labelThemeClass = _labelThemeClass;

#pragma mark - Init & dealloc

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        _drawControlsinUI= YES;
        
        _noDataLabelFont = [UIFont boldSystemFontOfSize:17.0f];
        _noDataLabelText = @"There is no data";
        
        _noDataLabel = [[UILabel alloc ] initWithFrame:[self noDataLabelFrameForViewFrame:frame]];
        [_noDataLabel setText:_noDataLabelText];
        [_noDataLabel setFont:_noDataLabelFont];
        [_noDataLabel setTextColor:[self getForeColor]];
        [_noDataLabel setTextAlignment:NSTextAlignmentCenter];
        [_noDataLabel setBackgroundColor:[UIColor clearColor]];
        [self addSubview:_noDataLabel];
        
        _hostingView = [[GXGraphHostingview alloc] initWithFrame:[self hostingViewFrameForControlBounds:self.bounds]];
        _hostingView.userInteractionEnabled = YES;
        [self addSubview:_hostingView];
        
        _graph = [[CPTXYGraph alloc] initWithFrame:CGRectZero];
        [_graph applyTheme:[self getCPTheme]];
        
        _hostingView.collapsesLayers = YES;
        _hostingView.hostedGraph = _graph;
        _graph.paddingLeft = 10.0;
        _graph.paddingTop = 10.0;
        _graph.paddingRight = 10.0;
        _graph.paddingBottom = 10.0;
        
        [self addPlotToGraph];

        /*
         // TODO: subviews that should be initialized here
        GXUCZoomView *_zoomView;
        GXUCLoadingView *_loadingView;
        UISlider *_xValueSlider;
        GXUIImageButton *_refreshButton;
        UILabel *_xValueLabel;
        
        CPTXYAxis *_xAxis;
        CPTXYAxis *_yAxis;
        
        UILabel *_titleLabel;*/

        _backgroundThemeClass = nil;
        _labelThemeClass = nil;
    }
    return self;
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    _delegate = nil;
    _dataSource = nil;
}

#pragma mark - UIView overrides

- (void)layoutSubviews {
    BOOL emptyChart = [self isEmptyChart];
    
    [_noDataLabel setHidden:!emptyChart];

    [_hostingView setHidden:emptyChart];
    [_refreshButton setHidden:emptyChart];
    [_titleLabel setHidden:emptyChart];
    [_zoomView setHidden:emptyChart];
    [_xValueSlider setHidden:emptyChart];
    
    if (emptyChart) {
        [_noDataLabel setFrame:[self noDataLabelFrameForViewFrame:self.frame]];
    }
    else {
        [_hostingView setFrame:[self hostingViewFrameForControlBounds:self.bounds]];
    }
}

#pragma mark - Layout helpers

- (CGRect)noDataLabelFrameForViewFrame:(CGRect)frame {
	CGSize noDataLabelSize = [_noDataLabelText gxMultilineSizeWithFont:_noDataLabelFont constrainedToSize:frame.size truncatingLastVisibleLine:YES];
    CGRect noDataLabelFrame;
    noDataLabelFrame.size = noDataLabelSize;
    noDataLabelFrame.origin.x = (self.bounds.size.width - noDataLabelSize.width) / 2;
    noDataLabelFrame.origin.y = (self.bounds.size.height - noDataLabelSize.height) / 2;
    return GXPixelAlignedFrameWithMainScreenScale(noDataLabelFrame);
}

#pragma mark - Properties

- (GXUCChartType)chartType {
    GXTHROW_ABSTRACT_METHOD_EXCEPTION();
}

#pragma mark -

- (void)showLoadingView  {
    if (!_loadingView) {
        _loadingView = [[GXUCLoadingView alloc] initWithFrame:self.bounds];
        [self addSubview:_loadingView];
        
        CATransition *animation = [CATransition animation];
        [animation setType:kCATransitionFade];
        [[self layer] addAnimation:animation forKey:@"layerAnimation"];
        
        _loadingView.labelThemeClass = _labelThemeClass;
        _loadingView.backgroundThemeClass = self.backgroundThemeClass;
        _loadingView.userInteractionEnabled = YES;
    }
    else {
        [_loadingView setHidden:NO];       
        [self bringSubviewToFront:_loadingView];
        [_loadingView setFrame:self.bounds];
    }
}

- (void)refreshChart {
    [self showLoadingView];
    [self refreshChartInternal];
    [self drawChartUserControl];
}

- (UIColor *)getForeColor {
	if (self.labelThemeClass != nil && [self.labelThemeClass conformsToProtocol:@protocol(GXThemeClassWithFont)]) {
		return ((id<GXThemeClassWithFont>)self.labelThemeClass).foreColor;
	}
    return [UIColor blackColor];
}

- (UIColor*)getBackgroundColor {
	if ([self.backgroundThemeClass conformsToProtocol:@protocol(GXThemeClassWithBackground)]) {
		return ((id<GXThemeClassWithBackground>)self.backgroundThemeClass).backgroundColor;
	}
    return [UIColor clearColor];
}

#pragma mark - drawChartUserControl

- (void)drawRefreshButton {
    if (!_refreshButton) {
        UIImage *image =  [GXUCChartResourceLoaderHelper loadImageForBundle:@"ChartRefreshButton"];
        
        CGRect frame = CGRectMake(self.bounds.size.width - image.size.width - 10, self.bounds.size.height - image.size.height, image.size.width, image.size.height);
        _refreshButton = [[GXUIImageButton alloc ] initWithFrame:frame];
        _refreshButton.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleTopMargin;
        _refreshButton.userInteractionEnabled = YES;
        [_refreshButton setImage:image];
        _refreshButton.notificationName = @"RefreshChart";
        
        [self addSubview:_refreshButton];   
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(refreshChartDataClick_Handler:)
                                                     name:@"RefreshChart"
                                                   object:_refreshButton
         ];
    }
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation interfaceOrientationWasLoaded:(UIInterfaceOrientation)interfaceOrientation {

}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [_loadingView setHidden:YES];
}

- (void)renderControl {
    self.backgroundColor = [self getBackgroundColor];
    [self drawGraphTitle];
    
    [self renderControlInternal];
     
    [self showLoadingView];
}

- (CPTTheme *)getCPTheme {
    UIColor* backgroundColor = [self getBackgroundColor];
    
    if ([backgroundColor isEqual:[UIColor whiteColor]])
        return [CPTTheme themeNamed:kCPTPlainWhiteTheme];
    
    if ([backgroundColor isEqual:[UIColor blackColor]])
        return [CPTTheme themeNamed:kCPTDarkGradientTheme];
    
    return [CPTTheme themeNamed:kCPTSlateTheme];
}

- (void)drawGraphTitle {
    if (!_titleLabel) {
        _titleLabel = [ [UILabel alloc ] initWithFrame:CGRectMake(0, 0, self.frame.size.width, 33.0) ];
        _titleLabel.adjustsFontSizeToFitWidth = YES;
        _titleLabel.textAlignment = NSTextAlignmentCenter;
        _titleLabel.textColor = [UIColor grayColor];
        _titleLabel.text = [_dataSource chartsViewTitle:self];
        _titleLabel.backgroundColor = [UIColor clearColor];
        _titleLabel.font = [UIFont fontWithName:@"Helvetica-Bold" size:(20.0)];
        // _titleLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth;
        [self addSubview:_titleLabel]; 
    }
    else {
        [_titleLabel setFrame:CGRectMake(0, 0, self.frame.size.width, 33.0)];
        _titleLabel.textAlignment = NSTextAlignmentCenter;
    }
}

#pragma mark - Plot Data Source Methods

- (NSUInteger)numberOfRecordsForPlot:(CPTPlot *)plot {
    return [_chartDataSDT count];
}

- (NSNumber *)numberForPlot:(CPTPlot *)plot
                      field:(NSUInteger)fieldEnum
                recordIndex:(NSUInteger)index
{
    GXTHROW_ABSTRACT_METHOD_EXCEPTION();
}

#pragma mark - Main Handlers

-(void)refreshChartDataClick_Handler: (NSNotification *) notification {
    _zoomView.zoomMode = MaxTime;
    [_zoomView refreshZoomView];
    [_dataSource chartsViewRefresh:self];
}

#pragma mark - Internal (abstract methods)

- (void)refreshChartInternal {
    GXTHROW_ABSTRACT_METHOD_EXCEPTION();
}

- (BOOL)isEmptyChart {
    GXTHROW_ABSTRACT_METHOD_EXCEPTION();
}

- (void)drawChartUserControl {
    GXTHROW_ABSTRACT_METHOD_EXCEPTION();
}

- (CPTLayer *)dataLabelForPlot:(CPTPlot *)plot recordIndex:(NSUInteger)index {
    GXTHROW_ABSTRACT_METHOD_EXCEPTION();
}

- (void)renderControlInternal {
    GXTHROW_ABSTRACT_METHOD_EXCEPTION();
}

- (CGRect)hostingViewFrameForControlBounds:(CGRect)bounds {
    GXTHROW_ABSTRACT_METHOD_EXCEPTION();
}

- (void)addPlotToGraph {
    GXTHROW_ABSTRACT_METHOD_EXCEPTION();
}

@end

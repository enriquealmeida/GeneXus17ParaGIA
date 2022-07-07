//
//  SDChartsTimeLineView.m
//  UCChart
//
//  Created by Marcos Crispino on 12/10/12.
//
//

#import "SDChartsTimeLineView.h"
#import "SDChartsMainView+Internal.h"
#import "GXChartColorsProvider.h"
#import "GXUCDateHelper.h"

#define kReferenceLinePosition 2

@implementation SDChartsTimeLineView {
    BOOL _firstRenderControl;
    NSMutableDictionary* _chartDataSeriesMap;

    NSTimeInterval  _previousXValue;
    NSTimeInterval  _lastXValue;
    
    NSTimeInterval _minDistanceXValue;
    NSTimeInterval _MinDate;
    NSTimeInterval _firstMinDate;
    NSTimeInterval _MaxDate;
    double _yMaxValue;
    double _yMinValue;
    
    GXUIImageButton *_legendsButton;
    UIView *_legendsView;
}

@synthesize seriesTitlesCollection = _seriesTitlesCollection;

#pragma mark - Init & dealloc

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(hostingViewTapHandler:)
                                                     name:@"GraphHostingViewClick"
                                                   object:_hostingView
         ];
        
        _firstRenderControl = YES;
        _chartDataSeriesMap = [[NSMutableDictionary alloc] init];
        _previousXValue = 0;
        _lastXValue = 0;
        _yMaxValue = 0;
        _yMinValue = 0;
        _MinDate = 0;
        _firstMinDate = 0;
        _MaxDate = 0;
        _minDistanceXValue = 0;
    }
    return self;
}

#pragma mark - Overrides

- (GXUCChartType)chartType {
    return GXUCChartTypeTimeLine;
}

#pragma mark - Internal

- (void)refreshChartInternal {
    [self loadChartDataSeriesMap];
    [self loadSeriesTitleFromSDT];
}

- (BOOL)isEmptyChart {
    BOOL isempty = YES;
    NSUInteger i = 0;
    NSArray* values = [_chartDataSeriesMap allKeys];
    while ((isempty) && (i < [values count])) {
        NSString* serieName = [values objectAtIndex:i];
        NSArray* serie = [self getChartDataSerieBySerieName:serieName];
        isempty = [serie count] == 0;
        i++;
    }
    return isempty;
}

- (void)drawChartUserControl {
    if (![self isEmptyChart]) {
        // Draw Chart User Control
        [self calculateScallingValues];
        [self refreshXValueSlider];
        
        if (_drawControlsinUI) {
            [self drawXValueLabel];
            [self drawXValueSlider];
            [self drawLegendsButton];
            [self drawRefreshButton];
            [self drawGraph];
            [self drawLegendsView];
            _drawControlsinUI = NO;
        }
        else {
            [self bringSubviewToFront:_xValueSlider];
            [self bringSubviewToFront:_legendsButton];
            [self bringSubviewToFront:_refreshButton];
            [self updateAxesWithRealValues];
        }
        [self setDateControlsInitialValues];
        
        [self zommClickViewHandler:nil];
    }
    else {
        [self drawRefreshButton];
    }
    [_loadingView setHidden:YES];
}

- (CPTLayer *)dataLabelForPlot:(CPTPlot *)plot recordIndex:(NSUInteger)index {
    return nil;
}

- (void)renderControlInternal {
    if (_firstRenderControl) {
        [self setAxesWithDummyValues];
        _firstRenderControl = NO;
    }
    else {
        [self maintainAxesValues];
    }
    [self drawXValueSlider];
    [self drawChartZoomView];
}

- (CGRect)hostingViewFrameForControlBounds:(CGRect)bounds {
    return CGRectMake(0, 42, bounds.size.width, bounds.size.height - 75);
}

- (void)addPlotToGraph {
}

#pragma mark - Plot Data Source Methods

- (NSUInteger)numberOfRecordsForPlot:(CPTPlot *)plot {
    NSArray *series = [self getChartDataSerieBySerieName:[plot.identifier description]];
    return [series count];
}

- (NSNumber *)numberForPlot:(CPTPlot *)plot
                      field:(NSUInteger)fieldEnum
                recordIndex:(NSUInteger)index
{
    NSDate *refDate = [NSDate dateWithTimeIntervalSince1970:_MinDate];
    NSArray *series = [self getChartDataSerieBySerieName:[plot.identifier description]];
    
    if (fieldEnum == CPTScatterPlotFieldX) {
        //Return X Value
        NSString* datevalueString = [[series objectAtIndex:index] objectForKey:@"XValue"];
        NSDateFormatter *dateFormatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:@"yyyy-MM-dd HH:mm:ss"];
        NSDate *datevalue = [dateFormatter dateFromString:datevalueString];
        if (!datevalue) {
            dateFormatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:@"yyyy-MM-dd"];
            datevalue = [dateFormatter dateFromString:datevalueString];
        }
        
        NSTimeInterval distance = [datevalue timeIntervalSinceDate:refDate];
        return @(distance);
	}
    
    // Return Serie Value
    // Precondicion las series son de tipo GXDataTypeNumeric
    NSNumber* yValue =  [((NSDictionary*) [series objectAtIndex:index]) objectForKey:@"YValue"];
    return yValue;
}

#pragma mark - Handlers

- (void)textButtonClick_Handler:(NSNotification *)notification {
    BOOL hidden = [_legendsView isHidden];
    [_legendsView setHidden:!hidden];
}

- (void)hostingViewTapHandler:(NSNotification *)notification {
    [_legendsView setHidden:YES];
}

#pragma mark - Slider Handlers

- (void)sliderValueChanged:(UISlider *)sender {
    NSDate* firstMinDate =[NSDate dateWithTimeIntervalSince1970:_firstMinDate];
    
    NSString* strvalue = [NSString stringWithFormat:@"%.1f", [sender value]];
    NSTimeInterval slidervalue = [strvalue doubleValue];
    
    NSDate* value = [self convertSilderNumberToDate:firstMinDate value:slidervalue];
    NSDateFormatter *dateformatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:@"d MMM yyyy"];
    
    _previousXValue = _lastXValue;
    _lastXValue = slidervalue;
    
    _xValueLabel.text = [dateformatter stringFromDate:value];
}

- (void)sliderTouchUp:(UISlider *)sender {
    NSTimeInterval slidervalue = _previousXValue;
    NSDate* oldMindate = [NSDate dateWithTimeIntervalSince1970:_firstMinDate];
    NSDate* newMinDate = [self convertSilderNumberToDate:oldMindate value:slidervalue];
    
    _zoomView.zoomMode = Other;
    [_zoomView refreshZoomView];
    [_loadingView setHidden:NO];
    [self bringSubviewToFront:_loadingView];
    
    GXTimerProxy* timerProxy = [GXTimerProxy scheduledTimerProxyWithTarget:self
                                                              timeInterval:0
                                                                   repeats:NO];
    
    [(SDChartsTimeLineView *)timerProxy refreshChartViewForNewMinDate:newMinDate onlyRefreshChart:YES];
}

- (NSDate *)convertSilderNumberToDate:(NSDate* )refDate value:(NSTimeInterval)value {
    return [NSDate dateWithTimeInterval:value sinceDate:refDate];
}

#pragma mark - Private

- (void)loadChartDataSeriesMap {
    [_chartDataSeriesMap removeAllObjects];
	NSUInteger index = 0;
	for (NSDictionary *chartData in self.chartDataSDT) {
        NSString *seriesName = [chartData objectForKey:@"Name"];
        if (seriesName) {
            [_chartDataSeriesMap setObject:[NSNumber numberWithUnsignedInteger:index++]
                                    forKey:seriesName
             ];
        }
    }
}

- (void)loadSeriesTitleFromSDT {
    _seriesTitlesCollection = [[self chartDataSDT] map:^id(NSDictionary *chartDataSDTItem) {
        return [chartDataSDTItem objectForKey:@"Name"] ? : @"";
    }];
}

- (void)setAxesWithDummyValues {
    NSDate*  refDate = [[NSDate  alloc] initWithTimeIntervalSince1970:NSTimeIntervalSince1970];
    NSTimeInterval totalMonth = 4;
    NSTimeInterval xLow = 0.0f;
    NSUInteger yMin = 0;
    
    // Grid line styles
    CPTMutableLineStyle *majorGridLineStyle = [CPTMutableLineStyle lineStyle];
    majorGridLineStyle.lineWidth = 0.75;
    majorGridLineStyle.lineColor = [[CPTColor colorWithGenericGray:0.2] colorWithAlphaComponent:0.75];
    CPTMutableLineStyle *minorGridLineStyle = [CPTMutableLineStyle lineStyle];
    minorGridLineStyle.lineWidth = 0.25;
    minorGridLineStyle.lineColor = [[CPTColor whiteColor] colorWithAlphaComponent:0.1];
    
    // Axes
    CPTXYAxisSet *axisSet = (CPTXYAxisSet *)_graph.axisSet;
    _xAxis = axisSet.xAxis;
    _xAxis.majorIntervalLength = CPTDecimalFromFloat(2);
    _xAxis.orthogonalCoordinateDecimal = CPTDecimalFromFloat(yMin);
    _xAxis.minorTicksPerInterval = 0;
    _xAxis.visibleRange = [CPTPlotRange plotRangeWithLocation:CPTDecimalFromInteger(xLow) length:CPTDecimalFromInteger(totalMonth)];
    _xAxis.labelRotation = M_PI/4;
    _xAxis.majorGridLineStyle = majorGridLineStyle;
    _xAxis.minorGridLineStyle = minorGridLineStyle;
    _xAxis.labelRotation = M_PI/4;
	_xAxis.labelingPolicy = CPTAxisLabelingPolicyAutomatic;
    _xAxis.coordinate = CPTCoordinateX;
	_xAxis.axisConstraints = [CPTConstraints constraintWithUpperOffset:0];
    
    NSString *ffstr = [NSDateFormatter dateFormatFromTemplate:@"ddMMMyyyy" options:0
                                                       locale:[NSLocale currentLocale]];
    NSDateFormatter *dateFormatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:ffstr];
    CPTTimeFormatter *timeFormatter = [[CPTTimeFormatter alloc] initWithDateFormatter:dateFormatter];
    timeFormatter.referenceDate = refDate;
    _xAxis.labelFormatter = timeFormatter;
    
    _yAxis = axisSet.yAxis;
    _yAxis.majorIntervalLength =  CPTDecimalFromInteger(2);
    _yAxis.minorTicksPerInterval = 5;
    _yAxis.orthogonalCoordinateDecimal = CPTDecimalFromString(@"0");
    _yAxis.majorGridLineStyle = majorGridLineStyle;
    _yAxis.minorGridLineStyle = minorGridLineStyle;
    _yAxis.labelingPolicy=CPTAxisLabelingPolicyAutomatic;
	_yAxis.axisConstraints = [CPTConstraints constraintWithUpperOffset:0];
    _yAxis.coordinate = CPTCoordinateY;
}

- (void)maintainAxesValues {
    // Grid line styles
    CPTMutableLineStyle *majorGridLineStyle = [CPTMutableLineStyle lineStyle];
    majorGridLineStyle.lineWidth = 0.75;
    majorGridLineStyle.lineColor = [[CPTColor colorWithGenericGray:0.2] colorWithAlphaComponent:0.75];
    CPTMutableLineStyle *minorGridLineStyle = [CPTMutableLineStyle lineStyle];
    minorGridLineStyle.lineWidth = 0.25;
    minorGridLineStyle.lineColor = [[CPTColor whiteColor] colorWithAlphaComponent:0.1];
    
    // Axes
    CPTXYAxisSet *axisSet = (CPTXYAxisSet *)_graph.axisSet;
    _xAxis = axisSet.xAxis;
    _xAxis.majorGridLineStyle = majorGridLineStyle;
    _xAxis.minorGridLineStyle = minorGridLineStyle;
    
    _yAxis = axisSet.yAxis;
    _yAxis.majorGridLineStyle = majorGridLineStyle;
    _yAxis.minorGridLineStyle = minorGridLineStyle;
}

- (void)drawChartZoomView {
    if (!_zoomView) {
        _zoomView = [[GXUCZoomView alloc] initWithFrame:CGRectMake(7, 28, self.frame.size.width,50)
                                         withTimePeriod:[self.dataSource chartsViewTimePeriodCollection:self]
                                    withLabelClassTheme:self.labelThemeClass
                                andBackgroundThemeClass:self.backgroundThemeClass
                     ];
        _zoomView.userInteractionEnabled = YES;
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(zommClickViewHandler:)
                                                     name:@"ZoomViewClick"
                                                   object:_zoomView
         ];
        [self addSubview:_zoomView];
    }
    else {
        [_zoomView setFrame:CGRectMake(7, 28, self.frame.size.width,50)];
    }
}

- (NSArray *)getChartDataSerieBySerieName:(NSString*)sName {
    NSString* serieIndexStr = [_chartDataSeriesMap objectForKey:sName];
    int serieIndex = [serieIndexStr intValue];
    NSArray* serie = [(NSDictionary*)[self.chartDataSDT objectAtIndex:serieIndex]  objectForKey:@"Data"];
    return serie;
}

- (void)drawXValueLabel {
    if (!_xValueLabel) {
        UIFont *font = [UIFont fontWithName:@"Helvetica-Bold" size:(17.0)];
        _xValueLabel = [[UILabel alloc ] initWithFrame:CGRectMake(0, self.frame.size.height - 35, self.frame.size.width -20, 43.0) ];
        _xValueLabel.text = @"0";
        _xValueLabel.textColor = [self getForeColor];
        _xValueLabel.textAlignment = NSTextAlignmentCenter;
        _xValueLabel.font = font;
        
        _xValueLabel.autoresizingMask = UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleWidth;
        _xValueLabel.backgroundColor = [UIColor clearColor];
        [self addSubview:_xValueLabel];
    }
}

- (void)drawXValueSlider {
    if (!_xValueSlider) {
        CGRect sliderFrame = CGRectMake(20, self.bounds.size.height - 50, self.bounds.size.width - 90, 43);
        _xValueSlider = [[UISlider alloc ] initWithFrame:sliderFrame];
        _xValueSlider.autoresizingMask = UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin |  UIViewAutoresizingFlexibleWidth;
        [_xValueSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
        [_xValueSlider addTarget:self action:@selector(sliderTouchUp:) forControlEvents:UIControlEventTouchUpInside| UIControlEventTouchUpOutside];
        [self addSubview:_xValueSlider];
    }
}

- (void)drawLegendsButton {
    if (!_legendsButton) {
        UIImage *image =  [GXUCChartResourceLoaderHelper loadImageForBundle:@"ChartLegendsButton"];
        _legendsButton = [ [ GXUIImageButton alloc ] initWithFrame:CGRectMake(self.bounds.size.width - image.size.width -10 - image.size.width, self.bounds.size.height - image.size.height -10, image.size.width, image.size.height) ];
        _legendsButton.userInteractionEnabled = YES;
        _legendsButton.notificationName = @"TextButtonClick";
        
        [_legendsButton setImage:image];
        
        _legendsButton.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleTopMargin;
        
        [self addSubview:_legendsButton];
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(textButtonClick_Handler:)
                                                     name:@"TextButtonClick"
                                                   object:_legendsButton
         ];
    }
    else {
        [_legendsButton setHidden:NO];
    }
}

- (void)drawLegendsView {
    if (!_legendsView) {
        CGSize legendViewSize;
        legendViewSize = [self getLegendsViewSize];
        
        CGRect svFrame = CGRectMake((self.bounds.size.width - legendViewSize.width)/2,   (self.bounds.size.height - legendViewSize.height) /2, legendViewSize.width, legendViewSize.height);
        
        _legendsView = [[UIScrollView alloc] initWithFrame:svFrame];
        [_legendsView setBackgroundColor:[self getLegendsBackgroundColor]];
        [_legendsView.layer setMasksToBounds:YES];
        [_legendsView.layer setCornerRadius:9.f];
        
        _legendsView.autoresizingMask = UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleBottomMargin |UIViewAutoresizingFlexibleTopMargin;
        [_legendsView setHidden:YES];
        
        [self drawSeriesLabels];
        [self addSubview:_legendsView];
    }
}

- (CGSize)getLegendsViewSize {
    NSUInteger n = [_seriesTitlesCollection count];
    //Height
    NSString* dummyText = @"Legends";
    UIFont *font = [UIFont boldSystemFontOfSize:17.0];
	CGSize boundsSize = self.bounds.size;
    
    CGSize titlesize = [dummyText gxSizeWithFont:font];
	titlesize.width = MIN(boundsSize.width, titlesize.width);
	titlesize.height = MIN(boundsSize.height, titlesize.height);
    
    CGFloat height = n * MIN(boundsSize.width, [font lineHeight]) + (n +2)*15 + titlesize.height;
    
    // Width
    CGFloat maxwidth = titlesize.width;
    for (int i = 0; i<n; i++) {
        NSString*  serietitle = [(NSString *)[_seriesTitlesCollection objectAtIndex:i] stringByAppendingString:@"AA AA"];
        CGSize tempsize = [serietitle gxSizeWithFont:font];
        maxwidth = MAX(maxwidth, tempsize.width);
    }
	maxwidth = MIN(boundsSize.width, maxwidth);
    
    CGSize result;
    result.height = height;
    result.width = maxwidth;
    return result;
}

- (UIColor*)getLegendsBackgroundColor {
    UIColor* backgroundColor = [self getBackgroundColor];
    
    if ([backgroundColor isEqual:[UIColor whiteColor]])
        return [UIColor colorWithWhite:0.8 alpha:0.8];
    
    if ([backgroundColor isEqual:[UIColor blackColor]])
        return [UIColor colorWithWhite:0.1 alpha:0.8];
    
    return [UIColor colorWithWhite:0.7 alpha:0.8];
    
}

- (void)drawSeriesLabels {
    NSUInteger n = [_seriesTitlesCollection count];
    NSUInteger i = 0;
    NSInteger position = 0;
    NSString* serietitle;
    [self drawSerieLegendsTitle];
    for (i=0; i<n; i++) {
        serietitle = (NSString*) [_seriesTitlesCollection objectAtIndex:i];
        position = 40 + i*100;
        [self drawSerieTitle:i serieTitle:serietitle position:position];
    }
}

- (void)drawSerieLegendsTitle {
    UIColor* labelcolor = [self getForeColor];
    UILabel *serielabeltitle;
    NSString* serieTitle = @"Legends";
    UIFont *font = [UIFont boldSystemFontOfSize:(18.0)];
    CGSize size = [serieTitle gxSizeWithFont:font];
	size.width = MIN(_legendsView.bounds.size.width, size.width);
	size.height = MIN(_legendsView.bounds.size.height, size.height);
    serielabeltitle = [[UILabel alloc ] initWithFrame:CGRectMake (0,  0, size.width, size.height) ];
    serielabeltitle.textAlignment = NSTextAlignmentCenter;
    serielabeltitle.textColor = labelcolor;
    serielabeltitle.text = serieTitle;
    serielabeltitle.backgroundColor = [UIColor clearColor];
    serielabeltitle.font = font;
    serielabeltitle.autoresizingMask = UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin;
    
    [_legendsView addSubview:serielabeltitle];
}

- (void)drawSerieTitle:(NSUInteger) serieindex serieTitle:(NSString*)serieTitle position:(NSUInteger)position {
    CPTColor* cpcolor = [[GXChartColorsProvider sharedInstance] colorAtIndex:serieindex];
    CGColorRef colorref = [cpcolor cgColor];
    UIColor* labelcolor = [UIColor colorWithCGColor:colorref];
    UILabel *serielabeltitle;
    
    UIFont *font = [UIFont boldSystemFontOfSize:(17.0)];
	CGSize size = [serieTitle gxSizeWithFont:font];
	size.width = MIN(_legendsView.bounds.size.width, size.width);
	size.height = MIN(_legendsView.bounds.size.height, size.height);
    serielabeltitle = [[UILabel alloc ] initWithFrame:CGRectMake (0,  (serieindex +1) * size.height + 15, size.width, size.height)];
    serielabeltitle.textAlignment =  NSTextAlignmentCenter;
    serielabeltitle.textColor = labelcolor;
    serielabeltitle.text = serieTitle;
    serielabeltitle.backgroundColor = [UIColor clearColor];
    serielabeltitle.font = font;
    
    serielabeltitle.autoresizingMask = UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin;
    [_legendsView addSubview:serielabeltitle];
}

- (void)refreshXValueSlider {
    NSDate* MinDate = [NSDate dateWithTimeIntervalSince1970:_MinDate];
    NSDate* MaxDate = [NSDate dateWithTimeIntervalSince1970:_MaxDate];
    
    NSTimeInterval  maxvalue = [MaxDate timeIntervalSinceDate:MinDate];
    
    _xValueSlider.maximumValue = maxvalue;
    _xValueSlider.minimumValue = 0;
    _xValueSlider.value = 0;
}

- (void)setDateControlsInitialValues {
    NSDate* MinDate = [NSDate dateWithTimeIntervalSince1970:_MinDate];
    NSDateFormatter *dateformatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:@"d MMM yyyy"];
    _xValueLabel.text = [dateformatter stringFromDate:MinDate];
    _xValueSlider.value = 0;
}

- (void)zommClickViewHandler:(NSNotification *)notification {
    [_loadingView setHidden:NO];
    [self bringSubviewToFront:_loadingView];
    
    NSDate* newMinDate = nil;
    
    if (notification == nil)
        [self setChartZoomViewMode];
    
    switch (_zoomView.zoomMode) {
        case OneDay:
            newMinDate = [GXUCDateHelper getDateNDaysBeforeMaxDate:_MaxDate n:1];
            break;
        case FiveDays:
            newMinDate = [GXUCDateHelper getDateNDaysBeforeMaxDate:_MaxDate n:5];
            break;
        case OneWeek:
            newMinDate = [GXUCDateHelper getDateNDaysBeforeMaxDate:_MaxDate n:6];
            // ES NECESARIO ASI POR BUG DE CORE PLOT por eso no se pone 7
            break;
        case OneMonth:
            newMinDate = [GXUCDateHelper getDateNMonthsBeforeMaxDate:_MaxDate n:1];
            break;
        case ThreeMonths:
            newMinDate = [GXUCDateHelper getDateNMonthsBeforeMaxDate:_MaxDate n:3];
            break;
        case SixMonths:
            newMinDate = [GXUCDateHelper getDateNMonthsBeforeMaxDate:_MaxDate n:6];
            break;
        case OneYear:
            newMinDate = [GXUCDateHelper getDateNYearsBeforeMaxDate:_MaxDate n:1];
            break;
        case MaxTime:
            newMinDate = [NSDate dateWithTimeIntervalSince1970:_firstMinDate];
            break;
        default:
            newMinDate = [NSDate dateWithTimeIntervalSince1970:_firstMinDate];
            break;
    }
    
    GXTimerProxy* timerProxy = [GXTimerProxy scheduledTimerProxyWithTarget:self
                                                              timeInterval:0
                                                                   repeats:NO];
    
    [(SDChartsTimeLineView *)timerProxy refreshChartViewForNewMinDate:newMinDate onlyRefreshChart:NO];
}

- (void)setChartZoomViewMode {
    NSDate* minDateTemp = nil;
    NSTimeInterval minDateTempInterval;
    
    minDateTemp = [GXUCDateHelper getDateNYearsBeforeMaxDate:_MaxDate n:1];
    minDateTempInterval = [minDateTemp timeIntervalSince1970];
    
    if (_firstMinDate <= minDateTempInterval) {
        _zoomView.zoomMode = OneYear;
    }
    else {
        minDateTemp = [GXUCDateHelper getDateNMonthsBeforeMaxDate:_MaxDate n:6];
        minDateTempInterval = [minDateTemp timeIntervalSince1970];
        if (_firstMinDate <= minDateTempInterval) {
            _zoomView.zoomMode = SixMonths;
        }
        else {
            minDateTemp = [GXUCDateHelper getDateNMonthsBeforeMaxDate:_MaxDate n:3];
            minDateTempInterval = [minDateTemp timeIntervalSince1970];
            if (_firstMinDate <= minDateTempInterval)
                _zoomView.zoomMode = ThreeMonths;
            else
                
            {
                minDateTemp = [GXUCDateHelper getDateNMonthsBeforeMaxDate:_MaxDate n:1];
                minDateTempInterval = [minDateTemp timeIntervalSince1970];
                if (_firstMinDate <= minDateTempInterval)
                    _zoomView.zoomMode = OneMonth;
                else
                    
                {
                    
                    minDateTemp = [GXUCDateHelper getDateNDaysBeforeMaxDate:_MaxDate n:7];
                    NSTimeInterval minDateTempInterval = [minDateTemp timeIntervalSince1970];
                    
                    if (_firstMinDate <= minDateTempInterval)
                        _zoomView.zoomMode = OneWeek;
                    else
                        _zoomView.zoomMode = MaxTime;
                }
            }
        }
    }
    
    [_zoomView refreshZoomView];
}

- (void)refreshChartViewForNewMinDate:(NSDate*) newMinDate onlyRefreshChart:(BOOL) onlyChart {
    [self refreshChartForNewMinDate:newMinDate];
    if (!onlyChart) {
        [self refreshXValueSliderAndXLabelForNewMinDate:newMinDate];
    }
    
    [_loadingView setHidden:YES];
    [_xValueSlider setHidden:NO];
    [_refreshButton setHidden:NO];
    [_legendsButton setHidden:NO];
    
}

- (void)refreshChartForNewMinDate:(NSDate*) newMinDate {
    NSTimeInterval  newMinDateNumber = [newMinDate timeIntervalSince1970];
    _MinDate = newMinDateNumber;
    [self drawGraph];
    [_graph reloadData];
}

- (void)refreshXValueSliderAndXLabelForNewMinDate:(NSDate*)newMinDate {
    NSDate* oldMindate = [NSDate dateWithTimeIntervalSince1970:_firstMinDate];
    NSTimeInterval newSliderValue = [newMinDate timeIntervalSinceDate:oldMindate];
    _xValueSlider.value = newSliderValue;
    
    NSDateFormatter *dateformatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:@"d MMM yyyy"];
    _xValueLabel.text = [dateformatter stringFromDate:newMinDate];
}

- (void)drawGraph {
    CPTXYPlotSpace *plotSpace = (CPTXYPlotSpace *)_graph.defaultPlotSpace;
    
    [self setPlotSpace:plotSpace];
    [self updateAxesWithRealValues];
    
    NSInteger seriesCount = [self.chartDataSDT count];
    NSUInteger i =0;
    NSString* serieName;
    NSArray* serieData;
    NSString* serieNameString;
    for (i=0;i<seriesCount; i++) {
        serieName = [((NSDictionary*)[self.chartDataSDT objectAtIndex:i]) objectForKey:@"Name"];
        serieData =  [((NSDictionary*)[self.chartDataSDT objectAtIndex:i]) objectForKey:@"Data"];
        serieNameString = [NSString stringWithString:serieName];
        
        if ([serieData count] > 0)
            [self setSerieField:plotSpace
                     yAxisLabel:serieNameString
                     yAxisColor:[[GXChartColorsProvider sharedInstance] colorAtIndex:i]
             yAxisGradientColor:[[GXChartColorsProvider sharedInstance] gradientColorAtIndex:i]
             ];
    }
}

- (void)setPlotSpace:(CPTXYPlotSpace *)pspace {
    double yMin = [self getYMin];
    double yMax = [self getYMax];
    
    NSTimeInterval totalMonth =  [self getTotalXAxisValue];
    NSTimeInterval xLow = 0.0f;
    
    pspace.allowsUserInteraction = YES;
    pspace.xRange = [CPTPlotRange plotRangeWithLocation:CPTDecimalFromFloat(xLow) length:CPTDecimalFromFloat(totalMonth)];
    pspace.yRange = [CPTPlotRange plotRangeWithLocation:CPTDecimalFromDouble(yMin) length:CPTDecimalFromDouble(yMax)];
}

- (void)setSerieField:(CPTXYPlotSpace *) pspace
           yAxisLabel:(NSString *) yAxisIdentifier
           yAxisColor:(CPTColor *) yAxiscolor
   yAxisGradientColor:(CPTColor *) yAxisGradientColor
{
	// Create plot area
    CPTScatterPlot *boundLinePlot = [[CPTScatterPlot alloc] init];
    boundLinePlot.identifier = yAxisIdentifier;
    CPTMutableLineStyle *dataLineStyle = [CPTMutableLineStyle lineStyle];
    dataLineStyle.miterLimit = 1.0f;
    dataLineStyle.lineWidth = 3.0f;
    dataLineStyle.lineColor = yAxiscolor;
    boundLinePlot.dataLineStyle = dataLineStyle;
    boundLinePlot.dataSource = self;
    [_graph addPlot:boundLinePlot toPlotSpace:pspace];
    
    CPTColor *areaColor1 = yAxisGradientColor;
    CPTGradient *areaGradient1 = [CPTGradient gradientWithBeginningColor:areaColor1 endingColor:[CPTColor clearColor]];
    areaGradient1.angle = -90.0f;
    CPTFill *areaGradientFill = [CPTFill fillWithGradient:areaGradient1];
    boundLinePlot.areaFill = areaGradientFill;
    boundLinePlot.areaBaseValue = [[NSDecimalNumber zero] decimalValue];
}

- (double)getYMin {
    if (_yMinValue == 0) {
        [self calculateScallingValues];
    }
    return _yMinValue;
}

- (double)getYMax {
    if (_yMaxValue == 0) {
        [self calculateScallingValues];
    }
    return _yMaxValue - _yMinValue;
}

- (void)updateAxesWithRealValues {
    NSDate* refDate =  [NSDate dateWithTimeIntervalSince1970:_MinDate];
    NSTimeInterval totalMonth = [self getTotalXAxisValue];
    NSTimeInterval xLow = 0.0f;
    
    // Grid line styles
    CPTMutableLineStyle *majorGridLineStyle = [CPTMutableLineStyle lineStyle];
    majorGridLineStyle.lineWidth = 0.75;
    majorGridLineStyle.lineColor = [[CPTColor colorWithGenericGray:0.2] colorWithAlphaComponent:0.75];
    CPTMutableLineStyle *minorGridLineStyle = [CPTMutableLineStyle lineStyle];
    minorGridLineStyle.lineWidth = 0.25;
    minorGridLineStyle.lineColor = [[CPTColor whiteColor] colorWithAlphaComponent:0.1];
    
    // Axes
    CPTXYAxisSet *axisSet = (CPTXYAxisSet *)_graph.axisSet;
    _xAxis = axisSet.xAxis;
    _xAxis.majorIntervalLength =  CPTDecimalFromFloat([self getMinXAxisDistance]);
    _xAxis.minorTicksPerInterval = 0;
    _xAxis.visibleRange = [CPTPlotRange plotRangeWithLocation:CPTDecimalFromInteger(xLow) length:CPTDecimalFromInteger(totalMonth)];
    
    NSDate* maxDayMinusOneMonthDate = [GXUCDateHelper getDateNMonthsBeforeMaxDate:_MaxDate n:1];
    NSTimeInterval maxDayMinusOneMonthTimeInterval = [maxDayMinusOneMonthDate timeIntervalSince1970];
    
    NSDate* maxDayMinusOneDayDate = [GXUCDateHelper getDateNDaysBeforeMaxDate:_MaxDate n:1];
    NSTimeInterval maxDayMinusOneDayTimeInterval = [maxDayMinusOneDayDate timeIntervalSince1970];
    
    NSString *ffstr = nil;
    _xAxis.labelingPolicy = CPTAxisLabelingPolicyAutomatic;
    if (_MinDate >= maxDayMinusOneMonthTimeInterval) {
        if (_MinDate == maxDayMinusOneDayTimeInterval) {
            ffstr = [NSDateFormatter dateFormatFromTemplate:@"dd" options:0 locale:[NSLocale currentLocale]];
            _xAxis.labelingPolicy = CPTAxisLabelingPolicyNone;
        }
        else {
            ffstr = [NSDateFormatter dateFormatFromTemplate:@"ddMMM" options:0 locale:[NSLocale currentLocale]];
        }
    }
    else {
        ffstr = [NSDateFormatter dateFormatFromTemplate:@"MMMyyyy" options:0 locale:[NSLocale currentLocale]];
    }
    
    NSDateFormatter *dateFormatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:ffstr];
    CPTTimeFormatter *timeFormatter = [[CPTTimeFormatter alloc] initWithDateFormatter:dateFormatter];
    timeFormatter.referenceDate = refDate;
    _xAxis.labelFormatter = timeFormatter;
    
    if  ([self.dataSource chartsViewXAxisPossition:self] == Bottom) {
        // Eje de las X abajo (normal)
        _xAxis.orthogonalCoordinateDecimal = CPTDecimalFromString(@"0");
        _xAxis.labelOffset =-80;
    }
    
    _yAxis = axisSet.yAxis;
    _yAxis.majorIntervalLength =  CPTDecimalFromInteger([self getYMajorIntervalLength]);
    _yAxis.minorTicksPerInterval = 5;
    
    if ([self.dataSource chartsViewYAxisPossition:self] == Left) {
        double ymax = [self getYMax];
        NSString* ymaxString = [NSString stringWithFormat:@"%.1f", ymax];
        
        NSInteger l = [ymaxString length];
        l = l -1; // le saco el punto de la coma;
        double offset = 12 * l * (-1);
        
        // Eje de las Y a la izquierda (lo normal en las graficas)
        _yAxis.labelOffset = offset;
        _yAxis.orthogonalCoordinateDecimal = CPTDecimalFromString(@"100");
    }
    
    [_graph setNeedsDisplay];
}

- (NSTimeInterval)getTotalXAxisValue {
    if (_MaxDate == 0) {
        [self calculateScallingValues];
    }
    
    return _MaxDate - _MinDate;
}

- (NSTimeInterval)getMinXAxisDistance {
    return _minDistanceXValue;
}

- (NSUInteger)getYMajorIntervalLength {
    return 10;
}

- (void)calculateScallingValues {
    NSTimeInterval maxDateforSerie = 0;
    NSTimeInterval maxDate = 0;
    NSTimeInterval minDate = 0;
    NSTimeInterval date1 = 0;
    NSTimeInterval date2 = 0;
    _minDistanceXValue = 0;
    NSTimeInterval distance = 0;
    double yResultMax = 0;
    double yResultMin = 0;
    NSDate* date1Date;
    NSDate* date2Date;
    
    NSUInteger seriesCount = [self.chartDataSDT count];
    NSUInteger realSeriesCount = 0;
    double recordCountMix = 0;
    double recordCountSumTotal = 0;
    
    // NSTimeInterval maxDateforSerie = 0;
    int first_serie_withdata = -1;
    for (int i = 0; i < seriesCount; i++) {
        NSArray* serieData = [[self.chartDataSDT objectAtIndex:i] objectForKey:@"Data"];
        NSUInteger n = [serieData count];
        
        if ((n > 0) && (first_serie_withdata == -1))
        {
            first_serie_withdata = i;
        }
        
        if (n > 0)
            realSeriesCount = realSeriesCount +1;
        
        
        recordCountSumTotal = recordCountSumTotal + n;
        maxDateforSerie = 0;
        for (int j=0; j < n ; j++)
        {
            // XValue
            NSString *datevalueString = [[serieData objectAtIndex:j] objectForKey:@"XValue"];
            NSDateFormatter *dateFormatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:@"yyyy-MM-dd HH:mm:ss"];
            
            NSDate *datevalue = [dateFormatter dateFromString:datevalueString];
            if (!datevalue) {
                dateFormatter = [NSDateFormatter dateFormatterWithTimeZone_UTC_Locale_en_US_POSIX_forDateFormat:@"yyyy-MM-dd"];
                datevalue = [dateFormatter dateFromString:datevalueString];
            }
            
            NSTimeInterval valueinterval = [datevalue timeIntervalSince1970];
            if ((i >first_serie_withdata) || ((i ==first_serie_withdata) && (j >0))) {
                if (valueinterval > maxDateforSerie) {
                    maxDateforSerie = valueinterval;
                }
                
                if (valueinterval < minDate) {
                    minDate = valueinterval;
                }
            }
            else {
                maxDateforSerie = valueinterval;
                minDate = valueinterval;
            }
            
            //Copio Dates
            date1 = date2;
            date2 = [datevalue timeIntervalSince1970];
            
            if ((i >first_serie_withdata) || ((i==first_serie_withdata) && (j > 0)) ) {
                date2Date = [NSDate dateWithTimeIntervalSince1970:date2];
                date1Date = [NSDate dateWithTimeIntervalSince1970:date1];
                distance = [date2Date timeIntervalSinceDate:date1Date];
                
                _minDistanceXValue = _minDistanceXValue + distance;
            }
            
            //YValue
            NSNumber *yValueAux =  [((NSDictionary*) [serieData objectAtIndex:j]) objectForKey:@"YValue"];
            if ((i >first_serie_withdata) || ((i ==first_serie_withdata) && (j >0))) {
                if ([yValueAux doubleValue] > yResultMax)
                    yResultMax = [yValueAux doubleValue];
                
                if ([yValueAux doubleValue] < yResultMin)
                    yResultMin = [yValueAux doubleValue];
                
            }
            else {
                yResultMax = [yValueAux doubleValue];
                yResultMin = [yValueAux doubleValue];
            }
        }
        
        if (i == first_serie_withdata || maxDateforSerie < maxDate) {
            maxDate = maxDateforSerie;
        }
    }
    
    recordCountMix = recordCountSumTotal / realSeriesCount;
    _minDistanceXValue = _minDistanceXValue / (recordCountMix -1);
    _MinDate = minDate;
    _firstMinDate = _MinDate;
    _MaxDate = maxDate;
    _yMaxValue = round(yResultMax) + 2;
    _yMinValue = floor(yResultMin) - 2;
    if (_yMinValue < 0) {
        _yMinValue = 0;  // Se fuerza a cero por Bug de Coreplot
    }
}

@end

//
//  SDChartsPieView.m
//  UCChart
//
//  Created by Marcos Crispino on 12/10/12.
//
//

#import "SDChartsPieView.h"
#import "SDChartsMainView+Internal.h"
#import "GXChartColorsProvider.h"
#import "GXPieChartLegendsView.h"

@implementation SDChartsPieView {
    NSMutableDictionary *_colorPalettePieDictionary; // necesario para que el refresh mantenga los colores
    double _totalValueForPieChart;
    GXPieChartLegendsView *_legendsView;
}

@synthesize pieProperties = _pieProperties;
@synthesize pieDataSource;

#pragma mark - Init & dealloc

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        _colorPalettePieDictionary = [[NSMutableDictionary alloc] init];
    }
    return self;
}

#pragma mark - Overrides

- (GXUCChartType)chartType {
    return GXUCChartTypePie;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation interfaceOrientationWasLoaded:(UIInterfaceOrientation)interfaceOrientationWasLoaded {
}

#pragma mark - Internal

- (void)refreshChartInternal {
}

- (BOOL)isEmptyChart {
    return [self.pieDataSource pieChartNumberOfSlices:self] == 0;
}

- (void)drawChartUserControl {
    _totalValueForPieChart = [self.pieDataSource pieChartTotal:self];
    
    if (![self isEmptyChart]) {
        // Draw Chart User Control
        if (_drawControlsinUI) {
            [self drawRefreshButton];
            _drawControlsinUI = NO;
        }
        [self drawPieLegendsView];
        [self drawCategoryValueLabel];
        
        [_graph reloadData];
        [_graph setNeedsDisplay];
    }
    else {
        [self drawRefreshButton];
    }
    
    if (_hostingView) {
        [self bringSubviewToFront:_hostingView];
        [self bringSubviewToFront:_legendsView];
    }
    
    if ([self.dataSource chartsViewDataSourceType:self] == GXUCChartDatasourceSDT) {
        [_refreshButton setHidden:YES];
    }
}

- (CPTLayer *)dataLabelForPlot:(CPTPlot *)plot recordIndex:(NSUInteger)index {
    CPTTextLayer *label;
    double value;
    NSString* categoryvalue;
    NSString* strvalue;
    NSString* showvalue;
    GXPieChartPoint* p = [self.pieDataSource pieChart:self pointAtIndex:index];
    
    value = [p.valueSerieAttribute doubleValue];
    categoryvalue = p.valueXAttribute;
    if ([self.dataSource chartsViewShowInPercentaje:self]) {
        double percentage = (value  * 100)/_totalValueForPieChart;
        strvalue = [NSString stringWithFormat:@"%.1f", percentage];
        showvalue = [strvalue stringByAppendingString:@"%"];
    }
    else {
        NSUInteger decimals = self.pieProperties.numberOfDecimals;
        NSString* decimalsstr = [NSString stringWithFormat:@"%lu", (unsigned long)decimals];
        NSString* decimalformat =  [[@"%." stringByAppendingString:decimalsstr] stringByAppendingString:@"f"];
        showvalue = [NSString stringWithFormat:decimalformat, value];
    }
    
    [self loadAllColorsForPiePaletteDictionary];
    CPTMutableTextStyle *textStyle = [CPTMutableTextStyle textStyle];
    textStyle.color = [_colorPalettePieDictionary objectForKey:categoryvalue];
    label = [[CPTTextLayer alloc] initWithText:[NSString stringWithFormat:@"%@", showvalue] style:textStyle];
    return label;
}

- (void)renderControlInternal {
    CGRect frame;
    if (_hostingView) {
        CGRect hostingViewFrame = [self hostingViewFrameForControlBounds:self.bounds];
        [_hostingView setFrame:hostingViewFrame];
        frame = [_hostingView frame];
    }
    else {
        frame = self.frame;
    }
    
    CGFloat width = frame.size.width;
    CGFloat height = frame.size.height;

    CPTPieChart* piePlot = (CPTPieChart*) [_graph plotAtIndex:0];
    piePlot.pieRadius = sqrt((width * width) + (height * height)) * 0.20;
}

- (CGRect)hostingViewFrameForControlBounds:(CGRect)bounds {
    return CGRectMake(0, 35, bounds.size.width, bounds.size.height - 65);
}

- (void)addPlotToGraph {
    CPTPieChart *pieChart = [[CPTPieChart alloc] init];
    pieChart.dataSource = self;
    pieChart.delegate = self;
    pieChart.identifier = @"PieChart1";
    pieChart.startAngle = M_PI_4;
    pieChart.sliceDirection = CPTPieDirectionCounterClockwise;
    pieChart.centerAnchor = CGPointMake(0.5, 0.6);
    [_graph addPlot:pieChart];
}

#pragma mark - CPTPlotDataSource

- (NSNumber *)numberForPlot:(CPTPlot *)plot
                      field:(NSUInteger)fieldEnum
                recordIndex:(NSUInteger)index
{
    GXPieChartPoint *p = [self.pieDataSource pieChart:self pointAtIndex:index];
    return p.valueSerieAttribute;
}

#pragma mark - CPTPieChartDelegate

- (void)pieChart:(CPTPieChart *)plot sliceWasSelectedAtRecordIndex:(NSUInteger)index {
    double value;
    NSString* categoryvalue;
    NSString* strvalue;
    NSString* showvalue;
    
    GXPieChartPoint* p = [self.pieDataSource pieChart:self pointAtIndex:index];
    value = [p.valueSerieAttribute doubleValue];
    categoryvalue = p.valueXAttribute;
    double percentage = value/_totalValueForPieChart * 100;
    
    NSUInteger decimals = self.pieProperties.numberOfDecimals;
    NSString* decimalsstr = [NSString stringWithFormat:@"%lu", (unsigned long)decimals];
    NSString* decimalformat =  [[@"%." stringByAppendingString:decimalsstr] stringByAppendingString:@"f"];
    strvalue = [NSString stringWithFormat:decimalformat, value];
    
    NSString* percentageString = [NSString stringWithFormat:@"%.1f", percentage];
    showvalue = [[[[[categoryvalue stringByAppendingString:@"  "] stringByAppendingString:strvalue]  stringByAppendingString: @" ("] stringByAppendingString: percentageString] stringByAppendingString: @"%) "];
    
    if (self.pieProperties.existCommentAttribute) {
        NSString* commentsString = p.valueCommentsAttribute;
        if (commentsString != nil) {
            showvalue = [[showvalue stringByAppendingString:@"  "] stringByAppendingString:commentsString];
        }
    }
    
    CPTColor* cpColor = [_colorPalettePieDictionary objectForKey:categoryvalue];
    CGColorRef colorref = [cpColor cgColor];
    UIColor* labelcolor = [UIColor colorWithCGColor:colorref];
    
    _xValueLabel.text = showvalue;
    _xValueLabel.textColor = labelcolor;
    [_legendsView setHidden:NO];
    [self bringSubviewToFront: _legendsView];
}

#pragma mark - CPTPieChartDataSource

- (CPTFill *)sliceFillForPieChart:(CPTPieChart *)pieChart recordIndex:(NSUInteger)index {
    GXPieChartPoint *p = [self.pieDataSource pieChart:self pointAtIndex:index];
    CPTColor *c =  [_colorPalettePieDictionary objectForKey:p.valueXAttribute];
    return [CPTFill fillWithColor:c];
}

#pragma mark - Private

- (void)drawPieLegendsView {
    CGRect svFrame = CGRectMake(20, self.frame.size.height - (self.frame.size.height/4) - 10, self.frame.size.width -20, (self.frame.size.height/4) - 35 );
	
	NSUInteger count = [self.pieDataSource pieChartNumberOfSlices:self];

	if (!_legendsView || [_legendsView legends].count != count) {
        [self loadAllColorsForPiePaletteDictionary];
		
        NSMutableArray *legends = [NSMutableArray arrayWithCapacity:count];
        NSMutableArray *colors  = [NSMutableArray arrayWithCapacity:count];
        
        GXPieChartPoint* p;
        for (NSUInteger i = 0; i < count; i++) {
            p = [self.pieDataSource pieChart:self pointAtIndex:i];
            
            [legends addObject:p.valueXAttribute];
            
            CPTColor* cpcolor = [_colorPalettePieDictionary objectForKey:p.valueXAttribute];
            [colors addObject:[UIColor colorWithCGColor:[cpcolor cgColor]]];
        }
        
        _legendsView = [[GXPieChartLegendsView alloc] initWithFrame:svFrame];
		[_legendsView setLegends:legends andColors:colors];
        [_legendsView setBackgroundColor:[UIColor clearColor]];
        [_legendsView setClipsToBounds:YES];
        _legendsView.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleTopMargin;
        [self addSubview:_legendsView];
    }
    else {
        [_legendsView setFrame:svFrame];
        [_legendsView setHidden:NO];
    }
}

- (void)drawCategoryValueLabel {
    if (!_xValueLabel) {
        UIImage *image = [UIImage imageNamed:@"ChartRefresh.png"];
        UIFont *font = [UIFont fontWithName:@"Helvetica-Bold" size:(17.0)];
        _xValueLabel = [[UILabel alloc ] initWithFrame:CGRectMake(0, self.bounds.size.height - 35, self.bounds.size.width - 2*image.size.width -15 -30, 43.0) ];
        _xValueLabel.text = @"Touch slices for more info";
        _xValueLabel.textColor = [self getForeColor];
        _xValueLabel.textAlignment = NSTextAlignmentCenter;
        _xValueLabel.font = font;
        _xValueLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleTopMargin;
        _xValueLabel.backgroundColor = [UIColor clearColor];
        [self addSubview:_xValueLabel];
    }
    else {
        _xValueLabel.text = @"Touch slices for more info";
        _xValueLabel.textColor = [self getForeColor];
    }
}

- (void)loadAllColorsForPiePaletteDictionary {
	NSArray* colorPiePalette = [self loadAllColorsForPiePalette];
	[self loadColorPalettePieDictionaryInOrder:colorPiePalette];
}

- (NSArray *)loadAllColorsForPiePalette {
    NSInteger n = [self.pieDataSource pieChartNumberOfSlices:self];
    
    NSMutableArray *colorPalette = [NSMutableArray arrayWithCapacity:n];
    
    for (int j= 0; j < 10; j++) {
        CPTColor* c = [[GXChartColorsProvider sharedInstance] colorAtIndex:j];
        [colorPalette addObject:c];
    }
    
    if (n > 10) {
        n = n - 10;
        NSInteger i = 0;
        BOOL finished = NO;
        NSUInteger notaddedcount = 0;
        NSUInteger addedcount = 0;
        while ((addedcount < n) && (!finished)) {
            CPTColor* cp = [CPTPieChart defaultPieSliceColorForIndex:i];
            BOOL exist = [self existColorinPalette:colorPalette color:cp];
            if (!exist) {
                [colorPalette addObject:cp];
                notaddedcount = 0;
                addedcount ++;
            }
            else {
                notaddedcount ++;
            }
            i++;
            
            if( notaddedcount > 20) // Cota arbitraria maxima de veces
                finished = true; // En teoria nunca deberia pasar
        }
    }
    return colorPalette;
}

- (void)loadColorPalettePieDictionaryInOrder:(NSArray*)colorPiePalette; {
	NSArray* categoryValues = [self.pieDataSource pieChartSortedCategoryNamesByValue:self];
	NSUInteger start = 0;
	if (_colorPalettePieDictionary.count > 0) {
		NSMutableArray *tmpCategoryValues = [categoryValues mutableCopy];
		[tmpCategoryValues removeObjectsInArray:[_colorPalettePieDictionary allKeys]];
		categoryValues = [tmpCategoryValues copy];
		
		start = _colorPalettePieDictionary.count;
	}
	
    for (int i = 0; i < [categoryValues count]; i++) {
        NSString* catValue = [categoryValues objectAtIndex:i];
        CPTColor* colorValue = [colorPiePalette objectAtIndex:(start+i)%[colorPiePalette count]];
        [_colorPalettePieDictionary setObject:colorValue forKey:catValue];
    }
}

- (BOOL)existColorinPalette:(NSArray*)palette color:(CPTColor*)color {
    BOOL founded = NO;
    NSUInteger i = 0;
    NSUInteger n = [palette count];
    while ((i< n) && (!founded))  {
        CPTColor* cp = [palette objectAtIndex:i];
        founded = [cp isEqual:color];
        i++;
    }
    
    return founded;
}

@end

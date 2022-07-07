//
//  SDChartsMainView+Internal.h
//  UCChart
//
//  Created by Marcos Crispino on 12/10/12.
//
//

#import <GXUCCharts/SDChartsMainView.h>

@interface SDChartsMainView (Internal)

#pragma mark - Abstract

- (void)refreshChartInternal;

- (BOOL)isEmptyChart;

- (void)drawChartUserControl;

- (CPTLayer *)dataLabelForPlot:(CPTPlot *)plot recordIndex:(NSUInteger)index;

- (void)renderControlInternal;

- (CGRect)hostingViewFrameForControlBounds:(CGRect)bounds;

- (void)addPlotToGraph;

#pragma mark - Protected methods

- (void)drawRefreshButton;

- (UIColor *)getForeColor;

- (UIColor *)getBackgroundColor;

@end

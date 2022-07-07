//
//  SDChartsMainView.h
//  UCChart
//
//  Created by Pablo Musso on 10/19/11.
//  Copyright 2011 Artech. All rights reserved.
//

@import UIKit;
@import GXObjectsModel;
#import <GXUCCharts/GXGraphHostingview.h>
#import <GXUCCharts/GXUCLoadingView.h>
#import <GXUCCharts/GXUIImageButton.h>
#import <GXUCCharts/GXUCZoomView.h>
#import <GXUCCharts/GXUCChartResourceLoaderHelper.h>
#import <GXUCCharts/GXPieChartPoint.h>
#import <GXUCCharts/GXPieChartProperties.h>
#import "CorePlot-CocoaTouch.h"


typedef NS_ENUM(uint_least8_t, GXUCChartDatasourceType) {
    GXUCChartDatasourceAttributeList,
    GXUCChartDatasourceSDT
};

typedef NS_ENUM(uint_least8_t, GXUCChartType) {
    GXUCChartTypeTimeLine,
    GXUCChartTypePie,
    GXUCChartTypeBar
};

typedef NS_ENUM(uint_least8_t, AxisPosition) {Left, Right, Top, Bottom, None};

@class SDChartsMainView;

#pragma mark -

@protocol SDChartsViewDelegate <NSObject>


@end

#pragma mark -

@protocol SDChartsViewDataSource <NSObject>

- (void)chartsViewRefresh:(SDChartsMainView *)chartView;

- (NSString *)chartsViewTitle:(SDChartsMainView *)chartView;

- (BOOL)chartsViewShowInPercentaje:(SDChartsMainView *)chartView;

- (GXUCChartDatasourceType)chartsViewDataSourceType:(SDChartsMainView *)chartView;

- (AxisPosition)chartsViewXAxisPossition:(SDChartsMainView *)chartView;

- (AxisPosition)chartsViewYAxisPossition:(SDChartsMainView *)chartView;

- (NSArray *)chartsViewTimePeriodCollection:(SDChartsMainView *)chartView;

@end

#pragma mark -

@interface SDChartsMainView : UIView <CPTPlotDataSource,CPTPlotSpaceDelegate, CPTPieChartDelegate> {
@protected
    BOOL _drawControlsinUI;

    GXGraphHostingview *_hostingView;
    GXUCZoomView *_zoomView;
    CPTXYGraph *_graph;
    GXUCLoadingView *_loadingView;
    UISlider *_xValueSlider;
    GXUIImageButton *_refreshButton;
    UILabel *_xValueLabel;

    CPTXYAxis *_xAxis;
    CPTXYAxis *_yAxis;

    UILabel *_titleLabel;
}

@property (nonatomic, weak) id<SDChartsViewDataSource> dataSource;
@property (nonatomic, weak) id<SDChartsViewDelegate> delegate;

@property (nonatomic, assign, readonly) GXUCChartType chartType;

@property (nonatomic, strong) GXThemeClass *backgroundThemeClass;
@property (nonatomic, strong) GXThemeClass *labelThemeClass;
@property (nonatomic, strong) NSArray *chartDataSDT;

- (void)refreshChart;
- (void)renderControl;
- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation interfaceOrientationWasLoaded:(UIInterfaceOrientation) interfaceOrientationWasLoaded;
- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation;

@end

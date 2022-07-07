//
//  GXUC_SDCharts.h
//  UCChart
//
//  Created by Pablo Musso on 10/19/11.
//  Copyright 2011 Artech. All rights reserved.
//

@import GXObjectsModel;
@import GXCoreUI;
#import <GXUCCharts/GXPieChartPoint.h>
#import <GXUCCharts/GXPieChartPoint.h>
#import <GXUCCharts/SDChartsMainView.h>
#import <GXUCCharts/SDChartsPieView.h>
#import <math.h>

@interface GXUC_SDCharts : GXControlGridBase <SDChartsViewDelegate, SDChartsViewDataSource, SDChartsPieViewDataSource>
{
    NSArray* _chartDataSDT;
    NSArray*  _seriesAttributeCollection;   
    NSArray*  _seriesTitlesCollection;
    NSString* _xAttribute;
    NSString* _commentsAttribute;
    NSString* _graphTitle;
    NSString* _xFieldSpecifier;
    NSString* _commentsFieldSpecifier;
    NSArray* _seriesFieldSpecifierCollection;

    BOOL _showInPercentage;
    GXUCChartDatasourceType _datasourceType;
    NSString* _sdtAttributeOrVariableName;
    GXThemeClass *_labelThemeClass;
    GXThemeClass* _backgroundThemeClass;
    AxisPosition _xAxisPosition;
    AxisPosition _yAxisPosition;
    NSArray* _timePeriodCollection;
    NSTimeInterval  _previousXValue;
    NSTimeInterval  _lastXValue;
    UIInterfaceOrientation _interfaceOrientationWasLoaded;
}

@property (nonatomic, strong)  NSArray *seriesAttributeCollection; 
@property (nonatomic, strong)  NSArray *seriesTitlesCollection;  
@property (nonatomic, strong)  NSString *sdtAttributeOrVariableName;
@property (nonatomic, strong)  GXThemeClass *labelThemeClass; 
@property (nonatomic, strong)  GXThemeClass *backgroundThemeClass; 

@end

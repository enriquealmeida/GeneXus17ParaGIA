//
//  SDChartsPieView.h
//  UCChart
//
//  Created by Marcos Crispino on 12/10/12.
//
//

#import <GXUCCharts/SDChartsMainView.h>

@class SDChartsPieView;

@protocol SDChartsPieViewDataSource <NSObject>

- (NSUInteger)pieChartNumberOfSlices:(SDChartsPieView *)pieView;

- (GXPieChartPoint *)pieChart:(SDChartsPieView *)pieView pointAtIndex:(NSUInteger)index;

- (double)pieChartTotal:(SDChartsPieView *)pieView;

- (NSArray *)pieChartSortedCategoryNamesByValue:(SDChartsPieView *)pieView;

@end

#pragma mark -

@interface SDChartsPieView : SDChartsMainView <CPTPieChartDelegate, CPTPieChartDataSource>

@property (nonatomic, weak) id<SDChartsPieViewDataSource> pieDataSource;
@property (nonatomic, strong) GXPieChartProperties *pieProperties;

@end

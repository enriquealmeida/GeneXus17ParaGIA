//
//  GXPieChartLegendsView.h
//  UCChart
//
//  Created by Marcos Crispino on 12/11/12.
//
//

@import UIKit;
#import "CorePlot-CocoaTouch.h"

@interface GXPieChartLegendsView : UIScrollView

@property (nonatomic, strong, readonly) NSArray<NSString *> *legends;
@property (nonatomic, strong, readonly) NSArray<UIColor *> *colors;

- (void)setLegends:(NSArray<NSString *> *)legends andColors:(NSArray<UIColor *> *)colors;

@end

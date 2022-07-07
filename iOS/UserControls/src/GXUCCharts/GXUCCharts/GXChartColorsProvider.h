//
//  GXChartColorsProvider.h
//  UCChart
//
//  Created by Marcos Crispino on 12/11/12.
//
//

@import Foundation;
#import "CorePlot-CocoaTouch.h"

@interface GXChartColorsProvider : NSObject

+ (instancetype)sharedInstance;

- (CPTColor *)colorAtIndex:(NSUInteger)index;

- (CPTColor *)gradientColorAtIndex:(NSUInteger)index;

@end

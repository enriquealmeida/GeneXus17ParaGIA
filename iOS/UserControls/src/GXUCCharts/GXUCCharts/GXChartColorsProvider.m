//
//  GXChartColorsProvider.m
//  UCChart
//
//  Created by Marcos Crispino on 12/11/12.
//
//

#import "GXChartColorsProvider.h"

@implementation GXChartColorsProvider {
    NSArray *_colorPalette;
    NSArray *_gradientColorPalette;
}

#pragma mark - Singleton

static GXChartColorsProvider *_instance;

+ (instancetype)sharedInstance {
    @synchronized([GXChartColorsProvider class]) {
        if (!_instance) {
            _instance = [[self alloc] init];
        }
    }
    return _instance;
}

#pragma mark - Init & dealloc

- (id)init {
    self = [super init];
    if (self) {
        _colorPalette = [[NSArray alloc] initWithObjects:
                         [CPTColor blueColor],
                         [CPTColor redColor],
                         [CPTColor greenColor],
                         [CPTColor yellowColor],
                         [CPTColor orangeColor],
                         [CPTColor cyanColor],
                         [CPTColor brownColor],
                         [CPTColor purpleColor],
                         [CPTColor whiteColor],
                         [CPTColor magentaColor],
                         nil];
        
        _gradientColorPalette = [[NSArray alloc] initWithObjects:
                                 [CPTColor colorWithComponentRed:0.3 green:0.3 blue:1.0 alpha:0.8],
                                 [CPTColor colorWithComponentRed:1.0 green:0.3 blue:0.3 alpha:0.8],
                                 [CPTColor colorWithComponentRed:0.3 green:1.0 blue:0.3 alpha:0.8],
                                 [CPTColor colorWithComponentRed:0.3 green:0.3 blue:1.0 alpha:0.8],
                                 [CPTColor colorWithComponentRed:0.3 green:0.3 blue:1.0 alpha:0.8],
                                 [CPTColor colorWithComponentRed:0.3 green:0.3 blue:1.0 alpha:0.8],
                                 [CPTColor colorWithComponentRed:0.3 green:0.3 blue:1.0 alpha:0.8],
                                 [CPTColor colorWithComponentRed:0.3 green:0.3 blue:1.0 alpha:0.8],
                                 [CPTColor colorWithComponentRed:0.3 green:0.3 blue:1.0 alpha:0.8],
                                 [CPTColor colorWithComponentRed:0.3 green:0.3 blue:1.0 alpha:0.8],
                                 nil];
    }
    return self;
}

#pragma mark - Public methods

- (CPTColor *)colorAtIndex:(NSUInteger)index {
    return [self colorFromArray:_colorPalette atIndex:index];
}

- (CPTColor*)gradientColorAtIndex:(NSUInteger)index {
    return [self colorFromArray:_gradientColorPalette atIndex:index];
}

# pragma mark - Private

- (CPTColor *)colorFromArray:(NSArray *)colorArray atIndex:(NSUInteger)index {
    NSUInteger count = [colorArray count];
    if (count > 0) {
        index = index % count;
        return [colorArray objectAtIndex:index];
    }
    return nil;
}

@end

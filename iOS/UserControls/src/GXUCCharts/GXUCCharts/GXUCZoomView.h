//
//  GXUC_ZoomView.h
//  UCChart
//
//  Created by admin on 7/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

@import UIKit;
@import GXObjectsModel;
#import <GXUCCharts/GXUCLinkView.h>

typedef NS_ENUM(uint_least8_t, ZoomMode) {OneDay, FiveDays, OneWeek, OneMonth, ThreeMonths, SixMonths, OneYear, MaxTime, Other};
@interface GXUCZoomView : UIView {

    GXUCLinkView* _zoom;
    GXUCLinkView* _oneWeek;
    GXUCLinkView* _oneDay;
    GXUCLinkView* _fiveDays;
    GXUCLinkView* _oneMonth;
    GXUCLinkView* _threeMonths;
    GXUCLinkView* _sixMonths;
    GXUCLinkView* _oneYear;
    GXUCLinkView* _maxTime;
    ZoomMode _mode;
    ZoomMode _oldMode;
    GXThemeClass* _labelThemeClass;
    NSArray *_timePeriodCollection;
}

@property (nonatomic)  ZoomMode zoomMode; 
@property (nonatomic, strong)  GXThemeClass *labelThemeClass;
@property (nonatomic, strong)  GXThemeClass *backgroundThemeClass;
@property (nonatomic, strong)  NSArray *timePeriodCollection; 
-(void) refreshZoomView;

+(CGFloat) getMaxHeight: (CGSize) maxsize;
- (id)initWithFrame:(CGRect)frame withTimePeriod:(NSArray*)timePeriod withLabelClassTheme:(GXThemeClass*)lThemeClass andBackgroundThemeClass:(GXThemeClass*) bThemeClass;

@end

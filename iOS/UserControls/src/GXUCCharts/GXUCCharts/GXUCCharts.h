//
//  GXUCCharts.h
//  GXUCCharts
//
//  Created by Fabian Inthamoussu on 31/5/16.
//  Copyright © 2016 GeneXus. All rights reserved.
//

#import <Foundation/Foundation.h>
//! Project version number for GXUCCharts.
FOUNDATION_EXPORT double GXUCChartsVersionNumber;

//! Project version string for GXUCCharts.
FOUNDATION_EXPORT const unsigned char GXUCChartsVersionString[];

// In this header, you should import all the public headers of your framework using statements like #import <GXUCCharts/PublicHeader.h>


#if TARGET_OS_IOS || TARGET_OS_TV
#import <UIKit/UIKit.h>
#elif TARGET_OS_WATCH
#import <WatchKit/WatchKit.h>
#endif

//
//  GXUCLinearGauge.h
//  GXUCLinearGauge
//
//  Created by Fabian Inthamoussu on 31/5/16.
//  Copyright © 2016 GeneXus. All rights reserved.
//

#import <Foundation/Foundation.h>
//! Project version number for GXUCLinearGauge.
FOUNDATION_EXPORT double GXUCLinearGaugeVersionNumber;

//! Project version string for GXUCLinearGauge.
FOUNDATION_EXPORT const unsigned char GXUCLinearGaugeVersionString[];

// In this header, you should import all the public headers of your framework using statements like #import <GXUCLinearGauge/PublicHeader.h>


#if TARGET_OS_IOS || TARGET_OS_TV
#import <UIKit/UIKit.h>
#elif TARGET_OS_WATCH
#import <WatchKit/WatchKit.h>
#endif

//
//  GXUCRelativeTimer.h
//  GXUCRelativeTimer
//
//  Created by José Echagüe on 8/3/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

#import <Foundation/Foundation.h>
//! Project version number for GXUCRelativeTimer.
FOUNDATION_EXPORT double GXUCRelativeTimerVersionNumber;

//! Project version string for GXUCRelativeTimer.
FOUNDATION_EXPORT const unsigned char GXUCRelativeTimerVersionString[];

// In this header, you should import all the public headers of your framework using statements like #import <GXUCRelativeTimer/PublicHeader.h>


#if TARGET_OS_IOS || TARGET_OS_TV
#import <UIKit/UIKit.h>
#elif TARGET_OS_WATCH
#import <WatchKit/WatchKit.h>
#endif

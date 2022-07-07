//
//  GXUCMaps.h
//  GXUCMaps
//
//  Created by José Echagüe on 3/22/18.
//  Copyright © 2018 genexus. All rights reserved.
//

#import <Foundation/Foundation.h>
//! Project version number for GXUCMaps.
FOUNDATION_EXPORT double GXUCMapsVersionNumber;

//! Project version string for GXUCMaps.
FOUNDATION_EXPORT const unsigned char GXUCMapsVersionString[];

// In this header, you should import all the public headers of your framework using statements like #import <GXUCMaps/PublicHeader.h>


#if TARGET_OS_IOS || TARGET_OS_TV
#import <UIKit/UIKit.h>
#elif TARGET_OS_WATCH
#import <WatchKit/WatchKit.h>
#endif

#if TARGET_OS_IOS || TARGET_OS_TV
#import <GXUCMaps/GXUC_MapList.h>
#import <GXUCMaps/GXUC_MapList+GXHelpers.h>
#import <GXUCMaps/GXUC_MapList+Internal.h>
#import <GXUCMaps/GXUC_MapList+GXProperties.h>
#endif // TARGET_OS_IOS || TARGET_OS_TV

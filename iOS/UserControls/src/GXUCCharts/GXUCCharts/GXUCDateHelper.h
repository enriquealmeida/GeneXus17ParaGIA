//
//  GXUC_DateHelper.h
//  UCChart
//
//  Created by admin on 7/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

@import Foundation;
#import <GXUCCharts/GXUCDateHelper.h>

@interface GXUCDateHelper : NSObject {
    
}

+(NSDate*) getDateNYearsBeforeMaxDate:(NSTimeInterval) date n:(NSUInteger)n;
+(NSDate*) getDateNMonthsBeforeMaxDate:(NSTimeInterval) date n:(NSUInteger)n;
+(NSDate*) getDateNDaysBeforeMaxDate:(NSTimeInterval) date n:(NSUInteger)n;
+(NSDate*) getDateNDaysAfterMaxDate:(NSTimeInterval) date n:(NSUInteger)n;
@end

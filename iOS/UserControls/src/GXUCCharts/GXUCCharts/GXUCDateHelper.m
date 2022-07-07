//
//  GXUC_DateHelper.m
//  UCChart
//
//  Created by admin on 7/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GXUCDateHelper.h"

typedef NS_ENUM(uint_least8_t, TimeComponent) {Year, Month, Day};

@implementation GXUCDateHelper


+(NSDate*) getDateNYearsBeforeMaxDate:(NSTimeInterval) date n:(NSUInteger)n
{
    return [self getDateNBeforeMaxDate:date n:n timeComponent:Year];
}

+(NSDate*) getDateNMonthsBeforeMaxDate:(NSTimeInterval) date n:(NSUInteger)n
{
    return [self getDateNBeforeMaxDate:date n:n timeComponent:Month];  
}

+(NSDate*) getDateNDaysBeforeMaxDate:(NSTimeInterval) date n:(NSUInteger)n
{
      return [self getDateNBeforeMaxDate:date n:n timeComponent:Day];
}

+(NSDate*) getDateNBeforeMaxDate:(NSTimeInterval) date n:(NSUInteger)n timeComponent:(TimeComponent)timeComponent
{
    return [GXUCDateHelper getDateNBeforeOrAfterMaxDate:date n:n timeComponent:timeComponent isNBefore:YES];
 
}

+(NSDate*) getDateNAfterMaxDate:(NSTimeInterval) date n:(NSUInteger)n timeComponent:(TimeComponent)timeComponent
{
    return [GXUCDateHelper getDateNBeforeOrAfterMaxDate:date n:n timeComponent:timeComponent isNBefore:NO];
}


+(NSDate*) getDateNBeforeOrAfterMaxDate:(NSTimeInterval) date n:(NSUInteger)n timeComponent:(TimeComponent)timeComponent isNBefore:(BOOL)isNBefore;
{
    
    NSInteger factor = 0;
    
    if (isNBefore)
        factor = -1;
     else
        factor = 1;
            
    
    NSCalendar *currentCalendar = [NSCalendar currentCalendar];
    NSDate* maxDate = [NSDate dateWithTimeIntervalSince1970:date];
    NSDateComponents* datecomponents = [[NSDateComponents alloc] init];

    [datecomponents setCalendar:currentCalendar];
    
    switch (timeComponent) {
        case Year:
            [datecomponents setYear:n*factor];
            break;
            
            
        case Month:
            [datecomponents setMonth:n*factor];
            break;
            
        case Day:
            [datecomponents setDay:n*factor];
            break;
        default:
            break;
    }
    
    
    NSDate* result = [currentCalendar dateByAddingComponents:datecomponents toDate:maxDate options:0];
    
    return result;
}


+(NSDate*) getDateNDaysAfterMaxDate:(NSTimeInterval) date n:(NSUInteger)n
{
    return [self getDateNAfterMaxDate:date n:n timeComponent:Day];
}

@end

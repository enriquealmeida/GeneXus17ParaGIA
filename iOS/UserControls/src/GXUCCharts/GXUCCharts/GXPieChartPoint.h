//
//  GXPieChartSpecification.h
//  UCChart
//
//  Created by admin on 12/1/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

@import Foundation;

@interface GXPieChartPoint : NSObject
{
    id _valXAttribute;
    id _valSerieAttribute;
    id _valCommentsAttribute;
}

@property (nonatomic, strong)  id valueXAttribute; 
@property (nonatomic, strong)  id valueSerieAttribute; 
@property (nonatomic, strong)  id valueCommentsAttribute; 

@end

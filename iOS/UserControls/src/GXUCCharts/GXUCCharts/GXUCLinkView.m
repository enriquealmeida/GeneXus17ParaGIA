//
//  GXUCLinkView.m
//  UCChart
//
//  Created by admin on 7/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GXUCLinkView.h"



@implementation GXUCLinkView


@synthesize notificationName = _notificationName;

- (id)initWithNotificationName:(NSString*)notifName
{
    
    self = [super init];
    if (self) {
        _notificationName = notifName;
    }
    return self;
    
}

// ESETA CLASE ES AL PEDO HAY QUE HACER TODO EN EL ZOOM

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    
    [[NSNotificationCenter defaultCenter]
     postNotificationName:self.notificationName
     object:self];
     
     
}



@end

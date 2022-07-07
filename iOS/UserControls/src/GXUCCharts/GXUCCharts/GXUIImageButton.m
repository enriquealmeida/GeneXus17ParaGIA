//
//  GXUITextButton.m
//  GXFlexibleClient
//
//  Created by Pablo Musso on 5/5/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXUIImageButton.h"



@implementation GXUIImageButton
@synthesize notificationName;

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    [[NSNotificationCenter defaultCenter]
     postNotificationName:notificationName
     object:self];
}



@end

//
//  GXUCLinkView.h
//  UCChart
//
//  Created by admin on 7/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

@import UIKit;

@interface GXUCLinkView : UILabel {
 
    NSString*  _notificationName;
}

- (id)initWithNotificationName:(NSString*)notifName;

@property (nonatomic, strong)  NSString *notificationName; 

@end

//
//  UIGradientView.h
//  GXFlexibleClient
//
//  Created by willy on 5/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface GXGradientView : UIView {
	CGGradientRef _gradient;	
	UIColor *_gradientColor;
}

@property (nonatomic, strong) UIColor *gradientColor;

@end

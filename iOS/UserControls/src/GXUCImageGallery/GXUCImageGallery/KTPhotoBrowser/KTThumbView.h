//
//  KTThumbView.h
//  KTPhotoBrowser
//
//  Created by Kirby Turner on 2/3/10.
//  Copyright 2010 White Peak Software Inc. All rights reserved.
//

@import UIKit;

@protocol KTThumbViewTouchDelegate <NSObject>

@required
- (void)didSelectThumbAtIndex:(NSUInteger)index;

@end

#pragma mark -

@interface KTThumbView : UIButton 
{
@private
	id<KTThumbViewTouchDelegate> __weak _touchDelegate;
	UIActivityIndicatorView *_activityIndicator;
	UIView *_overlayView;
}

@property (nonatomic, weak) id<KTThumbViewTouchDelegate> touchDelegate;

- (id)initWithFrame:(CGRect)frame;
- (void)setThumbImage:(UIImage *)newImage;
- (void)setHasBorder:(BOOL)hasBorder;
- (void)prepareForReuse;
- (void)setActivityIndicatorVisible:(BOOL)visible;

@end


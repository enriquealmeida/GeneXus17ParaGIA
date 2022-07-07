//
//  KTPhotoView.h
//  Sample
//
//  Created by Kirby Turner on 2/24/10.
//  Copyright 2010 White Peak Software Inc. All rights reserved.
//

@import UIKit;

@class KTPhotoScrollViewController;
@class KTPhotoView;

@protocol KTPhotoViewTouchDelegate <NSObject>
- (void)photoViewDidReceiveTouch:(KTPhotoView *)photoView;
@end

#pragma mark -

@interface KTPhotoView : UIScrollView <UIScrollViewDelegate>
{
	UIImageView *imageView_;
	KTPhotoScrollViewController __weak *scroller_;
	NSInteger index_;
	NSObject<KTPhotoViewTouchDelegate> __weak *_touchDelegate;
}

@property (nonatomic, weak) KTPhotoScrollViewController *scroller;
@property (nonatomic, assign) NSInteger index;
@property (nonatomic, weak) NSObject<KTPhotoViewTouchDelegate> *touchDelegate;

- (void)setImage:(UIImage *)newImage;
- (void)turnOffZoom;

- (CGPoint)pointToCenterAfterRotation;
- (CGFloat)scaleToRestoreAfterRotation;
- (void)setMaxMinZoomScalesForCurrentBounds;
- (void)restoreCenterPoint:(CGPoint)oldCenter scale:(CGFloat)oldScale;


@end

@interface KTPhotoView (GXWebImage)

- (void)setImageWithURL:(NSURL *)url placeholderImage:(UIImage *)placeholder fullSize:(BOOL)full;

@end

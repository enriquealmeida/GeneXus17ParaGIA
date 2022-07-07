//
//  GXUC_ImageGalleryDetail.h
//  UCImageGallery
//
//  Created by Marcos Crispino on 02/12/11.
//  Copyright (c) 2011 Artech. All rights reserved.
//

@import UIKit;
#import <GXUCImageGallery/GXUC_ImageGalleryDataSource.h>
#import <GXUCImageGallery/KTPhotoView.h>

@class GXUC_ImageGalleryDetail;

#pragma mark -

@protocol GXUC_ImageGalleryDetailDelegate <NSObject>

- (void)gxuc_imageGalleryDetailDidTapCloseButton:(GXUC_ImageGalleryDetail *)detail;
- (void)gxuc_imageGalleryDetail:(GXUC_ImageGalleryDetail *)detail didTapImageAtIndex:(NSInteger)index;
- (void)gxuc_imageGalleryDetail:(GXUC_ImageGalleryDetail *)detail didScrollToImageAtIndex:(NSInteger)index;

- (UIColor *)gxuc_imageGalleryBackgroundColorForDetail:(GXUC_ImageGalleryDetail *)detail;

@end

#pragma mark -

@interface GXUC_ImageGalleryDetail : UIView <UIScrollViewDelegate, KTPhotoViewTouchDelegate> {
	UIScrollView *_scrollView;
	UIToolbar *_toolbar;
	UIButton *_closeButton;
	UILabel *_titleView;
	UILabel *_captionView;
	
	id<GXUC_ImageGalleryDataSource> __weak _dataSource;
	
	id<GXUC_ImageGalleryDetailDelegate> __weak _delegate;
	
	NSUInteger _currentIndex;
	
	NSMutableArray *_photoViews;
}

@property (nonatomic, weak) id<GXUC_ImageGalleryDataSource> dataSource;
@property (nonatomic, assign) NSUInteger currentIndex;
@property (nonatomic, weak) id<GXUC_ImageGalleryDetailDelegate> delegate;

- (void)scrollToCurrentIndex;

- (void)dismissDetailView;

@end

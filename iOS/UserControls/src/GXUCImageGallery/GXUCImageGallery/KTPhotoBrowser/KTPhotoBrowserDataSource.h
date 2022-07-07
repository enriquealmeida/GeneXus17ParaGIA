//
//  KTPhotoBrowserDataSource.h
//  KTPhotoBrowser
//
//  Created by Kirby Turner on 2/7/10.
//  Copyright 2010 White Peak Software Inc. All rights reserved.
//

@import Foundation;

@class KTPhotoView;
@class KTThumbView;

typedef NS_ENUM(uint_least8_t, KTPhotoBrowserThumbsViewBehavior) {
    KTPhotoBrowserThumbsViewShowScroll,
    KTPhotoBrowserThumbsViewShowDetail,
    KTPhotoBrowserThumbsViewNone
};

@protocol KTPhotoBrowserDataSource <NSObject>
@required
- (NSInteger)numberOfPhotos;

@optional

// Implement either these, for synchronous images…
- (UIImage *)photoImageAtIndex:(NSInteger)index;
- (UIImage *)thumbImageAtIndex:(NSInteger)index;

// …or these, for asynchronous images.
- (void)photoImageAtIndex:(NSInteger)index photoView:(KTPhotoView *)photoView;
- (void)thumbImageAtIndex:(NSInteger)index thumbView:(KTThumbView *)thumbView;

- (void)deleteImageAtIndex:(NSInteger)index;
- (void)exportImageAtIndex:(NSInteger)index;

- (CGSize)thumbSize;
- (NSInteger)thumbsPerRow;
- (BOOL)thumbsHaveBorder;
- (UIColor *)photoImageBackgroundColor;

- (NSString *)titleForImageAtIndex:(NSInteger)index;
- (NSString *)captionForImageAtIndex:(NSInteger)index;

- (void) presentDetailViewForPhotoAtIndex:(NSUInteger) index;

@end

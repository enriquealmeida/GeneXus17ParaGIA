//
//  KTThumbsView.h
//  Sample
//
//  Created by Kirby Turner on 3/23/10.
//  Copyright 2010 White Peak Software Inc. All rights reserved.
//

@import UIKit;

@protocol KTThumbsViewDataSource;
@class KTThumbsViewController;
@class KTThumbView;

@interface KTThumbsView : UIScrollView <UIScrollViewDelegate>
{
@private
	id <KTThumbsViewDataSource> __weak dataSource_;
	KTThumbsViewController __weak *controller_;
	BOOL thumbsHaveBorder_;
	NSInteger thumbsPerRow_;
	CGSize thumbSize_;
	
	NSMutableSet *reusableThumbViews_;
	
	// We use the following ivars to keep track of 
	// which thumbnail view indexes are visible.
	NSInteger firstVisibleIndex_;
	NSInteger lastVisibleIndex_;
	NSInteger lastItemsPerRow_;
}

@property (nonatomic, weak) id<KTThumbsViewDataSource> dataSource;
@property (nonatomic, weak) KTThumbsViewController *controller;
@property (nonatomic, assign) BOOL thumbsHaveBorder;
@property (nonatomic, assign) NSInteger thumbsPerRow;
@property (nonatomic, assign) CGSize thumbSize;

- (KTThumbView *)dequeueReusableThumbView;
- (void)reloadData;

- (void)scrollToThumbAtIndex:(NSUInteger)index animated:(BOOL)animated;

@end

@protocol KTThumbsViewDataSource <NSObject>
@required
- (NSInteger)thumbsViewNumberOfThumbs:(KTThumbsView *)thumbsView;
- (KTThumbView *)thumbsView:(KTThumbsView *)thumbsView thumbForIndex:(NSInteger)index;

@end

//
//  KTThumbsViewController.h
//  KTPhotoBrowser
//
//  Created by Kirby Turner on 2/3/10.
//  Copyright 2010 White Peak Software Inc. All rights reserved.
//

@import UIKit;
#import <GXUCImageGallery/KTPhotoBrowserDataSource.h>
#import <GXUCImageGallery/KTThumbsView.h>
#import <GXUCImageGallery/KTThumbView.h>

@class KTThumbsView;

@interface KTThumbsViewController : UIViewController <KTThumbsViewDataSource, KTThumbViewTouchDelegate>
{
@private
    id <KTPhotoBrowserDataSource> dataSource_;
    KTThumbsView *scrollView_;
    UINavigationController *__weak navController;
    KTPhotoBrowserThumbsViewBehavior behavior;
}

@property (nonatomic, strong) id <KTPhotoBrowserDataSource> dataSource;
@property (nonatomic, weak) UINavigationController *navController;
@property (nonatomic, assign) KTPhotoBrowserThumbsViewBehavior behavior;

/**
 * Re-displays the thumbnail images.
 */
- (void)reloadThumbs;

/**
 * Called before the thumbnail images are loaded and displayed.
 * Override this method to prepare. For instance, display an
 * activity indicator.
 */
- (void)willLoadThumbs;

/**
 * Called immediately after the thumbnail images are loaded and displayed.
 */
- (void)didLoadThumbs;

@end

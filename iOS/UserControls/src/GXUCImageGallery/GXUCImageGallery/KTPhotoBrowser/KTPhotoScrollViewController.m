//
//  KTPhotoScrollViewController.m
//  KTPhotoBrowser
//
//  Created by Kirby Turner on 2/4/10.
//  Copyright 2010 White Peak Software Inc. All rights reserved.
//

#import "KTPhotoScrollViewController.h"
@import GXFoundation;
@import GXObjectsModel;
@import GXCoreUI;
#import "KTPhotoBrowserDataSource.h"
#import "KTPhotoBrowserGlobal.h"
#import "KTPhotoView.h"

const CGFloat ktkDefaultPortraitToolbarHeight   = 44;
const CGFloat ktkDefaultLandscapeToolbarHeight  = 33;
const CGFloat ktkDefaultToolbarHeight = 44;

@implementation KTPhotoScrollViewController

@synthesize statusBarStyle = statusBarStyle_;
@synthesize statusbarHidden = statusbarHidden_;


- (id)initWithDataSource:(id <KTPhotoBrowserDataSource>)dataSource andStartWithPhotoAtIndex:(NSUInteger)index 
{
	if ((self = [super init])) {
		startWithIndex_ = index;
		currentIndex_ = index;
		dataSource_ = dataSource;
		self.hidesBottomBarWhenPushed = YES;
	}
	return self;
}

- (void)loadView
{
   [super loadView];

   CGRect scrollFrame = [self frameForPagingScrollView];
   UIScrollView *newView = [[UIScrollView alloc] initWithFrame:scrollFrame];
   [newView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
   [newView setDelegate:self];

   UIColor *backgroundColor = [dataSource_ respondsToSelector:@selector(photoImageBackgroundColor)] ?
                                [dataSource_ photoImageBackgroundColor] : [UIColor blackColor];  
   [newView setBackgroundColor:backgroundColor];
   [newView setAutoresizesSubviews:YES];
   [newView setPagingEnabled:YES];
   [newView setShowsVerticalScrollIndicator:NO];
   [newView setShowsHorizontalScrollIndicator:NO];

   [[self view] addSubview:newView];

   scrollView_ = newView;


   nextButton_ = [[UIBarButtonItem alloc] 
                  initWithImage:KTLoadImageFromBundle(@"nextIcon")
                  style:UIBarButtonItemStylePlain
                  target:self
                  action:@selector(nextPhoto)];

   previousButton_ = [[UIBarButtonItem alloc] 
                      initWithImage:KTLoadImageFromBundle(@"previousIcon")
                      style:UIBarButtonItemStylePlain
                      target:self
                      action:@selector(previousPhoto)];

   UIBarButtonItem *trashButton = nil;
   if ([dataSource_ respondsToSelector:@selector(deleteImageAtIndex:)]) {
     trashButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemTrash
                                                                 target:self
                                                                 action:@selector(trashPhoto)];
   }

   UIBarButtonItem *exportButton = nil;
   if ([dataSource_ respondsToSelector:@selector(exportImageAtIndex:)])
   {
      exportButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAction 
                                                                   target:self
                                                                   action:@selector(exportPhoto)];
   }


   UIBarItem *space = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                                                    target:nil 
                                                                    action:nil];

   NSMutableArray *toolbarItems = [[NSMutableArray alloc] initWithCapacity:7];

   if (exportButton) [toolbarItems addObject:exportButton];
   [toolbarItems addObject:space];
   [toolbarItems addObject:previousButton_];
   [toolbarItems addObject:space];
   [toolbarItems addObject:nextButton_];
   [toolbarItems addObject:space];
   if (trashButton) [toolbarItems addObject:trashButton];

   CGRect screenFrame = UIScreen.mainScreen.bounds;
   CGRect toolbarFrame = CGRectMake(0, 
                                    screenFrame.size.height - ktkDefaultToolbarHeight, 
                                    screenFrame.size.width, 
                                    ktkDefaultToolbarHeight);
   toolbar_ = [[UIToolbar alloc] initWithFrame:toolbarFrame];
   [toolbar_ setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleRightMargin];
   [toolbar_ setBarStyle:UIBarStyleBlackTranslucent];
   [toolbar_ setItems:toolbarItems];
   [[self view] addSubview:toolbar_];


    if ([dataSource_ respondsToSelector:@selector(captionForImageAtIndex:)]) {

        CGRect screenFrame = self.view.frame;
        CGRect captionViewFrame = CGRectMake(0,
                                             screenFrame.size.height - toolbar_.frame.size.height,
                                             screenFrame.size.width,
                                             41);

        captionView_ = [[UILabel alloc] initWithFrame:captionViewFrame];
        [captionView_ setBackgroundColor:[UIColor colorWithRed:0.0f green:0.0f blue:0.0f alpha:0.5f]];
        [captionView_ setTextColor:[UIColor whiteColor]];
        [captionView_ setTextAlignment:NSTextAlignmentCenter];
        [captionView_ setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleTopMargin ];
        [captionView_ setNumberOfLines:2];

        [self.view addSubview:captionView_];
    }
}

- (void)setTitleWithCurrentPhotoIndex 
{
	NSString *formatString = [GXExecutionEnvironmentHelper isLayoutDirectionLeftToRight] ? @"%1$i / %2$i" : @"%2$i / %1$i";

    NSString *title = nil;
    if ([dataSource_ respondsToSelector:@selector(titleForImageAtIndex:)]) {
        title = [dataSource_ titleForImageAtIndex:currentIndex_];
    }

    title = title ? : [NSString stringWithFormat:formatString, currentIndex_ + 1, photoCount_, nil];

    [[self navigationItem] setTitle:title];
}

- (void) setCaptionWithCurrentPhotoIndex {
    if ([dataSource_ respondsToSelector:@selector(captionForImageAtIndex:)]) {
        NSString *caption = [dataSource_ captionForImageAtIndex:currentIndex_];
		[captionView_ setText:caption];
		CGRect frame;
		frame.origin.x = 0;
		frame.size.width = self.view.bounds.size.width;
		frame.size.height = [captionView_ sizeThatFits:captionView_.bounds.size].height + 20; // padding
		frame.origin.y = toolbar_.frame.origin.y - frame.size.height;
		[captionView_ setFrame:frame];
    }
}

- (void)scrollToIndex:(NSInteger)index 
{
   CGRect frame = scrollView_.frame;
   frame.origin.x = frame.size.width * index;
   frame.origin.y = 0;
   [scrollView_ scrollRectToVisible:frame animated:NO];
}

- (void)setScrollViewContentSize
{
   NSInteger pageCount = photoCount_;
   if (pageCount == 0) {
      pageCount = 1;
   }

   CGSize size = CGSizeMake(scrollView_.frame.size.width * pageCount, 
                            scrollView_.frame.size.height / 2);   // Cut in half to prevent horizontal scrolling.
   [scrollView_ setContentSize:size];
}

- (void)viewDidLoad 
{
   [super viewDidLoad];

   photoCount_ = [dataSource_ numberOfPhotos];
   [self setScrollViewContentSize];

   // Setup our photo view cache. We only keep 3 views in
   // memory. NSNull is used as a placeholder for the other
   // elements in the view cache array.
   photoViews_ = [[NSMutableArray alloc] initWithCapacity:photoCount_];
   for (int i=0; i < photoCount_; i++) {
      [photoViews_ addObject:[NSNull null]];
   }
}

- (void)didReceiveMemoryWarning 
{
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];

	// Release any cached data, images, etc that aren't in use.
}

- (void)viewWillAppear:(BOOL)animated 
{
    [super viewWillAppear:animated];

    // Set the scroll view's content size, auto-scroll to the stating photo,
    // and setup the other display elements.
    [self setScrollViewContentSize];
    [self setCurrentIndex:currentIndex_];
    [self scrollToIndex:currentIndex_];

    [self setTitleWithCurrentPhotoIndex];
    [self toggleNavButtons];
    [self startChromeDisplayTimer];
}

- (void)viewDidDisappear:(BOOL)animated 
{
   [self cancelChromeDisplayTimer];
   [super viewDidDisappear:animated];
}

- (void)deleteCurrentPhoto 
{
   if (dataSource_) {
      // TODO: Animate the deletion of the current photo.

      NSInteger photoIndexToDelete = currentIndex_;
      [self unloadPhoto:photoIndexToDelete];
      [dataSource_ deleteImageAtIndex:photoIndexToDelete];

      photoCount_ -= 1;
      if (photoCount_ == 0) {
         [self showChrome];
         [[self navigationController] popViewControllerAnimated:YES];
      } else {
         NSInteger nextIndex = photoIndexToDelete;
         if (nextIndex == photoCount_) {
            nextIndex -= 1;
         }
         [self setCurrentIndex:nextIndex];
         [self setScrollViewContentSize];
      }
   }
}

- (void)toggleNavButtons 
{
   [previousButton_ setEnabled:(currentIndex_ > 0)];
   [nextButton_ setEnabled:(currentIndex_ < photoCount_ - 1)];
}

- (void) presentDetailViewForPhotoAtIndex:(NSUInteger)index {
    [self showChrome];
    [dataSource_ presentDetailViewForPhotoAtIndex:index];
}

#pragma mark -
#pragma mark Frame calculations
#define PADDING  20

- (CGRect)frameForPagingScrollView 
{
   CGRect frame = UIScreen.mainScreen.bounds;
   frame.origin.x -= PADDING;
   frame.size.width += (2 * PADDING);
   return frame;
}

- (CGRect)frameForPageAtIndex:(NSUInteger)index 
{
   // We have to use our paging scroll view's bounds, not frame, to calculate the page placement. When the device is in
   // landscape orientation, the frame will still be in portrait because the pagingScrollView is the root view controller's
   // view, so its frame is in window coordinate space, which is never rotated. Its bounds, however, will be in landscape
   // because it has a rotation transform applied.
   CGRect bounds = [scrollView_ bounds];
   CGRect pageFrame = bounds;
   pageFrame.size.width -= (2 * PADDING);
   pageFrame.origin.x = (bounds.size.width * index) + PADDING;
   return pageFrame;
}


#pragma mark -
#pragma mark Photo (Page) Management

- (void)loadPhoto:(NSInteger)index
{
   if (index < 0 || index >= photoCount_) {
      return;
   }

   id currentPhotoView = [photoViews_ objectAtIndex:index];
   if (NO == [currentPhotoView isKindOfClass:[KTPhotoView class]]) {
      // Load the photo view.
      CGRect frame = [self frameForPageAtIndex:index];
      KTPhotoView *photoView = [[KTPhotoView alloc] initWithFrame:frame];
      [photoView setScroller:self];
      [photoView setIndex:index];
      [photoView setBackgroundColor:[UIColor clearColor]];

      // Set the photo image.
      if (dataSource_) {
         if ([dataSource_ respondsToSelector:@selector(photoImageAtIndex:photoView:)] == NO) {
            UIImage *image = [dataSource_ photoImageAtIndex:index];
            [photoView setImage:image];
         } else {
            [dataSource_ photoImageAtIndex:index photoView:photoView];
         }
      }

      [scrollView_ addSubview:photoView];
      [photoViews_ replaceObjectAtIndex:index withObject:photoView];
   } else {
      // Turn off zooming.
      [currentPhotoView turnOffZoom];
   }
}

- (void)unloadPhoto:(NSInteger)index
{
   if (index < 0 || index >= photoCount_) {
      return;
   }

   id currentPhotoView = [photoViews_ objectAtIndex:index];
   if ([currentPhotoView isKindOfClass:[KTPhotoView class]]) {
      [currentPhotoView removeFromSuperview];
      [photoViews_ replaceObjectAtIndex:index withObject:[NSNull null]];
   }
}

- (void)setCurrentIndex:(NSInteger)newIndex
{
   currentIndex_ = newIndex;

   [self loadPhoto:currentIndex_];
   [self loadPhoto:currentIndex_ + 1];
   [self loadPhoto:currentIndex_ - 1];
   [self unloadPhoto:currentIndex_ + 2];
   [self unloadPhoto:currentIndex_ - 2];

   [self setTitleWithCurrentPhotoIndex];
   [self setCaptionWithCurrentPhotoIndex];
   [self toggleNavButtons];
}


#pragma mark -
#pragma mark Rotation Magic

- (void)updateToolbarWithOrientation:(UIInterfaceOrientation)interfaceOrientation 
{
   CGRect toolbarFrame = toolbar_.frame;
   if ((interfaceOrientation) == UIInterfaceOrientationPortrait || (interfaceOrientation) == UIInterfaceOrientationPortraitUpsideDown) {
      toolbarFrame.size.height = ktkDefaultPortraitToolbarHeight;
   } else {
      toolbarFrame.size.height = ktkDefaultLandscapeToolbarHeight+1;
   }

   toolbarFrame.size.width = self.view.frame.size.width;
   toolbarFrame.origin.y =  self.view.frame.size.height - toolbarFrame.size.height;
   toolbar_.frame = toolbarFrame;

    if (captionView_) {
        [self setCaptionWithCurrentPhotoIndex];
    }
}

- (void)layoutScrollViewSubviews
{
   [self setScrollViewContentSize];

   NSArray *subviews = [scrollView_ subviews];

   for (KTPhotoView *photoView in subviews) {
      CGPoint restorePoint = [photoView pointToCenterAfterRotation];
      CGFloat restoreScale = [photoView scaleToRestoreAfterRotation];
      [photoView setFrame:[self frameForPageAtIndex:[photoView index]]];
      [photoView setMaxMinZoomScalesForCurrentBounds];
      [photoView restoreCenterPoint:restorePoint scale:restoreScale];
   }

   // adjust contentOffset to preserve page location based on values collected prior to location
   CGFloat pageWidth = scrollView_.bounds.size.width;
   CGFloat newOffset = (firstVisiblePageIndexBeforeRotation_ * pageWidth) + (percentScrolledIntoFirstVisiblePage_ * pageWidth);
   scrollView_.contentOffset = CGPointMake(newOffset, 0);

}

- (BOOL)shouldAutorotate {
	return YES;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return [GXViewControllerHelper defaultSupportedInterfaceOrientations];
}

#if TARGET_OS_IOS
- (void)viewWillTransitionToSize:(CGSize)size withTransitionCoordinator:(id <UIViewControllerTransitionCoordinator>)coordinator {
	[super viewWillTransitionToSize:size withTransitionCoordinator:coordinator];
	BOOL isRotationTransition = !CGAffineTransformIsIdentity(coordinator.targetTransform);
	if (isRotationTransition) {
		UIInterfaceOrientation toInterfaceOrientation = [self gxInterfaceOrientationFromTargetTransfrom:coordinator.targetTransform];
		if (toInterfaceOrientation != UIInterfaceOrientationUnknown) {
			// here, our pagingScrollView bounds have not yet been updated for the new interface orientation. So this is a good
			// place to calculate the content offset that we will need in the new orientation
			CGFloat offset = scrollView_.contentOffset.x;
			CGFloat pageWidth = scrollView_.bounds.size.width;
			if (offset >= 0) {
				firstVisiblePageIndexBeforeRotation_ = floorf(offset / pageWidth);
				percentScrolledIntoFirstVisiblePage_ = (offset - (firstVisiblePageIndexBeforeRotation_ * pageWidth)) / pageWidth;
			} else {
				firstVisiblePageIndexBeforeRotation_ = 0;
				percentScrolledIntoFirstVisiblePage_ = offset / pageWidth;
			}
			[coordinator animateAlongsideTransition:^(id<UIViewControllerTransitionCoordinatorContext>  _Nonnull context) {
				[self layoutScrollViewSubviews];
				// Rotate the toolbar.
				[self updateToolbarWithOrientation:toInterfaceOrientation];
			} completion:^(id<UIViewControllerTransitionCoordinatorContext> context) {
				[self startChromeDisplayTimer];
			}];
		}
	}
}
#endif // TARGET_OS_IOS

#pragma mark -
#pragma mark Chrome Helpers

- (BOOL)prefersStatusBarHidden {
	return isChromeHidden_;
}

- (void)toggleChromeDisplay 
{
   [self toggleChrome:!isChromeHidden_];
}

- (void)toggleChrome:(BOOL)hide 
{
   isChromeHidden_ = hide;
   if (hide) {
      [UIView beginAnimations:nil context:nil];
      [UIView setAnimationDuration:0.4];
   }

	[self setNeedsStatusBarAppearanceUpdate];

   CGFloat alpha = hide ? 0.0 : 1.0;

   // Must set the navigation bar's alpha, otherwise the photo
   // view will be pushed until the navigation bar.
   UINavigationBar *navbar = [[self navigationController] navigationBar];
   [navbar setAlpha:alpha];

   [toolbar_ setAlpha:alpha];
    [captionView_ setAlpha:alpha];

   if (hide) {
      [UIView commitAnimations];
   }

   if ( ! isChromeHidden_ ) {
      [self startChromeDisplayTimer];
   }
}

- (void)hideChrome 
{
   if (chromeHideTimer_ && [chromeHideTimer_ isValid]) {
      [chromeHideTimer_ invalidate];
      chromeHideTimer_ = nil;
   }
   [self toggleChrome:YES];
}

- (void)showChrome 
{
   [self toggleChrome:NO];
}

- (void)startChromeDisplayTimer 
{
   [self cancelChromeDisplayTimer];
   chromeHideTimer_ = [NSTimer scheduledTimerWithTimeInterval:5.0
                                                       target:self 
                                                     selector:@selector(hideChrome)
                                                     userInfo:nil
                                                      repeats:NO];
}

- (void)cancelChromeDisplayTimer 
{
   if (chromeHideTimer_) {
      [chromeHideTimer_ invalidate];
      chromeHideTimer_ = nil;
   }
}


#pragma mark -
#pragma mark UIScrollViewDelegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView 
{
   CGFloat pageWidth = scrollView.frame.size.width;
   float fractionalPage = scrollView.contentOffset.x / pageWidth;
   NSInteger page = floor(fractionalPage);
	if (page != currentIndex_) {
		[self setCurrentIndex:page];
	}
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView 
{
   [self hideChrome];
}


#pragma mark -
#pragma mark Toolbar Actions

- (void)nextPhoto 
{
   [self scrollToIndex:currentIndex_ + 1];
   [self startChromeDisplayTimer];
}

- (void)previousPhoto 
{
   [self scrollToIndex:currentIndex_ - 1];
   [self startChromeDisplayTimer];
}

- (void)trashPhoto 
{
	NSString *cancelTitle = [GXResources translationFor:@"GXM_cancel"];
	NSString *deleteTitle = [GXResources translationFor:@"GXM_DeletePhoto"];
	KTPhotoScrollViewController __weak *unretainedSelf = self;
	UIAlertController *alertController = [UIAlertController alertControllerWithTitle:nil
																			 message:nil
																	  preferredStyle:UIAlertControllerStyleActionSheet];
	void(^actionHandler)(UIAlertAction *) = ^(UIAlertAction *action) {
		KTPhotoScrollViewController *retainedSelf = unretainedSelf;
		if (retainedSelf) {
			if (action.style == UIAlertActionStyleDestructive) {
				[retainedSelf deleteCurrentPhoto];
			}
			[retainedSelf startChromeDisplayTimer];
		}
	};
	[alertController addAction:[UIAlertAction actionWithTitle:cancelTitle style:UIAlertActionStyleCancel handler:actionHandler]];
	[alertController addAction:[UIAlertAction actionWithTitle:deleteTitle style:UIAlertActionStyleDestructive handler:actionHandler]];
	[self presentViewController:alertController animated:YES completion:NULL];
}

- (void) exportPhoto
{
   if ([dataSource_ respondsToSelector:@selector(exportImageAtIndex:)])
      [dataSource_ exportImageAtIndex:currentIndex_];

   [self startChromeDisplayTimer];
}

@end

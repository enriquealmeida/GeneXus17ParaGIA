//
//  GXUC_ImageGalleryDetail.m
//  UCImageGallery
//
//  Created by Marcos Crispino on 02/12/11.
//  Copyright (c) 2011 Artech. All rights reserved.
//

#import "GXUC_ImageGalleryDetail.h"
@import GXCoreUI;
#import "KTPhotoView.h"
#import "KTPhotoBrowserGlobal.h"

#define kCaptionPadding 10
#define kCloseButtonWidth 44
#define kCloseButtonHeight 44
#define kDefaultLineHeight 21

@implementation GXUC_ImageGalleryDetail

#pragma mark - Init & dealloc

- (id)init {
	return [self initWithFrame:CGRectZero];
}

- (id)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		_dataSource = nil;
		
		_scrollView = [[UIScrollView alloc] initWithFrame:self.bounds];
		[_scrollView setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight];
		[_scrollView setDelegate:self];
		[_scrollView setBackgroundColor:[UIColor blackColor]];
		[_scrollView setAutoresizesSubviews:YES];
		[_scrollView setPagingEnabled:YES];
		[_scrollView setShowsVerticalScrollIndicator:NO];
		[_scrollView setShowsHorizontalScrollIndicator:NO];
		[self addSubview:_scrollView];
		
		GXTheme *theme = [GXTheme currentTheme] ;
		
		_titleView = [self newLabel];
		[_titleView setAutoresizingMask:UIViewAutoresizingNone];
		[_titleView setNumberOfLines:1];
		[_titleView applyThemeClass:[theme themeClassAttributeTitle]];
		[self addSubview:_titleView];
		
		_captionView = [self newLabel];
		[_captionView setAutoresizingMask:UIViewAutoresizingNone];
		[_captionView setNumberOfLines:2];
		[_captionView applyThemeClass:[theme themeClassAttributeSubtitle]];
		[self addSubview:_captionView];
		
		_closeButton = [UIButton buttonWithType:UIButtonTypeCustom];
		[_closeButton setImage:KTLoadImageFromBundle(@"closeIcon") forState:UIControlStateNormal];
		[_closeButton addTarget:self action:@selector(closeView:) forControlEvents:UIControlEventPrimaryActionTriggered];
		[_closeButton setFrame:CGRectMake(0, 0, kCloseButtonWidth, kCloseButtonHeight)];
		[_closeButton setAutoresizingMask:UIViewAutoresizingNone];
		[self addSubview:_closeButton];
	}
	return self;
}

- (UILabel *)newLabel {
	UILabel *label = [[UILabel alloc] initWithFrame:CGRectZero];
	[label setBackgroundColor:[UIColor colorWithRed:0.0f green:0.0f blue:0.0f alpha:0.5f]];
	[label setTextColor:[UIColor whiteColor]];
	[label setTextAlignment:NSTextAlignmentCenter];
	return label;
}

#pragma mark - Properties

@synthesize dataSource = _dataSource;

- (void)setDataSource:(id<GXUC_ImageGalleryDataSource>)dataSource {
    if (_dataSource != dataSource) {
        [self unloadAllPhotos];
        _currentIndex = 0;
        _dataSource = dataSource;
        [self createPhotoViewsCache];
    }
}

@synthesize currentIndex = _currentIndex;

- (void)setCurrentIndex:(NSUInteger)index {
	_currentIndex = index;

	[self loadPhoto:index fullSize:YES];
	[self loadPhoto:index + 1 fullSize:NO];
	[self loadPhoto:index - 1 fullSize:NO];
	[self unloadPhoto:index + 2];
	[self unloadPhoto:index - 2];

	[self setTitleWithCurrentPhotoIndex];
	[self setCaptionWithCurrentPhotoIndex];
}

@synthesize delegate = _delegate;

- (void)setDelegate:(id<GXUC_ImageGalleryDetailDelegate>)delegate {
    _delegate = delegate;
    
    UIColor *bgColor = [delegate gxuc_imageGalleryBackgroundColorForDetail:self];
    [_scrollView setBackgroundColor:bgColor ? : [UIColor blackColor]];
}

#pragma mark - Public

- (void)scrollToCurrentIndex {
	[self setScrollViewContentSize];
	[self scrollToIndex:_currentIndex];
}

- (void)dismissDetailView {
    _delegate = nil;
    [self closeView:nil];
}

#pragma mark - UIView

- (void)layoutSubviews {
	[super layoutSubviews];
	BOOL isRTL = self.layoutDirectionRightToLeft;
	[_scrollView setDelegate:nil];	// don't want the rotation to change the current page
	CGRect bounds = self.bounds;
	for (int index = 0; index < [_photoViews count]; index++) {
		id item = [_photoViews objectAtIndex:index];
		if ([item isKindOfClass:[UIView class]]) {
			UIView *view = (UIView *)item;
			CGRect viewFrame = CGRectMake((isRTL ? _photoViews.count - index - 1 : index) * bounds.size.width,
										  0,
										  bounds.size.width,
										  bounds.size.height);
			[view setFrame:viewFrame];
		}
	}
	[_scrollView setDelegate:self];
	
	CGFloat halfCloseButtonWidth = CGRectGetWidth(_closeButton.bounds) * 0.5;
	_closeButton.center = CGPointMake(isRTL ? halfCloseButtonWidth : CGRectGetWidth(bounds) - halfCloseButtonWidth, _closeButton.center.y);
	[_titleView setFrame:CGRectMake(0, 0, CGRectGetWidth(bounds), kDefaultLineHeight)];
	
	CGFloat captionHeight;
	if (bounds.size.width == 0 || bounds.size.height == 0) {
		captionHeight = 0;
	}
	else {
		captionHeight = MIN(CGRectGetHeight(bounds), [_captionView sizeThatFits:bounds.size].height + kCaptionPadding * 2);
	}
	[_captionView setFrame:CGRectMake(0, CGRectGetHeight(bounds) - captionHeight, CGRectGetWidth(bounds), captionHeight)];
}

- (void)setFrame:(CGRect)frame {
	[_scrollView setDelegate:nil];	// don't want the rotation to change the current page

	[super setFrame:frame];

	if (!CGSizeEqualToSize(CGSizeZero, frame.size)) {
		[self scrollToCurrentIndex];
	}
	
	[_scrollView setDelegate:self];
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
	CGFloat pageWidth = CGRectGetWidth(scrollView.bounds);
	float fractionalPage = scrollView.contentOffset.x / pageWidth;
	NSInteger pageIndex = MAX(0, floor(fractionalPage));
	if ([scrollView isLayoutDirectionRightToLeft]) {
		NSUInteger pageCount = pageWidth == 0 ? 1 : MAX(1, scrollView.contentSize.width / pageWidth);
		pageIndex = MAX(0, pageCount - 1 - pageIndex);
	}
	if (pageIndex != _currentIndex) {
		[self setCurrentIndex:pageIndex];
		[_delegate gxuc_imageGalleryDetail:self didScrollToImageAtIndex:pageIndex];
	}
}

#pragma mark - KTPhotoViewTouchDelegate

- (void)photoViewDidReceiveTouch:(KTPhotoView *)photoView {
	[_delegate gxuc_imageGalleryDetail:self didTapImageAtIndex:photoView.index];
}

#pragma mark - Private

- (void)setScrollViewContentSize {
	NSInteger pageCount = MAX(1, [_dataSource count]);

	CGSize size = CGSizeMake(_scrollView.frame.size.width * pageCount, 
							 _scrollView.frame.size.height / 2);   // Cut in half to prevent horizontal scrolling.
	[_scrollView setContentSize:size];
}

- (void)createPhotoViewsCache {
	NSUInteger photoCount = [_dataSource count];
	_photoViews = [[NSMutableArray alloc] initWithCapacity:photoCount];
	for (int i=0; i < photoCount; i++) {
		[_photoViews addObject:[NSNull null]];
	}
}

- (void)scrollToIndex:(NSInteger)index {
	CGRect frame = _scrollView.bounds;
	CGFloat width = CGRectGetWidth(frame);
	if ([_scrollView isLayoutDirectionRightToLeft]) {
		NSUInteger pageCount = width == 0 ? 1 : MAX(1, _scrollView.contentSize.width / width);
		if (index >= pageCount) {
			frame.origin.x = 0;
		}
		else {
			frame.origin.x = frame.size.width * (pageCount - 1 - index);
		}
	}
	else {
		frame.origin.x = frame.size.width * index;
	}
	frame.origin.y = 0;
	[_scrollView scrollRectToVisible:frame animated:NO];
}

- (void)loadPhoto:(NSInteger)index fullSize:(BOOL)full {
	if (index < 0 || index >= [_dataSource count]) {
		return;
	}
	
	if (!_photoViews) {
		[self createPhotoViewsCache];
	}
	
	id currentPhotoView = [_photoViews objectAtIndex:index];
	if (![currentPhotoView isKindOfClass:[KTPhotoView class]]) {
		// Load the photo view.
		CGRect frame = [self frameForPageAtIndex:index];
		KTPhotoView *photoView = [[KTPhotoView alloc] initWithFrame:frame];
		[photoView setIndex:index];
		[photoView setBackgroundColor:[UIColor clearColor]];
		[photoView setTouchDelegate:self];
		
		// Set the photo image.
		if (index < [_dataSource count]) {
			NSURL *url = [_dataSource photoURLAtIndex:index];
			if ([url isKindOfClass:[NSNull class]]) {
				[photoView setImage:KTPhotoPlaceholderThumbnail()];
			}
			else {
				[photoView setImageWithURL:url placeholderImage:KTPhotoPlaceholderThumbnail() fullSize:full];
			}
		}
		
		[_scrollView addSubview:photoView];
		[_photoViews replaceObjectAtIndex:index withObject:photoView];
	} else {
		// Turn off zooming.
		[currentPhotoView turnOffZoom];
	}
}

- (void)unloadPhoto:(NSInteger)index {
	if (index < 0 || index >= [_dataSource count]) {
		return;
	}
	
	id currentPhotoView = [_photoViews objectAtIndex:index];
	if ([currentPhotoView isKindOfClass:[KTPhotoView class]]) {
		[currentPhotoView removeFromSuperview];
		[_photoViews replaceObjectAtIndex:index withObject:[NSNull null]];
	}
}

- (void)unloadAllPhotos {
	for (int index = 0; index < [_dataSource count]; index++) {
		[self unloadPhoto:index];
	}
}

- (CGRect)frameForPageAtIndex:(NSUInteger)index {
	CGRect bounds = [_scrollView bounds];
	CGRect pageFrame = bounds;
	if ([_scrollView isLayoutDirectionRightToLeft]) {
		NSInteger pageCount = MAX(1, [_dataSource count]);
		pageFrame.origin.x = MAX(0, bounds.size.width * (pageCount - 1 - index));
	}
	else {
		pageFrame.origin.x = bounds.size.width * index;
	}
	return pageFrame;
}

- (void)setTitleWithCurrentPhotoIndex {
	NSString *formatString = [GXExecutionEnvironmentHelper isLayoutDirectionLeftToRight] ? @"%1$i / %2$i" : @"%2$i / %1$i";
	NSString *title = [_dataSource titleAtIndex:_currentIndex];
	title = title ? : [NSString stringWithFormat:formatString, _currentIndex + 1, [_dataSource count], nil];
	[_titleView setText:title];
}

- (void)setCaptionWithCurrentPhotoIndex {
	NSString *caption = [_dataSource captionAtIndex:_currentIndex];
	if (caption) {
		[_captionView setText:caption];
		[self setNeedsLayout];
	}
}

#pragma mark - ActionHandlers

- (void)closeView:(id)sender {
	if (_delegate) {
		[_delegate gxuc_imageGalleryDetailDidTapCloseButton:self];
	}
	else {
		[self removeFromSuperview];
		[self unloadAllPhotos];
	}
}

@end

//
//  GXUC_ImageGalleryList.m
//  GXFlexibleClient
//
//  Created by Marcos Crispino on 03/05/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXUC_ImageGalleryList.h"
#import "KTPhotoBrowserDataSource.h"
#import "KTPhotoBrowserGlobal.h"
#import "KTThumbView+SDWebImage.h"
#import "KTPhotoScrollViewController.h"
#import "GXUC_ImageGalleryDetail.h"

@implementation GXUC_ImageGalleryList {
	id<GXControlCustomActionBarItem> _doneActionBarItemControl;
	id<GXControlCustomActionBarItem> _shareActionBarItemControl;
}

#pragma mark - Internal properties

- (KTThumbsView *)thumbsView {
	return (KTThumbsView *)[self gridView];
}

- (NSString *)dataAttribute {
	NSString *propVal = [[self properties] getPropertyValueString:@"@SDImageGalleryDataAtt"];

	if (!propVal) {
		id <GXEntityDataDescriptor> dataDesctriptor = [[self.entityDataListProvider entityDataSource] entityDataSourceDataDescriptor];
		for(id <GXEntityDataFieldDescriptor> fieldDescriptor in [dataDesctriptor entityDataFieldDescriptors]) {
            if ([GXEntityHelper isImageFieldInfo:[fieldDescriptor entityDataFieldInfo]]) {
                propVal = [fieldDescriptor entityDataFieldName];
                break;
            }
        }
	}
	
	return propVal;
}

- (NSString *)dataFieldSpecifier {
	return [[self properties] getPropertyValueString:@"@SDImageGalleryDataField"];
}

- (NSString *)titleAttribute {
	return [[self properties] getPropertyValueString:@"@SDImageGalleryTitleAtt"];
}

- (NSString *)titleFieldSpecifier {
	return [[self properties] getPropertyValueString:@"@SDImageGalleryTitleField"];
}

- (NSString *)subtitleAttribute {
	return [[self properties] getPropertyValueString:@"@SDImageGallerySubtitleAtt"];
}

- (NSString *)subtitleFieldSpecifier {
	return [[self properties] getPropertyValueString:@"@SDImageGallerySubtitleField"];
}

- (KTPhotoBrowserThumbsViewBehavior)gridBehavior {
	NSString *behaviorStr = [[self properties] getPropertyValueString:@"@SDImageGalleryGridBehavior"];
	KTPhotoBrowserThumbsViewBehavior behavior = KTPhotoBrowserThumbsViewShowScroll;
	if (behaviorStr) {
		if ([behaviorStr isEqualToString:@"detail"])
			behavior = KTPhotoBrowserThumbsViewShowDetail;
		else if ([behaviorStr isEqualToString:@"none"])
			behavior = KTPhotoBrowserThumbsViewNone;
	}
	return behavior;
}

- (BOOL)enableShareAction {
	return [[self properties] getPropertyValueBool:@"@SDImageGalleryEnableShare"];
}

#pragma mark - Overrides

- (id)initWithLayoutElement:(id<GXLayoutElement>)layoutElement
			 dataDescriptor:(id<GXEntityDataDescriptor>)dataDescriptor
	 businessComponentLevel:(GXBusinessComponentLevel *)businessComponentLevel
				  controlId:(NSUInteger)controlId
					 gxMode:(GXModeType)modeType
			   indexerStack:(NSArray *)indexer
			  parentControl:(id<GXControlContainer>)parentControl
				  relations:(NSDictionary *)relationsByDataElementName
{
	self = [super initWithLayoutElement:layoutElement
						 dataDescriptor:dataDescriptor
				 businessComponentLevel:businessComponentLevel
							  controlId:controlId
								 gxMode:modeType
						   indexerStack:indexer
						  parentControl:parentControl
							  relations:relationsByDataElementName
			];
	if (self) {
		_selectedImageIndexes = [[NSMutableArray alloc] init];
		_isSelecting = NO;
	}
	return self;
}

- (void)dealloc {
	if (_detail) {
		[_detail setDataSource:nil];
		[_detail setDelegate:nil];
		_detail = nil;
	}
}

- (UIView *)newGridViewWithFrame:(CGRect)frame {
	KTThumbsView *thumbs = [[KTThumbsView alloc] initWithFrame:frame];
	[thumbs setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight];
	[thumbs setScrollsToTop:YES];
	[thumbs setScrollEnabled:YES];
	[thumbs setBackgroundColor:[UIColor clearColor]];
	[thumbs setDataSource:self];
	[thumbs setTag:NSIntegerMax];
    [thumbs setThumbSize:[self thumbsSize]];
	return thumbs;
}

- (CGSize)thumbsSize {
	return CURRENT_DEVICE_IPHONE ? CGSizeMake(75, 75) : CGSizeMake(120, 120);
}

- (void)unloadView {
	if (_detail) {
		[_detail setDataSource:nil];
		[_detail setDelegate:nil];
		_detail = nil;
	}
	[super unloadView];
}

- (void)reloadData:(NSDictionary<NSString *, id> *)loadInfo {
    NSUInteger currentIndex = [_detail currentIndex];
    if (_detail) {
        NSIndexPath *detailIndexPath = [NSIndexPath indexPathForItem:currentIndex inSection:0];
        
        NSSet<NSIndexPath *> *deletedItems = loadInfo[GXEntityDataListProviderDeletedIndexesKey];
        if ([deletedItems containsObject:detailIndexPath]) {
            [_detail dismissDetailView];
            _detail = nil;
        }
    }

    [_detail setDataSource:nil];

    [self loadData:[[self entityDataListProvider] numberOfLoadedEntitiesInSection:0]];

    [_detail setDataSource:self];
    [_detail setCurrentIndex:currentIndex];
}

#pragma mark - GXControlGrid Protocol

- (void)entityViewControllerWillPresentFiltersAdvancedViewController {
    [_detail dismissDetailView];
}

#pragma mark - Private

- (void) presentDetailViewForPhotoAtIndex:(NSUInteger) index {
    // This is the method that gets called when the user double-taps in an image to display the detail.
	[self executeDefaultActionForEntityAtIndex:index section:0 fromGxControlTable:nil];
}

- (NSString *)fieldValueAtIndex:(NSInteger)index
				   forFieldName:(NSString *)fieldName
				 fieldSpecifier:(NSString *)fieldSpecifier
{
	id <GXEntityDataDescriptor> dataDesctriptor = [[self.entityDataListProvider entityDataSource] entityDataSourceDataDescriptor];
	id<GXEntityDataFieldDescriptor> fieldDescriptor = [dataDesctriptor entityDataFieldDescriptorForName:fieldName];
	
	if (fieldSpecifier == nil) {
    	id <GXEntityDataFieldInfo> entityDataFieldInfo = fieldDescriptor.entityDataFieldInfo;
		GXBasedOnType entityDataFieldBasedOnType = entityDataFieldInfo.entityDataFieldInfoBasedOnType;
    	if (entityDataFieldBasedOnType == GXBasedOnTypeSDT || entityDataFieldBasedOnType == GXBasedOnTypeBC) {
        	// field specifier is required if the field is a SDT/BC
			return nil;
		}
    }
    
	NSArray *indexer = nil;
	id fieldValue = [self valueForEntityDataFieldName:fieldName
									   fieldSpecifier:fieldSpecifier
									 forEntityAtIndex:index
											  section:0
										  enitityData:NULL
									  fieldDescriptor:&fieldDescriptor
											  indexer:&indexer
								indexedFieldSpecifier:NULL];
    
	if (fieldDescriptor) {
		if (fieldSpecifier) {
			return [GXEntityHelper displayStringFieldValue:fieldDescriptor
									 fromFieldValue:fieldValue
									 fieldSpecifier:fieldSpecifier
											indexer:indexer
					];
		}
		else {
			return [GXEntityHelper displayStringFieldValue:fieldDescriptor
									 fromFieldValue:fieldValue
					];
		}
	}
	else {
		if ([fieldValue isKindOfClass:[NSString class]]) {
			return fieldValue;
		}
		else {
			return [fieldValue description];
		}
	}
}

- (NSString *)titleForImageAtIndex:(NSInteger)index {
	return [self fieldValueAtIndex:index
					  forFieldName:[self titleAttribute]
					fieldSpecifier:[self titleFieldSpecifier]
			];
}

- (NSString *)captionForImageAtIndex:(NSInteger)index {
	return [self fieldValueAtIndex:index
					  forFieldName:[self subtitleAttribute]
					fieldSpecifier:[self subtitleFieldSpecifier]
			];
}

#pragma mark - KTThumbsViewDataSource

- (NSInteger)thumbsViewNumberOfThumbs:(KTThumbsView *)thumbsView {
	return [self numberOfPhotos];
}

- (KTThumbView *)thumbsView:(KTThumbsView *)thumbsView thumbForIndex:(NSInteger)index {
	KTThumbView *thumbView = [thumbsView dequeueReusableThumbView];
	if (!thumbView) {
        CGRect frame = CGRectZero;
        frame.size = [self thumbsSize];
		thumbView = [[KTThumbView alloc] initWithFrame:frame];
	}
	
	[thumbView setTouchDelegate:self];
	
	// Set thumbnail image asynchronously.
    if (index < [_photos count]) {
        NSURL *url = [_photos objectAtIndex:index];
        if ([url isKindOfClass:[NSNull class]]) {
            [thumbView setImage:KTPhotoPlaceholderThumbnail() forState:UIControlStateNormal];
        }
        else {
            [thumbView setImageWithURL:url placeholderImage:KTPhotoPlaceholderThumbnail()];
        }
    }
    else {
		if (_autoLoad) {
			[thumbView setActivityIndicatorVisible:YES];
			[thumbView setImage:nil forState:UIControlStateNormal];
			NSInteger totalCount = [[self entityDataListProvider] totalCount];
			switch (totalCount) {
				case kTotalCountUnknownLocal:
				case kTotalCountUnknownRemote:
				case kTotalCountUnknownPendingRemote:
					[[self entityDataListProvider] load];
					break;
				default:
					break;
			}
		}
		else {
			[thumbView setActivityIndicatorVisible:NO];
			[thumbView setImage:KTLoadImageFromBundle(@"refreshIcon") forState:UIControlStateNormal];
		}
    }
	
	BOOL isSelected = NO;
	for (NSNumber *num in _selectedImageIndexes) {
		if ([num unsignedIntValue] == index) {
			isSelected = YES;
			break;
		}
	}
	[thumbView setSelected:isSelected];
	
	return thumbView;
}

- (CGRect)detailAnimationFrameForThumb:(nullable KTThumbView *)thumb {
	if (thumb != nil) {
		return [self.view convertRect:thumb.frame fromView:thumb.superview];
	}
	else {
		if (![self isViewLoaded] || [self.view isLayoutDirectionLeftToRight]) {
			return CGRectZero;
		}
		else {
			return CGRectMake(0, CGRectGetWidth(self.view.bounds), 0, 0);
		}
	}
}

- (void)didSelectThumbAtIndex:(NSUInteger)index {
	if (index >= [_photos count]) {
		_autoLoad = YES;
		[[self entityDataListProvider] load];
		return;
	}
	
	if (_isSelecting) {
		KTThumbView *thumb = (KTThumbView *)[[self thumbsView] subviewOfClass:[KTThumbView class] withTag:index];
        if (thumb) {
            BOOL selected = ![(KTThumbView *)thumb isSelected];
			[(KTThumbView *)thumb setSelected:selected];

            if (selected) {
                [_selectedImageIndexes addObject:@(index)];
            }
            else {
                for (NSNumber *num in _selectedImageIndexes) {
                    if (index == [num unsignedIntValue]) {
                        [_selectedImageIndexes removeObject:num];
                        break;
                    }
                }
            }
			[self updateDoneCustomActionBarItemControlTitle];
        }
	}
	else {
		if ([self gridBehavior] == KTPhotoBrowserThumbsViewShowScroll) {
			
			if (!_detail) {
				KTThumbView *thumb = (KTThumbView *)[[self thumbsView] subviewOfClass:[KTThumbView class] withTag:index];
				CGRect initialFrame = [self detailAnimationFrameForThumb:thumb];
				_detail = [[GXUC_ImageGalleryDetail alloc] initWithFrame:initialFrame];
				[_detail setAutoresizingMask:UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight];
				[_detail setDataSource:self];
				[_detail setDelegate:self];
				if (thumb != nil) {
					[_detail setCurrentIndex:index];
					[_detail scrollToCurrentIndex];
					[_detail layoutIfNeeded];
				}
			}
			
			[[self view] addSubview:_detail];
			
			[UIView animateWithDuration:kUIViewAnimationDuration
								  delay:0
								options:UIViewAnimationOptionLayoutSubviews | UIViewAnimationOptionBeginFromCurrentState
							 animations:^ {
								 [self->_detail setFrame:self.view.bounds];
							 }
							 completion:^(BOOL finished) {
								 [self->_detail setCurrentIndex:index];
								 [self->_detail scrollToCurrentIndex];
								 [self reloadActionBarItems];
							 }
			 ];
		}
		else {
			[self presentDetailViewForPhotoAtIndex:index];
		}
	}
}

#pragma mark - Private

- (void)loadData:(NSInteger)count{
    // This method reads the information of the entities and extracts the images
    NSMutableArray *tmpPhotos = [[NSMutableArray alloc] initWithCapacity:count];
	
    NSString *photoAttName = [self dataAttribute];
	NSString *photoFieldSpecifier = [self dataFieldSpecifier];
	id <GXEntityDataFieldDescriptor> photoFieldDesc = nil;
    if (photoAttName){
        for(NSUInteger i = 0; i < count; i++) {
			
			NSString *value = [self valueForEntityDataFieldName:photoAttName
												 fieldSpecifier:photoFieldSpecifier
											   forEntityAtIndex:i
														section:0
													enitityData:NULL
												fieldDescriptor:&photoFieldDesc
														indexer:NULL
										  indexedFieldSpecifier:NULL
							   ];
            NSURL *url = [GXEntityHelper absoluteURLFromFieldValue:value];
            if (url) {
                [tmpPhotos addObject:url];
            }
            else {
                [tmpPhotos addObject:[NSNull null]];
            }
		}
	}

	_photos = tmpPhotos;

    
	[[self thumbsView] reloadData];
}

- (NSInteger)numberOfPhotos {
    NSInteger numberOfRows = 0;
    NSInteger totalCount = [self.entityDataListProvider totalCount];
    
    if (totalCount == kTotalCountUnknownLocal) {
        numberOfRows = 1;
    }
    else {
        numberOfRows = [self.entityDataListProvider numberOfLoadedEntitiesInSection:0];
        switch (totalCount) {
            case kTotalCountUnknownRemote:
            case kTotalCountUnknownPendingRemote:
            {
                numberOfRows++; // "Load More..." or "Loading..." Cell
                break;
            }
            default:
            {
                if (totalCount > numberOfRows)
                    numberOfRows++; // "Load More..." or "Loading..." Cell
                break;
            }
        }
    }
    
	return numberOfRows;
}

- (void)reloadActionBarItems {
	id<GXControlWithActionBarItemsDelegate> delegate = self.controlWithActionBarItemsDelegate;
	[delegate controlWithActionBarItemsNeedsReloadActions:self];
}

- (void)clearSelectedImages {
	if (_selectedImageIndexes.count > 0) {
		KTThumbsView *thumbsView = self.thumbsView;
		for (NSNumber *index in _selectedImageIndexes) {
			NSUInteger thumbIndex = [index unsignedIntValue];
			KTThumbView *thumb = [thumbsView subviewOfClass:[KTThumbView class] withTag:thumbIndex];
			[thumb setSelected:NO];
		}
		[_selectedImageIndexes removeAllObjects];
		[self updateDoneCustomActionBarItemControlTitle];
	}
}

#pragma mark - KTPhotoViewTouchDelegate

- (void)photoViewDidReceiveTouch:(KTPhotoView *)photoView {
	[self presentDetailViewForPhotoAtIndex:photoView.index];
	[photoView removeFromSuperview];
}

#pragma mark - GXUC_ImageGalleryDetailDelegate

- (void)gxuc_imageGalleryDetailDidTapCloseButton:(GXUC_ImageGalleryDetail *)detail {
	KTThumbView *thumb = (KTThumbView *)[[self thumbsView] subviewOfClass:[KTThumbView class] withTag:detail.currentIndex];
	CGRect finalFrame = [self detailAnimationFrameForThumb:thumb];
	[UIView animateWithDuration:kUIViewAnimationDuration
						  delay:0
						options:UIViewAnimationOptionLayoutSubviews | UIViewAnimationOptionBeginFromCurrentState
					 animations:^ {
						 detail.frame = finalFrame;
					 }
					 completion:^(BOOL finished) {
						 [detail removeFromSuperview];
						 if (detail == self->_detail) {
							 self->_detail = nil;
						 }
						 [self reloadActionBarItems];
					 }
	 ];
}

- (void)gxuc_imageGalleryDetail:(GXUC_ImageGalleryDetail *)detail didTapImageAtIndex:(NSInteger)index {
	[self presentDetailViewForPhotoAtIndex:index];
}

- (void)gxuc_imageGalleryDetail:(GXUC_ImageGalleryDetail *)detail didScrollToImageAtIndex:(NSInteger)index {
	[[self thumbsView] scrollToThumbAtIndex:index animated:NO];
}

- (UIColor *)gxuc_imageGalleryBackgroundColorForDetail:(GXUC_ImageGalleryDetail *)detail {
    return [[self themeClassList] backgroundColor];
}

#pragma mark - GXUC_ImageGalleryDataSource

- (NSUInteger)count {
	return [_photos count];
}

- (NSURL *)photoURLAtIndex:(NSUInteger)index {
	return [_photos objectAtIndex:index];
}

- (NSString *)titleAtIndex:(NSUInteger)index {
	return [self titleForImageAtIndex:index];
}

- (NSString *)captionAtIndex:(NSUInteger)index {
	return [self captionForImageAtIndex:index];
}

#pragma mark - GXControlWithActionBarItems

- (nullable NSArray<id<GXControlActionBarItem>> *)controlActionBarItems {
	NSArray<id<GXControlActionBarItem>> *controlActionBarItems = super.controlActionBarItems;
	id<GXControlCustomActionBarItem> currentCustomActionBarItemControl = self.currentCustomActionBarItemControl;
	if (currentCustomActionBarItemControl == nil) {
		return controlActionBarItems;
	}
	else if (controlActionBarItems.count > 0) {
		return [controlActionBarItems arrayByAddingObject:currentCustomActionBarItemControl];
	}
	else {
		return @[currentCustomActionBarItemControl];
	}
}

- (void)enumerateControlActionBarItemsWithBlock:(void (^)(id <GXControlActionBarItem> item, BOOL *stop))block
										options:(GXControlWithActionBarItemsEnumerationOptions)options {
	
	__block BOOL stop_ = NO;
	[super enumerateControlActionBarItemsWithBlock:^(id<GXControlActionBarItem> _Nonnull item, BOOL * _Nonnull stop) {
		block(item, stop);
		stop_ = *stop;
	} options:options];
	if (!stop_) {
		BOOL existingOnly = (options & GXControlWithActionBarItems_ExistingOnly) == GXControlWithActionBarItems_ExistingOnly;
		id<GXControlCustomActionBarItem> currentCustomActionBarItemControl = [self currentCustomActionBarItemControl:existingOnly];
		if (currentCustomActionBarItemControl) {
			block(currentCustomActionBarItemControl, &stop_);
		}
	}
}

#pragma mark GXControlWithActionBarItems Helper Methods

- (nullable id<GXControlCustomActionBarItem>)currentCustomActionBarItemControl {
	return [self currentCustomActionBarItemControl:NO];
}

- (nullable id<GXControlCustomActionBarItem>)currentCustomActionBarItemControl:(BOOL)existingOnly {
	if (_isSelecting) {
		return existingOnly ? _doneActionBarItemControl : self.doneCustomActionBarItemControl;
	}
	else {
		return existingOnly ? _shareActionBarItemControl : self.shareCustomActionBarItemControl;
	}
}

- (NSString *)doneCustomActionBarItemControlTitle {
	return [GXResources translationFor:_selectedImageIndexes.count == 0 ? @"GXM_cancel" : @"GXM_Done"];
}

- (void)updateDoneCustomActionBarItemControlTitle {
	if (_doneActionBarItemControl != nil && _doneActionBarItemControl.viewLoaded) {
		id<GXControlActionBarItemViewProxy> viewProxy = _doneActionBarItemControl.viewReference.viewProxy;
		if ([viewProxy respondsToSelector:@selector(setCaption:)]) {
			viewProxy.caption = [self doneCustomActionBarItemControlTitle];
		}
	}
}

- (nullable id<GXControlCustomActionBarItem>)doneCustomActionBarItemControl {
	if (_doneActionBarItemControl == nil && [self enableShareAction] && [GXSharing canShareWithImage]) {
		id<GXActionBarUIPositionCustomDescriptor> posDesc;
		posDesc = [GXControlActionBarItemHelper actionBarUIPositionDescriptorWithHorizontalAlign:GXHorizontalAlignTypeRight
																				   verticalAlign:GXVerticalAlignTypeTop];
		NSString *controlName = [self.controlName stringByAppendingString:@"_GXDoneSelectingAction"];
		typeof(self) __weak wself = self;
		_doneActionBarItemControl = [GXControlActionBarItemHelper customActionBarItemControlWithParentControl:self
																								  controlName:controlName
																						   positionDescriptor:posDesc
																						 barButtonItemBuilder:
									 ^UIBarButtonItem<GXControlActionBarItemView> * _Nonnull() {
										 typeof(wself) __strong sself = wself;
										 return [[GXControlActionBarButtonItem alloc] initWithTitle:[sself doneCustomActionBarItemControlTitle]
																							  style:UIBarButtonItemStyleDone
																							 target:sself
																							 action:@selector(shareButtonTapped:)];
									 }];
	}
	return _doneActionBarItemControl;
}

- (nullable id<GXControlCustomActionBarItem>)shareCustomActionBarItemControl {
	if (_shareActionBarItemControl == nil && [self enableShareAction] && [GXSharing canShareWithImage]) {
		id<GXActionBarUIPositionCustomDescriptor> posDesc;
		posDesc = [GXControlActionBarItemHelper actionBarUIPositionDescriptorWithHorizontalAlign:GXHorizontalAlignTypeRight
																				   verticalAlign:GXVerticalAlignTypeTop];
		NSString *controlName = [self.controlName stringByAppendingString:@"_GXShareImageAction"];
		typeof(self) __weak wself = self;
		_shareActionBarItemControl = [GXControlActionBarItemHelper customActionBarItemControlWithParentControl:self
																								   controlName:controlName
																							positionDescriptor:posDesc
																						  barButtonItemBuilder:
									  ^UIBarButtonItem<GXControlActionBarItemView> * _Nonnull() {
										  return [[GXControlActionBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAction
																											target:wself
																											action:@selector(shareButtonTapped:)];
									  }];
	}
	return _shareActionBarItemControl;
}

#pragma mark - Sharing

- (void)shareButtonTapped:(nullable id)sender {
	BOOL performShareAction = NO;
	if (_detail) {
		[_selectedImageIndexes removeAllObjects];
		[_selectedImageIndexes addObject:@([_detail currentIndex])];
		[self updateDoneCustomActionBarItemControlTitle];
		performShareAction = YES;
	}
	else {
		if (_isSelecting) {
			performShareAction = YES;
		}
		_isSelecting = !_isSelecting;
		[self reloadActionBarItems];
	}
	
	if (performShareAction && [_selectedImageIndexes count] > 0) {
		NSMutableArray *imagesToAdd = [[NSMutableArray alloc] initWithCapacity:[_selectedImageIndexes count]];
		NSMutableArray *imagesURLStringToAdd = [[NSMutableArray alloc] initWithCapacity:[_selectedImageIndexes count]];
		
		for (int index = 0; index < _selectedImageIndexes.count; index++) {
			NSUInteger photoIndex = [[_selectedImageIndexes objectAtIndex:index] unsignedIntValue];
			NSURL *url = [_photos objectAtIndex:photoIndex];
			if ([url isKindOfClass:[NSNull class]]) {
				continue;
			}
			UIImage *image = [GXImageCache.sharedImageCache imageFromDiskCacheForKey:[GXWebImageManager.sharedManager cacheKeyForURL:url]];
			if (image) {
				[imagesToAdd addObject:image];
				[imagesURLStringToAdd addObject:[url absoluteString]];
			}
		}
		
		if ([imagesToAdd count] > 0) {
			_sharing = [[GXSharing alloc] init];
			[_sharing setSharingType:GXSharingActivity | GXSharingEmail | GXSharingTwitter | GXSharingFacebook];
			NSUInteger imagesToAddIndex = 0;
			for (UIImage *image in imagesToAdd) {
				[_sharing addImage:image withURLString:[imagesURLStringToAdd objectAtIndex:imagesToAddIndex++]];
			}
			
			// Wait one run loop so the bar buttom item gets changed before presenting the share action sheet
			dispatch_async(dispatch_get_main_queue(), ^{
				UIViewController *controller = self.context.entityController.gxUserInterfaceController;
				id<GXControlCustomActionBarItem> currentCustomActionBarItemControl = self.currentCustomActionBarItemControl;
				id uiContextSender = currentCustomActionBarItemControl.viewLoaded ? currentCustomActionBarItemControl.viewReference.view : sender;
				GXUserInterfaceContext *uiContext = [GXUserInterfaceContextHelper addSender:uiContextSender
																fromUserInterfaceController:controller
																	 toUserInterfaceContext:nil];
				GXUC_ImageGalleryList __weak *unretainedSelf = self;
				BOOL handled = [self->_sharing shareFromUserInterfaceContext:uiContext
														   completionHandler:^(BOOL completed, NSError *error, NSDictionary *context) {
															   GXUC_ImageGalleryList *retainedSelf = unretainedSelf;
															   [retainedSelf clearSelectedImages];
														   }];
				if (!handled) {
					[self clearSelectedImages];
				}
			});
		}
		else {
			[self clearSelectedImages];
		}
	}
}

@end




#pragma mark - GXThemeClassWithScrollHelpers


@implementation GXUC_ImageGalleryList (GXThemeClassWithScrollHelpers)

- (GXScrollBouncingStyle)resolvedScrollBouncingStyleDefault {
	return GXScrollBouncingStyleAlwaysVertically;
}

@end

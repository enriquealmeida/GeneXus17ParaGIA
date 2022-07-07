//
//  GXUC_ImageGalleryList.h
//  GXFlexibleClient
//
//  Created by Marcos Crispino on 03/05/11.
//  Copyright 2011 Artech. All rights reserved.
//

@import GXCoreUI;
#import <GXUCImageGallery/GXUC_ImageGalleryDataSource.h>
#import <GXUCImageGallery/GXUC_ImageGalleryDetail.h>
#import <GXUCImageGallery/KTPhotoBrowserDataSource.h>
#import <GXUCImageGallery/KTPhotoView.h>
#import <GXUCImageGallery/KTThumbsViewController.h>

@interface GXUC_ImageGalleryList : GXControlGridBase <KTThumbsViewDataSource, KTThumbViewTouchDelegate, KTPhotoViewTouchDelegate, GXUC_ImageGalleryDetailDelegate, GXUC_ImageGalleryDataSource>
{
	NSArray *_photos;
	GXUC_ImageGalleryDetail *_detail;
	NSMutableArray *_selectedImageIndexes;
	BOOL _isSelecting;
	
	GXSharing *_sharing;
}

@end

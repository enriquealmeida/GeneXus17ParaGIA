//
//  KTPhotoBrowserGlobal.m
//  Sample
//
//  Created by Kirby Turner on 2/11/10.
//  Copyright 2010 White Peak Software Inc. All rights reserved.
//

#import "KTPhotoBrowserGlobal.h"

@interface KTBundleClass : NSObject @end
@implementation KTBundleClass @end

///////////////////////

NSBundle * KTBundle() {
	return [NSBundle bundleForClass:[KTBundleClass class]];
}

NSString * KTPathForBundleResource(NSString *relativePath) {
   NSString *resourcePath = KTBundle().resourcePath;
   return [resourcePath stringByAppendingPathComponent:relativePath];
}

///////////////////////

UIImage * KTLoadImageFromBundle(NSString *imageName) {
	return [UIImage imageNamed:imageName inBundle:KTBundle() compatibleWithTraitCollection:nil];
}

UIImage * KTPhotoPlaceholderThumbnail() {
	return [UIImage imageNamed:@"photoPlaceholderThumbnail"
					  inBundle:KTBundle()
 compatibleWithTraitCollection:nil];
}

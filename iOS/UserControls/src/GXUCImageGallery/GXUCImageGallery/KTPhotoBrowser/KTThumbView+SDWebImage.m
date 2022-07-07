//
//  KTThumbView+GXWebImage.m
//  Sample
//
//  Created by Henrik Nyh on 3/18/10.
//

#import "KTThumbView+SDWebImage.h"

@implementation KTThumbView (GXWebImage)

- (void)setImageWithURL:(NSURL *)url {
   [self setImageWithURL:url placeholderImage:nil];
}

- (void)setImageWithURL:(NSURL *)url placeholderImage:(UIImage *)placeholder {
	__block BOOL async = YES;
	__weak typeof(self) wself = self;
	GXWebImageOptions options = GXWebImageDelayPlaceholder | GXWebImageAvoidAutoSetImage;
	[self gxSetImageWithURL:url forState:UIControlStateNormal placeholderImage:placeholder options:options completed:
	 ^(UIImage *image, NSError *error, GXImageCacheType cacheType, NSURL *imageURL) {
		 __strong typeof(wself) sself = wself;
		 if (!sself || !image) return;
		 [sself setThumbImage:image];
		 async = NO;
	}];
	if (async && placeholder) {
		[self setThumbImage:placeholder];
	}
}

@end

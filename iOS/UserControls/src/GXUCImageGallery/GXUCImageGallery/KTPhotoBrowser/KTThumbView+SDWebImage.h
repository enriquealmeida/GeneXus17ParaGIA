//
//  KTThumbView+GXWebImage.h
//  Sample
//
//  Created by Henrik Nyh on 3/18/10.
//

@import GXCoreUI;
#import <GXUCImageGallery/KTThumbView.h>

@interface  KTThumbView (GXWebImage)

- (void)setImageWithURL:(NSURL *)url;
- (void)setImageWithURL:(NSURL *)url placeholderImage:(UIImage *)placeholder;

@end

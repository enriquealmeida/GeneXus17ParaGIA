//
//  GXUC_ImageGalleryDataSource.h
//  UCImageGallery
//
//  Created by Marcos Crispino on 02/12/11.
//  Copyright (c) 2011 Artech. All rights reserved.
//

@import Foundation;

@protocol GXUC_ImageGalleryDataSource <NSObject>

- (NSUInteger)count;

- (NSURL *)photoURLAtIndex:(NSUInteger)index;
- (NSString *)titleAtIndex:(NSUInteger)index;
- (NSString *)captionAtIndex:(NSUInteger)index;

@end

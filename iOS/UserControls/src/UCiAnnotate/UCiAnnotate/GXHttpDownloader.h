//
//  GXHttpDownloader.h
//  GXFlexibleClient
//
//  Created by willy on 8/30/13.
//  Copyright (c) 2013 Artech. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GXHttpDownloadItem : NSObject <NSURLConnectionDataDelegate>
@property(nonatomic, strong, readonly)NSURL *url;
@property(nonatomic, strong, readonly)NSString *destinationFileName; // full path

- (id)initWithUrl:(NSURL*)url destinationFileName:(NSString *)destinationFileName;

@end

@protocol GXHttpDownloadItemDelegate <NSObject>

- (void)downloadItem:(GXHttpDownloadItem *)item didFinishDownloadWithError:(NSError *)error;

@end

@class GXHttpDownloader;

@protocol GXHttpDownloaderDelegate <NSObject>

- (void)gxHttpDownloader:(GXHttpDownloader *)downloader didFinishDownloadAllItemsWithError:(NSError *)error;

@end

@interface GXHttpDownloader : NSObject <GXHttpDownloadItemDelegate> {
	
}

@property(nonatomic, unsafe_unretained, readwrite) id<GXHttpDownloaderDelegate> delegate;

- (void)startWithDownloadItems:(NSArray *)downloadItems;;
- (void)cancel;

@end

//
//  GXHttpDownloader.m
//  GXFlexibleClient
//
//  Created by willy on 8/30/13.
//  Copyright (c) 2013 Artech. All rights reserved.
//

#import "GXHttpDownloader.h"


@interface GXHttpDownloadItem(Protected)

- (void)startWithDelegate:(id<GXHttpDownloadItemDelegate>)delegate;
- (void)cancel;
- (void)disconnect;

@end


@implementation GXHttpDownloadItem {
	NSURLConnection *_connection;
	id <GXHttpDownloadItemDelegate> __unsafe_unretained _delegate;
	BOOL _canceled;
}

@synthesize url = _url;
@synthesize destinationFileName = _destinationFileName;

- (id)initWithUrl:(NSURL *)url destinationFileName:(NSString *)destinationFileName {
	if((self = [super init])) {
		_url = url;
		_destinationFileName = destinationFileName;
	}
	return self;
}

- (NSString *)description {
	return [NSString stringWithFormat:@"GXHttpDownloadItem: %@ %@", self.url, self.destinationFileName];
}

- (void)startWithDelegate:(id<GXHttpDownloadItemDelegate>)delegate {
	_canceled = NO;
	_delegate = delegate;
	[[NSFileManager defaultManager] removeItemAtPath:self.destinationFileName error:nil];
	_connection = [[NSURLConnection alloc] initWithRequest:[NSURLRequest requestWithURL:self.url] delegate:self startImmediately:YES];
}

- (void)cancel {
	@synchronized(self) {
		_canceled = YES;
		[_connection cancel];
	}
}

- (void)disconnect {
	@synchronized(self) {
		[_connection cancel];
		_delegate = nil;
	}
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {

}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    NSFileHandle *hFile = [NSFileHandle fileHandleForWritingAtPath:self.destinationFileName];
	if (!hFile)
	{
		[[NSFileManager defaultManager] createFileAtPath:self.destinationFileName contents:nil attributes:nil];
		hFile = [NSFileHandle fileHandleForWritingAtPath:self.destinationFileName];
	}
	if (!hFile)
	{
		NSLog(@"exception when writing to file %@", self.destinationFileName);
		return;
	}
	@try
	{
		[hFile seekToEndOfFile];
		[hFile writeData:data];
	}
	@catch (NSException * e)
	{
		NSLog(@"exception when writing to file %@", self.destinationFileName);
	}
	[hFile closeFile];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
	@synchronized(self) {
		if (!_canceled) {
			[_delegate downloadItem:self didFinishDownloadWithError:error];
		}
	}
}

- (NSCachedURLResponse *) connection:(NSURLConnection *)connection willCacheResponse:(NSCachedURLResponse *)cachedResponse {
    return nil;
}

- (void) connectionDidFinishLoading:(NSURLConnection *)connection {
	@synchronized(self) {
		if (!_canceled) {
			[_delegate downloadItem:self didFinishDownloadWithError:nil];
		}
	}
}


@end

@implementation GXHttpDownloader {
	NSMutableArray *_downloadItems; // collection of GXHttpDownloadItem
	id <GXHttpDownloaderDelegate> __unsafe_unretained _delegate;
	NSUInteger _pendingItems;
	NSMutableArray *_errors;
}

@synthesize delegate=_delegate;

- (void)startWithDownloadItems:(NSArray *)downloadItems {
	@synchronized(self) {
		_downloadItems = [NSMutableArray arrayWithArray:downloadItems];
		_pendingItems = _downloadItems.count;
		_errors = [NSMutableArray array];
		for (GXHttpDownloadItem *item in _downloadItems) {
			[item startWithDelegate:self];
		}
	}
}

- (void)cancel {
	@synchronized(self) {
		for (GXHttpDownloadItem *item in _downloadItems) {
			[item cancel];
		}
		_pendingItems = 0;
		_downloadItems = nil;
	}
}

- (void)downloadItem:(GXHttpDownloadItem *)item didFinishDownloadWithError:(NSError *)error {
	@synchronized(self) {
		_pendingItems--;
		if (error) {
			[self cancel];
		}
		if (!_pendingItems || error) {
			[_delegate gxHttpDownloader:self didFinishDownloadAllItemsWithError:error];
			_downloadItems = nil;
		}
	}
}

- (void)dealloc {
	for (GXHttpDownloadItem *item in _downloadItems) {
		[item disconnect];
	}
}

@end

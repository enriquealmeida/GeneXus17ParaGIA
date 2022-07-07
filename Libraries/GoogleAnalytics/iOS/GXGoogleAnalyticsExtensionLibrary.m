//
//  GXGoogleAnalyticsExtensionLibrary.m
//

@import GXCoreBL;
#import "GXGoogleAnalyticsService.h"

@interface GXGoogleAnalyticsExtensionLibrary : NSObject <GXExtensionLibraryProtocol>
@end

@implementation GXGoogleAnalyticsExtensionLibrary

- (void)initializeExtensionLibraryWithContext:(GXExtensionLibraryContext * _Nonnull)context {
	[GXCoreBLServices registerAnalyticsService:GXGoogleAnalyticsService.sharedInstance];
}

@end

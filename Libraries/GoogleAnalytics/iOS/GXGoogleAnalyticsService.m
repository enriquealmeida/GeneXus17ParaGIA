//
//  GXGoogleAnalyticsService.m
//

#import "GXGoogleAnalyticsService.h"
#if TARGET_OS_IOS
@import GXFoundation;
@import GXObjectsModel;
@import CoreData; // GAI
@import SystemConfiguration; // GAI
@import GoogleAnalytics;
#endif // TARGET_OS_IOS

#if TARGET_OS_IOS
@interface GXGoogleAnalyticsService ()

@property(nonatomic, strong, readonly) id <GAITracker> tracker;
@property(nonatomic, strong, readonly) id <GAITracker> commerceTracker;

@end
#endif // TARGET_OS_IOS

@implementation GXGoogleAnalyticsService

static GXGoogleAnalyticsService *_instance;

+ (GXGoogleAnalyticsService *)sharedInstance {
	@synchronized([GXGoogleAnalyticsService class]) {
		if (!_instance) {
			_instance = [[self alloc] init];
		}
	}
	return _instance;
}

+(id)alloc
{
	@synchronized([GXGoogleAnalyticsService class])
	{
		NSAssert(_instance == nil, @"Attempted to allocate a second instance of GXGoogleAnalyticsService Singleton.");
		_instance = [super alloc];
		return _instance;
	}

	return nil;
}

#if TARGET_OS_IOS
- (instancetype)init {
	self = [super init];
	_tracker = nil;
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onCurrentModelWillChangeNotification:)
												 name:GXModelWillChangeCurrentModelNotification
											   object:GXModel.class];
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onCurrentModelDidUpdateNotification:)
												 name:GXModelDidUpdateCurrentModelNotification
											   object:nil];
	return self;
}

- (void)dealloc {
	[[NSNotificationCenter defaultCenter] removeObserver:self];
}

@synthesize tracker = _tracker;
@synthesize commerceTracker = _commerceTracker;

- (id <GAITracker>)commerceTracker {
	return (_commerceTracker == nil) ? _tracker : _commerceTracker;
}

- (void)setupAnalyticsWithModel:(GXModel *)model {
	_tracker = nil;
	if (model) {
		id <GXApplicationEntryPoint> appEntryPoint = [[model appModel] mainEntryPoint];
		if ([appEntryPoint valueForAppEntryPointProperty:kAppEntryPointPropertyAppEnableAnalytics]){
			NSString *appId = [appEntryPoint valueForAppEntryPointProperty:kAppEntryPointPropertyAppEnableAnalyticsAppId];
			if (appId) {
				[GAI sharedInstance].trackUncaughtExceptions = NO;
				GAILogLevel logLevel = kGAILogLevelNone;
				if ([GXUtilities showDeveloperInfo]) {
					logLevel = kGAILogLevelWarning;
				}
				[[GAI sharedInstance].logger setLogLevel:logLevel];
				NSNumber *interval = [appEntryPoint valueForAppEntryPointProperty:kAppEntryPointPropertyAppAnalyticsPeriod];
				NSTimeInterval dispatchInterval = [interval doubleValue];
				if (dispatchInterval <= 0) {
					dispatchInterval = 120;
				}
				[GAI sharedInstance].dispatchInterval = dispatchInterval;
				_tracker = [[GAI sharedInstance] trackerWithTrackingId:appId];
				_commerceTracker = nil;
			}
		}
	}
}
#else
- (void)logGoogleAnalyticsUnsupported {
	if ([GXLog isLogEnabled]) {
		[GXLog.loggerService logMessage:[@"Google Analytics is unavailable in " stringByAppendingString:GXClientInformation.osName]
								forType:GXLoggerTypeGeneral
							  withLevel:GXLoggerLevelDebug
						   logToConsole:NO];
	}
}
#endif // TARGET_OS_IOS

#pragma mark - Current GXModel observers

#if TARGET_OS_IOS
- (void)onCurrentModelWillChangeNotification:(NSNotification *)notification {
	GXModel *model = notification.userInfo[GXModelModelKey];
	[self setupAnalyticsWithModel:model];
}

- (void)onCurrentModelDidUpdateNotification:(NSNotification *)notification {
	[self setupAnalyticsWithModel:[GXModel currentModel]];
}
#endif // TARGET_OS_IOS

#pragma mark - GXAnalyticsService

- (void)trackView:(NSString *)viewName {
#if TARGET_OS_IOS
	id <GAITracker> tracker = self.commerceTracker;
	if (tracker) {
		GAIDictionaryBuilder *builder = [[GAIDictionaryBuilder createScreenView] set:viewName
																			  forKey:kGAIScreenName];
		[tracker send:[builder build]];
	}
#else
	[self logGoogleAnalyticsUnsupported];
#endif
}

- (void)trackViewFromObject:(id<GXSDObjectLocator>)object {
	[GXAnalyticsServiceHelper defaultAnalyticsService:self trackViewFromObject:object];
}

- (void)trackEventName:(NSString *)name category:(NSString *)category label:(nullable NSString *)label value:(nullable NSNumber *)value {
#if TARGET_OS_IOS
	id <GAITracker> tracker = self.commerceTracker;
	if (tracker) {
		GAIDictionaryBuilder *builder = [GAIDictionaryBuilder createEventWithCategory:category
																			   action:name
																				label:label ? : @""
																				value:value ? : @0];
		[tracker send:[builder build]];
	}
#else
	[self logGoogleAnalyticsUnsupported];
#endif
}

- (void)trackEventName:(NSString *)name from:(id<GXSDObjectLocator>)object sender:(nullable id)sender {
#if TARGET_OS_IOS
	[GXAnalyticsServiceHelper defaultAnalyticsService:self trackEventName:name from:object sender:sender];
#else
	[self logGoogleAnalyticsUnsupported];
#endif
}

- (void)setUserId:(NSString *)userId {
#if TARGET_OS_IOS
	id <GAITracker> tracker = self.commerceTracker;
	if (tracker) {
		[tracker set:@"&uid" value:userId];
	}
#else
	[self logGoogleAnalyticsUnsupported];
#endif
}

- (void)setCommerceTrackerId:(NSString *)trackerId {
#if TARGET_OS_IOS
	_commerceTracker = [[GAI sharedInstance] trackerWithTrackingId:trackerId];
#else
	[self logGoogleAnalyticsUnsupported];
#endif
}

- (void)trackPurchaseId:(NSString *)purchaseId
			affiliation:(NSString *)affiliation
				revenue:(NSNumber *)revenue
					tax:(NSNumber *)tax
			   shipping:(NSNumber *)shipping
		   currencyCode:(NSString *)currencyCode {

#if TARGET_OS_IOS
	id <GAITracker> tracker = self.commerceTracker;
	if (tracker) {
		GAIDictionaryBuilder *builder = [GAIDictionaryBuilder createTransactionWithId:purchaseId
																		  affiliation:affiliation
																			  revenue:revenue
																				  tax:tax
																			 shipping:shipping
																		 currencyCode:currencyCode];
		[tracker send:[builder build]];
	}
#else
	[self logGoogleAnalyticsUnsupported];
#endif
}

- (void)trackPurchasedItem:(NSString *)productId
				purchaseId:(NSString *)purchaseId
					  name:(NSString *)name
				  category:(NSString *)category
					 price:(NSNumber *)price
				  quantity:(NSNumber *)quantity
			  currencyCode:(NSString *)currencyCode {

#if TARGET_OS_IOS
	id <GAITracker> tracker = self.commerceTracker;
	if (tracker) {
		GAIDictionaryBuilder *builder = [GAIDictionaryBuilder createItemWithTransactionId:purchaseId
																					 name:name
																					  sku:productId
																				 category:category
																					price:price
																				 quantity:quantity
																			 currencyCode:currencyCode];
		[tracker send:[builder build]];
	}
#else
	[self logGoogleAnalyticsUnsupported];
#endif
}

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-implementations"
- (void)trackPanelName:(NSString *)name {
#if DEBUG
	NSAssert(NO, @"DEPRECATED");
#endif
#if TARGET_OS_IOS
	id <GAITracker> tracker = self.tracker;
	if (tracker) {
		GAIDictionaryBuilder *builder = [[GAIDictionaryBuilder createScreenView] set:name
																			  forKey:kGAIScreenName];
		[tracker send:[builder build]];
	}
#else
	[self logGoogleAnalyticsUnsupported];
#endif
}

- (void)trackEventName:(NSString *)name category:(NSString *)category sender:(nullable id)sender {
#if DEBUG
	NSAssert(NO, @"DEPRECATED");
#endif
#if TARGET_OS_IOS
	if (![GXAnalyticsServiceHelper shouldTrackEventName:name]) {
		return;
	}

	NSString *label = nil;
	NSNumber *value = nil;
	[GXAnalyticsServiceHelper defaultSender:sender label:&label value:&value];
	[self trackEventName:name category:category label:label value:value];
#else
	[self logGoogleAnalyticsUnsupported];
#endif
}
#pragma clang diagnostic pop

@end

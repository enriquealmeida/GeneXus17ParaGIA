//
//  GXUserNotificationsManager+OneSignal.m
//  TestPushNotifications
//

#if TARGET_OS_IOS

@import GXCoreBL;

@implementation GXUserNotificationsManager (GXUserNotificationsProviderOptionals_OneSignal)

- (nonnull NSDictionary *)notificationsProviderProperties {
	return @{ @"AppID": @"$Main.EnvironmentDynamic.ONESIGNAL_APP_ID$" };
}

@end

#endif // TARGET_OS_IOS

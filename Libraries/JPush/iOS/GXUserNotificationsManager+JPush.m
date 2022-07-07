//
//  GXUserNotificationsManager+JPush
//  TestPushNotifications
//

#if TARGET_OS_IOS

@import GXCoreBL;

@implementation GXUserNotificationsManager (GXUserNotificationsProviderOptionals_OneSignal)

- (nonnull NSDictionary *)notificationsProviderProperties {
    return @{ @"AppID": @"$Main.EnvironmentDynamic.JPUSH_AppKey$" };
    return @{ @"Channel": @"$Main.EnvironmentDynamic.JPUSH_APP_CHANNEL$" };
    return @{ @"ApsForProduction": @"$Main.EnvironmentDynamic.JPUSH_APP_ISPROD$" };
}

@end

#endif // TARGET_OS_IOS

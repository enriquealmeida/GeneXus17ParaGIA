//
//  GXUserNotificationsManager+Optionals.m
//  $Main.Name$
//

#if TARGET_OS_IOS

@import GXCoreBL;

@implementation GXUserNotificationsManager (GXUserNotificatinManagerOptionals)

- (BOOL)supportsRemoteNotifications {
$if(Main.EnableNotification)$
	return YES;
$else$
	return NO;
$endif$
}

- (BOOL)supportsLocalNotifications {
$if(Main.IsUsingLocalNotificationAPI)$
	return YES;
$else$
	return NO;
$endif$
}

@end
$if(Main.EnableNotification)$



@implementation GXUserNotificationsManager (GXUserNotificationsManagerRemoteOptionals)

- (NSString *const)applicationLaunchOptionsRemoteNotificationKey {
	return UIApplicationLaunchOptionsRemoteNotificationKey;
}

- (void)applicationRegisterForRemoteNotifications {
	id <GXUserNotificationsProviderProtocol> provider = self.class.notificationsProvider;
	if (provider && [provider respondsToSelector:@selector(registerForPushNotifications)]) {
		[provider registerForPushNotifications];
	}
	else {
		[[UIApplication sharedApplication] registerForRemoteNotifications];
	}
}

@end
$endif$
$if(Main.IsUsingLocalNotificationAPI)$



@implementation GXUserNotificationsManager (GXUserNotificationsManagerLocalOptionals)

- (NSString *const)applicationLaunchOptionsLocalNotificationKey {
	return UIApplicationLaunchOptionsLocalNotificationKey;
}

@end
$endif$
$if(Main.EnableNotification && !Main.IsDefaultNotificationsProvider)$



@implementation GXUserNotificationsManager (GXUserNotificationsProviderOptionals)

+ (nonnull NSString *)notificationsProviderTypeIdentifier {
	return @"$Main.EnvironmentDynamic.NOTIFICATIONS_PROVIDER$";
}

@end
$endif$

#endif // TARGET_OS_IOS

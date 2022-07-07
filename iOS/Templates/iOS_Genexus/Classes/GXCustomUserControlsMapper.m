//
//  GXCustomUserControlsMapper.m
//  $Main.Name$
//

#import "GXCustomUserControlsMapper.h"

@implementation GXCustomUserControlsMapper

- (nullable NSString *)userControlClassNameForControlName:(NSString *)name {
$Main.IOSUserControlsMapping:{ uc |
	if ([name isEqualToString:@"$uc.ucName$"])
		return @"$uc.ucClass$";
}; separator=""$
	return nil;
}

@end

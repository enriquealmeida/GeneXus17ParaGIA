//
//  GXApplicationModel+GXTwitterOptionals.m
//  $Main.Name$
//

$if(Main.Dynamic.TwitterAppKey || Main.Dynamic.TwitterAppSecret)$
@import GXObjectsModel;

@implementation GXApplicationModel (GXTwitterOptionals)

$if(Main.Dynamic.TwitterAppKey)$
- (NSString *)gxTWconsumerKey {
	return @"$Main.Dynamic.TwitterAppKey$";
}

$endif$
$if(Main.Dynamic.TwitterAppSecret)$
- (NSString *)gxTWconsumerSecret {
	return @"$Main.Dynamic.TwitterAppSecret$";
}

$endif$
@end
$endif$
#import "$EOData.FileName$.h"

@implementation GXExternalObjectBase($EOData.EOWrapperProtocolName$)

+ (null_unspecified Class<$EOData.EOWrapperProtocolName$>)gxEOClass_$EOData.Name$ {
	static Class<$EOData.EOWrapperProtocolName$> _gxEOClass_$EOData.Name$;
		static dispatch_once_t onceToken;
		dispatch_once(&onceToken, ^{
			_gxEOClass_$EOData.Name$ = NSClassFromString(@"$EOData.Name$");
		});
	return _gxEOClass_$EOData.Name$;
}

@end

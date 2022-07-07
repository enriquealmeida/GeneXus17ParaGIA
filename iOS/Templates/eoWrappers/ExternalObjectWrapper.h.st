@import GXStandardClasses;

NS_ASSUME_NONNULL_BEGIN

$EOData.ForwardDeclarations:{decl| @class $decl$;}; separator="\n"$

@protocol $EOData.EOWrapperProtocolName$ <NSObject>

- (instancetype)init;

$EOData.PropertiesListString$
$EOData.MethodsListString$
@end

@interface GXExternalObjectBase ($EOData.EOWrapperProtocolName$)

@property(class, nonatomic, readonly, null_unspecified) Class<$EOData.EOWrapperProtocolName$> gxEOClass_$EOData.Name$;

@end

NS_ASSUME_NONNULL_END

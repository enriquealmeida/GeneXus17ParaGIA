//
//  GXUC_MapList+GXHelpers.h
//  GXUCMaps-iOS/
//
//  Created by Fabian Inthamoussu on 27/11/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

#import "GXUC_MapList.h"
#import "GXUC_MapList+GXProperties.h"
@import GXCoreUI;

NS_ASSUME_NONNULL_BEGIN

@interface GXUC_MapList (GXHelpers)

- (void)zoomToFitMapAnnotations; // Animated if self.viewVisibleState == GXViewVisibleStateAppeared
- (void)zoomToFitMapAnnotations:(BOOL)animated;

- (void)updateMapViewLayoutMargins;
- (BOOL)updateMapViewEdgePadding:(BOOL)animated onlyIfChanged:(BOOL)onlyIfChanged;

- (BOOL)requestLocationAuthorizationIfNeeded;

- (void)setMapType:(GXMapType)mapType;

- (id<GXUC_MapPinAnnotation>)addAnnotationWithLocation:(CLLocationCoordinate2D)location
										   entitiyData:(id<GXEntityData>)entityData
											 indexPath:(NSIndexPath *)indexPath
										  animationKey:(nullable NSString *)animationKey
									pinImageFieldValue:(id)pinImageFieldValue
									pinImageThemeClass:(SDMapPinImageThemeClass *)pinImageThemeClass;

- (GXUCMapListAnimationEndBehavior)convertAnimationEndBehaviorFromString:(nullable NSString *)string;

@end

NS_ASSUME_NONNULL_END

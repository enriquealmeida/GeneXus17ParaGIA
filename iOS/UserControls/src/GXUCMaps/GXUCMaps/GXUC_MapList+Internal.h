//
//  GXUC_MapList+Internal.h
//  GXUCMaps-iOS
//
//  Created by Fabian Inthamoussu on 27/11/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

@import GXCoreUI;
#import <GXUCMaps/GXUC_MapList.h>

NS_ASSUME_NONNULL_BEGIN

@interface GXUC_MapList () {
@protected
	CLLocationManager *_locManager;
	CLLocation *_lastLocation;
	
	UIEdgeInsets _mapViewEdgePadding;
	BOOL _regionChangedSinceLastZoomToFitMapAnnotations;
	
	// Properties
	GXOptionalBoolean _showMyLocation;
	GXOptionalBoolean _showNavigation;
	GXOptionalBoolean _showTraffic;
	NSString *_pinImageName;
	NSString *_pinImageAttribute;
	NSString *_pinImageFieldSpecifier;
	NSString *_pinImageMyLocation;
	NSString *_locationAttribute;
	NSString *_locationFieldSpecifier;
	NSNumber *_mapType;
	GXOptionalBoolean _canChooseMapType;
	NSNumber *_initialZoom;
	NSString *_initialZoomRadiusAttribute;
	NSString *_initialZoomRadiusFieldSpecifier;
	NSNumber *_center;
	NSString *_centerAttribute;
	NSString *_centerFieldSpecifier;
	NSString *_pinImageClassName;
	SDMapPinImageThemeClass *_pinImageClass;
	
	BOOL _isCalloutVisible;
	
#pragma mark Selection Layer
	
	GXOptionalBoolean _selectionLayer;
	NSString *_selectionTargetImageName;
	NSString *_selectionTargetImageClassName;
	SDMapPinImageThemeClass *_selectionTargetImageClass;
	
#pragma mark Directions Layer
	
	GXOptionalBoolean _directionsLayer;
	NSString *_routeThemeClassName;
	
#pragma mark Animations Layer
	
	GXOptionalBoolean _animationsLayer;
	NSNumber *_animationsDefaultDuration;
	NSString *_animationKeyAttribute;
	NSString *_animationKeyFieldSpecifier;
	NSString *_animationDurationAttribute;
	NSString *_animationDurationFieldSpecifier;
	NSString *_animationDefaultendBehaviorStr;
	NSString *_animationEndBehaviorAttribute;
	NSString *_animationEndBehaviorFieldSpecifier;
}

- (void)fireLocationSelectionEventWithName:(NSString *)eventName;

- (void)setCenterCoordinate:(CLLocationCoordinate2D)newCenterCoordinate withZoomLevel:(nullable NSNumber*)zoomLevel animated:(BOOL)animated;
- (void)setZoomLevel:(float)zoomLevel animated:(BOOL)animated;
- (void)selectAnnotationAtIndexPath:(NSIndexPath *)indexPath animated:(BOOL)animated;
- (void)deselectAnnotationAtIndexPath:(NSIndexPath *)indexPath animated:(BOOL)animated;
- (BOOL)executeDefaultActionIfPossibleForAnnotation:(id<GXUC_MapPinAnnotation>)annotation;
- (BOOL)canShowCalloutForAnnotation:(id<GXUC_MapPinAnnotation>)annotation;
- (void)fireSelectionChangedEvent;
- (void)removeAnimatedAnnotationWithKey:(NSString *)animationKey;

- (void)enableSelectionLayer;
- (void)disableSelectionLayer;

@end

NS_ASSUME_NONNULL_END

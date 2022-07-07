//
//  GXUC_MapList+GXProperties.h
//  GXUCMaps-iOS
//
//  Created by Fabian Inthamoussu on 27/11/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

@import GXCoreUI;
#import <GXUCMaps/GXUC_MapList.h>

typedef NS_ENUM(uint_least8_t, GXUCMapListAnimationEndBehavior) {
	GXUCMapListAnimationEndBehaviorDoNotRepeat,
	GXUCMapListAnimationEndBehaviorRepeat,
	GXUCMapListAnimationEndBehaviorDisappear,
};

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(uint_least8_t, GXMapInitialZoomType) {
	GXMapInitialZoomShowAll,
	GXMapInitialZoomNearMe,
	GXMapInitialZoomRadius,
	GXMapInitialZoomNoZoom,
};

typedef NS_ENUM(uint_least8_t, GXMapCenterType) {
	GXMapCenterDefault,
	GXMapCenterMyLocation,
	GXMapCenterCustom
};

@interface GXUC_MapList (GXProperties)

@property(nonatomic, assign, readonly) BOOL showMyLocation;
@property(nonatomic, assign, readonly) BOOL showTraffic;
@property(nonatomic, assign, readonly) BOOL showNavigation;

@property(nullable, nonatomic, strong, readonly) NSString *pinImageName;
@property(nullable, nonatomic, strong, readonly) NSString *pinImageAttribute;
@property(nullable, nonatomic, strong, readonly) NSString *pinImageFieldSpecifier;
@property(nullable, nonatomic, strong, readonly) SDMapPinImageThemeClass *pinImageThemeClass;
@property(nullable, nonatomic, strong, readonly) NSString *pinImageMyLocation;

@property(nullable, nonatomic, strong, readonly) NSString *locationAttribute;
@property(nullable, nonatomic, strong, readonly) NSString *locationFieldSpecifier;

@property(nonatomic, assign, readonly) GXMapType mapType;
@property(nonatomic, assign, readonly) BOOL canChooseMapType;

@property(nonatomic, assign, readonly) GXMapInitialZoomType initialZoom;
@property(nullable, nonatomic, strong, readonly) NSString *initialZoomRadiusAttribute;
@property(nullable, nonatomic, strong, readonly) NSString *initialZoomRadiusFieldSpecifier;

@property(nonatomic, assign, readonly) GXMapCenterType center;
@property(nullable, nonatomic, strong, readonly) NSString *centerAttribute;
@property(nullable, nonatomic, strong, readonly) NSString *centerFieldSpecifier;

#pragma mark - Selection Layer

@property(nonatomic, assign, readwrite, getter=isSelectionLayerEnabled) BOOL selectionLayerEnabled;
@property(nullable, nonatomic, strong, readonly) NSString *selectionTargetImageName;
@property(nullable, nonatomic, strong, readonly) SDMapPinImageThemeClass *selectionTargetImageThemeClass;

#pragma mark - Directions Layer

@property(nonatomic, assign, readwrite, getter=isDirectionsLayerEnabled) BOOL directionsLayerEnabled;

#pragma mark - Animations Layer

@property(nonatomic, assign, readwrite, getter=isAnimationsLayerEnabled) BOOL animationLayerEnabled;
@property(nonatomic, assign, readonly) NSTimeInterval animationDefaultDuration;
@property(nullable, nonatomic, strong, readonly) NSString *animationKeyAttribute;
@property(nullable, nonatomic, strong, readonly) NSString *animationKeyFieldSpecifier;
@property(nullable, nonatomic, strong, readonly) NSString *animationDurationAttribute;
@property(nullable, nonatomic, strong, readonly) NSString *animationDurationFieldSpecifier;
@property(nonatomic, assign, readonly) GXUCMapListAnimationEndBehavior animationEndBehavior;
@property(nullable, nonatomic, strong, readonly) NSString *animationEndBehaviorAttribute;
@property(nullable, nonatomic, strong, readonly) NSString *animationEndBehaviorFieldSpecifier;

@end

NS_ASSUME_NONNULL_END


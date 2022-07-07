//
//  GXUC_MapList+GXProperties.m
//  GXUCMaps-iOS
//
//  Created by Fabian Inthamoussu on 27/11/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

#import "GXUC_MapList+Internal.h"
#import "GXUC_MapList+GXHelpers.h"
#import "GXUC_MapList+GXProperties.h"

@implementation GXUC_MapList (GXProperties)

- (BOOL)showMyLocation {
	if (_showMyLocation == GXOptionalBooleanUnknown) {
		_showMyLocation = [self.properties getPropertyValueBool:@"@SDMapsShowMyLocation"] ? GXOptionalBooleanYes : GXOptionalBooleanNo;
	}
	return _showMyLocation == GXOptionalBooleanYes;
}

- (BOOL)showTraffic {
	if (_showTraffic == GXOptionalBooleanUnknown) {
		_showTraffic = [self.properties getPropertyValueBool:@"@SDMapsShowTraffic"] ? GXOptionalBooleanYes : GXOptionalBooleanNo;
	}
	return _showTraffic == GXOptionalBooleanYes;
}

- (BOOL)showNavigation {
	if (_showNavigation == GXOptionalBooleanUnknown) {
		_showNavigation = [self.properties getPropertyValueBool:@"@SDMapsShowNavigation"] ? GXOptionalBooleanYes : GXOptionalBooleanNo;
	}
	return _showNavigation == GXOptionalBooleanYes;
}

- (nullable NSString *)pinImageName {
	if (_pinImageName == nil) {
		NSString *propVal = [self.properties getPropertyValueString:@"@SDMapsPinImage"];
		_pinImageName = [GXObjectHelper parseObjectNameOfType:kGXObjectIdImage from:propVal] ? : @"";
	}
	return _pinImageName.length > 0 ? _pinImageName : nil;
}

- (nullable NSString *)pinImageAttribute {
	if (_pinImageAttribute == nil) {
		NSString *propVal = nil;
		if (self.pinImageFieldSpecifier != nil || self.locationFieldSpecifier == nil) {
			propVal = [self.properties getPropertyValueString:@"@SDMapsPinImageAtt"];
		}
		_pinImageAttribute = propVal ? : @"";
	}
	return _pinImageAttribute.length > 0 ? _pinImageAttribute : nil;
}

- (nullable NSString *)pinImageFieldSpecifier {
	if (_pinImageFieldSpecifier == nil) {
		_pinImageFieldSpecifier = [self.properties getPropertyValueString:@"@SDMapsPinImageField"] ? : @"";
	}
	return _pinImageFieldSpecifier.length > 0 ? _pinImageFieldSpecifier : nil;
}

- (nullable SDMapPinImageThemeClass *)pinImageThemeClass {
	if (_pinImageClassName == nil) {
		_pinImageClassName = [self.properties getPropertyValueString:@"@SDMapsPinImageClass"] ? : @"";
		GXThemeClass *tmpPinImageClass = _pinImageClassName.length > 0 ? [[GXTheme currentTheme] themeClassForFullName:_pinImageClassName] : nil;
		if ([tmpPinImageClass isKindOfClass:[SDMapPinImageThemeClass class]]) {
			_pinImageClass = (SDMapPinImageThemeClass *)tmpPinImageClass;
		}
		else {
			_pinImageClass = nil;
		}
	}
	return _pinImageClass;
}

- (nullable NSString *)pinImageMyLocation {
	if (_pinImageMyLocation == nil) {
		NSString *propVal = [self.properties getPropertyValueString:@"@SDMapsPinImageMyLocation"];
		_pinImageMyLocation = [GXObjectHelper parseObjectNameOfType:kGXObjectIdImage from:propVal] ? : @"";
	}
	return _pinImageMyLocation.length > 0 ? _pinImageMyLocation : nil;
}

- (nullable NSString *)locationAttribute {
	if (_locationAttribute == nil) {
		NSString *propVal = [self.properties getPropertyValueString:@"@SDMapsLocationAtt"];
		if (propVal == nil) {
			// beta 4 compatibility: if the 'location att' property is not set, search for the geolocation attribute
			id <GXEntityDataDescriptor> dataDesctriptor = [[self.entityDataListProvider entityDataSource] entityDataSourceDataDescriptor];
			for(id <GXEntityDataFieldDescriptor> fieldDescriptor in [dataDesctriptor entityDataFieldDescriptors]) {
				if([[fieldDescriptor entityDataFieldInfo] entityDataFieldInfoSpecialDomain] == GXSpecialDomainLocation) {
					propVal = [fieldDescriptor entityDataFieldName];
					break;
				}
			}
		}
		_locationAttribute = propVal ? : @"";
	}
	return _locationAttribute.length > 0 ? _locationAttribute : nil;
}

- (nullable NSString *)locationFieldSpecifier {
	if (_locationFieldSpecifier == nil) {
		_locationFieldSpecifier = [self.properties getPropertyValueString:@"@SDMapsLocationField"] ? : @"";
	}
	return _locationFieldSpecifier.length > 0 ? _locationFieldSpecifier : nil;
}

- (GXMapType)mapType {
	GXMapType mapType;
	if (_mapType != nil) {
		mapType = (GXMapType)[_mapType unsignedIntegerValue];
	} else {
		mapType = GXMapStandard;
		NSString *propVal = [self.properties getPropertyValueString:@"@SDMapsMapType"];
		if ([propVal isEqualToString:@"Satellite"]) {
			mapType = GXMapSatellite;
		}
		else if ([propVal isEqualToString:@"Hybrid"]) {
			mapType = GXMapHybrid;
		}
		_mapType = @(mapType);
	}
	return mapType;
}

- (BOOL)canChooseMapType {
	if (_canChooseMapType == GXOptionalBooleanUnknown) {
		_canChooseMapType = [self.properties getPropertyValueBool:@"@SDMapsCanChooseType" defaultValue:NO] ? GXOptionalBooleanYes : GXOptionalBooleanNo;
	}
	return _canChooseMapType == GXOptionalBooleanYes;
}

- (GXMapInitialZoomType)initialZoom {
	GXMapInitialZoomType initialZoom;
	if (_initialZoom) {
		initialZoom = [_initialZoom unsignedIntegerValue];
	}
	else {
		initialZoom = GXMapInitialZoomShowAll;
		NSString *propVal = [self.properties getPropertyValueString:@"@SDMapsInitialZoomBehavior"];
		if ([propVal isEqualToString:@"NearestPoint"]) {
			initialZoom = GXMapInitialZoomNearMe;
		}
		else if ([propVal isEqualToString:@"Radius"]) {
			initialZoom = GXMapInitialZoomRadius;
		}
		else if ([propVal isEqualToString:@"NoZoom"]) {
			initialZoom = GXMapInitialZoomNoZoom;
		}
		_initialZoom = @(initialZoom);
	}
	return initialZoom;
}

- (nullable NSString *)initialZoomRadiusAttribute {
	if (_initialZoomRadiusAttribute == nil) {
		_initialZoomRadiusAttribute = [self.properties getPropertyValueString:@"@SDMapsZoomRadiusAtt"] ? : @"";
	}
	return _initialZoomRadiusAttribute.length > 0 ? _initialZoomRadiusAttribute : nil;
}

- (nullable NSString *)initialZoomRadiusFieldSpecifier {
	if (_initialZoomRadiusFieldSpecifier == nil) {
		_initialZoomRadiusFieldSpecifier = [self.properties getPropertyValueString:@"@SDMapsZoomRadiusField"] ? : @"";
	}
	return _initialZoomRadiusFieldSpecifier.length > 0 ? _initialZoomRadiusFieldSpecifier : nil;
}

- (GXMapCenterType)center {
	GXMapCenterType center;
	if (_center) {
		center = [_center unsignedIntegerValue];
	}
	else {
		center = GXMapCenterDefault;
		NSString *propVal = [self.properties getPropertyValueString:@"@SDMapsCenter"];
		if ([propVal isEqualToString:@"MyLocation"]) {
			center = GXMapCenterMyLocation;
		}
		else if ([propVal isEqualToString:@"Custom"]) {
			center = GXMapCenterCustom;
		}
		_center = @(center);
	}
	return center;
}

- (nullable NSString *)centerAttribute {
	if (_centerAttribute == nil) {
		_centerAttribute = [self.properties getPropertyValueString:@"@SDMapsCenterAtt"] ? : @"";
	}
	return _centerAttribute.length > 0 ? _centerAttribute : nil;
}

- (nullable NSString *)centerFieldSpecifier {
	if (_centerFieldSpecifier == nil) {
		_centerFieldSpecifier = [self.properties getPropertyValueString:@"@SDMapsCenterField"] ? : @"";
	}
	return _centerFieldSpecifier.length > 0 ? _centerFieldSpecifier : nil;
}

- (BOOL)isSelectionLayerEnabled {
	if (_selectionLayer == GXOptionalBooleanUnknown) {
		_selectionLayer = [self.properties getPropertyValueBool:@"@SDMapsSelectionLayer"] ? GXOptionalBooleanYes : GXOptionalBooleanNo;
	}
	return _selectionLayer == GXOptionalBooleanYes;
}

- (void)setSelectionLayerEnabled:(BOOL)selectionLayerEnabled {
	if (selectionLayerEnabled) {
		_selectionLayer = GXOptionalBooleanYes;
		[self enableSelectionLayer];
	} else {
		_selectionLayer = GXOptionalBooleanNo;
		[self disableSelectionLayer];
	}
}

- (nullable NSString *)selectionTargetImageName {
	if (_selectionTargetImageName == nil) {
		NSString *propVal = [self.properties getPropertyValueString:@"@SDMapsSelectionTargetImage"] ? : @"";
		_selectionTargetImageName = [GXObjectHelper parseObjectNameOfType:kGXObjectIdImage from:propVal] ? : @"";
	}
	return _selectionTargetImageName.length > 0 ? _selectionTargetImageName : nil;
}

- (nullable SDMapPinImageThemeClass *)selectionTargetImageThemeClass {
	if (_selectionTargetImageClassName == nil) {
		_selectionTargetImageClassName = [self.properties getPropertyValueString:@"@SDMapsSelectionTargetImageClass"] ? : @"";
		GXThemeClass *tmpPinImageClass = _selectionTargetImageClassName.length > 0 ? [[GXTheme currentTheme] themeClassForFullName:_selectionTargetImageClassName] : nil;
		if ([tmpPinImageClass isKindOfClass:[SDMapPinImageThemeClass class]]) {
			_selectionTargetImageClass = (SDMapPinImageThemeClass *)tmpPinImageClass;
		}
		else {
			_selectionTargetImageClass = nil;
		}
	}
	return _selectionTargetImageClass;
}

- (BOOL)isDirectionsLayerEnabled {
	if (_directionsLayer == GXOptionalBooleanUnknown) {
		_directionsLayer = [self.properties getPropertyValueBool:@"@SDMapsDirectionsLayer"] ? GXOptionalBooleanYes : GXOptionalBooleanNo;
	}
	return _directionsLayer == GXOptionalBooleanYes;
}

- (void)setDirectionsLayerEnabled:(BOOL)directionsLayerEnabled {
	_directionsLayer = directionsLayerEnabled ? GXOptionalBooleanYes : GXOptionalBooleanNo;
}

- (BOOL)isAnimationsLayerEnabled {
	if (_animationsLayer == GXOptionalBooleanUnknown) {
		_animationsLayer = [self.properties getPropertyValueBool:@"@SDMapsAnimationsLayer"] ?
			GXOptionalBooleanYes : GXOptionalBooleanNo;
	}
	return _animationsLayer == GXOptionalBooleanYes;
}

- (void)setAnimationLayerEnabled:(BOOL)animationLayerEnabled {
	_animationsLayer = animationLayerEnabled ? GXOptionalBooleanYes : GXOptionalBooleanNo;
}

- (NSTimeInterval)animationDefaultDuration {
	if (_animationsDefaultDuration == nil) {
		_animationsDefaultDuration = [NSNumber numberWithFloat:[self.properties getPropertyValueFloat:@"@SDMapsAnimationDuration"]];
	}
	return _animationsDefaultDuration.floatValue;
}

- (nullable NSString *)animationKeyAttribute {
	if (_animationKeyAttribute == nil) {
		_animationKeyAttribute = [self.properties getPropertyValueString:@"@SDMapsAnimationKeyAtt"];
	}
	return _animationKeyAttribute;
}

- (nullable NSString *)animationKeyFieldSpecifier {
	if (_animationKeyFieldSpecifier == nil) {
		_animationKeyFieldSpecifier = [self.properties getPropertyValueString:@"@SDMapsAnimationKeyField"];
	}
	return _animationKeyFieldSpecifier;
}

- (nullable NSString *)animationDurationAttribute {
	if (_animationDurationAttribute == nil) {
		_animationDurationAttribute = [self.properties getPropertyValueString:@"@SDMapsAnimationDurationAtt"];
	}
	return _animationDurationAttribute;
}

- (nullable NSString *)animationDurationFieldSpecifier {
	if (_animationDurationFieldSpecifier == nil) {
		_animationDurationFieldSpecifier = [self.properties getPropertyValueString:@"@SDMapsAnimationDurationField"];
	}
	return _animationDurationFieldSpecifier;
}

- (GXUCMapListAnimationEndBehavior)animationEndBehavior {
	if (_animationDefaultendBehaviorStr == nil) {
		_animationDefaultendBehaviorStr = [self.properties getPropertyValueString:@"@SDMapsAnimationEndBehavior"];
	}
	
	return [self convertAnimationEndBehaviorFromString:_animationDefaultendBehaviorStr];
}

- (nullable NSString *)animationEndBehaviorAttribute {
	if (_animationEndBehaviorAttribute == nil) {
		_animationEndBehaviorAttribute = [self.properties getPropertyValueString:@"@SDMapsAnimationEndBehaviorAtt"];
	}
	return _animationEndBehaviorAttribute;
}

- (nullable NSString *)animationEndBehaviorFieldSpecifier {
	if (_animationEndBehaviorFieldSpecifier == nil) {
		_animationEndBehaviorFieldSpecifier = [self.properties getPropertyValueString:@"@SDMapsAnimationEndBehaviorField"];
	}
	return _animationEndBehaviorFieldSpecifier;
}

@end

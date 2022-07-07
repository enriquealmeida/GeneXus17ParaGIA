//
//  GXUC_MapList+GXHelpers.m
//  GXUCMaps-iOS/
//
//  Created by Fabian Inthamoussu on 27/11/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

#import "GXUC_MapList+GXHelpers.h"
#import "GXUC_MapList+GXProperties.h"
#import "GXUC_MapList+Internal.h"
@import GXCoreUI;

@implementation GXUC_MapList (GXHelpers)

- (void)zoomToFitMapAnnotations {
	BOOL animated = self.viewVisibleState == GXViewVisibleStateAppeared;
	[self zoomToFitMapAnnotations:animated];
}

- (void)zoomToFitMapAnnotations:(BOOL)animated {
	UIView<GXMapView> *mapView = [self mapView];
	if (mapView == nil) {
		return;
	}
	
	_regionChangedSinceLastZoomToFitMapAnnotations = NO;
	NSArray *annotations = [mapView gxAnnotations];
	
	CLLocationCoordinate2D topLeftCoord;
	topLeftCoord.latitude = -90;
	topLeftCoord.longitude = 180;
	
	CLLocationCoordinate2D bottomRightCoord;
	bottomRightCoord.latitude = 90;
	bottomRightCoord.longitude = -180;
	
	CLLocationCoordinate2D (^getTopLeft)(CLLocationCoordinate2D, CLLocationCoordinate2D) = ^(CLLocationCoordinate2D c1, CLLocationCoordinate2D c2){
		CLLocationCoordinate2D result;
		result.longitude = fmin(c1.longitude, c2.longitude);
		result.latitude  = fmax(c1.latitude,  c2.latitude);
		return result;
	};
	
	CLLocationCoordinate2D (^getBottomRight)(CLLocationCoordinate2D, CLLocationCoordinate2D) = ^(CLLocationCoordinate2D c1, CLLocationCoordinate2D c2){
		CLLocationCoordinate2D result;
		result.longitude = fmax(c1.longitude, c2.longitude);
		result.latitude  = fmin(c1.latitude,  c2.latitude);
		return result;
	};
	
	CLLocation *userAnnotationLocation = [mapView gxUserLocation];
	CLLocation *userLocation = userAnnotationLocation ? : _lastLocation;
	
	if (userLocation && [self initialZoom] == GXMapInitialZoomNearMe) {
		id<GXUC_MapPinAnnotation> closestAnnotation = nil;
		switch ([annotations count]) {
			case 0:
				break;
			case 1:
			{
				closestAnnotation = [annotations lastObject];
				if (![closestAnnotation conformsToProtocol:@protocol(GXUC_MapPinAnnotation)]) {
					closestAnnotation = nil;
				}
				break;
			}
			default:
			{
				CLLocationDistance closestAnnotationDistanceSqr = DBL_MAX;
				CLLocationCoordinate2D userLocationCoordinate = userLocation.coordinate;
				for (id <MKAnnotation> annotation in annotations) {
					if ([annotation conformsToProtocol:@protocol(GXUC_MapPinAnnotation)]) {
						CLLocationCoordinate2D annCoordinate = annotation.coordinate;
						CLLocationDegrees latDiff = annCoordinate.latitude - userLocationCoordinate.latitude;
						CLLocationDegrees lonDiff = annCoordinate.longitude - userLocationCoordinate.longitude;
						CLLocationDistance annDistanceSqrToUserLocation = latDiff * latDiff + lonDiff * lonDiff;
						if (annDistanceSqrToUserLocation < closestAnnotationDistanceSqr) {
							closestAnnotationDistanceSqr = annDistanceSqrToUserLocation;
                            closestAnnotation = (id<GXUC_MapPinAnnotation>)annotation;
						}
					}
				}
				break;
			}
		}
		
		if (closestAnnotation) {
			topLeftCoord = getTopLeft(userLocation.coordinate, closestAnnotation.coordinate);
			bottomRightCoord = getBottomRight(userLocation.coordinate, closestAnnotation.coordinate);
		}
		else {
			topLeftCoord = bottomRightCoord = userLocation.coordinate;
		}
	}
	else {
		for (id <MKAnnotation> annotation in annotations) {
			topLeftCoord = getTopLeft(topLeftCoord, annotation.coordinate);
			bottomRightCoord = getBottomRight(bottomRightCoord, annotation.coordinate);
		}
		if (userAnnotationLocation) {
			topLeftCoord = getTopLeft(topLeftCoord, userLocation.coordinate);
			bottomRightCoord = getBottomRight(bottomRightCoord, userLocation.coordinate);
		}
	}
	
	CLLocation *centerLocation = nil;
	
	GXMapCenterType centerType = [self center];
	if (centerType == GXMapCenterCustom) {
		if ([annotations count]) {
			NSString *locationString = [self valueForEntityDataFieldName:[self centerAttribute]
														  fieldSpecifier:[self centerFieldSpecifier]
														forEntityAtIndex:0
																 section:0
															 enitityData:NULL
														 fieldDescriptor:NULL
																 indexer:NULL
												   indexedFieldSpecifier:NULL];
			centerLocation = [GXLocationManager newLocationFromString:locationString];
		}
		if (centerLocation) {
			topLeftCoord = getTopLeft(topLeftCoord, centerLocation.coordinate);
			bottomRightCoord = getBottomRight(bottomRightCoord, centerLocation.coordinate);
		}
		else {
			// if the center property was set to "custom" but the coordinates were not provided, force it to the default beahvior
			centerType = GXMapCenterDefault;
		}
	}
	else if (centerType == GXMapCenterMyLocation) {
		centerLocation = userLocation;
	}
	
	if (centerType == GXMapCenterDefault) {
		centerLocation = [[CLLocation alloc] initWithLatitude:(topLeftCoord.latitude + bottomRightCoord.latitude) / 2
													longitude:(topLeftCoord.longitude + bottomRightCoord.longitude) / 2
						  ];
	}
	
	if (self.initialZoom == GXMapInitialZoomRadius) {
		NSNumber *radius = [self valueForEntityDataFieldName:[self initialZoomRadiusAttribute]
											  fieldSpecifier:[self initialZoomRadiusFieldSpecifier]
											forEntityAtIndex:0
													 section:0
												 enitityData:NULL
											 fieldDescriptor:NULL
													 indexer:NULL
									   indexedFieldSpecifier:NULL];
		CGFloat mapRadius = [radius floatValue];
		
		MKCoordinateRegion coordRegion = MKCoordinateRegionMakeWithDistance(centerLocation.coordinate, mapRadius * 2, mapRadius * 2);
		
		topLeftCoord.longitude = coordRegion.center.longitude - coordRegion.span.longitudeDelta / 2;
		topLeftCoord.latitude = coordRegion.center.latitude + coordRegion.span.latitudeDelta / 2;
		
		bottomRightCoord.longitude = coordRegion.center.longitude + coordRegion.span.longitudeDelta / 2;
		bottomRightCoord.latitude = coordRegion.center.latitude - coordRegion.span.latitudeDelta / 2;
    }
    
    MKMapRect mapRect = MKMapRectNull;
    if (topLeftCoord.latitude < bottomRightCoord.latitude || topLeftCoord.longitude > bottomRightCoord.longitude) { // Valid?
        if (centerLocation) {
            mapRect.origin = MKMapPointForCoordinate(centerLocation.coordinate);
            mapRect.size = MKMapSizeMake(0, 0);
        }
    }
    else {
        if (centerLocation) {
            MKMapPoint centerPoint = MKMapPointForCoordinate(centerLocation.coordinate);
            MKMapPoint topLeftPoint = MKMapPointForCoordinate(topLeftCoord);
            MKMapPoint bottomRightPoint = MKMapPointForCoordinate(bottomRightCoord);
            mapRect.size = MKMapSizeMake(MAX(centerPoint.x - topLeftPoint.x, bottomRightPoint.x - centerPoint.x),
                                         MAX(centerPoint.y - topLeftPoint.y, bottomRightPoint.y - centerPoint.y));
            mapRect.origin = MKMapPointMake(centerPoint.x - mapRect.size.width, centerPoint.y - mapRect.size.height);
            mapRect.size.width *= 2;
            mapRect.size.height *= 2;
            mapRect = MKMapRectIntersection(mapRect, MKMapRectWorld);
        }
        else {
            MKMapPoint bottomRightPoint = MKMapPointForCoordinate(bottomRightCoord);
            mapRect.origin = MKMapPointForCoordinate(topLeftCoord);
            mapRect.size = MKMapSizeMake(bottomRightPoint.x - mapRect.origin.x, bottomRightPoint.y - mapRect.origin.y);
        }
    }
    
    if (!MKMapRectIsNull(mapRect)) {
        _mapViewEdgePadding = mapView.layoutMargins;
        CGSize viewSize = mapView.bounds.size;
        CGFloat xEdgeInset = round(viewSize.width * 0.1f);
        CGFloat yEdgeInset = round(viewSize.height * 0.1f);
        UIEdgeInsets adjustedMapViewEdgePadding = GXUIEdgeInsetsAdd(_mapViewEdgePadding, UIEdgeInsetsMake(yEdgeInset, xEdgeInset, yEdgeInset, xEdgeInset));
        [mapView gxSetVisibleMapRect:mapRect edgePadding:adjustedMapViewEdgePadding animated:animated];
        _regionChangedSinceLastZoomToFitMapAnnotations = NO;
	}
}

- (void)updateMapViewLayoutMargins {
	UIView *mapView = [self mapView];
	if (mapView != nil) {
        mapView.layoutMargins = GXUIEdgeInsetsAdd(self.validatedExpandableEdgeInsets, UIEdgeInsetsMakeWithGXLayoutQueadDimension(self.contentRequiredEdgeInsetsToViewFrame));
	}
}

- (BOOL)updateMapViewEdgePadding:(BOOL)animated onlyIfChanged:(BOOL)onlyIfChanged {
    UIView<GXMapView> *mapView = [self mapView];
    if (mapView == nil) {
        return NO;
    }
    
    BOOL edgePaddingChanged = UIEdgeInsetsEqualToEdgeInsets(mapView.layoutMargins, _mapViewEdgePadding);
    if (onlyIfChanged && !edgePaddingChanged) {
        return NO;
    }
    
    if (!_regionChangedSinceLastZoomToFitMapAnnotations) {
        [self zoomToFitMapAnnotations:animated];
        return YES;
    }
    else if (edgePaddingChanged) {
        MKMapRect mapRect = mapView.gxVisibleMapRect;
        if (MKMapRectIsNull(mapRect) || MKMapRectEqualToRect(mapRect, MKMapRectWorld)) {
            return NO;
        }
        UIEdgeInsets diffEdgePadding = UIEdgeInsetsMake(mapView.layoutMargins.top - _mapViewEdgePadding.top,
                                                        mapView.layoutMargins.left - _mapViewEdgePadding.left,
                                                        mapView.layoutMargins.bottom - _mapViewEdgePadding.bottom,
                                                        mapView.layoutMargins.right - _mapViewEdgePadding.right);
        _mapViewEdgePadding = mapView.layoutMargins;
        [mapView gxSetVisibleMapRect:mapRect edgePadding:diffEdgePadding animated:animated];
        return YES;
    }
    else {
        return NO;
    }
}

- (BOOL)requestLocationAuthorizationIfNeeded {
	NSError *error = [GXEOiOSPermissions requestLocationPermissionIfNeededWithLocationManager:_locManager
																					whenInUse:YES
																			 fallbackToAlways:YES];
	if (error && [error hasUserInfoLocalizedDescription]) {
		GXUserInterfaceContext *uiContext = nil;
		id<GXEntityControllerProtocol> entityController = self.parentControl.context.entityController;
		if (entityController) {
			uiContext = [GXUserInterfaceContext new];
			[uiContext addUserInterfaceContextWithController:entityController.gxUserInterfaceController];
		}
		[[GXObjectsModelServices alertUIMessageService] showAlertForError:error uiContext:uiContext];
	}
	return error == nil;
}

- (void)setMapType:(GXMapType)mapType {
    [self.mapView gxSetGXMapType:mapType];
}

- (id<GXUC_MapPinAnnotation>)addAnnotationWithLocation:(CLLocationCoordinate2D)location
										   entitiyData:(id<GXEntityData>)entityData
											 indexPath:(NSIndexPath *)indexPath
										  animationKey:(nullable NSString *)animationKey
									pinImageFieldValue:(id)pinImageFieldValue
									pinImageThemeClass:(SDMapPinImageThemeClass *)pinImageThemeClass {
	@throw [NSException exceptionWithName:NSInternalInconsistencyException
                                   reason:@"Method must be overriden in subclass"
                                 userInfo:nil];
}

- (GXUCMapListAnimationEndBehavior)convertAnimationEndBehaviorFromString:(nullable NSString *)string {
	if ([string isEqualToString:@"DoNotRepeat"]) {
		return GXUCMapListAnimationEndBehaviorDoNotRepeat;
	} else if ([string isEqualToString:@"Repeat"]) {
		return GXUCMapListAnimationEndBehaviorRepeat;
	} else if ([string isEqualToString:@"Disappear"]) {
		return GXUCMapListAnimationEndBehaviorDisappear;
	}
	return GXUCMapListAnimationEndBehaviorDoNotRepeat;
}
@end

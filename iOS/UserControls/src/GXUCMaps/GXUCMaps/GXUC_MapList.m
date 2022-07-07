//
//  GXUC_MapList.m
//  GXUCMaps
//
//  Created by willy on 1/28/11.
//  Copyright 2011 Artech. All rights reserved.
//

#import "GXUC_MapList.h"
#import "GXUC_MapList+Internal.h"
#import "GXUC_MapList+GXHelpers.h"
#import "GXUC_MapList+GXProperties.h"
#import <GXUCMaps/GXUCMaps-Swift.h>
@import GXCoreUI;
@import GXFoundation;
@import GXCoreBL;
@import GXCoreModule_Common_Maps;

@interface GXUC_MapListControlTableGridItem : GXControlTableGridItem
@end

@implementation GXUC_MapList {
	id<GXControlCustomActionBarItem> _chooseMapTypeCustomActionBarItem;
	BOOL _isHandlingCalloutTap;
	BOOL _forceUpdateMapViewEdgePadding;
	GXUC_MapCustomAnnotationView *_targetSelectionAnnotationView;
	CLLocationCoordinate2D _currentSelectedLocation;
	NSMutableDictionary<NSString*, id<GXUC_MapPinAnnotation>> *_animatedAnnotations;
	BOOL _nextLoadShouldKeepMapRegion;
}

#pragma mark - Properties

- (nullable UIView *)mapView {
	return [self gridView];
}

#define kLowecaseProperty_SelectionLayer @"selectionlayer"
#define kLowecaseProperty_DirectionsLayer @"directionslayer"
#define kLowecaseProperty_AnimationsLayer @"animationslayer"
#define kLowecaseProperty_SelectedIndex @"selectedindex"

@synthesize isCalloutVisible = _isCalloutVisible;

#pragma mark - Overrides

- (instancetype)initWithLayoutElement:(id <GXLayoutElement>)layoutElement
					   dataDescriptor:(id<GXEntityDataDescriptor>)dataDescriptor
			   businessComponentLevel:(nullable GXBusinessComponentLevel *)businessComponentLevel
							controlId:(NSUInteger)controlId
							   gxMode:(GXModeType)modeType
						 indexerStack:(nullable NSArray *)indexer
						parentControl:(nullable id<GXControlContainer>)parentControl
							relations:(nullable NSDictionary *)relationsByDataElementName {
	self = [super initWithLayoutElement:layoutElement dataDescriptor:dataDescriptor
				 businessComponentLevel:businessComponentLevel controlId:controlId gxMode:modeType indexerStack:indexer
						  parentControl:parentControl relations:relationsByDataElementName];
	if (self) {
		_animatedAnnotations = [[NSMutableDictionary alloc] init];
		_nextLoadShouldKeepMapRegion = NO;
	}
	return self;
}

- (BOOL)gridViewHandlesContentInsets {
	return YES;
}

- (BOOL)gridSupportsExpandableBounds {
	return YES;
}

- (void)applyThemeClass {
	[super applyThemeClass];
	if ([self isViewLoaded]) {
		[self updateMapViewLayoutMargins];
		[self updateMapViewEdgePadding:NO onlyIfChanged:!_forceUpdateMapViewEdgePadding];
		_forceUpdateMapViewEdgePadding = NO;
	}
}

- (void)onExpandableBoundsDidChange:(UIEdgeInsets)oldEdgeInsets {
	[super onExpandableBoundsDidChange:oldEdgeInsets];
	[self updateMapViewLayoutMargins];
}

- (void)onControlFrameChanged:(CGRect)oldControlFrame {
	[super onControlFrameChanged:oldControlFrame];
	if ([self isViewLoaded] && !CGSizeEqualToSize(self.frame.size, oldControlFrame.size)) {
		_forceUpdateMapViewEdgePadding = YES;
	}
}

- (void)layoutControls {
	[super layoutControls];
	dispatch_async(dispatch_get_main_queue(), ^{
		[self updateMapViewEdgePadding:self.viewVisibleState == GXViewVisibleStateAppeared onlyIfChanged:!self->_forceUpdateMapViewEdgePadding];
		self->_forceUpdateMapViewEdgePadding = NO;
	});
}

- (void)viewDidLoad {
	[super viewDidLoad];
	[[self entityDataListProvider] load];
	_didPresentDetail = NO;
	_didUpdateUserLocation = NO;
}

- (nullable id)executeMethod:(NSString *)methodName withParameters:(NSArray *)parameters {
	if ([methodName isEqualToString:@"clear"]) {
		[self.mapView gxClear];
	}
	else if ([methodName isEqualToString:@"drawgeoline"]) {
		if (parameters.count >= 1) {
			MKPolyline *polyline = [GXUtilities polylineFromGeoLine:parameters[0]];
			if (polyline) {
				if (parameters.count == 2 && [parameters[1] isKindOfClass:NSString.class]) {
					SDMapRouteThemeClass *themeClass = [GXTheme.currentTheme themeClassForFullName:parameters[1]];
					if ([themeClass isKindOfClass:SDMapRouteThemeClass.class]) {
						polyline.routeThemeClass = themeClass;
					}
				}
				[self drawPolyline:polyline isRefreshable:false];
			}
		}
	}
	else if ([methodName isEqualToString:@"setmapcenter"]) {
		if (parameters.count > 0 && [parameters.firstObject isKindOfClass:NSString.class]) {
			CLLocationCoordinate2D newCenter = [GXUtilities coordinateFromGeoString:parameters.firstObject forType:GXDataTypeGeography];
			NSNumber *zoomLevel;
			if (parameters.count == 2) {
				zoomLevel = [GXUtilities floatNumberFromValue:parameters[1]];
			}
			[self setCenterCoordinate:newCenter withZoomLevel:zoomLevel animated:UIView.areAnimationsEnabled];
		}
	} else if ([methodName isEqualToString:@"setzoomlevel"]) {
		if (parameters.count > 0) {
			NSNumber *zoomLevel = [GXUtilities floatNumberFromValue:parameters.firstObject];
			[self setZoomLevel:zoomLevel.floatValue animated:UIView.areAnimationsEnabled];
		}
	} else if ([methodName isEqualToString:@"select"] || [methodName isEqualToString:@"deselect"]) {
		if (parameters.count > 0 && [parameters.firstObject isKindOfClass:[NSNumber class]]) {
			NSInteger index = ((NSNumber *)parameters.firstObject).integerValue;
			if (index > 0) {
				NSIndexPath *indexPath = [self.loadedInfo indexPathForItemAtAbsoluteIndex:(index - 1)];
				if (indexPath != nil) {
					if ([methodName isEqualToString:@"select"]) {
						[self selectAnnotationAtIndexPath:indexPath animated:UIView.areAnimationsEnabled];
					} else {
						[self deselectAnnotationAtIndexPath:indexPath animated:UIView.areAnimationsEnabled];
					}
				}
			}
		}
	} else {
		return [super executeMethod:methodName withParameters:parameters];
	}
	return nil;
}

- (id)valueForPropertyName:(NSString *)propertyName {
	id propValue = [super valueForPropertyName:propertyName];

	if (!propValue) {
		if ([propertyName isEqualToString:kLowecaseProperty_SelectionLayer]) {
			propValue = self.isSelectionLayerEnabled ? @"True" : @"False";
		} else if ([propertyName isEqualToString:kLowecaseProperty_DirectionsLayer]) {
			propValue = self.isDirectionsLayerEnabled ? @"True" : @"False";
		} else if ([propertyName isEqualToString:kLowecaseProperty_AnimationsLayer]) {
			propValue = self.isAnimationsLayerEnabled ? @"True" : @"False";
		} else if ([propertyName isEqualToString:kLowecaseProperty_SelectedIndex]) {
			NSInteger selectedIndex = 0;
			id<MKAnnotation> selectedAnnotation =  self.mapView.gxSelectedAnnotations.firstObject;
			if (selectedAnnotation != nil && [selectedAnnotation conformsToProtocol:@protocol(GXUC_MapPinAnnotation)]) {
				id<GXUC_MapPinAnnotation> pinAnnotation = (id<GXUC_MapPinAnnotation>)selectedAnnotation;
				selectedIndex = [self.loadedInfo absoluteIndexForIndexPath:pinAnnotation.indexPath] + 1;
			}
			propValue = [[NSNumber alloc] initWithLong: selectedIndex];
		}
	}
	
	return propValue;
}

- (BOOL)applyPropertyValue:(id)propValue toPropertyName:(NSString *)propName {
	if ([super applyPropertyValue:propValue toPropertyName:propName]) {
		return YES;
	} else if ([propValue isKindOfClass:[NSArray class]]) {
		[self executeMethod:propName withParameters:propValue];
		return YES;
	} else if ([propName isEqualToString:kLowecaseProperty_SelectionLayer]) {
		if ([propValue respondsToSelector:@selector(boolValue)]) {
			self.selectionLayerEnabled = [propValue boolValue];
			return YES;
		}
	} else if ([propName isEqualToString:kLowecaseProperty_DirectionsLayer]) {
		if ([propValue respondsToSelector:@selector(boolValue)]) {
			self.directionsLayerEnabled = [propValue boolValue];
			return YES;
		}
	} else if ([propName isEqualToString:kLowecaseProperty_AnimationsLayer]) {
		if ([propValue respondsToSelector:@selector(boolValue)]) {
			self.animationLayerEnabled = [propValue boolValue];
			return YES;
		}
	}
	return NO;
}

- (void)reloadData:(NSDictionary<NSString *, id> *)loadInfo {
	if (!_isCalloutVisible) {
		id<GXEntityDataListProvider> provider = [self entityDataListProvider];
		[self loadData:[provider numberOfLoadedEntitiesInSection:0]];
		
		if (![provider allPagesLoaded] && self.autoLoad) {
			[provider load];
		}
	}
}

- (void)viewDidAppear:(BOOL)animated {
    if ([self showMyLocation]) {
        if ([self requestLocationAuthorizationIfNeeded]) {
            [[self mapView] gxSetShowsUserLocation:YES];
        }
    }
    else if ([self center] == GXMapCenterMyLocation) {
        if ([self requestLocationAuthorizationIfNeeded]) {
#if !TARGET_OS_TV
            [_locManager startUpdatingLocation];
#else
			[_locManager requestLocation];
#endif //!TARGET_OS_TV
        }
    }
	
	if (self.isSelectionLayerEnabled) {
		[self enableSelectionLayer];
	}
    
    if (_didPresentDetail) {
        _didPresentDetail = NO;
    }
    else {
        [super viewDidAppear:animated];
    }
}

- (void)viewDidDisappear:(BOOL)animated {
    if ([self showMyLocation]) {
        [[self mapView] gxSetShowsUserLocation:NO];
    }
    else if ([self center] == GXMapCenterMyLocation) {
        [_locManager stopUpdatingLocation];
    }
	
	if (_targetSelectionAnnotationView) {
		[_targetSelectionAnnotationView removeFromSuperview];
		_targetSelectionAnnotationView = nil;
	}

    [super viewDidDisappear:animated];
}

- (id <GXActionHandler>)executeDefaultActionForEntityAtIndex:(NSUInteger)index
													 section:(NSUInteger)section
													filtered:(BOOL)filtered
										userInterfaceContext:(GXUserInterfaceContext *)uiContext {
	
	_didPresentDetail = YES;
	id <GXActionHandler> actionHandler = [super executeDefaultActionForEntityAtIndex:index
																			 section:section
																			filtered:filtered
																userInterfaceContext:uiContext];
	if (actionHandler == nil) {
		_didPresentDetail = NO;
	}
	return actionHandler;
}

- (void)unloadView {
	_mapViewEdgePadding = UIEdgeInsetsZero;
	_regionChangedSinceLastZoomToFitMapAnnotations = NO;
	_isCalloutVisible = NO;
	[super unloadView];
}

+ (Class)gxControlTableGridItemClass {
	return [GXUC_MapListControlTableGridItem class];
}

- (void)gridItem:(id <GXControlGridItem>)gridItem needsHeightAdjustment:(CGFloat)requiredHeight {
	[gridItem.gridItemView sizeToFit];
}

- (void)refresh:(BOOL)keep {
	_nextLoadShouldKeepMapRegion = keep;
	[super refresh:keep];
}

#pragma mark - Private

- (void)loadData:(NSInteger)count {
    UIView<GXMapView> *mapView = [self mapView];
	
	if (!self.isAnimationsLayerEnabled) {
		NSArray *oldAnnotations = [mapView gxAnnotations];
		if ([oldAnnotations count]) {
			oldAnnotations = [oldAnnotations filteredArrayUsingPredicate:[NSPredicate predicateWithBlock:^BOOL(id<MKAnnotation> annotation, NSDictionary *bindings) {
				return [annotation conformsToProtocol:@protocol(GXUC_MapPinAnnotation)];
			}]];
			[mapView gxRemoveAnnotations:oldAnnotations];
		}
	}
    
    NSString *locationFieldSpecifier = [self locationFieldSpecifier];
    BOOL providerIsBasedInSDT = [self.gridElement layoutElementGridDataProviderName] == nil;
    
    if ((!providerIsBasedInSDT || [locationFieldSpecifier length]) && count > 0) {
		SDMapPinImageThemeClass *pinImageThemeClass = [self pinImageThemeClass];
        NSString *locationAttribute = [self locationAttribute];
        id <GXEntityDataFieldDescriptor> locationFieldDesc = nil;
		
		GXDataType locationDataType = GXDataTypeUnknown;
		
		CLLocationCoordinate2D lastLoadedLocation = kCLLocationCoordinate2DInvalid;
		[mapView gxClearPolylines];
        for (NSUInteger i=0; i < count; i++) {
            NSArray *indexer = nil;
            id <GXEntityDataWithOverrideValues> entityData = nil;
            id value = [self valueForEntityDataFieldName:locationAttribute
                                          fieldSpecifier:locationFieldSpecifier
                                        forEntityAtIndex:i
                                                 section:0
                                             enitityData:&entityData
                                         fieldDescriptor:&locationFieldDesc
                                                 indexer:&indexer
                                   indexedFieldSpecifier:NULL];
            if (entityData == nil) {
                continue;
            }
            
            if (locationDataType == GXDataTypeUnknown) {
                locationDataType = [[GXStructureDataTypeInfo resolvedFieldInfoForFieldDescriptor:locationFieldDesc
                                                                                  fieldSpecifier:locationFieldSpecifier] entityDataFieldInfoDataType];
            }
            CLLocationCoordinate2D location = [GXUtilities coordinateFromGeoString:[value isKindOfClass:[NSString class]] ? value : nil
                                                                           forType:locationDataType];
            if (!CLLocationCoordinate2DIsValid(location)) {
                continue;
            }
            
            id pinImageFieldValue = nil;
			BOOL shouldAddAnnotation = NO;
			NSString *animationKeyValue = nil;
			
			if (self.isDirectionsLayerEnabled) {
				if (CLLocationCoordinate2DIsValid(lastLoadedLocation)) {
					[self calculateAndDrawRouteFromSourceLocation:lastLoadedLocation
											toDestinationLocation:location
												withTransportType:self.transportType
										   requestAlternateRoutes:NO];
				}
				lastLoadedLocation = location;
			} if (self.isAnimationsLayerEnabled) {
				animationKeyValue = [self animationKeyAtIndex: i];
				if (animationKeyValue == nil) {
					continue;
				}
				
				id<GXUC_MapPinAnnotation> existingAnnotation = [_animatedAnnotations objectForKey:animationKeyValue];
				if (existingAnnotation != nil) {
					// Annotation is already on the map. Should animate it's coordinate change
					NSTimeInterval animationDuration = [self animationDurationAtIndex:i];
					GXUCMapListAnimationEndBehavior animationEndBehavior = [self animationEndBehaviorAtIndex:i];
					
					[self updateWithAnnotation:existingAnnotation
									toPosition:location
								  withDuration:animationDuration
								repeatBehavior:animationEndBehavior];
					continue;
				} else {
					// This is a new annotation. Should just add it to the map
					shouldAddAnnotation = YES;
				}
			} // self.isAnimationsLayerEnabled
			NSString *pinImageName = [self pinImageName];
			NSString *pinImageFieldSpecifier = [self pinImageFieldSpecifier];
			if (!providerIsBasedInSDT || [pinImageFieldSpecifier length]) {
				pinImageFieldValue = [self valueForEntityDataFieldName:self.pinImageAttribute
														fieldSpecifier:pinImageFieldSpecifier
													  forEntityAtIndex:i
															   section:0
														   enitityData:&entityData
													   fieldDescriptor:NULL
															   indexer:&indexer
												 indexedFieldSpecifier:NULL];
			}
			if (!pinImageFieldValue && pinImageName) {
				pinImageFieldValue = pinImageName;
			}
			
			NSIndexPath *indexPath = [NSIndexPath indexPathForGXEntityDataIndex:i inSection:0];
			id<GXUC_MapPinAnnotation> newAnnotation = [self addAnnotationWithLocation:location
																		  entitiyData:entityData
																			indexPath:indexPath
																		 animationKey:animationKeyValue
																   pinImageFieldValue:pinImageFieldValue
																   pinImageThemeClass:pinImageThemeClass];
			
			if (shouldAddAnnotation && animationKeyValue != nil && animationKeyValue.length > 0) {
				[_animatedAnnotations setObject:newAnnotation forKey:animationKeyValue];
			}
        }
    }
	
	if (self.initialZoom != GXMapInitialZoomNoZoom && !_nextLoadShouldKeepMapRegion) {
    	[self zoomToFitMapAnnotations];
	}
	
	if (_nextLoadShouldKeepMapRegion) {
		_nextLoadShouldKeepMapRegion = NO;
	}
}

- (void)enableSelectionLayer {
	if (_targetSelectionAnnotationView == nil) {
		_targetSelectionAnnotationView = [[GXUC_MapCustomAnnotationView alloc] init];
		[_targetSelectionAnnotationView gxSetImageFromEntityDataFieldValue:self.selectionTargetImageName themeClass:self.selectionTargetImageThemeClass];
	}
	_targetSelectionAnnotationView.center = CGPointMake(CGRectGetMidX(self.mapView.bounds), CGRectGetMidY(self.mapView.bounds));
	[self.mapView addSubview:_targetSelectionAnnotationView];
}

- (void)disableSelectionLayer {
	if (_targetSelectionAnnotationView) {
		[_targetSelectionAnnotationView removeFromSuperview];
	}
}

- (NSString *)selectedLocationGeoPoint {
	if (self.isSelectionLayerEnabled && CLLocationCoordinate2DIsValid(_currentSelectedLocation) ) {
		return [GXEntityHelper geoPointStringFromDoubleWithLongitude:_currentSelectedLocation.longitude
															latitude:_currentSelectedLocation.latitude];
	} else {
		return WKTGeometry.emptyValue;
	}
}

- (void)resetSelectedLocation {
	_currentSelectedLocation = kCLLocationCoordinate2DInvalid;
}

- (void)setCenterCoordinate:(CLLocationCoordinate2D)newCenterCoordinate withZoomLevel:(NSNumber *)zoomLevel animated:(BOOL)animated {
	if (CLLocationCoordinate2DIsValid(newCenterCoordinate)) {
		if (zoomLevel != nil) {
			[self.mapView gxSetCenterCoordinate:newCenterCoordinate withZoomLevel:zoomLevel.floatValue animated:animated];
		} else {
			[self.mapView gxSetCenterCoordinate:newCenterCoordinate animated:animated];
		}
	}
}

- (void)setZoomLevel:(float)zoomLevel animated:(BOOL)animated {
	[self.mapView gxSetZoomLevel:zoomLevel animated:animated];
}

- (void)selectAnnotationAtIndexPath:(NSIndexPath *)indexPath animated:(BOOL)animated {
	[self.mapView gxSelectAnnotationAtIndexPath:indexPath animated:animated];
}

- (void)deselectAnnotationAtIndexPath:(NSIndexPath *)indexPath animated:(BOOL)animated {
	[self.mapView gxDeselectAnnotationAtIndexPath:indexPath animated:animated];
}

- (BOOL)executeDefaultActionIfPossibleForAnnotation:(id<GXUC_MapPinAnnotation>)annotation {
	NSUInteger pIndexPathRow = annotation.indexPath.gxEntityDataIndex;
	NSUInteger pIndexPathSection = annotation.indexPath.gxEntityDataSection;
	if ([self canExecuteDefaultActionForEntityAtIndex:pIndexPathRow section:pIndexPathSection]) {
		GXUserInterfaceContext *uiContext = [GXUserInterfaceContextHelper newUserInterfaceContextForControl:self withSender:nil];
		[self executeDefaultActionForEntityAtIndex:pIndexPathRow section:pIndexPathSection userInterfaceContext:uiContext];
	}
	return true;
}

- (BOOL)canShowCalloutForAnnotation:(id<GXUC_MapPinAnnotation>)annotation {
	NSUInteger pIndexPathRow = annotation.indexPath.gxEntityDataIndex;
	NSUInteger pIndexPathSection = annotation.indexPath.gxEntityDataSection;
	NSString *layoutName = [self layoutGridItemNameForEntityData:annotation.entityData atIndex:pIndexPathRow section:pIndexPathSection];
	id<GXLayoutElementTable> tableLayout = [self.gridElement layoutElementRootElementWithLayoutName:layoutName];
	
	return ![GXLayoutHelper isLayoutElementTableEmpty:tableLayout];
}

- (void)fireSelectionChangedEvent {
	[self fireControlEvent:GXControlGridEventNameSelectionChanged userInterfaceContext:nil];
}

- (void)removeAnimatedAnnotationWithKey:(NSString *)animationKey {
	id<GXUC_MapPinAnnotation> annotation = _animatedAnnotations[animationKey];
	if (annotation != nil) {
		_animatedAnnotations[animationKey] = nil;
		[self.mapView gxRemoveAnnotation:annotation];
	}
}

#pragma mark - Memory management

- (void)dealloc {
	[_locManager stopUpdatingLocation];
}

#pragma mark - GXControlWithActionBarItems

- (nullable id<GXControlCustomActionBarItem>)chooseMapTypeCustomActionBarItem {
	if (!self.canChooseMapType) {
		return nil;
	}
	if (_chooseMapTypeCustomActionBarItem == nil) {
		typeof(self) __weak wself = self;
		void(^actionHandler)(id _Nullable) = ^(id _Nullable sender) {
			typeof(wself) __strong sself = wself;
			[sself changeMapTypeButtonTapped:sender];
		};
		Class selfClass = [GXUC_MapList class];
		UIImage * _Nullable (^imageLoader)(void) = ^{
			return [UIImage imageNamed:@"mapIcon" inBundle:[NSBundle bundleForClass:selfClass] compatibleWithTraitCollection:nil];
		};
		NSString *controlName = [self.controlName stringByAppendingString:@"_GXChooseMapType"];
		_chooseMapTypeCustomActionBarItem = [GXControlActionBarItemHelper customActionBarItemControlWithParentControl:self
																										  controlName:controlName
																								   positionDescriptor:nil
																								 barButtonItemBuilder:
											 ^UIBarButtonItem<GXControlActionBarItemView> * _Nonnull() {
												 return [[GXControlActionBarButtonItem alloc] initWithImage:imageLoader()
																									  style:UIBarButtonItemStylePlain
																									 target:wself
																									 action:@selector(changeMapTypeButtonTapped:)];
											 }
																								   alertActionBuilder:NULL
																						  segmentedControlItemBuilder:
											 ^GXSegmentedControlItem<GXControlActionBarItemView> * _Nonnull() {
												 GXSegmentedControlActionBarItemView *item = [GXSegmentedControlActionBarItemView alloc];
												 item = [item initWithHandler:^(GXSegmentedControlItem *senderItem) {
													 actionHandler(senderItem.segmentedControl);
												 }];
												 item.image = imageLoader();
												 return item;
											 }];
	}
	return _chooseMapTypeCustomActionBarItem;
}

- (nullable NSArray<id<GXControlActionBarItem>> *)controlActionBarItems {
	NSArray<id<GXControlActionBarItem>> *controlActionBarItems = super.controlActionBarItems;
	id<GXControlCustomActionBarItem> chooseMapTypeCustomActionBarItem = self.chooseMapTypeCustomActionBarItem;
	if (chooseMapTypeCustomActionBarItem == nil) {
		return controlActionBarItems;
	}
	else if (controlActionBarItems.count > 0) {
		return [controlActionBarItems arrayByAddingObject:chooseMapTypeCustomActionBarItem];
	}
	else {
		return @[chooseMapTypeCustomActionBarItem];
	}
}

- (void)enumerateControlActionBarItemsWithBlock:(void (^)(id <GXControlActionBarItem> item, BOOL *stop))block
										options:(GXControlWithActionBarItemsEnumerationOptions)options {
	
	__block BOOL stop_ = NO;
	[super enumerateControlActionBarItemsWithBlock:^(id<GXControlActionBarItem> _Nonnull item, BOOL * _Nonnull stop) {
		block(item, stop);
		stop_ = *stop;
	} options:options];
	if (!stop_ && self.canChooseMapType) {
		BOOL existingOnly = (options & GXControlWithActionBarItems_ExistingOnly) == GXControlWithActionBarItems_ExistingOnly;
		id<GXControlCustomActionBarItem> chooseMapTypeCustomActionBarItem = existingOnly ? _chooseMapTypeCustomActionBarItem : self.chooseMapTypeCustomActionBarItem;
		if (chooseMapTypeCustomActionBarItem) {
			block(chooseMapTypeCustomActionBarItem, &stop_);
		}
	}
}

#pragma mark - Actions

- (NSArray<NSString *> *)newMapTypesTitles { // order should match mapTypeFromIndex:
	return @[[GXResources translationFor:@"GXM_Standard"], [GXResources translationFor:@"GXM_Satellite"], [GXResources translationFor:@"GXM_Hybrid"]];
}

- (GXMapType)mapTypeFromIndex:(NSInteger)index {
	switch (index) {
		case 0:
			return GXMapStandard;
		case 1:
			return GXMapSatellite;
		case 2:
			return GXMapHybrid;
		default:
#ifdef DEBUG
			NSAssert(NO, @"Unknown map type");
#endif
			break;
	}
	return GXMapStandard;
}

- (void)changeMapTypeButtonTapped:(nullable id)sender {
	GXUC_MapList __weak *unretainedSelf = self;
	NSString *alertTitle = [GXResources translationFor:@"GXM_SelectMapType"];
	NSString *cancelTitle = [GXResources translationFor:@"GXM_cancel"];
	NSArray<NSString *> *typesArrayTitles = [self newMapTypesTitles];
	UIAlertController *alertController = [UIAlertController alertControllerWithTitle:alertTitle
																			 message:nil
																	  preferredStyle:UIAlertControllerStyleActionSheet];
	[typesArrayTitles enumerateObjectsUsingBlock:^(NSString* typeTytpe, NSUInteger idx, BOOL *stop) {
		GXMapType mapType = [self mapTypeFromIndex:idx];
		UIAlertAction *action = [UIAlertAction actionWithTitle:typeTytpe style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
			GXUC_MapList *retainedSelf = unretainedSelf;
			[retainedSelf setMapType:mapType];
		}];
		[alertController addAction:action];
	}];
	[alertController addAction:[UIAlertAction actionWithTitle:cancelTitle style:UIAlertActionStyleCancel handler:NULL]];
#if TARGET_OS_iOS
	if (CURRENT_DEVICE_IPAD) {
		alertController.modalPresentationStyle = UIModalPresentationPopover;
		if ([sender isKindOfClass:[UIBarButtonItem class]]) {
			alertController.popoverPresentationController.barButtonItem = sender;
		}
	}
#endif //!TARGET_OS_iOS
	UIViewController *presentingViewController = self.context.entityController.gxUserInterfaceController;
	[presentingViewController presentViewController:alertController animated:YES completion:NULL];
}

#pragma mark - Events

- (void)fireLocationSelectionEventWithName:(NSString *)eventName {
	if ([eventName isEqualToString:GXControlEventNameControlValueChanged]) {
		_currentSelectedLocation = self.mapView.gxRegionCenter;
	}
	[self fireControlEvent:eventName
	  userInterfaceContext:nil
			withEntityData:nil
				parameters:[NSArray arrayWithObject:[self selectedLocationGeoPoint]]];
}

#pragma mark - CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations {
	BOOL zoomMap = !_locManager;
	_lastLocation = [locations lastObject];
	if (zoomMap && [self isViewLoaded] && self.initialZoom != GXMapInitialZoomNoZoom) {
		[self zoomToFitMapAnnotations];
	}
}

@end



@implementation GXUC_MapListControlTableGridItem

- (BOOL)applyHighlighOnApplyThemeClass:(BOOL)borderOrBgImageViewsChanged {
	return YES;
}

@end


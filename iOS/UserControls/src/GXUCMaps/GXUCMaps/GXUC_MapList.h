//
//  GXUC_MapList.h
//  GXUCMaps
//
//  Created by willy on 1/28/11.
//  Copyright 2011 Artech. All rights reserved.
//

@import MapKit;
@import CoreLocation;
@import GXCoreUI;

NS_ASSUME_NONNULL_BEGIN

@interface GXUC_MapList : GXControlGridBase <GXActionControllerDelegate, CLLocationManagerDelegate>
{
	BOOL _didPresentDetail;
	BOOL _didUpdateUserLocation;
}

@property(nullable, nonatomic, strong, readonly) UIView<GXMapView> *mapView;

@property(nonatomic, assign, readonly) BOOL isCalloutVisible;

@end

NS_ASSUME_NONNULL_END

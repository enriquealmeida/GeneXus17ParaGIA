//
//  GXUC_MapList+Routing.swift
//  GXUCMaps-iOS
//
//  Created by José Echagüe on 6/14/18.
//  Copyright © 2018 GeneXus. All rights reserved.
//

import GXCoreUI
import GXCoreModule_Common_Maps

private var GXSDMapsDefaultRouteThemeClassKey = "GXSDMapsDefaultRouteThemeClassKey"

extension GXUC_MapList {
	
	//MARK: - GXProperties
	
	@objc public var routeThemeClass: SDMapRouteThemeClass? {
		get {
			if let routeThemeClass = objc_getAssociatedObject(self, &GXSDMapsDefaultRouteThemeClassKey) as? SDMapRouteThemeClass {
				return routeThemeClass
			} else {
				let routeThemeClassName: String = self.properties.getPropertyValueString("@SDMapsDefaultRouteClass") ?? ""
				guard routeThemeClassName.count > 0,
					let tmpRouteThemeClass = GXTheme.current()?.themeClass(forFullName: routeThemeClassName) as? SDMapRouteThemeClass else {
						return nil
				}
				objc_setAssociatedObject(self, &GXSDMapsDefaultRouteThemeClassKey, tmpRouteThemeClass, .OBJC_ASSOCIATION_RETAIN)
				return tmpRouteThemeClass
			}
		}
	}
	
	@objc public var transportType: GXTransportType {
		get {
			guard let transportTypeStr = self.properties.getPropertyValueString("@SDMapsTransportType") else {
				return .any
			}
			return GXMapUtilities.convertGXTransportType(fromRawValue: transportTypeStr)
		}
	}
	
	//MARK: - Directions
	
	@objc open func calculateDirections(fromSourceLocation source: CLLocationCoordinate2D,
							 toDestinationLocation destination: CLLocationCoordinate2D,
							 withTransportType transportType: GXTransportType,
							 requestAlternateRoutes: Bool,
							 completionHandler completionBlock: @escaping ([GXMapRoute]?, Error?) -> Swift.Void) {
		GXActionExObjMapsHandler.calculateDirections(fromSourceLocation: source,
															  toDestinationLocation: destination,
															  withTransportType: transportType,
															  requestAlternateRoutes: requestAlternateRoutes,
															  completionHandler: completionBlock)
	}
	
	@objc open func drawPolyline(_ polyline: GXPolyline, isRefreshable: Bool) {
		NSException.init(name: NSExceptionName.internalInconsistencyException,
						 reason: "Method must be overriden in subclass",
						 userInfo: nil).raise()
	}
	
	@objc open func calculateAndDrawRoute(fromSourceLocation source: CLLocationCoordinate2D,
									toDestinationLocation destination: CLLocationCoordinate2D,
									withTransportType transportType: GXTransportType,
									requestAlternateRoutes: Bool) {
		self.calculateDirections(fromSourceLocation: source,
							toDestinationLocation: destination,
							withTransportType: transportType,
							requestAlternateRoutes: requestAlternateRoutes) { (routes, error) in
								if let firstRoute = routes?.first {
									self.drawPolyline(firstRoute.gxPolyline, isRefreshable: true)
								}
		}
	}
}

//
//  GXEOMercadoPagoExtensionLibrary.swift
//
//  Copyright © 2018 GeneXus. All rights reserved.
//

import Foundation
import GXCoreBL

@objc(GXEOMercadoPagoExtensionLibrary)
class GXEOMercadoPagoExtensionLibrary: NSObject, GXExtensionLibraryProtocol {
	func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXActionExternalObjectHandler.register(GXActionExObjMercadoPagoHandler.self,
											   forExternalObjectName: "MercadoPago.MercadoPagoCheckout")
	}
}

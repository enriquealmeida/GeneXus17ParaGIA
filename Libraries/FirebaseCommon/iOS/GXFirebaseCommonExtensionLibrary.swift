//
//  GXFirebaseCommonExtensionLibrary.swift
//

import GXCoreBL
import FirebaseCore

@objc(GXFirebaseCommonExtensionLibrary)
public class GXFirebaseCommonExtensionLibrary: NSObject, GXExtensionLibraryProtocol {

	public func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		FirebaseApp.configure()
	}
}

//
//  ToggleButtonGroupExtensionLibrary.swift
//

import GXCoreBL
import GXCoreUI

@objc(ToggleButtonGroupExtensionLibrary)
public class ToggleButtonGroupExtensionLibrary: NSObject, GXExtensionLibraryProtocol {

	public func initializeExtensionLibrary(withContext context: GXExtensionLibraryContext) {
		GXControlFactory.register(controlClass: ToggleButtonGroupControl.self, forUserControlType: "ToggleButtonGroup")
	}
}


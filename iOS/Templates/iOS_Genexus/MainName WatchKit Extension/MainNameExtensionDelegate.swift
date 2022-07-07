//
//  $Main.WatchExtensionDelegateClassName$.swift
//  $Main.Name$ WatchKit Extension
//

import WatchKit
import Foundation
import GXWKExtension

class $Main.WatchExtensionDelegateClassName$: GXWKExtension.ExtensionDelegate {

$if(Main.WatchOSMainData.iOSHasExtensionLibrariesInUse)$
	private lazy var gxapplicationExtensionLibraries_: [GXExtensionLibraryProtocol] = {
		var moduleClasses: [String] = []
$Main.WatchOSMainData.iOSExtensionLibrariesInUseModuleClasses:{moduleClassName|			moduleClasses.append("$moduleClassName$")};separator="\n"$
		return moduleClasses.map { (moduleClassName) in
			let extLibClass = NSClassFromString(moduleClassName) as! NSObject.Type
			return extLibClass.init() as! GXExtensionLibraryProtocol
		};
	}()

	override public var gxapplicationExtensionLibraries: [GXExtensionLibraryProtocol] {
		return gxapplicationExtensionLibraries_
	}

$endif$
	open func gxDynamicFrameworksReferenceDummy() {
$Main.WatchOSExtensionBridgingHeaderFrameworks:{frameworkName |			print($frameworkName$VersionNumber)}; separator = "\n"$
	}
}

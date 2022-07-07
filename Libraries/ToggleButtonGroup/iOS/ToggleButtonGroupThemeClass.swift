//
//  ToggleButtonGroupThemeClass.swift
//

import Foundation
import GXObjectsModel

extension GXStyleClass.PropertyName {
	static let selectedForeColor = Self("ToggleButtonGroupSelectedForeColor")
	static let unselectedForeColor = Self("ToggleButtonGroupUnselectedForeColor")
	static let selectedBackgroundColor = Self("ToggleButtonGroupSelectedBackColor")
	static let unselectedBackgroundColor = Self("ToggleButtonGroupUnselectedBackColor")
}

fileprivate let toggleControlStyleProperties: [GXStyleClass.PropertyName] = [.selectedForeColor, .unselectedForeColor, .selectedBackgroundColor, .unselectedBackgroundColor]

fileprivate func metadataPropName(from stylePropName: GXStyleClass.PropertyName) -> String {
	switch stylePropName {
	case .selectedForeColor, .unselectedForeColor, .selectedBackgroundColor, .unselectedBackgroundColor:
		return stylePropName.rawValue // in these cases property names match metadata names
	default:
		fatalError()
	}
}

@objc(ToggleButtonGroupThemeClass)
public class ToggleButtonGroupThemeClass: GXThemeClassBase {
	
	var selectedForeColor: UIColor? {
		GXStyleClassHelper.typedProperty(.selectedForeColor, from: self, resolvingToDefaultWith: self)
	}
	
	var unselectedForeColor: UIColor? {
		GXStyleClassHelper.typedProperty(.unselectedForeColor, from: self, resolvingToDefaultWith: self)
	}
	
	var selectedBackgroundColor: UIColor? {
		GXStyleClassHelper.typedProperty(.selectedBackgroundColor, from: self, resolvingToDefaultWith: self)
	}
	
	var unselectedBackgroundColor: UIColor? {
		GXStyleClassHelper.typedProperty(.unselectedBackgroundColor, from: self, resolvingToDefaultWith: self)
	}
	
	@objc(colorForPropertyName:fromStyleClass:resolvingToDefaultWith:)
	public final class func color(for propName: GXStyleClass.PropertyName, from styleClass: GXStyleClass?, resolvingToDefaultWith resolver: GXStyleClassPropertyDefaultResolver?) -> UIColor? {
		guard toggleControlStyleProperties.contains(propName) else {
			return nil
		}
		return GXStyleClassHelper.typedProperty(propName, from: styleClass, resolvingToDefaultWith: resolver)
	}

	
	override public class func loadSpecificPropertiesValues(byName propertyValuesByName: NSMutableDictionary?,
															fromMetadata metadata: [String : Any],
															forStyleObjectType styleObjectType: GXObjectType) -> NSMutableDictionary? {
		
		var props = propertyValuesByName
		let loadColorProperty: (GXStyleClass.PropertyName) -> Void = { stylePropName in
			let metadataName = metadataPropName(from: stylePropName)
			if let color = GXStyleClassHelper.loadColorProperty(fromMetadata: metadata[metadataName], forStyleObjectType: styleObjectType) {
				if props == nil { props = NSMutableDictionary() }
				props![stylePropName] = color
			}
		}
		toggleControlStyleProperties.forEach(loadColorProperty)
		return props
	}
}

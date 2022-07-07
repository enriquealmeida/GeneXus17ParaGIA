//
//  ToggleButtonGroupControl.swift
//

class ToggleButtonGroupControl: GXControlRadioButton {
	
	override class var enumValuesPropertyName: String {
		"@ToggleButtonGroupValueRangeID"
	}
	
	override var allowsDeselecting: Bool { true }
	
	override func selectedForeColor(styleClass: GXStyleClass?) -> UIColor? {
		let resolver = self.styleClassPropertyDefaultResolver(for: styleClass)
		return ToggleButtonGroupThemeClass.color(for: .selectedForeColor, from: styleClass, resolvingToDefaultWith: resolver)
	}
	
	override func selectedBackgroundColor(styleClass: GXStyleClass?) -> UIColor? {
		let resolver = self.styleClassPropertyDefaultResolver(for: styleClass)
		return ToggleButtonGroupThemeClass.color(for: .selectedBackgroundColor, from: styleClass, resolvingToDefaultWith: resolver)
	}
	
	override func unselectedForeColor(styleClass: GXStyleClass?) -> UIColor? {
		let resolver = self.styleClassPropertyDefaultResolver(for: styleClass)
		return ToggleButtonGroupThemeClass.color(for: .unselectedForeColor, from: styleClass, resolvingToDefaultWith: resolver)
	}
	
	override func unselectedBackgroundColor(styleClass: GXStyleClass?) -> UIColor? {
		let resolver = self.styleClassPropertyDefaultResolver(for: styleClass)
		return ToggleButtonGroupThemeClass.color(for: .unselectedBackgroundColor, from: styleClass, resolvingToDefaultWith: resolver)
	}

}

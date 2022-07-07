//
//  GXUCRelativeTimerHelper.swift
//  GXUCRelativeTimer-iOS
//
//  Created by José Echagüe on 7/25/18.
//  Copyright © 2018 GeneXus. All rights reserved.
//

public class GXControlRelativeTimerHelper {
	
	public class func format(from value: String?) -> DateComponentsFormatter.UnitsStyle {
		switch value {
		case "Positional":
			return .positional
		case "Abbreviated":
			return .abbreviated
		case "Short":
			return .short
		case "Full":
			return .full
		case "SpelledOut":
			return .spellOut
		default:
			return .positional
		}
	}
	
	public class func units(from value: String?) -> NSCalendar.Unit {
		var units = NSCalendar.Unit.init(rawValue: 0)
		if let value = value {
			for unitChar in value {
				switch unitChar {
				case "s":
					units.insert(.second)
				case "m":
					units.insert(.minute)
				case "h":
					units.insert(.hour)
				case "d":
					units.insert(.day)
				case "w":
					units.insert(.weekOfMonth)
				case "M":
					units.insert(.month)
				case "Y":
					units.insert(.year)
				default:
					break
				}
			}
		}
		return units.isEmpty ? .hour : units
	}
	
	public class func relativeTimerCountingType(from value: String?) -> RelativeTimerCountingType {
		switch value {
		case "Auto":
			return .auto
		case "Down":
			return .down
		case "Up":
			return .up
		default:
			return .auto
		}
	}
}

public enum RelativeTimerCountingType {
	case auto
	case down
	case up
}


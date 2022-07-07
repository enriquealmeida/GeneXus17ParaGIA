//
//  GXUCRelativeTimer.swift
//  GXUCRelativeTimer
//
//  Created by José Echagüe on 8/3/17.
//  Copyright © 2017 GeneXus. All rights reserved.
//

import GXCoreUI

@objc(GXControlRelativeTimer)
public class GXControlRelativeTimer: GXControlWithLabelSingleEditorViewBase {
	
	fileprivate static let EventTimerStatusChanged = "TimerStatusChanged"
	
	fileprivate struct EventTimerStatusChangedState {
		var hasFiredEventForZeroSeconds = false // to prevent the event from firing twice because of timeInterval accuracy.
		var hasFiredEventForMinSeconds = false
		var hasFiredEventForMaxSeconds = false
	}
	
	fileprivate var eventTimerStatusChangedState : EventTimerStatusChangedState
	
	private func invalidateTimerAndResumeTimerIfCurrentStateIsValid(invalidateEventStatusChangedState: Bool = false, resumeAsync: Bool = true) {
		if invalidateEventStatusChangedState {
			self.eventTimerStatusChangedState = EventTimerStatusChangedState()
		}
		self.suspendTimer()
		if (resumeAsync) {
			DispatchQueue.main.async { [weak self] in
				self?.resumeTimerIfCurrentStateIsValid()
			}
		}
		else {
			self.resumeTimerIfCurrentStateIsValid()
		}
	}
	
	// MARK: - Private Variables

	#if !os(watchOS)
	private class GXDateComponentsFormatter: DateComponentsFormatter {
		override func string(for obj: Any?) -> String? {
			guard let result = super.string(for: obj) else {
				return nil
			}
			if self.unitsStyle == .positional, self.zeroFormattingBehavior == .pad, !result.isEmpty, self.allowedUnits.intersection([.hour, .minute, .second]) == self.allowedUnits {
				if result.count == 1 || result.dropFirst().first == ":" {
					return "0".appending(result)
				}
			}
			return result
		}
	}
	
    private var dateFormatter: GXDateComponentsFormatter {
		let unitsStyle = self.format
		let allowedUnits = self.units
		let mostRepresentativeOnly = self.mostRepresentativeOnly
		let tKey = "GXControlRelativeTimerDateComponentsFormatter_format:\(unitsStyle.rawValue)_units:\(allowedUnits.rawValue)_mro:\(mostRepresentativeOnly)"
		let threadDictionary = Thread.current.threadDictionary
		var dateFormatter: GXDateComponentsFormatter!
		dateFormatter = threadDictionary[tKey] as? GXDateComponentsFormatter
		if dateFormatter == nil {
			dateFormatter = GXDateComponentsFormatter()
			dateFormatter.unitsStyle = unitsStyle
			dateFormatter.allowedUnits = allowedUnits
			if (mostRepresentativeOnly) {
				dateFormatter.maximumUnitCount = 1
				dateFormatter.zeroFormattingBehavior = .dropLeading
			}
			else if (dateFormatter.unitsStyle == .positional) {
				dateFormatter.zeroFormattingBehavior = .pad
			}
			threadDictionary[tKey] = dateFormatter
		}
		return dateFormatter
	}
	#endif
	
	private var timer: Timer? {
		didSet {
			if self.timer != oldValue {
				oldValue?.invalidate()
			}
		}
	}
	
	#if os(watchOS)
	private let interfaceObjectUCPrefixLabel: WKInterfaceLabel
	private let interfaceObjectUCTimer: WKInterfaceTimer
	private let interfaceObjectUCSuffixLabel: WKInterfaceLabel
	private var interfaceObjectUCMinMaxTextLabel: WKInterfaceLabel? {
		guard let displayState = self.interfaceObjectsDisplayState, displayState == .showMinSeconds || displayState == .showMaxSeconds else {
			return nil
		}
		return self.interfaceObjectUCPrefixLabel
	}
	#else
	private var relativeTimeLabel: UILabel? {
		get {
			return self.loadedEditorView as? UILabel
		}
	}
	#endif
	
	// MARK: - Public properties
	
	let format: DateComponentsFormatter.UnitsStyle

	let units: NSCalendar.Unit
	
	let mostRepresentativeOnly: Bool
	
	public let countingType: RelativeTimerCountingType
	
	public var nonEmptyDateEntityDataFieldValue: Date? {
		if let date = self.entityDataFieldValue as? Date, !date.isGxEmpty() {
			return date
		}
		return nil
	}
	
	public var prefixText: String {
		didSet {
			let changed = oldValue != self.prefixText
			#if !os(watchOS)
			if let reuseContext = self.relativeTimerReuseContext {
				reuseContext.restoreLayoutElementPrefixText = false
				if changed {
					reuseContext.shouldInvalidateTimerAndResumeTimer = true
				}
			}
			#endif
			if changed {
				#if !os(watchOS)
				self.invalidateTimerAndResumeTimerIfCurrentStateIsValid()
				#else
				if let displayState = self.interfaceObjectsDisplayState, displayState == .showDate || displayState == .showDateTimeIntervalZero {
					self.interfaceObjectUCPrefixLabel.setText(self.suffixText)
				}
				#endif
			}
		}
	}
	
	public var suffixText: String {
		didSet {
			let changed = oldValue != self.suffixText
			#if !os(watchOS)
			if let reuseContext = self.relativeTimerReuseContext {
				reuseContext.restoreLayoutElementSuffixText = false
				if changed {
					reuseContext.shouldInvalidateTimerAndResumeTimer = true
				}
				return
			}
			#endif
			if changed {
				#if !os(watchOS)
				self.invalidateTimerAndResumeTimerIfCurrentStateIsValid()
				#else
				if let displayState = self.interfaceObjectsDisplayState, displayState == .showDate || displayState == .showDateTimeIntervalZero {
					self.interfaceObjectUCSuffixLabel.setText(self.suffixText)
				}
				#endif
			}
		}
	}
	
	public var maxSeconds: Double {
		didSet {
			if self.maxSeconds < 0 {
				self.maxSeconds = 0
			}
			let changed = oldValue != self.maxSeconds
			if changed {
				self.eventTimerStatusChangedState.hasFiredEventForMaxSeconds = false
			}
			#if !os(watchOS)
			if let reuseContext = self.relativeTimerReuseContext {
				reuseContext.restoreLayoutElementMaxSeconds = false
				if changed {
					reuseContext.shouldInvalidateTimerAndResumeTimer = true
				}
				return
			}
			#endif
			if changed {
				self.invalidateTimerAndResumeTimerIfCurrentStateIsValid()
			}
		}
	}
	
	public var maxText: String {
		didSet {
			let changed = oldValue != self.maxText
			#if !os(watchOS)
			if let reuseContext = self.relativeTimerReuseContext {
				reuseContext.restoreLayoutElementMaxText = false
				if changed {
					reuseContext.shouldInvalidateTimerAndResumeTimer = true
				}
				return
			}
			#endif
			if changed {
				#if !os(watchOS)
				self.invalidateTimerAndResumeTimerIfCurrentStateIsValid()
				#else
				if self.interfaceObjectsDisplayState == .showMaxSeconds {
					self.interfaceObjectUCMinMaxTextLabel?.setText(self.maxText)
				}
				#endif
			}
		}
	}
	
	public var minSeconds: Double {
		didSet {
			if self.minSeconds < 0 {
				self.minSeconds = 0
			}
			let changed = oldValue != self.minSeconds
			if changed {
				self.eventTimerStatusChangedState.hasFiredEventForMinSeconds = false
			}
			#if !os(watchOS)
			if let reuseContext = self.relativeTimerReuseContext {
				reuseContext.restoreLayoutElementMinSeconds = false
				if changed {
					reuseContext.shouldInvalidateTimerAndResumeTimer = true
				}
				return
			}
			#endif
			if changed {
				self.invalidateTimerAndResumeTimerIfCurrentStateIsValid()
			}
		}
	}
	
	public var minText: String {
		didSet {
			let changed = oldValue != self.minText
			#if !os(watchOS)
			if let reuseContext = self.relativeTimerReuseContext {
				reuseContext.restoreLayoutElementMinText = false
				if changed {
					reuseContext.shouldInvalidateTimerAndResumeTimer = true
				}
				return
			}
			#endif
			if changed {
				#if !os(watchOS)
				self.invalidateTimerAndResumeTimerIfCurrentStateIsValid()
				#else
				if self.interfaceObjectsDisplayState == .showMinSeconds {
					self.interfaceObjectUCMinMaxTextLabel?.setText(self.minText)
				}
				#endif
			}
		}
}
	
	// MARK: - Init & Deinit
	
	#if os(watchOS)
	@objc public required init(interfaceObject: WKInterfaceObject, layoutElement: GXLayoutElementData, controlId: UInt, gxMode modeType: GXModeType, fieldDescriptor: GXEntityDataFieldDescriptor, indexerStack indexer: [Any]?, parentControl: GXControlContainer)
	{
		let customControlProperties = layoutElement.layoutElementDataCustomProperties
		self.format = GXControlRelativeTimerHelper.format(from: customControlProperties.getPropertyValueString("@SDRelativeTimerFormat"))
		self.units = GXControlRelativeTimerHelper.units(from: customControlProperties.getPropertyValueString("@SDRelativeTimerUnits"))
		self.mostRepresentativeOnly = customControlProperties.getPropertyValueBool("@SDRelativeTimerMostRepresentativeUnitOnly")
		self.countingType = GXControlRelativeTimerHelper.relativeTimerCountingType(from: customControlProperties.getPropertyValueString("@SDRelativeTimerCountingType"))
		self.prefixText = type(of: self).layoutElementPrefixTextFrom(customControlProperties)
		self.suffixText = type(of: self).layoutElementSuffixTextFrom(customControlProperties)
		self.minText = type(of: self).layoutElementMinTextFrom(customControlProperties)
		self.maxText = type(of: self).layoutElementMaxTextFrom(customControlProperties)
		self.minSeconds = type(of: self).layoutElementMinSecondsFrom(customControlProperties)
		self.maxSeconds = type(of: self).layoutElementMaxSecondsFrom(customControlProperties)
		self.eventTimerStatusChangedState = EventTimerStatusChangedState()
		let interfaceObjectsProvider = parentControl.controlInterfaceObjectsProvider!
		self.interfaceObjectUCPrefixLabel = interfaceObjectsProvider.gxControlUserControlInterfaceObject(for: layoutElement.layoutElementControlName, objectName: "Prefix") as! WKInterfaceLabel
		self.interfaceObjectUCTimer = interfaceObjectsProvider.gxControlUserControlInterfaceObject(for: layoutElement.layoutElementControlName, objectName: "Timer") as! WKInterfaceTimer
		self.interfaceObjectUCSuffixLabel = interfaceObjectsProvider.gxControlUserControlInterfaceObject(for: layoutElement.layoutElementControlName, objectName: "Suffix") as!  WKInterfaceLabel
		super.init(interfaceObject: interfaceObject, layoutElement: layoutElement, controlId: controlId, gxMode: modeType, fieldDescriptor: fieldDescriptor, indexerStack: indexer, parentControl: parentControl)
		self.private_comminInit()
	}
	#else // os(watchOS)
	@objc public required init(layoutElement: GXLayoutElementData, controlId: UInt, gxMode modeType: GXModeType, fieldDescriptor: GXEntityDataFieldDescriptor, indexerStack indexer: [Any]?, parentControl: GXControlContainer)
		
	{
		let customControlProperties = layoutElement.layoutElementDataCustomProperties
		self.format = GXControlRelativeTimerHelper.format(from: customControlProperties.getPropertyValueString("@SDRelativeTimerFormat"))
		self.units = GXControlRelativeTimerHelper.units(from: customControlProperties.getPropertyValueString("@SDRelativeTimerUnits"))
		self.mostRepresentativeOnly = customControlProperties.getPropertyValueBool("@SDRelativeTimerMostRepresentativeUnitOnly")
		self.countingType = GXControlRelativeTimerHelper.relativeTimerCountingType(from: customControlProperties.getPropertyValueString("@SDRelativeTimerCountingType"))
		self.prefixText = type(of: self).layoutElementPrefixTextFrom(customControlProperties)
		self.suffixText = type(of: self).layoutElementSuffixTextFrom(customControlProperties)
		self.minText = type(of: self).layoutElementMinTextFrom(customControlProperties)
		self.maxText = type(of: self).layoutElementMaxTextFrom(customControlProperties)
		self.minSeconds = type(of: self).layoutElementMinSecondsFrom(customControlProperties)
		self.maxSeconds = type(of: self).layoutElementMaxSecondsFrom(customControlProperties)
		self.eventTimerStatusChangedState = EventTimerStatusChangedState()
		super.init(layoutElement: layoutElement, controlId: controlId, gxMode: modeType, fieldDescriptor: fieldDescriptor, indexerStack: indexer, parentControl: parentControl)
		self.private_comminInit()
	}
	#endif // os(watchOS)
	
	private func private_comminInit() {
		let addNotificationsObservers: Bool
		#if !os(watchOS)
			addNotificationsObservers = !self.parentControl!.context.isInsideGXControlTableForHeightCalculation && GXExecutionEnvironmentHelper.activeStateNotificationsSupported()
		#else
			addNotificationsObservers = GXExecutionEnvironmentHelper.activeStateNotificationsSupported()
		#endif
		if addNotificationsObservers {
			let notificationCenter = NotificationCenter.default
			notificationCenter.addObserver(self, selector: #selector(suspendTimer), name: Notification.Name(GXExecutionEnvironmentHelper.currentContextWillResignActiveNotification!), object: nil)
			notificationCenter.addObserver(self, selector: #selector(resumeTimerIfCurrentStateIsValid), name: Notification.Name(GXExecutionEnvironmentHelper.currentContextDidBecomeActiveNotification!), object: nil)
		}
	}
	
    @objc deinit {
        NotificationCenter.default.removeObserver(self)
        self.timer?.invalidate()
    }
	
	// MARK: - Layout Element Helpers
	
	private class func layoutElementPrefixTextFrom(_ customControlProperties: GXCodingPropertiesObjectProtocol) -> String {
		return GXUtilities.string(from: customControlProperties.getPropertyValue("@SDRelativeTimerPrefixText")) ?? ""
	}
	
	@objc fileprivate var layoutElementPrefixText: String {
		return type(of: self).layoutElementPrefixTextFrom(self.layoutElementData.layoutElementDataCustomProperties)
	}
	
	private class func layoutElementSuffixTextFrom(_ customControlProperties: GXCodingPropertiesObjectProtocol) -> String {
		return GXUtilities.string(from: customControlProperties.getPropertyValue("@SDRelativeTimerSuffixText")) ?? ""
	}
	
	@objc fileprivate var layoutElementSuffixText: String {
		return type(of: self).layoutElementSuffixTextFrom(self.layoutElementData.layoutElementDataCustomProperties)
	}
	
	private class func layoutElementMinTextFrom(_ customControlProperties: GXCodingPropertiesObjectProtocol) -> String {
		return GXUtilities.string(from: customControlProperties.getPropertyValue("@SDRelativeTimerMinText")) ?? ""
	}
	
	@objc fileprivate var layoutElementMinText: String {
		return type(of: self).layoutElementMinTextFrom(self.layoutElementData.layoutElementDataCustomProperties)
	}
	
	private class func layoutElementMaxTextFrom(_ customControlProperties: GXCodingPropertiesObjectProtocol) -> String {
		return GXUtilities.string(from: customControlProperties.getPropertyValue("@SDRelativeTimerMaxText")) ?? ""
	}
	
	@objc fileprivate var layoutElementMaxText: String {
		return type(of: self).layoutElementMaxTextFrom(self.layoutElementData.layoutElementDataCustomProperties)
	}
	
	private class func layoutElementMinSecondsFrom(_ customControlProperties: GXCodingPropertiesObjectProtocol) -> Double {
		return GXUtilities.unsignedIntegerNumber(fromValue: customControlProperties.getPropertyValue("@SDRelativeTimerMinSeconds"))?.doubleValue ?? 0
	}
	
	@objc fileprivate var layoutElementMinSeconds: Double {
		return type(of: self).layoutElementMinSecondsFrom(self.layoutElementData.layoutElementDataCustomProperties)
	}
	
	private class func layoutElementMaxSecondsFrom(_ customControlProperties: GXCodingPropertiesObjectProtocol) -> Double {
		return GXUtilities.unsignedIntegerNumber(fromValue: customControlProperties.getPropertyValue("@SDRelativeTimerMaxSeconds"))?.doubleValue ?? 0
	}
	
	@objc fileprivate var layoutElementMaxSeconds: Double {
		return type(of: self).layoutElementMaxSecondsFrom(self.layoutElementData.layoutElementDataCustomProperties)
	}
    
    // MARK: - Overrides
	
	#if !os(watchOS)
	override public func newEditorView(withFrame frame: CGRect) -> UIView {
		let label = UILabel(frame: frame)
		self.applyHorizontalAlignment(toRelativeTimeLabel: label)
		return label
	}
	
	public override func applyThemeClass() {
		super.applyThemeClass()
		#if os(watchOS)
		#warning("TODO_finthamoussu")
		#else
		self.relativeTimeLabel?.apply(self.themeClass, with: [.color, .font])
		#endif
	}
	
	public override func editorViewFrame(forEditorFrame editorFrame: CGRect) -> CGRect {
		var frame = editorFrame
		if (frame.width > 0 && self.verticalAlignDependsOnLayout) {
			let requiredHeight = self.controlFont.lineHeight
			frame = GXVerticalAlignedFrame(frame, ceil(requiredHeight), self.verticalAlign)
		}
		return super.editorViewFrame(forEditorFrame: frame)
	}
	
	public override var horizontalAlign: GXHorizontalAlignType {
		didSet {
			if oldValue != self.horizontalAlign, let label = self.relativeTimeLabel {
				self.applyHorizontalAlignment(toRelativeTimeLabel: label)
			}
		}
	}
	
	private var verticalAlignDependsOnLayout: Bool {
		switch self.verticalAlign {
		case .top, .bottom:
			return true
		default:
			return false
		}
	}
	
	private var controlFont: UIFont {
		var font: UIFont? = nil
		if let label = self.relativeTimeLabel {
			font = label.font
		}
		else if let themeClassWithFont = self.themeClass as? GXThemeClassWithFont {
			font = themeClassWithFont.font
		}
		#if os(iOS)
		return font ?? UIFont.systemFont(ofSize: UIFont.systemFontSize)
		#else
		return font ?? UIFont.systemFont(ofSize: CGFloat(kUIFontSystemFontSize))
		#endif
	}
	
	private func applyHorizontalAlignment(toRelativeTimeLabel label: UILabel) {
		let textAlign: NSTextAlignment
		switch self.horizontalAlign {
		case .left:
			textAlign = .left
		case .center:
			textAlign = .center
		case .right:
			textAlign = .right
		default:
			textAlign = .natural
		}
		label.textAlignment = textAlign
	}

	#endif
	
	public override func applyPropertyValue(_ propValue: Any?, toPropertyName propName: String) -> Bool {
		guard !super.applyPropertyValue(propValue, toPropertyName: propName) else {
			return true
		}
		switch propName {
		case "prefixtext":
			self.prefixText = GXUtilities.string(from: propValue) ?? ""
		case "suffixtext":
			self.suffixText = GXUtilities.string(from: propValue) ?? ""
		case "maxtext":
			self.maxText = GXUtilities.string(from: propValue) ?? ""
		case "mintext":
			self.minText = GXUtilities.string(from: propValue) ?? ""
		case "minseconds":
			self.minSeconds = GXUtilities.doubleNumber(fromValue: propValue)?.doubleValue ?? 0
		case "maxseconds":
			self.maxSeconds = GXUtilities.doubleNumber(fromValue: propValue)?.doubleValue ?? 0
		default:
			return false
		}
		return true
	}
	
	public override func value(forPropertyName propertyName: String) -> Any? {
		switch propertyName {
		case "prefixtext":
			return self.prefixText
		case "suffixtext":
			return self.suffixText
		case "maxtext":
			return self.maxText
		case "mintext":
			return self.minText
		case "minseconds":
			return NSNumber(value: self.minSeconds)
		case "maxseconds":
			return NSNumber(value: self.maxSeconds)
		default:
			return super.value(forPropertyName: propertyName)
		}
	}
    
	public override func viewWillDisappear(_ animated: Bool) {
		self.suspendTimer()
    }
    
	public override func viewDidDisappear(_ animated: Bool) {
		self.suspendTimer()
    }
    
    public override func viewDidAppear(_ animated: Bool) {
        self.resumeTimerIfCurrentStateIsValid()
    }
	
	public override func onEntityDataFiledValueChanged(_ oldEntityDataFieldValue: Any?) {
		super.onEntityDataFiledValueChanged(oldEntityDataFieldValue)
		#if !os(watchOS)
		if let reuseContext = self.relativeTimerReuseContext {
			reuseContext.shouldInvalidateTimerAndResumeTimer = true
			reuseContext.shouldInvalidateEventTimerStatusChangedState = true
			return
		}
		#endif
		self.invalidateTimerAndResumeTimerIfCurrentStateIsValid(invalidateEventStatusChangedState: true)
	}
    
	public override func loadEntityDataFieldValue() {
		#if !os(watchOS)
		var timeIntervalText: String?
		if let date = self.nonEmptyDateEntityDataFieldValue {
			timeIntervalText = self.generateText(forInterval: date.timeIntervalSinceNow)
		}
		self.relativeTimeLabel?.text = timeIntervalText
		#else
		guard let date = self.nonEmptyDateEntityDataFieldValue else {
			self.interfaceObjectsDisplayState = .empty
			return
		}
		self.interfaceObjectsDisplayState = self.displayState(forTimeInterval: date.timeIntervalSinceNow)
		#endif
	}
    
    // MARK: - Private Helpers
	
	#if os(watchOS)
	private var interfaceObjectsDisplayState: DisplayState? {
		didSet {
			guard let displayState = self.interfaceObjectsDisplayState, oldValue != displayState else { return }
			switch displayState {
			case .empty:
				self.interfaceObjectUCPrefixLabel.setHidden(true)
				self.interfaceObjectUCTimer.setHidden(true)
				self.interfaceObjectUCSuffixLabel.setHidden(true)
				self.interfaceObjectUCTimer.stop()
			case .showDate, .showDateTimeIntervalZero:
				self.interfaceObjectUCPrefixLabel.setHidden(self.prefixText.isEmpty)
				self.interfaceObjectUCPrefixLabel.setText(self.prefixText)
				self.interfaceObjectUCTimer.setHidden(false)
				if displayState == .showDate {
					if let date = self.nonEmptyDateEntityDataFieldValue {
						self.interfaceObjectUCTimer.setDate(date)
					}
					self.interfaceObjectUCTimer.start()
				}
				else {
					self.interfaceObjectUCTimer.setDate(Date())
					self.interfaceObjectUCTimer.stop()
				}
				self.interfaceObjectUCSuffixLabel.setHidden(self.suffixText.isEmpty)
				self.interfaceObjectUCSuffixLabel.setText(self.suffixText)
			case .showMaxSeconds, .showMinSeconds:
				self.interfaceObjectUCPrefixLabel.setHidden(false)
				self.interfaceObjectUCTimer.setHidden(true)
				self.interfaceObjectUCSuffixLabel.setHidden(true)
				self.interfaceObjectUCTimer.stop()
				self.interfaceObjectUCMinMaxTextLabel?.setText(displayState == .showMinSeconds ? self.minText : self.maxText)
			}
		}
	}
	#endif
	
	private func setTimerFireDate(_ fireDate: Date) {
		if let timer = self.timer, timer.isValid, timer.timeInterval != 0 {
			timer.fireDate = fireDate
		}
		else {
			self.scheduleNewTimer(fireDate: fireDate)
		}
	}
	
    private func scheduleNewTimer(fireDate: Date? = nil) {
		let timer: Timer
		let interval: TimeInterval
		#if os(watchOS)
		interval = 0
		#else
		interval = 1
		#endif
		if let fireDate = fireDate {
			timer = Timer(fireAt: fireDate, interval: interval, target: self, selector: #selector(timerTick), userInfo: nil, repeats: interval != 0)
		}
		else {
			timer = Timer(timeInterval: interval, target: self, selector: #selector(timerTick), userInfo: nil, repeats: interval != 0)
		}
		if interval != 0 {
			timer.tolerance = 0.1
		}
		self.timer = timer
		RunLoop.main.add(timer, forMode: .common)
		if fireDate == nil {
			timer.fire()
		}
    }
	
	@objc private func timerTick() {
		guard let timer = self.timer, timer.isValid else {
			self.suspendTimer()
			return
		}
		
		self.onTimerTick()
	}
	
	private func onTimerTick() {
		#if !os(watchOS)
		if self.isViewLoaded {
			self.loadEntityDataFieldValue()
		}
		#else
		self.loadEntityDataFieldValue()
		#endif
		
		guard let date = self.nonEmptyDateEntityDataFieldValue else {
			self.suspendTimer()
			return
		}
		
		let timeInterval = date.timeIntervalSinceNow
		let absTimeInterval = self.countingTypeConstrainedAbsoluteTimeInterval(timeInterval)
		let minSecondsCondition = self.minSeconds > 0 && absTimeInterval <= self.minSeconds
		let maxSecondsCondition = self.maxSeconds > 0 && absTimeInterval >= self.maxSeconds
		
		var eventState = self.eventTimerStatusChangedState
		var fireTimerStatusChangedEvent = false
		
		if Int(timeInterval) == 0 && !eventState.hasFiredEventForZeroSeconds {
			fireTimerStatusChangedEvent = true
			eventState.hasFiredEventForZeroSeconds = true
			eventState.hasFiredEventForMaxSeconds = false
		}
		if minSecondsCondition == !eventState.hasFiredEventForMinSeconds {
			fireTimerStatusChangedEvent = true
			eventState.hasFiredEventForMinSeconds = !eventState.hasFiredEventForMinSeconds
		}
		if maxSecondsCondition != !eventState.hasFiredEventForMaxSeconds {
			fireTimerStatusChangedEvent = true
			eventState.hasFiredEventForMaxSeconds = !eventState.hasFiredEventForMaxSeconds
		}
		self.eventTimerStatusChangedState = eventState
		
		if timeInterval <= 0 {
			if self.countingType == .down || maxSecondsCondition {
				self.suspendTimer()
			} else if minSecondsCondition {
				self.setTimerFireDate(date.addingTimeInterval(self.minSeconds + 1))
			}
			else {
				#if !os(watchOS)
				if self.invalidTimerAndCurrentStateIsValid {
					self.scheduleNewTimer()
				}
				#else
				if self.currentStateIsValid {
					if self.maxSeconds > 0 {
						self.setTimerFireDate(date.addingTimeInterval(self.maxSeconds + 1))
					}
				}
				#endif
			}
		}
		else { // timeInterval > 0
			if self.countingType == .up || minSecondsCondition {
				self.setTimerFireDate(date)
			}
			else if maxSecondsCondition {
				self.setTimerFireDate(date.addingTimeInterval(1 - self.maxSeconds))
			}
			else {
				#if !os(watchOS)
				if self.invalidTimerAndCurrentStateIsValid {
					self.scheduleNewTimer()
				}
				#else
				if self.currentStateIsValid {
					if self.minSeconds > 0 {
						self.setTimerFireDate(date.addingTimeInterval(1 - self.minSeconds))
					}
					else {
						self.setTimerFireDate(date)
					}
				}
				#endif
			}
		}
		
		if fireTimerStatusChangedEvent {
			self.fireControlEvent(type(of: self).EventTimerStatusChanged, userInterfaceContext: nil)
		}
	}
    
    @objc private func resumeTimerIfCurrentStateIsValid() {
		gx_dispatch_on_main_queue {
			if self.invalidTimerAndCurrentStateIsValid {
				self.onTimerTick()
			}
		}
    }
    
    @objc private func suspendTimer() {
		gx_dispatch_on_main_queue {
			self.timer = nil
		}
    }
	
	private var invalidTimerAndCurrentStateIsValid: Bool {
		return !(self.timer?.isValid ?? false) && self.currentStateIsValid
	}
    
	private var currentStateIsValid: Bool {
		guard self.currentApplicationStateIsValid, self.currentViewVisibleStateIsValid else {
			return false
		}
		#if !os(watchOS)
		guard !(self.parentControl?.context.isInsideGXControlTableForHeightCalculation ?? true) else {
			return false
		}
		#endif
		return true
	}
    
    
	private var currentApplicationStateIsValid: Bool {
        return GXExecutionEnvironmentHelper.applicationState == .active
    }
    
	private var currentViewVisibleStateIsValid: Bool {
		switch (self.viewVisibleState) {
        case .appearing, .appeared:
            return true
        default:
            return false
        }
	}
	
	enum DisplayState {
		case empty
		case showMaxSeconds
		case showMinSeconds
		case showDate
		#if os(watchOS)
		case showDateTimeIntervalZero
		#endif
	}
	
	private func displayState(forTimeInterval timeInterval: TimeInterval) -> DisplayState {
		let absTimeInterval = self.countingTypeConstrainedAbsoluteTimeInterval(timeInterval)
		return self.displayState(forConstrainedAbsoluteTimeInterval: absTimeInterval)
	}
	
	private func displayState(forConstrainedAbsoluteTimeInterval absTimeInterval: TimeInterval) -> DisplayState {
		assert(absTimeInterval == self.countingTypeConstrainedAbsoluteTimeInterval(absTimeInterval))
		if self.maxSeconds > 0 && absTimeInterval >= self.maxSeconds {
			return .showMaxSeconds
		}
		else if self.minSeconds > 0 && absTimeInterval <= self.minSeconds {
			return .showMinSeconds
		}
		else {
			#if os(watchOS)
			if self.countingType != .auto && absTimeInterval == 0 {
				return .showDateTimeIntervalZero
			}
			#endif
			return .showDate
		}
	}
	
	#if !os(watchOS)
    private func generateText(forInterval timeInterval: TimeInterval) -> String? {
		let absTimeInterval = self.countingTypeConstrainedAbsoluteTimeInterval(timeInterval)
		let displayState = self.displayState(forConstrainedAbsoluteTimeInterval: absTimeInterval)
		switch displayState {
		case .empty:
			return nil
		case .showDate:
			guard let formattedDate = self.dateFormatter.string(from: absTimeInterval) else {
				return nil
			}
			let prefix = self.prefixText.isEmpty ? "" : self.prefixText.appending(" ")
			let suffix = self.suffixText.isEmpty ? "" : " ".appending(self.suffixText)
			return "\(prefix)\(formattedDate)\(suffix)"
		case .showMaxSeconds:
			return self.maxText
		case .showMinSeconds:
			return self.minText
		}
    }
	#endif
	
	private func countingTypeConstrainedAbsoluteTimeInterval(_ timeInterval: TimeInterval) -> TimeInterval {
		let timeInterval = ceil(timeInterval) // to avoid double zeros
		let absTimeInterval: TimeInterval
		switch self.countingType {
		case .up:
			absTimeInterval = abs(min(0, timeInterval))
		case .down:
			absTimeInterval = max(0, timeInterval)
		case .auto:
			absTimeInterval = abs(timeInterval)
		}
		return absTimeInterval
	}
}


#if !os(watchOS)
@objc(GXControlRelativeTimerBaseReuseContext)
public class GXControlRelativeTimerBaseReuseContext: GXControlWithLabelBaseReuseContext {
	
	public var shouldInvalidateTimerAndResumeTimer = false
	public var shouldInvalidateEventTimerStatusChangedState = false

	public var restoreLayoutElementPrefixText = true
	public var restoreLayoutElementSuffixText = true
	public var restoreLayoutElementMinText = true
	public var restoreLayoutElementMaxText = true
	public var restoreLayoutElementMinSeconds = true
	public var restoreLayoutElementMaxSeconds = true
}

extension GXControlRelativeTimer {
	
	@objc var relativeTimerReuseContext: GXControlRelativeTimerBaseReuseContext? {
		guard let labelReuseContext = self.labelReuseContext else { return nil }
		return (labelReuseContext as! GXControlRelativeTimerBaseReuseContext)
	}
	
	// MARK: - Overrides
	
	public override func reuseContextClass() -> AnyClass {
		return GXControlRelativeTimerBaseReuseContext.self
	}
	
#if DEBUG
	public override func prepareForReuseViews() {
		super.prepareForReuseViews()
		assert(self.reuseContext is GXControlRelativeTimerBaseReuseContext, "\(NSStringFromSelector(#selector(GXControlRelativeTimer.reuseContextClass))) must be subclass of \(NSStringFromClass(GXControlRelativeTimerBaseReuseContext.self))")
	}
#endif
	
	public override func endPrepareForReuseViews() {
		if let reuseContext = self.relativeTimerReuseContext {
			if reuseContext.restoreLayoutElementPrefixText {
				self.prefixText = self.layoutElementPrefixText
			}
			if reuseContext.restoreLayoutElementSuffixText {
				self.suffixText = self.layoutElementSuffixText
			}
			if reuseContext.restoreLayoutElementMinText {
				self.minText = self.layoutElementMinText
			}
			if reuseContext.restoreLayoutElementMaxText {
				self.maxText = self.layoutElementMaxText
			}
			if reuseContext.restoreLayoutElementMinSeconds {
				self.minSeconds = self.layoutElementMinSeconds
			}
			if reuseContext.restoreLayoutElementMaxSeconds {
				self.maxSeconds = self.layoutElementMaxSeconds
			}
			#if DEBUG
			assert(!reuseContext.restoreLayoutElementPrefixText, "\(NSStringFromSelector(#selector(getter: type(of: self).layoutElementPrefixText))) not being restored?")
			assert(!reuseContext.restoreLayoutElementSuffixText, "\(NSStringFromSelector(#selector(getter: type(of: self).layoutElementSuffixText))) not being restored?")
			assert(!reuseContext.restoreLayoutElementMinText, "\(NSStringFromSelector(#selector(getter: type(of: self).layoutElementMinText))) not being restored?")
			assert(!reuseContext.restoreLayoutElementMaxText, "\(NSStringFromSelector(#selector(getter: type(of: self).layoutElementMaxText))) not being restored?")
			assert(!reuseContext.restoreLayoutElementMinSeconds, "\(NSStringFromSelector(#selector(getter: type(of: self).layoutElementMinSeconds))) not being restored?")
			assert(!reuseContext.restoreLayoutElementMaxSeconds, "\(NSStringFromSelector(#selector(getter: type(of: self).layoutElementMaxSeconds))) not being restored?")
			#endif
			if reuseContext.shouldInvalidateTimerAndResumeTimer {
				self.invalidateTimerAndResumeTimerIfCurrentStateIsValid(invalidateEventStatusChangedState: reuseContext.shouldInvalidateEventTimerStatusChangedState, resumeAsync: false)
			}
		}
		super.endPrepareForReuseViews()
	}
}
#endif

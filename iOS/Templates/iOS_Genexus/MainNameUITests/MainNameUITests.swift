//
//  $Main.Name$UITests.swift
//

import XCTest

class $Main.Name$UITests: XCTestCase {
        
    override func setUp() {
        super.setUp()
        
        // Put setup code here. This method is called before the invocation of each test method in the class.
        
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
        // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
        XCUIApplication().launch()

		// Handle system alerts by tapping the OK button
		addUIInterruptionMonitor(withDescription: "System Dialog") { (alert) -> Bool in
			let okButtonIndex = alert.buttons.count > 1 ? 1 : 0
			alert.buttons.element(boundBy: okButtonIndex).tap()
			return true
		}
		
		SdtUITestSD.visualTestingServer = "$Main.VisualTestingRepositoryURL$"
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    $Main.GxTest4UITestRelatedObjectNames:{ testName |
func test$testName$() {
    $testName; format="Lower"$().execute()
\}
    }; separator="\n"$
}

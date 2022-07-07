{
	"pages": [
		$data.PanelsToTest: { obj |"http://localhost:3030/$obj.RoutingPath$"};separator=",\r\n"$
	],
	"expectations": {
        "categories": {
            "performance":  80,
            "best-practices": 80,
            "accessibility": 80
        }
	},
	"lighthouseParams": {
		"flags": {
		  "chromeFlags": ["--headless"]
		},
		"config": {
		  "extends": "lighthouse:default"
		}
	},
	"failOnUnmetExpectation": true
}

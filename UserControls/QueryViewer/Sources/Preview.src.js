// Initialization for Query object preview tab
function IsQueryObjectPreview() {
	return window.external != undefined && window.external.SendText != undefined;
}

function IsDashboardEdit() {
	return typeof UCDashboardForEditor !== 'undefined';
}

function UpdateQueryObjectPreview(postDataJSON) {

	function SetPreviewData(qViewer, postDataJSON) {
		var postData = JSON.parse(postDataJSON);
		var keys = Object.keys(postData);
		for (var i = 0; i < keys.length; i++) {
			var key = keys[i];
			qViewer[key] = postData[key];
		}
	}

	var userControlId = Object.keys(qv.collection)[0];
	var qViewer = qv.collection[userControlId];
	SetPreviewData(qViewer, postDataJSON);
	qViewer.realShow();

}
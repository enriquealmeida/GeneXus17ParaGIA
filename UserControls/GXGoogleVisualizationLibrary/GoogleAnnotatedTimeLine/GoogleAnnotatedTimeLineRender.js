function GoogleAnnotatedTimeLine()
{
	// Databinding for property VisualizationData
	this.SetVisualizationData = function(data)
	{
		///UserCodeRegionStart:[SetVisualizationData] (do not remove this comment.)
		this.VisualizationData = data;					
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property VisualizationData
	this.GetVisualizationData = function() {
		///UserCodeRegionStart:[GetVisualizationData] (do not remove this comment.)
		return this.VisualizationData;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

    ///UserCodeRegionStart:[SetVisualizationDataSelectedItem] (do not remove this comment.)
    this.SetVisualizationDataSelectedItem = function(data) {
        this.SelectedItem = data;
        ///UserCodeRegionEnd: (do not remove this comment.)
    }
    ///UserCodeRegionStart:[GetVisualizationDataSelectedItem] (do not remove this comment.)
    this.GetVisualizationDataSelectedItem = function() {
        return this.SelectedItem;
        ///UserCodeRegionEnd: (do not remove this comment.)
    }

    this.show = function() {
        ///UserCodeRegionStart:[show] (do not remove this comment.)
        if (!this.IsPostBack) {
            var _this = this;
            google.load("visualization", "current", { packages: ["annotatedtimeline"], callback: function() { _this.DrawVisualization() } });
        }
        else {
            if (this.Reload) {
                this.Reload = false;
                this.DrawVisualization();
            }

        }
        ///UserCodeRegionEnd: (do not remove this comment.)
    }
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	this.DrawVisualization = function() {
	    this.getContainerControl().style.height = gx.dom.addUnits(this.Height);
	    this.getContainerControl().style.width = gx.dom.addUnits(this.Width);
	    this.Visualization = this.GetVisualizationControl();
	    this.CreateDataTable();
	    var properties = this.GetVisualizationProperties();
	    this.Visualization.draw(this.Data, properties);
	    this.AddListeners();
	}
	
	// template: implement this method according to your control
	this.GetVisualizationControl = function()
	{
	    return new google.visualization.AnnotatedTimeLine(this.getContainerControl());
	}

	// template: implement this method according to your control
	this.AddListeners = function() {
	    if (this.ItemSelected != undefined) {
	        google.visualization.events.addListener(this.Visualization, 'select', this.CreateDelegate(this, this.SelectHandler));
	    }
	}

	this.SelectHandler = function() {
	    var row = this.Visualization.getSelection()[0].row;
	    var column = this.Visualization.getSelection()[0].column;
	    this.SelectedItem.SerieName = this.Data.getColumnLabel(column);
	    this.SelectedItem.XValue = this.GetGXDate(this.Data.getValue(row, 0));
	    this.SelectedItem.YValue = this.Data.getValue(row, column);
	    this.ItemSelected();
	}
	
	// template: implement this method according to your control
	this.CreateDataTable = function() {
	    this.Data = new google.visualization.DataTable();
	    var dates = 0,
			datetype = 'date';
	    for (i = 0; this.VisualizationData[i] != undefined; i++) {
	        for (j = 0; this.VisualizationData[i].Data[j] != undefined; j++) {
	            dates++;
	            if (this.VisualizationData[i].Data[j].XValue.length > 10)
	                datetype = 'datetime'
	        }
	    }

	    this.Data.addColumn(datetype, 'Date');
	    for (i = 0; this.VisualizationData[i] != undefined; i++) {
	        this.Data.addColumn('number', this.VisualizationData[i].Name);
	        this.Data.addColumn('string', 'title');
	        this.Data.addColumn('string', 'text');
	    }

	    this.Data.addRows(dates);

	    var p = 0;
	    var k = 0;
	    var q = 0;
	    for (i = 0; this.VisualizationData[i] != undefined; i++) {
	        for (j = 0; this.VisualizationData[i].Data[j] != undefined; j++) {
	            this.Data.setValue(p, 0, (new gx.date.gxdate(this.VisualizationData[i].Data[j].XValue, 'ANSI')).Value);
	            k++;
	            this.Data.setValue(p, k + q, this.VisualizationData[i].Data[j].YValue);
	            k++;
	            this.Data.setValue(p, k + q, this.VisualizationData[i].Data[j].AnnotationTitle);
	            k++;
	            this.Data.setValue(p, k + q, this.VisualizationData[i].Data[j].AnnotationText);
	            p++;
	            k = 0;
	        }
	        q = q + 3;	        
	    }
	}
	
	// template: implement this method according to your control
	this.GetVisualizationProperties = function() {
		return {
			"displayExactValues": this.DisplayExactValues,
			"displayAnnotations": this.DisplayAnnotations,
			"allValuesSuffix": this.AllValuesSuffix,
			"displayAnnotationsFilter": this.DisplayAnnotationsFilter,
			"displayZoomButtons": this.DisplayZoomButtons,
			"allowHtml": true,
			"wmode": 'transparent',
			"scaleType": this.ScaleType,
			interpolateNulls: true
		};
	}

	this.CreateDelegate = function(that, thatMethod) {
	    return function() { return thatMethod.call(that); }
	}

	this.GetGXDate = function(aDate) {
	    var year = aDate.getFullYear();
	    var month = aDate.getMonth() + 1;
	    var day = aDate.getDate();
	    day = day + "";
	    if (day.length == 1) day = "0" + day;
	    return new gx.date.gxdate(year + "/" + month + "/" + day, "YMD").getStringWithFmt(gx.dateFormat);
	}
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

Array.prototype.size = function() {
    var l = this.length ? --this.length : -1;
    for (var k in this) {
        l++;
    }
    return l;
}





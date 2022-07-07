function GoogleIntensityMap()
{
	this.Width;
	this.Height;
	this.Series;

	// Databinding for property Series
	this.SetVisualizationData = function(data)
	{
		///UserCodeRegionStart:[SetVisualizationData] (do not remove this comment.)
		this.VisualizationData = data;
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Series
	this.GetVisualizationData = function()
	{
		///UserCodeRegionStart:[GetVisualizationData] (do not remove this comment.)
		var dummy = new Object();
		dummy.Width = "";
	    dummy.Height = "";
	    dummy.VisualizationData = new Array();
		return dummy;		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if (!this.IsPostBack)
		{
			google.load("visualization", "46", {
				packages:["intensitymap"], 
				callback: (function () {
					this.DrawVisualization()
				}).closure(this)
			});
		}
		else
		{
	        this.DrawVisualization();		        
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)


	this.DrawVisualization = function()
	{	
	    this.getContainerControl().style.height = gx.dom.addUnits(this.Height);
		this.getContainerControl().style.width = gx.dom.addUnits(this.Width);
	    this.Visualization = this.GetVisualizationControl();
	    this.CreateDataTable();
	    this.Visualization.draw(this.Data, null);	    
	}
		// template: implement this method according to your control
	this.GetVisualizationControl = function()
	{
	    return new google.visualization.IntensityMap(this.getContainerControl());
	}
	// template: implement this method according to your control
	this.CreateDataTable = function()
	{
	    var data = new google.visualization.DataTable();
	    data.addColumn('string','', 'Country');
	    for (i=0; this.VisualizationData.Info[i] != undefined ; i++)
		{
			var label = this.VisualizationData.Info[i];
			data.addColumn('number', label);
		}
		if (this.VisualizationData.Countries != undefined && this.VisualizationData.Countries.length > 0)
		{
			data.addRows(this.VisualizationData.Countries.length);
			for(i=0 ; this.VisualizationData.Countries[i] != undefined ; i++)
			{
				data.setValue(i, 0, gx.text.trim(this.VisualizationData.Countries[i].CountryISO));
				for (j = 0; this.VisualizationData.Countries[i].Values[j] != undefined ; j++)
				{
					data.setValue(i, j + 1, parseFloat(this.VisualizationData.Countries[i].Values[j]));
				}
			}
		}
		this.Data = data;
	 }
    this.CreateDelegate = function(that, thatMethod)
    {
        return function() { return thatMethod.call(that); }
    }		

	///UserCodeRegionEnd: (do not remove this comment.):
}

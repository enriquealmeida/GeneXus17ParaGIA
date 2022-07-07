function GoogleMotion()
{
	this.Width;
	this.Height;
	this.Series;
	this.XTitle;
	this.YTitle;
	this.Title;

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
			google.load("visualization", "current", {
				packages:["motionchart"],
				callback: (function(){
					this.DrawVisualization();
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
	    this.Visualization.draw(this.Data, {});
	}
	
		// template: implement this method according to your control
	this.GetVisualizationControl = function()
	{
	    return new google.visualization.MotionChart(this.getContainerControl());
	}
	
	// template: implement this method according to your control
	this.CreateDataTable = function()
	{
	    var data = new google.visualization.DataTable();
		data.addColumn('string', this.Title);
        data.addColumn('date', 'Years');
	    data.addColumn('number', this.XTitle);
        data.addColumn('number', this.YTitle);
        data.addRows(this.VisualizationData.length);
		if (this.VisualizationData != undefined && this.VisualizationData.length > 0)
		{
			for(i=0 ; this.VisualizationData[i]!=undefined ; i++)
			{
				data.setValue(i, 0, this.VisualizationData[i].Name);
				data.setValue(i, 1, this.GetDate(this.VisualizationData[i].Date));
				data.setValue(i, 2, this.VisualizationData[i].X);
				data.setValue(i, 3, this.VisualizationData[i].Y);
			}
		}
		this.Data = data;

    }

    this.GetDate = function(d) {
        if (typeof (d) == 'string') {
            var convert = this.str_to_date("%Y/%m/%d");
            return convert(d);
        }
        else {
            return d.Value;
        }
    }     
	 
	 this.str_to_date = function(format,utc){
	     var splt = "var temp=date.split(/[^0-9]+/g);";
	     var mask = format.match(/%[a-zA-Z]/g);
	     for (var i = 0; i < mask.length; i++) {
	         switch (mask[i]) {
	             case "%d": splt += "set[2]=temp[" + i + "]||0;";
	                 break;
	             case "%m": splt += "set[1]=(temp[" + i + "]||1)-1;";
	                 break;
	             case "%y": splt += "set[0]=temp[" + i + "]*1+(temp[" + i + "]>50?1900:2000);";
	                 break;
	             case "%h":
	             case "%H":
	                 splt += "set[3]=temp[" + i + "]||0;";
	                 break;
	             case "%i":
	                 splt += "set[4]=temp[" + i + "]||0;";
	                 break;
	             case "%Y": splt += "set[0]=temp[" + i + "]||0;";
	                 break;
	         }
	     }
	     var code = "set[0],set[1],set[2],set[3],set[4]";
	     if (utc) code = " Date.UTC(" + code + ")";
	     return new Function("date", "var set=[0,0,0,0,0]; " + splt + " return new Date(" + code + ");");
    }
	 
	 
    this.CreateDelegate = function(that, thatMethod)
    {
        return function() { return thatMethod.call(that); }
    }		

	
	
	
	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

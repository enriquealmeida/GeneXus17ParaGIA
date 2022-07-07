

function GoogleGauge()
{
	this.Width;
	this.Height;
	this.Data;
	this.GreenFrom;
	this.GreenTo;
	this.YellowFrom;
	this.YellowTo;
	this.RedFrom;
	this.RedTo;
	this.MajorTicksLabels;
	this.MinorTicks;
	this.Max;
	this.Min;
	this.Width;
	this.Height;
	this.Data;
	this.Title;

	// Databinding for property MajorTicksLabels
	this.SetMajorTicksLabels = function(data)
	{
		///UserCodeRegionStart:[SetMajorTicksLabels] (do not remove this comment.)
		this.MajorTicksLabels = data;

		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MajorTicksLabels
	this.GetMajorTicksLabels = function()
	{
		///UserCodeRegionStart:[GetMajorTicksLabels] (do not remove this comment.)

		return this.MajorTicksLabels;
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}


	// Databinding for property Data
	this.SetData = function(data)
	{
		
		///UserCodeRegionStart:[SetData] (do not remove this comment.)
		this.Data = data;

		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Data
	this.GetData = function()
	{
	
		///UserCodeRegionStart:[GetData] (do not remove this comment.)
		return this.Data;

		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function ()
	{
		var _this = this;
		google.load("visualization", "current", {packages:["gauge"], callback:function(){ _this.showImpl()}});
	}

	this.showImpl = function()
	{
    	this.DrawVisualization();
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	this.RefreshTable = function()
	{
		this.CreateDataTable();
	    
	    var options = {width:this.Width, height:this.Height, redFrom: this.RedFrom, redTo:this.RedTo, greenFrom: this.GreenFrom,
	    greenTo: this.GreenTo, majorTicks:this.MajorTicksLabels, max: this.Max, min: this.Min, yellowFrom: this.YellowFrom, yellowTo: this.YellowTo };
	    
	    this.Visualization.draw(this.Data, options);	    

	}

	this.DrawVisualization = function()
	{	
	    this.getContainerControl().style.height = gx.dom.addUnits(this.Height);
		this.getContainerControl().style.width = gx.dom.addUnits(this.Width);
	    this.Visualization = this.GetVisualizationControl();
	    
	    this.RefreshTable();
	}
		// template: implement this method according to your control
	this.GetVisualizationControl = function()
	{
	    return new google.visualization.Gauge(this.getContainerControl());
	}
	// template: implement this method according to your control
	this.CreateDataTable = function()
	{
	
	    var data = new google.visualization.DataTable();
	    data.addColumn('string', 'Label');
	    data.addColumn('number', 'Value');
		
		if (this.Data instanceof Array)
		{
			var row = 0;
			
			for (var i=0; this.Data[i] != undefined ; i++) 
			{
				
				// Here we are expecting a collection with a serie of values or names / values or a collection of numbers
				// So the control will take only 1 or 2 properties for each element, the first number and the first string
				var cantProps = 0;
				var labelAssigned = true;
				var baseName = "";
				data.addRows(1);
				if (isNaN(this.Data[i]))
				{
					var values = new Array();
					
					for (var prop in this.Data[i])
					{
						var value = parseInt(this.Data[i][prop], 10);
						if (!isNaN(value))
						{
							values.push({ name:prop, value:value });
						}
						else
						{
							baseName = this.Data[i][prop];
						}
					}
					if (values.length == 1)
					{
						if (baseName == "")
							data.setValue(row, 0, values[0].name);
						else
							data.setValue(row, 0, baseName);
						data.setValue(row, 1, values[0].value);
						row++;
					}
					else
					{
						data.addRows(values.length - 1);
						for (var j = 0; values[j] != undefined ; j++)
						{
							data.setValue(row, 0, baseName + " " + values[j].name);
							data.setValue(row, 1, values[j].value);
							row++;
						}
					}
				}
				else
				{
					data.setValue(row, 1, this.Data[i]);
					data.setValue(row, 0, this.Title);
					row++;
				}
			}
		}
		else
		{
			if (isNaN(this.Data))
			{
				var row = 0;
				for (var prop in this.Data)
				{
					var value = parseInt(this.Data[prop], 10);
					if (!isNaN(value))
					{
						data.addRows(1);
						data.setValue(row, 0, prop);
						data.setValue(row, 1, value);
						row++;
					}
						
				}
			}
			else
			{
				data.addRows(1);
				
				data.setValue(0, 1, parseInt(this.Data, 10));
				data.setValue(0, 0, this.Title);
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

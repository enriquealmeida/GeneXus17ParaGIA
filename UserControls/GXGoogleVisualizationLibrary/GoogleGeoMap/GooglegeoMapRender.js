function GoogleGeoMap()
{
	this.region;
	this.Width;
	this.Height;
	this.AccessKey;
	this.Mode;
	this.Data;
	this.Title;
	this.TitleValue;

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

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if(!this.IsPostBack)
		{
			google.load('visualization', 'current', {
				'packages': ['geomap'], 
				callback: this.DrawVisualization.closure(this),
				mapsApiKey: this.AccessKey
			});
		}
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	

	this.showImpl = function()
	{
	    	this.DrawVisualization();
	}
	
	this.RefreshTable = function()
	{
	       this.CreateDataTable();
	       this.Visualization.draw(this.Data, this.getConfigOptions());   	
	}
	
	this.selectHandler = function() 
	{
	
	}
	
	this.DrawVisualization = function()
	{
		this.getContainerControl().style.height = gx.dom.addUnits(this.Height);
		this.getContainerControl().style.width = gx.dom.addUnits(this.Width);
		this.Visualization = this.GetVisualizationControl();
		this.RefreshTable();			
	}
	
	this.getConfigOptions = function(){
		if (this.Mode=='regions') 
		{
		var config = {
			height: this.Height,
		        dataMode: 'regions'

	 		}		
		}
		else 		
		{
		var config = {
			height: this.Height,
		        dataMode: 'markers',

		        region: this.region,
		        colors: [0xFF8747, 0xFFB581, 0xc06000] //orange colors
			}		
		}
		return config;
	}
	
	function CBoolean(str){
  	if(str){
    	if(typeof(str) == 'string')
     		return (str.toLowerCase() == "true")
       		return str;
    }
    else
    	return false;
	}
	
	this.GetVisualizationControl = function()
	{
        return new google.visualization.GeoMap(this.getContainerControl());
	}

	this.CreateDataTable = function()
	{
	    var data = new google.visualization.DataTable();
	    data.addColumn('string',this.Title);
            data.addColumn('number',this.TitleValue);

	    data.addColumn('string','Texto'); 

	  if (this.Mode=='regions')
        	{          
		   // data.addColumn('string','Texto'); 	
        	    data.addRows(this.Data.Regions.length);
			for(i1=0;this.Data.Regions[i1]!=undefined;i1++)
				{
				data.setValue(i1, 0, this.Data.Regions[i1].RCountry);
				data.setValue(i1, 1, this.Data.Regions[i1].RValnum);
				if ((this.Data.Regions[i1].RValtext!='') && (this.Data.Regions[i1].RValtext!=undefined))
					data.setValue(i1, 2, this.Data.Regions[i1].RValtext);
                                else data.setValue(i1, 2, this.Data.Regions[i1].RCountry);
				}
				this.Data = data;
		}
        else    //Mode = 'markers'
		{
        	    data.addRows(this.Data.Markers.length);
			for(i1=0;this.Data.Markers[i1]!=undefined;i1++)
				{
				data.setValue(i1, 0, this.Data.Markers[i1].MCity);
				data.setValue(i1, 1, this.Data.Markers[i1].MValnum);
				if ((this.Data.Markers[i1].MValtext!='') && (this.Data.Markers[i1].MValtext!=undefined))
					data.setValue(i1, 2, this.Data.Markers[i1].MValtext);
                                else data.setValue(i1, 2, this.Data.Markers[i1].MCity);

				}
				this.Data = data;
	        }
        }
	this.CreateDelegate = function(that, thatMethod)
	{
	 	return function() { return thatMethod.call(that); }
	}
	
	
	
	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

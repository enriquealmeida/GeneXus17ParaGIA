function OrganizationalChart()
{
	this.VisualizationData;
	this.Width;
	this.Height;
	this.Size;
	this.Reload;
	this.SelectedItem;
	this.Color;

	// Databinding for property VisualizationData
	this.SetVisualizationData = function(data)
	{
		///UserCodeRegionStart:[SetVisualizationData] (do not remove this comment.)
		this.VisualizationData = data;			
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property VisualizationData
	this.GetVisualizationData = function()
	{
		///UserCodeRegionStart:[GetVisualizationData] (do not remove this comment.)
		this.Reload = false; 
		var dummy = new Array();
		return this.VisualizationData;
	    return dummy;	    		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if (!this.IsPostBack)
		{
			var _this = this;            
         	google.load("visualization", "current", { packages: ["orgchart"], callback: function() { _this.DrawVisualization() } });		  
		}
		else
		{
		    if (this.Reload)
		    {		        
		        this.DrawVisualization();		        
		    }
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
	    var properties = this.GetVisualizationProperties();
	    this.Visualization.draw(this.Data, properties);	    
	    google.visualization.events.addListener(this.Visualization, 'select', this.CreateDelegate(this,this.OnSelect));
	}
	
	// template: implement this method according to your control
	this.GetVisualizationControl = function()
	{
	    return new google.visualization.OrgChart(this.getContainerControl());	    
	}
	
	// template: implement this method according to your control
	this.CreateDataTable = function()
	{
	    this.Data = new google.visualization.DataTable();
	    this.Data.addColumn('string', 'Name');
  	    this.Data.addColumn('string', 'Manager');
		this.Data.addColumn('string', 'Tooltip');
	    var i;	    	    
	    for(i=0;this.VisualizationData[i]!=undefined;i++)
	    {
          var counter = new Object();
          counter.i = i;
          this.Data.addRow();
          this.Data.setCell(i, 0, this.VisualizationData[i].Text);
		  this.Data.setCell(i, 2, this.VisualizationData[i].Tooltip);                       
	      this.AddChildren(this.VisualizationData[i].Children,this.VisualizationData[i].Text,counter);    
	    }    
	}
	
	this.AddChildren = function(children, parent, counter)
	{
	    if (children!=undefined)
	    {
	        var i;
	        for(i=0;children[i]!=undefined;i++)
	        {
                counter.i = counter.i + 1;
                this.Data.addRow();
                this.Data.setCell(counter.i, 0, children[i].Text);
                this.Data.setCell(counter.i, 1, parent);        
				this.Data.setCell(counter.i, 2, children[i].Tooltip);              
                this.AddChildren(children[i].Children,children[i].Text,counter); 
	        }
	    }
	}
	
	// template: implement this method according to your control
	this.GetVisualizationProperties = function()
	{
	    var propertiesArray = new Array();
	    propertiesArray["size"] = this.Size;
	    propertiesArray["color"] = gx.color.fromRGB(this.Color.R,this.Color.G,this.Color.B).Html;
	    propertiesArray["selectionColor"] = gx.color.fromRGB(this.Selection_Color.R, this.Selection_Color.G, this.Selection_Color.B).Html;
	    propertiesArray["allowCollapse"] = this.AllowCollapse;
	    propertiesArray["allowHtml"] = true;
	    return propertiesArray;		
	}
	
    this.OnSelect = function() 
    {
        if (this.Selected)
        {
            var selection = this.Visualization.getSelection();
            this.SelectedItem = this.Data.getValue(selection[0].row,0)
            this.Selected()
        }
    }	
	
    this.CreateDelegate = function(that, thatMethod)
    {
        return function() { return thatMethod.call(that); }
    }		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

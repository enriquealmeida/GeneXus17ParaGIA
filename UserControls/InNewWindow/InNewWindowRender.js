function InNewWindow()
{
	this.Targets;
	this.Width;
	this.Height;
	this.Name;
	this.Target;
	this.Fullscreen;
	this.Location;
	this.MenuBar;
	this.Resizable;
	this.Scrollbars;
	this.TitleBar;
	this.ToolBar;
	this.directories;
	this.status;
	this.copyhistory;
	this.top;
	this.left;
	this.fitscreen;
	this.RefreshParentOnClose;

	// Databinding for property Targets
	this.SetTargets = function(data)
	{
		///UserCodeRegionStart:[SetTargets] (do not remove this comment.)
		this.Targets = data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Targets
	this.GetTargets = function()
	{
		///UserCodeRegionStart:[GetTargets] (do not remove this comment.)
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	
	this.OpenWindow = function()
	{	
		this.ToolBar = gx.lang.gxBoolean(this.ToolBar);
		this.Location = gx.lang.gxBoolean(this.Location);
		this.directories= gx.lang.gxBoolean(this.directories) ;
		this.status= gx.lang.gxBoolean(this.status);
		this.MenuBar= gx.lang.gxBoolean(this.MenuBar);
		this.Scrollbars= gx.lang.gxBoolean(this.Scrollbars);
		this.Resizable = gx.lang.gxBoolean(this.Resizable);
		this.TitleBar = gx.lang.gxBoolean(this.TitleBar);
		this.Fullscreen  = gx.lang.gxBoolean(this.Fullscreen);
		this.copyhistory = gx.lang.gxBoolean(this.copyhistory);
	
		var specs = "";
		if (gx.lang.gxBoolean(this.fitscreen)){
			  this.Width = (screen.width-25);
			  this.Height = (screen.height-160);
		}
	
		function convert(value){if (value ==true || value == 1 || value == "1" ) return 1; else if ( value ==false || value == 0 || value == "0" )return 0; return value;} 
		
		function addAttribute(property,value) {
			if (property && property != "" ) 
			{ 
				return property + "="+ convert(value) + ",";  
			} 
				else return ""; 
		}
		
		if (!gx.lang.emptyObject(this.Width))
			specs+=addAttribute("width", this.Width );
		if (!gx.lang.emptyObject(this.Height))
			specs+=addAttribute("height", this.Height);
		
		specs+=addAttribute("toolbar", this.ToolBar);
		if (this.Location == 0) specs+=addAttribute("location", this.Location);
		if (this.directories == 0) specs+=addAttribute("directories", this.directories);
		specs+=addAttribute("status", this.status);
		specs+=addAttribute("menubar", this.MenuBar);
		specs+=addAttribute("scrollbars", this.Scrollbars);
		specs+=addAttribute("resizable", this.Resizable);
		specs+=addAttribute("titlebar", this.TitleBar);
		if (this.Fullscreen == 1) specs+=addAttribute("fullscreen", this.Fullscreen);
		if (this.copyhistory == 1) specs+=addAttribute("copyhistory", this.copyhistory);
		

 		var win;
 		if (gx.lang.emptyObject(this.Targets) || this.Targets.length ==0){
			if (this.askfocus){
				win = window.open(this.Target,this.Name,specs).focus();
					}
			else{
				 win = window.open(this.Target,this.Name,specs);
				
			}
		}
		else{
		var i = 0;
			for ( i = 0; i<this.Targets.length;i++)
			{
				win = window.open(this.Targets[i].Target,this.Name,specs);
				
			}
		}
		
		if (gx.lang.gxBoolean(this.RefreshParentOnClose)){
				if (gx.util.browser.isFirefox() || gx.util.browser.isChrome()){ //FF Chrome
					var funcCloseFF = function (e) {
											  window.parent.gx.O.executeServerEvent( 'RFR', true);
											};
					win.onbeforeunload = funcCloseFF;
				}
				if (gx.util.browser.isIE()){ //IE
									var funcClose = function (e) {
				
										 window.gx.O.executeServerEvent( 'RFR', true);
									};
									win.attachEvent( "onbeforeunload", funcClose);
				}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

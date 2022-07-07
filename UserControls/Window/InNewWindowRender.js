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
		var fullscreen1 = (this.Fullscreen=='true')?'yes':'no';		
		var location1 = (this.Location==true)?'yes':'no';
		var menubar1 = (this.MenuBar==true)?'yes':'no';
		var resizable1 = (this.Resizable==true)?'yes':'no';		
		var scrollbars1 = (this.Scrollbars==true)?'yes':'no';
		var titlebar1 = (this.TitleBar==true)?'yes':'no';
		var toolbar1 = (this.ToolBar==true)?'yes':'no';
		var directories1 = (this.directories==true)?'yes':'no';
		var status1 = (this.status==true)?'yes':'no';
		var copyhistory1 = (this.copyhistory==true)?'yes':'no';
		var fitscreen1= (this.fitscreen==true)?'yes':'no';
 		var width1 = '';
		var height1 = '';  
		if (fitscreen1 == 'yes'){
		   var widthNro = (screen.width-25);
		   var heightNro = (screen.height-125);
		   width1 = widthNro ;
		   height1 = heightNro;

		}else{
                    width1 = this.Width;
		    height1 = this.Height;
		}
		  
		var specs='width='+width1 +',';
		specs+='height='+height1 +',';
		specs+='left='+this.left+',';
		specs+='top='+this.top+',';
		specs+='toolbar=' +toolbar1+',';
		specs+='location='+location1+',';
		specs+='directories='+directories1+',';
		specs+='status='+status1+',';
		specs+='menubar='+menubar1+',';
		specs+='scrollbars='+scrollbars1+',';
		specs+='resizable='+resizable1+ ',';
		specs+='titlebar=' + titlebar1+ ',';
		specs+='copyhistory='+copyhistory1;
 
 		var win;
 		if (gx.lang.emptyObject(this.Targets) || this.Targets.length ==0){
			if (this.askfocus == true){
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

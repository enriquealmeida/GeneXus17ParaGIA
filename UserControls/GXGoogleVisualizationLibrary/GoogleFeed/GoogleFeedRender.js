function GoogleFeed()
{
	this.FeedData;
	this.Width;
	this.Height;
	this.NumEntries;
	this.Reload;
	this.Type;
	this.CssClasses = [];

	// Databinding for property FeedData
	this.SetFeedData = function(data)
	{
		///UserCodeRegionStart:[SetFeedData] (do not remove this comment.)
		this.FeedData = data;		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property FeedData
	this.GetFeedData = function()
	{
		///UserCodeRegionStart:[GetFeedData] (do not remove this comment.)
		return this.FeedData;		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if (!this.IsPostBack)
		{
				var _this = this;
				google.load("feeds", "1", { callback: function() { _this.Initialize() } });			
				google.setOnLoadCallback(OnLoad); 
	      
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	this.Initialize = function()
	{
		this.getContainerControl().style.width = gx.dom.addUnits(this.Width);
		this.getContainerControl().style.height = gx.dom.addUnits(this.Height);
		this.LoadCss();
		this.DrawControl();				
	}

	this.DrawControl = function()
	{
		this.getContainerControl().style.visibility = "hidden";
		switch(this.Type)
		{
			case "simple":
				this.DrawSimpleControl();
				break;
			case "preview":
				this.DrawPreviewControl();
				break;
		}
		
	}
	
	this.Refresh = function()
	{
		this.DrawControl();	
	}
	
	this.DrawSimpleControl = function()
	{
		this.feedControl = new google.feeds.FeedControl();
		for(i=0 ; this.FeedData[i]!=undefined ; i++)
		{
			this.feedControl.addFeed(this.FeedData[i].Url, this.FeedData[i].Title);
		}
		this.feedControl.setNumEntries(this.NumEntries);
		this.feedControl.setLinkTarget(eval(this.GetLinkTarget(this.LinkTarget)));
		this.feedControl.draw(this.getContainerControl());
		
		this.CssClasses["control"] = "gfc-control";
		this.CssClasses["header"] = "gfc-resultsHeader";
		this.CssClasses["headerText"] = "gfc-title";
		this.CssClasses["link"] = "gf-title";
		this.CssClasses["snippet"] = "gf-snippet";
		this.CssClasses["time"] = "gf-relativePublishedDate";
		this.SetCssCustomProperties();  
	}

	this.DrawPreviewControl = function()
	{
		var auxFeed;
		var feedArray = new Array();
		for(i=0 ; this.FeedData[i]!=undefined ; i++)
		{
			auxFeed = new Object();			
			auxFeed.title = this.FeedData[i].Title;
			auxFeed.url = this.FeedData[i].Url;
			feedArray.push(auxFeed);			
		}		
		this.feedControl = new GFdynamicFeedControl(feedArray,this.ContainerName,{numResults : this.NumEntries, stacked : true, title: " ", feedLoadCallback:this.CreateDelegate(this,this.OnFeedLoaded)});	
		
		this.CssClasses["control"] = "gfg-root";
		this.CssClasses["header"] = "gfg-subtitle";
		this.CssClasses["headerText"] = "gfg-subtitle a";
		this.CssClasses["time"] = "gf-relativePublishedDate";
		this.CssClasses["link"] = "gfg-listentry a:link";
		this.CssClasses["snippet"] = "gf-snippet";
		
		
	}
	
	this.OnFeedLoaded = function()
	{
		var controls = this.getElementsByClass(this.CssClasses["control"]);
		var realControl = "";
		if (controls.length != 0)
		{
			for (var i=0; controls[i]!=undefined; i++)
			{
				if (controls[i].parentNode.id == this.ContainerName)
				{
					realControl = controls[i];
					continue;
				}	
			}		
			var headers = this.getElementsByClass(this.CssClasses["header"], realControl); 
			if (headers.length == this.FeedData.length)
			{
				this.SetCssCustomProperties();		
			}
			else
				setTimeout(this.CreateDelegate(this,this.OnFeedLoaded), 200);
		}
		else
			setTimeout(this.CreateDelegate(this,this.OnFeedLoaded), 200);
		
	}
	
	this.LoadCss = function()
	{
			var imgsDir = gx.staticDirectory.substring(1,gx.staticDirectory.length);
			var headID = document.getElementsByTagName("head")[0];
	    var cssNode = document.createElement('link');
	    cssNode.type = 'text/css';
	    cssNode.rel = 'stylesheet';
	    cssNode.media = 'screen';
	    var css;
	    if (this.Css == "Default")
	    {
	    	if (this.Type == "preview") 
	    		css = "gfdynamicfeedcontrol.css";
	    	else
	    		css = "feedControl.css";	  	    		
	    }
	    else
	    	css = this.Css;
	    cssNode.href = gx.util.resourceUrl(imgsDir + 'GXGoogleVisualizationLibrary/GoogleFeed/css/'+ css, true);
	    headID.appendChild(cssNode);	
	}

	this.CreateDelegate = function(that, thatMethod)
  {
  	return function() { return thatMethod.call(that); }
  }			
  
  this.SetCssCustomProperties = function()
  {
  	
  	
    var rule = "." + this.CssClasses["link"];
    var ruleText = this.LinkFont + "color:" + this.GetColor(this.LinkColor);
    this.InsertCssRule(rule, ruleText);
    
    rule = "." + this.CssClasses["snippet"];;
    ruleText = this.SnippetFont + "color:" + this.GetColor(this.SnippetColor);
    this.InsertCssRule(rule, ruleText);
    
    rule = "." + this.CssClasses["time"];
    ruleText = this.TimeFont;
    if (this.TimeFont.indexOf("color") < 0 )
    	ruleText+=  ";color:" + this.GetColor(this.TimeColor) + " !important";    
    this.InsertCssRule(rule, ruleText);
    
    rule = ".gf-author";
    ruleText = this.TimeFont;
    if (this.TimeFont.indexOf("color") < 0)
    	ruleText+=  ";color:" + this.GetColor(this.TimeColor) + " !important";    
    this.InsertCssRule(rule, ruleText);

    var controls = this.getElementsByClass(this.CssClasses["control"]);
    var realControl;
    for (var i = 0; controls[i]!=undefined;i++)
    {
	    if (controls[i].parentNode.id == this.ContainerName)
	    {    	
	    	realControl = controls[i];
	    	if (!this.Border)
	    		controls[i].style.border = "0px";
	    	continue;
	    }	    
  	}
  	
  	controls = this.getElementsByClass(this.CssClasses["header"], realControl);  	
    for (var i = 0; controls[i] != undefined; i++)
    {
    	controls[i].style.backgroundColor = this.GetColor(this.HeaderBgColor);	
    	if (!this.ShowTitle)    
    		controls[i].style.display = "none";	    	    	
    }
  	
  	if (this.Type == "simple")
  		this.SetSimpleCustomProperties(realControl);
  	if (this.Type == "preview")
  		this.SetPreviewCustomProperties(realControl);
  	
  	
    
    this.getContainerControl().style.visibility = "visible";  			
  }
  
  this.SetPreviewCustomProperties = function(realControl)
  {
  	var rule = "." + this.CssClasses["headerText"];
    var ruleText = this.HeaderFont + "color:" + this.GetColor(this.HeaderTextColor);
    this.InsertCssRule(rule, ruleText);	
    
    rule = ".gf-title";
    ruleText = this.LinkFont + "color:" + this.GetColor(this.LinkColor);
    this.InsertCssRule(rule, ruleText);	
    
    rule = ".gfg-listentry-odd";
    ruleText = "background-color:" + this.GetColor(this.LinesColorOdd);
    this.InsertCssRule(rule, ruleText);	
    
    rule = ".gfg-listentry-even";
    ruleText = "background-color:" + this.GetColor(this.LinesColorEven);
    this.InsertCssRule(rule, ruleText);	
    
    if (this.SelectionImage == "")
    {
    	rule = ".gfg-listentry-highlight";
    	ruleText = "background-image:url('" + gx.util.resourceUrl("GXGoogleVisualizationLibrary/GoogleFeed/css/garrow.gif", true)+ "');background-position:left center;background-repeat:no-repeat;"
    }
    else
    {
    	rule = ".gfg-listentry-highlight";
    	ruleText = "background-image:url('" + gx.util.resourceUrl(this.SelectionImage, true)+ "');background-position:left center;background-repeat:no-repeat;"	
   	}
   	this.InsertCssRule(rule, ruleText);	
    
    
    
  }
  
  this.SetSimpleCustomProperties = function(realControl)
  {  	
    controls = this.getElementsByClass(this.CssClasses["headerText"], realControl);  	
    for (var i = 0; controls[i] != undefined; i++)
    {
    	controls[i].style.cssText = this.HeaderFont + "color:" + this.GetColor(this.HeaderTextColor);	
    }	
  }
  
  this.GetColor = function(color)
  {
  	if (color.Html)
  		return color.Html;
  	return color;	
  }
  
  
    this.InsertCssRule = function(rule, ruleText, position) {
    		
        var i = 0;
        if (position == "last")
            i = (gx.util.browser.isIE()) ? document.styleSheets[0].rules.length : document.styleSheets[0].cssRules.length;
        if (gx.util.browser.isIE())
            document.styleSheets[0].addRule(rule, ruleText, i);
        else
            document.styleSheets[0].insertRule(rule + " { " + ruleText + " } ", i);        
    }
  
  this.getElementsByClass = function(searchClass,node,tag) {
		var classElements = new Array();
		if ( node == null )
			node = document;
		if ( tag == null )
			tag = '*';
		var els = node.getElementsByTagName(tag);
		var elsLen = els.length;
		var pattern = new RegExp('(^|\\\\s)'+searchClass+'(\\\\s|$)');
		for (i = 0, j = 0; i < elsLen; i++) {
			if ( pattern.test(els[i].className) ) {
				classElements[j] = els[i];
				j++;
			}
		}
		return classElements;
	}		
  
  
  this.GetLinkTarget = function(linkTarget)
  {
  	switch(linkTarget)
  	{
  		case "_blank":
  			return 	"google.feeds.LINK_TARGET_BLANK" ;
  		case "_self":
  			return 	"google.feeds.LINK_TARGET_SELF" ;
  		case "_top":
  			return 	"google.feeds.LINK_TARGET_TOP" ;
  		case "_parent":
  			return 	"google.feeds.LINK_TARGET_PARENT";		
  	}     	
  }
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

function FBHoverPanel()
{
	this.Width;
	this.Height;
	this.AutoHide;
	this.Shown = false;



	this.show = function()
	{
	    ///UserCodeRegionStart:[show] (do not remove this comment.)
	
			if (!this.IsPostBack)
			{
				//this.ControlCreated = true;
				gx.evt.on_ready(window, this.CreateDelegate(this, this.CreateHtml));
				this.CreateHtml();
				//gx.evt.on_ready(window, this.CreateDelegate(this, this.SetEvents));				
				this.SetEvents();
			}
			else
			{
				if (this.Ready)
				{
					this.getChildContainer("ContextDataContainer").style.visibility = "hidden";
					this.CreateHtml();
				}	
			}
			//UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remo//ve this comment.)
	
	this.CreateHtml = function()
	{
	    //set childs
	    	this.getContainerControl().appendChild(this.getChildContainer("DataContainer"));
        this.getContainerControl().appendChild(this.getChildContainer("ContextTitleContainer"));
        
        document.forms[0].appendChild(this.getChildContainer("ContextDataContainer"));
        
        //arrange data container
        this.setFloat(this.getChildContainer("DataContainer"),"left");
        this.getChildContainer("DataContainer").style.display = "inline";
        this.getChildContainer("DataContainer").style.width = "auto";		  
          	  	  
        //arrange context title container
        this.setFloat(this.getChildContainer("ContextTitleContainer"),"left");
        this.getChildContainer("ContextTitleContainer").style.display = "inline";
        this.getChildContainer("ContextTitleContainer").style.width = "auto";
        if (this.CBoolean(this.AutoHide) == true)
            this.getChildContainer("ContextTitleContainer").style.visibility = "hidden";
        else
            this.getChildContainer("ContextTitleContainer").style.visibility = "visible";
        this.getChildContainer("ContextTitleContainer").style.cursor = "pointer";		  
        		  

        //arrange context data container
        this.getChildContainer("ContextDataContainer").style.position = "absolute";
        this.getChildContainer("ContextDataContainer").style.display = "inline";        
        this.getChildContainer("ContextDataContainer").style.visibility = "hidden";
        this.getChildContainer("ContextDataContainer").style.cursor = "pointer";
        this.getChildContainer("ContextDataContainer").style.zIndex = 50000;  
        this.getChildContainer("ContextDataContainer").style.overflowY = "auto";
        this.getChildContainer("ContextDataContainer").style.overflowX = "auto";    

        //sets main div width and height

        if (this.GridRow == "")
        {	
        	
        	this.getContainerControl().style.height = this.getChildContainer("DataContainer").clientHeight;
        	this.getContainerControl().style.width = gx.dom.addUnits(this.getChildContainer("DataContainer").offsetWidth + this.getChildContainer("ContextTitleContainer").offsetWidth + 20);
        }
        else
        {
        	this.getContainerControl().style.height = "100%";
        	this.getContainerControl().style.position = "relative";
        	this.getChildContainer("ContextTitleContainer").style.position = "absolute";
					this.getContainerControl().style.width = gx.dom.addUnits(this.getChildContainer("DataContainer").offsetWidth + this.getChildContainer("ContextTitleContainer").offsetWidth + 20);
        	if (this.CBoolean(this.AutoFit) == true)
        	{
        		this.getContainerControl().style.width = "100%";
        		this.getChildContainer("DataContainer").style.width = gx.dom.addUnits(this.getContainerControl().clientWidth - this.getChildContainer("ContextTitleContainer").clientWidth);
        		this.getChildContainer("ContextTitleContainer").style.right = 0;		
        	}
        	
        }
        this.Ready = true;
	}
	
	this.PositionHoverPanel = function()
	{		
		var p = this.getChildContainer("ContextTitleContainer");
        var contextDataContainerDimensions = gx.dom.dimensions(this.getChildContainer("ContextDataContainer"));
        var bodyDimensions = gx.dom.dimensions(document.body);
        var titleDimensions = gx.dom.dimensions(this.getChildContainer("ContextTitleContainer"));        
        this.getChildContainer("ContextDataContainer").style.top = gx.dom.addUnits(gx.dom.position(p).y);		
        var positionX;
        var xAlignLeft = gx.dom.position(p).x - contextDataContainerDimensions.w;
        var xAlignRight = gx.dom.position(p).x + titleDimensions.w;;
        
        if (this.Alignment == "left")
        	if (xAlignLeft > 0)
        		positionX = xAlignLeft;
        	else
        	{
        		positionX = xAlignRight;
        		this.Alignment = "right";
        		
        	}
        if (this.Alignment == "right")
        {
        	if (xAlignLeft + contextDataContainerDimensions.w < bodyDimensions.w)
        		positionX = xAlignRight;
        	else
        	{
        		positionX = xAlignLeft;
        		this.Alignment = "left";
        	}
        }
        this.getChildContainer("ContextDataContainer").style.left = gx.dom.addUnits(positionX);
        this.getChildContainer("ContextDataContainer").align = (this.Alignment == "right")? "left":"right";
	}	
	
	this.SetEvents = function()
	{
	    this.getContainerControl().onmouseout = this.CreateDelegate(this,this.HideContextHeader);
			this.getContainerControl().onmouseover = this.CreateDelegate(this,this.ShowContextHeader);
			gx.evt.attach( document, "click", this.CreateDelegate(this,this.HideContextDataContainer));
			gx.evt.attach( this.getChildContainer("ContextTitleContainer"), "click", this.CreateDelegate(this,this.ShowContextDataContainer));			
			
	}	

	this.ShowContextHeader = function()
	{		
        this.getChildContainer("ContextTitleContainer").style.visibility = "visible";
	}

	this.HideContextHeader = function()
	{
		if (this.CBoolean(this.AutoHide) == true && this.getChildContainer("ContextDataContainer").style.visibility != "visible")
		{
			this.getChildContainer("ContextTitleContainer").style.visibility = "hidden";		
		}
	}
	
	this.ShowContextDataContainer = function(evt)
	{		
		if (this.ActionClicked != undefined)
		{
		    this.ActionClicked();
		    this.FromServer = true;		    
		}
		if (!this.FirstShow && this.CBoolean(this.DropDownSectionVisible))
		{			
			this.FirstShow = true;
			this.getChildContainer("ContextDataContainer").style.width = gx.dom.addUnits(this.getChildContainer("ContextDataContainer").clientWidth + 18); //overflowY scroll
			this.SetContextDataSize();
		}
		this.PositionHoverPanel();
		gx.fn.setOpacity(0,gx.dom.id(this.getChildContainer("ContextDataContainer")));		
		this.getChildContainer("ContextDataContainer").style.visibility = "visible";				
		gx.fn.fadeIn(this.getChildContainer("ContextDataContainer"), this.FadeInSpeed);
		this.Shown = true;		
		gx.evt.cancel(evt, true);
	}	
	
	this.SetContextDataSize = function()
	{
		if (this.getChildContainer("ContextDataContainer").clientHeight > this.MaxHeight)
			this.getChildContainer("ContextDataContainer").style.height = 	gx.dom.addUnits(this.MaxHeight);
		else
			this.getChildContainer("ContextDataContainer").style.height = this.getChildContainer("ContextDataContainer").clientHeight;	
		if (this.getChildContainer("ContextDataContainer").clientWidth > this.MaxWidth)
			this.getChildContainer("ContextDataContainer").style.width = 	gx.dom.addUnits(this.MaxWidth);
		else
			this.getChildContainer("ContextDataContainer").style.width = this.getChildContainer("ContextDataContainer").clientWidth;	
	}
	
this.HideContextDataContainer = function(t)
	{		    
	    if (!this.FromServer && this.Shown)
	    {       
	        if (!gx.fx.isUnderMouse(this.getContainerControl()) && !gx.fx.isUnderMouse(this.getChildContainer("ContextDataContainer")) && !gx.fx.isUnderMouse(this.getChildContainer("ContextTitleContainer")))
			{	     	        				
				this.getChildContainer("ContextDataContainer").style.visibility = "hidden";
				this.HideContextHeader();
				this.Shown = false;              
				
			}	
        }
        this.FromServer = false;               
    }
	
	this.CreateDelegate = function(obj, method, args, appendArgs){
        return function() {
            var callArgs = args || arguments;
            if(appendArgs === true){
                callArgs = Array.prototype.slice.call(arguments, 0);
                callArgs = callArgs.concat(args);
            }else if(typeof appendArgs == "number"){
                callArgs = Array.prototype.slice.call(arguments, 0);
                var applyArgs = [appendArgs, 0].concat(args);
                Array.prototype.splice.apply(callArgs, applyArgs);
            }
            return method.apply(obj || window, callArgs);
        };
    }
    
   
    this.setFloat = function(elem, side)
    {
        if (elem.style.styleFloat || elem.style.styleFloat == "")
            elem.style.styleFloat = side;
        else
            elem.style.cssFloat = side;        
    }
    
    this.CBoolean = function(str)
    {
        if(str){
      	    if(typeof(str) == 'string')
        	    return (str.toLowerCase() == "true")
            return str;
        }
        else
      	    return false;
    }	
	
	this.position =  function(el) {
            var left = 0;
            var top  = 0;
            if (el.getBoundingClientRect) 
            {
                var viewportElement = document.documentElement;  
                var box = el.getBoundingClientRect();
                var scrollLeft = viewportElement.scrollLeft;
                var scrollTop = viewportElement.scrollTop;

                left = box.left + scrollLeft;
                top = box.top + scrollTop;
            }
            else
            {
                while (el.offsetParent)
                {
                    left += el.offsetLeft + (el.currentStyle?(gx.num.intval(el.currentStyle.borderLeftWidth)):0) - (el.scrollLeft || 0);
                    top += el.offsetTop + (el.currentStyle?(gx.num.intval(el.currentStyle.borderTopWidth)):0) - (el.scrollTop || 0);
                    el = el.offsetParent;
                }
                left += el.offsetLeft + (el.currentStyle?(gx.num.intval(el.currentStyle.borderLeftWidth)):0);
                top += el.offsetTop + (el.currentStyle?(gx.num.intval(el.currentStyle.borderTopWidth)):0);
            }
            return {x:left, y:top};
        }
    

	///UserCodeRegionEnd: (do not remove this comment.):
}



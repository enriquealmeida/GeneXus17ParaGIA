function ImageGallery()
{
	this.Rows;
	this.Columns;
	this.BorderWidth;
	this.Border_Color;
	this.BackgroundColor;
	this.BackgroundImage;
	this.LoadingImage;
	this.ThumbnailWidth;
    this.ThumbnailHeight;
	this.CellSpace;
	this.CellPad;
	this.ResizeSpeed;
	this.ImagesData;
	this.Width;
	this.Height;
	this.Type;
	this.SelectedImageId; 
	this.ControlCreated;
	this.Scrolling;
	this.InternalCurrentImage;
	this.ImageMapper = new Array();

// Databinding for property ImagesData
	this.GetImages = function()
	{
		///UserCodeRegionStart:[GetImages] (do not remove this comment.)
		return this.ImagesData;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property ImagesData
	this.SetImages = function(data)
	{
		///UserCodeRegionStart:[SetImages] (do not remove this comment.)
        this.ImagesData = data; 
        if (!this.ControlCreated)
		{
		    for(var i=0;this.ImagesData[i]!=undefined;i++)
	        {
	            var myId = this.ImagesData[i].Id;
	            this.ImageMapper[gx.text.trim(myId)] = i;	            
	        }	 
		}               
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function(data) {
	    ///UserCodeRegionStart:[show] (do not remove this comment.)
	    //
	    if (!this.IsPostBack) {
	    	
	    		this.LoadCss();
	    	
	        document.body.noWrap = false;
	        var buffer = "";
	        if (this.Type == "table")
	            buffer += this.CreateLightBoxImageGallery();
	        if (this.Type == "slider")
	            buffer += this.CreateFrogJSSlider();

	        Frog.prototype.MyGXUserControlStatic = this;
	        Lightbox.prototype.MyGXUserControlStatic = this;
	        this.setHtml(buffer.toString());
	        this.ControlCreated = true;		
	        if (this.Type == "table")
	        	initLightbox();			
	        if (this.Type == "slider")
	        	initFrog();

	    }
	    else {
	        this.UpdateCurrentImage();
	    }

	    ///UserCodeRegionEnd: (do not remove this comment.)
	}
	
	this.LoadCss = function()
	{
			var imgsDir = gx.staticDirectory.substring(1,gx.staticDirectory.length);
			var headID = document.getElementsByTagName("head")[0];
	    var cssNode = document.createElement('link');
	    cssNode.type = 'text/css';
	    cssNode.rel = 'stylesheet';
	    cssNode.media = 'screen';	    
			cssNode.href = gx.util.resourceUrl(imgsDir + 'ImageGallery/CSSResources/frog.css', true);
	    headID.appendChild(cssNode);

			var cssNode2 = document.createElement('link');
	    cssNode2.type = 'text/css';
	    cssNode2.rel = 'stylesheet';
	    cssNode2.media = 'screen';	    
			cssNode2.href = gx.util.resourceUrl(imgsDir + 'ImageGallery/CSSResources/lightbox.css', true);
	    headID.appendChild(cssNode2);	    	    
	}
	
	
	this.CreateLightBoxImageGallery = function()
	{

	    resizeSpeed = this.ResizeSpeed;
	    
	    var buffer = "";
	    if (this.Columns > 0)
	    {
	      //buffer+= '<table width="' + this.Width + '" height="' + this.Height + '" border="' + this.BorderWidth + '" cellpadding="' + this.CellPad + '" cellspacing="' + this.CellSpace + '" background="' + this.BackgroundImage + '" style="background-color:rgb(' + this.BackgroundColor.R +  ',' + this.BackgroundColor.G + ',' + this.BackgroundColor.B + ');border:'  + this.BorderWidth + 'px solid rgb(' + this.Border_Color.R + ',' + this.Border_Color.G + ',' +this.Border_Color.B +  ')">';
	      buffer+= '<table width="' + this.Width + '" border="' + this.BorderWidth + '" cellpadding="' + this.CellPad + '" cellspacing="' + this.CellSpace + '" background="' + this.BackgroundImage + '" style="background-color:rgb(' + this.ReturnColor(this.BackgroundColor) + ');border:'  + this.BorderWidth + 'px solid rgb(' + this.ReturnColor(this.Border_Color) +  ')">';  		    
		    if (this.Rows > 0 && this.Columns > 0)
	            buffer+= this.CreateXYImageGallery();
		    if (this.Rows == 0)
		        buffer+= this.CreateVerticalImageGallery();		    
		
	    	buffer+= '</table>';	
		
	    }
		return buffer;			
	}
	
	this.CreateXYImageGallery = function()
	{
	    var buffer = "";
	    var imageIndex = 0;
	    for(i=0; i < this.Rows;i++)
		{
    	    buffer+='<tr>';
    	    for(j=0; j < this.Columns;j++)
		    {
		        if (this.ImagesData[imageIndex]!=undefined)
		        {
		            if (this.ImagesData[imageIndex].Thumbnail == "")
		                buffer+='<td><a href="' + this.ImagesData[imageIndex].Image + '" rel="lightbox[' + this.ContainerName + ']" title="' + this.ImagesData[imageIndex].Caption + '"><img style="border:0px;" width="' + this.ThumbnailWidth + '" height="' + this.ThumbnailHeight + '" src="' + this.ImagesData[imageIndex].Image  + '" /></a></td>';
		            else
		                buffer+='<td><a href="' + this.ImagesData[imageIndex].Image + '" rel="lightbox[' + this.ContainerName + ']" title="' + this.ImagesData[imageIndex].Caption + '"><img style="border:0px;" src="' + this.ImagesData[imageIndex].Thumbnail  + '" /></a></td>';
                }                           
                imageIndex++;                
		    }
		    buffer+='</tr>';		    
        }	  
        return buffer;
	}
	
	this.CreateVerticalImageGallery = function()
	{
	    var buffer = "";
	    var imageIndex = 0;
	    while (this.ImagesData[imageIndex]!=undefined)
		{
    	    buffer+='<tr>';
    	    for(j=0; j < this.Columns;j++)
		    {
		        if (this.ImagesData[imageIndex]!=undefined)
		        {
		            buffer+='<td><a href="' + this.ImagesData[imageIndex].Image + '" rel="lightbox[' + this.ContainerName + ']" title="' + this.ImagesData[imageIndex].Caption + '"><img src="' + this.ImagesData[imageIndex].Thumbnail  + '" /></a></td>';
                }	
                else
                {
                    buffer+='<td></td>';
                }                
                imageIndex++;
		    }
		    buffer+='</tr>';		    
        }	  
        return buffer;
	}	
		
	this.CreateFrogJSSlider = function()
	{
	    var buffer = "";
	    buffer+='<div id="FrogJS" align="center">';
	    for(var i=0;this.ImagesData[i]!=undefined;i++)
	    {
	        buffer+='<a href="' + this.ImagesData[i].Image + '"><img src="' + this.ImagesData[i].Thumbnail + '" alt="' + this.ImagesData[i].Caption + '" /></a>';
	        if (i==0) this.SelectedImageId = this.ImagesData[i].Id;
	    }	    
	    buffer+='</div>'
	    return buffer;
	}
	
	this.ReturnInternalIdFromImage = function(imageIndex)
	{
	    for (x in this.ImageMapper)
        {
            if (this.ImageMapper[x] == imageIndex)
                return x;
        }
	}
	
	this.UpdateCurrentImage = function()
	{
	    if (!this.Scrolling)
		{		    
		    var nextImageNumber = this.ImageMapper[gx.text.trim(this.SelectedImageId)];
		    if (nextImageNumber != this.InternalCurrentImage)
		    {
		        var imgPreloader = new Image();
		        imgPreloader.src = frogjsImageArray[nextImageNumber]['full'];
		        var side = (nextImageNumber > this.InternalCurrentImage)?"right":"left";		         
		        myFrog.loadImage(this.ImageMapper[gx.text.trim(this.SelectedImageId)],side,imgPreloader.width,false);
		    }		   
		 }
		 this.Scrolling = false;
	}
	
	this.OnImageChanged = function(imageNum)
	{
		if (this.ImageChanged)
		{
			this.SelectedImageId = this.ReturnInternalIdFromImage(imageNum);
			this.Scrolling = true;
			this.ImageChanged();
		}
	}
	
	this.ReturnColor = function(color)
	{
		if (color != undefined) 
			if (color.R != undefined) {
				return color.R + ',' + color.G + ',' + color.B;
			} else {
				var htmlcolor = gx.color.html(color);
				return htmlcolor.R + ',' + htmlcolor.G + ',' + htmlcolor.B;
			}
	}	
}


function StarRating()
{
	this.MaxStars;
	this.TooltipText;
	this.Attribute;
	this.Enabled;

	// Databinding for property Attribute
	this.SetAttribute = function(data)
	{
		///UserCodeRegionStart:[SetAttribute] (do not remove this comment.)
		this.Attribute = data;

		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Attribute
	this.GetAttribute = function()
	{
		///UserCodeRegionStart:[GetAttribute] (do not remove this comment.)
		return this.ClickedValue;
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		
			var buffer = '<div id="rateMe" title="' + this.TooltipText + '">';
			for (i=1; i<=this.MaxStars; i++) {
				className = '';
				if (i<=this.Attribute)
					className='on';
				buffer += '<a onclick="' + this.me() + '.rateIt(this)" id="_' + this.ContainerName + i + '" onmouseover="' + this.me() + '.rating(this)" onmouseout="' + this.me() + '.off(this)" class="' + className + '"></a>';
			}
			buffer += '</div>';
			
			this.setHtml(buffer);
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	// Rollover for image Stars //
	this.rating = function(num)
	{
		if (gx.lang.gxBoolean(this.Enabled))
		{
			this.sMax = this.MaxStars;	// Isthe maximum number of stars
			/*if (num.parentNode!=null){
				for(n=0; n<num.parentNode.childNodes.length; n++){
					if(num.parentNode.childNodes[n].nodeName == "A"){
						this.sMax++;	
					}
				}
			}*/
 			if(!this.rated){
				s = num.id.replace("_" + this.ContainerName, ''); // Get the selected star
				a = 0;
				for(i=1; i<=this.sMax; i++){		
					if(i<=s){
						document.getElementById("_" + this.ContainerName + i).className = "on";
						this.holder = a+1;
						a++;
					}else{
						document.getElementById("_" + this.ContainerName + i).className = "";
					}
				}
			}
		}
	}
	
		// For when you roll out of the the whole thing //
	this.off = function(me)
	{
		if (gx.lang.gxBoolean(this.Enabled))
		{		
			if(!this.rated){
				if(!this.preSet){	
					for(i=1; i<=this.sMax; i++){
						if (i <= this.Attribute)
							document.getElementById("_"+ this.ContainerName + i).className = "on";
						else
							document.getElementById("_"+ this.ContainerName + i).className = "";
					}
				}else{
					this.rating(this.preSet);
				}
			}
		}
	}
	
	// When you actually rate something //
	this.rateIt = function(me)
	{
		if (gx.lang.gxBoolean(this.Enabled))
		{		
			if(!this.rated){
				this.preSet = me;
				//this.rated=1;
				this.sendRate(me);
				//this.rating(me);			
			}
		}
	}
	
	// Send the rating information somewhere using Ajax or something like that.
	this.sendRate = function(sel)
	{
		if (gx.lang.gxBoolean(this.Enabled))
		{		
			this.Attribute = sel.id.replace("_" + this.ContainerName, '');
			this.ClickedValue = this.Attribute;
			if (this.RatingSelected != undefined)
						this.RatingSelected();	
		}				
	}

	
	

	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

function HoverPanel()
{
	this.image;
	this.face;
	this.size;
	this.color;
	this.controlText;
	this.verticalPos;
	this.horizontalPos;
	this.verticalOffset;
	this.horizontalOffset;
	this.topPos;
	this.leftPos;
	this.hasTitle;
	this.titleText;
	this.title_Fore_Color;
	this.title_Back_Color;
	this.position;
	this.panelWidth;
	this.panelHeight;
	this.effect;
	this.Width;
	this.Height;
	this.controlType;
	this.panelLink;
	this.clickToFix;
	this.AttachedControl;
	this.linkParameters;
	this.ParameterList;

	// Databinding for property Attribute
	this.SetParameterList = function(data)
	{
		///UserCodeRegionStart:[SetAttribute] (do not remove this comment.)
		this.ParameterList = data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Attribute
	this.GetParameterList = function()
	{
		///UserCodeRegionStart:[GetAttribute] (do not remove this comment.)
        return this.ParameterList;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
                
        ///UserCodeRegionStart:[show] (do not remove this comment.)
		var ajaxwin;
		var control;
		var ctrlName = this.AttachedControl;
		
		var titleFC = 'rgb(' + this.title_Fore_Color.R + ',' + this.title_Fore_Color.G + ',' + this.title_Fore_Color.B + ')'
		var titleBC = 'rgb(' + this.title_Back_Color.R + ',' + this.title_Back_Color.G + ',' + this.title_Back_Color.B + ')'
		var attr = 'width=' + this.panelWidth + 'px,height=' + this.panelHeight + 'px,top=' + this.topPos + 'px,left=' + this.leftPos + 'px';
									
		var plink = this.panelLink;
		var effect = this.effect;
		var htitle = this.hasTitle;
		var ttext = this.titleText;		
		var pos = this.position;
		var vpos = this.verticalPos;
		var hpos = this.horizontalPos;
		var voff = this.verticalOffset;
		var hoff = this.horizontalOffset;
		var param = this.ParameterList;
		var linkp = this.linkParameters;
		var cfix = this.clickToFix;
		
		var setLinkPopUpLevel = function(link) {
			var newLevel = 'gxPopupLevel%3D99%3B';
			return link.indexOf("?") == -1 ?  link + "?" + newLevel : link + "," + newLevel;
		}
		
		var omu_handler = function() 
		{
            ajaxwin.setClick(cfix); 
            return false;
		}
	
		var omo_handler = function(mthis)
		{
            var mplink = plink;
            if (linkp == "Variable")
            {                    
                var aux;
                var ctr;
                var ind = mplink.indexOf("?")
                if (ind != -1)
                {
                    mplink = mplink.substr(0,ind)
                }
                mplink = mplink + "?"
                for (var i=0; i<param.length; i++)
                { 
                    aux = "span_" + param[i].substr(0,param[i].lastIndexOf("_")) + "_" + mthis.id.substr(mthis.id.length - 4);
                    ctr = document.getElementById(aux);
			        if((ctr != null) && ctr != "")			
                    {
				        mplink = mplink + ctr.innerHTML + ",";			        
                    }
                    else
	                { 
                        aux = aux.substr(5);
                        ctr = document.getElementById(aux);
                        if((ctr != null) && ctr != "")			
                        {
				            mplink = mplink + ctr.value + ",";	
                        }     
                   }
                }
                mplink = mplink.substr(0,mplink.length - 1);
            }
            
            ajaxwin=dhtmlwindow.open('ajaxbox', 'ajax', setLinkPopUpLevel(mplink), effect, htitle, ttext, titleFC, titleBC, attr, 0, mthis.id, pos, vpos, hpos, voff, hoff); return false;
		}
        
        var buffer;
        var cname;
        var onload_handler = function() 
        {
            var ok = 1;
            var num = 1;
		    var numtxt;
            var cname1 = ctrlName.substr(0,ctrlName.length-5);	
                
            while (ok==1) 
            {
	           numtxt = ("000" + num).slice(-4);
               cname = "span_" + ctrlName.substr(0,ctrlName.length-5)+ "_" + numtxt;
	           control = document.getElementById(cname);
			   if((control != null) && control != "")			
               {
			      control.onmouseover = function() {omo_handler(this)};
			      control.onmouseup = function() { omu_handler(); };
                  control.onmouseout = function() { ajaxwin.hide(); return false; };
			      num = num + 1;
			   }
               else
	           { 
                  cname = cname.substr(5);
                  control = document.getElementById(cname);
                  if((control != null) && control != "")			
                  {
			         control.onmouseover = function() {omo_handler(this)};
			         control.onmouseup = function() { omu_handler(); };
                     control.onmouseout = function() { ajaxwin.hide(); return false; };
			         num = num + 1;
                  }
                  else
	              { 
                     ok = 0; 
                  }
              }
		    }
        }

        gx.fx.obs.addObserver("grid.onafterrefresh",window, onload_handler);
        		
        switch (this.controlType)
        {
            case "TextBlock" :
            
                buffer = '';
                buffer += '<span id="' + this.ControlName + '" '
		        buffer += 'onMouseOver="ajaxwin=dhtmlwindow.open(' + "'ajaxbox', 'ajax', '" + setLinkPopUpLevel(plink)  + "' , '" + effect + "' , '" + htitle + "','" + ttext 
                buffer += "', '" + titleFC + "', '" + titleBC + "',  '" + attr + "', '0','" + this.ContainerName + "'" 
                buffer += ",'" + pos + "','" + vpos + "','" + hpos + "','" + voff + "','" + hoff + "' ); return false" + '" ' 
		        buffer += 'onMouseUp="ajaxwin.setClick(' + "'" + cfix + "'" + '); return false" '
		        buffer += 'onMouseOut="ajaxwin.hide(); return false">'
	            var fontcolor = 'rgb(' + this.color.R + ',' + this.color.G + ',' + this.color.B + ')'
	            buffer += '<font face="' + this.face + '" color="' +  fontcolor + '" size="' + this.size + '">'
			    buffer += this.controlText + '</font></span>'
		        this.setHtml(buffer);
		        break;
            
            case "Image" :
            
                buffer = '';
	            buffer += '<img id="' + this.ControlName + '" src="' + this.image + '" height="' + this.Height + '" width="' + this.Width + '" '
		        buffer += 'onMouseOver="ajaxwin=dhtmlwindow.open(' + "'ajaxbox', 'ajax', '" + setLinkPopUpLevel(plink)  + "' , '" + effect + "' , '" + htitle + "','" + ttext 
                buffer += "', '" + titleFC + "', '" + titleBC + "',  '" + attr + "', '0','" +  this.ContainerName + "'" 
                buffer += ",'" + pos + "','" + vpos+ "','" + hpos + "','" + voff + "','" + hoff + "' ); return false" + '" ' 
		        buffer += 'onMouseUp="ajaxwin.setClick(' + "'" + cfix + "'" + '); return false" '
		        buffer += 'onMouseOut="ajaxwin.hide(); return false">'
		        this.setHtml(buffer);
		        break;
		        
            case "Control" :
                cname = this.AttachedControl;                
                //control = gx.dom.el(this.AttachedControl);                
                control = document.getElementById(cname);
                if((control == null) || (control == ""))			
	            {
	               cname = "span_" + this.AttachedControl;
	               control = document.getElementById(cname);
                }
				control.onmouseover = function() { ajaxwin=dhtmlwindow.open('ajaxbox', 'ajax', setLinkPopUpLevel(plink), effect, htitle, ttext, titleFC, titleBC, attr, 0, cname, pos, vpos, hpos, voff, hoff); return false; };         
                control.onmouseup = function() { omu_handler(); };
                control.onmouseout = function() { ajaxwin.hide(); return false; };
                break;
                
            case "ControlInGrid" :
                var ok = 1;
                var num = 1;
		        var numtxt;
                var cname1 = this.AttachedControl.substr(0,this.AttachedControl.length-5);	
                
                while (ok==1) 
                {
	               numtxt = ("000" + num).slice(-4);
                   cname = "span_" + this.AttachedControl.substr(0,this.AttachedControl.length-5)+ "_" + numtxt;
	               control = document.getElementById(cname);
			       if((control != null) && control != "")			
                   {
				        control.onmouseover = function() {omo_handler(this)};
				        control.onmouseup = function() { omu_handler(); };
                        control.onmouseout = function() { ajaxwin.hide(); return false; };
				        num = num + 1;
			       }
                   else
	               { 
                        cname = cname.substr(5);
                        control = document.getElementById(cname);
                        if((control != null) && control != "")			
                        {
				            control.onmouseover = function() {omo_handler(this)};
				            control.onmouseup = function() { omu_handler(); };
                            control.onmouseout = function() { ajaxwin.hide(); return false; };
				            num = num + 1;
                        }
                        else
	                    { 
                            ok = 0; 
                        }
                   }
		        }                	               
        }		
						
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

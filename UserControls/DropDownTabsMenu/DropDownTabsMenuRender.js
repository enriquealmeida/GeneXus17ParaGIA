function DropDownTabsMenu()
{
	this.TabsMenuData;
	this.Width;
	this.Height;
	this.MenuStyle;
	this.VersionNumber;

	// Databinding for property TabsMenuData
	this.SetTabsMenuData = function(data)
	{
		///UserCodeRegionStart:[SetTabsMenuData] (do not remove this comment.)
		this.TabsMenuData = data;

		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property TabsMenuData
	this.GetTabsMenuData = function()
	{
		///UserCodeRegionStart:[GetTabsMenuData] (do not remove this comment.)
		return this.TabsMenuData;	
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		//open main menu div
		
		var buffer = '<div id="' + this.MenuStyle + '" class="' + this.MenuStyle +'">'
		buffer += '<ul>'
		
		//draw main tabs options
		for(i=0;this.TabsMenuData[i]!=undefined;i++)
		{
			var vurl = '#';
			if(this.TabsMenuData[i].MenuURL!=""){
				vurl = this.TabsMenuData[i].MenuURL
			}
			buffer +='<li><a href="' + vurl + '" onclick="' + this.me() + '.JSTabsMenuItemClicked(\'' + this.ControlName + '\',' + i + ');"' + ' title="' + this.TabsMenuData[i].MenuDescription + '" rel="dropmenu' + i + '_' + this.TabMenuCode(this.MenuStyle) + '"><span>' + this.TabsMenuData[i].MenuTitle + '</span></a></li>'
		}
		buffer += '</ul>'
		buffer += '</div>' //close main menu div
		
		if(this.MenuStyle=="ddcolortabs")
			buffer += '<div class="ddcolortabsline">&nbsp;</div>'
		
		//open menu sections div
		i = 0;
		for(i=0;this.TabsMenuData[i]!=undefined;i++)
		{
			if(this.TabsMenuData[i].Sections[0].SectionURL!=""){
				var sections = '<div id="dropmenu' + i + '_' + this.TabMenuCode(this.MenuStyle) + '" class="dropmenudiv_' + this.TabMenuCode(this.MenuStyle) + '">'
				for(j=0;this.TabsMenuData[i].Sections[j]!=undefined;j++)
				{
					sections += '<a href="' + this.TabsMenuData[i].Sections[j].SectionURL + '" onclick="' + this.me() + '.JSTabsMenuItemClicked(\'' + this.ControlName + '\',' + i + ');">' + this.TabsMenuData[i].Sections[j].SectionTitle + '</a>'
    		}
				sections += '</div>'	
			}else{
				var sections = '<div id="dropmenu' + i + '_' + this.TabMenuCode(this.MenuStyle) + '" class="dropmenudivempty">'
				sections += '<a></a>'
				sections += '</div>'
			}
			buffer += sections
		}
		
		this.setHtml(buffer);
		
		if(getCookie('"' + this.ControlName + '"') == null){
			tabdropdown.init(this.MenuStyle, 0);	
		}else{
			tabdropdown.init(this.MenuStyle, parseInt(getCookie('"' + this.ControlName + '"')));		
		}
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	this.TabMenuCode = function(style)
	{
		switch(style)
		{
			case "ddcolortabs":
				return "a";
				break;
			case "bluetabs":
				return "b";
				break;
			case "slidetabsmenu":
				return "c";
				break;
			case "glowingtabs":
				return "d";
				break;
			case "halfmoon":
				return "e";
				break;
		}
	}
	this.JSTabsMenuItemClicked = function(ControlName, indexi)
	{
		setCookie('"' + ControlName + '"',indexi,null,"/");
	}
	//function getCookie(name)
	//name - name of the cookie 
	function getCookie(name)
	{
  		var cname = name + "=";               
  		var dc = document.cookie;             
  		if (dc.length > 0) {              
    		begin = dc.indexOf(cname);       
    		if (begin != -1) {           
      			begin += cname.length;       
      			end = dc.indexOf(";", begin);
      			if (end == -1) end = dc.length;
        			return unescape(dc.substring(begin, end));
    		} 
		}
		return null;
  	}
	//function setCookie(name, value, expires, path, domain, secure)
	//name - name of the cookie 
	//value - value of the cookie 
	//[expires] - expiration date of the cookie (defaults to end of current session) 
	//[path] - path for which the cookie is valid (defaults to path of calling document) 
	//[domain] - domain for which the cookie is valid (defaults to domain of calling document) 
	//[secure] - Boolean value indicating if the cookie transmission requires a secure transmission 
	function setCookie(name, value, expires, path, domain, secure) {
  		document.cookie = name + "=" + escape(value) + 
  		((expires == null) ? "" : "; expires=" + expires.toGMTString()) +
  		((path == null) ? "" : "; path=" + path) +
  		((domain == null) ? "" : "; domain=" + domain) +
  		((secure == null) ? "" : "; secure");
	}
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

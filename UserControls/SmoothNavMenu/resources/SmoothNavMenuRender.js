function SmoothNavMenu()
{
	this.MenuData;
	this.SelectedItem;
	this.Width;
	this.Height;
	this.Orientation;
	this.HeaderNormalBackground;
	this.HeaderHoverBackground;
	this.NormalBackground;
	this.HoverBackground;
	this.MenuBorderColor;
	this.FontHeaderNormalColor;
	this.FontHeaderHoverColor;
	this.FontItemNormalColor;
	this.FontItemHoverColor;
	this.DownArrow;
	this.RightArrow;
	this.HeaderBackgroundImage;
	this.HeaderVerticalPadding;
	this.ItemVerticalPadding;
	//this.HeaderFont;
	//this.ItemsFont;
	this.Arrows;
	this.op = false;
	
	this.SetMenuData = function(data)
	{
		this.MenuData = data;
	}
	
	this.GetMenuData = function()
	{
		return this.MenuData;		
	}
	
	this.SetSelectedItem = function(data)
	{
		this.SelectedItem = data;
	}
	this.GetSelectedItem = function()
	{
		return this.SelectedItem;
	}
	var self = this;
	this.show = function()
	{
		if (!this.IsPostBack)
		{
			var buffer = new gx.text.stringBuffer();
			buffer.clear();
			
			
			var _class = this.Orientation=="h"?_class="ddsmoothmenu":_class="ddsmoothmenu-v";
			
			if ((this.Orientation=="h")){
				buffer.append('<div id="' + this.ControlName + '" class="' + _class + '"  style="height:34px;">');
			} else {
				buffer.append('<div id="' + this.ControlName + '" class="' + _class + '">');
			}
			buffer.append('<ul>');
			var markup = this.loadSmoothMenuData(this.MenuData);
			buffer.append(markup);
			buffer.append('</ul>');
			buffer.append('</div>');
			this.setHtml(buffer.toString());
			this.initSmoothNavMenu();
		}
	}
	
	this.tmpbuffer = new gx.text.stringBuffer();
	this.tmpbuffer.clear();
	this.loadSmoothMenuData = function(data) {
	    var i = 0;
	    for (i = 0; data[i] != undefined; i++) {
	        if (data[i].Items != undefined && data[i].Items.toString() != "") {
	            this.tmpbuffer.append('<li class="smoothHeader">');
	            data[i].LinkTarget != undefined ? data[i].LinkTarget : "_self";
	            this.tmpbuffer.append('<a class="smoothHeader" href="' + data[i].Link + '" target="' + data[i].LinkTarget + '" onclick="' + this.me() + '.SmoothNavMenuOnClick(\'' + data[i].Id + '\',\'' + data[i].Title + '\',\'' + data[i].Description + '\',\'' + data[i].Link + '\');" >' + data[i].Title + '</a>');
	            this.tmpbuffer.append('<ul>');
	            this.loadSmoothSubMenuData(data[i].Items);
	            this.tmpbuffer.append('</ul>');
	            this.tmpbuffer.append('</li>');
	        } else {
	            this.tmpbuffer.append('<li class="smoothHeader">');
	            data[i].LinkTarget != undefined || data[i].LinkTarget == "" ? data[i].LinkTarget : "_self";
	            this.tmpbuffer.append('<a class="smoothHeader" href="' + data[i].Link + '" target="' + data[i].LinkTarget + '" onclick="' + this.me() + '.SmoothNavMenuOnClick(\'' + data[i].Id + '\',\'' + data[i].Title + '\',\'' + data[i].Description + '\',\'' + data[i].Link + '\');" >' + data[i].Title + '</a>');
	            this.tmpbuffer.append('</li>');
	        }
	    }
	    return this.tmpbuffer;
	}
	
	
	this.loadSmoothSubMenuData = function(subdata) {
	    var j = 0;
	    for (j = 0; subdata[j] != undefined; j++) {
	        if (subdata[j].Items != undefined && subdata[j].Items.toString() != "") {
	            this.tmpbuffer.append('<li class="smoothItem">');
	            subdata[j].LinkTarget != undefined ? subdata[j].LinkTarget : "_self";
	            this.tmpbuffer.append('<a class="smoothItem" href="' + subdata[j].Link + '" target="' + subdata[j].LinkTarget + '" onclick="' + this.me() + '.SmoothNavMenuOnClick(\'' + subdata[j].Id + '\',\'' + subdata[j].Title + '\',\'' + subdata[j].Description + '\',\'' + subdata[j].Link + '\');" > ' + subdata[j].Title + '</a>');
	            this.tmpbuffer.append('<ul>');
	            this.loadSmoothSubMenuData(subdata[j].Items);
	            this.tmpbuffer.append('</ul>');
	            this.tmpbuffer.append('</li>');
	        } else {
	            this.tmpbuffer.append('<li class="smoothItem">');
	            subdata[j].LinkTarget != undefined || subdata[j].LinkTarget == "" ? subdata[j].LinkTarget : "_self";
	            this.tmpbuffer.append('<a class="smoothItem" href="' + subdata[j].Link + '" target="' + subdata[j].LinkTarget + '" onclick="' + this.me() + '.SmoothNavMenuOnClick(\'' + subdata[j].Id + '\',\'' + subdata[j].Title + '\',\'' + subdata[j].Description + '\',\'' + subdata[j].Link + '\');" > ' + subdata[j].Title + '</span></a>');
	            this.tmpbuffer.append('</li>');
	        }
	    }
	    return this.tmpbuffer;
	}
	
	
	this.SmoothNavMenuOnClick = function(id, title, description, link) {
		if (typeof(this.OnClick) == 'function') {
			this.SelectedItem.Id = id;
			this.SelectedItem.Title = title;
			this.SelectedItem.Description = description;
			this.SelectedItem.Link = link;
			this.OnClick();
		}
	}
	
	this.initSmoothNavMenu = function(){
		var ctheme = new Array(this.HeaderNormalBackground.Html, this.HeaderHoverBackground.Html, this.NormalBackground.Html,      this.HoverBackground.Html, 
							   this.MenuBorderColor.Html,        this.FontHeaderNormalColor.Html, this.FontHeaderHoverColor.Html /*6*/,
							   "",            "",      ""/*9*/,        this.FontItemNormalColor.Html, this.FontItemHoverColor.Html, "" /*12*/,
							   "",            "",          this.NormalBackground.Html,      this.HoverBackground.Html /*16*/,
							   this.HeaderBackgroundImage,                this.DownArrow,            this.RightArrow /*19*/,
							   this.HeaderVerticalPadding, 	     this.ItemVerticalPadding, ""/*22*/, "",
							   "" /*this.HeaderFont 24*/,          ""  /*this.ItemsFont*/, this.Arrows);
		var options = {mainmenuid:this.ControlName, orientation:this.Orientation,classname:this.Orientation=="h"?"ddsmoothmenu":"ddsmoothmenu-v",customtheme:ctheme,contentsource: "markup"};
		ddsmoothmenu.init(options);
	}
	
}
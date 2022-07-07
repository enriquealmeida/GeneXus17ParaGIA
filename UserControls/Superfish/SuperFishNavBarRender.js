function SuperFishNavBar()
{
	this.DataSource;
	this.SelectedItem;
	this.Width;
	this.Height;
	this.Stateful;

	// Databinding for property DataSource
	this.SetDataSource = function(data)
	{
		///UserCodeRegionStart:[SetDataSource] (do not remove this comment.)
		this.DataSource = data;					

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property DataSource
	this.GetDataSource = function()
	{
		///UserCodeRegionStart:[GetDataSource] (do not remove this comment.)

		return this.DataSource;
						

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property SelectedItem
	this.SetSelectedItem = function(data)
	{
		///UserCodeRegionStart:[SetSelectedItem] (do not remove this comment.)
		this.SelectedItem = data;

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property SelectedItem
	this.GetSelectedItem = function()
	{
		///UserCodeRegionStart:[GetSelectedItem] (do not remove this comment.)
		return this.SelectedItem;

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)


		if (!this.IsPostBack)
		{
			var buffer = new gx.text.stringBuffer();
			buffer.clear();
						
			buffer.append('<ul id="Superfish" class="sf-menu sf-navbar">');
			buffer.append(' <li class="sf-navbar-title"><a class="sf-title">&nbsp;</a></li>');
						
			var markup = this.loadSuperFishNavBarData(this.DataSource);
			
			buffer.append(markup);
			
			buffer.append('</ul>');
			buffer.append('<ul class="sf-menu sf-display"></ul>')						
			this.setHtml(buffer.toString());
			this.initSuperFishNavBarData();
			
		}											

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

		this.strBuffer = new gx.text.stringBuffer();
		this.strBuffer.clear();
		
	this.loadSuperFishNavBarData = function(data){
	
		 var i = 0;
		 var currentPage = $(location).attr('pathname');		
			currentPage = currentPage.substr(currentPage.lastIndexOf('/')+1);
         var _selected="";
		 		 
				 
		for (i = 0; data[i] != undefined; i++) {
			 			 			 		
			if($.cookie("sf-menu-id")==null) $.cookie("sf-menu-id", data[i].Id );
				
			if(data[i].Id === $.cookie("sf-menu-id"))
			{
				_selected = 'class="current"';
			}
			else
			{
				_selected = "";
			}
			
			 if (data[i].Items != undefined && data[i].Items.toString() != "") {
			 			    
				this.strBuffer.append('<li '+_selected+' >');
				data[i].LinkTarget != undefined ? data[i].LinkTarget : "_self";
	            this.strBuffer.append('<a class="sf-with-ul" href="' + data[i].Link + '" target="' + data[i].LinkTarget + '" onclick="' + this.me() + '.SuperFishNavBarMenuOnClick(\'' + data[i].Id + '\',\'' + data[i].Title + '\',\'' + data[i].Description + '\',\'' + data[i].Link + '\');" >' + data[i].Title + '</a>');
	            this.strBuffer.append('<ul>');
	            this.loadSuperFishNavBarData(data[i].Items);
	            this.strBuffer.append('</ul>');
	            this.strBuffer.append('</li>');
			 }
			 else {
			    
	            this.strBuffer.append('<li '+_selected+' >');
	            data[i].LinkTarget != undefined || data[i].LinkTarget == "" ? data[i].LinkTarget : "_self";
	            this.strBuffer.append('<a class="sf-with-ul" href="' + data[i].Link + '" target="' + data[i].LinkTarget + '" onclick="' + this.me() + '.SuperFishNavBarMenuOnClick(\'' + data[i].Id + '\',\'' + data[i].Title + '\',\'' + data[i].Description + '\',\'' + data[i].Link + '\');" >' + data[i].Title + '</a>');

	            this.strBuffer.append('</li>');
	        }
			 
		}
						
		return this.strBuffer;							
	}
	

   this.SuperFishNavBarMenuOnClick = function(id, title, description, link) {   
		if (typeof(this.OnClick) == 'function') {
			this.SelectedItem.Id = id;
			this.SelectedItem.Title = title;
			this.SelectedItem.Description = description;
			this.SelectedItem.Link = link;
			this.OnClick();
			
		}		
		if(this.Stateful ===true)
		{
			$.cookie("sf-menu-id", id);		
		}
	}	
	
	this.initSuperFishNavBarData = function(){
	
		$("li.current").parents('li').addClass('current');	
		$("ul.sf-menu").superfish({ 
			pathClass:  'current' 
		}); 
		
		if ($.browser.msie)
		{
			if(parseInt(document.documentMode)=== 5)
			{
				alert("Supperfish menu.  Unsupported mode");
			}			
		}				
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

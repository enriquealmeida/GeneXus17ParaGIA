function SlideDownMenu()
{
	this.SlideDownMenuData;
	this.SelectedParentSection;
	this.Width;
	this.Height;
	this.Speed;
	this.Remember;
	this.Name;
	this.OneSubmenuOnly;
	this.Collapsed;

	// Databinding for property SlideDownMenuData
	this.SetSlideDownMenuData = function(data)
	{
		///UserCodeRegionStart:[SetSlideDownMenuData] (do not remove this comment.)
		this.SlideDownMenuData = data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property SlideDownMenuData
	this.GetSlideDownMenuData = function()
	{
		///UserCodeRegionStart:[GetSlideDownMenuData] (do not remove this comment.)
		return this.SlideDownMenuData;	
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property SelectedParentSection
	this.SetSelectedParentSection = function(data)
	{
		///UserCodeRegionStart:[SetSelectedParentSection] (do not remove this comment.)
		this.SelectedParentSection = data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property SelectedParentSection
	this.GetSelectedParentSection = function()
	{
		///UserCodeRegionStart:[GetSelectedParentSection] (do not remove this comment.)
		return this.SelectedParentSection;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function(){
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if (!this.IsPostBack){
			var buffer = '<div style="float: left" id="' + this.ControlName + '" class="sdmenu">' //open main menu div
			var i = 0;
			var vURL = '';
		
			for (i = 0; this.SlideDownMenuData[i] != undefined; i++) {
				buffer += '<div>'
				buffer += '<span>' + this.SlideDownMenuData[i].ParentTitle + '</span>'
				for (j = 0; this.SlideDownMenuData[i].Sections[j] != undefined; j++) {
					if (this.SlideDownMenuData[i].Sections[j].SectionURL != "") {
						vURL = this.SlideDownMenuData[i].Sections[j].SectionURL
					}
					else {
						vURL = '#'
					}
					buffer += '<a href="' + vURL + '" onclick="' + this.me() + '.JSSDItemClicked(' + i + ',' + j + ');">' + this.SlideDownMenuData[i].Sections[j].SectionTitle + '</a>'
				}
				buffer += '</div>'
			}
			buffer += '</div>' //close main menu div
			this.setHtml(buffer);
			this.initSDMenu();
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	var _mThis = this;
	this.initSDMenu = function() {
		var myMenu = new SDMenu(_mThis.ControlName);
		myMenu.speed = _mThis.Speed; // Menu sliding speed (1 - 5 recomended)
		myMenu.remember = _mThis.Remember; // Store menu states (expanded or collapsed) in cookie and restore later
		myMenu.oneSmOnly = _mThis.OneSubmenuOnly; // One expanded submenu at a time
		myMenu.markCurrent = true; // Mark current link / page (link.href == location.href)
		if (_mThis.Collapsed == "1")
			myMenu.collapseAll();
		myMenu.init();
	}
	gx.evt.attach(window, 'load', this.initSDMenu);
	
	this.JSSDItemClicked = function(indexi,indexj)
	{
		if (typeof(this.SDItemClicked) == 'function') {
			this.SelectedParentSection.ParentID = this.SlideDownMenuData[indexi].ParentID;
			this.SelectedParentSection.SectionID = this.SlideDownMenuData[indexi].Sections[indexj].SectionID;
			this.SDItemClicked();
		}
	}
	///UserCodeRegionEnd: (do not remove this comment.):
}

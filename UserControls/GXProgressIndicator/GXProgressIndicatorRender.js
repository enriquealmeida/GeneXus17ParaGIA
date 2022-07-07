function GXProgressIndicator($)
{
	this.Height;
	this.Width;

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if (!this.IsPostBack)
		{					
			var updateProgress = function(data) {														
				var progress = gx.json.evalJSON(data.Message);		
				this.Action = progress.Action;
				if (progress.Action == 3) {
					this.hide();
				}
				else
					this.updateProgressImpl(progress);				
			};
			this.ParentObject.addOnMessage('GX_PROGRESS_BAR', '', ["NotificationInfo"], updateProgress.closure(this), true);			
		}											
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	
	
	this.init = function(progressObj) {
		var self = this;
		var template =  "<div class='progress-bar-container' style='width:{{width}}'>" +
						"<span class='progress-bar-title'>{{title}}</span>" +
						"<div class='GXProgressBar progress progress-striped active'>" +
						"<div class='progress-bar' id ='{{id}}'  aria-valuetransitiongoal='{{progress}}'></div>" +
						"</div>" +
						"</div>";
		self.ProgressType = progressObj.Type;	
		var isDeterminate = !self.IsIndeterminate();
		var title = progressObj.Title || '';
		var progress = (!isDeterminate)? 100 : progressObj.Value || 1;
								
		var data = { progress: progress, title: title, id:self.ControlName};
		if (self.Width)
			data.width = self.Width + "px";
		var html = Mustache.to_html(template, data);				
		self.setHtml(html);    
		self.initialized = true;			
    }
	
	
	this.updateProgressImpl = function (progressObj)
	{				
		var self = this; 
		if (!this.initialized){		
			this.init(progressObj);						
		}
		var isDeterminate = !self.IsIndeterminate();
		var title = progressObj.Title || '';
		var progress = (!isDeterminate)? 100 : progressObj.Value;
		var cssClass = progressObj.Class;
		var description = progressObj.Description;
		
		this.progressBar = $('#' + this.ControlName);

		self.SetDescription(description);
		self.SetTitle(title);
		
		this.progressBar.attr('aria-valuetransitiongoal', progress).progressbar({
																				transition_delay: 10,
																				refresh_speed: 300,
																				display_text: (isDeterminate? 'fill': ''),
																				use_percentage: true																				
																		});	
		if (typeof(cssClass) != 'undefined') {
			if (this.lastClass)
				this.progressBar.removeClass(this.lastClass);
			this.progressBar.addClass(cssClass);
			this.lastClass = cssClass;
		}
	}
	
	this.hide = function()
	{
		var self = this;	
		this.progressBar.fadeOut('slow', function() {
			self.cleanUp();
		});		
	}
	this.IsIndeterminate = function()
	{
		return this.ProgressType == 0;
	}
	
	/*Action:  0: Show, 1:ShowWithTitle, 2: ShowWithTitleAndDesc, 3:Hide */
	this.SetTitle = function (title)
	{
		var titleEl = $(".progress-bar-title", this.getContainerControl());
		if (!title) {
			titleEl.hide();
		}
		else
		{
			titleEl.show();
			titleEl.text(title);
		}
	}
	
	this.UpdateCssClass = function(newClass)
	{					
		if (typeof(newClass) != 'undefined')
		{				
			var cClass = this.currentClass;
			if (typeof(cClass) != "undefined")
				this.ProgressBar.removeClass(cClass);
			this.ProgressBar.addClass(newClass);
			this.currentClass = newClass;
		}
	}
	
	this.SetDescription = function (description) 
	{		
		description = description || "";
		this.progressBar.attr('aria-description', description);
	}
	
	this.cleanUp = function()
	{
		this.initialized = false;
		this.setHtml(''); 
	}
	
	
	
	
	
	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

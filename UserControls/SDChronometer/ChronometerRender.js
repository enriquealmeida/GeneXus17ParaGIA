function Chronometer($) {
	this.Width;
	this.Height;
	this.Attribute;
	this.TickInterval;
	this.MaxValue;
	this.MaxValueText;
	this.Timer;
	this.AllSeconds;
	this.StartedDate;
	this.TimeFormat = "HH:mm:ss";
	this.Visible;	
	// Databinding for property Attribute
	this.SetAttribute = function(data) {
		///UserCodeRegionStart:[SetAttribute] (do not remove this comment.)		
		if (!this.IsRunning) {
			this.AllSeconds = parseInt(data);
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Attribute
	this.GetAttribute = function() {
		///UserCodeRegionStart:[GetAttribute] (do not remove this comment.)	
		if (this.IsRunning)
			return this.GetElapsedSeconds(moment());
		else
			return this.AllSeconds;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.Initialized = function() {
		return typeof(this.ChronoCtrlId) !== "undefined";
	}

	this.Initialize = function() {
		///UserCodeRegionStart:[show] (do not remove this comment.)	     
		this.AllSeconds = this.AllSeconds || 0;
		this.ChronoCtrlId = this.ContainerName + "_chrono";
		var initialVal = this.AllSeconds;
		var display = (this.Visible) ? "display:inline" : "display:none";
		var timerParms = {
			id: this.ChronoCtrlId,
			className: "gx-chronometer",
			style: display,
			initialText: moment.duration(parseInt(initialVal), "seconds").format(this.TimeFormat, {
				trim: false
			})
		};

		var template = '<span id="{{id}}" class="{{className}}" style={{style}}>{{initialText}}</span>';
		var html = Mustache.to_html(template, timerParms);
		this.setHtml(html);
	}
	this.show = function() {
		if (!this.Initialized()) {
			this.Initialize();
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)


	this.Start = function() {
		if (this.Initialized() && !this.IsRunning) {
			this.IsRunning = true;
			this.StartedDate = moment();			
			if (this.MaxValue) {
				this.MaxValueMoment = moment().add(this.MaxValue - this.AllSeconds, "seconds");
			}
			this.TimerUI = setInterval(this.UpdateUI.closure(this, []), 1000);
			if (this.Tick)
				this.TimerHandler = setInterval(this.CallHandler.closure(this, []), this.TickInterval * 1000);
		}
	}
	
	this.CallHandler = function() {		
		if (this.Tick)
			this.Tick();
	}
	
	this.UpdateUI = function() {
		var now = moment();
		if (this.MaxValue && moment().isAfter(this.MaxValueMoment)) {
			this.DisplayText(this.MaxValueText);
			this.Stop();
		} else {
			var elapsed = now.clone().subtract(this.StartedDate).add(this.AllSeconds, "seconds");
			this.DisplayText(elapsed.format(this.TimeFormat));
		}
	}

	this.GetElapsedSeconds = function(now) {
		now = now || moment();
		return this.AllSeconds + now.diff(this.StartedDate, "seconds");
	}

	this.DisplayText = function(text) {
		var ctrl = $("#" + this.ChronoCtrlId);
		ctrl.text(text);
	}

	this.Stop = function() {
		if (this.IsRunning) {
			this.AllSeconds = this.GetElapsedSeconds();
			this.IsRunning = false;
			clearInterval(this.TimerUI);
			clearInterval(this.TimerHandler);
			delete this.TimerUI;
			delete this.TimerHandler;
		}
	}

	this.Reset = function() {
		this.Stop();
		this.AllSeconds = 0;		
		this.Initialize();
		this.Start();
	}
	
	this.destroy = function () {
		this.Stop();
	};

	///UserCodeRegionEnd: (do not remove this comment.):
}
var gxSchedulerUC;
function GXScheduler() {
	this.TrackEvents = false;
	gxSchedulerUC = this;
	this.SchedulerCotainerName = this.ContainerName + "_Scheduler";
	this.DynamicCssRules = {};

	this.SetCurrentEvent = function (data) {
		this.CurrentEvent = data;
        
	}

	this.GetCurrentEvent = function () {
		return this.CurrentEvent;
	}

	this.SetInitialDate = function (data) {
		this.InitialDate = data;
        
	}

	this.GetInitialDate = function () {
		return this.InitialDate;
        
	}

	this.SetCurrentView = function (data, aView) {
		this.Refreshing = true;
		if (typeof aView === "undefined") { //Called with one parameter, it's the view
			scheduler.setCurrentView(scheduler._date, data);
		}
		else { // two parameters, date and view
			scheduler.setCurrentView(this.GetNormalizedDate(data, true), aView);
		}
		this.Refreshing = false;
	}

	this.GetCurrentView = function () {
		return scheduler._date;
	}

    this.show = function() {
		if (!this.IsPostBack) {
			this.Initialize();
		}
	}


	this.regIsNumber = function (fData) {
		var reg = new RegExp("^[0-9]+[0-9]*$");
		return reg.test(fData)
	}


	this.Initialize = function () {
		gx.fx.obs.addObserver("popup.close", this, this.HandlePopupClose);
		this.SetSchedulerHtml();
		this.LoadResources([
			this.LoadCss,
			this.LoadLocalization,
			this.LoadOverrides,
			this.LoadTooltip,
			this.LoadContainerAutoresize
		]);
		this.TrackEvents = true;
	}

	this.SetSchedulerHtml = function () {

		var containerControl = this.getContainerControl();
		var vheight = this.Height;
		if (this.CBoolean(this.AutoResize)) {
			containerControl.style.width = "100%";
			containerControl.style.height = "100%";
		} else {
			containerControl.style.height = this.regIsNumber(vheight) ? vheight + "px" : vheight;
			containerControl.style.width = this.regIsNumber(this.Width) ? this.Width + "px" : this.Width;
		}

		containerControl.className = "dhx_cal_container";
		var buffer = [];
		buffer.push("<div class='dhx_cal_navline'>");
		buffer.push("<div class='dhx_cal_prev_button' style='display:" + this.GetElementVisible(this.DisplayNavigationButtons) + " '>&nbsp;</div>");
		buffer.push("<div class='dhx_cal_next_button' style='display:" + this.GetElementVisible(this.DisplayNavigationButtons) + " '>&nbsp;</div>");
		buffer.push("<div class='dhx_cal_today_button' style='display:" + this.GetElementVisible(this.DisplayNavigationButtons) + " '></div>");
		buffer.push("<div class='dhx_cal_date' style='display:" + this.GetElementVisible(this.DisplayNavigationButtons) + " '></div>");
		buffer.push("<div class='dhx_cal_tab' name='day_tab' style='right:" + this.SetTabRight(this.DisplayDayTab) + "px;display:" + this.GetElementVisible(this.DisplayDayTab) + "'></div>");
		buffer.push("<div class='dhx_cal_tab' name='week_tab' style='right:" + this.SetTabRight(this.DisplayWeekTab) + "px;display:" + this.GetElementVisible(this.DisplayWeekTab) + "'></div>");
		buffer.push("<div class='dhx_cal_tab' name='month_tab' style='right:" + this.SetTabRight(this.DisplayMonthTab) + "px;display:" + this.GetElementVisible(this.DisplayMonthTab) + "'></div>");
		buffer.push("</div>");
		buffer.push("<div class='dhx_cal_header'>");
		buffer.push("</div>");
		buffer.push("<div class='dhx_cal_data'>");
		buffer.push("</div>");
		this.setHtml(buffer.join(""));

	}

	this.LoadResources = function (funcArray) {
		this.ResourcesLength = funcArray.length;
		for (var i = 0; funcArray[i] != undefined; i++)
			funcArray[i].call(this);
	}

	this.ProcessedResources = 0;
	this.OnResourcesLoaded = function () {
		this.ProcessedResources++;
		if (this.ProcessedResources == this.ResourcesLength)
			setTimeout(this.CreateDelegate(this, this.InitScheduler), 50);
	}

	this.LoadCss = function () {
		gx.http.loadStyle(gx.util.resourceUrl(gx.basePath + gx.staticDirectory + "GXScheduler/dhtmlxscheduler_" + this.Theme + ".css", false), this.CreateDelegate(this, this.OnResourcesLoaded), true);
	};

	this.LoadLocalization = function () {
		var language = (gx.languageCode != undefined) ? gx.languageCode : "";
		var file = "";
		var staticDir = gx.staticDirectory.substring(1, gx.staticDirectory.length);
		switch (language) {
			case "German":
				file = "GXScheduler/locale/locale_de.js";
				break;
			case "ita":
				file = "GXScheduler/locale/locale_it.js";
				break;
			case "spa":
				file = "GXScheduler/locale/locale_es.js";
				break;
			case "por":
				file = "GXScheduler/locale/locale_pt.js";
				break;
			case "jap":
				file = "GXScheduler/locale/locale_jp.js";
				break;
			default:
				file = "GXScheduler/locale/locale_en.js";
		}
		this.LoadDynamicResource(gx.util.resourceUrl(staticDir + file, true), "js", this.CreateDelegate(this, this.OnResourcesLoaded));
	}

	this.LoadOverrides = function () {
		var staticDir = gx.staticDirectory.substring(1, gx.staticDirectory.length);
		this.LoadDynamicResource(gx.util.resourceUrl(staticDir + "GXScheduler/GXSchedulerOverrides.js", true), "js", this.CreateDelegate(this, this.OnResourcesLoaded));
	}

	this.LoadTooltip = function () {
		var staticDir = gx.staticDirectory.substring(1, gx.staticDirectory.length);
		this.LoadDynamicResource(gx.util.resourceUrl(staticDir + "GXScheduler/dhtmlxscheduler_tooltip.js", true), "js", this.CreateDelegate(this, this.OnResourcesLoaded));
	}

	this.LoadContainerAutoresize = function () {
		if (this.CBoolean(this.AutoResize)) {
			var staticDir = gx.staticDirectory.substring(1, gx.staticDirectory.length);
			this.LoadDynamicResource(gx.util.resourceUrl(staticDir + "GXScheduler/dhtmlxscheduler_container_autoresize.js", true), "js", this.CreateDelegate(this, this.OnResourcesLoaded));
		}
		else {
			this.OnResourcesLoaded();
		}
	}

	this.InitScheduler = function () {
		this.SetProperties();
		this.SetOverrides();
		scheduler.init(this.ContainerName, this.GetInitialDate(), this.View);
		scheduler.setLoadMode(this.AutoLoad);
		scheduler.load(this.getCallbackUrl());
		this.SetEvents();
		this.GenerateCssRules();
		this.SetAppearance();
	}

	var hourSizeCoefficient = {
		"classic": 21,
		"terrace": 22,
		"flat": 22,
		"glossy": 21
	};
	this.SetProperties = function () {
		this.setCallbackObject();
		scheduler.config.xml_date = "%Y-%m-%d %H:%i";
		scheduler.config.multi_day = false;
		scheduler.config.first_hour = parseInt(this.FirstHour);
		scheduler.config.last_hour = parseInt(this.LastHour);
		scheduler.config.details_on_create = this.CBoolean(this.DetailsOnCreate);
		scheduler.config.details_on_dblclick = this.CBoolean(this.DetailsOnDblClick);
		if (this.CBoolean(this.ReadOnly)) this.SetReadOnly();
		scheduler.config.time_step = this.DefaultStepTime;
		scheduler.config.hour_size_px = parseInt(this.HourSize) * hourSizeCoefficient[this.Theme];
		scheduler.config.scroll_hour = parseInt(this.ScrollHour);
		scheduler.attachEvent("onBeforeDrag", function (eventId) {
			if (eventId)
				return (gxSchedulerUC.CBoolean(gxSchedulerUC.AllowDragNDrop));
			return true;
		});
	}

	this.SetReadOnly = function () {
		scheduler.config.drag_create = false;
		scheduler.config.drag_move = false;
		scheduler.config.drag_resize = false;
		scheduler.config.dblclick_create = false;

		scheduler.attachEvent("onClick", function () {
			return false;
		})
	}

	this.GenerateCssRules = function () {

		//past events class
		var rule = ".dhx_cal_event.past_event" + " div";
		var ruleText = "background-color:rgb(" + this.PastEventsColor.R + "," + this.PastEventsColor.G + "," + this.PastEventsColor.B + ") ;";
		//!important
		this.InsertCssRule(rule, ruleText);
		//today events class
		rule = ".dhx_cal_event.today_event" + " div";
		ruleText = "background-color:rgb(" + this.TodayEventsColor.R + "," + this.TodayEventsColor.G + "," + this.TodayEventsColor.B + ");";
		//!important
		this.InsertCssRule(rule, ruleText);
		//future events class
		rule = ".dhx_cal_event.future_event" + " div";
		ruleText = "background-color:rgb(" + this.FutureEventsColor.R + "," + this.FutureEventsColor.G + "," + this.FutureEventsColor.B + "); ";
		//!important
		this.InsertCssRule(rule, ruleText);
	}

	this.InsertCssRule = function (rule, ruleText, position) {
		if (!this.DynamicCssRules[rule]) {
			this.DynamicCssRules[rule] = true;
			var i = 0;
			if (position == "last")
				i = (_isIE) ? document.styleSheets[0].rules.length : document.styleSheets[0].cssRules.length;
			if (_isIE)
				document.styleSheets[0].addRule(rule, ruleText, i);
			else
				document.styleSheets[0].insertRule(rule + " { " + ruleText + " } ", i);
		}
	}

	this.Views = new Array();
	this.SetEvents = function () {
		scheduler.attachEvent("onEventAdded", function (event_id, event_object) {
			return gxSchedulerUC.onEventAdded(event_id, event_object);
		});
		scheduler.attachEvent("onEventChanged", function (event_id, event_object) {
			return gxSchedulerUC.onEventUpdated(event_id, event_object);
		});
		scheduler.attachEvent("onBeforeEventDelete", function (event_id, event_object) {
			return gxSchedulerUC.onEventDeleted(event_id, event_object);
		});
		if (!this.CBoolean(this.ReadOnly)) {
			scheduler.attachEvent("onClick", function (event_id, native_event_object) {
				return gxSchedulerUC.onEventClick(event_id, native_event_object);
			});
		}
	}

	this.SetAppearance = function () {
		this.SetTemplates();
	}

	this.SetTemplates = function () {
		scheduler.templates.event_header = function (start, end, event) {
			var name = (event.name == undefined) ? "" : event.name;
			var header = name + " (" + scheduler.templates.hour_scale(start) + " - " + scheduler.templates.hour_scale(end) + ")";
			switch (scheduler._mode) {
				case "day":
					if (event.namedayview)
						header = event.namedayview;
					break;
				case "week":
					if (event.nameweekview)
						header = event.nameweekview;
					break;
				case "month":
					if (event.namemonthview)
						header = event.namemonthview;
					break;
			}

			if (event.link && event.link != "") {
				var linkTarget = gxSchedulerUC.CBoolean(gxSchedulerUC.OpenLinkNewWindow) ? "_self" : "_blank";
				header = "<a href='" + event.link + "' target='" + linkTarget + "'>" + header + "</a>";
			}
			return header;

		}
		scheduler.templates.event_bar_text = function (start, end, ev) {
			if (ev.namemonthview)
				return ev.namemonthview;
			else
				return ev.text;
		}

		scheduler.templates.event_text = function (start, end, event) {
			var text = "";
			text += event.text;
			return text;
		}

		scheduler.templates.event_class = function (start, end, event) {
			var comp = new gx.date.gxdate(start).compare_date(new gx.date.gxdate(new Date()))
			if (comp == 0)
				return "today_event";
			if (comp == -1)
				return "past_event";
			if (comp == 1)
				return "future_event";

		}

		scheduler.config.lightbox.sections = [
			{ name: "name", height: 23, type: "textarea", map_to: "name" },
			{ name: "description", height: 200, map_to: "text", type: "textarea", focus: true },
			{ name: "time", height: 72, type: "time", map_to: "auto" }
		];

		scheduler.locale.labels.section_name = "Name";
	}

	this.SetAppearanceProperties = function () {
		var elements = this.getElementsByClassName("dhx_cal_container", "div");
		elements = elements.concat(this.getElementsByClassName("dhx_scale_hour", "div"));
		elements = elements.concat(this.getElementsByClassName("dhx_scale_bar", "div"));
		for (var i = 0; elements[i] != undefined; i++) {
			elements[i].style.backgroundColor = this.Color.Html;
		}

		if (this.BackgroundImage != "") {
			var scaleHolder = this.getElementsByClassName("dhx_scale_holder_now", "div")[0];
			if (scaleHolder)
				scaleHolder.style.backgroundImage = this.BackgroundImage;
		}
	}

	//////////////Events handlers///////////////////////

	this.onEventAdded = function (event_id, event_object) {
		if (this.EventAdded) {
			this.CurrentEvent = this.ConvertDhxSdtToGxSdt(event_object);
			this.EventAdded();
		}
		return true;
	}

	this.onEventUpdated = function (event_id, event_object) {
		if (this.EventUpdated) {
			this.CurrentEvent = this.ConvertDhxSdtToGxSdt(event_object);
			this.EventUpdated();
		}
		return true;
	}

	this.onEventDeleted = function (event_id, event_object) {
		if (this.EventDeleted) {
			this.CurrentEvent = this.ConvertDhxSdtToGxSdt(event_object);
			this.EventDeleted();
		}
		return true;
	}

	this.onEventClick = function (event_id, native_event_object) {
		if (this.EventSelected) {
			var event_object = scheduler.getEvent(event_id);
			this.CurrentEvent = this.ConvertDhxSdtToGxSdt(event_object, true);
			this.EventSelected();
		}
		return true;
	}


	this.HandlePopupClose = function (popupData) {
		this.Refresh();
	}

	/////////////////////////////////////////////////////


	///////////////////////Methods///////////////////////

	this.AddEvent = function (gxEvent_object) {
		var evt = this.ConvertGxSdtToDhxSdt(gxEvent_object, true);
		scheduler.addEvent(evt);
	}

	this.UpdateEvent = function (gxEvent_object) {
		var evt = scheduler.getEvent(gxEvent_object.Id);
		if (evt) {
			var evtAux = this.ConvertGxSdtToDhxSdt(gxEvent_object, true);
			evt.name = gxEvent_object.Name;
			evt.start_date = evtAux.start_date;
			evt.end_date = evtAux.end_date;
			evt.text = evtAux.text;
			evt.additionalInformation = evtAux.additionalInformation;
			scheduler.updateEvent(gxEvent_object.Id);
		}
	}

	this.DeleteEvent = function (gxEvent_object) {
		scheduler.deleteEvent(gxEvent_object.Id);
	}

	this.Refresh = function () {
		this.Refreshing = true;
		scheduler.clearAll();
		scheduler.load(this.getCallbackUrl());
		this.Refreshing = false;
	}
	/////////////////////////////////////////////////////

	this.GetInitialDate = function () {
		var initialDate = new Date(this.InitialDate.replace("-", "/").replace("-", "/"));
		return (initialDate == "Invalid Date" || initialDate == "NaN") ? null : initialDate;
	}

	this.GetDateMask = function (fullYear) {
		var year;
		if (fullYear)
			year = "Y";
		else
			year = "y";
		var dayMask;
		var hourMask;
		if (gx.dateFormat == "DMY")
			dayMask = "%d/%m/%" + year;
		if (gx.dateFormat == "MDY")
			dayMask = "%m/%d/%" + year;
		if (gx.dateFormat == "YMD")
			dayMask = "%" + year + "/%m/%d";

		if (gx.timeFormat == 24)
			hourMask = "%H:%i";
		else
			hourMask = "%h:%i";

		return dayMask + " " + hourMask;
	}

	this.ConvertGxSdtToDhxSdt = function (gxEvent_object, convertDates) {
		var evt = new Object();
		evt.id = gxEvent_object.Id;
		evt.name = gxEvent_object.Name;
		evt.properties = gxEvent_object.Properties;
		evt.start_date = this.GetNormalizedDate(gxEvent_object.StartTime);
		evt.end_date = this.GetNormalizedDate(gxEvent_object.EndTime);
		evt.text = gxEvent_object.Notes || gxEvent_object._tagvalue;
		evt.link = gxEvent_object.Link;
		evt.additionalInformation = gxEvent_object.AdditionalInformation;
		evt.textColor = gxEvent_object.Color;
		evt.color = gxEvent_object.BackgroundColor;
		evt.namedayview = gxEvent_object.NameDayView;
		evt.nameweekview = gxEvent_object.NameWeekView;
		evt.namemonthview = gxEvent_object.NameMonthView;
		evt.tooltip = gxEvent_object.ToolTip;
		this.SetMonthEventBarHeight(evt.text);
		return evt;
	}

	this.ConvertDhxSdtToGxSdt = function (event_object, convertDates) {
		var gen = this.getGenerator();
		var gxEvt = new Object();
		gxEvt.Id = event_object.id.toString();
		gxEvt.Name = (event_object.name == undefined) ? "" : event_object.name;
		gxEvt.StartTime = new gx.date.gxdate(event_object.start_date);
		gxEvt.StartTime.HasTimePart = true;
		gxEvt.EndTime = new gx.date.gxdate(event_object.end_date);
		gxEvt.EndTime.HasTimePart = true;
		gxEvt.Notes = event_object.text;
		gxEvt.Color = event_object.textColor;
		gxEvt.BackgroundColor = event_object.color;
		gxEvt.ToolTip = event_object.tooltip;
		gxEvt.AdditionalInformation = (event_object.additionalInformation == undefined) ? "" : event_object.additionalInformation;
		gxEvt.Link = (event_object.link == undefined) ? "" : event_object.link;
		gxEvt.Properties = (event_object.properties != undefined && event_object.properties instanceof Array) ? event_object.properties : new Array();
		return gxEvt;
	}
	// Undocumented - Day Color
	this.ConvertDayGxSdtToDhxSdt = function (gxDay_object) {
		var day = new Object();
		var d = this.GetNormalizedDate(gxDay_object.Date);
		day.date = new Date(d.getUTCFullYear(), d.getUTCMonth(), d.getUTCDate(), 0, 0, 0, 0);
		day.id = "A" + day.date.getTime();
		day.color = gxDay_object.Color;
		return day;
	}

	this.GetNormalizedDate = function (aDate, convertDates) {
		var aDateClean = aDate.replace("T", " ");
		var fullYear = this.isFullYearDateTime(new gx.date.gxdate(aDateClean).getString());
		var returnDate;
		if (convertDates) {
			if (typeof (aDate) == 'string') {
				var convert = scheduler.date.str_to_date(this.GetDateMask(fullYear));
				returnDate = convert(aDateClean);
			}
			else {
				returnDate = aDate.Value;
			}
		}
		else {
			returnDate = scheduler.templates.xml_date(aDateClean);
		}
		return returnDate;
	}

	this.MonthEventsViewPX = 20;
	this.SetMonthEventBarHeight = function (text) {
		if (this.MonthEventsView == "multiline") {
			var lines = Math.ceil(text.length / 20);
			var px = lines * 12;
			if (this.MonthEventsViewPX < px) {
				this.MonthEventsViewPX = px;
				scheduler.xy.bar_height = px;
			}
			this.InsertCssRule(".dhx_cal_event_clear", " white-space:normal;overflow:visible;", "last");
			this.InsertCssRule(".dhx_cal_event_line", " white-space:normal;overflow:visible;", "last");
		}
	}

	this.isFullYearDateTime = function (auxDate) {
		return (auxDate.length != 8);
	}

	this.setCallbackObject = function () {
		var gen = this.getGenerator();
		var objectName = this.LoadEventsObjectName;
		if (gen == "c#") {
			this.LoadEventsObjectName = objectName;
		}
		if (gen == "java") {
			this.LoadEventsObjectName = objectName;
		}
		if (gen == "ruby") {
			this.LoadEventsObjectName = objectName;
		}

	}

	this.getCallbackUrl = function () {
		var objectNameParts, packageName;
		var gen = this.getGenerator();
		var requestHandlerBaseUrl = "aschedulerrequesthandler";
		var i;
		if (this.RequestHandlerObjectName) {
			objectNameParts = this.RequestHandlerObjectName.split(".");
			for (i = 0, len = objectNameParts.length; i < len; i++) {
				objectNameParts[i] = objectNameParts[i].toLowerCase();
			}
			packageName = objectNameParts.slice(0, len - 1).join(".");
			requestHandlerBaseUrl = packageName + (packageName ? ".a" : "a") + objectNameParts[len - 1];
		}
		if (gen == "c#") {
			return requestHandlerBaseUrl + ".aspx";
		}
		if (gen == "java") {
			if (gx.O.PackageName != "")
				return gx.O.PackageName + "." + requestHandlerBaseUrl;
			else
				return requestHandlerBaseUrl;
		}
	}

	this.getPopupObjectUrl = function () {
		var gen = this.getGenerator();
		if (gen == "c#") {
			return this.DetailsFormObjectName.toLowerCase() + ".aspx";
		}
		if (gen == "java") {
            var pkg = gx.O.PackageName != "" ? gx.O.PackageName + "." : "";
			return pkg + this.DetailsFormObjectName.toLowerCase();
			//return this.DetailsFormObjectName.toLowerCase()
		}
		if (gen == "ruby") {
			return this.DetailsFormObjectName.toLowerCase() + ".html";;
		}
	}

	this.getGenerator = function () {
		var gen;
		if (gx.gen.isDotNet())
			gen = "c#";
		else if (gx.gen.ruby)
			gen = "ruby";
		else
			gen = "java";
		return gen;
	}

	this.OpenDetailsForm = function (id, mode) {
		var event_object = scheduler.getEvent(id);
		var startDateTime = new gx.date.gxdate(event_object.start_date);
		startDateTime.HasDatePart = true;
		startDateTime.HasTimePart = true;
		var endDateTime = new gx.date.gxdate(event_object.end_date);
		endDateTime.HasDatePart = true;
		endDateTime.HasTimePart = true;
		var popup = {};
		if (!mode || mode == "")
			mode = (event_object.name != undefined) ? "UPD" : "INS";
		if (this.CBoolean(this.ReadOnly))
			mode = "DSP";
		popup.Url = gx.http.formatLink(this.getPopupObjectUrl(), [mode, id, startDateTime.getUrlVal(), endDateTime.getUrlVal()]);
		gx.popup.open(popup);

	}

	this.CreateDelegate = function (obj, method, args, appendArgs) {
		return function () {
			var callArgs = args || arguments;
			if (appendArgs === true) {
				callArgs = Array.prototype.slice.call(arguments, 0);
				callArgs = callArgs.concat(args);
			} else if (typeof appendArgs == "number") {
				callArgs = Array.prototype.slice.call(arguments, 0);
				var applyArgs = [appendArgs, 0].concat(args);
				Array.prototype.splice.apply(callArgs, applyArgs);
			}
			return method.apply(obj || window, callArgs);
		};
	}

	this.CBoolean = function (str) {
		if (str) {
			if (typeof (str) == 'string')
				return (str.toLowerCase() == "true")
			return str;
		}
		else
			return false;
	}

	this.LoadDynamicResource = function (filename, filetype, callback) {
		if (filetype == "js") {
			var fileref = document.createElement('script')
			fileref.setAttribute("type", "text/javascript")
			fileref.setAttribute("src", filename)
		}
		else if (filetype == "css") { //if filename is an external CSS file
			var fileref = document.createElement("link")
			fileref.setAttribute("rel", "stylesheet")
			fileref.setAttribute("type", "text/css")
			fileref.setAttribute("href", filename)
		}
		if (typeof fileref != "undefined") {
			fileref.onreadystatechange = function () {
				if (this.readyState == 'loaded' || this.readyState == 'complete')
					callback();
			}
			fileref.onload = callback;
			document.getElementsByTagName("head")[0].appendChild(fileref)

		}
	}

	this.GetElementVisible = function (visible) {
		visible = this.CBoolean(visible);
		return (visible) ? "inline" : "none";
	}

	var tabRight = 270;
	this.SetTabRight = function (visible) {
		if (this.CBoolean(visible)) {
			tabRight = tabRight - 64;
		}
		return tabRight;
	}

	this.getElementsByClassName = function (clsName, tag) {
		var i, matches = new Array();
		var els = document.getElementsByTagName(tag);

		for (i = 0; i < els.length; i++) {
			if (els.item(i).className == clsName) {
				matches.push(els.item(i));
			}
		}
		return matches;
	}
}

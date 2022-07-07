GXScheduler.prototype.SetOverrides = function () {

	if (this.DetailsFormObjectName != "") {
		scheduler.showLightbox = function (id, mode) {
			gxSchedulerUC.OpenDetailsForm(id, mode);
		};
		scheduler._click.buttons["details"] = function (id) { scheduler.showLightbox(id, "DSP") };
		scheduler._click.buttons["edit"] = function (id) { scheduler.showLightbox(id, "UPD") };
		scheduler._click.buttons["delete"] = function (id) { scheduler.showLightbox(id, "DLT") };
	}

	scheduler._load = function (url, from) {
		url = url || this._load_url;
		url += (url.indexOf("?") == -1 ? "?" : "&") + "timeshift=" + (new Date()).getTimezoneOffset();
		if (this.config.prevent_cache) url += "&uid=" + this.uid();
		var to;
		from = from || this._date;

		if (this._load_mode) {
			var lf = this.templates.load_format;

			from = this.date[this._load_mode + "_start"](new Date(from.valueOf()));
			while (from > this._min_date) from = this.date.add(from, -1, this._load_mode);
			to = from;

			var cache_line = true;
			while (to < this._max_date) {
				to = this.date.add(to, 1, this._load_mode);
				if (this._loaded[lf(from)] && cache_line && !gxSchedulerUC.Refreshing)
					from = this.date.add(from, 1, this._load_mode);
				else cache_line = false;
			}

			var temp_to = to;
			do {
				to = temp_to;
				temp_to = this.date.add(to, -1, this._load_mode);
			} while (temp_to > from && this._loaded[lf(temp_to)]);

			if (to <= from && !gxSchedulerUC.Refreshing)
				return false; //already loaded
			dhtmlxAjax.get(url + "&callback=" + gxSchedulerUC.LoadEventsObjectName + "&from=" + lf(from) + "&to=" + lf(to), function (l) { scheduler.on_load(l); });
			while (from < to) {
				this._loaded[lf(from)] = true;
				from = this.date.add(from, 1, this._load_mode);
			}
		} else
			dhtmlxAjax.get(url, function (l) { scheduler.on_load(l); });
		this.callEvent("onXLS", []);
		return true;
	};

	scheduler._magic_parser = function (loader) {
		var xml = loader.getXMLTopNode("data");
		if (xml.tagName != "data") return []; //not an xml

		var evs = [];
		var xml = loader.doXPath("//event");

		for (var i = 0; i < xml.length; i++) {
			evs[i] = gxSchedulerUC.ConvertGxSdtToDhxSdt(scheduler._xmlNodeToJSON(xml[i]));
		}
		//undocumented - Colored Days
		xml = loader.doXPath("//Day");

		for (var i = 0; i < xml.length; i++) {
			var day = gxSchedulerUC.ConvertDayGxSdtToDhxSdt(scheduler._xmlNodeToJSON(xml[i]));
			var rule = "#" + day.id;  //"."
			var ruleText = "background-color:" + day.color + " !important;";
			gxSchedulerUC.InsertCssRule(rule, ruleText);
		}
		return evs;
	}

	scheduler._reset_month_scale = function (b, dd, sd) {
		var ed = scheduler.date.add(dd, 1, "month");

		//trim time part for comparation reasons
		var cd = new Date();
		this.date.date_part(cd);
		this.date.date_part(sd);

		var rows = Math.ceil(Math.round((ed.valueOf() - sd.valueOf()) / (60 * 60 * 24 * 1000)) / 7);
		var tdcss = [];
		var height = (Math.floor(b.clientHeight / rows) - 22);

		this._colsS.height = height + 22;
		var h = this._colsS.heights = [];
		for (var i = 0; i <= 7; i++)
			tdcss[i] = " style='height:" + height + "px; width:" + ((this._cols[i] || 0) - 1) + "px;' "



		var cellheight = 0;
		this._min_date = sd;
		var html = "<table cellpadding='0' cellspacing='0'>";
		for (var i = 0; i < rows; i++) {
			html += "<tr>";
			for (var j = 0; j < 7; j++) {
				html += "<td";
				var cls = "";
				if (sd < dd)
					cls = 'dhx_before';
				else if (sd >= ed)
					cls = 'dhx_after';
				else if (sd.valueOf() == cd.valueOf())
					cls = 'dhx_now';
				html += " class='" + cls + " " + this.templates.month_date_class(sd, cd) + "' ";
				// The following line is the only change from the original
				html += "><div class='dhx_month_head'>" + this.templates.month_day(sd) + "</div><div id='A" + sd.getTime() + "' class='dhx_month_body' " + tdcss[j] + "></div></td>";
				sd = this.date.add(sd, 1, "day");
			}
			html += "</tr>";
			h[i] = cellheight;
			cellheight += this._colsS.height;
		}
		html += "</table>";
		this._max_date = sd;

		b.innerHTML = html;
		return sd;
	};

	scheduler.render_event_bar = function (ev) {
		var parent = this._rendered_location;
		var x = this._colsS[ev._sday];
		var x2 = this._colsS[ev._eday];
		if (x2 == x) x2 = this._colsS[ev._eday + 1];
		var hb = this.xy.bar_height;

		var y = this._colsS.heights[ev._sweek] + (this._colsS.height ? (this.xy.month_scale_height + 2) : 2) + (ev._sorder * hb);

		var d = document.createElement("DIV");
		var cs = ev._timed ? "dhx_cal_event_clear" : "dhx_cal_event_line";
		var cse = scheduler.templates.event_class(ev.start_date, ev.end_date, ev);
		if (cse) cs = cs + " " + cse;

		var bg_color = (ev.color ? ("background-color:" + ev.color + ";") : "");
		var color = (ev.textColor ? ("color:" + ev.textColor + ";") : "");

		var html = '<div event_id="' + ev.id + '" class="' + cs + '" style="position:absolute; top:' + y + 'px; left:' + x + 'px; width:' + (x2 - x - 15) + 'px;' + color + '' + bg_color + '' + (ev._text_style || "") + '">';
		// The following lines (2) are changed from the original
		// if (ev._timed)
		// html+=scheduler.templates.event_bar_date(ev.start_date,ev.end_date,ev);
		html += scheduler.templates.event_bar_text(ev.start_date, ev.end_date, ev) + '</div>';
		html += '</div>';

		d.innerHTML = html;

		this._rendered.push(d.firstChild);
		parent.appendChild(d.firstChild);
	};

	scheduler.templates.tooltip_text = function (start, end, event) {
		return (event.tooltip) ? event.tooltip : event.text;
	};
}

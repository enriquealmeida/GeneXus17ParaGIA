function WebLinearGauge()
{
	var OFFSET_X = 20, 
		OFFSET_Y = 40, 
		MAX_LABEL_OFFSET_X = 20, 
		MAX_LABEL_OFFSET_Y = -6, 
		MIN_LABEL_OFFSET_X = 18, 
		MIN_LABEL_OFFSET_Y = -5,
		BOTTOM_LABEL_SPACE = 30;

	var createControl = function (container, cfg) {
		var r = Raphael(container);

		// Font size and face
		r.g.txtattr.font = "9pt Arial";
		r.g.txtattr.fill = "#747474";

		// Create control
		var gauge = r.g.hbarchart(cfg.offsetX, cfg.offsetY, cfg.width, cfg.height, cfg.ranges, cfg.options);
		
		// Add title
		if (cfg.title)
			r.g.text(cfg.width/2, 10, cfg.title);

		// Labels
		var prevBarWidth = 0;
		for (var i=0, len=gauge.bars.length; i < len; i++){
			var bar = gauge.bars[i][0];
			var label = r.g.labelise(cfg.labels[i], cfg.ranges[i][0], cfg.maxValue);
			var textLabel = r.g.text(bar.x - (bar.w-prevBarWidth)/2, bar.y + bar.h/2 + 10, label);
			prevBarWidth = bar.w;
		}

		// Value marker
		var bar = gauge.bars[0][0];
		r.g.popup((cfg.width * (cfg.value - cfg.minValue ) / (cfg.maxValue - cfg.minValue)) + cfg.offsetX, bar.y - bar.h/2 + 5, cfg.value, 2, 3);

		if (cfg.showMinMax) {
			// Min label
			r.g.text(MIN_LABEL_OFFSET_X, cfg.offsetY + MIN_LABEL_OFFSET_Y, cfg.minValue, 0);
			// Max label
			r.g.text(cfg.width + MAX_LABEL_OFFSET_X, cfg.offsetY + MAX_LABEL_OFFSET_Y, cfg.maxValue, 3);
		}

		return gauge;
	};
	
	var getConfig = function(uc) {
		var colors = [], labels = [], ranges = [], total = 0;

		var r = uc.config.Ranges;
		for (var i=0, len=r.length; i<len; i++){
			total += r[i].Length;
			labels.push(r[i].Name);
			ranges.push([r[i].Length]);
			var color = r[i].Color;
			if (color) {
				if (color.search("^[0-9a-fA-F]{1,6}$") >= 0)
					colors.push("#" + color);
				else
					colors.push(color);
			}
		}
		
		return {
			options: {
				stacked: true,
				colors: colors.length ? colors : undefined,
				type: uc.Style || 'square'
			},
			width: uc.config.Width - OFFSET_X - MAX_LABEL_OFFSET_X,
			height: uc.config.Height - OFFSET_Y - MIN_LABEL_OFFSET_Y - BOTTOM_LABEL_SPACE,
			offsetX: OFFSET_X,
			offsetY: OFFSET_Y,
			labels: labels,
			ranges: ranges,
			minValue: uc.config.MinValue,
			maxValue: uc.config.MaxValue,
			value: uc.value,
			title: uc.config.Title,
			showMinMax: uc.config.ShowMinMax
		};
	};

	this.SetData = function(data) {
		this.config = data;
		
		data.Width = data.Width || 300;
		data.Height = data.Height || 80;
		var offsetXSum = OFFSET_X + MAX_LABEL_OFFSET_X;
		data.Width = data.Width > offsetXSum ? data.Width : offsetXSum;
		var offsetYSum = OFFSET_Y + MIN_LABEL_OFFSET_Y + BOTTOM_LABEL_SPACE;
		data.Height = data.Height > offsetYSum ? data.Height : offsetYSum + 15;
	};
	
	this.GetData = function() {
		return this.config;
	};
	
	this.SetAttribute = function(value) {
		this.value = parseFloat(value);
	};
	
	this.GetAttribute = function() {
		return this.value;
	};
	
	this.show = function() {
		var container = this.getContainerControl();
		if (container.firstChild)
			container.removeChild(container.firstChild);

		if (!this.config || !this.config.Ranges || this.config.Ranges.length == 0) {
			delete this.gauge;
			return;
		}

		container.style.width = this.config.Width + "px";
		container.style.height = this.config.Height + "px";
		this.gauge = createControl(container, getConfig(this));
	};
	
	this.destroy = function() {
		delete this.gauge;
	};
}
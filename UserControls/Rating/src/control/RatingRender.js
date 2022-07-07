function Rating($) {
	this.Value;
	this.Step;
	this.MaxValue;
	this.Visible;
	this.Enabled;
	var $container, baseId, $starCtrl, $rootCtrl;

	this.SetAttribute = function (data) {
		this.SetValue(data);
	}

	this.GetAttribute = function () {
		return $starCtrl.val();
	}

	this.SetValue = function (newValue) {
		this.Value = (typeof newValue === 'string' || newValue instanceof String) ? newValue.replace(',', '.'): newValue;
		if ($starCtrl)
			$starCtrl.val(newValue);
	}

	this.show = function () {
		var ratingClass = ' gx-star-rating ' + (this.RatingClass || '');
		if (!this.IsPostBack) {
			$container = $(this.getContainerControl());
			baseId = $container.attr('id') + '_rating';
			var html = Mustache.to_html("<input type='number' id='{{id}}' value='{{value}}'/>", { id: baseId, value: this.Value });
			this.setHtml(html);
			var opts = {
				min: 0,
				max: this.MaxValue,
				stars: this.MaxValue,
				readonly: !this.Enabled,
				step: this.Step,
				showCaption: false,
				showClear: false,
				size: 'sm',
				ratingClass: ratingClass
			};
			$starCtrl = $('#' + baseId).rating(opts);

			var self = this;
			if (this.ControlValueChanged) {
				$starCtrl.on('rating.change', function (event, value) {
					self.ControlValueChanged();
				});
			}
			$rootCtrl = $starCtrl.closest('.form-group');
			if (!$rootCtrl.length)
				$rootCtrl = $container;
		}
		else {
			$starCtrl.rating('refresh', {
				readonly: !this.Enabled,
				step: this.Step,
				max: this.MaxValue,
				stars: this.MaxValue,
				ratingClass: ratingClass
			});
		}
		$rootCtrl.toggle(this.Visible);
	}


}
function selectGxControl(selectedControl)
{
	// InvokeScript from C# can't call this directly
	if (selectedControl)
		GxPreview.selectControl(selectedControl);
	else
		GxPreview.clearSelection();
}

GxPreview = (function () {
	$(document).ready(function () {
		if (template) {
			$.each(template.class_maps, function (i, class_map) {
				var $matches = $(class_map.selector);
				$matches.each(function (j, el) {
					var $el = $(el);
					if (class_map.first) {
						var className = $el.attr('class') || "";
						$el.attr('class', class_map.cssClass + " " + className.replace(class_map.cssClass, ""));
					}
					else {
						$el.addClass(class_map.cssClass);
					}
				});
			});
		}
		if (initSelectGxControl)
			GxPreview.selectControl(initSelectGxControl);
	});

	var importantify = function (dimension) {
		return dimension + " !important";
	};

	var setCssText = function ($el, css) {
		var cssText = [];
		for (var prop in css) {
			cssText.push(prop + ":" + importantify(css[prop]) + ";");
		}
		$el.css("cssText", cssText.join(""));
	};
	
	var absolutify = function (dimension) {
		return Math.abs(parseInt(dimension, 10)) + "px";
	};
			
	var getLayoutBoxEl = (function () {
		var layoutBoxEl,
			html = [
			'<div class="gxResetStyles gxBlockBackgroundColor gxLayoutBox gxLayoutBoxOffset">',
				'<div class="gxResetStyles gxLayoutBox">',
					'<div class="gxResetStyles gxLayoutBox">',
						'<div class="gxResetStyles gxLayoutBox">',
							'<div class="gxResetStyles gxLayoutBox">',
								'<div class="row">',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
									'<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">',
									'</div>',
								'</div>',
							'</div>',
						'</div>',
					'</div>',
				'</div>',
			'</div>'
		].join("");
		
		return function () {
			if (!layoutBoxEl) {
				layoutBoxEl = $(html);
			}
			return layoutBoxEl;
		};
	})();

	var lastSelectedControl;

	var MARGIN_COLOR = '#EDFF64',
		BORDER_COLOR = "#444",
		PADDING_COLOR = 'SlateBlue',
		CONTENT_COLOR = 'SkyBlue',
		GUTTER_COLOR = '#AAA',
		MIN_RESPONSIVE_TABLE_WIDTH = 690;

	return {
		clearSelection: function () {
			var $layoutBoxEl = getLayoutBoxEl();
			setCssText($layoutBoxEl, {
				top: "-10000px",
				left: "-10000px",
				visibility: "hidden"
			});
		},

		selectControl: (function () {
			var fn = function (selectedControl) {
				if (!selectedControl)
					return;
				lastSelectedControl = selectedControl;

				var $selectedEl = $("[data-gxcontrol='" + selectedControl + "']").children(':first-child');
				if ($selectedEl.length === 0)
					return;

				var	$layoutBoxEl = getLayoutBoxEl(),
					$marginEl = $layoutBoxEl.children(':first-child'),
					$borderEl = $marginEl.children(':first-child'),
					$paddingEl = $borderEl.children(':first-child'),
					$dimensionsEl = $paddingEl.children(':first-child'),
					$rowEl = $dimensionsEl.children('.row'),
					$colEl = $rowEl.children('.col-xs-1'),
					offset = $selectedEl.offset(),
					top = offset.top - $(document).scrollTop(),
					left = offset.left - $(document).scrollLeft(),
					height = $selectedEl.height(),
					width = $selectedEl.width(),
					marginTop = absolutify($selectedEl.css('margin-top')),
					marginRight = absolutify($selectedEl.css('margin-right')),
					marginBottom = absolutify($selectedEl.css('margin-bottom')),
					marginLeft = absolutify($selectedEl.css('margin-left')),
					borderTop = $selectedEl.css('border-top'),
					borderRight = $selectedEl.css('border-right'),
					borderBottom = $selectedEl.css('border-bottom'),
					borderLeft = $selectedEl.css('border-left'),
					paddingTop = $selectedEl.css('padding-top'),
					paddingRight = $selectedEl.css('padding-right'),
					paddingBottom = $selectedEl.css('padding-bottom'),
					paddingLeft = $selectedEl.css('padding-left'),
					showReponsiveGuides = $rowEl.length > 0 && $colEl.length > 0 && width >= MIN_RESPONSIVE_TABLE_WIDTH;
					
					if ($layoutBoxEl.parent().length === 0) {
						$(document.body).append($layoutBoxEl)
					}
					
					setCssText($layoutBoxEl, {
						top: top + "px",
						left: left + "px",
						visibility: 'visible'
					});
					setCssText($marginEl, {
						'padding-top': marginTop,
						'padding-right': marginRight,
						'padding-bottom': marginBottom,
						'padding-left': marginLeft,
						'background-color': MARGIN_COLOR
					});
					setCssText($borderEl, {
						'padding-top': borderTop,
						'padding-right': borderRight,
						'padding-bottom': borderBottom,
						'padding-left': borderLeft,
						'background-color': BORDER_COLOR
					});
					setCssText($paddingEl, {
						'padding-top': paddingTop,
						'padding-right': paddingRight,
						'padding-bottom': paddingBottom,
						'padding-left': paddingLeft,
						'background-color': PADDING_COLOR
					});
					setCssText($dimensionsEl, {
						width: width + "px",
						height: height + "px",
						'background-color': CONTENT_COLOR
					});
					// Responsive table guides
					if (showReponsiveGuides) {
						setCssText($rowEl, {
							display: 'block'
						});
						setCssText($colEl, {
							height: height + "px",
							'border-left': '15px solid ' + GUTTER_COLOR,
							'border-right': '15px solid ' + GUTTER_COLOR
						});
						setCssText($colEl.filter(':first-child'), {
							height: height + "px",
							'border-right': '15px solid ' + GUTTER_COLOR
						});
						setCssText($colEl.filter(':last-child'), {
							height: height + "px",
							'border-left': '15px solid ' + GUTTER_COLOR
						});
					}
					else {
						setCssText($rowEl, {
							display: 'none'
						});
					}
			};
			
			$(document).scroll(function () {
				if (lastSelectedControl) {
					fn(lastSelectedControl);
				}
			});
			
			$(window).resize(function () {
				if (lastSelectedControl) {
					fn(lastSelectedControl);
				}
			});
			
			return fn;
		})()
	};
})()
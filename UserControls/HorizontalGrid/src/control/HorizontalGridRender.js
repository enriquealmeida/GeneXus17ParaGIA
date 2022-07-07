gx.uc.HorizontalGrid = function ($) {
	var $container,
		waiting = false;

	var toBoolean = gx.lang.gxBoolean,
		gxDom = gx.dom;

	this.getCurrentPage = function () {
		this.CurrentPage = $container ? $container.slick("slickCurrentSlide") + 1 : 1;
		return this.CurrentPage;
	};

	this.setCurrentPage = function (value) {
		this.CurrentPage = value;
		if ($container) {
			$container.slick("slickGoTo", value - 1, true);
		}
	};

	this.show = function () {
		var baseId,
			gridHtml,
			responsiveCols = this.gxGridResponsiveCols,
			xsCols = responsiveCols[0],
			smCols = responsiveCols[1],
			mdCols = responsiveCols[2],
			lgCols = responsiveCols[3],
			xsRows = this.MultiRowsExtraSmall,
			smRows = this.MultiRowsSmall,
			mdRows = this.MultiRowsMedium,
			lgRows = this.MultiRowsLarge,
			slidesToScroll = toBoolean(this.Paged) ? false : 1,
			rtl = gx.dom.getComputedStyle(document.body).direction === 'rtl';

		$container = $(this.getContainerControl());
		baseId = $container.attr('id');

		$container.css("opacity", 0);
		gridHtml = this.drawGrid(baseId + "Tbl", !this.IsPostBack);

		if ($container.hasClass("slick-slider"))
			$container.slick('unslick');

		this.checkUnsupportedTableNesting();
		
		if (toBoolean(this.ShowArrows))
		{
			$container
				.css('margin-left', 10)
				.css('margin-right', 10);
		}

		$container.html(gridHtml);

		if (this.gxWidth) {
			$container.width(gxDom.addUnits(this.gxWidth, this.gxWidthUnit));
		}

		if (this.gxHeight) {
			$container.height(gxDom.addUnits(this.gxHeight, this.gxHeightUnit));
		}

		if (this.PageControllerClass) {
			$container.addClass(this.PageControllerClass);
		}

		// Control is created after runtime templates are applied, so the clones that Slick Carousel
		// creates are created on elements that have already been modified by the templates.
		if (!waiting) {
			waiting = true;
			gx.fx.obs.addObserver('gx.plugdesign.onafterapplytemplate', this, function () {
				var fn = (function () {
					gx.lang.requestAnimationFrame((function () {
						$container.on('init', function(){
							setTimeout(function () {$container.css("opacity", 1); }, 10);
							waiting = false;
						});
						if (rtl) {
							$container.parent().attr('dir', 'rtl')
						}
						$container.slick({
							arrows: toBoolean(this.ShowArrows),
							adaptiveHeight: false,
							autoplay: toBoolean(this.AutoPlay),
							autoplaySpeed: this.AutoPlaySpeed,
							centerMode: false,
							dots: toBoolean(this.ShowPageController),
							infinite: toBoolean(this.Infinite),
							variableWidth: toBoolean(this.VariableWidth),
							initialSlide: (this.CurrentPage || 1) - 1,
							speed: 500,
							slidesToShow: lgCols,
							slidesToScroll: slidesToScroll || lgCols,
							rows: lgRows,
							rtl: rtl,
							responsive: [
								{
									// XSmall
									breakpoint: 768,
									settings: {
										rows: xsRows,
										slidesToShow: xsCols,
										slidesToScroll: slidesToScroll || xsCols
									}
								},
								{
									// Small
									breakpoint: 992,
									settings: {
										rows: smRows,
										slidesToShow: smCols,
										slidesToScroll: slidesToScroll || smCols
									}
								},
								{
									// Medium
									breakpoint: 1200,
									settings: {
										rows: mdRows,
										slidesToShow: mdCols,
										slidesToScroll: slidesToScroll || mdCols
									}
								}
							]
						});
					}).closure(this));
				}).closure(this);

				if ($container.is("[data-gx-grid-rendering-additive-rows] #" + $container.get(0).id)) {
					$container
						.parent()
							.closest("[data-gx-grid-rendering-additive-rows]")
							.one("gx-grid:after-additive-rows-render", fn);
				}
				else {
					fn();
				}
			}, { single: true });
		}
	};

	this.destroy = function () {		
		if ($container) {
			$container.hide();
			$container.slick('unslick');
		}
	};

	this.checkUnsupportedTableNesting = function () {
		var el = this.getContainerControl(),
			style,
			error = false;

		while (el) {
			error = el.tagName == "TABLE" ||
					(el.tagName == "DIV" && el.getAttribute('data-align-inner') !== null) ||
					(el.tagName == "DIV" && el.getAttribute('data-align-outer') !== null);

			if (error)
			{
				gx.dbg.write("Important: Web Horizontal Grid does not support being nested neither inside aligned cells nor Table controls.", true);
				return;
			}

			el = el.parentNode;
		}
	};
	
	// Overrides
	this.appendContainerStart = gx.emptyFn;
	this.appendContainerEnd = gx.emptyFn;
	this.appendRowStart = gx.emptyFn;
	this.appendRowEnd = gx.emptyFn;
	this.getRowRenderingProps = function () {
		return {
			cls: "",
			tag: "div"
		};
	};
	this.appendFooterWrapperStart = gx.emptyFn;
	this.appendFooterWrapperEnd = gx.emptyFn;
	this.appendRowPrefix = gx.emptyFn;
}
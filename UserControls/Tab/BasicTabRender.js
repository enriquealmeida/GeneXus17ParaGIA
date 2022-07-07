gx.ui.controls.BasicTab = gx.ui.controls.defineTabControl(function ($) {
	var tabStripSelector = "",
		$container;

	var template = '<ul id="{{containerId}}_tabs" class="nav nav-tabs">' +
						'{{#ctx}}' + 
						'<li>' + 
							'<a id="Tab_{{panel.id}}" href="#{{code}}" data-target="#{{panel.id}}" data-toggle="tab" data-index="{{index}}" data-code="{{code}}">{{title.textContent}}</a>' + 
						'</li>' + 
						'{{/ctx}}'+
					'</ul>' + 
					'<div class="tab-content">'+
						'{{#ctx}}' + 
						'<div class="tab-pane" id="{{panel.id}}"></div>' + 
						'{{/ctx}}' +
					'</div>';

	this.render = function() {
		var container = this.getContainerControl(),
			containerId = container.id,
			currentGxClass;
			
		$container = $(container);
		$container.addClass("gx-basic-tab");

		tabStripSelector = '#' + containerId + '_tabs';

		var actx = [];
		for (var i=1; i<=this.PageCount; i++) {
			var ctx = {};
			ctx.index = i;
			ctx.panel = this.getChildContainer("panel" + i);
			ctx.title = this.getChildContainer("title" + i);
			ctx.code = $(ctx.title).contents('div').last().remove().text();
			if (ctx.panel && ctx.title)
				actx.push(ctx);
		}

		try {
			$container.append(Mustache.render(template, {
				ctx: actx, 
				containerId: containerId
			}));

			for (var i=0, len=actx.length; i<len; i++) {
				var $tabPane = $("#" + actx[i].panel.id);
				$(actx[i].panel).contents().each(function (i, el) {
					$tabPane[0].appendChild(el);
				});
				actx[i].panel.id = actx[i].panel.id + '_child';
				$tabPane[0].appendChild($(actx[i].panel)[0])
				var $tabItem = $("#Tab_" + actx[i].panel.id);
				$tabItem.data('target', '#' + actx[i].panel.id);
			}
		}
		catch(ex) {
			gx.dbg.write(ex.toString());
		}
	};
	
	this.afterRender = function () {
		var me = this;
		$(tabStripSelector)
			.find('a[data-toggle="tab"]')
				.on('click', function (e) {
					e.preventDefault();
					var tabIndex = parseInt($(this).attr("data-index"), 10);
					me.SelectTab(tabIndex);
				});
	};
	
	this.allways = function () {
		var className = this.Class || "";

		if (gx.lang.gxBoolean(this.Visible)) {
			$container.show();
		}
		else {
			$container.hide();
		}

		currentGxClass = $container.attr('data-gx-class');
		if (currentGxClass) {
			$container.removeClass(currentGxClass);
		}
		$container.attr("data-gx-class", className).addClass(className);
	};

	var findTabStripItem = function(i) {
		return $(tabStripSelector).find('li:nth-child(' + i + ')');
	};
	
	var getTabStripItemData = function ($tabStripItem) {
		var $tabStripItemAnchor = $tabStripItem.children('a');
		return {
			code: $tabStripItemAnchor.data('code'),
			index: $tabStripItemAnchor.data('index'),
			targetSelector: $tabStripItemAnchor.data('target')
		};
	};
	
	this.getTabPageIndexByControlName = function (controlName) {
		return $(tabStripSelector)
					.find('li a[data-code="' + controlName + '"]')
					.data('index');
	};

	this.getTabPageControlNameByIndex = function (index) {
		return $(tabStripSelector)
					.find('li a[data-index="' + index + '"]')
					.data('code');
	};

	this.selectTabPageByIndex = function (i) {
		var $tabStripItem = findTabStripItem(i);
		var tabItemData = getTabStripItemData($tabStripItem);
		// Set all the other tabs as not active
		$(tabStripSelector).find("li").removeClass("active");
		$container.find(".tab-pane").removeClass("active");
		
		// Set the selected tab strip item and its content pane as active
		$(tabItemData.targetSelector).addClass("active");
		$tabStripItem.addClass("active");
	};

	this.hideTabPageByIndex = function (i) {
		var $tabStripItem = findTabStripItem(i);
		var tabItemData = getTabStripItemData($tabStripItem);
		
		// Set the tab strip item and its content pane as hidden
		$tabStripItem.addClass("hidden");
		$(tabItemData.targetSelector).addClass("hidden");

		// If the tab was active, set the first visible tab as active
		if ($tabStripItem.hasClass('active')) {
			$tabStripItem.removeClass("active");
			$(tabStripSelector).find('li:visible a').each((function (i, el) {
				this.SelectTab(parseInt($(el).data("index"), 10));
				return false;
			}).closure(this));
		}
	};
	
	this.showTabPageByIndex = function (i) {
		var $tabStripItem = findTabStripItem(i);
		var tabItemData = getTabStripItemData($tabStripItem);
		$tabStripItem.removeClass("hidden");
		$(tabItemData.targetSelector).removeClass("hidden");
	};
});

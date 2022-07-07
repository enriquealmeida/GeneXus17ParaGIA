//Version 14.0

function DVelopBootstrapPanel(userControl) {

	this.control = userControl;
	this.containerName = userControl.ContainerName;
	this.header = userControl.ShowHeader;
	this.title = userControl.Title;

	this.render = function () {
		this.control.Collapsed = WWP_FixBooleanProperty(this.control.Collapsed);
		this.control.Collapsible = WWP_FixBooleanProperty(this.control.Collapsible);
		this.control.ShowCollapseIcon = WWP_FixBooleanProperty(this.control.ShowCollapseIcon);
		this.control.AutoScroll = WWP_FixBooleanProperty(this.control.AutoScroll);
		this.control.AutoWidth = WWP_FixBooleanProperty(this.control.AutoWidth);
		this.control.AutoHeight = WWP_FixBooleanProperty(this.control.AutoHeight);

		var panelDiv = document.createElement('div');
		panelDiv.id = 'DVelopBootstrapPanel_' + this.containerName;
		panelDiv.className = 'panel';
		if (this.control.Cls)
			panelDiv.className += ' ' + this.control.Cls;

		if (this.header) {
			var panelHeader = document.createElement('div');
			panelHeader.id = 'PanelHeader_' + this.containerName;
			panelHeader.className = 'panel-heading';

			if (this.control.Cls)
				panelHeader.className += ' ' + this.control.Cls + '_Header';

			var h3 = document.createElement('h3');
			h3.className = 'panel-title';

			var titleSpan = document.createElement('span');
			titleSpan.id = "Title_" + this.containerName;
			titleSpan.innerHTML = gx.getMessage(this.title);

			if (this.control.Collapsible) {
				var ref = document.createElement('a');
				ref.href = "#PanelBody_" + this.containerName;
				ref.className = 'accordion-toggle';
				ref.setAttribute("data-parent", "#PanelHeader_" + this.containerName);
				if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
					ref.setAttribute("data-toggle", "collapse");
				}
				var id = this.containerName;
				var thisC = this;
				ref.onclick = function () {
					$(this)[$("#PanelBody_" + id).hasClass('in') ? 'addClass' : 'removeClass']('collapsed');
					if ($("#PanelBody_" + id).hasClass('in')) {
						$("#" + id).addClass('main-panel-collapsed');
						$("#PanelBody_" + id).collapse('hide');
						thisC.control.Collapsed = true;
					} else {
						$("#" + id).removeClass('main-panel-collapsed');
						$("#PanelBody_" + id).collapse('show');
						thisC.control.Collapsed = false;
					}

					if ($("#" + id).parent().is('td') && (navigator.appVersion.toLowerCase().match('msie 8') || navigator.appVersion.toLowerCase().match('msie 7'))) {
						$(function () {
							$("#" + id).parent().height($("#" + id).height());
							$("#" + id).parent().css('vertical-align', 'top');
						})
					}

					if (!thisC.control.Collapsed && typeof (SetMinWidthPaginationBars) === 'function') {
						SetMinWidthPaginationBars();
					}
				}
				if (this.control.OnTitleClick)
					$(ref).bind("click", this.control.OnTitleClick);

				if (this.control.Collapsed)
					ref.className += ' collapsed';

				if (this.control.ShowCollapseIcon) {
					var toggleContainer = document.createElement('div');
					toggleContainer.className = 'toggle-container';

					var toggleIcon = document.createElement('div');
					toggleIcon.id = 'Toggle_' + this.containerName;
					toggleIcon.className = 'toggle-icon';

					ref.appendChild(toggleIcon);
					toggleContainer.appendChild(ref);

					if (this.control.IconPosition == 'right') {
						titleSpan.className = 'left';
						toggleIcon.className += ' toggle-icon-right';
						h3.appendChild(titleSpan);
						h3.appendChild(toggleContainer);
					} else {
						if (this.title != "")
							toggleContainer.className += ' left';
						h3.appendChild(toggleContainer);
						h3.appendChild(titleSpan);
					}
				} else {
					ref.appendChild(titleSpan);
					h3.appendChild(ref);
				}

				panelHeader.appendChild(h3);
			} else {
				h3.appendChild(titleSpan);
				panelHeader.appendChild(h3);
			}

			panelDiv.appendChild(panelHeader);
		}

		var panelBody = document.createElement('div');
		panelBody.id = 'PanelBody_' + this.containerName;
		panelBody.className = 'panel-collapse';

		if (this.control.AutoScroll)
			panelBody.className += ' panel-autoscroll';

		if (this.control.Cls)
			panelBody.className += ' ' + this.control.Cls + '_Body';

		if (this.control.Collapsed) {
			panelBody.className += ' collapse';
		} else {
			panelBody.className += ' in';
		}

		var body = document.getElementById(this.containerName + 'Body');
		if (!body) {
			var ucChild = this.control.getContainerControl().nextSibling;
			if ($(ucChild).hasClass('gx_usercontrol_child')) {
				var ucChildId = ucChild.id;
				var containerId = this.control.getContainerControl().id;
				if (ucChildId.startsWith(containerId) || ucChildId.length > 5 && DV_endsWith(containerId, ucChildId.substring(ucChildId.length - 5)) && ucChildId.startsWith(containerId.substr(0, containerId.length - 5))) {
					body = ucChild;
				}
			}
		}
		if (!body) {
			var num = this.containerName.substring(this.containerName.length - 5);
			body = document.getElementById(this.containerName.substring(0, this.containerName.length - 5) + 'Body' + num);
		}

		if (body) {
			body.className = 'panel-body';
			body.style.display = 'block';

			panelBody.appendChild(body);
		}

		panelDiv.appendChild(panelBody);

		jQuery("#" + this.containerName).addClass('panel-group');
		jQuery("#" + this.containerName).append(panelDiv);


		//Set width and height
		if (!this.control.AutoWidth) {
			var width = this.control.Width;
			if (width.substring(width.length - 1) == '%') {
				panelDiv.style.width = width;
				jQuery("#" + this.containerName).width(width);
			} else if (width.substring(width.length - 2) == 'px') {
				panelDiv.style.width = width;
			} else {
				panelDiv.style.width = width + 'px';
			}
		}

		if (!this.control.AutoHeight) {
			var height = this.control.Height;
			if (height.substring(height.length - 1) == '%') {
				body.style.height = height;
				jQuery("#" + this.containerName).height(height);
			} else if (height.substring(height.length - 2) == 'px') {
				body.style.height = height;
			} else {
				body.style.height = height + 'px';
			}
		}

		if (this.control.Collapsible && (this.control.AutoWidth || width.substring(width.length - 1) == '%')) {
			var panel = this;
			var headerWidth = this.control.Width;
			if (this.control.Width.substring(this.control.Width.length - 2) != 'px' && this.control.Width.substring(this.control.Width.length - 1) != '%')
				headerWidth = headerWidth + 'px';
			var setWidth = !this.control.AutoWidth;
			$(window).bind("load", function () {
				var body = jQuery("#PanelBody_" + panel.containerName);
				if (jQuery("#PanelBody_" + panel.containerName).width() <= 0) {
					if (setWidth) {
						jQuery("#PanelHeader_" + panel.containerName).css("width", headerWidth);
					} else {
						var parent = jQuery("#PanelBody_" + panel.containerName).parent();
						body.appendTo("body");
						if (jQuery("#PanelHeader_" + panel.containerName).width() < body.width()) {
							var padding = parseInt(jQuery("#PanelHeader_" + panel.containerName).css("padding-left")) + parseInt(jQuery("#PanelHeader_" + panel.containerName).css("padding-right"));
							var newWidth = body.width() - padding;
							jQuery("#PanelHeader_" + panel.containerName).width(newWidth);
							$(window).bind("resize", function () {
								var padding = parseInt(jQuery("#PanelHeader_" + panel.containerName).css("padding-left")) + parseInt(jQuery("#PanelHeader_" + panel.containerName).css("padding-right"));
								var newWidth = body.width() - padding;
								jQuery("#PanelHeader_" + panel.containerName).width(newWidth);
							});
						}
						$("#PanelBody_" + panel.containerName).remove();
						parent.append(body);
					}
				}
				else {
					if (jQuery("#PanelHeader_" + panel.containerName).width() < body.width()) {
						if (($(window).width() - (jQuery("#" + panel.containerName).position().left + body.width())) < 20) {
							jQuery("#PanelHeader_" + panel.containerName).css("width", headerWidth);
						} else {
							var body = jQuery("#PanelBody_" + panel.containerName);
							var padding = parseInt(jQuery("#PanelHeader_" + panel.containerName).css("padding-left")) + parseInt(jQuery("#PanelHeader_" + panel.containerName).css("padding-right"));
							var newWidth = body.width() - padding;
							jQuery("#PanelHeader_" + panel.containerName).width(newWidth);
							$(window).bind("resize", function () {
								var body = jQuery("#PanelBody_" + panel.containerName);
								var padding = parseInt(jQuery("#PanelHeader_" + panel.containerName).css("padding-left")) + parseInt(jQuery("#PanelHeader_" + panel.containerName).css("padding-right"));
								var newWidth = body.width() - padding;
								jQuery("#PanelHeader_" + panel.containerName).width(newWidth);
							});
						}
					}
				}
			});
		}

		var control = this.control;
		var contName = this.containerName;
		$(function () {
			var actions = $("#" + contName + " a.change-action").each(function (i) {
				$(this).click(function () {
					$("#DVelopBootstrapPanel_" + contName).css("overflow", "visible");
					$("#PanelBody_" + contName).css("overflow", "visible");
					$(".PopupBorder .PopupContent :button").each(
						function (i) {
							$(this).click(function () {
								$("#DVelopBootstrapPanel_" + contName).css("overflow", "");
								$("#PanelBody_" + contName).css("overflow", "");
							});
						}
					);

					$(".PopupBorder .PopupHeaderButton").each(
						function (i) {
							$(this).click(function () {
								$("#DVelopBootstrapPanel_" + contName).css("overflow", "");
								$("#PanelBody_" + contName).css("overflow", "");
							});
						}
					);
				});
			});
			//overflow visible when showing drop down list's options - overflow hidden when click on any control to hide drop down.
			if (!control.AutoScroll) {
				$("#" + contName).find(".dropdown-toggle").each(function (i) {
					$(this).click(function () {
						if ($(this).parent().hasClass("open")) {
							if ($('#DVelopBootstrapPanel_' + contName).css("overflow") == "visible" && $("#DVelopBootstrapPanel_" + contName).get(0).getAttribute('setH') == 'T') {
								$('#DVelopBootstrapPanel_' + contName).css("overflow", "hidden");
								$("#DVelopBootstrapPanel_" + contName).get(0).removeAttribute('setH');
							}
						} else {
							if ($("#DVelopBootstrapPanel_" + contName).css("overflow") == 'hidden') {
								$("#DVelopBootstrapPanel_" + contName).get(0).setAttribute('setH', 'T');
								$('#DVelopBootstrapPanel_' + contName).css("overflow", "visible");
							}
						}
					});

					$("*").click(function (e) {
						if (!$(e.target.offsetParent).hasClass("dropdown-toggle") && !$(e.target).hasClass("dropdown-toggle")) {
							if ($('#DVelopBootstrapPanel_' + contName).css("overflow") == "visible" && $("#DVelopBootstrapPanel_" + contName).get(0).getAttribute('setH') == 'T') {
								$('#DVelopBootstrapPanel_' + contName).css("overflow", "hidden");
								$("#DVelopBootstrapPanel_" + contName).get(0).removeAttribute('setH');
							}
						}
					});
				});
			}
		})
	}
	this.refresh = function () {
		$("#Title_" + this.containerName).html(gx.getMessage(this.control.Title));
	}
};

function DVelopBootstrapTabs(userControl) {

	this.control = userControl;
	this.containerName = userControl.ContainerName;
	this.designTabs = JSON.parse(userControl.DesignTimeTabs);
	this.tabs = new Array();
	this.tabCount = 0;
	this.openTabsCount = 0;

	if (this.designTabs != undefined && this.designTabs.length != undefined)
		this.tabCount = this.designTabs.length;

	for (var i = 0; i < this.tabCount; i++) {
		this.tabs[i] = new BootstrapTab(this.designTabs[i]["id"], this.containerName);
		this.openTabsCount += 1;
	}

	this.render = function () {
		var tabContainer = document.createElement('ul');
		tabContainer.id = 'DVelopBootstrapTabsContainer_' + this.containerName;
		tabContainer.className = 'nav nav-tabs';

		if (this.control.Cls)
			tabContainer.className += ' ' + this.control.Cls;

		var tabsTable = document.createElement('table');
		tabsTable.className = 'tabs-table';

		var tabContent = document.createElement('div');
		tabContent.id = 'DVelopBootstrapTabsContent_' + this.containerName;
		tabContent.className = 'tab-content';
		this.TabContainer = tabContainer;
		this.TabContent = tabContent;
		this.TabsTable = tabsTable;

		for (var i = 0; i < this.tabCount; i++) {
			var designTab = this.designTabs[i];
			this.RenderTab(tabContainer, tabContent, tabsTable, i, designTab, false);
		}

		var container = jQuery("#" + this.containerName);

		if (!this.control.AutoWidth) {
			container.width(this.control.Width);

			if (this.control.Width.substring(this.control.Width.length - 1) == '%' || this.control.Width.substring(this.control.Width.length - 2) == 'px') {
				tabsTable.style.width = this.control.Width;
			} else {
				tabsTable.style.width = this.control.Width + 'px';
			}
		}

		if (this.control.AutoScroll) {
			container.addClass('tab-container-scroll');
		} else {
			container.addClass('tab-container');
		}

		if (this.tabCount == 0)
			container.addClass('Tabs_NoTabs');

		//create table cell
		r = document.createElement('tr');
		c = document.createElement('td');
		c.appendChild(tabContainer);
		c.appendChild(tabContent);
		r.appendChild(c);
		tabsTable.appendChild(r);
		container.append(tabsTable);

		var tabsPanel = this;
		$(function () {
			tabsPanel.loadTabHeaders();
		});
	}

	this.RenderTab = function (tabContainer, tabContent, tabsTable, i, designTab, newTab) {
		var selected = false;

		if (this.control.SelectedTabIndex == i)
			selected = true;

		var tab = document.createElement('li');
		tab.id = this.containerName + '_' + designTab['id'];

		var ref = document.createElement('a');
		ref.href = "#" + this.containerName + designTab['id'];
		ref.setAttribute("data-toggle", "tab");

		if (newTab) {
			var titleDiv = document.createElement('div');
			titleDiv.setAttribute("id", this.containerName + 'Title' + designTab['id']);
			titleDiv.style.display = "block";
			ref.appendChild(titleDiv);
		}

		if (navigator.appVersion.toLowerCase().match('msie 8') || navigator.appVersion.toLowerCase().match('msie 7')) {
			ref.style.minHeight = "22px";
		}

		var tabContent_div = document.getElementById(this.containerName + designTab['id']);
		tabContent_div.style.display = '';
		tabContent_div.className = 'tab-pane fade';

		if (!this.control.AutoHeight) {
			var height = this.control.Height;
			if (height.substring(height.length - 1) == '%' || height.substring(height.length - 2) == 'px') {
				tabContent_div.style.height = height;
				tabsTable.style.height = height;
			} else {
				tabContent_div.style.height = height + 'px';
				tabsTable.style.height = height + 'px';
			}
		}

		if (selected) {
			this.setActiveTabId(designTab['id']);
			tab.className = 'active';
			tabContent_div.className += ' active in';
		}

		tab.appendChild(ref);
		tabContainer.appendChild(tab);
		tabContent.appendChild(tabContent_div);
	}

	this.loadTabHeaders = function () {
		var maxHeight = 0;

		for (var i = 0; i < this.tabCount; i++) {
			var tab = this.getTab(this.designTabs[i]['id']);

			var tabHeader = jQuery("#" + this.containerName + '_' + tab.id + " a:first-child");
			var tabTitle = jQuery("#" + this.containerName + 'Title' + tab.id);
			tabTitle.css("display", "block");

			if (tabTitle.height() > maxHeight)
				maxHeight = tabTitle.height();

			tabHeader.append(tabTitle);

			if (tab.title != "")
				tab.setTitle(tab.title, false, this);

			if (!tab.show)
				tab.hideTab();

			//set onclick handlers
			var tabsPanel = this;
			var clickedTabId = tab.id;
			tabHeader.bind("click", function (c) { return function () { tabsPanel.setActiveTabId(c); } }(clickedTabId));

			if (this.control.TabClicked)
				tabHeader.bind("click", this.control.TabClicked);
		}

		for (var i = 0; i < this.tabCount; i++) {
			var tab = this.getTab(this.designTabs[i]['id']);
			tab.setHeightToTab(maxHeight);
		}
		//IE8 hack for table heights
		if ($("#" + this.containerName).parent().is('td') && (navigator.appVersion.toLowerCase().match('msie 8') || navigator.appVersion.toLowerCase().match('msie 7'))) {
			$("#" + this.containerName).parent().css("_min-height: auto !important;");
			$("#" + this.containerName).parent().css("_height: auto !important;");
		}
	}

	this.setActiveTabId = function (tabId) {
		this.control.ActiveTabId = tabId;
		if ($("#" + this.containerName).parent().is('td') && (navigator.appVersion.toLowerCase().match('msie 8') || navigator.appVersion.toLowerCase().match('msie 7'))) {
			$("#" + this.containerName).parent().css("_min-height: auto !important;");
			$("#" + this.containerName).parent().css("_height: auto !important;");
		}
	}

	this.setActiveTabWhenHideClose = function (tabId) {
		var tabIndex = this.getTabIndex(tabId);
		if (tabIndex != null) {
			for (var i = tabIndex + 1; i < this.openTabsCount; i++) {
				if (this.tabs[i].show && !this.tabs[i].closed) {
					this.SelectTab(this.tabs[i].id);
					return;
				}
			}
			for (var i = tabIndex - 1; i >= 0; i--) {
				if (this.tabs[i].show && !this.tabs[i].closed) {
					this.SelectTab(this.tabs[i].id);
					return;
				}
			}
		}
		for (var i = 0; i < this.openTabsCount; i++) {
			if (tabIndex != i) {
				if (this.tabs[i].show && !this.tabs[i].closed) {
					this.SelectTab(this.tabs[i].id);
					break;
				}
			}
		}
	}

	this.getTab = function (tabId) {
		for (var i = 0; i < this.openTabsCount; i++) {
			if (tabId == this.tabs[i].id) {
				return this.tabs[i];
			}
		}

		return null;
	}

	this.getTabIndex = function (tabId) {
		for (var i = 0; i < this.openTabsCount; i++) {
			if (tabId == this.tabs[i].id) {
				return i;
			}
		}
	}

	//METHODS

	this.setTabTitle = function (tabId, title) {
		var tab = this.getTab(tabId);
		if (tab)
			tab.setTitle(title, false, this);
	}

	this.setIFrameTabTitle = function (tabId, title) {
		var tab = this.getTab(tabId);
		if (tab)
			tab.setTitle(title, true, this);
	}

	this.GetIFrameTabIdByUrl = function (url) {
		if (this.IFrameId != null) {
			for (var i = 1; i <= this.IFrameId; i++) {
				var existantTabDiv = document.getElementById(this.containerName + 'IFrameTab' + i);
				if (existantTabDiv != null
					&& existantTabDiv.childNodes.length > 0
					&& existantTabDiv.childNodes[0].tagName == "IFRAME"
					&& (existantTabDiv.childNodes[0].getAttribute('src') == url || DV_endsWith(existantTabDiv.childNodes[0].getAttribute('src'), '/' + url))
					/*&& (existantTabDiv.childNodes[0].contentWindow.location.href.length == null
						|| existantTabDiv.childNodes[0].src == existantTabDiv.childNodes[0].contentWindow.location.href
						|| existantTabDiv.childNodes[0].contentWindow.location.href.length > existantTabDiv.childNodes[0].src.length
							&& existantTabDiv.childNodes[0].src == existantTabDiv.childNodes[0].contentWindow.location.href.substring(0, existantTabDiv.childNodes[0].src.length))*/) {

					return 'IFrameTab' + i;
				}
			}
		}
		return null;
	}

	this.HasIFrameTab = function () {
		if (this.IFrameId != null) {
			for (var i = 1; i <= this.IFrameId; i++) {
				if (document.getElementById(this.containerName + 'IFrameTab' + i)) {
					return true;
				}
			}
		}
		return false;
	}

	this.UpdateIFrameTabCaption = function (url, title) {
		var tabId = this.GetIFrameTabIdByUrl(url);
		if (tabId != null) {
			this.setIFrameTabTitle(tabId, title);
		}
	}

	this.ReplaceIFrameTab = function (oldUrl, newUrl, title) {
		var tabId = this.GetIFrameTabIdByUrl(oldUrl);
		if (tabId != null) {
			var iFrameContainer = document.getElementById(this.containerName + tabId);
			iFrameContainer.removeChild(iFrameContainer.childNodes[0]);
			var iframeElem = document.createElement('iframe');
			iframeElem.setAttribute("src", newUrl);
			iframeElem.urlHist = [];
			$(iframeElem).bind("load", function (e) {
				this.urlHist.push(this.contentWindow.location + '');
			});
			iFrameContainer.appendChild(iframeElem);
			iframeElem.onload = function () {
				$(this.contentDocument).find('html').addClass('TabsIFrame');
			};
			this.setIFrameTabTitle(tabId, title);
		}
	}

	this.CloseIFrameTab = function (url) {
		var tabId = this.GetIFrameTabIdByUrl(url);
		if (tabId != null) {
			this.removeIFrameTab(tabId);
		}
	}

	this.addIFrameTab = function (url, title) {

		if (this.IFrameId == null) {
			this.IFrameId = 0;
		}
		else {
			var tabId = this.GetIFrameTabIdByUrl(url);
			if (tabId != null) {
				this.SelectTab(tabId);
				return;
			}
		}
		this.IFrameId++;

		$("#" + this.containerName).removeClass('Tabs_NoTabs');

		var designTab = { id: "IFrameTab" + this.IFrameId };
		var addedTabId = designTab['id'];

		var containerDiv = document.createElement('div');
		containerDiv.setAttribute("id", this.containerName + addedTabId);

		var iframeElem = document.createElement('iframe');
		iframeElem.setAttribute("src", url);
		iframeElem.urlHist = [];
		$(iframeElem).bind("load", function (e) {
			this.urlHist.push(this.contentWindow.location + '');
		});
		containerDiv.appendChild(iframeElem);
		iframeElem.onload = function () {
			$(this.contentDocument).find('html').addClass('TabsIFrame');
		};

		this.TabContent.appendChild(containerDiv);
		this.RenderTab(this.TabContainer, this.TabContent, this.TabsTable, -1, designTab, true);

		var tab = new BootstrapTab(addedTabId, this.containerName);
		this.tabs[this.openTabsCount] = tab;
		this.openTabsCount += 1;

		tab.setTitle(title, true, this);
		this.SelectTab(addedTabId);
		this.setActiveTabId(addedTabId);


		var tabHeader = jQuery("#" + this.containerName + '_' + addedTabId + " a:first-child");

		//set onclick handlers
		var tabsPanel = this;
		tabHeader.bind("click", function (c) { return function () { tabsPanel.setActiveTabId(c); } }(addedTabId));

		if (this.control.TabClicked)
			tabHeader.bind("click", this.control.TabClicked);
	}

	this.SelectTab = function (tabId) {
		var tab = this.getTab(tabId);
		if (tab) {
			if (!tab.show)
				tab.showTab();

			var tabHeader = jQuery("#" + this.containerName + '_' + tab.id + " a:first-child");
			tabHeader.trigger('click');
		}
	}

	this.showTab = function (tabId) {
		var tab = this.getTab(tabId);
		if (tab) {
			tab.showTab();
		}
	}

	this.removeIFrameTab = function (tabId) {
		this.hideTabById(tabId);
		var tabContent_div = document.getElementById(this.containerName + tabId);
		if (tabContent_div != null) {
			tabContent_div.parentNode.removeChild(tabContent_div);
		}
		var tabTitle_li = document.getElementById(this.containerName + '_' + tabId);
		if (tabTitle_li != null) {
			tabTitle_li.parentNode.removeChild(tabTitle_li);
		}
		if (this.control.ActiveTabId == tabId) {
			this.setActiveTabWhenHideClose(tabId);
		}

		if ((this.designTabs == null || this.designTabs.length == 0) && !this.HasIFrameTab()) {
			$("#" + this.containerName).addClass('Tabs_NoTabs');
		}
	}

	this.hideTabById = function (tabId) {
		var tab = this.getTab(tabId);
		if (tab) {
			tab.hideTab();

			if (this.control.ActiveTabId == tabId) {
				this.setActiveTabWhenHideClose(tabId);
			}
		}
	}

	this.hideTabByIndex = function (tabIndex) {
		if (tabIndex < this.tabs.length) {
			var tab = this.tabs[tabIndex];
			if (tab) {
				tab.hideTab();

				if (this.control.ActiveTabId == tab.id) {
					this.setActiveTabWhenHideClose(tab.id);
				}
			}
		}
	}

}

function BootstrapTab(tabIdParm, containerNameParm) {
	this.containerName = containerNameParm;
	this.id = tabIdParm;
	this.title = "";
	this.show = true;
	this.closed = false;

	this.setTitle = function (titleParm, addDelete, tabsElement) {
		this.title = gx.getMessage(titleParm);
		var tabTitle = jQuery("#" + this.containerName + '_' + this.id + " a:first-child span:first-child");

		if (tabTitle.length != 0) {
			if (tabTitle[0].childNodes.length > 1) {
				tabTitle.contents().filter(function () { return this.nodeType == Node.TEXT_NODE; })[0].textContent = this.title;
			}
			else {
				tabTitle.text(this.title);
			}
		} else {
			var tabTitleDiv = jQuery("#" + this.containerName + 'Title' + this.id);
			if (tabTitleDiv.length != 0) {
				var titleSpan = document.createElement('span');
				titleSpan.innerText = this.title;
				tabTitleDiv.prepend(titleSpan);

				if (addDelete) {
					var iDel = document.createElement('button');
					iDel.setAttribute("class", "close closeTab");
					iDel.innerHTML = '×';
					var tabs = tabsElement;
					var tabId = this.id;

					$(iDel).bind("click", function (e) {
						tabs.removeIFrameTab(tabId);
						e.preventDefault();
						e.stopPropagation();
					});
					titleSpan.appendChild(iDel);
				}
			}
		}
	}

	this.setHeightToTab = function (height) {
		jQuery("#" + this.containerName + 'Title' + this.id).height(height);
	}

	this.showTab = function () {
		this.show = true;

		var tab = jQuery("#" + this.containerName + '_' + this.id);
		tab.css("display", "block");

		var tabBody = jQuery("#" + this.containerName + this.id);
		tabBody.css("display", "block");
	}

	this.hideTab = function () {
		this.show = false;

		var tab = jQuery("#" + this.containerName + '_' + this.id);
		tab.css("display", "none");

		var tabBody = jQuery("#" + this.containerName + this.id);
		tabBody.css("display", "none");
	}

	this.closeTab = function () {
		this.closed = true;
	}
}

function DVelopBootstrapConfirmPanel(userControl) {

	this.control = userControl;
	this.containerName = userControl.ContainerName;

	this.render = function () {
		var myModal = jQuery("#" + this.containerName);
		myModal.addClass('modal fade');
		if (this.control.Class != '') {
			myModal.addClass(this.control.Class);
		}
		myModal.attr('tabindex', '-1');
		myModal.attr('role', 'dialog');
		myModal.attr('aria-labelledby', this.containerName + "_Title");
		myModal.attr('aria-hidden', 'true');

		var modalDialog = document.createElement('div');
		modalDialog.className = 'modal-dialog';

		var modalContent = document.createElement('div');
		modalContent.className = 'modal-content';

		var modalHeader = document.createElement('div');
		modalHeader.className = 'modal-header';

		var closeButton = this.createButton('CloseButton', 'close', '&times', 2);
		closeButton.setAttribute('aria-hidden', 'true');

		var modalTitle = document.createElement('h4');
		modalTitle.id = this.containerName + "_Title";
		modalTitle.className = 'modal-title';

		var modalBody = document.createElement('div');
		modalBody.className = 'modal-body';

		var confirmationText = document.createElement('span');
		confirmationText.id = this.containerName + "_ConfirmationText";
		if (this.control.TextType == 1) {
			modalTitle.innerText = gx.getMessage(this.control.Title);
			confirmationText.innerText = gx.getMessage(this.control.ConfirmationText);
		}
		else {
			modalTitle.innerHTML = gx.getMessage(this.control.Title);
			confirmationText.innerHTML = gx.getMessage(this.control.ConfirmationText);
		}

		var body = document.getElementById(this.containerName + "Body");
		body = body == null ? document.createElement(this.containerName + "Body") : body;
		body.style.display = 'block';

		var modalFooter = document.createElement('div');
		modalFooter.className = 'modal-footer';

		var saveButton = this.createButton('SaveButton', 'btn btn-primary btn-yes-DVelop', this.control.YesButtonCaption, this.control.TextType);
		var dismissButton;
		var cancelButton;

		if (this.control.ConfirmType == '1' || this.control.ConfirmType == '2')
			dismissButton = this.createButton('DismissButton', 'btn btn-default btn-no-DVelop', this.control.NoButtonCaption, this.control.TextType);

		if (this.control.ConfirmType == '2')
			cancelButton = this.createButton('CancelButton', 'btn btn-default btn-cancel-DVelop', this.control.CancelButtonCaption, this.control.TextType);

		if (this.control.YesButtonPosition == 'right') {
			if (this.control.ConfirmType == '2')
				modalFooter.appendChild(cancelButton);

			if (this.control.ConfirmType == '1' || this.control.ConfirmType == '2')
				modalFooter.appendChild(dismissButton);

			modalFooter.appendChild(saveButton);
		}
		else {
			modalFooter.appendChild(saveButton);

			if (this.control.ConfirmType == '1' || this.control.ConfirmType == '2')
				modalFooter.appendChild(dismissButton);

			if (this.control.ConfirmType == '2')
				modalFooter.appendChild(cancelButton);
		}

		modalHeader.appendChild(closeButton);
		modalHeader.appendChild(modalTitle);
		modalBody.appendChild(confirmationText);
		modalBody.appendChild(body);
		modalContent.appendChild(modalHeader);
		modalContent.appendChild(modalBody);
		modalContent.appendChild(modalFooter);
		modalDialog.appendChild(modalContent);
		myModal.append(modalDialog);

		var confirmPanel = this;
		$(function () {
			var yesBtn = $("#" + confirmPanel.containerName + "_" + "SaveButton");
			confirmPanel.bindClickHandlers(yesBtn, "Yes", confirmPanel.control.onYes, confirmPanel.control.Close);

			var closeBtn = $("#" + confirmPanel.containerName + "_" + "CloseButton");
			confirmPanel.bindClickHandlers(closeBtn, "Close", null, confirmPanel.control.Close);

			if (confirmPanel.control.ConfirmType == '1' || confirmPanel.control.ConfirmType == '2') {
				var noBtn = $("#" + confirmPanel.containerName + "_" + "DismissButton");
				confirmPanel.bindClickHandlers(noBtn, "No", confirmPanel.control.onNo, confirmPanel.control.Close);
			}

			if (confirmPanel.control.ConfirmType == '2') {
				var cancelBtn = $("#" + confirmPanel.containerName + "_" + "CancelButton");
				confirmPanel.bindClickHandlers(cancelBtn, "Cancel", confirmPanel.control.onCancel, confirmPanel.control.Close);
			}
		});
	}

	this.createButton = function (id, className, caption, textType) {
		var button = document.createElement('button');
		button.id = this.containerName + "_" + id;
		button.className = className;
		button.setAttribute('type', 'button');
		button.setAttribute('data-dismiss', 'modal');
		if (textType == 1)
			button.innerText = gx.getMessage(caption);
		else
			button.innerHTML = gx.getMessage(caption);

		return button;
	}

	this.bindClickHandlers = function (btn, result, btnHandler, closeHandler) {
		var confirmPanel = this;
		btn.bind("click", function (r) { return function () { confirmPanel.seResult(r); } }(result));
		if (btnHandler)
			btn.bind("click", btnHandler);
		if (closeHandler)
			btn.bind("click", closeHandler);
	}

	this.seResult = function (result) {
		this.control.Result = result;
	}

	//METHODS

	this.openModal = function () {
		if (this.control.TextType == 1) {
			$("#" + this.containerName + "_Title").text(gx.getMessage(this.control.Title));
			$("#" + this.containerName + "_ConfirmationText").text(gx.getMessage(this.control.ConfirmationText));
		}
		else {
			$("#" + this.containerName + "_Title").html(gx.getMessage(this.control.Title));
			$("#" + this.containerName + "_ConfirmationText").html(gx.getMessage(this.control.ConfirmationText));
		}
		$("#" + this.containerName).modal('show');
	}

	this.closeModal = function () {
		$("#" + this.containerName).modal('hide');
	}
}

function DVelopBootstrapDropDownOptions(userControl) {

	this.control = userControl;
	this.containerName = userControl.ContainerName;
	this.containerToDrawName = this.containerName;
	this.myT = null;
	this.calendarOpened = false;
	this.dateFilterVarName = '';
	this.m_UlDropDown;
	this.m_triggerButton;
	this.SelectedValue_getAUX = '';
	this.SelectedText_getAUX = '';

	this.ColumnsSelectorData_Aux;

	this.IsDate = function () {
		return this.control.FilterType == 'Date';
	}

	this.render = function () {
		this.control.Enabled = WWP_FixBooleanProperty(this.control.Enabled);
		this.control.Visible = WWP_FixBooleanProperty(this.control.Visible);
		this.control.IncludeAddNewOption = WWP_FixBooleanProperty(this.control.IncludeAddNewOption);
		this.control.IncludeOnlySelectedOption = WWP_FixBooleanProperty(this.control.IncludeOnlySelectedOption);
		this.control.IncludeSelectAllOption = WWP_FixBooleanProperty(this.control.IncludeSelectAllOption);
		this.control.EmptyItem = WWP_FixBooleanProperty(this.control.EmptyItem);
		this.control.IncludeFilter = WWP_FixBooleanProperty(this.control.IncludeFilter);
		this.control.FilterIsRange = WWP_FixBooleanProperty(this.control.FilterIsRange);
		this.control.IncludeDataList = WWP_FixBooleanProperty(this.control.IncludeDataList);
		this.control.IncludeSortASC = WWP_FixBooleanProperty(this.control.IncludeSortASC);
		this.control.IncludeSortDSC = WWP_FixBooleanProperty(this.control.IncludeSortDSC);
		this.control.AllowGroup = WWP_FixBooleanProperty(this.control.AllowGroup);
		this.control.AllowMultipleSelection = WWP_FixBooleanProperty(this.control.AllowMultipleSelection);
		this.control.AllowMultipleSelection = this.control.AllowMultipleSelection && (this.control.IncludeDataList || this.control.DropDownOptionsType == "ExtendedComboBox");
		if (this.control.IncludeDataList && this.control.DataListType.indexOf("Dynamic") == 0 && this.control.DataListProc.trim() == "") {
			this.control.IncludeDataList = false;
		}
		this.control.IncludeOnlySelectedOption = this.control.IncludeOnlySelectedOption && this.control.AllowMultipleSelection;
		this.control.IncludeSelectAllOption = this.control.IncludeSelectAllOption && this.control.AllowMultipleSelection;
		if (this.control.AllowMultipleSelection && this.control.SelectedValue_get == '') {
			this.control.SelectedValue_get = '[]';
			this.control.SelectedText_get = '[]';
		}
		if (this.control.FilterIsRange && this.control.DataListType == "Dynamic (with fixedValues)") {
			this.control.FilterIsRange = false;
		}
		this.control.DropDownOptionsTitleSettingsIcons_IsFI = (this.control.DropDownOptionsTitleSettingsIcons != null && this.control.DropDownOptionsTitleSettingsIcons["def"] != null && this.control.DropDownOptionsTitleSettingsIcons["def"].length > 0);

		if (this.control.TitleControlIdToReplace != '') {
			this.containerToDrawName = this.control.TitleControlIdToReplace;
			var isInput = false;
			var btnElement;
			if ($("#" + this.containerToDrawName).length == 0) {
				var objsByName = document.getElementsByName(this.containerToDrawName);
				if (objsByName.length == 1) {
					if (objsByName[0].nodeName == 'INPUT') {
						//case of gx button
						isInput = true;
						var btnElement = objsByName[0];
					} else {
						objsByName[0].setAttribute('id', this.containerToDrawName);
					}
				} else if (this.control.DropDownOptionsType == "GridTitleSettings") {
					//work around to fix empty GridInternalName bug after export report
					if (this.containerToDrawName.toLowerCase().startsWith(this.GetWebComponentPrefix().toLowerCase() + 'grid_')) {
						var containerToDrawNameAux = $('#' + this.GetWebComponentPrefix() + 'GridContainerDiv').find('thead').find('#' + this.containerToDrawName.substring(this.GetWebComponentPrefix().length + 4));
						if (containerToDrawNameAux.length == 1) {
							containerToDrawNameAux[0].setAttribute('id', this.containerToDrawName);
						}
					}
				}
			}
			else if ($("#" + this.containerToDrawName)[0].nodeName == 'INPUT') {
				//case of gx button in Tero
				isInput = true;
				var btnElement = $("#" + this.containerToDrawName)[0];
			}
			if (isInput) {
				var btnParent = btnElement.parentElement;
				var btnDivForDDO = document.createElement('div');
				btnDivForDDO.setAttribute('id', this.containerToDrawName);
				btnParent.insertBefore(btnDivForDDO, btnElement);
				btnParent.removeChild(btnElement);
			}
		}
		$("#" + this.containerToDrawName).html('');
		$("#" + this.containerToDrawName).unbind();
		if (!this.control.Visible) {
			return;
		}

		if (this.control.DropDownOptionsType == "ExtendedComboBox") {
			if (this.control.SelectedText == '' && (!this.control.AllowMultipleSelection || this.control.SelectedValue != '')) {
				if (this.control.SelectedValue == '' && this.control.EmptyItem) {
					this.control.SelectedText = gx.getMessage(this.control.EmptyItemText);
				} else {
					this.loadExtendedComboSelectedText(this.control.DropDownOptionsData, null, true);
				}
			}
			$("#" + this.containerToDrawName).off('hide.bs.dropdown').on('hide.bs.dropdown', function (thisC) {
				return function (e) {
					return thisC.OnDDOClosingRemoveError();
				}
			}(this));
		}

		if (!this.control.Enabled && this.control.DropDownOptionsType == "ExtendedComboBox") {
			var selTextSpan = document.createElement('span');
			selTextSpan.className = '';
			var classList = this.control.Cls.split(/\s+/);
			for (var i = 0; i < classList.length; i++) {
				if (selTextSpan.className != '') {
					selTextSpan.className += ' ';
				}
				selTextSpan.className += 'Readonly' + classList[i];
			}
			if (this.control.AllowMultipleSelection && this.control.SelectedText != null && this.control.SelectedText.startsWith('[') && DV_endsWith(this.control.SelectedText, ']')) {
				var selectedTextStr = '';
				var selectedTextObjs = JSON.parse(this.control.SelectedText);
				for (var index2 = 0; index2 < selectedTextObjs.length; index2 += 1) {
					selectedTextStr += (index2 > 0 ? this.control.MultipleValuesSeparator : '') + selectedTextObjs[index2];
				}
				selTextSpan.innerText = selectedTextStr;
			} else {
				selTextSpan.innerText = this.control.SelectedText;
			}
			$("#" + this.containerToDrawName).append(selTextSpan);

			//create other hidden for the AttributeFL selector
			selTextSpan = document.createElement('span');
			selTextSpan.className = 'Readonly' + this.control.Cls;
			selTextSpan.style.display = 'none';
			$("#" + this.containerToDrawName).append(selTextSpan);

			$("#" + this.containerToDrawName).addClass("ReadonlyExtendedCombo");
		}
		else {

			$("#" + this.containerToDrawName).addClass(this.control.Cls);
			if ($("#" + this.containerToDrawName).parent().parent().parent().css("text-align") == 'right' || $("#" + this.containerToDrawName).parent().parent().parent().css("text-align") == 'end') {
				$("#" + this.containerToDrawName).parent().addClass(this.control.Cls + 'AlignTitleRight');
			}
			$("#" + this.containerToDrawName).addClass("drop-down-options btn-group");

			var triggerButton = document.createElement('button');
			var triggerButtonId = this.containerName + "_btnGroupDrop";
			triggerButton.id = triggerButtonId;
			triggerButton.className = "btn btn-default dropdown-toggle";
			triggerButton.setAttribute("type", "button");
			triggerButton.setAttribute("data-toggle", "dropdown");

			var triggerButtonIcon = null;
			var triggerButtonFontIcon = null;
			if (this.control.UseAsGridTitleSetting) {
				if (this.control.DropDownOptionsTitleSettingsIcons == null) {
					this.control.DropDownOptionsTitleSettingsIcons = {};
				}
				var filtered = false;
				if (!this.control.AllowMultipleSelection || this.control.TitleControlIdToReplace != '') {
					filtered = (this.control.IncludeDataList && !this.IsValueEmpty(this.control.SelectedValue))
							|| (this.control.IncludeFilter && (!this.IsValueEmpty(this.control.FilteredText) || (this.control.FilterIsRange && !this.IsValueEmpty(this.control.FilteredTextTo))))
							|| (this.control.SelectedFixedFilter != '');
				}
				triggerButtonIcon = gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["Default"]);
				triggerButtonFontIcon = this.control.DropDownOptionsTitleSettingsIcons["def"];
				if (this.control.IncludeSortASC && (this.control.SortedStatus == 'ASC' || this.control.SortedStatus == 'ASC GRP')) {
					if (filtered) {
						triggerButtonIcon = gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["FilteredSortedASC"]);
						triggerButtonFontIcon = this.control.DropDownOptionsTitleSettingsIcons["fasc"];
						$(triggerButton).addClass('FilteredSortedASC');
					} else {
						triggerButtonIcon = gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["SortedASC"]);
						triggerButtonFontIcon = this.control.DropDownOptionsTitleSettingsIcons["asc"];
						$(triggerButton).addClass('SortedASC');
					}
				} else if (this.control.IncludeSortDSC && (this.control.SortedStatus == 'DSC' || this.control.SortedStatus == 'DSC GRP')) {
					if (filtered) {
						triggerButtonIcon = gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["FilteredSortedDSC"]);
						triggerButtonFontIcon = this.control.DropDownOptionsTitleSettingsIcons["fdsc"];
						$(triggerButton).addClass('FilteredSortedDSC');
					} else {
						triggerButtonIcon = gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["SortedDSC"]);
						triggerButtonFontIcon = this.control.DropDownOptionsTitleSettingsIcons["dsc"];
						$(triggerButton).addClass('SortedDSC');
					}
				} else if (filtered) {
					triggerButtonIcon = gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["Filtered"]);
					triggerButtonFontIcon = this.control.DropDownOptionsTitleSettingsIcons["fil"];
					$(triggerButton).addClass('Filtered');
				}
			}

			if (this.control.Tooltip.length > 0)
				triggerButton.setAttribute("title", gx.getMessage(this.control.Tooltip));

			var ulDropDown = document.createElement('ul');
			ulDropDown.className = "dropdown-menu";
			ulDropDown.setAttribute("role", "menu");
			ulDropDown.setAttribute("aria-labelledby", this.containerName + "_btnGroupDrop");

			//check if some option has image
			var images = false;
			for (var index = 0; this.control.DropDownOptionsData != null && index < this.control.DropDownOptionsData.length; index += 1) {
				if (this.control.DropDownOptionsData[index]["Icon"] != null && this.control.DropDownOptionsData[index]["Icon"].length > 0
					|| this.control.DropDownOptionsData[index]["FontIcon"] != null && this.control.DropDownOptionsData[index]["FontIcon"].length > 0) {

					images = true;
					break;
				}
			}
			this.m_UlDropDown = ulDropDown;
			this.m_triggerButton = triggerButton;

			if (this.control.TitleControlAlign != 'Right') {
				$("#" + this.containerToDrawName).bind('shown.bs.dropdown', function (thisC) {
					return function (e) {
						if (thisC.control.TitleControlAlign == 'Automatic') {
							dropdownAutoPosition("#" + thisC.containerToDrawName, ulDropDown, triggerButton);
						} else {
							dropdownInvertPosition(ulDropDown, triggerButton);
						}
					}
				}(this));
			}
			$("#" + this.containerToDrawName).bind('hidden.bs.dropdown', function (thisC) {
				return function (e) {
					thisC.setSectionGridMinHeight(false);
					thisC.shiftPageFooter(false);
				}
			}(this));
			$("#" + this.containerToDrawName).bind('shown.bs.dropdown', function (thisC) {
				return function (e) {
					thisC.setSectionGridMinHeight(true);
					thisC.shiftPageFooter(true);
					if (thisC.applyDateFocusHack) {
						var inputVal = $('#' + thisC.GetDateFilterVariable()).get(0);
						$(inputVal).focus();
						$(thisC.m_triggerButton).focus();
					}
					if (thisC.control.DropDownOptionsType == "ExtendedComboBox" && thisC.m_filterInput != null) {
						$(thisC.m_filterInput).focus();
					}
				}
			}(this));

			if (this.control.DropDownOptionsType == "ExtendedComboBox") {
				var ulOptions = this.loadExtendedComboBox(ulDropDown);
				$(triggerButton).bind("click", function (thisC, ulOptions, ulDropDown) {
					return function (e) {
						if ($("#" + thisC.containerToDrawName).attr('class').indexOf(' open') == -1) {
							thisC.updateExtendedComboList(ulOptions, '');
							if (thisC.m_filterInput != null) {
								$(thisC.m_filterInput).val('');
							}
						}
					}
				}(this, ulOptions, ulDropDown));
				if (this.control.AllowMultipleSelection && this.control.OnOptionClicked) {
					$("#" + this.containerToDrawName).off('hide.bs.dropdown').on('hide.bs.dropdown', this.control.OnOptionClicked);
				}
			}
			else {
				for (var index = 0; this.control.DropDownOptionsData != null && index < this.control.DropDownOptionsData.length; index += 1) {
					var item = this.control.DropDownOptionsData[index];
					var liDropDownItem = document.createElement('li');
					var filterInput = null;
					var filterInputTo = null;

					if (item["IsDivider"]) {
						liDropDownItem.className = "divider";
						//disable onclick close
						$(liDropDownItem).bind("click", function (e) {
							e.preventDefault();
							e.stopPropagation();
						});
					}
					else {
						if (item["EventKey"] == '<#RawHTML#>') {
							$(liDropDownItem).html(item["Title"]);
						} else if (item["EventKey"] == '<#FilterOptions#>') {

							//remove filter LI - Start
							var liRemoveFilter = document.createElement('li');
							if (!filtered) {
								$(liRemoveFilter).css("display", "none");
							}
							var aRemoveFilter = document.createElement('a');
							var spanRemoveFilter = document.createElement('span');
							spanRemoveFilter.innerHTML = gx.getMessage(this.control.CleanFilter);

							$(aRemoveFilter).bind("click", function (thisC) {
								return function (e) {
									if (thisC.IsDate()) {
										thisC.DetachAuxDateVariable(thisC.GetDateFilterVariable() + "_dp_container");
										if (thisC.control.FilterIsRange) {
											thisC.DetachAuxDateVariable(thisC.GetDateFilterVariableTo() + "_dp_container");
										}
									}
									thisC.SetActiveEventKey('<#Filter#>');
									thisC.SetFilteredText('');
									if (thisC.control.FilterIsRange) {
										thisC.SetFilteredTextTo('');
									}
									var emptyVal = '';
									if (thisC.control.AllowMultipleSelection) {
										emptyVal = '[]';
									}
									thisC.SetSelectedValue(emptyVal);
									thisC.SetSelectedText(emptyVal);
								}
							}(this));
							if (this.control.OnOptionClicked) {
								if (this.IsDate()) {
									$(aRemoveFilter).bind("click", function (thisC) {
										return function (e) {
											thisC.calendarOpened = false;
										}
									}(this));
								}
								$(aRemoveFilter).bind("click", this.control.OnOptionClicked);
							}

							aRemoveFilter.setAttribute("href", DDO_GetLocalURL());
							//to keep URLs
							if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
								aRemoveFilter.setAttribute("data-toggle", "dropdown");
							}

							aRemoveFilter.className = "image";
							aRemoveFilter.setAttribute("style", "margin-bottom:10px");

							if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
								var imgRemoveFilter = document.createElement('i');
								imgRemoveFilter.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["cle"]);
								aRemoveFilter.appendChild(imgRemoveFilter);
							}
							else {
								var imgRemoveFilter = document.createElement('img');
								imgRemoveFilter.setAttribute("src", gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["OptionCleanFilters"]));
								aRemoveFilter.appendChild(imgRemoveFilter);
							}

							aRemoveFilter.appendChild(spanRemoveFilter);
							liRemoveFilter.appendChild(aRemoveFilter);
							ulDropDown.appendChild(liRemoveFilter);

							if (filtered) {
								var liRemoveFilterDivider = document.createElement('li');
								liRemoveFilterDivider.className = "divider";
								//disable onclick close
								$(liRemoveFilterDivider).bind("click", function (e) {
									e.preventDefault();
									e.stopPropagation();
								});
								ulDropDown.appendChild(liRemoveFilterDivider);
							}
							//remove filter LI - End

							if (this.control.FixedFilters.trim() != '') {
								var liFixedFilters = document.createElement('li');
								ulDropDown.appendChild(liFixedFilters);
								this.LoadFixedFilters(liFixedFilters);

								var liFixedFiltersDivider = document.createElement('li');
								liFixedFiltersDivider.className = "divider";
								//disable onclick close
								$(liFixedFiltersDivider).bind("click", function (e) {
									e.preventDefault();
									e.stopPropagation();
								});
								ulDropDown.appendChild(liFixedFiltersDivider);
							}

							if (this.control.AllowMultipleSelection) {
								this.SelectedValue_getAUX = this.control.SelectedValue_get != null && this.control.SelectedValue_get != '' ? JSON.parse(this.control.SelectedValue_get) : [];
								this.SelectedText_getAUX = this.control.SelectedText_get != null && this.control.SelectedText_get != '' ? JSON.parse(this.control.SelectedText_get) : [];
							}

							//input search LI - Start
							if (this.control.IncludeFilter) {
								var liFilterInput = document.createElement('li');

								var divInput = document.createElement('div');
								divInput.setAttribute("style", "display: block; margin-right:10px;margin-left:10px;padding-bottom:10px");
								var searchAsButton = true;

								var searchElementId = "search_" + this.control.ControlName;
								if (this.IsDate()) {
									var dDate = document.createElement('div');
									dDate.setAttribute("id", this.control.ControlName + "DateDiv");
									dDate.setAttribute("class", "ColumnSettingsRangeContainerDiv");
									if (this.control.FilterIsRange) {
										var divDateFrom = document.createElement('div');
										divDateFrom.setAttribute("class", "ColumnSettingsRangeContainer");
										var spanFrom = document.createElement('span');
										spanFrom.setAttribute("class", "ColumnSettingsRangeContainerSpan");
										spanFrom.innerHTML = gx.getMessage(this.control.RangeFilterFrom);
										divInput.appendChild(divDateFrom);
										divDateFrom.appendChild(spanFrom);
										divDateFrom.appendChild(dDate);
										$(ulDropDown).css("min-width", "195px");
									}
									else {
										divInput.appendChild(dDate);
									}
									if (this.control.FilterIsRange) {
										var divDateTo = document.createElement('div');
										divDateTo.setAttribute("class", "ColumnSettingsRangeContainer");
										var spanTo = document.createElement('span');
										spanTo.setAttribute("class", "ColumnSettingsRangeContainerSpan");
										spanTo.innerHTML = gx.getMessage(this.control.RangeFilterTo);
										divInput.appendChild(divDateTo);
										divDateTo.appendChild(spanTo);

										var dDateTo = document.createElement('div');
										dDateTo.setAttribute("id", this.control.ControlName + "DateDivTo");
										dDateTo.setAttribute("class", "ColumnSettingsRangeContainerDiv");
										divDateTo.appendChild(dDateTo);
									}
								} else {
									var isRange = false;
									if (this.control.FilterIsRange) {
										var divFrom = document.createElement('div');
										divFrom.setAttribute("class", "ColumnSettingsRangeContainer");
										var spanFrom = document.createElement('span');
										spanFrom.setAttribute("class", "ColumnSettingsRangeContainerSpan");
										spanFrom.innerHTML = gx.getMessage(this.control.RangeFilterFrom);
										divFrom.appendChild(spanFrom);
										divInput.appendChild(divFrom);

										filterInput = document.createElement('input');
										divFrom.appendChild(filterInput);
										$(ulDropDown).css("min-width", "197px");
									} else {
										var divContainer = document.createElement('div');
										divContainer.setAttribute("class", "gx-attribute");
										divContainer.setAttribute("style", "margin-right:10px");
										var divInputGroup = document.createElement('div');
										divInputGroup.setAttribute("class", "input-group");
										divInputGroup.setAttribute("style", "width:123px");
										divContainer.appendChild(divInputGroup);
										divInput.appendChild(divContainer);

										searchAsButton = false;

										filterInput = document.createElement('input');
										divInputGroup.appendChild(filterInput);
									}
									if (this.control.FilterIsRange) {
										var divTo = document.createElement('div');
										divTo.setAttribute("class", "ColumnSettingsRangeContainer");
										var spanTo = document.createElement('span');
										spanTo.setAttribute("class", "ColumnSettingsRangeContainerSpan");
										spanTo.innerHTML = gx.getMessage(this.control.RangeFilterTo);
										divTo.appendChild(spanTo);
										divInput.appendChild(divTo);

										filterInputTo = document.createElement('input');
										divTo.appendChild(filterInputTo);
										this.CreateFilterInput(this.control.FilteredTextTo, liDropDownItem, filterInputTo, filterInput, filterInputTo, true, searchElementId, triggerButtonId);
										isRange = true;
									}
									this.CreateFilterInput(this.control.FilteredText, liDropDownItem, filterInput, filterInput, filterInputTo, isRange, searchElementId, triggerButtonId);
								}

								if (!this.control.IncludeDataList || this.control.DataListType == "Dynamic" && this.control.TitleControlIdToReplace != '') {
									var inputButtonFind = document.createElement('input');
									if (!searchAsButton) {
										var spanSearchContainer = document.createElement('span');
										spanSearchContainer.setAttribute("class", "input-group-btn");
										divInputGroup.appendChild(spanSearchContainer);
										var aSearchContainer = document.createElement('a');
										aSearchContainer.setAttribute("id", searchElementId);
										aSearchContainer.setAttribute("class", "btn btn-default");
										spanSearchContainer.appendChild(aSearchContainer);
										if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
											var imgSearchContainer = document.createElement('i');
											imgSearchContainer.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["app"]);
											aSearchContainer.appendChild(imgSearchContainer);
										}
										else {
											var imgSearchContainer = document.createElement('img');
											imgSearchContainer.setAttribute("src", gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["OptionApplyFilter"]));
											imgSearchContainer.setAttribute("class", "Image");
											aSearchContainer.appendChild(imgSearchContainer);
										}

										$(aSearchContainer).bind("click", function (thisC) {
											return function (e) {
												thisC.SetActiveEventKey('<#Filter#>');
												if (thisC.IsDate()) {
													thisC.DetachAuxDateVariable(thisC.GetDateFilterVariable() + "_dp_container");
													thisC.SetSelectedValue('');
													thisC.SetSelectedText('');
													thisC.SetFilteredText($('#' + thisC.GetDateFilterVariable()).val());
													if (thisC.control.FilterIsRange) {
														thisC.DetachAuxDateVariable(thisC.GetDateFilterVariableTo() + "_dp_container");
														thisC.SetFilteredTextTo($('#' + thisC.GetDateFilterVariableTo()).val());
													}
												} else {
													var emptyVal = '';
													if (thisC.control.AllowMultipleSelection) {
														emptyVal = '[]';
													}
													thisC.SetSelectedValue(emptyVal);
													thisC.SetSelectedText(emptyVal);
													thisC.SetFilteredText($(filterInput).val());
													if (thisC.control.FilterIsRange) {
														thisC.DetachAuxDateVariable(thisC.GetDateFilterVariableTo() + "_dp_container");
														thisC.SetFilteredTextTo($(filterInputTo).val());
													}
												}
											}
										}(this));
										if (this.control.OnOptionClicked)
											$(aSearchContainer).bind("click", this.control.OnOptionClicked);
									}
									else {
										inputButtonFind.setAttribute("id", searchElementId);
										inputButtonFind.setAttribute("type", "button");
										inputButtonFind.setAttribute("value", gx.getMessage(this.control.SearchButtonText));
										inputButtonFind.setAttribute("class", "btn btn-default Button");

										$(inputButtonFind).bind("click", function (thisC) {
											return function (e) {
												thisC.SetActiveEventKey('<#Filter#>');
												if (thisC.IsDate()) {
													thisC.DetachAuxDateVariable(thisC.GetDateFilterVariable() + "_dp_container");
													thisC.SetSelectedValue('');
													thisC.SetSelectedText('');
													thisC.SetFilteredText($('#' + thisC.GetDateFilterVariable()).val());
													if (thisC.control.FilterIsRange) {
														thisC.DetachAuxDateVariable(thisC.GetDateFilterVariableTo() + "_dp_container");
														thisC.SetFilteredTextTo($('#' + thisC.GetDateFilterVariableTo()).val());
													}
												} else {
													if (thisC.control.AllowMultipleSelection) {
														thisC.SetSelectedValue(JSON.stringify(thisC.SelectedValue_getAUX, null, 2));
														thisC.SetSelectedText(JSON.stringify(thisC.SelectedText_getAUX, null, 2));
													} else {
														thisC.SetSelectedValue('');
														thisC.SetSelectedText('');
														thisC.SetFilteredText($(filterInput).val());
														if (thisC.control.FilterIsRange) {
															thisC.DetachAuxDateVariable(thisC.GetDateFilterVariableTo() + "_dp_container");
															thisC.SetFilteredTextTo($(filterInputTo).val());
														}
													}
												}
											}
										}(this));
										if (this.control.OnOptionClicked)
											$(inputButtonFind).bind("click", this.control.OnOptionClicked);
										if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
											inputButtonFind.setAttribute("data-toggle", "dropdown");
										}
										divInput.appendChild(inputButtonFind);
									}
								}

								liFilterInput.appendChild(divInput);
								ulDropDown.appendChild(liFilterInput);
							}
							//input search LI - End

							//List Fixed Values - Start
							if (this.control.IncludeDataList && this.control.DataListType == "FixedValues") {
								this.LoadListFixedValues(liDropDownItem, false);
							}
							//List Fixed Values - End

							var liLisItem = liDropDownItem;
							if (this.control.AllowMultipleSelection) {
								//input search checked LI - Start
								var inputButtonFind = document.createElement('input');
								inputButtonFind.setAttribute("type", "button");
								inputButtonFind.setAttribute("value", gx.getMessage(this.control.SearchButtonText));
								inputButtonFind.setAttribute("class", "btn btn-default Button");

								$(inputButtonFind).bind("click", function (thisC) {
									return function (e) {
										thisC.SetActiveEventKey('<#Filter#>');
										thisC.SetSelectedValue(JSON.stringify(thisC.SelectedValue_getAUX, null, 2));
										thisC.SetSelectedText(JSON.stringify(thisC.SelectedText_getAUX, null, 2));
										thisC.SetFilteredText('');
										$("#" + thisC.containerToDrawName).removeClass('open');
									}
								}(this));
								if (this.control.OnOptionClicked)
									$(inputButtonFind).bind("click", this.control.OnOptionClicked);

								var liFindSelected = document.createElement('li');
								liFindSelected.setAttribute("class", "LiFilterSelected");
								liFindSelected.appendChild(inputButtonFind);

								ulDropDown.appendChild(liDropDownItem);
								liDropDownItem = null;

								ulDropDown.appendChild(liFindSelected);
								//input search checked LI - End
							}
							if (ulDropDown.children.length > 0 && ulDropDown.children[ulDropDown.children.length - 1].className == 'divider') {
								ulDropDown.removeChild(ulDropDown.children[ulDropDown.children.length - 1]);
							}

							$(triggerButton).bind("click", function (thisC) {
								return function (e) {
									if ($("#" + thisC.containerToDrawName).attr('class').indexOf(' open') == -1) {
										var inputVal;
										var inputValTo;
										if (thisC.control.AllowMultipleSelection) {
											thisC.SelectedValue_getAUX = thisC.control.SelectedValue_get != null && thisC.control.SelectedValue_get != '' ? JSON.parse(thisC.control.SelectedValue_get) : [];
											thisC.SelectedText_getAUX = thisC.control.SelectedText_get != null && thisC.control.SelectedText_get != '' ? JSON.parse(thisC.control.SelectedText_get) : [];
											try {
												//specific code for dynamic fitler
												if ($("#" + thisC.containerToDrawName).parent().next().children()[0].childNodes[1].className == "bootstrap-tagsinput") {
													var tagsInput = $("#" + thisC.containerToDrawName).parent().next().children()[0].childNodes[1];
													if ($(tagsInput).css("min-width") == "0px") {
														$(tagsInput).css("min-width", "150px");
														$("#" + thisC.containerToDrawName).off('hide.bs.dropdown').on('hide.bs.dropdown', function (thisC) {
															return function (e) {
																$(tagsInput).css("min-width", "0px");
																return true;
															}
														}(thisC));
													}

												}
											} catch (e) { }
										}
										if (thisC.control.IncludeFilter) {
											if (thisC.IsDate()) {
												$("#" + thisC.containerToDrawName).off('hide.bs.dropdown').on('hide.bs.dropdown', function (thisC) {
													return function (e) {
														return thisC.OnDDOClosing();
													}
												}(thisC));
												if (thisC.control.FilteredText == "") {
													var inputValDate = $('#' + thisC.GetDateFilterVariable()).get(0);
													thisC.control.FilteredText = $(inputValDate).val();
													for (var dig = 0; dig < 10; dig += 1) {
														thisC.control.FilteredText = WWP_replaceAll(thisC.control.FilteredText, dig.toString(), ' ');
													}
												}
												var auxDate = thisC.AttachAuxDateVariable(liRemoveFilter, thisC.GetDateFilterVariable(), thisC.control.ControlName + "DateDiv", thisC.control.FilteredText, searchElementId, triggerButtonId);
												inputVal = auxDate;
												inputValTo = null;
												if (thisC.control.FilterIsRange) {
													if (thisC.control.FilteredTextTo == "") {
														var inputValDate = $('#' + thisC.GetDateFilterVariable()).get(0);
														thisC.control.FilteredTextTo = $(inputValDate).val();
														for (var dig = 0; dig < 10; dig += 1) {
															thisC.control.FilteredTextTo = WWP_replaceAll(thisC.control.FilteredTextTo, dig.toString(), ' ');
														}
													}
													auxDate = thisC.AttachAuxDateVariable(liRemoveFilter, thisC.GetDateFilterVariableTo(), thisC.control.ControlName + "DateDivTo", thisC.control.FilteredTextTo, searchElementId, triggerButtonId);
													inputValTo = auxDate;
												}
											} else {
												inputVal = filterInput;
												inputValTo = null;
												if (thisC.control.IncludeDataList && thisC.control.DataListType == "FixedValues") {
													$(filterInput).val('');
													if (thisC.control.AllowMultipleSelection) {
														thisC.LoadListFixedValues(liLisItem, true);
													} else {
														thisC.updateFixedValuesList2(liLisItem, '');
													}
												} else {
													var filterdText = thisC.control.FilteredText.trim();
													if (thisC.control.DataListType == "Dynamic (with fixedValues)") {
														$(filterInput).val('');
													} else {
														$(filterInput).val(filterdText);
													}
												}
												if (thisC.control.FilterIsRange) {
													$(filterInputTo).val(thisC.control.FilteredTextTo.trim());
													inputValTo = filterInputTo;
												}
											}
										} else if (thisC.reloadListFixedValuesOnDDOOpen || thisC.includeFirstSelectedItem) {
											if (thisC.control.AllowMultipleSelection) {
												thisC.LoadListFixedValues(liLisItem, true);
											} else {
												thisC.updateFixedValuesList2(liLisItem, '');
											}
										}
										if (thisC.control.IncludeDataList && thisC.control.DataListType.indexOf("Dynamic") == 0) {
											thisC.updateFilterList(liLisItem, inputVal, inputValTo, false);
										}
									}
								}
							}(this));
						} else {
							var aDropDownItem = document.createElement('a');
							var spanDropDownItem = document.createElement('span');
							$(spanDropDownItem).text(item["Title"]);
							if (this.control.UseAsGridTitleSetting && ((this.control.IncludeSortASC && item["EventKey"] == "<#OrderASC#>" && (this.control.SortedStatus == "ASC" || this.control.SortedStatus == "ASC GRP"))
																	|| (this.control.IncludeSortASC && item["EventKey"] == "<#Group#>" && (this.control.SortedStatus == "ASC GRP" || this.control.SortedStatus == "DSC GRP"))
																	|| (this.control.IncludeSortDSC && item["EventKey"] == "<#OrderDSC#>" && (this.control.SortedStatus == "DSC" || this.control.SortedStatus == "DSC GRP")))) {
								spanDropDownItem.innerHTML = '<strong>' + spanDropDownItem.innerHTML + '</strong>';
							}

							var dropDownOptions = this;

							//set onclick event
							var aJSonclickEvent = item["JSonclickEvent"];
							if (aJSonclickEvent.length > 0) {
								$(aDropDownItem).bind("click", function (oc) {
									return function (e) {
										var result = eval(oc);
										if (!result) {
											e.stopImmediatePropagation();
											$("#" + dropDownOptions.containerToDrawName).removeClass('open');
										}
									}
								}(aJSonclickEvent));
							}

							//set link url
							if (item["Link"].length > 0) {
								aDropDownItem.setAttribute("href", item["Link"]);
							}
								//or set event key and onclick handler
							else {
								aDropDownItem.setAttribute("href", DDO_GetLocalURL());
								//to keep URLs
								if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
									aDropDownItem.setAttribute("data-toggle", "dropdown");
								}
								var eventKey = item["EventKey"];
								$(aDropDownItem).bind("click", function (ek) {
									return function () {
										if (dropDownOptions.control.IncludeDataList && dropDownOptions.ajaxCall != null) {
											dropDownOptions.ajaxCall.abort();
										}
										if (dropDownOptions.control.IncludeDataList && dropDownOptions.ajaxCall2 != null) {
											dropDownOptions.ajaxCall2.abort();
										}
										if (dropDownOptions.control.IsM) {
											if (ek == "<#OrderASC#>" || ek == "<#OrderDSC#>" || ek == "<#Group#>") {
												dropDownOptions.control.MControl.SelectedValue_get = dropDownOptions.control.MSortValue;
												if (ek == "<#Group#>") {
													dropDownOptions.control.MControl.SelectedText_get = dropDownOptions.control.MName;
												}
											}
										}
										dropDownOptions.SetActiveEventKey(ek);
										if (dropDownOptions.control.OnOptionClicked
											&& dropDownOptions.control.IncludeFilter
											&& dropDownOptions.IsDate()) {
											dropDownOptions.DetachAuxDateVariable(dropDownOptions.GetDateFilterVariable() + "_dp_container");
											if (dropDownOptions.control.FilterIsRange) {
												dropDownOptions.DetachAuxDateVariable(dropDownOptions.GetDateFilterVariableTo() + "_dp_container");
											}
										}
									}
								}(eventKey));
								if (this.control.OnOptionClicked)
									$(aDropDownItem).bind("click", this.control.OnOptionClicked);
							}

							if (item["Tooltip"].length > 0)
								aDropDownItem.setAttribute("title", item["Tooltip"]);

							if (images) {
								aDropDownItem.className = "image";
								if (item["Icon"] != null && item["Icon"].length > 0) {
									var imgDropDownItem = document.createElement('img');
									imgDropDownItem.setAttribute("src", item["Icon"]);
									aDropDownItem.appendChild(imgDropDownItem);
								}
								else if (item["FontIcon"] != null && item["FontIcon"].length > 0) {
									var imgDropDownItem = document.createElement('i');
									imgDropDownItem.className = item["FontIcon"];
									aDropDownItem.appendChild(imgDropDownItem);
								}
								else {
									spanDropDownItem.className = "no-image";
								}
							}

							if (item["ThemeClass"] != null && item["ThemeClass"].length > 0) {
								liDropDownItem.className = item["ThemeClass"];
							}

							aDropDownItem.appendChild(spanDropDownItem);
							liDropDownItem.appendChild(aDropDownItem);
						}
					}
					if (liDropDownItem != null) {
						ulDropDown.appendChild(liDropDownItem);
					}
				}
			}

			if (this.control.DropDownOptionsType == "GridColumnsSelector") {
				var liDropDownItem = document.createElement('li');

				var divInput = document.createElement('div');
				divInput.setAttribute("style", "display: block; margin-right:10px;margin-left:10px;padding-bottom:10px");

				var divContainer = document.createElement('div');
				divContainer.setAttribute("class", "gx-attribute");
				divContainer.setAttribute("style", "margin-right:10px");
				var divInputGroup = document.createElement('div');
				divInputGroup.setAttribute("class", "input-group");
				divInputGroup.setAttribute("style", "width:123px");
				divContainer.appendChild(divInputGroup);
				divInput.appendChild(divContainer);

				searchAsButton = false;

				filterInput = document.createElement('input');
				divInputGroup.appendChild(filterInput);
				liDropDownItem.appendChild(divInput);
				ulDropDown.appendChild(liDropDownItem);

				liDropDownItem = document.createElement('li');

				this.CreateFilterInput(this.control.FilteredText, liDropDownItem, filterInput, filterInput, filterInputTo, isRange, searchElementId, triggerButtonId);
				var divFixedValues = document.createElement('div');
				divFixedValues.className = "FilterOptions FilterOptionsMultiSelection";
				var ulColumns = document.createElement('ul');

				this.loadColumnsSelectorValues(ulColumns);

				divFixedValues.appendChild(ulColumns);

				liDropDownItem.appendChild(divFixedValues);
				ulDropDown.appendChild(liDropDownItem);

				liDropDownItem = document.createElement('li');
				var inputButtonFind = document.createElement('input');
				inputButtonFind.setAttribute("type", "button");
				inputButtonFind.setAttribute("value", gx.getMessage(this.control.UpdateButtonText));
				inputButtonFind.setAttribute("class", "btn btn-default Button");

				$(inputButtonFind).bind("click", function (thisC) {
					return function (e) {
						thisC.control.ColumnsSelectorValues = JSON.stringify(thisC.ColumnsSelectorData_Aux, null, 2);
						thisC.control.DropDownOptionsData = thisC.ColumnsSelectorData_Aux;
					}
				}(this));
				if (this.control.OnColumnsChanged)
					$(inputButtonFind).bind("click", this.control.OnColumnsChanged);

				var liFindSelected = document.createElement('li');
				liFindSelected.setAttribute("class", "LiFilterSelected");
				liFindSelected.appendChild(inputButtonFind);

				ulDropDown.appendChild(liDropDownItem);
				liDropDownItem = null;

				ulDropDown.appendChild(liFindSelected);


				if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
					liFindSelected.setAttribute("data-toggle", "dropdown");
				}


				$(triggerButton).bind("click", function (thisC) {
					return function (e) {
						thisC.ColumnsSelectorData_Aux = JSON.parse(JSON.stringify(thisC.control.DropDownOptionsData, null, 2));
						if (thisC.ColumnsSelectorData_Aux.VisibleColumns != undefined || thisC.ColumnsSelectorData_Aux.InvisibleColumns != undefined) {
							if (thisC.ColumnsSelectorData_Aux.VisibleColumns == undefined) {
								thisC.ColumnsSelectorData_Aux.VisibleColumns = [];
							}
							if (thisC.ColumnsSelectorData_Aux.InvisibleColumns == undefined) {
								thisC.ColumnsSelectorData_Aux.InvisibleColumns = [];
							}
						}
						$(filterInput).val('');
						thisC.loadColumnsSelectorValues(ulColumns);
					}
				}(this));
			}
			if (triggerButtonFontIcon != null) {
				var triggerButtonImage = document.createElement('i');
				triggerButtonImage.setAttribute("class", triggerButtonFontIcon);
				triggerButton.appendChild(triggerButtonImage);
			}
			else if (triggerButtonIcon != null) {
				var triggerButtonImage = document.createElement('img');
				triggerButtonImage.setAttribute("src", triggerButtonIcon);
				triggerButton.appendChild(triggerButtonImage);
			}
			else if (this.control.Icon.length > 0) {
				var triggerButtonImage = document.createElement(this.control.IconType == 'Image' ? 'img' : 'i');
				triggerButtonImage.setAttribute(this.control.IconType == 'Image' ? "src" : "class", this.control.Icon);
				triggerButton.appendChild(triggerButtonImage);
			}
			if (this.control.Caption.length > 0 || this.control.DropDownOptionsType == "ExtendedComboBox") {
				var triggerButtonSpan = document.createElement('span');
				if (this.control.DropDownOptionsType == "ExtendedComboBox") {
					this.m_triggerButtonSpan = triggerButtonSpan;
					this.updatedExtendedComboCaption(null);
				}
				else {
					triggerButtonSpan.innerText = this.control.Caption;
				}
				triggerButton.appendChild(triggerButtonSpan);
			}

			var caretTriggerButton = document.createElement('span');
			caretTriggerButton.className = "caret";
			triggerButton.appendChild(caretTriggerButton);

			$("#" + this.containerToDrawName).append(triggerButton);
			$("#" + this.containerToDrawName).append(ulDropDown);

		}

		this.ShowHideControl();
	}

	this.loadExtendedComboSelectedText = function (data, selValues, loadingRoot) {
		if (this.control.DataListType == 'FixedValues') {
			data = this.ExtendedCombo_getFixedValues();
		}
		if (selValues == null && this.control.AllowMultipleSelection) {
			selValues = JSON.parse(this.control.SelectedValue);
			if (selValues.length == 0) {
				return false;
			}
			this.m_SelectedTextJson = [];
			for (var index2 = 0; index2 < selValues.length; index2 += 1) {
				this.m_SelectedTextJson.push(selValues[index2]);
			}
		}
		for (var index = 0; data != null && index < data.length; index += 1) {
			var option = data[index];

			if (option.C != null && option.C.length > 0) {
				if (this.loadExtendedComboSelectedText(option.C, selValues, false) && !this.control.AllowMultipleSelection) {
					return true;
				}
			}
			else {
				var isSelected = false;
				var selItemIndex = -1;
				if (this.control.AllowMultipleSelection) {
					for (var index2 = 0; index2 < selValues.length; index2 += 1) {
						if (option.ID.trim() == (selValues[index2] + '').trim()) {
							isSelected = true;
							selItemIndex = index2;
							break;
						}
					}
				} else if (option.ID.trim() == this.control.SelectedValue.trim()) {
					isSelected = true;
				}

				if (isSelected) {
					var optionText = option.T;
					optionText = gx.getMessage(optionText);
					if (this.control.AllowMultipleSelection) {
						this.m_SelectedTextJson.splice(selItemIndex, 1, optionText);
					}
					else {
						this.SetSelectedText(optionText);
						return true;
					}
				}
			}
		}
		if (loadingRoot) {
			if (this.control.AllowMultipleSelection) {
				this.SetSelectedText(JSON.stringify(this.m_SelectedTextJson, null, 2));
				this.m_SelectedTextJson = null;
			}
			else if (this.control.SelectedText == '') {
				this.SetSelectedText(this.control.SelectedValue);
			}
		}
		return false;
	}

	this.loadExtendedComboBox = function (ulDropDown) {
		var liDropDownItem = document.createElement('li');

		var divInput = document.createElement('div');
		divInput.setAttribute("style", "display: block; margin-right:10px;margin-left:10px;padding-bottom:10px");

		var divContainer = document.createElement('div');
		divContainer.setAttribute("class", "gx-attribute");
		divContainer.setAttribute("style", "margin-right:10px");
		var divInputGroup = document.createElement('div');
		divInputGroup.setAttribute("class", "input-group");
		divInputGroup.setAttribute("style", "width:100%");
		divContainer.appendChild(divInputGroup);
		divInput.appendChild(divContainer);

		var filterInput = document.createElement('input');
		this.m_filterInput = filterInput;
		divInputGroup.appendChild(filterInput);
		liDropDownItem.appendChild(divInput);
		ulDropDown.appendChild(liDropDownItem);

		var ulVals = document.createElement('ul');

		liDropDownItem = document.createElement('li');

		this.CreateFilterInput(this.control.FilteredText, liDropDownItem, filterInput, null, null, false, null, null);

		this.ExtendedComboBox_FilterSelected = false;
		if (this.control.IncludeOnlySelectedOption) {
			var liOnlySelectedItem = document.createElement('li');
			liOnlySelectedItem.setAttribute('class', 'OnlySelected');
			var aRemoveFilter = document.createElement('a');
			var spanRemoveFilter = document.createElement('span');
			var onlySelectedValues = gx.getMessage(this.control.OnlySelectedValues);
			if (onlySelectedValues.indexOf('<SELECTED_COUNT>') != -1) {
				this.OnlySelectedItem_span = spanRemoveFilter;
				onlySelectedValues = WWP_replaceAll(onlySelectedValues, '<SELECTED_COUNT>', '0');
			}
			spanRemoveFilter.innerHTML = onlySelectedValues;

			aRemoveFilter.className = "image";
			aRemoveFilter.setAttribute("style", "margin-bottom:10px");

			var imgRemoveFilter;
			if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
				imgRemoveFilter = document.createElement('i');
				imgRemoveFilter.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["mul"]);
				aRemoveFilter.appendChild(imgRemoveFilter);
			}
			else {
				imgRemoveFilter = document.createElement('img');
				imgRemoveFilter.setAttribute("src", gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["MultiselOption"]));
				aRemoveFilter.appendChild(imgRemoveFilter);
			}

			$(aRemoveFilter).bind("click", function (thisC, imgRemoveFilter) {
				return function (e) {
					var filterSelected = (this.getAttribute('sel') != 'T');
					if (filterSelected) {
						this.setAttribute('sel', 'T');
					} else {
						this.removeAttribute('sel');
					}
					if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						imgRemoveFilter.setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons[filterSelected ? "muls" : "mul"]);
					}
					else {
						imgRemoveFilter.setAttribute("src", gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons[filterSelected ? "MultiselSelOption" : "MultiselOption"]));
					}
					thisC.ExtendedComboBox_FilterSelected = filterSelected;
					thisC.updateExtendedComboList(ulVals, $(filterInput).val());
				}
			}(this, imgRemoveFilter));
			$(aRemoveFilter).bind("click", function (e) {
				e.preventDefault();
				e.stopPropagation();
			});

			aRemoveFilter.appendChild(spanRemoveFilter);
			liOnlySelectedItem.appendChild(aRemoveFilter);
			ulDropDown.appendChild(liOnlySelectedItem);
		}

		var divFixedValues = document.createElement('div');
		if (this.control.AllowMultipleSelection) {
			divFixedValues.className = "FilterOptions FilterOptionsMultiSelection";
		}
		else {
			divFixedValues.className = "FilterOptions";
		}

		this.loadExtendedComboBoxItems(ulVals, this.control.DropDownOptionsData, 0, null, false);

		divFixedValues.appendChild(ulVals);

		liDropDownItem.appendChild(divFixedValues);
		ulDropDown.appendChild(liDropDownItem);

		if (this.control.DataListProc != null && this.control.DataListProc != '') {
			liDropDownItem = document.createElement('li');
			this.addLoadingOption(liDropDownItem);
			ulDropDown.appendChild(liDropDownItem);
			this.m_loadingLi = liDropDownItem;
			$(this.m_loadingLi).css("display", "none");
		}

		this.ExtendedComboBox_SelectAll = null;
		this.ExtendedComboBox_SelectAll_A = null;
		this.extendedComboBox_UL = ulVals;
		if (this.control.IncludeSelectAllOption) {
			liDropDownItem = document.createElement('li');
			liDropDownItem.setAttribute('class', 'SelectAll');
			this.ExtendedComboBox_SelectAll_A = document.createElement('a');
			var spanRemoveFilter = document.createElement('span');
			spanRemoveFilter.innerHTML = gx.getMessage(this.control.SelectAllText);

			this.ExtendedComboBox_SelectAll_A.className = "image";
			this.ExtendedComboBox_SelectAll_A.setAttribute("style", "margin-bottom:10px");

			var imgRemoveFilter;
			if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
				imgRemoveFilter = document.createElement('i');
				imgRemoveFilter.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["mul"]);
			} else {
				imgRemoveFilter = document.createElement('img');
				imgRemoveFilter.setAttribute("src", gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["MultiselOption"]));
			}
			this.ExtendedComboBox_SelectAll_A.appendChild(imgRemoveFilter);

			$(this.ExtendedComboBox_SelectAll_A).bind("click", function (thisC, imgRemoveFilter) {
				return function (e) {
					var selectAll = (this.getAttribute('sel') != 'T');
					if (selectAll) {
						this.setAttribute('sel', 'T');
					} else {
						this.removeAttribute('sel');
					}
					if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						imgRemoveFilter.setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons[selectAll ? "muls" : "mul"]);
					}
					else {
						imgRemoveFilter.setAttribute("src", gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons[selectAll ? "MultiselSelOption" : "MultiselOption"]));
					}
					thisC.ExtendedComboBox_SelectAll_A = null;
					this.extendedComboBox_DisableParentsUpdating = true;
					for (var index = 0; index < ulVals.childNodes.length; index += 1) {
						var li = ulVals.childNodes[index];
						var aLiFilters = li.childNodes[0];
						if (($(li).css("display") != "none" || thisC.extendedComboBox_IsOptionVisibleAndCollapsed(li))) {
							if (!$(aLiFilters).hasClass("ExtendedComboLevelWithChildren")) //is leaf
							{
								if (selectAll && aLiFilters.getAttribute('sel') != 'T'
									|| !selectAll && aLiFilters.getAttribute('sel') == 'T') {
									aLiFilters.click();
								}
							}
							else {
								if (aLiFilters.getAttribute('expanded') != 'true') {
									aLiFilters.setAttribute('expanded', 'true');
									if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
										var iSel = $(aLiFilters).find('i');
										iSel[iSel.length - 1].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["tcol"]);
									}
									else {
										$(aLiFilters.childNodes[aLiFilters.childNodes.length - 1]).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["TreeviewCollapse"]) + ")");
									}
								}
							}
							if ($(li).css("display") == "none") {
								$(li).css("display", '');
							}
						}
					}
					this.extendedComboBox_DisableParentsUpdating = false;
					thisC.ExtendedComboBox_SelectAll_A = this;
				}
			}(this, imgRemoveFilter));
			$(this.ExtendedComboBox_SelectAll_A).bind("click", function (e) {
				e.preventDefault();
				e.stopPropagation();
			});

			this.ExtendedComboBox_SelectAll_A.appendChild(spanRemoveFilter);
			liDropDownItem.appendChild(this.ExtendedComboBox_SelectAll_A);
			ulDropDown.appendChild(liDropDownItem);
		}

		return ulVals;
	}

	this.extendedComboBox_UpdateParentsSelection = function (li) {
		if (!this.extendedComboBox_DisableParentsUpdating == true) {
			var isSelected = li.childNodes[0].getAttribute('sel') == 'T';
			var liLevel = parseInt(li.childNodes[0].getAttribute('level'));
			var parentLi = li;
			while ((parentLi = parentLi.previousSibling) != null && parseInt(parentLi.childNodes[0].getAttribute('level')) > liLevel - 1);
			if (parentLi != null) {
				if (isSelected) {
					//check if every child is selected
					var everyChildSelected = true;
					var childLi = parentLi;
					while ((childLi = childLi.nextSibling) != null && parseInt(childLi.childNodes[0].getAttribute('level')) >= liLevel) {
						if (childLi.childNodes[0].getAttribute('sel') != 'T' //is not selected
							&& parseInt(childLi.childNodes[0].getAttribute('level')) == liLevel //is same level
							&& ($(childLi).css("display") != "none" || this.extendedComboBox_IsOptionVisibleAndCollapsed(childLi))) {
							everyChildSelected = false;
							break;
						}
					}

					if (everyChildSelected && parentLi.childNodes[0].getAttribute('sel') != 'T'
						|| !everyChildSelected && parentLi.childNodes[0].getAttribute('sel') == 'T') {
						parentLi.childNodes[0].setAttribute('sel', everyChildSelected ? 'T' : '');
						if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
							$(parentLi.childNodes[0]).find('i')[0].setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons[everyChildSelected ? "muls" : "mul"]);
						}
						else {
							$(parentLi.childNodes[0].childNodes[0]).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons[everyChildSelected ? "MultiselSelOption" : "MultiselOption"]) + ")");
						}
						this.extendedComboBox_UpdateParentsSelection(parentLi);
					}
				}
				else {
					if (parentLi.childNodes[0].getAttribute('sel') == 'T') {
						parentLi.childNodes[0].removeAttribute('sel');
						if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
							$(parentLi.childNodes[0]).find('i')[0].setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["mul"]);
						}
						else {
							$(parentLi.childNodes[0].childNodes[0]).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["MultiselOption"]) + ")");
						}
						this.extendedComboBox_UpdateParentsSelection(parentLi);
					}
				}
			}
		}
	}

	this.extendedComboBox_IsOptionVisibleAndCollapsed = function (li) {
		if ($(this.m_filterInput).val() == '' && !this.ExtendedComboBox_FilterSelected) {
			//not filtering
			return true;
		}

		if (this.control.DataListProc != null && this.control.DataListProc != '' && !this.ExtendedComboBox_FilterSelected) {
			//has rest proc and not filtering selected
			return true;
		}

		var aLiFilters = li.childNodes[0];
		if (aLiFilters.getAttribute('filtered') == 'true') {
			return true;
		}

		if (aLiFilters.getAttribute('level') != '0') {
			var level = parseInt(aLiFilters.getAttribute('level')) - 1;
			while ((li = li.previousSibling) != null) {
				var aLiFilters2 = li.childNodes[0];
				if (parseInt(aLiFilters2.getAttribute('level')) == level) {
					if (aLiFilters2.getAttribute('filteredChildren') == 'true') {
						return false;
					}
					else {
						if (aLiFilters2.getAttribute('filtered') == 'true') {
							return true;
						}
					}
					level--;
				}
			}
		}
		return false;
	}

	this.extendedComboBox_SelectSelectAll_A = function (sel) {
		if (this.ExtendedComboBox_SelectAll_A != null) {
			if (sel) {
				if (this.ExtendedComboBox_SelectAll_A.getAttribute('sel') != 'T') {
					this.ExtendedComboBox_SelectAll_A.setAttribute('sel', 'T');
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						this.ExtendedComboBox_SelectAll_A.childNodes[0].setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["muls"]);
					}
					else {
						this.ExtendedComboBox_SelectAll_A.childNodes[0].setAttribute("src", gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["MultiselSelOption"]));
					}
				}
			}
			else {
				if (this.ExtendedComboBox_SelectAll_A.getAttribute('sel') == 'T') {
					this.ExtendedComboBox_SelectAll_A.removeAttribute('sel');
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						this.ExtendedComboBox_SelectAll_A.childNodes[0].setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["mul"]);
					}
					else {
						this.ExtendedComboBox_SelectAll_A.childNodes[0].setAttribute("src", gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["MultiselOption"]));
					}
				}
			}
		}
	}

	this.extendedComboBox_UpdateSelectAll_A = function (li) {
		if (this.ExtendedComboBox_SelectAll_A != null) {
			if (li != null && li.childNodes[0].getAttribute('sel') != 'T') {
				this.extendedComboBox_SelectSelectAll_A(false);
			}
			else {
				var oneVisible = false;
				for (var index = 0; index < this.extendedComboBox_UL.childNodes.length; index += 1) {
					li = this.extendedComboBox_UL.childNodes[index];
					if (li.childNodes[0].getAttribute('sel') != 'T'
						&& !$(li.childNodes[0]).hasClass("ExtendedComboLevelWithChildren") //is leaf
						&& ($(li).css("display") != "none" || this.extendedComboBox_IsOptionVisibleAndCollapsed(li))) {
						this.extendedComboBox_SelectSelectAll_A(false);
						return;
					}
					else if (!oneVisible && ($(li).css("display") != "none" || this.extendedComboBox_IsOptionVisibleAndCollapsed(li))) {
						oneVisible = true;
					}
				}
				this.extendedComboBox_SelectSelectAll_A(oneVisible);
			}
		}
	}

	this.extendedComboBox_UpdateNodeWithChildrenSelection = function () {
		if (this.extendedComboBox_UL != null) {
			for (var index = 0; index < this.extendedComboBox_UL.childNodes.length; index += 1) {
				li = this.extendedComboBox_UL.childNodes[index];
				if (!$(li.childNodes[0]).hasClass("ExtendedComboLevelWithChildren") //is leaf
					&& ($(li).css("display") != "none" || this.extendedComboBox_IsOptionVisibleAndCollapsed(li))) {
					this.extendedComboBox_UpdateParentsSelection(li);
				}
			}
		}
	}

	this.ExtendedCombo_getFixedValues = function () {
		var data = [];
		var optionsText = this.control.DataListFixedValues;
		optionsText = WWP_replaceAll(optionsText, '\\\\', '[\\\\]');
		optionsText = WWP_replaceAll(optionsText, '\\,', '[\\\\\\]');
		var optionsTextSplitted = optionsText.split(',');

		for (var index2 = 0; index2 < optionsTextSplitted.length; index2 += 1) {
			var option = optionsTextSplitted[index2];
			option = WWP_replaceAll(option, '[\\\\\\]', ',');
			option = WWP_replaceAll(option, '\\:', '[\\\\\\]');
			var optionTextSplitted = option.split(':');
			var optionText = optionTextSplitted[0];
			optionText = WWP_replaceAll(optionText, '[\\\\\\]', ':');
			optionText = WWP_replaceAll(optionText, '[\\\\]', '\\');
			var optionVal;
			if (optionTextSplitted.length > 1) {
				optionVal = optionTextSplitted[1];
				optionVal = WWP_replaceAll(optionVal, '[\\\\\\]', ':');
				optionVal = WWP_replaceAll(optionVal, '[\\\\]', '\\');
			} else {
				optionText = optionText;
			}
			data.push({ ID: optionVal, T: optionText });
		}
		return data;
	}

	this.loadExtendedComboBoxItems = function (ulVals, data, ind, selValues, showNoResults) {
		if (selValues == null && this.control.AllowMultipleSelection) {
			selValues = JSON.parse(this.control.SelectedValue);
		}

		if (this.control.DataListType == 'FixedValues') {
			data = this.ExtendedCombo_getFixedValues();
		}

		if (ind == 0 && showNoResults && (data == null || data.length == 0) && !this.control.IncludeAddNewOption) {
			var liFilters = document.createElement('li');
			var aLiFilters = document.createElement('a');
			var spanLiFilters = document.createElement('span');
			aLiFilters.setAttribute("href", DDO_GetLocalURL());
			//to keep URLs
			if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
				aLiFilters.setAttribute("data-toggle", "dropdown");
			}
			$(aLiFilters).bind("click", function (e) {
				e.preventDefault();
				e.stopPropagation();
			});
			aLiFilters.appendChild(spanLiFilters);
			liFilters.appendChild(aLiFilters);
			ulVals.appendChild(liFilters);
			spanLiFilters.innerHTML = gx.getMessage(this.control.NoResultsFound);
		}
		else {
			var copyVarToAttVal = false;
			var attName = null;
			var wcName = '';
			if (DV_endsWith(this.control.ContainerName, 'Container') && (this.control.ContainerName.startsWith('COMBO_')
																	|| this.control.ContainerName.startsWith('W0') && this.control.ContainerName.substring(5).startsWith('COMBO_'))) {
				if (this.control.ContainerName.startsWith('COMBO_')) {
					attName = this.control.ContainerName.substring(6, this.control.ContainerName.length - 9);
				} else {
					wcName = this.control.ContainerName.substring(0, 5);
					attName = this.control.ContainerName.substring(11, this.control.ContainerName.length - 9);
				}
				if ($('#' + wcName + 'vCOMBO' + attName).length == 1 && $('#' + wcName + 'vCOMBO' + attName).parent().attr('id') == wcName + 'SECTIONATTRIBUTE_' + attName) {
					copyVarToAttVal = true;
				}
			}
			var dataLength = (data != null ? data.length : 0) + (ind == 0 && this.control.IncludeAddNewOption ? 1 : 0);
			for (var index = ((this.control.AllowMultipleSelection || !this.control.EmptyItem || ind > 0) ? 0 : -1) ; index == -1 || index < dataLength; index += 1) {

				var isAddNewOption = false;
				var option;
				if (index >= 0 && index == (data == null ? 0 : data.length)) {
					isAddNewOption = true;
					option = { ID: '<#NEW#>', T: gx.getMessage(this.control.AddNewOptionText) };
				}
				else {
					option = (index >= 0 ? data[index] : { ID: '', T: gx.getMessage(this.control.EmptyItemText) });
				}
				var optionVal = option.ID.trim();
				var optionText = option.T;
				var liFilters = document.createElement('li');
				var aLiFilters = document.createElement('a');
				var spanLiFilters = document.createElement('span');
				var optionTextOriginal = optionText;
				optionText = gx.getMessage(optionTextOriginal);
				spanLiFilters.setAttribute('dsc', optionText);
				spanLiFilters.setAttribute('class', 'OptionLabel');
				spanLiFilters.innerText = optionText;
				aLiFilters.setAttribute('level', ind);

				if (isAddNewOption) {
					spanLiFilters.setAttribute('add-new-option', '');
				}

				var imgTreeCollapse = null;
				if (option.C != null && option.C.length > 0) {
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						imgTreeCollapse = document.createElement('i');
						imgTreeCollapse.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["tcol"]);
					}
					else {
						$(spanLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["TreeviewCollapse"]) + ")");
					}
					aLiFilters.setAttribute('class', 'ExtendedComboLevel' + ind + ' ExtendedComboLevelWithChildren');
					aLiFilters.setAttribute('expanded', 'true');

					if (this.control.AllowMultipleSelection) {
						var spanSelectChildren = document.createElement('span');
						spanSelectChildren.setAttribute('class', 'SelectAllChildren');
						if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
							var imgSelOption = document.createElement('i');
							imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["mul"]);
							spanSelectChildren.appendChild(imgSelOption);
						}
						else {
							$(spanSelectChildren).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["MultiselOption"]) + ")");
						}
						aLiFilters.appendChild(spanSelectChildren);

						var thisC = this;
						$(spanSelectChildren).bind("click", function (e) {
							return function (e) {
								var isSelected = this.parentNode.getAttribute('sel') != 'T';
								this.parentNode.setAttribute('sel', isSelected ? 'T' : '');
								if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
									$(this).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons[isSelected ? "muls" : "mul"]);
								}
								else {
									$(this).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons[isSelected ? "MultiselSelOption" : "MultiselOption"]) + ")");
								}

								thisC.extendedComboBox_DisableParentsUpdating = true;
								var li = this.parentNode.parentNode;
								while ((li = li.nextSibling) != null && parseInt(li.childNodes[0].getAttribute('level')) > ind) {
									var currentALi = li.childNodes[0];
									if (($(li).css("display") != "none" || thisC.extendedComboBox_IsOptionVisibleAndCollapsed(li))) {
										if (!$(currentALi).hasClass("ExtendedComboLevelWithChildren")) { //is leaf									
											if (isSelected && currentALi.getAttribute('sel') != 'T'
												|| !isSelected && currentALi.getAttribute('sel') == 'T') {
												currentALi.click();
											}
										}
										else {
											if (currentALi.getAttribute('expanded') != 'true') {
												currentALi.setAttribute('expanded', 'true');
												if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
													var iSel = $(currentALi).find('i');
													iSel[iSel.length - 1].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["tcol"]);
												}
												else {
													$(currentALi.childNodes[currentALi.childNodes.length - 1]).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["TreeviewCollapse"]) + ")");
												}
											}
											if (isSelected && currentALi.getAttribute('sel') != 'T'
												|| !isSelected && currentALi.getAttribute('sel') == 'T') {
												currentALi.setAttribute('sel', isSelected ? 'T' : '');
												if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
													$(currentALi).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons[isSelected ? "muls" : "mul"]);
												}
												else {
													$(currentALi.childNodes[0]).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons[isSelected ? "MultiselSelOption" : "MultiselOption"]) + ")");
												}
											}
										}
										if ($(li).css("display") == "none") {
											$(li).css("display", '');
										}
									}
								}

								thisC.extendedComboBox_DisableParentsUpdating = false;
								thisC.extendedComboBox_UpdateParentsSelection(this.parentNode.parentNode);

								if (this.parentNode.getAttribute('expanded') != 'true') {
									this.parentNode.setAttribute('expanded', 'true');
									if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
										var iSel = $(this.parentNode).find('i');
										iSel[iSel.length - 1].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["tcol"]);
									}
									else {
										$(this.parentNode.childNodes[this.parentNode.childNodes.length - 1]).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["TreeviewCollapse"]) + ")");
									}
								}

								e.preventDefault();
								e.stopPropagation();
							}
						}());
					}
				}
				else {
					aLiFilters.setAttribute('class', 'ExtendedComboLevel' + ind);
					var isSelected = false;
					if (this.control.SelectedValue != '') {
						var selVal = this.control.SelectedValue;
						if (this.control.AllowMultipleSelection) {
							for (var index2 = 0; index2 < selValues.length; index2 += 1) {
								if (optionVal == (selValues[index2] + '').trim()) {
									isSelected = true;
									break;
								}
							}
						} else if (optionVal == this.control.SelectedValue.trim()) {
							isSelected = true;
						}
					} else {
						isSelected = (index == -1);
					}

					if (isSelected) {
						aLiFilters.setAttribute('sel', 'T');
						this.m_SelectedLI = liFilters;
					}

					if (this.control.AllowMultipleSelection) {
						if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
							var imgSelOption = document.createElement('i');
							imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "muls" : "mul"]);
							aLiFilters.appendChild(imgSelOption);
						}
						else {
							$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "MultiselSelOption" : "MultiselOption"]) + ")");
							$(aLiFilters).css("background-repeat", "no-repeat");
							$(aLiFilters).css("background-position", "5px 60%");
						}
					}
					else if (isSelected) {
						liFilters.setAttribute('class', 'SelectedItem');
					}
				}

				var thisC = this;
				$(aLiFilters).bind("click", function (txt, index) {
					return function () {
						var currentLi = this.parentNode;
						if (index >= 0 && data != null && index < data.length && data[index].C != null && data[index].C.length > 0) {
							var level = parseInt(this.getAttribute('level'));
							var expanded = (this.getAttribute('expanded') == 'true');
							this.setAttribute('expanded', expanded ? 'false' : 'true');
							if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
								var iSel = $(this).find('i');
								iSel[iSel.length - 1].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons[expanded ? "texp" : "tcol"]);
							}
							else {
								$(this.childNodes[this.childNodes.length - 1]).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons[expanded ? "TreeviewExpand" : "TreeviewCollapse"]) + ")");
							}
							var hasFilteredChildren = (this.getAttribute('filteredChildren') == 'true');
							while ((currentLi = currentLi.nextSibling) != null) {
								if (parseInt(currentLi.childNodes[0].getAttribute('level')) >= level + 1) {
									if (parseInt(currentLi.childNodes[0].getAttribute('level')) == level + 1) {
										if (!expanded && (!hasFilteredChildren || currentLi.childNodes[0].getAttribute('filtered') == 'true')) {
											$(currentLi).css("display", "");
											if (currentLi.childNodes[0].getAttribute('expanded') == 'true') {
												currentLi.childNodes[0].setAttribute('expanded', 'false');
												if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
													$(currentLi.childNodes[0]).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["texp"]);
												}
												else {
													$(currentLi.childNodes[0].childNodes[currentLi.childNodes[0].childNodes.length - 1]).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["TreeviewExpand"]) + ")");
												}
											}
										}
										else {
											$(currentLi).css("display", "none");
										}
									}
									else {
										$(currentLi).css("display", "none");
									}
								}
								else {
									break;
								}
							}
						}
						else {
							var data_UID = (data != null && index < data.length ? (index >= 0 ? data[index].ID : '') : '<#NEW#>');
							thisC.control.isNew = (data_UID == '<#NEW#>');
							var selDscs = null;
							var setFilterAsSelectedText = false;
							if (thisC.control.AllowMultipleSelection) {
								var selValues = (thisC.control.SelectedValue == null || thisC.control.SelectedValue == '') ? [] : JSON.parse(thisC.control.SelectedValue);
								selDscs = (thisC.control.SelectedText == null || thisC.control.SelectedText == '') ? [] : JSON.parse(thisC.control.SelectedText);

								var selected = (this.getAttribute('sel') == 'T');
								this.setAttribute('sel', selected ? '' : 'T');
								var selItemIndex = -1;
								for (var k = 0; k < selValues.length; k += 1) {
									if (selValues[k] == data_UID) {
										selItemIndex = k;
										break;
									}
								}
								if (selected && selItemIndex >= 0) {
									selValues.splice(selItemIndex, 1);
									selDscs.splice(selItemIndex, 1);
									if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
										$(this).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["mul"]);
									}
									else {
										$(this).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["MultiselOption"]) + ")");
									}
								} else if (!selected && selItemIndex < 0) {
									selValues.push(data_UID);
									selDscs.push(txt);
									if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
										$(this).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["muls"]);
									}
									else {
										$(this).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["MultiselSelOption"]) + ")");
									}
								}
								thisC.SetSelectedValue(JSON.stringify(selValues, null, 2));
								thisC.SetSelectedText(JSON.stringify(selDscs, null, 2));
								thisC.extendedComboBox_UpdateParentsSelection(this.parentNode);
								thisC.extendedComboBox_UpdateSelectAll_A(this.parentNode);
							} else {
								thisC.SetSelectedValue(data_UID);
								setFilterAsSelectedText = thisC.control.isNew;
								thisC.SetSelectedText(txt);
								if (thisC.m_SelectedLI != null) {
									thisC.m_SelectedLI.removeAttribute('class');
								}
								thisC.m_SelectedLI = currentLi;
								currentLi.setAttribute('class', 'SelectedItem');
							}
							thisC.updatedExtendedComboCaption(selDscs);
							if (setFilterAsSelectedText) {
								thisC.SetSelectedText(thisC.m_filterInput != null && $(thisC.m_filterInput).val() != null ? $(thisC.m_filterInput).val() : '');
							}
						}
					}
				}(optionText, index));

				if ((option.C == null || option.C.length == 0) && !this.control.AllowMultipleSelection) {
					aLiFilters.setAttribute("href", DDO_GetLocalURL());
					//to keep URLs
					if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
						aLiFilters.setAttribute("data-toggle", "dropdown");
					}
					if (this.control.OnOptionClicked) {
						$(aLiFilters).bind("click", this.control.OnOptionClicked);

						if (copyVarToAttVal) {
							var thisC = this;
							$(aLiFilters).bind("click", function (e) {
								var ctrlid = wcName + attName;
								$('#' + ctrlid).val($('#' + wcName + 'vCOMBO' + attName).val());
								$('#' + wcName + 'vCOMBO' + attName).parent().one("DOMSubtreeModified", function () {
									$('#' + ctrlid).val($('#' + wcName + 'vCOMBO' + attName).val());
									var gxO = gx.O;
									var controlValidStructFld = gxO.getValidStructFld(ctrlid);
									if (controlValidStructFld != null) {
										gx.csv.validControls(controlValidStructFld.id, controlValidStructFld.id + 1, true, gxO);
									}
								});
								var gxO = gx.O;
								var controlValidStructFld = gxO.getValidStructFld(ctrlid);
								if (controlValidStructFld != null) {
									gx.csv.validControls(controlValidStructFld.id, controlValidStructFld.id + 1, true, gxO);
								}
							});
						}
					}
				} else {
					$(aLiFilters).bind("click", function (e) {
						e.preventDefault();
						e.stopPropagation();
					});
				}

				aLiFilters.appendChild(spanLiFilters);
				if (imgTreeCollapse != null) {
					aLiFilters.appendChild(imgTreeCollapse);
				}
				liFilters.appendChild(aLiFilters);
				ulVals.appendChild(liFilters);
				if (this.control.DataListType != 'FixedValues') {
					this.loadExtendedComboBoxItems(ulVals, option.C, ind + 1, selValues, showNoResults);
				}
			}
		}
	}

	this.updatedExtendedComboCaption = function (selDscs) {
		var selValuesCaption = '';
		if (this.control.AllowMultipleSelection) {
			if (selDscs == null) {
				selDscs = (this.control.SelectedText == null || this.control.SelectedText == '') ? [] : JSON.parse(this.control.SelectedText);
			}
			if (selDscs.length > 0) {
				for (var k = 0; k < selDscs.length; k += 1) {
					if (this.control.MultipleValuesType == 'Tags') {
						selValuesCaption += '<span class="DDO_Tags">' + selDscs[k];
						if (!(window.navigator.userAgent.indexOf("MSIE ") >= 0 || window.navigator.userAgent.indexOf('Edge') >= 0 || (!!window.MSInputMethodContext && !!document.documentMode))) {
							//not IE
							selValuesCaption += '<span class="DDO_Tags_del"></span>';
						}
						selValuesCaption += '</span>';
					} else {
						selValuesCaption += (k > 0 ? this.control.MultipleValuesSeparator : '') + selDscs[k];
					}
				}
			} else {
				selValuesCaption = (this.control.EmptyItem ? gx.getMessage(this.control.EmptyItemText) : '');
			}
			if (this.control.MultipleValuesType == 'Tags' && selDscs.length > 0) {
				this.m_triggerButtonSpan.innerHTML = selValuesCaption;

				var thisC = this;
				$(thisC.m_triggerButtonSpan).find('.DDO_Tags').click(function (e) {

					e.preventDefault();
					e.stopPropagation();
				});

				$(thisC.m_triggerButtonSpan).find('.DDO_Tags_del').click(function (e) {
					var selValues = (thisC.control.SelectedValue == null || thisC.control.SelectedValue == '') ? [] : JSON.parse(thisC.control.SelectedValue);
					var selDscs = (thisC.control.SelectedText == null || thisC.control.SelectedText == '') ? [] : JSON.parse(thisC.control.SelectedText);

					var selItemIndex = $(this.parentNode).index();
					selValues.splice(selItemIndex, 1);
					selDscs.splice(selItemIndex, 1);
					thisC.SetSelectedValue(JSON.stringify(selValues, null, 2));
					thisC.SetSelectedText(JSON.stringify(selDscs, null, 2));
					thisC.updatedExtendedComboCaption(selDscs);

					if (thisC.control.DataListProc == null || thisC.control.DataListProc == '') {
						thisC.extendedComboBox_UL.innerHTML = '';
						thisC.loadExtendedComboBoxItems(thisC.extendedComboBox_UL, thisC.control.DropDownOptionsData, 0, null, false);
					}
					thisC.updateExtendedComboList(thisC.extendedComboBox_UL, $(thisC.m_filterInput).val());

					if (!$('#' + thisC.containerName).hasClass('open') && thisC.control.OnOptionClicked) {
						thisC.control.OnOptionClicked();
					}
					e.preventDefault();
					e.stopPropagation();
				});
			}
			else {
				this.m_triggerButtonSpan.innerText = selValuesCaption;
				if (this.control.MultipleValuesSeparator == '<br />') {
					this.m_triggerButtonSpan.innerHTML = WWP_replaceAll(this.m_triggerButtonSpan.innerHTML, '&lt;br /&gt;', '<br />');
				}
			}
			if (this.OnlySelectedItem_span != null) {
				this.OnlySelectedItem_span.innerHTML = WWP_replaceAll(gx.getMessage(this.control.OnlySelectedValues), '<SELECTED_COUNT>', selDscs.length);
			}
		} else {
			selValuesCaption = (this.control.SelectedText != '<#NEW#>' ? this.control.SelectedText : '');
			this.m_triggerButtonSpan.innerText = selValuesCaption;
		}
		this.control.Caption = selValuesCaption;
	}

	this.updateColumnsSelectorValuesIndex = function (selItem, newPositionItem) {
		var currentOrderIndex = this.columnsSelectorGetCurrentOrderIndex(selItem);
		var newPositionItemOrderIndex = this.columnsSelectorGetCurrentOrderIndex(newPositionItem);
		for (var k = 0; k < this.ColumnsSelectorData_Aux.Columns.length; k += 1) {
			var orderIndex = this.ColumnsSelectorData_Aux.Columns[k].O;
			if (currentOrderIndex == orderIndex) {
				this.ColumnsSelectorData_Aux.Columns[k].O = newPositionItemOrderIndex;
			} else if (currentOrderIndex < newPositionItemOrderIndex && orderIndex > currentOrderIndex && orderIndex <= newPositionItemOrderIndex) {
				this.ColumnsSelectorData_Aux.Columns[k].O = orderIndex - 1;
			} else if (currentOrderIndex > newPositionItemOrderIndex && orderIndex < currentOrderIndex && orderIndex >= newPositionItemOrderIndex) {
				this.ColumnsSelectorData_Aux.Columns[k].O = orderIndex + 1;
			}
		}
	}

	this.columnsSelectorGetCurrentOrderIndex = function (item) {
		for (var k = 0; k < this.ColumnsSelectorData_Aux.Columns.length; k += 1) {
			if (this.ColumnsSelectorData_Aux.Columns[k].C == item) {
				return this.ColumnsSelectorData_Aux.Columns[k].O;
			}
		}
		return -1;
	}

	this.loadColumnsSelectorValues = function (ulColumns) {
		$(ulColumns).html('')
		for (var index1 = 0; index1 < 3; index1 += 1) {
			var coll;
			if (index1 == 0) {
				coll = this.control.DropDownOptionsData_VisibleColumns;
			}
			else if (index1 == 1) {
				coll = this.control.DropDownOptionsData_InvisibleColumns;
			}
			else {
				coll = this.control.DropDownOptionsData_Columns;
			}
			if (coll != undefined) {
				for (var index2 = 0; index2 < coll.length; index2 += 1) {
					var option = coll[index2];
					if (option.O != null && this.control.AllowColumnsReorder) {
						for (var i = 0; i < coll.length; i += 1) {
							if (coll[i].O == index2) {
								option = coll[i];
								break;
							}
						}
					}
					var optionVal = (index1 < 2 ? option.ColumnName : option.C);
					var optionText = optionVal;
					if (index1 == 2 && option.D != null && option.D != '') {
						optionText = option.D;
					}
					if (index1 < 2 || optionVal != '') {
						var thisC = this;
						var liFilters = document.createElement('li');
						var aLiFilters = document.createElement('a');
						if (this.control.AllowColumnsReorder) {
							aLiFilters.draggable = true;
							$(liFilters).data('v', optionVal);
							var dragElemIndex = null;
							$(aLiFilters).bind("dragstart", function (ev) {
								dragElemIndex = $(this).parent().index();
							});
							$(liFilters).bind("dragover", function (ev) {
								ev.preventDefault();
								//to allow drop
							}).bind("drop", function (ev) {
								var newIndex = $(this).index();
								if (dragElemIndex != newIndex) {
									thisC.updateColumnsSelectorValuesIndex($(ulColumns).find('>li:eq(' + dragElemIndex + ')').data('v'), $(this).data('v'));
									if (dragElemIndex > newIndex) {
										$(ulColumns).find('>li:eq(' + dragElemIndex + ')').insertBefore($(this));
									} else {
										$(ulColumns).find('>li:eq(' + dragElemIndex + ')').insertAfter($(this));
									}
								}
							});
						}
						var spanLiFilters = document.createElement('span');
						var optionTextOriginal = optionText;
						optionText = gx.getMessage(optionTextOriginal);
						spanLiFilters.setAttribute('dsc', optionText);
						var isSelected = (index1 == 0 || index1 == 2 && (option.V + '').toLowerCase() != 'false');
						if (isSelected) {
							aLiFilters.setAttribute('sel', 'T');
						}
						spanLiFilters.innerHTML = optionText;

						if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
							var imgSelOption = document.createElement('i');
							imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "muls" : "mul"]);
							aLiFilters.appendChild(imgSelOption);
						} else {
							$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "MultiselSelOption" : "MultiselOption"]) + ")");
							$(aLiFilters).css("background-repeat", "no-repeat");
							$(aLiFilters).css("background-position", "5px 60%");
						}

						aLiFilters.setAttribute("href", DDO_GetLocalURL());
						//to keep URLs
						if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
							aLiFilters.setAttribute("data-toggle", "dropdown");
						}

						$(aLiFilters).bind("click", function (ek, txt) {
							return function () {
								var isSelected = (this.getAttribute('sel') == 'T');
								if (thisC.ColumnsSelectorData_Aux.Columns != null && thisC.ColumnsSelectorData_Aux.Columns.length > 0) {
									this.setAttribute('sel', isSelected ? '' : 'T');
									for (var k = 0; k < thisC.ColumnsSelectorData_Aux.Columns.length; k += 1) {
										if (thisC.ColumnsSelectorData_Aux.Columns[k].C == ek) {
											thisC.ColumnsSelectorData_Aux.Columns[k].V = (!isSelected);
											if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
												$(this).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons[isSelected ? "mul" : "muls"]);
											} else {
												$(this).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons[isSelected ? "MultiselOption" : "MultiselSelOption"]) + ")");
											}
											break;
										}
									}
								}
								else {
									if (isSelected) {
										this.setAttribute('sel', '');
										var selItemIndex = -1;
										for (var k = 0; k < thisC.ColumnsSelectorData_Aux.VisibleColumns.length; k += 1) {
											if (thisC.ColumnsSelectorData_Aux.VisibleColumns[k].ColumnName == ek) {
												selItemIndex = k;
												break;
											}
										}
										if (selItemIndex >= 0) {
											thisC.ColumnsSelectorData_Aux.VisibleColumns.splice(selItemIndex, 1)
											thisC.ColumnsSelectorData_Aux.InvisibleColumns.push({ ColumnName: ek })
											if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
												$(this).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["mul"]);
											} else {
												$(this).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["MultiselOption"]) + ")");
											}
										}
									} else {
										this.setAttribute('sel', 'T');
										var selItemIndex = -1;
										for (var k = 0; k < thisC.ColumnsSelectorData_Aux.InvisibleColumns.length; k += 1) {
											if (thisC.ColumnsSelectorData_Aux.InvisibleColumns[k].ColumnName == ek) {
												selItemIndex = k;
												break;
											}
										}
										if (selItemIndex >= 0) {
											thisC.ColumnsSelectorData_Aux.InvisibleColumns.splice(selItemIndex, 1)
											thisC.ColumnsSelectorData_Aux.VisibleColumns.push({ ColumnName: ek })
											if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
												$(this).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["muls"]);
											} else {
												$(this).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["MultiselSelOption"]) + ")");
											}
										}
									}
								}
							}
						}(optionVal, optionText));

						$(aLiFilters).bind("click", function (e) {
							e.preventDefault();
							e.stopPropagation();
						});

						aLiFilters.appendChild(spanLiFilters);
						liFilters.appendChild(aLiFilters);
						ulColumns.appendChild(liFilters);
					}
				}
			}
		}
	}

	this.IsValueEmpty = function (val) {
		return val == '' || this.IsDate() && val.replace('/', '').replace('/', '').trim() == ''
			|| this.control.AllowMultipleSelection && val == '[]';
	}

	this.minIndexForSelectedItemFirst = 4;
	this.includeFirstSelectedItem = false;
	this.reloadListFixedValuesOnDDOOpen = false;
	this.LoadListFixedValues = function (liDropDownItem, isReload) {
		var divFixedValues;
		if (isReload) {
			divFixedValues = liDropDownItem.childNodes[0];
			divFixedValues.removeChild(divFixedValues.childNodes[0]);
		} else {
			divFixedValues = document.createElement('div');
			if (this.control.AllowMultipleSelection) {
				divFixedValues.className = "FilterOptions FilterOptionsMultiSelection";
			}
			else {
				divFixedValues.className = "FilterOptions";
			}
		}
		var optionsText = this.control.DataListFixedValues;
		optionsText = WWP_replaceAll(optionsText, '\\\\', '[\\\\]');
		optionsText = WWP_replaceAll(optionsText, '\\,', '[\\\\\\]');
		if (optionsText.trim() == '') {
			optionsText = "Error\\: Filter with Data List as 'Filter Values' must have a non empty Filter Value."
		}
		var optionsTextSplitted = optionsText.split(',');
		var ulFixedValues = document.createElement('ul');

		if (optionsTextSplitted.length > this.minIndexForSelectedItemFirst && !this.control.AllowMultipleSelection) {
			this.reloadListFixedValuesOnDDOOpen = true;
			this.includeFirstSelectedItem = true;
			var liFilters = document.createElement('li');
			var aLiFilters = document.createElement('a');
			var spanLiFilters = document.createElement('span');

			if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
				var imgSelOption = document.createElement('i');
				imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["selo"]);
				aLiFilters.appendChild(imgSelOption);
			}
			else {
				$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["SelectedOption"]) + ")");
				$(aLiFilters).css("background-repeat", "no-repeat");
				$(aLiFilters).css("background-position", "10px 60%");
			}
			$(liFilters).css("display", "none");

			aLiFilters.setAttribute("href", DDO_GetLocalURL());
			//to keep URLs
			if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
				aLiFilters.setAttribute("data-toggle", "dropdown");
			}

			var dropDownOptions = this;
			$(aLiFilters).bind("click", function (ek) {
				return function () {
					dropDownOptions.SetActiveEventKey('<#Filter#>');
				}
			}(null));
			if (this.control.OnOptionClicked)
				$(aLiFilters).bind("click", this.control.OnOptionClicked);

			aLiFilters.appendChild(spanLiFilters);
			liFilters.appendChild(aLiFilters);
			ulFixedValues.appendChild(liFilters);
		} else if (this.control.IncludeFilter || this.control.AllowMultipleSelection) {
			this.reloadListFixedValuesOnDDOOpen = true;
		}

		var positionToInsert = 0;
		for (var index2 = 0; index2 < optionsTextSplitted.length; index2 += 1) {
			var option = optionsTextSplitted[index2];
			option = WWP_replaceAll(option, '[\\\\\\]', ',');
			option = WWP_replaceAll(option, '\\:', '[\\\\\\]');
			var optionTextSplitted = option.split(':');
			var optionVal = optionTextSplitted[0];
			optionVal = WWP_replaceAll(optionVal, '[\\\\\\]', ':');
			optionVal = WWP_replaceAll(optionVal, '[\\\\]', '\\');
			var optionText;
			if (optionTextSplitted.length > 1) {
				optionText = optionTextSplitted[1];
				optionText = WWP_replaceAll(optionText, '[\\\\\\]', ':');
				optionText = WWP_replaceAll(optionText, '[\\\\]', '\\');
			} else {
				optionText = optionVal;
			}
			var liFilters = document.createElement('li');
			var aLiFilters = document.createElement('a');
			var spanLiFilters = document.createElement('span');
			var optionTextOriginal = optionText;
			optionText = gx.getMessage(optionTextOriginal);
			spanLiFilters.setAttribute('dsc', optionText);
			var isSelected = false;
			if (this.control.AllowMultipleSelection) {
				for (var k = 0; k < this.SelectedValue_getAUX.length; k += 1) {
					if (this.SelectedValue_getAUX[k] == optionVal) {
						isSelected = true;
						break;
					}
				}
			} else {
				isSelected = (optionVal == this.control.SelectedValue.trim());
			}
			if (isSelected) {
				aLiFilters.setAttribute('sel', 'T');
				if (this.control.AllowMultipleSelection) {
					spanLiFilters.innerHTML = optionText;
				} else {
					spanLiFilters.innerHTML = '<strong>' + optionText + '</strong>';
					if (!this.includeFirstSelectedItem) {
						if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
							var imgSelOption = document.createElement('i');
							imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["selo"]);
							aLiFilters.appendChild(imgSelOption);
						}
						else {
							$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["SelectedOption"]) + ")");
							$(aLiFilters).css("background-repeat", "no-repeat");
							$(aLiFilters).css("background-position", "10px 60%");
						}
					}
				}
			} else {
				spanLiFilters.innerHTML = optionText;
			}

			if (this.control.AllowMultipleSelection) {
				if (isSelected) {
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						var imgSelOption = document.createElement('i');
						imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["muls"]);
						aLiFilters.appendChild(imgSelOption);
					}
					else {
						$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["MultiselSelOption"]) + ")");
					}
				} else {
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						var imgSelOption = document.createElement('i');
						imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["mul"]);
						aLiFilters.appendChild(imgSelOption);
					}
					else {
						$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["MultiselOption"]) + ")");
					}
				}
				$(aLiFilters).css("background-repeat", "no-repeat");
				$(aLiFilters).css("background-position", "5px 60%");
			}

			aLiFilters.setAttribute("href", DDO_GetLocalURL());
			//to keep URLs
			if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
				aLiFilters.setAttribute("data-toggle", "dropdown");
			}

			var thisC = this;
			$(aLiFilters).bind("click", function (ek, txt) {
				return function () {
					if (thisC.control.AllowMultipleSelection) {
						if (this.getAttribute('sel') == 'T') {
							this.setAttribute('sel', '');
							var selItemIndex = -1;
							for (var k = 0; k < thisC.SelectedValue_getAUX.length; k += 1) {
								if (thisC.SelectedValue_getAUX[k] == ek) {
									selItemIndex = k;
									break;
								}
							}
							if (selItemIndex >= 0) {
								thisC.SelectedValue_getAUX.splice(selItemIndex, 1);
								thisC.SelectedText_getAUX.splice(selItemIndex, 1);
							}
							if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
								$(this).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["mul"]);
							}
							else {
								$(this).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["MultiselOption"]) + ")");
							}
						} else {
							this.setAttribute('sel', 'T');
							thisC.SelectedValue_getAUX.push(ek);
							thisC.SelectedText_getAUX.push(txt);
							if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
								$(this).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons["muls"]);
							}
							else {
								$(this).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons["MultiselSelOption"]) + ")");
							}
						}
					} else {
						thisC.SetActiveEventKey('<#Filter#>');
						thisC.SetSelectedValue(ek);
						thisC.SetSelectedText(txt);
						thisC.SetFilteredText('');
						if (thisC.control.FilterIsRange) {
							thisC.SetFilteredTextTo('');
						}
					}
				}
			}(optionVal, optionText));

			if (this.control.AllowMultipleSelection) {
				$(aLiFilters).bind("click", function (e) {
					e.preventDefault();
					e.stopPropagation();
				});
			} else if (this.control.OnOptionClicked) {
				$(aLiFilters).bind("click", this.control.OnOptionClicked);
			}

			aLiFilters.appendChild(spanLiFilters);
			liFilters.appendChild(aLiFilters);
			if (isSelected && this.control.AllowMultipleSelection && optionsTextSplitted.length > this.minIndexForSelectedItemFirst) {
				ulFixedValues.insertBefore(liFilters, ulFixedValues.childNodes[positionToInsert]);
				positionToInsert++;
			}
			else {
				ulFixedValues.appendChild(liFilters);
			}
		}
		if (this.control.IncludeFilter) {
			var liFilters = document.createElement('li');
			var aLiFilters = document.createElement('a');
			var spanLiFilters = document.createElement('span');
			spanLiFilters.innerHTML = gx.getMessage(this.control.NoResultsFound);
			$(liFilters).css("display", "none");

			aLiFilters.setAttribute("href", DDO_GetLocalURL());
			//to keep URLs
			if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
				aLiFilters.setAttribute("data-toggle", "dropdown");
			}

			$(aLiFilters).bind("click", function (e) {
				e.preventDefault();
				e.stopPropagation();
			});

			aLiFilters.appendChild(spanLiFilters);
			liFilters.appendChild(aLiFilters);
			ulFixedValues.appendChild(liFilters);
		}
		divFixedValues.appendChild(ulFixedValues);
		if (!isReload) {
			liDropDownItem.appendChild(divFixedValues);
		}
	}

	this.LoadFixedFilters = function (liDropDownItem) {
		var divFixedValues = document.createElement('div');
		divFixedValues.className = "FilterOptions";
		var optionsText = this.control.FixedFilters;
		optionsText = WWP_replaceAll(optionsText, '\\\\', '[\\\\]');
		optionsText = WWP_replaceAll(optionsText, '\\,', '[\\\\\\]');
		var optionsTextSplitted = optionsText.split(',');

		var ulFixedValues = document.createElement('ul');
		for (var index2 = 0; index2 < optionsTextSplitted.length; index2 += 1) {
			var option = optionsTextSplitted[index2];
			option = WWP_replaceAll(option, '[\\\\\\]', ',');
			option = WWP_replaceAll(option, '\\:', '[\\\\\\]');
			var optionTextSplitted = option.split(':');
			var optionVal = optionTextSplitted[0];
			optionVal = WWP_replaceAll(optionVal, '[\\\\\\]', ':');
			optionVal = WWP_replaceAll(optionVal, '[\\\\]', '\\');
			var optionText;
			if (optionTextSplitted.length > 1) {
				optionText = optionTextSplitted[1];
				optionText = WWP_replaceAll(optionText, '[\\\\\\]', ':');
				optionText = WWP_replaceAll(optionText, '[\\\\]', '\\');
			} else {
				optionText = optionVal;
			}
			var liFilters = document.createElement('li');
			var aLiFilters = document.createElement('a');
			var spanLiFilters = document.createElement('span');
			optionText = gx.getMessage(optionText);
			spanLiFilters.setAttribute('dsc', optionText);
			var isSelected = (optionVal == this.control.SelectedFixedFilter.trim());
			if (isSelected) {
				aLiFilters.setAttribute('sel', 'T');
				spanLiFilters.innerHTML = '<strong>' + optionText + '</strong>';
				if (!this.includeFirstSelectedItem) {
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						var imgSelOption = document.createElement('i');
						imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["selo"]);
						aLiFilters.appendChild(imgSelOption);
					}
					else {
						$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["SelectedOption"]) + ")");
						$(aLiFilters).css("background-repeat", "no-repeat");
						$(aLiFilters).css("background-position", "10px 60%");
					}
				}
			} else {
				spanLiFilters.innerHTML = optionText;
			}

			aLiFilters.setAttribute("href", DDO_GetLocalURL());
			//to keep URLs
			if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
				aLiFilters.setAttribute("data-toggle", "dropdown");
			}

			var thisC = this;
			$(aLiFilters).bind("click", function (ek, txt) {
				return function () {
					thisC.control.SelectedFixedFilter = ek;
					if (thisC.control.IsM) {
						thisC.SetActiveEventKeyFixedFilter_M();
					} else {
						thisC.SetActiveEventKey('<#FixedFilter:' + ek + '#>');
					}
					thisC.SetSelectedValue('');
					thisC.SetSelectedText('');
					thisC.SetFilteredText('');
					if (thisC.control.FilterIsRange) {
						thisC.SetFilteredTextTo('');
					}
				}
			}(optionVal, optionText));

			$(aLiFilters).bind("click", this.control.OnOptionClicked);

			aLiFilters.appendChild(spanLiFilters);
			liFilters.appendChild(aLiFilters);
			ulFixedValues.appendChild(liFilters);
		}
		divFixedValues.appendChild(ulFixedValues);
		liDropDownItem.appendChild(divFixedValues);
	}

	this.OnDDOClosing = function () {
		var calendarV = false;
		var cals = $('.calendar');
		for (var i = 0; i < cals.length; i++) {
			if ($(cals[i]).is(":visible")) {
				calendarV = true;
				break;
			}
		}
		if (calendarV) {
			return false;
		} else {
			if (this.calendarOpened) {
				this.calendarOpened = false;
				return false;
			}
		}
		this.DetachAuxDateVariable(this.GetDateFilterVariable() + "_dp_container");
		if (this.control.FilterIsRange) {
			this.DetachAuxDateVariable(this.GetDateFilterVariableTo() + "_dp_container");
		}
		return true;
	}

	this.OnDDOClosingRemoveError = function () {
		var errorBtn = $("#" + this.containerToDrawName).find('.Errorbtn');
		if (errorBtn.length == 1 && errorBtn.attr('data-gx-unprefixed-class') != '' && errorBtn[0].nextSibling.tagName == 'DIV') {
			errorBtn.attr("class", errorBtn.attr('data-gx-unprefixed-class'));
			$(errorBtn[0].nextSibling).remove();
		}
		return true;
	}

	this.AttachAuxDateVariable = function (liRemoveFilter, varName, dateDivName, dateVal, idSearchButton, idOptions) {
		var dateContainer = $("#" + varName + "_dp_container").detach();
		dateContainer.appendTo("#" + dateDivName);
		var inputVal = $('#' + varName).get(0);
		$(inputVal).prop("class", "form-control AttributeDate")
		$(inputVal).val(dateVal);
		this.applyDateFocusHack = true;

		$(inputVal).unbind("click");
		$(inputVal).bind("click", function (e) {
			$(this).get(0).selectionStart = 0;
			$(this).get(0).selectionEnd = 0;
			$(this).select();
			e.preventDefault();
			e.stopPropagation();
		});

		$(inputVal).keydown(function (event) {

			if (event.keyCode == 13) {
				event.preventDefault();
				event.stopPropagation();
				var btnSearch = document.getElementById(idSearchButton);
				$(btnSearch).click();
				$(btnSearch).focus();
			}
		});

		$('#' + varName + '_dp_trigger').unbind("click");
		$('#' + varName + '_dp_trigger').bind("click", function (thisC) {
			return function () {
				$('.calendar').css("z-index", "1001");
				thisC.calendarOpened = true;
			}
		}(this));
		$(inputVal).blur();
		return inputVal;
	}

	this.CreateFilterInput = function (filteredText, liDropDownItem, filterInput, filterInputFrom, filterInputTo, isRange, elemSearchId, idOptions) {
		filterInput.className = "form-control Attribute";
		filterInput.setAttribute("type", "text");
		filterInput.setAttribute("maxlength", "40");
		if (isRange) {
			filterInput.setAttribute("style", "width: 125px;padding-right: 0px;height: 28px;float: right;");
		}
		if (this.control.DataListType != null && this.control.DataListType.indexOf("Dynamic") == 0) {
			filterInput.setAttribute("value", filteredText == undefined ? '' : filteredText);
		}

		$(filterInput).bind("click", function (e) {
			e.preventDefault();
			e.stopPropagation();
		});

		$(filterInput).keydown(function (event) {

			if (event.keyCode == 13) {
				event.preventDefault();
				event.stopPropagation();
				if (elemSearchId != null && elemSearchId != '') {
					var elemSearch = document.getElementById(elemSearchId);
					$(elemSearch).click();
					document.getElementById(idOptions).focus();
				}
			}
		});

		if (this.control.IncludeDataList || this.control.DropDownOptionsType == "GridColumnsSelector" || this.control.DropDownOptionsType == "ExtendedComboBox") {
			$(filterInput).on("input", function (thisC) {
				return function (e) {
					if (thisC.myT != null) {
						clearInterval(thisC.myT);
					}
					thisC.myT = setInterval(function () {
						clearInterval(thisC.myT);
						if (thisC.control.DropDownOptionsType == "GridColumnsSelector") {
							thisC.updateColumnsSelectorList(liDropDownItem.childNodes[0].childNodes[0], $(filterInput).val());
						} else if (thisC.control.DropDownOptionsType == "ExtendedComboBox") {
							thisC.updateExtendedComboList(liDropDownItem.childNodes[0].childNodes[0], $(filterInput).val());
						} else if (thisC.control.DataListType == "Dynamic") {
							thisC.updateFilterList(liDropDownItem, filterInputFrom, filterInputTo, true);
						} else if (thisC.control.DataListType == "Dynamic (with fixedValues)") {
							if (thisC.procResult == null) {
								thisC.updateFilterList(liDropDownItem, null, null, true);
							}
							thisC.ProcessProcResult(thisC.procResult, liDropDownItem, null, null, $(filterInputFrom).val(), true);
						} else if (!thisC.control.FilterIsRange) {
							thisC.updateFixedValuesList(liDropDownItem, filterInput);
						}
					}, 300);
				}
			}(this));
		}
		if (this.control.FilterType == 'Numeric' && this.control.DataListType != "Dynamic (with fixedValues)" && this.control.DataListType != "FixedValues") {
			$(filterInput).keypress(function (e) {
				//if the letter is not digit then don't type anything
				if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
					return false;
				}
			});
		}
	}

	this.updateFixedValuesList = function (liDropDownItem, filterInput) {
		var searchTxt = $(filterInput).val();
		this.updateFixedValuesList2(liDropDownItem, searchTxt);
	}

	this.updateFixedValuesList2 = function (liDropDownItem, searchTxt) {
		var ulNode = liDropDownItem.childNodes[0].childNodes[0];
		var found = 0;
		var selTitle = '';
		var showSelectedItemFirst = false;
		var index = 0;
		if (this.includeFirstSelectedItem) {
			index += 1;
		}
		var length = ulNode.childNodes.length;
		var isDynamic = (this.control.DataListType.indexOf("Dynamic") == 0);
		if (this.control.IncludeFilter) {
			length -= 1;
		}
		for (; index < length; index += 1) {
			var aLiFilters = ulNode.childNodes[index].childNodes[0];
			var spanNode = $(aLiFilters).find('span')[0];
			var title = spanNode.getAttribute('dsc');
			if (searchTxt != null && searchTxt.length > 0) {
				var ind = WWP_removeAccents(title.toLowerCase()).indexOf(WWP_removeAccents(searchTxt.toLowerCase()));
				if (ind >= 0) {
					title = title.substring(0, ind) + '<strong>' + title.substring(ind, ind + searchTxt.length) + '</strong>' + title.substring(ind + searchTxt.length);
					var dscSufix = spanNode.getAttribute('dscSuffix');
					if (dscSufix != null) {
						spanNode.innerHTML = title + dscSufix;
					} else {
						spanNode.innerHTML = title;
					}
					$(spanNode.parentNode.parentNode).css("display", "");
					found++;
				} else {
					$(spanNode.parentNode.parentNode).css("display", "none");
				}
			} else {
				var isSelected = (aLiFilters.getAttribute('sel') == 'T');
				if (this.control.AllowMultipleSelection) {
					$(spanNode.parentNode.parentNode).css("display", "");
					var dscSufix = spanNode.getAttribute('dscSuffix');
					if (dscSufix != null) {
						spanNode.innerHTML = title + dscSufix;
					} else {
						spanNode.innerHTML = title;
					}
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						$(aLiFilters).find('i')[0].setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "muls" : "mul"]);
					}
					else {
						$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "MultiselSelOption" : "MultiselOption"]) + ")");
					}
				} else {
					if (isSelected) {
						spanNode.innerHTML = '<strong>' + title + '</strong>';
						if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
							if ($(spanNode.parentNode).find('i').length > 0) {
								$(spanNode.parentNode).find('i')[0].setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["selo"]);
							} else {
								var imgSelOption = document.createElement('i');
								imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["selo"]);
								spanNode.parentNode.insertBefore(imgSelOption, spanNode.parentNode.childNodes[0]);
							}
						}
						else {
							$(spanNode.parentNode).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["SelectedOption"]) + ")");
							$(spanNode.parentNode).css("background-repeat", "no-repeat");
							$(spanNode.parentNode).css("background-position", "10px 60%");
						}
						if (index - 1 >= this.minIndexForSelectedItemFirst) {
							$(spanNode.parentNode.parentNode).css("display", "none");
							showSelectedItemFirst = true;
							selTitle = spanNode.innerHTML;
						} else {
							$(spanNode.parentNode.parentNode).css("display", "");
						}
					} else {
						var dscSufix = spanNode.getAttribute('dscSuffix');
						if (dscSufix != null) {
							spanNode.innerHTML = title + dscSufix;
						} else {
							spanNode.innerHTML = title;
						}
						$(spanNode.parentNode.parentNode).css("display", "");
						if (index - 1 >= this.minIndexForSelectedItemFirst) {
							if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
								$(spanNode.parentNode).find('i').remove();
							}
							else {
								$(spanNode.parentNode).css("background-image", "");
								$(spanNode.parentNode).css("background-repeat", "");
								$(spanNode.parentNode).css("background-position", "");
							}
						}
					}
				}
				found++;
			}
		}
		if (showSelectedItemFirst) {
			var selNode = ulNode.childNodes[0];
			$(selNode).css("display", "");
			$(selNode).find('span')[0].innerHTML = selTitle;
		} else if (this.includeFirstSelectedItem) {
			$(ulNode.childNodes[0]).css("display", "none");
		}
		if (this.control.IncludeFilter) {
			var noResultsNode = ulNode.childNodes[ulNode.childNodes.length - 1];
			if (found == 0) {
				$(noResultsNode).css("display", "");
			} else {
				$(noResultsNode).css("display", "none");
			}
		}
	}

	this.updateExtendedComboList = function (ulNode, searchTxt) {
		if (this.control.DataListProc != null && this.control.DataListProc != '') {
			ulNode.innerHTML = '';
			if (searchTxt.length >= this.control.DataListUpdateMinimumCharacters) {
				$(this.m_loadingLi).css("display", "");
				var thisC = this;
				var restURL = window.location.href;
				restURL = restURL.substring(0, restURL.length - window.location.search.length);
				restURL = restURL.substring(0, restURL.lastIndexOf('/'));
				var lastIndexOfQ = restURL.lastIndexOf('?');
				if (lastIndexOfQ >= 0) {
					restURL = restURL.substring(0, lastIndexOfQ);
					restURL = restURL.substring(0, restURL.lastIndexOf('/'));
				}
				if (restURL.lastIndexOf('/servlet') == restURL.length - 8) {
					restURL = restURL.substring(0, restURL.length - 8);
				}
				restURL = restURL + '/rest/' + WWP_replaceAll(this.control.DataListProc, ".", "/");

				var dataListProcParametersPrefix = this.control.DataListProcParametersPrefix;
				while (dataListProcParametersPrefix.indexOf('#') > 0) {
					var index = dataListProcParametersPrefix.indexOf('#');
					var dynVarName = dataListProcParametersPrefix.substring(index + 1, index + 1 + dataListProcParametersPrefix.substring(index + 1).indexOf('#'))
					dataListProcParametersPrefix = dataListProcParametersPrefix.replace('#' + dynVarName + '#', WWP_replaceAll($('#' + dynVarName).val(), '"', '\\"'));
				}

				this.ajaxCall = $.ajax({
					async: true,
					type: "POST",
					url: restURL,
					headers: {
						"Authorization": this.control.GAMOAuthToken
					},
					contentType: "application/json",
					data: '{' + dataListProcParametersPrefix + ', "SearchTxt": "' + searchTxt + '"}',
					success: function (result) {
						var data = JSON.parse(result.Combo_DataJson);
						$(thisC.m_loadingLi).css("display", "none");
						thisC.loadExtendedComboBoxItems(ulNode, data, 0, null, true);
						if (data != null && data.length > 0 || thisC.control.IncludeAddNewOption) {
							thisC.updateExtendedComboList2(ulNode, searchTxt);
						}
						thisC.shiftPageFooter(true);
					},
					error: function (jqXHR, textStatus, errorThrown) {
						console.log("jqXHR statusCode" + jqXHR.statusCode());
						console.log("textStatus " + textStatus);
						console.log("errorThrown " + errorThrown);
						if (jqXHR != null && jqXHR.status == 401) {
							//401 (Not Authorized) -> Reload page so the user will be redirected to login
							window.location.reload();
						}
					}
				});
			}
			else {
				var data = [];
				this.loadExtendedComboBoxItems(ulNode, data, 0, null, false);
				this.extendedComboBox_UpdateSelectAll_A(null);
			}
		}
		else {
			this.updateExtendedComboList2(ulNode, searchTxt);
		}
	}

	this.updateExtendedComboList2 = function (ulNode, searchTxt) {

		var selTitle = '';
		var index = 0;
		var length = ulNode.childNodes.length;
		var filterSelected = this.ExtendedComboBox_FilterSelected;
		var lastLevelFiltered = -1;
		for (; index < length; index += 1) {
			var li = ulNode.childNodes[index];
			var aLiFilters = li.childNodes[0];
			aLiFilters.removeAttribute('filtered');
			aLiFilters.removeAttribute('filteredChildren');
			var spanNode = $(aLiFilters).find('span.OptionLabel')[0];
			var title = spanNode.getAttribute('dsc');
			var elemVisible;
			var filteringTxt = (searchTxt != null && searchTxt.length > 0);
			elemVisible = false;
			if (filteringTxt) {

				var ind = WWP_removeAccents(title.toLowerCase()).indexOf(WWP_removeAccents(searchTxt.toLowerCase()));
				if (spanNode.hasAttribute('add-new-option')) {
					title = encodeHtmlEntities(title);
					elemVisible = true;
				}
				else if (ind >= 0 && (!filterSelected || aLiFilters.getAttribute('sel') == 'T')) {
					title = encodeHtmlEntities(title.substring(0, ind)) + '<strong>' + encodeHtmlEntities(title.substring(ind, ind + searchTxt.length)) + '</strong>' + encodeHtmlEntities(title.substring(ind + searchTxt.length));
					elemVisible = true;
					if (this.control.CategoryFilteredBehaviour == 'ShowAllChildren') {
						var level = parseInt(aLiFilters.getAttribute('level'));
						if (lastLevelFiltered == -1 || lastLevelFiltered > level) {
							lastLevelFiltered = level;
						}
					}
				}
				else if (this.control.CategoryFilteredBehaviour == 'ShowAllChildren' && lastLevelFiltered != -1) {
					var level = parseInt(aLiFilters.getAttribute('level'));
					if (lastLevelFiltered < level) {
						elemVisible = true;
						title = encodeHtmlEntities(title);
					}
					else {
						lastLevelFiltered = -1;
					}
				}
			} else {
				if (!filterSelected || aLiFilters.getAttribute('sel') == 'T') {
					elemVisible = true;
					title = encodeHtmlEntities(title);
				}
			}
			if (elemVisible) {
				$(li).css("display", "");
				if (aLiFilters.getAttribute('expanded') != null) {
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						var iSel = $(aLiFilters).find('i');
						iSel[iSel.length - 1].setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons[filteringTxt ? "texp" : "tcol"]);
					} else {
						$(aLiFilters.childNodes[aLiFilters.childNodes.length - 1]).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons[filteringTxt ? "TreeviewExpand" : "TreeviewCollapse"]) + ")");
					}
					aLiFilters.setAttribute('expanded', filteringTxt ? 'false' : 'true');
				}
				if (filteringTxt || filterSelected) {
					aLiFilters.setAttribute('filtered', 'true');
					this.updateExtendedComboList_ShowParent(ulNode, aLiFilters, index);
				}
			} else {
				$(li).css("display", "none");
			}
			spanNode.innerHTML = title;
		}
		this.extendedComboBox_UpdateSelectAll_A(null);
		this.extendedComboBox_UpdateNodeWithChildrenSelection();
	}

	this.updateExtendedComboList_ShowParent = function (ulNode, aLiFilters, index) {
		if (aLiFilters.getAttribute('level') != '0') {
			var level = parseInt(aLiFilters.getAttribute('level')) - 1;
			for (var index2 = index - 1; index2 >= 0; index2 -= 1) {
				var aLiFilters2 = ulNode.childNodes[index2].childNodes[0];
				if (parseInt(aLiFilters2.getAttribute('level')) == level) {
					aLiFilters2.setAttribute('filteredChildren', 'true');
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						var iSel = $(aLiFilters2).find('i');
						iSel[iSel.length - 1].setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["tcol"]);
					} else {
						$(aLiFilters2.childNodes[aLiFilters2.childNodes.length - 1]).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["TreeviewCollapse"]) + ")");
					}
					aLiFilters2.setAttribute('expanded', 'true');
					if ($(aLiFilters2.parentNode).css("display") == "none") {
						$(aLiFilters2.parentNode).css("display", "");
						aLiFilters2.setAttribute('filtered', 'true');
						level--;
					}
					else {
						break;
					}
				}
			}
		}
	}

	this.updateColumnsSelectorList = function (ulNode, searchTxt) {
		var found = 0;
		var selTitle = '';
		var index = 0;
		var length = ulNode.childNodes.length;
		for (; index < length; index += 1) {
			var aLiFilters = ulNode.childNodes[index].childNodes[0]
			var spanNode = $(aLiFilters).find('span')[0];
			var title = spanNode.getAttribute('dsc');
			if (searchTxt != null && searchTxt.length > 0) {
				var ind = WWP_removeAccents(title.toLowerCase()).indexOf(WWP_removeAccents(searchTxt.toLowerCase()));
				if (ind >= 0) {
					title = title.substring(0, ind) + '<strong>' + title.substring(ind, ind + searchTxt.length) + '</strong>' + title.substring(ind + searchTxt.length);
					var dscSufix = spanNode.getAttribute('dscSuffix');
					if (dscSufix != null) {
						spanNode.innerHTML = title + dscSufix;
					} else {
						spanNode.innerHTML = title;
					}
					$(spanNode.parentNode.parentNode).css("display", "");
					found++;
				} else {
					$(spanNode.parentNode.parentNode).css("display", "none");
				}
			} else {
				var isSelected = (aLiFilters.getAttribute('sel') == 'T');
				$(spanNode.parentNode.parentNode).css("display", "");
				var dscSufix = spanNode.getAttribute('dscSuffix');
				if (dscSufix != null) {
					spanNode.innerHTML = title + dscSufix;
				} else {
					spanNode.innerHTML = title;
				}
				if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
					$(aLiFilters).find('i')[0].setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "muls" : "mul"]);
				}
				else {
					$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "MultiselSelOption" : "MultiselOption"]) + ")");
				}
				found++;
			}
		}
	}

	this.DetachAuxDateVariable = function (varName) {

		var varContainer = $("#" + varName).detach();
		varContainer.appendTo("#" + this.GetWebComponentPrefix() + (this.control.IsM ? 'DDO_' + this.control.MName : this.control.ControlName).toUpperCase() + "AUXDATES");
	}

	this.GetDateFilterVariable = function () {
		if (this.dateFilterVarName == null || this.dateFilterVarName == '') {
			this.dateFilterVarName = this.GetWebComponentPrefix() + 'v' + (this.control.IsM ? 'DDO_' + this.control.MName : this.control.ControlName).toUpperCase() + 'AUXDATE';
		}
		return this.dateFilterVarName;
	}

	this.GetWebComponentPrefix = function () {
		if (this.control.IsM) {
			return this.control.MControl.ContainerName.replace(this.control.MControl.ControlName.toUpperCase() + 'Container', '');
		} else {
			return this.containerName.replace(this.control.ControlName.toUpperCase() + 'Container', '');
		}
	}

	this.GetDateFilterVariableTo = function () {
		return this.GetDateFilterVariable() + 'TO';
	}

	this.ajaxCall2;
	this.ajaxCall;
	this.procResult;
	this.updateFilterList = function (liDropDownItem, filterInput, filterInputTo, typing) {

		var originalSearchTxt = '';
		var searchTxt = '';
		var searchTxtTo = '';
		var updateList = true;
		if (this.control.DataListType != "Dynamic (with fixedValues)") {
			if (this.control.IncludeFilter) {
				if (filterInput != null) {
					originalSearchTxt = $(filterInput).val();
					searchTxt = originalSearchTxt;
					if (this.control.FilterType == 'Character' && searchTxt.length < this.control.DataListUpdateMinimumCharacters) {
						updateList = false;
					}
					if (searchTxt != null && searchTxt.length > 0) {
						searchTxt = WWP_replaceAll(searchTxt, '\\', '[\\]');
						searchTxt = WWP_replaceAll(searchTxt, '"', '\\"');
						searchTxt = WWP_replaceAll(searchTxt, '[\\]', '\\\\');
					}
				}
				else {
					searchTxt = '';
					if (this.control.FilterType == 'Character' && this.control.DataListUpdateMinimumCharacters > 0) {
						updateList = false;
					}
				}
				if (this.control.FilterIsRange) {
					searchTxtTo = $(filterInputTo).val();
					if (!updateList && searchTxtTo.length >= this.control.DataListUpdateMinimumCharacters) {
						updateList = true;
					}
					if (searchTxtTo != null && searchTxtTo.length > 0) {
						searchTxtTo = WWP_replaceAll(searchTxtTo, '\\', '[\\]');
						searchTxtTo = WWP_replaceAll(searchTxtTo, '"', '\\"');
						searchTxtTo = WWP_replaceAll(searchTxtTo, '[\\]', '\\\\');
					}
				}
			}
		}
		if (updateList) {
			this.addLoadingOption(liDropDownItem);

			if (this.control.TitleControlAlign == 'Left') {
				$(liDropDownItem).parent().css("margin-left", "-" + ($(liDropDownItem).parent().outerWidth() - $($(liDropDownItem).parent().parent().children()[0]).outerWidth()) + "px");
			}

			var thisC = this;
			var restURL = window.location.href;
			restURL = restURL.substring(0, restURL.length - window.location.search.length);
			restURL = restURL.substring(0, restURL.lastIndexOf('/'));
			var lastIndexOfQ = restURL.lastIndexOf('?');
			if (lastIndexOfQ >= 0) {
				restURL = restURL.substring(0, lastIndexOfQ);
				restURL = restURL.substring(0, restURL.lastIndexOf('/'));
			}
			if (restURL.lastIndexOf('/servlet') == restURL.length - 8) {
				restURL = restURL.substring(0, restURL.length - 8);
			}
			restURL = restURL + '/rest/' + WWP_replaceAll(this.control.DataListProc, ".", "/");
			this.ajaxCall = $.ajax({
				async: true,
				type: "POST",
				url: restURL,
				headers: {
					"Authorization": this.control.GAMOAuthToken
				},
				contentType: "application/json",
				data: '{"DDOName": "' + (this.control.IsM ? 'DDO_' + this.control.MName : this.control.ControlName) + '", "SearchTxt": "' + searchTxt + '", "SearchTxtTo": "' + searchTxtTo + '"}',
				success: function (result) {
					result.Options = JSON.parse(result.OptionsJson);
					result.OptionsDesc = JSON.parse(result.OptionsDescJson);
					result.OptionIndexes = JSON.parse(result.OptionIndexesJson);
					if (thisC.control.DataListType == "Dynamic (with fixedValues)") {
						thisC.procResult = result;
					}
					thisC.ProcessProcResult(result, liDropDownItem, filterInput, filterInputTo, originalSearchTxt, typing);
					thisC.setSectionGridMinHeight(true);
				},
				error: function (jqXHR, textStatus, errorThrown) {
					console.log("jqXHR statusCode" + jqXHR.statusCode());
					console.log("textStatus " + textStatus);
					console.log("errorThrown " + errorThrown);
					if (jqXHR != null && jqXHR.status == 401) {
						//401 (Not Authorized) -> Reload page so the user will be redirected to login
						window.location.reload();
					}
				}
			});
		} else {
			liDropDownItem.innerHTML = '';
		}
	}

	this.addLoadingOption = function (liDropDownItem) {
		liDropDownItem.innerHTML = '';
		var aDropDownItem2 = document.createElement('a');
		var spanDropDownItem2 = document.createElement('span');
		spanDropDownItem2.innerHTML = gx.getMessage(this.control.LoadingData);

		//disable onclick close
		$(aDropDownItem2).bind("click", function (e) {
			e.preventDefault();
			e.stopPropagation();
		});

		aDropDownItem2.setAttribute("href", DDO_GetLocalURL());
		//to keep URLs
		if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
			aDropDownItem2.setAttribute("data-toggle", "dropdown");
		}

		aDropDownItem2.className = "image";
		if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
			var imgDropDownItem2 = document.createElement('i');
			imgDropDownItem2.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["fildata"]);
			aDropDownItem2.appendChild(imgDropDownItem2);
		}
		else {
			var imgDropDownItem2 = document.createElement('img');
			imgDropDownItem2.setAttribute("src", gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["OptionFilteringData"]));
			aDropDownItem2.appendChild(imgDropDownItem2);
		}

		aDropDownItem2.appendChild(spanDropDownItem2);
		liDropDownItem.appendChild(aDropDownItem2);
	}

	this.setSectionGridMinHeight = function (setMinHeight) {
		var ul = $("#" + this.containerToDrawName)[0].children[$("#" + this.containerToDrawName)[0].children.length - 1];
		var overflowEl = ul.parentElement;
		for (var i = 0; i < 18; i++) {
			if (overflowEl == null || !$(overflowEl).is('table.table-responsive') && $(overflowEl).css('overflow') == 'auto') {
				break;
			}
			else {
				overflowEl = overflowEl.parentElement;
			}
		}
		if ($(overflowEl).css('overflow') == 'auto') {
			if (setMinHeight) {
				var minH = $(ul).offset().top + $(ul).outerHeight() + 12 - $(overflowEl).offset().top;//12 = shadow
				$(overflowEl).css({ 'min-height': minH + "px" });
				if (hasScroll($(overflowEl), false)) {
					//has horizontal scroll visible
					minH += getScrollBarWidth();
					$(overflowEl).css({ 'min-height': minH + "px" });
				}
				if (overflowEl.tagName == 'TABLE' && $(overflowEl).height() < minH) {
					$(overflowEl).css({ 'margin-bottom': (minH - $(overflowEl).height()) + "px" });
					$(overflowEl).data('bottomSet', 'T');
				}
				var th = $(ul).closest('th');
				if (th.css('position') == 'relative' && th.data('zIndexIncresed') == null && parseInt(th.css('z-index')) > 0) {
					th.css('z-index', (parseInt(th.css('z-index')) + 100));
					th.data('zIndexIncresed', 'T');
				}
			}
			else {
				$(overflowEl).css({ 'min-height': "" });
				var th = $(ul).closest('th');
				if (th.data('zIndexIncresed') == 'T') {
					th.css('z-index', (parseInt(th.css('z-index')) - 100));
					th.data('zIndexIncresed', null);
				}
				if ($(overflowEl).data('bottomSet') == 'T') {
					$(overflowEl).css({ 'margin-bottom': '' });
					$(overflowEl).data('bottomSet', null);
				}
			}
		}
	}

	this.shiftPageFooter = function (shiftFooter) {

		var ul = $("#" + this.containerToDrawName)[0].children[$("#" + this.containerToDrawName)[0].children.length - 1];
		var tableWithFooter = $(ul).closest('.CellTableContentWithFooter');
		if (tableWithFooter.length == 1) {
			tableWithFooter.parent().css('margin-bottom', '')
			if (shiftFooter) {
				var marginB = $(ul).offset().top + $(ul).height() - (tableWithFooter.offset().top + tableWithFooter.height())
				if (marginB > 0) {
					tableWithFooter.parent().css('margin-bottom', (marginB + 5) + 'px')
				}
			}
		}
	}

	this.getOptionCount = function (optionVal, liDropDownItem, filterInput, filterInputTo, typing) {

		this.getOptionCountResult = 0;
		try {
			var originalSearchTxt = '';
			var searchTxt = '';
			var searchTxtTo = '';
			var updateList = true;
			if (this.control.DataListType != "Dynamic (with fixedValues)") {
				if (this.control.IncludeFilter) {
					if (filterInput != null) {
						originalSearchTxt = optionVal;
						searchTxt = originalSearchTxt;
						if (this.control.FilterType == 'Character' && searchTxt.length < this.control.DataListUpdateMinimumCharacters) {
							updateList = false;
						}
						if (searchTxt != null && searchTxt.length > 0) {
							searchTxt = WWP_replaceAll(searchTxt, '\\', '[\\]');
							searchTxt = WWP_replaceAll(searchTxt, '"', '\\"');
							searchTxt = WWP_replaceAll(searchTxt, '[\\]', '\\\\');
						}
					}
					else {
						searchTxt = '';
						if (this.control.FilterType == 'Character' && this.control.DataListUpdateMinimumCharacters > 0) {
							updateList = false;
						}
					}
					if (this.control.FilterIsRange) {
						searchTxtTo = optionVal;
						if (!updateList && searchTxtTo.length >= this.control.DataListUpdateMinimumCharacters) {
							updateList = true;
						}
						if (searchTxtTo != null && searchTxtTo.length > 0) {
							searchTxtTo = WWP_replaceAll(searchTxtTo, '\\', '[\\]');
							searchTxtTo = WWP_replaceAll(searchTxtTo, '"', '\\"');
							searchTxtTo = WWP_replaceAll(searchTxtTo, '[\\]', '\\\\');
						}
					}
				}
			}
			if (updateList) {
				var thisC = this;
				var restURL = window.location.href;
				restURL = restURL.substring(0, restURL.length - window.location.search.length);
				restURL = restURL.substring(0, restURL.lastIndexOf('/'));
				var lastIndexOfQ = restURL.lastIndexOf('?');
				if (lastIndexOfQ >= 0) {
					restURL = restURL.substring(0, lastIndexOfQ);
					restURL = restURL.substring(0, restURL.lastIndexOf('/'));
				}
				if (restURL.lastIndexOf('/servlet') == restURL.length - 8) {
					restURL = restURL.substring(0, restURL.length - 8);
				}
				restURL = restURL + '/rest/' + WWP_replaceAll(this.control.DataListProc, ".", "/");
				this.ajaxCall2 = $.ajax({
					async: false,
					type: "POST",
					url: restURL,
					headers: {
						"Authorization": this.control.GAMOAuthToken
					},
					contentType: "application/json",
					data: '{"DDOName": "' + (this.control.IsM ? 'DDO_' + this.control.MName : this.control.ControlName) + '", "SearchTxt": "' + searchTxt + '", "SearchTxtTo": "' + searchTxtTo + '"}',
					success: function (result) {
						var options = JSON.parse(result.OptionsJson);
						var optionIndexes = JSON.parse(result.OptionIndexesJson);

						for (var index = 0; index < options.length; index += 1) {
							if (options[index] == optionVal) {
								thisC.getOptionCountResult = optionIndexes[index];
								return;
							}
						}
					},
					error: function (jqXHR, textStatus, errorThrown) {
						console.log("jqXHR statusCode" + jqXHR.statusCode());
						console.log("textStatus " + textStatus);
						console.log("errorThrown " + errorThrown);
						if (jqXHR != null && jqXHR.status == 401) {
							//401 (Not Authorized) -> Reload page so the user will be redirected to login
							window.location.reload();
						}
					}
				});
			} else {
				liDropDownItem.innerHTML = '';
			}
		} catch (e) {
		}
		return this.getOptionCountResult;
	}

	this.ProcessProcResult = function (result, liDropDownItem, filterInput, filterInputTo, searchTxt, typing) {
		if (typing && searchTxt == '') {
			typing = false;
		}
		var divFilters = document.createElement('div');
		divFilters.className = "FilterOptions";
		var ulFilters = document.createElement('ul');
		var selectedFound = false;
		var hasSelected = !this.IsValueEmpty(this.control.SelectedValue);
		var selectedFoundCount = 0;
		var selectedValue_getAUX2;
		var selectedText_getAUX2;
		if (this.control.AllowMultipleSelection) {
			divFilters.className = "FilterOptions FilterOptionsMultiSelection";
			selectedValue_getAUX2 = this.SelectedValue_getAUX.slice(0);
			if (this.SelectedValue_getAUX.length == this.SelectedText_getAUX.length) {
				selectedText_getAUX2 = this.SelectedText_getAUX.slice(0);
			} else {
				selectedText_getAUX2 = this.SelectedValue_getAUX.slice(0);
			}
		}
		if (hasSelected && !typing && result.Options != null && result.Options.length > 0) {
			if (this.control.AllowMultipleSelection) {
				hasSelected = (selectedValue_getAUX2.length > 0);
				for (var k = 0; k < selectedValue_getAUX2.length; k += 1) {
					selectedFound = false;
					for (var index = 0; index < result.Options.length; index += 1) {
						if (result.Options[index] == selectedValue_getAUX2[k]) {
							selectedFound = true;
							break;
						}
					}
					if (selectedFound) {
						selectedValue_getAUX2.splice(k, 1);
						selectedText_getAUX2.splice(k, 1);
						k--;
					}
				}
				selectedFound = (selectedValue_getAUX2.length == 0);
			} else {
				for (var index = 0; index < result.Options.length; index += 1) {
					if (result.Options[index] == this.control.SelectedValue) {
						selectedFound = true;
						break;
					}
				}
			}
		}
		var ulRedrawn = false;
		if (result.Options == null) {
			result.Options = [];
		}
		var visibleItems = 0;
		if ((result.Options != null && result.Options.length > 0) || (hasSelected && !selectedFound && !typing)) {
			if (result.OptionsDesc == null || result.OptionsDesc.length == 0) {
				result.OptionsDesc = result.Options;
			}
			var index = 0;
			if (hasSelected && !selectedFound && !typing) {
				if (this.control.AllowMultipleSelection) {
					index = (selectedValue_getAUX2.length * -1);
				} else {
					index = -1;
				}
				visibleItems = index;
			}
			var positionToInsert = 0;
			for (; index < result.Options.length; index += 1) {
				visibleItems += 1;
				var liFilters = document.createElement('li');
				var aLiFilters = document.createElement('a');
				var spanLiFilters = document.createElement('span');
				var optionVal, optionText;
				if (index < 0) {
					if (this.control.AllowMultipleSelection) {
						optionVal = selectedValue_getAUX2[0];
						optionText = selectedText_getAUX2[0];
						selectedValue_getAUX2.splice(0, 1);
						selectedText_getAUX2.splice(0, 1);
					} else {
						optionVal = this.control.SelectedValue;
						optionText = (this.control.SelectedText != null && this.control.SelectedText != '' ? this.control.SelectedText : optionVal);
					}
				} else {
					optionVal = result.Options[index];
					optionText = result.OptionsDesc[index].trim();
				}
				var isDynWithFixedValues = (this.control.DataListType == "Dynamic (with fixedValues)");
				var isSelected = false;
				if (this.control.AllowMultipleSelection) {
					for (var k = 0; k < this.SelectedValue_getAUX.length; k += 1) {
						if (this.SelectedValue_getAUX[k] == optionVal) {
							isSelected = true;
							break;
						}
					}
				} else {
					if (hasSelected && !isDynWithFixedValues || isDynWithFixedValues && searchTxt == '') {
						isSelected = (optionVal == this.control.SelectedValue);
					}
				}
				var title = optionText + '';
				var addDots = false;
				if (title.length > 100) {
					title = title.substring(0, 97).trim();
					addDots = true;
				}
				if (isSelected) {
					if (addDots) {
						title += '...';
					}
					if (this.control.AllowMultipleSelection) {
						aLiFilters.setAttribute('sel', 'T');
					} else {
						title = '<strong>' + encodeHtmlEntities(title) + '</strong>';
						if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
							var imgSelOption = document.createElement('i');
							imgSelOption.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons["selo"]);
							aLiFilters.appendChild(imgSelOption);
						}
						else {
							$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons["SelectedOption"]) + ")");
							$(aLiFilters).css("background-repeat", "no-repeat");
							$(aLiFilters).css("background-position", "10px 60%");
						}
					}
				} else if (searchTxt != null && searchTxt.length > 0 && !this.control.FilterIsRange && (this.control.FilterType == 'Character' || isDynWithFixedValues)) {
					var ind = WWP_removeAccents(title.toLowerCase()).indexOf(WWP_removeAccents(searchTxt.toLowerCase()));
					if (ind >= 0) {
						title = encodeHtmlEntities(title.substring(0, ind)) + '<strong>' + encodeHtmlEntities(title.substring(ind, ind + searchTxt.length)) + '</strong>' + encodeHtmlEntities(title.substring(ind + searchTxt.length));
					} else if (isDynWithFixedValues) {
						visibleItems -= 1;
						$(liFilters).css("display", "none");
					}
					if (addDots) {
						title += '...';
					}
				} else {
					title = encodeHtmlEntities(title);
				}
				if (index < 0) {
					var auxCount = 0;
					if (result.Options.length == 50) {
						auxCount = this.getOptionCount(optionVal, liDropDownItem, filterInput, filterInputTo, typing);
					}
					title = title.trim() + ' (' + auxCount + ')';
				} else if (result.OptionIndexes.length == result.Options.length) {
					title = title.trim() + ' (' + result.OptionIndexes[index] + ')';
				}
				spanLiFilters.innerHTML = title;

				aLiFilters.setAttribute("href", DDO_GetLocalURL());
				//to keep URLs
				if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
					aLiFilters.setAttribute("data-toggle", "dropdown");
				}

				if (this.control.AllowMultipleSelection) {
					if (this.control.DropDownOptionsTitleSettingsIcons_IsFI) {
						var imgFI = document.createElement('i');
						imgFI.setAttribute("class", this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "muls" : "mul"]);
						aLiFilters.appendChild(imgFI);
					}
					else {
						$(aLiFilters).css("background-image", "url(" + gx.util.resolveUrl(this.control.DropDownOptionsTitleSettingsIcons[isSelected ? "MultiselSelOption" : "MultiselOption"]) + ")");
						$(aLiFilters).css("background-repeat", "no-repeat");
						$(aLiFilters).css("background-position", "5px 60%");
					}
				}

				var thisC = this;
				$(aLiFilters).bind("click", function (ek, txt) {
					return function () {
						if (thisC.control.AllowMultipleSelection) {
							var isSelected = (this.getAttribute('sel') == 'T');
							if (isSelected) {
								this.setAttribute('sel', '');
								var selItemIndex = -1;
								for (var k = 0; k < thisC.SelectedValue_getAUX.length; k += 1) {
									if (thisC.SelectedValue_getAUX[k] == ek) {
										selItemIndex = k;
										break;
									}
								}
								if (selItemIndex >= 0) {
									thisC.SelectedValue_getAUX.splice(selItemIndex, 1);
									thisC.SelectedText_getAUX.splice(selItemIndex, 1);
								}
							} else {
								this.setAttribute('sel', 'T');
								thisC.SelectedValue_getAUX.push(ek);
								thisC.SelectedText_getAUX.push(txt);
							}
							if (thisC.control.DropDownOptionsTitleSettingsIcons_IsFI) {
								$(this).find('i')[0].setAttribute("class", thisC.control.DropDownOptionsTitleSettingsIcons[isSelected ? "mul" : "muls"]);
							}
							else {
								$(this).css("background-image", "url(" + gx.util.resolveUrl(thisC.control.DropDownOptionsTitleSettingsIcons[isSelected ? "MultiselOption" : "MultiselSelOption"]) + ")");
							}
						} else {
							thisC.SetActiveEventKey('<#Filter#>');
							thisC.SetFilteredText($(filterInput).val());
							thisC.SetSelectedValue(ek);
							thisC.SetSelectedText(txt);
							if (thisC.control.FilterIsRange) {
								thisC.SetFilteredTextTo($(filterInputTo).val());
							}
						}
					}
				}(optionVal, optionText));


				if (this.control.AllowMultipleSelection) {
					$(aLiFilters).bind("click", function (e) {
						e.preventDefault();
						e.stopPropagation();
					});
				} else if (this.control.OnOptionClicked) {
					$(aLiFilters).bind("click", this.control.OnOptionClicked);
				}

				aLiFilters.appendChild(spanLiFilters);
				liFilters.appendChild(aLiFilters);
				if (isSelected && (this.control.AllowMultipleSelection
									|| ulFilters.childNodes.length > 0 && index >= this.minIndexForSelectedItemFirst)) {
					ulFilters.insertBefore(liFilters, ulFilters.childNodes[positionToInsert]);
					if (this.control.AllowMultipleSelection) {
						positionToInsert++;
					}
				} else {
					ulFilters.appendChild(liFilters);
				}
			}
		}
		if (visibleItems == 0) {
			var liFilters = document.createElement('li');
			var aLiFilters = document.createElement('a');
			var spanLiFilters = document.createElement('span');
			if (result.Options != null && result.Options.length == 0 && result.OptionsDesc != null && result.OptionsDesc.length == 1) {
				spanLiFilters.innerHTML = result.OptionsDesc[0];
			} else {
				spanLiFilters.innerHTML = gx.getMessage(this.control.NoResultsFound);
			}

			aLiFilters.setAttribute("href", DDO_GetLocalURL());
			//to keep URLs
			if (!navigator.appVersion.toLowerCase().match('msie 8') && !navigator.appVersion.toLowerCase().match('msie 7') && !navigator.appVersion.toLowerCase().match('msie 9')) {
				aLiFilters.setAttribute("data-toggle", "dropdown");
			}

			$(aLiFilters).bind("click", function (e) {
				e.preventDefault();
				e.stopPropagation();
			});

			aLiFilters.appendChild(spanLiFilters);
			liFilters.appendChild(aLiFilters);
			ulFilters.appendChild(liFilters);
		}
		if (!ulRedrawn) {
			liDropDownItem.innerHTML = '';
			divFilters.appendChild(ulFilters);
			liDropDownItem.appendChild(divFilters);
		}
		if (this.control.TitleControlAlign == 'Automatic') {
			dropdownAutoPosition(null, "#" + this.containerToDrawName, this.m_UlDropDown, this.m_triggerButton);
		} else if (this.control.TitleControlAlign == 'Left') {
			$(liDropDownItem).parent().css("margin-left", "-" + ($(liDropDownItem).parent().outerWidth() - $($(liDropDownItem).parent().parent().children()[0]).outerWidth()) + "px");
		}
	}

	this.SetActiveEventKey = function (eventKey) {
		if (eventKey == '<#Filter#>') {
			this.control.SelectedFixedFilter = '';
			if (this.control.IsM) {
				this.control.MControl.SelectedColumnFixedFilter = '';
				this.control.MControl.UpdateColPropValueFromDDOs('SelectedFixedFilter');
			}
		}
		this.control.ActiveEventKey = eventKey;
		if (this.control.IsM) {
			this.control.MControl.ActiveEventKey = eventKey;
			this.control.MControl.SelectedColumn = this.control.MName;
		}
	}

	this.SetActiveEventKeyFixedFilter_M = function () {
		var eventKey = '<#Filter#>';
		this.control.ActiveEventKey = eventKey;
		this.control.MControl.ActiveEventKey = eventKey;
		this.control.MControl.SelectedColumn = this.control.MName;

		this.control.MControl.SelectedColumnFixedFilter = this.control.SelectedFixedFilter;
		this.control.MControl.UpdateColPropValueFromDDOs('SelectedFixedFilter');
	}

	this.SetFilteredText = function (filteredText) {
		this.control.FilteredText_get = filteredText != null ? filteredText : "";
		this.control.FilteredText_set = this.control.FilteredText_get;
		this.control.FilteredText = this.control.FilteredText_get;
		if (this.control.IsM) {
			this.control.MControl.FilteredText_get = this.control.FilteredText_get;
			this.control.MControl.UpdateColPropValueFromDDOs('FilteredText_set');
		}
	}

	this.SetFilteredTextTo = function (filteredText) {
		this.control.FilteredTextTo_get = filteredText != null ? filteredText : "";
		this.control.FilteredTextTo_set = this.control.FilteredTextTo_get;
		this.control.FilteredTextTo = this.control.FilteredTextTo_get;
		if (this.control.IsM) {
			this.control.MControl.FilteredTextTo_get = this.control.FilteredTextTo_get;
			this.control.MControl.UpdateColPropValueFromDDOs('FilteredTextTo_set');
		}
	}

	this.SetSelectedValue = function (selectedValue) {
		this.control.SelectedValue_get = selectedValue != null ? selectedValue : "";
		this.control.SelectedValue_set = this.control.SelectedValue_get;
		this.control.SelectedValue = this.control.SelectedValue_get;
		if (this.control.IsM) {
			this.control.MControl.SelectedValue_get = this.control.SelectedValue_get;
			this.control.MControl.UpdateColPropValueFromDDOs('SelectedValue_set');
		}
	}

	this.SetSelectedText = function (selectedValue) {
		this.control.SelectedText_get = selectedValue != null ? selectedValue : "";
		this.control.SelectedText_set = this.control.SelectedText_get;
		this.control.SelectedText = this.control.SelectedText_get;
		if (this.control.IsM) {
			this.control.MControl.SelectedText_get = this.control.SelectedText_get;
			this.control.MControl.UpdateColPropValueFromDDOs('SelectedText_set');
		}
	}

	this.ShowHideControl = function () {
		if (this.control.Visible == "false") {
			$("#" + this.containerToDrawName).css("display", "none");
		} else {
			$("#" + this.containerToDrawName).css("display", "inline-block");
		}
	}
}

function encodeHtmlEntities(string) {
	var encodedStr = string.replace(/[\u00A0-\u9999<>\&]/gim, function (i) {
		return '&#' + i.charCodeAt(0) + ';';
	});
	return encodedStr;
}

function isTitleControlAlignLeft(containerToDrawName, ulDropDown, triggerButton) {
	var actualThElement = $(containerToDrawName).parent().parent().parent();
	var trElement = actualThElement.parent();
	var totalEmptyColumns = 0;
	var totalColumns = 0;
	var actualThIndex = actualThElement.index();
	var actualColumnNumber = -1;
	for (var thIndex = 0; thIndex < trElement.children().length; thIndex += 1) {
		var thElement = trElement.children()[thIndex];
		if (actualThIndex == thIndex) {
			actualColumnNumber = totalColumns;
		}
		if ($(thElement).css("display") != "none") {
			totalColumns++;
			if (thElement.innerHTML.trim() == "" || thElement.innerHTML.trim() == "&nbsp;") {
				totalEmptyColumns++;
			}
		}
	}
	totalColumns = totalColumns - totalEmptyColumns;
	actualColumnNumber = actualColumnNumber - totalEmptyColumns;
	if (actualColumnNumber >= 0 && (totalColumns > 5 && actualColumnNumber >= totalColumns - 2
											|| totalColumns > 2 && actualColumnNumber == totalColumns - 1)) {
		return true;
	} else {
		return false;
	}
}

function dropdownAutoPosition(dropdownSelector, ulDropDown, triggerButton) {
	if (isTitleControlAlignLeft(dropdownSelector, ulDropDown, triggerButton)) {
		dropdownInvertPosition(ulDropDown, triggerButton);
	}
}

function dropdownInvertPosition(ulDropDown, triggerButton) {
	var marginDirection = ($('body').css('direction') == 'rtl' ? "right" : "left");
	$(ulDropDown).css("margin-" + marginDirection, "-" + ($(ulDropDown).outerWidth() - $(triggerButton).outerWidth()) + "px");
	$(ulDropDown).css("margin-" + marginDirection, "-" + ($(ulDropDown).outerWidth() - $(triggerButton).outerWidth()) + "px");
}
function cleanJsonString(s) {
	s = s.replace(/\\n/g, "\\n")
	   .replace(/\\'/g, "\\'")
	   .replace(/\\"/g, '\\"')
	   .replace(/\\&/g, "\\&")
	   .replace(/\\r/g, "\\r")
	   .replace(/\\t/g, "\\t")
	   .replace(/\\b/g, "\\b")
	   .replace(/\\f/g, "\\f");
	// remove non-printable and other non-valid JSON chars
	s = s.replace(/[\u0000-\u0019]+/g, "");
	return s;
}

/*   EDITED MANUALLY WORKWITHPLUS - START   */
var cookieHash = {};
//Create Cookie Function
function createCookie(name, value, days) {
	cookieHash[name] = value;
}

//Read Cookie Function
function readCookie(name) {
	return cookieHash[name];
}

function DDO_GetLocalURL() {
	return "#";
}
/*   EDITED MANUALLY WORKWITHPLUS - END   */


function WWP_FixBooleanProperty(propVal) {
	return (propVal == true || propVal == 'true');
}

//Erase Cookie Function
function eraseCookie(name) {
	createCookie(name, "", -1);
}

//Remove Classes Function
function removeClass(elem, cls) {
	var str = " " + elem.className + " ";
	elem.className = str.replace(" " + cls, "").replace(/^\s+/g, "").replace(/\s+$/g, "");
}

//EndsWith Classes Function
function DV_endsWith(str, strToSearch) {
	return str != null && strToSearch != null && str.length >= strToSearch.length && str.substring(str.length - strToSearch.length) == strToSearch;
}

//Has Classes Function
function hasClass(elem, cls) {
	var str = " " + elem.className + " ";
	var testCls = " " + cls + " ";
	return (str.indexOf(testCls) != -1);
}

//Add Classes Function
function addClass(elem, cls) {
	var oldCls = elem.className;
	if (oldCls) {
		oldCls += " ";
	}
	elem.className = oldCls + cls;
}

function WWP_removeAccents(str) {
	if (str == null) {
		return '';
	} else {
		return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "")
	}
}

var scrollWidth = null;
function getScrollBarWidth() {
	if (scrollWidth == null) {
		var inner = document.createElement('p');
		inner.style.width = "100%";
		inner.style.height = "200px";

		var outer = document.createElement('div');
		outer.style.position = "absolute";
		outer.style.top = "0px";
		outer.style.left = "0px";
		outer.style.visibility = "hidden";
		outer.style.width = "200px";
		outer.style.height = "150px";
		outer.style.overflow = "hidden";
		outer.appendChild(inner);

		document.body.appendChild(outer);
		var w1 = inner.offsetWidth;
		outer.style.overflow = 'scroll';
		var w2 = inner.offsetWidth;
		if (w1 == w2) w2 = outer.clientWidth;

		document.body.removeChild(outer);

		scrollWidth = (w1 - w2);
	}
	return scrollWidth;
}

function hasScroll(elem, vertical) {
	if (elem == null || elem.length != 1) {
		return false;
	}
	if (vertical) {
		return (elem[0].scrollHeight - elem.height()) > 0;
	} else {
		return (elem[0].scrollWidth + getScrollWidthIfVisible(elem, true) - elem.width()) > 0;
	}
}

function getScrollWidthIfVisible(elem, vertical) {
	return hasScroll(elem, vertical) ? getScrollBarWidth() : 0;
}
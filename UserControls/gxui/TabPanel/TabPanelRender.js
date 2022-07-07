/*
 * GxUI Library 2.0.1
 * Copyright (c) 2009, Artech
 * All rights reserved.
 * 
 * GxUI Library is freely distributable under the terms of the BSD license.
 * 
 */

/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />

/**
* @class gxui.TabPanel
* A basic tab container. Wraps Ext.tab.Panel so it can be used from GeneXus.
*
* The main purpose of this control is to enable the programmer to create a tabbed dialog in design time. However, it is possible to add
* tabs in runtime, using the {@link #OpenTab} method, by providing the HTML code that will be rendered inside the newly created tab.
*
* #Basic Samples#
* By programing the code below in a Web Panel you will get the behavior in shown in the image.
*
*		Event 'open tab'
*			gxui_tabpanel1.OpenTab("tabPanel4", "Tab4", "<html><head><title>Example</title></head><body><p>Tab Content</p></body></html>")
*			gxui_tabpanel1.SelectTab("tabPanel4")
*		EndEvent
*		
*		Event 'hide tab'
*			gxui_tabpanel1.HideTab(2) //hide tab by index
*			gxui_tabpanel1.SelectTab("tabPanel2")
*		EndEvent
*
* {@img tabPanel/tabPanel3.png}
*
* Other features you can develop with this control.
*
* Load a Web Component with its logic in a tab:
*
*		Event Start
*			gxui_tabpanel1.SelectTab("tabPanel1")
*			WebComp1.Object = TabPanelWebComp.Create()
*		EndEvent
* {@img tabPanel/tabPanel5.png}
*		
* Move between tabs:
*
*		Event 'show tab'
*			gxui_tabpanel1.OpenTab("tabPanel3", "Tab3", "") //You must open it first in case it could be closed
*			gxui_tabpanel1.ShowTabById("tabPanel3")
*			gxui_tabpanel1.SelectTab("tabPanel3")
*		EndEvent
*
* Or change the title of a tab:
*
*		Event 'set tab title'
*			gxui_TabPanel1.SetTabTitle("tabPanel2", format("Modified title for tab tabPanel2"))
*		EndEvent
*	
*
* Also you can do something a bit more complex like manage grids and attributes in it.
* {@img tabPanel/tabPanel2.png}
*
* #More information:#
* For more examples please see the [online KB][1].
* [1]: http://xev2.genexusserver.com/gxserver/action.aspx?1,RSSReader2.0:0:c9584656-94b6-4ccd-890f-332d11fc2c25:31
*
*/
Ext.define('gxui.TabPanel', {
	extend: 'gxui.UserControl',

	initialize: function () {
		this.callParent(arguments);

		this.HandleUniqueId = true;
	},

	//Private members
	m_tabPanel: null,
	m_designTabs: [],
	m_activeTab: 0,

	// Databinding for property Data
	SetRunTimeTabs: function (data) {
		if (data) {
			if (Ext.isArray(data))
				this.RunTimeTabs = data;
			else
				this.RunTimeTabs = [data];
		}
		else
			this.RunTimeTabs = [];
	},

	// Databinding for property Data
	GetRunTimeTabs: function () {
		return this.RunTimeTabs;
	},

	onRender: function () {
		this.m_designTabs = Ext.JSON.decode(this.DesignTimeTabs);
		this.RunTimeTabs = [];

		var tabCount = 0;
		if (this.m_designTabs && this.m_designTabs.length)
			tabCount = this.m_designTabs.length;

		if (tabCount > 0) {
			this.displayTabPanels();
			this.m_tabPanel = Ext.create('Ext.tab.Panel', this.getConfig());

			// Register this User Control as a container. Each tab of the tabpanel control is registered
			// as an individual container.
			this.registerAsContainer();
		}
	},

	onRefresh: function () {
		var setActiveTab = false;
		Ext.each(this.getTabPanelsList(), function (tab, index, allTabs) {
			this.m_tabPanel.add(tab);
			this.registerAsContainer(tab);
			setActiveTab = true;
		}, this);

		if (setActiveTab) {
			this.m_tabPanel.setActiveTab(this.m_activeTab);
		}
	},

	onAfterRender: function () {
		this.m_tabPanel.setActiveTab(this.m_activeTab);
		this.m_tabPanel.on('tabchange', this.handlers.tabChanged, this);
	},

	onDestroy: function () {
		if (this.m_tabPanel) {
			this.m_tabPanel.items.each(function (tab) {
				this.unregisterCt(tab);
			}, this);
		}
		this.callParent(arguments);
	},

	getUnderlyingControl: function () {
		return this.m_tabPanel;
	},

	addToParent: function () {
		return gxui.CBoolean(this.AddToParentGxUIControl);
	},

	getConfig: function () {
		var config = {
			id: this.getUniqueId(),
			cls: this.Cls,
			tabPosition: this.TabPosition || "top",
			deferredRender: false,
			border: gx.lang.gxBoolean(this.Border) ? undefined : 0,
			frame: gx.lang.gxBoolean(this.Frame),
			autoWidth: gxui.CBoolean(this.AutoWidth),
			autoHeight: gxui.CBoolean(this.AutoHeight),
			enableTabScroll: (this.TabPosition == "top") ? gxui.CBoolean(this.EnableTabScroll) : false,
			minTabWidth: parseInt(this.MinTabWidth),
			items: this.getTabPanelsList(),
			listeners: {
				'activate': this.handlers.tabItemActivated,
				'deactivate': this.handlers.tabItemDeactivated,
				'remove': this.handlers.tabItemClosed,
				'beforeremove': this.handlers.tabItemBeforeClosed,
				'afterrender': this.fixAutoDimensions,
				'add': this.fixAutoDimensions,
				scope: this
			}
		};

		if (!gxui.CBoolean(this.AutoWidth))
			config.width = parseInt(this.Width);

		if (!gxui.CBoolean(this.AutoHeight))
			config.height = parseInt(this.Height);

		return config;
	},

	getTabPanelsList: function () {
		var rawTabs = (this.RunTimeTabs && this.RunTimeTabs.length) ? this.m_designTabs.concat(this.RunTimeTabs) : this.m_designTabs;
		var tabPanels = [];
		Ext.each(rawTabs, function (tab, index, allTabs) {
			var panel;
			if (index >= this.m_designTabs.length)
				tab.isRuntimeTab = true;

			if (!tab.rendered) {
				if (!tab.isRuntimeTab) {
					var titleEl = Ext.get(this.getChildContainer("Title" + tab.id));
					if (titleEl) {
						tab.Name = titleEl.dom.innerHTML;
						titleEl.dom.parentNode.removeChild(titleEl.dom);
					}
				}

				if (tab.isRuntimeTab) {
					if (!tab.HTML) {
						tab.InternalName = null;
					}
				}
				else {
					tab.InternalName = tab.id;
				}

				if (!tab.HTML) {
					Ext.fly(this.getChildContainer(tab.id)).setStyle('display', 'inline-block');
				}

				if (tab.InternalName) {
					var layout = tab.layout || this.Layout;
					var config = {
						id: this.getTabUniqueId(tab.InternalName),
						layout: layout == "default" ? undefined : layout,
						contentEl: !tab.HTML ? this.getChildContainer(tab.id) : undefined,
						bodyCls: "gxui-noreset",
						html: tab.HTML,
						title: tab.Name,
						closable: (tab.isRuntimeTab) ? (tab.closable !== undefined ? gxui.CBoolean(tab.closable) : true) : gxui.CBoolean(tab.closable),
						autoScroll: tab.autoScroll || (layout == 'fit' ? false : true),
						autoWidth: gxui.CBoolean(this.AutoWidth),
						autoHeight: gxui.CBoolean(this.AutoHeight),
						listeners: {
							'activate': this.handlers.tabItemActivated,
							'deactivate': this.handlers.tabItemDeactivated,
							'render': this.handlers.tabItemRendered,
							'afterrender': this.fixAutoDimensions,
							'add': this.fixAutoDimensions,
							scope: this
						}
					};

					if (this.TabCls)
						config.cls = this.TabCls;

					// WA to support AutoHeight
					if (gxui.CBoolean(this.AutoHeight))
						if (!tab.HTML)
							Ext.fly(this.getChildContainer(tab.id)).setHeight('auto');

					panel = Ext.create('Ext.panel.Panel', config);
					tab.rendered = true;
					tabPanels.push(panel);
				}
				else
					return;
			}

			if (gxui.CBoolean(tab.selected) || gxui.CBoolean(tab.Selected))
				this.m_activeTab = panel;

		}, this);

		return tabPanels;
	},

	displayTabPanels: function () {
		Ext.each(this.m_designTabs, function (tab, index, allTabs) {
			Ext.get(this.getChildContainer(tab.id)).setDisplayed(true)
		}, this);
	},

	registerAsContainer: function (t) {
		if (t) {
			this.registerCt(Ext.get(t.contentEl || t.body).dom, t.add, t.doLayout, t);
		}
		else {
			Ext.each(this.m_tabPanel.items.items,
				function (tab, index, allTabs) {
					this.registerCt(Ext.get(tab.contentEl || tab.body).dom, tab.add, tab.doLayout, tab);
				},
			this);
		}
	},

	fixAutoDimensions: function (panel, onlyThisTab) {
		if (!panel.fixingAutoDims) {
			panel.fixingAutoDims = true;
			if (panel.rendered) {
				if (!onlyThisTab) {
					Ext.each(this.m_tabPanel.items.items, function (tab, index, allTabs) {
						this.fixAutoDimensions(tab, true);
					}, this);
				}

				if (gxui.CBoolean(this.AutoWidth)) {
					panel.el.setWidth('auto');
					panel.body.setWidth('auto');
					if (panel.header && (panel.headerPosition == "top" || panel.headerPosition == "bottom")) {
						Ext.defer(panel.header.setWidth, 50, panel.header, ['auto']);
					}
				}

				if (gxui.CBoolean(this.AutoHeight)) {
					panel.el.setHeight('auto');
					panel.body.setHeight('auto');
					if (panel.header && (panel.headerPosition == "top" || panel.headerPosition == "bottom")) {
						panel.body.setStyle('margin-bottom', Ext.dom.AbstractElement.addUnits(panel.header.getHeight(), "px"));
					}
				}
			}

			if (panel == this.m_tabPanel) {
				panel.getTabBar().doLayout();
			}
			panel.fixingAutoDims = false;
		}
	},

	handlers: {
		tabChanged: function (tab, tabItem) {
			/**
			* @event TabChanged
			* Fires when the active tab is changed.
			* The following properties are set when the event is fired:
			*
			* - {@link #ActiveTabId}
			*
			*/
			if (this.TabChanged) {
				this.TabChanged();
			}
		},

		tabItemRendered: function (panel) {
			panel.tab.on('click', this.handlers.tabStripClick, this);
		},

		tabItemActivated: function (tabItem) {
			if (gxui.CBoolean(this.AutoHeight) || gxui.CBoolean(this.AutoWidth)) {
				tabItem.el.select('.x-panel').each(function (panelEl) {
					var innerPanel = Ext.getCmp(panelEl.dom.id);
					var initialConfig = innerPanel.initialConfig;
					if (!innerPanel.ownerCt && (initialConfig.autoWidth || initialConfig.autoHeight)) {
						innerPanel.doLayout();
					}
				});
				Ext.defer(tabItem.ownerCt.doLayout, 50, tabItem.ownerCt);
			}
			this.ActiveTabId = tabItem.id;
			if (this.RunTimeTabs)
				Ext.each(this.RunTimeTabs, function (item, index, allItems) {
					if (this.getTabUniqueId(item.InternalName) == tabItem.id) {
						item.Selected = true;
						return false;
					}
				}, this);
		},

		tabItemDeactivated: function (tabItem) {
			if (this.RunTimeTabs)
				Ext.each(this.RunTimeTabs, function (item, index, allItems) {
					if (this.getTabUniqueId(item.InternalName) == tabItem.id) {
						item.Selected = false;
						return false;
					}
				}, this);
		},

		tabItemClosed: function (tabPanel, tabItem) {
			if (Ext.getClassName(tabPanel) == "Ext.tab.Panel") {
				if (this.RunTimeTabs) {
					var rtt = [];
					Ext.each(this.RunTimeTabs, function (tab, index, allTabs) {
						if (this.getTabUniqueId(tab.InternalName) != tabItem.id) {
							rtt.push(tab);
						}
					}, this);
					this.SetRunTimeTabs(rtt);
				}
				/**
				* @event TabClosed
				* Fires when a tab is closed
				* The following properties are set when the event is fired:
				*
				* - {@link #ClosedTabId}
				*
				*/
				if (this.TabClosed) {
					this.ClosedTabId = tabItem.id;
					this.TabClosed();
				}
			}
		},

		tabItemBeforeClosed: function (tabPanel, tabItem) {
			if (Ext.getClassName(tabPanel) == "Ext.tab.Panel") {
				/**
				* @event BeforeTabClosed
				* Fires before a tab is closed. The close action can be cancelled by setting to true the {@link #CancelEvent} property.
				* The following properties are set when the event is fired:
				*
				* - {@link #ClosedTabId}
				*
				*/
				if (this.BeforeTabClosed) {
					this.ClosedTabId = tabItem.id;
					this.CancelEvent = false;
					this.BeforeTabClosed();
					return !this.CancelEvent;
				}
			}
		},

		tabStripClick: function (tab, e) {
			/**
			* @event TabClick
			* Fires when a tab is clicked
			* The following properties are set when the event is fired:
			*
			* - {@link #ActiveTabId}
			*
			*/
			if (this.TabClick) {
				this.ActiveTabId = tab.card.id;
				this.TabClick();
			}
		}
	},

	getTabUniqueId: function (tabId) {
		if (gxui.CBoolean(this.HandleUniqueId))
			return this.getUniqueId() + "_tab_" + tabId;
		else
			return tabId;
	},

	methods: {
		// Methods
		/**
		* Opens a new tab. If a tab with the provided id already exists, the new tab is not created and the tab with the provided is is selected.
		* @param {String} tabId Tab id
		* @param {String} title Tab title
		* @param {String} tabHTMLContent Html code to render inside the new tab
		* @param {Boolean} [closable] True to display a close icon and to allow the user to close the tab 
		* @param {String} [layout] Layout to be used for the new tab. If not specified, the layout specified in {@link #Layout} property is used.
		* @method
		*/
		OpenTab: function (tabId, title, tabHTMLContent, closable, layout) {
			if (this.IsTabOpen(tabId)) {
				this.m_activeTab = this.getTabUniqueId(tabId);
			}
			else {
				var tab = {
					Name: title,
					InternalName: tabId,
					HTML: tabHTMLContent,
					Selected: true,
					closable: closable
				};
				if (layout)
					tab.layout = layout;
				this.RunTimeTabs.push(tab);

				Ext.each(this.getTabPanelsList(), function (tab, index, allTabs) {
					var tabPanel = this.m_tabPanel;
					tabPanel.add(tab);
					tabPanel.doLayout();
					this.registerAsContainer(tab);
				}, this);
			}

			this.m_tabPanel.setActiveTab(this.m_activeTab);
		},

		/**
		* Closes an existing tab.
		* @param {String} tabId Tab id
		* @method
		*/
		CloseTab: function (tabId) {
			var tabPanel = this.m_tabPanel;
			if (this.IsTabOpen(tabId)) {
				var tab = tabPanel.child("#" + this.getTabUniqueId(tabId));
				if (tab) {
					tabPanel.remove(tab, true);
				}
			}
		},

		/**
		* Selects an existing tab.
		* @param {String} tabId Tab id
		* @method
		*/
		SelectTab: function (tabId) {
			this.m_activeTab = this.getTabUniqueId(tabId);
			this.m_tabPanel.setActiveTab(this.m_activeTab);
		},

		/**
		* Checks if a tab is opened.
		* @param {String} tabId Tab id
		* @return {Boolean}
		* @method
		*/
		IsTabOpen: function (tabId) {
			var tab = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			return (tab) ? true : false;
		},

		/**
		* Show an existing tab, by index.
		* @param {Integer} i Tab index (0 based)
		* @method
		*/
		ShowTab: function (i) {
			var panel = this.m_tabPanel.items.get(i);
			if (panel)
				panel.tab.show();
		},

		/**
		* Hide an existing tab, by index.
		* @param {Integer} i Tab index (0 based)
		* @method
		*/
		HideTab: function (i) {
			var panel = this.m_tabPanel.items.get(i);
			if (panel)
				panel.tab.hide();
		},

		/**
		* Show an existing tab, by id.
		* @param {String} tabId Tab id
		* @method
		*/
		ShowTabById: function (tabId) {
			var panel = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			if (panel)
				panel.tab.show();
		},

		/**
		* Hide an existing tab, by id.
		* @param {String} tabId Tab id
		* @method
		*/
		HideTabById: function (tabId) {
			var panel = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			if (panel)
				panel.tab.hide();
		},

		/**
		* Toggles the dirty flag of a tab. When a tab is dirty, a mark (*) is shown next to its title.
		* @param {String} tabId Tab id
		* @param {Boolean} dirty True to set the tab as dirty
		* @method
		*/
		SetTabDirty: function (tabId, dirty) {
			var tab = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			if (tab) {
				tab.dirty = dirty;
				var tabTextEl = tab.tab.btnInnerEl
				if (tabTextEl) {
					if (Ext.isIE) {
						var tabTextHtmlEl = tabTextEl.dom;
						if (dirty) {

							tabTextHtmlEl.innerHTML += "*";
						}
						else {
							if (tabTextHtmlEl.innerHTML.charAt(tabTextHtmlEl.innerHTML.length - 1) == "*")
								tabTextHtmlEl.innerHTML = tabTextHtmlEl.innerHTML.substring(0, tabTextHtmlEl.innerHTML.length - 1);
						}
					}
					else {
						if (dirty) {
							tabTextEl.addCls("x-tab-strip-dirty");
						}
						else {
							tabTextEl.removeCls("x-tab-strip-dirty");
						}
					}
				}
			}
		},

		/**
		* Checks if a tab is dirty
		* @param {String} tabId Tab id
		* @return {Boolean}
		* @method
		*/
		IsTabDirty: function (tabId) {
			var tab = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			return tab && (tab.dirty == true);
		},

		/**
		* Sets the title of an existing tab
		* @param {String} tabId Tab id
		* @param {String} title New title of the tab
		* @method
		*/
		SetTabTitle: function (tabId, title) {
			var tab = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			if (tab) {
				tab.setTitle(title);
				this.SetTabDirty(tabId, tab.dirty || false);
			}
		}
	}
});
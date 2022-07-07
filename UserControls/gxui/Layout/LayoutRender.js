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
* @class gxui.Layout
* Layout User Control. Wraps an Ext.panel.Panel with a BorderLayout so it can be used from GeneXus.
* This is a multi-pane, application-oriented UI layout style that supports multiple nested panels, 
* automatic split bars between regions and built-in expanding and collapsing of regions.
*
* By setting {@link #Nested} = false (its defualt value), the control will automatically size itself to the size of the browser viewport 
* and manage window resizing. If you want to nest the gxui.Layout control inside another control, set {@link #Nested} = true.
*
* To hide a region, set the Hidden property of the region to false ({@link #NorthHidden}, {@link #SouthHidden}, {@link #WestHidden} , {@link #EastHidden} and {@link #CenterHidden} properties).
*/
Ext.define('gxui.Layout', function () {
	var regions = ["North", "West", "Center", "East", "South"];

	return {
		extend: 'gxui.UserControl',

		onRender: function () {
			var regionsCfg = Ext.Array.map(regions, function (regionName) {
				return this.createConfig(regionName);
			}, this);

			if (this.Width) {
				this.Width = parseInt(this.Width);
			}

			if (this.Height) {
				this.Height = parseInt(this.Height);
			}

			if (gxui.CBoolean(this.Nested)) {
				this.m_layout = Ext.create('Ext.panel.Panel', {
					id: this.getUniqueId(),
					border: 0,
					items: regionsCfg,
					layout: 'border',
					cls: this.Cls || undefined,
					width: this.Width != 0 ? this.Width : 'auto',
					height: this.Height != 0 ? this.Height : 'auto',
					stateful: gxui.CBoolean(this.Stateful),
					stateId: (this.StateId != "") ? this.StateId : undefined
				});
			}
			else {
				if (this.getContainerControl()) {
					this.m_layout = Ext.create('gxui.container.NestedViewport', {
						id: this.getUniqueId(),
						items: regionsCfg,
						layout: 'border',
						cls: this.Cls || undefined,
						renderTo: 'MAINFORM',
						stateful: gxui.CBoolean(this.Stateful),
						stateId: (this.StateId != "") ? this.StateId : undefined
					});
				}
			}

			// Register this User Control as a container. Each region of the layout is registered
			// as an individual container.
			this.registerAsContainer();
		},

		onRefresh: function () {
			Ext.each(regions, this.refreshRegion, this);
		},

		onAfterRender: function () {
			this.displayRegions();
			// WA to avoid problems with focus on first field if it's inside de Layout
			if (document.activeElement == document.body) {
				gx.fn.setFocusOnload();
			}
		},

		getUnderlyingControl: function () {
			return this.m_layout;
		},

		addToParent: function () {
			return gxui.CBoolean(this.Nested);
		},

		getMargins: function (regionKey, str) {
			var margins = this[regionKey + "Margins"];
			if (margins)
				return margins.replace(/,/ig, ' ');
		},

		getProperty: function (regionKey, propName) {
			var value = this[regionKey + propName];

			if (regionKey == "Center" && (propName == "Width" || propName == "MaxWidth" || propName == "MinWidth"))
				return undefined;

			if ((propName == "Width" || propName == "Height") && (typeof (value) == "string" && value.indexOf("%") == -1))
				return parseInt(value);

			if (value == "true" || value == "false")
				return gxui.CBoolean(value);
			else if (propName.indexOf("Margins") >= 0)
				return this.getMargins(regionKey, value);
			else if (propName.indexOf("Border") >= 0)
				return gx.lang.gxBoolean(value) ? undefined : 0;
			else if (propName == "Layout")
				return value == "default" ? undefined : value;
			else
				return value;
		},

		setRegionProperty: function (regionKey, propName, value) {
			if (regionKey) {
				var r = regionKey.toLowerCase();
				r = r.charAt(0).toUpperCase() + r.substr(1);
				if (this[r + propName]) {
					this[r + propName] = value;
				}
			}
		},

		createConfig: function (regionKey) {
			var config = {
				id: this.getUniqueId() + "-" + regionKey,
				itemId: regionKey.toLowerCase(),
				region: regionKey.toLowerCase(),
				contentEl: this.getChildContainer(regionKey),
				autoScroll: (this.getProperty(regionKey, "Layout") != "fit") ? this.getProperty(regionKey, "AutoScroll") : false,
				cls: "x-region-" + regionKey.toLowerCase(),
				bodyCls: "gxui-noreset",
				duration: this.getProperty(regionKey, "Duration") / 1000,
				listeners: {
					'collapse': function (p) {
						this.setRegionProperty(p.region, "Collapsed", "true");
					},

					'expand': function (p) {
						this.setRegionProperty(p.region, "Collapsed", "false");
					},

					scope: this
				},
				stateful: gxui.CBoolean(this.Stateful),
				stateId: (this.StateId != "") ? this.StateId + "-" + regionKey : undefined,
				stateEvents: gxui.CBoolean(this.Stateful) ? ['collapse', 'expand'] : undefined
			};

			if (!this.getProperty(regionKey, "TitleBar")) {
				config.header = false;
			}

			gxui.tryPropertyMapping(config, Ext.bind(this.getProperty, this, [regionKey], 0), {
				"hidden": "Hidden",
				"split": "Split",
				"title": "Title",
				"width": "Width",
				"height": "Height",
				"minWidth": "MinWidth",
				"minHeight": "MinHeight",
				"maxWidth": "MaxWidth",
				"maxHeight": "MaxHeight",
				"margins": "Margins",
				"collapsible": "Collapsible",
				"collapsed": "Collapsed",
				"floatable": "Floatable",
				"animate": "Animate",
				"animFloat": "Animate",
				"layout": "Layout",
				"border": "Border"
			});

			return config;
		},

		displayRegions: function () {
			var layout = this.m_layout;

			var displayRegion = function (region) {
				Ext.fly(layout.child('#' + region.toLowerCase()).contentEl).setDisplayed(true);
			};

			Ext.each(regions, displayRegion);
		},

		registerAsContainer: function () {
			Ext.each(regions, function (regionName) {
				var region = this.getRegion(regionName);
				this.registerCt(region.contentEl, region.add, region.doLayout, region);
			}, this);
		},

		getRegion: function (regionKey) {
			return this.m_layout.child('#' + regionKey.toLowerCase())
		},

		refreshRegion: function (regionKey) {
			var region = this.getRegion(regionKey);
			if (region) {
				region.setTitle(this.getProperty(regionKey, "Title"));
			}
		},

		methods: {
			// Methods
			/**
			* Collapses the region body so that it becomes hidden.
			* @param {String} regionKey Region name ("North", "South", "West" or "East")
			* @param {Boolean} [animate=true] True to animate the transition, else false.
			* @method
			*/
			CollapseRegion: function (regionKey, animate) {
				var region = this.getRegion(regionKey);

				if (region) {
					region.collapse(animate);
					this.setRegionProperty(regionKey, "Collapsed", "true");
				}
			},

			/**
			* Expands the region body so that it becomes visible.
			* @param {String} regionKey Region name ("North", "South", "West" or "East")
			* @param {Boolean} [animate] True to animate the transition, else false (defaults to the value of the AnimateCollapse property).
			* @method
			*/
			ExpandRegion: function (regionKey, animate) {
				var region = this.getRegion(regionKey);

				if (region) {
					region.expand(animate);
					this.setRegionProperty(regionKey, "Collapsed", "false");
				}
			},

			/**
			* Hides the region.
			* @param {String} regionKey Region name ("North", "South", "West" or "East")
			* @method
			*/
			HideRegion: function (regionKey) {
				var region = this.getRegion(regionKey);

				if (region) {
					region.hide();
					this.m_layout.doLayout();
					this.setRegionProperty(regionKey, "Hidden", "true");
				}
			},

			/**
			* Shows the region.
			* @param {String} regionKey Region name ("North", "South", "West" or "East")
			* @method
			*/
			ShowRegion: function (regionKey) {
				var region = this.getRegion(regionKey);

				if (region) {
					region.show();
					this.m_layout.doLayout();
					this.setRegionProperty(regionKey, "Hidden", "false");
				}
			}
		}
	};
} ());

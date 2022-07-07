/*
 * GxUI Library 2.0.1
 * Copyright (c) 2009, Artech
 * All rights reserved.
 * 
 * GxUI Library is freely distributable under the terms of the BSD license.
 * 
 */

// Fallback mechanism, when Ext is not included

if (!window.Ext) {
	alert("ExtJS not found. To solve the problem, please follow the installation instructions: http://wiki.gxtechnical.com/commwiki/servlet/hwikibypageid?19312.");
}
/// <reference path="..\VStudio\vswd-ext_2.2.js" />
// gxui namespace and user controls base class definition


gxui = function () {
	var m_GenexusBuild = null;

	var fixCssReset = function () {
		var cssLines = [],
			cellSpacing,
			cellPadding;

		cssLines.push("table, table[cellspacing='0'] {",
					"    border-collapse: separate;",
					"}");

		// CSS rules are created on the fly so cellpadding and cellspacing values are not reseted.
		for (var i = 1; i <= 100; i++) {
			if (gxui.fixSpacingReset) {
				cellSpacing = [
					"table[cellspacing='", i, "'] {",
					"    border-collapse: separate;",
					"    border-spacing: ", i, "px;",
					"}"
				];

				cssLines.push(cellSpacing.join(""));
			}
			if (gxui.fixPaddingReset) {
				cellPadding = [
					"table[cellpadding='", i, "'] > tbody > tr > td:not([class]), table[cellpadding='", i, "'] > tbody > tr > th:not([class]) {",
					"    padding: ", i, "px;",
					"}"
				];

				cssLines.push(cellPadding.join(""));
			}
		}

		var head = document.getElementsByTagName('head')[0],
			styleEl = document.createElement('style');

		styleEl.type = 'text/css';

		if (styleEl.styleSheet) {
			styleEl.styleSheet.cssText = cssLines.join("");
		}
		else {
			styleEl.appendChild(document.createTextNode(cssLines.join("")));
		}
		head.appendChild(styleEl);
	};

	return {
		
		fixPaddingReset: true,

		
		fixSpacingReset: true,

		initialize: function () {

			// Initialize QuickTips
			Ext.tip.QuickTipManager.init();

			// Define a namespace for extensions of Ext components
			Ext.namespace('gxui.ext');
			// Define a namespace for object properties management classes
			Ext.namespace('gxui.Properties');
			// Define a namespace for GX interop classes
			Ext.namespace('gxui.GX');
			// Define a namespace for GxUI user extensions
			Ext.namespace('gxui.ux');

			gx.fx.obs.addObserver('gx.onready', this, function () {
				if (gx && Ext.ieVersion > 0 && Ext.ieVersion < 8) {
					if (gx.staticDirectory != "")
						this.StaticContent = gx.staticDirectory;
					else
						this.StaticContent = this.getCookie('StaticContent');

					Ext.BLANK_IMAGE_URL = gx.util.resourceUrl(gx.basePath + this.StaticContent + "Shared/ext/resources/themes/images/default/tree/s.gif", true);
				}

				// Fix CSS reset made by ExtJS that affects tables
				gxui.afterShow(fixCssReset, gxui);

				// Force popup size recalculation after showing controls
				if (gx.popup.ispopup()) {
					gxui.afterShow(function () {
						var popup = gx.popup.getPopup().window.gx.popup;
						Ext.defer(popup.autofit, 100, popup)
					}, gx.popup);
				}
			});

			// Default State provider
			Ext.state.Manager.setProvider(new Ext.state.CookieProvider({
				expires: new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 365)) //365 days
			}));

			// For versions prior to build 55424, gx.lang.inherits function is overriden to allow
			// GxUI to work properly with older versions of GeneXus.
			var gxBuild = gxui.getGeneXusBuild();
			if (gxBuild) {
				if (gxBuild > 54798) {
					var docEl = Ext.fly(document.documentElement);
					if (docEl) {
						docEl.addCls("gxui-xev2");
						if (gxBuild <= 64355) {
							docEl.addCls("gxui-msg-fix");
						}
					}
				}
				if (gxBuild < 55424) {
					gx.lang.inherits = function (subclass, superclass) {
						var oldProt = subclass.prototype;
						subclass.prototype = new superclass();
						for (var pName in oldProt) {
							if (typeof (subclass.prototype[pName]) == 'undefined')
								subclass.prototype[pName] = oldProt[pName];
						}
						if (typeof (subclass.prototype.base) == 'undefined')
							subclass.prototype.base = superclass;

						subclass.prototype.constructor = function () {
							superclass.prototype.constructor.apply(this, arguments);
							oldProt.constructor.apply(this, arguments);
						};
					};
				}
			}
			
			// Override Ext.getBody() to allow ExtJS to work with SPA
			Ext.getBody = (function() {
				var body, domBody;
				return function() {
					if (!domBody || domBody != document.body) {
						domBody = document.body;
						body = Ext.get(domBody);
						return body;
					}
					return body;
				};
			}())
		},

		
		CBoolean: function (str) {
			if (str) {
				if (typeof (str) == 'string')
					return (str.toLowerCase() == "true")
				return str;
			}
			else
				return false;
		},


		
		clone: function (obj, fn) {
			if (obj instanceof Array)
				return gxui.copyArray(obj, fn);
			if (typeof (obj) != 'object')
				return obj;
			if (obj == null)
				return obj;
			if (obj.clone)
				return obj.clone();
			var cloneObj = new Object();
			for (var i in obj)
				cloneObj[i] = gxui.clone(obj[i], fn);
			if (fn && typeof (fn) == 'function')
				fn(cloneObj);
			return cloneObj;
		},

		
		copyArray: function (arr, fn) {
			var res = [];
			for (var i = 0; i < arr.length; i++)
				res.push(gxui.clone(arr[i], fn));
			return res;
		},

		getCookie: function (c_name) {
			if (document.cookie.length > 0) {
				c_start = document.cookie.indexOf(c_name + "=");
				if (c_start != -1) {
					c_start = c_start + c_name.length + 1;
					c_end = document.cookie.indexOf(";", c_start);
					if (c_end == -1) c_end = document.cookie.length;
					return unescape(document.cookie.substring(c_start, c_end));
				}
			}
			return "";
		},

		setCookie: function (name, value, days) {
			if (days) {
				var date = new Date();
				date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
				var expires = "; expires=" + date.toGMTString();
			}
			else expires = "";
			document.cookie = name + "=" + value + expires + "; path=/";
		},

		dateFormat: function () {
			var gxDF = gx.dateFormat;
			switch (gxDF) {
				case "MDY": return "m/d/y";
				case "MDY4": return "m/d/Y";
				case "YMD": return "y/m/d";
				case "Y4MD": return "Y/m/d";
				case "DMY": return "d/m/y";
				case "DMY4": return "d/m/Y";
			}
		},

		date: function (string) {
			return new Date.parseDate(string, this.dateFormat());
		},

		
		afterShow: function (fn, scope, options) {
			gxui.UserControlManager.afterShow(fn, scope, options);
		},

		
		tryPropertyMapping: function (targetObj, source, propertyMap) {
			for (var targetProp in propertyMap) {
				var sourceValue,
					sourceProp = propertyMap[targetProp],
					ignoreEmpty = false;

				if (typeof (propertyMap[targetProp]) == "object") {
					ignoreEmpty = propertyMap[targetProp].ignoreEmpty || false;
					sourceProp = propertyMap[targetProp].property;
				}

				if (typeof (source) == "function")
					sourceValue = source(sourceProp);
				else
					sourceValue = source[sourceProp];

				if (ignoreEmpty) {
					if (sourceValue)
						targetObj[targetProp] = sourceValue;
				}
				else {
					if (sourceValue !== undefined)
						targetObj[targetProp] = sourceValue;
				}
			}
		},

		
		getGeneXusBuild: function () {
			if (m_GenexusBuild === null) {
				try {
					var metaElements = document.getElementsByTagName('meta'),
						generatorEl = null,
						versionEl = null;
					for (var i = 0, len = metaElements.length; i < len; i++) {
						if (metaElements[i].name.toLowerCase() == "version")
							versionEl = metaElements[i];
						if (metaElements[i].name.toLowerCase() == "generator")
							generatorEl = metaElements[i];
					}

					var value = versionEl ? versionEl.getAttribute('content') : generatorEl.getAttribute('content');
					var matches = value.match(/-(\d+)$/);
					if (matches.length > 1)
						m_GenexusBuild = parseInt(matches[1]);
				}
				catch (e) {
					m_GenexusBuild = 0;
				}
			}

			return m_GenexusBuild;
		}
	};
} ();

gxui.initialize();
/// <reference path="..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.UserControl', {
	mixins: {
		observable: 'Ext.util.Observable'
	},

	
	unmanagedLayout: false,

	
	constructor: function (options) {
		this.setOptions(options)
		this.initialize();

		return this;
	},

	//private
	setOptions: function (options) {
		this.options = {
			register: true
		};

		for (property in (options || {})) {
			this.options[property] = options[property];
		}
	},

	//private
	initialize: function () {
		this.rendered = false;

		this.mixins.observable.constructor.call(this);

		this.addEvents({
			
			"show": true,
			
			"destroy": true
		});

		if (this.options.register)
			this.register();

		if (this.methods)
			this.addDeferredMethods(this.methods);
	},

	
	show: function () {
		if (!this.rendered) {
			Ext.onReady(function () {
				try {
					this.renderControl();
				}
				catch (e) {
					gx.dbg.logEx(e, 'gxui.js', 'show');
				}
				finally {
					this.fireEvent("show", this);
				}
			}, this);
		}
		else {
			try {
				if (this.onRefresh)
					this.onRefresh();
			}
			catch (e) {
				gx.dbg.logEx(e, 'gxui.js', 'show');
			}
			finally {
				this.fireEvent("show", this);
			}
		}
	},

	renderControl: function () {
		this.onRender();
		this.rendered = true;
		this.addToContainer();
		if (this.onAfterRender) {
			var control = this.getUnderlyingControl();
			if (control) {
				if (control.rendered)
					this.onAfterRender.call(this, control);
				else
					control.on('afterrender', this.onAfterRender, this, [control]);
			}
		}
	},
	
	
	forceRendering: function () {
		this.rendered = false;
	},

	
	destroy: function () {
		try {
			this.onDestroy();
		}
		catch (e) {
			gx.dbg.logEx(e, 'gxui.js', 'destroy');
		}

		this.fireEvent("destroy", this);
	},

	
	onRender: Ext.emptyFn,

	
	onRefresh: Ext.emptyFn,

	
	onDestroy: function () {
		var c = this.getUnderlyingControl();
		if (c) {
			var ct = c.ownerCt;
			if (ct) {
				if (ct.remove) {
					ct.remove(c);
				}
			}
			else {
				if (c.destroy) {
					c.destroy();
				}
			}
		}
	},

	
	getUnderlyingControl: function () {
		return false
	},

	
	addToParent: function () {
		return false;
	},

	
	register: function () {
		gxui.UserControlManager.register(this);
	},

	
	unregister: function () {
		gxui.UserControlManager.unregister(this);
	},

	
	registerCt: function (el, addFn, doLayoutFn, scope) {
		gxui.UserControlManager.registerContainer(this, el, addFn, doLayoutFn, scope);
	},

	
	unregisterCt: function (toRem) {
		gxui.UserControlManager.unregisterContainer(toRem);
	},

	
	addToContainer: function () {
		var control = this.getUnderlyingControl();
		if (control) {
			if (this.addToParent()) {
				gxui.UserControlManager.addToParentContainer(this, control);
			}
			else {
				if (!this.unmanagedLayout && !control.rendered) {
					control.render(this.getContainerControl());
				}
			}
		}
	},

	checkIfInline: function (el) {
		if (el.id.indexOf("gxHTMLWrp") >= 0 || el.hasCls("gx_usercontrol") || el.hasCls("gxui-uc-container"))
			el.setStyle("display", "inline");

		if (this.getContainerControl() == el.dom && gxui.CBoolean(this.AutoWidth) && this.getUnderlyingControl() && !this.getUnderlyingControl().ownerCt)
			el.setStyle("display", "inline-block");
	},

	
	getUniqueId: function () {
		var pO = this.ParentObject;
		return "gxui20" + (pO ? (pO.CmpContext ? "-" + pO.CmpContext : "") + "-" + pO.ServerClass.replace(/\./g, "-") || "" : "") + "-" + this.ControlName + (this.GridRow || "");
	},

	addDeferredMethods: function (methods) {
		for (var m in methods) {
			if (typeof (methods[m]) == 'function') {
				this[m] = Ext.bind(function () {
					var fn = arguments[arguments.length - 1],
						args = Array.prototype.slice.call(arguments, 0, arguments.length - 1);

					if (this.runDeferredMethod(m))
						return fn.apply(this, args);
					else
						gxui.afterShow(Ext.bind(fn, this, args), this, { single: true });
				}, this, [methods[m]], true);
			}
		}
	},

	runDeferredMethod: function (methodName) {
		var control = this.getUnderlyingControl();
		return control === false || (control && control.rendered);
	}
});


gxui.UserControlManager = function () {
	var mgr;
	var ucList = [];
	var ctList = [];

	var afterShowEvent;

	var initAfterShow = function () {
		afterShowEvent = new Ext.util.Event();
	};

	var allShownHandler = function () {
		afterShowEvent.fire();
		this.addControlsToContainer();
		for (var i = 0, len = ucList.length; i < len; i++) {
			var item = ucList[i],
				extUC = item.getUnderlyingControl();

			if (extUC) {
				if (!item.unmanagedLayout && !extUC.rendered)
					extUC.render(item.getContainerControl());
				else {
					// Fire doLayout function in those controls that don't have a parent control.
					if (extUC && !extUC.ownerCt && extUC.doLayout) {
						extUC.doLayout();
					}
					if (item.fixAutoDimensions) {
						item.fixAutoDimensions(extUC);
					}
				}
			}
			item.shown = false;
		}
	};
	
	var ucShowListener = function (uc) {
		try {
			var ucListItem = this.isRegisteredUC(uc)
			if (ucListItem) {
				ucListItem.shown = true;
			}

			var allShown = true;
			for (var i = 0, len = ucList.length; i < len; i++) {
				allShown = ucList[i].shown && allShown;
				if (!allShown)
					break;
			}

			if (allShown && afterShowEvent) {
				allShownHandler.call(this);
			}
		}
		catch (e) {
			gx.dbg.logEx(e, 'gxui.js', 'ucShowListener');
		}
	};

	gx.fx.obs.addObserver('gx.onobjectpostback', mgr, function () {
		for (var i = 0, len = ucList.length; i < len; i++) {
			if (ucList[i].shown === true) {
				allShownHandler.call(mgr);
				return;
			}
		}
	});
	
	mgr = {
		childControls: {},

		getUCList: function () {
			var l = [];
			for (var i = 0, len = ucList.length; i < len; i++) {
				l.push(ucList[i]);
			}
			return l;
		},

		getContainersList: function () {
			return ctList;
		},

		register: function (uc) {
			ucList.push(uc);
			uc.shown = false;

			uc.on("show", ucShowListener, this);

			uc.on("destroy", function (uc) {
				this.unregister(uc);
				this.unregisterContainer(uc);
				if (uc.afterShowHandler)
					afterShowEvent.removeListener(uc.afterShowHandler, uc);
			}, this);
		},

		unregister: function (uc) {
			var toRem = this.isRegisteredUC(uc);
			if (toRem) {
				for (var i = ucList.length - 1; i >= 0; i--) {
					if (toRem == ucList[i]) {
						ucList.splice(i, 1);
						break;
					}
				}
			}
		},

		registerContainer: function (uc, el, addFn, doLayoutFn, scope) {
			ctList.push({
				uc: uc,
				el: el,
				addFn: addFn,
				doLayoutFn: doLayoutFn,
				scope: scope
			});
		},

		unregisterContainer: function (obj) {
			toRem = this.isRegisteredContainer(obj);
			if (toRem) {
				for (var i = ctList.length - 1; i >= 0; i--) {
					if (toRem == ctList[i]) {
						ctList.splice(i, 1);
						break;
					}
				}
			}
		},

		isRegisteredUC: function (uc) {
			var obj = null;

			for (var i = 0, len = ucList.length; i < len; i++) {
				var item = ucList[i];
				if (uc == item) {
					obj = item;
					break;
				}
			}
			return obj;
		},

		isRegisteredContainer: function (el) {
			var ct = null;

			if (el.layout) {
				for (var i = 0, len = ctList.length; i < len; i++) {
					var item = ctList[i];
					if (el == item.scope) {
						ct = item;
						break;
					}
				}
			}
			else
				if (el.tagName) { // If el argument is a HTMLElement
					for (var i = 0, len = ctList.length; i < len; i++) {
						var item = ctList[i];
						if (el == item.el) {
							ct = item;
							break;
						}
					}
				}
				else { // If el argument is a gxui.UserControl
					uc = el;
					for (var i = 0, len = ctList.length; i < len; i++) {
						var item = ctList[i];
						if (uc == item.uc) {
							ct = item;
							break;
						}
					}
				}

			return ct;
		},

		setControlContainer: function (control, container) {
			if (!this.childControls)
				this.childControls = {};

			var containerId = container == 'ROOT' ? container : container.scope.id;
			if (!this.childControls[containerId])
				this.childControls[containerId] = [];
			this.childControls[containerId].push(control);
		},

		addToParentContainer: function (uc, control) {
			control.on('added', function () {
				control.width = undefined;
				control.height = undefined;
			}, uc);

			var findParentContainerFn = this.findParentContainer.closure(this, arguments);
			var afterShowEvent = gxui.afterShow(findParentContainerFn, this);
			
			uc.on('destroy', function () {
				if (afterShowEvent) {
					afterShowEvent.removeListener(findParentContainerFn, this);
				}
			}, this);
		},


		findParentContainer: function (uc, control) {
			try {
				var el = Ext.get(uc.getContainerControl());
				uc.checkIfInline(el);
				for (var el = Ext.get(uc.getContainerControl()); el; el = el.parent("div")) {
					var container = gxui.UserControlManager.isRegisteredContainer(el.dom)
					uc.checkIfInline(el);
					if (container) {
						this.setControlContainer(control, container);
						return;
					}
				}

				// Controls that don't have a parent container
				this.setControlContainer(control, 'ROOT');
			}
			catch (e) {
				gx.dbg.logEx(e, 'gxui.UserControl.js', 'addToParentContainer->' + uc.getUniqueId());
			}
		},

		addControlsToContainer: function () {
			try {
				var containers = this.getContainersList();
				for (var i = 0, len = containers.length; i < len; i++) {
					var container = containers[i],
						children = this.childControls[container.scope.id];
					var safeChildren = [];
					if (children) {
						for (var j = 0, len2 = children.length; j < len2; j++) {
							if (!children[j].isDestroyed)
								safeChildren.push(children[j]);
						}
						if (safeChildren && safeChildren.length > 0)
							container.addFn.call(container.scope, safeChildren);
					}
				}

				delete this.childControls;
			}
			catch (e) {
				gx.dbg.logEx(e, 'gxui.UserControl.js', 'addControlsToContainer');
			}
		},

		
		afterShow: function (fn, scope, options) {
			if (!afterShowEvent)
				initAfterShow();

			scope.afterShowHandler = fn;
			afterShowEvent.addListener(fn, scope, options);
			return afterShowEvent;
		}
	};

	return mgr;
} ();

Ext.define('gxui.container.NestedViewport', {
	extend: 'Ext.container.Container',

	initComponent: function () {
		this.callParent(arguments);

		var html = Ext.fly(document.body.parentNode);
		html.addCls(Ext.baseCSSPrefix + 'viewport');
		if (this.autoScroll) {
			html.setStyle('overflow', 'auto');
		}

		this.renderTo = Ext.get(this.renderTo);
		this.renderTo.addCls("x-nested");

		this.el = this.renderTo;
		this.el.setHeight = Ext.emptyFn;
		this.el.setWidth = Ext.emptyFn;
		this.el.setSize = Ext.emptyFn;
		this.el.dom.scroll = 'no';
		this.allowDomMove = false;
		Ext.EventManager.onWindowResize(this.fireResize, this);
		this.width = Ext.Element.getViewportWidth();
		this.height = Ext.Element.getViewportHeight();
	},

	fireResize: function (w, h) {
		this.setSize(w, h);
	}
});


/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.Panel', {
	extend: 'gxui.UserControl',

	

	//private
	SetToolbarData: function (data) {
		this.ToolbarData = data;
	},

	//private
	GetToolbarData: function (data) {
		return this.ToolbarData;
	},

	onRender: function () {
		var config = this.getConfig();

		if (gxui.CBoolean(this.ShowAsWindow)) {
			config.closeAction = "hide";
			config.renderTo = 'MAINFORM';
			config.modal = gxui.CBoolean(this.Modal);
			config.constrainHeader = true;
			this.m_panel = new Ext.create('Ext.window.Window', config);

			if (gx.lang.gxBoolean(this.Visible)) {
				this.m_panel.show();
			}
		}
		else {
			this.m_panel = Ext.create('Ext.panel.Panel', config);
		}

		// Register as UC Container
		this.registerCt(this.getChildContainer("Body"), this.m_panel.add, this.m_panel.doLayout, this.m_panel);
	},

	onRefresh: function () {
		var panel = this.m_panel;
		if (this.Title != panel.title) {
			panel.setTitle(this.Title);
		}
		if (!panel.ownerCt) {
			var width = parseInt(this.Width),
				height = parseInt(this.Height);

			if ((!gxui.CBoolean(this.AutoWidth) && panel.getWidth() != width) || (!gxui.CBoolean(this.AutoHeight) && panel.getHeight() != height)) {
				panel.animate({
					to: {
						width: width,
						height: height
					}
				});
			}
		}

		if (gx.lang.gxBoolean(this.Visible) && !this.m_panel.isVisible()) {
			panel.show();
		}
		else {
			if (!gx.lang.gxBoolean(this.Visible) && panel.isVisible()) {
				panel.hide();
			}
		}

		if (gxui.CBoolean(this.UseToolbar)) {
			this.m_gxTbar.SetData(this.ToolbarData);
			this.m_gxTbar.onRefresh();
		}
	},

	getUnderlyingControl: function () {
		return this.m_panel;
	},

	addToParent: function () {
		return !gxui.CBoolean(this.ShowAsWindow) && (this.AddToParentGxUIControl == undefined || gxui.CBoolean(this.AddToParentGxUIControl));
	},

	//private
	getConfig: function () {
		var dockedItems = [],
			bodyEl = Ext.get(this.getChildContainer("Body"));

		if (gxui.CBoolean(this.UseToolbar)) {
			var position = this.ToolbarPosition || 'top';

			this.m_gxTbar = Ext.create('gxui.Toolbar', { register: false });
			this.m_toolbar = this.m_gxTbar.createToolbar({
				id: this.getUniqueId() + "_Toolbar",
				data: this.ToolbarData,
				vertical: !(position == 'bottom' || position == 'top'),
				dock: position,
				container: this
			});

			dockedItems.push(this.m_toolbar);
		}

		bodyEl.enableDisplayMode().show();
		if (gxui.CBoolean(this.AutoHeight)) {
			bodyEl.setStyle({
				height: 'auto',
				display: 'inline-block'
			});
		}

		var config = {
			contentEl: bodyEl,
			id: this.getUniqueId(),
			autoWidth: gxui.CBoolean(this.AutoWidth),
			autoHeight: gxui.CBoolean(this.AutoHeight),
			autoScroll: (this.Layout == 'default'),
			frame: gxui.CBoolean(this.Frame),
			border: gxui.CBoolean(this.Border) ? 2 : false,
			collapsible: gxui.CBoolean(this.Collapsible),
			collapsed: gxui.CBoolean(this.Collapsed),
			animCollapse: gxui.CBoolean(this.AnimateCollapse),
			resizable: gxui.CBoolean(this.Resizable),
			dockedItems: dockedItems,
			listeners: this.getListeners(),
			stateful: gxui.CBoolean(this.Stateful),
			stateId: (this.StateId != "") ? this.StateId : undefined,
			layout: this.Layout == 'default' ? undefined : this.Layout,
			bodyCls: "gxui-noreset"
		};

		gxui.tryPropertyMapping(config, this, {
			"title": "Title",
			"headerPosition": "HeaderPosition",
			"minWidth": "MinWidth",
			"minHeight": "MinHeight",
			"maxWidth": "MaxWidth",
			"maxHeight": "MaxHeight",
			"collapseDirection": "CollapseDirection",
			"resizeHandles": "Handles",
			"iconCls": "IconCls",
			"cls": "Cls"
		});

		if (!gxui.CBoolean(this.AutoWidth))
			config.width = parseInt(this.Width);

		if (!gxui.CBoolean(this.AutoHeight))
			config.height = parseInt(this.Height);

		if (!gx.lang.gxBoolean(this.Visible))
			config.hidden = true;

		return config;
	},

	fixAutoDimensions: function (panel) {
		if (!this.fixingAutoDims) {
			this.fixingAutoDims = true;
			if (panel.rendered) {
				if (gxui.CBoolean(this.AutoHeight)) {
					panel.el.setHeight('auto');
					panel.body.setHeight('auto');
					if (panel.header && (panel.headerPosition == "top" || panel.headerPosition == "bottom")) {
						panel.body.setStyle('margin-bottom', Ext.dom.AbstractElement.addUnits(panel.header.getHeight(), "px"));
					}
				}

				if (gxui.CBoolean(this.AutoWidth)) {
					panel.el.setWidth('auto');
					panel.body.setWidth('auto');
					if (panel.header && (panel.headerPosition == "top" || panel.headerPosition == "bottom")) {
						Ext.defer(panel.header.setWidth, 50, panel.header, ['auto']);
					}
				}
			}
			this.fixingAutoDims = false;
		}
	},

	//private
	getListeners: function () {
		return {
			'collapse': function () {
				this.Collapsed = "true";
			},

			'expand': function () {
				this.Collapsed = "false";
			},

			'hide': function () {
				this.Visible = false;
				
				if (this.OnClose) {
					this.OnClose();
				}
			},

			'add': this.fixAutoDimensions,

			'afterrender': this.fixAutoDimensions,

			scope: this
		};
	},

	methods: {
		// Methods
		
		ChangeToolbar: function (toolbarData) {
			if (this.m_gxTbar)
				this.m_toolbar = this.m_gxTbar.ChangeToolbar(toolbarData, this.getUniqueId() + "_Toolbar", this);
		},

		
		Collapse: function (animate) {
			this.m_panel.collapse(this.CollapseDirection, animate);
		},

		
		Expand: function (animate) {
			this.m_panel.expand(animate);
		},

		
		DisableToolbarItem: function (itemId) {
			this.m_gxTbar.DisableItem(itemId);
		},

		
		EnableToolbarItem: function (itemId) {
			this.m_gxTbar.EnableItem(itemId);
		},

		
		HideToolbarItem: function (itemId) {
			this.m_gxTbar.HideItem(itemId);
		},

		
		ShowToolbarItem: function (itemId) {
			this.m_gxTbar.ShowItem(itemId);
		},

		
		CenterWindow: function () {
			if (gxui.CBoolean(this.ShowAsWindow)) {
				this.m_panel.center();
			}
		}
	}
});
/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.Toolbar', {
	extend: 'gxui.UserControl',

	initialize: function () {
		this.callParent();

		this.ButtonPressedId = "";
		this.EditFieldValue = "";

		this.addEvents({
			
			"beforebuttonpressed": true,
			
			"buttonpressed": true,
			
			"editfieldkeypress": true,
			
			"editfieldblur": true
		});


		// Register default Toolbar item resolvers
		gxui.Toolbar.ItemResolvers.register({
			"Button": function (toolbar, button) {
				var config = {
					id: toolbar.getUniqueButtonId(button.Id),
					gxConfirmation: gxui.CBoolean(button.AskConfirmation) ? button.Confirmation : false,
					cls: toolbar.getBtnCls(button),
					enableToggle: gxui.CBoolean(button.EnableToggle),
					pressed: gxui.CBoolean(button.Pressed),
					disabled: gxui.CBoolean(button.Disabled),
					hidden: gxui.CBoolean(button.Hidden),
					handler: toolbar.buttonClickHandler,
					isDropTarget: gxui.CBoolean(button.IsDropTarget),
					scope: toolbar,
					RefreshData: gxui.CBoolean(button.RefreshData)
				};

				gxui.tryPropertyMapping(config, button, {
					"gxid": "Id",
					"text": "Text",
					"tooltip": "Tooltip",
					"icon": "Icon",
					"iconCls": "IconCls",
					"rowspan": "RowSpan",
					"colspan": "ColSpan",
					"iconAlign": { property: "IconAlign", ignoreEmpty: true },
					"arrowAlign": { property: "ArrowAlign", ignoreEmpty: true },
					"scale": { property: "Scale", ignoreEmpty: true },
					"width": { property: "Width", ignoreEmpty: true }
				});

				return config;
			},

			"Text": function (toolbar, button) {
				return button.Text;
			},

			"Edit": function (toolbar, button) {
				var edit = Ext.create('Ext.form.field.Text', {
					id: toolbar.getUniqueButtonId(button.Id),
					cls: button.Cls,
					width: button.Width || 180,
					disabled: gxui.CBoolean(button.Disabled),
					hidden: gxui.CBoolean(button.Hidden),
					enableKeyEvents: true,
					value: button.Value || undefined
				});

				if (edit.Text != '')
					edit.emptyText = button.Text;

				edit.on({
					"keypress": {
						fn: function (field, e) {
							this.fireEvent("editfieldkeypress", this, field, e);
							if (e.getKey() == Ext.EventObject.ENTER) {
								e.stopEvent();
								this.editActionHandler(field);
							}
						},
						scope: toolbar
					},
					"blur": {
						fn: function (field) {
							this.fireEvent("editfieldblur", this, field);
							this.editActionHandler(field);
						},
						scope: toolbar
					}
				});
				edit.gxid = button.Id;

				return edit;
			},

			"Fill": function () {
				return Ext.create('Ext.toolbar.Fill');
			},

			"Separator": function () {
				return "-";
			},

			"Menu": function (toolbar, button) {
				var menuItems = [];

				if (button.Items) {
					Ext.each(button.Items, function (item, index, allItems) {
						menuItems.push(toolbar.getConfig(item));
					});
				}

				var config = {
					hidden: gxui.CBoolean(button.Hidden),
					menu: {
						items: menuItems,
						ignoreParentClicks: true
					},
					cls: toolbar.getBtnCls(button),
					disabled: gxui.CBoolean(button.Disabled)
				};

				gxui.tryPropertyMapping(config, button, {
					"text": "Text",
					"tooltip": "Tooltip",
					"icon": "Icon",
					"iconCls": "IconCls",
					"rowspan": "RowSpan",
					"colspan": "ColSpan",
					"iconAlign": { property: "IconAlign", ignoreEmpty: true },
					"arrowAlign": { property: "ArrowAlign", ignoreEmpty: true },
					"scale": { property: "Scale", ignoreEmpty: true },
					"width": { property: "Width", ignoreEmpty: true }
				});

				return config;
			},

			"SplitButton": function (toolbar, button) {
				var splitButton = gxui.Toolbar.ItemResolvers.get(gxui.Toolbar.ItemType.Menu)(toolbar, button);

				splitButton.gxid = button.Id;
				splitButton.gxConfirmation = gxui.CBoolean(button.AskConfirmation) ? button.Confirmation : false;
				splitButton.xtype = 'splitbutton';
				splitButton.enableToggle = gxui.CBoolean(button.EnableToggle);
				splitButton.pressed = gxui.CBoolean(button.Pressed);
				if (gxui.CBoolean(button.EnableToggle)) {
					splitButton.toggleHandler = toolbar.buttonClickHandler;
				}
				else {
					splitButton.handler = toolbar.buttonClickHandler;
				}
				splitButton.scope = toolbar;

				return splitButton;
			},

			"Group": function (toolbar, button) {
				var groupItems = [];

				if (button.Items) {
					Ext.each(button.Items, function (item, index, allItems) {
						groupItems.push(toolbar.getConfig(item));
					});
				}

				var config = {
					xtype: 'buttongroup',
					defaults: {},
					items: groupItems
				};

				gxui.tryPropertyMapping(config, button, {
					"title": "Text",
					"columns": "GroupColumns"
				});

				gxui.tryPropertyMapping(config.defaults, button, {
					"iconAlign": { property: "IconAlign", ignoreEmpty: true },
					"arrowAlign": { property: "ArrowAlign", ignoreEmpty: true },
					"scale": { property: "Scale", ignoreEmpty: true }
				});

				return config;
			},

			"ComboBox": function (toolbar, comboBox) {
				var config = {
					xtype: 'combobox',
					gxid: comboBox.Id,
					cls: toolbar.getBtnCls(comboBox),
					disabled: gxui.CBoolean(comboBox.Disabled),
					hidden: gxui.CBoolean(comboBox.Hidden),
					editable: true,
					triggerAction: 'all',
					selectOnFocus: true,
					disableKeyFilter: false,
					forceSelection: true,
					queryMode: 'local',
					store: {
						autoDestroy: true,
						fields: ['id', 'dsc'],
						data: []
					},
					displayField: 'dsc',
					valueField: 'id',
					listeners: {
						'select': function (field) {
							this.editActionHandler(field);
						},
						scope: toolbar
					}
				};

				if (comboBox.Items) {
					for (var i = 0, len = comboBox.Items.length; i < len; i++) {
						config.store.data.push({
							id: comboBox.Items[i].Id,
							dsc: comboBox.Items[i].Text
						});
					}
				}

				gxui.tryPropertyMapping(config, comboBox, {
					"gxid": "Id",
					"text": "Text",
					"tooltip": "Tooltip",
					"icon": "Icon",
					"iconCls": "IconCls",
					"rowspan": "RowSpan",
					"colspan": "ColSpan",
					"iconAlign": { property: "IconAlign", ignoreEmpty: true },
					"arrowAlign": { property: "ArrowAlign", ignoreEmpty: true },
					"scale": { property: "Scale", ignoreEmpty: true },
					"width": { property: "Width", ignoreEmpty: true },
					"value": { property: "Value", ignoreEmpty: true },
					"emptyText": { property: "Text", ignoreEmpty: true }
				});

				return config;
			}
		});
	},

	//private
	SetData: function (data) {
		this.Data = data;
	},

	//private
	GetData: function (data) {
		return this.Data;
	},

	//private
	GetToolbar: function (add) {
		return this.m_toolbar;
	},

	onRender: function () {
		this.createToolbar().render(this.getContainerControl());
	},

	onRefresh: function () {
		this.refreshButtons(this.Data.Buttons, this.m_toolbar.items);
	},

	getUnderlyingControl: function () {
		return this.m_toolbar;
	},

	//private
	createToolbar: function (options) {
		var vertical = false,
			itemId;
		if (options) {
			if (options.id) {
				this.getUniqueId = function () {
					return options.id;
				};
			}

			if (options.data) {
				this.SetData(options.data);
			}

			if (options.container) {
				options.container.buttonActionHandler = this.buttonActionHandler;
				options.container.editActionHandler = this.editActionHandler;

				this.buttonActionHandler = Ext.bind(this.buttonActionHandler, options.container);
				this.editActionHandler = Ext.bind(this.editActionHandler, options.container);
			}

			if (options.on) {
				this.on(options.on);
			}

			if (options.vertical)
				vertical = options.vertical;

			itemId = options.itemId;
		}

		var config = {
			id: this.getUniqueId(),
			stateful: false,
			items: this.createButtons(),
			listeners: {},
			vertical: vertical,
			itemId: itemId
		};

		if (options && options.container) {
			config.dock = options.dock || 'top';
		}

		if (!options || !options.container) {
			if (this.Width != 'auto') {
				config.width = parseInt(this.Width, 10);
			}

			if (this.Height != 'auto') {
				config.height = parseInt(this.Height, 10);
			}
		}

		config.listeners['afterrender'] = {
			fn: this.adjustWidth,
			delay: 300
		};
		config.listeners.scope = this;

		this.m_toolbar = Ext.create('Ext.toolbar.Toolbar', config);

		return this.m_toolbar;
	},

	//private
	createButtons: function () {
		var toolbarItems = [];
		if (this.Data && this.Data.Buttons) {
			Ext.each(this.Data.Buttons, function (item, index, allItems) {
				if (!item.Type) {
					item.Type = gxui.Toolbar.ItemType.Button;
				}
				toolbarItems.push(this.getConfig(item));
				if (this.SeparateAll && allItems[index + 1])
					toolbarItems.push('-');
			}, this);
		}
		return toolbarItems;
	},

	//private
	buttonClickHandler: function (btn, e) {
		if (this.fireEvent("beforebuttonpressed", this, btn, e) !== false) {
			if (btn.gxConfirmation) {
				var processResult = function (option, text) {
					if (option == 'ok')
						this.buttonActionHandler(btn, e);
				};

				var msgBox = new Ext.window.MessageBox({
					buttonText: {
						ok: btn.gxConfirmation.OKButtonText,
						cancel: btn.gxConfirmation.CancelButtonText
					},
					listeners: {
						'afterrender': function (mb) {
							// Put focus on Cancel button by default.
							mb.defaultFocus = 3;
						}
					}
				});

				msgBox.show({
					title: btn.gxConfirmation.Title,
					msg: btn.gxConfirmation.Message,
					buttons: Ext.Msg.OKCANCEL,
					fn: processResult,
					scope: this,
					animateTarget: btn.getEl(),
					icon: Ext.Msg.QUESTION
				});
			}
			else {
				this.buttonActionHandler(btn, e);
			}
			this.fireEvent("buttonpressed", this, btn, e);
		}
	},

	//private
	buttonActionHandler: function (btn, e) {
		
		if (this.ButtonPressed) {
			this.ButtonPressedId = btn.gxid;
			this.ButtonPressed();
		}
	},

	//private
	editActionHandler: function (field) {
		this.EditFieldValue = field.getValue();
		this.buttonActionHandler(field);
	},

	//private
	getConfig: function (button) {
		if (button.id && button.id.indexOf("ext") == 0)
			return button;

		if (!button.Type)
			button.Type = gxui.Toolbar.ItemType.Button;

		var resolver = gxui.Toolbar.ItemResolvers.get(button.Type) || gxui.Toolbar.ItemResolvers.get(gxui.Toolbar.ItemType.Button);
		if (resolver)
			return resolver(this, button);
	},

	//private
	defineBtnsDropTarget: function () {
		this.m_toolbar.items.each(function (item, pos) {
			if (item.type == "button" && item.isDropTarget) {
				var dt = new Ext.dd.DropTarget(item.getEl(), { ddGroup: 'GridDD' });
				dt._btn = item;
				dt._scope = this;
				dt.notifyOver = function (source, e, data) {
					if (data.grid) {
						return 'x-dd-drop-ok';
					}
					return 'x-dd-drop-nodrop';
				};
				dt.notifyDrop = function (source, e, data) {
					if (data.grid) {
						this._scope.buttonActionHandler(this._btn);
						return true;
					}
					return false;
				};
				dt.notifyEnter = function (source, e, data) {
					if (data.grid) {
						return 'x-dd-drop-ok';
					}
					return 'x-dd-drop-nodrop';
				};
			}
		},
		this);
	},

	//private
	refreshButtons: function (buttons, renderedButtons) {
		var i = 0;
		var ItemType = gxui.Toolbar.ItemType;
		renderedButtons.each(function (renderedBtn) {
			var button = buttons[i];
			if (button) {
				if (!gxui.CBoolean(button.Disabled) && renderedBtn.disabled) {
					if (renderedBtn.enable)
						renderedBtn.enable();
				}
				else {
					if (gxui.CBoolean(button.Disabled) && !renderedBtn.disabled) {
						if (renderedBtn.disable)
							renderedBtn.disable();
					}
				}

				if (gxui.CBoolean(button.Hidden) && !renderedBtn.hidden) {
					if (renderedBtn.hide)
						renderedBtn.hide();
				}
				else {
					if (!gxui.CBoolean(button.Hidden) && renderedBtn.hidden) {
						if (renderedBtn.show)
							renderedBtn.show();
					}
				}

				if (button.Type == ItemType.Text)
					renderedBtn.setText(button.Text);

				if ((button.Type == ItemType.Menu || button.Type == ItemType.SplitButton) && button.Items && renderedBtn.menu) {
					this.refreshButtons(button.Items, renderedBtn.menu.items);
				}
			}
			i += 1;
		}, this)

		this.adjustWidth(this.m_toolbar);
	},

	//private
	getUniqueButtonId: function (btnId) {
		return this.getUniqueId() + "_btn_" + btnId;
	},

	//private
	findItem: function (id, items) {
		var ItemType = gxui.Toolbar.ItemType;
		var searchedItem;
		Ext.each(items, function (item) {
			if (item.Id == id) {
				searchedItem = item;
			}
			else {
				if ((item.Type == ItemType.Menu || item.type == ItemType.SplitButton) && item.Items) {
					searchedItem = this.findItem(id, item.Items);
				}
			}

			if (searchedItem) {
				return false;
			}
		}, this);
		return searchedItem;
	},

	//private
	changeItemPropertyValue: function (itemId, propertyId, propertyValue) {
		var item = this.findItem(itemId, this.Data.Buttons);
		if (item) {
			item[propertyId] = propertyValue;
		}
		return item;
	},

	//private
	getBtnCls: function (btn) {
		if (!btn.Cls) {
			if (btn.Icon) {
				return (btn.Text) ? "x-btn-text-icon" : "x-btn-icon";
			}
			else {
				return "x-btn-text";
			}
		}
		return btn.Cls;
	},

	adjustWidth: function (toolbar) {
		if (!toolbar.ownerCt) {
			if (this.Width == 'auto') {
				var lastItem = null;
				toolbar.items.each(function (item) {
					if (item.isVisible())
						lastItem = item;
				})

				if (lastItem) {
					toolbar.setWidth(100); // WA
					width = lastItem.el.getLeft(true) + lastItem.el.getWidth() + toolbar.el.getFrameWidth('l r');
					toolbar.setWidth(width);
				}
			}
		}
	},

	methods: {
		// Methods
		
		ChangeToolbar: function (toolbarData, id, container) {
			this.m_toolbar.removeAll();
			this.m_toolbar.add(this.createButtons());
			return this.m_toolbar;
		},

		
		DisableItem: function (itemId) {
			this.changeItemPropertyValue(itemId, "Disabled", true);
			this.refreshButtons(this.Data.Buttons, this.m_toolbar.items);
		},

		
		EnableItem: function (itemId) {
			this.changeItemPropertyValue(itemId, "Disabled", false);
			this.refreshButtons(this.Data.Buttons, this.m_toolbar.items);
		},

		
		HideItem: function (itemId) {
			this.changeItemPropertyValue(itemId, "Hidden", true);
			this.refreshButtons(this.Data.Buttons, this.m_toolbar.items);
		},

		
		ShowItem: function (itemId) {
			this.changeItemPropertyValue(itemId, "Hidden", false);
			this.refreshButtons(this.Data.Buttons, this.m_toolbar.items);
		}
	}
});


gxui.Toolbar.ItemType = {
	Button: "Button",
	Text: "Text",
	Edit: "Edit",
	Fill: "Fill",
	Separator: "Separator",
	Menu: "Menu",
	SplitButton: "SplitButton",
	Group: "Group"
};


gxui.Toolbar.ItemResolvers = {
	// private
	items: {},

	
	register: function (type, resolver) {
		if (typeof (type) == 'string')
			this.items[type] = resolver;
		else if (typeof (type) == 'object') {
			for (var item in type) {
				if (typeof (type[item]) == 'function') {
					this.items[item] = type[item];
				}
			}
		}
	},

	
	unregister: function (type) {
		delete this.items[type];
	},

	
	get: function (type) {
		return this.items[type];
	}
};
/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


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
			
			CollapseRegion: function (regionKey, animate) {
				var region = this.getRegion(regionKey);

				if (region) {
					region.collapse(animate);
					this.setRegionProperty(regionKey, "Collapsed", "true");
				}
			},

			
			ExpandRegion: function (regionKey, animate) {
				var region = this.getRegion(regionKey);

				if (region) {
					region.expand(animate);
					this.setRegionProperty(regionKey, "Collapsed", "false");
				}
			},

			
			HideRegion: function (regionKey) {
				var region = this.getRegion(regionKey);

				if (region) {
					region.hide();
					this.m_layout.doLayout();
					this.setRegionProperty(regionKey, "Hidden", "true");
				}
			},

			
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

/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.Treeview', {
	extend: 'gxui.UserControl',

	initialize: function () {
		this.callParent(arguments);

		this.NotifyContext = "false";
		this.NotifyDataType = 'gxuiTreeNode';
		this.CheckedNodes = [];
	},

	// Databinding
	SetChildren: function (data) {
		this.Children = data;
	},

	// Databinding
	GetChildren: function (data) {
		return this.Children;
	},

	// Databinding
	SetCheckedNodes: function (data) {
		this.CheckedNodes = data;
	},

	// Databinding
	GetCheckedNodes: function (data) {
		this.CheckedNodes = [];
		if (this.m_tree) {
			var checkedNodes = this.m_tree.getChecked();
			this.CheckedNodes = Ext.Array.map(checkedNodes, function (node) {
				return node.data.id;
			}, this);
		}
		return this.CheckedNodes
	},

	// Databinding
	SetUncheckedNodes: function (data) {
		this.UncheckedNodes = data;
	},

	// Databinding
	GetUncheckedNodes: function (data) {
		this.UncheckedNodes = [];
		if (this.m_tree) {
			var root = this.m_tree.getRootNode();
			if (root) {
				var nodes = [];
				root.cascadeBy(function () {
					if (this.data.checked === false) {
						nodes.push(this.data.id);
					}
				});
				this.UncheckedNodes = nodes;
			}
		}
		return this.UncheckedNodes;
	},

	// Databinding
	SetDropData: function (data) {
		this.DropData = data;
	},

	// Databinding
	GetDropData: function (data) {
		return this.DropData;
	},

	// Databinding
	SetSelectedNodeData: function (data) {
		this.SelectedNodeData = data;
	},

	// Databinding
	GetSelectedNodeData: function (data) {
		return this.SelectedNodeData;
	},

	// Databinding
	SetSelectedNodes: function (data) {
		this.SelectedNodes = data;
	},

	// Databinding
	GetSelectedNodes: function () {
		return this.SelectedNodes;
	},

	onRender: function () {
		var Tree = Ext.tree;
		var ddGroup = this.DragDropGroup || undefined;

		this.Width = parseInt(this.Width);
		this.Height = parseInt(this.Height);
		this.EnableCheckbox = gxui.CBoolean(this.EnableCheckbox);
		this.LazyLoading = gxui.CBoolean(this.LazyLoading);

		var store = this.createStore();

		var config = {
			id: this.getUniqueId(),
			title: this.Title,
			frame: gxui.CBoolean(this.Frame),
			border: gxui.CBoolean(this.Border) ? undefined : 0,
			cls: this.Cls,
			animate: gxui.CBoolean(this.Animate),
			rootVisible: gxui.CBoolean(this.RootVisible),
			lines: gxui.CBoolean(this.ShowLines),
			store: store,
			folderSort: gxui.CBoolean(this.Sort),
			viewConfig: {},
			plugins: [],
			autoScroll: gxui.CBoolean(this.AutoScroll),
			stateful: gxui.CBoolean(this.Stateful),
			stateId: (this.StateId && this.StateId != "") ? this.StateId : this.getUniqueId(),
			stateEvents: gxui.CBoolean(this.Stateful) ? ['itemcollapse', 'itemexpand'] : undefined,
			getState: gxui.CBoolean(this.Stateful) ? this.getState : undefined,
			applyState: gxui.CBoolean(this.Stateful) ? this.applyState(this.LazyLoading) : undefined,
			listeners: this.getListeners()
		};

		// @TODO: Change this to use AutoWidth and AutoHeight
		if (this.Width != 100)
			config.width = this.Width;
		else
			config.autoWidth = true;

		if (this.Height != 100)
			config.height = this.Height;
		else
			config.autoHeight = true;

		if (gxui.CBoolean(this.EnableDragDrop)) {
			config.viewConfig.plugins = {
				ptype: 'treeviewdragdrop',
				appendOnly: gxui.CBoolean(this.AppendOnly),
				ddGroup: ddGroup
			};
			config.viewConfig.listeners = this.getDDListeners();
		}

		if (gxui.CBoolean(this.Multiselection)) {
			config.selModel = {
				mode: 'MULTI',
				listeners: {
					'selectionchange': function (selModel, nodes) {
						this.SelectedNodes = [];
						Ext.each(nodes, function (node) {
							this.SelectedNodes.push(node.data.id);
						}, this);
					},
					scope: this
				}
			};
		}

		if (gxui.CBoolean(this.Editable)) {
			// The column has to be explicitly defined, to set editor properties
			config.columns = [{
				xtype: 'treecolumn',
				dataIndex: 'text',
				flex: 1,
				editor: {
					xtype: 'textfield',
					allowBlank: false,
					selectOnFocus: true,
					cancelOnEsc: true,
					ignoreNoChange: true
				}
			}];

			config.hideHeaders = true;

			config.plugins.push({
				ptype: 'cellediting',
				pluginId: this.getUniqueId() + '-celledit',
				clicksToEdit: 2,
				listeners: {
					'edit': function (editor, e) {
						this.NodeEditText = e.value;
						if (this.NodeEdit) {
							this.NodeEdit();
						}
					},
					scope: this
				}
			});
			config.viewConfig.toggleOnDblClick = false;
		}

		this.m_tree = Ext.create('Ext.tree.Panel', config);
	},

	onRefresh: function () {
		var selNodes = this.m_tree.getSelectionModel().getSelection();
		if (selNodes && selNodes[0]) {
			this.setSelectedNode(selNodes[0]);
		}
	},

	onAfterRender: function () {
		if (gxui.CBoolean(this.ExpandRoot)) {
			Ext.defer(function () {
				this.m_tree.getRootNode().expand(gxui.CBoolean(this.ExpandAll));
			}, 300, this);
		}
	},

	getUnderlyingControl: function () {
		return this.m_tree;
	},

	addToParent: function () {
		return gxui.CBoolean(this.AddToParentGxUIControl);
	},

	createRootNode: function () {
		return {
			id: (this.RootId ? this.RootId : 'ROOT'),
			text: this.RootText,
			icon: (this.RootIcon ? this.RootIcon : undefined),
			cls: (this.RootCls ? this.RootCls : undefined),
			iconCls: (this.RootIconCls ? this.RootIconCls : undefined),
			draggable: false, // disable root node dragging
			children: !this.LazyLoading ? this.cloneNodes(this.Children) : undefined,
			expanded: gxui.CBoolean(this.ExpandRoot)
		};
	},

	createStore: function () {
		var config = {
			root: this.createRootNode(),
			_enableCheckbox: this.EnableCheckbox
		};


		if (this.LazyLoading) {
			config.proxy = {
				type: 'ajax',
				url: this.LoaderURL,
				reader: {
					type: 'json'
				},
				actionMethods: {
					create: "POST",
					read: "POST",
					update: "POST",
					destroy: "POST"
				}
			};
		}

		return config;
	},

	cloneNodes: function (children) {
		children = gxui.clone(children);

		return children.length && children.length > 0 ? children : [children]
	},

	getRowDropData: function (data) {
		if (data && data.gxRow) {
			var gxGrid = data.gxGrid,
				gxRow = data.gxRow,
				gxCols = data.gxColumns;

			var dropData = {};
			for (var i = 0, len = gxCols.length; i < len; i++) {
				var col = gxCols[i],
					colName = col.gxAttName || (col.gxAttId.charAt(0) == "&" ? col.gxAttId.substring(1) : col.gxAttId),
					cell = gxGrid.getPropertiesCell(data.gxGrid.getUnderlyingControl(), gxRow.id, i, true);
				dropData[colName] = cell.value;
			}

			return dropData;
		}

		return null;
	},

	getNodeById: function (nodeId) {
		return this.m_tree.getStore().getNodeById(nodeId);
	},

	getDDListeners: function () {
		return {
			'dragover': function (node, data, overModel, dropPosition) {
				if (this.NodeOver) {
					// Set UC properties before fireing the event
					this.DropTarget = overModel.data.id;
					this.DropPoint = dropPosition;

					if (data.gxGrid) {
						this.DropData = this.getRowDropData(data);
					}
					else {
						this.DropNode = data.records[0].data.id;
					}

					this.DropAllowed = true;

					
					this.NodeOver();

					return this.DropAllowed;
				}
				return true;
			},

			'beforedrop': function (node, data, overModel, dropPosition, opts) {
				if (data.gxGrid) {
					this.DropTarget = overModel.data.id;
					this.DropPoint = dropPosition;

					this.DropData = this.getRowDropData(data);
					opts.cancelDrop();

					if (this.RowDrop) {
						this.RowDrop();
					}
				}
			},

			'drop': function (node, data, overModel, dropPosition) {
				this.DropTarget = overModel.data.id;
				this.DropPoint = dropPosition;

				if (data.records.length > 0) {
					this.DropNode = data.records[0].data.id;
					if (this.NodeDrop) {
						
						this.NodeDrop();
					}
				}
			},

			scope: this
		};
	},

	getListeners: function () {
		var listeners = {
			'itemclick': function (view, node, item, index, e) {
				var editorPlugin = this.getEditorPlugin();
				var startEdit = (node.data.id == this.SelectedNode) && editorPlugin;
				this.endEdit();
				this.setSelectedNode(node);
				if (!node.data.href) {
					if (this.NotifyContext == "true") {
						this.notifyContext([this.NotifyDataType], { id: node.data.id, text: node.data.text, leaf: node.data.leaf, icon: node.data.icon });
					}
					if (this.Click && (!node.hasChildNodes() || !gxui.CBoolean(this.DisableBranchEvents))) {
						
						this.Click();
					}
				}
				if (startEdit) {
					editorPlugin.startEdit(node, 0);
				}
			},

			'itemdblclick': function (view, node) {
				this.endEdit();
				this.setSelectedNode(node);
				if (this.DoubleClick && (!node.hasChildNodes() || !gxui.CBoolean(this.DisableBranchEvents))) {
					
					this.DoubleClick();
				}
			},

			'checkchange': function (node) {
				if (this.CheckChange) {
					this.setSelectedNode(node);
					
					this.CheckChange();
				}
			},

			scope: this
		};

		if (this.ContextMenu) {
			listeners['itemcontextmenu'] = function (view, node) {
				this.endEdit();
				if (this.ContextMenu) {
					this.setSelectedNode(node);
					this.m_tree.getSelectionModel().select(node);
					
					this.ContextMenu();
				}
			};
		}

		return listeners;
	},

	setSelectedNode: function (node) {
		this.SelectedNode = node.data.id;
		this.SelectedText = node.data.text;
		this.SelectedIcon = node.data.icon;
		this.SelectedNodeData = node.data.data;
		this.SelectedNodeChecked = node.data.checked || false;
	},

	getEditorPlugin: function () {
		return this.m_tree.getPlugin(this.getUniqueId() + '-celledit');
	},

	endEdit: function () {
		var editorPlugin = this.getEditorPlugin();
		if (editorPlugin) {
			editorPlugin.completeEdit();
		}
	},

	getState: function () {
		var nodes = [], state = Ext.tree.Panel.prototype.getState.apply(this, arguments);
		this.getRootNode().eachChild(function (child) {
			//function to store state of tree recursively
			var storeTreeState = function (node, expandedNodes) {
				if (node.isExpanded() && node.childNodes.length > 0) {
					expandedNodes.push(node.getPath("id"));
					node.eachChild(function (child) {
						storeTreeState(child, expandedNodes);
					});
				}
			};
			storeTreeState(child, nodes);
		});

		Ext.apply(state, {
			expandedNodes: nodes
		});

		return state;
	},

	applyState: function (lazyLoading) {
		return function (state) {
			var nodes = state.expandedNodes || [],
				len = nodes.length;

			var expandNodes = Ext.bind(function () {
				for (var i = 0; i < len; i++) {
					if (typeof nodes[i] != 'undefined') {
						this.expandPath(nodes[i], 'id');
					}
				}
				Ext.tree.Panel.prototype.applyState.call(this, state)
			}, this);
			
			this.collapseAll();
			if (lazyLoading) {
				setTimeout(expandNodes, 100);
			}
			else {
				expandNodes();
			}
		};
	},

	findChildNode: function (id, nodes) {
		for (var i = 0, len = nodes.length; i < len; i++) {
			if (nodes[i].id == id) {
				return nodes[i];
			}
			if (nodes[i].children.length > 0) {
				var node = this.findChildNode(id, nodes[i].children);
				if (node) {
					return node;
				}
			}
		}
		return null;
	},

	setNodeProperty: function (nodeId, name, value) {
		var node = this.getNodeById(nodeId);
		if (node) {
			node.set(name, value);
		}
	},

	methods: {
		// Methods
		
		SelectNode: function (nodeId) {
			var node = this.getNodeById(nodeId);
			if (node) {
				this.setSelectedNode(node)
				this.m_tree.getSelectionModel().select(node);
				this.m_tree.expandPath(node.getPath("id"), "id");
			}
		},

		
		SelectNextNode: function () {
			this.m_tree.getSelectionModel().selectNext();
		},

		
		SelectPreviousNode: function () {
			this.m_tree.getSelectionModel().selectPrevious();
		},

		
		DeleteNode: function (nodeId) {
			var node = this.getNodeById(nodeId);
			if (node) {
				node.remove();
			}
		},

		
		ExpandNode: function (nodeId) {
			var node = this.getNodeById(nodeId);
			if (node) {
				node.expand();
			}
		},

		
		ExpandAllNodes: function () {
			this.m_tree.expandAll();
		},

		
		CollapseNode: function (nodeId) {
			var node = this.getNodeById(nodeId);
			if (node) {
				node.collapse();
			}
		},

		
		CollapseAllNodes: function () {
			this.m_tree.collapseAll();
		},

		
		Reload: function (node, expand) {
			var tree = this.m_tree;
			// node can be a TreeNode or a String with the Id of a node. If node is undefined, the root node is reloaded.
			var n = node ? ((typeof node == 'object') ? node : this.getNodeById(node)) : tree.getRootNode();

			if (n) {
				var loadCallback = function (node, initState) {
					node = node || n;
					if (expand || expand === undefined) {
						node.expand();
					}
					if (initState !== false) {
						tree.initState();
					}
				};

				var store = tree.getStore();
				if (this.LazyLoading) {
					var loadCfg = {
						callback: Ext.bind(loadCallback, this),
						node: n
					};

					store.getProxy().url = this.LoaderURL;
					if (store.isLoading())
						Ext.defer(store.load, 500, store, [loadCfg]);
					else
						store.load(loadCfg);
				}
				else {
					if (n == tree.getRootNode()) {
						tree.setRootNode(this.createRootNode());
					}
					else {
						var rawNode = this.findChildNode(node, this.Children),
							newNode = n.parentNode.replaceChild(rawNode, n);
					}
					loadCallback(newNode, false);
				}

				if (this.SelectedNode != undefined) {
					this.SelectNode(this.SelectedNode);
				}
			}
		},

		
		Refresh: function () {
			var tree = this.m_tree;
			tree.setHeight((this.Height != 100) ? this.Height : undefined);
			tree.setWidth((this.Width != 100) ? this.Width : undefined);
			tree.setTitle(this.Title);
			this.Reload(tree.getRootNode(), gxui.CBoolean(this.ExpandRoot));
		},

		
		Show: function () {
			this.m_tree.setVisible(true);
		},

		
		Hide: function () {
			this.m_tree.setVisible(false);
		},

		
		GetNodeParentId: function (nodeId) {
			var node = this.getNodeById(nodeId);
			if (node && node.parentNode) {
				return node.parentNode.data.id;
			}
			return "";
		},

		
		SetNodeData: function (nodeId, nodeData) {
			var node = this.getNodeById(nodeId);
			if (node) {
				node.data.data = nodeData;
			}
		},

		
		GetNodeData: function (nodeId) {
			var node = this.getNodeById(nodeId);
			if (node) {
				return node.data.data;
			}
		},

		
		SetNodeText: function (nodeId, text) {
			this.setNodeProperty(nodeId, 'text', text);
		},


		
		SetNodePropertyBoolean: function (nodeId, name, value) {
			this.setNodeProperty(nodeId, name, value);
		},

		
		SetNodePropertyString: function (nodeId, name, value) {
			this.setNodeProperty(nodeId, name, value);
		},

		
		SetNodePropertyNumber: function (nodeId, name, value) {
			this.setNodeProperty(nodeId, name, value);
		},

		
		StartEdit: function (nodeId, value) {
			var node = this.getNodeById(nodeId),
				editorPlugin;
			if (node) {
				editorPlugin = this.getEditorPlugin();
				if (editorPlugin) {
					if (value !== undefined) {
						editorPlugin.on({
							'beforeedit': function (editor, e) {
								e.value = value;
							},
							single: true
						});
					}
					editorPlugin.startEdit(node, 0);
				}
			}
		},

		
		CancelEdit: function () {
			var editorPlugin = this.getEditorPlugin();
			if (editorPlugin) {
				editorPlugin.cancelEdit();
			}
		},

		
		ClearAllNodes: function () {
			var root = this.m_tree.getRootNode();
			root.removeAll();
			delete root.data.children;
		},

		
		NodeExists: function (nodeId) {
			var node = this.getNodeById(nodeId);
			return (node ? true : false);
		}
	}
});

// isValidDropPoint is overriden to be able to fire dragover event.
Ext.tree.ViewDropZone.override({
	isValidDropPoint: function (node, position, dragZone, e, data) {
		if (this.callOverridden(arguments) === false)
			return false;

		if (this.view.fireEvent('dragover', node, data, this.view.getRecord(node), position, e) === false)
			return false;

		return true;
	}
});

Ext.data.TreeStore.override({
	fillNode: function (node, records) {
		if (records) {
			for (var i = 0, len = records.length; i < len; i++) {
				var record = records[i];
				if (!this._enableCheckbox) {
					delete record.raw.checked;
					record.data.checked = null;
				}
				if (record.raw.leaf === false && record.raw.children && record.raw.children.length == 0)
					delete record.raw.children;
			}
		}
		return this.callOverridden(arguments);
	}
});

Ext.define('Ext.ux.form.field.DateTime', {
	extend: 'Ext.form.FieldContainer',
	mixins: {
		field: 'Ext.form.field.Field'
	},
	alias: 'widget.xdatetime',

	//configurables

	combineErrors: true,
	msgTarget: 'under',
	layout: 'hbox',
	readOnly: false,

	
	dateFormat: 'Y-m-d',
	
	timeFormat: 'H:i:s',
	//    
	//    dateTimeFormat: 'Y-m-d H:i:s',
	
	dateConfig: {},
	
	timeConfig: {},


	// properties

	dateValue: null, // Holds the actual date
	
	dateField: null,
	
	timeField: null,

	initComponent: function () {
		var me = this
            , i = 0
            , key
            , tab;

		me.items = me.items || [];

		me.dateField = Ext.create('Ext.form.field.Date', Ext.apply({
			format: me.dateFormat,
			flex: 1,
			isFormField: false, //exclude from field query's
			submitValue: false
		}, me.dateConfig));
		me.items.push(me.dateField);

		me.timeField = Ext.create('Ext.form.field.Time', Ext.apply({
			format: me.timeFormat,
			flex: 1,
			isFormField: false, //exclude from field query's
			submitValue: false
		}, me.timeConfig));
		me.items.push(me.timeField);

		for (; i < me.items.length; i++) {
			me.items[i].on('focus', Ext.bind(me.onItemFocus, me));
			me.items[i].on('blur', Ext.bind(me.onItemBlur, me));
			me.items[i].on('specialkey', function (field, event) {
				key = event.getKey();
				tab = key == event.TAB;

				if (tab && me.focussedItem == me.dateField) {
					event.stopEvent();
					me.timeField.focus();
					return;
				}

				me.fireEvent('specialkey', field, event);
			});
		}

		me.callParent();

		// this dummy is necessary because Ext.Editor will not check whether an inputEl is present or not
		//		this.inputEl = {
		//			dom: {},
		//			swallowEvent: function () { }
		//		};

		me.initField();
	},

	focus: function () {
		this.callParent();
		Ext.defer(this.dateField.focus, 100, this.dateField);
	},

	onItemFocus: function (item) {
		if (this.blurTask) {
			this.blurTask.cancel();
		}
		this.focussedItem = item;
	},

	onItemBlur: function (item, e) {
		
	},

	getValue: function () {
		var value = null
            , date = this.dateField.getSubmitValue()
            , time = this.timeField.getSubmitValue()
            , format;

		if (date) {
			if (time) {
				format = this.getFormat();
				value = Ext.Date.parse(date + ' ' + time, format);
			} else {
				value = this.dateField.getValue();
			}
		}
		return value;
	},

	getSubmitValue: function () {
		//        var value = this.getValue();
		//        return value ? Ext.Date.format(value, this.dateTimeFormat) : null;

		var me = this
            , format = me.getFormat()
            , value = me.getValue();

		return value ? Ext.Date.format(value, format) : null;
	},

	setValue: function (value) {
		if (Ext.isString(value)) {
			value = Ext.Date.parse(value, this.getFormat()); //this.dateTimeFormat
		}
		this.dateField.setValue(value);
		this.timeField.setValue(value);
	},

	getFormat: function () {
		return (this.dateField.submitFormat || this.dateField.format) + " " + (this.timeField.submitFormat || this.timeField.format);
	},

	// Bug? A field-mixin submits the data from getValue, not getSubmitValue
	getSubmitData: function () {
		var me = this
            , data = null;

		if (!me.disabled && me.submitValue && !me.isFileUpload()) {
			data = {};
			data[me.getName()] = '' + me.getSubmitValue();
		}
		return data;
	},

	isValid: function () {
		return this.dateField.isValid() && this.timeField.isValid();
	}
});


Ext.define('Ext.ux.CheckColumn', {
    extend: 'Ext.grid.column.Column',
    alias: 'widget.checkcolumn',
    
    constructor: function() {
        this.addEvents(
            
            'checkchange'
        );
        this.callParent(arguments);
    },

    
    processEvent: function(type, view, cell, recordIndex, cellIndex, e) {
        if (type == 'mousedown' || (type == 'keydown' && (e.getKey() == e.ENTER || e.getKey() == e.SPACE))) {
            var record = view.panel.store.getAt(recordIndex),
                dataIndex = this.dataIndex,
                checked = !record.get(dataIndex);
                
            record.set(dataIndex, checked);
            this.fireEvent('checkchange', this, recordIndex, checked);
            // cancel selection.
            return false;
        } else {
            return this.callParent(arguments);
        }
    },

    // Note: class names are not placed on the prototype bc renderer scope
    // is not in the header.
    renderer : function(value){
        var cssPrefix = Ext.baseCSSPrefix,
            cls = [cssPrefix + 'grid-checkheader'];

        if (value) {
            cls.push(cssPrefix + 'grid-checkheader-checked');
        }
        return '<div class="' + cls.join(' ') + '">&#160;</div>';
    }
});



Ext.define('Skirtle.grid.AutoWidther', {
	mixins: { observable: 'Ext.util.Observable' },

	pendingCount: 0,

	constructor: function (config) {
		var me = this;

		Ext.apply(me, config);

		me.addEvents('beforecolumnresize', 'columnresize');
		me.mixins.observable.constructor.call(me);
	},

	autoWidthColumn: function (column, config) {
		var me = this,
            grid = me.grid,
            empty = grid.getStore().getCount() === 0,
            els,
            originalColumnElWidth,
            newWidth;

		config = config || {};

		// Don't resize hidden columns. Arguably this an overly simplistic approach but at least it has good performance
		if (column.isHidden() || !grid.rendered) {
			return false;
		}

		if (empty) {
			if (!Ext.isNumber(newWidth = me.getEmptyWidth(column, config))) {
				return false;
			}
		}
		else {
			newWidth = (config.includeHeader && me.calculateHeaderWidth(column, config)) || 1;

			// Can be multiple, e.g. using the grouping feature
			els = me.getColumnResizers(column, config);

			if (els.length) {
				// Reset the styles on the table so it uses auto sizing, unless this has already been done elsewhere
				me.start();

				Ext.each(els, function (el) {
					el = Ext.fly(el);

					
					originalColumnElWidth = el.dom.style.width;
					el.setStyle('width', 'auto');

					newWidth = Math.max(el.getWidth(), newWidth);

					// Put it back the way we found it
					el.setStyle('width', originalColumnElWidth);
				});

				// Put the table back the way we found it
				me.end();
			}
		}

		config.newWidth = newWidth;

		if (me.beforeColumnResize(column, config) === false
            || me.fireEvent('beforecolumnresize', me, column, config) === false) {
			return false;
		}

		// flex takes priority over width, so remove it
		if (column.flex) {
			column.flex = null;
		}

		column.setWidth(config.newWidth, !me.isPending());

		// Required if the column was previously flexed as setWidth thinks the width is already correct
		column.width = config.newWidth;

		me.onColumnResize(column, config);
		me.fireEvent('columnresize', me, column, config);

		return config.newWidth;
	},

	// TODO: Should this be empty?
	beforeColumnResize: function (column, config) {
		if (config.minWidth) {
			config.newWidth = Math.max(config.newWidth, config.minWidth);
		}

		if (config.maxWidth) {
			config.newWidth = Math.min(config.newWidth, config.maxWidth);
		}
	},

	calculateHeaderWidth: function (column, config) {
		// TODO: Verify the need for all the null ternary checks
		var me = this,
            grid = me.grid,
            selector = me.headerCellSelector(column, config),
            triggerSelector = me.headerTriggerSelector(column, config),
            el = grid.getEl(),
            headerCell = el.down(selector),
            trigger = headerCell ? headerCell.down(triggerSelector) : null;

		return headerCell
            ? me.calculateWidth(headerCell) + (trigger ? trigger.getComputedWidth() : 0)
            : false;
	},

	// TODO: Currently only used for the header... is it really necessary?
	calculateWidth: function (cell) {
		var el = Ext.fly(cell);

		return el.getTextWidth() + el.getFrameWidth('lr');
	},

	destroy: function () {
		this.clearListeners();
		this.grid = null;
	},

	// Called when resizing is complete to set the styles on the table elements back to what they were at the start
	end: function () {
		if (! --this.pendingCount) {
			Ext.iterate(this.originalWidths, function (id, width) {
				var tableEl = Ext.fly(id);

				tableEl.setStyle('table-layout', '');
				tableEl.setWidth(width);
			});
		}
	},

	getColumnResizers: function (column, config) {
		// Grab the <th> rows (one per table) that are used to size the columns
		// TODO: can't assume x- prefix
		var els = this.grid.getEl().query('.x-grid-col-resizer-' + column.id);

		// Grouping feature - first table wraps everything and needs to be ignored
		if (els.length > 1 && Ext.fly(els[0]).parent('table').contains(els[1])) {
			els.shift();
		}

		return els;
	},

	getEmptyWidth: function (column, config) {
		if (config.emptyWidth === 'header') {
			return this.calculateHeaderWidth(column, config);
		}

		return config.emptyWidth;
	},

	getTableResizers: function () {
		// TODO: can't assume x- prefix
		var els = this.grid.getView().getEl().query('.x-grid-table-resizer');

		// Grouping feature - first table wraps everything and can be ignored
		if (els.length > 1 && Ext.fly(els[0]).contains(els[1])) {
			els.shift();
		}

		return els;
	},

	headerCellSelector: function (column, config) {
		// TODO: can't assume x- prefix
		// TODO: Should we even use a selector for this?
		return Ext.String.format('#{0} .x-column-header-inner', column.id);
	},

	headerTriggerSelector: function (column, config) {
		// TODO: can't assume x- prefix
		// TODO: Should we even use a selector for this?
		return '.x-column-header-trigger';
	},

	isPending: function () {
		return this.pendingCount !== 0;
	},

	onColumnResize: Ext.emptyFn,

	// Sets the table element for the grid to automatic width... must be reset using endAutoWidth when we're done
	start: function () {
		var me = this;

		if (!me.pendingCount++) {
			me.originalWidths = {};

			Ext.each(me.getTableResizers(), function (tableEl) {
				tableEl = Ext.fly(tableEl);

				me.originalWidths[Ext.id(tableEl)] = tableEl.getWidth();

				tableEl.setStyle({
					'table-layout': 'auto',
					width: 'auto'
				});
			});
		}
	}
});





Ext.define('Skirtle.grid.plugin.AutoWidthColumns', {
	alias: 'plugin.autowidthcolumns',
	extend: 'Ext.AbstractPlugin',
	mixins: { observable: 'Ext.util.Observable' },
	requires: ['Skirtle.grid.AutoWidther'],

	// private
	cfgCopy: ['emptyWidth', 'includeHeader', 'maxWidth', 'minWidth'],

	// The default width for all auto-sized columns when there are no rows
	emptyWidth: 'header',

	// Whether or not the header text should be considered as content in the column
	includeHeader: true,

	//
	pluginId: 'autowidthcolumns',

	init: function (grid) {
		var me = this;

		me.addEvents('beforecolumnresize', 'columnresize');
		me.mixins.observable.constructor.call(me);

		me.bindGrid(grid);
	},

	autoWidthColumn: function (col, configOverrides) {
		var me = this,
            column = Ext.isNumber(col) ? me.getCmp().columns[col] : col,
            config = me.resolveColumnConfig(column, configOverrides),
            width = me.autoWidther.autoWidthColumn(column, config);

		// width can also be false if no resize occurred
		if (width) {
			me.handleSingle(column);
		}

		return width;
	},

	bindGrid: function (grid) {
		var me = this,
            disabled = me.disabled,
            autoWidther;

		// Removes listeners from the current grid, if any
		me.disable();

		me.cmp = grid;

		if (autoWidther = me.autoWidther) {
			autoWidther.destroy();
		}

		if (autoWidther = me.autoWidther = grid ? me.createAutoWidther(grid) : null) {
			me.registerRelayedEvents(autoWidther);
		}

		if (!disabled) {
			// Adds listeners to the new grid
			me.enable();
		}
	},

	createAutoWidther: function (grid) {
		return Ext.create('Skirtle.grid.AutoWidther', Ext.apply({
			grid: grid
		}, this.autoWidtherConfig));
	},

	destroy: function () {
		this.bindGrid(null);
		this.clearListeners();
	},

	disable: function () {
		var me = this,
            grid = me.getCmp();

		if (grid) {
			me.unregisterListeners(grid);
		}

		me.callParent();
	},

	enable: function () {
		var me = this,
            grid = me.getCmp();

		if (me.disabled && grid) {
			me.registerListeners(grid);
		}

		me.callParent();
	},

	getGridViewChangeEvents: function () {
		return ['refresh', 'itemadd', 'itemremove', 'itemupdate'];
	},

	handleSingle: function (column) {
		if (column.autoWidth === 'single') {
			column.autoWidth = false;
		}
	},

	// Handler for the grid's columnshow event
	onGridColumnShow: function (ct, column) {
		if (column.autoWidth) {
			this.autoWidthColumn(column);
		}
	},

	// Handler for the grid view's refresh event
	onGridViewChange: function () {
		this.refresh();
	},

	registerListeners: function (grid) {
		var me = this;

		Ext.each(me.getGridViewChangeEvents(), function (eventName) {
			grid.getView().on(eventName, me.onGridViewChange, me);
		});

		grid.on('columnshow', me.onGridColumnShow, me);
	},

	// TODO: Tidy this up a bit
	registerRelayedEvents: function (autoWidther) {
		var me = this;

		autoWidther.on('beforecolumnresize', function (aw, column, config) {
			return me.fireEvent('beforecolumnresize', me, column, config);
		});

		autoWidther.on('columnresize', function (aw, column, config) {
			return me.fireEvent('columnresize', me, column, config);
		});
	},

	refresh: function () {
		var me = this,
            grid = me.getCmp(),
            columns = grid.columns,
            index = 0,
            len = columns.length,
            updated = false;

		if (!grid.rendered || !grid.getView().rendered) {
			return;
		}

		me.autoWidther.start();

		for (; index < len; ++index) {
			var column = columns[index];

			if (column.autoWidth && me.autoWidthColumn(column)) {
				updated = true;
			}
		}

		me.autoWidther.end();

		if (updated) {
			// TODO: Not always necessary and can be very time-consuming
			grid.headerCt.doLayout();
		}
	},

	resolveColumnConfig: function (column, configOverrides) {
		var config = {},
            me = this;

		if (configOverrides) {
			Ext.apply(config, configOverrides);
		}

		if (Ext.isObject(column.autoWidth)) {
			Ext.applyIf(config, column.autoWidth);
		}

		Ext.each(me.cfgCopy, function (prop) {
			if (!(prop in config)) {
				config[prop] = me[prop];
			}
		});

		return config;
	},

	unregisterListeners: function (grid) {
		var me = this;

		Ext.each(me.getGridViewChangeEvents(), function (eventName) {
			grid.getView().un(eventName, me.onGridViewChange, me);
		});

		grid.un('columnshow', me.onGridColumnShow, me);
	}
});
/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />

Ext.define('gxui.GridExtension.DragDrop', {
	constructor: function (gridUC, grid, cfg) {
		Ext.apply(this, cfg || {});

		this.gridUC = gridUC;
		this.m_grid = grid;

		this.configGridDragZone();
	},

	configGridDragZone: function () {
		var grid = this.m_grid;
		var ddPlugin = grid.getView().getPlugin(this.gridUC.getUniqueId() + '-dd');
		if (ddPlugin && ddPlugin.dragZone) {
			var dragZone = ddPlugin.dragZone;
			dragZone.onInitDrag = Ext.bind(this.initializeDrag, this);
			dragZone.onBeforeDrag = Ext.bind(this.onBeforeDrag, this);
			dragZone.onStartDrag = function () { console.log('onStartDrag', arguments); }
			// If I want to show visual feedback when a row being dragged hovers a valid drop target, the group of valid
			// drop targets must be intialized using a Ext.dd.DropZone. Must be done afterShow, so the target elements
			// exist in the dom.
			this.defineDropTargets();

			dragZone.primaryButtonOnly = gx.lang.gxBoolean(this.PrimaryButtonOnly);
		}
	},

	initializeDrag: function () {
		
		if (this.gridUC.OnInitDrag) {
			this.gridUC.OnInitDrag();
		}

		var ddPlugin = this.m_grid.getView().getPlugin(this.gridUC.getUniqueId() + '-dd');
		if (ddPlugin && ddPlugin.dragZone) {
			var dragZone = ddPlugin.dragZone;
			dragZone.ddel.update(this.DragDropText || ddPlugin.dragText);
			dragZone.proxy.update(dragZone.ddel.dom);
		}
	},

	onBeforeDrag: function (data, e) {
		var dnd = gx.fx.dnd;
		var grid = this.m_grid,
			gridUC = this.gridUC,
			record = grid.getView().getRecord(data.item),
			rowIndex = grid.getView().indexOf(data.item),
			actualRowIndex = gridUC.getActualRowIndex(grid, rowIndex),
			row = gridUC.rows[actualRowIndex],
			dragSource = this.getGxRowDragSource(row);

		if (dragSource) {
			dnd.dragInfo = function () { return ""; }; // Override this function to avoid standard GX dd proxy
			dnd.dragCtrl = data.ddel.dom;
			dnd.dragCtrl.gxId = row.gxId;
			dnd.dragCtrl.gxGrid = gridUC.ownerGrid.containerName;
			dnd.dragCtrl.gxGridName = gridUC.ownerGrid.gridName;
			dnd.dragCtrl.gxDndClassName = dragSource.cssClass;
			// Set the row as the dragged object
			dnd.drag(dragSource.obj, dragSource.types, dragSource.hdl);

			// Set the internal GX row in the data so it can be accessed in beforenodedrop in Treeview.
			data.gxGrid = gridUC;
			data.gxRow = row;
			data.gxColumns = gridUC.columns;
		}
		return true;
	},

	defineDropTargets: function () {
		if (!this.m_dropTargetsCreated) {
			this.m_dropTargets = {};
			var dnd = gx.fx.dnd;
			if (dnd.sources.length > 0) {
				// Get the types of the grids rows
				var types = dnd.sources[0].types;
				Ext.each(dnd.targets, function (t) {
					// If the target types matches the types of the grid rows
					if (gx.fx.matchingTypes(types, t.types)) {
						this.m_dropTargets[t.id] = new Ext.dd.DropTarget(Ext.get(t.id), {
							ddGroup: this.gridUC.DragDropGroup,

							notifyOver: function () {
								return this.dropAllowed;
							},

							notifyDrop: function () {
								return true;
							}

						});
					}
				}, this);
				this.m_dropTargetsCreated = true;
			}
		}
	},

	getGxRowDragSource: function (row) {
		var trId = this.gridUC.getRowGxInternalId(row);

		var dragSource;
		// Find the drag source for the row
		Ext.each(gx.fx.dnd.sources, function (s) {
			if (s.id == trId) {
				dragSource = s;
				return false;
			}
		});

		return dragSource;
	},

	destroy: function () {
		delete this.gridUC;
		delete this.m_grid;

		if (this.m_dropTargets) {
			for (dropTarget in this.m_dropTargets) {
				dropTarget.unreg();
			}
		}
		delete this.m_dropTargets;
	}
});

/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.GridExtension', {
	extend: 'gxui.UserControl',

	onRender: function () {
		var cmConf = this.getColumnsConfig(),
			storeConf = this.getStoreConfig(cmConf.fields),
			smConf = this.getSelModelConfig(),
			viewConf = this.getViewConfig(),
			plugins = this.getPlugins(),
			features = this.getFeatures();

		// mask and unmask methods are overriden to avoid the default GX mask
		this.mask = Ext.emptyFn;
		this.unmask = Ext.emptyFn;

		// create the Grid
		this.m_grid = this.createGridPanel(cmConf, storeConf, smConf, viewConf, plugins, features);
	},

	onRefresh: function () {
		if ((!this.editable && this.isEditable(true)) || this.columnModelChanged(this.m_grid)) {
			this.m_grid.destroy();
			this.forceRendering();
			this.renderControl();
			this.keepSelection(this.m_grid);
		}
		else {
			var grid = this.m_grid,
				view = grid.getView();

			// Toggle summary row
			if (gx.lang.gxBoolean(this.Grouping)) {
				var groupingFeature = view.getFeature(this.getUniqueId() + '-grouping');
				if (groupingFeature && groupingFeature.ftype == 'groupingsummary')
					groupingFeature.toggleSummaryRow(gx.lang.gxBoolean(this.ShowGroupingSummary));
			}

			grid.getStore().loadRawData(this.properties);

			this.updatePagingToolbar(grid.getDockedComponent('toolbar'));

			this.keepSelection(grid);

			if (gx.lang.gxBoolean(this.gxAllowCollapsing)) {
				if (gx.lang.gxBoolean(this.gxCollapsed)) {
					grid.collapse();
				}
				else {
					grid.expand();
				}
			}

			if (this.Visible != undefined) {
				if (gx.lang.gxBoolean(this.gxVisible) && !grid.isVisible()) {
					grid.show();
				}
				else {
					if (!gx.lang.gxBoolean(this.gxVisible) && grid.isVisible()) {
						grid.hide();
					}
				}
			}

			if (!grid.ownerCt) {
				if (this.gxHeight && grid.getBox().height != this.gxHeight) {
					grid.setHeight(this.gxHeight);
				}

				if (this.gxWidth && grid.getBox().width != this.gxWidth) {
					grid.setWidth(this.gxWidth);
				}
			}

			grid.setTitle(this.Title);
		}
	},

	onAfterRender: function () {
		// D&D listeners:
		if (gx.lang.gxBoolean(this.ownerGrid.defaultDragable)) {
			this.m_DD = Ext.create('gxui.GridExtension.DragDrop', this, this.m_grid, {
				DragDropText: this.DragDropText,
				PrimaryButtonOnly: this.PrimaryButtonOnly
			});
		}
	},

	onDestroy: function () {
		if (this.m_DD) {
			this.m_DD.destroy();
		}
		gxui.GridExtension.superclass.onDestroy.call(this);
	},

	getUnderlyingControl: function () {
		return this.m_grid;
	},

	addToParent: function () {
		return gx.lang.gxBoolean(this.AddToParentGxUIControl);
	},

	createGridPanel: function (cmConf, storeConf, smConf, viewConf, plugins, features) {
		this.gxWidth = parseInt(this.gxWidth);
		this.gxHeight = parseInt(this.gxHeight);
		var config = {
			id: this.getUniqueId(),
			features: features,
			plugins: plugins,
			store: storeConf,
			columns: cmConf.columns,
			viewConfig: viewConf,
			selType: smConf.selType,
			selModel: smConf.selModel,
			dockedItems: [],
			//cls: this.gx.CssClass,
			forceFit: gx.lang.gxBoolean(this.ForceFit),
			enableColumnHide: gx.lang.gxBoolean(this.EnableColumnHide),
			enableColumnMove: gx.lang.gxBoolean(this.EnableColumnMove),
			enableLocking: gx.lang.gxBoolean(this.EnableColumnLocking),
			collapsible: gx.lang.gxBoolean(this.gxAllowCollapsing),
			collapsed: gx.lang.gxBoolean(this.gxCollapsed),
			header: gx.lang.gxBoolean(this.gxAllowCollapsing),
			height: this.gxHeight ? this.gxHeight : undefined,
			width: this.gxWidth ? this.gxWidth : undefined,
			title: this.Title ? this.Title : undefined,
			listeners: this.gridListeners(),
			stateful: gx.lang.gxBoolean(this.Stateful),
			stateId: this.StateId || undefined
		};

		if (gx.lang.gxBoolean(this.UseThemeClasses)) {
			config.cls = (config.cls || '') + ' ' + this.gxCssClass;
		}

		if (this.usePagingToolbar()) {
			config.dockedItems.push(this.getPagingToolbarConfig());
		}

		var grid = Ext.create('Ext.grid.GridPanel', config);

		return grid;
	},

	getPlugins: function () {
		var plugins = [{ ptype: 'autowidthcolumns'}];

		if (this.isEditable()) {
			var editingPlugin = {
				clicksToEdit: parseInt(this.ClicksToEdit),
				listeners: {
					'edit': this.afterEditHandler,
					'beforeedit': this.beforeEditHandler,
					scope: this
				}
			};

			plugins.push(editingPlugin);

			if (this.EditModel == 'CellEditModel') {
				editingPlugin.ptype = 'cellediting';
				editingPlugin.pluginId = this.getUniqueId() + '-celledit';
			}
			else {
				editingPlugin.ptype = 'rowediting';
				editingPlugin.pluginId = this.getUniqueId() + '-rowedit';
			}
		}

		return plugins;
	},

	getFeatures: function () {
		var features = [];
		if (gx.lang.gxBoolean(this.Grouping)) {
			features.push({
				id: this.getUniqueId() + '-grouping',
				ftype: gx.lang.gxBoolean(this.ShowGroupingSummary) ? 'groupingsummary' : 'grouping',
				groupHeaderTpl: this.GroupTemplate
			});
		}
		return features;
	},

	getSelModelConfig: function () {
		if (this.isEditable() && this.EditModel == 'CellEditModel') {
			return {
				selType: 'cellmodel'
			};
		}
		else if (this.SelectionModel == 'CheckBoxSelectionModel')
			return {
				selType: 'checkboxmodel',
				selModel: {
					injectCheckbox: 'first',
					mode: 'SINGLE'
				}
			};
		else
			return {
				selType: 'rowmodel',
				selModel: {
					mode: 'SINGLE'
				}
			};
	},

	getViewConfig: function () {
		var viewConf = {
			plugins: [],
			disableSelection: !gx.lang.gxBoolean(this.gxAllowSelection),
			trackOver: gx.lang.gxBoolean(this.gxAllowHovering),
			stripeRows: (this.gxTitleBackstyle == gx.grid.styles.report),
			enableTextSelection: gx.lang.gxBoolean(this.EnableTextSelection)
		};

		if (gx.lang.gxBoolean(this.ownerGrid.defaultDragable) && gx.lang.gxBoolean(this.gxAllowSelection))
			viewConf.plugins.push({
				pluginId: this.getUniqueId() + '-dd',
				ptype: 'gridviewdragdrop',
				enableDrop: false,
				ddGroup: this.DragDropGroup || "",
				dragText: this.DragDropText
			});

		return viewConf;
	},

	getColumnsConfig: function () {
		var columns = this.columns;

		var conf = {
			headers: {},
			columns: [],
			fields: []
		};

		for (var i = 0, len = this.columns.length; i < len; i++) {
			this.mapColumn(this.columns[i], i, conf);
		}

		delete conf.headers;

		return conf;
	},

	getColumnType: function (col) {
		if (col.gxControl.type == gx.html.controls.types.checkBox)
			return 'boolean';

		switch (col.type) {
			case gx.types.numeric:
				return "float";
			case gx.types.bool:
				return "boolean";
			case gx.types.date:
				return "date";
			case gx.types.dateTime:
				return "date";
			default:
				return "string";
		};
	},

	converter: {
		date: function (v) {
			var gxdate;
			if (typeof v == 'string' || v instanceof Date)
				gxdate = new gx.date.gxdate(v);
			else if (v instanceof gx.date.gxdate)
				gxdate = v
			gxdate.Value.gxdate = gxdate;
			return gxdate.Value;
		},

		checkBox: function (v, record, col) {
			if (typeof v == 'boolean')
				return v;
			if (v == col.gxChecked)
				return true;
			return false;
		}
	},

	findBcColumnName: function (bc, vStruct) {
		for (var m in bc) {
			if (bc[m] === vStruct)
				return m;
			if (bc[m].gxgrid || bc[m].ctrltype)
				continue;
			if (typeof (bc[m]) === 'object')
				return this.findBcColumnName(bc[m], vStruct);
		}
		return undefined;
	},

	resolveColumnName: function (col) {
		var ownerGrid = this.ownerGrid,
			boundColName = ownerGrid.boundedCollName,
			parentObject = this.parentGxObject,
			gridBCs = parentObject.GridBCs,
			gxControl = col.gxControl;

		if (boundColName) {
			if (gridBCs) {
				for (var m in gridBCs) {
					if (typeof (gridBCs[m]) === 'object' && gridBCs[m].gxvar == boundColName) {
						return this.findBcColumnName(gridBCs[m], gxControl.vStruct || parentObject.getValidStructFld(gxControl.column.htmlName)) || col.gxAttName || col.gxAttId;
					}
				}
			}

			// WA for Evo1, to be able to group complex SDT bound grids. As it's not possible to get the column name, the control name in upper case is used instead.
			return col.htmlName;
		}

		return col.gxAttName || col.gxAttId;
	},

	mapColumn: function (col, i, conf) {
		var GE = gxui.GridExtension, controlTypes = gx.html.controls.types;

		if (gx.lang.gxBoolean(col.visible)) {
			var colType = this.getColumnType(col),
				converter,
				dataIndex = this.resolveColumnName(col);

			if (colType == 'date')
				converter = this.converter.date;
			else if (col.gxControl.type == controlTypes.checkBox)
				converter = this.converter.checkBox;

			conf.fields.push({
				name: dataIndex,
				mapping: function (i, converter) {
					return function (obj) {
						var hasReturn = obj[i].grid.instanciateRow(obj[i].gridRow);
						var value;
						if (hasReturn && obj[i].column.gxControl.type != gx.html.controls.types.checkBox) {
							var bkpObj = gx.O;
							var pO = obj[i].grid.parentObject;
							gx.setGxO(pO.CmpContext, pO.IsMasterPage);
							value = pO.GXValidFnc[obj[i].column.gxId].val();
							gx.setGxO(bkpObj.CmpContext, bkpObj.IsMasterPage);
						}
						else
							value = obj[i].value;
						if (typeof value == "string")
							value = gx.text.trim(value);

						if (converter)
							return converter(value);
						return value;
					};
				} (i),
				type: colType,
				convert: converter ? Ext.bind(converter, this, [col], true) : undefined
			});

			var colWidth = undefined,
				colFlex = undefined;
			if (col.gxControl.type != controlTypes.comboBox) {
				if (!gx.lang.gxBoolean(col.AutoExpand)) {
					if (col.gxWidthUnit == "px")
						colWidth = col.width || undefined;
					else if (col.gxWidthUnit == "%")
						colFlex = col.width / 100 || undefined;
				}
				else {
					colFlex = 1;
				}
			}

			// WA because the width is not present in the column
			if (!colWidth && col.gxControl.type == controlTypes.comboBox) {
				colWidth = col.gxControl.width || undefined;
			}

			var colCfg = {
				xtype: GE.ColumnRenderers.get(col),
				id: this.getUniqueId() + '-col-' + col.htmlName,
				dataIndex: dataIndex,
				header: col.title,
				width: colWidth,
				flex: colFlex,
				hidden: gx.lang.gxBoolean(col.Hidden || false),
				align: col.align,
				hideable: gx.lang.gxBoolean(col.Hideable),
				menuDisabled: gx.lang.gxBoolean(col.MenuDisabled),
				resizable: gx.lang.gxBoolean(col.Resizable) ? undefined : false,
				sortable: gx.lang.gxBoolean(col.Sortable),
				locked: gx.lang.gxBoolean(col.Lock) || undefined,
				checkedValue: col.gxChecked,
				uncheckedValue: col.gxUnChecked,
				gxGrid: this,
				gxColumn: col,
				actualColIndex: i
			};

			var minWidth = parseInt(this.MinColumnWidth);
			if (minWidth)
				colCfg.minWidth = minWidth;

			if (colWidth === undefined && colFlex === undefined)
				colCfg.autoWidth = true;

			if (colCfg.width === undefined) {
				delete colCfg.width;
			}

			if (colCfg.flex === undefined) {
				delete colCfg.flex;
			}

			if (col.gxColumnClass) {
				colCfg.cls = col.gxColumnClass;
				colCfg.tdCls = col.gxColumnClass;
			}

			if (col.SummaryType != 'none')
				colCfg.summaryType = col.SummaryType;

			if (col.HeaderGroup) {
				var hGroup = conf.headers[col.HeaderGroup];
				if (!hGroup) {
					hGroup = {
						header: col.HeaderGroup,
						columns: []
					};
					conf.headers[col.HeaderGroup] = hGroup;
					conf.columns.push(hGroup);
				}
				hGroup.columns.push(colCfg);
			}
			else
				conf.columns.push(colCfg);
		}
	},

	getStoreId: function () {
		return this.getUniqueId() + '-store';
	},

	getStoreConfig: function (fields) {
		var remoteSort = gx.lang.gxBoolean(this.RemoteSort);
		var storeConfig = {
			storeId: this.getStoreId(),
			remoteSort: remoteSort,
			fields: fields,
			data: this.properties,
			clearOnPageLoad: true,
			proxy: {
				type: 'gxui.memory'
			},
			listeners: {
				'groupchange': function (store) {
					// Remember the GroupField selected by the user.
					if (gx.lang.gxBoolean(this.Grouping)) {
						this.GroupField = store.groupField;
					}
				},
				scope: this
			}
		};

		if (this.pageSize > 0)
			storeConfig.pageSize = this.pageSize;

		if (this.SortField) {
			storeConfig.sorters = {
				property: this.SortField,
				direction: this.SortOrder || "ASC"
			};
		}

		if (gx.lang.gxBoolean(this.Grouping)) {
			if (!storeConfig.sorters) {
				storeConfig.sorters = {
					property: this.GroupField
				};
			}
			storeConfig.groupField = this.GroupField;
		}
		return storeConfig;
	},

	usePagingToolbar: function () {
		return this.hasPagingButtons() || !!this.OnFirstPage || !!this.OnPreviousPage || !!this.OnNextPage || !!this.OnLastPage;
	},

	getPagingToolbarConfig: function () {
		var items = [],
			usePaging = this.hasPagingButtons();

		
		if (usePaging || this.OnFirstPage) {
			items.push({
				itemId: 'first',
				tooltip: this.FirstText,
				overflowText: this.FirstText,
				iconCls: "x-tbar-page-first",
				disabled: usePaging && this.isFirstPage(),
				handler: this.OnFirstPage || Ext.bind(this.goToPage, this, ["first"]),
				scope: this
			});
		}

		
		if (usePaging || this.OnPreviousPage) {
			items.push({
				itemId: 'previous',
				tooltip: this.PreviousText,
				overflowText: this.PreviousText,
				iconCls: "x-tbar-page-prev",
				disabled: usePaging && this.isFirstPage(),
				handler: this.OnPreviousPage || Ext.bind(this.goToPage, this, ["prev"]),
				scope: this
			});
		}

		if (usePaging || ((this.OnFirstPage || this.OnPreviousPage) && (this.OnNextPage || this.OnLastPage))) {
			items.push("-");
		}

		
		if (usePaging || this.OnNextPage) {
			items.push({
				itemId: 'next',
				tooltip: this.NextText,
				overflowText: this.NextText,
				iconCls: "x-tbar-page-next",
				disabled: usePaging && this.isLastPage(),
				handler: this.OnNextPage || Ext.bind(this.goToPage, this, ["next"]),
				scope: this
			});
		}

		
		if (usePaging || this.OnLastPage) {
			items.push({
				itemId: 'last',
				tooltip: this.LastText,
				overflowText: this.LastText,
				iconCls: "x-tbar-page-last",
				disabled: usePaging && this.isLastPage(),
				handler: this.OnLastPage || Ext.bind(this.goToPage, this, ["last"]),
				scope: this
			});
		}

		if (usePaging || this.OnFirstPage || this.OnPreviousPage || this.OnNextPage || this.OnLastPage) {
			items.push("-");
		}

		items.push({
			itemId: 'refresh',
			tooltip: this.RefreshText,
			overflowText: this.RefreshText,
			iconCls: Ext.baseCSSPrefix + 'tbar-loading',
			handler: this.refreshGrid,
			scope: this
		});

		if (this.StatusText) {
			items.push('->');
			items.push({
				itemId: 'status',
				xtype: 'tbtext',
				text: this.StatusText,
				overflowText: this.StatusText
			});
		}

		return {
			itemId: 'toolbar',
			xtype: 'toolbar',
			dock: 'bottom',
			items: items
		};
	},

	updatePagingToolbar: function (tb) {
		if (tb) {
			var usePaging = this.hasPagingButtons(),
				first = tb.child('#first'),
				previous = tb.child('#previous'),
				next = tb.child('#next'),
				last = tb.child('#last'),
				status = tb.child('#status');

			if (usePaging) {
				if (first) {
					first.setDisabled(this.isFirstPage());
				}
				if (previous) {
					previous.setDisabled(this.isFirstPage());
				}
				if (next) {
					next.setDisabled(this.isLastPage());
				}
				if (last) {
					last.setDisabled(this.isLastPage());
				}
			}
			if (status) {
				status.setText(this.StatusText);
			}
		}
	},

	setSelectedRow: function (rowIndex) {
		// Set row as selected
		this.SelectedRow = rowIndex + 1;
		var actualRowIndex = this.getActualRowIndex(this.m_grid, rowIndex);
		this.selectRow(actualRowIndex);
	},

	keepSelection: function (grid) {
		if (this.SelectedRow >= 1) {
			grid.getSelectionModel().select(this.SelectedRow - 1);
			var isLoading = this.ownerGrid.isLoading;
			this.ownerGrid.isLoading = true;
			this.selectRow(this.SelectedRow - 1);
			this.ownerGrid.isLoading = isLoading;
			return false;
		}
	},

	gridListeners: function () {
		return {
			'itemclick': function (view, record, el, rowIndex, e) {
				var actualRowIndex = this.getActualRowIndex(view.panel, rowIndex);
				var row = this.rows[actualRowIndex];
				if (row) {
					this.setSelectedRow(rowIndex);
					// Set context
					if (this.ownerGrid.defaultSetsContext) {
						var setter = this.getGxRowContextSetter(row);
						if (setter) {
							this.notifyContext(setter.types, setter.hdl.call(setter.obj, row));
						}
					}
				}
			},

			'cellclick': function (view, cellEl, columnIndex, record, rowEl, rowIndex, e) {
				var cell = this.getPropertiesCell(view.panel, rowIndex, columnIndex, false);

				if (this.isCellEventEnabled(cell))
					this.fireCellClickEvent(rowIndex, columnIndex);
			},

			'itemcontextmenu': function (view, record, rowEl, rowIndex, e) {
				if (this.ContextMenu) {
					this.setSelectedRow(rowIndex);
					this.ContextMenu();
					e.preventDefault();
				}
			},

			'sortchange': function (ct, column, direction) {
				if (this.m_grid) {
					var remoteSort = gx.lang.gxBoolean(this.RemoteSort);

					this.SortField = column.dataIndex;
					this.SortOrder = direction;
					if (remoteSort) {
						// Remember the SortField and SortOrder selected by the user.
						this.m_grid.saveState();
					}

					
					if (this.OnSortChange) {
						this.OnSortChange();
					}
					else {
						if (remoteSort) {
							Ext.defer(this.goToPage, 30, this, ["FIRST"]);
						}
					}

					return !remoteSort;
				}
			},

			'columnresize': function () {
				if (!this.fixingWidth)
					this.fixGridWidth(this.m_grid);
			},

			'beforestaterestore': function (grid, state) {
				if (gx.lang.gxBoolean(this.RemoteSort)) {
					delete state.sort;
				}
				return true;
			},

			'afterlayout': function (grid) {
				// Correct width when it isn't specified, to behave as standard GX grid.
				if (!this.fixingWidth)
					this.fixGridWidth(grid);
			},

			'beforestatesave': function () {
				// Avoid state save when the control is loaded for the first time, as an onResize event
				// is firing an unnecesary state save after rendering the control.
				if (!this.canSaveState) {
					this.canSaveState = true;
					return false;
				}
			},

			scope: this
		};
	},

	isEditable: function (force) {
		if (this.editable === undefined || force) {
			var editable = false;
			for (var i = 0, rows = this.properties.length; i < rows; i++) {
				for (var j = 0, cols = this.properties[i].length; j < cols; j++) {
					editable = editable || this.isCellEditable(this.properties[i][j]);
					if (editable) {
						this.editable = editable;
						return this.editable;
					}
				}
			}
		}

		return this.editable;
	},

	isCellEditable: function (cell) {
		return !gx.lang.gxBoolean(cell.readOnly) && gx.lang.gxBoolean(cell.enabled);
	},

	getActualColumnIndex: function (grid, colIndex) {
		var column;
		if (grid.columnManager) {
			column = grid.columnManager.getHeaderAtIndex(colIndex);
		}
		else {
			column = grid.columns[colIndex];
		}
		
		if (column)
			return column.actualColIndex;
		return -1;
	},

	getActualRowIndex: function (grid, rowIndex) {
		return grid.getStore().getAt(rowIndex).raw[0].row.id;
	},

	getPropertiesCell: function (grid, rowIndex, columnIndex, isActualColIndex) {
		var actualColIndex = columnIndex;
		if (!isActualColIndex) {
			actualColIndex = this.getActualColumnIndex(grid, columnIndex);
		}
		var record = grid.getStore().getAt(rowIndex);
		if (record) {
			return record.raw[actualColIndex];
		}
		return null;
	},

	goToPage: function (page) {
		if (typeof page == "string") {
			if (this.ownerGrid.pagingCommand) {
				this.ownerGrid.pagingCommand(page.toUpperCase());
				return;
			}
			this.UnSelectRows();
			if (this.changeGridPage) {
				this.changeGridPage(page.toUpperCase());
				return;
			}

			this.goToPage_Internal(page.toUpperCase());
		}
	},

	goToPage_Internal: function (pagingDirection) {
		var hiddenName = this.gxGridName.toUpperCase() + "PAGING",
			ownerGrid = this.ownerGrid,
			eventName = '',
			gridId;
		if (pagingDirection) {
			if (gx.pO.fullAjax) {
				gx.setGxO(this.parentGxObject);
				eventName = "E" + ownerGrid.realGridName.toUpperCase() + "_" + pagingDirection + "PAGE" + (ownerGrid.isMasterPageGrid ? "_MPAGE" : "");
				if (ownerGrid.parentGrid) {
					gridId = ownerGrid.parentGrid.gridId;
				}
			}
			else {
				gx.fn.setHidden(this.gxCmpContext + hiddenName, pagingDirection);
				eventName = this.gxCmpContext + "E" + hiddenName + '.';
			}
			gx.evt.execEvt(undefined, undefined, eventName, gx.evt.dummyCtrl, gridId);
		}
	},

	refreshGrid: function () {
		var og = this.ownerGrid, po = this.ParentObject;
		var bkpObj = gx.O;
		gx.setGxO(po.CmpContext, po.IsMasterPage);
		if (og.parentObject.autoRefresh && og.refreshVars.length > 0) {
			og.callAsyncRefresh(og.getRefreshParmsUrl())
		}
		else {
			po.executeServerEvent('RFR', true);
		}
		gx.setGxO(bkpObj.CmpContext, bkpObj.IsMasterPage);
	},

	getRowGxInternalId: function (row) {
		return this.ownerGrid.containerName + 'Row_' + row.gxId;
	},

	getGxRowContextSetter: function (row) {
		var trId = this.getRowGxInternalId(row);

		var setter;
		// Find the context source for the row
		Ext.each(gx.fx.ctx.setters, function (s) {
			if (s.id == trId) {
				setter = s;
				return false;
			}
		}, this);

		return setter;
	},

	isCellEventEnabled: function (cell) {
		var gxControlTypes = gx.html.controls.types;
		return cell.type == gxControlTypes.checkBox || (cell.type == gxControlTypes.image && cell.enabled && (cell.readOnly === undefined || cell.readOnly === true)) || (cell.type != gxControlTypes.image && !cell.enabled);
	},

	fireCellClickEvent: function (rowIndex, columnIndex) {
		var grid = this.m_grid;
		var actualColIndex = this.getActualColumnIndex(grid, columnIndex);
		var actualRowIndex = this.getActualRowIndex(grid, rowIndex);
		var cell = this.getPropertiesCell(grid, actualRowIndex, actualColIndex, true);
		if (this.executeEvent) {
			this.executeEvent(actualColIndex, actualRowIndex);
		}
	},

	fireCellIsValidEvent: function (rowIndex, columnIndex) {
		var grid = this.m_grid,
			actualColIndex = this.getActualColumnIndex(grid, columnIndex),
			actualRowIndex = this.getActualRowIndex(grid, rowIndex),
			cell = this.getPropertiesCell(grid, actualRowIndex, actualColIndex, true),
			gxO = this.parentGxObject,
			vStruct = cell.vStruct || gxO.GXValidFnc[cell.column.gxId]

		if (this.executeIsValid) {
			this.executeIsValid(actualColIndex, actualRowIndex);
		}
		else {
			// For older GX versions (executeIsValid method became available in Evo3U3)
			if (vStruct && vStruct.isvalid) {
				ctrlRow = cell.row.gxId;
				this.ownerGrid.instanciateRow(ctrlRow);
				gxO[vStruct.isvalid].call(gxO);
			}
		}
	},

	fireCellValidation: function (rowIndex, columnIndex, cell) {
		if (this.executeValidate) {
			this.executeValidate(rowIndex, columnIndex, cell.value);
		}
	},

	getSelectedRow: function () {
		return this.SelectedRow;
	},

	beforeEditHandler: function (grid, e) {
		var cell = e.record.raw[e.column.actualColIndex];
		return cell.enabled;
	},

	afterEditHandler: function (editor, e) {
		var actualColIndex = this.getActualColumnIndex(e.grid, e.colIdx),
			cell = this.getPropertiesCell(e.grid, e.rowIdx, actualColIndex, true),
			gxControl = cell.column.gxControl,
			controlTypes = gx.html.controls.types,
			columns = e.grid.columns;

		if (this.EditModel == 'CellEditModel') {
			this.setCellValue(cell, e.value);

			if (gxControl.vStruct && gxControl.vStruct.gxsgprm) {
				this.fireCellValidation(e.rowIdx, e.colIdx, cell);
			}

			// Fire cell click event
			if (gxControl.type == controlTypes.checkBox || gxControl.type == controlTypes.comboBox) {
				this.fireCellClickEvent(e.rowIdx, e.colIdx)
			}

			if (e.originalValue != e.value) {
				this.fireCellIsValidEvent(e.rowIdx, e.colIdx);
			}
		}
		else {
			for (var i = 0, len = columns.length; i < len; i++) {
				actualColIndex = this.getActualColumnIndex(e.grid, i),
				cell = this.getPropertiesCell(e.grid, e.rowIdx, actualColIndex, true);

				var value = e.record.getChanges()[columns[i].dataIndex];

				if (value !== undefined) {
					this.setCellValue(cell, value);
				}

				if (gxControl.vStruct && gxControl.vStruct.gxsgprm) {
					this.fireCellValidation(e.rowIdx, e.colIdx, cell);
				}
			}
		}

		if (gx.lang.gxBoolean(this.ShowGroupingSummary))
			e.grid.getView().refresh();
	},


	setCellValue: function (cell, value) {
		var gxControl = cell.column.gxControl,
			controlTypes = gx.html.controls.types;

		cell.value = value;

		if (gxControl.type == controlTypes.checkBox) {
			if (cell.column.type != gx.types.bool)
				cell.value = value ? cell.column.gxChecked : cell.column.gxUnChecked;
		}

		if (value instanceof Date) {
			var gxdate = value.gxdate;
			cell.value = gxdate.getString();
			if (cell.column.type == gx.types.dateTime) {
				gxdate.HasTimePart = true;
				var validStruct = gxControl.vStruct,
					nDec = validStruct.dec;
				cell.value += ' ' + gxdate.getTimeString(nDec > 3, nDec == 8, nDec > 1);
			}
		}

		if (typeof (value) == "number") {
			cell.value = gxui.GridExtension.Column.prototype.formatNumber(value, gxControl.vStruct);
		}
	},

	fixGridWidth: function (grid) {
		if (!grid.ownerCt) {
			var columns = grid.columns,
				width = grid.lockable ? 3 : 2;
			if (!this.gxWidth) {
				for (var i = 0, len = columns.length; i < len; i++) {
					var col = Ext.getCmp(columns[i].id);
					width += col.getWidth();
				}
				this.fixingWidth = true;
				grid.setWidth(width);
				this.fixingWidth = false;
			}
		}
	},

	columnModelChanged: function (grid) {
		var newColumnModel = this.getColumnsConfig(),
			newColumns = newColumnModel.columns,
			oldColumns = grid.initialConfig.columns,
			newCol,
			oldCol,
			properties = ['hideable', 'hidden', 'locked', 'resizable', 'sortable', 'header'];

		if (oldColumns.length != newColumns.length) {
			return true;
		}

		for (var i = 0, len = oldColumns.length; i < len; i++) {
			oldCol = oldColumns[i];
			newCol = newColumns[i];
			if (oldCol && newCol) {
				if (oldCol.dataIndex != newCol.dataIndex) {
					return true;
				}
				for (var j = 0, propLen = properties.length; j < propLen; j++) {
					if (oldCol[properties[j]] != newCol[properties[j]] && newCol[properties[j]] !== undefined) {
						return true;
					}
				}
			}
			else {
				return true;
			}
		}
	},

	getEditorPlugin: function () {
		var grid = this.m_grid,
				pluginId = grid.id + (this.EditModel == 'CellEditModel' ? '-celledit' : '-rowedit');
		return grid.getPlugin(pluginId);
	},

	methods: {
		// Methods
		
		SelectRow: function (rowIndex) {
			// Row index is 1 based, for compatibility with GeneXus criteria
			if (rowIndex) {
				this.setSelectedRow(rowIndex - 1);
				this.m_grid.getSelectionModel().selectRow(rowIndex - 1);
			}
		},

		UnSelectRows: function () {
			if (this.SelectedRow != undefined) {
				delete this.SelectedRow;
			}
			this.m_grid.getSelectionModel().deselectAll();
		}
	}
});

Ext.define('gxui.data.proxy.Memory', {
	extend: 'Ext.data.proxy.Memory',
	alias: 'proxy.gxui.memory',
	alternateClassName: 'gxui.data.MemoryProxy',

	reader: {
		type: 'json',
		createAccessor: function (i) {
			return function (obj) {
				if (i == 'id')
					return undefined;
			};
		},
		totalProperty: undefined,
		successProperty: undefined,
		idProperty: undefined
	}
});

/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />

Ext.define('gxui.GridExtension.Column', function () {
	var pictureHelperRegex,
		blankWhenZeroRegex = /^Z+(?:\.Z+)?$/;

	return {
		extend: 'Ext.grid.column.Column',
		alias: 'widget.gxui.column',

		constructor: function (config) {
			this.callParent([config]);
			this.renderer = Ext.bind(this.renderer, this);
			this.editor = this.defineEditor(this.gxColumn, this.actualColIndex);
		},

		destroy: function () {
			delete this.gxGrid;
			delete this.gxColumn;
			this.callParent(arguments);
		},

		mapDateFormat: function () {
			switch (gx.dateFormat) {
				case 'MDY':
					return "m/d/y";
				case 'DMY':
					return "d/m/y";
				case 'MDY4':
					return "m/d/Y";
				case 'DMY4':
					return "d/m/Y";
				case 'YMD':
					return "y/m/d";
				default:
					return "Y/m/d";
			}
		},

		mapTimeFormat: function (gxColumn) {
			if (gxColumn.gxControl.vStruct) {
				var nDec = gxColumn.gxControl.vStruct.dec,
				minutes = nDec > 3,
				seconds = nDec == 8,
				hours = nDec > 1;

				if (gx.timeFormat == 12) {
					if (hours && minutes && seconds)
						return "h:i:s A";

					if (hours && minutes)
						return "h:i A";

					if (hours)
						return "h A";
				}
				else {
					if (hours && minutes && seconds)
						return "H:i:s";

					if (hours && minutes)
						return "H:i";

					if (hours)
						return "H";
				}
			}

			if (gx.timeFormat == 12) {
				return "h:i A";
			}

			return "H:i";
		},

		defineEditor: function (gxColumn, actualColIndex) {
			var types = gx.types;
			switch (gxColumn.type) {
				case types.numeric:
					var colData = this.gxGrid.ParentObject.GXValidFnc[gxColumn.gxId];
					return {
						xtype: 'numberfield',
						allowDecimals: colData.dec > 0 ? true : false,
						minValue: colData.sign ? Number.NEGATIVE_INFINITY : 0,
						decimalPrecision: colData.dec,
						decimalSeparator: gx.decimalPoint,
						enforceMaxLength: true,
						maxLength: colData.len,
						maxValue: Math.pow(10, colData.len - colData.dec - (colData.dec > 0 ? 1 : 0)) - (colData.dec > 0 ? 1 / Math.pow(10, colData.dec) : 0)
					};

				case types.date:
					return {
						xtype: 'datefield',
						format: this.mapDateFormat()
					};

				case types.dateTime:
					return {
						xtype: 'xdatetime',
						dateFormat: this.mapDateFormat(),
						timeFormat: this.mapTimeFormat(gxColumn)
					};

				default:
					if (gxColumn.gxControl.type == gx.html.controls.types.multipleLineEdit)
						return {
							xtype: 'textareafield',
							maxLength: gxColumn.gxControl.maxLength
						};

					return {
						xtype: 'textfield',
						maxLength: gxColumn.gxControl.maxLength
					};
			}
		},

		formatNumber: function (value, colData) {
			var extUtilFormat = Ext.util.Format;
			var picture = colData.pic;
			var numberFormat = "";
			var integerPart = "0"
			if (value === 0 && picture.match(blankWhenZeroRegex)) {
				return "";
			}
			if (picture.indexOf(gx.thousandSeparator) >= 0) {
				integerPart += ",000";
			}
			if (colData.dec > 0)
				numberFormat = integerPart + "." + (extUtilFormat.leftPad("", colData.dec, '0') || '0');
			else
				numberFormat = integerPart;

			extUtilFormat.thousandSeparator = gx.thousandSeparator;
			extUtilFormat.decimalSeparator = gx.decimalPoint;
			v = extUtilFormat.number(value, numberFormat);

			// Left fill with zeros if applies
			if (!pictureHelperRegex) {
				pictureHelperRegex = new RegExp("^[9" + gx.decimalPoint + gx.thousandSeparator + "]+$");
			}
			var matches = picture.match(pictureHelperRegex);
			if (matches && matches.length > 0) {
				v = picture.substr(0, picture.length - v.length).replace(/9/ig, "0") + v;
			}

			return v + "";
		},

		mapDatePictureToFormat: function (vStruct) {
			var dateFormat = function (FormatPart, Picture) {
				if (FormatPart == 'Y' && Picture.substr(0, 10) == '99/99/9999')
					return 'Y';
				else if (FormatPart == 'Y')
					return 'y';
				else if (FormatPart == 'M')
					return 'm';
				else if (FormatPart == 'D')
					return 'd';
				else return '';
			};

			var dateTimeFormat = function (Dec) {
				var timeFmt = gx.timeFormat;
				var DPTF = '', AMPM = '', TimeFmt;
				if (timeFmt == 12) {
					DPTF = 'h';
					AMPM = ' A';
				} else if (timeFmt == 24) {
					DPTF = 'H';
					AMPM = '';
				}

				if (Dec == 2)
					TimeFmt = '';
				else if (Dec == 5)
					TimeFmt = ':i';
				else if (Dec == 8)
					TimeFmt = ':i:s';
				else
					return '';

				return DPTF + TimeFmt + AMPM;
			};


			var Picture = vStruct.dp.pic,
				Dec = vStruct.dp.dec,
				Len = vStruct.len,
				dateFmt = gx.dateFormat,
				D1 = dateFmt.substr(0, 1),
				D2 = dateFmt.substr(1, 1),
				D3 = dateFmt.substr(2, 1),
				DD1 = dateFormat(D1, Picture),
				DD2 = dateFormat(D2, Picture),
				DD3 = dateFormat(D3, Picture),
				DT = dateTimeFormat(Dec);

			if (Len > 0 && Dec > 0)
				return DD1 + '/' + DD2 + '/' + DD3 + ' ' + DT;
			else if (Len > 0)
				return DD1 + '/' + DD2 + '/' + DD3;
			else
				return DT;
		},

		formatDate: function (value, vStruct) {
			var gxdate = value.gxdate;
			if (value - new Date(0, 0, 0, 0, 0, 0, 0) === 0 && gxdate) {
				var gxFormat = gxdate.SFmt,
					dp = vStruct.dp;
				if (dp && dp.pic && dp.pic.indexOf("9999") >= 0) {
					gxFormat = "Y4";
				}
				return gxdate.emptyDateString(gxFormat);
			}
			else {
				var format = this.mapDatePictureToFormat(vStruct);
				return Ext.util.Format.date(value, format);
			}
		},

		renderer: function (value, metadata, record, rowIndex, colIndex, store) {
			var col = this.gxColumn,
				gxControl = col.gxControl,
				controlTypes = gx.html.controls.types,
				v = value;

			if (col.type == gx.types.date || col.type == gx.types.dateTime) {
				v = this.formatDate(value, gxControl.vStruct);
			}

			if (col.type == gx.types.numeric && typeof (value) == "number") {
				v = this.formatNumber(value, this.gxGrid.ParentObject.GXValidFnc[this.gxColumn.gxId]);
			}

			if (record.isSummary) {
				return v;
			}

			var cell = record.raw[this.actualColIndex];

			if (gx.lang.gxBoolean(cell.visible)) {
				if (!metadata.tdCls) {
					metadata.tdCls = '';
				}

				if (gx.lang.gxBoolean(this.gxGrid.UseThemeClasses) && cell.cssClass) {
					metadata.tdCls += ' ' + cell.cssClass;
				}

				if (this.gxGrid.isCellEditable(cell) && this.gxGrid.EditableCellClass) {
					metadata.tdCls += ' ' + this.gxGrid.EditableCellClass;
				}

				if (cell.link) {
					v = Ext.String.format('<a href="{0}" alt="{2}" target="{3}">{1}</a>', cell.link || "", v || "", cell.alt || "", cell.linkTarget || "");
				}

				var style = "";
				if (cell.style)
					style += this.extractCssProperties(["text-decoration", "color", "background-color", "font-weight"], cell.style);

				// If the cell fires a user event and is enabled, wrap with an anchor tag.
				if (cell.grid.grid.isCellEventEnabled(cell)) {
					style += 'cursor:pointer;';
				}

				if (style)
					metadata.style = style;

				//Show Tooltip text if set
				if (cell.title) {
					v = Ext.String.format("<span data-qtip='{0}'>{1}</span>", cell.title, v);
				}
				return v;
			}
			return "";
		},

		extractCssProperties: function (properties, inputStyle) {
			var buffer = [];
			for (var i = 0, len = properties.length; i < len; i++) {
				var propMatch = inputStyle.match(new RegExp(properties[i] + ":([^;]+);?"));
				if (propMatch)
					buffer.push(Ext.String.format(properties[i] + ":{0};", propMatch[1]));
			}
			return buffer.join("");
		},

		getEditorPlugin: function () {
			return this.gxGrid.getEditorPlugin();
		}
	};
} ());

Ext.define('gxui.GridExtension.ImageColumn', {
	extend: 'gxui.GridExtension.Column',
	alias: 'widget.gxui.imagecolumn',

	defineEditor: Ext.emptyFn,

	renderer: function (value, metadata, record, rowIndex, colIndex, store) {
		var cell = record.raw[this.actualColIndex];
		if (gx.lang.gxBoolean(cell.visible)) {
			var styleBuffer = [];
			if (cell.width) {
				styleBuffer.push("width:");
				styleBuffer.push(cell.width);
				styleBuffer.push(cell.widthUnit);
				styleBuffer.push(";");
			}
			if (cell.height) {
				styleBuffer.push("height:");
				styleBuffer.push(cell.height);
				styleBuffer.push(cell.heightUnit);
				styleBuffer.push(";");
			}
			value = Ext.String.format('<img src="{0}" class="{1}" title="{2}" style="{3}"/>', cell.value, cell.cssClass, cell.title, styleBuffer.join(""));
		}
		return this.callParent([value, metadata, record, rowIndex, colIndex, store]);
	}
});

Ext.define('gxui.GridExtension.BlobColumn', {
	extend: 'gxui.GridExtension.Column',
	alias: 'widget.gxui.blobcolumn',

	defineEditor: Ext.emptyFn,

	renderer: function (value, metadata, record, rowIndex, colIndex, store) {
		var cell = record.raw[this.actualColIndex],
			gxControl = cell.column.gxControl,
			imgType = (gxControl.contentType.toLowerCase().indexOf('image/') != -1),
			url = cell.url;

		if (gx.lang.gxBoolean(cell.visible)) {
			if (gxControl.display === 0 && imgType) {
				if (!url || url == gx.util.resourceUrl(gx.basePath + gx.staticDirectory)) {
					url = gx.ajax.getImageUrl(gx, 'blankImage');
				}
				value = Ext.String.format('<img src="{0}" border="0" class="gxui-gridcell-blob {1}"/>', url, cell.cssClass);
			}
			else {
				value = Ext.String.format('<img src="{0}" border="0" class="{1}"/>', gx.ajax.getImageUrl(gx, 'downloadImage'), cell.cssClass);
			}
			cell.link = url;
			cell.linkTarget = "_blank";
		}
		return this.callParent([value, metadata, record, rowIndex, colIndex, store]);
	}
});

Ext.define('gxui.GridExtension.CheckColumn', {
	extend: 'Ext.ux.CheckColumn',
	alias: 'widget.gxui.checkcolumn',

	processEvent: function (type, view, cell, recordIndex, cellIndex, e) {
		if (type == 'mousedown' || (type == 'keydown' && (e.getKey() == e.ENTER || e.getKey() == e.SPACE))) {
			var record = view.panel.store.getAt(recordIndex),
				cell = record.raw[this.actualColIndex];
			if (cell.enabled)
				return this.callParent(arguments);
		}
		else {
			return this.callParent(arguments);
		}
	},
	listeners: {
		'checkchange': function (column, rowIndex, checked) {
			var grid = column.ownerCt.ownerCt,
				editorPlugin = grid.getPlugin(grid.id + '-celledit'),
				editingContext = editorPlugin.getEditingContext(rowIndex, column);

			if (editorPlugin)
				editorPlugin.fireEvent('edit', this, editingContext);

			grid.fireEvent('cellclick', grid.getView(), null, editingContext.colIdx, editingContext.record, null, editingContext.rowIdx, editingContext);
		}
	}
});

Ext.define('gxui.GridExtension.RadioColumn', {
	extend: 'gxui.GridExtension.Column',
	alias: 'widget.gxui.radiocolumn',

	defineEditor: Ext.emptyFn,

	renderer: function (value, metadata, record, rowIndex, colIndex, store) {
		var cell = record.raw[this.actualColIndex];
		if (gx.lang.gxBoolean(cell.visible)) {
			if (typeof value == "string") {
				value = value.trim();
			}
			value = gx.fn.selectedDescription({ s: value, v: cell.possibleValues });
		}
		return this.callParent([value, metadata, record, rowIndex, colIndex, store]);
	}
});

Ext.define('gxui.GridExtension.ComboColumn', {
	extend: 'gxui.GridExtension.Column',
	alias: 'widget.gxui.combocolumn',

	defineEditor: function (gxColumn, actualColIndex) {
		var vStruct = gxColumn.gxControl.vStruct,
			isSuggest = vStruct && vStruct.gxsgprm && this.gxGrid.requestSuggest;

		return {
			xtype: 'combobox',
			editable: isSuggest,
			triggerAction: 'all',
			selectOnFocus: true,
			disableKeyFilter: false,
			forceSelection: true,
			store: [["", ""]],
			queryMode: 'local',
			typeAhead: isSuggest ? vStruct.gxsgprm[3] : false,
			getActiveRecord: function (column) {
				var plugin = column.getEditorPlugin();
				if (column.gxGrid.EditModel == 'CellEditModel')
					return plugin.getActiveRecord();
				return plugin.context.record;
			},
			populateCombo: function (column, query) {
				var record = this.getActiveRecord(column),
					cell = record.raw[column.actualColIndex],
					gxGrid = column.gxGrid;

				if (isSuggest) {
					query = query || "";
					gxGrid.requestSuggest(column.actualColIndex, cell.row.id, query).done(Ext.bind(function (data) {
						this.getStore().loadData(Ext.Array.map(data, function (item) {
							return [item.c, item.d];
						}));
					}, this));
				}
				else {
					this.getStore().loadData(cell.possibleValues);
					if (typeof cell.value == "string")
						this.select(cell.value.trim());
					else
						this.select(cell.value);
				}
			},
			listeners: {
				'beforerender': function (combo) {
					if (this.gxGrid.EditModel == 'CellEditModel') {
						combo.populateCombo(this);
					}
					else {
						combo.ownerCt.on('show', Ext.bind(combo.populateCombo, combo, [this]));
					}
				},
				'beforequery': function (queryEvent) {
					queryEvent.combo.populateCombo(this, queryEvent.query || queryEvent.combo.rawValue);
				},
				'select': function () {
					if (this.gxGrid.EditModel == 'CellEditModel') {
						this.getEditorPlugin().completeEdit();
					}
				},
				scope: this
			}
		};
	},

	renderer: function (value, metadata, record, rowIndex, colIndex, store) {
		var cell = record.raw[this.actualColIndex];
		if (gx.lang.gxBoolean(cell.visible)) {
			if (!cell.vStruct || !cell.vStruct.gxsgprm || !this.gxGrid.requestSuggest) {
				if (typeof cell.value == "string") {
					value = value + "";
				}
				value = gx.fn.selectedDescription({ s: value, v: cell.possibleValues });
			}
		}
		return this.callParent([value, metadata, record, rowIndex, colIndex, store]);
	}
});

gxui.GridExtension.ColumnRenderers = function () {
	var GE = gxui.GridExtension;
	var types = gx.html.controls.types;

	var renderers = {};
	renderers[types.image] = 'gxui.imagecolumn';
	renderers[types.checkBox] = 'gxui.checkcolumn';
	renderers[types.radio] = 'gxui.radiocolumn';
	renderers[types.comboBox] = 'gxui.combocolumn';
	renderers[types.blob] = 'gxui.blobcolumn'

	renderers.get = function (col) {
		var t = col.gxControl.type,
			vStruct = col.gxControl.vStruct;

		if (vStruct && t == types.singleLineEdit && vStruct.gxsgprm && col.gxControl.grid.grid.requestSuggest) {
			return renderers[types.comboBox];
		}

		if (this[t]) {
			return this[t];
		}

		return 'gxui.column';
	};

	return renderers;
} ();  

/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


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
				
				if (this.TabClosed) {
					this.ClosedTabId = tabItem.id;
					this.TabClosed();
				}
			}
		},

		tabItemBeforeClosed: function (tabPanel, tabItem) {
			if (Ext.getClassName(tabPanel) == "Ext.tab.Panel") {
				
				if (this.BeforeTabClosed) {
					this.ClosedTabId = tabItem.id;
					this.CancelEvent = false;
					this.BeforeTabClosed();
					return !this.CancelEvent;
				}
			}
		},

		tabStripClick: function (tab, e) {
			
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

		
		CloseTab: function (tabId) {
			var tabPanel = this.m_tabPanel;
			if (this.IsTabOpen(tabId)) {
				var tab = tabPanel.child("#" + this.getTabUniqueId(tabId));
				if (tab) {
					tabPanel.remove(tab, true);
				}
			}
		},

		
		SelectTab: function (tabId) {
			this.m_activeTab = this.getTabUniqueId(tabId);
			this.m_tabPanel.setActiveTab(this.m_activeTab);
		},

		
		IsTabOpen: function (tabId) {
			var tab = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			return (tab) ? true : false;
		},

		
		ShowTab: function (i) {
			var panel = this.m_tabPanel.items.get(i);
			if (panel)
				panel.tab.show();
		},

		
		HideTab: function (i) {
			var panel = this.m_tabPanel.items.get(i);
			if (panel)
				panel.tab.hide();
		},

		
		ShowTabById: function (tabId) {
			var panel = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			if (panel)
				panel.tab.show();
		},

		
		HideTabById: function (tabId) {
			var panel = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			if (panel)
				panel.tab.hide();
		},

		
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

		
		IsTabDirty: function (tabId) {
			var tab = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			return tab && (tab.dirty == true);
		},

		
		SetTabTitle: function (tabId, title) {
			var tab = this.m_tabPanel.child("#" + this.getTabUniqueId(tabId));
			if (tab) {
				tab.setTitle(title);
				this.SetTabDirty(tabId, tab.dirty || false);
			}
		}
	}
});
/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.Message', function () {

	// Private variables
	var msgCt;

	return {
		extend: 'gxui.UserControl',

		initialize: function () {
			this.callParent(arguments);
		},

		onRender: function () {
			if (gxui.CBoolean(this.Show)) {
				this.ShowMessage();
			}
		},

		onRefresh: function () {
			if (gxui.CBoolean(this.Show)) {
				this.ShowMessage();
			}
		},

		createBox: function (t, s) {
			return ['<div class="msg">',
	                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
	                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
	                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
	                '</div>'].join('');
		},

		methods: {
			
			ShowMessage: function (title, message, type) {
				type = type || this.Type;
				message = message || this.Message;
				title = title || this.Title;

				if (type == "alert") {
					var msgBox = Ext.create('Ext.window.MessageBox');
					Ext.defer(msgBox.show, 100, msgBox, [{
						title: title,
						msg: message,
						buttons: Ext.MessageBox.OK,
						icon: this.Icon == "info" ? Ext.MessageBox.INFO : (this.Icon == "question" ? Ext.MessageBox.QUESTION : (this.Icon == "warning" ? Ext.MessageBox.WARNING : Ext.MessageBox.ERROR))
					}]);
				}
				else {
					var titleMsgs = (title || "").split("|");
					Ext.each((message || "").split("|"), function (msg, i) {
						// Create the message box
						if (!msgCt) {
							msgCt = Ext.DomHelper.insertFirst(document.body, { id: 'msg-div' }, true);
							if (this.Cls != "")
								msgCt.addClass(this.Cls);
						}
						msgCt.alignTo(document, this.Position + '-' + this.Position);
						var m = Ext.DomHelper.append(msgCt, { html: this.createBox(titleMsgs[i], msg) }, true);

						var timeoutId;
						var hideMessage = function () {
							var f;
							if (this.Position == 'c') {
								f = function () {
									m.fadeOut({
										opacity: 0, //can be any value between 0 and 1 (e.g. .5)
										easing: 'easeOut',
										remove: true
									});
								};
							} else {
								f = Ext.bind(function () {
									m.ghost(this.Position, { remove: true });
								}, this);
							}
							timeoutId = setTimeout(f, this.Duration * 1000);
						};

						// Slide the message box into view
						m.slideIn(this.Position, {
							callback: hideMessage,
							scope: this
						});

						// Do not hide the message box if the mouse is over it
						m.on('mouseover', function (e) {
							if (timeoutId) {
								clearTimeout(timeoutId);
								timeoutId = null;
							}
						}, this);

						// If the mouse is outside the message box, schedule its hiding, according to duration
						m.on('mouseout', function (e) {
							var box = m.getBox();
							var x = e.getPageX();
							var y = e.getPageY();
							if (x && y) {
								if (x > box.x && x < box.x + box.width && y > box.y && y < box.y + box.height) {
									return
								}
							}
							Ext.bind(hideMessage, this)();
						}, this)
					}, this);
				}
			}
		}
	};
} ());
/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.Menu', {
	extend: 'gxui.UserControl',

	initialize: function () {
		this.callParent(arguments);
		this.unmanagedLayout = true;
	},

	// Databindings
	SetMenu: function (data) {
		this.Menu = data;
	},

	// Databindings
	GetMenu: function (data) {
		return this.Menu;
	},

	onRender: function () {
		this.m_menu = this.createMenu(this.Menu);
	},

	onRefresh: function () {
	},

	getUnderlyingControl: function () {
		return this.m_menu;
	},

	// Overriden
	runDeferredMethod: function () {
		return !!this.getUnderlyingControl();
	},

	createMenu: function (menu) {
		if (menu) {
			return new Ext.menu.Menu({
				items: this.getContextMenuItems(menu),
				ignoreParentClicks: true
			});
		}
	},

	getContextMenuItems: function (contextMenu) {
		var cmItems = [];

		if (contextMenu) {
			Ext.each(contextMenu, function (item) {
				var config;
				switch (item.Type) {
					case 'Text':
						config = item.Text;
						break;
					case 'Separator':
						config = '-';
						break;
					case 'Menu':
						config = this.getBasicItemConfig(item);
						config.menu = this.getContextMenuItems(item.Items);
						delete config.handler;
						break;
					default:
						config = this.getBasicItemConfig(item);
						break;
				}

				cmItems.push(config);
			}, this);
		}

		return cmItems;
	},

	getBasicItemConfig: function (item) {
		return {
			gxid: item.Id,
			text: item.Text,
			tooltip: item.Tooltip,
			icon: item.Icon,
			iconCls: item.IconCls,
			cls: (item.Cls != "") ? item.Cls : (item.Text != "") ? "x-btn-text-icon" : "x-btn-icon",
			disabled: item.Disabled,
			hidden: item.Hidden,
			handler: this.itemClickHandler,
			scope: this
		};
	},

	itemClickHandler: function (btn) {
		
		if (this.ItemClick) {
			this.ItemClickedId = btn.gxid;
			this.ItemClick();
		}
	},

	methods: {
		// Methods
		
		ShowMenu: function (m, x, y) {
			var menu = this.m_menu;
			if (m) {
				menu = this.createMenu(m);
			}
			var xy = (x && y) ? [x, y] : Ext.EventObject.getXY();
			if (menu) {
				menu.showAt(xy);
			}
		},

		
		ShowMenuXY: function (x, y) {
			this.ShowMenu(this.Menu, x, y);
		}
	}
});

// Supported item types
gxui.Menu.ItemType = {
	Button: "Button",
	Text: "Text",
	Separator: "Separator",
	Menu: "Menu"
};

/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.Viewport', {
	extend: 'gxui.UserControl',

	initialize: function () {
		this.callParent(arguments);
	},

	onRender: function () {
		var body = this.getChildContainer("Body");
		this.m_panel = Ext.create('gxui.container.NestedViewport', {
			id: this.getUniqueId(),
			contentEl: body,
			bodyCls: "gxui-noreset",
			layout: 'fit',
			renderTo: 'MAINFORM'
		});

		Ext.fly(body).setDisplayed(true);

		// Register as UC Container
		this.registerCt(this.getChildContainer("Body"), this.m_panel.add, this.m_panel.doLayout, this.m_panel);
	},

	getUnderlyingControl: function () {
		return this.m_panel;
	}
});
// vim: ts=4:sw=4:nu:fdc=2:nospell


Ext.define('Ext.ux.state.HttpProvider', {
	extend: 'Ext.state.Provider'

	
    , async: true
	
    , flushCache: false
	// localizable texts
    , saveSuccessText: 'Save Success'
    , saveFailureText: 'Save Failure'
    , readSuccessText: 'Read Success'
    , readFailureText: 'Read Failure'
    , dataErrorText: 'Data Error'

	//private
    , constructor: function (config) {
    	this.addEvents(
    	
             'readsuccess'
    	
            , 'readfailure'
    	
            , 'savesuccess'
    	
            , 'savefailure'
        );

    	this.callParent(arguments);

    	Ext.apply(this, config, {
    		// defaults
    		delay: 750 // buffer changes for 750 ms
            , dirty: false
            , started: false
            , autoStart: true
            , autoRead: true
            , user: 'user'
            , id: 1
            , session: 'session'
            , logFailure: false
            , logSuccess: false
            , queue: []
            , url: '.'
            , readUrl: undefined
            , saveUrl: undefined
            , method: 'POST'
            , saveBaseParams: {}
            , readBaseParams: {}
            , paramNames: {
            	id: 'id'
                , name: 'name'
                , value: 'value'
                , user: 'user'
                , session: 'session'
                , data: 'data'
            }
    	}); // eo apply

    	if (this.autoRead) {
    		this.readState();
    	}

    	this.dt = Ext.create('Ext.util.DelayedTask', this.submitState, this);
    	if (this.autoStart) {
    		this.start();
    	}
    } //eof constructor


	// {{{
	
    , initState: function (state) {
    	if (state instanceof Array) {
    		Ext.each(state, function (item) {
    			this.state[item.name] = this.decodeValue(item[this.paramNames.value]);
    		}, this);
    	}
    	else {
    		this.state = state ? state : {};
    	}
    } // eo function initState
	// }}}
	// {{{
	
    , set: function (name, value) {
    	if (!name) { return; }


    	this.queueChange(name, value);
    } // eo function set
	// }}}
	// {{{
	
    , start: function () {
    	this.dt.delay(this.delay);
    	this.started = true;
    } // eo function start
	// }}}
	// {{{
	
    , stop: function () {
    	this.dt.cancel();
    	this.started = false;
    } // eo function stop
	// }}}
	// {{{
	
    , queueChange: function (name, value) {
    	var o = {}
            , i = 0
            , found = false
    	// see http://extjs.com/forum/showthread.php?p=344233
            , oldValue = this.state[name]
            , newValue
            , changed;

    	for (; i < this.queue.length; i++) {
    		if (this.queue[i].name === name) {
    			oldValue = this.decodeValue(this.queue[i].value);
    		}
    	}
    	//changed = undefined === oldValue || oldValue !== value;
    	//http://www.sencha.com/forum/showthread.php?24970-Buffering-Http-State-Provider&p=581091&viewfull=1#post581091
    	changed = undefined === oldValue || this.encodeValue(oldValue) !== this.encodeValue(value);

    	if (changed) {
    		newValue = this.encodeValue(value);
    		o[this.paramNames.name] = name;
    		o[this.paramNames.value] = newValue;
    		for (i = 0; i < this.queue.length; i++) {
    			if (this.queue[i].name === o.name) {
    				this.queue[i] = o;
    				found = true;
    			}
    		}
    		if (false === found) {
    			this.queue.push(o);
    		}
    		this.dirty = true;
    	}
    	if (this.started) {
    		this.start();
    	}
    	return changed;
    } // eo function bufferChange
	// }}}
	// {{{
	
    , submitState: function () {
    	if (!this.dirty || Ext.isEmpty(this.queue)) {
    		this.dt.delay(this.delay);
    		return;
    	}
    	this.dt.cancel();

    	var o = {
    		url: this.saveUrl || this.url
            , method: this.method
            , scope: this
            , success: this.onSaveSuccess
            , failure: this.onSaveFailure
    		//,queue:Ext.ux.util.clone(this.queue)
            , queueCopy: Ext.Array.clone(this.queue) //don't use 'queue', conflicts with ext-basex QueueManager 
            , params: {}
    	};

    	var params = Ext.apply({}, this.saveBaseParams);
    	params[this.paramNames.id] = this.id;
    	params[this.paramNames.user] = this.user;
    	params[this.paramNames.session] = this.session;
    	params[this.paramNames.data] = Ext.encode(o.queueCopy);

    	Ext.apply(o.params, params);

    	// be optimistic
    	this.dirty = false;

    	Ext.Ajax.request(o);
    } // eo function submitState
	// }}}
	// {{{
	
    , clear: function (name) {
    	this.set(name, undefined);
    } // eo function clear
	// }}}
	// {{{
	
    , onSaveSuccess: function (response, options) {
    	var o = {};
    	try { o = Ext.decode(response.responseText); }
    	catch (e) {
    		if (true === this.logFailure) {
    			this.log(this.saveFailureText, e, response);
    		}
    		this.dirty = true;
    		return;
    	}
    	if (true !== o.success) {
    		if (true === this.logFailure) {
    			this.log(this.saveFailureText, o, response);
    		}
    		this.dirty = true;
    	}
    	else {
    		Ext.each(options.queueCopy, function (item) {
    			if (!item) {
    				return;
    			}
    			var name = item[this.paramNames.name];
    			var value = this.decodeValue(item[this.paramNames.value]);

    			if (undefined === value || null === value) {
    				Ext.ux.state.HttpProvider.superclass.clear.call(this, name);
    			}
    			else {
    				// parent sets value and fires event
    				Ext.ux.state.HttpProvider.superclass.set.call(this, name, value);
    			}
    		}, this);
    		if (false === this.dirty) {
    			this.queue = [];
    		}
    		else {
    			var i, j, found;
    			for (i = 0; i < options.queueCopy.length; i++) {
    				found = false;
    				for (j = 0; j < this.queue.length; j++) {
    					if (options.queueCopy[i].name === this.queue[j].name) {
    						found = true;
    						break;
    					}
    				}
    				if (true === found && this.encodeValue(options.queueCopy[i].value) === this.encodeValue(this.queue[j].value)) {
    					Ext.Array.remove(this.queue, this.queue[j]);
    				}
    			}
    		}
    		if (true === this.logSuccess) {
    			this.log(this.saveSuccessText, o, response);
    		}
    		this.fireEvent('savesuccess', this);
    	}
    } // eo function onSaveSuccess
	// }}}
	// {{{
	
    , onSaveFailure: function (response, options) {
    	if (true === this.logFailure) {
    		this.log(this.saveFailureText, response);
    	}
    	this.dirty = true;
    	this.fireEvent('savefailure', this);
    } // eo function onSaveFailure
	// }}}
	// {{{
	
    , onReadFailure: function (response, options) {
    	if (true === this.logFailure) {
    		this.log(this.readFailureText, response);
    	}
    	this.fireEvent('readfailure', this);

    } // eo function onReadFailure
	// }}}
	// {{{
	
    , onReadSuccess: function (response, options) {
    	var o = {}, data;
    	try { o = Ext.decode(response.responseText); }
    	catch (e) {
    		if (true === this.logFailure) {
    			this.log(this.readFailureText, e, response);
    		}
    		return;
    	}
    	if (true !== o.success) {
    		if (true === this.logFailure) {
    			this.log(this.readFailureText, o, response);
    		}
    	}
    	else {
    		data = o[this.paramNames.data];
    		if (!(data instanceof Array) && true === this.logFailure) {
    			this.log(this.dataErrorText, data, response);
    			return;
    		}
    		//flush cache if not appending
    		if (this.flushCache) {
    			this.state = {};
    		}
    		Ext.each(data, function (item) {
    			this.state[item[this.paramNames.name]] = this.decodeValue(item[this.paramNames.value]);
    		}, this);
    		this.queue = [];
    		this.dirty = false;
    		if (true === this.logSuccess) {
    			this.log(this.readSuccessText, data, response);
    		}
    		this.fireEvent('readsuccess', this);
    	}
    } // eo function onReadSuccess
	// }}}
	// {{{
	
    , readState: function () {
    	var o = {
    		url: this.readUrl || this.url
            , method: this.method
            , scope: this
            , success: this.onReadSuccess
            , failure: this.onReadFailure
            , params: {}
            , async: this.async
    	};

    	var params = Ext.apply({}, this.readBaseParams);
    	params[this.paramNames.id] = this.id;
    	params[this.paramNames.user] = this.user;
    	params[this.paramNames.session] = this.session;

    	Ext.apply(o.params, params);
    	Ext.Ajax.request(o);
    } // eo function readState
	// }}}
	// {{{
	
    , log: function () {
    	if (console) {
    		console.log.apply(console, arguments);
    	}
    } // eo log
	// }}}

}); // eo extend

// eof
/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.Settings', {
	extend: 'gxui.UserControl',

	initialize: function () {
		this.callParent(arguments);
	},

	// Databinding
	SetState: function (data) {
		this.State = data;
	},

	// Databinding
	GetState: function (data) {
		return this.State;
	},

	onRender: function () {
		gxui.fixPaddingReset = gxui.CBoolean(this.RevertTablePaddingReset);
		gxui.fixSpacingReset = gxui.CBoolean(this.RevertTableSpacingReset);

		var provider = null;
		if (gxui.CBoolean(this.Enable)) {
			if (this.Provider == gxui.Settings.StateProvider.HTTP) {
				if (this.SaveURL != "") {
					provider = Ext.create('Ext.ux.state.HttpProvider', {
						saveUrl: this.SaveURL,
						autoRead: false
						
					});
				}
			}
			else {
				if (this.Provider == gxui.Settings.StateProvider.Cookie) {
					provider = Ext.create('Ext.state.CookieProvider', {
						expires: new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 365)) //365 days
					})
				}
			}
		}
		else {
			// This is equivalent to removing the default provider set in gxui.js.
			provider = Ext.create('Ext.state.Provider');
		}

		if (provider) {
			// Initialize state provider (required to be able to keep state in controls)
			Ext.state.Manager.setProvider(provider);
			if (this.Provider == gxui.Settings.StateProvider.HTTP) {
				Ext.state.Manager.getProvider().initState(this.State);
			}
			this.State = []; //Reset initial state to avoid innecessary traffic
		}
	},

	onDestroy: Ext.emptyFn,

	LOCALE_SCRIPT_ID: "ext-lang",

	methods: {
		// Methods
		
		SetLanguage: function (lang, charset) {
			var s = Ext.getDom(this.LOCALE_SCRIPT_ID);
			var src = gx.util.resourceUrl(gx.basePath + gx.staticDirectory + "Shared/ext/locale/ext-lang-" + lang + ".js", true);
			if (!s) {
				s = document.createElement("script");
				s.id = this.LOCALE_SCRIPT_ID;
				s.type = 'text/javascript';
				document.getElementsByTagName("head")[0].appendChild(s);
			}
			s.src = src;
			if (charset) {
				s.charset = charset;
			}
		},

		RemoveLanguage: function () {
			var s = Ext.getDom(this.LOCALE_SCRIPT_ID);
			if (s) {
				document.getElementsByTagName("head")[0].removeChild(s);
			}
		}
	}
});

// Supported state providers
gxui.Settings.StateProvider = {
	Cookie: "Cookie",
	HTTP: "HTTP"
};

/// <reference path="..\..\Freezer\Ext\ext-all-dev.js" />


Ext.define('gxui.Splash', {
	extend: 'gxui.UserControl',

	initialize: function () {
		this.callParent(arguments);
	},

	onRender: function () {
		var cookieId = this.getUniqueId() + '-cookie';

		if (gxui.getCookie(cookieId) == "") {
			try {
				this.m_mask = Ext.get(document.body).mask(this.Message + " ", this.Cls);
				this.m_mask.next().addCls('gxui-splash-msg');
				setTimeout(function () {
					Ext.get(document.body).unmask();
				}, this.Duration * 1000);
			}
			catch (err) { };
			gxui.setCookie(cookieId, '1', 0);
		}
	}

});

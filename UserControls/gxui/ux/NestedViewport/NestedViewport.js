/*
 * GxUI Library 2.0.1
 * Copyright (c) 2009, Artech
 * All rights reserved.
 * 
 * GxUI Library is freely distributable under the terms of the BSD license.
 * 
 */

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

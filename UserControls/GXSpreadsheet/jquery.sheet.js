/*
	jQuery.sheet() Spreadsheet with Calculations Plugin
	Verison: 0.52
	Copywrite Robert Plummer 2008-2009
	
	Dimensions Info:
		When dealing with size, it seems that outerHeight is generally the most stable cross browser
		attribute to use for bar sizing.  We try to use this as much as possible.  But because col's
		don't have boarders, we subtract or add jS.attrH.boxModelCorrection() for those browsers.
	
	If vs Switch:
		I try to use case over If as much as possible, especially on loops, because it's faster, even on try false, not much,
		but a little (100ms in some cases).  So if you have 10 if statements firing at the same time,
		the difference is notable.
*/
jQuery.fn.extend({
	sheet: function(settings) {
		settings = jQuery.extend({
			urlGet: "documentation.html table:first",
			urlSave: "save.html",
			title: '',
			editable: true,
			urlBaseCss: 'jquery.sheet.css',
			urlTheme: "theme/jquery-ui-1.7.2.custom.css",
			urlMenu: "menu.html",
			urlMenuJs: "plugins/mbMenu.min.js",
			urlMenuCss: "plugins/menu.css",
			urlMetaData: "plugins/jquery.metadata.js",
			urlScrollTo: "plugins/jquery.scrollTo-min.js",
			urlScrollsync: 'plugins/scrollsync.js',
			urlJGCharts: 'plugins/jgcharts.pack.js',
			loading: 'Loading Spreadsheet...',
			newColumnWidth: 120,
			ajaxSaveType: 'POST',
			buildSheet: false,//'10x30', this can be slow
			calcOff: false,
			log: false,
			lockFormulas: false,
			parent: this,
			colMargin: 18, //If text size make cell bigger than this number the bars will be off on loadtime
			fnBefore: function() {},
			fnAfter: function() {
				jS.obj.formula().focus().select();
			},
			fnSave: function() { jS.saveSheet(); },
			fnOpen: function() {},
			fnClose: function() {},
			joinedResizing: false //this joins the column/row with the resize bar
		}, settings);
		jQuery.fn.sheet.settings = jS.s = settings;
		settings.fnBefore();
		
		jS.getCss(settings.urlBaseCss);
		
		//We need to take the sheet out of the parent in order to get an accurate reading of it's height and width
		var tempSheet = jQuery(this).find('table').hide();
		jQuery(this).html(jS.s.loading);

		settings.width = jQuery(settings.parent).width();
		settings.height = jQuery(settings.parent).height();
		
		if (jS.s.log) {
			jQuery(jS.s.parent).after('<textarea id="' + jS.id.log + '" />');
		}
		
		var sheetUI = jQuery(
			'<div id="' + jS.id.ui + '">'+
				'<table cellpadding="0" cellspacing="0" border="0" class="tableControl ui-corner-bottom" style="border-collapse:collapse; border-width: 1px ! important; padding: none ! important; margin: none ! important;"><tbody><tr>' + 
					'<td id="' + jS.id.barCornerParent + '">' + //corner
						'<div style="height: ' + jS.s.colMargin + '; width: ' + jS.s.colMargin + ';" id="' + jS.id.barCorner + '" class="ui-icon ui-icon-refresh" onclick="jS.cellEditAbandon();">&nbsp;</div>'+
					'</td><td>' + //barTop
						'<div style="overflow: hidden;" id="' + jS.id.barTopParent + '" />'+
					'</td></tr><tr><td>' + //barLeft
						'<div style="overflow: hidden;width: ' + jS.s.colMargin + ';" id="' + jS.id.barLeftParent + '" />' +
					'</td><td>' + //pane
						'<div id="' + jS.id.pane + '"></div>'+
					'</td>' +
				'</tr></tbody></table>' +
			'</div>'
		);

		//Make functions upper and lower case compatible
		for (var k in cE.fn) {
			var kLower = k.toLowerCase();
			if (kLower != k) {
				cE.fn[kLower] = cE.fn[k];
			}
		}
		
		jS.obj.parent().html(sheetUI);
		
		if (tempSheet) {
			jS.obj.pane().html(tempSheet);
		}
		
		if (!jS.s.buildSheet) {
			jS.openSheet(jS.s.urlGet, false, true);
		} else {
			jS.openSheet(jS.s.urlGet, jS.s.buildSheet.toLowerCase(), true);
		}
	}
});

var jS = jQuery.sheet = {
	version: 0.52,
	s: {},//s = settings object, used for shorthand, populated from jQuery.sheet
	obj: {//obj = object references
		parent: 		function() { return jQuery(jS.s.parent) },
		ui:				function() { return jQuery('#' + jS.id.ui) },
		sheet: 			function() { return jQuery('#' + jS.id.sheet) },
		bar:			function() { return jQuery('.' + jS.cl.bar) },
		barTop: 		function() { return jQuery('#' + jS.id.barTop) },
		barTopParent: 	function() { return jQuery('#' + jS.id.barTopParent) },
		barLeft: 		function() { return jQuery('#' + jS.id.barLeft) },
		barLeftParent: 	function() { return jQuery('#' + jS.id.barLeftParent) },
		barCorner:		function() { return jQuery('#' + jS.id.barCorner) },
		barCornerParent:function() { return jQuery('#' + jS.id.barCornerParent) },
		barSelected:	function() { return jQuery('.' + jS.cl.barSelected) },
		cell: 			function() { return jQuery('.' + jS.cl.cell) },
		controls:		function() { return jQuery('#' + jS.id.controls) },
		formula: 		function() { return jQuery('#' + jS.id.formula) },
		label: 			function() { return jQuery('#' + jS.id.label) },
		fx:				function() { return jQuery('#' + jS.id.fx) },
		pane: 			function() { return jQuery('#' + jS.id.pane) },
		log: 			function() { return jQuery('#' + jS.id.log) },
		menu:			function() { return jQuery('#' + jS.id.menu) },
		title:			function() { return jQuery('#' + jS.id.title) },
		uiDefault:		function() { return jQuery('.' + jS.cl.uiDefault) },
		uiActive:		function() { return jQuery('.' + jS.cl.uiActive) },
		uiBase:			function() { return jQuery('.' + jS.cl.uiBase) },
		uiCell:			function() { return jQuery('.' + jS.cl.uiCell) },
		toggle:			function() { return jQuery('.' + jS.cl.toggle) },
		tableBody: 		function() { return document.getElementById(jS.id.sheet) },
		title: 	function() { return jQuery('#' + jS.id.title) }
	},
	id: {//id = id's references
		sheet: 			'jSheet',//This con probably be just about any value as long as it's not a duplicated id
		ui:				'jSheetUI',
		barTop: 		'jSheetBarTop',
		barTopParent: 	'jSheetBarTopParent',
		barLeft: 		'jSheetBarLeft',
		barLeftParent: 	'jSheetBarLeftParent',
		barCorner:		'jSheetBarCorner',
		barCornerParent:'jSheetBarCornerParent',
		controls:		'jSheetControls',
		formula: 		'jSheetControls_formula',
		label: 			'jSheetControls_loc',
		fx:				'jSheetControls_fx',
		pane: 			'jSheetEditPane',
		log: 			'jSheetLog',
		menu:			'jSheetMenu',
		title:			'sheetTitle'
	},
	cl: {//cl = class references
		sheet: 			'jSheet',
		bar: 			'jSheetBar',
		cell: 			'jSheetCellActive',
		calcOff: 		'jSheetCalcOff',
		barSelected: 	'jSheetBarItemSelected',
		uiDefault:		'ui-state-default',
		uiActive:		'ui-state-active',
		uiBase:			'ui-widget-content',
		uiParent: 		'ui-widget-content ui-corner-all',
		uiBar: 			'ui-widget-header',
		uiPane: 		'ui-widget-content',
		uiMenuUl: 		'ui-widget-header',
		uiMenuLi: 		'ui-widget-header',
		uiMenuHighlighted: 'ui-state-highlight',
		uiControl: 		'ui-widget-header ui-corner-top',
		uiControlTextBox:'ui-widget-content',
		uiCell:			'themeRoller_activeCell',
		uiCellHighlighted: 'ui-state-highlight',
		toggle:			'cellStyleToggle'
	},
	ERROR: function() { return cE.ERROR },
	tuneTableForSheetUse: function(obj, r) {
		obj
			.addClass(jS.cl.sheet)
			.attr('id', jS.id.sheet)
			.attr('border', '1px');
		obj.find('.' + jS.cl.uiCell).removeClass(jS.cl.uiCell);
		obj.find('td')
			.css('background-color', '')
			.css('color', '')
			.css('height', '')
			.attr('height', '');
			
		if (r) {
			return obj;
		}
	},
	attrH: {//Attribute Helpers
	//I created this object so I could see, quickly, which attribute was most stable.
	//As it turns out, all browsers are different, thus this has evolved to a much uglier beast
		width: function(obj, skipCorrection) {
			return jQuery(obj).outerWidth() - jS.attrH.boxModelCorrection(skipCorrection);
		},
		widthReverse: function(obj, skipCorrection) {
			return jQuery(obj).outerWidth() + jS.attrH.boxModelCorrection(skipCorrection);
		},
		height: function(obj, skipCorrection) {
			return jQuery(obj).outerHeight() - jS.attrH.boxModelCorrection(skipCorrection);
		},
		heightReverse: function(obj, skipCorrection) {
			return jQuery(obj).outerHeight() + jS.attrH.boxModelCorrection(skipCorrection);
		},
		syncSheetWidthFromTds: function(obj) {
			var entireWidth = 0;
			obj = (obj ? obj : jS.obj.sheet());
			obj.find('tr:first').find('td').each(function() {
				entireWidth += jQuery(this).width();
			});
			obj.width(entireWidth);
		},
		boxModelCorrection: function(skipCorrection) {
			var correction = 0;
			if (jQuery.support.boxModel && !skipCorrection) {
				correction = 2;
			}
			return correction;
		},
		setHeight: function(i, from, skipCorrection, obj) {
			var correction = 0;
			var h = 0;
			var fn;
			
			switch(from) {
				case 'cell':
					obj = (obj ? obj : jS.obj.barLeft().find('div').eq(i))
					h = jS.attrH.height(jQuery(jS.getTd(null, i + 1, 1)).parent().andSelf(), skipCorrection);
					break;
				case 'bar':
					obj = (obj ? obj : jQuery(jS.getTd(null, i + 1, 1)).parent().andSelf());
					h = jS.attrH.heightReverse(jS.obj.barLeft().find('div').eq(i), skipCorrection);
					break;
			}
			
			if (h) {
				jQuery(obj)
					.height(h)
					.css('height', h)
					.attr('height', h);
			}

			return obj;
		}
	},
	makeBarItemLeft: function(url) {//Works great!
		jS.obj.barLeft().remove();
		var barLeft = jQuery('<div border="1px" id="' + jS.id.barLeft + '" />').height('10000px');
		var heightFn;
		if (url) { //This is our standard way of detecting height when a sheet loads from a url
			heightFn = function(i, objSource, objBar) {
				objBar.height(parseInt(objSource.outerHeight()) - jS.attrH.boxModelCorrection());
			}
		} else { //This way of detecting height is used becuase the object has some problems getting
				//height because both tr and td have height set
				//This corrects the problem
				//This is only used when a sheet is already loaded in the pane
			heightFn = function(i, objSource, objBar) {
				objBar.height(parseInt(objSource.css('height').replace('px','')) - jS.attrH.boxModelCorrection());
			}
		}
		jS.obj.sheet().find('tr').each(function(i) {
			
			var child = jQuery('<div><span>' + (i + 1) + '</span></div>');
			jQuery(barLeft).append(child);
			heightFn(i, jQuery(this), child);
			jS.getResizeControl.height(child);
		});
		barLeft.appendTo(jS.obj.barLeftParent());
	},
	makeBarItemTop: function(url) { //Works great!
		jS.obj.barTop().remove();
		var barTop = jQuery('<div id="' + jS.id.barTop + '" class="' + jS.cl.bar + '" />').width('10000px');
		barTop.height(jS.s.colMargin);
		
		var parents;
		var widthFn;
		
		if (url) {
			parents = jS.obj.pane().find('tr:first td');
			widthFn = function(obj) {
				return jS.attrH.width(obj);
			}
		} else {
			parents = jS.obj.pane().find('col');
			widthFn = function(obj) {
				return parseInt(jQuery(obj).css('width').replace('px','')) - jS.attrH.boxModelCorrection();
			}
		}
		
		parents.each(function(i) {
			var v = cE.columnLabelString(i + 1);
			var w = widthFn(this);
			
			var child = jQuery("<div>" + v + "</div>")
				.width(w)
				.height(jS.s.colMargin);
			
			jS.getResizeControl.width(child);

			barTop.append(child);
		});
		
		// Prepend one colgroup/col element that covers the new row headers.
		//jS.attrH.syncSheetWidthFromTds();
		
		jS.obj.barTopParent().append(barTop);
	},
	setTdIds: function() {
		jS.obj.sheet().find('tr').each(function(row) {
			jQuery(this).find('td').each(function(col) {
				jQuery(this).attr('id', jS.getTdId(row + 1, col + 1));
			});
		});
	},
	toggleHide: {//These are not ready for prime time
		row: function(i) {
			if (!i) {//If i is empty, lets get the current row
				i = jS.obj.cell().parent().attr('rowIndex');
			}
			if (i) {//Make sure that i equals something
				var o = jS.obj.barLeft().find('div').eq(i);
				if (o.is(':visible')) {//This hides the current row
					o.hide();
					jS.obj.sheet().find('tr').eq(i).hide();
				} else {//This unhides
					//This unhides the currently selected row
					o.show();
					jS.obj.sheet().find('tr').eq(i).show();
				}
			} else {
				alert('No row selected.');
			}
		},
		rowAll: function() {
			jS.obj.sheet().find('tr').show();
			jS.obj.barLeft().find('div').show();
		},
		column: function(i) {
			if (!i) {
				i = jS.obj.cell().attr('cellIndex');
			}
			if (i) {
				//We need to hide both the col and td of the same i
				var o = jS.obj.barTop().find('div').eq(i);
				if (o.is(':visible')) {
					jS.obj.sheet().find('tbody tr').each(function() {
						jQuery(this).find('td').eq(i).hide();
					});
					o.hide();
					jS.obj.sheet().find('colgroup col').eq(i).hide();
					jS.toggleHide.columnSizeManage();
				}
			} else {
				alert('Now column selected.');
			}
		},
		columnAll: function() {
		
		},
		columnSizeManage: function() {
			var w = jS.obj.barTop().width();
			var newW = 0;
			jS.obj.barTop().find('div').each(function() {
				var o = jQuery(this);
				if (o.is(':hidden')) {
					newW += o.width();
				}
			});
			jS.obj.barTop().width(w);
			jS.obj.sheet().width(w);
		}
	},
	getResizeControl: {
		height: function(obj) {
			if (jS.s.editable) {
				obj
					.unbind('mousedown')
					.unbind('dblclick')
					.mousedown(function(e) { 
						jS.barResizer(e, jQuery(this), 'row');
						return false;
					})
					.dblclick(function() {
						jS.cellEditAbandon();
						var i = jQuery.trim(jQuery(this).text());
						i = parseInt(i);
						jS.cellSetActiveMultiRow(i);
					});
			}
		},
		width: function(obj) {
			if (jS.s.editable) {
				obj
					.unbind('mousedown')
					.unbind('dblclick')
					.mousedown(function(e) {
						jS.barResizer(e, jQuery(this), 'column');
						return false;
					})
					.dblclick(function() {
						jS.cellEditAbandon();
						var i = cE.columnLabelIndex(jQuery.trim(jQuery(this).text()));
						i = parseInt(i);
						jS.cellSetActiveMultiColumn(i);
					});
			}
		}
	},
	makeControls: function(parent) {
		jS.obj.controls().remove();
		if (jS.s.editable) {
			// Register onclick for tableBody td elements.
			jS.obj.pane().find('td')
				.mousedown(jS.cellOnMouseDown)
				.click(jS.cellOnClick);
			var controls = jQuery('<div id="' + jS.id.controls + '" />');
			//Lets get the page title information			
			var sheetTitle = jS.sheetTitle(true);
			
			if (jS.s.urlMenuJs && jS.s.urlMenuCss && jS.s.urlMetaData && jS.s.urlMenu) {
				jQuery.getScript(jS.s.urlMetaData);
				jQuery.getScript(jS.s.urlMenuJs, function() {
					jS.getCss(jS.s.urlMenuCss);
					var menuObj = jQuery('<div />').load(jS.s.urlMenu, function() {
						controls.prepend(menuObj.html());
						jS.obj.menu()
							.buildMenu({
								additionalData:"pippo=1",
								menuWidth: 100,
								openOnRight:false,
								menuSelector: ".menuContainer",
								hasImages:false,
								fadeInTime: 0,
								fadeOutTime:0,
								adjustLeft:2,
								minZindex:"auto",
								adjustTop:10,
								opacity:.95,
								shadow:true,
								closeOnMouseOut:false,
								closeAfter:1000
							});
						
						//Append Title
						jS.obj.menu().find('#titleHolder').append('<span id="' + jS.id.title + '">' + sheetTitle + '</span>');
							
						jS.sheetSyncSize();
					});
				});
			} else {
				controls.append('&nbsp;&nbsp;&nbsp;<span id="' + jS.id.title + '">' + sheetTitle + '</span>');
			}
			
			controls.append('<table style="width: 100%;">' +
							'<tr>' +
								'<td style="width: 35px; text-align: right;" id="' + jS.id.label + '"></td>' +
								'<td style="width: 10px;" id="' + jS.id.fx + '">fx</td>' + 
								'<td>' +
									'<textarea id="' + jS.id.formula + '"></textarea>' +
								'</td>' +
							'</tr>' +
						'</table>');
			
			
			
			//Get the scrollTo Pluggin
			if (jS.s.urlScrollTo) {
				jQuery.getScript(jS.s.urlScrollTo);
			}
			
			controls.keydown(function(e) {
				return jS.formulaKeyDown(e);
			});

			jQuery(parent).prepend(controls);
			jS.sheetSyncSize();
		} else {
			jS.sheetSyncSize();
		}
	},
	sheetDecorate: function() {	
		jS.sheetSyncSizeToCols();
		jS.formatSheet();
		jS.sheetDecorateRemove(jS.obj.sheet());
		jS.makeControls(jS.obj.ui());
	},
	formatSheet: function() {
		if (!jS.obj.parent().find('tbody').length > 0) {
			jS.obj.sheet().wrap('<tbody />');
		}
		
		if (!jS.obj.parent().find('colgroup').length > 0) {
			var colgroup = jQuery('<colgroup />');
			jS.obj.sheet().find('tr:first').find('td').each(function() {
				var w = jQuery(this).width();
				jQuery(this)
					.width(w)
					.css('width', w)
					.attr('width', w);
				jQuery('<col />')
					.width(w)
					.css('width', w)
					.attr('width', w)
					.appendTo(colgroup);
			});
			colgroup.insertBefore(jS.obj.sheet().parent());
		}
	},
	getCss: function(url) {
		jQuery('head').append('<link rel="stylesheet" type="text/css" href="' + url + '"></link>')
	},
	themeRoller: {
		start: function() {
			jS.getCss(jS.s.urlTheme);		
			//Style sheet			
			jS.obj.parent().addClass(jS.cl.uiParent);
			jS.obj.sheet().addClass(jS.cl.uiParent);
			//Style bars
			jS.obj.barLeft().find('div').addClass(jS.cl.uiBar)
			jS.obj.barTop().find('div').addClass(jS.cl.uiBar);
			jS.obj.barCornerParent().addClass(jS.cl.uiBar);
			
			jS.obj.controls().addClass(jS.cl.uiControl);
			jS.obj.fx().addClass(jS.cl.uiControl);
			jS.obj.label().addClass(jS.cl.uiControl);
			jS.obj.formula().addClass(jS.cl.uiControlTextBox);
		},
		cell: function(td) {
			jS.themeRoller.clearCell();
			if (td) {
				jQuery(td)
					.addClass(jS.cl.uiCellHighlighted)
					.addClass(jS.cl.uiCell);;
			}
		},
		clearCell: function() {
			jS.obj.uiActive().removeClass(jS.cl.uiActive);
			jS.obj.uiCell()
				.removeAttr('style')
				.removeClass(jS.cl.uiCellHighlighted)
				.removeClass(jS.cl.uiCell);
		},
		newBar: function(obj) {//This is for a tr
			jQuery(obj).addClass(jS.cl.uiBar);
		},
		barTop: function(i) {
			jS.obj.barTop().find('div').eq(i - 1).addClass(jS.cl.uiActive);
		},
		barLeft: function(i) {
			jS.obj.barLeft().find('div').eq(i - 1).addClass(jS.cl.uiActive);
		},
		barObj: function(obj) {
			jQuery(obj).addClass(jS.cl.uiActive);
		},
		clearBar: function() {
			jS.obj.barTop().find('.' + jS.cl.uiActive).removeClass(jS.cl.uiActive);
			jS.obj.barLeft().find('.' + jS.cl.uiActive).removeClass(jS.cl.uiActive);
		}
	},
	manageHtmlToText: function(v) {
		v = jQuery.trim(v);
		if (v.charAt(0) != "=") {
			v = v.replace(/&nbsp;/g, ' ')
				.replace(/&gt;/g, '>')
				.replace(/&lt;/g, '<')
				.replace(/\t/g, '')
				.replace(/\n/g, '')
				.replace(/<br>/g, '\r')
				.replace(/<BR>/g, '\n');

			jS.log("from html to text");
		}
		return v;
	},
	manageTextToHtml: function(v) {	
		v = jQuery.trim(v);
		if (v.charAt(0) != "=") {
			v = v.replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;')
				.replace(/ /g, '&nbsp;')
				.replace(/>/g, '&gt;')
				.replace(/</g, '&lt;')
				.replace(/\n/g, '<br>')
				.replace(/\r/g, '<br>');
			
			jS.log("from text to html");
		}
		return v;
	},
	sheetDecorateRemove: function(obj, removeHeight) {
		//remove class jSheetCellActive
		jQuery(obj).find('.' + jS.cl.cell).removeClass(jS.cl.cell);
		//remove class ui-state-highlight
		jQuery(obj).find('.' + jS.cl.uiCellHighlighted).removeClass(jS.cl.uiCellHighlighted);
		//remove class themeRoller_activeCell
		
		jQuery(obj).find('.' + jS.cl.uiCell).removeClass(jS.cl.uiCell);
		//IE Bug, match width with css width
		jQuery('col', obj).each(function(i) {
			var v = jQuery(this).css('width') + 'px';
			jQuery(obj).find('col').eq(i).attr('width', v);
		});
		
		if (removeHeight) {
			jQuery(obj).find('td').andSelf()
				.css('height', '')
				.attr('height', '');
		}
	},
	cellIsEdit: false,
	sheetIsEdit: false, //we don't want to have to recompile the sheet every time a person presses a button
	getCellClickFn: function() {
		return (jS.s.lockFormulas ? jS.cellOnClickLocked : jS.cellOnClickReg);
	},
	cellClick: function(keyCode) {
		var h = 0;
		var v = 0;
		switch (keyCode) {
			case key.UP: 		v--; break;
			case key.DOWN: 		v++; break;
			case key.LEFT: 		h--; break;
			case key.RIGHT: 	h++; break;
		}
		jQuery(jS.getTd(null, jS.cellEditLastLoc[1] + v, jS.cellEditLastLoc[2] + h)).click();
		
		return false;
	},
	cellOnMouseDown: function(evt) {
		if (evt.altKey) {
			jS.cellSetActiveMulti(evt);
			jQuery(document).mouseup(function() {
				jQuery(this).unbind('mouseup');
				var v = jS.obj.formula().val();
				jS.obj.formula().val(v + jS.getTdRange());
			});
		} else {
			jS.cellSetActiveMulti(evt);
		}
		
		switch (!isNaN(evt.target.cellIndex)) {//This is to detect if it is a textarea or other obj
			case true: 
				jS.obj.formula().focus().select();
				return false;
		}
	},
	cellOnClickLocked: function(evt) {
		if (!isNaN(evt.target.cellIndex)) {
			if (!jQuery(evt.target).attr('formula')) {
				jS.cellOnClickManage(jQuery(evt.target));
			}
		} else {
			jSCellEditAbandon();
			jS.obj.formula().focus().select();
		}
	},
	cellOnClickReg: function(evt) {
		if (!isNaN(evt.target.cellIndex)) {		
			jS.cellOnClickManage(jQuery(evt.target));
		} else {
			jS.obj.formula().focus().select();
			return false;
		}
	},
	cellOnClickManage: function(td) {
		if (!isNaN(td[0].cellIndex)) {
			if (!td.hasClass(jS.cl.cell)) {
				
				jS.cellEdit(td);
				jS.log('click cell');
			} else {
				jS.cellIsEdit = true;
				jS.isSheetEdit = true;
				jS.cellTextArea(td, false, true);
				jS.log('click, textarea over table activated');
			}
			jS.followMe();
		} else {
			return false;
		}
	},
	cellEdit: function(td) {
		//This finished up the edit of the last cell
		jS.cellEditDone();
		var loc = jS.getTdLocation(td);
		//Show where we are to the user
		jS.obj.label().html(cE.columnLabelString(loc[1]) + loc[0]);
		
		var v = td.attr('formula');
		if (!v) {
			v = jS.manageHtmlToText(td.html());
		}
		
		jS.obj.formula()
			.val(v)
			.focus()
			.select();
		jS.cellSetActive(td, loc);
	},
	cellSetActive: function(td, loc) {
		jS.cellEditLastLoc[0] = td;
		jS.cellEditLastLoc[1] = loc[0];
		jS.cellEditLastLoc[2] = loc[1];
		
		jS.themeRoller.cell(td);
		jS.themeRoller.barLeft(loc[0]);
		jS.themeRoller.barTop(loc[1]);
		
		td.addClass(jS.cl.cell);
		jS.obj.barLeft().find('div').eq(loc[0] - 1).addClass(jS.cl.barSelected);
		jS.obj.barTop().find('div').eq(loc[1] - 1).addClass(jS.cl.barSelected);
	},
	cellEditLastLoc: new Array(),
	cellEditDone: function(bsheetClearActive) {
		switch (jS.cellIsEdit) {
			case true:
				// Any changes to the input controls are stored back into the table, with a recalc.
				var loc = [jS.cellEditLastLoc[1], jS.cellEditLastLoc[2]];
				var td = jS.cellEditLastLoc[0];
				var recalc = false;
				
				//Lets ensure that the cell being edited is actually active
				if (td && loc && td.hasClass(jS.cl.cell)) { 
					//This should return either a val from textbox or formula, but if fails it tries once more from formula.
					var v = jS.cellTextArea(td, true);

					//inputFormula.value;
					var noEditFormula = false;
					var noEditNumber = false;
					var noEditNull = false;
					var editedFormulaToFormula = false;
					var editedFormulaToReg = false;
					var editedRegToFormula = false;
					var editedRegToReg = false;
					var editedToNull = false;
					var editedNumberToNumber = false;
					var editedNullToNumber = false;
					
					var tdFormula = td.attr('formula');
					var tdPrevVal = td.attr('prevVal');

					if (v) {
						if (v.charAt(0) == '=') { //This is now a formula
							if (v != tdFormula) { //Didn't have a formula before but now does
								editedFormulaToFormula = true;
								jS.log('edit, new formula, possibly had formula');
							} else if (tdFormula) { //Updated using inline edit
								noEditFormula = true;
								jS.log('no edit, has formula');
							} else {
								jS.log('no edit, has formula, unknown action');
							}
						} else if (tdFormula) { //Updated out of formula
							editedRegToFormula = true;
							jS.log('edit, new value, had formula');
						} else if (!isNaN(parseInt(v))) {
							if ((v != tdPrevVal && v != jS.obj.formula().val()) || (td.text() != v)) {
								editedNumberToNumber = true;
								jS.log('edit, from number to number, possibly in function');
							} else {
								noEditNumber = true;
								jS.log('no edit, is a number');
							}
						} else { //Didn't have a formula before of after edit
							editedRegToReg = true;
							jS.log('possible edit from textarea, has value');
						}
					} else { //No length value
						if (td.html().length > 0 && tdFormula) {
							editedFormulaToReg = true;
							jS.log('edit, null value from formula');
						} else if (td.html().length > 0 && tdFormula) {
							editedToNull = true;
							jS.log('edit, null value from formula');
						
						} else {
							noEditNull = true;
							jS.log('no edit, null value');
						}
					}
					
					td.removeAttr('prevVal');
					v = jS.manageTextToHtml(v);
					if (noEditFormula) {
						td.html(tdPrevVal);
					} else if (editedFormulaToFormula) {
						recalc = true;
						td.attr('formula', v).html('');
					} else if (editedFormulaToReg) {
						recalc = true;
						td.removeAttr('formula').html(v);
					} else if (editedRegToFormula) {
						recalc = true;
						td.removeAttr('formula').html(v);
					} else if (editedRegToReg) {
						td.html(v);
					} else if (noEditNumber) {
						td.html(v); 
					} else if (noEditNull) {
						td.html(v);
					} else if (editedNumberToNumber) {
						recalc = true;
						td.html(v);
					} else if (editedToNull) {
						recalc = true;
						td.removeAttr('formula').html('');
					}
					
					if (recalc) {
						jS.calc(jS.obj.sheet());
					}
					
					if (bsheetClearActive != false) {
						// Treats null == true.
						jS.sheetClearActive();
					}
					
					jS.obj.barLeft().find('div').eq(loc[0] - 1)
						.height(jS.attrH.height(td.parent()));
					
					jS.obj.formula().focus().select();
					jS.cellIsEdit = false;
				}
				break;
			default:
				jS.attrH.setHeight(jS.cellEditLastLoc[2] - 1, 'cell', false);
				jS.sheetClearActive();
		}
	},
	cellEditAbandon: function() {
		jS.themeRoller.clearCell();
		jS.themeRoller.clearBar();
		var v = jS.cellTextArea(jS.cellEditLastLoc[0], true);
		if (v) {
			jS.cellEditLastLoc[0].html(jS.manageTextToHtml(v));
			jS.sheetClearActive();
			if (v.charAt(0) == '=') {
				jS.calc(jS.obj.sheet());
			}
		} else { //Even if the cell is blank, that doesn't mean it's not active
			jS.sheetClearActive();
			jS.calc(jS.obj.sheet());
		}
		jS.cellEditLastLoc[0] = jS.obj.sheet().find('td:first');
		jS.cellEditLastLoc[1] = 0;
		jS.cellEditLastLoc[2] = 1;
		jS.obj.label().html('');

		return false;
	},
	keyDownHandler: {
		enterOnTextArea: function(evt) {
			if (evt.ctrlKey) {
				return jS.cellClick(key.DOWN);
			} else {
				return true;
			}
		},
		enter: function(evt) {
			if (!jS.cellIsEdit && !evt.ctrlKey) {
				return jS.cellClick();
			} else {
				return jS.cellClick(key.DOWN);
			}
		},
		tab: function(evt) {
			if (evt.shiftKey) {
				return jS.cellClick(key.LEFT);
			} else {
				return jS.cellClick(key.RIGHT);
			}
		},
		textAreaKeyDown: function(evt) {
			switch (evt.keyCode) {
				case key.ENTER: 	return jS.keyDownHandler.enterOnTextArea(evt);
					break;
				case key.TAB: 		return jS.keyDownHandler.tab(evt);
					break;
			}
		},
		formulaKeyDown: function(evt) {
			switch (evt.keyCode) {
				case key.ESCAPE: 	jS.cellEditAbandon();					break;
				case key.TAB: 		return jS.keyDownHandler.tab(evt);		break;
				case key.ENTER: 	return jS.keyDownHandler.enter(evt);	break;
				case key.LEFT:
				case key.UP:
				case key.RIGHT:
				case key.DOWN:		return jS.cellClick(evt.keyCode);		break;
				default: 			jS.cellIsEdit = true;
			}
		}
	},
	formulaKeyDown: function(evt, isTextArea) {
		//Switch is much faster than if statements
		//I found that it's much easier to go from the origin key (up, down, left, right, tab, enter) and then detect if the ctrl key or shift keys are down.
		//It's just difficult to look at later on and it's probably faster overall
		return (isTextArea ? jS.keyDownHandler.textAreaKeyDown(evt) : jS.keyDownHandler.formulaKeyDown(evt))
	},
	cellStyleToggle: function(setClass, removeClass) {
		//Lets check to remove any style classes
		if (removeClass) {
			removeClass = removeClass.split(',');
			
			jQuery(removeClass).each(function() {
				jS.obj.uiCell().removeClass(this);
			});
		}
		//Now lets add some style
		if (jS.obj.uiCell().hasClass(setClass)) {
			jS.obj.uiCell().removeClass(setClass);
		} else {
			jS.obj.uiCell().addClass(setClass);
		}
		jS.obj.formula()
			.focus()
			.select();
		return false;
	},
	context: {},
	calc: function(tableBody, fuel) {
		jS.log('Calculation Started');
		if (tableBody && !jS.s.calcOff) {
			cE.calc(new jS.tableCellProvider(tableBody.id), jS.context, fuel);
			jS.isSheetEdit = false;
		}
		jS.log('Calculation Ended');
	},
	cellTextArea: function(td, returnVal, makeEdit, setVal) {
		//Remove Textarea and transfer value.
		var v;
		if (td) {
			if (!makeEdit) {
				var textArea = td.find('textarea');
				var textAreaVal = textArea.val();
				if (textAreaVal || jS.obj.formula().attr('disabled')) {
					jS.log('Textarea value used');
					v = textAreaVal;
					textArea.remove();
					//td
					//	.css('text-align', '')
					//	.css('vertical-align', '');
				} else {
					jS.log('Formula value used');
					v = jS.obj.formula().val();
				}
				jS.obj.formula().removeAttr('disabled');
			} else {
				if (setVal) {
					v = setVal;
				} else {
					v = jS.obj.formula().val();
				}
				
				jS.obj.formula().attr('disabled', 'true');
				
				var textArea = jQuery('<textarea id="tempText" />');
				var h = jS.attrH.height(td);
				
				//There was an error in some browsers where they would mess this up.
				td.parent().height(h + jS.attrH.boxModelCorrection());
				//create text area.  Agian, strings are faster than DOM.
				textArea
					.height(h < 75 ? 75 : h)
					.val(jQuery.trim(v))
					.click(function(){
						return false;
					})
					.keydown(function(e) {
						return jS.formulaKeyDown(e, true);
					});
				
				//Se we can look at the past value after edit.
				if (td.attr('formula')) {
					td.attr('prevVal', td.text()).removeAttr('formula');
				}
				//add it to cell
				td.html(textArea);
				//focus textarea
				textArea
					.focus()
					.select();
			}
			if (returnVal) {
				return v;
			}
		}
	},
	refreshLabelsColumns: function(){
		var w = 0;
		jS.obj.barTop().find('div').each(function(i) {
			jQuery(this).text(cE.columnLabelString(i+1));
			w += jQuery(this).width();
		});
		return w;
	},
	refreshLabelsRows: function(){
		jS.obj.barLeft().find('div').each(function(i) {
			jQuery(this).text((i + 1));
		});
	},
	addRowMulti: function(qty) {
		if (!qty) {
			qty = prompt('How many rows would you like to add?');
		}
		if (qty) {
			for (var i = 0; i <= qty; i++) {
				jS.addRow();
			}
		}
		jS.setTdIds();
	},
	addColumnMulti: function(qty) {
		if (!qty) {
			qty = prompt('How many columns would you like to add?');
		}
		if (qty) {
			for (var i = 0; i <= qty; i++) {
				jS.addColumn();
			}
		}
		jS.setTdIds();
	},
	addRow: function(atRow, insertBefore) {
		if (!atRow) {
			//if atRow has no value, lets just add it to the end.
			atRow = ':last';
		} else if (atRow == true) {//if atRow is boolean, then lets add it just after the currently selected row.
			atRow = ':eq(' + (jS.cellEditLastLoc[1] - 1) + ')';
		} else {
			//If atRow is a number, lets add it at that row
			atRow = ':eq(' + (atRow - 1) + ')';
		}
		
		jS.cellEditAbandon();
		var currentRow = jS.obj.sheet().find('tr' + atRow);
		var newRow = currentRow.clone();
		
		jQuery('td', newRow)
			.html('')
			.attr('class', '')
			.attr('formula', '')
			.css('background-color', '')
			.removeAttr('function')
			.unbind('click')
			.unbind('mousedown')
			.mousedown(jS.cellOnMouseDown)
			.click(jS.getCellClickFn());

		if (insertBefore) {
			newRow.insertBefore(currentRow);
		} else {
			newRow.insertAfter(currentRow);
		}
		
		var currentBar = jS.obj.barLeft().find('div' + atRow);
		var newBar = currentBar.clone();
		
		jS.themeRoller.newBar(newBar);
		
		jS.getResizeControl.height(
			newBar
				.html(parseInt(currentBar.text()) + 1)
				.removeClass(jS.cl.uiActive)
				.height(jS.attrH.height(newRow))
		);
		
		jS.log('New row at: ' + (parseInt(currentBar.text()) + 1));
		
		if (insertBefore) {
			newBar.insertBefore(currentBar);
		} else {
			newBar.insertAfter(currentBar);
		}
		
		if (atRow) {//If atRow equals anything it means that we inserted at a point, because of this we need to update the labels
			jS.obj.barLeft().find('div').each(function(i) {
				jQuery(this).text(i + 1);
			});
		}

		jS.setTdIds();
		jS.obj.pane().scroll();
	},
	addColumn: function(atColumn, insertBefore) {
		if (!atColumn) {
			//if atColumn has no value, lets just add it to the end.
			atColumn = ':last';
		} else if (atColumn == true) {
			//if atColumn is boolean, then lets add it just after the currently selected row.
			atColumn = ':eq(' + (jS.cellEditLastLoc[2] - 1) + ')';
		} else {
			//If atColumn is a number, lets add it at that row
			atColumn = ':eq(' + (atColumn - 1) + ')';
		}

		jS.cellEditAbandon();
		
		//there are 3 obj that need managed here div, col, and each tr's td
		//Lets get the current div & col, then later we go through each row
		var currentBar = jS.obj.barTop().find('div' + atColumn);
		var currentCol = jS.obj.sheet().find('col' + atColumn);
		
		//Lets create our new bar, cell, and col
		var newBar = currentBar.clone().width(jS.s.newColumnWidth - jS.attrH.boxModelCorrection());
		jS.getResizeControl.width(newBar);
		var newCol = currentCol.clone().width(jS.s.newColumnWidth);
		var newCell = jQuery('<td></td>');
		
		//This is just to get the new label
		var currentIndex = cE.columnLabelIndex(currentBar.text());
		var newLabel = cE.columnLabelString(currentIndex + 1);
		jS.log('New Column: ' + currentIndex + ', ' + newLabel);
		
		if (insertBefore) {
			currentCol.before(newCol);
			currentBar.before(newBar);
		} else {
			currentCol.after(newCol);
			currentBar.after(newBar);
		}
			
		//Add new spreadsheet column to top
		
		var j = 0;
		var addNewCellFn;
		if (insertBefore) {
			addNewCellFn = function(obj) {
				jQuery(obj).find('td' + atColumn).before(
					newCell.clone()
						.mousedown(jS.cellOnMouseDown)
						.click(jS.getCellClickFn())
				);
			}
		} else {
			addNewCellFn = function(obj) {
				jQuery(obj).find('td' + atColumn).after(
					newCell.clone()
						.mousedown(jS.cellOnMouseDown)
						.click(jS.getCellClickFn())
				);
			}
		}
		
		jS.obj.sheet().find('tr').each(function(i) {
			addNewCellFn(this);
			j++;
		});
		
		jS.log('Sheet length: ' + j);		
		
		if (atColumn) {//If atColumn equals anything it means that we inserted at a point, because of this we need to update the labels
			jS.obj.barTop().find('div').each(function(i) {
				jQuery(this).text(cE.columnLabelString(i + 1));
			});
		}
		
		jS.attrH.syncSheetWidthFromTds();
		
		jS.setTdIds();
		jS.obj.pane().scroll();
	},
	deleteRow: function() {
		if (jS.obj.cell()[0]) {
			var v = confirm("Are you sure that you want to delete that row? Fomulas will not be updated.");
			if (v) {
				var loc = jS.cellEditLastLoc[1] - 1;
				jS.obj.barLeft().find('div').eq(loc).remove();
				jS.obj.sheet().find('tr').eq(loc).remove();
				jS.obj.formula().val('');
				jS.setTdIds();
				jS.refreshLabelsRows();
			}
		}
		jS.obj.pane().scroll();
	},
	deleteColumn: function() {
		if (jS.obj.cell()[0]) {
			var v = confirm("Are you sure that you want to delete that column? Fomulas will not be updated.");
			if (v) {
				var loc = jS.cellEditLastLoc[2] - 1;

				jS.obj.barTop().find('div').eq(loc).remove();
				jS.obj.sheet().find('colgroup col').eq(loc).remove();
				jS.obj.sheet().find('tr').each(function(i) {
						jQuery(this).find('td').eq(loc).remove();
						//jQuery(jS.getTd(null, loc[0] + 1, loc[1] + 1)).remove();
				});
				
				jS.obj.formula().val('');
				
				var w = jS.refreshLabelsColumns();
				jS.setTdIds();
				jS.obj.sheet().width(w);
			}
		}
		jS.obj.pane().scroll();
	},
	sheetTitle: function(startup, newTitle) {
		var sheetTitle = '';
		if (startup) {
			var sheetTitles = new Array();
			sheetTitles[3] = jS.s.title;
			sheetTitles[2] = jS.obj.sheet().attr('sheettitle');
			sheetTitles[1] = jS.obj.sheet().find('td:contains("sheettitle")').text().split(':')[1];//sheetTitle:SheetTitle
			sheetTitles[0] = 'Untitled Spreadsheet';
			var i = sheetTitles.length;
			while (!sheetTitle) {
				sheetTitle = sheetTitles[i];
				i--;
			}
			return sheetTitle;
		} else {
			if (!newTitle) {
				newTitle = prompt("What would you like the sheet's title to be?", jS.obj.title().text());
			}
			switch (newTitle) {
				case '': newTitle = 'Untitled Spreadsheet'; break;
				case null: newTitle = jS.sheetTitle(true);
			}
			jS.obj.sheet().attr('sheettitle', newTitle);
			jS.obj.title().html(newTitle);
		}
	},
	viewSource: function(pretty) {
		var sheetClone = jS.obj.sheet().clone()[0];

		jS.sheetDecorateRemove(sheetClone);

		if (pretty) {
			var s = jS.HTMLtoPrettySource(sheetClone);
		} else {
			var s = jS.HTMLtoCompactSource(sheetClone);
		}
		var w = window.open();
		w.document.write("<html><body><xmp>" + s + "\n</xmp></body></html>");
		w.document.close();
		return false;
	},
	saveSheet: function() {
		var v = jS.obj.sheet().clone()[0];
		jS.sheetDecorateRemove(v);
		var s = jQuery('<div />').html(v).html();

		jQuery.ajax({
			url: jS.s.urlSave,
			type: jS.s.ajaxSaveType,
			data: 's=' + s,
			dataType: 'html',
			success: function(data) {
				alert('Success! - ' + data);
			}
		});
	},
	HTMLtoCompactSource: function(node) {
		var result = "";
		if (node.nodeType == 1) {
			// ELEMENT_NODE
			result += "<" + node.tagName;
			hasClass = false;
			
			var n = node.attributes.length;
			for (var i = 0, hasClass = false; i < n; i++) {
				var key = node.attributes[i].name;
				var val = node.getAttribute(key);
				if (val) {
					if (key == "contentEditable" && val == "inherit") {
						continue;
						// IE hack.
					}
					if (key == "class") {
						hasClass = true;
						jQuery(val).removeClass(jS.cl.cell);
					}
					
					if (typeof(val) == "string") {
						result += " " + key + '="' + val.replace(/"/g, "'") + '"';
					} else if (key == "style" && val.cssText) {
						result += ' style="' + val.cssText + '"';
					}
				}
			}

			if (node.tagName == "TABLE" && !hasClass) {
				// IE hack, where class doesn't appear in attributes.
				result += ' class="jSheet"';
			}
			if (node.tagName == "COL") {
				// IE hack, which doesn't like <COL..></COL>.
				result += '/>';
			} else {
				result += ">";
				var childResult = "";
				jQuery(node.childNodes).each(function() {
					childResult += jS.HTMLtoCompactSource(this);
				});
				result += childResult;
				result += "</" + node.tagName + ">";
			}

		} else if (node.nodeType == 3) {
			// TEXT_NODE
			result += node.data.replace(/^\s*(.*)\s*$/g, "$1");
		}
		return result;
	},
	HTMLtoPrettySource: function(node, prefix) {
		if (!prefix) {
			prefix = "";
		}
		var result = "";
		if (node.nodeType == 1) {
			// ELEMENT_NODE
			result += "\n" + prefix + "<" + node.tagName;
			var n = node.attributes.length;
			for (var i = 0; i < n; i++) {
				var key = node.attributes[i].name;
				var val = node.getAttribute(key);
				if (val) {
					if (key == "contentEditable" && val == "inherit") {
						continue; // IE hack.
					}
					if (typeof(val) == "string") {
						result += " " + key + '="' + val.replace(/"/g, "'") + '"';
					} else if (key == "style" && val.cssText) {
						result += ' style="' + val.cssText + '"';
					}
				}
			}
			if (node.childNodes.length <= 0) {
				result += "/>";
			} else {
				result += ">";
				var childResult = "";
				var n = node.childNodes.length;
				for (var i = 0; i < n; i++) {
					childResult += jS.HTMLtoPrettySource(node.childNodes[i], prefix + "  ");
				}
				result += childResult;
				if (childResult.indexOf('\n') >= 0) {
					result += "\n" + prefix;
				}
				result += "</" + node.tagName + ">";
			}
		} else if (node.nodeType == 3) {
			// TEXT_NODE
			result += node.data.replace(/^\s*(.*)\s*$/g, "$1");
		}
		return result;
	},
	barAdjustorCache: new Array(),
	barAdjustor: function(killTimer) {
		if (!killTimer) {
			if (!this.barAdjustorCache.length) {
				this.barAdjustorCache[0] = jS.obj.pane();
				this.barAdjustorCache[1] = jS.obj.barTop();
				this.barAdjustorCache[2] = jS.obj.barLeft();
			} else {
				this.barAdjustorCache[1].stop().animate({
					left: '-' + this.barAdjustorCache[0].scrollLeft()},
					50);
				this.barAdjustorCache[2].stop().animate({
					top: '-' + this.barAdjustorCache[0].scrollTop()},
					50);
			}
			this.barAdjustorCache[3] = window.setTimeout('jS.barAdjustor()', 500);
		} else { //reset barAdjustorCache to it's original state
			window.clearTimeout(this.barAdjustorCache[3]);
			this.barAdjustorCache = new Array();
		}
	},
	followMe: function() {
		if (jS.s.urlScrollTo) {
			jS.obj.pane().stop().scrollTo(jS.obj.cell(), {
				margin: true,
				axis: 'xy',
				duration: 250,
				offset: {
					top: -jS.s.height / 3,
					left: -jS.s.width / 5
				}
			});
		}
	},
	count: {
		rows: function() {
			return parseInt(jQuery.trim(jS.obj.barLeft().find('div:last').text()));
		},
		columns: function() {
			return parseInt(jS.columnLabelIndex(jQuery.trim(jS.obj.barTop().find('div:last').text())));
		}
	},
	openSheet: function(url, size, noNotify) {
		function initSheet(obj) {
			if (obj) {
				jS.obj.pane().html(jQuery(obj).show());
			} else {
				obj = jS.obj.pane().find('table:first').show();
			}

			obj = jS.tuneTableForSheetUse(obj, true);
			
			jS.sheetDecorate();
			
			jS.makeBarItemTop(url);
			jS.makeBarItemLeft(url);
		
			jS.sheetTitle(true);
			
			if (jS.s.editable) {
				jS.obj.pane().find('td')
					.mousedown(jS.cellOnMouseDown)
					.click(jS.getCellClickFn());
			}
			
			jS.themeRoller.start();

			jS.setTdIds();
			
			//We load the plugins
			if (jS.s.urlScrollsync) {
				jQuery.getScript(jS.s.urlScrollsync, function() {
					jS.obj.pane().add(jS.obj.barLeftParent()).scrollsync({axis: 'y'});
					jS.obj.pane().add(jS.obj.barTopParent()).scrollsync({axis: 'x'});
				});
			} else {
				jS.barAdjustor();
			}
			
			var calc = function() {}
			if (!jS.s.calcOff) {
					calc = function() {
						jS.calc(jS.obj.sheet());
					};
				}
			if (jS.s.urlJGCharts) { //When loading the charts, we need to make sure that the namespace exists before we fire fnAfter();
				jQuery.getScript(jS.s.urlJGCharts, function() {
					calc();
				});
			} else {
				calc();
			}
		}
		if (noNotify ? true : confirm("Are you sure you want to open a different sheet?  All unsaved changes will be lost.")) {
			jS.cellEditAbandon();
			if (!size) {
				if (url) {
					jQuery('<div />').load(url, function() {
						initSheet(jQuery(this).find('table:first'));
					});
				} else {
					initSheet();
				}
			} else if (size.toLowerCase().match('x')) {
				jS.s.title = '';
				initSheet(jS.buildSheet(size));
			} else {
				initSheet();
			}
		}
	},
	newSheet: function() {
		jS.openSheet('', prompt("What size would you like to make your spreadsheet? Example: '5x10' creates a sheet that is 5 columns by 10 rows."));
	},
	importRow: function(rowArray) {
		jS.addRow();

		var error = "";
		jS.obj.sheet().find('tr:last td').each(function(i) {
			jQuery(this).removeAttr('formula');
			try {
				//To test this, we need to first make sure it's a string, so converting is done by adding an empty character.
				if ((rowArray[i] + '').charAt(0) == "=") {
					jQuery(this).attr('formula', rowArray[i]);					
				} else {
					jQuery(this).html(rowArray[i]);
				}
			} catch(e) {
				//We want to make sure that is something bad happens, we let the user know
				error += e + ';\n';
			}
		});
		
		if (error) {//Show them the errors
			alert(error);
		}
		//Let's recalculate the sheet just in case
		jS.setTdIds();
		jS.calc(jS.obj.sheet());
	},
	importColumn: function(columnArray) {
		jS.addColumn();

		var error = "";
		jS.obj.sheet().find('tr').each(function(i) {
			var o = jQuery(this).find('td:last');
			try {
				//To test this, we need to first make sure it's a string, so converting is done by adding an empty character.
				if ((columnArray[i] + '').charAt(0) == "=") {
					o.attr('formula', columnArray[i]);					
				} else {
					o.html(columnArray[i]);
				}
			} catch(e) {
				//We want to make sure that is something bad happens, we let the user know
				error += e + ';\n';
			}
		});
		
		if (error) {//Show them the errors
			alert(error);
		}
		//Let's recalculate the sheet just in case
		jS.setTdIds();
		jS.calc(jS.obj.sheet());
	},
	buildSheet: function(size) {
		var sheetSize;
		if (!size) {
			sheetSize = jS.s.buildSheet.toLowerCase().split('x');
		} else {
			sheetSize = size.toLowerCase().split('x');
		}
		var columnsCount = parseInt(sheetSize[0]);
		var rowsCount = parseInt(sheetSize[1]);
		var columnWidth = jS.s.newColumnWidth;
		
		//Create elements before loop to make it faster.
		var newSheet = jQuery('<table border="1px" class="' + jS.cl.sheet + '" id="' + jS.id.sheet + '"><colgroup /><tbody><tr /></tbody></table>');
		var standardCol = '<col width="' + columnWidth + '" style="width: ' + columnWidth + '" />';
		var standardTd = '<td> </td>';
		
		//Using -- is many times faster than ++
		for (var i = columnsCount; i >= 1; i--) {
			newSheet.find('colgroup').prepend(standardCol);
			newSheet.find('tbody tr')
				.prepend(standardTd)
				.css('height', jS.s.colMargin);
		}
		
		
		var row = jQuery('<div />').html(newSheet.find('tbody tr')).html();
		for (var i = rowsCount; i >= 1; i--) {
			newSheet.find('tbody').prepend(row);
		}
		
		jS.attrH.syncSheetWidthFromTds(newSheet);
		
		return newSheet;
	},
	sheetSyncSizeToDivs: function() {
		var newSheetWidth = 0;
		jS.obj.barTop().find('div').each(function() {
			newSheetWidth += parseInt(jQuery(this).outerWidth());
		});
		jS.obj.sheet().width(newSheetWidth);
	},
	sheetSyncSizeToCols: function() {
		var newSheetWidth = 0;
		jS.obj.sheet().find('colgroup col').each(function() {
			newSheetWidth += jQuery(this).width();
		});
		jS.obj.sheet().width(newSheetWidth);
	},
	sheetSyncSize: function() {
		var h = jS.s.height;
		if (!h) {
			h = 400; //Height really needs to be set by the parent
		}
		var w = jS.s.width;

		jS.obj.pane()
			.height(
				(h - jS.attrH.height(jS.obj.controls())) -
				(jS.s.colMargin + 6)
			)
			.width(w);
			
		jS.obj.barLeftParent()
			.height(jS.obj.pane().height());
		
		jS.obj.barTopParent()
			.height(jS.s.colMargin)
			.width(jS.obj.pane().width());
		
		jS.themeRoller.start();
	},
	columnResizer: {
			xyDimension: 0,
			getIndex: function(td) {
				return cE.columnLabelIndex(jQuery.trim(td.text())) - 1;
			},
			getSize: function(obj) {
				return jS.attrH.width(obj, true);
			},
			setSize: function(obj, v) {
				obj.width(v);
			},
			setDesinationSize: function(w) {
				jS.sheetSyncSizeToDivs();
				
				jS.obj.sheet().find('col').eq(this.i)
					.width(w)
					.css('width', w)
					.attr('width', w);
				
				jS.obj.pane().scroll();
			}
		},
	rowResizer: {
		xyDimension: 1,
			getIndex: function(obj) {
				return parseInt(jQuery.trim(obj.text())) - 1;
			},
			getSize: function(obj) {
				return jS.attrH.height(obj, true);
			},
			setSize: function(obj, v) {
				if (v) {
				obj
					.height(v)
					.css('height', v)
					.attr('height', v);
				}
				return jS.attrH.height(obj);
			},
			setDesinationSize: function() {
				//Set the cell height
				jS.attrH.setHeight(this.i, 'bar', true);
				
				//Reset the bar height if the resized row don't match
				jS.attrH.setHeight(this.i, 'cell', false);
				
				jS.obj.pane().scroll();
			}
	},
	barResizer: function(evt, target, type) {
		//Resize Column & Row & Prototype functions are private under class jSheet		
		
		var o = (type == 'row' ? jS.rowResizer : jS.columnResizer);

		var barResizer = {
			start: function(evt) {
				//Finish up last editted cell.
				//jS.cellEditDone();
				//jS.cellEditAbandon();
				
				jS.log('start resize');
				//I never had any problems with the numbers not being ints but I used the parse method
				//to ensuev non-breakage
				o.offset = target.offset();
				o.tdPageXY = [o.offset.left, o.offset.top][o.xyDimension];
				o.startXY = [evt.pageX, evt.pageY][o.xyDimension];
				o.i = o.getIndex(target);
				o.srcBarSize = o.getSize(target);
				o.edgeDelta = o.startXY - (o.tdPageXY + o.srcBarSize);
				o.min = 15;
				
				if (jS.s.joinedResizing) {
					o.resizeFn = function(size) {
						o.setDesinationSize(size);
						o.setSize(target, size);
					}
				} else {
					o.resizeFn = function(size) {
						o.setSize(target, size);
					}
				}
				
				//We start the drag sequence
				if (Math.abs(o.edgeDelta) <= o.min) {
					jQuery(document)
						.mousemove(barResizer.drag)
						.mouseup(barResizer.stop);
				}
			},
			drag: function(evt) {
				var newSize = o.min;

				var v = o.srcBarSize + ([evt.pageX, evt.pageY][o.xyDimension] - o.startXY);
				if (v > 0) {// A non-zero minimum size saves many headaches.
					newSize = Math.max(v, o.min);
				}

				o.resizeFn(newSize)
				return false;
			},
			stop: function(evt) {	
				o.setDesinationSize(o.getSize(target));
				
				jQuery(document)
					.unbind('mousemove')
					.unbind('mouseup');
				//Remove themeRoller selection
				//jS.themeRoller.clearBar();
				//jS.barAdjustor();
				jS.obj.formula()
					.focus()
					.select();
				
				jS.log('stop resizing');
			}
		}
		barResizer.start(evt);
	},
	cellFind: function(v) {
		if(!v) {
			v = prompt("What are you looking for in this spreadsheet?");
		}
		if (v) {//We just do a simple uppercase/lowercase search.
			var obj = jS.obj.sheet().find('td:contains("' + v + '")');
			
			if (obj.length < 1) {
				obj = jS.obj.sheet().find('td:contains("' + v.toLowerCase() + '")');
			}
			
			if (obj.length < 1) {
				obj = jS.obj.sheet().find('td:contains("' + v.toUpperCase() + '")');
			}
			
			obj = obj.eq(0);
			if (obj.length > 0) {
				obj.click();
			} else {
				alert('No results found.');
			}
		}
	},
	cellSetActiveMulti: function(evt) {
		var o = {
			startRow: evt.target.parentNode.rowIndex,
			startColumn: evt.target.cellIndex
		};//These are the events used to selected multiple rows.
		jS.obj.sheet()
			.mousemove(function(evt) {
				o.endRow = evt.target.parentNode.rowIndex;
				o.endColumn = evt.target.cellIndex;
				for (var i = o.startRow; i <= o.endRow; i++) {
					for (var j = o.startColumn; j <= o.endColumn; j++) {
						var td = jS.getTd(jS.obj.tableBody(), i + 1, j + 1);
						jQuery(td)
							.addClass(jS.cl.uiCell)
							.addClass(jS.cl.uiCellHighlighted);
					}
				}
			})
			.mouseup(function() {
				jS.obj.sheet()
					.unbind('mousemove')
					.unbind('mouseup');
			});
		return false;
	},
	cellSetActiveMultiColumn: function(i) {
		jS.obj.sheet().find('tr').each(function() {
			var o = jQuery(this).find('td').eq(i - 1);
			o
				.addClass(jS.cl.uiCell)
				.addClass(jS.cl.uiCellHighlighted);
		});
		jS.themeRoller.barTop(i);
	},
	cellSetActiveMultiRow: function(i) {
		jS.obj.sheet().find('tr').eq(i - 1).find('td')
			.addClass(jS.cl.uiCell)
			.addClass(jS.cl.uiCellHighlighted);
		jS.themeRoller.barLeft(i);
	},
	sheetClearActive: function() {
		jS.obj.formula().val('');
		jS.obj.cell().removeClass(jS.cl.cell);
		jS.obj.barSelected().removeClass(jS.cl.barSelected);
	},
	getIndexTr: function(row) {
		// The row is 1-based.
		return row - 1;
		// A indexTr is 0-based.
	},
	getIndexTd: function(col) {
		return col - 1;
		// A indexTd is 0-based.
	},
	getTdRange: function() {
		//three steps here,
		//Get td's
		//Get locations
		//Get labels for locationa and return them
		
		var cells = jS.obj.uiCell().not('.' + jS.cl.cell);
		var firstCellLoc = jS.getTdLocation(cells.eq(0));
		var firstLabel = cE.columnLabelString(firstCellLoc[1]) + firstCellLoc[0];
		var lastCellLoc = jS.getTdLocation(cells.eq(cells.length - 1));
		var lastLabel = cE.columnLabelString(lastCellLoc[1]) + lastCellLoc[0];
		return firstLabel + ":" + lastLabel
	},
	getTdId: function(row, col) {
		return 'cell_c' + col + '_r' + row;
	},
	getTd: function(tableBody, row, col, indexTr, indexTd) {
		// The row and col are 1-based.
		/*if (!indexTr) {
			// The indexTr and indexTd are 0-based.
			indexTr = jS.getIndexTr(row);
		}
		if (tableBody.rows) {
			var tr = tableBody.rows[indexTr];
			if (tr) {
				if (!indexTd) {
					indexTd = jS.getIndexTd(col);
				}
				return tr.cells[indexTd];
			}
		}*/
		var cell =  document.getElementById(jS.getTdId(row, col));
		return cell;
	},
	getTdLocation: function(td) {
		var col = td[0].cellIndex + 1;
		var row = td[0].parentNode.rowIndex + 1;
		return [row--, col--];
		// The row and col are 1-based.
	},
	tableCellProvider: function(tableBodyId) {
		this.tableBodyId = tableBodyId;
		this.cells = {};
	},
	tableCell: function(tableBody, row, col) {
		this.tableBodyId = tableBody.id;
		this.row = row;
		this.col = col;
		this.indexTr = jS.getIndexTr(row);
		this.indexTd = jS.getIndexTd(col);
		this.value = jS.EMPTY_VALUE;
		
		//this.prototype = new cE.cell();
	},
	EMPTY_VALUE: {},
	log: function(msg) {
		switch (jS.s.log) {
			case true:
				var d = new Date();
				jS.obj.log().prepend(d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds() + ':' + d.getMilliseconds() + ';' + msg + '<br />\n');
				break;
		}
	},
	getChart: function(type, data, legend, axisLabels, w, h) {
		var api = new jGCharts.Api();
		function refine(v) {
			var refinedV = new Array();
			jQuery(v).each(function(i) {
				refinedV[i] = jS.manageHtmlToText(v[i] + '');
			});
			return refinedV;
		}
		var o = {};
		
		if (type) {
			o.type = type;
		}
		
		if (data) {
			o.data = data;
		}
		
		if (legend) {
			o.legend = refine(legend);
		}
		
		if (axisLabels) {
			o.axis_labels = refine(axisLabels);
		}
		
		if (w || h) {
			o.size = w + 'x' + h;
		}
		
		return jQuery('<img>').attr('src', api.make(o));
	}
}

jS.tableCellProvider.prototype = {
	getCell: function(row, col) {
		if (typeof(col) == "string") {
			col = cE.columnLabelIndex(col);
		}
		var key = row + "," + col;
		var cell = this.cells[key];
		if (!cell) {
			var tableBody = jS.obj.tableBody();
			if (tableBody) {
				var td = jS.getTd(tableBody, row, col);
				if (td) {
					cell = this.cells[key] = new jS.tableCell(tableBody, row, col);
				}
			}
		}
		return cell;
	},
	getNumberOfColumns: function(row) {
		var tableBody = jS.obj.tableBody();
		if (tableBody) {
			var tr = tableBody.rows[jS.getIndexTr(row)];
			if (tr) {
				return tr.cells.length;
			}
		}
		return 0;
	},
	toString: function() {
		result = "";
		jS.obj.sheet().find('tr').each(function() {
			result += this.innerHTML.replace(/\n/g, "") + "\n";
		});
		return result;
	}
};

jS.tableCell.prototype = {
	getTd: function() {
		return jS.getTd(jS.obj.tableBody(), this.row, this.col, this.indexTr, this.indexTd);
	},
	setValue: function(v, e) {
		this.error = e;
		this.value = v;
		this.getTd().innerHTML = (v ? v: "");

	},
	getValue: function() {
		var v = this.value;
		if (v === jS.EMPTY_VALUE && !this.getFormula()) {
			v = this.getTd().innerHTML;
			v = this.value = (v.length > 0 ? cE.parseFormulaStatic(v) : null);

		}
		return (v === jS.EMPTY_VALUE ? null: v);
	},
	getFormat: function() {
		return jQuery(this.getTd()).attr("format");
	},
	setFormat: function(v) {
		jQuery(this.getTd()).attr("format", v);
	},
	getFormulaFunc: function() {
		return this.formulaFunc;
	},
	setFormulaFunc: function(v) {
		this.formulaFunc = v;
	},
	getFormula: function() {
		return jQuery(this.getTd()).attr('formula');
	},
	setFormula: function(v) {
		if (v && v.length > 0) {
			jQuery(this.getTd()).attr('formula', v);
		} else {
			jQuery(this.getTd()).removeAttr('formula');
		}
	}
};

var key = {
	BACKSPACE: 8,
	CAPS_LOCK: 20,
	COMMA: 188,
	CONTROL: 17,
	DELETE: 46,
	DOWN: 40,
	END: 35,
	ENTER: 13,
	ESCAPE: 27,
	HOME: 36,
	INSERT: 45,
	LEFT: 37,
	NUMPAD_ADD: 107,
	NUMPAD_DECIMAL: 110,
	NUMPAD_DIVIDE: 111,
	NUMPAD_ENTER: 108,
	NUMPAD_MULTIPLY: 106,
	NUMPAD_SUBTRACT: 109,
	PAGE_DOWN: 34,
	PAGE_UP: 33,
	PERIOD: 190,
	RIGHT: 39,
	SHIFT: 16,
	SPACE: 32,
	TAB: 9,
	UP: 38
};

var cE = jQuery.calculationEngine = {
	TEST: {},
	ERROR: "#VALUE!",
	cFN: {//cFN = compiler functions, usually mathmatical
		SUM: 	function(x, y) { return x + y; },
		MAX: 	function(x, y) { return x > y ? x: y; },
		MIN: 	function(x, y) { return x < y ? x: y; },
		COUNT: 	function(x, y) { return (y != null) ? x + 1: x; },
		CLEAN: function(v) {
			if (typeof(v) == 'string') {
				v = v.replace(cE.RE_AMP, '&')
						.replace(cE.RE_NBSP, ' ')
						.replace(/\n/g,'')
						.replace(/\r/g,'');
			}
			return v;
		}
	},
	fn: {//fn = standard functions used in cells
		HTML: function(v) {
			return jQuery(v);
		},
		IMG: function(v) {
			return jQuery('<img src="' + v + '" style="border: ;"/>');
		},
		AVERAGE:	function(values) { 
			var arr = cE.foldPrepare(values, arguments);
			return cE.fn.SUM(arr) / cE.fn.COUNT(arr); 
		},
		AVG: 		function(values) { 
			return cE.fn.AVERAGE(values);
		},
		COUNT: 		function(values) { return cE.fold(cE.foldPrepare(values, arguments), cE.cFN.COUNT, 0); },
		SUM: 		function(values) { return cE.fold(cE.foldPrepare(values, arguments), cE.cFN.SUM, 0, true); },
		MAX: 		function(values) { return cE.fold(cE.foldPrepare(values, arguments), cE.cFN.MAX, Number.MIN_VALUE, true); },
		MIN: 		function(values) { return cE.fold(cE.foldPrepare(values, arguments), cE.cFN.MIN, Number.MAX_VALUE, true); },
		ABS	: 		function(v) { return Math.abs(cE.fn.N(v)); },
		CEILING: 	function(v) { return Math.ceil(cE.fn.N(v)); },
		FLOOR: 		function(v) { return Math.floor(cE.fn.N(v)); },
		INT: 		function(v) { return Math.floor(cE.fn.N(v)); },
		ROUND: 		function(v) { return Math.round(cE.fn.N(v)); },
		RAND: 		function(v) { return Math.random(); },
		RND: 		function(v) { return Math.random(); },
		TRUE: 		function() { return true; },
		FALSE: 		function() { return false; },
		NOW: 		function() { return new Date ( ); },
		TODAY: 		function() { return Date( Math.floor( new Date ( ) ) ); },
		DAYSFROM: 	function(year, month, day) { 
			return Math.floor( (new Date() - new Date (year, (month - 1), day)) / 86400000);
		},
		IF:			function(v, t, f){
			t = cE.cFN.CLEAN(t);
			f = cE.cFN.CLEAN(f);
			
			try { v = eval(v); } catch(e) {};
			try { t = eval(t); } catch(e) {};
			try { t = eval(t); } catch(e) {};

			if (v == 'true' || v == true || v > 0 || v == 'TRUE') {
				return t;
			} else {
				return f;
			}
		},
		FIXED: 		function(v, decimals, noCommas) { 
			if (decimals == null) {
				decimals = 2;
			}
			var x = Math.pow(10, decimals);
			var s = String(Math.round(cE.fn.N(v) * x) / x); 
			var p = s.indexOf('.');
			if (p < 0) {
				p = s.length;
				s += '.';
			}
			for (var i = s.length - p - 1; i < decimals; i++) {
				s += '0';
			}
			if (noCommas == true) {// Treats null as false.
				return s;
			}
			var arr	= s.replace('-', '').split('.');
			var result = [];
			var first  = true;
			while (arr[0].length > 0) { // LHS of decimal point.
				if (!first) {
					result.unshift(',');
				}
				result.unshift(arr[0].slice(-3));
				arr[0] = arr[0].slice(0, -3);
				first = false;
			}
			if (decimals > 0) {
				result.push('.');
				var first = true;
				while (arr[1].length > 0) { // RHS of decimal point.
					if (!first) {
						result.push(',');
					}
					result.push(arr[1].slice(0, 3));
					arr[1] = arr[1].slice(3);
					first = false;
				}
			}
			if (v < 0) {
				return '-' + result.join('');
			}
			return result.join('');
		},
		TRIM:		function(v) { 
			if (typeof(v) == 'string') {
				v = jQuery.trim(v);
			}
			return v;
		},
		HYPERLINK: function(link, name) {
			return jQuery('<a href="' + link + '" target="_new">' + name + '</a>');
		},
		DOLLAR: 	function(v, decimals, symbol) { 
			if (decimals == null) {
				decimals = 2;
			}
			if (symbol == null) {
				symbol = '$';
			}
			var r = cE.fn.FIXED(v, decimals, false);
			if (v >= 0) {
				return symbol + r; 
			}
			return '-' + symbol + r.slice(1);
		},
		VALUE: 		function(v) { return parseFloat(v); },
		N: 			function(v) { if (v == null) {return 0;}
						  if (v instanceof Date) {return v.getTime();}
						  if (typeof(v) == 'object') {v = v.toString();}
						  if (typeof(v) == 'string') {v = parseFloat(v.replace(cE.RE_N, ''));}
						  if (isNaN(v))		   {return 0;}
						  if (typeof(v) == 'number') {return v;}
						  if (v == true)			 {return 1;}
						  return 0; },
		PI: 		function() { return Math.PI; },
		POWER: 		function(x, y) {
			return Math.pow(x, y);
		},
		LIST:		function(v, noBlank) {
			var cell = jQuery(jS.getTd(null, cE.calcState.row, cE.calcState.col - 1));	
			var cellValues = cE.foldPrepare(v, arguments);
			var cellSelectedValue = cell.find('select').val();
			var selectObj = jQuery('<select style="width: 100%;">' + (!noBlank ? '<option value="empty">Select a value</option>' : '') + '</select>');
			var selected = '';
			for (var i = 0; i < (cellValues.length <= 25 ? cellValues.length : 25); i++) {
				var v = '';
				if (selected != 'SELECTED' && cellValues[i] == cellSelectedValue) {
					v = selected = 'SELECTED';
				}
				selectObj.append('<option ' + v + ' value="' + cellValues[i] + '">' + cellValues[i] + '</option>');
			}
			
			return selectObj;
		},
		LISTVAL:	function(v) {
			return jQuery(v).val();
		},
		CHART: {
			BAR:	function(v, legend, axisLabels, w, h) {
				return this.CUSTOM(null, cE.foldPrepare(v, arguments), legend, axisLabels, w, h);
			},
			BARH:	function(v, legend, axisLabels, w, h) {
				return this.CUSTOM('bhg', cE.foldPrepare(v, arguments), legend, axisLabels, w, h);
			},
			SBAR:	function(v, legend, axisLabels, w, h) {
				return this.CUSTOM('bvs', cE.foldPrepare(v, arguments), legend, axisLabels, w, h);
			},
			SBARH:	function(v, legend, axisLabels, w, h) {
				return this.CUSTOM('bhs', cE.foldPrepare(v, arguments), legend, axisLabels, w, h);
			},
			LINE:	function(v, legend, axisLabels, w, h) {
				return this.CUSTOM('lc', cE.foldPrepare(v, arguments), legend, axisLabels, w, h);
			},
			PIE:	function(v, legend, axisLabels, w, h) {
				return this.CUSTOM('p', cE.foldPrepare(v, arguments), legend, axisLabels, w, h);
			},
			CUSTOM:	function(type, v, legend, axisLabels, w, h) {
				return jS.getChart(type, cE.foldPrepare(v, arguments), legend, axisLabels,  w, h);
			}
		}
	},
	calcState: {},
	calc: function(cellProvider, context, startFuel) {
		// Returns null if all done with a complete calc() run.
		// Else, returns a non-null continuation function if we ran out of fuel.  
		// The continuation function can then be later invoked with more fuel value.
		// The fuelStart is either null (which forces a complete calc() to the finish) 
		// or is an integer > 0 to slice up long calc() runs.  A fuelStart number
		// is roughly matches the number of cells to visit per calc() run.
		cE.calcState = { 
			cellProvider:	cellProvider, 
			context: 		(context != null ? context: {}),
			row: 			1, 
			col: 			1, 
			done:			false,
			stack:			[],
			calcMore: 		function(moreFuel) {
								cE.calcState.fuel = moreFuel;
								return cE.calcLoop(cE.calcState);
							}
		};
		return cE.calcState.calcMore(startFuel);
	},
	cell: function() {
		prototype: {// Cells don't know their coordinates, to make shifting easier.
			getError = function()	 { return this.error; },
			getValue = function()	 { return this.value; },
			setValue = function(v, e) { this.value = v; this.error = e; },
			getFormula	 = function()  { return this.formula; },	 // Like "=1+2+3" or "'hello" or "1234.5"
			setFormula	 = function(v) { this.formula = v; },
			getFormulaFunc = function()  { return this.formulaFunc; },
			setFormulaFunc = function(v) { this.formulaFunc = v; },
			toString = function() { return "Cell:[" + this.getFormula() + ": " + this.getValue() + ": " + this.getError() + "]"; }
		}
	}, // Prototype setup is later.
	columnLabelIndex: function(str) {
		// Converts A to 1, B to 2, Z to 26, AA to 27.
		var num = 0;
		for (var i = 0; i < str.length; i++) {
			var digit = str.charCodeAt(i) - 65 + 1;	   // 65 == 'A'.
			num = (num * 26) + digit;
		}
		return num;
	},
	parseLocation: function(locStr) { // With input of "A1", "B4", "F20",
		if (locStr != null &&								  // will return [1,1], [4,2], [20,6].
			locStr.length > 0 &&
			locStr != "&nbsp;") {
			for (var firstNum = 0; firstNum < locStr.length; firstNum++) {
				if (locStr.charCodeAt(firstNum) <= 57) {// 57 == '9'
					break;
				}
			}
			return [ parseInt(locStr.substring(firstNum)),
					 cE.columnLabelIndex(locStr.substring(0, firstNum)) ];
		}
		return null;
	},
	columnLabelString: function(index) {
		// The index is 1 based.  Convert 1 to A, 2 to B, 25 to Y, 26 to Z, 27 to AA, 28 to AB.
		// TODO: Got a bug when index > 676.  675==YZ.  676==YZ.  677== AAA, which skips ZA series.
		//	   In the spirit of billg, who needs more than 676 columns anyways?
		var b = (index - 1).toString(26).toUpperCase();   // Radix is 26.
		var c = [];
		for (var i = 0; i < b.length; i++) {
			var x = b.charCodeAt(i);
			if (i <= 0 && b.length > 1) {				   // Leftmost digit is special, where 1 is A.
				x = x - 1;
			}
			if (x <= 57) {								  // x <= '9'.
				c.push(String.fromCharCode(x - 48 + 65)); // x - '0' + 'A'.
			} else {
				c.push(String.fromCharCode(x + 10));
			}
		}
		return c.join("");
	},
	RE_N: /[\$,\s]/g,
	RE_REF_CELL: /\$?([a-zA-Z]+)\$?([0-9]+)/g,
	RE_REF_RANGE: /\$?([a-zA-Z]+)\$?([0-9]+):\$?([a-zA-Z]+)\$?([0-9]+)/g,
	parseFormula: function(formula, dependencies, calcState) { // Parse formula (without "=" prefix) like "123+SUM(A1:A6)/D5" into JavaScript expression string.
		var nrows = null;
		var ncols = null;
		if (calcState != null &&
			calcState.cellProvider != null) {
			nrows = calcState.cellProvider.nrows;
			ncols = calcState.cellProvider.ncols;
		}
		var arrayReferencesFixed = formula.replace(cE.RE_REF_RANGE, 
			function(ignored, startColStr, startRowStr, endColStr, endRowStr) {
				var res = [];
				var startCol = cE.columnLabelIndex(startColStr.toUpperCase());
				var startRow = parseInt(startRowStr);
				var endCol   = cE.columnLabelIndex(endColStr.toUpperCase());
				var endRow   = parseInt(endRowStr);
				if (ncols != null) {
					endCol = Math.min(endCol, ncols);
				}
				if (nrows != null) {
					endRow = Math.min(endRow, nrows);
				}
				for (var r = startRow; r <= endRow; r++) {
					for (var c = startCol; c <= endCol; c++) {
						res.push(cE.columnLabelString(c) + r);
					}
				}
				return "[" + res.join(",") + "]";
			}
		);
		var result = arrayReferencesFixed.replace(cE.RE_REF_CELL, 
			function(ignored, colStr, rowStr) {
				colStr = colStr.toUpperCase();
				if (dependencies != null) {
					dependencies[colStr + rowStr] = [parseInt(rowStr), cE.columnLabelIndex(colStr)]; 
				}
				return "(getCell((" + rowStr + "),\'" + colStr + "\').getValue())";
			}
		);
		return result;
	},	
	parseFormulaStatic: function(formula) { // Parse static formula value like "123.0" or "hello" or "'hello world" into JavaScript value.
		if (formula == null) {
			return null;
		}
		var formulaNum = formula.replace(cE.RE_N, '');
		var value = parseFloat(formulaNum);
		if (isNaN(value)) {
			value = parseInt(formulaNum);
		}
		if (isNaN(value)) {
			value = (formula.charAt(0) == "\'" ? formula.substring(1): formula);
		}
		return value;
	},
	calcLoop: function(calcState) {
		with (calcState) {
			if (done == true) {
				return null;
			}
			while (fuel == null || fuel > 0) {
				if (stack.length > 0) {
					var workFunc = stack.pop();
					if (workFunc != null) {
						workFunc(calcState);
					}
				} else if (cellProvider.formulaCells != null) {
					if (cellProvider.formulaCells.length > 0) {
						var loc = cellProvider.formulaCells.shift();
						cE.visitCell(calcState, loc[0], loc[1]);
					} else {
						done = true;
						return null;
					}					
				} else {
					if (cE.visitCell(calcState, row, col) == true) {
						done = true;
						return null;
					}

					if (col >= cellProvider.getNumberOfColumns(row)) {
						row = row + 1;
						col = 1;
					} else {
						col = col + 1; // Sweep through columns first.
					}
				}
				
				if (fuel != null) {
					fuel -= 1;
				}
			}
		}
		return calcState.calcMore;
	},
	visitCell: function(calcState, r, c) { // Returns true if done with all cells.
		with (calcState) {
			[r, c]; //make current usable to fn's
			var cell = cellProvider.getCell(r, c);
			if (cell == null) {
				return true;
			}

			var value = cell.getValue();
			if (value == null) {
				var formula = cell.getFormula();
				if (formula != null) {
					var firstChar = formula.charAt(0);
					if (firstChar == '=') {
						var formulaFunc = cell.getFormulaFunc();
						if (formulaFunc == null ||
							formulaFunc.formula != formula) {
							formulaFunc = null;
							try {
								var dependencies = {};
								var body = cE.parseFormula(formula.substring(1), dependencies, calcState);
								formulaFunc = eval(
								"var Calc_spreadsheet_formula = " +
									"function(__CELL_PROVIDER, __CONTEXT, __STD_FUNCS) { " +
										"with (__CELL_PROVIDER) {" + 
											"with (__STD_FUNCS) { " +
												"with (__CONTEXT) { return (" + body + "); }" +
											"}" +
										"}" + 
									"}; " +
									"Calc_spreadsheet_formula"
								);
								formulaFunc.formula	  = formula;
								formulaFunc.dependencies = dependencies;
								cell.setFormulaFunc(formulaFunc);
							} catch (e) {
								//cell.setValue(cE.ERROR, e);
								cell.setValue(e,e);
							}
						}
						if (formulaFunc != null) {
							stack.push(cE.makeFormulaEval(r, c));

							// Push the cell's dependencies, first checking for any cycles. 
							var dependencies = formulaFunc.dependencies;
							for (var k in dependencies) {
								if (dependencies[k] instanceof Array &&
									cE.checkCycles(stack, dependencies[k][0], dependencies[k][1]) == true) {
									cell.setValue(cE.ERROR, "cycle detected");
									stack.pop();
									return false;
								}
							}
							for (var k in dependencies) {
								if (dependencies[k] instanceof Array) {
									stack.push(cE.makeCellVisit(dependencies[k][0], dependencies[k][1]));
								}
							}
						}
					} else {
						cell.setValue(cE.parseFormulaStatic(formula));
					}
				}
			}
		}
		return false;
	},
	makeCellVisit: function(row, col) {
		var func = function(calcState) { return cE.visitCell(calcState, row, col); };
		func.row = row;
		func.col = col;
		return func;
	},
	RE_AMP: /&/g,
	RE_LT: /</g,
	RE_GT: />/g,
	RE_NBSP: /&nbsp;/,
	makeFormulaEval: function(row, col) {
		var func = function(calcState) {
			var cell = calcState.cellProvider.getCell(row, col);
			if (cell != null) {
				var formulaFunc = cell.getFormulaFunc();
				if (formulaFunc != null) {
					try {
						var v = formulaFunc(calcState.cellProvider, calcState.context, cE.fn);
						switch(typeof(v)) {
							case "string":
								v = v
									.replace(cE.RE_AMP, '&amp;')
									.replace(cE.RE_LT, '&lt;')
									.replace(cE.RE_GT, '&gt;')
									.replace(cE.RE_NBSP, '&nbps;');
							case "object":
								v = jQuery('<div />').html(v).html();
						}
						cell.setValue(v);
						
					} catch (e) {
						cell.setValue(cE.ERROR + ': ' + e, e);
					}
				}
			}
		}
		func.row = row;
		func.col = col;
		return func;
	},
	checkCycles: function(stack, row, col) {
		for (var i = 0; i < stack.length; i++) {
			var item = stack[i];
			if (item.row != null && item.col != null &&
				item.row == row  && item.col == col) {
				return true;
			}
		}
		return false;
	},
	foldPrepare: function(firstArg, theArguments) { // Computes the best array-like arguments for calling fold().
		if (firstArg != null &&
			firstArg instanceof Object &&
			firstArg["length"] != null) {
			return firstArg;
		}
		return theArguments;
	},
	fold: function(arr, funcOfTwoArgs, result, castToN) {
		for (var i = 0; i < arr.length; i++) {
			result = funcOfTwoArgs(result, (castToN == true ? cE.fn.N(arr[i]): arr[i]));
		}
		return result;
	}
};
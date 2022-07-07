function GXSpreadsheet()
{
	this.Width;
	this.Height;
	this.Attribute;
	gxSpreadsheetUC = this;
	this.TITLE_HEIGHT = 130;
	this.Menus = new Array();


	// Databinding for property Attribute
	this.SetAttribute = function(data) {
	    ///UserCodeRegionStart:[SetAttribute] (do not remove this comment.)
	    this.SpreadsheetHtmlSource = data;
	    ///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Attribute
	this.GetAttribute = function() {
	    ///UserCodeRegionStart:[GetAttribute] (do not remove this comment.)
	    return jS.obj.pane().html();		
	    ///UserCodeRegionEnd: (do not remove this comment.)
	}
	
	this.GetMenu = function()
	{
			
	}

	this.SetMenu = function(data) {
	    if (!this.Menus.length > 0)
	        this.Menus.push(data);
	}

	this.show = function() {
	    ///UserCodeRegionStart:[show] (do not remove this comment.)
	  gx.fx.obs.addObserver("popup.close", this, this.HandlePopupClose);
		if (gx.util.browser.isChrome())
			setTimeout(this.CreateDelegate(this, this.InitSpreadsheet), 500);	
		else
			this.InitSpreadsheet();
	    ///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	this.InitSpreadsheet = function() {
		if (this.LastSpreadsheetHtmlSource != this.SpreadsheetHtmlSource)
		{
			this.LastSpreadsheetHtmlSource = this.SpreadsheetHtmlSource
			this.ToolbarLoaded = false;
			this.getContainerControl().style.position = "relative";
			this.setHtml(this.SpreadsheetHtmlSource);		
			var containerName = "#" + this.ContainerName;
			
			this.SetProperties();
			
		
			this.CreateSpreadsheet();
	    
		}
	}
	
	this.CreateSpreadsheet= function(initObject)
	{
		var create = this.Columns + "x" + this.Rows;		
		if (this.SpreadsheetHtmlSource && this.SpreadsheetHtmlSource != "undefined" && this.SpreadsheetHtmlSource != "")
			create = false;
		var staticDir = gx.staticDirectory.substring(1, gx.staticDirectory.length);
		var containerName = "#" + this.ContainerName;

    var initObject = {
    				title: gxSpreadsheetUC.Title,
	            urlGet: null,
	            urlTheme: gx.util.resourceUrl(staticDir + "GXSpreadsheet/theme/jquery-ui-1.7.2.custom.css", true),
	            urlBaseCss: gx.util.resourceUrl(staticDir + 'GXSpreadsheet/jquery.sheet.css', true),
	            urlMenu: gx.util.resourceUrl(staticDir + "GXSpreadsheet/menu.html", true),
	            urlMenuJs: gx.util.resourceUrl(staticDir + "GXSpreadsheet/plugins/mbMenu.min.js", true),
	            urlMenuCss: gx.util.resourceUrl(staticDir + "GXSpreadsheet/plugins/menu.css", true),
	            urlMetaData: gx.util.resourceUrl(staticDir + "GXSpreadsheet/plugins/jquery.metadata.js", true),
	            urlScrollTo: gx.util.resourceUrl(staticDir + "GXSpreadsheet/plugins/jquery.scrollTo-min.js", true),
	            urlScrollsync: gx.util.resourceUrl(staticDir + 'GXSpreadsheet/plugins/scrollsync.js', true),
	            fnAfter: gxSpreadsheetUC.CreateDelegate(gxSpreadsheetUC, gxSpreadsheetUC.OnSpreadsheetLoaded),
	            urlJGCharts: gx.util.resourceUrl(staticDir + 'GXSpreadsheet/plugins/jgcharts.pack.js', true),
	            buildSheet: create,				
							editable : gxSpreadsheetUC.CBoolean(gxSpreadsheetUC.Editable),
	            fnSave: gxSpreadsheetUC.CreateDelegate(gxSpreadsheetUC, gxSpreadsheetUC.Save),
							colMargin: this.CellHeight,
							newColumnWidth: this.CellWidth
	      
    	};
    	if (gx.util.browser.isChrome())
    		$(containerName).sheet(initObject);
    	else
    		$(function() {$(containerName).sheet(initObject); });			
	    this.LoadToolbars();

	}

	this.SetProperties = function()
	{
		this.SetSize();
	}
	
	this.OnSpreadsheetLoaded= function()
	{
		this.Scrolls = setInterval(gxSpreadsheetUC.CreateDelegate(gxSpreadsheetUC, gxSpreadsheetUC.SetScrollsAndSize), 500);
		if (gx.gen.isRuby()){
			this.RealControl = this.getContainerControl();
			if (jS.obj.formula()[0])
			jS.obj.formula()[0].onfocus = this.CreateDelegate(this, this.onfocus);
		}
	}
	
	this.SetScrollsAndSize = function()
	{
		try{
			if (this.CBoolean(this.Editable))
			{
				jS.obj.pane().add(jS.obj.barLeftParent()).scrollsync({axis: 'y'});
				jS.obj.pane().add(jS.obj.barTopParent()).scrollsync({axis: 'x'});	
			
				if (this.CBoolean(this.Autoresize))
				{
					gx.dom.byId("jSheetEditPane").style.height = gx.dom.addUnits(parseInt(this.Rows * this.CellHeight) - (this.Menus.length * 18));
					gx.dom.byId("jSheetBarLeftParent").style.height = gx.dom.addUnits(this.Rows * this.CellHeight - (this.Menus.length * 18));
					if (parseInt(this.getContainerControl().clientWidth) > parseInt(gx.dom.byId("jSheetEditPane").clientWidth)) 
						this.getContainerControl().style.width = gx.dom.addUnits(parseInt(gx.dom.byId("jSheetEditPane").clientWidth) + parseInt(gx.dom.byId("jSheetBarLeftParent").clientWidth * 2));
					//gx.dom.byId("jSheetBarTopParent").style.width= parseInt(this.Columns * this.CellWidth);
					//this.getContainerControl().style.width = gx.dom.byId("jSheetEditPane").clientWidth;				
				}
				else
				{
					gx.dom.byId("jSheetEditPane").style.height = gx.dom.addUnits(parseInt(this.Height) - (this.CBoolean(this.Editable)?this.TITLE_HEIGHT - (this.Menus.length * 18) :0));
					gx.dom.byId("jSheetBarLeftParent").style.height = gx.dom.addUnits(parseInt(this.Height) - (this.CBoolean(this.Editable)?this.TITLE_HEIGHT - (this.Menus.length * 18):0));
				}					
			}
			clearInterval(this.Scrolls);
		}
		catch(e){}
	}
	
	
	this.SetSize = function()
	{
		if (this.CBoolean(this.Autoresize))
			this.Height = this.Rows * this.CellHeight + (this.CBoolean(this.Editable)?this.TITLE_HEIGHT:0);		
		else
			if (this.Height.indexOf("%") > -1)
				this.Height = "500";				
		this.getContainerControl().style.height = gx.dom.addUnits(this.Height);
		this.getContainerControl().style.width = gx.dom.addUnits(this.Width);

	}

	this.LoadToolbars = function() {

	    var menu = document.getElementById("jSheetMenu");
	    if (menu && !this.ToolbarLoaded) {
	    		this.ToolbarLoaded = true;
	        this.LoadDefaultToolbar();
	        var i = 0;
	        for (i = 0; this.Menus[i] != undefined; i++) {
	            var toolbar = this.ToolbarMgr.NewToolbarFromSDT(this.Menus[i]);
	            this.AttachToolbarEvents(toolbar.ToolbarObj)	            
	        }	        
	    }
	    else
	        setTimeout(this.CreateDelegate(this, this.LoadToolbars), 200);
	}

	this.LoadDefaultToolbar = function() {
	    var staticDir = gx.staticDirectory.substring(1, gx.staticDirectory.length);
	    var toolbar = this.ToolbarMgr.NewToolbarFromXML(gx.util.resourceUrl(staticDir + "GXSpreadsheet/imgs/", true), gx.util.resourceUrl(staticDir + "GXSpreadsheet/dhtmlxtoolbar.xml?etc=", true));
	    this.AttachToolbarEvents(toolbar.ToolbarObj)
	}
	
	this.AttachToolbarEvents = function(toolbar)
	{
		toolbar.attachEvent("onStateChange", function(id) {
	        return gxSpreadsheetUC.HandleToolbarClick(id);
	    });
		toolbar.attachEvent("onClick", function(id) {
	        return gxSpreadsheetUC.HandleToolbarClick(id);
	    });
	}

	this.HandleToolbarClick = function(id) {
	    if (id.indexOf(this.ToolbarMgr.BUTTON_PREFIX) == -1) {
	        var funcExec = eval("this.toolbarFunctions." + id);
	        if (funcExec)
	            funcExec();
	    }
	    else {
	        this.OpenPrompt(id);
	    }
	}

	this.OpenPrompt = function(id) {
	    var buttonProperties = this.ToolbarMgr.ToolbarsButtons[id];
	    this.handle = gx.popup.openUrl(gx.http.formatLink(buttonProperties.Link), ["OutPutParm1"]);
	}

	this.HandlePopupClose = function(popupData) {
	    
	    var cellProvider = new jS.tableCellProvider(null);
	    var cell = cellProvider.getCell(jS.cellEditLastLoc[1], jS.cellEditLastLoc[2]);
	    cell.setValue(this.handle.RawReturnedParms[0]);
	    jS.cellEdit(jQuery(cell.getTd()));
	    jS.cellOnClickManage(jQuery(cell.getTd()));
	}

	this.ToolbarMgr =
	{
	    Toolbars: [],
	    ToolbarsButtons: [],
	    LastId: 0,
	    LastButtonID: 0,
	    BUTTON_PREFIX: "Custom",

	    Class: function(id, toolbarObj) {
	        this.Id = id;
	        this.ToolbarObj = toolbarObj;
	    },

	    NewToolbarFromXML: function(imgPath, xmlPath) {
	        var toolbarObj = new dhtmlXToolbarObject(this.GetNewDiv());
	        toolbarObj.setIconsPath(imgPath);
	        toolbarObj.loadXML(xmlPath + new Date().getTime());
	        var toolbar = new this.Class(this.LastId, toolbarObj);
	        this.Toolbars[this.LastId] = toolbar;
	        return toolbar;
	    },

	    NewToolbarFromSDT: function(sdt) {
	        var toolbarObj = new dhtmlXToolbarObject(this.GetNewDiv());
	        this.AddToolbarButtons(toolbarObj, sdt);
	        var toolbar = new this.Class(this.LastId, toolbarObj);
	        return toolbar;
	    },

	    AddToolbarButtons: function(toolbarObj, sdt) {
	        var i = 0;
	        for (i; sdt[i] != undefined; i++) {
	            this.GetLastToolbarsButtonsId();
	            toolbarObj.addButton(this.BUTTON_PREFIX + this.LastButtonID, 0, "", sdt[i].Image, sdt[i].Image);
	            this.ToolbarsButtons[this.BUTTON_PREFIX + this.LastButtonID] = sdt[i];
	        }
	    },

	    GetLastToolbarsButtonsId: function() {
	        var id = this.LastButtonID;
	        this.LastButtonID++;
	        return id;
	    },

	    GetNewDiv: function() {
	        this.LastId++;
	        var newDiv = document.createElement('div');
	        newDiv.id = "Menu" + this.LastId;
	        var mainMenu = document.getElementById("jSheetMenu");
	        mainMenu.appendChild(newDiv);
	        return newDiv.id;
	    }
	}

	this.toolbarFunctions = {

	    text_bold: function(id) {
	        jS.cellStyleToggle('styleBold');
	    },
	    text_italic: function(id) {
	        jS.cellStyleToggle('styleItalics');
	    },
	    text_underline: function(id) {
	        jS.cellStyleToggle('styleUnderline');
	    },
	    text_left: function(id) {
	        jS.cellStyleToggle('styleLeft', 'styleCenter,styleRight');
	    },
	    text_center: function(id) {
	        jS.cellStyleToggle('styleCenter', 'styleLeft,styleCenter');
	    },
	    text_right: function(id) {
	        jS.cellStyleToggle('styleRight', 'styleCenter,styleRight');
	    },
	    text_fonttype_arial: function(id) {
	        jS.obj.uiCell()[0].style.fontFamily = "Arial";
	    },
	    text_fonttype_comicssansms: function(id) {
	        jS.obj.uiCell()[0].style.fontFamily = "Comics Sans Ms";
	    },
	    text_fonttype_couriernew: function(id) {
	        jS.obj.uiCell()[0].style.fontFamily = "Courier New";
	    },
	    text_fonttype_tahoma: function(id) {
	        jS.obj.uiCell()[0].style.fontFamily = "Tahoma";
	    },
	    text_fonttype_timesnewroman: function(id) {
	        jS.obj.uiCell()[0].style.fontFamily = "Times New Roman";
	    },
	    text_fonttype_verdana: function(id) {
	        jS.obj.uiCell()[0].style.fontFamily = "Verdana";
	    },
	    text_fontcolor_blue: function(id) {
	        jS.obj.uiCell()[0].style.color = "blue";
	    },
	    text_fontcolor_green: function(id) {
	        jS.obj.uiCell()[0].style.color = "green";
	    },
	    text_fontcolor_red: function(id) {
	        jS.obj.uiCell()[0].style.color = "red";
	    }
	}

	this.CreateDelegate = function(obj, method, args, appendArgs) {
	    return function() {
	        var callArgs = args || arguments;
	        if (appendArgs === true) {
	            callArgs = Array.prototype.slice.call(arguments, 0);
	            callArgs = callArgs.concat(args);
	        } else if (typeof appendArgs == "number") {
	            callArgs = Array.prototype.slice.call(arguments, 0);
	            var applyArgs = [appendArgs, 0].concat(args);
	            Array.prototype.splice.apply(callArgs, applyArgs);
	        }
	        return method.apply(obj || window, callArgs);
	    };
	}

	this.CBoolean = function(str) {
	    if (str) {
	        if (typeof (str) == 'string')
	            return (str.toLowerCase() == "true")
	        return str;
	    }
	    else
	        return false;
	}
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

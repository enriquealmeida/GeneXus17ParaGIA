function Treeview($)
{
	this.TreeNodeCollectionData;
	this.SelectedTreeNode;
	this.Width;
	this.Height;
	this.UseIcons;
	this.ParentIcon;
	this.ParentSelectedIcon;
	this.LeafIcon;
	this.LeafSelectedIcon;
	this.InternalParentMapper = new Array();
	this.LastSelectedNode;
    this.LinkTarget;
    this.Style;
    this.Font;
    this.FontWhenSelected;
    

	// Databinding for property TreeNodeCollectionData
	this.SetTreeNodeCollectionData = function(data)
	{
		///UserCodeRegionStart:[SetTreeNodeCollectionData] (do not remove this comment.)
		this.TreeNodeCollectionData = data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property TreeNodeCollectionData
	this.GetTreeNodeCollectionData = function()
	{
		///UserCodeRegionStart:[GetTreeNodeCollectionData] (do not remove this comment.)
		if (this.NodeClick != undefined)
	  	if (this.NodeClick)
	    {
	    	var dummy = new Object();
	    	dummy.Id = "";
	    	dummy.Name = "";
	    	dummy.Link = "";
	    	dummy.Expanded = true;
	    	dummy.DynamicLoad = true;
	    	dummy.LinkTarget = "";
	    	dummy.Icon = "";
	    	dummy.IconWhenSelected = "";
	    	dummy.Nodes= new Array();
	    	var dummy2 = new Array();
	    	dummy2[0] = dummy;
	    	return dummy2;
			}
			return this.TreeNodeCollectionData;	
			///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property SelectedTreeNode
	this.SetSelectedTreeNode = function(data)
	{
		///UserCodeRegionStart:[SetSelectedTreeNode] (do not remove this comment.)
		this.SelectedTreeNode = data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property SelectedTreeNode
	this.GetSelectedTreeNode = function()
	{
		///UserCodeRegionStart:[GetSelectedTreeNode] (do not remove this comment.)
		return this.SelectedTreeNode;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if (this.Postback != true)
		{
			if (!window.myTreeInstancesHandle)
				myTreeInstancesHandle = new Array();
			myTreeInstancesHandle[this.ContainerName] = this;

			this.CreateTreeview();

			gx.fx.obs.addObserver("gx.onclick", this, this.DocumentClickHandler);
			gx.spa.addIgnoredSelector(".BasicNodeTextDecoration");
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	
	this.destroy = function () {
		gx.fx.obs.deleteObserver("gx.onclick", this, this.DocumentClickHandler);
		gx.spa.removeIgnoredSelector(".BasicNodeTextDecoration");
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	this.Refresh = function() {
		this.lastSelectedNode = null;
	    this.CreateTreeview();
	}

	this.CreateTreeview = function()
	{
        this.SetStyle();

        this.YahooTree = new YAHOO.widget.TreeView(this.ContainerName);
		var root = this.YahooTree.getRoot();
		this.AddTreeNodes(this.TreeNodeCollectionData,root);
        //event suscriptions
		var auxThis = this;
		this.YahooTree.subscribe("labelClick", function(node) {
			auxThis.OnNodeClicked(node);
			return !node.data.GXNode.Link;
		});
		this.YahooTree.subscribe("expand", function(node) {
		    auxThis.OnNodeExpanded(node);
		});
		this.YahooTree.subscribe("collapse", function(node) {
		    auxThis.OnNodeCollapsed(node);
		});

		this.YahooTree.draw();
	}

	this.AddTreeNodes = function(treeNodeCollection, parentNode)
	{
	    if (treeNodeCollection!=undefined)
	    {
	        var i = 0;
	        for(i=0;treeNodeCollection[i]!=undefined;i++)
	        {
	            if (gx.text.trim(treeNodeCollection[i].Id)=="") treeNodeCollection[i].Id = treeNodeCollection[i].Name;
	            var dynamicLoad = this.GetBoolean(treeNodeCollection[i].DynamicLoad);
	            var isParent = ((treeNodeCollection[i].Nodes != undefined && treeNodeCollection[i].Nodes.length > 0 ) || dynamicLoad)?true:false;
	            if (gx.text.trim(treeNodeCollection[i].LinkTarget) == "") treeNodeCollection[i].LinkTarget = this.LinkTarget;
	            var myobj = { label: Mustache.escape(treeNodeCollection[i].Name), href:treeNodeCollection[i].Link, GXNode:treeNodeCollection[i], target:treeNodeCollection[i].LinkTarget};
	            var tmpNode = new YAHOO.widget.TextNode(myobj, parentNode, false);
	            tmpNode.Appearance = new Array();
              //Customize icon appareance
              this.CustomizeNodeAppareance(tmpNode,isParent);
	            //Sets dynamic load
              if (dynamicLoad) tmpNode.setDynamicLoad(this.loadDataForNode);
   	          //Mantains InternalParentMapper
   	          if (parentNode.data)
	                this.InternalParentMapper[treeNodeCollection[i].Id] = parentNode.data.GXNode.Id;
	            else
	                this.InternalParentMapper[treeNodeCollection[i].Id] = 0;
              //Process children
	            this.AddTreeNodes(treeNodeCollection[i].Nodes, tmpNode);
	        }
	    }
	}

	this.OnNodeClicked = function(node)
	{
		var afterNodeClicked = function () {
			var linkTarget = node.data.GXNode.LinkTarget;
			node.Appearance["ImageIcon"] = node.data.GXNode.IconWhenSelected;
			node.Appearance["Font"] = node.data.GXNode.FontWhenSelected;
			node.labelStyle = node.data.GXNode.NodeTextDecorationSelected;
			this.RunNodeRefresh(node);
			this.UpdateLastSelectedNode(node);
			if (node.data.GXNode.Link) {
				if (!linkTarget) {
					gx.http.redirect(node.data.GXNode.Link);
				}
				else {
					window.open(node.data.GXNode.Link, node.data.GXNode.LinkTarget);
				}
			}
		};

	    if (this.NodeClicked)
	    {
	        this.NodeClick = true;
	        this.Postback = true;
	        this.SelectedTreeNode = node.data.GXNode;
	        this.NodeClicked(function () {
				this.NodeClick = false;
				afterNodeClicked.call(this);
			});
	    }
		else {
			afterNodeClicked.call(this);
		}
	}

	this.OnNodeExpanded = function(node)
	{
	    node.Appearance["ImageIcon"] = node.data.GXNode.IconWhenSelected;
	    //node.Appearance["Font"] = node.data.GXNode.FontWhenSelected;
	    this.RunNodeRefresh(node);
	}

	this.OnNodeCollapsed = function(node)
	{
	    node.Appearance["ImageIcon"] = node.data.GXNode.Icon;
	    //node.Appearance["Font"] = node.data.GXNode.Font;
	    this.RunNodeRefresh(node);
	}

	this.loadDataForNode = function(node, onCompleteCallback)
	{
	    var myTreeInstance = myTreeInstancesHandle[this.tree.id];
	    if (myTreeInstance.PopulateNode)
	    {
	        myTreeInstance.Postback = true;
	        myTreeInstance.SelectedTreeNode = node.data.GXNode;
	        myTreeInstance.PopulateNode(function () {
				myTreeInstance.AddTreeNodes(myTreeInstance.SelectedTreeNode.Nodes,node);
				onCompleteCallback();
			});
	        //myTreeInstance.UpdateTreeNode(myTreeInstance.SelectedTreeNode.Nodes);
	    }
		else {
			onCompleteCallback();
		}
	}

	this.UpdateLastSelectedNode = function(node)
	{
	    if (this.lastSelectedNode!=undefined && node.index != this.lastSelectedNode.index) 
        {
            if (!this.lastSelectedNode.hasChildren())
            {
                this.lastSelectedNode.Appearance["ImageIcon"] = this.lastSelectedNode.data.GXNode.Icon;
                this.lastSelectedNode.Appearance["Font"] = node.data.GXNode.Font;
                this.lastSelectedNode.labelStyle = this.lastSelectedNode.data.GXNode.NodeTextDecoration;
                this.RunNodeRefresh(this.lastSelectedNode);
            }
            if (this.lastSelectedNode.hasChildren() )
            {
                this.lastSelectedNode.Appearance["Font"] = node.data.GXNode.Font;
                this.lastSelectedNode.labelStyle = this.lastSelectedNode.data.GXNode.NodeTextDecoration;
                this.RunNodeRefresh(this.lastSelectedNode);
            }
        }
	    this.lastSelectedNode = node;
	}

	this.RunNodeRefresh = function(node)
	{
	    document.getElementById(node.labelElId + "Span").className= node.labelStyle;
	    document.getElementById(node.labelElId + "Span").style.cssText = node.Appearance["Font"];
	    node.getLabelEl().style.background="url('" + node.Appearance["ImageIcon"] + "') no-repeat";
	}

	this.UpdateTreeNode = function(treeNodeCollection)
	{
	    var parentArray = this.CalculateParentsForSelectedNode();
	    var node = this.FindSelectedNodeFromTree(parentArray);
	    node.Nodes = treeNodeCollection;
	}

	this.CalculateParentsForSelectedNode = function()
	{
	    var auxId = this.SelectedTreeNode.Id;
	    var parentArray = new Array();
	    parentArray.push(auxId);
	    while (auxId!=0)
	    {
	        var parent = this.InternalParentMapper[auxId];
	        if (parent!=0) parentArray.push(parent);
	        auxId = parent;
	    }
	    return parentArray;
	}

	this.FindSelectedNodeFromTree = function(parentArray)
	{
	    var treeNodeCollection = this.TreeNodeCollectionData;
	    var i = 0;
	    var node;
	    while ((idToSearch = parentArray.pop())!=undefined)
	    {
	        for(i=0;treeNodeCollection[i]!=undefined;i++)
	        {
	            if (treeNodeCollection[i].Id == idToSearch)
	            {
	                node = treeNodeCollection[i];
	                treeNodeCollection = treeNodeCollection[i].Nodes;
	                break;
	            }
	        }
	        i++;
	    }
	    return node;
	}

	this.CustomizeNodeAppareance = function(node, isParent)
	{
        node.labelStyle = "NodeTextDecoration";
        node.data.GXNode.NodeTextDecoration = "NodeTextDecoration";
        node.data.GXNode.NodeTextDecorationSelected = "NodeTextDecorationSelected";
        node.data.GXNode.Font = this.Font;
        node.data.GXNode.FontWhenSelected = this.FontWhenSelected;

		if (isParent)
        {
            if (gx.text.trim(node.data.GXNode.Icon) == "" || node.data.GXNode.Icon=="undefined")
                if (gx.text.trim(this.ParentIcon) == "" || this.ParentIcon=="undefined")
                    node.data.GXNode.Icon = this.DefaultParentIcon;
                else
                    node.data.GXNode.Icon = this.ParentIcon;

            if (gx.text.trim(node.data.GXNode.IconWhenSelected) == "" || node.data.GXNode.IconWhenSelected=="undefined")
                if (gx.text.trim(this.ParentSelectedIcon) == "" || this.ParentSelectedIcon=="undefined") 
                    node.data.GXNode.IconWhenSelected = this.DefaultParentSelectedIcon;
                else
                    node.data.GXNode.IconWhenSelected = this.ParentSelectedIcon;
        }
        else
        {
            if (gx.text.trim(node.data.GXNode.Icon) == "" || node.data.GXNode.Icon=="undefined")
                if (gx.text.trim(this.LeafIcon) == "" || this.LeafIcon=="undefined")
                    node.data.GXNode.Icon = this.DefaultLeafIcon;
                else
                    node.data.GXNode.Icon = this.LeafIcon;

            if (gx.text.trim(node.data.GXNode.IconWhenSelected) == "" || node.data.GXNode.IconWhenSelected=="undefined")
                if (gx.text.trim(this.LeafSelectedIcon) == "" || this.LeafSelectedIcon=="undefined") 
                    node.data.GXNode.IconWhenSelected = this.DefaultLeafSelectedIcon;
                else
                    node.data.GXNode.IconWhenSelected = this.LeafSelectedIcon;

        }

        if (this.GetBoolean(node.data.GXNode.Expanded) == false || this.GetBoolean(node.data.GXNode.DynamicLoad)==true)
        {
            node.Appearance["ImageIcon"] = node.data.GXNode.Icon;
        }
        else
        {
            node.Appearance["ImageIcon"] = node.data.GXNode.IconWhenSelected;
            node.expanded = true;
        }

        //Customize fonts
        node.Appearance["Font"] = node.data.GXNode.Font;

    }

    this.SetStyle = function()
    {

        this.DefaultParentIcon = "";
        this.DefaultParentSelectedIcon = "";
        this.DefaultLeafIcon = "";
        this.DefaultLeafSelectedIcon = "";
        var folder="";

        var imgsDir = gx.staticDirectory.substring(1,gx.staticDirectory.length);

        //set treeView icons according to this.Style
        switch (this.Style)
        {
            case "Default":
                folder = "default";
                break;
            case "DefaultWithDefaultIcons":
                this.DefaultParentIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/folder.gif", true);
                this.DefaultParentSelectedIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/folderopen.gif", true);
                this.DefaultLeafIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/page.gif", true);
                this.DefaultLeafSelectedIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/globe.gif", true);
                var folder = "default";
                break;
            case "Modern":
                folder = "";
                break;
            case "ModernWithDefaultIcons":
                   this.DefaultParentIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/folder.gif", true);
                this.DefaultParentSelectedIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/folderopen.gif", true);
                this.DefaultLeafIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/page.gif", true);
                this.DefaultLeafSelectedIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/globe.gif", true);
                var folder = "";
                break;
            case "Menu":
                folder = "menu";
                break ;
            case "NoLines":
                folder = "nolines";
                break ;
            case "NoLinesWithDefaultIcons":
                this.DefaultParentIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/folder.gif", true);
                this.DefaultParentSelectedIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/folderopen.gif", true);
                this.DefaultLeafIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/page.gif", true);
                this.DefaultLeafSelectedIcon = gx.util.resourceUrl(imgsDir + "Treeview/assets/img/customIcons/globe.gif", true);
                folder = "nolines";
                break ;
        }

        //load css according to this.Style
	    var headID = document.getElementsByTagName("head")[0];
	    var cssNode = document.createElement('link');
	    cssNode.type = 'text/css';
	    cssNode.rel = 'stylesheet';
	    cssNode.media = 'screen';
	    if (folder!="")
			cssNode.href = gx.util.resourceUrl(imgsDir + 'Treeview/assets/css/'+ folder + '/tree.css', true);
	    else
			cssNode.href = gx.util.resourceUrl(imgsDir + 'Treeview/assets/css/treeview.css', true);
	    headID.appendChild(cssNode);


    }

	this.GetBoolean = function(aBoolean)
	{
		try
		{
			return aBoolean.toLowerCase()=="true";
		}
		catch(err)
		{
			return aBoolean;
		}
	}
	
	this.DocumentClickHandler = function (eventObject) {
		var targetEl = eventObject.event.target;
		if (targetEl && targetEl.id) {
			eventObject.cancel = $("#" + this.ContainerName + " #" + targetEl.id).length > 0;
		}
	}

	///UserCodeRegionEnd: (do not remove this comment.):
}
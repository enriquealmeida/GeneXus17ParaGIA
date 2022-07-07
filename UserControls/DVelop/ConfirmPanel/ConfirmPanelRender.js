function DVelop_ConfirmPanel()
{
	this.Width;
	this.Height;
	this.Closeable;
	this.Title;
	this.ConfirmText;
	this.Icon;
	this.Modal;
	this.Result;
	this.ButtonYesText;
	this.ButtonNoText;
	this.ButtonCancelText;
	this.ConfirmType;
	this.Draggable;

	this.Show = function()
	{
		///UserCodeRegionStart:[Show] (do not remove this comment.)
		if (!this.PanelRef)
		{
		    var panelId = this.ContainerName + "_ConfirmPanel";

		    this.setHtml('<div id="' + panelId + '" style="display:none" ></div>');
			// Define various event handlers for Dialog
			var DVelopConfirmPanel = this;

			var handleYes = function()
			{
				DVelopConfirmPanel.OnClose("Yes");
			};
			var handleNo = function()
			{
				DVelopConfirmPanel.OnClose("No");
			};
			var handleCancel = function()
			{
				DVelopConfirmPanel.OnClose("Cancel");
			};
			var handleClose = function()
			{
				DVelopConfirmPanel.OnClose("Close");
			};

			this.PanelRef = new YAHOO.widget.SimpleDialog(panelId, {
				width: this.Width,
				fixedcenter: true,
				visible: false,
				close: this.IsTrue(this.Closeable),
				text: this.ConfirmText,
				modal: this.IsTrue(this.Modal),
				constraintoviewport: true,
				underlay: "none",
				draggable: this.IsTrue(this.Draggable)
			});

			var myButtons = [];

			if (this.ConfirmType == 0)
			{
				myButtons = [{ text: this.ButtonYesText, handler: handleYes, isDefault: true}];
			}
			else if (this.ConfirmType == 1)
			{
				myButtons = [{ text: this.ButtonYesText, handler: handleYes, isDefault: true },
							{ text: this.ButtonNoText, handler: handleNo}];
			}
			else
			{
				myButtons = [{ text: this.ButtonYesText, handler: handleYes, isDefault: true },
							{ text: this.ButtonNoText, handler: handleNo },
							{ text: this.ButtonCancelText, handler: handleCancel}];
			}
			this.PanelRef.cfg.queueProperty("buttons", myButtons);
			this.PanelRef.setHeader(this.Title);
			this.PanelRef.render();

			YAHOO.util.Event.on(YAHOO.util.Dom.getElementsByClassName("Close", "a", panelId), "click", handleClose);
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	this.PanelRef;

	this.Confirm = function() {
	document.getElementById(this.ContainerName + '_ConfirmPanel').style.display = '';
	this.Result = "Running";
	this.PanelRef.setHeader(this.Title);
	var icon = "";
	//	0: ICON_HELP, 1: ICON_BLOCK, 2: ICON_WARN, 3: ICON_INFO, 4: ICON_ALARM, 5: ICON_TIP
	if (this.Icon == 1)
	{
		icon = YAHOO.widget.SimpleDialog.ICON_BLOCK;
	}
	else if (this.Icon == 2)
	{
		icon = YAHOO.widget.SimpleDialog.ICON_WARN;
	}
	else if (this.Icon == 3)
	{
		icon = YAHOO.widget.SimpleDialog.ICON_INFO;
	}
	else if (this.Icon == 4)
	{
		icon = YAHOO.widget.SimpleDialog.ICON_ALARM;
	}
	else if (this.Icon == 5)
	{
		icon = YAHOO.widget.SimpleDialog.ICON_TIP;
	}
	else
	{
		icon = YAHOO.widget.SimpleDialog.ICON_HELP;
	}

	var htmlIconStart = "<span class=\"DVelop-Icon ";
	var htmlIconEnd =  "\">&nbsp;</span>";
	this.PanelRef.setBody(htmlIconStart.concat(icon, htmlIconEnd, this.ConfirmText)); 
	this.PanelRef.show();
	    
	}

	this.OnClose = function(result)
	{
		this.Result = result;
		this.PanelRef.hide();
		this.Close();
		document.getElementById(this.ContainerName + '_ConfirmPanel').style.display = 'none';
	}

	this.IsTrue = function(condition)
	{
		if (condition == true || condition == 1 || condition == "true" || condition == "True")
			return true;
		return false;
	}
	///UserCodeRegionEnd: (do not remove this comment.):
}

function NoRightClick()
{
	this.DisableRightClick;
	this.ShowAlert;
	this.AlertMessage;

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)

		var mthis = this;
		var mblck = function() {
				if (mthis.ShowAlert) {
					alert(mthis.AlertMessage);
				}
				if (mthis.RightClick)
					mthis.RightClick();
			};
		var mfunc = function(evt) {
				if (mthis.DisableRightClick) {
					if (evt.which==3) {
						mblck();
						evt.preventDefault();
						return false;
					}
				}
			};
		var mfuncIE = function() {
				if (mthis.DisableRightClick) {
					mblck();
					event.cancelBubble = true;
					return false;
				}
			};
		if (!this.IsPostBack) {
			if (document.addEventListener) {
				document.addEventListener('contextmenu',mfunc,false);
			} else if (document.attachEvent) {
				document.attachEvent('oncontextmenu',mfuncIE);
			}
		}
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

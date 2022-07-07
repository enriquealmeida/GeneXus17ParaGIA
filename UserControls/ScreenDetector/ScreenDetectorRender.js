function ScreenDetector()
{
	this.ScreenWidth;
	this.ScreenHeight;
	this.ClientScreenWidth;
	this.ClientScreenHeight;
	this.ScreenColorDepth;
	this.FirstTime;
	
	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if (this.FirstTime != false)
		{
		    this.FirstTime = false;
		    gx.evt.on_ready(this, this.getScreenData);
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	this.getScreenData = function()
	{
		this.ScreenWidth = screen.width;
		this.ScreenHeight = screen.height;
		this.ScreenColorDepth = screen.colorDepth;
		this.ClientScreenWidth = this.getClientWidth();
		this.ClientScreenHeight = this.getClientHeight();
    	if (this.ScreenDetected)
	    {
   			this.ScreenDetected();
	    }
	}
	
	this.getClientWidth = function()
	{
    	return window.innerWidth ? window.innerWidth : /* For non-IE */
        	document.documentElement ? document.getElementsByTagName('body')[0].clientWidth : /* IE 6+ (Standards Compliant Mode) */
            document.body ? document.body.clientWidth : /* IE 4 Compatible */
            window.screen.width; /* Others (It is not browser window size, but screen size) */
	}

	this.getClientHeight = function()
	{
    	return window.innerHeight ? window.innerHeight : /* For non-IE */
        	document.documentElement ? document.getElementsByTagName('body')[0].clientHeight : /* IE 6+ (Standards Compliant Mode) */
            document.body ? document.body.clientHeight : /* IE 4 Compatible */
            window.screen.height; /* Others (It is not browser window size, but screen size) */
	}
	///UserCodeRegionEnd: (do not remove this comment.):
}
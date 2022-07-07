function Timer()
{
	this.Interval;
	this.Enabled;

	var loaded = false;
	var intervalId = null;
	this.start = function()
	{
		///UserCodeRegionStart:[start] (do not remove this comment.)
		if (!loaded && this.isEnabled())
		{
			loaded = true;
			
			var control = this;
			intervalId = window.setInterval(function(){
					if (control.isEnabled() && control.Elapsed)
					{
						control.Elapsed();
					}
				},
				this.Interval
			);
		}
		
		if (!this.isEnabled() && intervalId)
		{
			window.clearInterval(intervalId);
			loaded = false;
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	
	this.isEnabled = function()
	{
		return gx.lang.gxBoolean(this.Enabled);
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)


	
	
	
	
	
	
	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

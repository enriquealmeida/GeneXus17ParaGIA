function reCAPTCHA()
{
	this.Theme;
	this.PublicKey;
	this.reCaptchaData;

	// Databinding for property reCaptchaData
	this.SetreCaptchaData = function(data)
	{
		///UserCodeRegionStart:[SetreCaptchaData] (do not remove this comment.)
		this.reCaptchaData = data;
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property reCaptchaData
	this.GetreCaptchaData = function()
	{
		///UserCodeRegionStart:[GetreCaptchaData] (do not remove this comment.)
		
		this.reCaptchaData.Challenge = Recaptcha.get_challenge()
		this.reCaptchaData.Response = Recaptcha.get_response();
		return this.reCaptchaData;
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		var buffer = "<div id=\"" + this.ContainerName + "_div\"></div>";
		this.setHtml(buffer);
		
		Recaptcha.create(this.PublicKey,
    	this.ContainerName + "_div",
    {
      theme: this.Theme,
      callback: Recaptcha.focus_response_field
    });
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

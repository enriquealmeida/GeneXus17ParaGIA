function GoogleAnalytics() {
	this.Dimensions = null;
	
	this.GetDimensions = function () {
		return this.Dimensions;
	};

	this.SetDimensions = function (dimensions) {
		this.Dimensions = dimensions;
	};

	this.show = function() {
			///UserCodeRegionStart:[show] (do not remove this comment.)
			(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
          (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
          m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
          })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
			var cookieDomain = (this.DomainName != '<yourDomainNameHere>')? this.DomainName: 'auto';
			var gaOpts = {
				'trackingId': this.Code,
				'cookieDomain': cookieDomain,
				'allowLinker': this.AllowLinker
			};
			ga('create',  gaOpts);
			ga('set', 'dimension1', gx.gxVersion);

			// Send custom dimensions
			if (this.Dimensions) {
				ga('send', 'pageview', this.Dimensions);
			}
			else {
				ga('send', 'pageview');
			}

			gx.evt.attach(window, 'error', function(messageOrEvent, source, lineno, colno, error) {
				if (typeof(messageOrEvent) !== 'string' && messageOrEvent.error) {
					msg = messageOrEvent.error.message + ' - ' + messageOrEvent.error.stack || ''
				}
				ga('send', 'exception', {
					'exDescription': msg,
					'exFatal': true
				});
				return false;
			})
			///UserCodeRegionEnd: (do not remove this comment.)
		}
		///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	///UserCodeRegionEnd: (do not remove this comment.):
}
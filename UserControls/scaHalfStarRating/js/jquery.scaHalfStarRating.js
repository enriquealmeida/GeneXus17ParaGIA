// JavaScript Document
/*************************************************
Inspiration: Will Stuckey's star rating system (http://sandbox.wilstuckey.com/jquery-ratings/) - Half-Star Addition: Karl Swedberg - Ritesh Agrawal (http://php.scripts.psu.edu/rja171/widgets/rating.php)
************************************************/

jQuery.fn.rating = function(render) {
	var settings = {
    increment : (render.HalfStar) ? 1 : 1,//.5, 
    maxvalue  : (render.HalfStar)?(render.Stars)*2:render.Stars,
    curvalue  : render.Attribute,    // number of selected stars
	enabled : render.Enabled,
	cancel: render.ClearButton
  };
	

  var container = jQuery(this);
	
	jQuery.extend(container, {
    averageRating: settings.curvalue,
    url: settings.url
  });
  
  var s = 0;
	for(var i= 0; i <= settings.maxvalue ; i++){
    if (i == 0) {
	    if((settings.cancel) && (settings.enabled)){
        var div = '<div class="cancel"><a href="#0" title="Cancel Rating">Cancel Rating</a></div>';
        container.empty().append(div);
      }
    } else {
	if(settings.enabled)
	{
      var $div = $('<div class="star"></div>').append('<a href="javascript: void(0)">'+i+'</a>').appendTo(container);
	}
	else
	{
      var $div = $('<div class="star"></div>').appendTo(container);	  
	}
      if (render.HalfStar) {
        if (s%2) {
          $div.addClass('star-left');
        } else {
          $div.addClass('star-right');
        }
      }
    }
    i=i-1+settings.increment;
    s++;
  }
	
	var stars = jQuery(container).children('.star');
  var cancel = jQuery(container).children('.cancel');
	
  stars
    .mouseover(function(){
	  if (settings.enabled)
	  {
      event.drain();
      event.fill(this);
	  if (render.MouseOver) {
	  render.MouseOverValue = this.textContent;
	  render.MouseOver() }
	 } 
    })
    .mouseout(function(){
	 if (settings.enabled)
	  {
      event.drain();
      event.reset();
	 if (render.MouseOut) {
	  render.MouseOverValue = '0';
	  render.MouseOut() }
	  }
    })
    .focus(function(){
	 if (settings.enabled)
	  {
      event.drain();
      event.fill(this);
	  }
    })
    .blur(function(){
	 if (settings.enabled)
	  {
      event.drain();
      event.reset();
	  }
    });

  stars.click(function(){
   if (settings.enabled)
	  {
		render.Attribute = (stars.index(this) * settings.increment) + settings.increment;
		settings.curvalue = render.Attribute;
		if(settings.cancel == true){
			render.Attribute = (stars.index(this) * settings.increment) + settings.increment;
			settings.curvalue = render.Attribute;
			if (render.Click) {
				render.Click();
			}
			return false;
		} else if (settings.maxvalue == 1) {
			render.Attribute = (render.Attribute == 0) ? 1 : 0;
			settings.curvalue = render.Attribute;
			$(this).toggleClass('on');
			if (render.Click) {
				render.Click();
				}	
			return false;
		}
		 if (render.Click) {
			render.Click();
			}
		event.drain();
		return true;
		}
  });



  // cancel button events
	if(cancel){
    cancel
    .mouseover(function(){
      event.drain();
      jQuery(this).addClass('on');
    })
    .mouseout(function(){
      event.reset();
      jQuery(this).removeClass('on');
    })
    .focus(function(){
      event.drain();
      jQuery(this).addClass('on');
    })
    .blur(function(){
      event.reset();
      jQuery(this).removeClass('on');
    });
      
    // click events.
    cancel.click(function(){
      event.drain();
      settings.curvalue = 0;
	  if (render.Clear) {
	  render.Clear();
	  }
      return false;
    });
  }
        
	var event = {
		fill: function(el){ // fill to the current mouse position.
			var index = stars.index(el) + 1;
			stars
				.children('a').css('width', '100%').end()
				.slice(0,index).addClass('hover').end();
		},
		drain: function() { // drain all the stars.
			stars
				.filter('.on').removeClass('on').end()
				.filter('.hover').removeClass('hover').end();
		},
		reset: function(){ // Reset the stars to the default index.
			stars.slice(0,settings.curvalue / settings.increment).addClass('on').end();
		}
	};    
	event.reset();
	
	return(this);	

};
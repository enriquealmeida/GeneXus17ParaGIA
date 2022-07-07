//** Smooth Navigational Menu- By Dynamic Drive DHTML code library: http://www.dynamicdrive.com
//** Script Download/ instructions page: http://www.dynamicdrive.com/dynamicindex1/ddlevelsmenu/
//** Menu created: Nov 12, 2008

//** Dec 12th, 08" (v1.01): Fixed Shadow issue when multiple LIs within the same UL (level) contain sub menus: http://www.dynamicdrive.com/forums/showthread.php?t=39177&highlight=smooth

//** Feb 11th, 09" (v1.02): The currently active main menu item (LI A) now gets a CSS class of ".selected", including sub menu items.

//** May 1st, 09" (v1.3):
//** 1) Now supports vertical (side bar) menu mode- set "orientation" to 'v'
//** 2) In IE6, shadows are now always disabled

//** July 27th, 09" (v1.31): Fixed bug so shadows can be disabled if desired.
//** Feb 2nd, 10" (v1.4): Adds ability to specify delay before sub menus appear and disappear, respectively. See showhidedelay variable below

//** Dec 17th, 10" (v1.5): Updated menu shadow to use CSS3 box shadows when the browser is FF3.5+, IE9+, Opera9.5+, or Safari3+/Chrome. Only .js file changed.

var ddsmoothmenu={

//Specify full URL to down and right arrow images (23 is padding-right added to top level LIs with drop downs):
arrowimages: {down:['downarrowclass', gx.util.resourceUrl(gx.basePath + gx.staticDirectory + 'SmoothNavMenu/resources/images/down.gif'), 23], right:['rightarrowclass', gx.util.resourceUrl(gx.basePath + gx.staticDirectory + 'SmoothNavMenu/resources/images/right.gif')], arrows:true},
transition: {overtime:300, outtime:300}, //duration of slide in/ out animation, in milliseconds
shadow: { enable: (gx.util.browser.isIE()) ? false : true, offsetx: 5, offsety: 5 }, //enable shadow?
showhidedelay: {showdelay: 100, hidedelay: 200}, //set delay in milliseconds before sub menus appear and disappear, respectively

///////Stop configuring beyond here///////////////////////////

detectwebkit: navigator.userAgent.toLowerCase().indexOf("applewebkit")!=-1, //detect WebKit browsers (Safari, Chrome etc)
detectie6: document.all && !window.XMLHttpRequest,
css3support: window.msPerformance || (!document.all && document.querySelector), //detect browsers that support CSS3 box shadows (ie9+ or FF3.5+, Safari3+, Chrome etc)

getajaxmenu:function($, setting){ //function to fetch external page containing the panel DIVs
	var $menucontainer=$('#'+setting.contentsource[0]) //reference empty div on page that will hold menu
	$menucontainer.html("Loading Menu...")
	$.ajax({
		url: setting.contentsource[1], //path to external menu file
		async: true,
		error:function(ajaxrequest){
			$menucontainer.html('Error fetching content. Server Response: '+ajaxrequest.responseText)
		},
		success:function(content){
			$menucontainer.html(content)
			ddsmoothmenu.buildmenu($, setting)
		}
	})
},


buildmenu:function($, setting){
	var smoothmenu=ddsmoothmenu
	var $mainmenu=$("#"+setting.mainmenuid+">ul") //reference main menu UL
	$mainmenu.parent().get(0).className=setting.classname || "ddsmoothmenu"
	var $headers=$mainmenu.find("ul").parent()
	$headers.hover(
		function(e){
			$(this).children('a:eq(0)').addClass('selected')
		},
		function(e){
			$(this).children('a:eq(0)').removeClass('selected')
		}
	)
	$headers.each(function(i){ //loop through each LI header
		var $curobj=$(this).css({zIndex: 100-i}) //reference current LI header
		var $subul=$(this).find('ul:eq(0)').css({display:'block'})
		$subul.data('timers', {})
		this._dimensions={w:this.offsetWidth, h:this.offsetHeight, subulw:$subul.outerWidth(), subulh:$subul.outerHeight()}
		this.istopheader=$curobj.parents("ul").length==1? true : false //is top level header?
		$subul.css({top:this.istopheader && setting.orientation!='v'? this._dimensions.h+"px" : 0})
		if (smoothmenu.arrowimages.arrows){
			$curobj.children("a:eq(0)").css(this.istopheader? {paddingRight: smoothmenu.arrowimages.down[2]} : {}).append( //add arrow images
				'<img src="'+ (this.istopheader && setting.orientation!='v'? smoothmenu.arrowimages.down[1] : smoothmenu.arrowimages.right[1])
				+'" class="' + (this.istopheader && setting.orientation!='v'? smoothmenu.arrowimages.down[0] : smoothmenu.arrowimages.right[0])
				+ '" style="border:0;" />'
			)
		}
		if (smoothmenu.shadow.enable && !smoothmenu.css3support){ //if shadows enabled and browser doesn't support CSS3 box shadows
			this._shadowoffset={x:(this.istopheader?$subul.offset().left+smoothmenu.shadow.offsetx : this._dimensions.w), y:(this.istopheader? $subul.offset().top+smoothmenu.shadow.offsety : $curobj.position().top)} //store this shadow's offsets
			if (this.istopheader)
				$parentshadow=$(document.body)
			else{
				var $parentLi=$curobj.parents("li:eq(0)")
				$parentshadow=$parentLi.get(0).$shadow
			}
			this.$shadow=$('<div class="ddshadow'+(this.istopheader? ' toplevelshadow' : '')+'"></div>').prependTo($parentshadow).css({left:this._shadowoffset.x+'px', top:this._shadowoffset.y+'px'})  //insert shadow DIV and set it to parent node for the next shadow div
		}
		$curobj.hover(
			function(e){
				var $targetul=$subul //reference UL to reveal
				var header=$curobj.get(0) //reference header LI as DOM object
				clearTimeout($targetul.data('timers').hidetimer)
				$targetul.data('timers').showtimer=setTimeout(function(){
					header._offsets={left:$curobj.offset().left, top:$curobj.offset().top}
					var menuleft=header.istopheader && setting.orientation!='v'? 0 : header._dimensions.w
					menuleft=(header._offsets.left+menuleft+header._dimensions.subulw>$(window).width())? (header.istopheader && setting.orientation!='v'? -header._dimensions.subulw+header._dimensions.w : -header._dimensions.w) : menuleft //calculate this sub menu's offsets from its parent
					if ($targetul.queue().length<=1){ //if 1 or less queued animations
						$targetul.css({left:menuleft+"px", width:header._dimensions.subulw+'px'}).animate({height:'show',opacity:'show'}, ddsmoothmenu.transition.overtime)
						if (smoothmenu.shadow.enable && !smoothmenu.css3support){
							var shadowleft=header.istopheader? $targetul.offset().left+ddsmoothmenu.shadow.offsetx : menuleft
							var shadowtop=header.istopheader?$targetul.offset().top+smoothmenu.shadow.offsety : header._shadowoffset.y
							if (!header.istopheader && ddsmoothmenu.detectwebkit){ //in WebKit browsers, restore shadow's opacity to full
								header.$shadow.css({opacity:1})
							}
							header.$shadow.css({overflow:'', width:header._dimensions.subulw+'px', left:shadowleft+'px', top:shadowtop+'px'}).animate({height:header._dimensions.subulh+'px'}, ddsmoothmenu.transition.overtime)
						}
					}
				}, ddsmoothmenu.showhidedelay.showdelay)
			},
			function(e){
				var $targetul=$subul
				var header=$curobj.get(0)
				clearTimeout($targetul.data('timers').showtimer)
				$targetul.data('timers').hidetimer=setTimeout(function(){
					$targetul.animate({height:'hide', opacity:'hide'}, ddsmoothmenu.transition.outtime)
					if (smoothmenu.shadow.enable && !smoothmenu.css3support){
						if (ddsmoothmenu.detectwebkit){ //in WebKit browsers, set first child shadow's opacity to 0, as "overflow:hidden" doesn't work in them
							header.$shadow.children('div:eq(0)').css({opacity:0})
						}
						header.$shadow.css({overflow:'hidden'}).animate({height:0}, ddsmoothmenu.transition.outtime)
					}
				}, ddsmoothmenu.showhidedelay.hidedelay)
			}
		) //end hover
	}) //end $headers.each()
	if (smoothmenu.shadow.enable && smoothmenu.css3support){ //if shadows enabled and browser supports CSS3 shadows
		var $toplevelul=$('#'+setting.mainmenuid+' ul li ul')
		var css3shadow=parseInt(smoothmenu.shadow.offsetx)+"px "+parseInt(smoothmenu.shadow.offsety)+"px 5px #aaa" //construct CSS3 box-shadow value
		var shadowprop=["boxShadow", "MozBoxShadow", "WebkitBoxShadow", "MsBoxShadow"] //possible vendor specific CSS3 shadow properties
		for (var i=0; i<shadowprop.length; i++){
			$toplevelul.css(shadowprop[i], css3shadow)
		}
	}
	$mainmenu.find("ul").css({display:'none', visibility:'visible'})
},

init:function(setting){
	if (typeof(setting.customtheme)=="object" && setting.customtheme.length>=2){ //override default menu styles
		var mainmenuid='#'+setting.mainmenuid
		var mainselector=(setting.orientation=="v")? mainmenuid : mainmenuid+', '+mainmenuid
		
		if (setting.customtheme[20] === "Default"){
			if (setting.orientation!="v"){
				setting.customtheme[20]="8px"
			} else {
				setting.customtheme[20]="6px"
			}
		}
		
		if (setting.customtheme[21] === "Default"){
			if (setting.orientation!="v"){
				setting.customtheme[21]="5px"
			} else {
				setting.customtheme[21]="6px"
			}
		}
		
		ddsmoothmenu.arrowimages.arrows = setting.customtheme[26];
		
		if (setting.customtheme[18] != ""){
			ddsmoothmenu.arrowimages.down[1] = gx.util.resourceUrl(setting.customtheme[18]);
		}
		if (setting.customtheme[19] != ""){
			ddsmoothmenu.arrowimages.right[1] = gx.util.resourceUrl(setting.customtheme[19]);
		}
		
		var backgroundMain = setting.customtheme[0];
		if (setting.customtheme[17] != ""){
			backgroundMain = 'url(' + gx.util.resourceUrl(setting.customtheme[17]) +') repeat-x scroll 0 50% ' + setting.customtheme[0]
		}
		
		if (setting.orientation!="v"){
			$('<style type="text/css">\n'
				+ mainmenuid + '{background:' + backgroundMain + ' !important; }\n'
			+ '</style>').appendTo("head");
		}
		
		var cssHeader = 'font-size: 14px; font-weight: bold; font-family: Verdana;  font-style: normal'; //(setting.customtheme[24] != undefined) ? setting.customtheme[24] : 'font-size: 14px; font-weight: bold; font-family: Verdana;  font-style: normal';
		var cssItems  = 'font-size: 13px; font-weight: normal; font-family: Verdana;  font-style: normal'; //(setting.customtheme[25] != undefined) ? setting.customtheme[25] : 'font-size: 13px; font-weight: normal; font-family: Verdana;  font-style: normal'; 
		
		
		$('<style type="text/css">\n' 
			//Header Properties 
			+ mainmenuid + ' a.smoothHeader {background:' + backgroundMain +'; border-color: '+ setting.customtheme[4] +'; color: '+ setting.customtheme[5] +
					'; ' + cssHeader 
					+'; padding-top: '+ setting.customtheme[20] +'; padding-bottom: '+ setting.customtheme[20] +';}\n'  
			+ mainmenuid + ' a.smoothHeader:hover {color:'+ setting.customtheme[6] +'; background: '+ setting.customtheme[1] +';}\n'
			+ mainmenuid + ' a.smoothHeader.selected {color: '+ setting.customtheme[6] +'; background: '+ setting.customtheme[1] +';}\n'
			//Items Properties
			+ mainmenuid + ' a.smoothItem {background-color: '+ setting.customtheme[2] +'; border-color: '+ setting.customtheme[4] +'; color: '+ setting.customtheme[10] +'; '+
			  	cssItems	 
			  	+';  padding-top: '+ setting.customtheme[21] +'; padding-bottom: '+ setting.customtheme[21] +';}\n'  
			+ mainmenuid + ' a.smoothItem:hover {color: '+ setting.customtheme[11] +'; background-color: '+ setting.customtheme[3] +';}\n'
			
			+ '</style>').appendTo("head");
		
			
	}
	this.shadow.enable=(document.all && !window.XMLHttpRequest)? false : this.shadow.enable //in IE6, always disable shadow
	jQuery(document).ready(function($){ //ajax menu?
		if (typeof setting.contentsource=="object"){ //if external ajax menu
			ddsmoothmenu.getajaxmenu($, setting)
		}
		else{ //else if markup menu
			ddsmoothmenu.buildmenu($, setting)
		}
		if(setting.orientation!="v"){
			jQuery(mainmenuid).css({height: jQuery(mainmenuid + ' a.smoothHeader')[0].offsetHeight+"px"});
			for(var i=0; i < jQuery(mainmenuid + ' img.downarrowclass').length; i++){
				if (setting.customtheme[18] != ""){
					jQuery(mainmenuid + ' img.downarrowclass')[i].setAttribute("src", gx.util.resourceUrl(setting.customtheme[18]));
				} else {
					jQuery(mainmenuid + ' img.downarrowclass')[i].setAttribute("src", gx.util.resourceUrl(gx.basePath + gx.staticDirectory + 'SmoothNavMenu/resources/images/down.gif') );
				}
			}
			for(var i=0; i < jQuery(mainmenuid + ' img.rightarrowclass').length; i++){
				if (setting.customtheme[19] != ""){
					jQuery(mainmenuid + ' img.rightarrowclass')[i].setAttribute("src", gx.util.resourceUrl(setting.customtheme[19]));
				} else {
					jQuery(mainmenuid + ' img.rightarrowclass')[i].setAttribute("src", gx.util.resourceUrl(gx.basePath + gx.staticDirectory + 'SmoothNavMenu/resources/images/right.gif') );
				}
			}
		}
		
		if (setting.orientation!="v"){
			var itrl = setTimeout( function() {
				jQuery(mainmenuid).css( {height: jQuery(mainmenuid + " .smoothHeader")[0].clientHeight + "px"} );
				}, 300)
		}
		
	})
}

} //end ddsmoothmenu variable

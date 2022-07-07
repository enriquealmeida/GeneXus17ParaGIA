// -------------------------------------------------------------------
// DHTML Window Widget- By Dynamic Drive, available at: http://www.dynamicdrive.com
// v1.0: Script created Feb 15th, 07'
// v1.01: Feb 21th, 07' (see changelog.txt)
// v1.02: March 26th, 07' (see changelog.txt)
// v1.03: May 5th, 07' (see changelog.txt)
// -------------------------------------------------------------------


var dhtmlwindow={
ajaxbustcache: true, //Bust caching when fetching a file via Ajax?
minimizeorder: 0,
tobjects: [], //object to contain references to dhtml window divs, for cleanup purposes
hideBool: true,

init:function(t, hasTitle, titleFC, titleBC){
	var domwindow=document.createElement("div") //create dhtml window div
	domwindow.id=t
	domwindow.className="dhtmlwindow"
	if (domwindow.addEventListener)
			domwindow.addEventListener('click', setClick2, false)
	else
			domwindow.attachEvent('onclick',setClick2)
	var domwindowdata=''

	domwindowdata='<div id="wtitle" class="drag-handle" style="background-color:#FFFFFF; color:#FFFFFF; display=none">'		
  domwindowdata+='* <div></div>'
	domwindowdata+='</div>'
	domwindowdata+='<div  class="drag-contentarea"></div>'
	domwindow.innerHTML=domwindowdata
	document.getElementById("dhtmlwindowholder").appendChild(domwindow)
	this.zIndexvalue=(this.zIndexvalue)? this.zIndexvalue+1 : 100 //z-index value for DHTML window: starts at 0, increments whenever a window has focus
	var t=document.getElementById(t)
	var divs=t.getElementsByTagName("div")
	for (var i=0; i<divs.length; i++){ //go through divs inside dhtml window and extract all those with class="drag-" prefix
		if (/drag-/.test(divs[i].className))
			t[divs[i].className.replace(/drag-/, "")]=divs[i] //take out the "drag-" prefix for shorter access by name
	}
	t.style.zIndex=this.zIndexvalue //set z-index of this dhtml window
	t.handle._parent=t //store back reference to dhtml window 
	t.onclose=function(){return true} //custom event handler "onclose"
	t.onmousedown=function(){dhtmlwindow.zIndexvalue++; this.style.zIndex=dhtmlwindow.zIndexvalue} //Increase z-index of window when focus is on it
	t.show=function(){dhtmlwindow.show(this)} //public function for showing dhtml window
	t.hide=function(){dhtmlwindow.close(this)} //public function for hiding dhtml window
	t.setClick=function(r){dhtmlwindow.setClick(this, r)} //public function for hiding dhtml window
	t.hideBool=true
	t.setSize=function(w, h){dhtmlwindow.setSize(this, w, h)} //public function for setting window dimensions
	t.moveTo=function(x, y){dhtmlwindow.moveTo(this, x, y)} //public function for moving dhtml window (relative to viewpoint)
	t.isResize=function(bol){dhtmlwindow.isResize(this, bol)} //public function for specifying if window is resizable
	t.isScrolling=function(bol){dhtmlwindow.isScrolling(this, bol)} //public function for specifying if window content contains scrollbars
	t.load=function(contenttype, contentsource, hasTitle, title, titleFC, titleBC){dhtmlwindow.load(this, contenttype, contentsource, hasTitle, title, titleFC, titleBC)} //public function for loading content into window
	this.tobjects[this.tobjects.length]=t
	return t //return reference to dhtml window div
},

open:function(t, contenttype, contentsource, effect, hasTitle, title, titleFC, titleBC, attr, recalonload, ctrl, posit, vert, horiz, voffset, hoffset){


	var d=dhtmlwindow //reference dhtml window object
	function getValue(Name){
		var config=new RegExp(Name+"=([^,]+)", "i") //get name/value config pair (ie: width=400px,)
		return (config.test(attr))? parseInt(RegExp.$1) : 0 //return value portion (int), or 0 (false) if none found
	}
	if (document.getElementById(t)==null) //if window doesn't exist yet, create it
		t=this.init(t, hasTitle, titleFC, titleBC) //return reference to dhtml window div
	else
		t=document.getElementById(t)
if (t.hideBool != false) {
	t.setSize(getValue(("width")), (getValue("height"))) //Set dimensions of window
	var xpos
	var ypos
	if (typeof recalonload!="undefined" && recalonload=="recal" && this.scroll_top==0){ //reposition window when page fully loads with updated window viewpoints?
		if (window.attachEvent && !window.opera) //In IE, add another 400 milisecs on page load (viewpoint properties may return 0 b4 then)
			this.addEvent(window, function(){setTimeout(function(){t.moveTo(xpos, ypos)}, 400)}, "load")
		else
			this.addEvent(window, function(){t.moveTo(xpos, ypos)}, "load")
	}
	t.isResize(getValue("resize")) //Set whether window is resizable
	t.isScrolling(getValue("scrolling")) //Set whether window should contain scrollbars
	t.style.display="block"	
	t.load(contenttype, contentsource, hasTitle, title, titleFC, titleBC)
	
	if (posit == "Fixed"){
		xpos = getValue("center")? "middle" : getValue("left") //Get x coord of window
		ypos = getValue("center")? "middle" : getValue("top") //Get y coord of window
	}
	else {		
		var im = document.getElementById(ctrl)
		ypos = getRealTop(im, vert, getValue("height"), voffset)
		xpos = getRealLeft(im, horiz, getValue("width"), hoffset)
	}
  
  t.moveTo(xpos, ypos) //Position window
  
	t.contentarea.style.display="block"
	t.style.filter = "revealTrans(duration=1)"
	
	if (document.all && effect < 98) {		
		t.filters.revealTrans.apply()
		t.style.visibility="visible"
		t.filters.revealTrans.transition=effect
		t.filters.revealTrans.Play()
	}
	else {
		if (effect != 99)
		{
			changeOpac(0,"ajaxbox")
			t.style.visibility="visible"
	  	opacity("ajaxbox",500)
	  }
	  else {
	  	t.style.visibility="visible"	
	  }
	}
	}
	return t

},

setSize:function(t, w, h){ //set window size (min is 150px wide by 100px tall)
	t.style.width=parseInt(w)+"px"
	t.contentarea.style.height=parseInt(h)+"px"
},

moveTo:function(t, x, y){ //move window. Position includes current viewpoint of document
	this.getviewpoint() //Get current viewpoint numbers
	t.style.left=(x=="middle")? this.scroll_left+(this.docwidth-t.offsetWidth)/2+"px" : this.scroll_left+parseInt(x)+"px"
	t.style.top=(y=="middle")? this.scroll_top+(this.docheight-t.offsetHeight)/2+"px" : this.scroll_top+parseInt(y)+"px"
},

isResize:function(t, bol){ //show or hide resize inteface (part of the status bar)
	t.resizeBool=(bol)? 1 : 0
},

isScrolling:function(t, bol){ //set whether loaded content contains scrollbars
	t.contentarea.style.overflow=(bol)? "auto" : "hidden"
},

load:function(t, contenttype, contentsource, hasTitle, title, titleFC, titleBC){ //loads content into window plus set its title (3 content types: "inline", "iframe", or "ajax")
	var contenttype=contenttype.toLowerCase() //convert string to lower case
	if (hasTitle == "Yes" && typeof title!="undefined")
	{	
		document.getElementById("wtitle").style.display = "block"
		document.getElementById("wtitle").style.color = titleFC
		document.getElementById("wtitle").style.backgroundColor = titleBC
		t.handle.firstChild.nodeValue=title
	}
	else
		document.getElementById("wtitle").style.display = "none"
	if (contenttype=="inline")
		t.contentarea.innerHTML=contentsource
	else if (contenttype=="div"){
		t.contentarea.innerHTML=document.getElementById(contentsource).innerHTML //Populate window with contents of specified div on page
		document.getElementById(contentsource).style.display="none" //hide that div
	}
	else if (contenttype=="iframe"){
		t.contentarea.style.overflow="hidden" //disable window scrollbars, as iframe already contains scrollbars
		if (!t.contentarea.firstChild || t.contentarea.firstChild.tagName!="IFRAME") //If iframe tag doesn't exist already, create it first
			t.contentarea.innerHTML='<iframe src="" style="margin:0; padding:0; width:100%; height: 100%" name="_iframe-'+t.id+'"></iframe>'
		window.frames["_iframe-"+t.id].location.replace(contentsource) //set location of iframe window to specified URL
		}
	else if (contenttype=="ajax"){
		this.ajax_connect(contentsource, t) //populate window with external contents fetched via Ajax
	}
	t.contentarea.datatype=contenttype //store contenttype of current window for future reference
},

getviewpoint:function(){ //get window viewpoint numbers
	var ie=document.all && !window.opera
	var domclientWidth=document.documentElement && parseInt(document.documentElement.clientWidth) || 100000 //Preliminary doc width in non IE browsers
	this.standardbody=(document.compatMode=="CSS1Compat")? document.documentElement : document.body //create reference to common "body" across doctypes
	this.scroll_top=(ie)? this.standardbody.scrollTop : window.pageYOffset
	this.scroll_left=(ie)? this.standardbody.scrollLeft : window.pageXOffset
	this.docwidth=(ie)? this.standardbody.clientWidth : (/Safari/i.test(navigator.userAgent))? window.innerWidth : Math.min(domclientWidth, window.innerWidth-16)
	this.docheight=(ie)? this.standardbody.clientHeight: window.innerHeight
},

rememberattrs:function(t){ //remember certain attributes of the window when it's minimized or closed, such as dimensions, position on page
	this.getviewpoint() //Get current window viewpoint numbers
	t.lastx=parseInt((t.style.left || t.offsetLeft))-dhtmlwindow.scroll_left //store last known x coord of window just before minimizing
	t.lasty=parseInt((t.style.top || t.offsetTop))-dhtmlwindow.scroll_top
	t.lastwidth=parseInt(t.style.width) //store last known width of window just before minimizing/ closing
},

minimize:function(button, t){
	dhtmlwindow.rememberattrs(t)
	button.setAttribute("src", dhtmlwindow.imagefiles[2])
	button.setAttribute("title", "Restore")
	t.state="minimized" //indicate the state of the window as being "minimized"
	t.contentarea.style.display="none"
	if (typeof t.minimizeorder=="undefined"){ //stack order of minmized window on screen relative to any other minimized windows
		dhtmlwindow.minimizeorder++ //increment order
		t.minimizeorder=dhtmlwindow.minimizeorder
	}
	t.style.left="10px" //left coord of minmized window
	t.style.width="200px"
	var windowspacing=t.minimizeorder*10 //spacing (gap) between each minmized window(s)
	t.style.top=dhtmlwindow.scroll_top+dhtmlwindow.docheight-(t.handle.offsetHeight*t.minimizeorder)-windowspacing+"px"
},

restore:function(button, t){
	dhtmlwindow.getviewpoint()
	button.setAttribute("src", dhtmlwindow.imagefiles[0])
	button.setAttribute("title", "Minimize")
	t.state="fullview" //indicate the state of the window as being "fullview"
	t.style.display="block"
	t.contentarea.style.display="block"
	if (t.resizeBool) //if this window is resizable, enable the resize icon
	t.style.left=parseInt(t.lastx)+dhtmlwindow.scroll_left+"px" //position window to last known x coord just before minimizing
	t.style.top=parseInt(t.lasty)+dhtmlwindow.scroll_top+"px"
	t.style.width=parseInt(t.lastwidth)+"px"
},

setClick:function(t, r) {
if (r == "Yes") {
	if (t.hideBool != false) {
		if (t.style.visibility == "visible")	
		 t.hideBool = false;
	}
	else {
		 t.hideBool = true
		 if (t.style.visibility == "visible")
		 		t.hide()		 		
	}
}
},

close:function(t){
	if (t.hideBool != false) {
	try{
		var closewinbol=t.onclose()
	}
	catch(err){ //In non IE browsers, all errors are caught, so just run the below
		var closewinbol=true
 }
	finally{ //In IE, not all errors are caught, so check if variable isn't defined in IE in those cases
		if (typeof closewinbol=="undefined"){
			alert("An error has occured somwhere inside your \"onclose\" event handler")
			var closewinbol=true
		}
	}
	if (closewinbol){ //if custom event handler function returns true
		if (t.state!="minimized") //if this window isn't currently minimized
			dhtmlwindow.rememberattrs(t) //remember window's dimensions/position on the page before closing
		t.style.display="none"
		t.contentarea.innerHTML=""
		t.style.visibility="hidden"
	}
	return closewinbol
}
},

show:function(t){
	if (t.lastx) //If there exists previously stored information such as last x position on window attributes (meaning it's been minimized or closed)
		dhtmlwindow.restore(t.controls.firstChild, t) //restore the window using that info
	else
		t.style.display="block"
	t.state="fullview" //indicate the state of the window as being "fullview"
},

ajax_connect:function(url, t){
	var page_request = false
	var bustcacheparameter=""
	if (window.XMLHttpRequest) // if Mozilla, IE7, Safari etc
		page_request = new XMLHttpRequest()
	else if (window.ActiveXObject){ // if IE6 or below
		try {
		page_request = new ActiveXObject("Msxml2.XMLHTTP")
		} 
		catch (e){
			try{
			page_request = new ActiveXObject("Microsoft.XMLHTTP")
			}
			catch (e){}
		}
	}
	else
		return false
	page_request.onreadystatechange=function(){dhtmlwindow.ajax_loadpage(page_request, t)}
	page_request.open('GET', url, true)
	page_request.send(null)
},

ajax_loadpage:function(page_request, t){
	if (page_request.readyState == 4 && (page_request.status==200 || window.location.href.indexOf("http")==-1)){
	t.contentarea.innerHTML=page_request.responseText
	}
},

stop:function(){
	dhtmlwindow.etarget=null //clean up
	document.onmousemove=null
	document.onmouseup=null
},

addEvent:function(target, functionref, tasktype){ //assign a function to execute to an event handler (ie: onunload)
	var tasktype=(window.addEventListener)? tasktype : "on"+tasktype
	if (target.addEventListener)
		target.addEventListener(tasktype, functionref, false)
	else if (target.attachEvent)
		target.attachEvent(tasktype, functionref)
},

cleanup:function(){
	for (var i=0; i<dhtmlwindow.tobjects.length; i++){
		dhtmlwindow.tobjects[i].handle._parent=dhtmlwindow.tobjects[i].resizearea._parent=dhtmlwindow.tobjects[i].controls._parent=null
	}
	window.onload=null
}

} //End dhtmlwindow object

function opacity(id, millisec) { 
    //speed for each frame 
    var speed = Math.round(millisec / 100); 
    var timer = 0; 

    //determine the direction for the blending, if start and end are the same nothing happens   
        for(i = 0; i <= 100; i++) 
            { 
            setTimeout("changeOpac(" + i + ",'" + id + "')",(timer * speed)); 
            timer++; 
        }     
} 

function setClick2() {
	var t1=document.getElementById('ajaxbox')
	if (t1.hideBool != false) {
		t1.hide()			 
	}
	else {
		 t1.hideBool = true
		 if (t1.style.visibility == "visible")
		 		t1.hide()		 		
	}
}

//change the opacity for different browsers 
function changeOpac(opacity, id) { 
    var object = document.getElementById(id).style; 
    object.opacity = (opacity / 100); 
    object.MozOpacity = (opacity / 100); 
    object.KhtmlOpacity = (opacity / 100); 
    object.filter = "alpha(opacity=" + opacity + ")"; 
} 
function getRealLeft(imgElem, horiz, xdiv, hoffset) {
	var x
	if (document.all || !imgElem.x) {
	 	x = imgElem.offsetLeft;
		tempEl = imgElem.offsetParent;
  	while (tempEl != null) {
  		x += tempEl.offsetLeft;
  		tempEl = tempEl.offsetParent;
  	}
  }
  else
  	x = imgElem.x
  
  if (horiz == "Right")
  	x = x + imgElem.offsetWidth + parseInt(hoffset)
  if (horiz == "AlignRight")
  	x = x + imgElem.offsetWidth - parseInt(xdiv)
  if (horiz == "Centered")
  	x = x + (imgElem.offsetWidth / 2) - (parseInt(xdiv) / 2)
  if (horiz == "Left") 
  	x = x - parseInt(xdiv) - parseInt(hoffset)
  	
  x = x - document.body.scrollLeft
	return x;
}
function getRealTop(imgElem, vert, ydiv, voffset) {
	
	var y
	if (document.all || !imgElem.y) {
		y = imgElem.offsetTop;
		tempEl = imgElem.offsetParent;
		while (tempEl != null) {			
  		y += tempEl.offsetTop;
  		tempEl = tempEl.offsetParent;
  	}
  }
  else
  	y = imgElem.y
  	
  var ypos1 = 0
  if (document.getElementById("wtitle").style.display == "block")
		if (document.all)
  			ypos1 = document.getElementById("wtitle").offsetHeight  
  	else
  			ypos1 = document.getElementById("wtitle").clientHeight
  
  if (vert == "Above")
  	y = y - parseInt(ydiv) - parseInt(voffset) - ypos1
  if (vert == "AlignBottom")
  	y = y + imgElem.offsetHeight - parseInt(ydiv) - ypos1
  if (vert == "Centered")
  	y = y + (imgElem.offsetHeight / 2) - ((parseInt(ydiv)+ypos1) / 2)
  if (vert == "Under")
  	y = y + imgElem.offsetHeight + parseInt(voffset)
/*  if (vert == "AlignTop")
  	y = y + ypos1*/
  	
  y = y - document.body.scrollTop    

	return y;
}
 
document.write('<div id="dhtmlwindowholder"><span style="display:none">.</span></div>') //container that holds all dhtml window divs on page
window.onunload=dhtmlwindow.cleanup

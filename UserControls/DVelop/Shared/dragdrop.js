YAHOO.util.DragDropMgr||(YAHOO.util.DragDropMgr=function(){var n=YAHOO.util.Event,t=YAHOO.util.Dom;return{useShim:!1,_shimActive:!1,_shimState:!1,_debugShim:!1,_createShim:function(){var i=document.createElement("div");i.id="yui-ddm-shim";document.body.firstChild?document.body.insertBefore(i,document.body.firstChild):document.body.appendChild(i);i.style.display="none";i.style.backgroundColor="red";i.style.position="absolute";i.style.zIndex="99999";t.setStyle(i,"opacity","0");this._shim=i;n.on(i,"mouseup",this.handleMouseUp,this,!0);n.on(i,"mousemove",this.handleMouseMove,this,!0);n.on(window,"scroll",this._sizeShim,this,!0)},_sizeShim:function(){if(this._shimActive){var n=this._shim;n.style.height=t.getDocumentHeight()+"px";n.style.width=t.getDocumentWidth()+"px";n.style.top="0";n.style.left="0"}},_activateShim:function(){if(this.useShim){this._shim||this._createShim();this._shimActive=!0;var n=this._shim,i="0";this._debugShim&&(i=".5");t.setStyle(n,"opacity",i);this._sizeShim();n.style.display="block"}},_deactivateShim:function(){this._shim.style.display="none";this._shimActive=!1},_shim:null,ids:{},handleIds:{},dragCurrent:null,dragOvers:{},deltaX:0,deltaY:0,preventDefault:!0,stopPropagation:!0,initialized:!1,locked:!1,interactionInfo:null,init:function(){this.initialized=!0},POINT:0,INTERSECT:1,STRICT_INTERSECT:2,mode:0,_execOnAll:function(n,t){var r,u,i;for(r in this.ids)for(u in this.ids[r])(i=this.ids[r][u],this.isTypeOfDD(i))&&i[n].apply(i,t)},_onLoad:function(){this.init();n.on(document,"mouseup",this.handleMouseUp,this,!0);n.on(document,"mousemove",this.handleMouseMove,this,!0);n.on(window,"unload",this._onUnload,this,!0);n.on(window,"resize",this._onResize,this,!0)},_onResize:function(){this._execOnAll("resetConstraints",[])},lock:function(){this.locked=!0},unlock:function(){this.locked=!1},isLocked:function(){return this.locked},locationCache:{},useCache:!0,clickPixelThresh:3,clickTimeThresh:1e3,dragThreshMet:!1,clickTimeout:null,startX:0,startY:0,fromTimeout:!1,regDragDrop:function(n,t){this.initialized||this.init();this.ids[t]||(this.ids[t]={});this.ids[t][n.id]=n},removeDDFromGroup:function(n,t){this.ids[t]||(this.ids[t]={});var i=this.ids[t];i&&i[n.id]&&delete i[n.id]},_remove:function(n){var i,t;for(i in n.groups)i&&(t=this.ids[i],t&&t[n.id]&&delete t[n.id]);delete this.handleIds[n.id]},regHandle:function(n,t){this.handleIds[n]||(this.handleIds[n]={});this.handleIds[n][t]=t},isDragDrop:function(n){return this.getDDById(n)?!0:!1},getRelated:function(n,t){var r=[],u,f,i;for(u in n.groups)for(f in this.ids[u])(i=this.ids[u][f],this.isTypeOfDD(i))&&(!t||i.isTarget)&&(r[r.length]=i);return r},isLegalTarget:function(n,t){for(var r=this.getRelated(n,!0),i=0,u=r.length;i<u;++i)if(r[i].id==t.id)return!0;return!1},isTypeOfDD:function(n){return n&&n.__ygDragDrop},isHandle:function(n,t){return this.handleIds[n]&&this.handleIds[n][t]},getDDById:function(n){for(var t in this.ids)if(this.ids[t][n])return this.ids[t][n];return null},handleMouseDown:function(n,t){this.currentTarget=YAHOO.util.Event.getTarget(n);this.dragCurrent=t;var i=t.getEl();this.startX=YAHOO.util.Event.getPageX(n);this.startY=YAHOO.util.Event.getPageY(n);this.deltaX=this.startX-i.offsetLeft;this.deltaY=this.startY-i.offsetTop;this.dragThreshMet=!1;this.clickTimeout=setTimeout(function(){var n=YAHOO.util.DDM;n.startDrag(n.startX,n.startY);n.fromTimeout=!0},this.clickTimeThresh)},startDrag:function(n,t){this.dragCurrent&&this.dragCurrent.useShim&&(this._shimState=this.useShim,this.useShim=!0);this._activateShim();clearTimeout(this.clickTimeout);var i=this.dragCurrent;i&&i.events.b4StartDrag&&(i.b4StartDrag(n,t),i.fireEvent("b4StartDragEvent",{x:n,y:t}));i&&i.events.startDrag&&(i.startDrag(n,t),i.fireEvent("startDragEvent",{x:n,y:t}));this.dragThreshMet=!0},handleMouseUp:function(n){this.dragCurrent&&(clearTimeout(this.clickTimeout),this.dragThreshMet&&(this.fromTimeout&&(this.fromTimeout=!1,this.handleMouseMove(n)),this.fromTimeout=!1,this.fireEvents(n,!0)),this.stopDrag(n),this.stopEvent(n))},stopEvent:function(n){this.stopPropagation&&YAHOO.util.Event.stopPropagation(n);this.preventDefault&&YAHOO.util.Event.preventDefault(n)},stopDrag:function(n,t){var i=this.dragCurrent;if(i&&!t&&(this.dragThreshMet&&(i.events.b4EndDrag&&(i.b4EndDrag(n),i.fireEvent("b4EndDragEvent",{e:n})),i.events.endDrag&&(i.endDrag(n),i.fireEvent("endDragEvent",{e:n}))),i.events.mouseUp)){i.onMouseUp(n);i.fireEvent("mouseUpEvent",{e:n})}this._shimActive&&(this._deactivateShim(),this.dragCurrent&&this.dragCurrent.useShim&&(this.useShim=this._shimState,this._shimState=!1));this.dragCurrent=null;this.dragOvers={}},handleMouseMove:function(n){var t=this.dragCurrent,i,r;if(t){if(YAHOO.util.Event.isIE&&!n.button)return this.stopEvent(n),this.handleMouseUp(n);if(n.clientX<0||n.clientY<0,this.dragThreshMet||(i=Math.abs(this.startX-YAHOO.util.Event.getPageX(n)),r=Math.abs(this.startY-YAHOO.util.Event.getPageY(n)),(i>this.clickPixelThresh||r>this.clickPixelThresh)&&this.startDrag(this.startX,this.startY)),this.dragThreshMet){if(t&&t.events.b4Drag&&(t.b4Drag(n),t.fireEvent("b4DragEvent",{e:n})),t&&t.events.drag){t.onDrag(n);t.fireEvent("dragEvent",{e:n})}t&&this.fireEvents(n,!1)}this.stopEvent(n)}},fireEvents:function(n,t){var i=this.dragCurrent,r,y,a,f,g,u,s,rt;if(i&&!i.isLocked()&&!i.dragOnly){var ut=YAHOO.util.Event.getPageX(n),ft=YAHOO.util.Event.getPageY(n),c=new YAHOO.util.Point(ut,ft),v=i.getTargetCoord(c.x,c.y),b=i.getDragEl(),l=["out","over","drop","enter"],w=new YAHOO.util.Region(v.y,v.x+b.offsetWidth,v.y+b.offsetHeight,v.x),k=[],d={},o=[],e={outEvts:[],overEvts:[],dropEvts:[],enterEvts:[]};for(r in this.dragOvers)(y=this.dragOvers[r],this.isTypeOfDD(y))&&(this.isOverTarget(c,y,this.mode,w)||e.outEvts.push(y),k[r]=!0,delete this.dragOvers[r]);for(a in i.groups)if("string"==typeof a)for(r in this.ids[a])(f=this.ids[a][r],this.isTypeOfDD(f))&&f.isTarget&&!f.isLocked()&&f!=i&&this.isOverTarget(c,f,this.mode,w)&&(d[a]=!0,t?e.dropEvts.push(f):(k[f.id]?e.overEvts.push(f):e.enterEvts.push(f),this.dragOvers[f.id]=f));this.interactionInfo={out:e.outEvts,enter:e.enterEvts,over:e.overEvts,drop:e.dropEvts,point:c,draggedRegion:w,sourceRegion:this.locationCache[i.id],validDrop:t};for(g in d)o.push(g);if(t&&!e.dropEvts.length&&(this.interactionInfo.validDrop=!1,i.events.invalidDrop)){i.onInvalidDrop(n);i.fireEvent("invalidDropEvent",{e:n})}for(r=0;r<l.length;r++)if(u=null,e[l[r]+"Evts"]&&(u=e[l[r]+"Evts"]),u&&u.length){var p=l[r].charAt(0).toUpperCase()+l[r].substr(1),nt="onDrag"+p,h="b4Drag"+p,tt="drag"+p+"Event",it="drag"+p;if(this.mode)i.events[h]&&(i[h](n,u,o),i.fireEvent(h+"Event",{event:n,info:u,group:o})),i.events[it]&&(i[nt](n,u,o),i.fireEvent(tt,{event:n,info:u,group:o}));else for(s=0,rt=u.length;s<rt;++s)i.events[h]&&(i[h](n,u[s].id,o[0]),i.fireEvent(h+"Event",{event:n,info:u[s].id,group:o[0]})),i.events[it]&&(i[nt](n,u[s].id,o[0]),i.fireEvent(tt,{event:n,info:u[s].id,group:o[0]}))}}},getBestMatch:function(n){var t=null,u=n.length,r,i;if(u==1)t=n[0];else for(r=0;r<u;++r)if(i=n[r],this.mode==this.INTERSECT&&i.cursorIsOver){t=i;break}else(!t||!t.overlap||i.overlap&&t.overlap.getArea()<i.overlap.getArea())&&(t=i);return t},refreshCache:function(n){var f=n||this.ids,i,u,t,r;for(i in f)if("string"==typeof i)for(u in this.ids[i])t=this.ids[i][u],this.isTypeOfDD(t)&&(r=this.getLocation(t),r?this.locationCache[t.id]=r:delete this.locationCache[t.id])},verifyEl:function(n){try{if(n){var t=n.offsetParent;if(t)return!0}}catch(i){}return!1},getLocation:function(n){if(!this.isTypeOfDD(n))return null;var i=n.getEl(),t,r,f,u,e,o,s,h,c;try{t=YAHOO.util.Dom.getXY(i)}catch(l){}return t?(r=t[0],f=r+i.offsetWidth,u=t[1],e=u+i.offsetHeight,o=u-n.padding[0],s=f+n.padding[1],h=e+n.padding[2],c=r-n.padding[3],new YAHOO.util.Region(o,s,h,c)):null},isOverTarget:function(n,t,i,r){var u=this.locationCache[t.id],f,e,o,s;return(u&&this.useCache||(u=this.getLocation(t),this.locationCache[t.id]=u),!u)?!1:(t.cursorIsOver=u.contains(n),f=this.dragCurrent,!f||!i&&!f.constrainX&&!f.constrainY)?t.cursorIsOver:(t.overlap=null,r||(e=f.getTargetCoord(n.x,n.y),o=f.getDragEl(),r=new YAHOO.util.Region(e.y,e.x+o.offsetWidth,e.y+o.offsetHeight,e.x)),s=r.intersect(u),s?(t.overlap=s,i?!0:t.cursorIsOver):!1)},_onUnload:function(){this.unregAll()},unregAll:function(){this.dragCurrent&&(this.stopDrag(),this.dragCurrent=null);this._execOnAll("unreg",[]);this.ids={}},elementCache:{},getElWrapper:function(n){var t=this.elementCache[n];return t&&t.el||(t=this.elementCache[n]=new this.ElementWrapper(YAHOO.util.Dom.get(n))),t},getElement:function(n){return YAHOO.util.Dom.get(n)},getCss:function(n){var t=YAHOO.util.Dom.get(n);return t?t.style:null},ElementWrapper:function(n){this.el=n||null;this.id=this.el&&n.id;this.css=this.el&&n.style},getPosX:function(n){return YAHOO.util.Dom.getX(n)},getPosY:function(n){return YAHOO.util.Dom.getY(n)},swapNode:function(n,t){if(n.swapNode)n.swapNode(t);else{var i=t.parentNode,r=t.nextSibling;r==n?i.insertBefore(n,t):t==n.nextSibling?i.insertBefore(t,n):(n.parentNode.replaceChild(t,n),i.insertBefore(n,r))}},getScroll:function(){var t,i,n=document.documentElement,r=document.body;return n&&(n.scrollTop||n.scrollLeft)?(t=n.scrollTop,i=n.scrollLeft):r&&(t=r.scrollTop,i=r.scrollLeft),{top:t,left:i}},getStyle:function(n,t){return YAHOO.util.Dom.getStyle(n,t)},getScrollTop:function(){return this.getScroll().top},getScrollLeft:function(){return this.getScroll().left},moveToEl:function(n,t){var i=YAHOO.util.Dom.getXY(t);YAHOO.util.Dom.setXY(n,i)},getClientHeight:function(){return YAHOO.util.Dom.getViewportHeight()},getClientWidth:function(){return YAHOO.util.Dom.getViewportWidth()},numericSort:function(n,t){return n-t},_timeoutCount:0,_addListeners:function(){var n=YAHOO.util.DDM;YAHOO.util.Event&&document?n._onLoad():n._timeoutCount>2e3||(setTimeout(n._addListeners,10),document&&document.body&&(n._timeoutCount+=1))},handleWasClicked:function(n,t){if(this.isHandle(t,n.id))return!0;for(var i=n.parentNode;i;){if(this.isHandle(t,i.id))return!0;i=i.parentNode}return!1}}}(),YAHOO.util.DDM=YAHOO.util.DragDropMgr,YAHOO.util.DDM._addListeners()),function(){var t=YAHOO.util.Event,n=YAHOO.util.Dom;YAHOO.util.DragDrop=function(n,t,i){n&&this.init(n,t,i)};YAHOO.util.DragDrop.prototype={events:null,on:function(){this.subscribe.apply(this,arguments)},id:null,config:null,dragElId:null,handleElId:null,invalidHandleTypes:null,invalidHandleIds:null,invalidHandleClasses:null,startPageX:0,startPageY:0,groups:null,locked:!1,lock:function(){this.locked=!0},unlock:function(){this.locked=!1},isTarget:!0,padding:null,dragOnly:!1,useShim:!1,_domRef:null,__ygDragDrop:!0,constrainX:!1,constrainY:!1,minX:0,maxX:0,minY:0,maxY:0,deltaX:0,deltaY:0,maintainOffset:!1,xTicks:null,yTicks:null,primaryButtonOnly:!0,available:!1,hasOuterHandles:!1,cursorIsOver:!1,overlap:null,b4StartDrag:function(){},startDrag:function(){},b4Drag:function(){},onDrag:function(){},onDragEnter:function(){},b4DragOver:function(){},onDragOver:function(){},b4DragOut:function(){},onDragOut:function(){},b4DragDrop:function(){},onDragDrop:function(){},onInvalidDrop:function(){},b4EndDrag:function(){},endDrag:function(){},b4MouseDown:function(){},onMouseDown:function(){},onMouseUp:function(){},onAvailable:function(){},getEl:function(){return this._domRef||(this._domRef=n.get(this.id)),this._domRef},getDragEl:function(){return n.get(this.dragElId)},init:function(n,i,r){this.initTarget(n,i,r);t.on(this._domRef||this.id,"mousedown",this.handleMouseDown,this,!0);for(var u in this.events)this.createEvent(u+"Event")},initTarget:function(i,r,u){this.config=u||{};this.events={};this.DDM=YAHOO.util.DDM;this.groups={};typeof i!="string"&&(this._domRef=i,i=n.generateId(i));this.id=i;this.addToGroup(r?r:"default");this.handleElId=i;t.onAvailable(i,this.handleOnAvailable,this,!0);this.setDragElId(i);this.invalidHandleTypes={A:"A"};this.invalidHandleIds={};this.invalidHandleClasses=[];this.applyConfig()},applyConfig:function(){if(this.events={mouseDown:!0,b4MouseDown:!0,mouseUp:!0,b4StartDrag:!0,startDrag:!0,b4EndDrag:!0,endDrag:!0,drag:!0,b4Drag:!0,invalidDrop:!0,b4DragOut:!0,dragOut:!0,dragEnter:!0,b4DragOver:!0,dragOver:!0,b4DragDrop:!0,dragDrop:!0},this.config.events)for(var n in this.config.events)this.config.events[n]===!1&&(this.events[n]=!1);this.padding=this.config.padding||[0,0,0,0];this.isTarget=this.config.isTarget!==!1;this.maintainOffset=this.config.maintainOffset;this.primaryButtonOnly=this.config.primaryButtonOnly!==!1;this.dragOnly=this.config.dragOnly===!0?!0:!1;this.useShim=this.config.useShim===!0?!0:!1},handleOnAvailable:function(){this.available=!0;this.resetConstraints();this.onAvailable()},setPadding:function(n,t,i,r){this.padding=t||0===t?i||0===i?[n,t,i,r]:[n,t,n,t]:[n,n,n,n]},setInitPosition:function(t,i){var r=this.getEl();if(!this.DDM.verifyEl(r)){r&&r.style&&r.style.display=="none";return}var f=t||0,e=i||0,u=n.getXY(r);this.initPageX=u[0]-f;this.initPageY=u[1]-e;this.lastPageX=u[0];this.lastPageY=u[1];this.setStartPosition(u)},setStartPosition:function(t){var i=t||n.getXY(this.getEl());this.deltaSetXY=null;this.startPageX=i[0];this.startPageY=i[1]},addToGroup:function(n){this.groups[n]=!0;this.DDM.regDragDrop(this,n)},removeFromGroup:function(n){this.groups[n]&&delete this.groups[n];this.DDM.removeDDFromGroup(this,n)},setDragElId:function(n){this.dragElId=n},setHandleElId:function(t){typeof t!="string"&&(t=n.generateId(t));this.handleElId=t;this.DDM.regHandle(this.id,t)},setOuterHandleElId:function(i){typeof i!="string"&&(i=n.generateId(i));t.on(i,"mousedown",this.handleMouseDown,this,!0);this.setHandleElId(i);this.hasOuterHandles=!0},unreg:function(){t.removeListener(this.id,"mousedown",this.handleMouseDown);this._domRef=null;this.DDM._remove(this)},isLocked:function(){return this.DDM.isLocked()||this.locked},handleMouseDown:function(n){var o=n.which||n.button,u,i,f,r,e;this.primaryButtonOnly&&o>1||this.isLocked()||(u=this.b4MouseDown(n),i=!0,this.events.b4MouseDown&&(i=this.fireEvent("b4MouseDownEvent",n)),f=this.onMouseDown(n),r=!0,this.events.mouseDown&&(r=this.fireEvent("mouseDownEvent",n)),u!==!1&&f!==!1&&i!==!1&&r!==!1)&&(this.DDM.refreshCache(this.groups),e=new YAHOO.util.Point(t.getPageX(n),t.getPageY(n)),(this.hasOuterHandles||this.DDM.isOverTarget(e,this))&&this.clickValidator(n)&&(this.setStartPosition(),this.DDM.handleMouseDown(n,this),this.DDM.stopEvent(n)))},clickValidator:function(n){var t=YAHOO.util.Event.getTarget(n);return this.isValidHandleChild(t)&&(this.id==this.handleElId||this.DDM.handleWasClicked(t,this.id))},getTargetCoord:function(n,t){var i=n-this.deltaX,r=t-this.deltaY;return this.constrainX&&(i<this.minX&&(i=this.minX),i>this.maxX&&(i=this.maxX)),this.constrainY&&(r<this.minY&&(r=this.minY),r>this.maxY&&(r=this.maxY)),i=this.getTick(i,this.xTicks),r=this.getTick(r,this.yTicks),{x:i,y:r}},addInvalidHandleType:function(n){var t=n.toUpperCase();this.invalidHandleTypes[t]=t},addInvalidHandleId:function(t){typeof t!="string"&&(t=n.generateId(t));this.invalidHandleIds[t]=t},addInvalidHandleClass:function(n){this.invalidHandleClasses.push(n)},removeInvalidHandleType:function(n){var t=n.toUpperCase();delete this.invalidHandleTypes[t]},removeInvalidHandleId:function(t){typeof t!="string"&&(t=n.generateId(t));delete this.invalidHandleIds[t]},removeInvalidHandleClass:function(n){for(var t=0,i=this.invalidHandleClasses.length;t<i;++t)this.invalidHandleClasses[t]==n&&delete this.invalidHandleClasses[t]},isValidHandleChild:function(t){var i=!0,u,r,f;try{u=t.nodeName.toUpperCase()}catch(e){u=t.nodeName}for(i=i&&!this.invalidHandleTypes[u],i=i&&!this.invalidHandleIds[t.id],r=0,f=this.invalidHandleClasses.length;i&&r<f;++r)i=!n.hasClass(t,this.invalidHandleClasses[r]);return i},setXTicks:function(n,t){var r,i;for(this.xTicks=[],this.xTickSize=t,r={},i=this.initPageX;i>=this.minX;i=i-t)r[i]||(this.xTicks[this.xTicks.length]=i,r[i]=!0);for(i=this.initPageX;i<=this.maxX;i=i+t)r[i]||(this.xTicks[this.xTicks.length]=i,r[i]=!0);this.xTicks.sort(this.DDM.numericSort)},setYTicks:function(n,t){var r,i;for(this.yTicks=[],this.yTickSize=t,r={},i=this.initPageY;i>=this.minY;i=i-t)r[i]||(this.yTicks[this.yTicks.length]=i,r[i]=!0);for(i=this.initPageY;i<=this.maxY;i=i+t)r[i]||(this.yTicks[this.yTicks.length]=i,r[i]=!0);this.yTicks.sort(this.DDM.numericSort)},setXConstraint:function(n,t,i){this.leftConstraint=parseInt(n,10);this.rightConstraint=parseInt(t,10);this.minX=this.initPageX-this.leftConstraint;this.maxX=this.initPageX+this.rightConstraint;i&&this.setXTicks(this.initPageX,i);this.constrainX=!0},clearConstraints:function(){this.constrainX=!1;this.constrainY=!1;this.clearTicks()},clearTicks:function(){this.xTicks=null;this.yTicks=null;this.xTickSize=0;this.yTickSize=0},setYConstraint:function(n,t,i){this.topConstraint=parseInt(n,10);this.bottomConstraint=parseInt(t,10);this.minY=this.initPageY-this.topConstraint;this.maxY=this.initPageY+this.bottomConstraint;i&&this.setYTicks(this.initPageY,i);this.constrainY=!0},resetConstraints:function(){if(this.initPageX||this.initPageX===0){var n=this.maintainOffset?this.lastPageX-this.initPageX:0,t=this.maintainOffset?this.lastPageY-this.initPageY:0;this.setInitPosition(n,t)}else this.setInitPosition();this.constrainX&&this.setXConstraint(this.leftConstraint,this.rightConstraint,this.xTickSize);this.constrainY&&this.setYConstraint(this.topConstraint,this.bottomConstraint,this.yTickSize)},getTick:function(n,t){var i,u,r,f,e;if(t){if(t[0]>=n)return t[0];for(i=0,u=t.length;i<u;++i)if(r=i+1,t[r]&&t[r]>=n)return f=n-t[i],e=t[r]-n,e>f?t[i]:t[r];return t[t.length-1]}return n},toString:function(){return"DragDrop "+this.id}};YAHOO.augment(YAHOO.util.DragDrop,YAHOO.util.EventProvider)}();YAHOO.util.DD=function(n,t,i){n&&this.init(n,t,i)};YAHOO.extend(YAHOO.util.DD,YAHOO.util.DragDrop,{scroll:!0,autoOffset:function(n,t){var i=n-this.startPageX,r=t-this.startPageY;this.setDelta(i,r)},setDelta:function(n,t){this.deltaX=n;this.deltaY=t},setDragElPos:function(n,t){var i=this.getDragEl();this.alignElWithMouse(i,n,t)},alignElWithMouse:function(n,t,i){var r=this.getTargetCoord(t,i),f,e,o,u;this.deltaSetXY?(YAHOO.util.Dom.setStyle(n,"left",r.x+this.deltaSetXY[0]+"px"),YAHOO.util.Dom.setStyle(n,"top",r.y+this.deltaSetXY[1]+"px")):(f=[r.x,r.y],YAHOO.util.Dom.setXY(n,f),e=parseInt(YAHOO.util.Dom.getStyle(n,"left"),10),o=parseInt(YAHOO.util.Dom.getStyle(n,"top"),10),this.deltaSetXY=[e-r.x,o-r.y]);this.cachePosition(r.x,r.y);u=this;setTimeout(function(){u.autoScroll.call(u,r.x,r.y,n.offsetHeight,n.offsetWidth)},0)},cachePosition:function(n,t){if(n)this.lastPageX=n,this.lastPageY=t;else{var i=YAHOO.util.Dom.getXY(this.getEl());this.lastPageX=i[0];this.lastPageY=i[1]}},autoScroll:function(n,t,i,r){if(this.scroll){var s=this.DDM.getClientHeight(),h=this.DDM.getClientWidth(),u=this.DDM.getScrollTop(),f=this.DDM.getScrollLeft(),c=i+t,l=r+n,a=s+u-t-this.deltaY,v=h+f-n-this.deltaX,e=40,o=document.all?80:30;c>s&&a<e&&window.scrollTo(f,u+o);t<u&&u>0&&t-u<e&&window.scrollTo(f,u-o);l>h&&v<e&&window.scrollTo(f+o,u);n<f&&f>0&&n-f<e&&window.scrollTo(f-o,u)}},applyConfig:function(){YAHOO.util.DD.superclass.applyConfig.call(this);this.scroll=this.config.scroll!==!1},b4MouseDown:function(n){this.setStartPosition();this.autoOffset(YAHOO.util.Event.getPageX(n),YAHOO.util.Event.getPageY(n))},b4Drag:function(n){this.setDragElPos(YAHOO.util.Event.getPageX(n),YAHOO.util.Event.getPageY(n))},toString:function(){return"DD "+this.id}});YAHOO.util.DDProxy=function(n,t,i){n&&(this.init(n,t,i),this.initFrame())};YAHOO.util.DDProxy.dragElId="ygddfdiv";YAHOO.extend(YAHOO.util.DDProxy,YAHOO.util.DD,{resizeFrame:!0,centerFrame:!1,createFrame:function(){var f=this,u=document.body,t,r,n,i;if(!u||!u.firstChild){setTimeout(function(){f.createFrame()},50);return}t=this.getDragEl();r=YAHOO.util.Dom;t||(t=document.createElement("div"),t.id=this.dragElId,n=t.style,n.position="absolute",n.visibility="hidden",n.cursor="move",n.border="2px solid #aaa",n.zIndex=999,n.height="25px",n.width="25px",i=document.createElement("div"),r.setStyle(i,"height","100%"),r.setStyle(i,"width","100%"),r.setStyle(i,"background-color","#ccc"),r.setStyle(i,"opacity","0"),t.appendChild(i),u.insertBefore(t,u.firstChild))},initFrame:function(){this.createFrame()},applyConfig:function(){YAHOO.util.DDProxy.superclass.applyConfig.call(this);this.resizeFrame=this.config.resizeFrame!==!1;this.centerFrame=this.config.centerFrame;this.setDragElId(this.config.dragElId||YAHOO.util.DDProxy.dragElId)},showFrame:function(n,t){var u=this.getEl(),i=this.getDragEl(),r=i.style;this._resizeProxy();this.centerFrame&&this.setDelta(Math.round(parseInt(r.width,10)/2),Math.round(parseInt(r.height,10)/2));this.setDragElPos(n,t);YAHOO.util.Dom.setStyle(i,"visibility","visible")},_resizeProxy:function(){var o,s;if(this.resizeFrame){var n=YAHOO.util.Dom,e=this.getEl(),t=this.getDragEl(),i=parseInt(n.getStyle(t,"borderTopWidth"),10),r=parseInt(n.getStyle(t,"borderRightWidth"),10),u=parseInt(n.getStyle(t,"borderBottomWidth"),10),f=parseInt(n.getStyle(t,"borderLeftWidth"),10);isNaN(i)&&(i=0);isNaN(r)&&(r=0);isNaN(u)&&(u=0);isNaN(f)&&(f=0);o=Math.max(0,e.offsetWidth-r-f);s=Math.max(0,e.offsetHeight-i-u);n.setStyle(t,"width",o+"px");n.setStyle(t,"height",s+"px")}},b4MouseDown:function(n){this.setStartPosition();var t=YAHOO.util.Event.getPageX(n),i=YAHOO.util.Event.getPageY(n);this.autoOffset(t,i)},b4StartDrag:function(n,t){this.showFrame(n,t)},b4EndDrag:function(){YAHOO.util.Dom.setStyle(this.getDragEl(),"visibility","hidden")},endDrag:function(){var n=YAHOO.util.Dom,t=this.getEl(),i=this.getDragEl();n.setStyle(i,"visibility","");n.setStyle(t,"visibility","hidden");YAHOO.util.DDM.moveToEl(t,i);n.setStyle(i,"visibility","hidden");n.setStyle(t,"visibility","")},toString:function(){return"DDProxy "+this.id}});YAHOO.util.DDTarget=function(n,t,i){n&&this.initTarget(n,t,i)};YAHOO.extend(YAHOO.util.DDTarget,YAHOO.util.DragDrop,{toString:function(){return"DDTarget "+this.id}});YAHOO.register("dragdrop",YAHOO.util.DragDropMgr,{version:"2.7.0",build:"1799"})
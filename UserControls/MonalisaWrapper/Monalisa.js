//******************************************************************************
//    Agrega scripts para manejar suggest
//******************************************************************************
function include_js(file_path){
	try{
		var script = '<script type = "text/javascript" src="'+file_path+'"></script>';
		//alert(script);
		document.write(script);
	}catch(e){
		alert("No se pudo cargar script: "+file_path+" Error: "+e.message);	
	}
}

for(var i=0; i< document.scripts.length; i++){
	if (document.scripts[i].src.endsWith("Monalisa.js")){
		var basepath = document.scripts[i].src.replace("Monalisa.js", "");
		break;
	} 
}

include_js("Monalisawrapper\jquery-1.9.1.min.js");
include_js("MonaLisaWrapper\jquery.autocomplete.min.js");


//******************************************************************************
//    Manejo de Eventos Cross Browser
//******************************************************************************

var addEvent = (function () { 
    if (document.addEventListener) {
        return function (el, type, fn) {
                if (el && el.nodeName || el === window) {
                    el.addEventListener(type, fn, false); 
                } 
				else	if (el && el.length) { 
                    for (var i = 0; i < el.length; i++) { 
                        addEvent(el[i], type, fn); 
                    }
                } 
        }; 
    } else { 
        return function (el, type, fn) { 
            if (el && el.nodeName || el === window) { 
                el.attachEvent('on' + type, function () { 
                        return fn.call(el, window.event); 
                    }); 
            } else if (el && el.length) { 
                for (var i = 0; i < el.length; i++) { 
                    addEvent(el[i], type, fn); 
                } 
            } 
        }; 
    } 
})();
var removeEvent = (function () { 
    if (document.addEventListener) {
        return function (el, type, fn) {
                if (el && el.nodeName || el === window) {
                    el.removeEventListener(type, fn, false); 
                } 
				else if (el && el.length) { 
                    for (var i = 0; i < el.length; i++) { 
                        removeEvent(el[i], type, fn); 
					}
                } 
        }; 
    } else { 
        return function (el, type, fn) { 
            if (el && el.nodeName || el === window) { 
                el.attachEvent('on' + type, function () { 
                        return fn.call(el, window.event); 
                    }); 
            } else if (el && el.length) { 
                for (var i = 0; i < el.length; i++) { 
                    addEvent(el[i], type, fn); 
                } 
            } 
        }; 
    } 
})();


function cloneObject(obj) {
    if (null == obj || "object" != typeof obj) return obj;
    var copy = obj.constructor();
    for (var attr in obj) {
        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
    }
    return copy;
}


//******************************************************************************
//    Eventos Drag and Drop
//******************************************************************************

function dragStart(event) {
	
   var debug = document.getElementById("Debug");
   if(debug)
	debug.appendChild(document.createTextNode(event.type+"-"+event.target.id+";"));


   event.dataTransfer.effectAllowed='move';
   event.dataTransfer.setData("Text", event.target.id);//Esta linea es fundamental para que funcione con Firefox
   Monalisa.dragging = event.target;

   
   //var dragIcon = document.createElement('img');
   //dragIcon.src = 'http://twitter.com/api/users/profile_image/twitter?size=mini';
   //event.dataTransfer.setDragImage(dragIcon, -10, -10);

   //event.dataTransfer.setDragImage(event.target,0,0);

   // create an empty <span>
   
   var dragImgEl = document.getElementById("dragImgEl");
   if(!dragImgEl){
        var dragImgEl = document.createElement('span');
        dragImgEl.id = "dragImgEl";
        dragImgEl.setAttribute('style',  'z-index:-1;display:block;');
        
        var dragImgElMask = document.createElement('span');
        dragImgElMask.id = "dragImgElMask";
        dragImgElMask.setAttribute('style',  'position:absolute;left:0px;top:0px;z-index:1;display:block;width:100%; height:100%;;background-color:white');
    

        var hiddenEl = document.createElement('span');
        hiddenEl.id = "hiddenEl";
        hiddenEl.setAttribute('style',  'position:absolute;left:5px;top:5px;z-index:-1;display:block');
        hiddenEl.appendChild(dragImgEl);
        //hiddenEl.appendChild(dragImgElMask);    
        // add it to the document
        document.body.appendChild(hiddenEl);

    }
    dragImgEl.innerHTML = event.target.outerHTML;

    //dragImgEl.appendChild(document.createTextNode("Hola Pelotudo!!!"));
    // set its style so it'll be effectively (but not technically) invisible and
    // won't change document flow
    dragImgEl.setAttribute('style',  'display: block;zoom:0.5;' );
    //dragImgEl.height = event.target.clientHeight;
    dragImgEl.style.width = event.target.offsetWidth + 40;
    //dragImgEl.style.heigth = 240;

    
    // your DataTransfer code here--assume we put it in a variable called 'dt'
	try{
		event.dataTransfer.setDragImage(dragImgEl, 0, 0);
	}catch(ex){}

   
   //Monalisa.RegisterAllowdrop(event.target.ObjRef.type.split(" ",1));
   event.target.ObjRef.GetEditor().RegisterAllowdrop(event.target.ObjRef.type.split(" ",1));

   return true;
   
}

function dragEnter(event) {
   var debug = document.getElementById("Debug");
   if(debug)
	debug.appendChild(document.createTextNode(event.type+"-"+event.target.id+";"));

   var el = event.target;
   var vclassName = el.className.split(" ");
   if (event.target.allowdrop && vclassName[vclassName.length-1] != "drop")
   	event.target.className = event.target.className+" drop";
   if (event.preventDefault) {
    event.preventDefault();
  }
  return false;
}

function dragLeave(event) {
   var debug = document.getElementById("Debug");
   if(debug)
	debug.appendChild(document.createTextNode(event.type+"-"+event.target.id+";"));

   var el = event.target;
   var vclassName = el.className.split(" ");
   if (event.target.allowdrop && vclassName[vclassName.length-1] == "drop")
   	el.className = el.className.split(" ", vclassName.length-1);

   if (event.preventDefault) {
    event.preventDefault();
  }
  return false;
}

function dragOver(event) {
//   var debug = document.getElementById("Debug");
//   if(debug)
//    	debug.appendChild(document.createTextNode(event.type+"-"+event.target.id+";"));

    //alert("dragOver");
    if (event.preventDefault) {
    event.preventDefault();
  }
  return false;
}

function dragEnd(event) {
   var debug = document.getElementById("Debug");
   if(debug)
	    debug.appendChild(document.createTextNode(event.type+"-"+event.target.id+";"));

   //Monalisa.DesregisterAllowdrop();
   event.target.ObjRef.GetEditor().DesregisterAllowdrop();
   //Monalisa.dragging = null;
   
  if (event.preventDefault) {
    event.preventDefault();
  }
  return false;
}


function dragDrop(event) {
   	var debug = document.getElementById("Debug");
   	if(debug)
		debug.appendChild(document.createTextNode(event.type+"-"+event.target.id+";"));


	var el = event.target;
	if (event.target.allowdrop){

		el.className = el.className.split(" ", el.className.split(" ").length-1);

	   	var src = Monalisa.dragging;
		
		//Si se arrastra desde el toolbox es un objeto nuevo
		if (Monalisa.dragging.ObjRef.className == "Monalisa.ToolboxItem")
	   		event.target.ObjRef.DropNewElement(Monalisa.dragging.ObjRef);
		//Si se arrastra a la papelera se esta eliminando
		if (event.target.className == "trash") 		
			Monalisa.dragging.ObjRef.DropRemoveElement();

		//Sino es porque se esta reubicando, hay que copiar todo el subarbol que se arrastra y borrarlo
		if (Monalisa.dragging.ObjRef.className != "Monalisa.ToolboxItem" && event.target.className != "trash") {
            event.target.ObjRef.DropMoveElement(Monalisa.dragging.ObjRef);
		}
		
   
	}
	if (event.preventDefault) {
		event.preventDefault();
	}
	event.stopPropagation();
   	return false;
}



//Definicion del namespace
if (Monalisa == undefined) var Monalisa = {};
//******************************************************************************
//    Monalisa propiedades globales
//******************************************************************************

Monalisa.Editor = function(){
	
    this.allowdrop = new Array(); //Elementos sobre el cual se puede hacer un drop
	this.dragging = null; //Elemento que se esta dragando
	this.DOMRef = null;
	this.Condition = null;
	this.Results = new Array();
    this.Fields = new Array();
    this.Lists = new Array();
    this.ReadOnly = false;
	this.ModeText = false;
	
};

Monalisa.Editor.prototype.CreateHTML = function(){

	var Monalisatag = document.createElement('table');
	
	Monalisatag.id = "Container";
    Monalisatag.style.borderSpacing="0px"
    Monalisatag.style.height="100%"
    Monalisatag.style.width="100%"
	var tr1 = document.createElement('tr');
	Monalisatag.appendChild(tr1);
    var td1 = document.createElement('td');
	td1.style.align="left";
	td1.style.verticalAlign="top";
	td1.style.width="1px"
	tr1.appendChild(td1);
	
	
	//toolbox
    if (!this.ReadOnly){
	    var toolbox = new Monalisa.Toolbox();
	    toolbox.Editor = this;
	    var group = new Monalisa.ToolboxGroup("Campos");
	    toolbox.AddGroup(group);
    	
		//fiesds
        for(var i=0; i< this.Fields.length; i++){
			if (this.Fields[i].class == "field"){
				var item = new Monalisa.ToolboxItem(this.Fields[i].name, this.Fields[i].description, "field");
				item.ElementRef = this.Fields[i];
				group.AddItem(item);
			}
	    }
		
		//Models
		var firstModel = true;
		for(var i=0; i< this.Fields.length; i++){
			if (this.Fields[i].class == "model"){
				if (firstModel){
					var group = new Monalisa.ToolboxGroup("Modelos");
					toolbox.AddGroup(group);
					firstModel = false;
				}
				var item = new Monalisa.ToolboxItem(this.Fields[i].name, this.Fields[i].description, "field");
				item.ElementRef = this.Fields[i];
				group.AddItem(item);
			}
	    }
		
		//Catalog
		var firstCatalog = true;
		for(var i=0; i< this.Fields.length; i++){
			if (this.Fields[i].class == "catalog"){
				if (firstCatalog){
					var group = new Monalisa.ToolboxGroup("Catálogos");
					toolbox.AddGroup(group);
					firstCatalog = false;
				}
				var item = new Monalisa.ToolboxItem(this.Fields[i].name, this.Fields[i].description, "field");
				item.ElementRef = this.Fields[i];
				group.AddItem(item);
			}
	    }
        
        if (this.Lists.length > 0){
            var group = new Monalisa.ToolboxGroup("Listas");
	        toolbox.AddGroup(group);
    	
            for(var i=0; i< this.Lists.length; i++){
                var item = new Monalisa.ToolboxItem(this.Lists[i].name, this.Lists[i].description, "list");
                item.ElementRef = this.Fields[i];
	            group.AddItem(item);
	        }
        }

	    var group = new Monalisa.ToolboxGroup("Constante");
	    toolbox.AddGroup(group);
	    var item = new Monalisa.ToolboxItem("Constante", "", "constant");
	    group.AddItem(item);
    	
	    var group = new Monalisa.ToolboxGroup("Comparación");
	    toolbox.AddGroup(group);
		
		var menu = new Monalisa.Menu();
		this.menu = menu;
		menu.Editor = this;
		
	    var item = new Monalisa.ToolboxItem("Es", "", "constraint");
	    group.AddItem(item);
		menu.AddItem(new Monalisa.MenuItem("Es", MenuChangeContraintClick));
    		
	    var item = new Monalisa.ToolboxItem("No Es", "", "constraint");
	    group.AddItem(item);
    	menu.AddItem(new Monalisa.MenuItem("No Es", MenuChangeContraintClick));
    	
        var item = new Monalisa.ToolboxItem("Mayor", "", "constraint");
	    group.AddItem(item);
		menu.AddItem(new Monalisa.MenuItem("Mayor", MenuChangeContraintClick));
    	
        var item = new Monalisa.ToolboxItem("Mayor o igual", "", "constraint");
	    group.AddItem(item);
		menu.AddItem(new Monalisa.MenuItem("Mayor o igual", MenuChangeContraintClick));
    	
        var item = new Monalisa.ToolboxItem("Menor", "", "constraint");
	    group.AddItem(item);
		menu.AddItem(new Monalisa.MenuItem("Menor", MenuChangeContraintClick));
    	
        var item = new Monalisa.ToolboxItem("Menor o igual", "", "constraint");
	    group.AddItem(item);
		menu.AddItem(new Monalisa.MenuItem("Menor o igual", MenuChangeContraintClick));
    	
        var item = new Monalisa.ToolboxItem("Contiene", "", "constraint");
	    group.AddItem(item);
		menu.AddItem(new Monalisa.MenuItem("Contiene", MenuChangeContraintClick));
    	
	    var item = new Monalisa.ToolboxItem("EN", "", "in");
	    group.AddItem(item);
		menu.AddItem(new Monalisa.MenuItem("EN", MenuChangeContraintClick));
		
	    var group = new Monalisa.ToolboxGroup("Conectores Lógico");
	    toolbox.AddGroup(group);
	    var item = new Monalisa.ToolboxItem("OR", "", "logic");
	    group.AddItem(item);
    	
	    var item = new Monalisa.ToolboxItem("NOT", "", "logic");
	    group.AddItem(item);	

	    var group = new Monalisa.ToolboxGroup("Operadores Aritmeticos");
	    toolbox.AddGroup(group);
	    var item = new Monalisa.ToolboxItem("+", "", "arithmetic");
	    group.AddItem(item);
    	
	    var item = new Monalisa.ToolboxItem("-", "", "arithmetic");
	    group.AddItem(item);	

        var item = new Monalisa.ToolboxItem("*", "", "arithmetic");
	    group.AddItem(item);

        var item = new Monalisa.ToolboxItem("/", "", "arithmetic");
	    group.AddItem(item);

	    var group = new Monalisa.ToolboxGroup("Funciones de Texto");
	    toolbox.AddGroup(group);
	    var item = new Monalisa.ToolboxItem("concat", "", "functionText");
	    group.AddItem(item);
    	
	    var item = new Monalisa.ToolboxItem("substr", "", "functionText");
	    group.AddItem(item);	

	    var group = new Monalisa.ToolboxGroup("Funciones Numericas");
	    toolbox.AddGroup(group);
	    var item = new Monalisa.ToolboxItem("Valor Absoluto", "", "functionNumber");
	    group.AddItem(item);
    	
        var item = new Monalisa.ToolboxItem("Redondeo", "", "functionNumber");
	    group.AddItem(item);
    	
        var item = new Monalisa.ToolboxItem("Minimo", "", "functionNumber");
	    group.AddItem(item);
    	
	    var item = new Monalisa.ToolboxItem("Maximo", "", "functionNumber");
	    group.AddItem(item);	

	    var group = new Monalisa.ToolboxGroup("Resultados");
	    toolbox.AddGroup(group);

        for(var i=0; i< this.Results.length; i++){
            var item = new Monalisa.ToolboxItem(this.Results[i].name, this.Results[i].description, "resultado rojo");
            item.ElementRef = this.Results[i];
	        group.AddItem(item);
	    }

    //	var item = new Monalisa.ToolboxItem("Rojo", "resultado rojo");
    //	group.AddItem(item);

	    td1.appendChild(toolbox.CreateHTML());	
		Monalisatag.appendChild(menu.CreateHTML());
		Monalisatag.Editor = this;
		
    }
    
    var td2 = document.createElement('td');
    td2.style.align="left";
    td2.style.verticalAlign="top";
    tr1.appendChild(td2);

	
	var sourceText = document.createElement('span');
	sourceText.id = "sourceText";
	if (this.ModeText){
		sourceText.style.display = "";
	}
	else{
		sourceText.style.display = "none";
	}
	td2.appendChild(sourceText);
	this.sourceText = sourceText;
	


	//workspace
	var workspace = document.createElement('span');
	workspace.id = "workspace";
	this.workspace = workspace;
	if (this.ModeText){
		workspace.style.display = "none";
	}
	else{
		workspace.style.display = "";
	}
	td2.appendChild(workspace);
	
	var vif = document.createElement('span');
	vif.id = "if";
	//vif.draggable = "true";
	//addEvent(vif, 'dragstart', dragStart); 
	//addEvent(vif, 'dragend', dragEnd);

	workspace.appendChild(vif);

	var vtable = document.createElement('table');
	vif.appendChild(vtable);

	var ifRow1 = document.createElement('tr');
	vtable.appendChild(ifRow1);
	var td = document.createElement('td');
	td.style.align="left";
	td.style.verticalAlign="top";
	ifRow1.appendChild(td);
	
	var rombo = document.createElement('span');
	rombo.id = "rombo";
	td.appendChild(rombo);
	rombo.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"30px\" width=\"30px\"><rect x=\"5\" y=\"5\" width=\"20\" height=\"20\" style=\"fill:rgb(255,255,255);stroke-width:0\" transform=\"rotate(45 15 15)\"/></svg>";

	var td = document.createElement('td');
	td.style.align="left";
	td.style.verticalAlign="top";
	ifRow1.appendChild(td);
	
	
	var newcondition = new Monalisa.Node("condition", "if");
	newcondition.Editor = this;
	this.Condition = newcondition;
	td.appendChild(newcondition.CreateHTML());

	var ifRow2 = document.createElement('tr');
	vtable.appendChild(ifRow2);
		
	var td = document.createElement('td');
	td.style.align="left";
	td.style.verticalAlign="top";
	ifRow2.appendChild(td);
	
	var flecha = document.createElement('span');
	flecha.id = "flecha";
	td.appendChild(flecha);
	flecha.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"35px\" width=\"30px\"><path style=\"fill:rgb(255,255,255);stroke-width:0\" d=\"m 0,5 0,4 5,0 0,3 5,-5 -5,-5 0,3 z\" transform=\"scale(2.75)\"/></svg>";

	var td = document.createElement('td');
	td.style.align="left";
	td.style.verticalAlign="top";
	ifRow2.appendChild(td);

	var newresult = new Monalisa.Node("result", "result");
	newresult.Editor = this;
	this.Result = newresult;
	td.appendChild(newresult.CreateHTML());

	
	

	
	//Trash	
	if (!this.ReadOnly){
	    var td3 = document.createElement('td');
	    td3.style.align="left";
	    td3.style.verticalAlign="top";
	    td3.style.width="40px"
	    tr1.appendChild(td3);

	    var trash = document.createElement('span');
	    //trash.appendChild(document.createTextNode("Trash"));
	    trash.id = "trash";
	    trash.className = "trash";
	    td3.appendChild(trash);
	} 
	this.DOMRef = Monalisatag;
	return Monalisatag;
	
}

Monalisa.Editor.prototype.RegisterAllowdrop = function(vtype){
	
	if (vtype == 'resultado')
		this.allowdrop = this.Result.RegisterAllowdrop();
	else
		this.allowdrop = this.Condition.RegisterAllowdrop();
	
	for(var i=0; i< this.allowdrop.length; i++){
		var el = this.allowdrop[i];
		el.allowdrop = true;
		addEvent(el, 'dragover', dragOver);
		addEvent(el, 'dragenter', dragEnter);
	   	addEvent(el, 'dragleave', dragLeave);
	   	addEvent(el, 'drop', dragDrop); 
		
	}
		                
	var trash = document.getElementById("trash");
   	if(trash){
		trash.allowdrop = true;
		addEvent(trash, 'dragover', dragOver);
		addEvent(trash, 'dragenter', dragEnter);
	   	addEvent(trash, 'dragleave', dragLeave);
		addEvent(trash, 'drop', dragDrop); 
	}
	
}

Monalisa.Editor.prototype.DesregisterAllowdrop = function(){
	for(var i=0; i< this.allowdrop.length; i++){
		var el = this.allowdrop[i];
		el.allowdrop = false;
	   	removeEvent(el, 'dragover', dragOver);
	   	removeEvent(el, 'dragenter', dragEnter);
	   	removeEvent(el, 'dragleave', dragLeave);
	   	removeEvent(el, 'drop', dragDrop); 
		
	}
	this.allowdrop = new Array();
		                
}
Monalisa.Editor.prototype.AddField = function(vField){
    this.Fields[this.Fields.length] = vField;
}

Monalisa.Editor.prototype.GetField = function(vFieldName){
    for(var i=0; i< this.Fields.length; i++){
		if (this.Fields[i].name == vFieldName){
			return this.Fields[i];
		};
	}
	return null;
}

Monalisa.Editor.prototype.AddList = function(vList){
    this.Lists[this.Lists.length] = vList;
}

Monalisa.Editor.prototype.AddResult = function(vresult){
    this.Results[this.Results.length] = vresult;
}

//******************************************************************************
//    Manejo de la toolbox
//******************************************************************************

Monalisa.Toolbox = function(){
	this.className = "Monalisa.Toolbox";
	this.groups = new Array();	
}

Monalisa.Toolbox.prototype.CreateHTML = function(){

	var newToolbox = document.createElement("span");        
    	newToolbox.id = 'toolbox';
    	newToolbox.className ='toolbox';
	for(var i=0; i< this.groups.length; i++){
		newToolbox.appendChild(this.groups[i].CreateHTML());
	}
	this.DOMRef = newToolbox;

    	return newToolbox;

}

Monalisa.Toolbox.prototype.AddGroup = function(vgroup){
	this.groups[this.groups.length] = vgroup;	
	vgroup.Editor = this.Editor;
}

Monalisa.ToolboxGroup = function(vname){
	this.className = "Monalisa.ToolboxGroup";
	this.name = vname;	
	this.items = new Array();
}

Monalisa.ToolboxGroup.prototype.CreateHTML = function(){
	
	var newGroup = document.createElement("span");        
    	newGroup.id = 'toolbox'+this.name;
    	newGroup.className ='toolboxgroup';
    var newGroupTitle = document.createElement("span");        
    	newGroupTitle.id = 'toolboxtitle'+this.name;
    	newGroupTitle.className ='toolboxgrouptitle';
        newGroupTitle.appendChild(document.createTextNode(this.name));
        newGroup.appendChild(newGroupTitle);
	
	for(var i=0; i< this.items.length; i++){
		newGroup.appendChild(this.items[i].CreateHTML());
	}
	this.DOMRef = newGroup;

    	return newGroup;

}
Monalisa.ToolboxGroup.prototype.AddItem = function(vitem){
	this.items[this.items.length] = vitem;
	vitem.Editor = this.Editor;
}

Monalisa.ToolboxItem = function(vname, vdescription, vtype){
	this.className = "Monalisa.ToolboxItem";
	this.name = vname;	
	this.description = vdescription;
	this.type = vtype;
	this.DOMRef = null;
    	this.ElementRef = null;
	this.Editor = null;
}

Monalisa.ToolboxItem.prototype.CreateHTML = function(vname){
	
	var newItem = document.createElement("span");        
    newItem.id = 'toolbox'+this.name;
    newItem.className ='toolboxitem';
	newItem.draggable = "true";
	newItem.title = this.description;
	newItem.appendChild(document.createTextNode(this.name));
	addEvent(newItem, 'dragstart', dragStart); 
	addEvent(newItem, 'dragend', dragEnd); 
	
	this.DOMRef = newItem;
	this.DOMRef.ObjRef = this;
    	return newItem;

}

Monalisa.ToolboxItem.prototype.GetEditor = function(){
	return this.Editor;
}

//******************************************************************************
//    Manejo del menu
//******************************************************************************

Monalisa.Menu = function(){
	this.className = "Monalisa.Menu";
	this.items = new Array();	
	this.status = "hide";
}

Monalisa.Menu.prototype.CreateHTML = function(){

	var newmenu = document.createElement("span");        
	newmenu.id = 'menu';
	newmenu.className ='menu';
	
	newmenu.style.position = "absolute";
	newmenu.style.top = 0; //this.getBoundingClientRect().top + this.offsetHeight;
	newmenu.style.left = 0; //this.getBoundingClientRect().left;
	newmenu.style.display = "none";
	newmenu.style.display = "9999";
	//newmenu.style.background = "red";
	
	
	for(var i=0; i< this.items.length; i++){
		newmenu.appendChild(this.items[i].CreateHTML());
	}
	this.DOMRef = newmenu;

	return newmenu;

}

Monalisa.Menu.prototype.Show = function(){
	if (this.status == "visible"){
		this.Hide()
	}
	else{
		var calcuarTop = event.currentTarget.getBoundingClientRect().top + event.currentTarget.offsetHeight - event.currentTarget.parentNode.ObjRef.GetEditor().DOMRef.getBoundingClientRect().top;
		var calcularLeft = event.currentTarget.getBoundingClientRect().left
		this.DOMRef.style.top = calcuarTop+"px";
		this.DOMRef.style.left = calcularLeft+"px";
		this.DOMRef.style.display = "block";
		this.ObjRef = event.currentTarget.parentNode.ObjRef;
		this.status = "starting";
		addEvent(this.Editor.DOMRef, 'click', closeMenu);
	}
	
}

closeMenu = function(){
	//alert('click');
	this.Editor.menu.Hide()
}

Monalisa.Menu.prototype.Hide = function(){
	
	if (this.status == "starting"){
		this.status = "visible"
	}
	else{
		this.status = "hide";
		this.DOMRef.style.display = "none";
		removeEvent(this.Editor.DOMRef, 'click', closeMenu);
	}
	
}

Monalisa.Menu.prototype.AddItem = function(vitem){
	this.items[this.items.length] = vitem;	
	vitem.menu = this;
}

Monalisa.MenuItem = function(vitemname, vaction){
	this.className = "Monalisa.MenuItem";
	this.itemname = vitemname;	
	this.action = vaction;
	this.DOMRef = null;
    this.Editor = null;
}

Monalisa.MenuItem.prototype.CreateHTML = function(){
	
	var vitem = document.createElement("div"); 
	vitem.className ='menuitem';
	vitem.appendChild(document.createTextNode(this.itemname));
//	vitem.menu = menuConstarint;
	addEvent(vitem, 'click', this.action);

	this.DOMRef = vitem;
	this.DOMRef.ObjRef = this;
    return vitem;
}

Monalisa.MenuItem.prototype.Click = function(){

	//cambiar el nodo por la nueva constraint
	this.menu.ObjRef.ChangeConstraint(this.itemname);
	this.menu.Hide();
	
}




//******************************************************************************
//    Definicion de la clase Node
//******************************************************************************

Monalisa.Node = function(vtype, vname, vdescription){
    this.className = "Monalisa.Node";
    this.type = vtype;
    this.name = vname;
	this.description = vdescription;
    this.Children = new Array();
    this.DOMRef = null;  
    this.Debug = "Node:"+this.name+" type"+this.type;              
    this.parent = null;
    this.element = null;
    this.domain = null;
    if ( vtype == "field" || vtype == "list" || vtype == "constant" || vtype == "resultado")
        this.terminal = true;	
    else
        this.terminal = false;
	this.linebreak = false;
	this.domainType = null;
	this.removable = false;
	this.fieldClass = null;
}

Monalisa.Node.prototype.AddChild = function(vnode){
    if (this.removable == true && this.Children.length == 0 && this.DOMRef.children[0]) //Es una condicion removible con el primer hijo
		this.DOMRef.removeChild(this.DOMRef.children[0]);//alert("Eliminar el boton de eliminar condicion");
	this.Children[this.Children.length] = vnode;	
    vnode.parent = this;
    if (this.DOMRef)   
		if (this.name == "OR"){
			var text = document.createTextNode(" "+this.name+" ");
			this.DOMRef.insertBefore(text, this.DOMRef.childNodes[this.DOMRef.childNodes.length-1]);
			this.DOMRef.insertBefore(document.createElement("br"), this.DOMRef.childNodes[this.DOMRef.childNodes.length-1]);
			this.DOMRef.insertBefore(vnode.CreateHTML(), this.DOMRef.childNodes[this.DOMRef.childNodes.length-1]);
		}else	
			this.DOMRef.appendChild(vnode.CreateHTML());
	
		
}


Monalisa.Node.prototype.RemoveChild = function(vnode){
    
	for(var i=0; i< this.Children.length; i++){
        if (this.Children[i] == vnode){
			//Si elimina una condición del OR elimina la condicion y el texto
			if(this.name == "OR"){
				this.DOMRef.removeChild(vnode.DOMRef.previousSibling);//Elimina BR
				this.DOMRef.removeChild(vnode.DOMRef.previousSibling);//Elimina OR
				this.DOMRef.removeChild(vnode.DOMRef);
			}
			else{
				this.DOMRef.removeChild(vnode.DOMRef);//Elimina el DOM
			}	
            this.Children.splice(i, 1);
		}	
    }
	if (this.removable == true && this.Children.length == 0){ //Es una condicion removible con el primer hijo
		//alert("crear el boton de eliminar condicion");
		this.DOMRef.appendChild(CreateEliminarcondition());
	}	
}

Monalisa.Node.prototype.ChangeConstraint = function(vnewconstraint){
	if (this.type == "constraint"){
		this.name = vnewconstraint;
		if (this.DOMRef){
			this.DOMRef.childNodes[1].childNodes[0].nodeValue = vnewconstraint;			
		}
		//Si cambia de constraint a in hay que cambiar el argument por un argumentlist
		if (vnewconstraint == "EN"){
			this.type = "in";
			
			//Copia el argument para dentro del nuevo argument list
			
			var argumentlist = new Monalisa.Node("argumentlist", "argumentlist");
			if (this.Children[1] && this.Children[1].Children[0]) {
				argumentlist.AddChild(this.Children[1]);
			}	
			this.RemoveChild(this.Children[1]);
			this.AddChild(argumentlist);
			//this.DOMRef = null;
			//this.CreateHTML();
			
		}
	}else if(this.type == "in"){
		this.name = vnewconstraint;
		if (this.DOMRef){
			this.DOMRef.childNodes[1].childNodes[0].nodeValue = vnewconstraint;			
		}
		//Si cambia de in a constraint hay que cambiar el argumentlist por un argument con el primer elemento de la lista
		if (vnewconstraint != "EN"){
			this.type = "constraint";
			
			//Copia el argument para dentro del nuevo argument list
			
			var argument = new Monalisa.Node("argument", "argument");
			if (this.Children[1] && this.Children[1].Children[0] && this.Children[1].Children[0].Children[0]) {
				argument.AddChild(this.Children[1].Children[0].Children[0]);
			}
			if (this.Children[1]){
				this.RemoveChild(this.Children[1]);
			}	
			this.AddChild(argument);
			//this.DOMRef = null;
			//this.CreateHTML();
			
		}
	}

}

Monalisa.Node.prototype.GetRoot = function(){
    if (this.parent == null)
	return this;
    else
	return this.parent.GetRoot();
}

Monalisa.Node.prototype.GetEditor = function(){
    return this.GetRoot().Editor;
}


Monalisa.Node.prototype.RegisterAllowdrop = function(){
	var allowdroplist = new Array();
    if (this != Monalisa.dragging.ObjRef){
	    for(var i=0; i< this.Children.length; i++){
            allowdroplist = allowdroplist.concat(this.Children[i].RegisterAllowdrop());
	    }		                
	    if (this.type == "condition" || this.type == "argument" || this.type == "argumentlist" || this.type == "result" )
		    allowdroplist[allowdroplist.length] = this.DOMRef;
    }
	return allowdroplist;
}

Monalisa.Node.prototype.DropNewElement = function(vsource){
    var sourcetype = vsource.type;
    var sourcename = vsource.name;
	var sourcedescription = vsource.description;
	

	if (sourcetype == "field"){
		if (this.type == "argument"){
			if (this.Children[0]){
				//alert('El argumento ya tiene un valor desea reemplazarlo?');
                this.RemoveChild(this.Children[0]);
            }
			var newnode = new Monalisa.Node("field", sourcename, sourcedescription);
			var fieldaux = this.GetEditor().GetField(sourcename);
			if (fieldaux){
				newnode.domain = fieldaux.domain;
				newnode.domainType = fieldaux.type;
				newnode.fieldClass = fieldaux.class;
			}	
			this.AddChild(newnode);
		}else{ if (this.type == "argumentlist"){
			
			var newnode = new Monalisa.Node("field", sourcename, sourcedescription);
			newnode.domainType = vsource.ElementRef.type;
			var argument1 = new Monalisa.Node("argument", "argument1");
			argument1.AddChild(newnode);
			this.AddChild(argument1);
			
		} else{
			var newnode = new Monalisa.Node("constraint", "Es");
			var argument1 = new Monalisa.Node("argument", "argument1");
			newnode.AddChild(argument1);
			var newField = new Monalisa.Node("field", sourcename, sourcedescription);
			var fieldaux = this.GetEditor().GetField(sourcename);
			if (fieldaux){
				newField.domain = fieldaux.domain;
				newField.domainType = fieldaux.type;
				newField.fieldClass = fieldaux.class;
			}
			argument1.AddChild(newField);
			var argument2 = new Monalisa.Node("argument", "argument2");
			newnode.AddChild(argument2);
			var newvalue = new Monalisa.Node("constant", "");
			if (fieldaux){
				newvalue.domain = fieldaux.domain;
				newvalue.domainType = fieldaux.type;
			}
            argument2.AddChild(newvalue);
			this.AddChild(newnode);
			newvalue.DOMRef.children[0].focus();
		}}

	}
    if (sourcetype == "list"){
		if (this.type == "argumentlist"){
			//si tiene algo lo elimina ya que las lista tiene solo un argumento que es la lista
			if (this.Children[0]){
				while(this.Children.length > 0){
					this.RemoveChild(this.Children[0])
				}
				
            }
			var newnode = new Monalisa.Node("list", sourcename, sourcedescription);	
			this.AddChild(newnode);
			
		} else {
			
			var newnode = new Monalisa.Node("in", "EN");
			var argument1 = new Monalisa.Node("argument", "argument1");
			newnode.AddChild(argument1);
			var argument2 = new Monalisa.Node("argumentlist", "argumentlist");
			newnode.AddChild(argument2);
			var newlist = new Monalisa.Node("list", sourcename, sourcedescription);	
			argument2.AddChild(newlist);
			
			this.AddChild(newnode);
		}
	}
	if (sourcetype == "constraint"){
		if (this.type == "condition"){

			var newnode = new Monalisa.Node("constraint", sourcename);
			var argument1 = new Monalisa.Node("argument", "argument1");
			newnode.AddChild(argument1);
			var argument2 = new Monalisa.Node("argument", "argument2");
			newnode.AddChild(argument2);
			this.AddChild(newnode);
		}else{
			alert('no se puede tirar una constraint sobre algo distinto a una condition');
		}
	}
	
	if (sourcetype == "in"){
			
		var newnode = new Monalisa.Node("in", sourcename);
		var argument1 = new Monalisa.Node("argument", "argument1");
		newnode.AddChild(argument1);
		var argumentlist = new Monalisa.Node("argumentlist", "argumentlist");
		newnode.AddChild(argumentlist);
		//var argument1 = new Monalisa.Node("argument", "argument1");
		//argumentlist.AddChild(argument1);
		this.AddChild(newnode);	
	}

	if (sourcetype == "logic"){
			
		var newnode = new Monalisa.Node("logic", sourcename);
		var condition = new Monalisa.Node("condition", "condition1");
		newnode.AddChild(condition);
		if (sourcename == "OR"){
			var condition = new Monalisa.Node("condition", "condition2");
			newnode.AddChild(condition);
		}
		this.AddChild(newnode);	
	}

	if (sourcetype == "arithmetic"){
		var newnode = new Monalisa.Node("arithmetic", sourcename);
		var argument1 = new Monalisa.Node("argument", "argument1");
		argument1.domainType = "Numeric";
		newnode.AddChild(argument1);
		var argument2 = new Monalisa.Node("argument", "argument2");
		argument2.domainType = "Numeric";
		newnode.AddChild(argument2);
		this.AddChild(newnode);			
	}

	if (sourcetype == "functionText"){
			
		var newnode = new Monalisa.Node("functionText", sourcename);
		if (sourcename == "concat"){
			var argument1 = new Monalisa.Node("argument", "argument1");
			argument1.domainType = "Text";
			newnode.AddChild(argument1);
			var argument2 = new Monalisa.Node("argument", "argument2");
			argument2.domainType = "Text";
			newnode.AddChild(argument2);
		}
		if (sourcename == "substr"){
			var argument1 = new Monalisa.Node("argument", "argument1");
			argument1.domainType = "Text";
			newnode.AddChild(argument1);
			var argument2 = new Monalisa.Node("argument", "argument2");
			argument2.domainType = "Numeric";
			newnode.AddChild(argument2);
			var argument3 = new Monalisa.Node("argument", "argument3");
			argument3.domainType = "Numeric";
			newnode.AddChild(argument3);
		}

		this.AddChild(newnode);
	
	}

	if (sourcetype == "functionNumber"){
			
		var newnode = new Monalisa.Node("functionNumber", sourcename);
		if (sourcename == "Valor Absoluto"){
			var argument1 = new Monalisa.Node("argument", "argument1");
			argument1.domainType = "Numeric";
			newnode.AddChild(argument1);
		}
		if (sourcename == "Redondeo"){
			var argument1 = new Monalisa.Node("argument", "argument1");
			argument1.domainType = "Numeric";
			newnode.AddChild(argument1);
		}
		if (sourcename == "Minimo"){
			var argument1 = new Monalisa.Node("argument", "argument1");
			argument1.domainType = "Numeric";
			newnode.AddChild(argument1);
			var argument2 = new Monalisa.Node("argument", "argument2");
			argument2.domainType = "Numeric";
			newnode.AddChild(argument2);
		}
		if (sourcename == "Maximo"){
			var argument1 = new Monalisa.Node("argument", "argument1");
			argument1.domainType = "Numeric";
			newnode.AddChild(argument1);
			var argument2 = new Monalisa.Node("argument", "argument2");
			argument2.domainType = "Numeric";
			newnode.AddChild(argument2);
		}
		this.AddChild(newnode);
	}

	if (sourcetype == "constant"){
		if (this.type == "argument"){
			if (this.Children[0]){
				alert('El argumento ya tiene un valor');
			}else{
				var newnode = new Monalisa.Node("constant", "");
				if (this.parent.Children[0].Children[0] && this.parent.Children[0].Children[0].type == "field"){ 
					newnode.domain = this.parent.Children[0].Children[0].domain;
				}
				this.AddChild(newnode);
				newnode.DOMRef.children[0].focus();
			}
		} else { if (this.type == "argumentlist"){
			
			var newnode = new Monalisa.Node("constant", "");
			var argument1 = new Monalisa.Node("argument", "argument1");
			argument1.linebreak = true;
			argument1.AddChild(newnode);
			if (this.parent.Children[0].Children[0] && this.parent.Children[0].Children[0].type == "field"){ 
				newnode.domain = this.parent.Children[0].Children[0].domain;
			}
			this.AddChild(argument1);
			newnode.DOMRef.children[0].focus();
		} else {
			alert("el tipo del destino no es valido para una constante");
		}}


	}

	if (sourcetype.split(" ",1) == "resultado"){
	    var existe = false;
	    for(var i=0; i< this.Children.length; i++){
	        if(this.Children[i].name == sourcename){
	            existe = true;
	            break;
	        }
	    }
		if(existe){	
		    alert('Este resultado ya fue definido');    
		}else{
		    var newnode = new Monalisa.Node("resultado", sourcename, sourcedescription);
		    for(var i=0; i< vsource.ElementRef.args.length; i++){
	            var newnodearg = new Monalisa.Node(vsource.ElementRef.args[i].type, vsource.ElementRef.args[i].value);
	            if (vsource.ElementRef.args[i].domain)
	                newnodearg.domain = vsource.ElementRef.args[i].domain;
		        newnode.AddChild(newnodearg);
		    }     
		    this.AddChild(newnode);
		    
		}
	}

}

Monalisa.Node.prototype.DropRemoveElement = function(){
	//Si elemino una constante dentro de un argumentlist tambien elimina el argument
	if (this.type == "constant" && this.parent.parent.type == "argumentlist"){
		this.parent.parent.RemoveChild(this.parent);
	}else{
		this.parent.RemoveChild(this);
	}
	/*
	this.DOMRef.parentNode.removeChild(this.DOMRef);
	this.DOMRef = null;
	if (this.parent){
        for(var i=0; i< this.parent.Children.length; i++){
            if (this.parent.Children[i] == this)
                this.parent.Children.splice(i, 1);
        }
    }
	*/
}

Monalisa.Node.prototype.DropMoveElement = function(velement){
	var newElement = velement;
    velement.parent.RemoveChild(velement)
    this.AddChild(newElement);
}

Monalisa.Node.prototype.CreateHTML = function(){

    if (this.DOMRef)
        return this.DOMRef;

    //Create HTML
    var newNode = document.createElement("span");        
    newNode.id = this.type+this.name;
	if (this.type == "field")
		newNode.className =this.fieldClass;
	else
		newNode.className =this.type;
	if (this.linebreak){
		newNode.setAttribute("style", "display:block;");
	}
    
    //Si el nodo se puede eliminar o mover
    
    if (!this.GetEditor().ReadOnly && this.type != "condition" && this.type != "argument" && this.type != "argumentlist" && this.type != "result"){
	    newNode.draggable = "true";
    	addEvent(newNode, 'dragstart', dragStart); 
    	addEvent(newNode, 'dragend', dragEnd);
    }
    if (this.type == "field"){
	    var text = document.createTextNode(this.name);
		newNode.appendChild(text);
		newNode.title = this.description;
    }
    if (this.type == "list"){
	    var text = document.createTextNode(this.name);
	    newNode.appendChild(text);
		newNode.title = this.description;
    }
    if (this.type == "constraint"){
		newNode.appendChild(this.Children[0].CreateHTML());
		//crear span con constrain
		var constarinttext = document.createElement("span");        
		constarinttext.className = "constraintText";
		constarinttext.appendChild(document.createTextNode(" "+this.name+" "));
		if (!this.GetEditor().ReadOnly){
			var constraintarrowdropdown = document.createElement('span');
			constraintarrowdropdown.id = "menuarrow";
			constarinttext.appendChild(constraintarrowdropdown);
			constraintarrowdropdown.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" height=\"8px\" width=\"8px\"><polygon points=\"0,0  8,0  4,8\" style=\"stroke:#000000; fill:#000000;\"/></svg>";
		}
		addEvent(constarinttext, 'click', MenuChangeConstraintShow);
    	
		newNode.appendChild(constarinttext);
    	newNode.appendChild(this.Children[1].CreateHTML());
    }
    if (this.type == "in"){
	    newNode.appendChild(this.Children[0].CreateHTML());
    	//crear span con constrain
		var constarinttext = document.createElement("span");        
		constarinttext.className = "constraintText";
		constarinttext.appendChild(document.createTextNode(" "+this.name+" "));
		if (!this.GetEditor().ReadOnly){
			var constraintarrowdropdown = document.createElement('span');
			constraintarrowdropdown.id = "menuarrow";
			constarinttext.appendChild(constraintarrowdropdown);
			constraintarrowdropdown.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" height=\"8px\" width=\"8px\"><polygon points=\"0,0  8,0  4,8\" style=\"stroke:#000000; fill:#000000;\"/></svg>";
		}
		addEvent(constarinttext, 'click', MenuChangeConstraintShow);
    	
    	newNode.appendChild(constarinttext);
    	newNode.appendChild(this.Children[1].CreateHTML());
    }
    if (this.type == "argument"){
	if (this.Children[0])
		newNode.appendChild(this.Children[0].CreateHTML());
    }
    if (this.type == "argumentlist"){
	for (var i=0; i < this.Children.length; i++){
		this.Children[i].linebreak = true;
	    newNode.appendChild(this.Children[i].CreateHTML());
//		newNode.appendChild(document.createElement("br"));
	}
    }
    if (this.type == "constant"){
	    
        if (this.domain){
            var value = document.createElement("select");
            if(this.GetEditor().ReadOnly){
                value.disabled = true;
            }
            value.ObjRef = this;
            addEvent(value, 'change', function() { this.ObjRef.name = this.options[this.selectedIndex].value});
        
            for (var i=0; i < this.domain.items.length; i++){
                var option = document.createElement("option");
                value.appendChild(option);
                option.text = this.domain.items[i].description;
                option.value = this.domain.items[i].value;
                if(this.domain.items[i].value == this.name)
                    value.selectedIndex = i;
            }
            this.name = value.options[value.selectedIndex].value
        }
        else{
            var value = document.createElement("input");
            if(this.GetEditor().ReadOnly){
                value.disabled = true;
            }
            value.ObjRef = this;
            value.setAttribute("type", "text");
            value.setAttribute("value", this.name);
			value.setAttribute("style", "display:inline-block;");
            
			//Obtiene el campo asociado con la constante
			var vfildaux = null;
			//sube hasta la primer constraint o in
			var vaux = this.parent;
			while(vaux && vaux.type != "constraint" && vaux.type != "in" && vaux != null){
				if(vaux.parent){
					vaux = vaux.parent;
				}else{
					vaux = null;
				}
			}
			if (vaux && vaux.Children[0].Children[0] && vaux.Children[0].Children[0].type == "field"){ 
				var sourcename = vaux.Children[0].Children[0].name;
				vfildaux = this.GetEditor().GetField(sourcename);
				var vfieldsize = vfildaux.length;
				var vpattern = vfildaux.pattern;
				var vpatternmessage = vfildaux.patternMessage;
				var vsuggestId = vfildaux.suggestId;
			}
			if(vfieldsize){
				vfieldmaxlength = vfieldsize;
			}else{	
				vfieldmaxlength = 100;
				vfieldsize = 10;
			}	
			/*
			if (vsuggestId != ""){
				vfieldmaxlength = 100;
				vfieldsize = 60;
			}
			*/
			value.setAttribute("maxlength",vfieldmaxlength);
			value.setAttribute("size",vfieldsize);
			if (this.domainType == "Number" && vsuggestId == ""){
				value.setAttribute("onkeypress", "return isNumberKey(event)");
			}
			if (vpattern && vpattern.replace(/\s/g,"") != "" && vsuggestId == ""){
				value.setAttribute("onblur", "validateRegExp(this,'" +vpattern +"', '" +vpatternmessage + "')");
			}	
            addEvent(value, 'change', function() { this.ObjRef.name = this.value});
			
			if(vsuggestId){
				
				//Si tiene un valor cargado busca la descripcion del suggest
				if (value.value != ""){
					$((function(x, y) { return function() {
						$.getJSON("awsdominiovalores.aspx?"+y+" ,"+x.value, function(result){
							if (result.suggestions[0]){
								x.value = result.suggestions[0].value;
								x.title = x.value;
							}	
						})
						
					}})(value, vsuggestId));
				}
				
				//si es un suggest se agrega el codigo
				$((function(x, y) { return function() {
					/*
					var currencies = [
						{ value: 'Alejandro', data: 'AFN' },
						{ value: 'Albanian lek', data: 'ALL' },
						{ value: 'Algerian dinar', data: 'DZD' },
					  ];
					  */
					$(x).autocomplete({
						delay: 5001,
						//lookup: currencies,
						serviceUrl: 'awsdominiovalores.aspx?'+y+',',
						minChars: 3,
						width:600,
						onSelect: function (suggestion) {
							x.value = suggestion.value;
							x.title = x.value;
							x.ObjRef.name = suggestion.data; 
							//alert('You selected: ' + suggestion.value + ', ' + suggestion.data);
						}
					});
				}})(value, vsuggestId));
			}
			
        }
             
	    newNode.appendChild(value);
    }
    if (this.type == "logic"){
	var text = document.createTextNode(" "+this.name+" ");
	if (this.Children.length == 1){
		newNode.appendChild(text);
		//newNode.appendChild(document.createElement("br"));
	}
    for (var i=0; i < this.Children.length; i++){
			if (i > 0){
				newNode.appendChild(document.createTextNode(" "+this.name+" "));
				newNode.appendChild(document.createElement("br"));
			}
			if (i > 1){
				this.Children[i].removable = true;
			}	
	    	newNode.appendChild(this.Children[i].CreateHTML());
		//newNode.appendChild(document.createElement("br"));
	}
	if (!this.GetEditor().ReadOnly && this.name == "OR"){
		var agregarcondicion = document.createElement("span");        
		newNode.id = "agregarcondicion";
		agregarcondicion.title="Agregar condición"
		//agregarcondicion.appendChild(document.createTextNode(" Agregar condición "));
		agregarcondicion.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"16px\" width=\"16px\"><path id=\"mas\" style=\"fill:rgb(0,0,0);stroke-width:0\" d=\"m0.92001,6.17469l4.75971,0l0,-4.75968l4.48061,0l0,4.75968l4.75968,0l0,4.48063l-4.75968,0l0,4.75968l-4.48061,0l0,-4.75968l-4.75971,0l0,-4.48063z\"/></svg>";
		addEvent(agregarcondicion, 'click', AgregarCondition);
		newNode.appendChild(agregarcondicion);
		
		
	
	}	
    }
    if (this.type == "condition"){
		if (!this.GetEditor().ReadOnly && this.removable && this.Children.length == 0){
			newNode.appendChild(CreateEliminarcondition());
		}
		for (var i=0; i < this.Children.length; i++){
				newNode.appendChild(this.Children[i].CreateHTML());
		}
    }
    if (this.type == "arithmetic"){
	    newNode.appendChild(this.Children[0].CreateHTML());
    	var text = document.createTextNode(" "+this.name+" ");
    	newNode.appendChild(text);
    	newNode.appendChild(this.Children[1].CreateHTML());
    }

    if (this.type == "functionText"){
	var text = document.createTextNode(" "+this.name+" ");
    	newNode.appendChild(text);
	for (var i=0; i < this.Children.length; i++){
	    	newNode.appendChild(this.Children[i].CreateHTML());
	}
    }
	
    if (this.type == "functionNumber"){
	var text = document.createTextNode(" "+this.name+" ");
    	newNode.appendChild(text);
	for (var i=0; i < this.Children.length; i++){
	    	newNode.appendChild(this.Children[i].CreateHTML());
	}
    }

    if (this.type == "resultado"){
	    var text = document.createTextNode(this.name);
	    newNode.appendChild(text);
		newNode.title = this.description;
	    for (var i=0; i < this.Children.length; i++){
	        newNode.appendChild(this.Children[i].CreateHTML());
	    }
	        
        //<input type="number" name="quantity" min="1" max="5" />
        
        /*var value = document.createElement("input");
 	    if(this.GetEditor().ReadOnly){
            value.disabled = true;
        }
        value.setAttribute("type", "number");
        value.setAttribute("min","1");
        value.setAttribute("max","100");
        if(!this.porcent)
            this.porcent = 100;
	    value.setAttribute("value", this.porcent);
        value.setAttribute("pattern", "[0-9.]+");
        //value.setAttribute("style", "display:inline-block;");
        newNode.appendChild(value);
        newNode.appendChild(document.createTextNode("%"));
        value.ObjRef = this;
        addEvent(value, 'change', function() { this.ObjRef.porcent = this.value});
        */
    }
    
    if (this.type == "percent"){
	    
	    var value = document.createElement("input");
 	    if(this.GetEditor().ReadOnly){
            value.disabled = true;
        }
        value.setAttribute("type", "number");
        value.setAttribute("min","1");
        value.setAttribute("max","100");
        value.setAttribute("value", this.name);
        value.setAttribute("pattern", "[0-9.]+");
        //value.setAttribute("style", "display:inline-block;");
        newNode.appendChild(value);
        newNode.appendChild(document.createTextNode("%"));
        value.ObjRef = this;
        addEvent(value, 'change', function() { this.ObjRef.name = this.value});
        
    }
    
    if (this.type == "mail"){
    
	    if (this.domain){
            var value = document.createElement("select");
            if(this.GetEditor().ReadOnly){
                value.disabled = true;
            }
            value.ObjRef = this;
            addEvent(value, 'change', function() { this.ObjRef.name = this.options[this.selectedIndex].value});
        
            for (var i=0; i < this.domain.items.length; i++){
                var option = document.createElement("option");
                value.appendChild(option);
                option.text = this.domain.items[i].description;
                option.value = this.domain.items[i].value;
                if(this.domain.items[i].value == this.name)
                    value.selectedIndex = i;
            }
            this.name = value.options[value.selectedIndex].value
        }
        else{
            var value = document.createElement("input");
            if(this.GetEditor().ReadOnly){
                value.disabled = true;
            }
            value.ObjRef = this;
            value.setAttribute("type", "text");
            value.setAttribute("value", this.name);
            value.setAttribute("style", "display:inline-block;");
            value.setAttribute("maxlength", 100);
            value.setAttribute("size",10);
            addEvent(value, 'change', function() { this.ObjRef.name = this.value});
        }
             
	    newNode.appendChild(value);
    }
    
    //Crear referencias
    this.DOMRef = newNode;
    this.DOMRef.ObjRef = this;


    return newNode;    
};

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : evt.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46)
        return false;
    return true;
}

function validateRegExp(obj, vpattern, vpatternMessage){
	var re = new RegExp(vpattern);
	if (!re.test(obj.value)) 
	{
		alert(vpatternMessage);
		obj.focus()
	} 
} 

AgregarCondition = function(){
	//alert("nueva condición:"+this.parentNode.ObjRef.name);
	var condition = new Monalisa.Node("condition", "conditionN");
	condition.removable = true;
	this.parentNode.ObjRef.AddChild(condition);
			
}

EliminarCondition = function(){
	//alert("eliminar condición:"+this.parentNode.ObjRef.name);
	this.parentNode.parentNode.ObjRef.RemoveChild(this.parentNode.ObjRef);
			
}

CreateEliminarcondition = function(){
	var eliminarcondicion = document.createElement("span");        
	eliminarcondicion.id = "eliminarcondicion";
	eliminarcondicion.title="Eliminar condición"
	//eliminarcondicion.appendChild(document.createTextNode(" Eliminar condición "));
	eliminarcondicion.innerHTML = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"16px\" width=\"16px\"><path id=\"eliminar\" transform=\"rotate(-45 7.92000961303711,8.415004730224608) \" style=\"fill:rgb(0,0,0);stroke-width:0\" d=\"m0.92001,6.17469l4.75971,0l0,-4.75968l4.48061,0l0,4.75968l4.75968,0l0,4.48063l-4.75968,0l0,4.75968l-4.48061,0l0,-4.75968l-4.75971,0l0,-4.48063z\"/></svg>";
	addEvent(eliminarcondicion, 'click', EliminarCondition);
	return eliminarcondicion;
}

MenuChangeConstraintShow = function() {
	event.currentTarget.parentNode.ObjRef.GetEditor().menu.Show();
}
 
MenuChangeContraintClick = function(){
	
	event.target.ObjRef.Click();
	
}
			
Monalisa.Node.prototype.Validate = function(){
    var nodeval = "";
    if( this.terminal)
        return "OK";
    for(var i =0; i< this.Children.length; i++){
        nodeval = this.Children[i].Validate();
        if ( nodeval != "OK"){
            return nodeval;
        }
    }
    if ( nodeval == "OK"){
        return "OK";
    }else{
        return "Error: falta una expresión en " + this.name;
    }

}

Monalisa.Node.prototype.Compile = function(){
    var tokenlist = new Array();
    switch(this.type){
        case "condition":
            for(var i =0; i< this.Children.length; i++){
                 tokenlist = tokenlist.concat(this.Children[i].Compile());
                 if (i < this.Children.length - 1){
                    //alert(" Y ");
                    var newtoken = new Monalisa.Token('logicAND', 'AND');
                    tokenlist[tokenlist.length] = newtoken;
                 }
            }
            break;
        case "logic":
            if (this.name == "NOT"){
                //alert("NO");
                var newtoken = new Monalisa.Token('logicNOT', 'NOT');
                tokenlist[tokenlist.length] = newtoken;
                var tokenlistaux = this.Children[0].Compile();
                if (tokenlistaux.length > 3){
                    var newtoken = new Monalisa.Token('parizq', '(');
                    tokenlist[tokenlist.length] = newtoken;
                }
                tokenlist = tokenlist.concat(tokenlistaux);
                if (tokenlistaux.length > 3){
                    var newtoken = new Monalisa.Token('parder', ')');
                    tokenlist[tokenlist.length] = newtoken;
                }
            }else{
                //alert("O");
                
                var newtoken = new Monalisa.Token('parizq', '(');
                tokenlist[tokenlist.length] = newtoken;

				for(var i=0; i< this.Children.length; i++){
					var tokenlistaux = this.Children[i].Compile();
                
					if (tokenlistaux.length > 3){
						var newtoken = new Monalisa.Token('parizq', '(');
						tokenlist[tokenlist.length] = newtoken;
					}
					tokenlist = tokenlist.concat(tokenlistaux);
					if (tokenlistaux.length > 3){
						var newtoken = new Monalisa.Token('parder', ')');
						tokenlist[tokenlist.length] = newtoken;
					}
					if( i<this.Children.length-1){
						var newtoken = new Monalisa.Token('logicOR', 'OR');
						tokenlist[tokenlist.length] = newtoken;
					}
				}
				
				var newtoken = new Monalisa.Token('parder', ')');
                tokenlist[tokenlist.length] = newtoken;
                
				
            }
            break;
        case "constraint":
            tokenlist = tokenlist.concat(this.Children[0].Compile());
            //alert(this.name);
            var newtoken = new Monalisa.Token('constraint', this.name);
            tokenlist[tokenlist.length] = newtoken;
            tokenlist = tokenlist.concat(this.Children[1].Compile());
            break;
        case "argument":
            tokenlist = tokenlist.concat(this.Children[0].Compile());
            break;
        case "field":
            //alert(this.name);
            var newtoken = new Monalisa.Token(this.fieldClass, this.name);
            tokenlist[tokenlist.length] = newtoken;
            break;
		case "model":
            //alert(this.name);
            var newtoken = new Monalisa.Token("model", this.name);
            tokenlist[tokenlist.length] = newtoken;
            break;
		case "catalog":
            //alert(this.name);
            var newtoken = new Monalisa.Token("catalog", this.name);
            tokenlist[tokenlist.length] = newtoken;
            break;	
        case "constant":
            //alert(this.name);
            if(this.ComputeDomainType() == "Text")
                var newtoken = new Monalisa.Token('constantText', this.name);
            else
                var newtoken = new Monalisa.Token('constantNum', this.name);
				tokenlist[tokenlist.length] = newtoken;
            break;
        case "arithmetic":
            var tokenlistaux = this.Children[0].Compile();
            if (tokenlistaux.length > 1) {
                var newtoken = new Monalisa.Token('parizq', '(');
                tokenlist[tokenlist.length] = newtoken;
            }
            tokenlist = tokenlist.concat(tokenlistaux);
            if (tokenlistaux.length > 1) {
                var newtoken = new Monalisa.Token('parder', ')');
                tokenlist[tokenlist.length] = newtoken;
            }
            var newtoken = new Monalisa.Token('arithmetic', this.name);
            tokenlist[tokenlist.length] = newtoken;
            var tokenlistaux = this.Children[1].Compile();
            if (tokenlistaux.length > 1) {
                var newtoken = new Monalisa.Token('parizq', '(');
                tokenlist[tokenlist.length] = newtoken;
            }
            tokenlist = tokenlist.concat(tokenlistaux);
            if (tokenlistaux.length > 1) {
                var newtoken = new Monalisa.Token('parder', ')');
                tokenlist[tokenlist.length] = newtoken;
            }
            break;
        case "in":
            tokenlist = tokenlist.concat(this.Children[0].Compile());
            var newtoken = new Monalisa.Token('in', this.name);
            tokenlist[tokenlist.length] = newtoken;
            tokenlist = tokenlist.concat(this.Children[1].Compile());
            break;
        case "argumentlist":
            if (this.Children[0].type == "list"){
                var newtoken = new Monalisa.Token('list', this.Children[0].name);
                tokenlist[tokenlist.length] = newtoken;
            }else{
                var newtoken = new Monalisa.Token('parrecizq', "[");
                tokenlist[tokenlist.length] = newtoken;
                for(var i =0; i< this.Children.length; i++){
                     tokenlist = tokenlist.concat(this.Children[i].Compile());
                     if (i < this.Children.length - 1){
                        //alert(" , ");
                        var newtoken = new Monalisa.Token('coma', ',');
                        tokenlist[tokenlist.length] = newtoken;
                     }
                }
                var newtoken = new Monalisa.Token('parrecder', "]");
                tokenlist[tokenlist.length] = newtoken;
            }
            break;
        case "functionNumber":
			switch(this.name){
				case "Valor Absoluto":
					var newtoken = new Monalisa.Token('functionNumber', 'abs');
					tokenlist[tokenlist.length] = newtoken;
					var newtoken = new Monalisa.Token('parizq', '(');
					tokenlist[tokenlist.length] = newtoken;
					var tokenlistaux = this.Children[0].Compile();
					tokenlist = tokenlist.concat(tokenlistaux);
					var newtoken = new Monalisa.Token('parder', ')');
					tokenlist[tokenlist.length] = newtoken;
					break;
				case "Redondeo":
					var newtoken = new Monalisa.Token('functionNumber', 'round');
					tokenlist[tokenlist.length] = newtoken;
					var newtoken = new Monalisa.Token('parizq', '(');
					tokenlist[tokenlist.length] = newtoken;
					var tokenlistaux = this.Children[0].Compile();
					tokenlist = tokenlist.concat(tokenlistaux);
					var newtoken = new Monalisa.Token('parder', ')');
					tokenlist[tokenlist.length] = newtoken;
					break;
				case "Minimo":
					var newtoken = new Monalisa.Token('functionNumber', 'min');
					tokenlist[tokenlist.length] = newtoken;
					var newtoken = new Monalisa.Token('parrecizq', "[");
					tokenlist[tokenlist.length] = newtoken;
					var tokenlistaux = this.Children[0].Compile();
					tokenlist = tokenlist.concat(tokenlistaux);
					var newtoken = new Monalisa.Token('coma', ',');
					tokenlist[tokenlist.length] = newtoken;
					var tokenlistaux = this.Children[1].Compile();
					tokenlist = tokenlist.concat(tokenlistaux);
					var newtoken = new Monalisa.Token('parrecder', "]");
					tokenlist[tokenlist.length] = newtoken;
					break;
				case "Maximo":
					var newtoken = new Monalisa.Token('functionNumber', 'max');
					tokenlist[tokenlist.length] = newtoken;
					var newtoken = new Monalisa.Token('parrecizq', "[");
					tokenlist[tokenlist.length] = newtoken;
					var tokenlistaux = this.Children[0].Compile();
					tokenlist = tokenlist.concat(tokenlistaux);
					var newtoken = new Monalisa.Token('coma', ',');
					tokenlist[tokenlist.length] = newtoken;
					var tokenlistaux = this.Children[1].Compile();
					tokenlist = tokenlist.concat(tokenlistaux);
					var newtoken = new Monalisa.Token('parrecder', "]");
					tokenlist[tokenlist.length] = newtoken;
					break;
				default:
					alert("functionNumber no implementado:"+this.name);
			}
            break;
		case "functionText":
			if (this.name == "concat"){
				var newtoken = new Monalisa.Token('functionText', 'concat');
                tokenlist[tokenlist.length] = newtoken;
                var newtoken = new Monalisa.Token('parizq', '(');
                tokenlist[tokenlist.length] = newtoken;
                var tokenlistaux = this.Children[0].Compile();
                tokenlist = tokenlist.concat(tokenlistaux);
                var newtoken = new Monalisa.Token('coma', ',');
                tokenlist[tokenlist.length] = newtoken;
				var tokenlistaux = this.Children[1].Compile();
                tokenlist = tokenlist.concat(tokenlistaux);
				var newtoken = new Monalisa.Token('parder', ')');
                tokenlist[tokenlist.length] = newtoken;
			}else{
                if (this.name == "substr"){
					var newtoken = new Monalisa.Token('functionText', 'substr');
					tokenlist[tokenlist.length] = newtoken;
					var newtoken = new Monalisa.Token('parizq', '(');
					tokenlist[tokenlist.length] = newtoken;
					var tokenlistaux = this.Children[0].Compile();
					tokenlist = tokenlist.concat(tokenlistaux);
					var newtoken = new Monalisa.Token('coma', ',');
					tokenlist[tokenlist.length] = newtoken;
					var tokenlistaux = this.Children[1].Compile();
					tokenlist = tokenlist.concat(tokenlistaux);
					var newtoken = new Monalisa.Token('coma', ',');
					tokenlist[tokenlist.length] = newtoken;
					var tokenlistaux = this.Children[2].Compile();
					tokenlist = tokenlist.concat(tokenlistaux);
					var newtoken = new Monalisa.Token('parder', ')');
					tokenlist[tokenlist.length] = newtoken;
				}else{
					alert("functionText no implementado:"+this.name);
				}
            }
			break;	
        case "result":
            var newtoken = new Monalisa.Token('parrecizq', "[");
            tokenlist[tokenlist.length] = newtoken;
            for(var i =0; i< this.Children.length; i++){
                 tokenlist = tokenlist.concat(this.Children[i].Compile());
                 if (i < this.Children.length - 1){
                    var newtoken = new Monalisa.Token('coma', ',');
                    tokenlist[tokenlist.length] = newtoken;
                 }
            }
            var newtoken = new Monalisa.Token('parrecder', "]");
            tokenlist[tokenlist.length] = newtoken;
            break;
        case "resultado":
            var newtoken = new Monalisa.Token("resultado", this.name);
            tokenlist[tokenlist.length] = newtoken;
            for(var i =0; i< this.Children.length; i++){
                var newtoken = new Monalisa.Token(this.Children[i].type, this.Children[i].name);
                tokenlist[tokenlist.length] = newtoken;
            }
            
            //var newtoken = new Monalisa.Token("porcent", this.porcent);
            //tokenlist[tokenlist.length] = newtoken;
            break;
        default:
            alert("typo de nodo no implementado:"+this.type);
    }
    return tokenlist;
}


Monalisa.Node.prototype.ComputeDomainType = function(){
	var vdomainType = null;
	if (this.parent){
		if (this.parent.domainType){
			vdomainType = this.parent.domainType;
		}else{	
			vdomainType = this.parent.FindChildDomainType();
			if (! vdomainType){
				vdomainType = this.parent.ComputeDomainType();
			}
		}	
	}
	return vdomainType;
}

Monalisa.Node.prototype.FindChildDomainType = function(){
	var vdomainType = null;
	for(var i =0; i< this.Children.length; i++){
		if (this.Children[i].domainType){
			vdomainType = this.Children[i].domainType;
			break;
		}	
		vdomainType = this.Children[i].FindChildDomainType()
		if (vdomainType)
			break;
	}
	return vdomainType;
}
//******************************************************************************
//    Definicion de la clase Token
//******************************************************************************

Monalisa.Token = function(vtype, vvalue){
    this.className = "Monalisa.Token";
    this.type = vtype;
    this.value = vvalue;
    if ( vtype == "field" || vtype == "model" || vtype == "catalog" || vtype == "list" || vtype == "constantText"|| vtype == "constantNum")
        this.terminal = true;	
    else
        this.terminal = false;
    if (  vtype == "list")
		this.tree = new Monalisa.Node(vtype, vvalue);	
	else
		if(vtype == "field" || vtype == "model" || vtype == "catalog"){
			this.tree = new Monalisa.Node("field", vvalue);	
			this.tree.fieldClass = vtype;
		}
		else
			if ( vtype == "constantText"|| vtype == "constantNum")
				this.tree = new Monalisa.Node("constant", vvalue);	
			else
				this.tree = null;

    switch(this.value){
        case "AND":
            this.tag = "Y";
            break;
        case "OR":
            this.tag = "O";
            break;
        case "NOT":
            this.tag = "NO";
            break;
        case "Es":
            this.tag = "=";
            break;
        case "No Es":
            this.tag = "<>";
            break;
        case "Mayor":
            this.tag = ">";
            break;
        case "Mayor o igual":
            this.tag = ">=";
            break;
        case "Menor":
            this.tag = "<";
            break;
        case "Menor o igual":
            this.tag = "<=";
            break;
        default:
            this.tag = this.value;
    }
    if ( vtype == "constantText")
        this.tag = "\""+this.value+"\"";
    


}

Monalisa.Editor.prototype.Parse = function(vToken){
    this.ParseFase(vToken, 1);
	//si esta en modo texto muestra el codigo
	if (this.ModeText){
		if(this.sourceText.firstChild)
			this.sourceText.removeChild(this.sourceText.firstChild)
		this.sourceText.appendChild(document.createTextNode(this.Compile()));
	}
	
}

Monalisa.Editor.prototype.ParseFase = function(vToken, vfase){
	

    if (vToken.length == 0){
        return;
    }
	if (vToken.length == 1){
		for(var j =0; j< vToken[0].tree.Children.length; j++){
			this.Condition.AddChild(vToken[0].tree.Children[j]);
		}	
		for(var k=0; k < vToken[0].tree2.Children.length; k++){
			this.Result.AddChild(vToken[0].tree2.Children[k]);
		}
		//Monalisa.Condition = vToken[0].tree;
		return;
	}
	
	for(var i=0; i < vToken.length; i++){
		
		vToken1 = vToken[i];
        if ( i+1 < vToken.length)
		    vToken2 = vToken[i+1];
        else
            vToken2 = null;
		if ( i+2 < vToken.length)
			vToken3 = vToken[i+2];
		else
			vToken3 = null;
		if ( i+3 < vToken.length)
			vToken4 = vToken[i+3];
		else
			vToken4 = null;
		if ( i+4 < vToken.length)
			vToken5 = vToken[i+4];
		else
			vToken5 = null;
		if ( i+5 < vToken.length)
			vToken6 = vToken[i+5];
		else
			vToken6 = null;
		if ( i+6 < vToken.length)
			vToken7 = vToken[i+6];
		else
			vToken7 = null;
		if ( i+7 < vToken.length)
			vToken8 = vToken[i+7];
		else
			vToken8 = null;
		var vnewTokenList = new Array();



//Producciones con 1 elementos
        // argument :: campo
        if (vToken1.type == 'field' || vToken1.type == 'model' || vToken1.type == 'catalog'){

            for(var j=0; j< this.Fields.length; j++){
                if (this.Fields[j].name == vToken1.tree.name){
                     vToken1.tree.ElementRef = this.Fields[j];
					 vToken1.tree.domainType = this.Fields[j].type;
					 vToken1.tree.description = this.Fields[j].description;
                     break;
                }
	        }
			
			var newargument = new Monalisa.Node("argument", "");
			newargument.AddChild(vToken1.tree);
		    
            var newToken = new Monalisa.Token('argument', '');
            newToken.tree = newargument;

			vnewTokenList = vToken.slice(0, i);
			vnewTokenList[vnewTokenList.length] = newToken;
			vnewTokenList = vnewTokenList.concat(vToken.slice(i+1));
			
			this.ParseFase(vnewTokenList, vfase);
			return;	
		}
        // argument :: constant
        if (vToken1.type == 'constantText' || vToken1.type == 'constantNum'){

			var newargument = new Monalisa.Node("argument", "");
			newargument.AddChild(vToken1.tree);
		    
            var newToken = new Monalisa.Token('argument', '');
            newToken.tree = newargument;

			vnewTokenList = vToken.slice(0, i);
			vnewTokenList[vnewTokenList.length] = newToken;
			vnewTokenList = vnewTokenList.concat(vToken.slice(i+1));
			
			this.ParseFase(vnewTokenList, vfase);
			return;	
		}
        // argumentlist :: list
        if (vToken1.type == 'list'){

			for(var j=0; j< this.Lists.length; j++){
                if (this.Lists[j].name == vToken1.tree.name){
                     vToken1.tree.description = this.Lists[j].description;
                     break;
                }
	        }
			
            var newargument = new Monalisa.Node("argumentlist", "");
			newargument.AddChild(vToken1.tree);
		    
            var newToken = new Monalisa.Token('argumentlist', '');
            newToken.tree = newargument;

			vnewTokenList = vToken.slice(0, i);
			vnewTokenList[vnewTokenList.length] = newToken;
			vnewTokenList = vnewTokenList.concat(vToken.slice(i+1));
			
			this.ParseFase(vnewTokenList, vfase);
			return;	
		}
        // arithmetic :: arithmeticPlus
        if (vToken1.type == 'arithmeticPlus'){

			var newnode = new Monalisa.Node("arithmetic", "+");
			//newnode.AddChild(vToken1.tree);
			
			var newToken = new Monalisa.Token('arithmetic', '+');
            newToken.tree = newnode;

			vnewTokenList = vToken.slice(0, i);
			vnewTokenList[vnewTokenList.length] = newToken;
			vnewTokenList = vnewTokenList.concat(vToken.slice(i+1));
			
			this.ParseFase(vnewTokenList, vfase);
			return;	
		}

        // arithmetic :: arithmeticMinus
        if (vToken1.type == 'arithmeticMinus'){

			var newnode = new Monalisa.Node("arithmetic", "-");
			//newnode.AddChild(vToken1.tree);
			
			var newToken = new Monalisa.Token('arithmetic', '-');
            newToken.tree = newnode;

			vnewTokenList = vToken.slice(0, i);
			vnewTokenList[vnewTokenList.length] = newToken;
			vnewTokenList = vnewTokenList.concat(vToken.slice(i+1));
			
			this.ParseFase(vnewTokenList, vfase);
			return;	
		}

        // arithmetic :: arithmeticMultiplication
        if (vToken1.type == 'arithmeticMultiplication'){

			var newnode = new Monalisa.Node("arithmetic", "*");
			//newnode.AddChild(vToken1.tree);
			
			var newToken = new Monalisa.Token('arithmetic', '*');
            newToken.tree = newnode;

			vnewTokenList = vToken.slice(0, i);
			vnewTokenList[vnewTokenList.length] = newToken;
			vnewTokenList = vnewTokenList.concat(vToken.slice(i+1));
			
			this.ParseFase(vnewTokenList, vfase);
			return;	
		}

        // arithmetic :: arithmeticDivision
        if (vToken1.type == 'arithmeticDivision'){

			var newnode = new Monalisa.Node("arithmetic", "/");
			//newnode.AddChild(vToken1.tree);
			
			var newToken = new Monalisa.Token('arithmetic', '/');
            newToken.tree = newnode;

			vnewTokenList = vToken.slice(0, i);
			vnewTokenList[vnewTokenList.length] = newToken;
			vnewTokenList = vnewTokenList.concat(vToken.slice(i+1));
			
			this.ParseFase(vnewTokenList, vfase);
			return;	
		}
//Producciones con 2 elementos
        if (vToken2){

		    // condition :: NOT condition
		    if (vfase > 1 && (vToken1.type == 'logicNOT' && vToken2.type == 'condition')){

			    var newnode = new Monalisa.Node("logic", "NOT");
			    newnode.AddChild(vToken2.tree);
			
			    var newcondition = new Monalisa.Node("condition", "condition");
			    newcondition.AddChild(newnode);
		
			    var newToken = new Monalisa.Token('condition', '');
                newToken.tree = newcondition;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+2));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;	
		    }
		    //result :: resultado porcent
		    if (vToken1.type == 'resultado' && vToken2.type == 'percent'){

				var vresultadodescription = "";
				for(var j=0; j< this.Results.length; j++){
					if (this.Results[j].name == vToken1.value){
						 vresultadodescription = this.Results[j].description;
						 break;
					}
				}
		        var newnode = new Monalisa.Node("resultado", vToken1.value, vresultadodescription);
			    //newnode.porcent = vToken2.value;
		        var newnodearg = new Monalisa.Node('percent', vToken2.value);
                newnode.AddChild(newnodearg);
	        
			    
			    var newToken = new Monalisa.Token('result', '');
                newToken.tree = newnode;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+2));
			
			    this.ParseFase(vnewTokenList, vfase);

			    return;

		    }
        }

//Producciones con 3 tokens
		if (vToken3){
		    
		    // condition :: argument constraint argument
		    if (vfase > 1 && (vToken1.type == 'argument' && vToken2.type == 'constraint' && vToken3.type == 'argument')){

			    var newnode = new Monalisa.Node("constraint", vToken2.value);
			    newnode.AddChild(vToken1.tree);
                newnode.AddChild(vToken3.tree);
                if((vToken1.tree.Children[0].type == "field" || vToken1.tree.Children[0].type == "model" || vToken1.tree.Children[0].type == "catalog")&& vToken3.tree.Children[0].type == "constant"){
                    if (vToken2.value == "Es")
						vToken3.tree.Children[0].domain = vToken1.tree.Children[0].ElementRef.domain;
                    vToken3.tree.Children[0].domainType = vToken1.tree.Children[0].ElementRef.type;
                }
                    
			    
			    var newcondition = new Monalisa.Node("condition", "condition");
			    newcondition.AddChild(newnode);
	
			    var newToken = new Monalisa.Token('condition', '');
                newToken.tree = newcondition;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;
		    }

            //argument :: argument arithmetic argument
		    if(vToken1.type == 'argument' && vToken2.type == 'arithmetic' && vToken3.type == 'argument'){

                var newnode = new Monalisa.Node("arithmetic", vToken2.value);
		        newnode.AddChild(vToken1.tree);
		        newnode.AddChild(vToken3.tree);
        		
       		    var newargument = new Monalisa.Node("argument", "argument");
    		    newargument.AddChild(newnode);
	            
			    var newToken = new Monalisa.Token('argument', '');
                newToken.tree = newargument;			
			    
                vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;			

		    
		    }
            //condition :: condition OR condition
            if (vfase > 1 && (vToken1.type == 'condition' && vToken2.type == 'logicOR' && vToken3.type == 'condition')){
		
				//Si ya tengo una condicion de OR a la izquierda la agrego
				if(vToken1.tree.Children.length == 1 && vToken1.tree.Children[0].name == "OR"){
					vToken1.tree.Children[0].AddChild(vToken3.tree);
					
					var newToken = new Monalisa.Token('condition', '');
					newToken.tree = vToken1.tree;
				}
				else{
					var newnode = new Monalisa.Node("logic", "OR");
					newnode.AddChild(vToken1.tree);
					//Si el subarbol de la derecha es un OR lo mergeo
					if(vToken3.tree.Children.length == 1 && vToken3.tree.Children[0].name == "OR"){
						for(var z =0; z< vToken3.tree.Children[0].Children.length; z++){
							newnode.AddChild(vToken3.tree.Children[0].Children[z]);
						}
					
					}else{
						newnode.AddChild(vToken3.tree);
					}
					var newcondition = new Monalisa.Node("condition", "condition");
					newcondition.AddChild(newnode);
			
					var newToken = new Monalisa.Token('condition', '');
					newToken.tree = newcondition;
				}
				
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;	

		    }
            
            // condition :: condition AND condition
            if (vfase > 1 && (vToken1.type == 'condition' && vToken2.type == 'logicAND' && vToken3.type == 'condition')){
		
			    //var newnode = new Monalisa.Node("condition", "and");
			    var newnode = vToken1.tree;
			    //newnode.AddChild( vToken3.tree);

			    for(var j =0; j< vToken3.tree.Children.length; j++){
				    newnode.AddChild( vToken3.tree.Children[j]);
			    }
			
			
			    var newToken = new Monalisa.Token('condition', '');
                newToken.tree = newnode;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;	
		
		    
		    }

            //argument :: ( argument )
            if(vToken1.type == 'parizq' && vToken2.type == 'argument' && vToken3.type == 'parder'){
		
			    var newToken = new Monalisa.Token('argument', '');
                newToken.tree = vToken2.tree;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;
			
		    }
		    
            //condition :: ( condition )
            if(vToken1.type == 'parizq' && vToken2.type == 'condition' && vToken3.type == 'parder'){
		
			    var newToken = new Monalisa.Token('condition', '');
                newToken.tree = vToken2.tree;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;
			
		    }

            //condition :: argument in argumentlist
            if (vToken1.type == 'argument' && vToken2.type == 'in' && vToken3.type == 'argumentlist'){

			    var newnode = new Monalisa.Node("in", "En");
			    newnode.AddChild(vToken1.tree);

			    newnode.AddChild(vToken3.tree);
			
			    var newcondition = new Monalisa.Node("condition", "condition");
			    newcondition.AddChild(newnode);
	
			    var newToken = new Monalisa.Token('condition', 'c');
                newToken.tree = newcondition;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);

			    return;

		    
		    }

            // argumentlist :: [ argumentlistelements ]
            // argumentlist :: [ argument ]
            if (vToken1.type == 'parrecizq' && (vToken2.type == 'argument' || vToken2.type == 'argumentlistelements') && vToken3.type == 'parrecder'){

			    if (vToken2.type == 'argument'){
				    var newnode = new Monalisa.Node("argumentlist", "al");
				    var argument1 = new Monalisa.Node("argument", "argument1");
				    argument1.AddChild(vToken2.tree);
				    newnode.AddChild(argument1);
			    }else{
				    var newnode = vToken2.tree;
			    }
		


			    var newToken = new Monalisa.Token('argumentlist', 'argumentlist');
                newToken.tree = newnode;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);

			    return;

		    }

            // argumentlistelements :: argumentlistelements , argument
            // argumentlistelements :: argument , argument
            if (vfase > 1 && ((vToken1.type == 'argument' || vToken1.type =='argumentlistelements') && vToken2.type == 'coma' && vToken3.type == 'argument')){

                if (vToken1.type == 'argument'){
			        var newnode = new Monalisa.Node("argumentlist", "al");
                    newnode.AddChild(vToken1.tree);
                }else{
                    var newnode = vToken1.tree
                }
                
			    newnode.AddChild(vToken3.tree);

			    var newToken = new Monalisa.Token('argumentlistelements', '');
                newToken.tree = newnode;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);

			    return;

		    }
		    //resultlistelement :: resultlistelement coma result
		    //resultlistelement :: result coma result
		    if ((vToken1.type == 'resultlistelement' || vToken1.type == 'result') && vToken2.type == 'coma' && vToken3.type == 'result'){

                var newToken = new Monalisa.Token('resultlistelement', '');
                if (vToken1.type == 'result'){ 
                    var newnode = new Monalisa.Node("result", "");
                    newnode.AddChild(vToken1.tree);
                    newnode.AddChild(vToken3.tree);
                    newToken.tree = newnode;    
                }
                else{
                    newToken.tree = vToken1.tree;
                    newToken.tree.AddChild(vToken3.tree);    
                }
                			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);

			    return;

		    }
		    
		    //resultlist :: parrecizq resultlistelement parrecder
		    //resultlist :: parrecizq result parrecder
		    if (vToken1.type == 'parrecizq' && (vToken2.type == 'resultlistelement' || vToken2.type == 'result')&& vToken3.type == 'parrecder'){

                var newToken = new Monalisa.Token('resultlist', '');
                
                //Tiene un solo resultado
                if (vToken2.type == 'result'){ 
                    var newnode = new Monalisa.Node("result", "");
                    newnode.AddChild(vToken2.tree);
                    newToken.tree = newnode;
                }
                else{
                    newToken.tree = vToken2.tree;
                }
                
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);

			    return;

		    }
            //result :: resultado mail porcent
		    if (vToken1.type == 'resultado' && vToken2.type == 'mail' && vToken3.type == 'percent'){

		        var newnode = new Monalisa.Node("resultado", vToken1.value);
		        var newnodearg = new Monalisa.Node('mail', vToken2.value);
		        for (var j = 0; j < this.Results.length; j++) {
		            if (this.Results[j].name == vToken1.value) {
		                newnodearg.domain = this.Results[j].args[0].domain;
		                break;
		            }
		        }
		        newnode.AddChild(newnodearg);
		        var newnodearg = new Monalisa.Node('percent', vToken3.value);
                newnode.AddChild(newnodearg);
	        
			    
			    var newToken = new Monalisa.Token('result', '');
                newToken.tree = newnode;
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+3));
			
			    this.ParseFase(vnewTokenList, vfase);

			    return;

		    }
		}
		
		
		if(vToken4){
		
		    // rule :: si condition entonces resultlist
		    if (vToken1.type == 'si' && vToken2.type == 'condition' && vToken3.type == 'entonces' && vToken4.type == 'resultlist'){
		        
		        var newToken = new Monalisa.Token('rule', '');
                newToken.tree = vToken2.tree;
                newToken.tree2 = vToken4.tree
			
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+4));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;
            }
            
            //argument :: abs parizq argument1 parder 
            if ((vToken1.type == 'functionNumber' && vToken1.value == 'abs')&& vToken2.type == 'parizq' && vToken3.type == 'argument' && vToken4.type == 'parder'){
		        
		        var newargument = new Monalisa.Node("argument", "");
		        var newfunction = new Monalisa.Node("functionNumber", "Valor Absoluto");
		        newfunction.AddChild(vToken3.tree);
		        newargument.AddChild(newfunction);
			    
		        var newToken = new Monalisa.Token('argument', '');
                newToken.tree = newargument;
                
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+4));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;
            }
			//argument :: round parizq argument1 parder 
            if ((vToken1.type == 'functionNumber' && vToken1.value == 'round')&& vToken2.type == 'parizq' && vToken3.type == 'argument' && vToken4.type == 'parder'){
		        
		        var newargument = new Monalisa.Node("argument", "");
		        var newfunction = new Monalisa.Node("functionNumber", "Redondeo");
		        newfunction.AddChild(vToken3.tree);
		        newargument.AddChild(newfunction);
			    
		        var newToken = new Monalisa.Token('argument', '');
                newToken.tree = newargument;
                
			    vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+4));
			
			    this.ParseFase(vnewTokenList, vfase);
			    return;
            }
			
		}
		if(vToken6){
			//argument :: concat parizq argument1 coma argument2 parder 
            if ((vToken1.type == 'functionText' && vToken1.value == 'concat')&& vToken2.type == 'parizq' && vToken3.type == 'argument' && vToken4.type == 'coma' && vToken5.type == 'argument' && vToken6.type == 'parder'){
				
				var newargument = new Monalisa.Node("argument", "");
		        var newfunction = new Monalisa.Node("functionText", "concat");
				newfunction.AddChild(vToken3.tree);
				vToken3.tree.domainType = "Text";
				newfunction.AddChild(vToken5.tree);
				vToken5.tree.domainType = "Text";
		        newargument.AddChild(newfunction);
				
				var newToken = new Monalisa.Token('argument', '');
                newToken.tree = newargument;
				
				vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+6));
				
				this.ParseFase(vnewTokenList, vfase);
			    return;
			}
			
			//argument :: max parrecizq argument1 coma argument2 parrecder 
            if ((vToken1.type == 'functionNumber' && vToken1.value == 'max')&& vToken2.type == 'parrecizq' && vToken3.type == 'argument' && vToken4.type == 'coma' && vToken5.type == 'argument' && vToken6.type == 'parrecder'){
				
				var newargument = new Monalisa.Node("argument", "");
		        var newfunction = new Monalisa.Node("functionNumber", "Maximo");
				newfunction.AddChild(vToken3.tree);
				vToken3.tree.domainType = "Numeric";
				newfunction.AddChild(vToken5.tree);
				vToken5.tree.domainType = "Numeric";
		        newargument.AddChild(newfunction);
				
				var newToken = new Monalisa.Token('argument', '');
                newToken.tree = newargument;
				
				vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+6));
				
				this.ParseFase(vnewTokenList, vfase);
			    return;
			}
			//argument :: min parrecizq argument1 coma argument2 parrecder 
            if ((vToken1.type == 'functionNumber' && vToken1.value == 'min')&& vToken2.type == 'parrecizq' && vToken3.type == 'argument' && vToken4.type == 'coma' && vToken5.type == 'argument' && vToken6.type == 'parrecder'){
				
				var newargument = new Monalisa.Node("argument", "");
		        var newfunction = new Monalisa.Node("functionNumber", "Minimo");
				newfunction.AddChild(vToken3.tree);
				vToken3.tree.domainType = "Numeric";
				newfunction.AddChild(vToken5.tree);
				vToken5.tree.domainType = "Numeric";
		        newargument.AddChild(newfunction);
				
				var newToken = new Monalisa.Token('argument', '');
                newToken.tree = newargument;
				
				vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+6));
				
				this.ParseFase(vnewTokenList, vfase);
			    return;
			}
		}
		if(vToken8){
			//argument :: substr parizq argument1 coma argument2 coma argument3 parder 
            if ((vToken1.type == 'functionText' && vToken1.value == 'substr')&& vToken2.type == 'parizq' && vToken3.type == 'argument' && vToken4.type == 'coma' && vToken5.type == 'argument' && vToken6.type == 'coma' && vToken7.type == 'argument' && vToken8.type == 'parder'){
				
				var newargument = new Monalisa.Node("argument", "");
		        var newfunction = new Monalisa.Node("functionText", "substr");
				newfunction.AddChild(vToken3.tree);
				vToken3.tree.domainType = "Text";
				newfunction.AddChild(vToken5.tree);
				vToken5.tree.domainType = "Numeric"
				newfunction.AddChild(vToken7.tree);
		        vToken7.tree.domainType = "Numeric"
				newargument.AddChild(newfunction);
				
				var newToken = new Monalisa.Token('argument', '');
                newToken.tree = newargument;
				
				vnewTokenList = vToken.slice(0, i);
			    vnewTokenList[vnewTokenList.length] = newToken;
			    vnewTokenList = vnewTokenList.concat(vToken.slice(i+8));
				
				this.ParseFase(vnewTokenList, vfase);
			    return;
			}
		}
		
	}
	if (vfase >= 2){
	    alert('no se pudo parser en una expresion valida');
	}else{
	    this.ParseFase(vToken, vfase + 1);
	}    
    
}

Monalisa.Editor.prototype.Clear = function(){
    if (this.Condition.Children)
        for(var i=this.Condition.Children.length-1;i>=0;i--){
            this.Condition.Children[i].DropRemoveElement();
        }
    if (this.Result.Children)
        for(var i=this.Result.Children.length-1;i>=0;i--){
            this.Result.Children[i].DropRemoveElement();
        }
           
}

Monalisa.Editor.prototype.GetToken = function(){
    var tokenlist = new Array();
    if (this.Validate() == "OK"){
        var newtoken = new Monalisa.Token("si", "si");
        tokenlist[tokenlist.length] = newtoken;
        if(this.Condition)
            tokenlist = tokenlist.concat(this.Condition.Compile());
        var newtoken = new Monalisa.Token("entonces", "entonces");
        tokenlist[tokenlist.length] = newtoken;
        if(this.Result)
        tokenlist = tokenlist.concat(this.Result.Compile());
    }else{
        var newtoken = new Monalisa.Token("error", "error");
        tokenlist[tokenlist.length] = newtoken;
    }    
    return tokenlist;
    
}

Monalisa.Editor.prototype.Validate = function(){
    var conditionvalidate = this.Condition.Validate();
    if ( conditionvalidate == "OK")
        return this.Result.Validate();
    else
        return conditionvalidate;
        
}

Monalisa.Editor.prototype.Compile = function(){
    var valnode = this.Validate();
    if ( valnode == "OK"){
        var tokenlist = this.GetToken()
        var code = "";
        for(var i=0; i< tokenlist.length; i++){
            code = code + " " + tokenlist[i].tag
        }
        //alert(code);
    }
    else
        code = "Error al compilar:" + valnode;
	return code;
}



Monalisa.Editor.prototype.showText = function(){
    this.ModeText = true;
	this.displayMode();
}

Monalisa.Editor.prototype.showBlock = function(){
    this.ModeText = false;
	this.displayMode();
}

Monalisa.Editor.prototype.displayMode = function(){
    if (this.ModeText)
	{
		this.sourceText.style.display = "";
		this.workspace.style.display = "none";
	}
	else{
		this.sourceText.style.display = "none";
		this.workspace.style.display = "";
	};
}


//******************************************************************************
//    Definicion de la clase Field
//******************************************************************************

Monalisa.Field = function(vname, vdescription, vtype, vdomain){
    this.name = vname;
	this.description = vdescription;
    this.type = vtype;
    this.domain = vdomain;
	this.suggestId = null;
    this.defaultValue = "";
    this.index = -1;
    this.DOMRef = null;
	this.length = null;	
	this.pattern = null;
	this.patternMessage = null;
}


//******************************************************************************
//    Definicion de la clase List
//******************************************************************************

Monalisa.List = function(vname, vdescription, vtype){
    this.name = vname;
	this.description = vdescription;
    this.type = vtype;
    //this.domain = vdomain;
    this.defaultValue = "";
    this.index = -1;
    this.DOMRef = null;                
}

//******************************************************************************
//    Definicion de la clase Domain
//******************************************************************************

Monalisa.Domain = function(vname){
    this.name = vname;
    this.type = "Text"
    this.items = new Array();                 
}

Monalisa.Domain.prototype.AddItem = function(value, description){
    
    var item = new this.Item(value, description);
    this.items[this.items.length] = item;
}

Monalisa.Domain.prototype.Item = function(value, description){
    this.value = value;
    this.description = description
}

//******************************************************************************
//    Definicion de la clase Resultado
//******************************************************************************

Monalisa.Result = function(vname, vdescription){
    this.name = vname;
	this.description = vdescription;
    this.DOMRef = null;                
    this.percent = 100;
    this.args = new Array(); 
}

Monalisa.Result.prototype.AddArg = function(varg){
    
    this.args[this.args.length] = varg;
}

Monalisa.Arg = function(vtype, vvalue){
    
    this.type = vtype;
    this.value = vvalue;
}
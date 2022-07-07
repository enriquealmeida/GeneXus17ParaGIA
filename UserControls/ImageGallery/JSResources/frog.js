//-------------------------------------------------------------
//  FrogJS v.1.1
//  Created by Eric Puidokas (www.puidokas.com)
//
//  Licensed under the Creative Commons Attribution 2.5 License
//  (http://creativecommons.org/licenses/by/2.5/)
//-------------------------------------------------------------

// CONFIGURATION VARIABLES
var thumbTop = '60px'; // distance you want you thumbnails to be from the top of their container
var scalePercent = '300'; // percent thumbnails expand when fading into main image

// GLOBALS
var frogjsImageArray = new Array;

// Extensions to the Element class from prototype.js
Object.extend(Element, {
	removeDimensions: function(element){
	   	element = $(element);
	   	element.style.width = '';
		element.style.height = '';
	},
	removeOnclick: function(element){
	   	element = $(element);
	   	element.onclick = function(){}
	},
	setCursor: function(element, cursor){
		element = $(element);
		element.style.cursor = cursor;
	}
});

// Frog Class
var Frog = Class.create();
Frog.prototype = {
	
	// initialize()
	// Constructor runs once the page has been loaded.  It extracts any linked images within an element with id 'FrogJS' and builds the array for the FrogJS Gallery
	// It then empties the 'FrogJS' element and inserts the neccessary DOM elements to run a FrogJS gallery.  Lastly, it calls the functions to load the first image and thumbnail.
	initialize: function(){
		
		this.MyGXUserControl = this.MyGXUserControlStatic;
		var imgsDir = gx.staticDirectory.substring(1,gx.staticDirectory.length);
		if (gx.text.trim(this.MyGXUserControl.LoadingImage)!="")
		    var loadingAni = gx.text.trim(this.MyGXUserControl.LoadingImage);
		else
            var loadingAni = gx.util.resourceUrl(imgsDir + "ImageGallery/images/loading1.gif", true);		
        var fileBottomNavCloseImage = gx.util.resourceUrl(imgsDir + "ImageGallery/images/closelabel.gif", true);
		if(!document.getElementsByTagName){ return; }
		
		// Builds frogjsImageArray of all images, thumbnails, credits, and captions
		if ($('FrogJS')!=null)
		{		
		    var anchors = $('FrogJS').getElementsByTagName('a');
		    for (var i=0; i<anchors.length; i++){
		        frogjsImageArray[i] = new Array();
			    frogjsImageArray[i]['full'] = anchors[i].getAttribute('href');
			    frogjsImageArray[i]['credit'] = anchors[i].getAttribute('title');
			    if (gx.text.trim(anchors[i].getElementsByTagName('img')[0].getAttribute('src')) == gx.util.resourceUrl("",true))
			        frogjsImageArray[i]['thumb'] = "";
			    else
			        frogjsImageArray[i]['thumb'] = anchors[i].getElementsByTagName('img')[0].getAttribute('src');
			    frogjsImageArray[i]['caption'] = anchors[i].getElementsByTagName('img')[0].getAttribute('alt');
			    frogjsImageArray[i]['link'] = anchors[i].getAttribute('rel');
		    }
    		
		    //-----------------------------------------
		    // Inserting new HTML elements into 'FrogJS' Element
		    //-----------------------------------------
		    var ribbit = $('FrogJS');
		    ribbit.innerHTML = '';
		    ribbit.style.position = 'relative';
		    ribbit.style.display = 'block';
		    ribbit.style.textAlign = 'center';
		    ribbit.style.width = this.MyGXUserControl.Width;
		    ribbit.style.height = this.MyGXUserControl.Height;
			
		    var mainContainer = document.createElement("div");
		    mainContainer.setAttribute('id','FrogJSMainContainer');
		    mainContainer.style.margin = '0 auto';
		    mainContainer.style.whiteSpace = 'normal';
		    ribbit.appendChild(mainContainer);
    		
		    var mainImage = document.createElement("img");
		    mainImage.setAttribute('id','FrogJSImage');
		    mainImage.style.display = 'none';
		    mainContainer.appendChild(mainImage);
    		
		    var credit = document.createElement("div");
		    credit.setAttribute('id','FrogJSCredit');
		    mainContainer.appendChild(credit);
    		
		    var caption = document.createElement("div");
		    caption.setAttribute('id','FrogJSCaption');
		    mainContainer.appendChild(caption);
    		
		    var loadingImg = document.createElement("img");
		    loadingImg.setAttribute('id','FrogJSLoadingAni');
		    loadingImg.src = loadingAni;
		    loadingImg.style.display = 'none';
		    loadingImg.style.position = 'absolute';
		    loadingImg.style.top = thumbTop;
		    ribbit.appendChild(loadingImg);
    		
		    var rThumb1 = document.createElement("img");
		    rThumb1.setAttribute('id','FrogJSrightThumb1');
		    rThumb1.style.display = 'none';
		    rThumb1.style.position = 'absolute';
		    rThumb1.style.top = thumbTop;
		    rThumb1.style.right = '0';
		    rThumb1.style.cursor = 'pointer';
		    ribbit.appendChild(rThumb1);
    		
		    var lThumb1 = document.createElement("img");
		    lThumb1.setAttribute('id','FrogJSleftThumb1');
		    lThumb1.style.display = 'none';
		    lThumb1.style.position = 'absolute';
		    lThumb1.style.top = thumbTop;
		    lThumb1.style.left = '0';
		    lThumb1.style.cursor = 'pointer';
		    ribbit.appendChild(lThumb1);
    		
		    var rThumb2 = document.createElement("img");
		    rThumb2.setAttribute('id','FrogJSrightThumb2');
		    rThumb2.style.display = 'none';
		    rThumb2.style.position = 'absolute';
		    rThumb2.style.top = thumbTop;
		    rThumb2.style.right = '0';
		    ribbit.appendChild(rThumb2);
    		
		    var lThumb2 = document.createElement("img");
		    lThumb2.setAttribute('id','FrogJSleftThumb2');
		    lThumb2.style.display = 'none';
		    lThumb2.style.position = 'absolute';
		    lThumb2.style.top = thumbTop;
		    lThumb2.style.left = '0';
		    ribbit.appendChild(lThumb2);
		    //-----------------------------------------
		    // End Inserting new elements
		    //-----------------------------------------
    		
		    // Preloads first image and displays image along with next thumbnail
		    var myFrog = this; // IE can't use the global `myFrog` until it's been initialized
		    var imgPreloader = new Image();
		    imgPreloader.onload=function(){
			    myFrog.loadMainImage(0, imgPreloader.width);
			    myFrog.thumbIn(1, 'right');
			    myFrog.MyGXUserControl.InternalCurrentImage = 0;
		    }
		    imgPreloader.src = frogjsImageArray[0]['full'];
        }
	},
	
	// resizeImage()
	// Scale down the image when its size is greater than the screen size
	resizeImage: function(width, height) {
		if (width >= screen.width) {
			//reduce the image 
			var scale = width / screen.width;
			
			width = (width / scale);
			height = (height / scale);

			//now check the image height
			if (height >= screen.height) {
				var scaleHeight = height / screen.height;
				width = (width / scaleHeight) * 0.75;
				height = (height / scaleHeight) * 0.75;
			}
		}
		return width;
	},

	// loadImage()
	// Loads main image and updates thumbnails accordingly.  Uses all other functions of the Frog class.
	loadImage: function(imageNum, side, width, height, raise){
		//resize image if it's necessary
		var resizedWidth = myFrog.resizeImage(width, height);
		
		if (raise!=false)
		{
		    var auxSelectedId = myFrog.MyGXUserControl.SelectedImageId;
		    this.MyGXUserControl.OnImageChanged(imageNum);
		    if (auxSelectedId != myFrog.MyGXUserControl.SelectedImageId)
		        imageNum = myFrog.MyGXUserControl.ImageMapper[myFrog.MyGXUserControl.SelectedImageId];
		}
		if (frogjsImageArray[imageNum]!=undefined)
		{
            myFrog.MyGXUserControl.InternalCurrentImage = imageNum;
			myFrog.loadMainImage(imageNum, resizedWidth);
			
		    if(side=='right'){
			    myFrog.mainIn(imageNum, 'right');
			    myFrog.thumbIn(imageNum+1, 'right');
			    myFrog.mainOut(imageNum-1, 'left');
			    myFrog.thumbOut(imageNum-2, 'left');
		    }else{
			    myFrog.mainIn(imageNum, 'left');
			    myFrog.thumbIn(imageNum-1, 'left');
			    myFrog.mainOut(imageNum+1, 'right');
			    myFrog.thumbOut(imageNum+2, 'right');
		    }
        }
	},
	
	// loadMainImage()
	// Fades out old main image and fades in new one.  Also updates credit and caption
	loadMainImage: function(imageNum, width){
		Element.setCursor('FrogJSImage','');
		$('FrogJSImage').onclick = function(){};
		new Effect.Fade('FrogJSMainContainer', { duration:0.5, afterFinish: function(){ showMainImage(); } });
		function showMainImage(){
			$('FrogJSImage').style.display = 'block';
			$('FrogJSImage').src = frogjsImageArray[imageNum]['full'];
			$('FrogJSImage').style.width = width+'px';
			$('FrogJSMainContainer').style.width = width+'px';
			$('FrogJSCredit').innerHTML = frogjsImageArray[imageNum]['credit'];
			$('FrogJSCaption').innerHTML = frogjsImageArray[imageNum]['caption'];
			new Effect.Appear('FrogJSMainContainer', { duration: 0.5, afterFinish: function(){ addOnclick(); } });
			function addOnclick(){
				if(frogjsImageArray[imageNum]['link']){
					Element.setCursor('FrogJSImage','pointer');
					$('FrogJSImage').onclick = function(){
						location.href = frogjsImageArray[imageNum]['link'];
					}
				}
			}
		}
	},
	
	// thumbIn()
	// Loads in new thumbnail and preloads image if neccessary
	thumbIn: function(imageNum, side){
		Element.hide('FrogJS'+side+'Thumb1');
		if(frogjsImageArray[imageNum]){
			Element.setCursor('FrogJS'+side+'Thumb1','');
			$('FrogJSLoadingAni').style.left = (side=='left') ? '0' : '';
			$('FrogJSLoadingAni').style.right = (side=='right') ? '0' : '';
			Element.show('FrogJSLoadingAni');
			var imgPreloader = new Image();
			imgPreloader.onload=function(){
				Element.hide('FrogJSLoadingAni');
				Element.setCursor('FrogJS'+side+'Thumb1','');
				$('FrogJS'+side+'Thumb1').onclick = function(){};
				if (frogjsImageArray[imageNum]['thumb'] != "")
				{
				    $('FrogJS'+side+'Thumb1').src = frogjsImageArray[imageNum]['thumb'];
						Element.removeDimensions('FrogJS'+side+'Thumb1');
				}
				else
				{
				    frogjsImageArray[imageNum]['thumb'] = frogjsImageArray[imageNum]['full'];
				    $('FrogJS'+side+'Thumb1').src = frogjsImageArray[imageNum]['full'];
				    $('FrogJS'+side+'Thumb1').style.width = myFrog.MyGXUserControl.ThumbnailWidth;
				    $('FrogJS'+side+'Thumb1').style.height = myFrog.MyGXUserControl.ThumbnailHeight;
				}
								
				new Effect.Appear('FrogJS'+side+'Thumb1',{duration:1.0});
				new Effect.Scale('FrogJS'+side+'Thumb1', 100, { duration: 1.0, scaleFrom: 0.1, afterFinish: function(){ addOnclick(); } });
				function addOnclick(){
					Element.setCursor('FrogJS'+side+'Thumb1','pointer');
					$('FrogJS'+side+'Thumb1').onclick = function(){
						myFrog.loadImage(imageNum, side, imgPreloader.width, imgPreloader.height);

						Element.removeOnclick('FrogJSleftThumb1');
						Element.removeOnclick('FrogJSrightThumb1');
					}
				}
			}
			imgPreloader.src = frogjsImageArray[imageNum]['full'];
		}
	},
	
	// thumbOut()
	// Removes old thumbnail
	thumbOut: function(imageNum, side){
		if(frogjsImageArray[imageNum]){
			if (frogjsImageArray[imageNum]['thumb'] != "")
			{
			    $('FrogJS'+side+'Thumb2').src = frogjsImageArray[imageNum]['thumb'];
		  		Element.removeDimensions('FrogJS'+side+'Thumb2');
			}
		  else
			{
			    frogjsImageArray[imageNum]['thumb'] = frogjsImageArray[imageNum]['full'];
			    $('FrogJS'+side+'Thumb2').src = frogjsImageArray[imageNum]['full'];
			    $('FrogJS'+side+'Thumb2').style.width = myFrog.MyGXUserControl.ThumbnailWidth;
			    $('FrogJS'+side+'Thumb2').style.height = myFrog.MyGXUserControl.ThumbnailHeight;
			}			
			
			Element.show('FrogJS'+side+'Thumb2');
			new Effect.Fade('FrogJS'+side+'Thumb2',{duration:1.0});
			new Effect.Scale('FrogJS'+side+'Thumb2', 0, { duration: 1.0, scaleFrom: 100 });
		}
	},
	
	// mainIn()
	// Fades thumbnail into main image
	mainIn: function(imageNum, side){
		//$('FrogJS'+side+'Thumb2').src = frogjsImageArray[imageNum]['thumb'];
		if (frogjsImageArray[imageNum]['thumb'] != "")
		{
			Element.removeDimensions('FrogJS'+side+'Thumb2');
			$('FrogJS'+side+'Thumb2').src = frogjsImageArray[imageNum]['thumb'];
		}
		else
		{
			frogjsImageArray[imageNum]['thumb'] = frogjsImageArray[imageNum]['full'];
			$('FrogJS'+side+'Thumb2').src = frogjsImageArray[imageNum]['full'];
			$('FrogJS'+side+'Thumb2').style.width = myFrog.MyGXUserControl.ThumbnailWidth;
			$('FrogJS'+side+'Thumb2').style.height = myFrog.MyGXUserControl.ThumbnailHeight;
		}		
		Element.show('FrogJS'+side+'Thumb2');
		new Effect.Fade('FrogJS'+side+'Thumb2',{duration:1.0});
		new Effect.Scale('FrogJS'+side+'Thumb2', scalePercent, { duration: 1.0 });
	},
	
	// mainOut()
	// Fades old main image into thumbnail
	mainOut: function(imageNum, side){
		Element.hide('FrogJS'+side+'Thumb1');
		if(frogjsImageArray[imageNum]){
			Element.setCursor('FrogJS'+side+'Thumb1','');
			var imgPreloader = new Image();
			imgPreloader.onload=function(){
				//Element.removeDimensions('FrogJS'+side+'Thumb1');
				if (frogjsImageArray[imageNum]['thumb'] != "")
			  {
			   	Element.removeDimensions('FrogJS'+side+'Thumb1');
			    $('FrogJS'+side+'Thumb1').src = frogjsImageArray[imageNum]['thumb'];
		    }
		    else
			  {
			  	$('FrogJS'+side+'Thumb1').src = frogjsImageArray[imageNum]['full'];
			    $('FrogJS'+side+'Thumb1').style.width = myFrog.MyGXUserControl.ThumbnailWidth;
			    $('FrogJS'+side+'Thumb1').style.height = myFrog.MyGXUserControl.ThumbnailHeight;
			  }		
				new Effect.Appear('FrogJS'+side+'Thumb1',{duration:1.0});
				new Effect.Scale('FrogJS'+side+'Thumb1', 100, { duration: 1.0, scaleFrom: scalePercent, afterFinish: function(){ addOnclick(); } });
				function addOnclick(){
					Element.setCursor('FrogJS'+side+'Thumb1','pointer');
					$('FrogJS'+side+'Thumb1').onclick = function(){
						myFrog.loadImage(imageNum, side, imgPreloader.width, imgPreloader.height);
						Element.removeOnclick('FrogJSleftThumb1');
						Element.removeOnclick('FrogJSrightThumb1');
					}
				}
			}
			imgPreloader.src = frogjsImageArray[imageNum]['full'];	
		}
	}
}

//It's time for this frog to hop
function initFrog(){ myFrog = new Frog(); }
//Event.observe(window, 'load', initFrog, false);
//gx.evt.on_ready( window,initFrog);

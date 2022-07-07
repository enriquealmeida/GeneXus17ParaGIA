function ReportHtmlControl()
{
	this.PageHeaderHeight;
	this.PageHeaderTextAlign;
	this.PageHeaderPadding;
	this.PageHeaderHtmlSource;
	this.PageHeaderColor;
	this.PageBodyHeight;
	this.PageBodyPadding;
	this.PageBodyTextAlign;
	this.PageBodytextJustify;
	this.PageBodyHtmlSource;
	this.PageFooterPadding;
	this.PageFooterHeight;
	this.PageFooterTextAlign;
	this.PageFooterBottom;
	this.PageFooterHtmlMaskPag;
	this.IsPageFooterHtmlDataPag;
	this.PageFooterHtmlSource;
	this.PageWidth;
	this.PageMinHeight;
	this.PageBackGroundColor;
	this.PageBackGroundImage;
	this.IsPageHeader;
	this.IsPageFooter;
	this.PagePadding;
	this.PrintPageMargin;
	this.PrintPageSize;
	this.IsPrintPageHeader;
	this.IsPrintPageFooter;
	this.PrintPageIESize;
	this.PrintPageIEWidth;
	this.PrintPageIEMiniHeight;
	this.Width;
	this.Height;
	this.BackGroundColor;
	this.BackGroundImage;
	this.ToolbarPosition;
	this.IsBreakingTable;
	this.ModeRender;
	this.PrintviewFont;
	this.PrintviewWidth;
	this.BtnCaptionPrint;

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)



		var bufferhtml = '';
	
		bufferhtml += '	body { \n';
		bufferhtml += '	margin: 0; \n';
		bufferhtml += '	padding: 0; \n';
		bufferhtml += '	background-color: none repeat scroll 0% 0% rgba(0, 0, 255, 0.3); \n';
		bufferhtml += (this.BackGroundColor!=undefined) ? '	background-color: '+ this.BackGroundColor.Html +'; \n' : "";
		bufferhtml += '	background-image: url("'+ this.BackGroundImage +'");\n'; 
		bufferhtml += '	font: '+ this.PrintviewFont +'; \n';
			/*color: #444;*/ 
		bufferhtml += '	} \n';
		bufferhtml += '	* { \n';
		bufferhtml += '	-webkit-box-sizing: border-box; \n';
		bufferhtml += '	-khtml-box-sizing: border-box; \n';
		bufferhtml += '	-moz-box-sizing: border-box; \n';
		bufferhtml += '	-ms-box-sizing: border-box; \n';
		bufferhtml += '	-o-box-sizing: border-box; \n';
		bufferhtml += '	box-sizing: border-box; \n';
		bufferhtml += '	-webkit-user-select: none; \n';
		bufferhtml += '	-khtml-user-select: none; \n';
		bufferhtml += '	-moz-user-select: none; \n';
		bufferhtml += '	-ms-user-select: none; \n';
		bufferhtml += '	-o-user-select: none; \n';
		bufferhtml += '	user-select: none; \n';
		bufferhtml += '	} \n';
		bufferhtml += '	.printview { \n';
		bufferhtml += '	margin-left: auto; \n';
		bufferhtml += '	margin-right: auto; \n';
		bufferhtml += '	width: '+ this.PrintviewWidth +'; \n';
		bufferhtml += '	font: '+ this.PrintviewFont +'; \n';
		bufferhtml += '	} \n';
		bufferhtml += '	.toolbar { \n';
		bufferhtml += '	background: none repeat scroll 0% 0% rgba(0, 0, 255, 0.3); \n';
		bufferhtml += '	background-color: rgb(71, 71, 71); \n';
		bufferhtml += '	min-height: 32px; \n';
		bufferhtml += '	width: 100%; \n';
		bufferhtml += '	box-shadow: 1px 0px 0px rgba(255, 255, 255, 0.08) inset, 0px 1px 1px rgba(0, 0, 0, 0.15) inset, 0px -1px 0px rgba(255, 255, 255, 0.05) inset, 0px 1px 0px rgba(0, 0, 0, 0.15), 0px 1px 1px rgba(0, 0, 0, 0.1); \n';
		bufferhtml += '	padding: 0; \n';
		bufferhtml += '	position: '+ this.ToolbarPosition +'; \n';
		bufferhtml += '	left: 0px; \n';
		bufferhtml += '	top: 0px; \n';
		bufferhtml += '	} \n';
		bufferhtml += '	.page { \n';
		bufferhtml += '	width: '+ this.PageWidth +'; \n';
		bufferhtml += '	min-height: '+ this.PageMinHeight +'; \n';
		bufferhtml += '	padding: '+ this.PagePadding +'; \n';
		bufferhtml += '	margin-top: 5%; \n';
		bufferhtml += '	margin-left: 5%; \n';
		bufferhtml += '	border: 1px #D3D3D3 solid; \n';
		bufferhtml += (this.PageBackGroundColor!=undefined) ? '	background-color: '+ this.PageBackGroundColor.Html +';\n ' : "";
		
		if(this.PageBackGroundImage!="")
			bufferhtml += '	background-image: url("'+ this.PageBackGroundImage +'");';
		bufferhtml += '	box-shadow: 0 0 5px rgba(0, 0, 0, 0.8); \n';
		bufferhtml += '	} \n';
		bufferhtml += '	.printview:first-child { \n';
		bufferhtml += '	margin-top: 2%; \n';
		bufferhtml += '	} \n';
		bufferhtml += '	.page:last-child { \n';
		bufferhtml += '	margin-bottom: 2%; \n';
		bufferhtml += '	} \n';

		if(this.IsPageHeader==true){
			bufferhtml += '	.page-header { \n';
			bufferhtml += '	padding: '+ this.PageHeaderPadding +'; \n';
			bufferhtml += '	border-top: 1px #ccc dotted; \n';
			bufferhtml += '	border-right: 1px #ccc dotted; \n';
			bufferhtml += '	border-left: 1px #ccc dotted; \n';
			bufferhtml += '	height: '+ this.PageHeaderHeight +'; \n';
			bufferhtml += '	text-align: '+ this.PageHeaderTextAlign +'; \n';
			bufferhtml += (this.PageHeaderColor!=undefined) ? '	color: '+ this.PageHeaderColor.Html +'; \n' : "";
			bufferhtml += '	} \n';
		}
		else{
			bufferhtml += '	.page-header { \n';
			bufferhtml += '	display: none; \n';
			bufferhtml += '	} \n';
		}
		bufferhtml += '	.page-body { \n';
		bufferhtml += '	padding: '+ this.PageBodyPadding +';\n'; 
		bufferhtml += '	border-right: 1px #ccc dotted; \n';
		bufferhtml += '	border-left: 1px #ccc dotted; \n';
		bufferhtml += '	height: '+ this.PageBodyHeight +'; \n';
		bufferhtml += '	text-align:'+ this.PageBodyTextAlign +';\n'; 
		bufferhtml += '	text-justify:'+ this.PageBodytextJustify +'; \n';
		bufferhtml += '	} \n';
		if(this.IsPageFooter==true){
			bufferhtml += '	.page-footer { \n';
			bufferhtml += '	bottom: '+ this.PageFooterBottom +'; \n';
			bufferhtml += '	padding: '+ this.PageFooterPadding +'; \n';
			bufferhtml += '	border-right: 1px #ccc dotted; \n';
			bufferhtml += '	border-bottom: 1px #ccc dotted; \n';
			bufferhtml += '	border-left: 1px #ccc dotted; \n';
			bufferhtml += '	height: '+ this.PageFooterHeight +'; \n';
			bufferhtml += '	text-align: '+ this.PageFooterTextAlign +'; \n';
			bufferhtml += '	} \n';
		}
		else{
			bufferhtml += '	.page-footer { \n';
			bufferhtml += '	display: none; \n';
			bufferhtml += '	} \n';
		}
		//bufferhtml += '	.content { \n';
		//bufferhtml += '	display: none; \n';
		//bufferhtml += '	} \n';
		
		bufferhtml += '	.SuperContent { \n';
		bufferhtml += '		display: none; \n';
		bufferhtml += '	} \n';
			/* 
			.tftable {font-size:12px;color:black;width:100%;border-width: 1px;border-color: #a9a9a9;border-collapse: collapse;} 
			.tftable th {font-size:12px;background-color:#b8b8b8;border-width: 1px;padding: 8px;border-style: solid;border-color: #a9a9a9;text-align:left;} 
			.tftable tackground-color:#ffffff;} 
			.tftable td {font-size:12px;border-width: 1px;padding: 8px;border-style: solid;border-color: #a9a9a9;} 
			*/ 
		bufferhtml += '	.columns-2 p { \n';
		bufferhtml += '	width: 49%; \n';
		bufferhtml += '	float: left; \n';
		bufferhtml += '	} \n';
		bufferhtml += '	.columns-2 p:nth-child(even) { \n';
		bufferhtml += '	margin-left: 2%; \n';
		bufferhtml += '	} \n';
		bufferhtml += '	.columns-2 p:nth-child(odd){ \n';
		bufferhtml += '	clear: left; \n';
		bufferhtml += '	} \n';

	
		if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) { /*Navegadores IE*/
			bufferhtml += '	@media print { \n';
			bufferhtml += '	@page { \n';
			bufferhtml += '	size: '+ this.PrintPageSize +'; \n';
			//bufferhtml += '	margin: 0px 0px 0px 0px; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	body \n';
			bufferhtml += '	{ \n';
			bufferhtml += '	background-color: rgb(255, 255, 255); \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.printview { \n';
			bufferhtml += '	width: '+ this.PrintPageIEWidth +'; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.toolbar { \n';
			bufferhtml += '	display: none; \n';
			bufferhtml += '	} \n';
			
			bufferhtml += '	.page { \n';
			bufferhtml += '	border: none; \n';
			bufferhtml += '	border-radius: 0; \n';
			bufferhtml += '	min-height: '+ this.PrintPageIEMiniHeight +'; \n';
			bufferhtml += '	box-shadow: 0 0 0px rgba(0, 0, 0, 0); \n';
			bufferhtml += '	background: initial; \n';
			bufferhtml += '	page-break-after: always ; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.page-header, .page-body { \n';
			bufferhtml += '	border: none; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.page-header, .page-body { \n';
			bufferhtml += '	border: none; \n';
			bufferhtml += '	} \n';
			if(this.IsPrintPageHeader==false){
				bufferhtml += '	.page-header \n';
				bufferhtml += '	{ \n';
				bufferhtml += '	display: none; \n';
				bufferhtml += '	} \n';
			}
			if(this.IsPrintPageFooter==false){
				bufferhtml += '	.page-footer \n';
				bufferhtml += '	{ \n';
				bufferhtml += '	display: none; \n';
				bufferhtml += '	} \n';
			}
			bufferhtml += '	.SuperContent { \n';
			bufferhtml += '	flow: page-flow; \n';
			bufferhtml += '	-webkit-flow: page-flow; \n';
			bufferhtml += '	display: none; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.printview:first-child { \n';
			bufferhtml += '	margin-top: 0; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.page:last-child { \n';
			bufferhtml += '	margin-bottom: 0; \n';
			bufferhtml += '	}\n';
			bufferhtml += '	}\n';
		}
		else { /*Outros navegadores*/
		
			bufferhtml += '	@media print { \n';
			bufferhtml += '	@page { \n';
			bufferhtml += '	size: '+ this.PrintPageSize +'; \n';
			bufferhtml += '	margin: '+ this.PrintPageMargin +'; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	body \n';
			bufferhtml += '	{ \n';
			bufferhtml += '	background-color: rgb(255, 255, 255); \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.printview { \n';
			bufferhtml += '	margin: initial; \n';
			bufferhtml += '	width: initial; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.toolbar { \n';
			bufferhtml += '	display: none; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.page { \n';
			bufferhtml += '	margin: '+ this.PrintPageMargin +'; \n';
			bufferhtml += '	border: none; \n';
			bufferhtml += '	border-radius: 0; \n';
			bufferhtml += '	box-shadow: 0 0 0px rgba(0, 0, 0, 0); \n';
			bufferhtml += '	background: initial; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.page-header, .page-body { \n';
			bufferhtml += '	border: none; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.page-header, .page-body { \n';
			bufferhtml += '	border: none; \n';
			bufferhtml += '	} \n';
			if(this.IsPrintPageHeader==false){
				bufferhtml += '	.page-header \n';
				bufferhtml += '	{ \n';
				bufferhtml += '	display: none; \n';
				bufferhtml += '	} \n';
			}
			if(this.IsPrintPageFooter==false){
				bufferhtml += '	.page-footer \n';
				bufferhtml += '	{ \n';
				bufferhtml += '	display: none; \n';
				bufferhtml += '	} \n';
			}
			bufferhtml += '	.SuperContent { \n';
			bufferhtml += '	flow: page-flow; \n';
			bufferhtml += '	-webkit-flow: page-flow; \n';
			bufferhtml += '	display: none; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.printview:first-child { \n';
			bufferhtml += '	margin-top: 0; \n';
			bufferhtml += '	} \n';
			bufferhtml += '	.page:last-child { \n';
			bufferhtml += '	margin-bottom: 0; \n';
			bufferhtml += '	}\n';
			bufferhtml += '	}\n';
		}
		

		this.addNewStyle(bufferhtml);
		

		bufferhtml = '<div class="printview">';
		bufferhtml += '	<div class="toolbar">';
		bufferhtml += '		<button id="print">'+ this.BtnCaptionPrint +'</button>';
		bufferhtml += '	</div>';
		bufferhtml += '	<div class="page">';
		bufferhtml += '		<div class="page-header">';
		bufferhtml += '			' + this.PageHeaderHtmlSource;
		bufferhtml += '				</div>';
		bufferhtml += '		<div class="page-body"></div>';
		bufferhtml += '		<div class="page-footer">'+ this.PageFooterHtmlSource +'</div>';
		bufferhtml += '	</div>';
		bufferhtml += '</div>';
		bufferhtml += '<div class="SuperContent"><div class="content">';
		bufferhtml += this.PageBodyHtmlSource;
		bufferhtml += '</div></div>';
		bufferhtml += '<input type="hidden" id="PageFooterHtmlMaskPag" value="'+ this.PageFooterHtmlMaskPag +'"/><input type="hidden" id="IsPageFooterHtmlDataPag" value="'+ this.IsPageFooterHtmlDataPag +'"/>';


		
		this.setHtml(bufferhtml);	
		
		if(this.BackGroundImage!="") {
			$("body").css( "background-image",  "url('"+this.BackGroundImage+"')");
		}
		if(this.PageBackGroundImage!="") {
			$(".page").css( "background-image",  "url('"+this.PageBackGroundImage+"')");
		}
		
		
		$("body").css( "font",  "10pt 'Arial'");
			
		
	$.fn.hasOverflow = function () {
		var el = $(this)[0];
		return el.scrollHeight > el.clientHeight;
	};
	$.fn.isTable = function () {
		var el = $(this)[0];
		return el.nodeType === 1 && (el.tagName || '').toUpperCase() === 'TABLE';
	};

	$.fn.withColumns = function(){
		var el = $(this);
		return el.is('[class^="columns"]');
	}


	var g = {},quebrarTabela=this.IsBreakingTable, modeRederizar=this.ModeRender; // Globals

	function makePage(template) {
		console.log('Making a new page');
		var clone = $('.page').first().clone();
		$(clone).find('.page-body').empty();
		$(clone).appendTo('.printview');
		g.currentPage = clone;
		g.currentPbody  =  getPageBody(clone);
		return $(clone).hasOverflow()? undefined: clone;
	}

	function getPageBody(page) {
		//console.log('Getting the page-body');
		var currentPage = page || makePage();
		return currentPage.find('.page-body');
	}


	function paginate(content,  page, cont) {
		console.log('pagination initiated');
		if (!Object.hasOwnProperty(g, 'currentPage')){
			g.currentPage = page || makePage();
		}
		if (!Object.hasOwnProperty(g, 'currentPbody')){
			g.currentPbody = getPageBody(page || g.currentPage || MakePage());
		}
		var currentContent = $(content),
			container;
		if (currentContent.withColumns()){
			container = currentContent.clone().empty().appendTo(g.currentPbody);
		} else if (currentContent.parent().withColumns() && cont){
			container = cont;
		} else {
			container = null;
		}
		

		if(modeRederizar==0) {
			console.log('working with html content');
			paginateHtml(currentContent, g.currentPage, container);
		}
		else{
			if (currentContent.isTable()) {
				console.log('working with a table');
				paginateTable(currentContent, g.currentPage);
			} else if (currentContent.children().size()>0) {
				//console.log('element has childrens');
				console.log(container);
				var i = 0,
					childrens = currentContent.children(),
					l = childrens.size(),
					child;
				for (; i < l; i++) {
					child = childrens[i];
					paginate( child,  g.currentPage, container);
				}
			} else {
				//console.log('working with text content');
				paginateText(currentContent, g.currentPage, container);
			}
		}
	}

	function paginateHtml(node, page, cont) {
		var currentPbody = getPageBody(page || g.currentPage || getMakePage()),
			regex = /(<([^>]+)>)/ig,/*/<(.|\n)*?>/g,*/
			container = node.clone().empty(),
			contentHtml = node.html(),
			contentHtml = contentHtml.replace(regex, " $1 "),
			wordArray = contentHtml.split(" "),
			contentHtml = "",
			contentHtmlAux = "",
			contentHtmlAux2 = "",
			arrayTag = [],
			i = 0,
			iux = 0,
			iuxInicio = 0,
			posicaoTag = 0,
			l = wordArray.length,
			oldText,
			word,
			wordAux = "";


		container.appendTo(cont ? cont : currentPbody);
		//console.log(currentPbody);    
		for (; i < l; i++) {
			word = wordArray[i];
			oldText = contentHtml;
			contentHtml += word + " ";
			//console.log('oldText:' + oldText);
			//console.log('word:' + word);
			
			// Não deixa quebra  tags html no documento
			if((word.indexOf("<") >= 0 && word.indexOf(">") < 0) || iux > 0) {
				iux = iux + 1;
				contentHtmlAux += word + " ";
				if(word.indexOf(">") >= 0){
					iuxInicio = iux
					iux = 0;
					container.html(contentHtml);
					//console.log('Tag word:' + contentHtmlAux);
					contentHtmlAux2 = contentHtmlAux 
					//contentHtmlAux = "";
				}	
				
			}
			else{
				//contentHtmlAux = "";
				iux = 0
				container.html(contentHtml);
			}
			if(word.indexOf("<") >= 0 && word.indexOf(">") >= 0) {
				if(word.indexOf("/") >= 0) {
					//console.log('1---cont:' + cont + '   word: ' + word);
					wordAux = word.replace("/","");
					//console.log('2---cont:' + cont + '   wordAux: ' + wordAux);
					wordAux = wordAux.replace(">","");
					posicaoTag = contentHtmlAux.lastIndexOf(wordAux);
					//console.log('1 + ---cont:' + cont + '   wordAux: ' + wordAux + '  ---- Tag posicaoTag:' + posicaoTag + ' Tag word:' + contentHtmlAux);
					contentHtmlAux = contentHtmlAux.substring(0,posicaoTag-word.length)
					//console.log('2 - cont:' + cont + '   iuxInicio: ' + iuxInicio + '   Tag word:' + contentHtmlAux);  
				}else
				contentHtmlAux += word + " ";
			}
			
			if (currentPbody.hasOverflow()) {
				//console.log('in text has encountred overflow');
				container.html(oldText);
				//alert('if oldText:' + oldText);
				//alert('container.html():' + container.html());
				//console.log([word, wordArray.slice(i)]);
				//console.log(' NOVA PAGINA cont:' + cont + '   iuxInicio: ' + iuxInicio + '   Tag word:' + contentHtmlAux2); 
				console.log(' NOVA PAGINA cont:' + cont + '   iuxInicio: ' + iuxInicio + '   Tag word contentHtmlAux:' + contentHtmlAux); 
				console.log(' NOVA PAGINA cont:' + cont + '   iuxInicio: ' + iuxInicio + '   Tag word' + word); 
				if(contentHtmlAux.indexOf("<td>") >=0 || contentHtmlAux.indexOf("<tr>") >=0)
					contentHtmlAux = contentHtmlAux2 + contentHtmlAux;
			
				paginate(
					//node.clone().empty().html(wordArray.slice(i - 1 - iuxInicio).join(' ')),
					node.clone().empty().html(contentHtmlAux+wordArray.slice(i).join(' ')),
					makePage()
				);
				break;
			}
		}
	}




	function paginateText(node, page, cont) {
		var currentPbody = getPageBody(page || g.currentPage || getMakePage()),
			container = node.clone().empty(),
			contentText = node.text(),
			wordArray = contentText.split(" "),
			currentText = "",
			i = 0,
			l = wordArray.length,
			oldText,
			word;
			
			//console.log('contentText:' + contentText);
			//console.log('wordArray:' + wordArray);
			//console.log('oldText:' + oldText);
			//console.log('word:' + word);

			
		container.appendTo(cont ? cont : currentPbody);
		//console.log(currentPbody);    
		for (; i < l; i++) {
			word = wordArray[i];
			oldText = currentText;
			currentText += word + " ";
			//console.log('oldText:' + oldText);
			//console.log('word:' + word);
			container.text(currentText);
			if (currentPbody.hasOverflow()) {
				//console.log('in text has encountred overflow');
				container.text(oldText);
				//alert('if oldText:' + oldText);
				//console.log([word, wordArray.slice(i)]);
				paginate(
					node.clone().empty().text(wordArray.slice(i - 1).join(' ')),
					makePage()
				);
				break;
			}
		}
	}

	function paginateTable(table, page, cont) {    
		var currentPbody = getPageBody(page || g.currentPage || makePage()),
			container = cont || table.clone(),
			currentTbody = container.find('tbody'),
			currentRows = container.find('tbody > tr'),
			i = 0,
			l = currentRows.length,
			row,
			controleOverflow = false;
			
		//console.log( '  l:' + l);
		currentTbody.empty();
		container.appendTo(currentPbody);
		
		if(quebrarTabela==false) {
			for (; i < l; i++) {
				row = $(currentRows[i]);
				currentTbody.append(row);
				//console.log('i:' + i + '  currentRows.slice(i):' + );
				if (currentPbody.hasOverflow()) {
					//console.log('i:' + i + '  controleOverflow = true;' );
					container.detach(); //desmembra a tabela que não coube na pagina
					controleOverflow = true;
					//break;
				}
			}
			if (controleOverflow == true) {
				paginate(container, makePage());
			}
		}else {
			for (; i < l; i++) {
				row = $(currentRows[i]);
				
				currentTbody.append(row);
				//console.log('i:' + i + '  currentRows.slice(i):' + );
				if (currentPbody.hasOverflow()) {
					//console.log('in table has encountred overflow');
					//console.log([container, container.find('tbody > tr').length]);
					//console.log([container, container.find('welton').length ? 'Sim---' : 'Não---']);
					//console.log([i, container.has(currentTbody) ? 'Sim' : 'Não']);
					row.detach();  //desmembra a linhas da tabela que não coube na pagina
					container = $(container).clone();
					currentTbody = container.find('tbody');
					currentTbody.empty().append(currentRows.slice(i));
					//console.log('i-----:' + i + '  currentTbody:' + currentTbody.text);
					//console.log('currentTbody.find(\'tr\').length:'+[container, currentTbody.find('tr').length]);
					paginate(container, makePage());
					break;
				}
				
			}
		}
	}

	$(function () {
		/* insert content and generate regions */
		paginate(
			$('.content'),
			$('.page')
		);
		/* remove pages without content
		$('.page').each(function(){
			var pbody = $(this).find('.page-body');
			if (pbody.text().length===0){
				$(this).remove();
			}
		});*/
		/* define page numbers and add current date */
		var pages = $('.page').length,
			date = new Date(),
			tDate = date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear(),
			txtPageFooterHtmlMaskPag   = document.getElementById("PageFooterHtmlMaskPag").value,
			txtIsPageFooterHtmlDataPag = document.getElementById("IsPageFooterHtmlDataPag").value,
			txtPageFooterHtmlMaskPag2 = ""
			paginaAtual = 0;
			
			
		$('.page').each(function (i, p) {
			paginaAtual = (1 + i);
			txtPageFooterHtmlMaskPag2 = txtPageFooterHtmlMaskPag.replace('#A#',paginaAtual);
			txtPageFooterHtmlMaskPag2 = txtPageFooterHtmlMaskPag2.replace('#T#',pages);
			var foot = $(p).find('.page-footer'),
			htmlPageFooterHtmlMaskPag = (txtPageFooterHtmlMaskPag2!="") ? '<span style="float: right">'+ txtPageFooterHtmlMaskPag2 +'</span>' : '',
			htmlIsPageFooterHtmlDataPag = (txtIsPageFooterHtmlDataPag=="true") ? '<span style="float: left;">' + tDate + '</span>' : '';
			
			$(foot).html(htmlIsPageFooterHtmlDataPag + $(foot).html() + htmlPageFooterHtmlMaskPag);
		});
		$('#print').click(function (ev) {
			window.print();
		});
		console.log($('.page').size());
	});
		

		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

    this.addNewStyle = function (newStyle) {
        var styleElement = document.getElementById("styles_js");
        if (!styleElement) {
            styleElement = document.createElement("style");
            styleElement.type = "text/css";
            styleElement.id = "styles_js";
            document.getElementsByTagName("head")[0].appendChild(styleElement)
        }
        styleElement.appendChild(document.createTextNode(newStyle))
    }
	


	
	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

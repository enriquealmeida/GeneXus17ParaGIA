function DigitalSign() {
	this.Width;
	this.Height;
	this.InstallersResourceURL;
	this.Mode;
	this.LogLevel;
	this.LogFile;
	this.Language;
	this.WSNative;
	this.Type;
	this.NoJavaMessage;
	this.JNLPMessage;
	this.MultipleDocSDT;
	this.OneTimePassword;
	this.ReceiverLocation;
	this.MultipleLocation;
	this.FinishLocation;
	this.CheckerLocation;
	this.FontName;
	this.FontSize;
	this.JRE_Arguments;
	this.ForceUseToken;
	this.EnableUseWinKeyStore;
	this.ForceUseWinKeyStore;
	this.TokenListLocation;
	this.XmlIncludeCertificate;
	this.XmlReferenceTag;
	this.XadesEpesPolicyIdentifierURI;
	this.XadesEpesPolicyData;
	this.XadesEpesPolicyEmbedded;
	this.PdfCertificationLevel;
	this.PdfShowSave;
	this.PdfShowPrint;
	this.WithCI;
	this.VisibleSignature;
	this.ExistWCInstallExtension;
	this.ExistWCInstallNative;
	this.ExistWCUpdateNative;
	this.ExistWCDownloadJNLP;
	this.ExistWCInstallOK;
	this.Browser;
	this.Platform;
	this.AlgorithmName;
	this.ExistCancelURL;
	this.CheckAll;

	this.show = function() {
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if (window.DigitalSignCreated == undefined) {
			GXC_DigitalSign_UC = this;

			var container = GXC_DigitalSign_UC.getContainerControl();
			var div = GXC_DigitalSign_UC.createElement("div", "GXCDigitalSign", "", "", "");
			container.appendChild(div);

			if (((GXC_DigitalSign_UC.Mode == 1) && (deployJava.versionCheck('1.6+'))) || (GXC_DigitalSign_UC.Mode == 3)) {
				if (GXC_DigitalSign_UC.ExistWCInstallOK) {
					if (GXC_DigitalSign_UC.InstallOK) {
						GXC_DigitalSign_UC.InstallOK();
					} else {
						alert('Digital Sign InstallOK event not defined! Contact system administrator.');
					}
				}
			} else {
				javaBrowser = false;

				var buttonInstallNative = GXC_DigitalSign_UC.createElement("button", "GXC_DigitalSign_ButtonInstallNative", "", "display:none;", "");
				buttonInstallNative.onclick = function(event) { 
					event.preventDefault();

					if (GXC_DigitalSign_UC.InstallNative) {
						GXC_DigitalSign_UC.InstallNative();
					} else {
						alert('Digital Sign InstallNative event not defined! Contact system administrator.');
					}
				}
				container.appendChild(buttonInstallNative);

				var buttonUpdateNative = GXC_DigitalSign_UC.createElement("button", "GXC_DigitalSign_ButtonUpdateNative", "", "display:none;", "");
				buttonUpdateNative.onclick = function(event) { 
					event.preventDefault();

					if (GXC_DigitalSign_UC.UpdateNative) {
						GXC_DigitalSign_UC.UpdateNative();
					} else {
						alert('Digital Sign UpdateNative event not defined! Contact system administrator.');
					}
				}
				container.appendChild(buttonUpdateNative);

				var buttonInstallOK = GXC_DigitalSign_UC.createElement("button", "GXC_DigitalSign_ButtonInstallOK", "", "display:none;", "");
				buttonInstallOK.onclick = function(event) { 
					event.preventDefault();

					if (GXC_DigitalSign_UC.InstallOK) {
						GXC_DigitalSign_UC.InstallOK();
					} else {
						alert('Digital Sign InstallOK event not defined! Contact system administrator.');
					}
				}
				container.appendChild(buttonInstallOK);

				var buttonOnCancel = GXC_DigitalSign_UC.createElement("button", "GXC_DigitalSign_ButtonOnCancel", "", "display:none;", "");
				buttonOnCancel.onclick = function(event) { 
					event.preventDefault();

					if (GXC_DigitalSign_UC.OnCancel) {
						GXC_DigitalSign_UC.OnCancel();
					} else {
						alert('Digital Sign OnCancel event not defined! Contact system administrator.');
					}
				}
				container.appendChild(buttonOnCancel);

				var buttonEnd = GXC_DigitalSign_UC.createElement("button", "GXC_DigitalSign_ButtonEnd", "", "display:none;", "");
				buttonEnd.onclick = function(event) { 
					event.preventDefault();
					GXC_DigitalSign_UC.onSignFinish();
				}
				container.appendChild(buttonEnd);
			}

			// Import doLoad scripts and styles
			if (!(false || !!document.documentMode)) { // not IE
				var translationUrl = gx.util.resourceUrl(gx.basePath + gx.staticDirectory + 'DigitalSign/DigitalSignTranslations.js', true)
				var script = [ translationUrl ];

				var customStyle = GXC_DigitalSign_UC.createElement("link", "customStyle", "", "", "");
				customStyle.href = gx.util.resourceUrl(gx.basePath + gx.staticDirectory + 'DigitalSign/CustomStyles.css');
				customStyle.type = "text/css";
				customStyle.rel = "stylesheet";
				document.head.appendChild(customStyle);

				var style = GXC_DigitalSign_UC.createElement("link", "digitalSignStyle", "", "", "");
				style.href = gx.util.resourceUrl(gx.basePath + gx.staticDirectory + 'DigitalSign/DigitalSignStyles.css');
				style.type = "text/css";
				style.rel = "stylesheet";
				document.head.appendChild(style);

				gx.http.loadScripts(script, GXC_DigitalSign_UC.doLoad);
			} else {
				gx.fx.obs.addObserver('gx.onload', this, GXC_DigitalSign_UC.doLoad);
			}

			window.DigitalSignCreated = 1;
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	this.doLoad = function() {
		GXC_DigitalSign_UC.detectBrowserAndPlatform();

		if (window.frameElement) {
			if (window.gx.popup.getPopup()) {
				window.parent.gx.popup.ext.resize(window.gx.popup.getPopup().id, 900, 450);
				window.parent.gx.popup.autofit();
				window.parent.gx.popup.ext.cid = window.gx.popup.getPopup().id;
				window.parent.gx.popup.ext.stopdrag();
			}
		}

		// If extension not installed

		if (document.getElementById("GXC_DigitalSign_Extension") == undefined) {
			// Install extension or Download JNLP
			var isChrome = (navigator.userAgent.indexOf("Chrome") != -1) && (!(navigator.userAgent.indexOf("Edge") != -1));
			var isFirefox = typeof InstallTrigger !== 'undefined'; // Firefox 1.0+
			var isMobile = ( navigator.userAgent.includes("Mobile") || navigator.userAgent.includes("Android") || navigator.userAgent.includes("iPhone") );

			if (isMobile) {
				var divBody = GXC_DigitalSign_UC.createElement("div", "", "backGround", "", "");

				var container = GXC_DigitalSign_UC.createElement("div", "", "Container", "width: 80%; margin: 15% auto; text-align: center; border: 1px solid black; border-radius: 15px;", "");
				var p = GXC_DigitalSign_UC.createElement("p", "", "", "font-size: 400%; color: black;", "Dispositivo no soportado");

				var div = GXC_DigitalSign_UC.createElement("div", "", "", "width: 80%; margin: 15% auto; text-align: center;", "");
				var img = GXC_DigitalSign_UC.createElement("img", "", "", "width: 80%;", "");
				img.src = gx.util.resourceUrl(gx.basePath + gx.staticDirectory + 'DigitalSign/NoMobile.png');
				var texto = GXC_DigitalSign_UC.createElement("p", "", "", "font-size: 300%; color: red; margin-top: 10%;", "Los dispositivos mobile no estan soportados por esta aplicación.");

				divBody.appendChild(container);
				container.appendChild(p);
				container.appendChild(div);
				div.appendChild(img);
				div.appendChild(texto);

				document.getElementById("GXCDigitalSign").appendChild(divBody);
			} else {

				if (((GXC_DigitalSign_UC.Mode == 1) && (deployJava.versionCheck('1.6+'))) || (GXC_DigitalSign_UC.Mode == 3)) {
					
					// Sign JNLP mode
					GXC_DigitalSign_UC.JNLPSign();

				} else {

					// Sign Extension mode

					if (isChrome || isFirefox) {
						// Install Extension Button
						if ( GXC_DigitalSign_UC.ExistWCInstallExtension ) {
							GXC_DigitalSign_UC.InstallExtension();
						} else {
							var header = GXC_DigitalSign_UC.createElement("header", "", "", "", "");
							var headerp = GXC_DigitalSign_UC.createElement("p", "", "", "color: red;", GXC_DigitalSign_UC.i18n(['Extension not installed. Please follow the steps below for installing.']));
							header.appendChild(headerp);
	
							var instructionsContainer = GXC_DigitalSign_UC.createElement("div", "", "instructionsContainer", "", "");
							var installationInstructions1 = GXC_DigitalSign_UC.createElement("p", "", "", "", GXC_DigitalSign_UC.i18n(['Press the button below to install it']));
							var installationButton = GXC_DigitalSign_UC.createElement("button", "Installation_Button", "", "", GXC_DigitalSign_UC.i18n(['Install extension']));
							instructionsContainer.appendChild(installationInstructions1);
							instructionsContainer.appendChild(installationButton);
							instructionsContainer.appendChild(document.createElement("br"));
	
							installationButton.onclick = function(event) { 
								event.preventDefault();
	
								GXC_DigitalSign_UC.installBrowserExtension();
	
								if (window.frameElement) {
									if (isChrome) {
										var xhttp = new XMLHttpRequest();
										const interval = setInterval(function() {
											xhttp.onreadystatechange = function() {
												if (xhttp.readyState == 4 && xhttp.status == 200) {
													clearInterval(interval);
	
													localStorage.removeItem("GXClang");
													console.clear();
													window.frameElement.src = window.frameElement.src;
												}
											};
	
											xhttp.open("GET", "chrome-extension://glmjepecpbjeajhnecnjjpbnncppjdee/content_script_idle.js", true);
											xhttp.send();			
										}, 2000);
									}
								}
							};
	
							if (window.parent.FirefoxRefreshingExtension) {
								setTimeout(GXC_DigitalSign_UC.refreshFirefox, 5000, window);
							}
	
							// JNLP
							var jnlpButton = GXC_DigitalSign_UC.createElement("a", "", "link", "font-size: 12px;", GXC_DigitalSign_UC.i18n(['If you would like to download JNLP file press this link.']));
							instructionsContainer.appendChild(jnlpButton);
							jnlpButton.onclick = function(event) { 
								event.preventDefault();
	
								GXC_DigitalSign_UC.JNLPSign();
							};
	
							// Insert texts and buttons in page
							var divBody = GXC_DigitalSign_UC.createElement("div", "", "backGround", "", "");
							var div = GXC_DigitalSign_UC.createElement("div", "UC_Extension", "Container", "border: 1px solid;", "");
							divBody.appendChild(div);
	
							div.appendChild(header);
							div.appendChild(instructionsContainer);
							document.getElementById("GXCDigitalSign").appendChild(divBody);
						}
					} else {
						if (GXC_DigitalSign_UC.ExistWCInstallOK) {
							if (GXC_DigitalSign_UC.InstallOK) {
								GXC_DigitalSign_UC.InstallOK();
							} else {
								alert('Digital Sign InstallOK event not defined! Contact system administrator.');
							}
						} else {
							GXC_DigitalSign_UC.JNLPSign();
						}
					}
				}
			}
		} else {

			// run else: if Extension is installed

			if (window.frameElement) {
				if (window.gx.popup.getPopup()) {
					window.parent.gx.popup.ext.resize(window.gx.popup.getPopup().id, parseInt(GXC_DigitalSign_UC.Width.replace('px',''))+100, parseInt(GXC_DigitalSign_UC.Height.replace('px',''))+100);
					window.parent.gx.popup.autofit();
					window.parent.gx.popup.ext.cid = window.gx.popup.getPopup().id;
					window.parent.gx.popup.ext.stopdrag();
				}
			}

			localStorage.setItem("GXClang",GXC_DigitalSign_UC.Language);
			localStorage.setItem("gx.resourceURL",gx.util.resourceUrl(gx.basePath + gx.staticDirectory + "DigitalSign/", true));
			localStorage.setItem("Width", GXC_DigitalSign_UC.Width);
			localStorage.setItem("Height", GXC_DigitalSign_UC.Height);
			localStorage.setItem("Mode", GXC_DigitalSign_UC.Mode);
			localStorage.setItem("LogLevel", GXC_DigitalSign_UC.LogLevel);
			localStorage.setItem("LogFile", GXC_DigitalSign_UC.LogFile);
			localStorage.setItem("Language", GXC_DigitalSign_UC.Language);
			localStorage.setItem("WSNative", GXC_DigitalSign_UC.WSNative);
			localStorage.setItem("Type", GXC_DigitalSign_UC.Type);
			localStorage.setItem("NoJavaMessage", GXC_DigitalSign_UC.NoJavaMessage);
			localStorage.setItem("JNLPMessage", GXC_DigitalSign_UC.JNLPMessage);
			localStorage.setItem("MultipleDocSDT", GXC_DigitalSign_UC.MultipleDocSDT);
			localStorage.setItem("OneTimePassword", GXC_DigitalSign_UC.OneTimePassword);
			localStorage.setItem("ReceiverLocation", GXC_DigitalSign_UC.ReceiverLocation);
			localStorage.setItem("MultipleLocation", GXC_DigitalSign_UC.MultipleLocation);
			localStorage.setItem("FinishLocation", GXC_DigitalSign_UC.FinishLocation);
			localStorage.setItem("CheckerLocation", GXC_DigitalSign_UC.CheckerLocation);
			localStorage.setItem("FontName", GXC_DigitalSign_UC.FontName);
			localStorage.setItem("FontSize", GXC_DigitalSign_UC.FontSize);
			localStorage.setItem("JRE_Arguments", GXC_DigitalSign_UC.JRE_Arguments);
			localStorage.setItem("ForceUseToken", GXC_DigitalSign_UC.ForceUseToken);
			localStorage.setItem("EnableUseWinKeyStore", GXC_DigitalSign_UC.EnableUseWinKeyStore);
			localStorage.setItem("ForceUseWinKeyStore", GXC_DigitalSign_UC.ForceUseWinKeyStore);
			localStorage.setItem("TokenListLocation", GXC_DigitalSign_UC.TokenListLocation);
			localStorage.setItem("XmlIncludeCertificate", GXC_DigitalSign_UC.XmlIncludeCertificate);
			localStorage.setItem("XmlReferenceTag", GXC_DigitalSign_UC.XmlReferenceTag);
			localStorage.setItem("XadesEpesPolicyIdentifierURI", GXC_DigitalSign_UC.XadesEpesPolicyIdentifierURI);
			localStorage.setItem("XadesEpesPolicyData", GXC_DigitalSign_UC.XadesEpesPolicyData);
			localStorage.setItem("XadesEpesPolicyEmbedded", GXC_DigitalSign_UC.XadesEpesPolicyEmbedded);
			localStorage.setItem("PdfCertificationLevel", GXC_DigitalSign_UC.PdfCertificationLevel);
			localStorage.setItem("PdfShowSave", GXC_DigitalSign_UC.PdfShowSave);
			localStorage.setItem("PdfShowPrint", GXC_DigitalSign_UC.PdfShowPrint);
			localStorage.setItem("WithCI", GXC_DigitalSign_UC.WithCI);
			localStorage.setItem("VisibleSignature", GXC_DigitalSign_UC.VisibleSignature);
			localStorage.setItem("ExistWCInstallNative", GXC_DigitalSign_UC.ExistWCInstallNative);
			localStorage.setItem("ExistWCUpdateNative", GXC_DigitalSign_UC.ExistWCUpdateNative);
			localStorage.setItem("ExistWCInstallOK", GXC_DigitalSign_UC.ExistWCInstallOK);
			localStorage.setItem("AlgorithmName", GXC_DigitalSign_UC.AlgorithmName);
			localStorage.setItem("ExistCancelURL", GXC_DigitalSign_UC.ExistCancelURL);
			localStorage.setItem("CheckAll", GXC_DigitalSign_UC.CheckAll);

			// 1.2.6.1
			if (GXC_DigitalSign_UC.InstallersResourceURL != "" && GXC_DigitalSign_UC.InstallersResourceURL != null) {
				if (!GXC_DigitalSign_UC.InstallersResourceURL.endsWith("/")) {
					let InstallersResourceURL = GXC_DigitalSign_UC.InstallersResourceURL + "/";
					localStorage.setItem("InstallersResourceURL", InstallersResourceURL);
				} else {
					localStorage.setItem("InstallersResourceURL", GXC_DigitalSign_UC.InstallersResourceURL);
				}
			} else {
				localStorage.setItem("InstallersResourceURL", gx.util.resourceUrl(gx.basePath + gx.staticDirectory + "DigitalSign/", true));
			}

			localStorage.setItem("MinimalVersion", "1.2.6.2");
		}
	}

	this.installBrowserExtension = function() {
		// Install Extension
		if (GXC_DigitalSign_UC.Browser == "Chrome") {
			var b=1050;
			var c=670;
			var f=(screen.width-b)/2;
			var e=(screen.height-c)/4;
			window.open("https://chrome.google.com/webstore/detail/glmjepecpbjeajhnecnjjpbnncppjdee","GXC Digital Sign Extension","width="+b+", height="+c+", top="+e+", left="+f);
		} else {
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (xhttp.readyState == 4 && xhttp.status == 200) {
					var firefoxVersion = JSON.parse(xhttp.responseText).version;
					var installLink = document.createElement("a");
					installLink.download = "gxc_digital_sign-" + firefoxVersion + "-fx";
					installLink.href = "https://componentes.genexusconsulting.com/DigitalSign/Firefox/gxc_digital_sign-" + firefoxVersion + "-fx.xpi";
					document.body.appendChild(installLink);
					installLink.click();
					installLink.remove();

					window.parent.FirefoxRefreshingExtension = 1;
					setTimeout(GXC_DigitalSign_UC.refreshFirefox, 5000, window);
				}
			};
			xhttp.open("GET", "https://componentes.genexusconsulting.com/DigitalSign/last-firefox.json", true);
			xhttp.send();
		}
	}
	
	this.JNLPSign = function() {
		if ( GXC_DigitalSign_UC.ExistWCDownloadJNLP ) {
			if (GXC_DigitalSign_UC.WCDownloadJNLP) {
				document.getElementById('GXCDigitalSign').style.display = 'none';
				GXC_DigitalSign_UC.WCDownloadJNLP();
			} else {
				if (!(false || !!document.documentMode)) { // not IE
					alert(GXC_DigitalSign_UC.i18n(['WCDownloadJNLP event missing. Please contact administrator.']));
				} else {
					alert('DownloadJNLP event missing. Please contact administrator.');
				}
			}
		} else {
			var div = document.getElementById("GXCDigitalSign");
			GXC_DigitalSign_UC.removeChilds(div);

			var headTitle = 'JNLP file is downloading. Please follow the steps below for sign.';
			var titles = new Array('Get JNLP file', 'Open JNLP file');
			var images = new Array("JNLPStep_1.png", "JNLPStep_2.png");
			var descriptions = new Array('Go to downloads and find JNLP file', 'Sign document with JNLP');

			var instContainer = GXC_DigitalSign_UC.createElement("div", "", "backGround", "display: block;", "");
			var instGlobo = GXC_DigitalSign_UC.createElement("div", "", "Container", "padding: 30px; width: 1200px;", "");

			var head = GXC_DigitalSign_UC.createElement("header", "", "", "width: 95%; height: 30pt; text-align: center;", "");
			var headp = GXC_DigitalSign_UC.createElement("p", "", "", "color: red; font-size: 20.0pt; font-family: 'Arial'; font-style: normal; margin: 0;", headTitle);
			head.appendChild(headp);

			var systemInformation = GXC_DigitalSign_UC.createElement("table", "", "", "font-size: 15pt; margin-top: 15px; margin-bottom: 40px;", "");
			systemInformation.setAttribute("align", "center");

			var tr = document.createElement("tr");
			tr.width = '100%';
			var tdBrowser1 = document.createElement("td");
			tdBrowser1.innerHTML = 'Browser:';
			var tdBrowserN1 = document.createElement("td");
			tdBrowserN1.innerHTML = GXC_DigitalSign_UC.Browser;

			var tr2 = document.createElement("tr");
			tr2.width = '100%';
			var tdBrowser2 = document.createElement("td");
			tdBrowser2.innerHTML = 'Platform:';
			var tdBrowserN2 = document.createElement("td");
			tdBrowserN2.innerHTML = GXC_DigitalSign_UC.Platform;

			tr.appendChild(tdBrowser1);
			tr.appendChild(tdBrowserN1);
			tr2.appendChild(tdBrowser2);
			tr2.appendChild(tdBrowserN2);
			systemInformation.appendChild(tr);
			systemInformation.appendChild(tr2);

			var stepsTable = GXC_DigitalSign_UC.createElement("table", "", "" , "border: 1px solid black; padding: 20pt;" , "");
			stepsTable.setAttribute("align", "center");
			stepsTable.width = '1000px';

			var tr = document.createElement("tr");
			tr.setAttribute("align", "center");
			tr.width = '100%';
			var tr2 = document.createElement("tr");
			tr2.setAttribute("align", "center");
			tr2.width = '100%';
			var tr3 = document.createElement("tr");
			tr3.setAttribute("align", "center");
			tr3.width = '100%';

			var tdTitle1 = GXC_DigitalSign_UC.createElement("td", "", "" , "height: 25px; width: 49%; font-family: 'Arial'; font-size: 15.0pt; font-weight: normal; font-style: normal;" , "");
			tdTitle1.innerHTML = titles[0];
			var tdTitle2 = GXC_DigitalSign_UC.createElement("td", "", "" , "height: 25px; width: 49%; font-family: 'Arial'; font-size: 15.0pt; font-weight: normal; font-style: normal;" , "");
			tdTitle2.innerHTML = titles[1];

			var tdImage1 = GXC_DigitalSign_UC.createElement("td", "", "" , "height: 25px; width: 49%; font-family: 'Arial'; font-size: 15.0pt; font-weight: normal; font-style: normal;" , "");
			var img1 = GXC_DigitalSign_UC.createElement("img", "", "", "margin: 10px; width: 1000px; max-width: 300px;", "");
			img1.src = gx.util.resourceUrl(gx.basePath + gx.staticDirectory + "DigitalSign/", true) + images[0];
			tdImage1.appendChild(img1);
			var tdImage2 = GXC_DigitalSign_UC.createElement("td", "", "" , "height: 25px; width: 49%; font-family: 'Arial'; font-size: 15.0pt; font-weight: normal; font-style: normal;" , "");
			var img2 = GXC_DigitalSign_UC.createElement("img", "", "", "margin: 10px; width: 1000px; max-width: 300px;", "");
			img2.src = gx.util.resourceUrl(gx.basePath + gx.staticDirectory + "DigitalSign/", true) + images[1];
			tdImage2.appendChild(img2);

			var tdDescriptions1 = GXC_DigitalSign_UC.createElement("td", "", "" , "height: 25px; width: 49%; font-family: 'Arial'; font-size: 15.0pt; font-weight: normal; font-style: normal;" , "");
			tdDescriptions1.innerHTML = descriptions[0];
			var tdDescriptions2 = GXC_DigitalSign_UC.createElement("td", "", "" , "height: 25px; width: 49%; font-family: 'Arial'; font-size: 15.0pt; font-weight: normal; font-style: normal;" , "");
			tdDescriptions2.innerHTML = descriptions[1];

			tr.appendChild(tdTitle1);
			tr.appendChild(tdTitle2);
			tr2.appendChild(tdImage1);
			tr2.appendChild(tdImage2);
			tr3.appendChild(tdDescriptions1);
			tr3.appendChild(tdDescriptions2);

			stepsTable.appendChild(tr);
			stepsTable.appendChild(tr2);
			stepsTable.appendChild(tr3);

			instGlobo.appendChild(head);
			instGlobo.appendChild(systemInformation);
			instGlobo.appendChild(stepsTable);

			instContainer.appendChild(instGlobo);

			div.appendChild(instContainer);
		}


		// DOWNLOAD JNLP
		if (GXC_DigitalSign_UC.DownloadJNLP) {
			GXC_DigitalSign_UC.DownloadJNLP();
		} else {
			if (!(false || !!document.documentMode)) { // not IE
				alert(GXC_DigitalSign_UC.i18n(['DownloadJNLP event missing. Please contact administrator.']));
			} else {
				alert('DownloadJNLP event missing. Please contact administrator.');
			}
		}

		if ((GXC_DigitalSign_UC.CheckerLocation) && (GXC_DigitalSign_UC.CheckerLocation !== "")){
			if (!((window.Notification == undefined) || (!Notification))) {
				if (Notification.permission !== "granted")
				Notification.requestPermission();
			}

			GXC_DigitalSign_UC.sendAsyncRequest();
		}
	}

	this.check = function(result) {
		if (result == "notfinished"){
			setTimeout(GXC_DigitalSign_UC.sendAsyncRequest, 5000);
		} else{
			GXC_DigitalSign_UC.onSignFinish();
		}
	}

	this.getGenerator = function() {
		var gen;
		if (gx.gen.isDotNet())
			gen = "c#";
		else if (gx.gen.ruby)
			gen = "ruby";
		else
			gen = "java";
		return gen;
	}

	this.getDocument = function() {
		var gen = this.getGenerator();
		if (gen == "c#") {
			if (this.Document.indexOf(".") != -1) {
				return this.Document;
			} else {
				return this.Document + ".aspx";
			}
		}
		if (gen == "java") {
				return this.Document;
		}
	}

	this.onSignFinish = function() {
		if (GXC_DigitalSign_UC.OnComplete) {
			GXC_DigitalSign_UC.OnComplete();
		} else {
			if (!(false || !!document.documentMode)) { // not IE
				alert(GXC_DigitalSign_UC.i18n(['Digital Sign onComplete event not defined! Contact system administrator.']));
			} else {
				alert('Digital Sign onComplete event not defined! Contact system administrator.');
			}
		}
	}

	this.notifyMe = function() {
		if ((window.Notification == undefined) || (!Notification)) {
			return;
		}

		if (Notification.permission !== "granted")
			Notification.requestPermission();
		else {
			var notification;
			if (!(false || !!document.documentMode)) { // not IE
				notification = new Notification('Signature finished', {
					body: 'The signature process has ended.',
				});
			} else {
				notification = new Notification('Signature finished', {
					body: 'The signature process has ended.',
				});
			}

			notification.onclick = function () {
				window.focus();
				notification.close();
			};
		}
	}

	this.sendAsyncRequest = function() {
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				GXC_DigitalSign_UC.check(xhttp.responseText);
			}
		};
		xhttp.open("GET", GXC_DigitalSign_UC.CheckerLocation + '?' + encodeURIComponent(GXC_DigitalSign_UC.OneTimePassword), true);
		xhttp.send();
	}

	// Utils
	this.removeChilds = function(myNode){
		while (myNode.firstChild) {
			myNode.removeChild(myNode.firstChild);
		}
	}

	this.createElement = function(type, id, classname, style, text) {
		var element = document.createElement(type);

		if (id != "") {
			element.id = id;
		}

		if (classname != "") {
			element.className = classname;
		}

		if (style != "") {
			element.style = style;
		}

		if (text != "") {
			element.appendChild(document.createTextNode(text));
		}

		return element;
	}

	this.refreshFirefox = function(win) {
		win.frameElement.src = win.frameElement.src;
	}

	this.detectBrowserAndPlatform = function() {
		var isChrome = (navigator.userAgent.indexOf("Chrome") != -1) && (!(navigator.userAgent.indexOf("Edge") != -1));
		var isFirefox = typeof InstallTrigger !== 'undefined'; // Firefox 1.0+
		var isSafari = /constructor/i.test(window.HTMLElement) || (function (p) { return p.toString() === "[object SafariRemoteNotification]"; })(!window['safari'] || (typeof safari !== 'undefined' && safari.pushNotification)); // Safari 3.0+
		var isIE = false || !!document.documentMode; // Internet Explorer 6-11
		var isEdge = !isIE && !!window.StyleMedia; // Edge 20+

		if (isChrome) {
			GXC_DigitalSign_UC.Browser = 'Chrome';
		} else if ( isFirefox ) {
			GXC_DigitalSign_UC.Browser = 'Firefox';
		} else if ( isEdge ) {
			GXC_DigitalSign_UC.Browser = 'Edge';
		} else {
			GXC_DigitalSign_UC.Browser = 'Chrome';
		}

		var platform = 'x64';

		if (navigator.userAgent.includes("x64")) {
			platform = 'x64';
		} else {
			platform = 'x86';
		}

		if (navigator.platform == "Win32") {
			GXC_DigitalSign_UC.Platform = 'Windows ' + platform;
		} else if (navigator.platform == "MacIntel") {
			GXC_DigitalSign_UC.Platform = 'Mac ' + platform;
		} else if (navigator.platform.includes("Linux")) {
			GXC_DigitalSign_UC.Platform = 'Linux ' + platform;
		}
	}
	///UserCodeRegionEnd: (do not remove this comment.):
}

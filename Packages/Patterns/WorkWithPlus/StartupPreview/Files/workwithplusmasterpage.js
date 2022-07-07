/*!   GeneXus C# 15_0_4-113785 on 7/13/2017 10:16:28.14
*/
gx.evt.autoSkip = false;
gx.define('wwpbaseobjects.workwithplusmasterpage', false, function () {
   this.ServerClass =  "wwpbaseobjects.workwithplusmasterpage" ;
   this.PackageName =  "GeneXus.Programs" ;
   this.setObjectType("web");
   this.IsMasterPage=true;
   this.hasEnterEvent = false;
   this.skipOnEnter = false;
   this.autoRefresh = true;
   this.fullAjax = true;
   this.supportAjaxEvents =  true ;
   this.ajaxSecurityToken =  true ;
   this.SetStandaloneVars=function()
   {
   };
   this.e11091_client=function()
   {
      this.clearMessages();
      
      this.refreshOutputs([]);
   };
   this.e18091_client=function()
   {
      this.clearMessages();
      if ( this.DDO_ADMINAG_MPAGEContainer.ActiveEventKey == "ActionLogOut" )
      {
         this.s112_client();
      }
      this.refreshOutputs([]);
   };
   this.s112_client=function()
   {
   };
   this.e17091_client=function()
   {
      this.clearMessages();
      this.refreshOutputs([]);
   };
   this.e12092_client=function()
   {
      this.executeServerEvent("DDO_LANGUAGESICONS_MPAGE.ONOPTIONCLICKED_MPAGE", false, null, true, true);
   };
   this.e14092_client=function()
   {
      this.executeServerEvent("DOENGLISHLINK_MPAGE", false, null, false, false);
   };
   this.e15092_client=function()
   {
      this.executeServerEvent("DOSPANISHLINK_MPAGE", false, null, false, false);
   };
   this.e19092_client=function()
   {
      this.executeServerEvent("ENTER_MPAGE", true, null, false, false);
   };
   this.e20092_client=function()
   {
      this.executeServerEvent("CANCEL_MPAGE", true, null, false, false);
   };
   this.GXValidFnc = [];
   var GXValidFnc = this.GXValidFnc ;
   this.GXCtrlIds=[2,3,4,5,6,7,8,9,10,11,12,13,14,17,20,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,45,46,47,48,49,51,52,53,54,58,59,60,61,62,63];
   this.GXLastCtrlId =63;


   this.DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer = gx.uc.getNew(this, 57, 0, "BootstrapTooltip", "DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer", "Dvelop_gxbootstrap_tooltip1", "DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGE");
   var DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer = this.DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer;
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("Class", "Class", "", "char");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("Enabled", "Enabled", true, "boolean");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("ClassSelector", "Classselector", "BootstrapTooltip", "str");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("DefaultPosition", "Defaultposition", "bottom", "str");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("LabelsShowDelay", "Labelsshowdelay", 300, "num");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("ButtonsShowDelay", "Buttonsshowdelay", 300, "num");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("InputsShowDelay", "Inputsshowdelay", 300, "num");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("ImagesShowDelay", "Imagesshowdelay", 0, "num");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("HideDelay", "Hidedelay", 0, "num");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setProp("Visible", "Visible", true, "bool");
   DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer.setC2ShowFunction(function(UC) { UC.show(); });
   this.setUserControl(DVELOP_GXBOOTSTRAP_TOOLTIP1_MPAGEContainer);
   this.DDO_ADMINAG_MPAGEContainer = gx.uc.getNew(this, 18, 0, "BootstrapDropDownOptions", "DDO_ADMINAG_MPAGEContainer", "Ddo_adminag", "DDO_ADMINAG_MPAGE");
   var DDO_ADMINAG_MPAGEContainer = this.DDO_ADMINAG_MPAGEContainer;
   DDO_ADMINAG_MPAGEContainer.setProp("Class", "Class", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("Enabled", "Enabled", true, "boolean");
   DDO_ADMINAG_MPAGEContainer.setDynProp("Icon", "Icon", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("Caption", "Caption", "", "str");
   DDO_ADMINAG_MPAGEContainer.setProp("Tooltip", "Tooltip", "", "str");
   DDO_ADMINAG_MPAGEContainer.setProp("Cls", "Cls", "ActionGroupHeader", "str");
   DDO_ADMINAG_MPAGEContainer.setProp("ActiveEventKey", "Activeeventkey", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("FilteredText_set", "Filteredtext_set", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("FilteredText_get", "Filteredtext_get", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("FilteredTextTo_set", "Filteredtextto_set", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("FilteredTextTo_get", "Filteredtextto_get", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("SelectedValue_set", "Selectedvalue_set", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("SelectedValue_get", "Selectedvalue_get", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("SelectedText_set", "Selectedtext_set", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("SelectedText_get", "Selectedtext_get", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("GAMOAuthToken", "Gamoauthtoken", "", "char");
   DDO_ADMINAG_MPAGEContainer.setDynProp("TitleControlAlign", "Titlecontrolalign", "Automatic", "str");
   DDO_ADMINAG_MPAGEContainer.setProp("DropDownOptionsType", "Dropdownoptionstype", "Regular", "str");
   DDO_ADMINAG_MPAGEContainer.setProp("TitleControlIdToReplace", "Titlecontrolidtoreplace", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("IncludeSortASC", "Includesortasc", false, "boolean");
   DDO_ADMINAG_MPAGEContainer.setProp("IncludeSortDSC", "Includesortdsc", false, "boolean");
   DDO_ADMINAG_MPAGEContainer.setProp("SortedStatus", "Sortedstatus", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("IncludeFilter", "Includefilter", false, "boolean");
   DDO_ADMINAG_MPAGEContainer.setProp("FilterType", "Filtertype", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("FilterIsRange", "Filterisrange", false, "boolean");
   DDO_ADMINAG_MPAGEContainer.setProp("IncludeDataList", "Includedatalist", false, "boolean");
   DDO_ADMINAG_MPAGEContainer.setProp("DataListType", "Datalisttype", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("AllowMultipleSelection", "Allowmultipleselection", false, "boolean");
   DDO_ADMINAG_MPAGEContainer.setProp("DataListFixedValues", "Datalistfixedvalues", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("DataListProc", "Datalistproc", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("DataListUpdateMinimumCharacters", "Datalistupdateminimumcharacters", '', "int");
   DDO_ADMINAG_MPAGEContainer.setProp("FixedFilters", "Fixedfilters", "", "str");
   DDO_ADMINAG_MPAGEContainer.setProp("SelectedFixedFilter", "Selectedfixedfilter", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("SortASC", "Sortasc", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("SortDSC", "Sortdsc", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("LoadingData", "Loadingdata", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("CleanFilter", "Cleanfilter", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("RangeFilterFrom", "Rangefilterfrom", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("RangeFilterTo", "Rangefilterto", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("NoResultsFound", "Noresultsfound", "", "char");
   DDO_ADMINAG_MPAGEContainer.setProp("SearchButtonText", "Searchbuttontext", "Search", "str");
   DDO_ADMINAG_MPAGEContainer.DropDownOptionsTitleSettingsIcons = '';
   DDO_ADMINAG_MPAGEContainer.addV2CFunction('AV5AdminAGData', "vADMINAGDATA_MPAGE", 'SetDropDownOptionsData');
   DDO_ADMINAG_MPAGEContainer.addC2VFunction(function(UC) { UC.ParentObject.AV5AdminAGData=UC.GetDropDownOptionsData();gx.fn.setControlValue("vADMINAGDATA_MPAGE",UC.ParentObject.AV5AdminAGData); });
   DDO_ADMINAG_MPAGEContainer.setProp("Visible", "Visible", true, "bool");
   DDO_ADMINAG_MPAGEContainer.setC2ShowFunction(function(UC) { UC.show(); });
   DDO_ADMINAG_MPAGEContainer.addEventHandler("OnOptionClicked", this.e18091_client);
   this.setUserControl(DDO_ADMINAG_MPAGEContainer);
   this.DDO_LANGUAGESICONS_MPAGEContainer = gx.uc.getNew(this, 21, 0, "BootstrapDropDownOptions", "DDO_LANGUAGESICONS_MPAGEContainer", "Ddo_languagesicons", "DDO_LANGUAGESICONS_MPAGE");
   var DDO_LANGUAGESICONS_MPAGEContainer = this.DDO_LANGUAGESICONS_MPAGEContainer;
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("Class", "Class", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("Enabled", "Enabled", true, "boolean");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("Icon", "Icon", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setDynProp("Caption", "Caption", gx.getMessage( "Language "), "str");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("Tooltip", "Tooltip", "", "str");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("Cls", "Cls", "DropDownOptionsHeader", "str");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("ActiveEventKey", "Activeeventkey", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("FilteredText_set", "Filteredtext_set", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("FilteredText_get", "Filteredtext_get", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("FilteredTextTo_set", "Filteredtextto_set", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("FilteredTextTo_get", "Filteredtextto_get", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("SelectedValue_set", "Selectedvalue_set", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("SelectedValue_get", "Selectedvalue_get", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("SelectedText_set", "Selectedtext_set", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("SelectedText_get", "Selectedtext_get", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("GAMOAuthToken", "Gamoauthtoken", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setDynProp("TitleControlAlign", "Titlecontrolalign", "Automatic", "str");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("DropDownOptionsType", "Dropdownoptionstype", "Regular", "str");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("TitleControlIdToReplace", "Titlecontrolidtoreplace", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("IncludeSortASC", "Includesortasc", false, "boolean");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("IncludeSortDSC", "Includesortdsc", false, "boolean");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("SortedStatus", "Sortedstatus", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("IncludeFilter", "Includefilter", false, "boolean");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("FilterType", "Filtertype", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("FilterIsRange", "Filterisrange", false, "boolean");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("IncludeDataList", "Includedatalist", false, "boolean");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("DataListType", "Datalisttype", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("AllowMultipleSelection", "Allowmultipleselection", false, "boolean");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("DataListFixedValues", "Datalistfixedvalues", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("DataListProc", "Datalistproc", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("DataListUpdateMinimumCharacters", "Datalistupdateminimumcharacters", '', "int");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("FixedFilters", "Fixedfilters", "", "str");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("SelectedFixedFilter", "Selectedfixedfilter", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("SortASC", "Sortasc", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("SortDSC", "Sortdsc", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("LoadingData", "Loadingdata", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("CleanFilter", "Cleanfilter", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("RangeFilterFrom", "Rangefilterfrom", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("RangeFilterTo", "Rangefilterto", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("NoResultsFound", "Noresultsfound", "", "char");
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("SearchButtonText", "Searchbuttontext", "Search", "str");
   DDO_LANGUAGESICONS_MPAGEContainer.DropDownOptionsTitleSettingsIcons = '';
   DDO_LANGUAGESICONS_MPAGEContainer.addV2CFunction('AV11LanguagesIconsData', "vLANGUAGESICONSDATA_MPAGE", 'SetDropDownOptionsData');
   DDO_LANGUAGESICONS_MPAGEContainer.addC2VFunction(function(UC) { UC.ParentObject.AV11LanguagesIconsData=UC.GetDropDownOptionsData();gx.fn.setControlValue("vLANGUAGESICONSDATA_MPAGE",UC.ParentObject.AV11LanguagesIconsData); });
   DDO_LANGUAGESICONS_MPAGEContainer.setProp("Visible", "Visible", true, "bool");
   DDO_LANGUAGESICONS_MPAGEContainer.setC2ShowFunction(function(UC) { UC.show(); });
   DDO_LANGUAGESICONS_MPAGEContainer.addEventHandler("OnOptionClicked", this.e12092_client);
   this.setUserControl(DDO_LANGUAGESICONS_MPAGEContainer);
   GXValidFnc[2]={fld:"",grid:0};
   GXValidFnc[3]={fld:"LAYOUTMAINTABLE",grid:0};
   GXValidFnc[4]={fld:"",grid:0};
   GXValidFnc[5]={fld:"",grid:0};
   GXValidFnc[6]={fld:"TABLEMAIN",grid:0};
   GXValidFnc[7]={fld:"",grid:0};
   GXValidFnc[8]={fld:"",grid:0};
   GXValidFnc[9]={fld:"TABLEHEADER",grid:0};
   GXValidFnc[10]={fld:"",grid:0};
   GXValidFnc[11]={fld:"",grid:0};
   GXValidFnc[12]={fld:"HEADER",grid:0};
   GXValidFnc[13]={fld:"",grid:0};
   GXValidFnc[14]={fld:"TABLEUSERROLE",grid:0};
   GXValidFnc[17]={fld:"HTML_USERTABLE_ADMINAG",grid:0};
   GXValidFnc[20]={fld:"HTML_USERTABLE_LANGUAGESICONS",grid:0};
   GXValidFnc[23]={fld:"",grid:0};
   GXValidFnc[24]={fld:"",grid:0};
   GXValidFnc[25]={fld:"BTNENGLISHLINK",grid:0};
   GXValidFnc[26]={fld:"",grid:0};
   GXValidFnc[27]={fld:"BTNPORTUGUESELINK",grid:0};
   GXValidFnc[28]={fld:"",grid:0};
   GXValidFnc[29]={fld:"BTNSPANISHLINK",grid:0};
   GXValidFnc[30]={fld:"",grid:0};
   GXValidFnc[31]={fld:"",grid:0};
   GXValidFnc[32]={fld:"SHOWMENU",grid:0,evt:"e11091_client"};
   GXValidFnc[33]={fld:"",grid:0};
   GXValidFnc[34]={fld:"",grid:0};
   GXValidFnc[35]={fld:"TABLECONTENT",grid:0};
   GXValidFnc[36]={fld:"",grid:0};
   GXValidFnc[37]={fld:"",grid:0};
   GXValidFnc[38]={fld:"TEXTBLOCKTITLE", format:0,grid:0};
   GXValidFnc[39]={fld:"",grid:0};
   GXValidFnc[40]={fld:"",grid:0};
   GXValidFnc[41]={fld:"TABLECONTENTPLACEHOLDER",grid:0};
   GXValidFnc[42]={fld:"",grid:0};
   GXValidFnc[43]={fld:"",grid:0};
   GXValidFnc[45]={fld:"",grid:0};
   GXValidFnc[46]={fld:"",grid:0};
   GXValidFnc[47]={fld:"MENU",grid:0};
   GXValidFnc[48]={fld:"",grid:0};
   GXValidFnc[49]={fld:"",grid:0};
   GXValidFnc[51]={fld:"",grid:0};
   GXValidFnc[52]={fld:"",grid:0};
   GXValidFnc[53]={fld:"HTML_USERTABLE_UTTOOLTIP",grid:0};
   GXValidFnc[54]={fld:"UTTOOLTIP",grid:0};
   GXValidFnc[58]={fld:"",grid:0};
   GXValidFnc[59]={fld:"",grid:0};
   GXValidFnc[60]={fld:"TABLEFOOTER",grid:0};
   GXValidFnc[61]={fld:"",grid:0};
   GXValidFnc[62]={fld:"",grid:0};
   GXValidFnc[63]={fld:"TEXTBLOCKFOOTER", format:0,grid:0};
   this.AV5AdminAGData = [ ] ;
   this.AV11LanguagesIconsData = [ ] ;
   
   
   this.Events = {"e12092_client": ["DDO_LANGUAGESICONS_MPAGE.ONOPTIONCLICKED_MPAGE", true] ,"e14092_client": ["DOENGLISHLINK_MPAGE", true] ,"e15092_client": ["DOSPANISHLINK_MPAGE", true] ,"e19092_client": ["ENTER_MPAGE", true] ,"e20092_client": ["CANCEL_MPAGE", true] ,"e11091_client": ["DOSHOWMENU_MPAGE", false] ,"e18091_client": ["DDO_ADMINAG_MPAGE.ONOPTIONCLICKED_MPAGE", false] ,"e17091_client": ["DOPORTUGUESELINK_MPAGE", false]};
   this.EvtParms["REFRESH_MPAGE"] = [[],[]];
   this.EvtParms["START_MPAGE"] = [[],[{av:'gx.fn.getCtrlProperty("LAYOUTMAINTABLE_MPAGE","Class")',ctrl:'LAYOUTMAINTABLE_MPAGE',prop:'Class'},{av:'AV9SidebarMenuOptionsData',fld:'vSIDEBARMENUOPTIONSDATA_MPAGE',pic:'',nv:null},{av:'this.DDO_ADMINAG_MPAGEContainer.Icon',ctrl:'DDO_ADMINAG_MPAGE',prop:'Icon'},{av:'AV5AdminAGData',fld:'vADMINAGDATA_MPAGE',pic:'',nv:null},{av:'AV11LanguagesIconsData',fld:'vLANGUAGESICONSDATA_MPAGE',pic:'',nv:null},{av:'this.DDO_ADMINAG_MPAGEContainer.TitleControlAlign',ctrl:'DDO_ADMINAG_MPAGE',prop:'TitleControlAlign'},{av:'this.DDO_LANGUAGESICONS_MPAGEContainer.TitleControlAlign',ctrl:'DDO_LANGUAGESICONS_MPAGE',prop:'TitleControlAlign'},{ctrl:'BTNENGLISHLINK_MPAGE',prop:'Class'},{ctrl:'BTNSPANISHLINK_MPAGE',prop:'Class'},{ctrl:'BTNPORTUGUESELINK_MPAGE',prop:'Class'},{av:'this.DDO_LANGUAGESICONS_MPAGEContainer.Caption',ctrl:'DDO_LANGUAGESICONS_MPAGE',prop:'Caption'}]];
   this.EvtParms["DOSHOWMENU_MPAGE"] = [[],[]];
   this.EvtParms["DDO_ADMINAG_MPAGE.ONOPTIONCLICKED_MPAGE"] = [[{av:'this.DDO_ADMINAG_MPAGEContainer.ActiveEventKey',ctrl:'DDO_ADMINAG_MPAGE',prop:'ActiveEventKey'}],[]];
   this.EvtParms["DDO_LANGUAGESICONS_MPAGE.ONOPTIONCLICKED_MPAGE"] = [[{av:'this.DDO_LANGUAGESICONS_MPAGEContainer.ActiveEventKey',ctrl:'DDO_LANGUAGESICONS_MPAGE',prop:'ActiveEventKey'}],[]];
   this.EvtParms["DOENGLISHLINK_MPAGE"] = [[],[]];
   this.EvtParms["DOPORTUGUESELINK_MPAGE"] = [[],[]];
   this.EvtParms["DOSPANISHLINK_MPAGE"] = [[],[]];
   this.Initialize( );
});
gx.createMasterPage(wwpbaseobjects.workwithplusmasterpage);

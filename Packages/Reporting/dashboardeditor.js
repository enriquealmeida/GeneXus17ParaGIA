/*!   GeneXus .NET Framework 17_0_10-161416 on 6/22/2022 21:45:33.92
*/
gx.evt.autoSkip=!1;gx.define("dashboardeditor",!1,function(){var t,n;this.ServerClass="dashboardeditor";this.PackageName="QueryViewer.Services";this.ServerFullClass="dashboardeditor.aspx";this.setObjectType("web");this.setOnAjaxSessionTimeout("Warn");this.hasEnterEvent=!1;this.skipOnEnter=!1;this.autoRefresh=!0;this.fullAjax=!0;this.supportAjaxEvents=!0;this.ajaxSecurityToken=!0;this.SetStandaloneVars=function(){};this.e13062_client=function(){return this.executeServerEvent("ENTER",!0,null,!1,!1)};this.e14062_client=function(){return this.executeServerEvent("CANCEL",!0,null,!1,!1)};this.GXValidFnc=[];t=this.GXValidFnc;this.GXCtrlIds=[2,3,4,5];this.GXLastCtrlId=5;this.DASHBOARDVIEWER1Container=gx.uc.getNew(this,6,0,"DashboardViewer","DASHBOARDVIEWER1Container","Dashboardviewer1","DASHBOARDVIEWER1");n=this.DASHBOARDVIEWER1Container;n.setProp("Enabled","Enabled",!0,"boolean");n.setProp("Width","Width","100vw","str");n.setProp("Height","Height","100vh","str");n.setProp("Object","Objectcall","","str");n.setProp("Class","Class","DashboardViewer","str");n.setDynProp("DashboardSpec","Dashboardspec","","char");n.setDynProp("ReturnSampleData","Returnsampledata",!1,"boolean");n.setDynProp("WidgetsExpanded","Widgetsexpanded",!0,"bool");n.setDynProp("CSSRules","Cssrules","","char");n.setDynProp("DefaultStyle","Defaultstyle","","char");n.setProp("TranslationType","Translationtype","RunTime","str");n.setProp("ItemClickData","Itemclickdata","","str");n.setProp("FiltersChangedData","Filterschangeddata","","str");n.setProp("ValuesHighlightedData","Valueshighlighteddata","","str");n.setProp("Visible","Visible",!0,"bool");n.setC2ShowFunction(function(n){n.show()});this.setUserControl(n);t[2]={id:2,fld:"",grid:0};t[3]={id:3,fld:"MAINTABLE",grid:0};t[4]={id:4,fld:"",grid:0};t[5]={id:5,fld:"",grid:0};this.Events={e13062_client:["ENTER",!0],e14062_client:["CANCEL",!0]};this.EvtParms.REFRESH=[[],[{av:"this.DASHBOARDVIEWER1Container.DashboardSpec",ctrl:"DASHBOARDVIEWER1",prop:"DashboardSpec"},{av:"this.DASHBOARDVIEWER1Container.ReturnSampleData",ctrl:"DASHBOARDVIEWER1",prop:"ReturnSampleData"},{av:"this.DASHBOARDVIEWER1Container.WidgetsExpanded",ctrl:"DASHBOARDVIEWER1",prop:"WidgetsExpanded"},{av:"this.DASHBOARDVIEWER1Container.CSSRules",ctrl:"DASHBOARDVIEWER1",prop:"CSSRules"},{av:"this.DASHBOARDVIEWER1Container.DefaultStyle",ctrl:"DASHBOARDVIEWER1",prop:"DefaultStyle"}]];this.EvtParms.ENTER=[[],[]];this.Initialize()});gx.wi(function(){gx.createParentObj(this.dashboardeditor)})
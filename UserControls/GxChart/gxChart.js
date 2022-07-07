function gxChart()
{
    this.Type="Bar";
    
    this.Title="";
    this.X_AxisTitle="";
    this.Y_AxisTitle="";
    
    this.Width = "400";
    this.Height = "400";
    
    this.DrawShadows = 1;
    this.DrawBorder = 1;
    this.ShowValues = 1;
    this.LegendPosition = "Left";
    
    this.BackgroundColor1;
    this.BackgroundColor2;
    this.GraphColor1;
    this.GraphColor2;
    this.Opacity = 255;
    this.Scale="auto";
    this.CustomParameters = "";
    this.ServiceName = "drawchart.aspx";
    
    this.Categories = "Categories=Values:Category 1,Category 2, Category 3";
    this.Series = "&Series1=Values:Serie 1:96,35,23&Series2=Values:Serie 2:50,60,75";
    
    this.ServiceUrl="http://www.gxchart.com/service/";
    this.m_data;
    this.EventType = "ClickOnValues";
    
    this.SetData = function(data)
    {
        this.Categories = "&Categories=Values:";
        this.Series = "";
        if (gx.lang.emptyObject(data))
            return;
        this.m_data = data;
        for(i=0;data.Categories[i]!=undefined;i++)
            this.Categories+=data.Categories[i]+",";

        this.Categories = this.Categories.substring(0,this.Categories.length-1);
        
        for(i=0;data.Series[i];i++)
        {
             this.Series+="&Series"+(i+1)+"=Values:"+data.Series[i].Name+":";
             for(x=0;data.Series[i].Values[x]!=undefined;x++)
                this.Series+=gx.text.replaceAll(data.Series[i].Values[x],",",".")+",";
                
             this.Series = this.Series.substring(0,this.Series.length-1);
        }
        
    }
    
    this.GetData = function()
    {
        return this.m_data;
    }

    this.show = function() {
        if (gx.lang.emptyObject(this.m_data))
            return;
        var buff = "";
        if (this.EventType == "ClickOnGraph") {
            buff += "<img style=\"cursor: pointer\" onclick=\"" + this.me() + ".chartClicked();\" ";

            buff += "src=\"";
        }
        else if (this.EventType != "ClickOnValues" && this.EventType != "ClickOnCategories") {
            buff += "<img src=\"";
        }

        buff += this.ServiceUrl + this.ServiceName + "?type=" + this.Type;
        buff += "&Title=" + this.Title;
        buff += "&Width=" + gx.text.replaceAll(this.Width, "px", "");
        buff += "&Height=" + gx.text.replaceAll(this.Height, "px", "");
        buff += "&domtitle=" + this.X_AxisTitle;
        buff += "&rantitle=" + this.Y_AxisTitle;
        buff += "&shadow=" + this.DrawShadows;
        buff += "&border=" + this.DrawBorder;
        if (this.ShowValues == "0" || this.ShowValues == "false")
            buff+="&values=0";
        else
            buff+="&values=1";
        buff += "&legend=" + this.LegendPosition;
        buff += this.Categories;
        buff += this.Series;
        buff += "&scale=" + this.Scale;
        buff += this.CustomParameters;
        if (this.EventType != "ClickOnValues" && this.EventType != "ClickOnCategories") {
            buff+="&bgc1="+this.BackgroundColor1.R+","+this.BackgroundColor1.G+","+this.BackgroundColor1.B;
            buff+="&bgc2="+this.BackgroundColor2.R+","+this.BackgroundColor2.G+","+this.BackgroundColor2.B;
            buff+="&gbgc1="+this.GraphColor1.R+","+this.GraphColor1.G+","+this.GraphColor1.B;
            buff+="&gbgc2="+this.GraphColor2.R+","+this.GraphColor2.G+","+this.GraphColor2.B;
            buff += "\" />";
            document.getElementById(this.ContainerName).innerHTML = buff.toString();
        }
        else {
            var mapReq = "&getcatmap";
            if (this.EventType == "ClickOnValues")
                mapReq = "getmap";
            var postInfo = this.getPostInfo(buff.toString() + mapReq + "=" + this.me() + "&end=t");
            gx.http.doCall(postInfo);
        }
    }
    
    this.getPostInfo= function( fullpath) {
		var url = fullpath;
		var postInfo = {};
		postInfo.url = url;
		postInfo.obj = this;
		postInfo.formNode = gx.dom.form();
		postInfo.method = 'GET';
		postInfo.encoding = 'UTF-8';
		postInfo.useCash = false;
		postInfo.sync = false;
		postInfo.handler = this.readResponse;
		return postInfo;
	}
    
   this.readResponse = function(data, response){
		var buff = "";
        buff+="<MAP name=\""+this.ContainerName+"map\">";
        buff+=response;
        buff+="</MAP>";
   		buff+="<img ";
	    buff+="USEMAP=\"#"+this.ContainerName+"map\" src=\""+this.ServiceUrl+this.ServiceName+"?type=" + this.Type;
        buff+="&Title="+this.Title;
        buff+="&Width="+gx.text.replaceAll(this.Width,"px","");
        buff+="&Height="+gx.text.replaceAll(this.Height,"px","");
        buff+="&domtitle="+ this.X_AxisTitle;
        buff+="&rantitle=" + this.Y_AxisTitle;
        buff+="&shadow=" + this.DrawShadows;
        buff+="&border=" + this.DrawBorder;
        if(this.EventType == 'ClickOnCategories')
            buff+="&categoriesAsLink=true";
        if (this.ShowValues == "0" || this.ShowValues == "false")
            buff+="&values=0";
        else
            buff+="&values=1";
        buff+="&legend=" + this.LegendPosition;
        buff+="&alpha=" + this.Opacity;
        buff+="&bgc1="+this.BackgroundColor1.R+","+this.BackgroundColor1.G+","+this.BackgroundColor1.B;
        buff+="&bgc2="+this.BackgroundColor2.R+","+this.BackgroundColor2.G+","+this.BackgroundColor2.B;
        buff+="&gbgc1="+this.GraphColor1.R+","+this.GraphColor1.G+","+this.GraphColor1.B;
        buff+="&gbgc2="+this.GraphColor2.R+","+this.GraphColor2.G+","+this.GraphColor2.B;
        buff+=this.Categories;
        buff+=this.Series;
        buff+="&scale="+this.Scale;
        buff+=this.CustomParameters;
        buff+="&nnnn";
        buff+="\" />";
        
		document.getElementById(this.ContainerName).innerHTML = buff.toString();
	}
	
	this.ClickedSerieName = "";
	this.ClickedCategoryName = "";
	
	this.valueClick = function(serieName, catName)
	{
		if(this.Click)
		{
			this.ClickedSerieName = serieName;
			this.ClickedCategoryName = catName;
			this.Click();
		}
	}
	
	this.chartClicked = function()
	{
		if(this.Click)
			this.Click();
	}
}
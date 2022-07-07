function MonalisaWrapper($)
{
	this.MonalisaTokenCollection;
	this.MonalisaFieldCollection;
	this.MonalisaResultCollection;
	this.MonalisaDomainCollection;
	this.Validate;
	this.MonalisaListCollection;
	this.Width;
	this.Height;
	this.ReadOnly;
	this.ModeText;

	// Databinding for property MonalisaTokenCollection
	this.SetMonalisaTokenCollection = function(data)
	{
		///UserCodeRegionStart:[SetMonalisaTokenCollection] (do not remove this comment.)
		//alert("SetMonalisaTokenCollection");
		//if(data.length > 0){
			this.MonalisaTokenCollection = new Array();
			for(var i=0; i<data.length;i++)
				this.MonalisaTokenCollection[this.MonalisaTokenCollection.length] = new Monalisa.Token(data[i].type, data[i].value);
		//}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MonalisaTokenCollection
	this.GetMonalisaTokenCollection = function()
	{
		///UserCodeRegionStart:[GetMonalisaTokenCollection] (do not remove this comment.)

		//alert("GetMonalisaTokenCollection");
		this.MonalisaTokenCollection = this.editor.GetToken();
		return this.MonalisaTokenCollection;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MonalisaFieldCollection
	this.SetMonalisaFieldCollection = function(data)
	{
		///UserCodeRegionStart:[SetMonalisaFieldCollection] (do not remove this comment.)

		//var Field = new Monalisa.Field("TIPO_DOCUMENTO", "Text", null);
		if(data.length > 0){
			this.MonalisaFieldCollection = data;
			this.FirstLoad = true;
		}else{
			this.FirstLoad = false;
		}	
		
		//alert("SetMonalisaFieldCollection");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MonalisaFieldCollection
	this.GetMonalisaFieldCollection = function()
	{
		///UserCodeRegionStart:[GetMonalisaFieldCollection] (do not remove this comment.)

		
		//alert("GetMonalisaFieldCollection");
		//return this.MonalisaFieldCollection;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MonalisaResultCollection
	this.SetMonalisaResultCollection = function(data)
	{
		///UserCodeRegionStart:[SetMonalisaResultCollection] (do not remove this comment.)

		//alert("SetMonalisaResultCollection");
		if(data.length > 0)
			this.MonalisaResultCollection = data;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MonalisaResultCollection
	this.GetMonalisaResultCollection = function()
	{
		///UserCodeRegionStart:[GetMonalisaResultCollection] (do not remove this comment.)


		//alert("GetMonalisaResultCollection");
		//return this.MonalisaResultCollection;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MonalisaDomainCollection
	this.SetMonalisaDomainCollection = function(data)
	{
		///UserCodeRegionStart:[SetMonalisaDomainCollection] (do not remove this comment.)

				
		//alert("SetMonalisaDomainCollection");
		if(data.length > 0)
			this.MonalisaDomainCollection = data;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MonalisaDomainCollection
	this.GetMonalisaDomainCollection = function()
	{
		///UserCodeRegionStart:[GetMonalisaDomainCollection] (do not remove this comment.)

		
		
		//alert("GetMonalisaDomainCollection");
		
		//return this.MonalisaDomainCollection;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Validate
	this.SetValidate = function(data)
	{
		///UserCodeRegionStart:[SetValidate] (do not remove this comment.)

		//alert("SetValidate");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Validate
	this.GetValidate = function()
	{
		///UserCodeRegionStart:[GetValidate] (do not remove this comment.)
		
		//alert("GetValidate");
		return this.editor.Validate();

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MonalisaListCollection
	this.SetMonalisaListCollection = function(data)
	{
		///UserCodeRegionStart:[SetMonalisaListCollection] (do not remove this comment.)
		this.MonalisaListCollection = data;

		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property MonalisaListCollection
	this.GetMonalisaListCollection = function()
	{
		///UserCodeRegionStart:[GetMonalisaListCollection] (do not remove this comment.)
		//return this.MonalisaListCollection;

		
		
		
		
		
		
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	
	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)


	    //var buffer= '<a id="myAnchor" href="#">Hello Monalisa!!!</a>';   
	    //this.setHtml(buffer);   
	    //document.getElementById("myAnchor").onclick = this.MonalisaClicked;  


		
	    if (!this.IsPostBack) {
	        this.getContainerControl().style.height = "100%";
	        this.getContainerControl().style.width = "100%";

	        this.editor = new Monalisa.Editor();
	        this.editor.ReadOnly = gx.lang.gxBoolean(this.ReadOnly);
			this.editor.ModeText = gx.lang.gxBoolean(this.ModeText);
	    }
		if (typeof(this.ModeText) == "string"){
			this.editor.ModeText = gx.lang.gxBoolean(this.ModeText)
		}
		if (this.FirstLoad) {
	        var Domains = new Array();
	        if (this.MonalisaDomainCollection) {
	            for (var i = 0; i < this.MonalisaDomainCollection.length; i++) {

	                var newDomain = new Monalisa.Domain(this.MonalisaDomainCollection[i].Name);

	                for (var j = 0; j < this.MonalisaDomainCollection[i].Items.length; j++) {
	                    newDomain.AddItem(this.MonalisaDomainCollection[i].Items[j].Id, this.MonalisaDomainCollection[i].Items[j].Name);
	                }
	                Domains[Domains.length] = newDomain;
	            }
	        }

	        this.editor.Fields = new Array();
	        if (this.MonalisaFieldCollection) {
	            for (var i = 0; i < this.MonalisaFieldCollection.length; i++) {

	                var FieldDomain = this.GetDomainObj(Domains, this.MonalisaFieldCollection[i].DomainId);
	                var Field = new Monalisa.Field(this.MonalisaFieldCollection[i].Name, this.MonalisaFieldCollection[i].Type, FieldDomain);
					Field.suggestId = this.MonalisaFieldCollection[i].SuggestId;
	                Field.length = this.MonalisaFieldCollection[i].Length;
					Field.pattern = this.MonalisaFieldCollection[i].Pattern;
					Field.patternMessage = this.MonalisaFieldCollection[i].PatternMessage;
	                this.editor.AddField(Field);
	            }
	        }

	        this.editor.Lists = new Array();
	        if (this.MonalisaListCollection) {
	            for (var i = 0; i < this.MonalisaListCollection.length; i++) {

	                var List = new Monalisa.List(this.MonalisaListCollection[i].Name, this.MonalisaListCollection[i].Type);
	                this.editor.AddList(List);
	            }
	        }
	        this.editor.Results = new Array();
	        if (this.MonalisaResultCollection) {
	            for (var i = 0; i < this.MonalisaResultCollection.length; i++) {

	                var Result = new Monalisa.Result(this.MonalisaResultCollection[i].Name);
	                var ResultDomain = this.GetDomainObj(Domains, this.MonalisaResultCollection[i].DomainId);
	                if (ResultDomain) {
	                    var arg = new Monalisa.Arg("mail", "");
	                    arg.domain = ResultDomain;
	                    Result.AddArg(arg);
	                }
	                var arg = new Monalisa.Arg("percent", "100");
	                Result.AddArg(arg);
	                this.editor.AddResult(Result);
	            }
	        }
	    }

	    //Agrega el control
	    if (!this.getContainerControl().childNodes[0])
	        this.getContainerControl().appendChild(this.editor.CreateHTML());

	    if (this.MonalisaTokenCollection) {
	        if (this.MonalisaTokenCollection.length == 0) {
	            this.editor.Clear(this.MonalisaTokenCollection);
	        } else {
	            if (this.MonalisaTokenCollection[0].type != 'error') {
	                this.editor.Clear(this.MonalisaTokenCollection);
	                this.editor.Parse(this.MonalisaTokenCollection);
	            }
	        }
	    }
		if (this.editor.ModeText){
			this.editor.showText();
		}else{
			this.editor.showBlock();
		}
	    






















































	    
		
		
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)


	this.GetDomainObj = function(vDomains, vDomainId)
	{
		var domain = null;
		for(var i=0; i<vDomains.length; i++){
			if(vDomains[i].name == vDomainId){
				domain = vDomains[i]
				break;
			}
		}
		return domain;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

function GoogleTable()
{
	this.Height;
	this.Width;
	this.Data;
	this.Page;
	this.PageSize;
	this.ShowRowNumber;
	this.Sort;
	this.SortAscending;
	this.SortColumn;
	this.Selection;
	this.CurrentPage;
	this.Columns;
	this.BackColorStyle;
	
	this.m_columnModel = {};

	// Databinding for property Data
	this.SetData = function(data)
	{
		///UserCodeRegionStart:[SetData] (do not remove this comment.)
		this.Data = data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Data
	this.GetData = function()
	{
		///UserCodeRegionStart:[GetData] (do not remove this comment.)
		return this.Data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Columns
	this.SetColumns = function(data)
	{
		///UserCodeRegionStart:[SetColumns] (do not remove this comment.)
		this.Columns = data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Columns
	this.GetColumns = function()
	{
		///UserCodeRegionStart:[GetColumns] (do not remove this comment.)
		return this.Columns;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Selection
	this.SetSelection = function(data)
	{
		///UserCodeRegionStart:[SetSelection] (do not remove this comment.)
		this.Selection = data;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Selection
	this.GetSelection = function()
	{
		///UserCodeRegionStart:[GetSelection] (do not remove this comment.)
		var sel = this.m_table.getSelection();
		this.Selection = [];
		for (var i=0; i < sel.length; i++){
			this.Selection[i] = this.Data[sel[i].row];
		}
		return this.Selection;
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)
		if (!this.m_initialized){
			this.m_initialized = true;
			this.CurrentPage = 1;
			google.load("visualization", "current", {
				packages: ["table"], 
				callback: this.createDelegate(this, this.drawTable)
			});
		}
		else{
			// This is a work-around for the problem of boolean and numerics as string when they return from the server
			this.CurrentPage = this.StringToNumeric(this.CurrentPage); // WA
			this.SortColumn = this.StringToNumeric(this.SortColumn); // WA
			this.PageSize = this.StringToNumeric(this.PageSize); // WA
			this.ShowRowNumber = this.StringToBoolean(this.ShowRowNumber); // WA
			this.SortAscending = this.StringToBoolean(this.SortAscending); // WA
			// If tha paging or sorting are configured to fire events, redraw the table and keep selection
			if (this.Page == "event" || this.Sort == "event"){
				this.drawTable();
				this.m_table.setSelection(this.m_lastSelection);
			}
		}
		///UserCodeRegionEnd: (do not remove this comment.)
	};
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	
	this.drawTable = function() {
		var data = new google.visualization.DataTable();
		this.defineColumns(data);
		this.addRows(data);
		this.defineFormatters(data);

		var table = this.m_table = new google.visualization.Table(this.getContainerControl());
		table.draw(data, {
			width: this.Width,
			height: this.Height,
			alternatingRowStyle: (this.BackColorStyle == "report"),
			firstRowNumber: (this.CurrentPage - 1)*this.PageSize + 1,
			allowHtml: true,
			showRowNumber: this.ShowRowNumber,
			page: this.Page.toLowerCase(),
			pageSize: this.PageSize,
			sort: this.Sort.toLowerCase(),
			sortAscending: this.SortAscending,
			sortColumn: this.SortColumn
		});
		
		// Attach paging and sort events if the user wants to be notified
		if  (this.Page == "event")
			google.visualization.events.addListener(table, 'page', this.createDelegate(this, this.pagingHandler, table, true));
		if  (this.Sort == "event")
			google.visualization.events.addListener(table, 'sort', this.createDelegate(this, this.sortHandler, table, true));
		google.visualization.events.addListener(table, 'select', this.createDelegate(this, this.selectHandler, table, true));
	};
	
	// Get the columns from the SDT with "reflection"
	this.defineColumns = function(data){
		this.m_columns = (this.Data.length && this.Data.length > 0) ? this.getDataColumns(this.Data[0]) : []
		this.m_columnModel = this.getColumnModel();
		
		for (var i=0;i < this.m_columns.length;i++){
			var col = this.m_columns[i];
			var description = this.formatColumnName(col.property); // The default column name is the SDT field name, separeted with spaces
			if (this.m_columnModel[col.property] && this.m_columnModel[col.property].description)
				description = this.m_columnModel[col.property].description;
			data.addColumn(col.type, description);
		}
	};
	
	// Add data rows to the DataTable
	this.addRows = function(data){
		if (this.Data.length > 0){
			data.addRows(this.Data.length);
			for (var i=0;i<this.Data.length;i++){
				for (var j=0;j<this.m_columns.length;j++){
					data.setCell(i, j, this.Data[i][this.m_columns[j].property]);
				}
			}
		}
	};
	
	// Define columns formatters (if any was specified) 
	this.defineFormatters = function(data){
		for (var i=0;i < this.m_columns.length;i++){
			var col = this.m_columns[i];
			var formatter = null;
			if (this.m_columnModel[col.property]){
				var col = this.m_columnModel[col.property];
				this.applyFormatter(col.formatter, this.getFormatterOptions(col.formatterOptions), data, i);
			}
		}
	};

	// Paging event handler
	this.pagingHandler = function(e, table){
		this.CurrentPage += e.page;
		this.CurrentPage = this.CurrentPage || 1;
		this.fireEvent(this.PageChanged, table);
	};

	// Sorting event handler
	this.sortHandler = function(e, table){
		this.SortColumn = e.column;
		this.SortAscending = e.ascending;
		this.fireEvent(this.SortChanged, table);
	};
	
	// Selection event handler
	this.selectHandler = function(e, table){
		this.m_lastSelection = table.getSelection();
		this.fireEvent(this.Select, table);
	};
	
	this.fireEvent = function(evt, table){
		if (evt)
			this.createDelegate(this, evt)();
	};

	// Given an objects, it returns its members
	this.getDataColumns = function(obj){
		var props = [];
		if (typeof(obj) == "object"){
			for (var s in obj)
			{
				if (typeof(obj[s]) != "function") {
					props[props.length] = {property: s, type: typeof(obj[s])};
				}
			}
		}
		else
			props[0] = {property: "Data", type: typeof(obj)}

		return props;
	};
	
	// If  the user specified a column model, returns a map of it, with the property name as key. The value is an object with the description, formatter to be used and the formatter options.
	this.getColumnModel = function(){
		var cm = {};
		if (this.Columns && this.Columns.length){
			for (var i=0; i < this.Columns.length; i++){
				var col = this.Columns[i];
				cm[col.Column] = {
					description: (col.Description && col.Description != "") ? col.Description : undefined
				};
				if (col.Formatter) {
					cm[col.Column] = Object.assign({
						formatter: col.Formatter.Type,
						formatterOptions: col.Formatter.Options
					}, cm[col.Column]);
				}
			}
		}
		
		return cm;
	};
	
	// Apply the formatter that maps to "type", with the specified "options", to the column "colIdx" of the DataTable "data"
	this.applyFormatter = function(type, opts, data, colIdx){
		var formatter;
		if (type == "arrow"){
			formatter = new google.visualization.TableArrowFormat(opts);
			formatter.format(data, colIdx);
		}
		if (type == "bar"){
			formatter = new google.visualization.TableBarFormat(opts);
			formatter.format(data, colIdx);
		}
		if (type == "color"){ // Not supported
			formatter = new google.visualization.TableColorFormat(opts);
			formatter.format(data, colIdx);
		}
		if (type == "number"){
			formatter = new google.visualization.TableNumberFormat(opts);
			formatter.format(data, colIdx);
		}
		if (type == "pattern"){
			formatter = new google.visualization.TablePatternFormat(opts.pattern);
			var columnsArray = opts.columnsArray ? opts.columnsArray.split(",") : undefined;
			for (var i=0; columnsArray && i<columnsArray.length ;i++)
				columnsArray[i] = this.StringToNumeric(columnsArray[i]);
			formatter.format(data, columnsArray, colIdx);
		}
	};

	// Converts a formatter options name-value array to an object
	this.getFormatterOptions = function(optList){
		var options = {};
		if (optList){
			for (var j=0; j < optList.length; j++){
				var val = optList[j].Value;
				if (val ===	 "true" || val === "false")
					options[optList[j].Name] = this.StringToBoolean(val);
				else
					options[optList[j].Name] = val;
			}
		}
		return options;
	};
	
	// Converts a string in Upper Camel Case format to a space separated string.
	this.formatColumnName = function(col){
		return col.replace(/([a-z](?=[A-Z]))/g,"$1 ")
	};
	
    this.createDelegate = function(obj, method, args, appendArgs){
        return function() {
            var callArgs = args || arguments;
            if(appendArgs === true){
                callArgs = Array.prototype.slice.call(arguments, 0);
                callArgs = callArgs.concat(args);
            }else if(typeof appendArgs == "number"){
                callArgs = Array.prototype.slice.call(arguments, 0);
                var applyArgs = [appendArgs, 0].concat(args);
                Array.prototype.splice.apply(callArgs, applyArgs);
            }
            return method.apply(obj || window, callArgs);
        };
    };
	
	this.StringToBoolean = function(str){
		if(str){
			if(typeof(str) == 'string')
				return (str.toLowerCase() == "true")
			return str;
		}
		else
			return false;
	};
	
	this.StringToNumeric = function(str){
		if(str){
			if(typeof(str) == 'string')
				return parseInt(str);
			else
				return str;
		}
		return 0;
	};
	///UserCodeRegionEnd: (do not remove this comment.):
}

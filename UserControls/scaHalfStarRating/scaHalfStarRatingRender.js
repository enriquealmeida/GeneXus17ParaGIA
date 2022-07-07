function HalfStarRating()
{
	this.Stars;
	this.MouseOverValue;
	this.HalfStar;
	this.TooltipText;
	this.Attribute;
	this.Enabled;
	this.Name;
	this.ClearButton;
	this.NotStarted=true;

	this.SetAttribute = function(data)
	{
		this.Attribute = data;
	}

	this.GetAttribute = function()
	{
		return this.Attribute;
	}

	
	
	this.show = function()
	{
		if (this.NotStarted)
		{
		this.Name = "_" + this.ContainerName
		if (this.Enabled)
		{
		var buffer = '<div id="' + this.Name + '" class="rating"></div> ';		
		}
		else
		{
		var buffer = '<div id="' + this.Name + '" class="ratingDisable"></div> ';		
		}
		this.setHtml(buffer);
		InitScaHalfStarRating(this);
		this.NotStarted = false;
		}
	}
}

function InitScaHalfStarRating(render) {
	$(document).ready(function(){
	$("#" + render.Name).rating(render);
	});
}
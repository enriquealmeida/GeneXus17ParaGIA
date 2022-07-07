function Sparkline()
{
	this.Values;
	this.DefaultPixelsPerValue;
	this.SpotColor;
	this.SpotRadius;
	this.LineWidth;
	this.NormalRangeMin;
	this.NormalRangeMax;
	this.NormalRangeColor;
	this.LineRangeMin;
	this.LineRangeMax;
	this.LineRangeMinX;
	this.LineRangeMaxX;
	this.BarColor;
	this.NegBarColor;
	this.ZeroColor;
	this.BarWidth;
	this.BarSpacing;
	this.BarRangeMax;
	this.BarRangeMin;
	this.TristatePosBarColor;
	this.TristateNegBarColor;
	this.TristateZeroBarColor;
	this.TristateBarWidth;
	this.TristateBarSpacing;
	this.LineHeight;
	this.ThresholdValue;
	this.ThresholdColor;
	this.DiscreteRangeMax;
	this.DiscreteRangeMin;
	this.TargetColor;
	this.TargetWidth;
	this.PerformanceColor;
	this.Offset;
	this.ShowOutliers;
	this.OutlierIQR;
	this.BoxLineColor;
	this.BoxFillColor;
	this.WhiskerColor;
	this.OutlierLineColor;
	this.OutlierFillColor;
	this.MedianColor;
	this.Target;
	this.BoxTargetColor;
	this.BoxSpotRadius;
	this.BoxRangeMax;
	this.BoxRangeMin;
	this.Width;
	this.Height;
	this.Type;
	this.LineColor;
	this.FillColor;

	// Databinding for property Values
	this.SetValues = function(data)
	{
		///UserCodeRegionStart:[SetValues] (do not remove this comment.)
		this.Values = data;
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}

	// Databinding for property Values
	this.GetValues = function()
	{
		///UserCodeRegionStart:[GetValues] (do not remove this comment.)
		return this.Values;

		///UserCodeRegionEnd: (do not remove this comment.)
	}

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)

		// Genexus User Control based on JQuery Plugin http://omnipotent.net/jquery.sparkline/
		// TODO: define an event as http://trends.builtwith.com/
		
		try
		{
			var myValues = "";
			var w = ((this.Width == 65) ? "auto" : this.Width+"px");
			var h = ((this.Height == 1) ? "auto" : this.Height);
			var myLineColor = ((this.LineColor.Html != "#000000") ? this.LineColor.Html : "#00f" );
			var myFillColor = ((this.FillColor.Html != "#000000") ? this.FillColor.Html : "#cdf" );
			var parameterList = { width:w, height:h, lineColor:myLineColor, fillColor:myFillColor}; // Common properties
			
			switch(this.Type)
			{
				case "Line":
					parameterList.type = "line";
					parameterList.defaultPixelsPerValue = ((this.DefaultPixelsPerValue != 3) ? this.DefaultPixelsPerValue : 3);
					parameterList.spotColor = ((this.SpotColor.Html != "#000000") ? this.SpotColor.Html : "#f80");
					parameterList.spotRadius = ((this.SpotRadius != 0) ? this.SpotRadius : 1.5);
					parameterList.lineWidth = ((this.LineWidth != 1) ? this.LineWidth : 1);					
					parameterList.normalRangeMin = ((this.NormalRangeMin != 0) ? this.NormalRangeMin : undefined);
					parameterList.normalRangeMax = ((this.NormalRangeMax != 0) ? this.NormalRangeMax : undefined);
					parameterList.normalRangeColor = ((this.NormalRangeColor.Html != "#000000") ? this.NormalRangeColor.Html : "#ccc");
					parameterList.chartRangeMin = ((this.LineRangeMin != 0) ? this.LineRangeMin : undefined);
					parameterList.chartRangeMax = ((this.LineRangeMax != 0) ? this.LineRangeMax : undefined);
					parameterList.chartRangeMinX = ((this.LineRangeMinX != 0) ? this.LineRangeMinX : undefined);
					parameterList.chartRangeMaxX = ((this.LineRangeMaxX != 0) ? this.LineRangeMaxX : undefined);					
					break;
				case "Bar":
					parameterList.type = "bar";
					parameterList.barColor = ((this.BarColor.Html != "#000000") ? this.BarColor.Html : "#00f");
					parameterList.negBarColor = ((this.NegBarColor.Html != "#000000") ? this.NegBarColor.Html : "#f44");
					parameterList.zeroColor = ((this.ZeroColor.Html != "#000000") ? this.ZeroColor.Html : undefined);
					parameterList.barWidth = ((this.BarWidth != 0) ? this.BarWidth : 4);
					parameterList.barSpacing = ((this.BarSpacing != 0) ? this.BarSpacing : 1);
					parameterList.chartRangeMin = ((this.BarRangeMin != 0) ? this.BarRangeMin : undefined);
					parameterList.chartRangeMax = ((this.BarRangeMax != 0) ? this.BarRangeMax : undefined);
					break;
				case "Tristate":
					parameterList.type = "tristate";
					parameterList.posBarColor = ((this.TristatePosBarColor.Html != "#000000") ? this.TristatePosBarColor.Html : "#6f6");
					parameterList.negBarColor = ((this.TristateNegBarColor.Html != "#000000") ? this.TristateNegBarColor.Html : "#f44");
					parameterList.zeroBarColor = ((this.TristateZeroBarColor.Html != "#000000") ? this.TristateZeroBarColor.Html : "#999");
					parameterList.barWidth = ((this.TristateBarWidth != 0) ? this.TristateBarWidth : 4);
					parameterList.barSpacing = ((this.TristateBarSpacing != 0) ? this.TristateBarSpacing : 1);
					break;
				case "Discrete":
					parameterList.type = "discrete";
					parameterList.lineHeight = ((this.LineHeight != 0) ? this.LineHeight : "auto");
					parameterList.thresholdValue = ((this.ThresholdValue != 0) ? this.ThresholdValue : 0);
					parameterList.thresholdColor = ((this.ThresholdColor.Html != "#000000") ? this.ThresholdColor.Html : undefined);
					parameterList.chartRangeMin = ((this.DiscreteRangeMin != 0) ? this.DiscreteRangeMin : undefined);
					parameterList.chartRangeMax = ((this.DiscreteRangeMax != 0) ? this.DiscreteRangeMax : undefined);					
					break;
				case "Bullet":
					parameterList.type = "bullet";
					parameterList.targetColor = ((this.TargetColor.Html != "#000000") ? this.TargetColor.Html : "red");
					parameterList.targetWidth = ((this.TargetWidth != 0) ? this.TargetWidth : 3);
					parameterList.performanceColor = ((this.PerformanceColor.Html != "#000000") ? this.PerformanceColor.Html : "blue");
					break;
				case "Pie":
					parameterList.type = "pie";
					parameterList.offset = this.Offset;
					break;
				case "BoxPlot":
					parameterList.type = "box";
					parameterList.showOutliers = this.ShowOutliers;
					parameterList.outlierIQR = ((this.OutlierIQR != 0) ? this.OutlierIQR : 1.5);
					parameterList.boxLineColor = ((this.BoxLineColor.Html != "#000000") ? this.BoxLineColor.Html : "black");
					parameterList.boxFillColor = ((this.BoxFillColor.Html != "#000000") ? this.BoxFillColor.Html : "#cdf");
					parameterList.whiskerColor = ((this.WhiskerColor.Html != "#000000") ? this.WhiskerColor.Html : "black");
					parameterList.outlierLineColor = ((this.OutlierLineColor.Html != "#000000") ? this.OutlierLineColor.Html : "#333");
					parameterList.outlierFillColor = ((this.OutlierFillColor.Html != "#000000") ? this.OutlierFillColor.Html : "white");
					parameterList.medianColor = ((this.MedianColor.Html != "#000000") ? this.MedianColor.Html : "red");
					parameterList.target = ((this.Target != 0) ? this.Target : undefined);
					parameterList.targetColor = ((this.BoxTargetColor.Html != "#000000") ? this.BoxTargetColor.Html : "#4a2");
					parameterList.spotRadius = ((this.BoxSpotRadius != 0) ? this.BoxSpotRadius : 1.5); // Agregar Otra
					parameterList.chartRangeMin = ((this.BoxRangeMin != 0) ? this.BoxRangeMin : undefined);
					parameterList.chartRangeMax = ((this.BoxRangeMax != 0) ? this.BoxRangeMax : undefined);					
					break;
				default:
					parameterList.type = "line";
					break;
			}
			
			// get container
			var myCtrl = this.getContainerControl();
			if (this.Values.Value!=undefined)
			{
				// Parameter list is in the Value element from the SparklineList SDT binded to the "this.Values" properties.
				myValues = this.Values.Value;
			}
			else
			{
				// Use an empty object, no data detected
				myValues = this.Values;
			}
			//Set values
			if (myCtrl)
			{
				$(myCtrl).sparkline(myValues, parameterList );
			}
			else
			{
				gx.dbg.enabled = true;
				gx.dbg.logMsg('Could not render sparkline');
			}
		}
		catch(e)
		{
			gx.dbg.logEx(e, 'SparklineRender.js', 'show');
		}
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)
	
	///UserCodeRegionEnd: (do not remove this comment.):
}

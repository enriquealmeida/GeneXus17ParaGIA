package com.genexus.android.core.usercontrols.sparkline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import com.genexus.android.core.base.utils.Strings;

public class SparkLineView extends View {
	private static final Float MAX_TEXT_FRAC         = 0.5f;     // maximum fraction of view to give to the textual part
	private static final Float MARKER_MIN_SIZE       = 4.0f;     // maximum size of the anchor marker we'll use (in points)
	private static final Float DEF_MARKER_SIZE_FRAC  = 0.2f;     // default fraction of the view height we'll use for the anchor marker
	private static final Float MARKER_MAX_SIZE       = 8.0f;     // maximum size of the anchor marker we'll use (in points)
	private static final Float GRAPH_X_BORDER        = 2.0f;     // horizontal border width for the graph line (in points)
	private static final Float GRAPH_Y_BORDER        = 2.0f;     // vertical border width for the graph line (in points)
	private static final Float CONSTANT_GRAPH_BUFFER = 0.1f;     // fraction to move the graph limits when min = max

	private SparkLineData mDataValues;
	private SparkLineOptions mOptions;

	public SparkLineView(Context context) {
		super(context);
	}

	/* Settings */
	public SparkLineOptions getOptions() {
		return mOptions;
	}

	public void setOptions(SparkLineOptions options) {
		mOptions = options;
	}

	private SparkLineData getDataValues() {
		return mDataValues;
	}

	public void setDataValues(SparkLineData dataValues) {
		mDataValues = dataValues;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		doDraw(canvas);
	}

	// returns the Y plot value, given the limitations we have
	private static float yPlotValue(float maxHeight, float yInc, float val, float offset, float penWidth) {
	    return maxHeight - ((yInc * (val - offset)) + GRAPH_Y_BORDER + (penWidth / 2.0f));
	}

	private void doDraw(Canvas canvas) {
		// Don't draw anything if we haven't got the data values yet.
		if (getDataValues() == null || getDataValues().size() == 0) {
			return;
		}

		// Text Label Drawing
	    // see how much text we have to show
		String graphText = getOptions().getLabelText();
		if (getOptions().isShowCurrentValue()) {
			graphText = graphText + Strings.SPACE + String.valueOf(getDataValues().getCurrentValue());
		}

	    // calculate the width the text would take with the specified font
		Paint pLegends = new Paint() {
			{ setStyle(Paint.Style.STROKE);  }
		};
		pLegends.setColor(Color.BLACK);
		pLegends.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);

		float textWidth = pLegends.measureText(graphText);
		float textHeight = pLegends.getTextSize();

		float textStartX = (getWidth() * 0.975f) - textWidth;
		float textStartY = (getHeight() / 2) - (textHeight / 2);
		// draw the label
		canvas.drawText(getOptions().getLabelText(), textStartX, textStartY, pLegends);

		Paint p = new Paint() { { setStyle(Paint.Style.STROKE);
		setAntiAlias(true);
		setColor(Color.WHITE);
		}};

		Paint pen = new Paint() { { setStyle(Paint.Style.STROKE);
		setAntiAlias(true);
		setColor(Color.BLUE);
		}};

	    // conditionally draw the current value in the chosen colour
	    if (getOptions().isShowCurrentValue()) {
	        p.setColor(getOptions().getCurrentValueColor());

	        canvas.drawText(Strings.SPACE + String.valueOf(getDataValues().getCurrentValue()), textStartX + p.measureText(getOptions().getLabelText()), textStartY, pen);
	    }
	    // ---------------------------------------------------
	    // Graph Drawing
	    // ---------------------------------------------------

	    // calculate the view fraction that will be the graph
	    Float graphSize = (getWidth() * 0.95f) - textWidth;
	    Float graphFrac = graphSize / getWidth();

	    // calculate the graph area and X & Y widths and scales
	    float dataMin = getDataValues().getMinimum();
	    float dataMax = getDataValues().getMaximum();

	    int fullWidth = getWidth();
	    int fullHeight = getHeight();
	    Float sparkWidth  = (fullWidth  - (2 * GRAPH_X_BORDER)) * graphFrac;
	    Float sparkHeight = fullHeight - (2 * GRAPH_Y_BORDER);

	    // defaults: upper and lower graph bounds are data maximum and minimum, respectively
	    float graphMax = dataMax;
	    float graphMin = dataMin;

	    // special case if min = max, push the limits 10% further
	    if (graphMin == graphMax) {
	        graphMin *= 1.0f - CONSTANT_GRAPH_BUFFER;
	        graphMax *= 1.0f + CONSTANT_GRAPH_BUFFER;
	    }

	    // X scale is set to show all values
	    float xinc = sparkWidth / (getDataValues().size() - 1);

	    // Y scale is auto-zoomed to specified limits (allowing for pen width)
	    float yInc = (sparkHeight - getOptions().getPenWidth()) / (graphMax - graphMin);

	    // ensure the pen is a suitable width for the device we are on (i.e. we use *pixels* and not points)
	    p.setStrokeWidth(getOptions().getPenWidth());

	    // Customization to allow pen color changes
	    pen.setColor(getOptions().getPenColor());

	    Path path = new Path();
	    for (int idx = 0; idx < getDataValues().size(); idx++) {
	    	float xpos = (xinc * idx) + GRAPH_X_BORDER;

	    	float value = getDataValues().get(idx);
			float ypos = yPlotValue(fullHeight, yInc, value, graphMin, pen.getStrokeWidth());

	    	if (idx == 0)
	    		path.moveTo(xpos, ypos);
	    	else
	    		path.lineTo(xpos, ypos);
	    }
	    canvas.drawPath(path, p);
	    path.close();

	    // draw the value marker circle, if requested
	    if (getOptions().isShowCurrentValue()) {
	        float markX = (xinc * (getDataValues().size() - 1 )) + GRAPH_X_BORDER;
	        float markY = yPlotValue(fullHeight, yInc, getDataValues().getCurrentValue(), graphMin, getOptions().getPenWidth());

	        // calculate the accent marker size, with limits
	        float markSize = fullHeight * DEF_MARKER_SIZE_FRAC;
	        if (markSize < MARKER_MIN_SIZE)
	            markSize = MARKER_MIN_SIZE;
	        else if (markSize > MARKER_MAX_SIZE)
	            markSize = MARKER_MAX_SIZE;


	        RectF markRect =  new RectF(markX - (markSize/2.0f), markY - (markSize/2.0f), markX + (markSize/2.0f), markY + (markSize/2.0f));
	        pen.setColor(getOptions().getCurrentValueColor());
	        pen.setStyle(Style.FILL);
	        canvas.drawOval(markRect, pen);
	    }

	}

}

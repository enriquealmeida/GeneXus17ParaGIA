package com.genexus.android.core.usercontrols.gauge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.ThemeUtils;

public class ChartSurface extends View
{
	private GaugeSpecification mSpec;
	private int mWidth;
	private int mMargin = Services.Device.dipsToPixels(5);  
	private int mMarkSize = Services.Device.dipsToPixels(3);  
	
	protected ThemeClassDefinition mThemeClass;

	public ChartSurface(Context context) {
		super(context);
		init();
	}

	public ChartSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setSpec(GaugeSpecification spec) {
		mSpec = spec;
	}

	private void init() {
		setSpec(null);
	}

	private float mPercentage = 1f;
	protected void setPercentage(float p) {
		mPercentage = p;
	}

	@Override
	public void onDraw(Canvas canvas) {
		doDraw(canvas);
	}

	private void doDraw(Canvas canvas) {
		if (mSpec == null)
			return;

		Integer defaultTextColor = ThemeUtils.getAndroidThemeColorId(this.getContext(), android.R.attr.textColorPrimary);
		// if null, should not, use a default color visible on light and dark background.
		if (defaultTextColor == null)
			defaultTextColor = Color.argb(255, 192, 92, 0);
		
		mWidth = getWidth() - mMargin;
		// use Height definition as in dips
		int height = Services.Device.dipsToPixels(mSpec.Height);
		if (height == 0)
			height = getHeight() / 2;
		if (mWidth == 0)
			return;

		ThemeClassDefinition themeClass = mThemeClass; // get the att/var class

		//Keep just for compatibility, could be removed...
		if (themeClass == null)
			themeClass = Services.Themes.getThemeClass("Attribute.SDLinearGaugeText");

		if (mSpec.Type != null && mSpec.Type.equalsIgnoreCase("Circular")) {
			doDrawCircular(canvas, defaultTextColor, themeClass);
		}
		else {
			doDrawLinear(canvas, defaultTextColor, themeClass, height);
		}
	}

	private void doDrawLinear(Canvas canvas, Integer defaultTextColor, ThemeClassDefinition themeClass, int height) {
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(defaultTextColor);

		if (themeClass != null)
        {
            //Text Size
            Integer textSizef = themeClass.getFont().getFontSize();
            if (textSizef != null && textSizef > 0)
                p.setTextSize(textSizef);
            else
                p.setTextSize(Services.Device.dipsToPixels(12));
            // Text Color
            Integer colorId = ThemeUtils.getColorId(themeClass.getColor());
            //Integer highlightedColorId = ThemeUtils.getColorId(themeClass.getHighlightedColor());
            if (colorId != null /*|| highlightedColorId != null*/)
            {
                p.setColor(colorId);
            }
        }
        else
        {
            p.setTextSize(Services.Device.dipsToPixels(12));
        }

		float fontHeight = p.getTextSize();

		float showMinMaxYSize = p.getTextSize() + mMargin;
		// Min/ Max Value
		if (mSpec.ShowMinMax)
        {
            Paint pLegends = new Paint();
            pLegends.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
            // Draw Text of Min
            canvas.drawText(String.valueOf(mSpec.MinValue), 0, showMinMaxYSize, p);
            // Draw Maximum
            String maximumLabel = String.valueOf(mSpec.MaxValue);
            int labelWidth = (int) p.measureText(maximumLabel);
            canvas.drawText(maximumLabel, mWidth - labelWidth, showMinMaxYSize, p);
        }

		int currentX = 0;
		boolean hasRangeNames = false;
		for (int i = 0; i < mSpec.Ranges.size() ; i++) {
            RangeSpec range = mSpec.Ranges.get(i);
            if (Strings.hasValue(range.Name))
                hasRangeNames = true;
        }
		// use a default color visible on light and dark background.
		// Override with range color if possible.
		int currentValueColor = Color.argb(255, 192, 92, 0);
		float rangeStartValue = mSpec.MinValue;

		int lastWidthRange = 0;
		for (int i = 0; i < mSpec.Ranges.size(); i++) {
            RangeSpec range = mSpec.Ranges.get(i);
			int originalWidthRange = getWidth(range.Length);
			int widthRange;
            if (i == mSpec.Ranges.size() - 1) {
				widthRange = originalWidthRange + lastWidthRange;
			}
			else {
				widthRange = (int) (originalWidthRange * mPercentage);
				lastWidthRange += originalWidthRange - widthRange;
			}
            int elementY = height;
            if (mSpec.ShowMinMax || hasRangeNames)
                elementY += Math.round(showMinMaxYSize);
            elementY += 5;
            ColumnElement element = new ColumnElement(currentX, elementY, height, widthRange, range.Color);
            element.drawElement(canvas);
            int textWidth = (int) p.measureText(range.Name.trim());
            int xText = (currentX + (widthRange / 2)) - (textWidth / 2);
            canvas.drawText(range.Name.trim(), xText, showMinMaxYSize, p);
            currentX += widthRange;
            if (rangeStartValue <= mSpec.CurrentValue && mSpec.CurrentValue <= (rangeStartValue + range.Length))
                currentValueColor = range.Color;
            rangeStartValue = rangeStartValue + range.Length;
        }

		if (mSpec.ShowValue) {
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setStrokeWidth(2);
            p.setColor(Color.YELLOW);
            Path path = new Path();
            path.moveTo(0, -10);
            path.lineTo(5, 0);
            path.lineTo(-5, 0);
            path.close();
            int x = getWidth(mSpec.CurrentValue - mSpec.MinValue);
            int y = (int) (height + fontHeight);
            if (mSpec.ShowMinMax || hasRangeNames)
                y += Math.round(showMinMaxYSize);
            Rect rect = new Rect(x, y, x + mMarkSize, y + mMarkSize);
            p.setShader(ColumnElement.getColumnShader(currentValueColor, rect));
            path.offset(x, y);
            canvas.drawPath(path, p);
            p.setStrokeWidth(1);

            String valueText = String.valueOf(mSpec.CurrentValue);
            float valueLength = p.measureText(valueText);
            if (x + valueLength > mWidth)
                x = (int) (mWidth - valueLength);

            Paint pText = new Paint();
            pText.setAntiAlias(true);
            pText.setColor(defaultTextColor);
            pText.setTextSize(fontHeight);

            // Current Value color
            if (themeClass != null) {
                // Text Color
                Integer colorId = ThemeUtils.getColorId(themeClass.getColor());
                //	Integer highlightedColorId = ThemeUtils.getColorId(themeClass.getHighlightedColor());
                if (colorId != null /*|| highlightedColorId != null*/) {
                    pText.setColor(colorId);
                }
            }

            canvas.drawText(valueText, Math.max(x - (valueLength / 2), 0), y + mMarkSize + fontHeight, pText);
        }
	}

	private void doDrawCircular(Canvas canvas, Integer defaultTextColor, ThemeClassDefinition themeClass) {
		Paint pArc = new Paint();
		pArc.setStyle(Paint.Style.STROKE);
		pArc.setAntiAlias(true);
		int thickness = Services.Device.dipsToPixels(mSpec.Thickness);
		pArc.setStrokeWidth(thickness);
		int diameter = Math.min(getWidth(), getHeight()) - thickness;
		int startAngle = -90;
		if (mSpec.Ranges.size() > 0) {
			int lastAngle = 0;
			for (int i = 0; i < mSpec.Ranges.size(); i++) {
				RangeSpec range = mSpec.Ranges.get(i);
				int originalAngle = getRatio(range.Length, 360);
				int angle;
				if (i == mSpec.Ranges.size() - 1) {
					angle = originalAngle + lastAngle;
				}
				else {
					angle = (int) (originalAngle * mPercentage);
					lastAngle += originalAngle - angle;
				}
				startAngle = doDrawCircularArc(canvas, pArc, range.Color, angle, diameter, startAngle);
			}
		}

		if (Services.Strings.hasValue(mSpec.Title)) {
			Paint pText = new Paint() {
				{
					setStyle(Style.STROKE);
					setAntiAlias(true);
				}
			};
			pText.setColor(defaultTextColor);

			float fontHeight = Services.Device.dipsToPixels(12);
			if (themeClass != null) {
				// Text Size
				Integer textSizef = themeClass.getFont().getFontSize();
				if (textSizef != null && textSizef > 0) {
					fontHeight = textSizef;
				}

				// Text Color
				Integer colorId = ThemeUtils.getColorId(themeClass.getColor());
				//	Integer highlightedColorId = ThemeUtils.getColorId(themeClass.getHighlightedColor());
				if (colorId != null /*|| highlightedColorId != null*/) {
					pText.setColor(colorId);
				}
			}
			pText.setTextSize(fontHeight);

			String text = mSpec.Title.trim();
			canvas.drawText(text, Math.max((getWidth() - pText.measureText(text)) / 2, 0), (getHeight() - (pText.descent() + pText.ascent())) / 2, pText);
		}
	}

	private int doDrawCircularArc(Canvas canvas, Paint pArc, int color, int angle, int diameter, int startAngle)
	{
		pArc.setColor(color);
		Path path = new Path();
		int xArc = (getWidth() - diameter) / 2;
		int yArc = (getHeight() - diameter) / 2;
		path.addArc(new RectF(xArc, yArc, xArc + diameter, yArc + diameter), startAngle, angle);
		startAngle += angle;
		canvas.drawPath(path, pArc);
		return startAngle;
	}

	private int getRatio(float value, int max) {
		if (mSpec.MaxValue > mSpec.MinValue)
			return (int) ((value * max) / (mSpec.MaxValue - mSpec.MinValue));
		return (int) ((value * max) / mSpec.MaxValue);
	}

	private int getWidth(float value) {
		return getRatio(value, mWidth);
	}
}

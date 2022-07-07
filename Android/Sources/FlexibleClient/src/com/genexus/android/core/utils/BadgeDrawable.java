package com.genexus.android.core.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.TransformationDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.handlers.fonts.FontsHelperHandlers;

public class BadgeDrawable extends Drawable {
    private final Context mContext;
    private float mTextSize;
    private float mPaddingLeft;
    private float mPaddingRight;
    private float mPaddingTop;
    private float mPaddingBottom;
    private Paint mBadgePaint;
    private Paint mBadgeBorderPaint;
    private Paint mTextPaint;
    private TransformationDefinition mTransformation;
    private Drawable mBadgeImage;
    private float mZoom = 1;

    private static final int MIN_HORIZONTAL_PADDING = 2;
    private static final int MIN_VERTICAL_PADDING = 1;

    private String mText = "";
    private boolean mWillDraw = false;

    public BadgeDrawable(Context context) {
        mContext = context;
        mTextSize = Services.Device.dipsToPixels(10);

        mBadgePaint = new Paint();
        mBadgePaint.setColor(Color.RED);
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);
	    mBadgePaint.setFilterBitmap(true);
	    mBadgePaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
	    mTextPaint.setFilterBitmap(true);
	    mTextPaint.setDither(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mWillDraw) {
            return;
        }

        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        // Position the badge in the top-right quadrant of the icon.
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float lineHeight = metrics.bottom - metrics.top;
        float badgeRadius = (lineHeight + mPaddingTop + mPaddingBottom) / 2;
        float badgeCenterX = width; // Right with half badge outside
        float badgeCenterY = 0; // Top with half badge outside

        canvas.save();

        // Apply transformations
        if (mTransformation != null) {
            float translateX = getTranslate(Math.max(width, badgeRadius * 2), mTransformation.getTranslateX()); // The badge may be bigger that the icon so we take the max
            float translateY = getTranslate(Math.max(height, badgeRadius * 2), mTransformation.getTranslateY());
            if (translateX != 0 || translateY != 0)
                canvas.translate(translateX, translateY);

            float scaleX = getScale(mTransformation.getScaleX());
            float scaleY = getScale(mTransformation.getScaleY());
            if (scaleX != 1 || scaleY != 1)
                canvas.scale(scaleX, scaleY);
        }

        if (mZoom != 1) {
            // do it after mTransformation translate
            // translate so the zoom is done from the center of the circle
            canvas.translate(badgeCenterX, badgeCenterY);
            canvas.scale(mZoom, mZoom);
            canvas.translate(-badgeCenterX, -badgeCenterY);
        }

        // Draw badge image
        if (mBadgeImage != null) {
            float moveX = badgeCenterX - badgeRadius;
            float moveY = badgeCenterY - badgeRadius;
            float scale = badgeRadius * 2 / Math.max(mBadgeImage.getIntrinsicWidth(), mBadgeImage.getIntrinsicHeight());
            canvas.translate(moveX, moveY);
            canvas.scale(scale, scale);
            mBadgeImage.setBounds(0, 0, mBadgeImage.getIntrinsicWidth(), mBadgeImage.getIntrinsicHeight()); // WA: sometimes it has empty bounds
            mBadgeImage.draw(canvas);
            canvas.scale(1 / scale, 1 / scale);
            canvas.translate(-moveX, -moveY);
        }

        // Get text bounds
        Rect textRect = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), textRect);
        float textWidth = textRect.right - textRect.left;
        float textHeight = textRect.bottom - textRect.top;

        // Draw badge circle
        float badgeMinimumWidth = textWidth + mPaddingLeft + mPaddingRight;
        if (badgeMinimumWidth < badgeRadius * 2 ) {
            if (mBadgePaint != null)
                canvas.drawCircle(badgeCenterX, badgeCenterY, badgeRadius, mBadgePaint);
            if (mBadgeBorderPaint != null)
                canvas.drawCircle(badgeCenterX, badgeCenterY, badgeRadius, mBadgeBorderPaint);
        }
        else {
            float left = badgeCenterX - badgeMinimumWidth / 2;
            float right = badgeCenterX + badgeMinimumWidth / 2;
            float top = badgeCenterY - badgeRadius;
            float bottom = badgeCenterY + badgeRadius;
            if (mBadgePaint != null)
                canvas.drawOval(left, top, right, bottom, mBadgePaint);
            if (mBadgeBorderPaint != null)
                canvas.drawOval(left, top, right, bottom, mBadgeBorderPaint);
        }

        // Draw text inside the badge
        float textX = badgeCenterX + mPaddingLeft - mPaddingRight;
        float textY = badgeCenterY + (textHeight / 2f) + mPaddingTop - mPaddingBottom;
        canvas.drawText(mText, textX, textY, mTextPaint);

        canvas.restore();
    }

    private float getTranslate(float total, DimensionValue translate) {
        if (translate == null)
            return 0;
        else if (translate.Type == DimensionValue.ValueType.PIXELS)
            return translate.Value;
        else
            return total * (translate.Value / 100f);
    }

    private float getScale(DimensionValue scale) {
        if (scale == null)
            return 1;
        else if (scale.Type == DimensionValue.ValueType.PIXELS)
            return scale.Value;
        else
            return scale.Value / 100f;
    }

    public void setZoom(float zoom) {
        mZoom = zoom;
    }

    /*
    Sets the text (i.e notifications) to display.
     */
    public void setText(String text) {
        mText = text;

        // Only draw a badge if there are notifications.
        mWillDraw = Services.Strings.tryParseInt(text, 1) > 0;
        invalidateSelf();
    }

    public void setThemeClass(ThemeClassDefinition themeClass) {
        if (themeClass != null) {
            String image = themeClass.getBackgroundImage();
            if (image != null && image.length() > 0) {
                mBadgeImage = Services.Images.getStaticImage(mContext, image);
            }

            if (mBadgeImage != null) {
                mBadgePaint = null;
            }
            else {
                Integer backgroundColor = ThemeUtils.getColorId(themeClass.getBackgroundColor());
                if (backgroundColor != null) {
                    if (backgroundColor == Color.TRANSPARENT)
                        mBadgePaint = null;
                    else
                        mBadgePaint.setColor(backgroundColor);
                }

                Integer borderColor = ThemeUtils.getColorId(themeClass.getBorderColor());
                if (borderColor != null) {
                    mBadgeBorderPaint = new Paint();
                    mBadgeBorderPaint.setColor(borderColor);
                    mBadgeBorderPaint.setAntiAlias(true);
                    mBadgeBorderPaint.setStyle(Paint.Style.STROKE);
                    mBadgeBorderPaint.setFilterBitmap(true);
                    mBadgeBorderPaint.setDither(true);
                    mBadgeBorderPaint.setStrokeWidth(themeClass.getBorderWidth());
                    String borderStyle = themeClass.getBorderStyle();
                    if (borderStyle != null) {
                        if (borderStyle.equalsIgnoreCase("dotted"))
                            mBadgeBorderPaint.setPathEffect(new DashPathEffect(new float[]{3, 8}, 0)); // Arbitrary constants that looked well in the emulator
                        else if (borderStyle.equalsIgnoreCase("dashed"))
                            mBadgeBorderPaint.setPathEffect(new DashPathEffect(new float[]{12, 12}, 0));
                    }
                }
            }

            Integer foreColor = ThemeUtils.getColorId(themeClass.getColor());
            if (foreColor != null)
                mTextPaint.setColor(foreColor);

            String fontFamily = themeClass.getFont().getFontFamily();
            Typeface font = Strings.hasValue(fontFamily) ?
                    Services.Fonts.getFontFamily(themeClass.getTheme(), fontFamily,
						themeClass.getFont().getFontWeight(),
						themeClass.getFont().getFontItalic(),
						new FontsHelperHandlers.SetPaintHandler(mTextPaint))
                    : null;
            Integer fontStyle = themeClass.getFont().getFontStyle();
            if (fontStyle == null || fontStyle == Typeface.NORMAL) {
                if (font != null)
                    mTextPaint.setTypeface(font);
            }
            else if (font != null)
                mTextPaint.setTypeface(Typeface.create(font, fontStyle));
            else
                mTextPaint.setTypeface(Typeface.create(mTextPaint.getTypeface(), fontStyle));

            Integer fontSize = themeClass.getFont().getFontSize();
            if (fontSize != null) {
                mTextSize = fontSize;
                mTextPaint.setTextSize(mTextSize);
            }

            mPaddingLeft = Math.max(themeClass.getPadding().left, MIN_HORIZONTAL_PADDING);
            mPaddingRight = Math.max(themeClass.getPadding().right, MIN_HORIZONTAL_PADDING);
            mPaddingTop = Math.max(themeClass.getPadding().top, MIN_VERTICAL_PADDING);
            mPaddingBottom = Math.max(themeClass.getPadding().bottom, MIN_VERTICAL_PADDING);

            mTransformation = themeClass.getTransformation();
            invalidateSelf();
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

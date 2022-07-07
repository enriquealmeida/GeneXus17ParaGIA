package com.genexus.android.core.usercontrols.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.enums.ImageScaleType;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.GxImageViewBase;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.BitmapUtils;
import com.genexus.android.core.utils.TaskRunner;

import java.util.ArrayList;
import java.util.List;

import static com.genexus.android.core.utils.FileUtils2.saveBitmapToFile;

@SuppressLint("ViewConstructor")
public class GxImageAnnotations extends GxImageViewBase implements IGxEditThemeable, IGxControlRuntime {

	public static final String NAME = "SDImageAnnotations";
	private static final String SUBDIR = "transformations";

	private static final String PROPERTY_TRACE_THICKNESS = "TraceThickness";
	private static final String PROPERTY_TRACE_COLOR = "TraceColor";

	private static final String METHOD_GET_ANNOTATED_IMAGE = "getannotatedimage";
	private static final String METHOD_GET_ANNOTATIONS = "getannotations";
	private static final String METHOD_UNDO = "Undo";
	private static final String METHOD_REDO = "Redo";

	private static final float TOUCH_TOLERANCE = 4;
	private static final float DEFAULT_WIDTH = 3.0f;
	private static final int DEFAULT_COLOR = 0x00000000;

	private final GxImageAnnotationsDefinition mDefinition;
	private final Context mContext;

	private ThemeClassDefinition mThemeClassDefinition;
	private ImageScaleType mScaleType;
	private String mTag;
	private String mUri;
	private float mLastPositionX;
	private float mLastPositionY;
	private boolean mShouldScreenBeCleared = false;
	private boolean mShouldDrawImage = false;

	private Bitmap mCanvasBitmap;
	private Bitmap mImageBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mDrawPaint;
	private Paint mClearPaint;
	private Paint mBitmapPaint;
	private Matrix mTransformation;

	private List<DrawItem> mDrawHistory;
	private List<DrawItem> mUndoHistory;
	private int mCurrentHistoryIndex = 0;

	public GxImageAnnotations(Context context, Coordinator coordinator, LayoutItemDefinition def) {
		super(context, null);
		mContext = context;
		mDefinition = new GxImageAnnotationsDefinition(def);
		mScaleType = ImageScaleType.FIT;
		setWillNotDraw(false);
		setupDrawing();
	}

	@Override
	public Expression.Value getPropertyValue(String propertyName) {
		switch (propertyName) {
			case PROPERTY_TRACE_THICKNESS:
				return Expression.Value.newInteger(mDefinition.getTraceThickness());
			case PROPERTY_TRACE_COLOR:
				return Expression.Value.newString(mDefinition.getTraceColor());
		}
		return null;
	}

	@Override
	public void setPropertyValue(String propertyName, Expression.Value value) {
		switch (propertyName) {
			case PROPERTY_TRACE_THICKNESS:
				setTraceThickness(value.coerceToInt());
				break;
			case PROPERTY_TRACE_COLOR:
				setTraceColor(value.coerceToString());
				break;
			default:
				Services.Log.warning(NAME, "Unsupported property: " + propertyName);
		}
	}

	@Override
	public Expression.Value callMethod(String methodName, List<Expression.Value> parameters) {
		Bitmap bitmap = null;
		switch (methodName) {
			case METHOD_GET_ANNOTATIONS:
				if (mDrawHistory.isEmpty())
					return Expression.Value.newString(Strings.EMPTY);

				bitmap = computeFinalBitmap(false);
				break;
			case METHOD_GET_ANNOTATED_IMAGE:
				if (mDrawHistory.isEmpty())
					return Expression.Value.newString(mUri);

				bitmap = computeFinalBitmap(true);
				break;
			case METHOD_UNDO:
				undoDrawing();
				break;
			case METHOD_REDO:
				redoDrawing();
				break;
		}

		if (bitmap == null)
			return null;

		String annotationsOnly = "file://" + saveBitmapToFile(mContext, bitmap, SUBDIR, null, Strings.EMPTY);
		return Expression.Value.newString(annotationsOnly);
	}

	private void setupDrawing() {
		mTransformation = new Matrix();
		mUndoHistory = new ArrayList<>();
		mDrawHistory = new ArrayList<>();
		mPath = new Path();
		mClearPaint = new Paint();
		mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		mBitmapPaint = new Paint();
		mBitmapPaint.setFilterBitmap(true);
		mDrawPaint = new Paint(Paint.DITHER_FLAG);
		mDrawPaint.setAntiAlias(true);
		mDrawPaint.setDither(true);
		mDrawPaint.setStyle(Paint.Style.STROKE);
		mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
		mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
		mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

		if (mDefinition.getTraceColor().isEmpty())
			mDrawPaint.setColor(DEFAULT_COLOR);
		else
			mDrawPaint.setColor(parseHexColor(mDefinition.getTraceColor()));

		if (mDefinition.getTraceThickness() == 0)
			mDrawPaint.setStrokeWidth(DEFAULT_WIDTH);
		else
			mDrawPaint.setStrokeWidth(Services.Device.dipsToPixels(mDefinition.getTraceThickness()));
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);
		mCanvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		createCanvasFromBitmap(mCanvasBitmap);
	}

	private void createCanvasFromBitmap(Bitmap bitmap) {
		mImageBitmap = bitmap;
		mCanvasBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mCanvasBitmap);

		float originalWidth = mImageBitmap.getWidth();
		float originalHeight = mImageBitmap.getHeight();

		Services.Log.debug("image size: " + originalHeight + "x" + originalWidth);
		Services.Log.debug("control size: " + this.getHeight() + "x" + this.getWidth());

		// we cannot handle null or TILE
		if (mScaleType == null || mScaleType == ImageScaleType.TILE) {
			mScaleType = ImageScaleType.FIT;
		}

		BitmapUtils.computeScalingMatrix(originalWidth, originalHeight, getWidth(), getHeight(), mScaleType, Alignment.CENTER, mTransformation);
		mShouldDrawImage = true;

		invalidate();
	}

	private Bitmap computeFinalBitmap(boolean withImage) {
		Bitmap finalBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas finalCanvas = new Canvas(finalBitmap);

		if (withImage && mImageBitmap != null) {
			finalCanvas.drawBitmap(mImageBitmap, mTransformation, mBitmapPaint);
		}

		for (DrawItem drawItem : mDrawHistory) {
			mDrawPaint.setColor(drawItem.mColor);
			mDrawPaint.setStrokeWidth(drawItem.mStrokeWidth);
			finalCanvas.drawPath(drawItem.mPath, mDrawPaint);
		}

		return finalBitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();

		if (mShouldScreenBeCleared) {
			mCanvas.drawPaint(mClearPaint);
			mShouldScreenBeCleared = false;
			if (mImageBitmap != null) mShouldDrawImage = true;
		}

		if (mShouldDrawImage) {
			mCanvas.drawBitmap(mImageBitmap, mTransformation, mBitmapPaint);
			mShouldDrawImage = false;
		}

		for (DrawItem drawItem : mDrawHistory) {
			mDrawPaint.setColor(drawItem.mColor);
			mDrawPaint.setStrokeWidth(drawItem.mStrokeWidth);
			mCanvas.drawPath(drawItem.mPath, mDrawPaint);
		}

		canvas.drawBitmap(mCanvasBitmap, 0, 0, mDrawPaint);
		canvas.restore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getParent().requestDisallowInterceptTouchEvent(true);

		float touchX = event.getX();
		float touchY = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchStart(touchX, touchY);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touchMove(touchX, touchY);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touchUp();
				invalidate();
				break;
		}

		return true;
	}

	private void touchStart(float x, float y) {
		mPath = new Path();
		mPath.reset();
		mPath.moveTo(x, y);

		addDrawingToHistory();

		mLastPositionX = x;
		mLastPositionY = y;
	}

	private void touchMove(float x, float y) {
		float dx = Math.abs(x - mLastPositionX);
		float dy = Math.abs(y - mLastPositionY);

		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mLastPositionX, mLastPositionY, (x + mLastPositionX) / 2, (y + mLastPositionY) / 2);
			mLastPositionX = x;
			mLastPositionY = y;
		}
	}

	private void touchUp() {
		mPath.lineTo(mLastPositionX, mLastPositionY);
	}

	private void setTraceThickness(int newThickness) {
		mDefinition.setTraceThickness(newThickness);
		mDrawPaint.setStrokeWidth(Services.Device.dipsToPixels(newThickness));
	}

	private void setTraceColor(String newColor) {
		mDefinition.setTraceColor(newColor);
		mDrawPaint.setColor(parseHexColor(newColor));
	}

	public void clear() {
		mShouldScreenBeCleared = true;
		mDrawHistory.clear();
		invalidate();
	}

	private void addDrawingToHistory() {
		DrawItem drawItem = new DrawItem(parseHexColor(mDefinition.getTraceColor()),
			Services.Device.dipsToPixels(mDefinition.getTraceThickness()), mPath);

		mDrawHistory.add(drawItem);
		if (mDrawHistory.size() == 1)
			mCurrentHistoryIndex = 0;
		else
			mCurrentHistoryIndex += 1;

		clearUndoHistory();
	}

	private void clearUndoHistory() {
		mUndoHistory.clear();
	}

	private void undoDrawing() {
		if (mDrawHistory.size() > 0) {
			DrawItem undoDrawItem = mDrawHistory.get(mCurrentHistoryIndex);
			mUndoHistory.add(undoDrawItem);
			mDrawHistory.remove(mCurrentHistoryIndex);
			mCurrentHistoryIndex -= 1;
			mShouldScreenBeCleared = true;
			invalidate();
		}
	}

	private void redoDrawing() {
		if (mUndoHistory.size() > 0) {
			DrawItem redoDrawItem = mUndoHistory.get(mUndoHistory.size() - 1);
			mDrawHistory.add(redoDrawItem);
			mUndoHistory.remove(mUndoHistory.size() - 1);
			mCurrentHistoryIndex += 1;
			invalidate();
		}
	}

	static class DrawItem {

		final int mColor;
		final int mStrokeWidth;
		final Path mPath;

		DrawItem(int color, int strokeWidth, Path path) {
			this.mColor = color;
			this.mStrokeWidth = strokeWidth;
			this.mPath = path;
		}
	}

	int parseHexColor(String hexColor) {
		return Color.parseColor(hexColor);
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass) {
		mThemeClassDefinition = themeClass;
		mScaleType = themeClass.getImageScaleType();
		setImageScaleType(mScaleType);
	}

	@Override
	public void setGxValue(String value) {
		mUri = value;
		if (Strings.hasValue(mUri)) {
			//Running async to avoid NetworkOnMainThreadException
			TaskRunner.execute(new TaskRunner.BaseTask<Bitmap>()  {

				@Override
				public Bitmap doInBackground() {
					return Services.Images.getBitmap(mContext, mUri);
				}

				@Override
				public void onPostExecute(Bitmap bitmap) {
					super.onPostExecute(bitmap);
					createCanvasFromBitmap(bitmap);
				}
			});
		}
	}

	@Override
	public String getGxValue() {
		return mUri;
	}

	@Override
	public String getGxTag() {
		return mTag;
	}

	@Override
	public void setGxTag(String tag) {
		mTag = tag;
		this.setTag(tag);
	}

	@Override
	public void setValueFromIntent(Intent data) {
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public IGxEdit getViewControl() {
		return this;
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	@Override
	public ThemeClassDefinition getThemeClass() {
		return mThemeClassDefinition;
	}
}

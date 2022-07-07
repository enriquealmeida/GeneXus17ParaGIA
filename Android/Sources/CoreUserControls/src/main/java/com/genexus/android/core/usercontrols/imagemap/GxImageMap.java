package com.genexus.android.core.usercontrols.imagemap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.artech.base.services.AndroidContext;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.core.controls.ImageViewDisplayImageWrapper;
import com.genexus.android.core.controls.grids.GridAdapter;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.usercontrols.R;
import com.genexus.android.core.usercontrols.image.ImageViewTouch;
import com.genexus.android.core.ui.Coordinator;

import static com.genexus.android.layout.ControlHelper.METHOD_BACKGROUND_IMAGE;

@SuppressLint("ViewConstructor")
@SuppressWarnings("deprecation")
public class GxImageMap extends AbsoluteLayout implements IGridView, IGxControlRuntime {

	public static final String NAME = "SD ImageMap";
	private final Coordinator mCoordinator;
	private GridHelper mHelper;
	private GridAdapter mAdapter;
	private GxImageMapDefinition mDefinition;
	private HashMap<List<String>, View> mItemsViews = new LinkedHashMap<>();

	private ImageMapTouch mImageMap;

	private EntityList mEntities;
	private GxImageMapGestureListener mGestureManager;

	protected boolean mBitmapLoaded = false;
	protected boolean mDataArrived = false;
	protected boolean mRuntimeImage = false;
	protected float mRuntimeDensityRatio = 1;

	private static final String EVENT_LONG_TAP = "LongTap";

	public GxImageMap(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
		super(context);
		mCoordinator = coordinator;
		setLayoutDefinition(definition);
	}

	private void setLayoutDefinition(LayoutItemDefinition layoutItemDefinition) {
		init((GridDefinition) layoutItemDefinition);
	}

	@SuppressWarnings("deprecation")
	private void init(GridDefinition gridDefinition) {
		mDefinition = new GxImageMapDefinition(getContext(), gridDefinition);
		mHelper = new GridHelper(this, mCoordinator, gridDefinition);

		mImageMap = new ImageMapTouch(getContext(), null);
		mImageMap.setMaxZoom(5f); // 5???
		mImageMap.setMinZoom(1f);

		mGestureManager = new GxImageMapGestureListener(this);
		mImageMap.addZoomListener(mGestureManager);

		GxImageMapTapListener tapListener = new GxImageMapTapListener(this);
		mImageMap.setTapListener(tapListener);

		ImageViewTouch.OnBitmapChangedListener bitmapChangedListener = new GxImageMapOnBitmapChangedListener(this);
		mImageMap.setOnBitmapChangedListener(bitmapChangedListener);

		addView(mImageMap, new AbsoluteLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0, 0));

		Services.Images.showStaticImage(getContext(), ImageViewDisplayImageWrapper.to(mImageMap), mDefinition.getmImage());
	}

	protected HashMap<List<String>, View> getItemsViews() {
		return mItemsViews;
	}

	protected GridHelper getHelper() {
		return mHelper;
	}

	protected ImageMapTouch getControl() {
		return mImageMap;
	}

	private void prepareAdapter() {
		if (mAdapter == null)
			mAdapter = new GridAdapter(getContext(), mHelper, mDefinition.getGrid());
	}


	@Override
	public void addListener(GridEventsListener listener) {
		mHelper.setListener(listener);
	}

	@Override
	public void update(ViewData data) {
		prepareAdapter();
		mAdapter.setData(data);
		mEntities = data.getEntities();

		mDataArrived = true;
		if (mBitmapLoaded)
			updateMap();
	}

	public void updateMap() {
		updateMap(mEntities);
	}

	@SuppressWarnings("deprecation")
	private void updateMap(EntityList data) {
		Services.Log.debug("GxImageMap updateMap with data");
		//Services.Log.debug("GxImageMap updateMap bitmapLoaded : " + mBitmapLoaded);
		//Services.Log.debug("GxImageMap updateMap dataReady : " + mDataArrived);
		//Services.Log.debug("GxImageMap updateMap runtimeImage : " + mRuntimeImage);

		int id = 0;
		RectF bmpRect = mImageMap.getBitmapRect();

		float currentFactor = mImageMap.getCurrentScaleFactor();

		int rowcount = getChildCount();
		if (rowcount > 1)
			removeViews(1, rowcount - 1);

		float densityRatio = getDensityRatio();

		// calculate density ratio for runtime images.
		if (mRuntimeImage) {
			densityRatio = mRuntimeDensityRatio;
		}

		for (Entity entity : data) {
			float x = Float.valueOf(entity.optStringProperty(mDefinition.getHCoordinateAttribute())) * densityRatio;
			float y = Float.valueOf(entity.optStringProperty(mDefinition.getVCoordinateAttribute())) * densityRatio;
			String sProp = entity.optStringProperty(mDefinition.getSizeAttribute());
			String wProp = entity.optStringProperty(mDefinition.getWidthAttribute());
			String hProp = entity.optStringProperty(mDefinition.getHeightAttribute());
			String rProp = entity.optStringProperty(mDefinition.getRotationAttribute());
			if (!Strings.hasValue(wProp))
				wProp = sProp;
			if (!Strings.hasValue(hProp))
				hProp = sProp;
			float w = Float.valueOf(wProp) * densityRatio;
			if (w == 0)
				w = Float.valueOf(sProp) * densityRatio;
			float h = Float.valueOf(hProp) * densityRatio;
			if (h == 0)
				h = Float.valueOf(sProp) * densityRatio;

			float r = Strings.hasValue(rProp) ? Float.valueOf(rProp) : 0;

			float scaledUnzoomedX = getScaledDimension(x);
			float scaledUnzoomedY = getScaledDimension(y);
			float scaledUnzoomedW = getScaledDimension(w);
			float scaledUnzoomedH = getScaledDimension(h);

			float scaledX = (bmpRect != null ? bmpRect.left : 0) + (scaledUnzoomedX * currentFactor);
			float scaledY = (bmpRect != null ? bmpRect.top : 0) + (scaledUnzoomedY * currentFactor);

			mAdapter.setBounds((int) w, (int) h);

			View previousView = null;
			//if (mItemsViews.containsKey(entity.getKey())){
			//	previousView = mItemsViews.get(entity.getKey());
			//	mHelper.discardItemView(previousView);
			//}

			View itemView = mAdapter.getView(id, previousView, null);

			itemView.setPivotX(0);
			itemView.setPivotY(0);

			if (previousView == null) {
				RectF original = new RectF(scaledUnzoomedX, scaledUnzoomedY, scaledUnzoomedW, scaledUnzoomedH);

				itemView.setTag(R.id.tag_imagemap_item_origin, original);
				itemView.setTag(R.id.tag_imagemap_item_entity, entity);
			}

			itemView.setLayoutParams(new AbsoluteLayout.LayoutParams((int) w, (int) h, (int) scaledX, (int) scaledY));

			if (previousView == null)
				addView(itemView);

			scaleView(itemView, currentFactor);

			if (r != 0) {
				itemView.setPivotX(itemView.getWidth() / 2);
				itemView.setPivotY(itemView.getHeight() / 2);
				itemView.setRotation(-r);
			}

			id++;
		}
		invalidate();
	}

	public int getUnscaledValue(float scaledVal, char axis) {
		float densityRatio = getDensityRatio();
		if (mRuntimeImage) densityRatio = mRuntimeDensityRatio;
		RectF bmpRect = mImageMap.getBitmapRect();
		float currentFactor = mImageMap.getCurrentScaleFactor();

		float scaledUnzoomedValue = (scaledVal - (bmpRect != null ? (axis == 'x' ? bmpRect.left : bmpRect.top) : 0)) / currentFactor;
		float value = getUnscaledDimension(scaledUnzoomedValue);
		float finalCoordinate = value / densityRatio;
		return (int) finalCoordinate;
	}

	public float getDensityRatio() {
		if (Strings.hasValue(mDefinition.getmImage()) && !mRuntimeImage) {
			int resourceId = Services.Resources.getResourceId(mDefinition.getmImage(), "drawable");
			if (resourceId != 0) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeResource(getResources(), resourceId, options);
				return (float) options.inTargetDensity / (float) options.inDensity;
			}
		}
		return 1;
	}

	private float getUnscaledDimension(float scaled) {
		return scaled / mImageMap.getScaleRatio();
	}

	private float getScaledDimension(float original) {
		return original * mImageMap.getScaleRatio();
	}

	protected void scaleView(View view, float scale) {

		view.setScaleX(scale * mImageMap.getScaleRatio());
		view.setScaleY(scale * mImageMap.getScaleRatio());
	}

	private class GxImageMapGestureListener implements ImageMapTouch.OnImageZoomedListener {

		GxImageMap mView;

		public GxImageMapGestureListener(GxImageMap view) {
			mView = view;
		}

		@Override
		public void zoom(float scale) {

			int count = mView.getChildCount();
			if (count == 1) // 1 is the map image
				return;

			for (int i = 1; i < count; i++) {
				View v = mView.getChildAt(i);

				mView.scaleView(v, scale);

				RectF original = (RectF) v.getTag(R.id.tag_imagemap_item_origin);
				RectF bmpRect = mImageMap.getBitmapRect();

				if (original != null && bmpRect != null) {
					float newX = bmpRect.left + (original.left * scale);
					float newY = bmpRect.top + (original.top * scale);

					v.setX(newX);
					v.setY(newY);
				}
			}
		}

		@Override
		public void panBy(double dx, double dy) {
			int count = mView.getChildCount();
			//Services.Log.debug("scale : " + mImageMap.getScale() );
			//Services.Log.debug("zooms : " + mImageMap.getMinZoom() + " , " + mImageMap.getMaxZoom() );
			if (count == 1) // 1 is the map image
				return;

			for (int i = 1; i < count; i++) {
				View v = mView.getChildAt(i);

				v.setX(v.getX() + (float) dx);
				v.setY(v.getY() + (float) dy);
			}
		}

		// do not use this method because get raise in the wrong moment, use OnBitmapChangedListener instead.
		@Override
		public void bitmapLoaded() {
		}
	}

	private static class GxImageMapTapListener implements ImageViewTouch.OnImageViewTouchTapListener {

		GxImageMap mView;

		public GxImageMapTapListener(GxImageMap view) {
			mView = view;
		}

		@Override
		public void onDoubleTap() { }

		@Override
		public void onLongTap(MotionEvent event) {
			View background = mView.getChildAt(0); //Position 0 refers to background image

			if (background == null || background.getX() < 0 || background.getY() < 0)
				return;

			int[] screenLocation = new int[2];
			background.getLocationOnScreen(screenLocation);

			int tapX = mView.getUnscaledValue(Math.round(event.getRawX() - screenLocation[0]), 'x');
			int tapY = mView.getUnscaledValue(Math.round(event.getRawY() - screenLocation[1]), 'y');

			if (tapX >= 0 && tapY >= 0) raiseMapEvent(EVENT_LONG_TAP, tapX, tapY);
		}

		@Override
		public void onSwipe(String swipeDirectionEvent) { }

		@Override
		public void onTap(MotionEvent e) {

			int count = mView.getChildCount();
			if (count == 1) // 1 is the map image
				return;

			double minDistance = Double.MAX_VALUE;
			View candidateView = null;

			for (int i = 1; i < count; i++) {
				View v = mView.getChildAt(i);

				if (v.getX() < 0 || v.getY() < 0)
					continue; // not visible at the moment

				int[] screenLocation = new int[2];
				v.getLocationOnScreen(screenLocation);

				if ((e.getRawX() > screenLocation[0] && e.getRawX() < screenLocation[0] + (v.getWidth() * v.getScaleX()))
					&& e.getRawY() > screenLocation[1] && e.getRawY() < screenLocation[1] + (v.getHeight() * v.getScaleY())) {

					float centerX = screenLocation[0] + ((v.getWidth() * v.getScaleX()) / 2);
					float centerY = screenLocation[1] + ((v.getHeight() * v.getScaleY()) / 2);

					//PITAGORAS!
					double distance = Math.sqrt((double) (centerY - e.getRawY()) * (centerY - e.getRawY()) + ((centerX - e.getRawX()) * (centerX - e.getRawX())));

					minDistance = Math.min(minDistance, distance);
					if (minDistance == distance) {
						candidateView = v;
						break;
					}
				}
			}

			if (candidateView != null) {
				Entity entity = (Entity) candidateView.getTag(R.id.tag_imagemap_item_entity);
				mView.getHelper().runDefaultAction(entity);
			}
		}

		private void raiseMapEvent(String eventName, int coordX, int coordY) {
			ActionDefinition actionDef = mView.mCoordinator.getControlEventHandler(mView, eventName);
			if (actionDef != null) {
				List<ActionParameter> params = actionDef.getEventParameters();
				if (params.size() == 2) {
					String paramNameX = params.get(0).getValue();
					String paramNameY = params.get(1).getValue();
					mView.mCoordinator.setValue(paramNameX, coordX);
					mView.mCoordinator.setValue(paramNameY, coordY);
				}
				mView.mCoordinator.runControlEvent(mView, eventName);
			}
		}
	}

	boolean mFirstLoad = true;

	//private
	private class GxImageMapOnBitmapChangedListener implements ImageViewTouch.OnBitmapChangedListener {

		GxImageMap mView;
		Drawable lastDrawable = null;

		public GxImageMapOnBitmapChangedListener(GxImageMap view) {
			mView = view;
		}

		@Override
		public void onBitmapChanged(Drawable drawable) {
			if (drawable != null) {
				if (mFirstLoad) {
					mView.mBitmapLoaded = true;
					refreshMapWithData();
				}
				mFirstLoad = false;
			}

			if (lastDrawable == null)
				lastDrawable = drawable;
			else {
				if (drawable != null && !lastDrawable.equals(drawable)) {
					// resize imagemap with the new drawable.
					Services.Log.debug("imagen changed");
					//how to update point?
					refreshMapWithData();
				}
				if (drawable != null)
					lastDrawable = drawable;
			}
		}

		private void refreshMapWithData() {
			if (mView.mDataArrived) {
				post(new Runnable() {
					@Override
					public void run() {
						mView.updateMap();
					}
				});
			}
		}

	}

	@Override
	public void setPropertyValue(String name, Expression.Value value) {
		if (name.equalsIgnoreCase("Image")) {
			Drawable d = Services.Images.getStaticImage(getContext(), value.coerceToString());
			if (d != null)
				mImageMap.setImageDrawable(d);
		}
	}

	@Override
	public Expression.Value callMethod(String name, List<Expression.Value> parameters) {
		if (name.equalsIgnoreCase(METHOD_BACKGROUND_IMAGE) && parameters.size() >= 1) {
			String image = parameters.get(0).coerceToString();
			if (image.startsWith("./")) {
				//relative path in android file system.
				String blobBasePath = AndroidContext.ApplicationContext.getFilesBlobsApplicationDirectory();
				// Local path in sdcard.
				image = blobBasePath + image.substring(1);
			}
			if (image.startsWith("gxblobdata://")) {
				//relative path in android file system.
				String blobBasePath = AndroidContext.ApplicationContext.getFilesBlobsApplicationDirectory();
				// Local path in sdcard.
				image = blobBasePath + image.substring(12);
			}
			Services.Log.debug("change image to : " + image);
			Services.Images.showDataImage(getContext(), ImageViewDisplayImageWrapper.to(mImageMap), image, false, true);
			mRuntimeImage = true;

			// calculate density ratio for runtime images.
			//decode image to get size and densityRatio if from resources
			int resourceId = Services.Resources.getDataImageResourceId(image);
			if (resourceId != 0) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeResource(getResources(), resourceId, options);
				mRuntimeDensityRatio = (float) options.inTargetDensity / (float) options.inDensity;
			} else {
				// from server , not resize it?
				mRuntimeDensityRatio = 1;
			}
		}

		return null;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		// disabled zoom.
		mImageMap.setEnabled(enabled);
	}
}

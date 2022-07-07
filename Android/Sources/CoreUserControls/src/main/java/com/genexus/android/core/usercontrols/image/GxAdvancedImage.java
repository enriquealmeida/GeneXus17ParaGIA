package com.genexus.android.core.usercontrols.image;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;

import com.genexus.android.content.FileProviderUtils;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.enums.ImageScaleType;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.GxTouchEvents;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.ImageViewDisplayImageWrapper;
import com.genexus.android.core.ui.Anchor;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.BitmapUtils;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.usercontrols.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.CLIPBOARD_SERVICE;

@SuppressLint("ViewConstructor")
public class GxAdvancedImage extends ImageViewTouch implements IGxEdit, IGxEditThemeable {
	public static final String NAME = "SD Advanced Image";
	private String mTag;
	private String mUri;
	private final GxAdvancedImageDefinition mDefinition;
	private ImageScaleType mScaleType = null;
	private static final int ITEM_ID_COPY = 1;
	private static final int ITEM_ID_PASTE = 2;

	public GxAdvancedImage(Context context, final Coordinator coordinator, LayoutItemDefinition def) {
		super(context, null);
		mDefinition = new GxAdvancedImageDefinition(def);
		setImageScaleType(ImageScaleType.FIT);
		setMaxZoom(mDefinition.getImageMaxZoom());
		setZoomOutsideControl(mDefinition.getZoomOutsideControl());
		setLongClickable(mDefinition.getEnableCopy());

		setTapListener(new GxAdvancedImageOnImageViewTouchTapListener(coordinator));
	}

	@Override
	public String getGxValue() {
		return mUri;
	}

	@Override
	public void setGxValue(String value) {
		mUri = value;
		ImageViewDisplayImageWrapper imageViewWrapper = ImageViewDisplayImageWrapper.to(this);
		Services.Images.showImage(getContext(), imageViewWrapper, mUri, false, true);
	}

	@Override
	public String getGxTag() {
		return mTag;
	}

	@Override
	public void setGxTag(String data) {
		mTag = data;
		this.setTag(data);
	}

	@Override
	public void setValueFromIntent(Intent data) {
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
	public boolean isEditable() {
		return false; // Never editable.
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass) {
		setImageScaleType(themeClass.getImageScaleType());
	}

	private void setImageScaleType(@NonNull ImageScaleType scaleType) {
		if (mScaleType != scaleType) {
			mScaleType = scaleType;
			if (mScaleType == ImageScaleType.NO_SCALE || mScaleType == ImageScaleType.TILE) {
				setFitToScreen(false);
			} else {
				setFitToScreen(true);
				requestLayout(); // Necessary because we might be changing between "different fits".
			}
		}
	}

	@Override
	protected void getProperBaseMatrixForFitToScreen(Drawable bitmap, Matrix matrix) {
		if (mScaleType == null || mScaleType == ImageScaleType.NO_SCALE || mScaleType == ImageScaleType.TILE)
			throw new IllegalStateException("Incorrect mScaleType value: " + mScaleType);

		// Substitute base implementation of this method to use different fit criteria.
		computeScalingMatrix(bitmap, mScaleType, matrix);
	}

	private void computeScalingMatrix(Drawable bitmap, ImageScaleType scaleType, Matrix matrix) {
		float viewWidth = getWidth();
		float viewHeight = getHeight();
		float w = bitmap.getIntrinsicWidth();
		float h = bitmap.getIntrinsicHeight();

		matrix.reset();
		BitmapUtils.computeScalingMatrix(w, h, viewWidth, viewHeight, scaleType, Alignment.CENTER, matrix);
	}

	private class GxAdvancedImageOnImageViewTouchTapListener implements ImageViewTouch.OnImageViewTouchTapListener {
		Coordinator mCoordinator;

		public GxAdvancedImageOnImageViewTouchTapListener(Coordinator coordinator) {
			mCoordinator = coordinator;
		}

		@Override
		public void onTap(MotionEvent e) {
			runEvent(GxTouchEvents.TAP);
		}

		@Override
		public void onDoubleTap() {
			if (mDefinition.getZoomOutsideControl())
				runEvent(GxTouchEvents.DOUBLE_TAP);
		}

		@Override
		public void onLongTap(MotionEvent event) {
			if (mDefinition.getEnableCopy()) {
				if (getDrawable() != null) { // don't show menu if there is nothing to copy
					PopupMenu menu = new PopupMenu(getContext(), GxAdvancedImage.this);
					menu.getMenu().add(0, ITEM_ID_COPY, 0, getContext().getString(R.string.GX_TipCopy));
					//menu.getMenu().add(0, ITEM_ID_PASTE, 1, "Paste"); // uncomment for testing
					menu.setOnMenuItemClickListener(item -> {
						if (item.getItemId() == ITEM_ID_COPY)
							doCopy();
						else // add option Paste to enable this function
							doPaste();
						return true;
					});
					menu.show();
				}
			} else {
				runEvent(GxTouchEvents.LONG_TAP);
			}
		}

		private @NonNull Bitmap getBitmapFromView(View view)
		{
			Bitmap bitmap;
			BitmapDrawable bitmapDrawable = Cast.as(BitmapDrawable.class, getDrawable());
			if (bitmapDrawable != null) {
				bitmap = bitmapDrawable.getBitmap();
			} else {
				IBitmapDrawable iBitmapDrawable = Cast.as(IBitmapDrawable.class, getDrawable());
				if (iBitmapDrawable != null) {
					bitmap = iBitmapDrawable.getBitmap();
				} else {
					// As a fallback always return what it is drawn on the screen
					bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmap);
					view.draw(canvas);
				}
			}
			return bitmap;
		}

		private void doCopy() {
			try {
				Bitmap bmp = getBitmapFromView(GxAdvancedImage.this);
				String path = getContext().getCacheDir() + "/clip.png";

				File file = new File(path);
				try (FileOutputStream out = new FileOutputStream(file)) {
					bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
				}

				ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
				if (clipboard != null) {
					Uri uri = FileProviderUtils.getUriForFile(getContext(), file);
					for (PackageInfo p : getContext().getPackageManager().getInstalledPackages(0)) {
						// grant each package one by one because sending an intent with flag Intent.FLAG_GRANT_READ_URI_PERMISSION doesn't work
						getContext().grantUriPermission(p.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
					}
					ClipData clip = ClipData.newUri(getContext().getContentResolver(), "Image", uri);
					clipboard.setPrimaryClip(clip);
				}
			} catch (IOException e) {
				Services.Log.error("Error copying to clipboard", e);
			}
		}

		private void doPaste() {
			try {
				ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
				if (clipboard != null) {
					ClipData clip = clipboard.getPrimaryClip();
					if (clip != null && clip.getItemCount() > 0) {
						Uri uri = clip.getItemAt(0).getUri();
						if (uri == null) { // check if it has the data in the intent
							Intent intent = clip.getItemAt(0).getIntent();
							if (intent != null
								&& intent.getClipData() != null
								&& intent.getClipData().getItemCount() > 0) {
								uri = intent.getClipData().getItemAt(0).getUri();
							}
						}
						if (uri != null) {
							InputStream is = getContext().getContentResolver().openInputStream(uri);
							setImageBitmap(BitmapFactory.decodeStream(is));
						}
					}
				}
			} catch (FileNotFoundException e) {
				Services.Log.error("Error pasting from clipboard", e);
			}
		}

		@Override
		public void onSwipe(String swipeDirectionEvent) {
			if (mDefinition.getZoomOutsideControl())
				runEvent(swipeDirectionEvent);
		}

		private void runEvent(String eventName) {
			if (mCoordinator != null) {
				// run this control tap event
				boolean handled = mCoordinator.runControlEvent(GxAdvancedImage.this, eventName);
				// temporary fix , send this event to parents table and grid.
				if (!handled) {
					// try with table parent
					final TableDefinition parentTableDef = mDefinition.getItem().findParentOfType(TableDefinition.class);
					if (parentTableDef != null) {
						View parentTable = mCoordinator.getControl(parentTableDef.getName());
						if (parentTable != null) {
							handled = mCoordinator.runControlEvent(parentTable, eventName);
						}
					}
				}
				if (!handled) {
					// try with grid parent
					final GridDefinition parentGridDef = mDefinition.getItem().findParentOfType(GridDefinition.class);
					if (parentGridDef != null) {
						// tab action grid
						View parentGrid = mCoordinator.getControl(parentGridDef.getName());
						if (parentGrid != null) {
							handled = mCoordinator.runControlEvent(parentGrid, eventName);
						}
						// otherwise use default action
						if (!handled && parentGridDef.getDefaultAction() != null) {
							mCoordinator.runAction(parentGridDef.getDefaultAction(), new Anchor(GxAdvancedImage.this));
						}
					}
				}
			}
		}
	}

}

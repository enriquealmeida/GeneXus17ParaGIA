package com.genexus.android.core.usercontrols.imagegallery;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import androidx.core.app.ShareCompat.IntentBuilder;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.ShareActionProvider;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.genexus.android.core.actions.ICustomMenuManager;
import com.genexus.android.content.FileProviderUtils;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MathUtils;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.GxTextView;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.controls.ImageViewDisplayImageWrapper;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.FileUtils2;
import com.genexus.android.core.usercontrols.R;

@SuppressLint("ViewConstructor")
@SuppressWarnings("deprecation")
public class ImageGallery extends LinearLayout implements IGridView, ICustomMenuManager, IGxThemeable
{
	public static final String NAME = "SD ImageGallery";

	/***
	 * Gallery and ImageView
	 */
	private Gallery mGallery;
	private ImageView mImageView;
	private GxTextView mTitleTextView;
	private GxTextView mSubTitleTextView;

	// Current images in the grid view.
    private ArrayList<String> mCurrentImages = new ArrayList<>();
	private ArrayList<Entity> mCurrentEntities = new ArrayList<>();
    private ArrayList<String> mCurrentTitleImages = new ArrayList<>();
    private ArrayList<String> mCurrentSubTitleImages = new ArrayList<>();
	private int mCurrentImage = -1;

    private ImageGalleryAdapter mAdapter;
    private SDImageGalleryDefinition mGalleryDefinition;

    private Context mContext;
    private ShareActionProvider mShareActionProvider;

	private final GridHelper mHelper;
	private GridEventsListener mListener;
	private ThemeClassDefinition mThemeClass;

	// For flip.
	private final GestureDetector mGestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	public ImageGallery(Context context, Coordinator coordinator, LayoutItemDefinition definition)
	{
		super(context);
		GridDefinition gridDefinition = (GridDefinition)definition;

		mContext = context;
		mGalleryDefinition = new SDImageGalleryDefinition(getContext(), gridDefinition);
		mGestureDetector = new GestureDetector(context, new GestureListener());

		createView(context);

		if (mGalleryDefinition.hasShareAction())
		{
			Context actionBarContext = SherlockHelper.getActionBarThemedContext(Cast.as(Activity.class, mContext));
			if (actionBarContext != null)
				mShareActionProvider = new ShareActionProvider(actionBarContext);
		}

		mHelper = new GridHelper(this, coordinator, gridDefinition);
	}

	private void createView(Context context)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.gximagegallerylayout, this, true);

        mGallery = findViewById(R.id.GalleryImageGallery);
        mGallery.setCallbackDuringFling(false);

        mTitleTextView = findViewById(R.id.titleTextViewImageGallery);
        mSubTitleTextView = findViewById(R.id.subTitleTextViewImageGallery);

        mImageView = findViewById(R.id.ImageViewImageGallery);

        mImageView.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(final View view, final MotionEvent event)
            {
            	mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        mImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mImageView.setPadding(15, 5, 15, 5);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setThemeImageGallery();
	}

	private void setThemeImageGallery()
	{
		ThemeClassDefinition themeTitle = Services.Themes.getThemeClass("Attribute.Title");
		ThemeClassDefinition themeSubtitle = Services.Themes.getThemeClass("Attribute.Subtitle");

		if ((themeTitle != null) && (mTitleTextView != null))
			mTitleTextView.setThemeClass(themeTitle);

		if ((themeSubtitle != null) && (mSubTitleTextView != null))
			mSubTitleTextView.setThemeClass(themeSubtitle);
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass;
		applyClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return mThemeClass;
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass)
	{
		mHelper.setThemeClass(themeClass);
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void addListener(GridEventsListener listener)
	{
		mListener = listener;
		mHelper.setListener(listener);
		mGallery.setOnItemClickListener(mOnItemClickList);
        mGallery.setOnItemSelectedListener(mOnItemSelectedListener);
	}

	@Override
	public void update(ViewData data)
	{
		// There is no image in the definition?
	 	if (!Services.Strings.hasValue(mGalleryDefinition.getThumbnailAttribute()))
    		return;

    	String thumbnailField = mGalleryDefinition.getThumbnailAttribute();
    	String titleField = mGalleryDefinition.getTitleAttribute();
    	String subtitleField = mGalleryDefinition.getSubtitleAttribute();

    	mCurrentImages.clear();
		mCurrentEntities.clear();
    	mCurrentTitleImages.clear();
    	mCurrentSubTitleImages.clear();

        for (Object entity : data.getEntities())
        {
            Entity e = (Entity) entity;
            mCurrentImages.add(e.optStringProperty(thumbnailField));
			mCurrentEntities.add(e);

            String strTitleImages = Strings.EMPTY;
            if (Services.Strings.hasValue(titleField))
            	strTitleImages = e.optStringProperty(titleField);

            String strSubTitleImages = Strings.EMPTY;
            if (Services.Strings.hasValue(subtitleField))
            	strSubTitleImages = e.optStringProperty(subtitleField);

            mCurrentTitleImages.add(strTitleImages);
            mCurrentSubTitleImages.add(strSubTitleImages);
        }

	 	if (mAdapter == null)
		{
			mAdapter = new ImageGalleryAdapter(mContext);
			mGallery.setAdapter(mAdapter);
		}

	 	mAdapter.setData(data.getEntities());
		mAdapter.notifyDataSetChanged();

		mCurrentImage = MathUtils.constrain(mCurrentImage, 0, mCurrentImages.size() - 1);
		setCurrentImage(mCurrentImage, true);
	}

	//OnItemSelected Listener
	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener()
	{
		private static final int VISIBLE_THRESHOLD = 5;

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			if (position >= mCurrentImages.size() - VISIBLE_THRESHOLD)
				mListener.requestMoreData();

			setCurrentImage(position, false);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
		{
			if (mCurrentImages.size()>0)
				setCurrentImage(0, false);
		}
	};

	//OnItemClick Listener
	private OnItemClickListener mOnItemClickList = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			setCurrentImage(position, false);
		}
	};

	private void setCurrentImage(int index, boolean forceRefresh)
	{
		if (mCurrentImage != index || forceRefresh)
		{
			mCurrentImage = index;
			if (!MathUtils.isConstrained(mCurrentImage, 0, mCurrentImages.size() - 1))
			{
				mCurrentImage = -1;
				mImageView.setImageDrawable(null);
				mTitleTextView.setText(Strings.EMPTY);
				mSubTitleTextView.setText(Strings.EMPTY);
				return;
			}

			String imageIdentifier = mCurrentImages.get(index);
			String imageTitle = mCurrentTitleImages.get(index);
			String imageSubtitle = mCurrentSubTitleImages.get(index);

			// Update image view.
			Services.Images.showDataImage(mContext, ImageViewDisplayImageWrapper.to(mImageView),
					imageIdentifier, false, false);

			mTitleTextView.setText(imageTitle);
			mSubTitleTextView.setText(imageSubtitle);

			// Prepare share intent, if enabled.
			if (mShareActionProvider != null)
				mShareActionProvider.setShareIntent(getShareIntent(imageIdentifier, imageTitle));
		}
	}

    public class ImageGalleryAdapter extends BaseAdapter
    {
        private Context context;
        private int imageBackground;

        private EntityList mData;

        public ImageGalleryAdapter(Context c)
        {
            context = c;
            TypedArray ta = context.obtainStyledAttributes(R.styleable.ImageGallery);
			imageBackground = ta.getResourceId(R.styleable.ImageGallery_android_galleryItemBackground, 1);
			ta.recycle();
        }

        @Override
		public int getCount() {
            return mCurrentImages.size();
        }

        @Override
		public Object getItem(int position) {
            return mCurrentImages.get(position);
        }

        @Override
		public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
            	ImageView image = new ImageView(context);

            	Rect thumbnailSize = mGalleryDefinition.getThumbnailSize();

            	int right = thumbnailSize.right;
            	int bottom = thumbnailSize.bottom;

            	image.setLayoutParams(new Gallery.LayoutParams(right,bottom));
            	image.setScaleType(ImageView.ScaleType.FIT_XY);
            	image.setPadding(5, 5, 5, 5);
            	image.setBackgroundResource(imageBackground);

				String imageUri = mCurrentImages.get(position);
				Services.Images.showDataImage(mContext,	ImageViewDisplayImageWrapper.to(image),
						imageUri, false, false);
				image.setAnimation(null);

				// set current item context
				Entity entity = mCurrentEntities.get(position);
				mHelper.createGridItemUIContext(entity);

		    	return image;
            }
            else
                return convertView;
        }

        public void setData(EntityList data)
    	{
    		mData = data;
    	}

        public Entity getEntity(int position)
    	{
        	if (mData!=null)
        	{
        		if (mData.size()>position)
        		{
        			return mData.get(position);
        		}
        	}
        	return null;
    	}
    }

	private class GestureListener extends SimpleOnGestureListener
	{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;

			// right to left swipe
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
			{
				if (mCurrentImage < mCurrentImages.size() - 1)
					mGallery.setSelection(mCurrentImage + 1);
				else
					Toast.makeText(mContext, R.string.GXM_NoNext, Toast.LENGTH_LONG).show();
			}
			else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
			{
				if (mCurrentImage > 0)
					mGallery.setSelection(mCurrentImage - 1);
				else
					Toast.makeText(mContext, R.string.GXM_NoPrevious, Toast.LENGTH_LONG).show();
			}

			return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
        	if (mListener == null)
				return false;

        	if (!MathUtils.isConstrained(mCurrentImage, 0, mCurrentImages.size() - 1))
        		return false;

        	Entity myEntity = mAdapter.getEntity(mCurrentImage);
        	if (myEntity!=null)
        		return mHelper.runDefaultAction(myEntity);
        	return false;
        }
    }

	private Intent getShareIntent(String imageIdentifier, String imageTitle)
	{
		File localFile = Services.Images.getCachedImageFile(imageIdentifier);
		if (localFile != null && localFile.exists())
		{
			IntentBuilder builder = IntentBuilder.from(mHelper.getActivity());
			builder.setText(imageTitle);
			builder.setStream(FileProviderUtils.getUriForFile(getContext(), localFile));

			String mimeType = FileUtils2.getMimeType(localFile);
			if (!Strings.hasValue(mimeType))
				mimeType = "image/*";

			builder.setType(mimeType);

			return builder.getIntent();
		}
		else
			return null;
	}

	@SuppressLint("AlwaysShowAction")
	@Override
	public void onCustomCreateOptionsMenu(Menu menu)
	{
		// Add the "Share" action if specified.
		if (mShareActionProvider != null)
		{
			MenuItem shareItem = menu.add("<share>");
			MenuItemCompat.setShowAsAction(shareItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
			MenuItemCompat.setActionProvider(shareItem, mShareActionProvider);
		}
	}
}

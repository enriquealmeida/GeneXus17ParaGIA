package com.genexus.android.core.usercontrols.matrixgrid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class ObservableHorizontalScrollView extends HorizontalScrollView {
	   private ScrollViewListener scrollViewListener=null;

	   public ObservableHorizontalScrollView(Context context) {
	       super(context);
	   }

	   public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
	       super(context, attrs, defStyle);
	   }

	   public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
	       super(context, attrs);
	   }

	   public void setScrollViewListener(ScrollViewListener scrollViewListener) {
	       this.scrollViewListener = scrollViewListener;
	   }

	   @Override
	   protected void onScrollChanged(int x, int y, int oldX, int oldY) {
	       super.onScrollChanged(x, y, oldX, oldY);
	       if (null!=scrollViewListener) {
	           scrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
	       }
	   }
	}

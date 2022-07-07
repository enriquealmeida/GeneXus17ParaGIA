package com.genexus.android.core.usercontrols.matrixgrid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableVerticalScrollView extends ScrollView {

	public ObservableVerticalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	   private ScrollViewListener scrollViewListener=null;

	   public ObservableVerticalScrollView(Context context) {
	       super(context);
	   }

	   public ObservableVerticalScrollView(Context context, AttributeSet attrs, int defStyle) {
	       super(context, attrs, defStyle);
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

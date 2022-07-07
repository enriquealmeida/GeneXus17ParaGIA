package com.genexus.android.layout;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/***
 * This view scroll vertically only if the absolute scroll on Y is greater than the absolute scroll y X
 */
public class CustomScrollView extends NestedScrollView {
	private float xDistance;
	private float yDistance;
	private float lastX;
	private float lastY;
	
	public CustomScrollView(Context context) {
		super(context);
	}
	
	public CustomScrollView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
	
	private ArrayList<View> vScrolls = new ArrayList<>();
	
	@Override
	public boolean onInterceptTouchEvent(@NonNull MotionEvent ev)
	{
		if (isUseless())
			return false;

		float x = ev.getRawX();
		float y = ev.getRawY();
		
		for (View vScroll : vScrolls)
		{
			if (vScroll != null)
			{
				int [] xy = new int[2];
				vScroll.getLocationOnScreen(xy);
				int xv = xy[0];
				int yv = xy[1];
				Rect rect = new Rect(xv, yv, xv + vScroll.getWidth(), yv + vScroll.getHeight());
				if (rect.contains((int)x,(int) y))
					return false;
			}
		}
		
	    switch (ev.getAction())
	    {
	        case MotionEvent.ACTION_DOWN:
	            xDistance = yDistance = 0f;
	            lastX = ev.getX();
	            lastY = ev.getY();
	            break;

	        case MotionEvent.ACTION_MOVE:
	            final float curX = ev.getX();
	            final float curY = ev.getY();
	            xDistance += Math.abs(curX - lastX);
	            yDistance += Math.abs(curY - lastY);
	            lastX = curX;
	            lastY = curY;
	            if (xDistance > yDistance)
	                return false;
	    }
	
	    return super.onInterceptTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(@NonNull MotionEvent ev)
	{
		if (ev.getAction() == MotionEvent.ACTION_DOWN && isUseless())
			return false; // Do not handle the event, disable scroll.
		
		return super.onTouchEvent(ev);
	}
	
	public boolean isUseless()
	{
		// A "useless" ScrollView is one that is (currently) larger than its child. 
		// Hence it doesn't need to scroll at all.
		if (getChildCount() == 1)
		{
			// Disable scrolling if the child doesn't need it (it's smaller than the scrollview itself).
			View child = getChildAt(0);
			if (getHeight() != 0 && child.getHeight() != 0 && getHeight() >= child.getHeight())
				return true;
		}
		
		return false;
	}
	
	public void registerScrollableZone(View child)
	{
		vScrolls.add(child);
	}
}

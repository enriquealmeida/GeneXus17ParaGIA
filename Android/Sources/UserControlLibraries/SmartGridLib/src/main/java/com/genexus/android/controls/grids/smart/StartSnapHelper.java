package com.genexus.android.controls.grids.smart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.TextUtilsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.Locale;

/**
 * Copied from
 * https://blog.mindorks.com/using-snaphelper-in-recyclerview-fc616b6833e8
 */
public class StartSnapHelper extends LinearSnapHelper {

    private OrientationHelper mVerticalHelper;
    private OrientationHelper mHorizontalHelper;
    private int mStartOffset;

	private boolean mIsRtl;

    public void setStartOffset(int offset) {
        mStartOffset = offset;
    }

	@Override
	public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
		mIsRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
			== ViewCompat.LAYOUT_DIRECTION_RTL;

		super.attachToRecyclerView(recyclerView);
	}

	@Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                              @NonNull View targetView) {
        int[] out = new int[2];

        if (layoutManager.canScrollHorizontally()) {
        	if (mIsRtl)
				out[0] = distanceToEnd(targetView, getHorizontalHelper(layoutManager));
			else
				out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
	    return out;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {

        if (layoutManager instanceof LinearLayoutManager) {

            if (layoutManager.canScrollHorizontally()) {
                return getStartView(layoutManager, getHorizontalHelper(layoutManager));
            } else {
                return getStartView(layoutManager, getVerticalHelper(layoutManager));
            }
        }

        return super.findSnapView(layoutManager);
    }

    private int distanceToStart(View targetView, OrientationHelper helper) {
        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding() - mStartOffset;
    }

	private int distanceToEnd(View targetView, OrientationHelper helper) {
		return helper.getDecoratedEnd(targetView) - helper.getEndAfterPadding() - mStartOffset;
	}

    private View getStartView(RecyclerView.LayoutManager layoutManager,
                              OrientationHelper helper) {

        if (layoutManager instanceof LinearLayoutManager) {
            int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

            boolean isLastItem = ((LinearLayoutManager) layoutManager)
                    .findLastCompletelyVisibleItemPosition()
                    == layoutManager.getItemCount() - 1;

            if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
                return null;
            }

            View child = layoutManager.findViewByPosition(firstChild);

			if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2
				&& helper.getDecoratedEnd(child) > 0) {
				return child;
			} else {
				if (((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition()
					== layoutManager.getItemCount() - 1) {
					return null;
				} else if (layoutManager instanceof GridLayoutManager) {
					return layoutManager.findViewByPosition(firstChild + ((GridLayoutManager) layoutManager).getSpanCount());
				} else {
					return layoutManager.findViewByPosition(firstChild + 1);
				}
			}

        }

        return super.findSnapView(layoutManager);
    }

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return mVerticalHelper;
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }
}

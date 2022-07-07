package com.genexus.android.core.controls;

import android.view.View;
import android.widget.ListView;

import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.ui.Coordinator;

public class ListViewHelper extends GridHelper
{
	private final ListView mListView;
    private LoadingIndicatorView mFooterView = null;

    private int mInsideOnMeasure;

    public ListViewHelper(ListView listview, Coordinator coordinator, GridDefinition definition)
    {
    	super(listview, coordinator, definition);
    	mListView = listview;
    }

	public void showFooter(boolean showLoading, String errorMessage, ThemeClassDefinition themeClass)
	{
		if (showLoading)
		{
			removeFooterView(mFooterView);

			mFooterView = new LoadingIndicatorView(mListView.getContext());
			mFooterView.setStyle(LoadingIndicatorView.Style.SMALL);
			// initialize loading indicator animation.
			// TODO: in footer should use a different class
			if (themeClass!=null)
				mFooterView.setThemeClass(themeClass.getThemeAnimationClass());

			addFooterView(mFooterView);
		}
		else if (Services.Strings.hasValue(errorMessage))
		{
			removeFooterView(mFooterView);

			mFooterView = new LoadingIndicatorView(mListView.getContext());
			mFooterView.setText(errorMessage);
			addFooterView(mFooterView);
		}
		else
		{
			removeFooterView(mFooterView);
			// Not needed to release mFooterView, the view is already removed from it parent.
		}
	}

	private void addFooterView(View view)
	{
		if (getDefinition().hasInverseLoad())
			mListView.addHeaderView(view, null, false);
		else
			mListView.addFooterView(view, null, false);
	}

	private void removeFooterView(View view)
	{
		if (getDefinition().hasInverseLoad())
			mListView.removeHeaderView(view);
		else
			mListView.removeFooterView(view);
	}

	public void onScroll()
	{
		if (isFooterViewVisible())
			requestMoreData();
	}

	boolean isFooterViewVisible()
	{
	  	return (mFooterView != null && mFooterView.isShown());
	}

	public void beginOnMeasure()
	{
		mInsideOnMeasure++;
	}

	public void endOnMeasure()
	{
		mInsideOnMeasure--;
	}

	@Override
	protected boolean isMeasuring()
	{
		return (mInsideOnMeasure > 0);
	}
}

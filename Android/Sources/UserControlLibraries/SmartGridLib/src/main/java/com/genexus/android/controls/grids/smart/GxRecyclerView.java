package com.genexus.android.controls.grids.smart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genexus.android.layout.DynamicProperties;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.GxHorizontalSeparator;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.controls.LoadingIndicatorView;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.controls.grids.ISupportsEditableControls;
import com.genexus.android.core.controls.grids.ISupportsMultipleSelection;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.ThemeUtils;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.List;

@SuppressLint("ViewConstructor")
public class GxRecyclerView extends FastScrollRecyclerView implements IGridView, IGxThemeable, IGxControlRuntime, ISupportsMultipleSelection, ISupportsEditableControls {
    private final GridDefinition mDefinition;
    private final GridHelper mHelper;
    private final RecyclerViewAdapter mAdapter;
    private boolean mIsMoreAvailable;
    private ThemeClassDefinition mThemeClass;
	private int mScrollToItemPosition = -1;
	private ActionDefinition mInSelectionForAction;
	private ActionMode mActionMode;

    private static final int VISIBLE_THRESHOLD_ROWS = 5;

    public GxRecyclerView(Context context, Coordinator coordinator, GridDefinition gridDefinition) {
        super(context);
        mDefinition = gridDefinition;
		if (mDefinition != null)
            setControlInfo(mDefinition.getControlInfo(), mDefinition.optBooleanProperty("@inverseLoad"));
        mHelper = createHelper(coordinator, mDefinition);

        updateSeparator(new GxHorizontalSeparator(getContext(), mDefinition)); // after setControlInfo

        mAdapter = new RecyclerViewAdapter(mDefinition, mHelper, this);
        setAdapter(mAdapter);
        setPagination();
		addOnItemTouchListener(this);
    }

    protected GridHelper createHelper(Coordinator coordinator, GridDefinition gridDefinition) {
        return new GridHelper(this, coordinator, gridDefinition, false);
    }

    private void setPagination() {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mIsMoreAvailable)
                    return;

                int totalItemCount = getLayoutManager().getItemCount();
                int lastVisibleItemPosition = findLastVisibleItemPosition();

                if (lastVisibleItemPosition + VISIBLE_THRESHOLD_ROWS * itemsPerRow() >= totalItemCount) {
                    mHelper.requestMoreData();
                }
            }
        });
    }

    protected void setControlInfo(ControlInfo controlInfo, boolean reverseLayout) {
    }

    protected int findLastVisibleItemPosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager)
            return ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
        else if (layoutManager instanceof GridLayoutManager)
            return ((GridLayoutManager)layoutManager).findLastVisibleItemPosition();
        else
            return layoutManager.getItemCount();
    }

    protected int itemsPerRow() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
            return ((GridLayoutManager)layoutManager).getSpanCount();
        else
            return 1;
    }

    @Override
    public void addListener(GridEventsListener listener) {
        mHelper.setListener(listener);
    }

    @Override
    public void update(ViewData data) {
		setFastScrollEnabled(data.getUri());
        mIsMoreAvailable = data.isMoreAvailable();
        int selectedIndex = mAdapter.getSelectedIndex();
        mAdapter.setData(data);
        updateSelection(data);
		// if previous has selected item and now not, keep selected item
		if (selectedIndex >= 0 && mAdapter.getSelectedIndex() == -1 && selectedIndex < mAdapter.getItemCount())  {
			mAdapter.selectIndex(selectedIndex, false);
		}
		showFooter(data.isMoreAvailable(), data.getStatusMessage(), mThemeClass);
    }

	private void setFastScrollEnabled(GxUri uri) {
		if (uri != null &&
			uri.getOrder() != null &&
			uri.getOrder().getEnableAlphaIndexer() &&
			uri.getOrder().getAttributes().size() > 0)
		{
			setFastScrollEnabled(true);
			Integer colorAccent = ThemeUtils.getAndroidThemeColorId(getContext(), com.genexus.android.R.attr.colorAccent);
			if (colorAccent != null)
				setPopupBgColor(colorAccent);
			mAdapter.setSectionProperty(uri.getOrder().getAttributes().get(0).getName());
		} else {
			setFastScrollEnabled(false);
		}
	}

    @Override
    public void setThemeClass(ThemeClassDefinition themeClass) {
        mThemeClass = themeClass;
        applyClass(themeClass);
    }

    @Override
    public ThemeClassDefinition getThemeClass() {
        return mThemeClass;
    }

    @Override
    public void applyClass(ThemeClassDefinition themeClass) {
        mHelper.setThemeClass(themeClass);
        updateSeparator(new GxHorizontalSeparator(getContext(), mDefinition, themeClass));
        mAdapter.notifyDataSetChanged();
    }

	private void updateSeparator(GxHorizontalSeparator separator) {
		if (getItemDecorationCount() > 0)
			removeItemDecorationAt(0);

		if (separator.isVisible()) {
			// Support only horizontal separator in linear layout manager
			LinearLayoutManager linearLayoutManager = Cast.as(LinearLayoutManager.class, getLayoutManager());
			if (linearLayoutManager != null && linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
				if (separator.isDefault())
					addItemDecoration(new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation()));
				else
					addItemDecoration(new GxDividerItemDecoration(separator));
			}
		}
	}

	@Override
	public void setPropertyValue(String name, Expression.Value value) {
    	List<DynamicProperties> dynProps = Cast.as(List.class, value.getValue());
    	if (dynProps != null) {
			for (int i = 0; i < dynProps.size() && i < mAdapter.getItemCount(); i++)
				mHelper.setEntityDynProps(mAdapter.getEntity(i), dynProps.get(i));
		}
	}

	@Override
    public Expression.Value getPropertyValue(String name) {
        if (GridHelper.PROPERTY_SELECTED_INDEX.equalsIgnoreCase(name)) {
            return Expression.Value.newInteger(mAdapter.getSelectedIndex() + 1);
        }
        return null;
    }

    @Override
    public Expression.Value callMethod(String name, List<Expression.Value> parameters) {
        if (GridHelper.METHOD_SELECT.equalsIgnoreCase(name) && parameters.size() == 1) {
            int selectedIndex = parameters.get(0).coerceToInt() - 1;

            // If the item to select has not been loaded in the grid, this method has no effect.
            if (selectedIndex >= mAdapter.getItemCount()) {
                return null;
            }

            smoothScrollToPosition(selectedIndex);
            mAdapter.selectIndex(selectedIndex, true);
        }
        else if (GridHelper.METHOD_DESELECT.equalsIgnoreCase(name) && parameters.size() == 1) {
            int deselectedIndex = parameters.get(0).coerceToInt() - 1;

            // If the item to deselect has not been loaded in the grid, this method has no effect.
            if (deselectedIndex >= mAdapter.getItemCount()) {
                return null;
            }

            mAdapter.deselectIndex(deselectedIndex, true);
        }
        return null;
    }

    public void scrollToPositionAfterLayout(int itemPosition) {
		mScrollToItemPosition = itemPosition;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (mScrollToItemPosition >= 0) {
			// Hack: so the scroll is done after the new GX Layouts are rendered
			// which are in the first call to super.onLayout(), after that a call
			// to scrollToPosition() is needed but it is only made on the next layout
			// hence the second call to super.onLayout()
			scrollToPosition(mScrollToItemPosition);
			mScrollToItemPosition = -1;
			super.onLayout(changed, l, t, r, b);
		}
	}

	@Override
	public void setSelectionMode(boolean enabled, ActionDefinition forAction) {
		// Notify adapter, needed to redraw views (to add or remove checkbox).
		mAdapter.setSelectionMode(enabled);
		if (enabled) {
			mInSelectionForAction = forAction;
			if (forAction != null && mActionMode == null)
				mActionMode = startActionMode(mActionModeCallBack);
		} else {
			mInSelectionForAction = null;
			if (mActionMode != null)
				mActionMode.finish();
			mActionMode = null;
		}
	}

	private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.grid_multi_select_menu, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			if (item.getItemId() == R.id.done) {
				mHelper.runExternalAction(mInSelectionForAction);
				mode.finish();
				return true;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mAdapter.setSelectionMode(false);
			mActionMode = null;
		}
	};

	@Override
	public void setItemSelected(int position, boolean selected) {
		mAdapter.setItemSelected(position, selected);
	}

	private void updateSelection(ViewData data) {
		for (int i = 0; i < data.getEntities().size(); i++)
			mAdapter.setItemSelected(i, data.getEntities().get(i).isSelected());
	}

	public void showFooter(boolean showLoading, String errorMessage, ThemeClassDefinition themeClass) {
		if (showLoading) {
			LoadingIndicatorView view = new LoadingIndicatorView(getContext());
			view.setStyle(LoadingIndicatorView.Style.SMALL);
			// initialize loading indicator animation.
			// TODO: in footer should use a different class
			if (themeClass != null)
				view.setThemeClass(themeClass.getThemeAnimationClass());

			// center
			view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
			LinearLayout layout = new LinearLayout(getContext());
			layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			layout.addView(view);

			mAdapter.setFooterView(layout);
		} else if (Services.Strings.hasValue(errorMessage)) {
			LoadingIndicatorView view = new LoadingIndicatorView(getContext());
			view.setText(errorMessage);
			mAdapter.setFooterView(view);
		} else {
			mAdapter.setFooterView(null); // remove footer view
		}
	}

	@Override
	public void saveEditValues()
	{
		mHelper.saveEditValues();
	}
}

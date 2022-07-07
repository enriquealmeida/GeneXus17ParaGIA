package com.genexus.android.controls.grids.smart;

import static com.genexus.android.core.controls.grids.GridAdapter.SELECTED_INDEX_NONE;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.grids.GridHelper;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {
    private final GridDefinition mDefinition;
    private final GridHelper mHelper;
    private ViewData mData;
    private ArrayList<Entity> mEntities;
    private ArrayList<TableDefinition> mLayout;
    private ArrayList<Integer> mSelectedIndices;
    private View.OnClickListener mClickListener;
    private String mSectionProperty;
    private View mFooterView;

	private boolean mInSelectionMode;
	private static final int CHECKBOX_WIDTH = 70; // dip
	private static final int FOOTER_VIEW_TYPE = -1;

    public RecyclerViewAdapter(GridDefinition definition, GridHelper helper, GxRecyclerView recyclerView) {
        mDefinition = definition;
        mHelper = helper;
        mLayout = new ArrayList<>();
        mSelectedIndices = new ArrayList<>();
        mClickListener = new MyClickListener(recyclerView);
    }

	public void setFooterView(View view) {
		mFooterView = view;
		notifyDataSetChanged();
	}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    private class MyClickListener implements View.OnClickListener {
        private GxRecyclerView mRecyclerView;

        public MyClickListener(GxRecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        @Override
        public void onClick(View v) {
			class ClearSelectionHelper
			{
				private boolean selectionChangedExecuted = false;
				private boolean defaultActionExecuted = false;

				private void check() {
					if (selectionChangedExecuted && defaultActionExecuted &&
						mDefinition.getSelectionType() == GridDefinition.SelectionType.KeepWhileExecuting) {
						Services.Device.runOnUiThread(RecyclerViewAdapter.this::clearSelection);
					}
				}

				public void onSelectionChangedExecuted() {
					selectionChangedExecuted = true;
					check();
				}

				public void onDefaultActionExecuted() {
					defaultActionExecuted = true;
					check();
				}
			}
			final ClearSelectionHelper clearSelectionHelper = new ClearSelectionHelper();

            int itemPosition = mRecyclerView.getChildLayoutPosition(v);
            if (mDefinition.getShowSelector() == GridDefinition.ShowSelector.None) {
				selectIndex(itemPosition, true, clearSelectionHelper::onSelectionChangedExecuted);
				mRecyclerView.scrollToPositionAfterLayout(itemPosition);
			}
            runDefaultAction(itemPosition, clearSelectionHelper::onDefaultActionExecuted);
        }
    }

    private void runDefaultAction(int position, Runnable postAction) {
        Entity item = getEntity(position);
        if (item != null)
            mHelper.runDefaultAction(item, postAction);
    }

    public void setData(ViewData data) {
        mData = data;
        mEntities = mData.getEntities();
        mHelper.setData(data);
        notifyDataSetChanged();
    }

    private boolean isSelected(int position) {
        return mSelectedIndices.contains(position);
    }

    private Entity getSelectedItem() {
    	int selectedIndex = getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < getItemCount())
            return getEntity(selectedIndex);
        else
            return null;
    }

    public Entity getEntity(int position) {
        return (mData != null ? mEntities.get(position) : null);
    }

    public int getSelectedIndex()
    {
    	if (mSelectedIndices.size() == 1)
        	return mSelectedIndices.get(0);
    	else
    		return SELECTED_INDEX_NONE;
    }

	public void selectIndex(int index, boolean fireSelectionChangedEvent)
	{
		selectIndex(index, fireSelectionChangedEvent, () -> {
			if (mDefinition.getSelectionType() == GridDefinition.SelectionType.KeepWhileExecuting)
				Services.Device.runOnUiThread(this::clearSelection);
		});
	}

    private void selectIndex(int index, boolean fireSelectionChangedEvent, Runnable postAction)
    {
        if (mDefinition.getSelectionType() == GridDefinition.SelectionType.NoSelection) {
            // Can't select a grid item in this mode.
            return;
        }

        if (index < 0 || index >= getItemCount())
            return;

        Entity newSelection = getEntity(index);
        Entity previousSelection = getSelectedItem();

        if (!mSelectedIndices.contains(index)) {
        	if (!mInSelectionMode)
        		mSelectedIndices.clear(); // If not multiple selection then clear old selection first.
            mSelectedIndices.add(index);
        }
        else
        {
        	mSelectedIndices.remove((Integer) index);
        }

		if (fireSelectionChangedEvent)
			mHelper.getCoordinator().runControlEvent(mHelper.getGridView(), GridHelper.EVENT_SELECTION_CHANGED, null, postAction);

		// Force a re-layout, if necessary.
        if (mInSelectionMode || mHelper.hasDifferentLayoutWhenSelected(newSelection) || mHelper.hasDifferentLayoutWhenSelected(previousSelection))
            notifyDataSetChanged();
    }

    public void deselectIndex(int index, boolean fireSelectionChangedEvent)
    {
        if (mDefinition.getSelectionType() == GridDefinition.SelectionType.NoSelection)
        {
            // Can't deselect a grid item in this mode.
            return;
        }

        if (index < 0 || index >= getItemCount())
            return;

        boolean selectionChanged;
        Entity previousSelection = getSelectedItem();

        if (mSelectedIndices.contains(index)) {
            mSelectedIndices.remove((Integer) index);
            selectionChanged = true;

            if (fireSelectionChangedEvent)
                mHelper.getCoordinator().runControlEvent(mHelper.getGridView(), GridHelper.EVENT_SELECTION_CHANGED);
        } else {
            selectionChanged = false;
        }

        // Force a re-layout, if necessary.
        if (selectionChanged && (mInSelectionMode || mHelper.hasDifferentLayoutWhenSelected(previousSelection)))
            notifyDataSetChanged();
    }

    private void clearSelection() {
		mSelectedIndices.clear();
		notifyDataSetChanged();
	}

    @Override
    public int getItemViewType(int position) {
    	if (mFooterView != null && position == mEntities.size())
    		return FOOTER_VIEW_TYPE;

        TableDefinition table = mHelper.getLayoutFor(getEntity(position), position % 2 == 0, isSelected(position));
        for (int n = 0; n < mLayout.size(); n++) {
            if (table == mLayout.get(n)) {
                return n;
            }
        }
        mLayout.add(table);
        return mLayout.size() - 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    	if (viewType == FOOTER_VIEW_TYPE) {
    		return new ViewHolder(mFooterView);
		} else {
			TableDefinition table = mLayout.get(viewType);
			View view = mHelper.createNewView(table);
			view.setOnClickListener(mClickListener);
			return new ViewHolder(view);
		}
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
    	if (position >= mEntities.size())
    		return; // Footer

        mHelper.bindView(viewHolder.itemView, getEntity(position), mData, position, mInSelectionMode);
        if (viewHolder.itemView instanceof Checkable)
			((Checkable)viewHolder.itemView).setChecked(isSelected(position));
		mHelper.setGroupHeader(viewHolder.itemView, getEntity(position), position > 0 ? getEntity(position - 1) : null);
    }

    @Override
    public int getItemCount() {
        return (mEntities == null ? 0 : mEntities.size()) + (mFooterView == null ? 0 : 1);
    }

    public void setSelectionMode(boolean value) {
		if (mInSelectionMode != value) {
			mInSelectionMode = value;
			if (value)
				mSelectedIndices.clear(); // Don't clear when leaving selection mode because this list is needed to not lose the selection status in the entities

			// Update table bounds to reserve/free space for checkbox.
			mHelper.setReservedSpace(value ? CHECKBOX_WIDTH : 0);

			// Refresh so that checkbox is shown/hidden.
			notifyDataSetChanged();
		}
	}

	public void setItemSelected(int position, boolean selected) {
		if (selected && !mSelectedIndices.contains(position)) {
			mSelectedIndices.add(position);
			notifyDataSetChanged();
		} else if (!selected && mSelectedIndices.contains(position)) {
			mSelectedIndices.remove((Integer) position);
			notifyDataSetChanged();
		}
	}

	@Override
	public @NonNull String getSectionName(int position) {
		Entity entity = getEntity(position);
		Object value = entity.getProperty(mSectionProperty);
		if (value == null || value.toString().isEmpty())
			return "";
		else
			return value.toString().substring(0, 1);
	}

	public void setSectionProperty(String propertyName) {
		mSectionProperty = propertyName;
	}
}

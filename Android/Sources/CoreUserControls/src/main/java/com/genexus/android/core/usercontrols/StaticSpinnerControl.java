package com.genexus.android.core.usercontrols;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.IValuesFormatter;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.DynamicSpinnerControl;
import com.genexus.android.core.controls.GxTextView;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.SpinnerControl;
import com.genexus.android.core.controls.common.SpinnerItemsAdapter;
import com.genexus.android.core.controls.common.StaticValueItems;
import com.genexus.android.core.controls.common.TextViewUtils;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.UITestingUtils;

public class StaticSpinnerControl extends SpinnerControl implements IGxEdit {
    public static final String NAME = "Combo Box";
	private String mEmptyItemDescription;

    public StaticSpinnerControl(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
        super(context, coordinator, definition);
        initValues();
    }

    private void initValues()
    {
        StaticValueItems items = new StaticValueItems(mDefinition.getDataItem(), mDefinition.getControlInfo());
        mEmptyItemDescription = items.getEmptyItem() == null ? Strings.EMPTY : items.getEmptyItem().Description;
        mAdapter = new SpinnerItemsAdapter(getContext(), items, mThemeClass, mDefinition);
        setAdapter(mAdapter);

        SpinnerInteractionListener listener = new SpinnerInteractionListener(mCoordinator, this);
        setOnTouchListener(listener);
        setOnItemSelectedListener(listener);
    }

    @Override
    public String getGxValue() {
        if (getSelectedItemPosition() == -1)
            return null; // It's important to return null instead of Strings.EMPTY because when called from AdaptersHelper.SaveEditValue() the property is not set
        else
            return mAdapter.getItem(getSelectedItemPosition()).Value;
    }

    @Override
    public void setGxValue(String value) {
        // Only make a selection if there are items in the adapter.
        if (getCount() > 0)
        {
            int currentPosition = getSelectedItemPosition();
            int newPosition = getIndex(value);

            // Reset to the first item if the value was invalid.
            if (newPosition == -1)
                newPosition = 0;

            // Do nothing if there's no change of item position.
            if (newPosition == currentPosition)
                return;

            setSelection(newPosition, false);
        }
    }

    @Override
    public IGxEdit getViewControl()
    {
        return new GxTextView(getContext(), mCoordinator, mDefinition, new Formatter());
    }

    @Override
    public IGxEdit getEditControl()
    {
        return this;
    }

    // Auxliliar classes

    private class Formatter implements IValuesFormatter
    {
        @Override
        public CharSequence format(String value)
        {
            int index = getIndex(value);
            String description = index == -1 ? mEmptyItemDescription : mAdapter.getItem(index).Description;
            return TextViewUtils.toCharSequence(description, mDefinition);
        }

        @Override
        public boolean needsAsync()
        {
            return false;
        }
    }

    private static class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener,
            View.OnTouchListener {
        private final Coordinator mCoordinator;
        private final IGxEdit mView;
        private boolean mWasUserSelected;

        public SpinnerInteractionListener(Coordinator coordinator, IGxEdit view) {
            mCoordinator = coordinator;
            mView = view;
            mWasUserSelected = false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mWasUserSelected = true;
			Services.Log.debug(" StaticSpinnerControl onTouch");
			// previous control remove focus
			DynamicSpinnerControl.removeFocusPrevious(mCoordinator);
			return false;
        }



		@Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			Services.Log.debug(" onItemSelected");
			/* UITest selects value programmatically so ControlValueChanged is not fired
				because mWasUserSelected is false. Read IsTesting value in that case which is only
				set to true by the UITest SelectValue action. */
        	if (mWasUserSelected || UITestingUtils.Companion.isRunningUnderTest()) {
                mCoordinator.onValueChanged(mView, true);
                mWasUserSelected = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}

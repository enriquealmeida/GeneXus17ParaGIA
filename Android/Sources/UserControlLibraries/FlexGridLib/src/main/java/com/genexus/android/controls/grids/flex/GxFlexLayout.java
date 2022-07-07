package com.genexus.android.controls.grids.flex;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.genexus.android.layout.GxLayout;
import com.genexus.android.layout.IGxLayout;
import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.metadata.layout.CellDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeOverrideProperties;
import com.genexus.android.core.controls.GxHorizontalSeparator;
import com.genexus.android.core.controls.ThemedOverrideViewHelper;
import com.genexus.android.core.controls.common.IAdjustContentSizeText;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class GxFlexLayout extends FlexboxLayout implements IGxLayout {
    /***
     *  The TableDefinition for this Table, this is set on constructor or using setTags function
     */
    private TableDefinition mTable;
    /***
     * Coordinator know all associated controls and events for this layout.
     */
    private Coordinator mCoordinator;

    private ThemedOverrideViewHelper mThemedHelper;

    public GxFlexLayout(Context context, TableDefinition table, Coordinator coordinator) {
        super(context);
        mTable = table;
        mCoordinator = coordinator;
        mThemedHelper = new ThemedOverrideViewHelper(this, mTable);
        setFlexProperties();
    }

    private void setFlexProperties()
    {
        switch (mTable.getFlexDirection())
        {
            case "Row":
            default:
                this.setFlexDirection(FlexDirection.ROW);
                break;
            case "Row Reverse":
                this.setFlexDirection(FlexDirection.ROW_REVERSE);
                break;
            case "Column":
                this.setFlexDirection(FlexDirection.COLUMN);
                break;
            case "Column Reverse":
                this.setFlexDirection(FlexDirection.COLUMN_REVERSE);
                break;
        }

        switch (mTable.getFlexWrap())
        {
            case "No Warp":
            default:
                this.setFlexWrap(FlexWrap.NOWRAP);
                break;
            case "Wrap":
                this.setFlexWrap(FlexWrap.WRAP);
                break;
            case "Wrap Reverse":
                this.setFlexWrap(FlexWrap.WRAP_REVERSE);
                break;
        }

        switch (mTable.getJustifyContent())
        {
            case "Flex Start":
            default:
                this.setJustifyContent(JustifyContent.FLEX_START);
                break;
            case "Flex End":
                this.setJustifyContent(JustifyContent.FLEX_END);
                break;
            case "Center":
                this.setJustifyContent(JustifyContent.CENTER);
                break;
            case "Space Between":
                this.setJustifyContent(JustifyContent.SPACE_BETWEEN);
                break;
            case "Space Around":
                this.setJustifyContent(JustifyContent.SPACE_AROUND);
                break;
        }

        switch (mTable.getAlignItems())
        {
            case "Stretch":
            default:
                this.setAlignItems(AlignItems.STRETCH);
                break;
            case "Flex Start":
                this.setAlignItems(AlignItems.FLEX_START);
                break;
            case "Flex End":
                this.setAlignItems(AlignItems.FLEX_END);
                break;
            case "Center":
                this.setAlignItems(AlignItems.CENTER);
                break;
            case "Baseline":
                this.setAlignItems(AlignItems.BASELINE);
                break;
        }

        switch (mTable.getAlignContent())
        {
            case "Stretch":
                this.setAlignContent(AlignContent.STRETCH);
                break;
            case "Flex Start":
                this.setAlignContent(AlignContent.FLEX_START);
                break;
            case "Flex End":
                this.setAlignContent(AlignContent.FLEX_END);
                break;
            case "Center":
                this.setAlignContent(AlignContent.CENTER);
                break;
            case "Space Between":
                this.setAlignContent(AlignContent.SPACE_BETWEEN);
                break;
            case "Space Around":
                this.setAlignContent(AlignContent.SPACE_AROUND);
                break;
        }
    }

    // IGxLayout

    @Override
    public void setLayout(Coordinator coordinator, TableDefinition table) {
        mCoordinator = coordinator;
        mTable = table;
        mThemedHelper.setLayoutItem(table);
    }

    @Override
    public void updateHorizontalSeparators(GxHorizontalSeparator separator) {
        // Particular of GxLayout
    }

    @Override
    public void requestAlignFieldLabels() {
        // TODO: Check later and see if it is needed or we implement the label in another way
    }

    @Override
	public void addChild(View view) {
    	addView(view);
	}

    @Override
    public void setChildLayoutParams(CellDefinition cell, LayoutItemDefinition item, View view) {
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        params.setOrder(cell.getFlexOrder());
        params.setFlexGrow(cell.getFlexGrow());
        params.setFlexShrink(cell.getFlexShrink());

        String alignSelf = cell.getAlignSelf();
        if (alignSelf == null)
            params.setAlignSelf(AlignSelf.AUTO);
        else {
            switch (alignSelf) {
                case "Auto":
                default:
                    params.setAlignSelf(AlignSelf.AUTO);
                    break;
                case "Flex Start":
                    params.setAlignSelf(AlignSelf.FLEX_START);
                    break;
                case "Flex End":
                    params.setAlignSelf(AlignSelf.FLEX_END);
                    break;
                case "Center":
                    params.setAlignSelf(AlignSelf.CENTER);
                    break;
                case "Baseline":
                    params.setAlignSelf(AlignSelf.BASELINE);
                    break;
                case "Stretch":
                    params.setAlignSelf(AlignSelf.STRETCH);
                    break;
            }
        }

        setChildLayoutParamsSize(cell.getFlexWidth(), params, this.getFlexDirection() == FlexDirection.ROW || this.getFlexDirection() == FlexDirection.ROW_REVERSE, true, view instanceof IAdjustContentSizeText);
        setChildLayoutParamsSize(cell.getFlexHeight(), params, this.getFlexDirection() == FlexDirection.COLUMN || this.getFlexDirection() == FlexDirection.COLUMN_REVERSE, false, view instanceof IAdjustContentSizeText);
        if (cell.getMinWidth() != -1)
            params.setMinWidth(cell.getMinWidth());
        if (cell.getMinHeight() != -1)
            params.setMinHeight(cell.getMinHeight());
        if (cell.getMaxWidth() != -1)
            params.setMaxWidth(cell.getMaxWidth());
        if (cell.getMaxHeight() != -1)
            params.setMaxHeight(cell.getMaxHeight());

        view.setLayoutParams(params);
        if (mTable.hasAdjustContentSize() && view instanceof IAdjustContentSizeText)
            ((IAdjustContentSizeText)view).adjustContentSize();
    }

    private void setChildLayoutParamsSize(DimensionValue dimensionValue, LayoutParams params, boolean allowPercentage, boolean setWidth, boolean isTextView) {
        if (dimensionValue != null) {
            if (dimensionValue.Type == DimensionValue.ValueType.PERCENT) {
                if (allowPercentage) {
                    params.setFlexBasisPercent(dimensionValue.Value / 100f);
                }
            }
            else {
                int value = (int)dimensionValue.Value;
                if (mTable.hasAdjustContentSize() && value == MATCH_PARENT && isTextView)
                    value = WRAP_CONTENT;
                if (setWidth)
                    params.width = value;
                else
                    params.height = value;
            }
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED && !mTable.hasAdjustContentSize()) {
            // Workaround because it doesn't calculate the height correctly when nowrap and baseline
            if (getFlexWrap() == FlexWrap.NOWRAP && getAlignItems() == AlignItems.BASELINE) {
                int max = getMeasuredHeight();
                for (int n = 0; n < getChildCount(); n++) {
                    View child = getChildAt(n);
                    if (child.getMeasuredHeight() > max)
                        max = child.getMeasuredHeight();
                }
                setMeasuredDimension(getMeasuredWidth(), max);
            }

            // Workaround because this.height = WRAP_CONTENT is not enough if the align is center or flex end
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if (height > getMeasuredHeight())
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
    }

    @Override
    public void updateChildLayoutParams(CellDefinition cell, LayoutItemDefinition item, View view) { }

    @Override
    public void updateSelfLayoutParams() {
        GxLayout.LayoutParams lp = Cast.as(GxLayout.LayoutParams.class, getLayoutParams());
        if (lp != null && mTable.hasAdjustContentSize()) {
            lp.width = WRAP_CONTENT;
            lp.height = WRAP_CONTENT;
            lp.cell.autoHeight = true; // so GxLayout don't set a fixed height in layoutCell()
        }
    }

    @Override
    public void setOverride(String propertyName, String propertyValue) {
        mThemedHelper.setOverride(propertyName, propertyValue);
    }

    @Override
    public ThemeOverrideProperties getThemeOverrideProperties() {
        return mThemedHelper.getThemeOverrideProperties();
    }

    @Override
    public void setThemeClass(ThemeClassDefinition themeClass) {
        mThemedHelper.setThemeClass(themeClass);
    }

    @Override
    public ThemeClassDefinition getThemeClass() {
        return mThemedHelper.getThemeClass();
    }

    @Override
    public void applyClass(ThemeClassDefinition themeClass) {
        mThemedHelper.applyClass(themeClass);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params)
    {
        if (mTable.hasAdjustContentSize()) {
            params.width = WRAP_CONTENT;
            params.height = WRAP_CONTENT;
        }
        mThemedHelper.setLayoutParams(params);
        super.setLayoutParams(params);
    }
}

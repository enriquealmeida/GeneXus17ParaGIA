package com.genexus.android.controls.grids.flex;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.genexus.android.layout.GxLayout;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.IGridView;
import com.genexus.android.core.controls.common.IAdjustContentSizeText;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.controls.grids.GridItemViewInfo;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.controls.grids.smart.GxRecyclerView;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class FlexGridView extends GxRecyclerView implements IGridView, IGxControlRuntime {
    public static final String NAME = "SDFlexGrid";

    public FlexGridView(Context context, Coordinator coordinator, GridDefinition def) {
        super(context, coordinator, def);
    }

    @Override
    protected void setControlInfo(ControlInfo controlInfo, boolean reverseLayout) {
        FlexboxLayoutManager layoutManager = new GxFlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(getFlexDirection(controlInfo));
        layoutManager.setFlexWrap(getFlexWrap(controlInfo));
        layoutManager.setJustifyContent(getJustifyContent(controlInfo));
        layoutManager.setAlignItems(getAlignItems(controlInfo));
        layoutManager.setAlignContent(getAlignContent(controlInfo));
        setLayoutManager(layoutManager);
    }

    private static class FlexGridHelper extends GridHelper
    {
        public FlexGridHelper(View grid, Coordinator coordinator, GridDefinition definition, boolean supportItemViewReuse) {
            super(grid, coordinator, definition, supportItemViewReuse);
        }

        @Override
        public void bindView(View itemView, Entity item, ViewData data, int position, boolean inSelectionMode) {
            super.bindView(itemView, item, data, position, inSelectionMode);
            GridItemViewInfo itemViewInfo = GridItemViewInfo.fromView(itemView);
            ViewGroup table = itemViewInfo.getHolder();
            if (table instanceof GxLayout) {
                table.setMinimumHeight(0); // To undo the set on LayoutBuilder.expandLayout() since the final height can be lower than the desired height
                for (int n = 0; n < table.getChildCount(); n++) {
                    View child = table.getChildAt(n);
                    if (child instanceof IAdjustContentSizeText) {
                        GxLayout.LayoutParams params = (GxLayout.LayoutParams)child.getLayoutParams();
                        params.width = WRAP_CONTENT;
                        params.height = WRAP_CONTENT;
                        params.cell.autoHeight = true; // To get height = WRAP_CONTENT in GxLayout.layoutCell for DataBoundControl
                        child.setLayoutParams(params);
                        ((IAdjustContentSizeText) child).adjustContentSize();
                    }
                }
            }
        }
    }

    @Override
    protected GridHelper createHelper(Coordinator coordinator, GridDefinition gridDefinition) {
        return new FlexGridHelper(this, coordinator, gridDefinition, false);
    }

    private int getFlexDirection(ControlInfo controlInfo) {
        String direction = controlInfo.optStringProperty("@SDFlexGridflexDirection");
        return getFlexDirection(direction);
    }

    private int getFlexDirection(String direction) {
        if (direction.equals("row"))
            return FlexDirection.ROW;
        else if (direction.equals("row-reverse"))
            return FlexDirection.ROW_REVERSE;
        else if (direction.equals("column"))
            return FlexDirection.COLUMN;
        else if (direction.equals("column-reverse"))
            return FlexDirection.COLUMN_REVERSE;
        else
            return FlexDirection.ROW;
    }

    private String getFlexDirectionProperty(int direction) {
        if (direction == FlexDirection.ROW)
            return "row";
        else if (direction == FlexDirection.ROW_REVERSE)
            return "row-reverse";
        else if (direction == FlexDirection.COLUMN)
            return "column";
        else if (direction == FlexDirection.COLUMN_REVERSE)
            return "column-reverse";
        else
            return "row";
    }

    private int getFlexWrap(ControlInfo controlInfo) {
        String wrap = controlInfo.optStringProperty("@SDFlexGridflexWrap");
        return getFlexWrap(wrap);
    }

    private int getFlexWrap(String wrap) {
        if (wrap.equals("nowrap"))
            return FlexWrap.NOWRAP;
        else if (wrap.equals("wrap"))
            return FlexWrap.WRAP;
        else if (wrap.equals("wrap-reverse"))
            return FlexWrap.WRAP; // Value wrap-reverse in the FlexboxLayoutManager is not supported. (Exception is thrown)
        else
            return FlexWrap.NOWRAP;
    }

    private String getFlexWrapProperty(int wrap) {
        if (wrap == FlexWrap.NOWRAP)
            return "nowrap";
        else if (wrap == FlexWrap.WRAP)
            return "wrap";
        else if (wrap == FlexWrap.WRAP_REVERSE)
            return "wrap-reverse";
        else
            return "nowrap";
    }

    private int getJustifyContent(ControlInfo controlInfo) {
        String justifyContent = controlInfo.optStringProperty("@SDFlexGridjustifyContent");
        return getJustifyContent(justifyContent);
    }

    private int getJustifyContent(String justifyContent) {
        if (justifyContent.equals("flex-start"))
            return JustifyContent.FLEX_START;
        else if (justifyContent.equals("flex-end"))
            return JustifyContent.FLEX_END;
        else if (justifyContent.equals("center"))
            return JustifyContent.CENTER;
        else if (justifyContent.equals("space-between"))
            return JustifyContent.SPACE_BETWEEN;
        else if (justifyContent.equals("space-around"))
            return JustifyContent.SPACE_AROUND;
        else
            return JustifyContent.FLEX_START;
    }

    private String getJustifyContentProperty(int justifyContent) {
        if (justifyContent == JustifyContent.FLEX_START)
            return "flex-start";
        else if (justifyContent == JustifyContent.FLEX_END)
            return "flex-end";
        else if (justifyContent == JustifyContent.CENTER)
            return "center";
        else if (justifyContent == JustifyContent.SPACE_BETWEEN)
            return "space-between";
        else if (justifyContent == JustifyContent.SPACE_AROUND)
            return "space-around";
        else
            return "flex-start";
    }

    private int getAlignItems(ControlInfo controlInfo) {
        String alignItems = controlInfo.optStringProperty("@SDFlexGridalignItems");
        return getAlignItems(alignItems);
    }

    private int getAlignItems(String alignItems) {
        if (alignItems.equals("stretch"))
            return AlignItems.STRETCH;
        else if (alignItems.equals("flex-start"))
            return AlignItems.FLEX_START;
        else if (alignItems.equals("flex-end"))
            return AlignItems.FLEX_END;
        else if (alignItems.equals("center"))
            return AlignItems.CENTER;
        else if (alignItems.equals("baseline"))
            return AlignItems.BASELINE;
        else
            return AlignItems.STRETCH;
    }

    private String getAlignItemsProperty(int alignItems) {
        if (alignItems == AlignItems.STRETCH)
            return "stretch";
        else if (alignItems == AlignItems.FLEX_START)
            return "flex-start";
        else if (alignItems == AlignItems.FLEX_END)
            return "flex-end";
        else if (alignItems == AlignItems.CENTER)
            return "center";
        else if (alignItems == AlignItems.BASELINE)
            return "baseline";
        else
            return "stretch";
    }

    private int getAlignContent(ControlInfo controlInfo) {
        String alignContent = controlInfo.optStringProperty("@SDFlexGridalignContent");
        return getAlignContent(alignContent);
    }

    private int getAlignContent(String alignContent) {
        if (alignContent.equals("stretch"))
            return AlignContent.STRETCH;
        else if (alignContent.equals("flex-start"))
            return AlignContent.FLEX_START;
        else if (alignContent.equals("flex-end"))
            return AlignContent.FLEX_END;
        else if (alignContent.equals("center"))
            return AlignContent.CENTER;
        else if (alignContent.equals("space-between"))
            return AlignContent.SPACE_BETWEEN;
        else if (alignContent.equals("space-around"))
            return AlignContent.SPACE_AROUND;
        else
            return AlignContent.STRETCH;
    }

    private String getAlignContentProperty(int alignContent) {
        if (alignContent == AlignContent.STRETCH)
            return "stretch";
        else if (alignContent == AlignContent.FLEX_START)
            return "flex-start";
        else if (alignContent == AlignContent.FLEX_END)
            return "flex-end";
        else if (alignContent == AlignContent.CENTER)
            return "center";
        else if (alignContent == AlignContent.SPACE_BETWEEN)
            return "space-between";
        else if (alignContent == AlignContent.SPACE_AROUND)
            return "space-around";
        else
            return "stretch";
    }

    @Override
    protected int findLastVisibleItemPosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof FlexboxLayoutManager)
            return ((FlexboxLayoutManager)layoutManager).findLastVisibleItemPosition();
        else
            return 0;
    }

    @Override
    protected int itemsPerRow() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof FlexboxLayoutManager && layoutManager.getItemCount() > 0) {
            int lines = ((FlexboxLayoutManager) layoutManager).getFlexLines().size();
            return (int)Math.ceil((double)layoutManager.getItemCount() / lines);
        }
        else
            return 1;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // WA for a problem when doing a layout for the second time.
        // mCoordinate = mOrientationHelper.getDecoratedStart(anchor) in FlexLayoutManager returns the left position and later is used as top.
        // This caused that items had wrong positions.
        // So the positions are reset for it to make the correct calculation.
        for (int n = 0; n < getChildCount(); n++) {
            View child = getChildAt(n);
            child.setLeft(0);
            child.setTop(0);
        }
        super.onLayout(changed, l, t, r, b);
    }

    private static final String PROPERTY_DIRECTION = "FlexDirection";
    private static final String PROPERTY_WRAP = "FlexWrap";
    private static final String PROPERTY_JUSTIFY_CONTENT = "JustifyContent";
    private static final String PROPERTY_ALIGN_ITEMS = "AlignItems";
    private static final String PROPERTY_ALIGN_CONTENT = "AlignContent";

    @Override
    public void setPropertyValue(String name, Value value) {
        FlexboxLayoutManager layoutManager = Cast.as(FlexboxLayoutManager.class, getLayoutManager());
        if (layoutManager != null) {
            if (name.equalsIgnoreCase(PROPERTY_DIRECTION)) {
                layoutManager.setFlexDirection(getFlexDirection(value.coerceToString()));
            } else if (name.equalsIgnoreCase(PROPERTY_WRAP)) {
                layoutManager.setFlexWrap(getFlexWrap(value.coerceToString()));
            } else if (name.equalsIgnoreCase(PROPERTY_JUSTIFY_CONTENT)) {
                layoutManager.setJustifyContent(getJustifyContent(value.coerceToString()));
            } else if (name.equalsIgnoreCase(PROPERTY_ALIGN_ITEMS)) {
                layoutManager.setAlignItems(getAlignItems(value.coerceToString()));
            } else if (name.equalsIgnoreCase(PROPERTY_ALIGN_CONTENT)) {
                layoutManager.setAlignContent(getAlignContent(value.coerceToString()));
            } else {
                super.setPropertyValue(name, value);
            }
        }
    }

    @Override
    public Value getPropertyValue(String name) {
        FlexboxLayoutManager layoutManager = Cast.as(FlexboxLayoutManager.class, getLayoutManager());
        if (layoutManager != null) {
            if (name.equalsIgnoreCase(PROPERTY_DIRECTION)) {
                return Value.newString(getFlexDirectionProperty(layoutManager.getFlexDirection()));
            } else if (name.equalsIgnoreCase(PROPERTY_WRAP)) {
                return Value.newString(getFlexWrapProperty(layoutManager.getFlexWrap()));
            } else if (name.equalsIgnoreCase(PROPERTY_JUSTIFY_CONTENT)) {
                return Value.newString(getJustifyContentProperty(layoutManager.getJustifyContent()));
            } else if (name.equalsIgnoreCase(PROPERTY_ALIGN_ITEMS)) {
                return Value.newString(getAlignItemsProperty(layoutManager.getAlignItems()));
            } else if (name.equalsIgnoreCase(PROPERTY_ALIGN_CONTENT)) {
                return Value.newString(getAlignContentProperty(layoutManager.getAlignContent()));
            } else {
                return super.getPropertyValue(name);
            }
        }
        return null;
    }

    @Override
	public void showFooter(boolean showLoading, String errorMessage, ThemeClassDefinition themeClass) {
		// Don't support footer because we can't add a view that hasn't a LayoutParams that is not FlexItem
	}
}

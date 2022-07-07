package com.genexus.android.core.base.metadata.layout;

import java.util.List;

import com.genexus.android.core.base.metadata.DimensionValue;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MathUtils;

public class CellDefinition extends LayoutItemDefinition {
    private static final long serialVersionUID = 1L;

    // All intermediate and final fields are in pixels
    private int x;
    private int y;
    private int z;
    private float relativex; // [0..1]
    private float relativey; // [0..1]
    private int width;
    private int height;
    private float relativewidth; // [0..1]
    private float relativeheight; // [0..1]

    private float mCalculatedAbsoluteX;
    private float mCalculatedAbsoluteY;
    private float mCalculatedAbsoluteWidth;
    private float mCalculatedAbsoluteHeight;
    private Integer mRow;

    // Flex
    private int mFlexOrder;
    private float mFlexGrow;
    private float mFlexShrink;
    private String mAlignSelf;
    private DimensionValue mFlexWidth;
    private DimensionValue mFlexHeight;
    private int mMinWidth;
    private int mMinHeight;
    private int mMaxWidth;
    private int mMaxHeight;

    public CellDefinition(LayoutDefinition parent, LayoutItemDefinition itemParent) {
        super(parent, itemParent);
    }

    @Override
    public RowDefinition getParent() {
        return (RowDefinition) super.getParent();
    }

    void calculateBounds(float containerWidth, float containerHeight) {
        mCalculatedAbsoluteX = (relativex * containerWidth) + x;
        mCalculatedAbsoluteY = (relativey * containerHeight) + y;
        mCalculatedAbsoluteWidth = (relativewidth * containerWidth) + width;
        mCalculatedAbsoluteHeight = (relativeheight * containerHeight) + height;

        if (mCalculatedAbsoluteWidth < 0)
            mCalculatedAbsoluteWidth = 0;
        if (mCalculatedAbsoluteHeight < 0)
            mCalculatedAbsoluteHeight = 0;

        List<LayoutItemDefinition> vector = getChildItems();
        for (LayoutItemDefinition child : vector) {
            if (child instanceof ILayoutContainer)
                ((ILayoutContainer) child).calculateBounds(mCalculatedAbsoluteWidth, mCalculatedAbsoluteHeight);
            if (child instanceof GridDefinition)
                ((GridDefinition) child).calculateBounds(mCalculatedAbsoluteWidth, mCalculatedAbsoluteHeight);
            if (child instanceof TabControlDefinition)
                ((TabControlDefinition) child).calculateBounds(mCalculatedAbsoluteWidth, mCalculatedAbsoluteHeight);
        }
    }

    @Override
    public void readData(INodeObject cellNode) {
        super.readData(cellNode);

        // Set Bounds, calculate is differed to runtime
        setBounds(cellNode);
    }

    private void setBounds(INodeObject cellNode) {
        INodeObject node = cellNode.getNode("CellBounds");

		/* "@x": "0",
         "@y": "0",
         "@z": "0",
         "@xRelative": "0",
         "@yRelative": "0",
         "@width": "0",
         "@height": "0",
         "@widthRelative": "100",
         "@heightRelative": "100"*/
        if (node != null) {
            // Convert from dips to pixels position and dimensions
            x = Services.Device.dipsToPixels((int) Float.parseFloat(node.optString("@x")));
            y = Services.Device.dipsToPixels((int) Float.parseFloat(node.optString("@y")));
            z = Services.Strings.tryParseInt(node.optString("@z"), 0);
            width = Services.Device.dipsToPixels((int) Float.parseFloat(node.optString("@width")));
            height = Services.Device.dipsToPixels((int) Float.parseFloat(node.optString("@height")));

            // Percentages so no conversion to pixels!
            relativex = Float.parseFloat(node.optString("@xRelative")) / 100f;
            relativey = Float.parseFloat(node.optString("@yRelative")) / 100f;
            relativeheight = Float.parseFloat(node.optString("@heightRelative")) / 100f;
            relativewidth = Float.parseFloat(node.optString("@widthRelative")) / 100f;
        } else {
            x = 0;
            y = 0;
            z = 0;
            relativex = 0;
            relativey = 0;
            width = 0;
            height = 0;
            relativeheight = 1f;
            relativewidth = 1f;

            mFlexOrder = cellNode.optInt("@flexOrder");
            mFlexGrow = Float.parseFloat(cellNode.optString("@flexGrow"));
            mFlexShrink = Float.parseFloat(cellNode.optString("@flexShrink"));
            mAlignSelf = cellNode.optString("@alignSelf");
            mFlexWidth = DimensionValue.parse(cellNode.optString("@flexWidth"));
            mFlexHeight = DimensionValue.parse(cellNode.optString("@flexHeight"));
            mMinWidth = getWidthHeight(cellNode, "@minWidth");
            mMinHeight = getWidthHeight(cellNode, "@minHeight");
            mMaxWidth = getWidthHeight(cellNode, "@maxWidth");
            mMaxHeight = getWidthHeight(cellNode, "@maxHeight");

            // Workaround because it doesn't work wrap reverse row with only minHeight
            if (mFlexWidth == null && mMinWidth == mMaxWidth && mMinWidth != -1)
                mFlexWidth = DimensionValue.pixels(mMinWidth);
            if (mFlexHeight == null && mMinHeight == mMaxHeight && mMinHeight != -1)
                mFlexHeight = DimensionValue.pixels(mMinHeight);
        }

        RowDefinition parent = getParent();
        parent.Cells.add(this);
    }

    private static int getWidthHeight(INodeObject node, String name) {
        try {
            return Services.Device.dipsToPixels(Integer.parseInt(node.optString(name)));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public LayoutItemDefinition getContent() {
        if (getChildItems().size() != 0)
            return getChildItems().get(0);
        else
            return null;
    }

    public int getRowSpan() {
        return optIntProperty("@rowSpan");
    }

    public Size getAbsoluteSize() {
        return new Size(getAbsoluteWidth(), getAbsoluteHeight());
    }

    public int getRow() {
        if (mRow == null)
            mRow = getParent().getIndex();

        return mRow;
    }

    public int getAbsoluteX() {
        return MathUtils.round(mCalculatedAbsoluteX);
    }

    public int getAbsoluteY() {
        return MathUtils.round(mCalculatedAbsoluteY);
    }

    public int getAbsoluteWidth() {
        // Reason for this strange calculation: a double rounding may produce differences.
        // For example, say cell1 has x = 1.4 and width = 1.4. This means that cell2 has x = 2.8
        // Rounding each separately ends up with x = 1 and width = 1, while cell2 has x = 3 and there is a blank pixel there.
        // With this calculation, right = round(2.8) = 3, which means that width = 2.
        int right = MathUtils.round(mCalculatedAbsoluteX + mCalculatedAbsoluteWidth);
        return right - getAbsoluteX();
    }

    public int getAbsoluteHeight() {
        // See above method for explanation.
        int bottom = MathUtils.round(mCalculatedAbsoluteY + mCalculatedAbsoluteHeight);
        return bottom - getAbsoluteY();
    }

    public int getZOrder() {
        return z;
    }

    public int getFlexOrder() {
        return mFlexOrder;
    }

    public float getFlexGrow() {
        return mFlexGrow;
    }

    public float getFlexShrink() {
        return mFlexShrink;
    }

    public String getAlignSelf() {
        return mAlignSelf;
    }

    public DimensionValue getFlexWidth() {
        return mFlexWidth;
    }

    public DimensionValue getFlexHeight() {
        return mFlexHeight;
    }

    public int getMinWidth() {
        return mMinWidth;
    }

    public int getMinHeight() {
        return mMinHeight;
    }

    public int getMaxWidth() {
        return mMaxWidth;
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }
}

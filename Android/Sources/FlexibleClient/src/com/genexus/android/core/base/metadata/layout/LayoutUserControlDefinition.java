package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.serialization.INodeObject;

public class LayoutUserControlDefinition extends LayoutItemDefinition {


    public LayoutUserControlDefinition(LayoutDefinition layout, LayoutItemDefinition itemParent)
    {
        super(layout, itemParent);
    }

    @Override
    public void readData(INodeObject node)
    {
        super.readData(node);
    }

}

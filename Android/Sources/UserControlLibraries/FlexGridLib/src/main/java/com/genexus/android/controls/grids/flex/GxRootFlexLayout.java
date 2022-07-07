package com.genexus.android.controls.grids.flex;

import android.content.Context;
import android.view.View;

import com.genexus.android.layout.IGxRootLayout;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.ui.Coordinator;

public class GxRootFlexLayout extends GxFlexLayout implements IGxRootLayout
{
    public GxRootFlexLayout(Context context, LayoutDefinition layout, Coordinator coordinator)
    {
        super(context, layout.getTable(), coordinator);
    }

    // IGxRootLayout

    @Override
    public View getFirstChild() {
        return null;
    }

    @Override
    public void afterExpandLayout() {

    }
}

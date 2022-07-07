package com.genexus.android.layout;

import android.view.View;

import com.genexus.android.core.base.controls.IGxOverrideThemeable;
import com.genexus.android.core.base.metadata.layout.CellDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.controls.GxHorizontalSeparator;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.ui.Coordinator;

public interface IGxLayout extends IGxThemeable, IGxOverrideThemeable {
    void setLayout(Coordinator coordinator, TableDefinition layout);
    void updateHorizontalSeparators(GxHorizontalSeparator separator);
    void requestAlignFieldLabels();
    void addChild(View view);
    void setChildLayoutParams(CellDefinition cell, LayoutItemDefinition item, View view);
    void updateChildLayoutParams(CellDefinition cell, LayoutItemDefinition item, View view);
    void updateSelfLayoutParams();
}

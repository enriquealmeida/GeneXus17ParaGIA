package com.genexus.android.controls.wheels;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.controls.common.StaticValueItems;

public class GxWheelValuesControl extends GxWheelEnumControl {

    public GxWheelValuesControl(DataItem dataItem, ControlInfo info) {
        // Just use the new parsing mechanism for now, but refactor all this ASAP.
        load(new StaticValueItems(dataItem, info));
    }
}

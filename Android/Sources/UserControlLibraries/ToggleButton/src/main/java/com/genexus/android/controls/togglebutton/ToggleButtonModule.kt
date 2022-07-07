package com.genexus.android.controls.togglebutton

import android.content.Context
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.core.usercontrols.UcFactory
import com.genexus.android.core.usercontrols.UserControlDefinition

class ToggleButtonModule : GenexusModule {
    override fun initialize(context: Context?) {
        val toggleButtonUserControl = UserControlDefinition(
            ToggleButtonControl.NAME,
            ToggleButtonControl::class.java
        )
        UcFactory.addControl(toggleButtonUserControl)
    }
}

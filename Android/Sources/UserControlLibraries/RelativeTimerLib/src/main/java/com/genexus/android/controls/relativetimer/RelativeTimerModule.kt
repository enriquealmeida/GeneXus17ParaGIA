package com.genexus.android.controls.relativetimer

import android.content.Context
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.core.usercontrols.UcFactory
import com.genexus.android.core.usercontrols.UserControlDefinition
import net.time4j.android.ApplicationStarter

class RelativeTimerModule : GenexusModule {
    override fun initialize(context: Context?) {
        val basicUserControl = UserControlDefinition(
            RelativeTimerControl.NAME,
            RelativeTimerControl::class.java
        )
        UcFactory.addControl(basicUserControl)
        ApplicationStarter.initialize(context, true)
    }
}

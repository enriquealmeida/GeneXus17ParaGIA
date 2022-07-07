package com.genexus.android.miniapps

import com.genexus.android.core.activities.ActivityHelper
import com.genexus.android.core.base.services.Services

object MiniAppAPIOffline {
    @JvmStatic
    fun exit() {
        Services.SuperApps.exit(ActivityHelper.getCurrentActivity())
    }
}

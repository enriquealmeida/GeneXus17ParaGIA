package com.genexus.android.remoteconfig.firebase

import android.content.Context
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.remoteconfig.RemoteConfig

class FirebaseRemoteConfigModule : GenexusModule {

    override fun initialize(context: Context?) {
        RemoteConfig.provider = FirebaseRemoteConfigProvider()
    }
}

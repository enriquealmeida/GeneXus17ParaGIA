package com.genexus.android.controls.filepicker

import android.content.Context
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.core.usercontrols.UcFactory
import com.genexus.android.core.usercontrols.UserControlDefinition

class FilePickerModule : GenexusModule {
    override fun initialize(context: Context?) {
        ExternalApiFactory.addApi(ExternalApiDefinition(FilePickerAPI.OBJECT_NAME, FilePickerAPI::class.java))
        UcFactory.addControl(UserControlDefinition(FileControl.NAME, FileControl::class.java))
    }
}

package com.genexus.android.core.base.services

import com.genexus.android.core.providers.EntityDataProvider
import com.genexus.android.core.services.EntityService

interface IEntityProvider {
    fun getEntityServiceClass(): Class<out EntityService>?
    fun getProvider(): EntityDataProvider?
}

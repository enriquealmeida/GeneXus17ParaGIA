package com.genexus.android.core.externalapi.superapps

import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.externalapi.ExternalApi

abstract class MiniAppExternalApi(action: ApiAction?) : ExternalApi(action), IMethodRestrictions {
    // Empty collection means all API methods are blocked
    override fun restrictedMethods(): List<String> {
        return listOf()
    }
}

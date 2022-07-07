package com.genexus.android.core.externalobjects

import com.genexus.android.ApiAuthorizationStatus
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalobjects.permissions.Permissions

object PermissionsAPIOffline {
    fun getStatus(permission: String): Int {
        return ApiAuthorizationStatus.getStatus(Services.Application.appContext, Permissions(permission).toTypedArray())
    }
}

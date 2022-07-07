package com.genexus.android.core.base.services

import android.app.Activity
import com.genexus.android.core.actions.Action
import com.genexus.android.core.actions.UIContext

interface IExtensions {
    val externalLoginManager: IExternalLoginManager
}

interface IExternalLoginManager {
    fun addProvider(provider: ILoginProvider)
    fun removeProvider(provider: ILoginProvider)
    fun getProvider(name: String): ILoginProvider?
}

interface ILoginProvider {
    val name: String
    fun login(context: UIContext, activity: Activity, action: Action)
    fun loginToken(): String?
}

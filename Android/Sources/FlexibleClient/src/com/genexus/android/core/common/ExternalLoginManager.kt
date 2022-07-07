package com.genexus.android.core.common

import com.genexus.android.core.base.services.IExternalLoginManager
import com.genexus.android.core.base.services.ILoginProvider

class ExternalLoginManager : IExternalLoginManager {
    val providerMap = HashMap<String, ILoginProvider>()

    override fun addProvider(provider: ILoginProvider) {
        providerMap.set(provider.name, provider)
    }

    override fun removeProvider(provider: ILoginProvider) {
        providerMap.remove(provider.name)
    }

    override fun getProvider(name: String): ILoginProvider? {
        return providerMap.get(name)
    }
}

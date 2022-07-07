package com.genexus.android.core.common

import com.genexus.android.core.base.services.IExtensions

class ExtensionsManager : IExtensions {
    override val externalLoginManager = ExternalLoginManager()
}

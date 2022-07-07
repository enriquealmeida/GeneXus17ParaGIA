package com.genexus.android.live_editing.commands

import android.app.Activity
import com.genexus.android.core.base.metadata.loader.MetadataParser
import com.genexus.android.core.base.metadata.theme.ThemeDefinition
import com.genexus.android.core.base.serialization.INodeObject
import com.genexus.android.core.base.services.Services
import com.genexus.android.live_editing.support.ILiveEditingImageManager
import com.google.gson.annotations.SerializedName

abstract class CommandDesignSystemChanged(designSystemName: String, newMetadata: INodeObject) : IServerCommand {
    @SerializedName("ObjName")
    private val designSystemName: String

    @SerializedName("Data")
    private val newMetadata: INodeObject

    init {
        this.designSystemName = designSystemName
        this.newMetadata = newMetadata
    }

    protected abstract fun clean(theme: ThemeDefinition)

    override fun execute(liveEditingImageManager: ILiveEditingImageManager?): Boolean {
        val currentTheme = Services.Themes.currentTheme

        val currentDesignSystem = if (MetadataHelper.checkCurrentThemeName(currentTheme, designSystemName))
            currentTheme
        else {
            currentTheme.clearCache()
            Services.Application.definition.getTheme(designSystemName)
        }

        if (currentDesignSystem == null)
            return false // not currently used design system

        clean(currentDesignSystem)

        val newDesignSystem = MetadataParser.readOneTheme(Services.Application.appContext, designSystemName, newMetadata, true)
        newDesignSystem.mergeTo(currentDesignSystem) // Modify current theme instance so we don't have to change the reference in every place it is used
        return true
    }

    override fun applyChanges(activity: Activity?) {
        Services.Themes.applyCurrentThemeForced(activity)
    }

    override fun shouldInspectUIAfterApplyingChanges() = true
}

package com.genexus.android.core.application

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View
import androidx.annotation.WorkerThread
import com.artech.base.services.AndroidContext
import com.genexus.android.ContextImpl
import com.genexus.android.core.actions.UIContext
import com.genexus.android.core.activities.AppLinksIntentHandler
import com.genexus.android.core.activities.IntentHandlers
import com.genexus.android.core.adapters.AdaptersHelper
import com.genexus.android.core.base.metadata.ApplicationDefinition
import com.genexus.android.core.base.metadata.GenexusApplication
import com.genexus.android.core.base.metadata.enums.Connectivity
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.providers.IApplicationServer
import com.genexus.android.core.base.services.ClientStorage
import com.genexus.android.core.base.services.IApplication
import com.genexus.android.core.base.services.IEntityProvider
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.PlatformHelper
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.common.ImageLoader
import com.genexus.android.core.common.PreferencesHelper
import com.genexus.android.core.common.ServicesLinker
import com.genexus.android.core.controls.DynamicSpinnerControl
import com.genexus.android.core.controls.GxCheckBox
import com.genexus.android.core.controls.SearchBox
import com.genexus.android.core.controls.ads.SDAdsViewControl
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.core.layers.ApplicationServers
import com.genexus.android.core.providers.DatabaseStorage
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.usercontrols.TableLayoutFactory
import com.genexus.android.core.usercontrols.UcFactory
import com.genexus.android.core.usercontrols.UserControlDefinition
import com.genexus.specific.android.Connect

class ApplicationHelper(private val application: Application, private val entityProvider: IEntityProvider) : IApplication {
    private var currentGenexusApp: GenexusApplication? = null
    private var currentMiniApp: MiniApp? = null

    private var modulesInitialized = false
    private var liveEditingModeEnabled = false
    private val modules: MutableSet<GenexusModule> = LinkedHashSet() // respect order to call initialize

    private var appsLinksProtocol = Strings.EMPTY

    private val servers = ApplicationServers()
    private val lifecycle = ApplicationLifecycle(this)
    private val externalApiFactory = ExternalApiFactory()
    private val tableLayoutFactory = TableLayoutFactory()
    private lateinit var servicesLinker: ServicesLinker
    private lateinit var prevConfig: Configuration

    override fun onCreate(genexusApplication: GenexusApplication) {
        check(!modulesInitialized) { "Application has already been initialized" }
        currentGenexusApp = genexusApplication
        prevConfig = Configuration(application.resources.configuration)
        servicesLinker = ServicesLinker.get(this)
        lifecycle.notifyApplicationCreated(this)
        PlatformHelper.reset()
        connectUserControls()
        connectIntentHandlers()
        connectModules()
        Connect.init() // init Std Classes, connect service for use Std classes from Android app
        Services.Themes.setDarkMode(prevConfig.uiMode and Configuration.UI_MODE_NIGHT_YES != 0)
        Services.Preferences.removePreferences("BCMessages_") // LocalUtils.BC_PREFERENCES_PREFIX
        AndroidContext.ApplicationContext = ContextImpl(appContext)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        // DebugService.onConfigurationChanged(newConfig);
        // LocaleHelper.onConfigurationChanged(newConfig);

        if (newConfig == null)
            return

        Services.Log.debug("App on onConfigurationChanged")
        val diff = newConfig.diff(prevConfig)

        var changeSizeOnly = false
        if (diff and ActivityInfo.CONFIG_SCREEN_SIZE != 0) {
            Services.Log.debug("Screen size has been changed.")
            changeSizeOnly = true
        }

        if (diff and ActivityInfo.CONFIG_ORIENTATION != 0) {
            Services.Log.debug("Orientation has been changed.")
            changeSizeOnly = false
        }

        if (diff and ActivityInfo.CONFIG_LOCALE != 0)
            Services.Log.debug("Locale has been changed.")

        Services.Themes.setDarkMode(newConfig.uiMode and Configuration.UI_MODE_NIGHT_YES != 0)

        // other configuration change checking goes here
        // if windows size change , clear windows size cache.
        if (changeSizeOnly) AdaptersHelper.clearCacheWindowSizes()

        prevConfig = Configuration(newConfig)
    }

    override fun attachBaseContext(context: Context?) {
        // could attach new context here, but this only happens on App Startup.
        // this not help in change language in runtime
        // https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758
    }

    override fun getAppContext(): Context? {
        return application.applicationContext
    }

    override fun getApplicationServer(connectivity: Connectivity?): IApplicationServer? {
        return servers.getApplicationServer(connectivity)
    }

    override fun get(): GenexusApplication? {
        return if (currentMiniApp != null) currentMiniApp else currentGenexusApp
    }

    override fun getAndroidApplication(): Application {
        return application
    }

    override fun getDefinition(): ApplicationDefinition? {
        val app = get() ?: throw IllegalStateException("Current application is null, it is too soon to call this method!")
        return app.definition
    }

    override fun getLifecycle(): ApplicationLifecycle {
        return lifecycle
    }

    override fun getServicesLinker(): ServicesLinker {
        return servicesLinker
    }

    override fun getEntityProvider(): IEntityProvider {
        return entityProvider
    }

    override fun setMiniApp(miniApp: MiniApp) {
        currentMiniApp = miniApp
        PlatformHelper.reset()
    }

    override fun unloadMiniApp() {
        currentMiniApp = null
        PlatformHelper.reset()
    }

    override fun getMiniApp(): MiniApp? {
        return currentMiniApp
    }

    override fun hasActiveMiniApp(): Boolean {
        return currentMiniApp != null
    }

    override fun isLoaded(): Boolean {
        return definition?.isLoaded == true
    }

    override fun isLoadingMetadata(): Boolean {
        return definition?.isLoadingMetadata == true
    }

    override fun saveNewDynamicServicesUrl(newServicesUrl: String?) {
        Services.Preferences.getAppSharedPreferences(PreferencesHelper.DYNAMIC_URL_PREFS_KEY).edit().apply {
            putString(PreferencesHelper.DYNAMIC_URL_VALUE_KEY, newServicesUrl)
            apply()
        }
    }

    override fun getExternalApiFactory(): ExternalApiFactory {
        return externalApiFactory
    }

    override fun getTableLayoutFactory(): TableLayoutFactory {
        return tableLayoutFactory
    }

    @WorkerThread
    override fun clearData() {
        // Initialize DB offline storage.
        DatabaseStorage.initialize(application, definition?.cacheDatabase)

        // If language changes, clear stored data, since it may have translations.
        Services.Language.clearCacheOnLanguageChange()

        // Make sure that the cache has not grown too much.
        ImageLoader.trimCacheSize()
    }

    override fun getClientStorage(name: String): ClientStorage {
        val preferences = Services.Preferences.getAppSharedPreferences(name)
        return ClientStorageImpl(application, preferences)
    }

    override fun setAppsLinksProtocol(value: String) {
        appsLinksProtocol = value
    }

    override fun getAppsLinksProtocol(): String {
        return appsLinksProtocol
    }

    override fun isLiveEditingEnabled(): Boolean {
        return liveEditingModeEnabled
    }

    override fun enableLiveEditingMode() {
        liveEditingModeEnabled = true
    }

    override fun isRTLLanguage(): Boolean {
        // from : https://stackoverflow.com/a/18996319
        return application.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    override fun registerModule(module: GenexusModule) {
        check(!modulesInitialized) { "Cannot register new modules at this point -- initialization has already been completed." }
        require(!modules.contains(module)) { String.format("Module '%s' has already been registered.", module.javaClass) }
        modules.add(module)
    }

    override fun handleIntent(ctx: UIContext?, intent: Intent?, data: Entity?): Boolean {
        return IntentHandlers.tryHandleIntent(ctx, intent, data)
    }

    private fun connectModules() {
        for (module in modules)
            module.initialize(application)

        modulesInitialized = true
    }

    private fun connectIntentHandlers() {
        IntentHandlers.addHandler(AppLinksIntentHandler())
    }

    private fun connectUserControls() {
        // Load Default Controls
        val definitions = arrayOf(
            UserControlDefinition("Check Box", GxCheckBox::class.java),
            UserControlDefinition("Dynamic Combo Box", DynamicSpinnerControl::class.java),
            UserControlDefinition("SDAdsView", SDAdsViewControl::class.java),
            UserControlDefinition("SearchBox", SearchBox::class.java)
        )
        for (definition in definitions)
            UcFactory.addControl(definition)
    }
}

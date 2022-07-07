package com.genexus.android.core.base.services

import android.app.Activity
import android.location.Location
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.MiniAppCachedCollection
import com.genexus.android.core.superapps.MiniAppCollection
import com.genexus.android.core.superapps.errors.LoadError
import com.genexus.android.core.superapps.errors.SearchError
import com.genexus.android.core.tasking.Task

interface ISuperApps {
    // Properties

    /**
     * @return The Provisioning Server URL currently set.
     * @throws IllegalStateException if the Provisioning URL has not been set
     */
    @Throws(IllegalStateException::class)
    fun getProvisioningUrl(): String

    /**
     * @return The Super App Id currently set.
     */
    fun getId(): String

    /**
     * @return The Super App version currently set.
     */
    fun getVersion(): Int

    // Search

    /**
     * Performs a request to the Provisioning Server for available Mini Apps given the text.
     * @param text The string with the search criteria.
     * @param start 0-based index from which elements will be returned.
     * @param count Maximum number of returned elements (0 means no limit).
     * @return A cancelable Task
     */
    fun searchByText(text: String, start: Int, count: Int): Task<MiniAppCollection, SearchError>

    /**
     * Performs a request to the Provisioning Server for available Mini Apps that are available inside the given circular region.
     * @param center The center point of the specified region.
     * @param radius The radius in meters of the circular region.
     * @param start 0-based index from which elements will be returned.
     * @param count Maximum number of returned elements (0 means no limit).
     * @return A cancelable Task
     */
    fun searchByLocation(center: Location, radius: Int, start: Int, count: Int): Task<MiniAppCollection, SearchError>

    /**
     * Performs a request to the Provisioning Server for available Mini Apps given the tag.
     * @param tag The tag to search for (exact match).
     * @param start 0-based index from which elements will be returned.
     * @param count Maximum number of returned elements (0 means no limit).
     * @return A cancelable Task
     */
    fun searchByTag(tag: String, start: Int, count: Int): Task<MiniAppCollection, SearchError>

    /**
     * Performs a request to the Provisioning Server for available featured Mini Apps.
     * @param start 0-based index from which elements will be returned.
     * @param count Maximum number of returned elements (0 means no limit).
     * @return A cancelable Task
     */
    fun searchFeatured(start: Int, count: Int): Task<MiniAppCollection, SearchError>

    // Loading

    /**
     * Loads a Mini app and transitions to it if the passed in Mini App is already installed.
     * @param miniApp The Mini App to load.
     * @return A cancelable Task
     */
    fun load(miniApp: MiniApp): Task<Boolean, LoadError>

    /**
     * Exits from the currently loaded Mini App and transitions to the Super App.
     * @param activity An Activity instance
     */
    fun exit(activity: Activity)

    // Cache

    /**
     * Queries the file system for cached mini apps.
     * Avoid calling from the main Thread as it performs several file IO operations.
     * @return The cached Mini Apps collection.
     */
    fun getCachedMiniApps(): MiniAppCachedCollection

    /**
     * Removes the Mini App from cache if found for the given identifier and version.
     * Avoid calling from the main Thread as it performs several file IO operations.
     * @param id The Mini App identifier.
     * @param version The Mini App version to remove.
     * @return True if the Mini App was found, False otherwise.
     */
    fun removeMiniApp(id: String, version: Int): Boolean

    /**
     * Removes all Mini Apps from the cache.
     * Avoid calling from the main Thread as it performs several file IO operations.
     * @return True if the cached Mini Apps were correctly deleted from file system, False otherwise.
     */
    fun clearCache(): Boolean
}

package com.genexus.android.maps

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.genexus.android.PermissionUtil
import com.genexus.android.core.activities.ActivityHelper
import com.genexus.android.core.activities.GxBaseActivity
import com.genexus.android.core.base.metadata.ActionDefinition
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.common.UIActionHelper
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapOptions
import com.genexus.android.core.controls.maps.common.LocationPickerConstants.EXTRA_AUTOCOMPLETE_ENABLED
import com.genexus.android.core.controls.maps.common.LocationPickerConstants.EXTRA_LOCATION
import com.genexus.android.core.controls.maps.common.LocationPickerConstants.EXTRA_MAP_TYPE
import com.genexus.android.core.controls.maps.common.LocationPickerConstants.EXTRA_SHOW_LOCATION
import com.genexus.android.core.controls.maps.common.LocationPickerConstants.EXTRA_ZOOM
import com.genexus.android.location.R

abstract class LocationPickerActivityBase<MapView : View, Map, Marker>(private val options: IMapOptions<*>) : GxBaseActivity() {

    private var helper: LocationPickerHelper? = null
    private var autocompleteEnabled = false
    private var container: LinearLayout? = null
    var map: Map? = null
    var marker: Marker? = null
    var mapView: MapView? = null
        set(value) {
            field = value
            container?.addView(
                value,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHelper.initialize(this, savedInstanceState)
        setContentView(R.layout.map_layout)
        ActivityHelper.setSupportActionBar(this)
        helper = LocationPickerHelper(this, false)
        container = findViewById(R.id.map_container)
        mapView = Services.Maps.getMapViewFactory()?.createProviderMapView(this, options) as MapView?

        if (mapView == null)
            throw MapViewIsNullException()

        initializeFromIntent(savedInstanceState)
    }

    private fun initializeFromIntent(savedInstanceState: Bundle?) {
        val showMyLocation = intent.getBooleanExtra(EXTRA_SHOW_LOCATION, false) &&
            PermissionUtil.checkSelfPermissions(this, Services.Location.requiredPermissions)

        autocompleteEnabled = intent.getBooleanExtra(EXTRA_AUTOCOMPLETE_ENABLED, false)
        val mapType = intent.getStringExtra(EXTRA_MAP_TYPE)
        val zoom = intent.getIntExtra(EXTRA_ZOOM, 15)
        var mapLocation = intent.getStringExtra(EXTRA_LOCATION)
        if (!Strings.hasValue(mapLocation))
            mapLocation = GeoFormats.getLocationString(Services.Location.lastKnownLocation)

        val locationCoordinates = GeoFormats.parseGeolocation(mapLocation)
        initialize(savedInstanceState, showMyLocation, mapType, zoom, locationCoordinates)
    }

    fun setPickedLocation(position: IMapLocation) {
        helper?.setPickedLocation(position)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val itemSelect = menu.add(Menu.NONE, MENU_SELECT, Menu.NONE, R.string.GX_BtnSelect)
        UIActionHelper.setStandardMenuItemImage(this, itemSelect, ActionDefinition.StandardAction.SAVE)
        itemSelect.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

        val itemCancel = menu.add(Menu.NONE, MENU_CANCEL, Menu.NONE, R.string.GXM_cancel)
        UIActionHelper.setStandardMenuItemImage(this, itemCancel, ActionDefinition.StandardAction.CANCEL)
        itemCancel.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

        if (autocompleteEnabled) {
            val itemSearch = menu.add(Menu.NONE, MENU_SEARCH, Menu.NONE, R.string.GXM_search)
            UIActionHelper.setStandardMenuItemImage(this, itemSearch, ActionDefinition.StandardAction.SEARCH)
            itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_SELECT -> helper?.selectLocation()
            MENU_CANCEL -> helper?.cancelSelect()
            MENU_SEARCH -> showAutocompleteSearchBox()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ActivityHelper.onNewIntent(this, intent)
    }

    override fun onStart() {
        super.onStart()
        ActivityHelper.onStart(this)
    }

    override fun onStop() {
        ActivityHelper.onStop(this)
        super.onStop()
    }

    override fun onPause() {
        ActivityHelper.onPause(this)
        super.onPause()
    }

    override fun onDestroy() {
        ActivityHelper.onDestroy(this)
        super.onDestroy()
    }

    protected class MapViewIsNullException : java.lang.IllegalStateException("Map view cannot be null")

    companion object {
        private const val MENU_SELECT = 2
        private const val MENU_CANCEL = 3
        private const val MENU_SEARCH = 4
        const val AUTOCOMPLETE_REQUEST_CODE = 2291
    }

    abstract fun initialize(savedInstanceState: Bundle?, myLocationEnabled: Boolean, mapType: String?, zoom: Int, locationLatLng: Pair<Double, Double>?)
    abstract fun setPointOnMap(location: IMapLocation, zoom: Int)
    abstract fun showAutocompleteSearchBox()
}

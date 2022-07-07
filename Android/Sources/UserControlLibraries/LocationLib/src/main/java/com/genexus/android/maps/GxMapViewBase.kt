package com.genexus.android.maps

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Pair
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.genexus.android.WithPermission
import com.genexus.android.core.base.controls.IGxControlNotifyEvents
import com.genexus.android.core.base.metadata.layout.GridDefinition
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.base.utils.NameMap
import com.genexus.android.core.base.utils.RunnableUtils
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controllers.ViewData
import com.genexus.android.core.controls.GxImageViewStatic
import com.genexus.android.core.controls.IGridView
import com.genexus.android.core.controls.grids.GridAdapter
import com.genexus.android.core.controls.grids.GridHelper
import com.genexus.android.core.controls.grids.GridHelper.EVENT_SELECTION_CHANGED
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IGeographiesManager
import com.genexus.android.core.controls.maps.common.IGeographiesServerLoader
import com.genexus.android.core.controls.maps.common.IGxMapView
import com.genexus.android.core.controls.maps.common.IGxMapViewRuntimeMethods
import com.genexus.android.core.controls.maps.common.IGxMapViewSupportLayers
import com.genexus.android.core.controls.maps.common.IMapFeatureFactory
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapLocationBounds
import com.genexus.android.core.controls.maps.common.IMapOptions
import com.genexus.android.core.controls.maps.common.MapDataBase
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer
import com.genexus.android.core.controls.maps.common.MapUtilsBase
import com.genexus.android.core.controls.maps.common.OnGeographiesLoadedCallback
import com.genexus.android.core.controls.maps.common.OnGeographiesManagerCreatedCallback
import com.genexus.android.core.controls.maps.common.OnGeographyClickedCallback
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.core.utils.TaskRunner

@Suppress("UNCHECKED_CAST")
abstract class GxMapViewBase<ProviderMap, ProviderMapView : View, CameraUpdate, Marker,
    Line, Polygon, MarkerOptions, LineOptions, PolygonOptions, IconType, MarkerDragListener,
    K : IMapLocation, V : IMapLocationBounds<K>, U : MapItemBase<K>, MapViewData : MapDataBase<U, K, V>>
constructor(
    context: Context,
    private val mapOptions: IMapOptions<*>,
    val definition: GxMapViewDefinition,
    private val coordinator: Coordinator
) : FrameLayout(context),
    IGxMapView,
    IGxControlNotifyEvents,
    IGxMapViewSupportLayers,
    IGxMapViewRuntimeMethods,
    OnMapLifecycleEvent {

    val mapView: ProviderMapView by lazy { getViewInstance(mapOptions) }

    private val grid: GridDefinition = definition.grid
    private val helper: GridHelper = GridHelper(mapView, coordinator, grid).apply { setReservedSpace(ITEM_VIEW_WIDTH_MARGIN) }
    private val adapter = GridAdapter(context, helper, grid)
    private val itemViewHelper = MapItemViewHelper(mapView as ViewGroup)
    private val pendingLayers = ArrayList<MapLayer>()
    private var pendingUpdate: ViewData? = null
    private var lastCenterSet: IMapLocation? = null
    private var editableGeographies = EditMode.None
    private var selectedIndexInt = -1
    private var selectionLayerImageView: GxImageViewStatic? = null

    private val initialZoom = definition.initialZoom
    private val routeTransportType: String = definition.routeTransportType
    private val routeClass: ThemeClassDefinition = definition.routeClass
    private val showMyLocation = definition.showMyLocation
    private val needsUserLocationForMapBounds = definition.needsUserLocationForMapBounds()
    private var gxMapType: String? = definition.mapType
    private var showDirectionLayer = definition.showDirectionsLayer
    var showAnimationLayer = definition.showAnimationLayer
    var showSelectionLayer = definition.showSelectionLayer
    var showTrafficLayer = definition.showTraffic
    val myLocationImageResourceId = definition.myLocationImageResourceId

    private var updateFromDataReady = false
    private var shouldUpdateCamera = true
    private var isFirstUpdate = true
    private var isMarkerClickCameraChange = false
    private var currentlyEditing = false
    private var isReady = false
    private var onResumeInvoked = false
    private var onDestroyInvoked = false
    private var disableRequestPermission = false

    lateinit var geographiesManager: IGeographiesManager<Marker, MarkerDragListener>
    private var geographiesLoader: IGeographiesServerLoader<MapViewData>? = null
    private var camera: MapViewCamera<CameraUpdate, K, V, U, MapViewData>? = null
    private var mapViewWrapperCallback: OnGeographiesManagerCreatedCallback? = null
    private var mapRouteLayer: MapRouteLayerBase<K, V, U, MapViewData>? = null
    private lateinit var mapUtils: MapUtilsBase<K, V>
    private var mapData: MapViewData? = null
    private var dragListener: MarkerDragListener? = null
    var map: ProviderMap? = null

    private fun getViewInstance(mapOptions: IMapOptions<*>): ProviderMapView {
        val mapView = Services.Maps.getMapViewFactory()?.getProviderMapView(context as Activity, mapOptions) as ProviderMapView
        addView(mapView)
        onMapCreate(mapView)
        return mapView
    }

    open fun onMapCreate(mapView: ProviderMapView) {}
    open fun onBeforeUpdate() { selectionLayer = showSelectionLayer }

    fun setMap(providerMap: ProviderMap, dragListener: MarkerDragListener?) {
        this.map = providerMap
        this.dragListener = dragListener
        Services.Device.postOnUiThread(initializationRunnable)
    }

    /**
     * A Runnable is run on a new Thread because the initialization makes use of mapView which is under instantiation in
     * setMap method, thus creating an infinite loop and likely a crash. Despite of this, some providers require
     * that some fields are set on the Main thread so GeographiesManager and GeographiesLoader run on it.
     */
    private val initializationRunnable = Runnable {
        val featureFactory = Services.Maps.getMapFeatureFactory() as IMapFeatureFactory<ProviderMapView, ProviderMap>?
        featureFactory?.let {
            geographiesManager = featureFactory.getGeographiesManager(map as ProviderMap, mapView, definition) as IGeographiesManager<Marker, MarkerDragListener>
            geographiesManager.setGeographyClickedListener(geographyClickedListener)
            dragListener?.let { geographiesManager.setMarkerDragListener(it) }
            geographiesLoader = featureFactory.getGeographiesLoader(geographiesManager) as GeographiesServerLoader<K, V, U, MapViewData>?
            geographiesLoader?.setGeographiesLoadedCallback(geographiesLoadedCallback)
            camera = featureFactory.getMapViewCamera(map as ProviderMap, mapView) as MapViewCamera<CameraUpdate, K, V, U, MapViewData>?
            mapRouteLayer = featureFactory.getMapRouteLayer(this, context as Activity)
                as MapRouteLayerBase<K, V, U, MapViewData>?
            mapUtils = featureFactory.getMapUtils(definition) as MapUtilsBase<K, V>
        }

        onBeforeUpdate()
        isReady = true
        pendingUpdate?.let {
            update(it)
            pendingUpdate = null
        }
    }

    override fun update(data: ViewData) {
        if (isReady) {
            // MapView.onResume() may not have been called if the fragment was added
            // afterwards (e.g. with slide navigation).
            if (!onResumeInvoked) {
                onResumeInvoked = true
                onResume()
            }
            adapter.data = data

            if (isFirstUpdate)
                isFirstUpdate = false
            else
                shouldUpdateCamera = data.moveToTop

            // Add markers and position camera according to center/zoom properties.
            updateFromData()
        } else pendingUpdate = data
    }

    private fun updateFromData() {
        // We have two tasks that MAY need the location permissions: "update map" (if the location
        // participates in boundary calculations) and "show my location".
        val tasksWithLocationSuccess = ArrayList<Runnable>()
        val tasksWithLocationFailure = ArrayList<Runnable>()
        val standaloneTasks = ArrayList<Runnable>()
        if (showMyLocation)
            tasksWithLocationSuccess.add(myLocationRunnable)

        if (needsUserLocationForMapBounds) {
            // If we don't get the location permission, we still need to load the data.
            // We will have to make do without the user's location (e.g. bounds will be inaccurate).
            tasksWithLocationSuccess.add(runnableUpdateFromData)
            tasksWithLocationFailure.add(runnableUpdateFromData)
        } else standaloneTasks.add(runnableUpdateFromData)

        // If the request is denied, don't ask permission again in this screen (e.g. on refresh).
        tasksWithLocationFailure.add(Runnable { disableRequestPermission = true })

        // We fire the "standalone" tasks immediately, and ask permission for the others.
        RunnableUtils.chainRunnables(standaloneTasks).run()
        if (!disableRequestPermission && tasksWithLocationSuccess.isNotEmpty()) {
            val permissionRequestBuilder = WithPermission.Builder<Void>(context as Activity)
                .require(Services.Location.requiredPermissions)
                .setRequestCode(200)
                .attachToActivityController()
                .onSuccess(RunnableUtils.chainRunnables(tasksWithLocationSuccess))
                .onFailure(RunnableUtils.chainRunnables(tasksWithLocationFailure))
            permissionRequestBuilder.build().run()
        } else RunnableUtils.chainRunnables(tasksWithLocationFailure).run()
    }

    private val runnableUpdateFromData = Runnable {
        itemViewHelper.removeCurrentItem()
        mapData = Services.Maps.newMapData(definition, adapter.data) as MapViewData
        Services.Log.debug("update from data")
        geographiesLoader?.update(mapData, showAnimationLayer)

        if (shouldUpdateCamera)
            camera?.update(mapData, lastCenterSet as K?)

        if (showDirectionLayer) {
            val zoomToLayer = initialZoom == GxMapViewDefinition.INITIAL_ZOOM_DEFAULT
            mapRouteLayer?.addRouteLayer(mapData!!, routeTransportType, routeClass, zoomToLayer)
        }

        updateFromDataReady = true

        // Draw pending layers.
        // Draw it in every update from data, because update clear the map at refresh.
        for (layer in pendingLayers) {
            addLayer(layer)
        }
    }

    private val geographiesLoadedCallback = object : OnGeographiesLoadedCallback {
        override fun geographiesLoaded(serverLayer: MapLayer) {
            mapViewWrapperCallback?.geographiesManagerCreated(geographiesManager)
            raiseGeographiesLoadedEvent(serverLayer)
        }
    }

    private val geographyClickedListener = object : OnGeographyClickedCallback {
        override fun geographyClicked(mapFeature: MapLayer.MapFeature?): Boolean {
            return handleGeographyClicked(mapFeature)
        }
    }

    fun onMapClick(location: IMapLocation?): Boolean = mapClickListener.onMapClick(location)

    private val mapClickListener = object : OnMapClickListener {
        override fun onMapClick(location: IMapLocation?): Boolean {
            if (location == null)
                return false

            val type: MapLayer.FeatureType = when (editableGeographies) {
                EditMode.None -> return false
                EditMode.Points -> MapLayer.FeatureType.Point
                EditMode.Lines -> MapLayer.FeatureType.Polyline
                EditMode.Polygons -> MapLayer.FeatureType.Polygon
            }
            val feature = geographiesManager.updateDrawnFeatureWithNewPoint(type, location, Strings.EMPTY)
            feature?.let { raiseGeographySavedEvent(it) }
            return true
        }
    }

    fun onDragStarted(marker: Marker) = markerDragListener.onDragStarted(marker)
    fun onDrag(marker: Marker) = markerDragListener.onDrag(marker)
    fun onDragEnded(marker: Marker) = markerDragListener.onDragEnded(marker)

    private val markerDragListener = object : OnMarkerDragListener<Marker> {
        override fun onDragStarted(marker: Marker) {
            geographiesManager.extractMarkerId(marker)?.let { raiseMakerDragStartedEvent(it) }
        }

        override fun onDrag(marker: Marker) {}

        override fun onDragEnded(marker: Marker) {
            // This flag is needed because updateDrawnFeatureWithNewPoint might end up updating a Geography
            // and Mapbox's AnnotationManager finishes dragging (thus calling this method again) before creating it,
            // crashing because of an infinite loop
            if (currentlyEditing)
                return

            if (geographiesManager.markerDrawnInEditMode(marker)) {
                currentlyEditing = true
                geographiesManager.handleMarkerDragged(marker)?.let { raiseGeographySavedEvent(it) }
                currentlyEditing = false
            } else {
                val key = geographiesManager.getFeatureKey(marker)
                if (!key.isNullOrEmpty())
                    raiseMarkerDragEndedEvent(key, geographiesManager.getMarkerPosition(marker))

                camera?.updateCamera(geographiesManager.getMarkerPosition(marker), 0)
            }
        }
    }

    fun onCameraChange() = cameraActionListener.onCameraChange()
    fun onCameraMove(location: IMapLocation?) = cameraActionListener.onCameraMove(location)
    fun onCameraIdle(location: IMapLocation?) = cameraActionListener.onCameraIdle(location)

    private val cameraActionListener = object : OnCameraActionListener {
        override fun onCameraChange() {
            // Fire camera update if it was pending due to layout not having been performed yet.
            camera?.pendingUpdate?.let {
                camera?.updateCamera(it, 0, null)
                camera?.pendingUpdate = null
            }
            if (isMarkerClickCameraChange) {
                isMarkerClickCameraChange = false
                return
            }

            // Remove item view when the map is scrolled.
            itemViewHelper.removeCurrentItem()
            if (selectedIndexInt != -1) {
                // selected index none
                selectedIndexInt = -1
                raiseControlSelectionChangedEvent()
            }
        }

        override fun onCameraMove(location: IMapLocation?) {
            if (showSelectionLayer)
                center(location)?.let { raiseControlChangingEvent(it) }
        }

        override fun onCameraIdle(location: IMapLocation?) {
            if (showSelectionLayer)
                center(location)?.let { raiseControlChangedEvent(it) }
        }

        private fun center(location: IMapLocation?): IMapLocation? {
            return location ?: camera?.getProjection(null)?.center
        }
    }

    fun updateCamera(location: IMapLocation, zoom: Int) {
        camera?.updateCamera(location, zoom)
    }

    private inner class OnMarkerCameraUpdate(private val itemView: View) :
        OnMarkerCameraUpdateCallback {
        override fun onFinish() {
            itemViewHelper.displayItem(itemView)
            isMarkerClickCameraChange = true
        }

        override fun onStart() {
            itemViewHelper.removeCurrentItem()
        }

        override fun onCancel() {}
    }

    override fun notifyEvent(type: IGxControlNotifyEvents.EventType) {
        if (onDestroyInvoked) return // Ignore double onDestroy().
        when (type) {
            IGxControlNotifyEvents.EventType.ACTIVITY_STARTED -> onStart()
            IGxControlNotifyEvents.EventType.ACTIVITY_PAUSED -> onPause()
            IGxControlNotifyEvents.EventType.ACTIVITY_STOPPED -> onStop()
            IGxControlNotifyEvents.EventType.ACTIVITY_RESUMED -> {
                onResume()
                onResumeInvoked = true
            }
            IGxControlNotifyEvents.EventType.ACTIVITY_DESTROYED -> {
                onDestroy()
                onDestroyInvoked = true
            }
            else -> {
            }
        }
    }

    override fun addListener(listener: IGridView.GridEventsListener) {
        helper.setListener(listener)
    }

    override fun addLayer(layer: MapLayer) {
        if (!updateFromDataReady) {
            pendingLayers.add(layer)
            return
        }
        var auxFeature: MapLayer.MapFeature?
        for (feature in layer.features) {
            val featureInfo = geographiesManager.buildFeatureInfoFromFeature(feature, null)
            auxFeature = geographiesManager.addFeature(layer.id, featureInfo, closeFeature = true, updateAnimation = false)
            if (auxFeature != null)
                feature.mapObject = auxFeature.mapObject
        }
    }

    override fun removeLayer(layer: MapLayer?) {
        if (layer != null)
            for (feature in layer.features)
                geographiesManager.removeFeature(layer.id, feature, true)
    }

    override fun updateLayer(layerId: String, feature: MapLayer.MapFeature?) {
        feature?.let { geographiesManager.updateLayer(layerId, it) }
    }

    override fun clearLayers() {
        geographiesManager.removeAll()
        adapter.data = ViewData.empty(false)
        itemViewHelper.removeCurrentItem()
    }

    override fun setLayerVisible(layer: MapLayer, visible: Boolean) {
        geographiesManager.setVisible(layer, visible)
    }

    override fun adjustBoundsToLayer(layer: MapLayer) {
        val locations = mutableListOf<K>()
        for (feature in layer.features)
            for (point in feature.points)
                locations.add(point as K)

        if (locations.size > 1) {
            val bounds = mapUtils.newMapBounds(locations)
            camera?.updateCamera(
                bounds,
                Services.Device.dipsToPixels(MapViewCamera.CAMERA_MARGIN_DIPS),
                0
            )
        }
    }

    override fun setGeographiesManagerCreatedCallback(callback: OnGeographiesManagerCreatedCallback?) {
        mapViewWrapperCallback = callback
    }

    override fun saveEdition() {
        geographiesManager.saveEdition()?.let { raiseGeographySavedEvent(it) }
    }

    override fun selectIndex(index: Int) {
        if (index >= 0) {
            // index in data , not in markers.
            if (index >= adapter.count) {
                Services.Log.debug("Index of map point not found " + index + " Total " + adapter.count)
                return
            }
            val itemData = adapter.getEntity(index)
            if (itemData != null) {
                // Do not show InfoWindow if item has no layout.
                if (adapter.isItemViewEmpty(itemData)) return

                // find map item  view and location
//              val mapData = Services.MapsnewMapData(context, definition, adapter.data)
                val gxMapViewItem = mapData?.newMapItem(itemData)
                if (gxMapViewItem == null) {
                    Services.Log.debug("Marker not found. $itemData")
                    return
                }
                val itemView = adapter.getView(index, null, null)

                // select this item in map.
                selectFeatureInMap(gxMapViewItem.locationList, itemData, itemView)

                // selected index position
                if (index != selectedIndexInt) {
                    selectedIndexInt = index
                    raiseControlSelectionChangedEvent()
                }
            }
        }
    }

    override fun deselectIndex(index: Int) {
        if (index >= 0) {
            // Remove item view when deselect it.
            itemViewHelper.removeCurrentItem()
            // selected index none
            if (selectedIndexInt != -1) {
                selectedIndexInt = -1
                raiseControlSelectionChangedEvent()
            }
        }
    }

    override fun getSelectedIndex(): Int {
        return selectedIndexInt
    }

    override fun setDirectionsLayer(directionsLayer: Boolean) {
        showDirectionLayer = directionsLayer
        if (directionsLayer) {
            if (updateFromDataReady) {
                val mapData = Services.Maps.newMapData(definition, adapter.data) as MapViewData
                val zoomToLayer = initialZoom == GxMapViewDefinition.INITIAL_ZOOM_DEFAULT
                mapRouteLayer?.removeRouteLayer()
                mapRouteLayer?.addRouteLayer(mapData, routeTransportType, routeClass, zoomToLayer)
            }
        } else {
            mapRouteLayer?.removeRouteLayer()
        }
    }

    override fun setAnimationLayer(useAnimationLayer: Boolean) {
        showAnimationLayer = useAnimationLayer
    }

    override fun getSelectionLayer(): Boolean {
        return showSelectionLayer
    }

    override fun setSelectionLayer(useSelectionLayer: Boolean) {
        showSelectionLayer = useSelectionLayer
        if (showSelectionLayer)
            TaskRunner.execute(ShowSelectionLayerImageTask(definition.selectionPinImageName))
        else
            selectionLayerImageView?.let { (parent as ViewGroup).removeView(it) }
    }

    private inner class ShowSelectionLayerImageTask(private val pin: String) : TaskRunner.BaseTask<Drawable?>() {
        override fun doInBackground(): Drawable? {
            return Services.Images.getStaticImage(context, pin, false, true)
        }

        override fun onPostExecute(result: Drawable?) {
            if (result == null) {
                Services.Log.error(String.format("Selection Layer not turned on as the specified image '%s' doesn't exist", pin))
                selectionLayer = false
                return
            }

            val view = selectionLayerImageView ?: GxImageViewStatic(context, null).apply {
                val pinClass = definition.selectionPinImageClass
                if (pinClass != null && pinClass.pinImageWidth > 0 && pinClass.pinImageHeight > 0) {
                    setImageScaleType(pinClass.pinImageScaleType)
                    setImageSize(pinClass.pinImageWidth, pinClass.pinImageHeight)
                }
            }

            // Image will be be loaded from cache so this won't require downloading the file again
            Services.Images.displayImage(view, pin)
            val lpImage = RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE) }
            (parent as ViewGroup).addView(view, lpImage)
        }
    }

    override fun getEditableGeographies(): String {
        return editableGeographies.toString()
    }

    override fun setEditableGeographies(mode: String?) {
        mode?.let {
            val newMode = EditMode.valueOf(mode)
            if (editableGeographies != newMode) {
                editableGeographiesPropertyChanged(newMode)
                editableGeographies = newMode
            }
        }
    }

    private fun editableGeographiesPropertyChanged(newMode: EditMode) {
        when (newMode) {
            EditMode.None, EditMode.Points, EditMode.Lines, EditMode.Polygons -> if (editableGeographies != EditMode.None) saveEdition()
        }
    }

    override fun getMapType(): String? {
        return gxMapType
    }

    override fun setMapType(type: String?) {
        gxMapType = type
    }

    override fun setMapCenter(location: IMapLocation, zoomLevel: Int) {
        lastCenterSet = location
        if (zoomLevel > 0)
            camera?.updateCamera(location, zoomLevel, MARKER_CAMERA_ANIMATION_DURATION, null)
        else
            camera?.updateCamera(location, MARKER_CAMERA_ANIMATION_DURATION)
    }

    override fun setZoomLevel(zoomLevel: Int) {
        getMapProjection(null)?.let {
            camera?.updateCamera(it.center, zoomLevel, MARKER_CAMERA_ANIMATION_DURATION, null)
        }
    }

    override fun getVisibleRegion(): Entity {
        val entity = EntityFactory.newSdt("GeneXus.SD.MapRegion")
        getMapProjection(null)?.let {
            val ne = it.latLngBounds.northeast()
            val sw = it.latLngBounds.southwest()
            val zoom = it.zoom
            entity.setProperty("NorthEast", GeoFormats.buildGeopoint(ne.latitude, ne.longitude))
            entity.setProperty("SouthWest", GeoFormats.buildGeopoint(sw.latitude, sw.longitude))
            entity.setProperty("MinZoom", zoom)
            entity.setProperty("MaxZoom", zoom)
        }

        return entity
    }

    private fun selectFeatureInMap(points: List<IMapLocation>, itemData: Entity, itemView: View) {
        if (grid.defaultAction != null)
            itemView.setOnClickListener(OnItemViewClickListener(itemData, helper))

        val position = (if (points.size > 1) calculateCenter(points) else points[0]) as K
        getMapProjection(position)?.let {
            camera?.updateCamera(
                it.projectionCenter,
                0,
                MARKER_CAMERA_ANIMATION_DURATION,
                OnMarkerCameraUpdate(itemView)
            )
        }
    }

    private fun handleGeographyClicked(mapFeature: MapLayer.MapFeature?): Boolean {
        if (mapFeature == null) return false
        val mapItem = mapFeature.itemData
        val key = mapFeature.id
        if (mapItem == null || adapter.isItemViewEmpty(mapItem.data)) {
            Services.Log.info(String.format("No mapItem data for %s", key))
            return false
        }
        val itemData = mapItem.data
        val position = adapter.getIndexOf(itemData)
        if (position == -1) {
            Services.Log.info(String.format("MapItem %s index not found in adapter", key))
            return false
        }
        val itemView = adapter.getView(position, null, null)
        selectFeatureInMap(mapFeature.points, itemData, itemView)
        if (position != selectedIndexInt) {
            selectedIndexInt = position
            raiseControlSelectionChangedEvent()
            raiseGeographySelectedEvent(key)
        }
        return true
    }

    private class OnItemViewClickListener(
        private val itemData: Entity,
        private val helper: GridHelper
    ) : OnClickListener {
        override fun onClick(v: View) {
            helper.runDefaultAction(itemData)
        }
    }

    private fun raiseRegionDownloadedEvent(regionId: String, success: Boolean) {
        val params = mutableListOf<Any>()
        params.add(regionId)
        params.add(success)
        raiseEvent(EVENT_REGION_DOWNLOADED, params)
    }

    private fun raiseGeographiesLoadedEvent(serverLayer: MapLayer) {
        val params = mutableListOf<EntityList>()
        featuresToEntities(serverLayer.features)?.let { params.add(it) }
        raiseEvent(EVENT_GEOGRAPHIES_LOADED, params)
    }

    private fun raiseControlChangedEvent(location: IMapLocation) {
        val params = mutableListOf<String>()
        params.add(GeoFormats.buildGeopoint(location.latitude, location.longitude))
        raiseEvent(EVENT_CONTROL_VALUE_CHANGED, params)
    }

    private fun raiseControlChangingEvent(location: IMapLocation) {
        val params = mutableListOf<String>()
        params.add(GeoFormats.buildGeopoint(location.latitude, location.longitude))
        raiseEvent(EVENT_CONTROL_VALUE_CHANGING, params)
    }

    private fun raiseMarkerDragEndedEvent(markerId: String, location: IMapLocation) {
        val params = mutableListOf<String>()
        params.add(markerId)
        params.add(GeoFormats.buildGeopoint(location.latitude, location.longitude))
        raiseEvent(EVENT_MARKER_DRAG_ENDED, params)
    }

    private fun raiseMakerDragStartedEvent(markerId: String) {
        val params = mutableListOf<String>()
        params.add(markerId)
        raiseEvent(EVENT_MARKER_DRAG_STARTED, params)
    }

    private fun raiseControlSelectionChangedEvent() {
        raiseEvent(EVENT_SELECTION_CHANGED, listOf())
    }

    private fun raiseGeographySelectedEvent(featureKey: String) {
        val params = mutableListOf<String>()
        params.add(featureKey)
        raiseEvent(EVENT_GEOGRAPHY_SELECTED, params)
    }

    private fun raiseGeographySavedEvent(feature: MapLayer.MapFeature) {
        val params = mutableListOf<String>()
        params.add(GeoFormats.buildGeography(latLngToPairLists(feature.points), feature.type))
        params.add(feature.id)
        raiseEvent(EVENT_GEOGRAPHY_SAVED, params)
    }

    private fun raiseEvent(eventName: String, parameters: List<Any>) {
        val gridControl = parent as View
        val actionDef = coordinator.getControlEventHandler(gridControl, eventName)
        if (actionDef != null && (parameters.isNotEmpty() || eventName == EVENT_SELECTION_CHANGED)) {
            val actualEventParameters = actionDef.eventParameters
            require(actualEventParameters.size == parameters.size) { "$eventName requires ${actualEventParameters.size} parameters." }
            for ((i, entry) in parameters.withIndex()) {
                val paramName = actualEventParameters[i].value
                coordinator.setValue(paramName, entry)
            }

            coordinator.runControlEvent(gridControl, eventName)
        }
    }

    private fun featuresToEntities(mapFeatures: List<MapLayer.MapFeature>): EntityList? {
        if (mapFeatures.isEmpty())
            return null

        try {
            val entities = EntityList()
            for (feature in mapFeatures) {
                val entity = EntityFactory.newSdt("GeneXus.SD.MapGeographies", "MapGeography")
                entity.setProperty("Id", feature.id)
                entity.setProperty(
                    "Feature",
                    GeoFormats.buildGeography(latLngToPairLists(feature.points), feature.type)
                )
                entities.add(entity)
            }
            return entities
        } catch (e: IllegalArgumentException) {
            Services.Log.warning("SDT definition for 'GeneXus.SD.MapGeographies' is missing.")
            return null
        }
    }

    @Suppress("unused")
    private fun layersToFeatures(layers: NameMap<MapLayer>): List<MapLayer.MapFeature> {
        val mapFeatures = mutableListOf<MapLayer.MapFeature>()
        for ((_, value) in layers)
            mapFeatures.addAll(value.features)

        return mapFeatures
    }

    private fun latLngToPairLists(latLngs: List<IMapLocation>): List<Pair<Double, Double>> {
        val points: MutableList<Pair<Double, Double>> = ArrayList()
        for (latLng in latLngs) points.add(Pair(latLng.latitude, latLng.longitude))
        return points
    }

    private fun getMapProjection(initialLocation: K?): MapViewCamera.Projection<K, V>? {
        return camera?.getProjection(initialLocation)
    }

    private fun calculateCenter(points: List<IMapLocation>): IMapLocation? {
        var centroidX = 0.0
        var centroidY = 0.0
        for (latLng in points) {
            centroidX += latLng.latitude
            centroidY += latLng.longitude
        }
        return Services.Maps.newMapLocation(centroidX / points.size, centroidY / points.size)
    }

    companion object {
        const val ITEM_VIEW_WIDTH_MARGIN = 20 // dips
        const val MARKER_CAMERA_ANIMATION_DURATION = 500 // ms
        const val EVENT_CONTROL_VALUE_CHANGED = "ControlValueChanged"
        const val EVENT_CONTROL_VALUE_CHANGING = "ControlValueChanging"
        const val EVENT_MARKER_DRAG_ENDED = "MarkerDragEnded"
        const val EVENT_MARKER_DRAG_STARTED = "MarkerDragStarted"
        const val EVENT_GEOGRAPHY_SAVED = "GeographySaved"
        const val EVENT_GEOGRAPHIES_LOADED = "GeographiesLoaded"
        const val EVENT_GEOGRAPHY_SELECTED = "GeographySelected"
        const val EVENT_REGION_DOWNLOADED = "RegionDownloaded"
    }

    private interface OnMapClickListener {
        fun onMapClick(location: IMapLocation?): Boolean
    }

    private interface OnCameraActionListener {
        fun onCameraChange()
        fun onCameraMove(location: IMapLocation?)
        fun onCameraIdle(location: IMapLocation?)
    }

    private interface OnMarkerDragListener<Marker> {
        fun onDragStarted(marker: Marker)
        fun onDrag(marker: Marker)
        fun onDragEnded(marker: Marker)
    }

    interface OnMarkerCameraUpdateCallback {
        fun onFinish()
        fun onCancel()
        fun onStart()
    }
}

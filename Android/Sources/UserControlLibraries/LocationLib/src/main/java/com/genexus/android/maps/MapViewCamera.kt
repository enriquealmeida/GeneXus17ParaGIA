package com.genexus.android.maps

import android.util.Pair
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapLocationBounds
import com.genexus.android.core.controls.maps.common.IMapViewCamera
import com.genexus.android.core.controls.maps.common.MapDataBase
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.utils.TaskRunner

abstract class MapViewCamera<CameraUpdate, K : IMapLocation, V : IMapLocationBounds<K>, U : MapItemBase<K>, MapViewData : MapDataBase<U, K, V>> :
    IMapViewCamera<CameraUpdate, K, V, U, MapViewData> {

    private var task: CalculateBoundsTask<K, V, U, MapViewData>? = null
    var pendingUpdate: CameraUpdate? = null

    override fun update(mapData: MapViewData?, lastMapCenter: K?) {
        task?.cancel()
        task = mapData?.let { CalculateBoundsTask(it, lastMapCenter) }
        TaskRunner.execute(task)
    }

    private inner class CalculateBoundsTask<K : IMapLocation, MapBounds : IMapLocationBounds<K>, U : MapItemBase<K>, MapViewData : MapDataBase<U, K, MapBounds>>
    (private val mapData: MapViewData, private val lastMapCenter: K?) : TaskRunner.BaseTask<V?>() {

        @Suppress("UNCHECKED_CAST")
        override fun doInBackground(): V? {
            var currentCenter: Pair<Double, Double>? = null
            lastMapCenter?.let {
                currentCenter = Pair(it.latitude, it.longitude)
            }

            return mapData.calculateBounds(currentCenter) as V?
        }

        override fun onPostExecute(result: V?) {
            updateCameraInternal(result)
        }

        private fun updateCameraInternal(bounds: V?) {
            bounds?.let { updateCamera(it, Services.Device.dipsToPixels(CAMERA_MARGIN_DIPS), 0) }
        }
    }

    companion object {
        const val CAMERA_MARGIN_DIPS = 40
    }

    class Projection<K : IMapLocation, V : IMapLocationBounds<K>>
    (val latLngBounds: V, val center: K, val projectionCenter: K, val zoom: Int)

    abstract fun getProjection(initialLocation: K?): Projection<K, V>?
    abstract fun updateCamera(bounds: V, padding: Int, duration: Int)
    abstract fun updateCamera(location: IMapLocation, zoom: Int)
    abstract fun updateCamera(
        location: IMapLocation,
        zoom: Int,
        duration: Int,
        callback: GxMapViewBase.OnMarkerCameraUpdateCallback?
    )
    abstract fun updateCamera(
        cameraUpdate: CameraUpdate,
        duration: Int,
        callback: GxMapViewBase.OnMarkerCameraUpdateCallback?
    )
}

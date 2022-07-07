package com.genexus.android.maps

import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.FeatureInfo
import com.genexus.android.core.controls.maps.common.IGeographiesManager
import com.genexus.android.core.controls.maps.common.IGeographiesServerLoader
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapLocationBounds
import com.genexus.android.core.controls.maps.common.MapDataBase
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer
import com.genexus.android.core.controls.maps.common.OnGeographiesLoadedCallback
import com.genexus.android.core.utils.TaskRunner
import com.genexus.android.core.utils.ThemeUtils
import com.genexus.android.maps.GeographiesManagerBase.Companion.LAYER_ID_SERVER

open class GeographiesServerLoader<K : IMapLocation, V : IMapLocationBounds<K>, U : MapItemBase<K>, MapViewData : MapDataBase<U, K, V>>
(private val geographiesManager: IGeographiesManager<*, *>) : IGeographiesServerLoader<MapViewData> {

    private var task: LoaderTask<K, V, U, MapViewData>? = null
    private var updateAnimation = false
    private var isFirstUpdate = true
    private lateinit var callback: OnGeographiesLoadedCallback

    override fun update(mapData: MapViewData?, useAnimation: Boolean) {
        task?.cancel()
        updateAnimation = useAnimation
        if (!updateAnimation && !isFirstUpdate)
            geographiesManager.removeAll()

        if (isFirstUpdate)
            isFirstUpdate = false

        task = mapData?.let { LoaderTask(geographiesManager, callback, it, updateAnimation) }
        TaskRunner.execute(task)
    }

    override fun setGeographiesLoadedCallback(geographiesLoadedCallback: OnGeographiesLoadedCallback) {
        callback = geographiesLoadedCallback
    }

    private class LoaderTask<K : IMapLocation, V : IMapLocationBounds<K>, U : MapItemBase<K>, MapViewData : MapDataBase<U, K, V>>(
        private val taskGeographiesManager: IGeographiesManager<*, *>,
        private val taskGeographiesLoadedCallback: OnGeographiesLoadedCallback,
        private val mapData: MapViewData,
        private val updateAnimation: Boolean
    ) : TaskRunner.UpdateTask<FeatureInfo?, MapViewData?>() {

        private val loadedFeatures = mutableListOf<MapLayer.MapFeature>()

        override fun doInBackground(): MapViewData? {
            for (item in mapData) {
                if (isCancelled)
                    return null

                if (item == null)
                    continue

                val iconData = taskGeographiesManager.getPinImage(item.data)
                val themeClass = Services.Themes.getThemeClass(item.themeClassName)
                val width = themeClass?.lineWidth?.toFloat()
                val lineColor = ThemeUtils.getColorId(themeClass?.strokeColor)
                val polygonColor = ThemeUtils.getColorId(themeClass?.fillColor)
                val lineDashPattern = themeClass?.dashPattern
                val featureInfo = FeatureInfo(item).apply {
                    lineWidth = width
                    strokeColor = lineColor
                    fillColor = polygonColor
                    icon = iconData
                    dashPattern = lineDashPattern
                }

                when (item.featureType) {
                    MapLayer.FeatureType.Point -> publishProgress(featureInfo.apply { type = MapLayer.FeatureType.Point })
                    MapLayer.FeatureType.Polyline -> publishProgress(featureInfo.apply { type = MapLayer.FeatureType.Polyline })
                    MapLayer.FeatureType.Polygon -> publishProgress(featureInfo.apply { type = MapLayer.FeatureType.Polygon })
                    null -> { }
                }
            }

            return null
        }

        override fun onProgressUpdate(param: FeatureInfo?) {
            val feature = param?.let { taskGeographiesManager.addFeature(LAYER_ID_SERVER, it, true, updateAnimation) }
            if (feature != null && feature.points.size > 0)
                loadedFeatures.add(feature)
        }

        override fun onPostExecute(result: MapViewData?) {
            super.onPostExecute(result)
            taskGeographiesLoadedCallback.geographiesLoaded(taskGeographiesManager.getServerLayer())
        }
    }
}

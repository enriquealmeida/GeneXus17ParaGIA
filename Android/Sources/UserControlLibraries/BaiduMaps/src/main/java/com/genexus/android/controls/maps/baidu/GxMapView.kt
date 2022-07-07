package com.genexus.android.controls.maps.baidu

import android.annotation.SuppressLint
import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener
import com.baidu.mapapi.map.BitmapDescriptor
import com.baidu.mapapi.map.MapPoi
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdate
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.Marker
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.map.Polygon
import com.baidu.mapapi.map.PolygonOptions
import com.baidu.mapapi.map.Polyline
import com.baidu.mapapi.map.PolylineOptions
import com.baidu.mapapi.model.LatLng
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.maps.GxMapViewBase

@SuppressLint("ViewConstructor")
internal class GxMapView(context: Context, coordinator: Coordinator, definition: GxMapViewDefinition) :
    GxMapViewBase<BaiduMap, MapView, MapStatusUpdate, Marker, Polyline, Polygon,
        MarkerOptions, PolylineOptions, PolygonOptions, BitmapDescriptor, BaiduMap.OnMarkerDragListener,
        MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData>
    (context, MapOptions(), definition, coordinator) {

    private var baiduMapType = 0
    private var locationClient: LocationClient? = null

    override fun onMapCreate(mapView: MapView) {
        super.onMapCreate(mapView)
        mapView.onCreate(context, null)
        mapView.showZoomControls(true)
        mapView.isFocusable = true
        mapView.isEnabled = true
        mapView.isClickable = true
        mapView.map?.let {
            it.setOnMapClickListener(onMapClickListener)
            it.setOnMapStatusChangeListener(cameraChangeListener)
            setMap(it, markerDragListener)
        }
    }

    override fun onBeforeUpdate() {
        super.onBeforeUpdate()
        map?.let {
            baiduMapType = BaiduMapsHelper.mapTypeToBaiduMapType(mapType)
            it.mapType = baiduMapType
            it.isTrafficEnabled = showTrafficLayer
        }
    }

    private val markerDragListener: BaiduMap.OnMarkerDragListener =
        object : BaiduMap.OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker) {
                onDrag(marker)
            }

            override fun onMarkerDragEnd(marker: Marker) {
                onDragEnded(marker)
            }

            override fun onMarkerDragStart(marker: Marker) {
                onDragStarted(marker)
            }
        }
    private val onMapClickListener: BaiduMap.OnMapClickListener = object : BaiduMap.OnMapClickListener {
        override fun onMapClick(latLng: LatLng) {
            this@GxMapView.onMapClick(MapLocation(latLng.latitude, latLng.longitude))
        }

        override fun onMapPoiClick(mapPoi: MapPoi) {}
    }

    private val cameraChangeListener: OnMapStatusChangeListener = object : OnMapStatusChangeListener {
        override fun onMapStatusChangeStart(mapStatus: MapStatus) {
            onCameraMove(MapLocation(mapStatus.target.latitude, mapStatus.target.longitude))
        }

        override fun onMapStatusChangeStart(mapStatus: MapStatus, i: Int) {
            onCameraMove(MapLocation(mapStatus.target.latitude, mapStatus.target.longitude))
        }

        override fun onMapStatusChange(mapStatus: MapStatus) {
            onCameraChange()
        }

        override fun onMapStatusChangeFinish(mapStatus: MapStatus) {
            onCameraIdle(MapLocation(mapStatus.target.latitude, mapStatus.target.longitude))
        }
    }

    override fun setMapType(type: String?) {
        super.setMapType(type)
        baiduMapType = BaiduMapsHelper.mapTypeToBaiduMapType(type)
        map?.mapType = baiduMapType
    }

    override fun getMyLocationRunnable(): Runnable {
        return Runnable {
            map?.setMyLocationConfiguration(
                MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null)
            )

            map?.isMyLocationEnabled = true

            // show my location icon and animate it
            // if (mDefinition.getMyLocationImageResourceId()!=0)
            // 	mBaiduMap.setOnMyLocationChangeListener(new GxMapOnMyLocationChangeListener());

            // 定位初始化
            // use http://lbsyun.baidu.com/index.php?title=android-locsdk
            locationClient = LocationClient(context)
            locationClient?.registerLocationListener(MyBDLocationListener())
            val option = LocationClientOption()
            option.isOpenGps = true // 打开gps
            option.setCoorType("bd09ll") // 设置坐标类型
            option.setScanSpan(1000)
            locationClient?.locOption = option
            locationClient?.start()
        }
    }

    private inner class MyBDLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            // 此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            // 以下只列举部分获取经纬度相关（常用）的结果信息
            // 更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            // Get latitude information double latitude = location . GetLatitude ();
            // Get longitude information double longitude = location . GetLongitude ();
            // Get positioning accuracy, the default value is 0.0f float radius = location . GetRadius ();
            // Get the latitude and longitude coordinate type, based on the coordinate type set in
            // LocationClientOption String coorType = location . GetCoorType ();
            // Get the positioning type and positioning error return code. For specific information,
            // please refer to the description in the BDLocation class in the class reference
            // int errorCode = location . getLocType ();

            // 此处设置开发者获取到的方向信息，顺时针0-360
            val mCurrentDirection = 0
            val locData = MyLocationData.Builder()
                .accuracy(location.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentDirection.toFloat()).latitude(location.latitude)
                .longitude(location.longitude).build()
            map?.setMyLocationData(locData)
        }
    }

    override fun onStart() {}

    override fun onResume() {
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
    }

    override fun onStop() {}

    override fun onDestroy() {
        // 退出时销毁定位
        locationClient?.stop()

        // 关闭定位图层
        map?.isMyLocationEnabled = false
        mapView.onDestroy()
    }
}

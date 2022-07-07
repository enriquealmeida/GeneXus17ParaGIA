package com.genexus.android.location.baidu

import com.baidu.location.BDLocation
import com.genexus.android.core.base.services.Services

object LocationDebugger {
    fun debug(location: BDLocation?, sdkVersion: String) {
        if (location != null && location.locType != BDLocation.TypeServerError) {
            val sb = StringBuilder()
            sb.append("time : ")
            sb.append(location.time)
            sb.append("\nlocType : ") // 定位类型
            sb.append(location.locType)
            sb.append("\nlocType description : ") // *****对应的定位类型说明*****
            sb.append(location.locTypeDescription)
            sb.append("\nlatitude : ") // 纬度
            sb.append(location.latitude)
            sb.append("\nlongtitude : ") // 经度
            sb.append(location.longitude)
            sb.append("\nradius : ") // 半径
            sb.append(location.radius)
            sb.append("\nCountryCode : ") // 国家码
            sb.append(location.countryCode)
            sb.append("\nProvince : ") // 获取省份
            sb.append(location.province)
            sb.append("\nCountry : ") // 国家名称
            sb.append(location.country)
            sb.append("\ncitycode : ") // 城市编码
            sb.append(location.cityCode)
            sb.append("\ncity : ") // 城市
            sb.append(location.city)
            sb.append("\nDistrict : ") // 区
            sb.append(location.district)
            sb.append("\nTown : ") // 获取镇信息
            sb.append(location.town)
            sb.append("\nStreet : ") // 街道
            sb.append(location.street)
            sb.append("\naddr : ") // 地址信息
            sb.append(location.addrStr)
            sb.append("\nStreetNumber : ") // 获取街道号码
            sb.append(location.streetNumber)
            sb.append("\nUserIndoorState: ") // *****返回用户室内外判断结果*****
            sb.append(location.userIndoorState)
            sb.append("\nDirection(not all devices have value): ")
            sb.append(location.direction) // 方向
            sb.append("\nlocationdescribe: ")
            sb.append(location.locationDescribe) // 位置语义化信息
            sb.append("\nPoi: ") // POI信息

            if (location.poiList != null) {
                for (poi in location.poiList) {
                    sb.append("poiName:")
                    sb.append(poi.name + ", ")
                    sb.append("poiTag:")
                    sb.append(poi.tags + "\n")
                }
            }

            if (location.poiRegion != null) {
                sb.append("PoiRegion: ") // 返回定位位置相对poi的位置关系，仅在开发者设置需要POI信息时才会返回，在网络不通或无法获取时有可能返回null
                val poiRegion = location.poiRegion
                sb.append("DerectionDesc:") // 获取POIREGION的位置关系，ex:"内"
                sb.append(poiRegion.derectionDesc + " ")
                sb.append("Name:") // 获取POIREGION的名字字符串
                sb.append(poiRegion.name + " ")
                sb.append("Tags:") // 获取POIREGION的类型
                sb.append(poiRegion.tags + " ")
                sb.append("\nSDK版本: ")
            }

            sb.append(sdkVersion)

            when (location.locType) {
                BDLocation.TypeGpsLocation -> { // GPS定位结果
                    sb.append("\nspeed : ")
                    sb.append(location.speed) // 速度 单位：km/h
                    sb.append("\nsatellite : ")
                    sb.append(location.satelliteNumber) // 卫星数目
                    sb.append("\nheight : ")
                    sb.append(location.altitude) // 海拔高度 单位：米
                    sb.append("\ngps status : ")
                    sb.append(location.gpsAccuracyStatus) // *****gps质量判断*****
                    sb.append("\ndescribe : ")
                    sb.append("gps定位成功")
                }
                BDLocation.TypeNetWorkLocation -> { // 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) { // *****如果有海拔高度*****
                        sb.append("\nheight : ")
                        sb.append(location.altitude) // 单位：米
                    }
                    sb.append("\ndescribe : ")
                    sb.append("网络定位成功")
                }
                BDLocation.TypeOffLineLocation -> { // 离线定位结果
                    sb.append("\ndescribe : ")
                    sb.append("离线定位成功，离线定位结果也是有效的")
                }
                BDLocation.TypeServerError -> {
                    sb.append("\ndescribe : ")
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因")
                }
                BDLocation.TypeNetWorkException -> {
                    sb.append("\ndescribe : ")
                    sb.append("网络不同导致定位失败，请检查网络是否通畅")
                }
                BDLocation.TypeCriteriaException -> {
                    sb.append("\ndescribe : ")
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机")
                }
            }

            Services.Log.debug("BaiduLocation", sb.toString())
        }
    }
}

package net.hyntech.common.model.entity

import com.baidu.mapapi.model.LatLng

data class EBikePoint(val latLng: LatLng, val imei:String, val speed:Int, val course:Int, val accStatus:Boolean, val positionType:String, val gpsTime:Long, val new_address:String = "")
package net.hyntech.common.widget.map.ebike

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.TypedValue
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BitmapDescriptor
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.DistanceUtil
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.model.entity.EBikePoint

operator fun LatLng.plus(latLng: LatLng): LatLng {
    return LatLng(latitude + latLng.latitude, longitude + latLng.longitude)
}

operator fun LatLng.minus(latLng: LatLng): LatLng {
    return LatLng(latitude - latLng.latitude, longitude - latLng.longitude)
}

operator fun LatLng.times(times: Int): LatLng {
    return LatLng(latitude * times, longitude * times)
}

operator fun LatLng.div(div: Int): LatLng {
    return LatLng(latitude / div, longitude / div)
}

fun carRotation(start: LatLng, end: LatLng): Float {
    val rotation = if (end.longitude != start.longitude) {
        val tan = (end.latitude - start.latitude) / (end.longitude - start.longitude)
        val atan = Math.atan(tan)
        var deg = atan * 360 / (2 * Math.PI)
        deg = if (end.longitude < start.longitude) {
            -deg + 90 + 90
        } else {
            -deg
        }
        -deg.toFloat()
    } else {
        val disy = end.latitude - start.latitude
        var bias = 1
        if (disy > 0) {
            bias = -1
        }
        -bias * 90f
    }
    return rotation
}

fun carRotate(start: LatLng, end: LatLng):Float{
    val lat1 = start.latitude
    val lon1 = start.longitude
    val lat2 = end.latitude
    val lon2 = end.longitude

    val longitude1 = lon1
    val longitude2 = lon2
    val latitude1 = Math.toRadians(lat1)
    val latitude2 = Math.toRadians(lat2)
    val longDiff = Math.toRadians(longitude2-longitude1)
    val y = Math.sin(longDiff)*Math.cos(latitude2)
    val x = Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff)
    return ((Math.toDegrees(Math.atan2(y, x))+360) % 360).toFloat()
}

// 计算经纬度旋转角度
fun getAngle(start: LatLng, end: LatLng):Float{
    val lat1 = start.latitude
    val lng1 = start.longitude
    val lat2 = end.latitude
    val lng2 = end.longitude
    var dRotateAngle = Math.atan2(Math.abs(lng2 - lng1), Math.abs(lat2 - lat1))
    if (lng2 >= lng1) {
        if (lat2 > lat1) {
            dRotateAngle = 2 * Math.PI - dRotateAngle
        }
    }else{
        if (lat2 >= lat1) {
            dRotateAngle = Math.PI + dRotateAngle
        }else{
            dRotateAngle = Math.PI - dRotateAngle
        }
    }

    dRotateAngle = dRotateAngle * 180 / Math.PI
    val angle = dRotateAngle.toFloat()
    LogUtils.logGGQ("--角度-->>>${angle}")
    return angle

}
fun splitLatLng4MoveDistance(actualLatLngList: ArrayList<LatLng>, moveDistance: Int): ArrayList<ArrayList<LatLng>> {
    val moveLatLngList = ArrayList<ArrayList<LatLng>>()
    moveLatLngList.add(arrayListOf(actualLatLngList[0]))
    for (i in 1 until actualLatLngList.size) {
        val distance = DistanceUtil.getDistance(actualLatLngList[i - 1], actualLatLngList[i]).toInt()
        if (distance <= moveDistance) {
            moveLatLngList.add(arrayListOf(actualLatLngList[i]))
        } else {
            val count = distance / moveDistance - if (distance % moveDistance == 0) 1 else 0
            val subLatLng = actualLatLngList[i] - actualLatLngList[i - 1]
            val divLatLng = subLatLng / count
            val list = ArrayList<LatLng>()
            repeat(count) {
                val ll = actualLatLngList[i - 1] + (divLatLng * (it + 1))
                list.add(ll)
            }
            list.add(actualLatLngList[i])
            moveLatLngList.add(list)
        }
    }
    return moveLatLngList
}

fun splitPoint4MoveDistance(actualLatLngList: ArrayList<EBikePoint>, moveDistance: Int): ArrayList<ArrayList<LatLng>> {
    val moveLatLngList = ArrayList<ArrayList<LatLng>>()
    moveLatLngList.add(arrayListOf(actualLatLngList[0].latLng))
    for (i in 1 until actualLatLngList.size) {
        val distance = DistanceUtil.getDistance(actualLatLngList[i - 1].latLng, actualLatLngList[i].latLng).toInt()
        if (distance <= moveDistance) {
            moveLatLngList.add(arrayListOf(actualLatLngList[i].latLng))
        } else {
            val count = distance / moveDistance - if (distance % moveDistance == 0) 1 else 0
            val subLatLng = actualLatLngList[i].latLng - actualLatLngList[i - 1].latLng
            val divLatLng = subLatLng / count
            val list = ArrayList<LatLng>()
            repeat(count) {
                val ll = actualLatLngList[i - 1].latLng + (divLatLng * (it + 1))
                list.add(ll)
            }
            list.add(actualLatLngList[i].latLng)
            moveLatLngList.add(list)
        }
    }
    return moveLatLngList
}

fun Context.getBitmapDescriptor4Resource(redId: Int, scale: Float = 1f): BitmapDescriptor {
    if (scale == 1f) {
        return BitmapDescriptorFactory.fromResource(redId)
    }
    val srcBitmap = BitmapFactory.decodeResource(resources, redId)
    val matrix = Matrix()
    matrix.postScale(scale, scale)
    val destBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.width, srcBitmap.height, matrix, true)
    return BitmapDescriptorFactory.fromBitmap(destBitmap)
}

fun Context.getBitmapDescriptor4Resource(redId: Int, destWidth: Int, destHeight: Int): BitmapDescriptor {
    val srcBitmap = BitmapFactory.decodeResource(resources, redId)
    val matrix = Matrix()
    matrix.postScale(destWidth.toFloat() / srcBitmap.width.toFloat(), destHeight.toFloat() / srcBitmap.height.toFloat())
    val destBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.width, srcBitmap.height, matrix, true)
    return BitmapDescriptorFactory.fromBitmap(destBitmap)
}

fun Context.px2dp(px: Float): Float {
    return px * (1 / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1F, resources.displayMetrics))
}

fun Context.px2sp(px: Float): Float {
    return px * (1 / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1F, resources.displayMetrics))
}

fun Context.dp2px(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}

fun Context.sp2px(sp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

fun BaiduMap.onZoomLevelChange(action: (Float) -> Unit) {
    setOnMapStatusChangeListener(object : BaiduMap.OnMapStatusChangeListener {
        override fun onMapStatusChangeStart(status: MapStatus?) {

        }

        override fun onMapStatusChangeStart(status: MapStatus?, p1: Int) {

        }

        override fun onMapStatusChange(status: MapStatus?) {

        }

        override fun onMapStatusChangeFinish(status: MapStatus?) {
            status?.also {
                //这些数据都是从百度地图开放平台拿的
                val distance = when (it.zoom.toInt()) {
                    22 -> 2f
                    21 -> 5f
                    20 -> 10f
                    19 -> 20f
                    18 -> 50f
                    17 -> 100f
                    16 -> 200f
                    15 -> 500f
                    14 -> 1000f
                    13 -> 2000f
                    12 -> 5000f
                    11 -> 10000f
                    10 -> 20000f
                    9 -> 25000f
                    8 -> 50000f
                    7 -> 100000f
                    6 -> 200000f
                    5 -> 500000f
                    4 -> 1000000f
                    3 -> 2000000f
                    else -> 20f
                }
                action(distance)
            }
        }
    })
}

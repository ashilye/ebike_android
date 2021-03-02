package net.hyntech.common.widget.map.ebike

import android.content.Context
import android.os.Bundle
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.DistanceUtil
import com.blankj.utilcode.util.TimeUtils
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.model.entity.EBikePoint
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.async
import java.lang.ref.WeakReference
import java.util.concurrent.Future

abstract class AbsEBikeTrackManager (val context: Context, protected val baiduMap: BaiduMap, private val carIcon: BitmapDescriptor, val startIcon: BitmapDescriptor, val endIcon: BitmapDescriptor) {

    //随便写的,但Marker的ZIndex必须比Polyline的ZIndex大
    protected var carMarkerZIndex = -50
    protected var polylineLineZIndex = -100
    var polylineWidth = 10
    var moveDistance = 5
    var sleepTime = 20L

    var animateZoom = 18f

    protected val threadController = AnkoAsyncContext(WeakReference(this))
    protected var asyncTask: Future<Unit>? = null
    protected var isStop = false
    protected var isPause = false
    protected var isRunning = false

    protected var pointList = ArrayList<EBikePoint>()

    protected var listener: OnMoveListener? = null
    fun setOnMoveListener(l: OnMoveListener){
        this.listener = l
    }

    /**
     * 建议最后控制一下数据量,过大的话就导致计算不过来
     */
    fun setPointList(list: ArrayList<EBikePoint>, distance: Int = 0) {
        if (list.size <= 1) {
            pointList = list
            onSetTrackLatLngListFinish()
            return
        }
        if (distance <= 0) {
            pointList = list
            onSetTrackLatLngListFinish()
            return
        }
        pointList.clear()
        pointList.add(list[0])
        (1 until list.size).filter { DistanceUtil.getDistance(list[it - 1].latLng, list[it].latLng) > distance }.mapTo(pointList) { list[it] }
        onSetTrackLatLngListFinish()
    }

    protected open fun onSetTrackLatLngListFinish() {}

    var maxLevel = 19F
        set(maxLevel) {
            field = maxLevel
            baiduMap.setMaxAndMinZoomLevel(maxLevel, minLevel)
        }
    var minLevel = 4F
        set(minLevel) {
            field = minLevel
            baiduMap.setMaxAndMinZoomLevel(maxLevel, minLevel)
        }

    init {
        baiduMap.setMaxAndMinZoomLevel(maxLevel, minLevel)
    }

    companion object {
        init {
            LogUtils.logGGQ("--CarTrackManager---")
        }

        @JvmStatic
        fun newEbikeOnlineInstance(context: Context, baiduMap: BaiduMap, carIcon: BitmapDescriptor, startIcon:BitmapDescriptor, endIcon:BitmapDescriptor): EBikeOnlineManager {
            return EBikeOnlineManager(context,baiduMap, carIcon,startIcon,endIcon)
        }
    }

    /**
     * 可选，建议在构造器调用。移除会影响最终展示效果的UI设置，否则还要为这些设置写相应的代码去适配。
     */
    protected fun removeUiSetting() {
        baiduMap.uiSettings.isOverlookingGesturesEnabled = false
        baiduMap.uiSettings.isRotateGesturesEnabled = false
        baiduMap.uiSettings.isCompassEnabled = false
    }

    protected fun generateCarMarker(firstLatLng: LatLng): MarkerOptions {
        return MarkerOptions().anchor(0.5f, 0.5f).icon(carIcon).position(firstLatLng).zIndex(carMarkerZIndex)
    }


    //起点 和 终点 marker
    // 添加infoWindow
    protected fun generateStartEndMarker(start: EBikePoint, end: EBikePoint, startIcon:BitmapDescriptor, endIcon: BitmapDescriptor): List<OverlayOptions> {
        return listOf(MarkerOptions().position(start.latLng).icon(startIcon).apply {
            val bundle: Bundle = Bundle()
            bundle.putString("new_address",start.new_address)
            bundle.putString("lat",start.latLng.latitude.toString())
            bundle.putString("lng",start.latLng.longitude.toString())
            bundle.putString("imei",start.imei)
            bundle.putInt("speed",start.speed)
            bundle.putInt("course",start.course)
            bundle.putBoolean("accStatus",start.accStatus)
            bundle.putString("positionType",start.positionType)
            bundle.putString("gpsTime", TimeUtils.millis2String(start.gpsTime*1000))
            this.extraInfo(bundle)
        }, MarkerOptions().position(end.latLng).icon(endIcon).apply {
            val bundle: Bundle = Bundle()
            bundle.putString("new_address",start.new_address)
            bundle.putString("lat",end.latLng.latitude.toString())
            bundle.putString("lng",end.latLng.longitude.toString())
            bundle.putString("imei",end.imei)
            bundle.putInt("speed",end.speed)
            bundle.putInt("course",end.course)
            bundle.putBoolean("accStatus",end.accStatus)
            bundle.putString("positionType",end.positionType)
            bundle.putString("gpsTime", TimeUtils.millis2String(end.gpsTime*1000))
            this.extraInfo(bundle)
        })
    }

    protected fun generatePolylineOptions(pointList: List<LatLng>): PolylineOptions {
        return PolylineOptions().width(polylineWidth).points(pointList).zIndex(polylineLineZIndex).customTexture(
            BitmapDescriptorFactory.fromAsset("line_road.png"))
    }

    abstract fun start()

    open fun pause() {
        isStop = true
        isPause = true
        isRunning = false
    }

    open fun stop() {
        isStop = true
        isRunning = false
        threadController.async {
            Thread.sleep(sleepTime * 2)
            resetIndex()
        }
    }

    protected abstract fun resetIndex()

    fun release() {
        stop()
        asyncTask?.cancel(true)
        carIcon.recycle()
    }

    interface OnMoveListener {
        fun onStart()
        fun onFinish()
    }

    protected class MoveCarStopException : Exception()


    //防碰壁
    fun onMoveUpScreen(p:LatLng){
        try {
            baiduMap.let {
                LogUtils.logGGQ("纠偏-->${p}")
                val pt = it.mapStatus.targetScreen
                val point = it.projection.toScreenLocation(p)
                if(point.x < 0 || point.x > pt.x * 2 || point.y < 0 || point.y > pt.y * 2){
//                    val mapStatus = MapStatus.Builder().target(p).zoom(15f).build()
                    val mapStatus = MapStatus.Builder().target(p).build()
                    it.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus))
                }
            }
        }catch (e:java.lang.Exception){
            LogUtils.logGGQ("--onMoveCorrect--error->>${e.fillInStackTrace()}")
        }
    }
}
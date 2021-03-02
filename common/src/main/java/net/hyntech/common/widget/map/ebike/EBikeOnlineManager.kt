package net.hyntech.common.widget.map.ebike

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.geocode.*
import com.blankj.utilcode.util.TimeUtils
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.R
import net.hyntech.common.widget.map.GeoCoderUtils
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread


class EBikeOnlineManager(context: Context, baiduMap: BaiduMap, carIcon: BitmapDescriptor, startIcon:BitmapDescriptor, endIcon:BitmapDescriptor) : AbsEBikeTrackManager(context,baiduMap,carIcon, startIcon, endIcon),BaiduMap.OnMarkerClickListener {
    protected var firstIndex = 0
    protected var secondIndex = 0
    protected var lastMarker: Marker? = null
    protected var moveLatLngList = ArrayList<ArrayList<LatLng>>()

    private val overLayList:MutableList<Overlay> = mutableListOf()

    private val gcoding by lazy { BitmapDescriptorFactory.fromResource(R.drawable.ic_gcoding) }

    private var trackLine: Polyline? = null

    init {
        removeUiSetting()
        moveDistance = 3
        sleepTime = 22L
        // marker 点击监听
        baiduMap.setOnMarkerClickListener(this)
    }

    override fun onSetTrackLatLngListFinish() {
        moveLatLngList = splitPoint4MoveDistance(pointList, moveDistance)
    }

    override fun start() {
        if (isRunning) {
            return
        }
        isRunning = true
        isPlay = true
        //如果不是暂停后再播放,清除之前的makrer 标记
        if(!isPause){
            if(overLayList.isNotEmpty()){
                this.baiduMap.removeOverLays(overLayList)
            }
        }
        this.listener?.onStart()
        asyncTask = threadController.async {
            isStop = false
            for (i in firstIndex + 1 until pointList.size - 1) {
                try {
                    if (isStop) {
                        return@async
                    }
                    if (isPause) {
                        isPause = false
                    } else {
                        Thread.sleep(sleepTime)
                    }
                    moveCar(moveLatLngList[i])
                    //每循环一小段,添加一个marker


                    val overlay = baiduMap.addOverlay(MarkerOptions().position(pointList[i].latLng).apply {
                        //添加 点击marker
                        val bundle: Bundle = Bundle()
                        val c = pointList[i]
                        bundle.putString("new_address",c.new_address)
                        bundle.putString("lat",c.latLng.latitude.toString())
                        bundle.putString("lng",c.latLng.longitude.toString())
                        bundle.putString("imei",c.imei)
                        bundle.putInt("speed",c.speed)
                        bundle.putInt("course",c.course)
                        bundle.putBoolean("accStatus",c.accStatus)
                        bundle.putString("positionType",c.positionType)
                        bundle.putString("gpsTime", TimeUtils.millis2String(c.gpsTime*1000))
                        this.extraInfo(bundle)
                    }.icon(gcoding))
                    overLayList.add(overlay)
                    onMoveUpScreen(pointList[i + 1].latLng)
                    firstIndex = i
                    secondIndex = 0
                } catch (e: MoveCarStopException) {
                    return@async
                }
            }
            try {
                moveCar(moveLatLngList[pointList.lastIndex])
                moveCarFinish()
            } catch (e: MoveCarStopException) {
                return@async
            }
        }
    }

    @Throws(MoveCarStopException::class)
    protected fun moveCar(list: ArrayList<LatLng>) {
        LogUtils.logGGQ("移动段->>${list.size}")
        lastMarker?.position = list[0]
        for (i in secondIndex + 1 until list.size) {
            if (isStop) {
                LogUtils.logGGQ("stop-->>>${isStop}")
                throw MoveCarStopException()
            }
            Thread.sleep(sleepTime)
            lastMarker?.position = list[i]
            secondIndex = i
        }
    }

    fun moveCarFinish() {
        this.firstIndex = 0
         this.secondIndex = 0
         this.isRunning = false
         this.isPlay = false
         this.listener?.onFinish()
        threadController.uiThread {
            listener?.onFinish()
        }
    }

    override fun resetIndex() {
        firstIndex = 0
        secondIndex = 0
    }


    fun onLine(){
        if (isRunning) {
            return
        }
        if (firstIndex == 0 && secondIndex == 0) {
            threadController.async {
                lastMarker?.remove()
                val ood = generateCarMarker(pointList[0].latLng)
                lastMarker = baiduMap.addOverlay(ood) as Marker
                val u = MapStatusUpdateFactory.newLatLngZoom(pointList[0].latLng, animateZoom)
                baiduMap.animateMapStatus(u)
                baiduMap.addOverlays(generateStartEndMarker(pointList.first(),pointList.last(),startIcon,endIcon))

                trackLine?.remove()
                val line = generatePolylineOptions(pointList.map { it.latLng })
                trackLine = baiduMap.addOverlay(line) as Polyline
            }
        }
    }



    protected var isPlay:Boolean = false

    fun isCarPlay():Boolean{
        return isPlay
    }


    fun onPause(){
        isStop = true
        isPause = true
        isRunning = false
        isPlay = false
    }

    fun onResume(){
        this.start()
    }


    fun onFastBySleepTime(time:Long){
        this.sleepTime = time
    }



    //重新播放
    fun onReplay(){
        this.resetIndex()
        this.isStop = false
        this.isPause = false
        this.isRunning = false
        this.isPlay = false
        this.threadController.weakRef.clear()
        this.asyncTask?.cancel(true)
        this.start()
    }

    // 点击marker 回调
    override fun onMarkerClick(marker: Marker?): Boolean {

        var tvAddr:TextView? = null

        marker?.let {
            val bundle: Bundle = it.extraInfo

            val new_address:String = bundle.getString("new_address","")
            val lat:String = bundle.getString("lat","")
            val lng:String = bundle.getString("lng","")

            LogUtils.logGGQ("经纬度---》》》${lat}---${lng}")
            val imei:String = bundle.getString("imei","")
            val speed:Int = bundle.getInt("speed",0)
            val course:Int = bundle.getInt("course",0)
            val accStatus:Boolean = bundle.getBoolean("accStatus",false)
            val positionType:String = bundle.getString("positionType","")
            val gpsTime:String = bundle.getString("gpsTime","")
            val view = View.inflate(context,R.layout.window_marker_info,null)

            var accStr:String = "关闭"
            if(accStatus){
                accStr = "开启"
            }

            view?.apply {
                findViewById<TextView>(R.id.tv_imei)?.text = "设备号:${imei}"
                tvAddr = findViewById(R.id.tv_addr)

                findViewById<TextView>(R.id.tv_speed)?.text = "速度:${speed}km/h"
//                findViewById<TextView>(R.id.tv_course)?.text = "航向:${course}度"

                val direction:String = BiaduUtils.getDirectionByAngle(course)
                findViewById<TextView>(R.id.tv_course)?.text = "航向:${direction}"
                findViewById<TextView>(R.id.tv_accStatus)?.text = "ACC状态：${accStr}"
                findViewById<TextView>(R.id.tv_positionType)?.text = "定位类型：${positionType}"
                findViewById<TextView>(R.id.tv_gpsTime)?.text = "定位时间：${gpsTime}"
            }

            val latLng: LatLng = LatLng(lat.toDouble() ,lng.toDouble())
            GeoCoderUtils.getAddressFromLocation(latLng,object:
                GeoCoderUtils.AbsGetGeoCoderResultListener() {
                override fun onReverseGeoCodeResult(result: ReverseGeoCodeResult) {
                    super.onReverseGeoCodeResult(result)
                    LogUtils.logGGQ("onReverseGeoCodeResult->:${result}")
                    tvAddr?.text = "位置：${result.address}${result.sematicDescription}"
                }
            })

            // 点击关闭所有
            view?.findViewById<LinearLayout>(R.id.info_window)?.setOnClickListener {
                baiduMap.hideInfoWindow()
            }
            val infoWindow:InfoWindow = InfoWindow(view,marker.position,-56)

            //false 点击另一个不隐藏
            //baiduMap.showInfoWindow(infoWindow,false)
            baiduMap.showInfoWindow(infoWindow)
        }
        return false
    }

}
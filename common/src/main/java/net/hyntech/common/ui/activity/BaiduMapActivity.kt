package net.hyntech.common.ui.activity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import net.hyntech.common.R
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.provider.ARouterConstants
import net.hyntech.common.widget.map.baidu.MapViewHandler
import net.hyntech.common.widget.map.baidu.MyLocationListener

/**
 * 百度地图定位
 *   -> 当前位置
 *   -> 跟随模式传感器
 *   -> poi 检索
 */
@Route(path = ARouterConstants.BAIDU_MAP_PAGE)
class BaiduMapActivity: BaseActivity(), SensorEventListener {

    private var mapView: TextureMapView? = null
    private var rvPoint: RecyclerView? = null
    private var baiduMap: BaiduMap? = null

    private val myMarker by lazy { BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location) }

    //取传感器管理服务
    private val sensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    //默认模式
    private val currentMode by lazy { MyLocationConfiguration.LocationMode.NORMAL }
    private var locData: MyLocationData? = null

    private val locClient: LocationClient by lazy { LocationClient(this) }
    private val locListener: MyLocationListener by lazy {
        MyLocationListener(object :
            MyLocationListener.LocationListener {
            override fun onReceive(bdLocation: BDLocation) {
                receiveLocation(bdLocation)
            }
        })
    }

    private val locClientOption: LocationClientOption by lazy {
        LocationClientOption().apply {
            this.isOpenGps = true //打开gps
            this.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
            this.coorType = "bd09ll" //设置坐标类型
            this.setIsNeedAddress(true) //必须设置之后才能获取到详细的地址信息
            this.scanSpan = 0 //可选3000，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            this.setIsNeedLocationDescribe(true)
            this.setIsNeedLocationPoiList(true)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_baidu_map


    override fun initData(savedInstanceState: Bundle?) {
        setTitle<BaiduMapActivity>("选择位置").onBack<BaiduMapActivity> { onFinish() }

        rvPoint = findViewById(R.id.rv_point)
        mapView = findViewById(R.id.bmap_view)
        mapView?.let {
            it.showZoomControls(false) //设置隐藏放大缩小按钮
            baiduMap = it.map
            baiduMap?.uiSettings?.isRotateGesturesEnabled = false
            // 开启定位图层
            baiduMap?.isMyLocationEnabled = true
            //普通地图
            baiduMap?.setMapType(BaiduMap.MAP_TYPE_NORMAL)
            MapViewHandler(this@BaiduMapActivity).setMapView(it)

            //隐藏百度logo,ZoomControl
            val count: Int = it.childCount
            for (index in 1..count) {
                val child = it.getChildAt(index)
                child?.let { v ->
                    if (v is ImageView || v is com.baidu.platform.comapi.map.an) {
                        v.visibility = View.INVISIBLE
                    }
                }
            }
        }
        rvPoint?.layoutManager = LinearLayoutManager(this)
        initLocation()
    }

    private fun initLocation() {
        locClient.registerLocationListener(locListener)
        locClient.locOption = locClientOption
        locClient.start()
    }

    private var radius: Float = 0.0f
    private var direction: Float = 0.0f

    private fun receiveLocation(bdLocation: BDLocation) {
        bdLocation.let {
            radius = it.radius
            lat = bdLocation.latitude
            lng = bdLocation.longitude
            val locData = MyLocationData.Builder().accuracy(radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(it.direction).latitude(it.latitude)
                    .longitude(it.longitude).build()
            baiduMap?.setMyLocationData(locData)

            val builder = MapStatus.Builder()
            builder.target(LatLng(lat,lng)).zoom(18.0f)
            //跟随模式
            baiduMap?.setMyLocationConfiguration(
                MyLocationConfiguration(currentMode, true, null)
            )
            baiduMap?.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
        }
    }

    override fun onResume() {
        super.onResume()
        // 为系统的方向传感器注册监听器
        sensorManager.registerListener(this@BaiduMapActivity,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_UI)
    }

    override fun onStop() {
        sensorManager.unregisterListener(this)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 退出时销毁定位
        locClient.unRegisterLocationListener(locListener)
        locClient.stop()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private var lastX: Double = 0.0
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    override fun onSensorChanged(event: SensorEvent?) {
        //每次方向改变，重新给地图设置定位数据，用上一次onReceiveLocation得到的经纬度、精度
        event?.let {
            val x = it.values[SensorManager.DATA_X]
            if (Math.abs(x - lastX) > 1.0) {// 方向改变大于1度才设置，以免地图上的箭头转动过于频繁
                direction = x
                locData = MyLocationData.Builder().accuracy(radius)
                    .direction(direction).latitude(lat).longitude(lng).build()
                baiduMap?.setMyLocationData(locData)
            }
            lastX = x.toDouble()
        }
    }
}
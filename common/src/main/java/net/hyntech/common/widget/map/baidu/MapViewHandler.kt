package net.hyntech.common.widget.map.baidu

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.baidu.mapapi.map.TextureMapView
import net.hyntech.baselib.app.manager.LifecycleHandler
import net.hyntech.baselib.utils.LogUtils

class MapViewHandler(lifecycleOwner: LifecycleOwner):LifecycleHandler(lifecycleOwner) {
    private var mapView:TextureMapView? = null

    fun setMapView(mapView:TextureMapView){
        this.mapView = mapView
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mapView?.onResume()
        LogUtils.logGGQ("map view ->>onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mapView?.onPause()
        LogUtils.logGGQ("map view ->>onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
        LogUtils.logGGQ("map view ->>onDestroy")
    }

}
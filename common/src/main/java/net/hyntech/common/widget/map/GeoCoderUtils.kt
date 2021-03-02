package net.hyntech.common.widget.map

import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*

class GeoCoderUtils {

    companion object{

        // 逆地理编码（即坐标转地址）
        fun getAddressFromLocation(latLng: LatLng?, resultListener: OnGetGeoCoderResultListener?){
            if(latLng != null && resultListener != null){
                val search: GeoCoder? = GeoCoder.newInstance()
                search?.let {
                    it.setOnGetGeoCodeResultListener(resultListener)
                    it.reverseGeoCode(ReverseGeoCodeOption().location(latLng))
                    destroyGeoCoder(it)
                }
            }
        }

        private fun destroyGeoCoder(geoCoder: GeoCoder) {
            geoCoder.destroy()
        }
    }

    abstract class AbsGetGeoCoderResultListener: OnGetGeoCoderResultListener {

        open fun onGeoCodeResult(result: GeoCodeResult) {}
        open fun onReverseGeoCodeResult(result: ReverseGeoCodeResult) {}

        //获取地理编码结
        override fun onGetGeoCodeResult(result: GeoCodeResult?) {
            if(result == null || result.error != SearchResult.ERRORNO.NO_ERROR){
                return
            }
            onGeoCodeResult(result)
        }

        //获取反向地理编码结果
        override fun onGetReverseGeoCodeResult(result: ReverseGeoCodeResult?) {
            if(result == null || result.error != SearchResult.ERRORNO.NO_ERROR){
                return
            }
            onReverseGeoCodeResult(result)
        }
    }
}
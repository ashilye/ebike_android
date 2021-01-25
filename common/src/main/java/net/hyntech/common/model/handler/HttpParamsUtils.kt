package net.hyntech.common.model.handler

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import net.hyntech.baselib.app.BaseApp
import net.hyntech.baselib.http.error.ApiException
import net.hyntech.common.app.global.Constants
import net.hyntech.common.utils.MD5Utils
import java.util.*

class HttpParamsUtils {
        companion object{

            fun addPublicRequestParams(addToken:Boolean = true):WeakHashMap<String,Any>{
                val map:WeakHashMap<String,Any> = WeakHashMap()
                val accessToken = SPUtils.getInstance(BaseApp.instance.getAppPackage()).getString(
                    Constants.SaveInfoKey.ACCESS_TOKEN)
                val currentTimeStr = TimeUtils.getNowString()
                val uuidStr = UUID.randomUUID().toString()
                val sigStr = MD5Utils.sigMD5Hex(accessToken,uuidStr,currentTimeStr,addToken)
                if(addToken){
                    map.put("accessToken",accessToken)
                }
                map.put("timestamp",currentTimeStr)
                map.put("nonce",uuidStr)
                map.put("sig",sigStr)
                map.put("mobileTye","AN-Android")
                map.put("netType",getNetWorkStatusName(BaseApp.instance))
                map.put("appVersion", AppUtils.getAppVersionName(BaseApp.instance.getAppPackage()))
                return map
            }


            private fun getNetWorkStatusName(context: Context):String{
                var netWorkType:String = "UNKNOWN"
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                if(networkInfo != null && networkInfo.isConnected){
                    val type = networkInfo.type
                    if(type == ConnectivityManager.TYPE_WIFI){
                        netWorkType = "WIFI"
                    }else if(type == ConnectivityManager.TYPE_MOBILE){
                        netWorkType = getNetTypeClassName(context)
                    }
                }

                return netWorkType
            }

            private fun getNetTypeClassName(context: Context):String{
                var networkType = 0
                try {
                    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    networkType = tm.networkType
                }catch (e: ApiException){
                    e.fillInStackTrace()
                }
                when(networkType){
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN ->{
                        return "2G"
                    }
                    TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP, TelephonyManager.NETWORK_TYPE_TD_SCDMA ->{
                        return "3G"
                    }
                    TelephonyManager.NETWORK_TYPE_LTE, TelephonyManager.NETWORK_TYPE_IWLAN,19 ->{
                        return "4G"
                    }
                    else ->{
                        return "UNKNOWN"
                    }
                }
        }
    }
}
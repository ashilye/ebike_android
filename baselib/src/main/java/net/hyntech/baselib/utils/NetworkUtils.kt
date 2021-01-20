package net.hyntech.baselib.utils

import android.content.Context
import android.net.ConnectivityManager
import net.hyntech.baselib.app.BaseApp

/**
 * 网络工具类
 */
object NetworkUtil {
    fun isConnected(): Boolean {
        val manager = BaseApp.instance.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as? ConnectivityManager?
        if (manager != null) {
            val info = manager.activeNetworkInfo
            return info != null && info.isAvailable
        }
        return false
    }
}
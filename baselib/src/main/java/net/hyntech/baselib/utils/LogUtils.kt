package net.hyntech.baselib.utils

import android.util.Log
import net.hyntech.baselib.app.config.GlobalConfig

class LogUtils {
    companion object {

        @JvmStatic
        fun logGGQ(s: String?) {
            if (GlobalConfig.gIsDebug) {
                Log.i("GGQ", "${s}")
            }
        }
    }
}
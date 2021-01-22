package net.hyntech.baselib.ext

import android.text.TextUtils
import net.hyntech.baselib.app.config.Config


// 自定义 String 的 扩展方法 http 的 响应 code
internal fun String.isResultSuccess():Boolean{
    return TextUtils.equals(Config.SUCCESS_CODE,this)
}

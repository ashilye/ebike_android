package net.hyntech.baselib.http

import java.io.Serializable

/**
 * 响应数据封装
 * 这里暂无用
 */
open class BaseResponse<out T> {
    var code:String = "1000"
    var msg:String = "未知错误!"
    val data:T? = null
}
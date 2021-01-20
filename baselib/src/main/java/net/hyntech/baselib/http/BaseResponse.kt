package net.hyntech.baselib.http


/**
 * 响应数据封装
 */
open class BaseResponse<out T> {
    val code:String = "1000"
    val msg:String = "未知错误!"
    val data:T? = null
}
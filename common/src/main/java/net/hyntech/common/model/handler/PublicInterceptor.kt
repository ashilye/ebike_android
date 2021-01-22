package net.hyntech.common.model.handler

import net.hyntech.baselib.utils.LogUtils
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*

/**
 * 添加公共请求头拦截器
 */
class PublicInterceptor (private val headers: WeakHashMap<String, Any>?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
//        val original = chain.request()
//        val originalHeaders = original.headers
//        val requestBuilder = original.newBuilder()
//        // 添加统一通用header，不存在则添加，存在则不添加。
//        for (key in headers.keys) {
//            if (originalHeaders.get(key) == null) {
//                headers[key]?.let {
//                    requestBuilder.addHeader(key, it)
//                }
//            }
//        }
//        val request = requestBuilder.build()
//        return chain.proceed(request)

        return chain.proceed(chain.request().newBuilder().run {
            if (!headers.isNullOrEmpty()) {
                val originalRequest:Request = chain.request()

                val originalHeaders:Headers = originalRequest.headers
                for (headMap in headers) {
                    //添加统一通用header，不存在则添加，存在则不添加。
                    if (originalHeaders.get(headMap.key) == null) {
                        LogUtils.logGGQ("公共请求参数：${headMap.key}--${headMap.value}")
                        addHeader(headMap.key, headMap.value.toString()).build()
                    }
                }
            }
            build()
        })
    }
}
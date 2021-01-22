package net.hyntech.baselib.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

object HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().build())
    }
}
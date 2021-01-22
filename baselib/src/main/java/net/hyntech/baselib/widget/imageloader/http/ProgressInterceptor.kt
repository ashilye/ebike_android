package net.hyntech.baselib.widget.imageloader.http

import net.hyntech.baselib.widget.imageloader.listener.ProgressListener
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody


class ProgressInterceptor : Interceptor {

    companion object {
        val LISTENER_MAP: MutableMap<String, ProgressListener> = HashMap()

        fun addListener(
            url: String,
            listener: ProgressListener
        ) {
            LISTENER_MAP.put(url, listener)
        }


        fun removeListener(url: String) {
            LISTENER_MAP.remove(url)
        }
    }


    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val url: String? = request.url.toString()
        val body: ResponseBody? = response.body
        val newResponse: Response =
            response.newBuilder().body(
                ProgressResponseBody(
                    url,
                    body
                )
            ).build()
        return newResponse
    }
}
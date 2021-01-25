package net.hyntech.common.model.handler
import android.text.TextUtils
import net.hyntech.baselib.app.config.Config
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.app.global.Global
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response


class BaseURLInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //  配置请求头
        val request = chain.request()
        //从request中获取原有的HttpUrl实例oldHttpUrl
        val oldHttpUrl = request.url
        //获取request的创建者builder
        val builder = request.newBuilder()
        //从request中获取headers，通过给定的键url_name
        val headerValues:List<String>? = request.headers("urlname")
        if (headerValues != null && headerValues.isNotEmpty()) {
            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
            builder.removeHeader("urlname")
            //匹配获得新的BaseUrl
            val headerValue = headerValues.get(0)
            val newBaseUrl: HttpUrl? =
            if (TextUtils.equals("center",headerValue)) {
                Global.BASE_URL.toHttpUrlOrNull()
            } else if (TextUtils.equals("hyntech",headerValue)) {
                Global.BASE_URL.toHttpUrlOrNull()
            } else if(TextUtils.equals("test",headerValue)){
                "https://api.hyntech.net".toHttpUrlOrNull()
            }else{
                oldHttpUrl
            }

            //重建新的HttpUrl，修改需要修改的url部分
            newBaseUrl?.let {
                val newFullUrl = oldHttpUrl.newBuilder()
                    .scheme(newBaseUrl.scheme) //更换网络协议
                    .host(newBaseUrl.host) //更换主机名
                    .port(newBaseUrl.port) //更换端口
                    .build()
                //重建这个request，通过builder.url(newFullUrl).build()；
                // 然后返回一个response至此结束修改
                LogUtils.logGGQ("Url-intercept->>${newFullUrl}")
                return chain.proceed(builder.url(newFullUrl).build())
            }
        }
        return chain.proceed(request)
    }
}
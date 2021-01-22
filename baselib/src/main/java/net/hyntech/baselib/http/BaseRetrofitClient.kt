package net.hyntech.baselib.http

import net.hyntech.baselib.app.config.Config
import net.hyntech.baselib.http.interceptor.HeaderInterceptor
import net.hyntech.baselib.http.interceptor.SSLSocketClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {
    companion object CLIENT {
        private const val TIME_OUT:Long = 60000L
    }

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            //添加日志拦截
            val logging = HttpLoggingInterceptor()
            if (Config.CONFIG_DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.BASIC
            }
            builder.run {
                //添加日志
                addInterceptor(logging)
                //添加头部参数
                addInterceptor(HeaderInterceptor)
                //连接时间
                connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                //读取时间
                //readTimeout(TIME_OUT, TimeUnit.SECONDS)
                //写入时间
                //writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                // 允许重定向
                followRedirects(true)
                // https支持
                SSLSocketClient.getSSLSocketFactory()?.run {
                    sslSocketFactory(sslSocketFactory, trustManager)
                }
                handleBuilder(builder)
            }
            return builder.build()
        }



    /**
     * 以便对builder可以再扩展
     */
    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)

    open fun <S> getService(serviceClass: Class<S>, baseUrl: String): S {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(jsonConverterFactory)
            .baseUrl(baseUrl)
            .build()
            .create(serviceClass)
    }

    private val jsonConverterFactory by lazy {
        GsonConverterFactory.create()
//        MyGsonConverterFactory.create()
    }
}
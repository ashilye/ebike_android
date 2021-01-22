package net.hyntech.common.model

import net.hyntech.baselib.app.config.Config
import net.hyntech.baselib.http.BaseRetrofitClient
import net.hyntech.common.model.api.ApiService
import net.hyntech.common.model.handler.BaseURLInterceptor
import net.hyntech.common.model.handler.HttpParamsUtils
import net.hyntech.common.model.handler.PublicInterceptor
import okhttp3.OkHttpClient

object RetrofitClient: BaseRetrofitClient() {

    val service by lazy { getService(ApiService::class.java, Config.BASE_URL) }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        //添加多base url
        builder.addInterceptor(BaseURLInterceptor())
        //添加公共请求头参数
        val headers = HttpParamsUtils.addPublicRequestParams(true)
        builder.addInterceptor(PublicInterceptor(headers))
    }
}
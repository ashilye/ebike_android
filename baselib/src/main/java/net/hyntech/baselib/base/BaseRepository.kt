package net.hyntech.baselib.base

import net.hyntech.baselib.utils.LogUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*


abstract class BaseRepository {


    abstract fun getPublicParams(addToken:Boolean): WeakHashMap<String, Any>


    fun params2Body(params: WeakHashMap<String, Any>, addPublicParams:Boolean = true, addToken:Boolean = true): RequestBody {
        //添加公共请求参数
        if(addPublicParams){
            params.putAll(getPublicParams(addToken))
        }

        LogUtils.logGGQ("-------参数明细---------")
        params.forEach {
            LogUtils.logGGQ("提交参数：：->>>${it.key} = ${it.value}")
        }
        LogUtils.logGGQ("----共：${params.size}----")
        return JSONObject(params.toMap()).toString().toRequestBody("application/json;charset=utf-8".toMediaType())
    }

}
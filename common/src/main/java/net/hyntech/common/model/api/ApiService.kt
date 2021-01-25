package net.hyntech.common.model.api

import net.hyntech.common.base.BaseApi
import net.hyntech.common.model.entity.NoticeListEntity
import net.hyntech.common.model.entity.UserEntity
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService: BaseApi {

    @Headers("urlname:hyntech")
    @POST(ApiURL.URL_USER_LOGIN)
    suspend fun loginPhone(@Body requestBody: RequestBody): UserEntity

    @Headers("urlname:hyntech")
    @POST(ApiURL.URL_ALARM_LIST)
    suspend fun getNoticeList(@Body requestBody: RequestBody): NoticeListEntity

}
package net.hyntech.common.app.global

import android.os.Environment


class Constants {

    object Path {
        val IMEI_PATH: String = Environment.getExternalStorageDirectory().toString() + "/android/imei.text"
    }

    object ApiParams {

    }

    object SaveInfoKey {
        const val HAS_WELCOME_USUAL = "welcome_usual"
        const val HAS_WELCOME_POLICE = "welcome_police"
        const val HAS_WELCOME_TEST = "welcome_test"
        const val ACCESS_TOKEN = "accessToken"
    }

    object PayValue {
        const val PAYCHANNEL_WECHAT = "weixin" //微信支付渠道
        const val PAYCHANNEL_ALIPAY = "alipay" //支付宝支付渠道
    }

    object GlobalValue {
        const val BUILD_TYPE = "build_type"
    }

    object BundleKey {
        const val EXTRA_POLICE = "police"
        const val EXTRA_USUAL = "usual"
        const val EXTRA_TITLE = "title"
        const val EXTRA_URL = "url"
        const val EXTRA_OBJ = "obj"
        const val EXTRA_INDEX = "index"
        const val EXTRA_PHONE = "phone"
        const val EXTRA_IDCARD = "idcard"
        const val EXTRA_TYPE = "type"
        const val EXTRA_PRICE = "price"
        const val EXTRA_ID = "id"
        const val EXTRA_LAT = "lat"
        const val EXTRA_LNG = "lng"

    }

}
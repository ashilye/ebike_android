package net.hyntech.common.model.api



class ApiURL {
    companion object{


        // https://api.hyntech.net/app/v1/user/usual/get_user_info.thtml
            private const val EXT = "/app"
//            private const val EXT = "/antitheft"

//        private const val PREFIX_URL = "/antitheft/v1/user/usual"
//        private const val PREFIX_URL = "/antitheft/v1/user/police"


        //常用前缀
//        private const val PREFIX_URL = "${EXT}/v1/user/usual"
        private const val PREFIX_URL = "${EXT}/v1/user/police"


        // 特殊的 获取短信 前缀
//        private const val PREFIX_SMS_URL = "${EXT}/v1/sms/usual"
        private const val PREFIX_SMS_URL = "${EXT}/v1/sms/police"

        // 特殊的 上传图片 前缀
//        private const val PREFIX_OSS_URL = "${EXT}/v1/oss/usual"
        private const val PREFIX_OSS_URL = "${EXT}/v1/oss/police"


        // 特殊的 便民服务 前缀
//        private const val PREFIX_SHOP_URL = "${EXT}/v1/service_shop/usual"
        private const val PREFIX_SHOP_URL = "${EXT}/v1/service_shop/police"

        //用户登录
        const val URL_USER_LOGIN = "${PREFIX_URL}/login.thtml"




        //警用版  报警信息列表 (可搜索)
        const val URL_ALARM_LIST = "${EXT}/v1/alarm_info/police/list.thtml"

        //----------警用版结束--------------------------------------------------------------------------------------------------







    }
}
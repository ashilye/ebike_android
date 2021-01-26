package net.hyntech.common.provider

class ARouterConstants {
    companion object{
        //-----------page-----------
        //APP模块--不同module名字必须不同
        private const val GROUP_COMMON = "/common/"
        private const val GROUP_POLICE = "/police/"
        private const val GROUP_USUAL = "/usual/"
        //Test
        const val TEST_PAGE = GROUP_COMMON + "test"
        //浏览器
        const val BROWSER_PAGE = GROUP_COMMON + "browser"
        //预览大图
        const val PREVIEW_PAGE = GROUP_COMMON + "preview"
        //个人资料
        const val USER_INFO_PAGE = GROUP_COMMON + "user-info"
        //修改密码
        const val RESET_PWD_PAGE = GROUP_COMMON + "reset-pwd"
        //选择位置
        const val BAIDU_MAP_PAGE = GROUP_COMMON + "baidu-map"

        const val USUAL_HOME_PAGE = GROUP_USUAL + "home"
        const val POLICE_HOME_PAGE = GROUP_POLICE + "home"


        //---------obj------
        const val DEMO_OBJ = "/app/demo"
    }
}
package net.hyntech.common.app

import android.app.Application
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate.entity.UpdateError
import com.xuexiang.xupdate.listener.OnUpdateFailureListener
import net.hyntech.baselib.app.BaseApp
import net.hyntech.baselib.app.config.Config
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.R
import net.hyntech.common.app.global.Global
import net.hyntech.common.widget.update.OKHttpUpdateHttpService

abstract class CommonApp:BaseApp() {

    companion object {
        @JvmStatic
        lateinit var instance: CommonApp
            private set
    }

    override fun initApp() {
        super.initApp()
        instance = this
    }

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.common_white, R.color.common_colorTheme)
            ClassicsHeader(context)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> ClassicsFooter(context).setDrawableSize(18f).setAccentColor(
            UIUtils.getColor(R.color.common_colorTheme)) }
    }

    override fun initGlobalSDK(app: Application) {
        super.initGlobalSDK(app)

        initUpdate()
    }


    private fun initUpdate(){
        XUpdate.get()
            .debug(Config.CONFIG_DEBUG)
            .isWifiOnly(false)    //默认设置只在wifi下检查版本更新
            .isGet(true)              //默认设置使用get请求检查版本
            .isAutoMode(false)
            .setOnUpdateFailureListener { error ->
                error?.let {
                    if(it.code != UpdateError.ERROR.CHECK_NO_NEW_VERSION){
                        LogUtils.logGGQ(it.message)
                    }
                }
            }.supportSilentInstall(true)
            .setIUpdateHttpService(OKHttpUpdateHttpService())
            .init(this)
    }

}
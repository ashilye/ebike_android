package net.hyntech.common.app

import android.app.Application
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer

import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate.entity.UpdateError

import net.hyntech.baselib.app.BaseApp
import net.hyntech.baselib.app.config.Config
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.R

import net.hyntech.common.widget.update.OKHttpUpdateHttpService

abstract class CommonApp: BaseApp() {

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
            layout.setPrimaryColorsId(R.color.common_white, R.color.common_color_refresh)
            ClassicsHeader(context).setDrawableArrowSize(14f).setDrawableProgressSize(14f).setTextSizeTitle(14f).setTextSizeTime(10f)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> ClassicsFooter(context).setDrawableSize(14f).setTextSizeTitle(14f).setAccentColor(
            UIUtils.getColor(R.color.common_color_refresh)) }
    }

    override fun initGlobalSDK(app: Application) {
        super.initGlobalSDK(app)
        initUpdate()
    }

    override fun initSDK(app: Application) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)
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
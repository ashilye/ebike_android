package net.hyntech.common.app

import android.app.Application
import com.zy.multistatepage.MultiStateConfig
import com.zy.multistatepage.MultiStatePage
import net.hyntech.baselib.app.BaseApp
import net.hyntech.common.R

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

    override fun initGlobalSDK(app: Application) {
        super.initGlobalSDK(app)

        val config = MultiStateConfig.Builder()
            .alphaDuration(300)
            .errorIcon(R.mipmap.state_error)
            .emptyIcon(R.mipmap.state_empty)
            .emptyMsg("暂无数据！")
            .loadingMsg("请等待...")
            .errorMsg("出错了~")
            .build()

        MultiStatePage.config(config)
    }

}
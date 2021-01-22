package net.hyntech.baselib.app

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import net.hyntech.baselib.app.config.Config
import net.hyntech.baselib.app.manager.AppLifeCycleCallBack
import net.hyntech.baselib.app.manager.ForebackLifeObserver

abstract class BaseApp : Application(), ViewModelStoreOwner {

    abstract fun getAppPackage(): String

    abstract fun initSDK(app:Application)

    companion object {
        @JvmStatic
        lateinit var instance: BaseApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initApp()
        Utils.init(this)
        SPUtils.getInstance(getAppPackage())
        registerLifecycle()
        initGlobalSDK(this)
        initSDK(this)
    }

    open fun initApp(){}

    open fun initGlobalSDK(app: Application) {
        if (Config.CONFIG_DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(app) // 尽可能早，推荐在Application中初始化





        // Crash 捕捉界面   参考链接 https://blog.csdn.net/huangxiaoguo1/article/details/79053197
//        CaocConfig.Builder.create() //程序在后台时，发生崩溃的三种处理方式
//            //BackgroundMode.BACKGROUND_MODE_SHOW_CUSTOM: //当应用程序处于后台时崩溃，也会启动错误页面，
//            //BackgroundMode.BACKGROUND_MODE_CRASH:      //当应用程序处于后台崩溃时显示默认系统错误（一个系统提示的错误对话框），
//            //BackgroundMode.BACKGROUND_MODE_SILENT:     //当应用程序处于后台时崩溃，默默地关闭程序！
//            .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
//            .enabled(true)//false表示对崩溃的拦截阻止。用它来禁用customactivityoncrash框架
//            .trackActivities(true)
//            .minTimeBetweenCrashesMs(2000) //定义应用程序崩溃之间的最短时间，以确定我们不在崩溃循环中。比如：在规定的时间内再次崩溃，框架将不处理，让系统处理！
//            .restartActivity(SplashActivity::class.java)// 重启的 Activity
//            .errorActivity(CustomCrashActivity::class.java)// 错误的 Activity
//            .eventListener(CustomEventListener()) // 设置监听器
//            .apply()
    }

    /**
     * 监听程序崩溃/重启
     */
//    private class CustomEventListener : CustomActivityOnCrash.EventListener {
//        //程序崩溃回调
//        override fun onLaunchErrorActivity() {
//            LogUtils.logGGQ("程序崩溃回调")
//        }
//
//        //重启程序时回调
//        override fun onRestartAppFromErrorActivity() {
//            LogUtils.logGGQ("重启程序时回调")
//        }
//
//        //在崩溃提示页面关闭程序时回调
//        override fun onCloseAppFromErrorActivity() {
//            LogUtils.logGGQ("在崩溃提示页面关闭程序时回调")
//        }
//    }

    private fun registerLifecycle() {
        // 监听所有 Activity 的创建和销毁
        if (Config.CONFIG_ACTIVITY_MANAGER) {
            registerActivityLifecycleCallbacks(AppLifeCycleCallBack())
            ProcessLifecycleOwner.get().lifecycle.addObserver(ForebackLifeObserver())
        }
    }
}
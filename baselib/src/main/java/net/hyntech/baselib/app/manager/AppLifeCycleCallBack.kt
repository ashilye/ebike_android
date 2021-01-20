package net.hyntech.baselib.app.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import net.hyntech.baselib.app.config.AppActivityManager

/**
 * 全局管理 Activity
 */
class AppLifeCycleCallBack : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        AppActivityManager.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        AppActivityManager.add(activity)
    }

    override fun onActivityResumed(activity: Activity) {
    }
}

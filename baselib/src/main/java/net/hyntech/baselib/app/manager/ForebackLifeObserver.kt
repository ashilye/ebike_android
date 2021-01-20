package net.hyntech.baselib.app.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import net.hyntech.baselib.utils.ToastUtils

/**
 * 前后台切换的观察者
 */
class ForebackLifeObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        ToastUtils.showToast("APP进入前台")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        ToastUtils.showToast("APP进入后台")
    }
}
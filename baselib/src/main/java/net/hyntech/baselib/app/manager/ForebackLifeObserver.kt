package net.hyntech.baselib.app.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.ToastUtils

/**
 * 前后台切换的观察者
 */
class ForebackLifeObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        ToastUtils.showShort("APP进入前台")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        ToastUtils.showShort("APP进入后台")
    }
}
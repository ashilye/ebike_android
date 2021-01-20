package net.hyntech.baselib.app.manager

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import net.hyntech.baselib.utils.LogUtils
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 只有一个观察者能收到通知，并且只有明确调用了 setValue 的时候才会发出通知。
 * 反复注册观察并不会触发重新通知。
 */
class SingleLiveEvent<T> : MutableLiveData<T?>() {

    private val pending: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        if (hasActiveObservers()) {
            LogUtils.logGGQ("SingleLiveEvent : Multiple observers registered but only one will be notified of changes.")
        }
//        super.observe(owner, observer)
        super.observe(owner, Observer { t: T? ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }


    @MainThread
    fun call() {
        value = null
    }
}
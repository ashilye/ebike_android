package net.hyntech.baselib.utils

import org.greenrobot.eventbus.EventBus

object EventBusUtils {
    /**
     * 订阅事件
     */
    fun register(subscriber:Any){
        EventBus.getDefault().register(subscriber)
    }

    /**
     * 取消订阅事件
     */
    fun unregister(subscriber:Any){
        if(EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().unregister(subscriber)
        }
    }


    /**
     * 发送普通事件
     */
    fun <T> sendEvent(event: Event<T>){
        EventBus.getDefault().post(event)
    }


    /**
     * 发送黏性事件
     */
    fun <T> sendStickyEvent(event: Event<T>){
        EventBus.getDefault().postSticky(event)
    }

}

class Event<T>(var code:Int,var data:T)
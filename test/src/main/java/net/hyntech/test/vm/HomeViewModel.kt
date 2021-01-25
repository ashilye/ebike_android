package net.hyntech.test.vm

import androidx.databinding.ObservableField
import net.hyntech.baselib.app.manager.SingleLiveEvent
import net.hyntech.baselib.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    val messageCount: ObservableField<String> = ObservableField("100")

    val noticeEvent: SingleLiveEvent<Any> = SingleLiveEvent()



    fun onClickNotice(){
        noticeEvent.call()
    }


}
package net.hyntech.test.vm

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.AppUtils
import net.hyntech.baselib.app.manager.SingleLiveEvent
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.test.R

class AccountViewModel : BaseViewModel() {

    val bgDrawable: ObservableField<Drawable> = ObservableField()
    val verName: ObservableField<String> = ObservableField("")

    val account: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()

    val loginEvent: SingleLiveEvent<Any> = SingleLiveEvent()

    init {
        bgDrawable.set(UIUtils.getDrawable(R.drawable.pic_test))
        verName.set("当前版本：${AppUtils.getAppVersionName()}")
    }

    fun onChooseCompany() {

    }


    fun onForgetPassword() {

    }

    fun onLogin() {
        loginEvent.call()
    }

}
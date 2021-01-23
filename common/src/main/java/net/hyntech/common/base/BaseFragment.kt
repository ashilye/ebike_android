package net.hyntech.common.base

import kotlinx.android.synthetic.main.include_title.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.ext.toVisible
import net.hyntech.common.utils.ToastUtils
import net.hyntech.common.widget.dialog.LoadingDialog
import net.hyntech.baselib.base.BaseFragment as B

abstract class BaseFragment : B() {

    private var loadingDialog: LoadingDialog? = null


    open fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = context?.let {
                LoadingDialog(it)
            }
        }
        loadingDialog?.show()
    }

    open fun dismissLoading() {
        loadingDialog?.run {
            if (isShowing) {
                runBlocking {
                    delay(500L)
                }
                dismiss()
            }
        }
    }

    open fun showToast(s:String?){
        ToastUtils.showToast(s)
    }

    //-------------titleBar--------------------
    inline fun <reified T : BaseFragment> setTitle(txt: String): T {
        tv_title_center?.text = txt
        return this as T
    }

    inline fun <reified T : BaseFragment> setLeftTxt(txt: String): T {
        ll_title_left?.toVisible()
        tv_title_left?.apply {
            this.toVisible()
            this.text = txt
        }
        return this as T
    }

    inline fun <reified T : BaseFragment> setRightTxt(txt: String): T {
        ll_title_right?.toVisible()
        tv_title_right?.apply {
            this.toVisible()
            this.text = txt }
        return this as T
    }

    inline fun <reified T : BaseFragment> onBack(crossinline m: () -> Unit): T {
        ll_title_left?.apply {
            this.toVisible()
            this.setOnClickListener { if(!UIUtils.isFastDoubleClick()){m()} }
        }
        return this as T
    }

    inline fun <reified T : BaseFragment> onSide(crossinline m: () -> Unit): T {
        ll_title_right?.apply {
            this.toVisible()
            this.setOnClickListener { if(!UIUtils.isFastDoubleClick()){m()} }
        }
        return this as T
    }

    //------------end------------------
}
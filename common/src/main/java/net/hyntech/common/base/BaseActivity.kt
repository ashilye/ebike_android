package net.hyntech.common.base

import android.app.Activity
import android.content.Intent
import com.zackratos.ultimatebarx.library.UltimateBarX
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.OnRetryEventListener
import com.zy.multistatepage.bindMultiState
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.ext.toVisible
import net.hyntech.common.utils.ToastUtils
import net.hyntech.common.widget.dialog.LoadingDialog
import net.hyntech.common.widget.state.showEmpty
import net.hyntech.common.widget.state.showError
import net.hyntech.common.widget.state.showLoading
import net.hyntech.common.widget.state.showSuccess
import net.hyntech.baselib.base.BaseActivity as B

abstract class BaseActivity : B() {

    private val multiState by lazy {
        bindMultiState(object : OnRetryEventListener {
            override fun onRetryEvent(container: MultiStateContainer?) {
                onStateRetry(container)
            }
        })
    }

    private var loadingDialog: LoadingDialog? = null

    open fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = this?.let {
                LoadingDialog(this)
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

    open fun showToast(s: String?) {
        ToastUtils.showToast(s)
    }


    open fun onStateLoading() {
        multiState.showLoading()
    }

    open fun onStateEmpty() {
        multiState.showEmpty()
    }

    open fun onStateError() {
        multiState.showError()
    }

    open fun onStateSuccess() {
        multiState.showSuccess()
    }

    open fun onStateRetry(container: MultiStateContainer?) {}


    //-------------titleBar--------------------

    override fun setStatusBarMode(color: Int) {
        super.setStatusBarMode(color)
        UltimateBarX.with(this)
            .color(color)
            .applyStatusBar()
    }

    inline fun <reified T : BaseActivity> setTitle(txt: String): T {
        tv_title_center?.text = txt
        return this as T
    }

    inline fun <reified T : BaseActivity> setLeftTxt(txt: String): T {
        ll_title_left?.toVisible()
        tv_title_left?.apply {
            this.toVisible()
            this.text = txt
        }
        return this as T
    }

    inline fun <reified T : BaseActivity> setRightTxt(txt: String): T {
        ll_title_right?.toVisible()
        tv_title_right?.apply {
            this.toVisible()
            this.text = txt
        }
        return this as T
    }

    inline fun <reified T : BaseActivity> onBack(crossinline m: () -> Unit): T {
        ll_title_left?.apply {
            this.toVisible()
            this.setOnClickListener {
                if (!UIUtils.isFastDoubleClick()) {
                    m()
                }
            }
        }
        return this as T
    }

    inline fun <reified T : BaseActivity> onSide(crossinline m: () -> Unit): T {
        ll_title_right?.apply {
            this.toVisible()
            this.setOnClickListener {
                if (!UIUtils.isFastDoubleClick()) {
                    m()
                }
            }
        }
        return this as T
    }

    //------------end------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            onEventResult(requestCode, data)
        }
    }


    open fun onEventResult(requestCode: Int, data: Intent?) {}

}
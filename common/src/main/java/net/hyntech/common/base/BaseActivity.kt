package net.hyntech.common.base

import android.app.Activity
import android.content.Intent
import android.view.View
import com.zackratos.ultimatebarx.library.UltimateBarX
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.OnRetryEventListener
import com.zy.multistatepage.bindMultiState
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.coroutines.*
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.R
import net.hyntech.common.ext.toVisible
import net.hyntech.common.utils.ToastUtils
import net.hyntech.common.widget.dialog.LoadingDialog
import net.hyntech.common.widget.state.showEmpty
import net.hyntech.common.widget.state.showError
import net.hyntech.common.widget.state.showLoading
import net.hyntech.common.widget.state.showSuccess
import net.hyntech.baselib.base.BaseActivity as B

abstract class BaseActivity : B() {

    open fun hasUsedStateView(): Boolean = false

    /**
     * 多状态视图
     * 如果使用多状态视图,子类必须重写 hasUsedStateView 并返回 true,即可调用 onStateXXX() 等方法
     * 标题栏 不属于多状态视图内的View,布局文件中需要有一个id为 common_container 作为 切换的视图主体
     * 否则为整个 contentView
     */
    private val multiState by lazy {
        if(hasUsedStateView()){
            this.findViewById<View>(R.id.common_container)?.bindMultiState(object : OnRetryEventListener {
                override fun onRetryEvent(container: MultiStateContainer?) {
                    onClickProxy {
                        onStateRetry(container)
                    }
                }
            }) ?: bindMultiState(object : OnRetryEventListener {
                override fun onRetryEvent(container: MultiStateContainer?) {
                    onClickProxy {
                        onStateRetry(container)
                    }
                }
            })
        }else{ null }
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
        if(hasUsedStateView())multiState?.showLoading()
    }

    open fun onStateEmpty() {
        if(hasUsedStateView())multiState?.showEmpty()
    }

    open fun onStateError() {
        if(hasUsedStateView())multiState?.showError()
    }

    open fun onStateSuccess() {
        if(hasUsedStateView()){
//            GlobalScope.launch(Dispatchers.IO) {
//                delay(1000)
//                runOnUiThread {
//                    multiState?.showSuccess()
//                }
//            }
            multiState?.showSuccess()
        }
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
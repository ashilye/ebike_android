package net.hyntech.common.base

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorRes
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.ImmersionOwner
import com.gyf.immersionbar.components.ImmersionProxy
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.R
import net.hyntech.common.ext.toVisible
import net.hyntech.common.utils.ToastUtils
import net.hyntech.common.widget.dialog.LoadingDialog
import net.hyntech.baselib.base.BaseFragment as B


abstract class BaseFragment : B(),ImmersionOwner {

    private val immersionProxy = ImmersionProxy(this)

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

    override fun immersionBarEnabled(): Boolean {
        return hasStatusBarMode()
    }

    override fun onLazyBeforeView() {}

    override fun onLazyAfterView() {}

    override fun onVisible() {}

    override fun onInvisible() {}

    override fun initImmersionBar() {
        if(hasStatusBarMode())setStatusBarMode()
    }

    open fun getStatusBarColor(): Int{
        return R.color.common_trans
    }

    open fun setStatusBarMode() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(getStatusBarColor())
            .init()
    }


    override fun immersionSetUserVisibleHint(isVisibleToUser: Boolean) {
        super.immersionSetUserVisibleHint(isVisibleToUser)
        if(hasStatusBarMode())immersionProxy.setUserVisibleHint(isVisibleToUser)
    }

    override fun immersionOnCreate(savedInstanceState: Bundle?) {
        super.immersionOnCreate(savedInstanceState)
        if(hasStatusBarMode())immersionProxy.onCreate(savedInstanceState)
    }

    override fun immersionOnActivityCreated(savedInstanceState: Bundle?) {
        super.immersionOnActivityCreated(savedInstanceState)
        if(hasStatusBarMode())immersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun immersionOnResume() {
        super.immersionOnResume()
        if(hasStatusBarMode())immersionProxy.onResume()
    }

    override fun immersionOnPause() {
        super.immersionOnPause()
        if(hasStatusBarMode())immersionProxy.onPause()
    }

    override fun immersionOnDestroy() {
        super.immersionOnDestroy()
        if(hasStatusBarMode())immersionProxy.onDestroy()
    }

    override fun immersionOnHiddenChanged(hidden: Boolean) {
        super.immersionOnHiddenChanged(hidden)
        if(hasStatusBarMode())immersionProxy.onHiddenChanged(hidden)
    }

    override fun immersionOnConfigurationChanged(newConfig: Configuration) {
        super.immersionOnConfigurationChanged(newConfig)
        if(hasStatusBarMode())immersionProxy.onConfigurationChanged(newConfig)
    }

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
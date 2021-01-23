package net.hyntech.baselib.base

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import net.hyntech.baselib.utils.UIUtils

abstract class BaseFragment : Fragment(), IView {


    private var isFirst: Boolean = true

    open fun hasStatusBarMode(): Boolean = false

    abstract fun getLayoutId(): Int

    abstract fun initData(savedInstanceState: Bundle?): Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setContentLayout(inflater, container, savedInstanceState)
    }

    open fun setContentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(getLayoutId(), container, false)
    }

    fun onClickProxy(m: () -> Unit) {
        if (!UIUtils.isFastDoubleClick()) {
            m()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        immersionOnActivityCreated(savedInstanceState)
        this.initData(savedInstanceState)
        this.onVisible()
    }

    override fun onResume() {
        super.onResume()
        immersionOnResume()
        this.onVisible()
    }

    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && this.isFirst) {
            this.isFirst = false
            lazyLoadData()
        } else if (!this.isFirst) {
            refreshData()
        }
    }

    open fun lazyLoadData() {}
    open fun refreshData() {}


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        immersionSetUserVisibleHint(isVisibleToUser)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersionOnCreate(savedInstanceState)
    }


    override fun onPause() {
        super.onPause()
        immersionOnPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        immersionOnDestroy()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        immersionOnHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        immersionOnConfigurationChanged(newConfig)
    }


    open fun immersionSetUserVisibleHint(isVisibleToUser: Boolean) {}
    open fun immersionOnCreate(savedInstanceState: Bundle?) {}
    open fun immersionOnActivityCreated(savedInstanceState: Bundle?) {}
    open fun immersionOnResume() {}
    open fun immersionOnPause() {}
    open fun immersionOnDestroy() {}
    open fun immersionOnHiddenChanged(hidden: Boolean) {}
    open fun immersionOnConfigurationChanged(newConfig: Configuration) {}
}
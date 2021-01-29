package net.hyntech.baselib.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import net.hyntech.baselib.utils.UIUtils


abstract class BaseFragment : Fragment(), IView {


    /// 是否第一次加载 用于懒加载
    private var isFirst: Boolean = true

    open fun hasStatusBarMode(): Boolean = false

    abstract fun getLayoutId(): Int

    abstract fun initData(savedInstanceState: Bundle?): Unit

    open fun initView(savedInstanceState: Bundle?){}

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

    open fun setStatusBarMode(color: Int = Color.TRANSPARENT) {}

    fun onClickProxy(m: () -> Unit) {
        if (!UIUtils.isFastDoubleClick()) {
            m()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (hasStatusBarMode()) {
            setStatusBarMode()
        }
        this.initView(savedInstanceState)
        this.initData(savedInstanceState)
        this.onVisible()
    }


    override fun onResume() {
        super.onResume()
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

}
package net.hyntech.common.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.common.R

abstract class BaseListViewActivity<VB : ViewDataBinding, VM : BaseViewModel>:BaseViewActivity<VB,VM>(){

    protected var refreshLayout:SmartRefreshLayout? = null

    open fun isEnableRefresh():Boolean = true
    open fun isEnableLoadMore():Boolean = true

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        refreshLayout = this.findViewById<SmartRefreshLayout>(R.id.common_refreshLayout)


        refreshLayout?.setEnableRefresh(isEnableRefresh())//是否启用下拉刷新功能
        refreshLayout?.setEnableLoadMore(isEnableLoadMore())//是否启用上拉加载功能

        refreshLayout?.setOnRefreshListener { ref ->
            onRefreshData()
        }

        refreshLayout?.setOnLoadMoreListener { ref ->
            onLoadMoreData()
        }
    }


    open fun onRefreshData(){}

    open fun onLoadMoreData(){}

    //结束下拉刷新
    protected fun finishRefresh() {
        refreshLayout?.let {
            if (it.isRefreshing) it.finishRefresh(300)
        }
    }

    //结束加载更多
    protected fun finishLoadMore() {
        refreshLayout?.let {
            if (it.isLoading) it.finishLoadMore(300)
        }
    }
}
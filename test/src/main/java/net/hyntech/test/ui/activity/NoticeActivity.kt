package net.hyntech.test.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zy.multistatepage.MultiStateContainer
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.base.BaseAdapter
import net.hyntech.common.base.BaseListViewActivity
import net.hyntech.common.base.BaseViewActivity
import net.hyntech.common.model.entity.NoticeListEntity
import net.hyntech.test.R
import net.hyntech.test.databinding.ActivityNoticeBinding
import net.hyntech.test.ui.adapter.NoticeAdapter
import net.hyntech.test.vm.NoticeViewModel

class NoticeActivity : BaseListViewActivity<ActivityNoticeBinding, NoticeViewModel>() {

    private val noticeAdapter by lazy { NoticeAdapter(this).apply {
        this.setListener(object : BaseAdapter.OnClickListener<NoticeListEntity.AlarmInfoListBean>{
            override fun onItemClick(pos: Int, item: NoticeListEntity.AlarmInfoListBean?) {
                item?.let {
                    showToast(it.ebikeNo)
                } } }) } }


    private var keyword: String = ""

    private val viewModel by viewModels<NoticeViewModel>()

    override fun hasUsedStateView(): Boolean = true

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun getLayoutId(): Int = R.layout.activity_notice

    override fun initData(savedInstanceState: Bundle?) {
        setTitle<NoticeActivity>("消息列表").onBack<NoticeActivity> {
            onFinish()
        }

        viewModel.defUI.showDialog.observe(this, Observer {
            showLoading()
        })

        viewModel.defUI.dismissDialog.observe(this, Observer {
            dismissLoading()
        })

        viewModel.defUI.toastEvent.observe(this, Observer {
            showToast(it)
        })

        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = noticeAdapter

        viewModel.defUI.stateViewEvent.observe(this, Observer {
            LogUtils.logGGQ("多状态视图--->${it}")
            when(it){
                0 -> onStateSuccess()
                1 -> onStateLoading()
                2 -> onStateEmpty()
                3 -> onStateError()
            }
        })

        viewModel.noticeList.observe(this, Observer {
            LogUtils.logGGQ("初始数据--->${it.size}")
            noticeAdapter.setData(it)
        })

        viewModel.noticeListRefresh.observe(this, Observer {
            LogUtils.logGGQ("刷新数据--->${it}")
            noticeAdapter.setData(it)
            finishRefresh()
        })

        viewModel.noticeListLoadMore.observe(this, Observer {
            LogUtils.logGGQ("更多数据--->${it}")
            noticeAdapter.updataList(it)
            finishLoadMore()
        })

        viewModel.getNoticeList(keyword)
    }


    override fun onStateRetry(container: MultiStateContainer?) {
        super.onStateRetry(container)
        viewModel.getNoticeList(keyword)
    }

    override fun onRefreshData() {
        super.onRefreshData()
        viewModel.getNoticeListRefresh(keyword)
    }


    override fun onLoadMoreData() {
        super.onLoadMoreData()
        viewModel.getNoticeListLoadMore(keyword)
    }

}
package net.hyntech.test.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zackratos.ultimatebarx.library.UltimateBarX
import com.zy.multistatepage.MultiStateContainer
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.base.ViewState
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.base.BaseAdapter
import net.hyntech.common.base.BaseListViewFragment
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.common.model.entity.NoticeListEntity
import net.hyntech.common.widget.decoration.SimpleItemDecoration
import net.hyntech.test.R
import net.hyntech.common.R as CR
import net.hyntech.test.databinding.FragmentOrgBinding
import net.hyntech.test.ui.adapter.NoticeAdapter
import net.hyntech.test.vm.AccountViewModel

class OrgFragment : BaseListViewFragment<FragmentOrgBinding, AccountViewModel>() {

    private val noticeAdapter by lazy { NoticeAdapter(requireActivity()).apply {
        this.setListener(object : BaseAdapter.OnClickListener<NoticeListEntity.AlarmInfoListBean>{
            override fun onItemClick(pos: Int, item: NoticeListEntity.AlarmInfoListBean?) {
                item?.let {
                    showToast(it.ebikeNo)
                } } }) } }


    private var keyword: String = ""

    private val viewModel by viewModels<AccountViewModel>()

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun hasNavController(): Boolean = true

    override fun hasStatusBarMode(): Boolean = true

    override fun hasUsedStateView(): Boolean = true

    override fun setStatusBarMode(color: Int) {
        UltimateBarX.with(this)
            .color(UIUtils.getColor(CR.color.common_toolbar))
            .applyStatusBar()
    }

    override fun getLayoutId(): Int = R.layout.fragment_org

    override fun initData(savedInstanceState: Bundle?) {
        setTitle<OrgFragment>("选择组织").onBack<OrgFragment> {
            onPopBack()
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

        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView?.addItemDecoration(SimpleItemDecoration(requireContext()))
        recyclerView?.adapter = noticeAdapter

        viewModel.defUI.stateViewEvent.observe(this, Observer {
            LogUtils.logGGQ("多状态视图--->${it}")
            when(it){
                ViewState.SUCCESS -> onStateSuccess()
                ViewState.LOADING -> onStateLoading()
                ViewState.EMPTY -> onStateEmpty()
                ViewState.ERROR -> onStateError()
            }
        })

        viewModel.noticeList.observe(this, Observer {
            LogUtils.logGGQ("初始数据--->${it.size}")
            noticeAdapter.setData(it)
        })

        viewModel.noticeListRefresh.observe(this, Observer {
            LogUtils.logGGQ("刷新数据--->${it.size}")
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
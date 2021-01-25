package net.hyntech.test.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.base.BaseAdapter
import net.hyntech.common.base.BaseViewActivity
import net.hyntech.common.model.entity.NoticeListEntity
import net.hyntech.test.R
import net.hyntech.test.databinding.ActivityNoticeBinding
import net.hyntech.test.ui.adapter.NoticeAdapter
import net.hyntech.test.vm.NoticeViewModel

class NoticeActivity : BaseViewActivity<ActivityNoticeBinding, NoticeViewModel>() {

    private val noticeAdapter by lazy { NoticeAdapter(this).apply {
        this.setListener(object : BaseAdapter.OnClickListener<NoticeListEntity.AlarmInfoListBean>{
            override fun onItemClick(pos: Int, item: NoticeListEntity.AlarmInfoListBean?) {

            }
        })
    } }


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
            LogUtils.logGGQ("初始数据--->${it}")
        })

        viewModel.getNoticeList(keyword)
    }
}
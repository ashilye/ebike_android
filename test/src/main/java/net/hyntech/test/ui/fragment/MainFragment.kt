package net.hyntech.test.ui.fragment

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.common.ext.isFastClick
import net.hyntech.test.R
import net.hyntech.test.databinding.FragmentMainBinding
import net.hyntech.test.ui.activity.NoticeActivity
import net.hyntech.test.vm.HomeViewModel


class MainFragment(val viewModel: HomeViewModel) : BaseViewFragment<FragmentMainBinding, HomeViewModel>() {

    companion object {
        fun getInstance(viewModel: HomeViewModel): MainFragment {
            return MainFragment(viewModel)
        }
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun initData(savedInstanceState: Bundle?) {

        view?.let { v ->
            v.findViewById<TextView>(R.id.tv_title_center)?.text = "首页"
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

        viewModel.noticeEvent.observe(this, Observer {
            (activity as? BaseActivity)?.onStartActivity(NoticeActivity::class.java)
        })
    }
}
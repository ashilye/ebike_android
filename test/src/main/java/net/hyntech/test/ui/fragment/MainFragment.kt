package net.hyntech.test.ui.fragment

import android.os.Bundle
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.test.R
import net.hyntech.test.databinding.FragmentMainBinding
import net.hyntech.test.vm.HomeViewModel


class MainFragment(val viewModel: HomeViewModel) : BaseViewFragment<FragmentMainBinding, HomeViewModel>() {

    companion object {
        fun getInstance(viewModel: HomeViewModel): MainFragment {
            return MainFragment(viewModel)
        }
    }

    override fun bindViewModel(): BaseViewModel {
        binding.viewModel = viewModel
        return viewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun initData(savedInstanceState: Bundle?) {

    }
}
package net.hyntech.test.ui.fragment

import android.os.Bundle
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.test.R
import net.hyntech.test.databinding.FragmentMineBinding
import net.hyntech.test.vm.HomeViewModel

class MineFragment(val viewModel: HomeViewModel):BaseViewFragment<FragmentMineBinding,HomeViewModel>() {

    companion object {
        fun getInstance(viewModel: HomeViewModel): MineFragment {
            return MineFragment(viewModel)
        }
    }

    override fun bindViewModel(): BaseViewModel {
        binding.viewModel = viewModel
        return viewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine


    override fun initData(savedInstanceState: Bundle?) {

    }
}
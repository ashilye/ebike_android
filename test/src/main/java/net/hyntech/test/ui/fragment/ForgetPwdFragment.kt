package net.hyntech.test.ui.fragment

import android.os.Bundle
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.test.R
import net.hyntech.test.databinding.FragmentForgetPwdBinding
import net.hyntech.test.vm.AccountViewModel

class ForgetPwdFragment : BaseViewFragment<FragmentForgetPwdBinding, AccountViewModel>() {

    private val viewModel by viewModels<AccountViewModel>()


    override fun bindViewModel(): BaseViewModel {
        binding.viewModel = viewModel
        return viewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_forget_pwd

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun hasStatusBarMode(): Boolean = true

    override fun getStatusBarColor(): Int = R.color.common_toolbar
}
package net.hyntech.test.ui.activity

import android.os.Bundle
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.common.base.BaseViewActivity
import net.hyntech.test.R
import net.hyntech.test.databinding.ActivityLoginBinding
import net.hyntech.test.vm.AccountViewModel

class LoginActivity:BaseViewActivity<ActivityLoginBinding,AccountViewModel>() {
    private val viewModel by viewModels<AccountViewModel>()
    override fun bindViewModel(): BaseViewModel {
        binding.viewModel = viewModel
        return viewModel
    }

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initData(savedInstanceState: Bundle?) {

    }
}
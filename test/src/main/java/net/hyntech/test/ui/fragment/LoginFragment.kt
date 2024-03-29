package net.hyntech.test.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.common.ext.afterTextChanged
import net.hyntech.test.R
import net.hyntech.test.databinding.FragmentLoginBinding
import net.hyntech.test.ui.activity.HomeActivity
import net.hyntech.test.vm.AccountViewModel

class LoginFragment : BaseViewFragment<FragmentLoginBinding, AccountViewModel>() {

    private val viewModel by viewModels<AccountViewModel>()

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun hasNavController(): Boolean = true

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun hasStatusBarMode(): Boolean = true

    override fun setStatusBarMode(color: Int) {
        UltimateBarX.with(this)
            .fitWindow(false)
            .color(color)
            .applyStatusBar()
    }

    override fun initData(savedInstanceState: Bundle?) {

        viewModel.defUI.showDialog.observe(this, Observer {
            showLoading()
        })

        viewModel.defUI.dismissDialog.observe(this, Observer {
            dismissLoading()
        })

        viewModel.defUI.toastEvent.observe(this, Observer {
            showToast(it)
        })

        viewModel.loginEvent.observe(this, Observer {
            (activity as BaseActivity).onStartActivity(HomeActivity::class.java,isFinish = true)
        })

        viewModel.companyEvent.observe(this, Observer {
            navController?.navigate(R.id.action_loginFragment_to_orgFragment)
        })

        viewModel.forgetPwdEvent.observe(this, Observer {
            navController?.navigate(R.id.action_loginFragment_to_forgetPwdFragment)
        })

        binding.etAccount.afterTextChanged {
            viewModel.account.set(it)
        }

        binding.etPassword.afterTextChanged {
            viewModel.password.set(it)
        }
    }
}
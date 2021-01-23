package net.hyntech.test.ui.fragment

import android.os.Bundle
import com.blankj.utilcode.util.BarUtils
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.test.R
import net.hyntech.test.databinding.FragmentOrgBinding
import net.hyntech.test.vm.AccountViewModel

class OrgFragment : BaseViewFragment<FragmentOrgBinding, AccountViewModel>() {

    private val viewModel by viewModels<AccountViewModel>()

    override fun bindViewModel(): BaseViewModel {
        binding.viewModel = viewModel
        return viewModel
    }

    override fun hasStatusBarMode(): Boolean = true

    override fun getLayoutId(): Int = R.layout.fragment_org

    override fun initData(savedInstanceState: Bundle?) {
    }



}
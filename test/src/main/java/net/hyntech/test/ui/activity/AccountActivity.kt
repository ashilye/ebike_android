package net.hyntech.test.ui.activity

import android.os.Bundle
import net.hyntech.common.base.BaseActivity
import net.hyntech.test.R

class AccountActivity : BaseActivity() {

    override fun hasStatusBarMode(): Boolean = true

    override fun getLayoutId(): Int = R.layout.activity_account

    override fun initData(savedInstanceState: Bundle?) {

    }
}
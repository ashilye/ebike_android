package net.hyntech.test.ui.activity

import android.os.Bundle
import net.hyntech.common.base.BaseActivity
import net.hyntech.test.R
import net.hyntech.common.R as CR

class WelcomeActivity:BaseActivity() {

    private val list by lazy { arrayOf(
        CR.drawable.welcome1,
        CR.drawable.welcome2,
        CR.drawable.welcome3,
        CR.drawable.welcome4)
    }

    override fun getLayoutId(): Int = R.layout.activity_welcome

    override fun initData(savedInstanceState: Bundle?) {

    }
}
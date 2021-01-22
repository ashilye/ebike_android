package net.hyntech.test.ui.activity

import android.os.Bundle
import net.hyntech.common.base.BaseActivity
import net.hyntech.test.R

class SplashActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initData(savedInstanceState: Bundle?) {
        onStartActivity(WelcomeActivity::class.java,isFinish = true)
    }
}

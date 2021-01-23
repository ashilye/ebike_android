package net.hyntech.test.ui.activity

import android.os.Bundle
import com.blankj.utilcode.util.SPUtils
import kotlinx.android.synthetic.main.activity_welcome.*
import net.hyntech.baselib.app.BaseApp
import net.hyntech.common.app.global.Constants
import net.hyntech.common.base.BaseActivity
import net.hyntech.test.R
import net.hyntech.test.ui.adapter.WelcomeAdapter
import net.hyntech.common.R as CR

class WelcomeActivity : BaseActivity() {

    private val list by lazy {
        arrayOf(
            CR.drawable.welcome1,
            CR.drawable.welcome2,
            CR.drawable.welcome3,
            CR.drawable.welcome4
        )
    }

    override fun getLayoutId(): Int = R.layout.activity_welcome

    override fun initData(savedInstanceState: Bundle?) {
        vp.adapter = WelcomeAdapter(this, list, object : WelcomeAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                if (list.size == (position + 1)) {
                    launchTarget()
                }
            }
        })
    }

    private fun launchTarget() {
        SPUtils.getInstance(BaseApp.instance.getAppPackage())
            .put(Constants.SaveInfoKey.HAS_WELCOME_TEST, true)
        onStartActivity(AccountActivity::class.java, isFinish = true)
    }
}
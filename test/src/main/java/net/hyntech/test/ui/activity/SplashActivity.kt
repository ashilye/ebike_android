package net.hyntech.test.ui.activity

import android.os.Bundle
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import net.hyntech.baselib.app.BaseApp
import net.hyntech.baselib.utils.PermissionUtil
import net.hyntech.baselib.utils.RequestPermission
import net.hyntech.common.app.global.Constants
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.widget.dialog.CommonDialog
import net.hyntech.test.R

class SplashActivity : BaseActivity() {

    private var dialog: CommonDialog? = null

    private val rxPermissions: RxPermissions = RxPermissions(this)

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initData(savedInstanceState: Bundle?) {
        applyPermissions()
    }

    override fun onRestart() {
        super.onRestart()
        applyPermissions()
    }

    private fun applyPermissions() {
        PermissionUtil.applyPermissions(object : RequestPermission {
            override fun onRequestPermissionSuccess() {
                launchTarget()
            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                showLoading()
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                showLoading()
            }
        }, rxPermissions)
    }

    private fun launchTarget() {
        if(SPUtils.getInstance(BaseApp.instance.getAppPackage()).getBoolean(Constants.SaveInfoKey.HAS_WELCOME_TEST,false)){
            onStartActivity(AccountActivity::class.java,isFinish = true)
        }else{
            onStartActivity(WelcomeActivity::class.java,isFinish = true)
        }
    }

    override fun showLoading() {
        dialog ?: CommonDialog(this,
            "权限申请",
            "请开启必要权限，否则您无法正常使用一些功能",
            "不同意",
            "前往开启",object :CommonDialog.OnClickListener{
                override fun onCancleClick() {
                    onFinish()
            }
                override fun onConfirmClick() {
                    AppUtils.launchAppDetailsSettings()
                }
            }).let {
            if (!it.isShowing) it.show()
        }
    }

    override fun dismissLoading() {
        dialog?.cancel()
    }
}

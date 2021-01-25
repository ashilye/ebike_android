package net.hyntech.test.ui.activity

import android.os.Bundle
import androidx.navigation.Navigation
import net.hyntech.common.base.BaseActivity
import net.hyntech.test.R

class AccountActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_account

    override fun initData(savedInstanceState: Bundle?) {

        val navController = Navigation.findNavController(this, R.id.fragment)
//        navController.setGraph(R.navigation.navigation_account,intent?.extras)
        navController.setGraph(R.navigation.navigation_account)
        navController.navigateUp()


    }
}
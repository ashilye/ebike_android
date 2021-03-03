package net.hyntech.test.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.king.zxing.CameraScan
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.app.global.Code
import net.hyntech.common.app.global.Constants
import net.hyntech.common.base.BaseViewActivity
import net.hyntech.common.common.MyFragmentStateAdapter
import net.hyntech.test.R
import net.hyntech.test.databinding.ActivityHomeBinding
import net.hyntech.test.ui.fragment.MainFragment
import net.hyntech.test.ui.fragment.MineFragment
import net.hyntech.test.vm.HomeViewModel

class HomeActivity : BaseViewActivity<ActivityHomeBinding, HomeViewModel>() {

    private var lastBackPressedMillis: Long = 0


    private val viewModel by viewModels<HomeViewModel>()

    private val list by lazy { listOf(
        MainFragment.getInstance(viewModel),
        MineFragment.getInstance(viewModel)
    ) }

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }


    override fun initData(savedInstanceState: Bundle?) {
        binding.pager.isUserInputEnabled = false
        val adapter: MyFragmentStateAdapter = MyFragmentStateAdapter(this, list)
        binding.pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.pager.adapter = adapter
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bnav.menu.getItem(position).isChecked = true
            }
        })
        binding.bnav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.item_nav_main ->{
                    binding.pager.currentItem = 0
                }
                R.id.item_nav_mine ->{
                    binding.pager.currentItem = 1
                }
            }
            false
        }

        viewModel.defUI.showDialog.observe(this, Observer {
            showLoading()
        })

        viewModel.defUI.dismissDialog.observe(this, Observer {
            dismissLoading()
        })

        viewModel.defUI.toastEvent.observe(this, Observer {
            showToast(it)
        })




    }

    override fun hasEventKeyBack(): Boolean = true


    override fun onKeyBack(keyCode: Int) {
        if (lastBackPressedMillis + 2000 > System.currentTimeMillis()) {
            //moveTaskToBack(true)
            this@HomeActivity.finish()
        } else {
            lastBackPressedMillis = System.currentTimeMillis()
            showToast("再按一次退出程序")
        }
        super.onKeyBack(keyCode)
    }

    override fun onEventResult(requestCode: Int, data: Intent?) {
        super.onEventResult(requestCode, data)
        when(requestCode){
            Code.RequestCode.REQUEST_CODE_BAIDUMAP ->{
                data?.let {
                    val address = it.getStringExtra(Constants.BundleKey.EXTRA_ADDRESS)
                    //纬度
                    val lat = it.getStringExtra(Constants.BundleKey.EXTRA_LAT)
                    //经度
                    val lng = it.getStringExtra(Constants.BundleKey.EXTRA_LNG)
                    LogUtils.logGGQ("地址:${address}")
                    LogUtils.logGGQ("纬度:${lat}")
                    LogUtils.logGGQ("经度:${lng}")
                    showToast(address)
                }
            }
        }
    }
}
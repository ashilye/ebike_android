package net.hyntech.test.ui.activity

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import net.hyntech.baselib.base.BaseViewModel
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

    override fun bindViewModel(): BaseViewModel {
        binding.viewModel = viewModel
        return viewModel
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
}
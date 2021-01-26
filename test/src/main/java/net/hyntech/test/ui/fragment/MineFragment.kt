package net.hyntech.test.ui.fragment

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.common.app.global.Constants
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.common.provider.ARouterConstants
import net.hyntech.test.R
import net.hyntech.test.databinding.FragmentMineBinding
import net.hyntech.test.vm.HomeViewModel

class MineFragment(val viewModel: HomeViewModel):BaseViewFragment<FragmentMineBinding,HomeViewModel>() {

    companion object {
        fun getInstance(viewModel: HomeViewModel): MineFragment {
            return MineFragment(viewModel)
        }
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine


    override fun initData(savedInstanceState: Bundle?) {

        setTitle<MineFragment>("我的")

        viewModel.defUI.showDialog.observe(this, Observer {
            showLoading()
        })

        viewModel.defUI.dismissDialog.observe(this, Observer {
            dismissLoading()
        })

        viewModel.defUI.toastEvent.observe(this, Observer {
            showToast(it)
        })

        binding.btnPreview.setOnClickListener {


            val bundle = Bundle()
            val pos:Int = 2
            val list = java.util.ArrayList<String>()
            list.add("http://oss-public.hyntech.net/appUpload/20201202/2018LENoOyAYmq/f6dfa0aad1dc4672b2725b55546f147e.jpg")
            list.add("http://oss-public.hyntech.net/appUpload/20201202/2018LENoOyAYmq/41fd054c25ea40ec80b621215117d0b7.jpg")
            list.add("http://oss-public.hyntech.net/appUpload/20201202/2018LENoOyAYmq/7f6dbad84fbe4c498f147f53468edb63.jpg")
            list.add("http://oss-public.hyntech.net/appUpload/20201202/2018LENoOyAYmq/6ab148ac98f34e738122aa8e38b9947a.jpg")
            bundle.putSerializable(Constants.BundleKey.EXTRA_LIST,list)
            bundle.putInt(Constants.BundleKey.EXTRA_INDEX,pos)

            ARouter.getInstance().build(ARouterConstants.PREVIEW_PAGE)
                .with(bundle)
                .navigation()
        }

    }
}
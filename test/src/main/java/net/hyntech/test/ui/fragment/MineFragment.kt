package net.hyntech.test.ui.fragment

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.PathUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate._XUpdate
import com.xuexiang.xupdate.service.OnFileDownloadListener
import net.hyntech.baselib.app.config.AppActivityManager
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.utils.PermissionUtil
import net.hyntech.baselib.utils.RequestPermission
import net.hyntech.common.app.global.Constants
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.common.model.entity.AppUpdateEntity
import net.hyntech.common.provider.ARouterConstants
import net.hyntech.common.widget.dialog.CommonDialog
import net.hyntech.common.widget.dialog.LiveDialog
import net.hyntech.common.widget.dialog.PictureOptionDialog
import net.hyntech.common.widget.update.UpdateDialog
import net.hyntech.test.R
import net.hyntech.test.databinding.FragmentMineBinding
import net.hyntech.test.vm.HomeViewModel
import java.io.File

class MineFragment(val viewModel: HomeViewModel):BaseViewFragment<FragmentMineBinding,HomeViewModel>() {


    private val commonDialog by lazy { CommonDialog(requireActivity(),content = "提示内容",listener = object :
        CommonDialog.OnClickListener{
        override fun onConfirmClick() {
          showToast("确定")
        } }) }

    private val pictureDialog by lazy {
        PictureOptionDialog(requireActivity(),object :PictureOptionDialog.OnClickListener{
            override fun onCameraClick() {

            }

            override fun onPhotoClick() {

            } })
    }


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

        binding.btnDialog.setOnClickListener {
            commonDialog.show()
        }

        binding.btnPicture.setOnClickListener {
            pictureDialog.show()
        }

        binding.btnPermissions.setOnClickListener {
            PermissionUtil.applyCamera(object : RequestPermission {
                override fun onRequestPermissionSuccess() {
                    //showToast("权限通过")
                    showPermissionsDialog()
                }

                override fun onRequestPermissionFailure(permissions: List<String>) {
                    showPermissionsDialog()
                }

                override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                    showPermissionsDialog()
                }
            }, rxPermissions)
        }

        binding.btnUpdate.setOnClickListener {
            val data = AppUpdateEntity("type","version",100,"https://www.hyntech.net/appdownload/download/app-release-police-1.0.8-8.apk","content","time")
            showUpdateDialog(data)
        }
    }

    private var updateDialog: UpdateDialog? = null

    private fun showUpdateDialog(data:AppUpdateEntity){
        if (updateDialog == null) {
            updateDialog = UpdateDialog(requireActivity(), data, object : UpdateDialog.OnClickListener {
                override fun onConfirmClick(url: String) {
                    onDownloadApk(url)
                }
                override fun onCancelClick() {
                    super.onCancelClick()
                    showToast("取消更新")
                }
            },isCancelable = true)
        }
        updateDialog?.let {
            it.show()
        }
    }

    private fun onDownloadApk(url: String) {
        XUpdate.newBuild(requireActivity())
            .apkCacheDir(PathUtils.getExternalDownloadsPath()) //设置下载缓存的根目录
            .build()
            .download(url,object : OnFileDownloadListener {
                override fun onStart() {}
                override fun onProgress(progress: Float, total: Long) {
                    updateDialog?.setProgress(progress)
                }
                override fun onError(throwable: Throwable?) {}
                override fun onCompleted(file: File?): Boolean {
                    updateDialog?.cancel()
                    file?.let {
                        installApk(it)
                        return true
                    }
                    return false
                }
            })
    }

    private fun installApk(file: File){
        //填写文件所在的路径
        _XUpdate.startInstallApk(requireActivity(), file)
    }
    //----------------update---------------------

    private val rxPermissions: RxPermissions by lazy { RxPermissions(this) }




    // dialogFragment
//    private val permissionsDialog by lazy {
//        LiveDialog(listener = object : LiveDialog.OnClickListener {
//            override fun onConfirmClick() {
//                AppUtils.launchAppDetailsSettings()
//            }
//
//            override fun onCancleClick() {
//                super.onCancleClick()
//                showToast("取消")
//            }
//        })
//    }

    // dialog
    private var permissionsDialog:CommonDialog? = null
    private fun showPermissionsDialog(){
        permissionsDialog ?: CommonDialog(requireActivity(),
            "权限申请",
            "请开启相机权限，否则您无法正常使用该功能",
            "不同意",
            "前往开启",object :CommonDialog.OnClickListener{
                override fun onCancelClick() {
                    showToast("取消")
                }
                override fun onConfirmClick() {
                    AppUtils.launchAppDetailsSettings()
                }
            },isCancelable = true).let {
            if (!it.isShowing) it.show()
        }
    }







//    private fun showPermissionsDialog(){
//        permissionsDialog.apply {
//            this.showNow(this@MineFragment.childFragmentManager,"PermissionsDialog")
//            this.setTitleText("权限申请")
//            this.setContentText("相机权限")
//        }
//    }
}
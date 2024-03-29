package net.hyntech.test.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.Fade
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.VibrateUtils
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate._XUpdate
import com.xuexiang.xupdate.service.OnFileDownloadListener
import net.hyntech.baselib.app.config.AppActivityManager
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.baselib.utils.PermissionUtil
import net.hyntech.baselib.utils.RequestPermission
import net.hyntech.common.app.global.Code
import net.hyntech.common.app.global.Constants
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.base.BaseAdapter
import net.hyntech.common.base.BaseViewFragment
import net.hyntech.common.ext.loadImage
import net.hyntech.common.model.entity.AppUpdateEntity
import net.hyntech.common.provider.ARouterConstants
import net.hyntech.common.ui.activity.MyScanKitActivity
import net.hyntech.common.ui.adapter.MyImgAdapter
import net.hyntech.common.utils.RegexUtils
import net.hyntech.common.widget.dialog.CommonDialog
import net.hyntech.common.widget.dialog.LiveDialog
import net.hyntech.common.widget.dialog.PictureOptionDialog
import net.hyntech.common.widget.picture.GlideEngine
import net.hyntech.common.widget.update.UpdateDialog
import net.hyntech.test.R
import net.hyntech.test.databinding.FragmentMineBinding
import net.hyntech.test.vm.HomeViewModel
import org.w3c.dom.Text
import java.io.File

class MineFragment(val viewModel: HomeViewModel):BaseViewFragment<FragmentMineBinding,HomeViewModel>() {


    private val commonDialog by lazy { CommonDialog(requireActivity(),content = "提示内容",listener = object :
        CommonDialog.OnClickListener{
        override fun onConfirmClick() {
          showToast("确定")
        } }) }

    private val pictureDialog by lazy {
        PictureOptionDialog(requireActivity(),object :PictureOptionDialog.OnClickListener{
            override fun onCameraClick(type: Int) {
                //打开相机
//                openCamer(type)
                applyPermissions(type)
            }

            override fun onPhotoClick(type: Int) {
                //打开相册
//                openPhoto(type)
                applyPermissions(type)
            }
        }) }

    private fun openCamer(type:Int) {
        showToast("访问相机")
        PictureSelector.create(this)
            .openCamera(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .isCompress(true)// 是否压缩
            .forResult(object :OnResultCallbackListener<LocalMedia>{
                override fun onResult(result: MutableList<LocalMedia>?) {
                    result?.last()?.let {
                        var compressPath:String? = ""
                        if(it.isCompressed && !TextUtils.isEmpty(it.compressPath))
                            compressPath = it.compressPath
                        else{
                            compressPath = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                it.androidQToPath
                            }else{
                                it.path
                            }
                        }
                        //上传图片
                        if(!TextUtils.isEmpty(compressPath)){
                            //uploadImage(compressPath)
                            showToast("type->${type}-图片大小：${FileUtils.getSize(compressPath)}")
                            binding.ivPicture.loadImage(compressPath)
                        }else{
                            showToast("拍照出错,请重新拍照！")
                        }
                    }?:let {
                        showToast("拍照出错,请重新拍照！")
                    }
                }

                override fun onCancel() {
                    showToast("取消")
                }
            })
    }

    private fun openPhoto(type:Int) {
        showToast("访问相册")
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .imageEngine(GlideEngine.createGlideEngine())
            .selectionMode(PictureConfig.SINGLE)
            .isCamera(false)
            .isCompress(true)// 是否压缩
            .isGif(false)
            .forResult(object :OnResultCallbackListener<LocalMedia>{
                override fun onResult(result: MutableList<LocalMedia>?) {
                    result?.last()?.let {
                        var compressPath:String? = ""
                        if(it.isCompressed && !TextUtils.isEmpty(it.compressPath))
                            compressPath = it.compressPath
                        else{
                            compressPath = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                it.androidQToPath
                            }else{
                                it.path
                            }
                        }
                        //上传图片
                        if(!TextUtils.isEmpty(compressPath)){
                            showToast("type->${type}-图片大小：${FileUtils.getSize(compressPath)}")
                            binding.ivPicture.loadImage(compressPath)
                        }else{
                            showToast("选择照片出错,请重新选择！")
                        }
                    }?:let {
                        showToast("选择照片出错,请重新选择！")
                    }
                }

                override fun onCancel() {
                    showToast("取消")
                }
            })
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

        setTitle<MineFragment>("我的").onUseBack<MineFragment>(false)

        viewModel.defUI.showDialog.observe(this, Observer {
            showLoading()
        })

        viewModel.defUI.dismissDialog.observe(this, Observer {
            dismissLoading()
        })

        viewModel.defUI.toastEvent.observe(this, Observer {
            showToast(it)
        })


        binding.btnDialog.setOnClickListener {
            commonDialog.show()
        }

        binding.btnPicture.setOnClickListener {
            pictureDialog.show()
        }

        binding.btnUpdate.setOnClickListener {
            val data = AppUpdateEntity("type","version",100,"https://www.hyntech.net/appdownload/download/app-release-police-1.0.8-8.apk","content","time")
            showUpdateDialog(data)
        }

        binding.btnRegex.setOnClickListener {
            val input = binding.etInput.text.toString().trim()
//            if(!RegexUtils.isPhone(input)){
//                showToast("手机号不正确")
//                return@setOnClickListener
//            }
//            showToast("手机号")
//            if(RegexUtils.isEmail(input)){
//                showToast("邮箱")
//                return@setOnClickListener
//            }
            if(!RegexUtils.isIDCard(input)){
                showToast("不是身份证")
                return@setOnClickListener
            }
            showToast("身份证")

//            if(RegexUtils.isValidAccount(input)){
//                showToast("账号")
//                return@setOnClickListener
//            }
//            if(RegexUtils.isValidPassword(input)){
//                showToast("密码")
//                return@setOnClickListener
//            }


        }


        binding.btnQrcode.setOnClickListener {


            startActivityForResult(Intent(requireActivity(),MyScanKitActivity::class.java),Code.RequestCode.REQUEST_CODE_QRCODE)

        }

        binding.btnBdmap.setOnClickListener {
            ARouter.getInstance().build(ARouterConstants.BAIDU_MAP_PAGE).navigation(requireActivity(),Code.RequestCode.REQUEST_CODE_BAIDUMAP)
        }


        // 预览大图转场动画
        val list = java.util.ArrayList<String>()
        list.add("http://oss-public.hyntech.net/appUpload/20201202/2018LENoOyAYmq/f6dfa0aad1dc4672b2725b55546f147e.jpg")
        list.add("http://oss-public.hyntech.net/appUpload/20201202/2018LENoOyAYmq/41fd054c25ea40ec80b621215117d0b7.jpg")
        list.add("http://oss-public.hyntech.net/appUpload/20201202/2018LENoOyAYmq/7f6dbad84fbe4c498f147f53468edb63.jpg")
        list.add("http://oss-public.hyntech.net/appUpload/20201202/2018LENoOyAYmq/6ab148ac98f34e738122aa8e38b9947a.jpg")
        binding.rvList.layoutManager = GridLayoutManager(this.requireContext(),2)
        val myImgAdapter = MyImgAdapter(this@MineFragment.requireContext()).apply {
            this.setListener(object : MyImgAdapter.OnClickListener {
                override fun onItemClick(view: View?, pos: Int, item: String?) {
                    //showToast(pos.toString())
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.BundleKey.EXTRA_LIST,list)
                    bundle.putInt(Constants.BundleKey.EXTRA_INDEX,pos)

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        val pair:androidx.core.util.Pair<View, String> = androidx.core.util.Pair(view,pos.toString())
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this@MineFragment.requireActivity(),
                            pair
                        )
                        ARouter.getInstance().build(ARouterConstants.PREVIEW_PAGE)
                            .with(bundle)
                            .withOptionsCompat(options)
                            .navigation(this@MineFragment.requireContext())
                    }else{
                        ARouter.getInstance().build(ARouterConstants.PREVIEW_PAGE)
                            .with(bundle)
                            .navigation()
                    }
                }
            })
        }

        myImgAdapter.setData(list)
        binding.rvList.apply {
            this.layoutManager = GridLayoutManager(this@MineFragment.requireContext(),2)
            this.adapter = myImgAdapter
        }
    }


    override fun onEventResult(requestCode: Int, data: Intent?) {
        super.onEventResult(requestCode, data)
        when(requestCode){
            Code.RequestCode.REQUEST_CODE_QRCODE ->{
                data?.let {
                    val hmsScan: HmsScan? = it.getParcelableExtra<HmsScan>(ScanUtil.RESULT)
                    if(hmsScan != null && !TextUtils.isEmpty(hmsScan.originalValue)){
                        VibrateUtils.vibrate(300)
                        showToast("扫描结果：${hmsScan.originalValue}")
                    }
                }
            }
        }
    }

    private fun applyPermissions(type: Int){
        PermissionUtil.applyCamera(object : RequestPermission {
            override fun onRequestPermissionSuccess() {
                when(type){
                    PictureOptionDialog.TYPE_CAMERA ->{openCamer(type)}
                    PictureOptionDialog.TYPE_PHOTO ->{openPhoto(type)}
                }
            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                showPermissionsDialog()
            }

            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                showPermissionsDialog()
            }
        }, rxPermissions)
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
        updateDialog?.show()
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
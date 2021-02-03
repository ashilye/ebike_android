package net.hyntech.common.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.camera.view.PreviewView
import com.google.zxing.Result
import com.king.zxing.*
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.king.zxing.util.PermissionUtils
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.R
import net.hyntech.common.base.BaseActivity

class QRCodeActivity : BaseActivity(), CameraScan.OnScanResultCallback {

    private val CAMERA_PERMISSION_REQUEST_CODE = 0X86

    private var previewView: PreviewView? = null
    private var viewfinderView: ViewfinderView? = null
    private var ivFlashlight: View? = null

    private var mCameraScan: CameraScan? = null

    override fun getLayoutId(): Int = R.layout.activity_qrcode

    override fun initData(savedInstanceState: Bundle?) {

        previewView = findViewById(getPreviewViewId())
        val viewfinderViewId: Int = getViewfinderViewId()
        if (viewfinderViewId != 0) {
            viewfinderView = findViewById(viewfinderViewId)
            viewfinderView.apply {

            }
        }
        val ivFlashlightId: Int = getFlashlightId()
        if (ivFlashlightId != 0) {
            ivFlashlight = findViewById(ivFlashlightId)
            if (ivFlashlight != null) {
                ivFlashlight?.setOnClickListener { v: View? -> onClickFlashlight() }
            }
        }
        initCameraScan()
        startCamera()

        val decodeConfig = DecodeConfig()
        decodeConfig.setHints(DecodeFormatManager.DEFAULT_HINTS)//如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS)
        decodeConfig.setFullAreaScan(true)//设置是否全区域识别，默认false)
        decodeConfig.setAreaRectRatio(0.8f)//设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
        decodeConfig.setAreaRectVerticalOffset(0) //设置识别区域垂直方向偏移量，默认为0，为0表示居中，可以为负数
        decodeConfig.setAreaRectHorizontalOffset(0) //设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数

        //在启动预览之前，设置分析器，只识别二维码
        getCameraScan()
            ?.setVibrate(true)//设置是否震动，默认为false
            ?.setAnalyzer(MultiFormatAnalyzer(decodeConfig))//设置分析器,如果内置实现的一些分析器不满足您的需求，你也可以自定义去实现
            ?.startCamera()

    }


    protected fun onClickFlashlight() {
        toggleTorchState()
    }


    /**
     * 切换闪光灯状态（开启/关闭）
     */
    protected fun toggleTorchState() {
        if (mCameraScan != null) {
            val isTorch: Boolean = mCameraScan?.isTorchEnabled() ?: false
            mCameraScan?.enableTorch(!isTorch)
            if (ivFlashlight != null) {
                ivFlashlight?.setSelected(!isTorch)
            }
        }
    }


    fun initCameraScan() {
        mCameraScan = DefaultCameraScan(this, previewView)
        mCameraScan?.setOnScanResultCallback(this)
    }

    /**
     * 启动相机预览
     */
    fun startCamera() {
        if (mCameraScan != null) {
            if (PermissionUtils.checkPermission(this, Manifest.permission.CAMERA)) {
                mCameraScan?.startCamera()
            } else {
                LogUtils.logGGQ("checkPermissionResult != PERMISSION_GRANTED")
                PermissionUtils.requestPermission(
                    this,
                    Manifest.permission.CAMERA,
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    /**
     * 释放相机
     */
    protected fun releaseCamera() {
        if (mCameraScan != null) {
            mCameraScan?.release()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            requestCameraPermissionResult(permissions, grantResults)
        }
    }

    /**
     * 请求Camera权限回调结果
     * @param permissions
     * @param grantResults
     */
    protected fun requestCameraPermissionResult(
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (PermissionUtils.requestPermissionsResult(
                Manifest.permission.CAMERA,
                permissions,
                grantResults
            )
        ) {
            startCamera()
        } else {
            finish()
        }
    }


    override fun onDestroy() {
        releaseCamera()
        super.onDestroy()
    }

    /**
     * 返回true时会自动初始化{@link #setContentView(int)}，返回为false是需自己去初始化{@link #setContentView(int)}
     * @param layoutId
     * @return 默认返回true
     */
    fun isContentView(@LayoutRes layoutId: Int): Boolean {
        return true
    }

    /**
     * {@link #viewfinderView} 的 ID
     * @return 默认返回{@code R.id.viewfinderView}, 如果不需要扫码框可以返回0
     */
    fun getViewfinderViewId(): Int {
        return R.id.viewfinderView
    }

    /**
     * 预览界面{@link #previewView} 的ID
     * @return
     */
    fun getPreviewViewId(): Int {
        return R.id.previewView
    }

    /**
     * 获取 {@link #ivFlashlight} 的ID
     * @return  默认返回{@code R.id.ivFlashlight}, 如果不需要手电筒按钮可以返回0
     */
    fun getFlashlightId(): Int {
        return R.id.ivFlashlight
    }

    /**
     * Get {@link CameraScan}
     * @return {@link #mCameraScan}
     */
    fun getCameraScan(): CameraScan? {
        return mCameraScan
    }

    /**
     * 接收扫码结果回调
     * @param result 扫码结果
     * @return 返回true表示拦截，将不自动执行后续逻辑，为false表示不拦截，默认不拦截
     */
    override fun onScanResultCallback(result: Result?): Boolean {
        LogUtils.logGGQ("扫码结果：${result?.text}")
        val intent = Intent()
        intent.putExtra(CameraScan.SCAN_RESULT, result?.text?:"")
        this.setResult(Activity.RESULT_OK, intent)
        this.finish()
        return false
    }
}
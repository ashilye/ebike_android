package net.hyntech.common.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.FlashlightUtils
import com.huawei.hms.hmsscankit.OnLightVisibleCallBack
import com.huawei.hms.hmsscankit.OnResultCallback
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import net.hyntech.common.R
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.ext.toVisible

class MyScanKitActivity : BaseActivity() {

    //声明 RemoteView instance
    private var remoteView: RemoteView? = null

    //声明 key,用于从scankit得到返回的值
//    private val SCAN_RESULT = ScanUtil.RESULT

    private var mSceenWidth = 0
    private var mSceenHeight = 0

    //scan_view_finder width & height is  300dp
    private val SCAN_FRAME_SIZE = 300

    override fun getLayoutId(): Int = R.layout.activity_my_scankit

    private var ivFlashlight: ImageView? = null

    override fun initData(savedInstanceState: Bundle?) {
        this.findViewById<ImageButton>(getBackId())?.setOnClickListener {
            this.finish()
        }

        ivFlashlight = findViewById(getFlashlightId())
        ivFlashlight?.setOnClickListener {
            onFlashlight()
        }

        if(FlashlightUtils.isFlashlightEnable()){
            ivFlashlight?.toVisible()
        }


        //1.设置扫码识别区域，您可以按照需求调整参数
        val dm : DisplayMetrics = resources.displayMetrics
        val density = dm.density
        //2.获取屏幕尺寸
        mSceenWidth = resources.displayMetrics.widthPixels
        mSceenHeight = resources.displayMetrics.heightPixels

        val scanFrameSize = (SCAN_FRAME_SIZE * density).toInt()
        //3.声明 viewfinder'的 矩形,在布局中间
        //设置扫描区域(可选,矩形可以为空,如果不配置,默认是中心的布局)

        val rect = Rect()
        rect.left = mSceenWidth / 2 - scanFrameSize / 2
        rect.right = mSceenWidth / 2 + scanFrameSize / 2
        rect.top = mSceenHeight / 2 - scanFrameSize / 2
        rect.bottom = mSceenHeight / 2 + scanFrameSize / 2

        //初始化RemoteView，并通过如下方法设置参数:
        // setContext()（必选）传入context、setBoundingBox()设置扫描区域、
        // setFormat()设置识别码制式，设置完毕调用build()方法完成创建
        remoteView = RemoteView.Builder().setContext(this).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build()
        remoteView?.setOnLightVisibleCallback(OnLightVisibleCallBack { visible ->
            if (visible) {
                ivFlashlight?.setVisibility(View.VISIBLE)
            }
        })
        //将自定义view加载到activity
        remoteView?.onCreate(savedInstanceState)
        remoteView?.setOnResultCallback(OnResultCallback { result -> //获取到扫码结果HmsScan，判断结果是否有效
            if (result != null && result.size > 0 && result[0] != null && !TextUtils.isEmpty(
                    result[0].getOriginalValue()
                )
            ) {
                val intent = Intent()
                intent.putExtra(ScanUtil.RESULT, result[0])
                setResult(Activity.RESULT_OK, intent)
                this@MyScanKitActivity.finish()
            }
        })

        //添加 remoteView 到Framelayout布局中

        //添加 remoteView 到Framelayout布局中
        val params = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        //绑定相机预览布局
        //绑定相机预览布局
        val frameLayout = findViewById<FrameLayout>(R.id.rim)
        frameLayout.addView(remoteView, params)

    }


    private fun onFlashlight() {
        if(remoteView == null) return
        if(remoteView!!.getLightStatus()){
            remoteView?.switchLight()
            ivFlashlight?.isSelected = false
        }else{
            remoteView?.switchLight()
            ivFlashlight?.isSelected = true
        }
    }


    ///返回键
    fun getBackId(): Int {
        return R.id.left_back
    }

    //照明按钮
    fun getFlashlightId(): Int {
        return R.id.ivFlashlight
    }

    //扫图按钮
    fun getPictureId(): Int {
        return R.id.ivPicture
    }


    override fun onStart() {
        super.onStart()
        remoteView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        remoteView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        remoteView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        remoteView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteView?.onDestroy()
    }
}
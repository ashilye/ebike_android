package net.hyntech.common.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.R
import net.hyntech.common.app.global.Constants
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.ext.toGone
import net.hyntech.common.provider.ARouterConstants
import net.hyntech.common.ui.adapter.PreviewAdapter
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection


@Route(path = ARouterConstants.PREVIEW_PAGE)
class PreviewActivity : BaseActivity() {

    private var vp: ViewPager2? = null
    private var tvIndex: TextView? = null
    private var ivSave:ImageView? = null

    private var list:List<String>? = null

    override fun getLayoutId(): Int = R.layout.activity_preview

    override fun initData(savedInstanceState: Bundle?) {
        setTitle<PreviewActivity>("预览大图")
            .onBack<PreviewActivity> { onFinish() }

        vp = this.findViewById(R.id.vp)
        tvIndex = this.findViewById(R.id.tv_index)
        ivSave = this.findViewById(R.id.iv_save)
        ivSave?.setOnClickListener {
            onClickProxy {
                saveImage()
            }
        }
        this.intent?.extras?.let {
            list = it.getSerializable(Constants.BundleKey.EXTRA_LIST) as? List<String>
            val index:Int = it.getInt(Constants.BundleKey.EXTRA_INDEX,0)
            val tag:Boolean = it.getBoolean(Constants.BundleKey.EXTRA_TAG,true)
            if(!tag){
                ivSave?.toGone()
            }

            if(!list.isNullOrEmpty()){
                LogUtils.logGGQ("--list-->${list?.size}")
                LogUtils.logGGQ("--index-->${index}")
                vp?.adapter = PreviewAdapter(this).apply {
                    this.setData(list!!)
                }
                vp?.currentItem = index
                tvIndex?.text = "${index+1}/${list?.size}"
                vp?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        tvIndex?.text = "${position+1}/${list?.size}"
                    }
                })
            }
        }

    }

    private fun saveImage() {
        list?.let {
            val url = it.get(vp?.currentItem?:0)
            if(!TextUtils.isEmpty(url)){
                LogUtils.logGGQ("-url->${url}")
                url2bitmap(url)
            }
        }
    }

    private fun url2bitmap(url:String){
       GlobalScope.launch(Dispatchers.IO){
           try {
               val iconUlr: URL = URL(url)
               val conn: URLConnection = iconUlr.openConnection()
               val http = conn as HttpURLConnection
               val length = http.getContentLength()
               conn.connect()
               // 获得图像的字符流
               val inputStream = conn.getInputStream()
               val bis = BufferedInputStream(inputStream,length)
               val bm = BitmapFactory.decodeStream(bis)
               bis.close()
               inputStream.close()
               if(bm != null){
                   ImageUtils.save2Album(bm, Bitmap.CompressFormat.JPEG)
                   runOnUiThread{
                       showToast("保存成功！")
                   }
               }else{
                   showToast("保存失败,请稍候重试！")
               }
           }catch (e:Exception){
               e.fillInStackTrace()
               LogUtils.logGGQ("--保存图片-->${e.fillInStackTrace()}")
               runOnUiThread{
                   showToast("保存失败,请稍候重试！")
               }
           }
       }

    }
}
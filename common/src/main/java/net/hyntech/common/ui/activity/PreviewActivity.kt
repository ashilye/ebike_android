package net.hyntech.common.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.alibaba.android.arouter.facade.annotation.Route
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.common.R
import net.hyntech.common.app.global.Constants
import net.hyntech.common.base.BaseActivity
import net.hyntech.common.provider.ARouterConstants
import net.hyntech.common.ui.adapter.PreviewAdapter

@Route(path = ARouterConstants.PREVIEW_PAGE)
class PreviewActivity : BaseActivity() {

    private var vp: ViewPager2? = null
    private var tvIndex: TextView? = null

    override fun getLayoutId(): Int = R.layout.activity_preview

    override fun initData(savedInstanceState: Bundle?) {
        setTitle<PreviewActivity>("预览大图").onBack<PreviewActivity> { onFinish() }

        vp = this.findViewById(R.id.vp)
        tvIndex = this.findViewById(R.id.tv_index)

        this.intent?.extras?.let {
            val list = it.getSerializable(Constants.BundleKey.EXTRA_LIST) as List<String>
            val index:Int = it.getInt(Constants.BundleKey.EXTRA_INDEX,0)
            if(list.isNotEmpty()){
                LogUtils.logGGQ("--list-->${list.size}")
                LogUtils.logGGQ("--index-->${index}")
                vp?.adapter = PreviewAdapter(this).apply {
                    this.setData(list)
                }
                vp?.currentItem = index
                tvIndex?.text = "${index+1}/${list.size}"
                vp?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        tvIndex?.text = "${position+1}/${list.size}"
                    }
                })
            }
        }

    }
}
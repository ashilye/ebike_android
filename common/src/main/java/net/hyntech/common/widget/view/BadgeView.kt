package net.hyntech.common.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.SizeUtils
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.R
import java.lang.Exception

class BadgeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {


    init {
        width = SizeUtils.dp2px(14f)
        height = SizeUtils.dp2px(14f)
        gravity = Gravity.CENTER
        background = UIUtils.getDrawable(R.drawable.shape_badge_bg)
    }


    override fun setText(text: CharSequence?, type: BufferType?) {
        var txt = 0
        try {
            txt = text?.toString()?.toInt()?:0
        }catch (e:Exception){
            e.fillInStackTrace()
        }
        if(txt <= 0){
            visibility = View.GONE
        }else{
            visibility = View.VISIBLE
            if (txt > 99) {
                txt = 99
            }
        }

        super.setText(txt.toString(), type)
    }
}
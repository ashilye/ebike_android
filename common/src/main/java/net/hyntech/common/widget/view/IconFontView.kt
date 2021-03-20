package net.hyntech.common.widget.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

/**
 * 字体图标 TextView
 */
class IconFontView: androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : this(context,null,0)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet,0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ){
        initView(context,
            attributeSet,
            defStyleAttr)
    }

    private fun initView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int = 0) {
        val font :Typeface? = Typeface.createFromAsset(context.assets,"iconfont.ttf")
        font?.let {
            this.typeface = it
        }
    }

}
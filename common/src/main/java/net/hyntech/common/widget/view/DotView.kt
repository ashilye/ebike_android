package net.hyntech.common.widget.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import net.hyntech.common.R

/**
 * 实心圆形/圆环
 */
class DotView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var paint: Paint = Paint()
    private var dotColor: Int = Color.RED
    private var dotStype: Int = 0
    private val strokeWidth: Float = 2.0f

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DotView)
        dotColor = array.getColor(R.styleable.DotView_dotColor, dotColor)
        paint.color = dotColor
        dotStype = array.getInteger(R.styleable.DotView_dotStyle, dotStype)
        if (dotStype != 0) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
        }
        array.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(width / 2.0f, height / 2.0f, width / 2.0f - strokeWidth, paint)
    }


    fun setColor(color: Int) {
        paint.color = color
        invalidate()
    }
}
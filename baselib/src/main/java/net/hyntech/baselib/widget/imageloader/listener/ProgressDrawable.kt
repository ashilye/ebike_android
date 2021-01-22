package net.hyntech.baselib.widget.imageloader.listener

import android.graphics.*
import android.graphics.drawable.Drawable

class ProgressDrawable(var width: Float, var color: Int) : Drawable() {

    // 画笔对象
    private val paint = Paint()

    //当前进度
    private var currentProgress: Int = 0

    //总进度
    private var maxProgress: Int = 100

    //开始角度
    private var startAngle: Float = START_ANGLE_RIGHT

    // 旋转方向, 顺时针,逆时针.
    private var oritation: Int = ORITATION_CLOCKWISE

    companion object {
        // 右
        const val START_ANGLE_RIGHT = 0F

        // 下
        const val START_ANGLE_BOTTOM = 90F

        // 左
        const val START_ANGLE_LEFT = 180F

        // 上
        const val START_ANGLE_TOP = 270F

        //顺时针
        const val ORITATION_CLOCKWISE = 0x00

        //逆时针
        const val ORITATION_ANTI_CLOCKWISE = 0x01
    }


    fun setOritation(oritation: Int) {
        if (oritation == ORITATION_CLOCKWISE || oritation == ORITATION_ANTI_CLOCKWISE) {
            this.oritation = oritation
        } else {
            throw RuntimeException("you must set ORITATION_CLOCKWISE or ORITATION_ANTI_CLOCKWISE!!!")
        }
    }


    init {
        paint.isAntiAlias = true //是否抗锯齿
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = width
    }


    fun setProgress(progress: Int) {
        currentProgress =
            if (oritation == ORITATION_CLOCKWISE) if (progress > maxProgress) maxProgress else progress
            else -(if (progress > maxProgress) maxProgress else progress)
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        //清除画布
        canvas.drawColor(Color.BLUE)
        //获得尺寸
        val bounds: Rect = bounds
        //计算半径
        val radius: Float = (Math.min(bounds.width(), bounds.height()) - width) / 2
        //计算位置
        val offsetX: Float = (bounds.width() - radius * 2) / 2
        val offsetY: Float = (bounds.height() - radius * 2) / 2
        val rect: RectF = RectF(offsetX, offsetY, offsetX + radius * 2, offsetY + radius * 2)
        // 1. 绘制圆环
        val centerX: Float = (bounds.width() / 2).toFloat()
        val centerY: Float = (bounds.height() / 2).toFloat()
        val outRadius: Float = radius + width / 2
        paint.strokeWidth = 1F
        canvas.drawCircle(centerX, centerY, outRadius, paint)
        // 计算角度.
        val angle: Float = ((currentProgress * 1.0 / maxProgress) * 360).toFloat()
        // 2. 绘制进度圆环.
        paint.strokeWidth = width
        canvas.drawArc(rect, startAngle, angle, false, paint)
        // 3. 绘制内圆
        val innerMaxCircleRadius: Float = radius - width / 2 + 1
        val innerCurCircleRadius: Float =
            (innerMaxCircleRadius * (currentProgress * 1.0 / maxProgress)).toFloat()
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, Math.abs(innerCurCircleRadius), paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getIntrinsicHeight(): Int {
        return bounds.height()
    }

    override fun getIntrinsicWidth(): Int {
        return bounds.width()
    }

}
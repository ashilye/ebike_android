package net.hyntech.common.widget.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginTop

/**
 * 可折叠带动画的 LinearLayout (带 icon 旋转动画)
 *
 * 如何使用?
 *
    <com.maple.common.widget.view.ExpandLinearLayout
        android:id="@+id/ell"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#f5f5f5"
        android:orientation="vertical"
        android:padding="10dp">

        <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="10dp"
        android:text="@string/app_name"
        android:textColor="@android:color/holo_red_dark"
        android:textSize="20sp" />

        ...

    </com.maple.common.widget.view.ExpandLinearLayout>

    ll_btn.setOnClickListener {
        val toggle = ell.toggle()
        tv_tip.text = if (toggle) "收起" else "展开"
    }
 *
 */

class ExpandLinearLayout: LinearLayout {

    //是否展开，默认展开
    private var isOpen = true

    //第一个子view的高度，即收起保留高度
    private var firstChildHeight = 0

    //所有子view高度，即总高度
    private var allChildHeight = 0

    /**
     * 动画值改变的时候 请求重新布局
     */
    private var animPercent: Float = 0f
        set(value) {
            field = value
            requestLayout()
        }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        //横向的话 稍加修改计算宽度即可
        orientation = VERTICAL
        animPercent = 1f
        isOpen = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //重置高度
        allChildHeight = 0
        firstChildHeight = 0

        if (childCount > 0) {
            //遍历计算高度
            for (index in 0 until childCount) {
                //这个地方实际使用中除了measuredHeight，以及margin等，也要计算在内
                if (index == 0) {
                    firstChildHeight = getChildAt(index).measuredHeight
                    +getChildAt(index).marginTop + getChildAt(index).marginBottom
                    +this.paddingTop + this.paddingBottom
                }
                //实际使用时或包括padding等
                allChildHeight += getChildAt(index).measuredHeight + getChildAt(index).marginTop + getChildAt(
                    index
                ).marginBottom

                //最后一条的时候 加上当前view自身的padding
                if (index == childCount - 1) {
                    allChildHeight += this.paddingTop + this.paddingBottom
                }
            }

            // 根据是否展开设置高度
            if (isOpen) {
                setMeasuredDimension(
                    widthMeasureSpec,
                    firstChildHeight + ((allChildHeight - firstChildHeight) * animPercent).toInt()
                )
            } else {
                setMeasuredDimension(
                    widthMeasureSpec,
                    allChildHeight - ((allChildHeight - firstChildHeight) * animPercent).toInt()
                )
            }
        }
    }


    fun toggle(duration:Long = 300L,icRotate:View? = null): Boolean {
        isOpen = !isOpen
        startAnim(isOpen,duration,icRotate)
        return isOpen
    }

    /**
     * 执行动画的时候 更改 animPercent 属性的值 即从0-1
     */
    @SuppressLint("AnimatorKeep")
    private fun startAnim(isOpen: Boolean,duration:Long,icon:View? = null) {
        //ofFloat，of xxxX 根据参数类型来确定
        //1，动画对象，即当前view。2.动画属性名。3，起始值。4，目标值。
        val animator = ObjectAnimator.ofFloat(this, "animPercent", 0f, 1f)
        animator.duration = if(isOpen) duration else duration/2
        if(icon != null){
            val rotateAnim = RotateAnimation(if(isOpen) 180f else 0f, if(isOpen) 0f else 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            // 设置保持动画最后的状态
            rotateAnim.fillAfter = true
            rotateAnim.duration = if(isOpen) duration else duration/2
            rotateAnim.interpolator = AccelerateInterpolator()
            icon.startAnimation(rotateAnim)
        }
        animator.start()
    }

}
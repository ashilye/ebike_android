package net.hyntech.common.widget.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.hyntech.common.R

class SimpleItemDecoration(
    val context: Context,
    val drawableId: Int = R.drawable.shape_divider_line,
    val orientation: Int = LinearLayoutManager.VERTICAL
) :
    RecyclerView.ItemDecoration() {


    private val divider: Drawable?
    private val height:Int = 1

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, divider?.intrinsicHeight?:height)
        } else {
            outRect.set(0, 0, divider?.intrinsicHeight?:height, 0)
        }
    }

    init {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw IllegalArgumentException("请输入正确的参数！")
        }
        divider = ContextCompat.getDrawable(context, drawableId)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (orientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }


    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            if(i != childSize - 1){ //最后一条不绘制
                val child = parent.getChildAt(i)
                val layoutParams = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + layoutParams.bottomMargin
                val bottom = top + (divider?.intrinsicHeight?:height)

                divider?.let {
                    divider.setBounds(left, top, right, bottom)
                    divider.draw(c)
                }
            }
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            if(i != childSize - 1){ //最后一条不绘制
                val child = parent.getChildAt(i)
                val layoutParams = child.layoutParams as RecyclerView.LayoutParams
                val left = child.right + layoutParams.rightMargin
                val right = left + (divider?.intrinsicHeight?:height)

                divider?.let {
                    divider.setBounds(left, top, right, bottom)
                    divider.draw(c)
                }
            }
        }
    }
}
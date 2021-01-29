package net.hyntech.common.widget.decoration

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class GridItemDecoration(val context: Context): RecyclerView.ItemDecoration() {
    private val ATTRS = intArrayOf(R.attr.listDivider)
    private var divider: Drawable? = null

    init {
        val a: TypedArray = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(0)
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        super.onDraw(c, parent, state)
        drawHorizontal(c, parent)
        drawVertical(c, parent)
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val childCount:Int  = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            val top: Int = child.top - params.topMargin
            val bottom: Int = child.bottom + params.bottomMargin
            val left: Int = child.right + params.rightMargin
            val right: Int = left + (divider?.intrinsicWidth?:0)
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val childCount:Int  = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val right: Int = (child.right + params.rightMargin + (divider?.intrinsicWidth?:0))
            val top = child.bottom + params.bottomMargin
            val bottom: Int = top + (divider?.intrinsicHeight?:0)
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val spanCount: Int = getSpanCount(parent)
        val childCount = parent.adapter?.itemCount
        if (isLastRaw(parent, spanCount, childCount?:0)) // 如果是最后一行，则不需要绘制底部
        {
            outRect[0, 0, (divider?.intrinsicWidth?:0)] = 0
        } else if (isLastColum(parent, spanCount, view)) // 如果是最后一列，则不需要绘制右边
        {
            outRect[0, 0, 0] = (divider?.intrinsicHeight?:0)
        } else {
            outRect[0, 0, (divider?.intrinsicWidth?:0)] = (divider?.intrinsicHeight?:0)
        }
    }

    private fun isLastColum(parent: RecyclerView, spanCount: Int, view: View): Boolean {
        val layoutManager = parent.layoutManager
        val pos = layoutManager?.getPosition(view)
        if (layoutManager is GridLayoutManager) {
            if ((pos?:0 + 1) % spanCount == 0) { // 如果是最后一列，则不需要绘制右边
                return true
            }
        }
        return false
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

    private fun isLastRaw(parent: RecyclerView, spanCount: Int, childCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if (childCount % spanCount == 0 || childCount % spanCount < spanCount)
                return true
        }
        return false
    }


}
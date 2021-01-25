package net.hyntech.common.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import net.hyntech.baselib.utils.UIUtils

abstract class BaseAdapter<T : Any, VH : BaseViewHolder<T>>(val context: Context) :
    RecyclerView.Adapter<VH>() {

    protected val list: List<T> = arrayListOf()

    open fun setData(l: List<T>) {
        this.list.toMutableList().apply {
            clear()
            addAll(l)
        }
        this.notifyDataSetChanged()
    }


    open fun updataList(l: List<T>) {
        this.list.toMutableList().addAll(l)
        this.notifyDataSetChanged()
    }

    fun onClickProxy(m: () -> Unit) {
        if (!UIUtils.isFastDoubleClick()) {
            m()
        }
    }

    open fun getListSize(): Int {
        return list.size
    }

    protected var l: OnClickListener<T>? = null

    fun setListener(l: OnClickListener<T>?) {
        this.l = l
    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnClickListener<T> {
        fun onItemClick(pos: Int, item: T?)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.setData(list.get(position))
    }

}
package net.hyntech.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.hyntech.common.R

/**
 * 封装通用的抽象BaseViewHolder,所有ViewHolder 都必须实现此抽象类方便开发
 * 如果 item 需要点击事件,xml 中的id应为 common_item_root
 */
abstract class BaseViewHolder<T:Any>(itemView: View): RecyclerView.ViewHolder(itemView){

    val itemRoot: View? = itemView.findViewById(R.id.common_item_root)

    abstract fun setData(pos:Int,data:T?)

}
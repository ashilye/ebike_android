package net.hyntech.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.hyntech.common.R

abstract class BaseViewHolder<T:Any>(itemView: View): RecyclerView.ViewHolder(itemView){

    val itemRoot: View? = itemView.findViewById(R.id.common_item_root)

    abstract fun setData(pos:Int,data:T?)

}
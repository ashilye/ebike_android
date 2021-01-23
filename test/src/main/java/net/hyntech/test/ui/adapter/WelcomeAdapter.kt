package net.hyntech.test.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import net.hyntech.common.base.BaseAdapter
import net.hyntech.common.base.BaseViewHolder
import net.hyntech.common.ext.isFastClick
import net.hyntech.common.ext.loadImage
import net.hyntech.test.R


class WelcomeAdapter(val context:Context, val data:Array<Int>, val listener:OnClickListener) : BaseAdapter<WelcomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_welcome,parent,false))

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(position,data.get(position))
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        fun setData(pos:Int,item: Int) {
            itemView.findViewById<ImageView>(R.id.iv_img)?.let {
                it.loadImage(item)
                it.setOnClickListener { v->
                    !v.isFastClick().apply { listener.onItemClick(pos) }
                }
            }
        }
    }

    interface OnClickListener{
        fun onItemClick(position:Int)
    }
}
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


class WelcomeAdapter(context:Context) : BaseAdapter<Int,WelcomeAdapter.ViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_welcome,parent,false))
    }

        inner class ViewHolder(itemView: View) : BaseViewHolder<Int>(itemView) {
        override fun setData(pos:Int,data: Int?) {
            itemView.findViewById<ImageView>(R.id.iv_img)?.let {img ->
                data?.let {
                    img.loadImage(it)
                    img.setOnClickListener { v->
                        !v.isFastClick().apply { l?.onItemClick(pos) }
                    }
                }

            }
        }
    }


}
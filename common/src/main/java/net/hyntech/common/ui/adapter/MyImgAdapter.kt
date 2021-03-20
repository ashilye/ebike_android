package net.hyntech.common.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import net.hyntech.common.R
import net.hyntech.common.base.BaseAdapter
import net.hyntech.common.base.BaseViewHolder
import net.hyntech.common.ext.layoutInflater
import net.hyntech.common.ext.loadImage

class MyImgAdapter(context: Context) : BaseAdapter<String, MyImgAdapter.ViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = context.layoutInflater.inflate(R.layout.item_img, parent, false)
        val holder: MyImgAdapter.ViewHolder = ViewHolder(view)
        holder.itemRoot?.setOnClickListener {
            onClickProxy {
                l?.onItemClick(holder.adapterPosition, list.get(holder.adapterPosition))
            }
        }
        return holder
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        private val ivImg: ImageView = itemView.findViewById(R.id.iv_img)

        override fun setData(pos: Int, data: String?) {
            data?.let {url ->
                ivImg.loadImage(url)
            }
        }
    }

}
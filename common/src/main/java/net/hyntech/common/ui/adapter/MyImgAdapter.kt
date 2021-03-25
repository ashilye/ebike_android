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

    private var cl: OnClickListener? = null

    fun setListener(l: OnClickListener?) {
        this.cl = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = context.layoutInflater.inflate(R.layout.item_img, parent, false)
        val holder: MyImgAdapter.ViewHolder = ViewHolder(view)
        holder.itemRoot?.setOnClickListener {
            onClickProxy {
                cl?.onItemClick(holder.ivImg,holder.adapterPosition, list.get(holder.adapterPosition))
            }
        }
        return holder
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        val ivImg: ImageView = itemView.findViewById(R.id.iv_img)

        override fun setData(pos: Int, data: String?) {
            data?.let {url ->
                ivImg.loadImage(url)
            }
        }
    }


    interface OnClickListener {
        fun onItemClick(view:View?,pos: Int = 0, item: String?)
    }

}
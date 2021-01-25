package net.hyntech.test.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.hyntech.common.base.BaseAdapter
import net.hyntech.common.base.BaseViewHolder
import net.hyntech.common.ext.layoutInflater
import net.hyntech.common.model.entity.NoticeListEntity
import net.hyntech.test.R

class NoticeAdapter(context: Context) : BaseAdapter<NoticeListEntity.AlarmInfoListBean, NoticeAdapter.ViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeAdapter.ViewHolder {
        val view: View = context.layoutInflater.inflate(R.layout.item_notice_list, parent, false)
        val holder: ViewHolder = ViewHolder(view)
        holder.itemRoot?.setOnClickListener {
            onClickProxy {
                l?.onItemClick(holder.adapterPosition, list.get(holder.adapterPosition))
            }
        }
        return holder
    }


    inner class ViewHolder(itemView: View) : BaseViewHolder<NoticeListEntity.AlarmInfoListBean>(itemView) {

        private val tvTitle:TextView = itemView.findViewById(R.id.tv_title)

        override fun setData(data: NoticeListEntity.AlarmInfoListBean?) {
            tvTitle.text = "${data?.ebikeNo}"
        }
    }

}
package net.hyntech.common.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.youth.banner.adapter.BannerAdapter
import net.hyntech.common.ext.layoutInflater
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.R
import net.hyntech.common.base.BaseViewHolder
import net.hyntech.common.ext.loadImage
import net.hyntech.common.model.entity.BannerEntity

class MyBannerAdapter(val context: Context,val list:List<BannerEntity>):BannerAdapter<BannerEntity,MyBannerAdapter.BannerViewHolder>(list){

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val view: View = context.layoutInflater.inflate(R.layout.item_banner,parent,false)
        val holder = BannerViewHolder(view)
        return holder
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: BannerEntity?,
        position: Int,
        size: Int
    ) {
        holder?.setData(position,list.get(position))
    }

    inner class BannerViewHolder(itemView: View) : BaseViewHolder<BannerEntity>(itemView) {

        override fun setData(pos: Int, data: BannerEntity?) {
            data?.let { item ->
                itemView.findViewById<ImageView>(R.id.iv_img)?.loadImage(if(item.type == 1) UIUtils.getDrawable(item.resId) else item.url)
            }
        }
    }
}
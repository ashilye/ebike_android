package net.hyntech.common.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.baidu.mapapi.search.core.PoiInfo
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.common.R
import net.hyntech.common.base.BaseAdapter
import net.hyntech.common.base.BaseViewHolder
import net.hyntech.common.ext.layoutInflater

class PoiResultAdapter(context: Context): BaseAdapter<PoiInfo,PoiResultAdapter.ViewHolder>(context) {

    private val drawDefault:Drawable = UIUtils.getDrawable(R.drawable.ic_poi_default)
    private val drawSelected:Drawable = UIUtils.getDrawable(R.drawable.ic_poi_selected)

    var index:Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = context.layoutInflater.inflate(R.layout.item_map_poi, parent, false)
        val holder: PoiResultAdapter.ViewHolder = ViewHolder(view)
        holder.itemRoot?.setOnClickListener {
            onClickProxy {
                l?.onItemClick(holder.adapterPosition, list.get(holder.adapterPosition))
            }
        }
        return holder
    }

    inner class ViewHolder(itemView: View): BaseViewHolder<PoiInfo>(itemView){
        private var tvTitle: TextView? = itemView.findViewById(R.id.tv_title)
        private var tvContent: TextView? = itemView.findViewById(R.id.tv_content)
        private var ivPointState: ImageView? = itemView.findViewById(R.id.iv_point_state)

        override fun setData(pos: Int, data: PoiInfo?) {
            data?.let {
                tvTitle?.text = it.name
                tvContent?.text = it.address
                if(pos == index){
                    ivPointState?.background =  drawSelected
                }else{
                    ivPointState?.background =  drawDefault
                }
            }
        }
    }
}
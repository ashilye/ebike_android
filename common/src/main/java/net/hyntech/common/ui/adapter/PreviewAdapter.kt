package net.hyntech.common.ui.adapter

import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.github.chrisbanes.photoview.PhotoView
import net.hyntech.common.R
import net.hyntech.common.base.BaseAdapter
import net.hyntech.common.base.BaseViewHolder
import net.hyntech.common.ext.layoutInflater
import net.hyntech.common.ext.loadImage

class PreviewAdapter(context: Context):BaseAdapter<String, PreviewAdapter.ViewHolder>(context) {

    private val is21 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = context.layoutInflater.inflate(R.layout.item_preview, parent, false)
        val holder: ViewHolder = ViewHolder(view)
        return holder
    }


    inner class ViewHolder(itemView: View):BaseViewHolder<String>(itemView){
        private val pvImg: PhotoView = itemView.findViewById(R.id.pv_img)
        override fun setData(pos: Int, data: String?) {
            if(is21){
                ViewCompat.setTransitionName(pvImg, pos.toString());
            }
            data?.let {url ->
                pvImg.loadImage(url)
            }
        }
    }

}
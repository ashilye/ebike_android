package net.hyntech.baselib.widget.imageloader.glide

import android.widget.ImageView
import net.hyntech.baselib.R
import net.hyntech.baselib.widget.imageloader.ImageConfig
import net.hyntech.baselib.widget.imageloader.TransType
import net.hyntech.baselib.widget.imageloader.listener.ProgressListener

class GlideImageConfig: ImageConfig {
    var overWidth:Int = 0
    var overHeight:Int = 0

    var valueBlur:Int = 0
    var valueRound:Int = 0

    var progressListener: ProgressListener? = null
    var type: TransType = TransType.NORMAL

    constructor(any:Any, imageView: ImageView, progressListener: ProgressListener? = null, placeholder:Int = android.R.drawable.picture_frame, errorPic:Int = android.R.drawable.picture_frame){
        this.any = any
        this.imageView = imageView
        this.progressListener = progressListener
        this.placeholder = placeholder
        this.errorPic = errorPic
    }
}
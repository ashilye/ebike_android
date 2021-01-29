package net.hyntech.common.widget.update

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.xuexiang.xupdate.widget.NumberProgressBar
import net.hyntech.baselib.widget.dialog.BaseCenterDialog
import net.hyntech.common.R
import net.hyntech.common.model.entity.AppUpdateEntity
import kotlin.math.roundToInt

class UpdateDialog(context: Context,
                   val data: AppUpdateEntity,
                   val listener: OnClickListener,
                   isCancelable:Boolean = false):BaseCenterDialog(context,style = R.style.CommonDialogStyle,isCancelable = isCancelable) {

    private var npb: NumberProgressBar? = null


    override fun getLayoutId(): Int = R.layout.dialog_update


    override fun initData(savedInstanceState: Bundle?) {
        this.findViewById<TextView>(R.id.tv_title)?.text  = "是否升级到: V${data.version}"
        this.findViewById<TextView>(R.id.tv_update_info)?.text  = "${data.content}"
        this.findViewById<TextView>(R.id.tv_update_time)?.text  = "${data.time}"
        npb = this.findViewById(R.id.npb_progress)
        this.findViewById<Button>(R.id.btn_update).setOnClickListener {
            npb?.visibility = View.VISIBLE
            npb?.max = 100
            it?.visibility = View.GONE
            listener.onConfirmClick(data.url)
        }

        this.findViewById<AppCompatImageView>(R.id.iv_close)?.setOnClickListener {
            this.onCancel()
            listener.onCancelClick()
        }
    }

    fun setProgress(progress:Float){
        npb?.progress = (progress * 100).roundToInt()
    }


    interface OnClickListener{
        fun onCancelClick(){}
        fun onConfirmClick(url:String)
    }
}
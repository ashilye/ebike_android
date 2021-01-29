package net.hyntech.common.widget.dialog

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.baselib.widget.dialog.BaseCenterDialog
import net.hyntech.baselib.widget.dialog.BaseDailog
import net.hyntech.common.R

class CommonDialog(context: Context,
                   val title: String = UIUtils.getString(R.string.common_tip_warm),
                   val content: String = "",
                   val cancle: String = UIUtils.getString(R.string.common_text_cancel),
                   val confirm: String = UIUtils.getString(R.string.common_text_confirm),
                   val listener:OnClickListener,
                   isCancelable:Boolean = false
):BaseCenterDialog(context,isCancelable = isCancelable,style = R.style.CommonDialogStyle){
    override fun getLayoutId(): Int = R.layout.dialog_common
    override fun initData(savedInstanceState: Bundle?) {
        findViewById<TextView>(R.id.tv_title).let {
            it.text = title
        }

        findViewById<TextView>(R.id.tv_content).let {
            it.text = content
        }
        findViewById<Button>(R.id.btn_cancle).let {
            it.text = cancle
            it.setOnClickListener {
                this.onCancel()
                listener.onCancelClick()
            }
        }
        findViewById<Button>(R.id.btn_confirm).let {
            it.text = confirm
            it.setOnClickListener {
                this.onCancel()
                listener.onConfirmClick()
            }
        }

    }

    interface OnClickListener{
        fun onCancelClick(){}
        fun onConfirmClick()
    }
}



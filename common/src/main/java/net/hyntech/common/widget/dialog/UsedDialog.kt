package net.hyntech.common.widget.dialog

import android.content.Context
import android.os.Bundle
import android.widget.Button
import net.hyntech.baselib.widget.dialog.BaseCenterDialog
import net.hyntech.common.R

/**
 *
 */
class UsedDialog(context: Context, isCancelable:Boolean = false, val listener: OnClickListener): BaseCenterDialog(context,isCancelable = isCancelable,style = R.style.CommonDialogStyle) {
    /**

     *
     */
    override fun getLayoutId(): Int = R.layout.dialog_used

    override fun initData(savedInstanceState: Bundle?) {

        findViewById<Button>(R.id.btn_cancle).let {
            it.setOnClickListener {
                this.onCancel()
                listener.onCancelClick()
            }
        }
        findViewById<Button>(R.id.btn_confirm).let {
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
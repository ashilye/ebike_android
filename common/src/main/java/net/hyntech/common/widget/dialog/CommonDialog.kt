package net.hyntech.common.widget.dialog

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.baselib.widget.dialog.BaseDailog
import net.hyntech.common.R

class CommonDialog : BaseDailog {
    private val title: String
    private val content: String
    private val cancle: String
    private val confirm: String
    private val listener:OnClickListener

    constructor(
        context: Context,
        title: String,
        content: String,
        cancle: String = UIUtils.getString(R.string.common_text_cancel),
        confirm: String = UIUtils.getString(R.string.common_text_confirm),
        listener:OnClickListener,
        isCancelable:Boolean = false
    ) : super(context,style = R.style.CommonDialogStyle,isCancelable = isCancelable) {
        this.title = title
        this.content = content
        this.cancle = cancle
        this.confirm = confirm
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_common)
        findViewById<TextView>(R.id.tv_title).let {
            it.text = title
        }

        findViewById<TextView>(R.id.tv_content).let {
            it.text = content
        }
        findViewById<Button>(R.id.btn_cancle).let {
            it.text = cancle
            it.setOnClickListener {
                this.cancel()
                listener.onCancleClick()
            }
        }
        findViewById<Button>(R.id.btn_confirm).let {
            it.text = confirm
            it.setOnClickListener {
                this.cancel()
                listener.onConfirmClick()
            }
        }
    }

    interface OnClickListener{
        fun onCancleClick()
        fun onConfirmClick()
    }
}
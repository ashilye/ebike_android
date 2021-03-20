package net.hyntech.common.widget.dialog

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.baselib.widget.dialog.BaseCenterDialog
import net.hyntech.common.R
import net.hyntech.common.utils.MySpannableString

class UsedDialog(context: Context, isCancelable: Boolean = false, val listener: OnClickListener) :
    BaseCenterDialog(
        context,
        (ScreenUtils.getScreenWidth() * 0.8).toInt(),
        (ScreenUtils.getScreenHeight() * 0.42).toInt(),
        isCancelable = isCancelable, style = R.style.CommonDialogStyle
    ) {

    private var tvContent: TextView? = null

    //协议内容
    private val content: String by lazy { UIUtils.getString(R.string.common_used_content) }

    override fun getLayoutId(): Int = R.layout.dialog_used

    override fun initData(savedInstanceState: Bundle?) {
        tvContent = findViewById(R.id.tv_content)
        tvContent?.let {
            val mss = MySpannableString(context, content)
                .first("《隐私政策》").onClick(it) {
                    listener.onPolicyClick()
                }.textColor(R.color.common_color_red)
                .first("《用户协议》").onClick(it) {
                    listener.onAgreementClick()
                }.textColor(R.color.common_color_red)
            it.text = mss
        }


        findViewById<Button>(R.id.btn_cancle)?.let {
            it.setOnClickListener {
                this.onCancel()
                listener.onCancelClick()
            }
        }
        findViewById<Button>(R.id.btn_confirm)?.let {
            it.setOnClickListener {
                this.onCancel()
                listener.onConfirmClick()
            }
        }

    }

    interface OnClickListener {
        fun onCancelClick()
        fun onConfirmClick()
        fun onPolicyClick()
        fun onAgreementClick()
    }
}
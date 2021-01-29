package net.hyntech.common.widget.dialog

import android.os.Bundle
import android.view.View
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.baselib.widget.dialog.BaseDialogFragment
import net.hyntech.common.R
import net.hyntech.common.databinding.DialogLiveBinding

class LiveDialog(val title: String = UIUtils.getString(R.string.common_tip_warm),
                 val content: String = "",
                 val cancle: String = UIUtils.getString(R.string.common_text_cancel),
                 val confirm: String = UIUtils.getString(R.string.common_text_confirm),
                 val listener:OnClickListener
                 ):BaseDialogFragment<DialogLiveBinding>() {

    override fun getLayoutId(): Int = R.layout.dialog_live


    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        binding.tvTitle.text = title
        binding.tvContent.text = content
        binding.btnCancle.apply {
            this.text = cancle
            this.setOnClickListener {
                this@LiveDialog.dialog?.cancel()
                listener.onCancleClick()
            }
        }

        binding.btnConfirm.apply {
            this.text = confirm
            this.setOnClickListener {
                this@LiveDialog.dialog?.cancel()
                listener.onConfirmClick()
            }
        }
    }

    fun setTitleText(s:String){
        binding.tvTitle.text = s
    }

    fun setContentText(s:String){
        binding.tvContent.text = s
    }

    interface OnClickListener{
        fun onCancleClick(){}
        fun onConfirmClick()
    }
}
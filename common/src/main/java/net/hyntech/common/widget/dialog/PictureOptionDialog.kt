package net.hyntech.common.widget.dialog

import android.content.Context
import android.os.Bundle
import android.widget.Button
import net.hyntech.baselib.widget.dialog.BaseBottomDialog
import net.hyntech.common.R


class PictureOptionDialog(context: Context,val listener:OnClickListener):BaseBottomDialog(context,style = R.style.CommonDialogBottomStyle,isCancelable = true){

    interface OnClickListener {
        fun onCameraClick()
        fun onPhotoClick()
        fun onCancelClick(){}
    }

    override fun getLayoutId(): Int = R.layout.dialog_picture_option

    override fun initData(savedInstanceState: Bundle?) {

        findViewById<Button>(R.id.btn_camera)?.setOnClickListener {
            this.onCancel()
            this.listener.onCameraClick()
        }

        findViewById<Button>(R.id.btn_photo)?.setOnClickListener {
            this.onCancel()
            this.listener.onPhotoClick()
        }

        findViewById<Button>(R.id.btn_cancel)?.setOnClickListener {
            this.onCancel()
            this.listener.onCancelClick()
        }
    }
}
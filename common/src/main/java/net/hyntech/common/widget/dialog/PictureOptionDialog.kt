package net.hyntech.common.widget.dialog

import android.content.Context
import android.os.Bundle
import android.widget.Button
import net.hyntech.baselib.widget.dialog.BaseBottomDialog
import net.hyntech.common.R


class PictureOptionDialog(context: Context,val listener:OnClickListener):BaseBottomDialog(context,style = R.style.CommonDialogBottomStyle,isCancelable = true){

    companion object{
        const val TYPE_CAMERA = 1
        const val TYPE_PHOTO = 2
    }

    interface OnClickListener {
        fun onCameraClick(type:Int)
        fun onPhotoClick(type:Int)
        fun onCancelClick(){}
    }

    override fun getLayoutId(): Int = R.layout.dialog_picture_option

    override fun initData(savedInstanceState: Bundle?) {

        findViewById<Button>(R.id.btn_camera)?.setOnClickListener {
            this.onCancel()
            this.listener.onCameraClick(TYPE_CAMERA)
        }

        findViewById<Button>(R.id.btn_photo)?.setOnClickListener {
            this.onCancel()
            this.listener.onPhotoClick(TYPE_PHOTO)
        }

        findViewById<Button>(R.id.btn_cancel)?.setOnClickListener {
            this.onCancel()
            this.listener.onCancelClick()
        }
    }
}
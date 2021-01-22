package net.hyntech.baselib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import com.blankj.utilcode.util.ScreenUtils

open class BaseDailog(
    context: Context,
    val width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    val height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    val style:Int,
    val isCancelable:Boolean = false
) : Dialog(context, style) {

    init {
        setCancelable(isCancelable)
        window?.setGravity(Gravity.CENTER)
        //默认的Dialog只有5/6左右的宽度
        window?.decorView?.setPadding(0, 0, 0, 0);
        val lp = window?.attributes
        lp?.width = width
    lp?.width = (ScreenUtils.getScreenWidth() * 0.8).toInt()
        //lp?.height = (ScreenUtils.getScreenHeight() * 0.4).toInt()
        lp?.height = height
        window?.attributes = lp
    }
}
package net.hyntech.baselib.widget.dialog

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.blankj.utilcode.util.ScreenUtils

abstract class BaseBottomDialog(context: Context,
                                width: Int = (ScreenUtils.getScreenWidth() * 0.96).toInt(),
                                height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
                                gravity: Int = Gravity.BOTTOM,
                                style:Int,
                                isCancelable:Boolean = false):BaseDailog(context,width,height,gravity,style,isCancelable){

    override fun show() {
        if(this.isShowing){
           return
        }

        this.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        super.show()
        fullScreenImmersive(window?.decorView)
        this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    open fun fullScreenImmersive(view: View?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //布局位于状态栏下方
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            view?.systemUiVisibility = uiOptions
        }
    }

}
package net.hyntech.baselib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.blankj.utilcode.util.ScreenUtils

abstract class BaseDailog(
    context: Context,
    val width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    val height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    val gravity: Int,
    val style:Int,
    val isCancelable:Boolean = false
) : Dialog(context, style) {

    init {
        setCancelable(isCancelable)
        window?.setGravity(gravity)
        //默认的Dialog只有5/6左右的宽度
        window?.decorView?.setPadding(0, 0, 0, 0)
        val lp = window?.attributes
        lp?.width = width
        lp?.height = height
        window?.attributes = lp
    }

    abstract fun getLayoutId(): Int
    abstract fun initData(savedInstanceState: Bundle?)

    open fun onCancel(){
        this.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId:Int = getLayoutId()
        if(layoutId != 0){
            setContentView(layoutId)
            initData(savedInstanceState)
        }
    }

    override fun show() {
        if(!this.isShowing){
            super.show()
        }
    }
}
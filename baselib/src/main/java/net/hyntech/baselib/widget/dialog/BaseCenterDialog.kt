package net.hyntech.baselib.widget.dialog

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import com.blankj.utilcode.util.ScreenUtils

abstract class BaseCenterDialog(context: Context,
                                width: Int = (ScreenUtils.getScreenWidth() * 0.8).toInt(),
                                height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
                                gravity: Int = Gravity.CENTER,
                                style:Int,
                                isCancelable:Boolean = false):BaseDailog(context,width,height,gravity,style,isCancelable){





}
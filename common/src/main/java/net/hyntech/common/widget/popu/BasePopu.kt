package net.hyntech.common.widget.popu

import android.content.Context
import android.view.WindowManager
import android.view.animation.Animation
import razerdp.basepopup.BaseLazyPopupWindow
import razerdp.basepopup.BasePopupWindow
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.ScaleConfig

abstract class BasePopu(val context: Context,
                        val mWidth: Int = WindowManager.LayoutParams.WRAP_CONTENT,
                        val mHeight: Int = WindowManager.LayoutParams.WRAP_CONTENT): BaseLazyPopupWindow(context) {


    override fun setWidth(width: Int): BasePopupWindow {
        return super.setWidth(mWidth)
    }

    override fun setHeight(height: Int): BasePopupWindow {
        return super.setHeight(mHeight)
    }

    override fun onCreateShowAnimation(): Animation {
//        return super.onCreateShowAnimation()
        return AnimationHelper.asAnimation().withScale(ScaleConfig.TOP_TO_BOTTOM.duration(200L)).toShow()
    }


    override fun onCreateDismissAnimation(): Animation {
//        return super.onCreateDismissAnimation()
        return AnimationHelper.asAnimation().withScale(ScaleConfig.BOTTOM_TO_TOP.duration(100L)).toDismiss()
    }

}
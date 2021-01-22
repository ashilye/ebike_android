package net.hyntech.baselib.utils

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.StringUtils

class UIUtils {

    companion object {

        fun getString(@StringRes resId: Int): String {
            return StringUtils.getString(resId)
        }

        fun getColor(@ColorRes resId: Int): Int {
            return ColorUtils.getColor(resId)
        }

        fun getDrawable(@DrawableRes resId: Int): Drawable {
            return ResourceUtils.getDrawable(resId)
        }

        private var lastClickTime: Long = 0
        private const val DELAY_TIME: Long = 600

        fun isFastDoubleClick(): Boolean {
            val time = System.currentTimeMillis()
            val lastTime = time - lastClickTime
            if (0 < lastTime && lastTime < DELAY_TIME) {
                return true
            }
            lastClickTime = time
            return false
        }
    }
}
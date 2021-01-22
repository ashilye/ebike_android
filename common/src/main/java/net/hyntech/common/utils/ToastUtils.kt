package net.hyntech.common.utils

import android.text.TextUtils
import com.blankj.utilcode.util.ColorUtils
import net.hyntech.common.R
import com.blankj.utilcode.util.ToastUtils as Toast

class ToastUtils {
    companion object {
        @JvmStatic
        fun showToast(s: String?) {
            if (!TextUtils.isEmpty(s)) {
                Toast.getDefaultMaker().setBgColor(ColorUtils.getColor(R.color.common_color_toast)).setTextColor(ColorUtils.getColor(R.color.common_white))
                Toast.showShort(s)
            }
        }
    }
}



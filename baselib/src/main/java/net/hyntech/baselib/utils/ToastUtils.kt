package net.hyntech.baselib.utils

import android.text.TextUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ToastUtils as Toast

class ToastUtils {
    companion object {
        @JvmStatic
        fun showToast(s: String?) {
            if (!TextUtils.isEmpty(s)) {
                Toast.getDefaultMaker().setBgColor(ColorUtils.getColor(android.R.color.background_light)).setTextColor(ColorUtils.getColor(android.R.color.black))
                Toast.showShort(s)
            }
        }
    }
}



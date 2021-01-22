package net.hyntech.common.ext

import android.app.Service
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import net.hyntech.baselib.app.BaseApp
import net.hyntech.baselib.utils.UIUtils
import net.hyntech.baselib.widget.imageloader.ImageLoader
import net.hyntech.baselib.widget.imageloader.TransType
import net.hyntech.baselib.widget.imageloader.glide.GlideImageConfig
import net.hyntech.common.R

fun View.showKeyboard() {
    (BaseApp.instance.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (BaseApp.instance.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.GONE
}

fun EditText.afterTextChanged(action: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action(s.toString())
        }
    })
}

//常用图片加载
fun ImageView.loadImage(any: Any) = ImageLoader.getInstance().loadImage(
    BaseApp.instance,
    GlideImageConfig(
        any,
        this,
        placeholder = R.drawable.ic_default_placeholder,
        errorPic = R.drawable.ic_default_errorpic
    ).also { c -> c.type = TransType.NORMAL })

//自定义配置图片加载
fun ImageView.loadConfigImage(
    any: Any,
    config: GlideImageConfig = GlideImageConfig(
        any,
        this,
        placeholder = R.drawable.ic_default_placeholder,
        errorPic = R.drawable.ic_default_errorpic
    ).also { c -> c.type = TransType.NORMAL }
) = ImageLoader.getInstance().loadImage(BaseApp.instance, config)

//是否快速点击了 true 否, false 是
fun View.isFastClick(): Boolean {
    return !UIUtils.isFastDoubleClick()
}
package net.hyntech.baselib.widget.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ScreenUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseDialogFragment <VB : ViewDataBinding> : DialogFragment(),
    CoroutineScope by MainScope() {

    protected lateinit var binding: VB
    private var savedState = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog_Alert)
        dialog?.window?.let {
            it.requestFeature(Window.FEATURE_NO_TITLE)
            //it.setWindowAnimations(R.style.DialogPushInOutAnimation)
        }

        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        savedState = true
        super.onSaveInstanceState(outState)
    }

    fun showAllowStateLoss(manager: FragmentManager, tag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.isStateSaved) return
        }

        if (savedState) return
        show(manager, tag)
    }

    override fun onStart() {
        super.onStart()

        val attrs = dialog?.window?.attributes?.apply {
            width = (ScreenUtils.getScreenWidth() * 0.8f).toInt()
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        dialog?.setCancelable(getCancelable())
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(0))
            attributes = attrs
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        initFragment(view, savedInstanceState)
    }



    override fun onDestroy() {
        super.onDestroy()
        cancel()
        binding.unbind()
    }

    abstract fun getLayoutId(): Int

    abstract fun initFragment(view: View, savedInstanceState: Bundle?)

    /**
     * 是否点击外部可关闭dialog
     * true 可关闭
     * false 不可关闭
     */
    open fun getCancelable():Boolean{
        return true
    }
}
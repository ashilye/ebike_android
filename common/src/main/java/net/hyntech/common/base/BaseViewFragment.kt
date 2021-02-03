package net.hyntech.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.OnRetryEventListener
import com.zy.multistatepage.bindMultiState
import kotlinx.coroutines.*
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.base.ViewModelLazy
import net.hyntech.common.R
import net.hyntech.common.widget.state.showEmpty
import net.hyntech.common.widget.state.showError
import net.hyntech.common.widget.state.showLoading
import net.hyntech.common.widget.state.showSuccess

abstract class BaseViewFragment<VB : ViewDataBinding, VM : BaseViewModel>: net.hyntech.common.base.BaseFragment(), CoroutineScope by MainScope()  {

    /**
     * 多状态视图
     * 如果使用多状态视图,子类必须重写 hasUsedStateView 并返回 true,即可调用 onStateXXX() 等方法
     * 标题栏 不属于多状态视图内的View,布局文件中需要有一个id为 common_container 作为 切换的视图主体
     * 否则为整个 contentView
     */
    protected var multiState: MultiStateContainer? = null

    protected lateinit var binding: VB

    protected var navController: NavController? = null


    abstract fun bindViewModel()

    open fun hasNavController(): Boolean = false

    open fun hasUsedStateView(): Boolean = false

    inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> {
        return lazy {
            ViewModelProvider(requireActivity()).get(VM::class.java)
        }
    }

//    inline fun <reified VM : ViewModel> viewModels(
//        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
//    ): Lazy<VM> {
//        val factoryPromise = factoryProducer ?: {
//            defaultViewModelProviderFactory
//        }
//        return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
//    }

    override fun setContentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return if(hasUsedStateView()){
            multiState = binding.root.let {rootView ->
                rootView.findViewById<View>(R.id.common_container)?.bindMultiState(object : OnRetryEventListener {
                    override fun onRetryEvent(container: MultiStateContainer?) {
                        onClickProxy {
                            onStateRetry(container)
                        }
                    }
                }) ?: rootView.bindMultiState(object : OnRetryEventListener {
                    override fun onRetryEvent(container: MultiStateContainer?) {
                        onClickProxy {
                            onStateRetry(container)
                        }
                    }
                })
            }
        //    multiState?:binding.root
            if(binding.root.findViewById<View>(R.id.common_container) == null){
                multiState!!
            }else{
                binding.root
            }
        }else{
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding.lifecycleOwner = this
        this.bindViewModel()
        if (this.hasNavController()) {
            this.navController = Navigation.findNavController(view)
        }
    }


    open fun onStateLoading(){
        if(hasUsedStateView())multiState?.showLoading()
    }

    open fun onStateEmpty(){
        if(hasUsedStateView())multiState?.showEmpty()
    }

    open fun onStateError(){
        if(hasUsedStateView())multiState?.showError()
    }

    open fun onStateSuccess(){
        if(hasUsedStateView()){
//            GlobalScope.launch(Dispatchers.IO) {
//                delay(1000)
//                runOnUiThread {
//                    multiState?.showSuccess()
//                }
//            }
            multiState?.showSuccess()
        }
    }

    open fun onStateRetry(container: MultiStateContainer?){}


    /**
     * 返回
     */
    open fun onPopBack(){
        if(hasNavController()){
            this.navController?.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        this.binding.unbind()
    }

}
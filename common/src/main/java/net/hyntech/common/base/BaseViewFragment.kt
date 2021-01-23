package net.hyntech.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.OnRetryEventListener
import com.zy.multistatepage.bindMultiState
import kotlinx.coroutines.*
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.common.widget.state.showEmpty
import net.hyntech.common.widget.state.showError
import net.hyntech.common.widget.state.showLoading
import net.hyntech.common.widget.state.showSuccess


abstract class BaseViewFragment<VB : ViewDataBinding, VM : BaseViewModel>: net.hyntech.common.base.BaseFragment(), CoroutineScope by MainScope()  {

    private lateinit var multiState: MultiStateContainer
    protected lateinit var binding: VB

    protected var navController: NavController? = null


    abstract fun bindViewModel():BaseViewModel

    open fun hasNavController(): Boolean = false


    inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> {
        return lazy {
            ViewModelProvider(requireActivity()).get(VM::class.java)
        }
    }

    override fun setContentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        val view = binding.root
        multiState = view.bindMultiState(object : OnRetryEventListener {
            override fun onRetryEvent(container: MultiStateContainer?) {
                onStateRetry(container)
            }
        })
        return multiState
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding.lifecycleOwner = this
        this.bindViewModel().defUI.let { def ->
            def.showDialog.observe(this, Observer {
                showLoading()
            })
            def.dismissDialog.observe(this, Observer {
                dismissLoading()
            })
            def.toastEvent.observe(this, Observer {
                showToast(it)
            })
        }
        if (this.hasNavController()) {
            this.navController = Navigation.findNavController(view)
        }
    }


    open fun onStateLoading(){
        multiState.showLoading()
    }

    open fun onStateEmpty(){
        multiState.showEmpty()
    }

    open fun onStateError(){
        multiState.showError()
    }

    open fun onStateSuccess(){
        multiState.showSuccess()
    }

    open fun onStateRetry(container: MultiStateContainer?){}


    override fun onDestroy() {
        super.onDestroy()
        cancel()
        this.binding.unbind()
    }

}
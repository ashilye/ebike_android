package net.hyntech.common.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.hyntech.baselib.base.BaseViewModel
import net.hyntech.baselib.base.ViewModelLazy


abstract class BaseViewFragment<VB : ViewDataBinding, VM : BaseViewModel>: net.hyntech.common.base.BaseFragment<VB, VM>() {


    inline fun <reified VM : ViewModel> viewModels(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM> {
        val factoryPromise = factoryProducer ?: {
            defaultViewModelProviderFactory
        }
        return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
    }



}


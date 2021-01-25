package net.hyntech.baselib.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import kotlinx.coroutines.*
import net.hyntech.baselib.app.BaseApp
import net.hyntech.baselib.app.manager.SingleLiveEvent
import net.hyntech.baselib.ext.isResultSuccess
import net.hyntech.baselib.http.BaseResponse
import net.hyntech.baselib.http.error.ExceptionHandle
import net.hyntech.baselib.http.error.ResponseThrowable
import net.hyntech.baselib.utils.LogUtils
import net.hyntech.baselib.utils.UIUtils
import java.lang.Exception

open class BaseViewModel : ViewModel(), LifecycleObserver {


    val defUI: UIChange by lazy { UIChange() }

    val app: BaseApp by lazy {
        BaseApp.instance
    }


    fun onClickProxy(m: () -> Unit) {
        if (!UIUtils.isFastDoubleClick()) {
            m()
        }
    }


    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
    }

    /**
     *  不过滤请求结果
     * @param block 请求体
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun launchGo(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
            defUI.toastEvent.postValue("${it.code}:${it.errMsg}")
        },
        complete: suspend CoroutineScope.() -> Unit = {},
        isShowDialog: Boolean = true
    ) {
        if (!NetworkUtils.isConnected()) {
            defUI.toastEvent.postValue("网络异常,请检查网络！")
            return
        }
        if (isShowDialog) defUI.showDialog.call()
        launchUI {
            handleException(
                withContext(Dispatchers.IO) { block },
                { error(it) },
                {
                    defUI.dismissDialog.call()
                    complete()
                }
            )
        }
    }

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun <T> launchOnlyResult(
        block: suspend CoroutineScope.() -> BaseResponse<T>,
        success: (T?) -> Unit,
        error: (ResponseThrowable) -> Unit = {
            defUI.toastEvent.postValue(it.errMsg)
        },
        complete: () -> Unit = {},
        isShowDialog: Boolean = true,
        isShowToast: Boolean = true
    ) {
        if (!NetworkUtils.isConnected()) {
            defUI.toastEvent.postValue("网络异常,请检查网络！")
            return
        }
        if (isShowDialog) defUI.showDialog.call()
        launchUI {
            handleException(
                { withContext(Dispatchers.IO) { block() } },
                { res ->
                    executeResponse(res) {
                        success(it)
                    }
                },
                {
                    if (isShowToast) error(it)
                },
                {
                    if (isShowDialog) defUI.dismissDialog.call()
                    complete()
                }
            )
        }
    }

    /**
     * 请求结果过滤
     */
    private suspend fun <T> executeResponse(
        response: BaseResponse<T>,
        success: suspend CoroutineScope.(T?) -> Unit
    ) {
        coroutineScope {
            if (response.code.isResultSuccess()) success(response.data)
            else {
                throw ResponseThrowable(response.code, response.msg)
            }
        }
    }

    /**
     * 异常统一处理
     */
    private suspend fun <T> handleException(
        block: suspend CoroutineScope.() -> BaseResponse<T>,
        success: suspend CoroutineScope.(BaseResponse<T>) -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        try {
            coroutineScope {
                try {
                    success(block())
                    LogUtils.logGGQ("-->>-T-<<")
                } catch (e: ResponseThrowable) {
                    error(ExceptionHandle.handleException(e, e))
                } finally {
                    complete()
                }
            }
        } catch (e: Exception) {
            e.fillInStackTrace()
            LogUtils.logGGQ("异常--e->${e.message}")
            defUI.toastEvent.postValue("数据异常,请稍候再试！")
            defUI.dismissDialog.call()
        }
    }


    /**
     * 异常统一处理
     */
    private suspend fun handleException(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                block()
                LogUtils.logGGQ("-->>-N-T-<<")
            } catch (e: ResponseThrowable) {
                error(ExceptionHandle.handleException(e, e))
            } finally {
                complete()
            }
        }
    }


    /**
     * UI事件
     */
    inner class UIChange {
        val showDialog by lazy { SingleLiveEvent<Any>() }
        val dismissDialog by lazy { SingleLiveEvent<Any>() }
        val toastEvent by lazy { SingleLiveEvent<String>() }

        //0 content --  1 loading --  2 empty --  3 error
        val stateViewEvent by lazy { MutableLiveData<Int>() }
        fun showUIContent(){
            stateViewEvent.postValue(0)
        }

        fun showUILoading(){
            stateViewEvent.postValue(1)
        }

        fun showUIEmpty(){
            stateViewEvent.postValue(2)
        }

        fun showUIError(){
            stateViewEvent.postValue(3)
        }
    }


    override fun onCleared() {
        super.onCleared()
        LogUtils.logGGQ("VM ->> onCleared")
    }
}
package net.hyntech.common.widget.state

import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.state.SuccessState

//内容视图
fun MultiStateContainer.showSuccess(callBack: () -> Unit = {}) {
    show<SuccessState> {
        callBack.invoke()
    }
}

//自定义的错误视图
fun MultiStateContainer.showError(callBack: () -> Unit = {}) {
    show<MyErrorState> {
        callBack.invoke()
    }
}

//自定义的空视图
fun MultiStateContainer.showEmpty(callBack: () -> Unit = {}) {
    show<MyEmptyState> {
        callBack.invoke()
    }
}

//自定义的加载视图
fun MultiStateContainer.showLoading(callBack: () -> Unit = {}) {
    show<MyLoadingState> {
        callBack.invoke()
    }
}
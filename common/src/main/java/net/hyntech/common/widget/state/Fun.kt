package net.hyntech.common.widget.state

import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.state.EmptyState
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState

fun MultiStateContainer.showSuccess(callBack: () -> Unit = {}) {
    show<SuccessState> {
        callBack.invoke()
    }
}

fun MultiStateContainer.showError(callBack: () -> Unit = {}) {
    show<ErrorState> {
        callBack.invoke()
    }
}

fun MultiStateContainer.showEmpty(callBack: () -> Unit = {}) {
    show<EmptyState> {
        callBack.invoke()
    }
}

fun MultiStateContainer.showLoading(callBack: () -> Unit = {}) {
    show<LoadingState> {
        callBack.invoke()
    }
}
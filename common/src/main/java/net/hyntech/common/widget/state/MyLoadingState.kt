package net.hyntech.common.widget.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.MultiStatePage
import net.hyntech.common.R

/**
 * 自定义加载视图
 */
class MyLoadingState: MultiState() {

    private var tvLoadingMsg: TextView? = null

    override fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return inflater.inflate(R.layout.my_state_loading, container, false)
    }

    override fun onMultiStateViewCreate(view: View) {
        tvLoadingMsg = view.findViewById(R.id.tv_loading_msg)
    }
}
package net.hyntech.common.widget.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.MultiStatePage
import net.hyntech.common.R

/**
 * 自定义错误视图
 */
class MyErrorState: MultiState() {

    private var tvErrorMsg: TextView? = null
    private var imgError: ImageView? = null
    private var tvRetry: TextView? = null

    override fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return inflater.inflate(R.layout.my_state_error, container, false)
    }

    override fun onMultiStateViewCreate(view: View) {
        tvErrorMsg = view.findViewById(R.id.tv_error_msg)
        imgError = view.findViewById(R.id.img_error)
        tvRetry = view.findViewById(R.id.tv_retry)

    }

    override fun enableReload(): Boolean = true

    override fun bindRetryView(): View? {
        return tvRetry
    }
}
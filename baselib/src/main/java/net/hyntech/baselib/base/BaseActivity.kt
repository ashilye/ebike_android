package net.hyntech.baselib.base

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import net.hyntech.baselib.utils.Event
import net.hyntech.baselib.utils.EventBusUtils
import net.hyntech.baselib.utils.UIUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity(), IView {

    private val KEY_BUNDLE_DATA: String = "BUNDLE_DATA"

    abstract fun getLayoutId(): Int
    abstract fun initData(savedInstanceState: Bundle?): Unit

    open fun hasUsedEventBus(): Boolean = false
    open fun hasStatusBarMode(): Boolean = false
    open fun hasEventKeyBack(): Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        if (hasStatusBarMode()) {
            setStatusBarMode()
        }
        super.onCreate(savedInstanceState)
        setContentLayout()
        if (hasUsedEventBus()) {
            EventBusUtils.register(this)
        }
        initData(savedInstanceState)
    }

    open fun setStatusBarMode(color: Int = Color.TRANSPARENT) {
        BarUtils.setStatusBarColor(this, color)
    }

    open fun setContentLayout() {
        setContentView(getLayoutId())
    }

    fun onClickProxy(m: () -> Unit) {
        if (!UIUtils.isFastDoubleClick()) {
            m()
        }
    }

    /**
     * 接收到普通的Event
     * 封装MAIN线程模式，子类可重写 onEvnetBusReceive,
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun <T> onEventBusReceive(event: Event<T>?) {
        if (event != null) {
            onEventBusDispense(event)
        }
    }

    /**
     * 接收到粘性的Event
     * 封装MAIN线程模式，子类可重写 onStickyEvnetBusReceive,
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun <T> onStickyEventBusReceive(event: Event<T>?) {
        if (event != null) {
            onStickyEventBusDispense(event)
        }
    }

    /**
     * 子类重写onEventBusDispense，处理接收到的普通事件
     */
    open fun <T> onEventBusDispense(event: Event<T>) {}

    /**
     * 子类重写onStickyEventBusDispense，处理接收到的粘性事件
     */
    open fun <T> onStickyEventBusDispense(event: Event<T>) {}

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (hasEventKeyBack()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onKeyBack(keyCode)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    open fun onKeyBack(keyCode: Int) {}


    open fun getBundle(): Bundle? {
        return this.intent?.getBundleExtra(KEY_BUNDLE_DATA)
    }

    open fun onStartActivity(
        clazz: Class<out Activity?>,
        bundle: Bundle? = null,
        isFinish: Boolean = false
    ) {
        this.startActivity(Intent(this, clazz).apply {
            bundle?.let {
                this.putExtra(KEY_BUNDLE_DATA, it)
            }
        })
        if (isFinish) this.finish()
    }

    open fun onStartActivityForResult(
        clazz: Class<out Activity?>,
        requestCode: Int,
        bundle: Bundle? = null,
        isFinish: Boolean = false
    ) {
        this.startActivityForResult(Intent(this, clazz).apply {
            bundle?.let {
                this.putExtra(KEY_BUNDLE_DATA, it)
            }
        }, requestCode)
        if (isFinish) this.finish()
    }


    open fun onFinish(isResultOK: Boolean = false) {
        if (isResultOK) {
            setResult(Activity.RESULT_OK)
        }
        this.finish()
    }

    open fun onFinishByIntent(intent: Intent) {
        setResult(Activity.RESULT_OK, intent)
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (hasUsedEventBus()) {
            EventBusUtils.unregister(this)
        }
    }
}
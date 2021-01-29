package net.hyntech.baselib.base

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import net.hyntech.baselib.R
import net.hyntech.baselib.utils.Event
import net.hyntech.baselib.utils.EventBusUtils
import net.hyntech.baselib.utils.UIUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/***
 * 基础组件封装
 */
abstract class BaseActivity : AppCompatActivity(), IView {

    /// 传递bundle 数据 Key
    private val KEY_BUNDLE_DATA: String = "BUNDLE_DATA"

    /// 布局id
    abstract fun getLayoutId(): Int

    abstract fun initData(savedInstanceState: Bundle?): Unit

    /// 是否使用 event bus
    open fun hasUsedEventBus(): Boolean = false
    // 是否使用状态栏
    open fun hasStatusBarMode(): Boolean = false
    // 是否使用 返回键拦截
    open fun hasEventKeyBack(): Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout()
        if (hasStatusBarMode()) {
            setStatusBarMode()
        }
        if (hasUsedEventBus()) {
            EventBusUtils.register(this)
        }
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    /// base 中相比 initData 之前的调用的方法,用来封装初始化下拉刷新组件
    open fun initView(savedInstanceState: Bundle?){}

    /// 默认透明状态栏
    open fun setStatusBarMode(color: Int = Color.TRANSPARENT) {}

    /// 设置布局文件(BaseActivity 和 BaseViewActivity分别重写)
    open fun setContentLayout() {
        setContentView(getLayoutId())
    }

    /// 按钮防抖判断
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


    /// 获取封装 bundle
    open fun getBundle(): Bundle? {
        return this.intent?.getBundleExtra(KEY_BUNDLE_DATA)
    }

    /***
     * 打开新的页面
     * @param clazz 新页面
     * @param bundle 传递bundle 数据,非必填
     * @param isFinish 是否关闭当前页面 默认不关闭
     */
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

    /***
     * 打开新的页面带结果
     * @param clazz 新页面
     * @param requestCode 请求code
     * @param bundle 传递bundle 数据,非必填
     * @param isFinish 是否关闭当前页面 默认不关闭
     */
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


    /***
     * 关闭页面
     * @param isResultOK 是否以设置结果关闭
     */
    open fun onFinish(isResultOK: Boolean = false) {
        if (isResultOK) {
            setResult(Activity.RESULT_OK)
        }
        this.finish()
    }

    /***
     * 关闭页面 (用于 onStartActivityForResult )
     * @param intent 携带intent数据关闭页面
     */
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
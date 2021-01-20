package net.hyntech.baselib.app.config

/**
 * 全局配置
 */
object GlobalConfig {

    const val gIsDebug: Boolean = true
    /**
     * 是否保存 log 到缓存目录。目录地址：
     * /sdcard/Android/data/应用包名/cache/Log
     *
     * 建议打包提测的都开启保存。比如 beta 构建选项
     */
    const val gIsSaveLog = true

    /**
     * 是否需要管理 Activity 堆栈
     */
    const val gIsNeedActivityManager = true

    /**
     * 是否需要动态修改 BaseURL，如果需要，请设置为 true，并在合适的位置调用：[com.imyyq.mvvm.http.HttpRequest.multiClickToChangeBaseUrl]
     */
    const val gIsNeedChangeBaseUrl = true

    object ImageView {
        var errorRes: Int? = null
        var placeholderRes: Int? = null
    }


    object Click {

        /**
         * 在 xml 配置点击事件，可配置的属性如下：
         * onClickCommand 点击事件
         * isInterval 是否开启防止点击过快
         * intervalMilliseconds 防止点击过快的间隔时间，毫秒为单位
         *
         * 这里可全局设置是否开启防止点击事件过快的功能，局部可单独开启或关闭。
         *
         * 如果关闭，那么和 setOnClickListener 没啥区别
         */
        var gIsClickInterval = false

        /**
         * 点击事件时间间隔
         */
        var gClickIntervalMilliseconds = 800
    }
}
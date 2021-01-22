package net.hyntech.common.widget.timer

interface MyCountDownTimerListener {
    fun onStart()
    fun onTick(millisUntilFinished: Long)
    fun onFinish()
}
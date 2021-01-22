package net.hyntech.common.widget.timer

import android.os.CountDownTimer

class MyCountDownTimer(
    millisInFuture: Long,
    private val countDownInterval: Long,
    private val listener: MyCountDownTimerListener
) : CountDownTimer(millisInFuture, countDownInterval) {


    override fun onTick(millisUntilFinished: Long) {
        listener.onTick(millisUntilFinished/countDownInterval)
    }


    override fun onFinish() {
        listener.onFinish()
    }
}
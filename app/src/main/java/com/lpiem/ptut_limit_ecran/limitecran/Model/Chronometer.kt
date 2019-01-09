package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.os.SystemClock
import com.lpiem.ptut_limit_ecran.limitecran.ChronometerFragment
import com.lpiem.ptut_limit_ecran.limitecran.MainActivity

class Chronometer(chronometerFragment: ChronometerFragment, mainActivity: MainActivity){
    private var milliseconds : Long = 0
    private var seconds : Int = 0
    private var minutes : Int = 0
    private var hours : Int = 0
    private var startTime = 0L
    private var timeSwapBuff = 0L
    private var updateTime = 0L
    private val chronometerFragment:ChronometerFragment = chronometerFragment

    private val mainActivity: MainActivity = mainActivity

    private val timerHandler = android.os.Handler()
    private val timerRunnable = object : Runnable {
        override fun run() {
        milliseconds = SystemClock.uptimeMillis() - startTime
        updateTime = timeSwapBuff + milliseconds
        var seconds = (updateTime / 1000).toInt()
        var minutes = seconds / 60
        seconds %= 60
        hours = minutes / 60
        minutes %= 60
        timerHandler.postDelayed(this, 0)
        chronometerFragment.updateTextView(formatTimeIntoText(seconds, minutes, hours))
        //mainActivity.updateNotification(formatTimeIntoText(seconds, minutes, hours))
        }
    }

    var MilliSeconds : Long
        get() = this.milliseconds
        set(newValue){
            this.milliseconds = newValue
        }
    var Seconds : Int
        get() = this.seconds
        set(newValue){
            this.seconds = newValue
        }
    var Minutes : Int
        get() = this.minutes
        set(newValue){
            this.minutes = newValue
        }
    var Hours : Int
        get() = this.hours
        set(newValue){
            this.hours = newValue
        }

    fun startChrono(){
        timerHandler.postDelayed(timerRunnable,0)
    }

    fun stopChrono(){

    }

    fun formatTimeIntoText(seconds: Int, minutes: Int, hours: Int): String {
        return (if (hours < 10) "0" + hours.toString() else hours.toString()) + " : " +
                (if (minutes < 10) "0" + minutes.toString() else minutes.toString()) + " : " +
                if (seconds < 10) "0" + seconds.toString() else seconds.toString()
    }
}
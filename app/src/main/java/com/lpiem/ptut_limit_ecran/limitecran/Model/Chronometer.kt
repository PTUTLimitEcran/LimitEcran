package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.os.Handler
import android.os.SystemClock
import com.lpiem.ptut_limit_ecran.limitecran.TreeFragment

class Chronometer(treeFragment: TreeFragment){
    private var milliseconds : Long = 0L
    private var seconds : Int = 0
    private var minutes : Int = 0
    private var hours : Int = 0
    private var startTime = 0L
    private var timeSwapBuff = 0L
    private var updateTime = 0L
    private val treeFragment:TreeFragment = treeFragment
    private var hasChronometerStarted:Boolean=false

    //private val mainActivity: SplashScreenActivity = mainActivity

    private var timerHandler = Handler()
    private var timerRunnable: Runnable? = null

    var ChronometerStartStatus:Boolean
    get() = this.hasChronometerStarted
    set(value){
        this.hasChronometerStarted=value
    }

    fun initChrono() {
        startTime = SystemClock.uptimeMillis()
        this.timerRunnable = object : Runnable {
            override fun run() {
                milliseconds = SystemClock.uptimeMillis() - startTime
                updateTime = timeSwapBuff + milliseconds
                seconds = (updateTime / 1000).toInt()
                minutes = seconds / 60
                seconds %= 60
                hours = minutes / 60
                minutes %= 60
                timerHandler.postDelayed(this, 0)
                treeFragment.updateTextView(formatTimeIntoText(seconds,minutes,hours))
                hasChronometerStarted = true
            }
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
        this.timeSwapBuff += this.milliseconds
        timerHandler.removeCallbacks(timerRunnable)
    }

    fun formatTimeIntoText(seconds: Int, minutes: Int, hours: Int): String {
        return (if (hours < 10) "0" + hours.toString() else hours.toString()) + " : " +
                (if (minutes < 10) "0" + minutes.toString() else minutes.toString()) + " : " +
                if (seconds < 10) "0" + seconds.toString() else seconds.toString()
    }
}
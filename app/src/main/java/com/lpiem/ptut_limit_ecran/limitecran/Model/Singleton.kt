package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.app.NotificationManager
import android.content.Context
import android.os.CountDownTimer
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.lpiem.ptut_limit_ecran.limitecran.TreeFragment
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class Singleton(context: Context) {

    private var firstime = false
    private var currentCountDownTimer = 0L
    private lateinit var countDownTimer:CountDownTimer
    private lateinit var smallRemoteView:RemoteViews
    private lateinit var largeRemoteViews:RemoteViews

    private var size = 0
    init {
        initSingleton(context)
    }

    var FirstTime:Boolean
    get() = firstime
    set(newValue){
        firstime = newValue
    }

    var SmallRemoteView:RemoteViews
    get() = smallRemoteView
    set(newValue){
        smallRemoteView = newValue
    }

    var CurrentCountDownTimer:Long
    get() = currentCountDownTimer
    set(newValue){
        currentCountDownTimer = newValue
    }

    var LargeRemoteView:RemoteViews
    get() = largeRemoteViews
    set(newValue){
        largeRemoteViews = newValue
    }

    var IsRunning:Boolean
    get() = isRunning
    set(newValue){
        isRunning = newValue
    }

    var ScreenSize:Int
    get() = size
    set(newValue){
        size = newValue
    }

    var TreeList:HashMap<Date, ArrayList<TreeImage>>
    get() = treeList
    set(newValue){
        treeList = newValue
    }

    var Notification:NotificationCompat.Builder
    get() = notification
    set(newValue){
        notification = newValue
    }

    var NotificationChannel:NotificationManager
    get() = notificationManager
    set(newValue){
        notificationManager = newValue
    }

    /**
     * Start the chronometer
     */
    fun initCountDownTimer(countDownTimerTime:Long, currentFragment:TreeFragment){
        countDownTimer = object : CountDownTimer(countDownTimerTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentCountDownTimer = millisUntilFinished
                /*if(millisUntilFinished%1000 == 0L){
                    currentFragment.updateTextView(formatTime(millisUntilFinished))
                }*/
            }
            override fun onFinish() {
                isRunning = false
            }
        }
    }

    fun formatTime(countDownTimer: Long):String{
        var seconds = (countDownTimer/1000)
        var minutes = (seconds/60)
        var hours = (minutes/60)
        hours %= 24
        minutes %= 60
        seconds %= 60
        return (if(hours<10)"0"+hours.toString()else hours.toString()) +" : "+
                (if(minutes<10)"0"+minutes.toString()else minutes.toString()) +" : "+
                if(seconds<10)"0"+seconds.toString()else seconds.toString()
    }

    fun startCountDownTimer(){
        countDownTimer.start()
    }

    fun pauseCountDownTimer(){
        countDownTimer.cancel()
    }

    fun resumeCountDownTimer(currentFragment:TreeFragment){
        initCountDownTimer(currentCountDownTimer, currentFragment)
        countDownTimer.start()
    }

    companion object{
        private lateinit var notification: NotificationCompat.Builder
        private lateinit var notificationManager: NotificationManager
        private lateinit var treeList: HashMap<Date, ArrayList<TreeImage>>
        private var isRunning:Boolean = false

        private lateinit var context: Context
        var loadingTreeImageRegex: String = "^wonder_tree{1}.{0,}[.png]{1}"

        private var initialized: Boolean=false

        private lateinit var singleton: Singleton

        fun getInstance(context: Context):Singleton{
            if(initialized == true){
                return this.singleton
            }else{
                initialized = true
                singleton = Singleton(context)
                return singleton
            }
        }

        fun importImageList(){
            treeList = HashMap()
            val list = File(Environment.getExternalStorageDirectory().absolutePath+"/LimitEcran").listFiles()
            for(i in 0 until list.size){
                if(Regex(loadingTreeImageRegex).matches(list[i].name)){
                    val date = Date(list[i].lastModified())
                    date.minutes = 0
                    date.seconds = 0
                    date.hours = 0
                    if(!isDateIndexAlreadyPresentInArray(date)){
                        treeList[date] = ArrayList()
                    }
                    treeList[date]!!.add(TreeImage(list[i].name, date))
                }
            }
        }

        private fun isDateIndexAlreadyPresentInArray(date:Date):Boolean{
            return (treeList.containsKey(date))
        }

        fun initSingleton(context: Context){
            this.context = context
            this.treeList = HashMap()
        }
    }

    /*fun loadImages(){
        importImageList()
    }*/
}
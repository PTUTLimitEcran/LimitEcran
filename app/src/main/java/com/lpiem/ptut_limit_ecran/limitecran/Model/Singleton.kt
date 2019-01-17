package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Chronometer
import android.widget.RemoteViews
import com.lpiem.ptut_limit_ecran.limitecran.R
import com.lpiem.ptut_limit_ecran.limitecran.TreeFragment
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class Singleton(context: Context) {

    val timeInterval = 1000L

    private var firstime = false
    private var isDeviceOn = false
    private var currentCountDownTimer = 0L
    private lateinit var countDownTimer:CountDownTimer
    private var smallRemoteView = RemoteViews(context.packageName, R.layout.notification_small)

    var IsDeviceOn:Boolean
    get() = isDeviceOn
    set(value){
        isDeviceOn = value
    }

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

    fun initNotificationChannel(context: Context, notificationService:NotificationManager){
        NotificationChannel = notificationService
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Register the channel with the system
            val notificationManager: NotificationManager = notificationService
            notificationManager.createNotificationChannel(
                android.app.NotificationChannel(
                    context.getString(R.string.channelId),
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = context.getString(R.string.channel_description)
                })
        }
        Notification.setDefaults(android.app.Notification.DEFAULT_LIGHTS or android.app.Notification.DEFAULT_SOUND)
            .setVibrate(longArrayOf(0L)) // Passing null here silently fails
        NotificationChannel.notify(0, Notification.build())
    }

    /**
     * Start the chronometer
     */
    fun initCountDownTimer(countDownTimerTime:Long, currentFragment:TreeFragment){
        singleton.SmallRemoteView.setTextViewText(R.id.smallNotificationChrono, formatTime(countDownTimerTime))
        notificationManager.notify(0, notification.build())
        countDownTimer = object : CountDownTimer(countDownTimerTime, timeInterval) {
            override fun onTick(millisUntilFinished: Long) {
                currentCountDownTimer = millisUntilFinished
                Log.d("Compteur", "$currentCountDownTimer")
                if(isDeviceOn){
                    updateNotification(formatTime(millisUntilFinished))
                }
            }
            override fun onFinish() {
                isRunning = false
            }
        }
    }

    fun updateNotification(formattedTime: String) {
        singleton.SmallRemoteView.setTextViewText(R.id.smallNotificationChrono,formattedTime)
        singleton.NotificationChannel.notify(0,singleton.Notification.build())
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

    fun initNotification(context: Context, channelId:String, channelName:String, channelDescription:String){
        smallRemoteView.setImageViewResource(R.id.notificationIcon,R.drawable.ic_phonelink_erase_black_24dp)
        notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_phonelink_erase_black_24dp)
            .setContentTitle(channelName)
            .setOngoing(true)
            .setContentText(channelDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCustomContentView(singleton.SmallRemoteView)
            //.setCustomBigContentView(singleton.SmallRemoteView)
    }

    companion object{
        private lateinit var notification: NotificationCompat.Builder
        private lateinit var notificationManager: NotificationManager
        private lateinit var treeList: HashMap<Date, ArrayList<TreeImage>>
        private var isRunning:Boolean = false

        private lateinit var context: Context
        var loadingTreeImageRegex: String = "^wonder_tree.*[.png]"

        private var initialized: Boolean=false

        private lateinit var singleton: Singleton

        private lateinit var chronometer: Chronometer

        fun getInstance(context: Context):Singleton{
            if(initialized == true){
                return singleton
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
                    date.hours = i
                    if(!isDateIndexAlreadyPresentInArray(date)){
                        treeList[date] = ArrayList()
                    }
                    treeList[date]!!.add(TreeImage(list[i].name, date))
                } else {
                    Log.d("SINGLETON", "out of regex")
                }
            }
        }

        private fun isDateIndexAlreadyPresentInArray(date:Date):Boolean{
            return treeList.containsKey(date)
        }

        fun initSingleton(newContext: Context){
            context = newContext
            treeList = HashMap()
        }
    }

    fun loadImages(){
        importImageList()
    }

    var Chronometer: Chronometer
        get() = chronometer
        set(value){
            chronometer = value
        }

}
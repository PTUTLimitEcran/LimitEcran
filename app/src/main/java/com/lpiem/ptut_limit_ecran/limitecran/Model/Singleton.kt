package com.lpiem.ptut_limit_ecran.limitecran.Model


import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.os.Environment
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.lpiem.ptut_limit_ecran.limitecran.TreeFragment
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Singleton(context: Context) {

    private var firstime = true
    init {
        initSingleton(context)
    }

    var IsRunning:Boolean
    get() = isRunning
    set(newValue){
        isRunning = newValue
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
     * Create an instance of the [Chronometer] class
     * @param chronoFragment instance of the activity which the chrono will run
     */
    fun initChronometer(chronoFragment: TreeFragment){
        chronometer = Chronometer(chronoFragment)
    }

    /**
     * Start the chronometer
     */
    fun startChronometer(){
        chronometer.initChrono()
        chronometer.startChrono()
    }

    companion object{
        private lateinit var notification: NotificationCompat.Builder
        private lateinit var notificationManager: NotificationManager
        private var treeList: HashMap<Date, ArrayList<TreeImage>> = HashMap()
        private var isRunning:Boolean = true

        private lateinit var context: Context
        var loadingTreeImageRegex: String = "^wonder_tree{1}.{0,}[.png]{1}"

        private var initialized: Boolean=false

        private lateinit var singleton: Singleton

        private lateinit var chronometer: Chronometer

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
        }
    }

    fun loadImages(){
        importImageList()
    }

    var Chronometer:Chronometer
        get() = chronometer
        set(value){
            chronometer = value
        }
}
package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Environment
import android.support.v4.app.NotificationCompat
import com.lpiem.ptut_limit_ecran.limitecran.R
import com.lpiem.ptut_limit_ecran.limitecran.TreeFragment
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Singleton(context: Context) {
    init {
        initSingleton(context)
    }

    var TreeList:HashMap<Date, TreeImage>
    get() = treeList
    set(newValue){
        treeList = newValue
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
        private lateinit var treeList: HashMap<Date, TreeImage>

        private lateinit var context: Context
        private lateinit var resources: Resources
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
            val fileNameRegex = Regex(loadingTreeImageRegex)
            val directory = File(Environment.getExternalStorageDirectory().absolutePath)
            val list = directory.listFiles()
            for(i in 0..list.size){
                if(fileNameRegex.matches(list[0].name)){
                    treeList[Date(list[i].lastModified())] = TreeImage(list[i].name, Date(list[i].lastModified()))
                }
            }
        }

        fun initSingleton(context: Context){
            this.context = context
            this.resources = context.resources
            this.createNotification()
            this.createNotificationChannel()
            this.importImageList()
        }

        fun createNotificationChannel() {
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = resources.getString(R.string.app_name)
                val descriptionText = resources.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(resources.getString(R.string.channelId), name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            this.notificationManager!!.notify(0, this.notification!!.build())
        }

        fun createNotification(){
            this.notification = NotificationCompat.Builder(this.context, resources.getString(R.string.channelId))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(resources.getString(R.string.app_name))
                .setOngoing(true)
                .setContentText(resources.getString(R.string.channel_description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }
    }

    var Chronometer:Chronometer
        get() = chronometer
        set(value){
            chronometer = value
        }

    fun updateNotification(updateTimeText: String){
        notification!!.setContentText("Temps écoulé : "+updateTimeText)
        notificationManager!!.notify(0, notification!!.build())
    }
}
package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import com.lpiem.ptut_limit_ecran.limitecran.MainActivity
import com.lpiem.ptut_limit_ecran.limitecran.R
import com.lpiem.ptut_limit_ecran.limitecran.TreeFragment

class Singleton(context: Context) {
    init {
        initSingleton(context)

    }

    companion object{
        private lateinit var notification: NotificationCompat.Builder
        private lateinit var notificationManager: NotificationManager
        private lateinit var context: Context
        private lateinit var resources: Resources
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

        fun initSingleton(context: Context){
            //this.chronometer = Chronometer()
            this.context = context
            this.resources = context.resources
            this.createNotification()
            this.createNotificationChannel()
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
                .setContentText(resources.getString(R.string.channel_description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }
    }
}
package com.lpiem.ptut_limit_ecran.limitecran

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton


class MainActivity : AppCompatActivity() {
    private lateinit var notification:NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var singleton: Singleton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.createNotification()
        this.createNotificationChannel()
        this.singleton = Singleton.getInstance(this)


        val handler = Handler()
        handler.postDelayed(
            {
            val intent = Intent(applicationContext, MainContainer::class.java)
            startActivity(intent)
            }
            , 2000L)


    }

    private fun createNotificationChannel() {
        this.notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.channelId), name, importance).apply {
                description = descriptionText
                enableVibration(false)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        this.notificationManager!!.notify(0, this.notification!!.build())
    }

    private fun createNotification(){
        this.notification = NotificationCompat.Builder(this.applicationContext, getString(R.string.channelId))
        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.channel_description))
            .setVibrate(longArrayOf(0L))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    /**
     * Update time in the notification
     */
    fun updateNotification(updateTimeText : String){
        this.notification.setContentText(updateTimeText)
        this.notificationManager.notify(0, this.notification.build())
    }

}

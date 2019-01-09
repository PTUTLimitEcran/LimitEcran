package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
   // private val notificationManager : NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var chronoFragment:ChronometerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chronoFragment = chronometerFragment as ChronometerFragment
        chronoFragment.initChrono(this)
        val handler = Handler()
        handler.postDelayed(
            {
            val intent = Intent(applicationContext, MainContainer::class.java)
            startActivity(intent)
            }
            , 2000L)

    }

    private fun initComponents(){
        chronoFragment.MainActivity = this
    }

    private fun createNotification() : NotificationCompat.Builder{
        val notification = NotificationCompat.Builder(this.applicationContext, getString(R.string.channelId))
        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.channel_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return notification
    }

    /**
     * Update time in the notification
     */
    /*fun updateNotification(updateTimeText : String){
        this.notifications.setContentText(updateTimeText)
        this.notificationManager.notify(0, this.notifications.build())
    }*/
}

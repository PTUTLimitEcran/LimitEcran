package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class BackgroundService() : Service() {
    override fun onBind(p0: Intent?): IBinder {
        return mBinder
    }

    private val mBinder = LocalBinder()

    private lateinit var screenOnOffReceiver:BroadcastReceiver
    //private val singleton: Singleton = Singleton.getInstance(mainActivity)

    /**
     * Create an instance of the notification channel
     */
    private fun createNotificationChannel() {
        //singleton.initNotificationChannel(mainActivity, mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
    }

    inner class LocalBinder: Binder(){
        fun getService(): BackgroundService = this@BackgroundService
    }

}
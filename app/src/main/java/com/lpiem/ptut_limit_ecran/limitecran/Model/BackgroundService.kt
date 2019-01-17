package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.lpiem.ptut_limit_ecran.limitecran.MainActivityContainer
import com.lpiem.ptut_limit_ecran.limitecran.R

class BackgroundService : Service() {
    private val binder = LocalBinder()
    private val singleton:Singleton = Singleton.getInstance(this,null)
    private lateinit var screenOnOffReceiver:BroadcastReceiver

    /**
     * Function about managing activity before screen lock
     */
    fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter()
        /** System Defined Broadcast */
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter.addAction(Intent.ACTION_USER_UNLOCKED)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)

        screenOnOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                Log.d("Intent", intent?.action.toString())

                val keyguardManager = context?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (action == Intent.ACTION_USER_PRESENT ||
                    action == Intent.ACTION_USER_UNLOCKED ||
                    action == Intent.ACTION_SCREEN_OFF ||
                    action == Intent.ACTION_SCREEN_ON ||
                    action == Context.FINGERPRINT_SERVICE
                )
                    if (action == Intent.ACTION_SCREEN_ON) {
                        Log.d("Screen","Screen turned on")
                        if(keyguardManager.isKeyguardLocked){
                            Log.d("Screen", "Screen locked")
                            singleton.IsDeviceOn = true
                            startOrResumeCountDownTimer()
                        }
                    }
                if (action == Intent.ACTION_USER_PRESENT) {
                    Log.d("Screen", "Screen unlocked")
                    if (singleton.IsRunning) {
                        singleton.IsRunning = false
                        singleton.pauseCountDownTimer()
                        singleton.TreeFragment.updateTextView(singleton.formatTime(singleton.CurrentCountDownTimer))
                    }
                }
                if (action == Intent.ACTION_SCREEN_OFF) {
                    Log.d("Screen", "Screen locked")
                    Log.d("Screen", "Phone screen turned off")
                    singleton.IsDeviceOn = false
                    startOrResumeCountDownTimer()
                }
                val openMainActivity= Intent(context, MainActivityContainer::class.java)
                openMainActivity.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(openMainActivity)
            }
        }
        registerReceiver(screenOnOffReceiver, intentFilter)
    }

    private fun initService(){
        createNotificationChannel()
    }

    /**
     * Start or resume countDownTimer
     */
    private fun startOrResumeCountDownTimer() {
        if (!singleton.IsRunning) {
            singleton.IsRunning = true
            if (singleton.CurrentCountDownTimer == 0L) {
                singleton.startCountDownTimer()
            } else {
                singleton.resumeCountDownTimer()
            }
        }
    }

    /**
     * Create an instance of the notification channel
     */
    private fun createNotificationChannel() {
        singleton.initNotificationChannel(this, getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
    }

    /**
     * Create the notification
     */
    private fun createNotification() {
        singleton.initNotification(
            this,
            getString(R.string.app_name),
            getString(R.string.channelId),
            getString(R.string.channel_description)
        )
    }

    fun startChallenge(){
        createNotification()
        initCountDownTimer()
    }

    private fun initCountDownTimer() {
        if (!singleton.FirstTime) {
            singleton.initCountDownTimer(singleton.CurrentCountDownTimer)
            singleton.FirstTime = true
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        try{
            singleton.destroyNotification()
            singleton.stopCountDownTimer()
            unregisterReceiver(screenOnOffReceiver)
            Log.d("Service", "Service destroyed")
        }catch (e: Throwable){
            Log.e("Error",e.localizedMessage)
        }
        return super.onUnbind(intent)
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): BackgroundService = this@BackgroundService
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }
}
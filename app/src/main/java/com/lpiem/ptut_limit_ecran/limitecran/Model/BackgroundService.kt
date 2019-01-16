package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.app.IntentService
import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.lpiem.ptut_limit_ecran.limitecran.MainActivityContainer
import com.lpiem.ptut_limit_ecran.limitecran.R
import com.lpiem.ptut_limit_ecran.limitecran.TreeFragment

class BackgroundService(mainActivityContainer: MainActivityContainer, treeFragment: TreeFragment) : IntentService(BackgroundService::class.simpleName) {
    private val screenOnOffReceiver:BroadcastReceiver = initBroadcastReceiver()
    private val mainActivity:MainActivityContainer = mainActivityContainer
    private val treeFragment:TreeFragment = treeFragment
    private val singleton: Singleton = Singleton.getInstance(this)
    override fun onHandleIntent(intent: Intent?) {
        Log.i("Information","Service started")
        createNotification()
        createNotificationChannel()
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

    private fun initBroadcastReceiver():BroadcastReceiver{
        val intentFilter = IntentFilter()
        /** System Defined Broadcast */
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter.addAction(Intent.ACTION_USER_UNLOCKED)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action

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
                        treeFragment.updateTextView(singleton.formatTime(singleton.CurrentCountDownTimer))
                    }
                }
                if (action == Intent.ACTION_SCREEN_OFF) {
                    Log.d("Screen", "Screen locked")
                    Log.d("Screen", "Phone screen turned off")
                    singleton.IsDeviceOn = false
                    startOrResumeCountDownTimer()
                }
                val openMainActivity = Intent(context, MainActivityContainer::class.java)
                openMainActivity.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                mainActivity.startActivityIfNeeded(openMainActivity, 0)
            }
        }
        registerReceiver(screenOnOffReceiver, intentFilter)
        return broadcastReceiver
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
                singleton.resumeCountDownTimer(treeFragment)
            }
        }
    }

}
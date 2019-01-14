package com.lpiem.ptut_limit_ecran.limitecran

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler()
        handler.postDelayed(
            {
            val intent = Intent(applicationContext, MainActivityContainer::class.java)
            startActivity(intent)
            }
            , 2000L)


    }
}

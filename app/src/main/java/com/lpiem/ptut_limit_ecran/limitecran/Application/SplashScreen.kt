package com.lpiem.ptut_limit_ecran.limitecran.Application

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.lpiem.ptut_limit_ecran.limitecran.Manager.Manager
import com.lpiem.ptut_limit_ecran.limitecran.R


class SplashScreen : AppCompatActivity() {

    private lateinit var manager: Manager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        manager = Manager.getInstance(this)

        val intent = Intent(applicationContext, MainActivityContainer::class.java)

        val handler =  Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, 2000L)


    }
}

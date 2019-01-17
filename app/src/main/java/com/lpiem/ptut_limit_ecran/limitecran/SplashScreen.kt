package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton


class SplashScreen : AppCompatActivity() {

    private lateinit var singleton: Singleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        singleton = Singleton.getInstance(this)

        val intent = Intent(applicationContext, MainActivityContainer::class.java)

        val handler =  Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, 2000L)


    }
}

package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(applicationContext, MainActivityContainer::class.java)
        startActivity(intent)
        finish()

    }
}

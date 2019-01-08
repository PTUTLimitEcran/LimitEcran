package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler()
        handler.postDelayed(
            {
            val intent = Intent(applicationContext, MainContainer::class.java)
            startActivity(intent)
            }
            , 2000L)

    }


}

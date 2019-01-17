package com.lpiem.ptut_limit_ecran.limitecran

import android.app.Application
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton

class App : Application() {

    private lateinit var singleton: Singleton

    override fun onCreate() {
        super.onCreate()
        singleton = Singleton(this)
    }
}
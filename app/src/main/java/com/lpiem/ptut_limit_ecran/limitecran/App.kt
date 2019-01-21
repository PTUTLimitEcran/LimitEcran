package com.lpiem.ptut_limit_ecran.limitecran

import android.app.Application
import com.lpiem.ptut_limit_ecran.limitecran.Manager.Manager

class App : Application() {

    private lateinit var manager: Manager

    override fun onCreate() {
        super.onCreate()
        manager = Manager(this)
    }
}
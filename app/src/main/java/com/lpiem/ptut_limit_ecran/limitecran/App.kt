package com.lpiem.ptut_limit_ecran.limitecran

import android.app.Application
import android.os.SystemClock

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SystemClock.sleep(2000)
    }
}
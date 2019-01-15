package com.lpiem.ptut_limit_ecran.limitecran

interface TimeManagmentInterface {
    fun updateTextView(formattedTime: String)

    fun updateNotification(formattedTime: String)
}
package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.util.Log
import java.sql.Timestamp
import java.util.*



class TreeImage(filePath:String, fileDate: Date){
    private val filePath = filePath
    private val fileDate = fileDate

    var FilePath:String = ""
    get() = filePath

    var FileDate:Date = Date()
    get() = fileDate

    fun formatDate():String{
        val stamp = Timestamp(System.currentTimeMillis())
        val date = Date(stamp.getTime())
        Log.d("Calendrier",date.year.toString())
        Log.d("Calendrier",date.month.toString())
        Log.d("Calendrier",date.year.toString())
        Log.d("Calendrier",fileDate.month.toString())
        Log.d("Calendrier",fileDate.day.toString())
        return fileDate.day.toString()+"/"+
               fileDate.month.toString()+"/"+
               fileDate.year.toString()
    }
}

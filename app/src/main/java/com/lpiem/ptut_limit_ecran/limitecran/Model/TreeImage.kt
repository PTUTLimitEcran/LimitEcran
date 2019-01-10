package com.lpiem.ptut_limit_ecran.limitecran.Model

import java.util.*

class TreeImage(filePath:String, fileDate: Date){
    private val filePath = filePath
    private val fileDate = fileDate

    var FilePath:String = ""
    get() = filePath

    var FileDate:Date = Date()
    get() = fileDate

    fun formatDate():String{
        return fileDate.day.toString()+"/"+
               fileDate.month.toString()+"/"+
               fileDate.year.toString()
    }
}

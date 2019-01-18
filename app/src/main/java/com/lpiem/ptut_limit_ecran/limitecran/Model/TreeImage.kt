package com.lpiem.ptut_limit_ecran.limitecran.Model

import java.io.File
import java.time.format.DateTimeFormatter
import java.util.*



class TreeImage(filePath:String, fileDate: Date){
    private val filePath = filePath
    private val fileDate = fileDate

    var FilePath:String = ""
    get() = filePath

    var FileDate:Date = Date()
    get() = fileDate

    fun formatDate():String{

        /*val file:File = File(filePath)
        file.lastModified()
        val calendar = Calendar.getInstance()
        calendar.time = Date(file.lastModified())

        val builder = StringBuilder()
        builder.append(calendar.firstDayOfWeek)
            .append("-")
            .append(dateFile.substring(4..5))
            .append("-")
            .append(dateFile.substring(6..7))

        val formattedDate = builder.toString()*/

        return ""
    }
}

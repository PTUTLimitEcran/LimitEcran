package com.lpiem.ptut_limit_ecran.limitecran.Gallery

import java.util.*


class TreeImage(filePath: String, fileDate: Date) {
    private val filePath = filePath
    private val fileDate = fileDate

    var FilePath: String = ""
        get() = filePath

    var FileDate: Date = Date()
        get() = fileDate

    fun formatDate(): String {

        val dateFile = filePath.subSequence(11..filePath.length - 7)
        val builder = StringBuilder()
        builder.append(dateFile.substring(4..5))
            .append(" - ")
            .append(dateFile.substring(6..7))
            .append(" - ")
            .append(dateFile.substring(0..3))


        val formattedDate = builder.toString()

        return formattedDate
    }
}
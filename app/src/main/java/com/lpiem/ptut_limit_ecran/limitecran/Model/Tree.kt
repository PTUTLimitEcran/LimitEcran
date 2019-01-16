package com.lpiem.ptut_limit_ecran.limitecran.Model

import java.util.Date

class Tree(creationDate:Date) {
    private var size:Int = 0
    private var treeCreationDate: Date = creationDate

    var TreeDate: Date
    get() = this.treeCreationDate
    set(value){
        this.treeCreationDate = value
    }

    var TreeSize: Int
    get() = this.size
    set(value){
        this.size = value
    }

    fun formatDate():String{
        return treeCreationDate.day.toString() +"/"+
               treeCreationDate.day.toString() +"/"+
               treeCreationDate.day.toString()
    }
}

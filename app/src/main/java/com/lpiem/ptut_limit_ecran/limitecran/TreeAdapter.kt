package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tree_day_ressource_layout.view.*
import android.util.Log
import java.util.*


class TreeAdapter(val treeCollection : ArrayList<Sketch>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    private val FROM_LOW_TO_HIGH = 0
    private val FROM_HIGHT_TO_LOW = 1
    private val SORT_BY_DATE  = 0
    private val SORT_BY_SIZE  = 1
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.tree_day_ressource_layout, p0, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        holder?.treeCollection?.text = treeCollection.get(index).formatDate()
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return treeCollection.size
    }

    fun filterTree(mode: Int, filterType: Int):Int{
        Collections.sort(treeCollection, object : Comparator<Sketch> {
            override fun compare(lhs: Sketch, rhs: Sketch): Int {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                when (mode) {
                    FROM_HIGHT_TO_LOW ->
                        return filterSort(filterType, lhs, rhs)
                    FROM_LOW_TO_HIGH ->
                        return filterSort(filterType, lhs, rhs)
                    else-> return sortingError()
                }
            }
        })
        return -2
    }

    private fun filterSort(
        filterType: Int,
        lhs: Sketch,
        rhs: Sketch
    ): Int {
        when (filterType) {
            SORT_BY_DATE -> return sortDates(lhs.TreeDate, rhs.TreeDate)
            SORT_BY_SIZE -> return sortBySize(0, 0)
            else -> return sortingError()
        }
    }

    fun sortDates(d1:Date, d2:Date):Int{
        return if(d1.before(d2)) -1 else if (d2.before(d1)) 1 else 0
    }

    fun sortBySize(s1: Int, s2:Int):Int{
        return if(s1<s2)-1 else if (s2<s1) 1 else 0
    }

    private fun sortingError():Int {
        Log.e("Sorting", "No sorting possible")
        return -2
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val treeCollection = view.treeList
    val treeListRecyclerView = view.dailyTreeListRecyclerView
}
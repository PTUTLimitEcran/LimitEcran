package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tree_list_ressource_layout.view.*
import android.util.Log
import com.lpiem.ptut_limit_ecran.limitecran.Model.TreeImage
import java.util.*


class TreeAdapter(val treeCollection : List<List<TreeImage>>, val context: Context) : RecyclerView.Adapter<TreeAdapter.ViewHolder>() {
    private val FROM_LOW_TO_HIGH = 0
    private val FROM_HIGHT_TO_LOW = 1
    private val SORT_BY_DATE  = 0
    private val SORT_BY_SIZE  = 1
    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.tree_list_ressource_layout, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        holder?.treeDateTextView?.text = treeCollection[index][0].formatDate()
        holder?.treeCollectionsByDate.adapter = SingleDayTreeListAdapter(treeCollection[index],context)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return treeCollection.size
    }

    private fun filterSort(
        filterType: Int,
        lhs: TreeImage,
        rhs: TreeImage
    ): Int {
        when (filterType) {
            SORT_BY_DATE -> return sortDates(lhs.FileDate, rhs.FileDate)
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
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val treeDateTextView = view.treeListDate
        val treeCollectionsByDate = view.dailyTreeListRecyclerView
    }

}

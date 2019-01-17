package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Context
import android.graphics.Point
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import com.lpiem.ptut_limit_ecran.limitecran.Model.TreeImage
import kotlinx.android.synthetic.main.tree_list_ressource_layout.view.*
import java.util.*
import kotlin.collections.ArrayList


class TreeAdapter(var treeCollection : List<TreeImage>, val context: Context) : RecyclerView.Adapter<TreeAdapter.ViewHolder>() {
    private val FROM_LOW_TO_HIGH = 0
    private val FROM_HIGHT_TO_LOW = 1
    private val SORT_BY_DATE  = 0
    private val SORT_BY_SIZE  = 1
    private val singleton : Singleton = Singleton.getInstance(context)
    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.tree_list_ressource_layout, viewGroup, false))
    }



    override fun onBindViewHolder(holder: ViewHolder, index: Int) {

        if (holder.treeCollectionsByDate.adapter != null) {
            holder.treeCollectionsByDate.adapter.notifyDataSetChanged()
        } else {
            holder.treeCollectionsByDate?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            holder.treeCollectionsByDate?.itemAnimator  = DefaultItemAnimator()
            holder.adapter = SingleDayTreeListAdapter(treeCollection,context)
            holder.treeCollectionsByDate.adapter = holder.adapter
        }


    }

    fun filterListBySize():List<List<TreeImage>>{
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val treeOutput = ArrayList<ArrayList<TreeImage>>()
        var currentIndex = 0
        val width = size.x
        val height = size.y
        for(i in 0 until treeCollection.size){
            treeOutput.add(ArrayList())
            for(j in 0 until treeCollection.size){
                treeOutput[currentIndex].add(treeCollection[i])
            }
            currentIndex++
        }
        return treeOutput
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
        return when (filterType) {
            SORT_BY_DATE -> sortDates(lhs.FileDate, rhs.FileDate)
            SORT_BY_SIZE -> sortBySize(0, 0)
            else -> sortingError()
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
        val treeCollectionsByDate = view.treeListRecyclerView
        lateinit var adapter: SingleDayTreeListAdapter
    }

}

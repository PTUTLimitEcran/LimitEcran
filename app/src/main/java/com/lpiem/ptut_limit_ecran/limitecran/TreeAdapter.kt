package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lpiem.ptut_limit_ecran.limitecran.Model.Tree
import kotlinx.android.synthetic.main.tree_day_ressource_layout.view.*
import android.R.attr.data
import android.R.attr.data
import java.util.*


class TreeAdapter(val treeCollection : ArrayList<Tree>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
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

    fun filterTreeListByCreationDate(mode: Int, filterType: Int){
        Collections.sort(treeCollection, object : Comparator<Tree> {
            override fun compare(lhs: Tree, rhs: Tree): Int {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                when (mode) {
                    0 ->
                        when(filterType){

                        }
                    1 -> println("Number too low")
                    else -> println("Number too high")
                }
                return if (lhs.TreeDate.before(rhs.TreeDate)) -1 else if (rhs.TreeDate.before(lhs.TreeDate)) 1 else 0
            }
        })
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val treeCollection = view.treeList
    val treeListRecyclerView = view.treeListRecyclerView
}
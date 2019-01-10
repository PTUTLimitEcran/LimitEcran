package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.lpiem.ptut_limit_ecran.limitecran.Model.TreeImage
import kotlinx.android.synthetic.main.single_day_tree_ressource_list.view.*

class SingleDayTreeListAdapter(val treeCollection : List<TreeImage>, val context: Context) : RecyclerView.Adapter<SingleDayTreeListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_day_tree_ressource_list, viewGroup, false))
    }

    override fun getItemCount(): Int {
        return treeCollection.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        Glide.with(context)
            .load(treeCollection[index].FilePath)
            .into(viewHolder.treeImage)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val treeImage = view.imageTree
    }
}

package com.lpiem.ptut_limit_ecran.limitecran.Model

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lpiem.ptut_limit_ecran.limitecran.R
import com.lpiem.ptut_limit_ecran.limitecran.SingleDayTreeListAdapter
import com.lpiem.ptut_limit_ecran.limitecran.TreeAdapter
import kotlinx.android.synthetic.main.multilines_tree_ressource_layout.view.*

class MultiLinesTreeGalleryAdapter(var treeCollection : List<List<TreeImage>>, val context: Context) : RecyclerView.Adapter<TreeAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TreeAdapter.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return this.treeCollection.size
    }

    override fun onBindViewHolder(viewHolder: TreeAdapter.ViewHolder, index: Int) {
        viewHolder?.treeCollectionsByDate.layoutManager = LinearLayoutManager(context)
        viewHolder?.treeCollectionsByDate.itemAnimator  = DefaultItemAnimator()
        viewHolder?.treeCollectionsByDate.adapter = SingleDayTreeListAdapter(treeCollection[index],context)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val treeCollectionsByDate = view.multiLinesRecyclerView
    }

}
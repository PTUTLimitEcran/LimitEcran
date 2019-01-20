package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Context
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.lpiem.ptut_limit_ecran.limitecran.Model.TreeImage
import kotlinx.android.synthetic.main.single_day_tree_ressource_list.view.*


class TreeAdapter(var treeCollection : List<TreeImage>, val context: Context) : RecyclerView.Adapter<TreeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_day_tree_ressource_list, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {

        Glide.with(holder.image)
            .load(Environment.getExternalStorageDirectory().absolutePath+"/LimitEcran/"+treeCollection[index].FilePath)
            .into(holder.image)

        holder.date.text = treeCollection[index].formatDate()

    }

    override fun getItemCount(): Int {
        return treeCollection.size
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val image = view.imageTree
        val date = view.date
    }

}

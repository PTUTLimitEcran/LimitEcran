package com.lpiem.ptut_limit_ecran.limitecran

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import com.lpiem.ptut_limit_ecran.limitecran.Model.TreeImage
import kotlinx.android.synthetic.main.fragment_gallery.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GalleryFragment : Fragment() {

    private lateinit var singleton: Singleton
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var treeAdapter:TreeAdapter
    private var initOnce = true

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singleton = Singleton.getInstance(activity!!.applicationContext)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        singleton.loadImages()

        treeAdapter = TreeAdapter(convertHashmapToArrayList(),context!!)
        treeAdapter.treeCollection = convertHashmapToArrayList()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        treeAdapter.treeCollection = convertHashmapToArrayList()
        if (initOnce) {
            treeListRecyclerView.layoutManager = GridLayoutManager(context, 3)
            treeListRecyclerView.itemAnimator = DefaultItemAnimator()
            treeListRecyclerView.adapter = treeAdapter
            initOnce = false
        }

        treeAdapter.notifyDataSetChanged()

    }

    /**
     * Import the Hashmap from the [Singleton] class and transform it into a [Hashmap] object
     */
    fun convertHashmapToArrayList():ArrayList<TreeImage>{
        val treeList = ArrayList<TreeImage>()

        val dateTreeIterator = this.singleton.TreeList

        var i = 0
        for(dateTree in dateTreeIterator){
            treeList.add(dateTree.value[i])
        }
        return treeList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        treeAdapter = TreeAdapter(convertHashmapToArrayList(),context!!)

        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onPause() {
        super.onPause()
        initOnce = true
    }

}

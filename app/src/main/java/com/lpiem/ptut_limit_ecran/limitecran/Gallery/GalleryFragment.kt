package com.lpiem.ptut_limit_ecran.limitecran.Gallery

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lpiem.ptut_limit_ecran.limitecran.Manager.Manager
import com.lpiem.ptut_limit_ecran.limitecran.R
import kotlinx.android.synthetic.main.fragment_gallery.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GalleryFragment : Fragment(){

    private lateinit var manager: Manager
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var treeAdapter: TreeAdapter
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
        manager = Manager.getInstance(activity!!.applicationContext)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        manager.loadImages()

        treeAdapter = TreeAdapter(getTreeList(), context!!)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        treeAdapter.treeCollection = getTreeList()
        if (initOnce) {
            treeListRecyclerView.layoutManager = GridLayoutManager(context, 3)
            treeListRecyclerView.itemAnimator = DefaultItemAnimator()
            treeListRecyclerView.adapter = treeAdapter
            treeListRecyclerView.addOnItemTouchListener(
                RecyclerTouchListener(
                    activity!!.applicationContext,
                    treeListRecyclerView,
                    object : RecyclerTouchListener.ClickListener {
                        override fun onClick(view: View, position: Int) {
                            val filePath = treeAdapter.treeCollection[position].FilePath
                            val detailIntent =
                                Intent(context, DetailAndShareTree::class.java)
                            detailIntent.putExtra("filePath", filePath)
                            startActivity(detailIntent)

                        }

                        override fun onLongClick(view: View?, position: Int) {
                            Toast.makeText(
                                activity!!.applicationContext,
                                "Arbre gagné le ${treeAdapter.treeCollection[position].formatDate()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            )
            initOnce = false
        }
        treeAdapter.notifyDataSetChanged()
    }

    /**
     * Import the Hashmap from the [Manager] class and transform it into a [Hashmap] object
     */
    fun getTreeList():ArrayList<TreeImage>{
        val treeList = ArrayList<TreeImage>()

        val dateTreeIterator = this.manager.TreeList

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
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onPause() {
        super.onPause()
        initOnce = true
    }




}

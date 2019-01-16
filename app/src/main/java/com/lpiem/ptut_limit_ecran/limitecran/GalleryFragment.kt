package com.lpiem.ptut_limit_ecran.limitecran

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import com.lpiem.ptut_limit_ecran.limitecran.Model.TreeImage
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlin.collections.ArrayList
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.DefaultItemAnimator






// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GalleryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GalleryFragment : Fragment() {
    private lateinit var singleton: Singleton

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GalleryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singleton = Singleton.getInstance(activity!!.applicationContext)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        /*treeAdapter = TreeAdapter(convertHashmapToArrayList(),context!!)
        treeAdapter.treeCollection = convertHashmapToArrayList()*/
    }

    private lateinit var treeAdapter:TreeAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //treeAdapter.treeCollection = convertHashmapToArrayList()
        //treeAdapter.notifyDataSetChanged()
        treeListRecyclerView.setLayoutManager(LinearLayoutManager(context!!))
        treeListRecyclerView.setItemAnimator(DefaultItemAnimator())
        //treeListRecyclerView.adapter = TreeAdapter(convertHashmapToArrayList(),activity!!.applicationContext)
    }

    /**
     * Import the Hashmap from the [Singleton] class and transform it into a [Hashmap] object
     */
    fun convertHashmapToArrayList():ArrayList<ArrayList<TreeImage>>{
        val treeList = ArrayList<ArrayList<TreeImage>>()

        val dateTreeIterator = this.singleton.TreeList.iterator()

        while(dateTreeIterator.hasNext()){
            val imageTreeIterator = dateTreeIterator.next().value
            treeList.add(imageTreeIterator)
        }
        return treeList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //treeAdapter = TreeAdapter(convertHashmapToArrayList(),context!!)

        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

}

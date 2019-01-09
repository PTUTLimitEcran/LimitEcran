package com.lpiem.ptut_limit_ecran.limitecran

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lpiem.ptut_limit_ecran.limitecran.Model.Chronometer
import kotlinx.android.synthetic.main.fragment_chronometer.*
import kotlinx.android.synthetic.main.fragment_chronometer.view.*


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

class ChronometerFragment() : Fragment() {
    private lateinit var chronometer: Chronometer
    private lateinit var mainActivity:MainActivity
    private lateinit var viewOfLayout:View

    var Chronometer : Chronometer
        get() = this.chronometer
        set(newValue) {
            this.chronometer = newValue
        }
    var MainActivity : MainActivity
        get() = this.mainActivity
        set(newValue) {
            this.mainActivity = newValue
        }

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

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun initChrono(mainActivity: MainActivity){
        this.mainActivity = mainActivity
        chronometer = Chronometer(this,mainActivity)
        chronometer.startChrono()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater!!.inflate(R.layout.fragment_chronometer, container, false)
        println(viewOfLayout.currentChronometer.text)
        return viewOfLayout
    }

    fun updateTextView(updateTimeText : String){
        this.viewOfLayout.currentChronometer.text = updateTimeText
    }
}
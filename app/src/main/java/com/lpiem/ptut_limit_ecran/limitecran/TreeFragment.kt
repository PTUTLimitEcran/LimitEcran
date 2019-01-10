package com.lpiem.ptut_limit_ecran.limitecran

import android.app.NotificationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lpiem.ptut_limit_ecran.limitecran.Model.Chronometer
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import kotlinx.android.synthetic.main.fragment_tree.*
import processing.android.PFragment
import java.io.Serializable


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TreeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TreeFragment() : PFragment() {
    private lateinit var chronometer: Chronometer

    private lateinit var viewOfLayout:View
    private lateinit var singleton: Singleton

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param sketch Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TreeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(sketch: Sketch, param2: String?) =
            TreeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, sketch)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // TODO: Rename and change types of parameters
    private var param1: Serializable? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Sketch
            param2 = it.getString(ARG_PARAM2)
        }
        this.singleton = Singleton(activity!!.applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_tree, container, false)
        this.singleton.initChronometer(this)
        if(this.singleton.Chronometer.ChronometerStartStatus==false){
            this.singleton.startChronometer()
        }
        return inflater.inflate(R.layout.fragment_tree, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sketch = Sketch()

        val frame = FrameLayout(context)
        frame.id = R.id.sketch_frame
        val pFragment = PFragment(sketch)
        pFragment.setView(frame, activity)
        //frame.id = R.id.sketch_frame//CompatUtils.getUniqueViewId()

        //val frame: FrameLayout? = activity?.findViewById(R.id.sketch_frame)

//        activity?.setContentView(
//            sketch_frame, ViewGroup.LayoutParams(
//                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_WRAP,
//                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_WRAP
////                ViewGroup.LayoutParams.WRAP_CONTENT,
////                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        )

//        val fragment = PFragment(sketch)
//        fragment.setView(sketch_frame, activity)

        initChrono()
    }

    /**
     * Update chronometer
     */
    fun updateTextView(updateTimeText : String){
        print(currentChronometerTime)
        currentChronometerTime.text = updateTimeText
    }

    fun updateNotification(updateTimeText: String){
        this.singleton.updateNotification(updateTimeText)
    }
}

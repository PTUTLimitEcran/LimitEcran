package com.lpiem.ptut_limit_ecran.limitecran

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lpiem.ptut_limit_ecran.limitecran.Model.Chronometer
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import kotlinx.android.synthetic.main.fragment_tree.*
import processing.android.PFragment
import java.io.Serializable




// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TreeFragment : PFragment() {


    private lateinit var chronometer: Chronometer

    private lateinit var viewOfLayout: View
    private lateinit var singleton: Singleton
    private lateinit var saveImage: SaveImage
    //private var gram = "S[L[L[R]]R[L[L[LR]R[C]]R[L]]"
    //private var gram = "S[L[C[L[C[L[L[LC]CR[R]]R]]]]R[C[L[L[C[LC[R[LR]]R]]]]R[C[L[C[LCR]]C[C[C[LR]]]R]]]"
    //private var gram = "S[L[L[L[L[C[CR]R]]C[LC[LC]R]]R[C[C[LCR]R]]]R[CR[C[CR]R]]"
    //private var gram = "S[L[LC[LCR[C[LCR]R[R]]]]C[R[C]]R[R[C[CR]]]]"
    private var gram = "S[L[L[L[L[C[C[CR]R]]]C[LC[LC]R]]R[C[C[L[LC]C[C]R[CR]]R]]]R[CR[C[C[R]R[R]]R]]"
    //private var gram = "S[L[L[C[LC[LC]R]]R[C[C[LCR]R]]]R[CR[C[CR]R]]"
    private var bool = true
    private var countTurn = 0

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

    /**
     * Initialize the chronometer
     */
    private fun initChrono() {
        chronometer = Chronometer(this)
        chronometer.initChrono()
        chronometer.startChrono()
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
        //orderToSaveImage = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater.inflate(R.layout.fragment_tree, container, false)
        this.singleton.initChronometer(this)
        if (!this.singleton.Chronometer.ChronometerStartStatus) {
            this.singleton.startChronometer()
        }

        return inflater.inflate(R.layout.fragment_tree, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_save.setOnClickListener { saveImage.savePictureToStorage(true) }


        chronometerXml.start()
        chronometerXml.addTextChangedListener(object  : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "00:10" && bool) {
                    drawTree(gram, true)
                    bool = false
                }
                if (s.toString() == "00:10") {
                    countTurn++
                    Log.d("TAG_TEST", "number of turns: $countTurn")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
//        currentChronometerTime.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if (s.toString() == "00 : 00 : 10" && bool) {
//                    drawTree(gram, true)
//                    bool = false
//                } else if (s.toString() == "00 : 00 : 10") {
//                    countTurn++
//                    Log.d("TEST______", "number of turns: $countTurn")
//                }
//            }
//        })


        //initChrono()
    }

    /**
     * Update chronometer
     */
    fun updateTextView(updateTimeText: String) {
        print(currentChronometerTime)
        currentChronometerTime.text = updateTimeText
    }

    fun updateNotification(updateTimeText: String) {
        this.singleton.updateNotification(updateTimeText)
    }

//    override fun saveIt(save: SaveImage) {
//        saveImage = save
//    }

    override fun onResume() {
        super.onResume()
        drawTree(gram, false)
    }

    private fun drawTree(gram: String, savePicture: Boolean) {


        //TODO: change with percentage of challenge
        //val treeCurrentSize = (0 until gram.length).random()
        //gram.subSequence(0..treeCurrentSize).toString()
        val sketch = Sketch(gram, savePicture)

        val frame = FrameLayout(context)
        frame.id = R.id.sketch_frame
        val pFragment = PFragment(sketch)
        pFragment.setView(frame, activity)

    }


}

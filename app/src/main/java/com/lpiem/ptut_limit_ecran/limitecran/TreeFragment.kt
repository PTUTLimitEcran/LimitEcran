package com.lpiem.ptut_limit_ecran.limitecran

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import kotlinx.android.synthetic.main.fragment_tree.*
import processing.android.PFragment


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CHALLENGE_TIME = "challengeTime"


class TreeFragment : PFragment(), TimeManagmentInterface {


    private lateinit var viewOfLayout: View
    private lateinit var singleton: Singleton
    private var alreadySaved = false
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
        fun newInstance(param: Int) =
            TreeFragment().apply {
                arguments = Bundle().apply {
                    putInt(CHALLENGE_TIME, param)
                }
            }
    }

    // TODO: Rename and change types of parameters
    private var timerLength: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            timerLength = it.getInt(CHALLENGE_TIME)
        }
        singleton = Singleton.getInstance(activity?.applicationContext!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater.inflate(R.layout.fragment_tree, container, false)

        return inflater.inflate(R.layout.fragment_tree, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateTextView(singleton.formatTime(if (timerLength != null) timerLength?.toLong()!! else 0L))

    }

    override fun updateTextView(formattedTime: String) {
        currentChronometerTime.text = formattedTime
    }

    override fun onResume() {
        super.onResume()
        Log.d("CompteurFrag", "${singleton.CurrentCountDownTimer}")
        if ((singleton.CurrentCountDownTimer < 1000L && singleton.CurrentCountDownTimer != 0L) && !alreadySaved) {
            drawTree(gram, true)
            Log.d("CompteurFragDraw", "drawn")
            alreadySaved = true
        } else drawTree(gram, false)
    }

    private fun drawTree(gram: String, savePicture: Boolean) {

        var gramToDraw = ""
        if (singleton.IsRunning) {
            val timeLeft = timerLength!!.toDouble()
            val ellapsedTime = timeLeft - singleton.CurrentCountDownTimer
            val coef = ellapsedTime / timeLeft
            val timeToStop = coef * gram.length
             gramToDraw = gram.subSequence(0 until timeToStop.toInt()).toString()
        }


        val sketch = Sketch(gramToDraw, savePicture)

        val frame = FrameLayout(context)
        frame.id = R.id.sketch_frame
        val pFragment = PFragment(sketch)
        fragmentManager?.beginTransaction()?.replace(frame.id, pFragment)?.commit()
    }


}

package com.lpiem.ptut_limit_ecran.limitecran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lpiem.ptut_limit_ecran.limitecran.Model.ChallengeGrammarTree
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import kotlinx.android.synthetic.main.fragment_tree.*
import processing.android.PFragment


private const val CHALLENGE_TIME = "challengeTime"

class TreeFragment : PFragment(), TimeManagmentInterface {

    private lateinit var viewOfLayout: View
    private lateinit var singleton: Singleton
    private var alreadySaved = false
    private lateinit var gram: String
    private var timerLength: Long = 0L

    companion object {

        @JvmStatic
        fun newInstance(param: Long) =
            TreeFragment().apply {
                arguments = Bundle().apply {
                    putLong(CHALLENGE_TIME, param)
                }
            }
    }

    private fun randomGrammarTree(): String {
        return when (timerLength) {
            900000L -> ChallengeGrammarTree.QuarterHour.randomTree()
            900000L * 2 -> ChallengeGrammarTree.HalfHour.randomTree()
            900000L * 4 -> ChallengeGrammarTree.OneHour.randomTree()
            900000L * 8 -> ChallengeGrammarTree.TwoHours.randomTree()
            else -> "S[L[L[C[LC[LC]R]]R[C[C[LCR]R]]]R[CR[C[CR]R]]"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singleton = Singleton.getInstance(activity?.applicationContext!!)
        gram = randomGrammarTree()
        timerLength = singleton.ChallengeTime
        singleton.CurrentCountDownTimer = timerLength

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
        currentChronometerTime.text = singleton.formatTime(timerLength)
    }

    override fun onResume() {
        super.onResume()
        currentChronometerTime.text = singleton.formatTime(singleton.CurrentCountDownTimer)
        if ((singleton.CurrentCountDownTimer < 1500L && singleton.CurrentCountDownTimer != 0L) && !alreadySaved) {
            drawTree(gram, true)
            alreadySaved = true
        } else drawTree(gram, false)
    }

    override fun updateTextView(formattedTime: String) {
        currentChronometerTime.text = formattedTime
    }

    private fun drawTree(gram: String, savePicture: Boolean) {
        var gramToDraw = ""
        if (singleton.FirstTime && singleton.CurrentCountDownTimer != 0L) {
            val timeLeft = timerLength!!.toDouble()
            val ellapsedTime = timeLeft - singleton.CurrentCountDownTimer
            val coef = ellapsedTime / timeLeft
            val timeToStop = coef * gram.length
            gramToDraw = gram.subSequence(0 until timeToStop.toInt()).toString()
            if (singleton.CurrentCountDownTimer <= 1500L && singleton.CurrentCountDownTimer != 0L) {
                gramToDraw = gram
                singleton.CurrentCountDownTimer = 0L
                singleton.FirstTime = false

            }
        }

        if (singleton.CurrentCountDownTimer <= 1500L && !singleton.FirstTime) {
            gramToDraw = gram
        }

        val sketch = Sketch(gramToDraw, savePicture)

        val frame = FrameLayout(context)
        frame.id = R.id.sketch_frame
        val pFragment = PFragment(sketch)
        fragmentManager?.beginTransaction()?.replace(frame.id, pFragment)?.commit()
    }

}

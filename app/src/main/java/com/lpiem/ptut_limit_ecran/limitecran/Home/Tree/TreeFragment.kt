package com.lpiem.ptut_limit_ecran.limitecran.Home.Tree

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lpiem.ptut_limit_ecran.limitecran.Application.MainActivityContainer
import com.lpiem.ptut_limit_ecran.limitecran.Home.Challenge.ChallengeGrammarTree
import com.lpiem.ptut_limit_ecran.limitecran.Home.Sketch.Sketch
import com.lpiem.ptut_limit_ecran.limitecran.Manager.Manager
import com.lpiem.ptut_limit_ecran.limitecran.R
import kotlinx.android.synthetic.main.fragment_tree.*
import processing.android.PFragment


private const val CHALLENGE_TIME = "challengeTime"

class TreeFragment : PFragment(), TimeManagmentInterface {

    private lateinit var viewOfLayout: View
    private lateinit var manager: Manager
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
            10000L -> ChallengeGrammarTree.QuarterHour.randomTree()
            10000L*2 -> ChallengeGrammarTree.HalfHour.randomTree()
            10000L*4 -> ChallengeGrammarTree.OneHour.randomTree()
            10000L*8 -> ChallengeGrammarTree.TwoHours.randomTree()
            else -> "S[L[L[C[LC[LC]R]]R[C[C[LCR]R]]]R[CR[C[CR]R]]"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = Manager.getInstance(activity?.applicationContext!!)
        timerLength = manager.ChallengeTime
        gram = randomGrammarTree()
        manager.CurrentCountDownTimer = timerLength
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
        currentChronometerTime.text = manager.formatTime(timerLength)
    }

    override fun onResume() {
        super.onResume()
        currentChronometerTime.text = manager.formatTime(manager.CurrentCountDownTimer)
        if ((manager.CurrentCountDownTimer < 1500L && manager.CurrentCountDownTimer != 0L) && !alreadySaved) {
            drawTree(gram, true)
            alreadySaved = true
        } else drawTree(gram, false)
    }

    override fun updateTextView(formattedTime: String) {
        currentChronometerTime.text = formattedTime
    }

    private fun drawTree(gram: String, savePicture: Boolean) {
        var gramToDraw = ""
        if (manager.FirstTime && manager.CurrentCountDownTimer != 0L) {
            val timeLeft = timerLength.toDouble()
            val ellapsedTime = timeLeft - manager.CurrentCountDownTimer
            val coef = ellapsedTime / timeLeft
            val timeToStop = coef * gram.length
            gramToDraw = gram.subSequence(0 until timeToStop.toInt()).toString()
            if (manager.CurrentCountDownTimer <= 2000L && manager.CurrentCountDownTimer != 0L) {
                gramToDraw = gram
                manager.CurrentCountDownTimer = 0L
                manager.FirstTime = false

                val sketch = Sketch(gramToDraw, savePicture)

                val frame = FrameLayout(context)
                frame.id = R.id.sketch_frame
                val pFragment = PFragment(sketch)
                fragmentManager?.beginTransaction()?.replace(frame.id, pFragment)?.commit()

                challengeSucceed()
            }
        }

        if (manager.CurrentCountDownTimer <= 2000L && !manager.FirstTime) {
            gramToDraw = gram
        }

        val sketch = Sketch(gramToDraw, savePicture)

        val frame = FrameLayout(context)
        frame.id = R.id.sketch_frame
        val pFragment = PFragment(sketch)
        fragmentManager?.beginTransaction()?.replace(frame.id, pFragment)?.commit()


    }

    private fun challengeSucceed() {
        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(context!!)
        }
        builder?.setMessage(getString(R.string.Congrats))
            ?.setTitle(getString(R.string.ChallengeSucceed))
            ?.setPositiveButton("J'y vais !") { dialog, id ->
                run {
                    (activity as MainActivityContainer).onBackPressed()
                }
            }

        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

}

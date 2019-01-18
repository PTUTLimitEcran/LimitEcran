package com.lpiem.ptut_limit_ecran.limitecran

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import kotlinx.android.synthetic.main.fragment_challenge.*

private const val ARG_PARAM1 = "param1"

class ChallengeFragment() : Fragment(), View.OnClickListener{

    private lateinit var singleton: Singleton

    override fun onClick(v: View?) {
        val fifteenMins = 900000L
        val intent = Intent(requireContext(), MainActivityContainer::class.java )
        when(v) {
            challenge1 -> {
                singleton.ChallengeTime = 5000L
                singleton.ChallengeAccepted = true
                startActivity(intent)
            }
            challenge2 -> {
                singleton.ChallengeTime = fifteenMins*2
                singleton.ChallengeAccepted = true
                startActivity(intent)
            }
            challenge3 -> {
                singleton.ChallengeTime = fifteenMins*4
                singleton.ChallengeAccepted = true
                startActivity(intent)
            }
            challenge4 -> {
                singleton.ChallengeTime = fifteenMins*8
                singleton.ChallengeAccepted = true
                startActivity(intent)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ChallengeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)

                }
            }
    }

    private lateinit var param:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getString(ARG_PARAM1) as String
            param
        }
        singleton = Singleton.getInstance(requireContext())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        challenge1.setOnClickListener(this)
        challenge2.setOnClickListener(this)
        challenge3.setOnClickListener(this)
        challenge4.setOnClickListener(this)
    }

}
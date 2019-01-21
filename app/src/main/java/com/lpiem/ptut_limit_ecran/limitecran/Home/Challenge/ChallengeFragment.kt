package com.lpiem.ptut_limit_ecran.limitecran.Home.Challenge

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lpiem.ptut_limit_ecran.limitecran.Application.MainActivityContainer
import com.lpiem.ptut_limit_ecran.limitecran.Manager.Manager
import com.lpiem.ptut_limit_ecran.limitecran.R
import kotlinx.android.synthetic.main.fragment_challenge.*

private const val ARG_PARAM1 = "param1"

class ChallengeFragment : Fragment(), View.OnClickListener{

    private lateinit var manager: Manager

    override fun onClick(v: View?) {
        val fifteenMins = 10000L
        val intent = Intent(requireContext(), MainActivityContainer::class.java )
        when(v) {
            challenge1 -> {
                manager.ChallengeTime = fifteenMins
                manager.ChallengeAccepted = true
                startActivity(intent)
            }
            challenge2 -> {
                manager.ChallengeTime = fifteenMins*2
                manager.ChallengeAccepted = true
                startActivity(intent)
            }
            challenge3 -> {
                manager.ChallengeTime = fifteenMins*4
                manager.ChallengeAccepted = true
                startActivity(intent)
            }
            challenge4 -> {
                manager.ChallengeTime = fifteenMins*8
                manager.ChallengeAccepted = true
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
        manager = Manager.getInstance(requireContext())

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
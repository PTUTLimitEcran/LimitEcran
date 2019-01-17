package com.lpiem.ptut_limit_ecran.limitecran

import java.io.Serializable

interface ChallengeUpdateManager: Serializable {

    fun setNewChallenge(challengeTime: Int)
    fun newChallenge()
}
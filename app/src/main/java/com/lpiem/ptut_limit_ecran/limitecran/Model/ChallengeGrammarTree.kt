package com.lpiem.ptut_limit_ecran.limitecran.Model

enum class ChallengeGrammarTree(val challengeTime: Long) {
    QuarterHour(900000){
        override fun randomTree():String{
            val list = listOf("S[L[LC]C[R]R[R]]",
                "S[L[C[LR]]C[CR]R]",
                "S[L[LC]CR[LC]]")
            return list[(0 until list.size).random()]
        }
    },
    HalfHour(900000*2){
        override fun randomTree():String{
            val list = listOf("[C[L[L[C]C[C]]C[CR]]R[R[C]]]",
                "[L[C[L[LC]C[C]]]C[C[C[C]]R[R[LCR]]]]",
                "S[L[L[C[C[LR]]]C[C[R]]]C[C[C]]R[C[C[L]]R[C[C[LR]]]]",
                "S[L[L[R]]R[L[L[LR]R[C]]R[L]]",
                "S[L[L[C[LC[LC]R]]R[C[C[LCR]R]]]R[CR[C[CR]R]]")
            return list[(0 until list.size).random()]
        }
    },
    OneHour(900000*4){
        override fun randomTree():String{
            val list = listOf("S[L[L[L[C[C[LC]R[C]]]C]C[CR[L]]]C[R]R[R[L[C[L[LC]CR[L[C]R]]]]]",
                "S[L[L[L[L[C[CL]]]C[LC[LC]R]]R[C[C[LCR]R]]]R[CR[C[CR]R]]",
                "S[L[LC[LCR[C[LCR]R[R]]]]C[R[C]]R[R[C[CR]]]]",
                "S[L[L[C[C[LR]]]C[C[R]]]C[C[C]]R[C[C[C[L]]R[C[LR]]]]")
            return list[(0 until list.size).random()]
        }
    },
    TwoHours(900000*8){
        override fun randomTree():String{
            val list = listOf("S[L[C[L[C[L[L[LC]CR[R]]R]]]]R[C[L[L[C[LC[R[LR]]R]]]]R[C[L[C[LCR]]C[C[C[LR]]]R]]]",
                "S[L[L[L[L[C[C[CR]R]]]C[LC[LC]R]]R[C[C[L[LC]C[C]R[CR]]R]]]R[CR[C[C[R]R[R]]R]]")
            return list[(0 until list.size).random()]
        }
    };
    abstract fun randomTree():String
}
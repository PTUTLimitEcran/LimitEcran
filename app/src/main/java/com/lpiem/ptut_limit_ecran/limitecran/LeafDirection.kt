package com.lpiem.ptut_limit_ecran.limitecran

enum class LeafDirection(val direction: Char) {
    LEFT('L'),
    RIGHT('R'),
    SOURCE('S'),
    CENTER('C'),
    NEW_NODE('['),
    END_NODE(']')
}
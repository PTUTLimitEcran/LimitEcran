package com.lpiem.melkius.testprocessing

enum class LeafDirection(val direction: Char) {
    LEFT('L'),
    RIGHT('R'),
    SOURCE('S'),
    CENTER('C'),
    NEW_NODE('['),
    END_NODE(']')
}
package com.chronosoft.sheepwolf.game.model

enum class Side(val turn: Char) {
    WOLF('b'), SHEEP('w'), NONE(' ');

    fun opponent(): Side {
        return when (this) {
            WOLF -> SHEEP
            SHEEP -> WOLF
            NONE -> NONE
        }
    }
}
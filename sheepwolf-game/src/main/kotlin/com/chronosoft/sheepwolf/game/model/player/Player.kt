package com.chronosoft.sheepwolf.game.model.player

import com.chronosoft.sheepwolf.game.model.event.Move
import com.chronosoft.sheepwolf.game.model.Side

interface Player {
    fun id(): String
    fun side(): Side
    fun onOpponentMove(move: Move)
    fun onGameStart()
    fun onGameOver(winner: Side)

}
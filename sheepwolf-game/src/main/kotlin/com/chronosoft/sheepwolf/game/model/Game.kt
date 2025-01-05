package com.chronosoft.sheepwolf.game.model

import com.chronosoft.sheepwolf.game.model.event.Move
import com.chronosoft.sheepwolf.game.model.player.PlayerFactory

interface Game {
    fun id(): String
    fun join(playerFactory: PlayerFactory)
    fun move(playerId: String, move: Move)
    /**
     * @return the side that won the game
     */
    fun finish(playerId: String)
}
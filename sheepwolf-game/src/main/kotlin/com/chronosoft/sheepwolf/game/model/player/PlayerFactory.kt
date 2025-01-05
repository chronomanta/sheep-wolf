package com.chronosoft.sheepwolf.game.model.player

import com.chronosoft.sheepwolf.game.model.Side

@FunctionalInterface
fun interface PlayerFactory {
    fun createPlayer(side: Side): Player
}
package com.chronosoft.sheepwolf.game.model.event

data class Move(
    val from: String,
    val to: String
): GameEvent

package com.chronosoft.sheepwolf.game.model

data class GameRequest(
    val gameType: GameType,
    val metadata: Map<String, String>
)

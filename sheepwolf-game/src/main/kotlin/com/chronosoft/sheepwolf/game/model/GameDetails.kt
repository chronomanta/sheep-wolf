package com.chronosoft.sheepwolf.game.model

import java.time.LocalDateTime

data class GameDetails(
    val gameId: String,
    val gameRequest: GameRequest,
    val createTime: LocalDateTime
)

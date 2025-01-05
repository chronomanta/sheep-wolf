package com.chronosoft.sheepwolf.game.model.event

import com.chronosoft.sheepwolf.game.model.Side

data class GameOverEvent(val winner: Side): GameEvent

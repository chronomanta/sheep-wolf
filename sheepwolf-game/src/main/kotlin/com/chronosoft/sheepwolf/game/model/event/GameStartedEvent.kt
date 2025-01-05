package com.chronosoft.sheepwolf.game.model.event

import com.chronosoft.sheepwolf.game.model.Side

data class GameStartedEvent(val side: Side): GameEvent

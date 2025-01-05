package com.chronosoft.sheepwolf.game.model.player

import com.chronosoft.sheepwolf.game.model.Side
import com.chronosoft.sheepwolf.game.model.event.GameOverEvent
import com.chronosoft.sheepwolf.game.model.event.GameStartedEvent
import com.chronosoft.sheepwolf.game.model.event.Move
import java.util.function.Consumer

data class OnlinePlayer(
    val sessionId: String,
    val side: Side,
    val gameStarted: Consumer<GameStartedEvent>,
    val opponentMoved: Consumer<Move>,
    val gameOver: Consumer<GameOverEvent>
) : Player {

    override fun id() = sessionId

    override fun side() = side

    override fun onGameStart() = gameStarted.accept(GameStartedEvent(side))

    override fun onOpponentMove(move: Move) = opponentMoved.accept(move)

    override fun onGameOver(winner: Side) = gameOver.accept(GameOverEvent(winner))
}

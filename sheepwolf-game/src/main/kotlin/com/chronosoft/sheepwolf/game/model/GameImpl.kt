package com.chronosoft.sheepwolf.game.model

import com.chronosoft.sheepwolf.game.model.event.Move
import com.chronosoft.sheepwolf.game.model.player.Player
import com.chronosoft.sheepwolf.game.model.player.PlayerFactory
import com.chronosoft.sheepwolf.game.run.SheepWolf

data class GameImpl(
    val gameId: String,
    val owner: Player
) : Game {
    private val game: SheepWolf = SheepWolf()
    private var guest: Player? = null
    private var started = false

    override fun id() = gameId

    override fun join(playerFactory: PlayerFactory) {
        guest = playerFactory.createPlayer(owner.side().opponent())
        started = true
        owner.onGameStart()
        guest!!.onGameStart()
    }

    override fun move(playerId: String, move: Move) {
        if (!started) {
            throw IllegalArgumentException("Game not started")
        }
        val player = getPlayer(playerId)
        if (player.side().turn != game.turn()) {
            throw IllegalArgumentException("It's not your turn")
        }
        val opponent = getOpponent(playerId)
        game.applyMove(move)
        opponent.onOpponentMove(move)
        if (game.gameOver()) {
            val winner = if (game.wolfWon()) Side.WOLF else Side.SHEEP
            owner.onGameOver(winner)
            guest!!.onGameOver(winner)
        }
    }

    private fun getPlayer(playerId: String): Player {
        return if (owner.id() == playerId) owner else if (guest?.id() == playerId) guest!! else throw IllegalArgumentException("Not a player")
    }

    private fun getOpponent(playerId: String): Player {
        return if (owner.id() == playerId) guest!! else if (guest?.id() == playerId) owner else throw IllegalArgumentException("Not a player")
    }

    override fun finish(playerId: String) {
        val winner = getPlayer(playerId).side().opponent()
        owner.onGameOver(winner)
        guest?.onGameOver(winner)
    }


}

package com.chronosoft.sheepwolf.game.model

import com.chronosoft.sheepwolf.game.model.event.Move
import com.chronosoft.sheepwolf.game.model.player.Player
import com.chronosoft.sheepwolf.game.model.player.PlayerFactory
import com.chronosoft.sheepwolf.game.run.SheepWolf

data class GameImpl(
    val owner: Player
) : Game {
    private val game: SheepWolf = SheepWolf()
    private var guest: Player? = null

    override fun join(playerFactory: PlayerFactory) {
        guest = playerFactory.createPlayer(owner.side().opponent())
        owner.onGameStart()
        guest!!.onGameStart()
    }

    override fun move(playerId: String, move: Move) {
        val player = if (owner.id() == playerId) owner else guest!!
        if (player.side().turn != game.turn()) {
            throw IllegalArgumentException("It's not your turn")
        }
        val opponent = if (owner.id() == playerId) guest!! else owner
        game.applyMove(move)
        opponent.onOpponentMove(move)
        if (game.gameOver()) {
            val winner = if (game.wolfWon()) Side.WOLF else Side.SHEEP
            owner.onGameOver(winner)
            guest!!.onGameOver(winner)
        }
    }

    override fun finish(): Side {
        TODO("Not yet implemented")
    }


}

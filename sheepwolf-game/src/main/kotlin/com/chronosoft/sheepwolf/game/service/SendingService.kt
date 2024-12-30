package com.chronosoft.sheepwolf.game.service

import com.chronosoft.sheepwolf.game.model.Move
import com.chronosoft.sheepwolf.game.run.SheepWolf
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service


@Service
data class SendingService(
    val simpMessagingTemplate: SimpMessagingTemplate
) {
    var game: SheepWolf = SheepWolf()

    fun sendMoveBack(move: Move, sessionId: String) {
        if (game.gameOver()) {
            game = SheepWolf()
        }
        game.applyMove(move)
        if (game.gameOver()) {
            return
        }

        val randomMove = createRandomMove()
        game.applyMove(randomMove)

        val headerAccessor = SimpMessageHeaderAccessor
            .create(SimpMessageType.MESSAGE)
        headerAccessor.sessionId = sessionId
        headerAccessor.setLeaveMutable(true)

        simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/move", randomMove, headerAccessor.messageHeaders)
    }

    fun createRandomMove(): Move {
        val moves: List<Move> = if (game.turn() == 'b') {
            game.possibleMoves(game.wolf()).flatMap { if (it.from[1] > it.to[1]) listOf(it, it, it) else listOf(it) }
        } else if (game.turn() == 'w') {
            game.sheeps().flatMap { game.possibleMoves(it) }
        } else {
            throw IllegalStateException("Invalid turn")
        }
        return moves.random()
    }
}

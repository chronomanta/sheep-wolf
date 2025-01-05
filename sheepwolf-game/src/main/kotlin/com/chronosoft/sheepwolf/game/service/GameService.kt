package com.chronosoft.sheepwolf.game.service

import com.chronosoft.sheepwolf.game.model.*
import com.chronosoft.sheepwolf.game.model.event.*
import com.chronosoft.sheepwolf.game.model.player.OnlinePlayer
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@Service
data class GameService(
    private val simpMessagingTemplate: SimpMessagingTemplate,
) {
    private val gamesByUser: MutableMap<String, Game> = mutableMapOf()
    private val gamesById: MutableMap<String, Game> = ConcurrentHashMap()

    fun createGame(gameRequest: GameRequest, sessionId: String): GameCreatedEvent {
        if (gameRequest.gameType == GameType.ONLINE) {
            val side = Optional.ofNullable(gameRequest.metadata["side"]).map { Side.valueOf(it) }.orElseGet { randomSide() }
            val owner = OnlinePlayer(
                sessionId = sessionId,
                side = side,
                gameStarted = { answerWithGameStarted(sessionId, it) },
                opponentMoved = { answerWithMove(sessionId, it) },
                gameOver = { answerWithGameOver(sessionId, it) }
            )
            val game: Game = GameImpl(owner)
            val gameId: String = UUID.randomUUID().toString()
            gamesByUser[sessionId] = game
            gamesById[gameId] = game
            return GameCreatedEvent(gameId)
        }
        throw IllegalArgumentException("Unsupported game type")
    }

    fun joinGame(joinGameRequest: JoinGameRequest, sessionId: String) {
        val game = gamesById.remove(joinGameRequest.gameId)?: throw IllegalArgumentException("Game not found")
        game.join { side ->
            OnlinePlayer(
                sessionId = sessionId,
                side = side,
                gameStarted = { answerWithGameStarted(sessionId, it) },
                opponentMoved = { answerWithMove(sessionId, it) },
                gameOver = { answerWithGameOver(sessionId, it) }
            )
        }
        gamesByUser[sessionId] = game
    }

    fun move(move: Move, sessionId: String) {
        val game = gamesByUser[sessionId]?: throw IllegalArgumentException("Game not found")
        game.move(sessionId, move)
    }

    private fun answerWithMove(sessionId: String, move: Move) = answerWithEvent(sessionId, move, "/queue/move")

    private fun answerWithGameStarted(sessionId: String, gameStartedEvent: GameStartedEvent) = answerWithEvent(sessionId, gameStartedEvent, "/queue/game-started")

    private fun answerWithGameOver(sessionId: String, gameOverEvent: GameOverEvent) = answerWithEvent(sessionId, gameOverEvent, "/queue/game-over")

    private fun answerWithEvent(sessionId: String, event: GameEvent, destination: String) {
        val headerAccessor = SimpMessageHeaderAccessor
            .create(SimpMessageType.MESSAGE)
        headerAccessor.sessionId = sessionId
        headerAccessor.setLeaveMutable(true)

        simpMessagingTemplate.convertAndSendToUser(sessionId, destination, event, headerAccessor.messageHeaders)

    }

    private fun randomSide(): Side {
        return if (Random().nextBoolean()) Side.WOLF else Side.SHEEP
    }
}

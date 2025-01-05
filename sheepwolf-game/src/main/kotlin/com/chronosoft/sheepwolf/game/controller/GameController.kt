package com.chronosoft.sheepwolf.game.controller

import com.chronosoft.sheepwolf.game.model.GameRequest
import com.chronosoft.sheepwolf.game.model.JoinGameRequest
import com.chronosoft.sheepwolf.game.model.event.Move
import com.chronosoft.sheepwolf.game.service.GameService
import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionDisconnectEvent



@Controller
data class GameController(
    val gameService: GameService
) {

    @EventListener
    fun onDisconnectEvent(event: SessionDisconnectEvent) {
        gameService.finishGame(event.sessionId)
    }

    @MessageMapping("/create")
    fun create(@Header("simpSessionId")sessionId: String, gameRequest: GameRequest) {
        gameService.createGame(gameRequest, sessionId)
    }

    @MessageMapping("/join")
    fun join(@Header("simpSessionId")sessionId: String, joinGameRequest: JoinGameRequest) {
        gameService.joinGame(joinGameRequest, sessionId)
    }

    @MessageMapping("/move")
    fun move(@Header("simpSessionId")sessionId: String, move: Move) {
        gameService.move(move, sessionId)
    }
}
package com.chronosoft.sheepwolf.game.controller

import com.chronosoft.sheepwolf.game.model.Move
import com.chronosoft.sheepwolf.game.service.SendingService
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Controller
data class GreetingController(
    val sendingService: SendingService
) {

    @MessageMapping("/move")
    fun greeting(@Header("simpSessionId")sessionId: String, move: Move) {
        CompletableFuture.runAsync({ sendingService.sendMoveBack(move, sessionId) }, CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS))
    }
}
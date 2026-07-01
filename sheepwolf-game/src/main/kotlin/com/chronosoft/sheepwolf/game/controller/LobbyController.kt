package com.chronosoft.sheepwolf.game.controller

import com.chronosoft.sheepwolf.game.model.GamesOverview
import com.chronosoft.sheepwolf.game.service.OverviewService
import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Controller
@MessageMapping("/lobby")
data class LobbyController(
    val overviewService: OverviewService
) {
    @EventListener
    fun onDisconnectEvent(event: SessionDisconnectEvent) = overviewService.playerLeaves(event.sessionId)

    @MessageMapping("/overview")
    @SendToUser("/queue/overview")
    fun overview(@Header("simpSessionId")sessionId: String): GamesOverview {
        overviewService.playerEnters(sessionId)
        return overviewService.gamesOverview()
    }
}
